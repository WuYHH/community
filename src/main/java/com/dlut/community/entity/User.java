package com.dlut.community.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
/**
 * @author wuyuhan
 * @date 2023/4/22 22:17
 */
public class User implements Serializable {
    private int id;
    private String username;
    private String password;
    private String salt;
    private String email;
    private int type;
    private int status;
    // 将数据库字段的下划线命名改为驼峰命名
    private String activationCode;
    private String headerUrl;
    private Date createTime;
}
