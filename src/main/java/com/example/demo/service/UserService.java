package com.example.demo.service;

import com.example.demo.dao.UserMapper;
import com.example.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wuyuhan
 * @date 2023/4/24 16:08
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User findById(int id) {
        return userMapper.selectById(id);
    }
}
