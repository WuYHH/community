package com.example.demo.util;

/**
 * @author wuyuhan
 * @date 2023/5/2 14:19
 */
public interface CommunityContant {

    int ACTIVATION_SUCCESS = 0;

    int ACTIVATION_REPEAT = 1;

    int ACTIVATION_FAILURE = 2;

    int REMEBER_EXPIRED_SECONDS = 1000 * 60 * 3600;

    int DEFAULT_EXPIRED_SECONDS = 1000 * 60 * 60;

    int ENTITY_TYPE_POST = 1;
    int ENTITY_TYPE_REPALY = 2;

}
