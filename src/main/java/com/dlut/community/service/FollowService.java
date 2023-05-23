package com.dlut.community.service;

import com.dlut.community.entity.User;
import com.dlut.community.util.CommunityContant;
import com.dlut.community.util.CurrentUserUtil;
import com.dlut.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author wuyuhan
 * @date 2023/5/21 14:19
 */
@Service
public class FollowService implements CommunityContant {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private CurrentUserUtil currentUserUtil;

    /**
     * 开启事务：用户关注某个人的同时，用户的关注列表更新，被关注列表的粉丝更新
     * @param userId
     * @param entityType
     * @param entityId
     */
    public void follow(int userId, int entityType, int entityId) {
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                // 同时更新
                String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
                String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);

                operations.multi();
                redisTemplate.opsForZSet().add(followeeKey, entityId, System.currentTimeMillis());
                redisTemplate.opsForZSet().add(followerKey, userId, System.currentTimeMillis());
                return operations.exec();
            }
        });
    }
    public void unfollow(int userId, int entityType, int entityId) {
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                // 同时更新
                String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
                String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);

                operations.multi();
                redisTemplate.opsForZSet().remove(followeeKey, entityId);
                redisTemplate.opsForZSet().remove(followerKey, userId);
                return operations.exec();
            }
        });
    }

    // 查询关注的实体的数量
    public long getFolloweeCount(int userId, int entityType) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return redisTemplate.opsForZSet().zCard(followeeKey);

    }

    // 查询关注的粉丝数量
    public long getFollowerCount(int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return redisTemplate.opsForZSet().zCard(followerKey);
    }

    // 查询当前用户是否已经关注该实体
    public boolean hasFollowed(int userId, int entityType, int entityId) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return redisTemplate.opsForZSet().score(followeeKey, entityId) != null;
    }

    // 查询某个用户关注的人，支持分页
    public List<Map<String, Object>> getFolloweeList(int userId, int offset, int limit) {
        // entityType类型为人
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, ENTITY_TYPE_USER);
        // 取数据时，按照时间倒序输出
        Set<Integer> targetIds = redisTemplate.opsForZSet().reverseRange(followeeKey, offset, offset + limit - 1);
        List<Map<String, Object>> res = new ArrayList<>();
        if (targetIds != null && !targetIds.isEmpty()) {
            for (Integer id : targetIds) {
                Map<String, Object> map = new HashMap<>();
                User targetUser = userService.findUserById(id);
                map.put("targetUser", targetUser);
                Double date = redisTemplate.opsForZSet().score(followeeKey, id);
                map.put("followDate", new Date(date.longValue()));
                // 查询当前登录用户对该用户关注用户的关注状态
                map.put("followStatus", hasFollowed(currentUserUtil.getUser().getId(), ENTITY_TYPE_USER, id));
                res.add(map);
            }
        }
        return res;
    }

    // 查询某个用户的粉丝，支持分页
    public List<Map<String, Object>> getFollowerList(int userId, int offset, int limit) {
        // entityType类型为人
        String followerKey = RedisKeyUtil.getFollowerKey(ENTITY_TYPE_USER, userId);
        // 取数据时，按照时间倒序输出
        Set<Integer> targetIds = redisTemplate.opsForZSet().reverseRange(followerKey, offset, offset + limit - 1);
        List<Map<String, Object>> res = new ArrayList<>();
        if (targetIds != null && !targetIds.isEmpty()) {
            for (Integer id : targetIds) {
                Map<String, Object> map = new HashMap<>();
                User targetUser = userService.findUserById(id);
                map.put("targetUser", targetUser);
                Double date = redisTemplate.opsForZSet().score(followerKey, id);
                map.put("followDate", new Date(date.longValue()));
                // 查询当前登录用户对该用户关注用户的关注状态
                map.put("followStatus", redisTemplate.opsForZSet().score(followerKey, id) != null);
                res.add(map);
            }
        }
        return res;
    }

}
