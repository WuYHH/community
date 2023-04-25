package com.example.demo.entity;

import java.io.Serializable;
import java.util.Date;

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


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", email='" + email + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", activationCode='" + activationCode + '\'' +
                ", headerUrl='" + headerUrl + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public String getHeaderUrl() {
        return headerUrl;
    }

    public void setHeaderUrl(String headerUrl) {
        this.headerUrl = headerUrl;
    }
}
