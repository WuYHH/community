package com.dlut.community.event;

import com.alibaba.fastjson.JSONObject;
import com.dlut.community.entity.Event;
import com.dlut.community.entity.Message;
import com.dlut.community.service.MessageService;
import com.dlut.community.util.CommunityContant;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wuyuhan
 * @date 2023/5/25 10:03
 */
@Component
public class EventConsumer implements CommunityContant {

    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    @Autowired
    private MessageService messageService;

    @KafkaListener(topics = {KAFKA_TOPIC_LIKE, KAFKA_TOPIC_POST, KAFKA_TOPIC_FOLLOW})
    public void handleMessage(ConsumerRecord record) {
        if (record == null || record.value() == null) {
            logger.error("消息内容为空");
            return;
        }
        // 获取事件
        Event event = JSONObject.parseObject(record.value().toString(), Event.class);

        // 事件-->消息，构建消息，插入消息表
        Message message = new Message();
        message.setFromId(SYSTEM_USER_ID);
        message.setToId(event.getEntityUserId());
        message.setConversationId(event.getTopic());
        message.setCreateTime(new Date());
        // 构建内容数据，用户xxx评论（点赞）了你的帖子
        // 这里不再需要 "你" 的信息，因为message中已经包含了
        Map<String, Object> content = new HashMap<>();
        content.put("userId", event.getUserId());
        content.put("entityType", event.getEntityType());
        content.put("entityId", event.getEntityId());

        // 增加额外数据
        if (!event.getData().isEmpty()) {
            for (Map.Entry<String, Object> entry : event.getData().entrySet()) {
                content.put(entry.getKey(), entry.getValue());
            }
        }

        message.setContent(JSONObject.toJSONString(content));
        messageService.addMessage(message);
    }

}
