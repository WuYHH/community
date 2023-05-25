package com.dlut.community.event;

import com.alibaba.fastjson.JSONObject;
import com.dlut.community.entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author wuyuhan
 * @date 2023/5/25 09:58
 */
@Component
public class EventProducer {

    @Autowired
    private KafkaTemplate kafkaTemplate;
    // 处理事件
    public void sendMessage(Event event) {
        // 将事件发布到指定主题，序列化为json格式发送
        kafkaTemplate.send(event.getTopic(), JSONObject.toJSONString(event));
    }
}
