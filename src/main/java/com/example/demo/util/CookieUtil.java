package com.example.demo.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author wuyuhan
 * @date 2023/5/5 21:34
 */
public class CookieUtil {

    public static String getValue(HttpServletRequest request, String cookieName) {
        if (request != null && cookieName != null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie: cookies) {
                    if (cookie.getName().equals(cookieName)) {
                        return cookie.getValue();
                    }
                }
            }
        } else {
            throw new IllegalArgumentException("参数为空！");
        }
        return null;
    }
}
