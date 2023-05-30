package com.dlut.community.dao;

import com.dlut.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author wuyuhan
 * @date 2023/5/15 09:47
 */
@Mapper
public interface MessageMapper {

    /**
     * 查找当前用户的所有会话
     * @param currentUserId
     * @param offset 偏移量
     * @param limit 每一页显示的数量
     * @return
     */
    List<Message> findConversations(int currentUserId, int offset, int limit);

    /**
     * 返回会话数量
     * @return
     */
    int findConversationsRows(int currentUserId);


    List<Message> findMessages(String conversationId, int offset, int limit);

    /**
     * 返回会话中的私信数量
     * @return
     */
    int findMessagesRows(String conversationId);

    /**
     * 查询未读消息的数量
     * 如果conversationId为null，则查询所有会话的所有消息；
     * 如果不为null，则查询指定对话的消息
     * @return
     */
    int findMessageUnreadCount(int currentUserId, String conversationId);

    int insertMessage(Message message);

    /**
     * 更新私信状态
     * @param ids
     * @param status
     * @return
     */
    int updateStatus(List<Integer> ids, int status);

    // 查询某个主题下的最新的通知
    Message findLatestNotice(String topic, int userId);
    // 查询某个主题所包含的通知数
    int findNoticeCount(String topic, int userId);
    // 查询未读的通知数量
    int unreadNoticeCount(String topic, int userId);

    // 查询某个主题所包含的通知列表
    List<Message> selectNotice(String topic, int userId, int offset, int limit);
}
