package com.example.demo.service;

import com.example.demo.dao.CommentMapper;
import com.example.demo.entity.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wuyuhan
 * @date 2023/5/11 15:43
 */
@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    public List<Comment> selectComment(int entityType, int entityId, int offset, int limit) {
        return commentMapper.selectComment(entityType, entityId, offset, limit);
    }

    public int selectCountByEntity(int entityType, int entityId) {
        return commentMapper.selectCountByEntity(entityType, entityId);
    }
}
