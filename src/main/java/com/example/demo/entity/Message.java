package com.example.demo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wuyuhan
 * @date 2023/5/15 09:31
 */
@Data
public class Message implements Serializable {

    private int id;
    private int fromId;
    private int toId;
    private String conversationId;
    private String content;
    private int status;
    private Date createTime;

}
