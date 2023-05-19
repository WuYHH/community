package com.dlut.community.service;

import com.dlut.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

/**
 * @author wuyuhan
 * @date 2023/5/17 16:08
 */
@Service
public class LikeService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 点赞
     * @param entityType 被点赞实体
     * @param entityId 被点赞实体id
     * @param userId 点赞的用户
     * @param entityUserId 被点赞的用户
     */
    public void like(int entityType, int entityId, int userId, int entityUserId) {
//        String likePrefix = RedisKeyUtil.getEntityLike(entityType, entityId);
//        // 判断是否点过赞，如果点过赞，则取消点赞
//        Boolean isLiked = redisTemplate.opsForSet().isMember(likePrefix, userId);
//        if (isLiked) {
//            redisTemplate.opsForSet().remove(likePrefix, userId);
//        } else {
//            // 没点过就增加userId
//            redisTemplate.opsForSet().add(likePrefix, userId);
//        }
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String entityLike = RedisKeyUtil.getEntityLike(entityType, entityId);
                String entityUserLike = RedisKeyUtil.getUserLike(entityUserId);

                // 当前用户user是否点赞过entityUser的任何实体<entityType, entityId>
                Boolean isLiked = operations.opsForSet().isMember(entityLike, userId);

                // 开启事务，查询点赞状态要在事务开始前，在事务里无法查找到任何值
                operations.multi();
                if (isLiked) {
                    operations.opsForSet().remove(entityLike, userId);
                    operations.opsForValue().decrement(entityUserLike);
                } else {
                    operations.opsForSet().add(entityLike, userId);
                    operations.opsForValue().increment(entityUserLike);
                }
                // 执行事务
                return operations.exec();
            }
        });
    }

    /**
     * 获取某个实体被点赞的数量
     * @param entityType
     * @param entityId
     * @return
     */
    public long getLikeCount(int entityType, int entityId) {
        String likePrefix = RedisKeyUtil.getEntityLike(entityType, entityId);
        return redisTemplate.opsForSet().size(likePrefix);
    }


    /**
     * 查询某人对某实体的点赞状态，1：点赞；0：未点赞
     * @param entityType
     * @param entityId
     * @param userId
     * @return
     */
    public int getLikeStatus(int entityType, int entityId, int userId) {
        String likePrefix = RedisKeyUtil.getEntityLike(entityType, entityId);
        return redisTemplate.opsForSet().isMember(likePrefix, userId) ? 1 : 0;
    }


    /**
     * 查询用户的点赞数量
     * @param userId
     * @return
     */
    public int findUserLikeCount(int userId) {
        String userLike = RedisKeyUtil.getUserLike(userId);
        Integer count = (Integer) redisTemplate.opsForValue().get(userLike);
        return count == null ? 0 : count.intValue();
    }
}
