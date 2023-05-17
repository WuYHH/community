package com.dlut.community.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @author wuyuhan
 * @date 2023/5/14 22:40
 */
//@Component
//@Aspect
public class AlphaAspect {

    // 定义切点（织入点的子集）
    @Pointcut("execution(* com.dlut.community.service.*.*(..))")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void before() {
        System.out.println("before....");
    }

    @After("pointcut()")
    public void after() {
        System.out.println("after....");
    }

    @AfterThrowing("pointcut()")
    public void afterReturing() {
        System.out.println("afterThrowing....");
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        System.out.println("Around before...");
        // 织入的方法
        Object proceed = joinPoint.proceed();

        System.out.println("Around after...");
        return proceed;
    }


}
