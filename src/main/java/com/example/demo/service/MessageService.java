package com.example.demo.service;

import com.example.demo.dao.MessageMapper;
import com.example.demo.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wuyuhan
 * @date 2023/5/15 15:03
 */
@Service
public class MessageService {

    @Autowired
    private MessageMapper messageMapper;

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
}
