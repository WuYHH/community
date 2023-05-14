package com.example.demo.advice;

import com.example.demo.util.CommunityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author wuyuhan
 * @date 2023/5/14 22:19
 */
// 捕获所有controller的异常
@ControllerAdvice(annotations = Controller.class)
public class ExceptionAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    @ExceptionHandler({Exception.class})
    public void handleException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.error("服务器发生异常：" + e.getMessage());
        for (StackTraceElement  elment: e.getStackTrace()) {
            logger.error(elment.toString());
        }
        String requestMeans = request.getHeader("x-requested-with");
        // 异步请求，例如弹出框的请求
        if ("XMLHttpRequest".equals(requestMeans)) {
            response.setContentType("application/plain;charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(CommunityUtil.getJsonString(1, "服务器异常！"));
        } else {
            // 同步请求，重定向到error页面
            response.sendRedirect(request.getContextPath() + "/error");
        }

    }
}
