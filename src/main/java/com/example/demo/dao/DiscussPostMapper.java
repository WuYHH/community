package com.example.demo.dao;

import com.example.demo.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author wuyuhan
 * @date 2023/4/24 15:31
 */
@Mapper
public interface DiscussPostMapper {
    /**
     * 首页展示全部帖子和展示用户帖子，区分靠<if></if>标签
     * @return
     */
    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    /**
     * 获取所有记录的行数，当只有一个参数时，并且使用<if></if>标签，必须用@Param
     * @param userId
     * @return
     */
    int selectDiscussPostRows(@Param("userId") int userId);

    int insertDiscussPost(DiscussPost discussPost);
}
