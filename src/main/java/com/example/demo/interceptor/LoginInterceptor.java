package com.example.demo.interceptor;

import com.example.demo.dao.LoginTicketMapper;
import com.example.demo.dao.UserMapper;
import com.example.demo.entity.LoginTicket;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.util.CookieUtil;
import com.example.demo.util.HostHolderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @author wuyuhan
 * @date 2023/5/5 21:31
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolderUtil hostHolderUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ticket = CookieUtil.getValue(request, "ticket");
        if (ticket != null) {
            // 取出凭证
            LoginTicket loginTicket = userService.findLoginTicket(ticket);
            // 检查凭证是否有效
            if (loginTicket != null && loginTicket.getStatus() == 0 &&
                loginTicket.getExpired().after(new Date())) {
                User user = userService.findUserById(loginTicket.getUserId());
                if (user != null) {
                    // 本次请求中持有用户
                    hostHolderUtil.setUser(user);
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // controller处理完后，发送到模板前进行取出
        User user = hostHolderUtil.getUser();
        if (user != null && modelAndView != null) {
            modelAndView.addObject("currentUser", user);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolderUtil.clear();
    }
}
