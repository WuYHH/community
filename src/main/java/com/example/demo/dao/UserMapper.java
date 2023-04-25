package com.example.demo.dao;

import com.example.demo.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author wuyuhan
 * @date 2023/4/22 22:21
 */
@Mapper
public interface UserMapper {
    User selectById(int id);
    User selectByName(String username);
    User selectByEmail(String email);

    int insertUser(User user);

    int updateStatus(int id, int status);
    int updateHeader(int id, String headerUrl);
    int updatePassword(int id, String password);
}
