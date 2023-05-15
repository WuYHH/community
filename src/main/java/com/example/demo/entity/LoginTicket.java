package com.example.demo.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
/**
 * @author wuyuhan
 * @date 2023/5/5 14:40
 */
public class LoginTicket implements Serializable {

    private int id;
    private int userId;
    private String ticket;
    private int status;
    private Date expired;

}
