package com.mdy.stock.advice;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @author mdy
 * @date 2024-04-16 9:40
 * @description 测试Spring的AOP实现
 */

@Component
@Aspect
public class UserAdvice {

    @Pointcut("execution(* com.mdy.stock.service.impl.UserServiceImpl.getInfoByUsername(..))")
    private void pt(){}

    @After("pt()")
    private void doAfter() {
//        System.out.println("我是after");
    }

    @Around("pt()")
    private Object boostByAround(ProceedingJoinPoint pjp) throws Throwable {
//        System.out.println("方法执行前");
        Object info = pjp.proceed();
//        System.out.println("方法执行后");
        return info;
    }


}
