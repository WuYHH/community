package com.dlut.community.service;

import com.dlut.community.dao.CommentMapper;
import com.dlut.community.entity.Comment;
import com.dlut.community.dao.DiscussPostMapper;
import com.dlut.community.util.CommunityContant;
import com.dlut.community.util.CurrentUserUtil;
import com.dlut.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @author wuyuhan
 * @date 2023/5/11 15:43
 */
@Service
public class CommentService implements CommunityContant {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Autowired
    private CurrentUserUtil currentUserUtil;

    public List<Comment> selectComment(int entityType, int entityId, int offset, int limit) {
        return commentMapper.selectComment(entityType, entityId, offset, limit);
    }

    public int selectCountByEntity(int entityType, int entityId) {
        return commentMapper.selectCountByEntity(entityType, entityId);
    }

    /**
     * 涉及到了添加数据和更新数据必须同时成功，所以使用声明式的事务
     * @param comment
     * @return
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int insertComment(Comment comment) {
        // 对title和content过滤敏感词
        comment.setContent(sensitiveFilter.filter(comment.getContent()));
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        // 绑定是谁评论的，需要获取当前登录用户
        comment.setUserId(currentUserUtil.getUser().getId());
        int rows = commentMapper.insertComment(comment);

        // 更新评论数量:注意，是帖子的评论
        if (comment.getEntityType() == ENTITY_TYPE_POST) {
            int count = commentMapper.selectCountByEntity(comment.getEntityType(), comment.getEntityId());
            // 评论的实体id即为帖子的id
            discussPostMapper.updateCommentCount(comment.getEntityId(), count);
        }
        return rows;
    }

    public Comment selectCommentById(int entityId) {
        return commentMapper.selectCommentById(entityId);
    }
}
