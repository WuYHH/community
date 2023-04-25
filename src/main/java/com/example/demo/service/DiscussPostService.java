package com.example.demo.service;

import com.example.demo.dao.DiscussPostMapper;
import com.example.demo.entity.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wuyuhan
 * @date 2023/4/24 15:59
 */
@Service
public class DiscussPostService {

    @Autowired
    private DiscussPostMapper discussPostMapper;

    public List<DiscussPost> findDiscussPosts(int userId, int offset, int limit){
        return discussPostMapper.selectDiscussPosts(userId, offset, limit);
    }

    public int findDiscussPostRows(int userId) {
        return discussPostMapper.selectDiscussPostRows(userId);
    }
}
