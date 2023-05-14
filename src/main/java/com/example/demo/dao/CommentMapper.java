package com.example.demo.dao;

import com.example.demo.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author wuyuhan
 * @date 2023/5/11 15:32
 */
@Mapper
public interface CommentMapper {

    List<Comment> selectComment(int entityType, int entityId, int offset, int limit);

    /**
     * 获取评论数量
     * @param entityType 评论类型：评论帖子 or 回复评论
     * @param entityId 被评论的id（帖子id or 评论id）
     * @return
     */
    int selectCountByEntity(int entityType, int entityId);

    int insertComment(Comment comment);

}
