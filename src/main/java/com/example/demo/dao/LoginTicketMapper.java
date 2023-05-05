package com.example.demo.dao;

import com.example.demo.entity.LoginTicket;
import org.apache.ibatis.annotations.*;


/**
 * @author wuyuhan
 * @date 2023/5/5 14:42
 */
@Mapper
public interface LoginTicketMapper {

    /**
     * 新增一条凭证
     */
    @Insert({
            "insert into login_ticket(user_id, ticket, status, expired) ",
            "values(#{userId}, #{ticket}, #{status}, #{expired})"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertTicket(LoginTicket loginTicket);

    /**
     * 查询一条凭证
     */
    @Select({
            "select id, user_id, ticket, status, expired ",
            "from login_ticket ",
            "where ticket = #{ticket}"
    })
    LoginTicket selectTicket(String ticket);

    /**
     * 更新一条凭证
     */
    @Update({"<script> ",
            "update login_ticket set status = #{status} where ticket = #{ticket} ",
            "<if test = \" ticket != null \"> ",
            "and 1 = 1 ",
            "</if> ",
            "</script> "
    })
    int updateStatus(String ticket, int status);
}
