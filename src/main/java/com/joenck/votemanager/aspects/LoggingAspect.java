package com.joenck.votemanager.aspects;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Aspect
@Log4j2
public class LoggingAspect {

    @Before("execution(* com.joenck.votemanager.*.*Controller.*(..))")
    private void beforeControllers(JoinPoint joinPoint) {
        String argsString = Arrays.stream(joinPoint.getArgs()).filter(Objects::nonNull).map(value -> value.toString()).collect(Collectors.joining(", "));
        log.info("Entrando {}.{} com argumentos: {}", joinPoint.getSignature().getDeclaringTypeName(),joinPoint.getSignature().getName(),argsString);
    }

    @Before("execution(* com.joenck.votemanager.*.*Service.*(..))")
    private void beforeServices(JoinPoint joinPoint) {
        String argsString = Arrays.stream(joinPoint.getArgs()).filter(Objects::nonNull).map(value -> value.toString()).collect(Collectors.joining(", "));
        log.debug("Entrando {}.{} com argumentos: {}", joinPoint.getSignature().getDeclaringTypeName(),joinPoint.getSignature().getName(),argsString);
    }

    @AfterReturning(pointcut="execution(* com.joenck.votemanager.*.*Service.*(..))",
            returning="returnValue")
    private void afterReturningLists(JoinPoint joinPoint, Object returnValue) {
        String resultado = returnValue != null ? returnValue.toString() : "";
        log.info("Retornando de {}.{} com resultado: {}", joinPoint.getSignature().getDeclaringTypeName(),joinPoint.getSignature().getName(),resultado);
    }

}