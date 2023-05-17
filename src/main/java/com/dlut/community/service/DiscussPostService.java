package com.dlut.community.service;

import com.dlut.community.util.SensitiveFilter;
import com.dlut.community.dao.DiscussPostMapper;
import com.dlut.community.entity.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @author wuyuhan
 * @date 2023/4/24 15:59
 */
@Service
public class DiscussPostService {

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;


    public List<DiscussPost> findDiscussPosts(int userId, int offset, int limit){
        return discussPostMapper.selectDiscussPosts(userId, offset, limit);
    }

    public int findDiscussPostRows(int userId) {
        return discussPostMapper.selectDiscussPostRows(userId);
    }

    public int insertDiscussPost(DiscussPost discussPost) {
        if (discussPost == null) {
            throw new IllegalArgumentException("内容不能为空！");
        }
        // 标题和内容进行敏感词过滤
        String title = discussPost.getTitle();
        String content = discussPost.getContent();
        discussPost.setTitle(HtmlUtils.htmlEscape(title));
        discussPost.setContent(HtmlUtils.htmlEscape(content));

        discussPost.setTitle(sensitiveFilter.filter(discussPost.getTitle()));
        discussPost.setContent(sensitiveFilter.filter(discussPost.getContent()));

        return discussPostMapper.insertDiscussPost(discussPost);
    }

    public DiscussPost selectDiscussPost(int id) {

        return discussPostMapper.selectDiscussPost(id);
    }

    public int updateCommentCount(int id, int commentCount) {
        return discussPostMapper.updateCommentCount(id, commentCount);
    }
}
