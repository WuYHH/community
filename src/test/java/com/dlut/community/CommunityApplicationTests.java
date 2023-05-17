package com.dlut.community;

import com.dlut.community.dao.FirstDao;
import com.dlut.community.service.FirstService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;


@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
class CommunityApplicationTests implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Test
    void testApplicationContext() {
        System.out.println(applicationContext);
        // 主动获取bean,通过名称进行指定
        FirstDao firstDao = applicationContext.getBean("mybatisImpl",FirstDao.class);
        System.out.println(firstDao.select());
    }

    @Test
    void testBeanManagement() {
        FirstService bean = applicationContext.getBean(FirstService.class);
        FirstService bean1 = applicationContext.getBean(FirstService.class);
        // 当Scope范围为prototype时，为false；默认为单例模式
        System.out.println(bean.equals(bean1));
    }

    @Test
    void testBeanConfig() {
        SimpleDateFormat bean = applicationContext.getBean(SimpleDateFormat.class);
        System.out.println(bean.format(new Date()));
    }

    // 测试依赖注入
    @Autowired
    @Qualifier("mybatisImpl")
    private FirstDao firstDao;

    @Autowired
    private FirstService firstService;

    @Autowired
    private SimpleDateFormat simpleDateFormat;

    @Test
    void testDI() {
        System.out.println(firstDao);
        System.out.println(firstService);
        System.out.println(simpleDateFormat);
    }
}
