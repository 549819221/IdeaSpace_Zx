package com.example.fastdfsdel.util.aspect;

import com.example.fastdfsdel.util.PowerUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * @description  本地调试切面,正式环境默认不开启
 * @date  20/04/20 14:05
 * @author  wanghb
 * @edit
 */
@Component
@Aspect
public class DebugLogAop {
    private final static com.example.fastdfsdel.util.aspect.DebugLogConfig config = new DebugLogConfig(true,true,false,true,false,"entity");
    @Value("${spring.profiles.active}")
    private String active;
    /**
     * @description  用于打印入参
     * @param  joinPoint
     * @return  void
     * @date  20/04/25 11:29
     * @author  wanghb
     * @edit
     */
    //execution表达式  可自行定义
    @Before("execution(* com.example.fastdfsdel..*(..)) ")
    public void advice(JoinPoint joinPoint) {
        config.isOpne = PowerUtil.getString( active ).indexOf(  "dev" ) >= 0 ;
        com.example.fastdfsdel.util.aspect.DebugLogAopUtil.advice(joinPoint,config);

    }

    /**
     * @description  用于打印返回值
     * @param  joinPoint
     * @return
     * @date  20/04/25 11:29
     * @author  wanghb
     * @edit
     */
    //execution表达式  可自行定义
    @Around("execution(* com.example.fastdfsdel..*(..)) ")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        config.isOpne = PowerUtil.getString( active ).indexOf(  "dev" ) >= 0 ;
        return DebugLogAopUtil.around( joinPoint,config);
    }

}
