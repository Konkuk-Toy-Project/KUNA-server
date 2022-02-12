package konkuk.shop.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
public class AspectLog {
    @Around("execution(* konkuk.shop.controller..*(..))")
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        //log.info("호출한 컨트롤러 메소드 이름 = {}", joinPoint.getSignature().getName());

        return joinPoint.proceed();
    }
}