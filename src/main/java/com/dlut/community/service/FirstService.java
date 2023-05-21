package com.dlut.community.service;

import com.dlut.community.dao.FirstDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author wuyuhan
 * @date 2023/4/20 16:31
 */
@Service
// 设置实例范围
//@Scope("prototype")
public class FirstService {
    @Autowired
    @Qualifier("mybatisImpl")
    private FirstDao firstDao;

    public String find() {
        return firstDao.select();
    }
    public FirstService() {
        System.out.println("实例化firstService");
    }

    @PostConstruct
    public void init() {
        System.out.println("初始化firstService");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("准备销毁firstService");
    }

}
