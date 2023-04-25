package com.example.demo.dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * @author wuyuhan
 * @date 2023/4/20 16:26
 */

@Repository("HibernateImpl")
public class FirstDaoHibernateImpl implements FirstDao {
    @Override
    public String select() {
        return "hello, Hibernate!";
    }
}
