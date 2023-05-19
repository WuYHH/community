package com.dlut.community.util;

/**
 * @author wuyuhan
 * @date 2023/5/17 16:09
 */
public class RedisKeyUtil {

    private static final String SPLIT = ":";
    // 对实体点赞，包括帖子和评论；
    private static final String ENTITY_LIKE = "like:entity";
    // 用户
    private static final String USER_LIKE = "like:user";

    // 对实体点赞
    // like:entity: entityType:entityId  --> set(userId),
    public static String getEntityLike(int entityType, int entityId) {
        return ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    // 对用户点赞
    // like:user:userId --> int
    public static String getUserLike(int userId) {
        return USER_LIKE + SPLIT + userId;
    }

//    // 对帖子点赞, entity_type = 1
//    public String getPostLikeKey() {
//        return
//    }
//
//    // 对评论点赞, entity_type = 2
//    public String getCommentLikeKey() {
//
//    }
}
