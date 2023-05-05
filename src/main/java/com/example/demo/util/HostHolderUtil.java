package com.example.demo.util;

import com.example.demo.entity.User;
import org.springframework.stereotype.Component;

/**
 * @author wuyuhan
 * @date 2023/5/5 21:48
 */
@Component
public class HostHolderUtil {
    private ThreadLocal<User> userThreadLocal = new ThreadLocal<>();

    public void setUser(User user) {
        userThreadLocal.set(user);
    }

    public User getUser() {
        return userThreadLocal.get();
    }

    public void clear() {
        userThreadLocal.remove();
    }
}
