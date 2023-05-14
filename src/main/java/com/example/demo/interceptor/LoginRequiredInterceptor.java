package com.example.demo.interceptor;

import com.example.demo.annotation.LoginRequired;
import com.example.demo.util.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author wuyuhan
 * @date 2023/5/6 16:50
 */
@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

    // 再方法调用前拦截
    @Autowired
    private CurrentUserUtil currentUserUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判定是否拦截的为方法method
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            LoginRequired annotation = method.getAnnotation(LoginRequired.class);
            // 如果存在注解
            if (annotation != null && currentUserUtil.getUser() == null ) {
                // response的重定向
                response.sendRedirect(request.getContextPath() + "/login");
                // 拒绝执行controller
                return false;
            }
        }
        return true;
    }
}
