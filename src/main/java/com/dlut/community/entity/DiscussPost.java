package com.dlut.community.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
/**
 * @author wuyuhan
 * @date 2023/4/24 15:28
 */
public class DiscussPost implements Serializable {
    private int id;
    private int userId;
    private String title;
    private String content;
    private int type;
    private int status;
    private Date createTime;
    // 冗余字段
    private int commentCount;
    private double score;
}
