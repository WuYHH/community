package com.dlut.community.service;

import com.dlut.community.util.SensitiveFilter;
import com.dlut.community.dao.MessageMapper;
import com.dlut.community.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @author wuyuhan
 * @date 2023/5/15 15:03
 */
@Service
public class MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    public List<Message> findConversations(int currentUserId, int offset, int limit) {
        return messageMapper.findConversations(currentUserId, offset, limit);
    }

    public int findConversationRows(int currentUserId) {
        return messageMapper.findConversationsRows(currentUserId);
    }

    public List<Message> findMessages(String conversationId, int offset, int limit) {
        return messageMapper.findMessages(conversationId, offset, limit);
    }

    public int findMessagesRows(String conversationId) {
        return messageMapper.findMessagesRows(conversationId);
    }

    public int findUnreadCount(int currentUserId, String conversationId) {
        return messageMapper.findMessageUnreadCount(currentUserId, conversationId);
    }


    public int addMessage(Message message) {
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        message.setContent(sensitiveFilter.filter(message.getContent()));
        return messageMapper.insertMessage(message);
    }

    public int updateStatus(List<Integer> ids, int status) {
        return messageMapper.updateStatus(ids, status);
    }
}
