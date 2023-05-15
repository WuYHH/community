package com.example.demo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
/**
 * @author wuyuhan
 * @date 2023/5/11 15:26
 */
public class Comment implements Serializable {

    private int id;
    private int userId;
    private int entityType;
    private int entityId;
    private int targetId;
    private String content;
    private int status;
    private Date createTime;
}
