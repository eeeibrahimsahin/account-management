package com.abc.bank.accountmanagement.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    private static final String TRACE_ID_HEADER = "Trace-Id";

    @Value("${debug.mode:false}")
    private boolean debugMode;

    @Before("execution(* com.abc.bank.accountmanagement.controller..*(..)) || execution(* com.abc.bank.accountmanagement.service..*(..)) || execution(* com.abc.bank.accountmanagement.repository..*(..))")
    public void logBefore(JoinPoint joinPoint) {
        if (debugMode) {
            String traceId = MDC.get(TRACE_ID_HEADER);
            logger.info("Before executing method: " + joinPoint.getSignature().getName() + " Trace-ID: " + traceId);
        }
    }

    @After("execution(* com.abc.bank.accountmanagement.controller..*(..)) || execution(* com.abc.bank.accountmanagement.service..*(..)) || execution(* com.abc.bank.accountmanagement.repository..*(..))")
    public void logAfter(JoinPoint joinPoint) {
        if (debugMode) {
            String traceId = MDC.get(TRACE_ID_HEADER);
            logger.info("After executing method: " + joinPoint.getSignature().getName() + " Trace-ID: " + traceId);
        }
    }
}
