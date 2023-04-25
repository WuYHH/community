package com.example.demo.dao;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

/**
 * @author wuyuhan
 * @date 2023/4/20 16:24
 */
@Repository("mybatisImpl")
//@Primary
public class FirstDaoMybatisImpl implements FirstDao {
    @Override
    public String select() {
        return "hello1, mybatis!";
    }
}
