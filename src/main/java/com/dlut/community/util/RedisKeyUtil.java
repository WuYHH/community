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

    private static final String PREFIX_FOLLOWEE = "followee";

    private static final String PREFIX_FOLLOWER = "follower";

    private static final String PREFIX_KAPTCHA = "kaptcha";

    private static final String PREFIX_TICKET = "ticket";

    private static final String PREFIX_USER = "user";


    // 对实体点赞(这个其实也可以用zset)
    // like:entity: entityType:entityId  --> set(userId),
    public static String getEntityLike(int entityType, int entityId) {
        return ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

    // 对用户点赞
    // like:user:userId --> int
    public static String getUserLike(int userId) {
        return USER_LIKE + SPLIT + userId;
    }

    /**
     * 某个用户关注的实体
     * key: 用户，value：实体
     * followee:userId:entityType --> zset(entityId, nowDate)
     * @param userId
     * @param entityType
     * @return
     */
    public static String getFolloweeKey(int userId, int entityType) {
        return PREFIX_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
    }

    /**
     * 某个实体类型拥有的粉丝(人)
     * follower:entityType:entityId --> zset(userId, nowDate)
     * @param entityType 实体类型
     * @param entityId 实体Id
     * @return
     */
    public static String getFollowerKey(int entityType, int entityId) {
        return PREFIX_FOLLOWER + SPLIT + entityType + SPLIT + entityId;
    }

    public static String getKaptch(String code) {
        return PREFIX_KAPTCHA + SPLIT + code;
    }

    /**
     * 登录凭证
     * @param ticket
     * @return
     */
    public static String getTicket(String ticket) {
        return PREFIX_KAPTCHA + SPLIT + ticket;
    }


    public static String getUserKey(int userId) {
        return PREFIX_USER + SPLIT + userId;
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
