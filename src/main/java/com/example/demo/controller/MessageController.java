package com.example.demo.controller;

import com.example.demo.entity.Message;
import com.example.demo.entity.Page;
import com.example.demo.entity.User;
import com.example.demo.service.MessageService;
import com.example.demo.service.UserService;
import com.example.demo.util.CommunityUtil;
import com.example.demo.util.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * @author wuyuhan
 * @date 2023/5/15 15:10
 */
@Controller
@RequestMapping("/letter")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private CurrentUserUtil currentUser;

    @Autowired
    private UserService userService;

    @RequestMapping(path = "/send", method = RequestMethod.POST)
    @ResponseBody
    public String sendMessage(String toName, String content) {
        User targetUser = userService.findUserByName(toName);
        if (targetUser == null) {
            return CommunityUtil.getJsonString(1, "目标用户不存在");
        }
        Message message = new Message();
        message.setContent(content);
        message.setCreateTime(new Date());
        message.setStatus(0);
        int fromUserId = currentUser.getUser().getId();
        message.setFromId(fromUserId);
        int targetId = targetUser.getId();
        message.setToId(targetId);
        String conversationId = fromUserId < targetId ? fromUserId + "_" + targetId : targetId + "_" + fromUserId;
        message.setConversationId(conversationId);

        messageService.addMessage(message);

        return CommunityUtil.getJsonString(0, "发送成功！");
    }


    @RequestMapping(path = "/detail/{conversationId}", method = RequestMethod.GET)
    public String getMessage(Model model, Page page, @PathVariable("conversationId") String conversationId) {
        // 设置消息分页
        page.setLimit(5);
        page.setPath("/letter/detail/" + conversationId);
        page.setRows(messageService.findMessagesRows(conversationId));

        // 获取消息列表
        List<Message> messages = messageService.findMessages(conversationId, page.getOffset(), page.getLimit());
        List<Map<String, Object>> messageList = new ArrayList<>();
        List<Integer> unreadList = new ArrayList<>();
        if (messageList != null) {
            for (Message message : messages) {
                if (message.getStatus() == 0 && currentUser.getUser().getId() == message.getToId()) {
                    unreadList.add(message.getId());
                }
                Map<String, Object> map = new HashMap<>();
                map.put("message", message);
                map.put("fromUser", userService.findUserById(message.getFromId()));
                messageList.add(map);
            }
        }
        model.addAttribute("messages", messageList);

        // 在消息外面包装私信目标，处理"来自xxx的私信"
        model.addAttribute("target", getMessageTarget(conversationId));

        // 修改未读消息的状态
        if (unreadList != null && unreadList.size() != 0) {
            messageService.updateStatus(unreadList,1);
        }

        return "/site/letter-detail";
    }

    private User getMessageTarget(String conversationId) {
        String[] ids = conversationId.split("_");
        int id0 = Integer.parseInt(ids[0]);
        int id1 = Integer.parseInt(ids[1]);
        if (currentUser.getUser().getId() == id0) {
            // 目标用户
            return userService.findUserById(id1);
        } else {
            return userService.findUserById(id0);
        }
    }

    // 私信列表
    @RequestMapping(path = "/list", method = RequestMethod.GET)
    public String getConversation(Model model, Page page) {
        // 获取当前登录用户
        int userId = currentUser.getUser().getId();
        // 设置分页信息
        page.setRows(messageService.findConversationRows(userId));
        page.setPath("/letter/list");
        page.setLimit(5);

        // 会话列表
        List<Message> conversations = messageService.findConversations(userId, page.getOffset(), page.getLimit());
        List<Map<String, Object>> conversationsList = new ArrayList<>();
        if (conversations != null) {
            for (Message conversation : conversations) {
                Map<String, Object> map = new HashMap<>();
                map.put("conversation", conversation);
                map.put("unreadCount", messageService.findUnreadCount(userId, conversation.getConversationId()));
                map.put("messageCount", messageService.findMessagesRows(conversation.getConversationId()));
                // 找到当前用户currentUser正在和谁会话
                int targetId =conversation.getFromId() == userId? conversation.getToId() : conversation.getFromId();
                // 将对话用户注入
                User targetUser = userService.findUserById(targetId);
                map.put("target", targetUser);
                conversationsList.add(map);
            }
        }
        model.addAttribute("conversations", conversationsList);
        // 查询未读消息数量
        int totalUnreadCount = messageService.findUnreadCount(userId, null);
        model.addAttribute("totalUnreadCount", totalUnreadCount);


        return "/site/letter";
    }
}
