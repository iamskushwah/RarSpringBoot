package com.wellsfargo.rarconsumer.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * Utility class to handle logging in the application.
 *
 * @author u670595
 * @version 1.0
 */

@Aspect
@Component
@Configuration
public class RarLoggingAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(RarLoggingAspect.class);

    @Before("execution(* com.wellsfargo.rarconsumer.controllers.*.*(..))")
    public void beforeController(JoinPoint joinPoint) {
        //Advice
        LOGGER.info("Allowed execution for Controller {}", joinPoint);
        LOGGER.info(getParams(joinPoint));
    }

    @Before("execution(* com.wellsfargo.rarconsumer.service.*.*(..))")
    public void before(JoinPoint joinPoint) {
        //Advice
        LOGGER.info("Allowed execution for Service {}", joinPoint);
    }

    @Before("execution(* com.wellsfargo.rarconsumer.repository.*.*(..))")
    public void beforeRepository(JoinPoint joinPoint) {
        //Advice
        LOGGER.info("Allowed execution for Repository {}", joinPoint);
    }


    @Around("execution(* com.wellsfargo.rarconsumer.service..*(..)))")
    public Object profileAllMethods(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();

        //Get intercepted method details
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();

        final StopWatch stopWatch = new StopWatch();

        //Measure method execution time
        stopWatch.start();
        Object result = proceedingJoinPoint.proceed();
        stopWatch.stop();

        //Log method execution time
        LOGGER.info("Execution time of " + className + "." + methodName + " :: " + stopWatch.getTotalTimeMillis() + " ms");

        return result;
    }

    @AfterReturning(value = "execution(* com.wellsfargo.rarconsumer.service.*.*(..))",
            returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        LOGGER.debug("{} returned with value {}", joinPoint, result);
    }

    @After(value = "execution(* com.wellsfargo.rarconsumer.service.*.*(..))")
    public void after(JoinPoint joinPoint) {
        LOGGER.info("after execution of Service {}", joinPoint);
    }

    @After(value = "execution(* com.wellsfargo.rarconsumer.controllers.*.*(..))")
    public void afterController(JoinPoint joinPoint) {
        LOGGER.info("after execution of Controller {}", joinPoint);
    }

    @After(value = "execution(* com.wellsfargo.rarconsumer.repository.*.*(..))")
    public void afterRepository(JoinPoint joinPoint) {
        LOGGER.info("after execution of Repository {}", joinPoint);
    }

    private String getParams(JoinPoint joinPoint) {
        StringBuilder sb = new StringBuilder();
        Object[] signatureArgs = joinPoint.getArgs();
        int index = 0;
        for (Object signatureArg : signatureArgs) {
            sb.append("Arg ").append(index).append(":").append(signatureArg);
            index++;
        }
        return sb.toString();
    }
}
