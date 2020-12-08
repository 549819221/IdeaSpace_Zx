package com.example.fastdfsdel.util.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)//记录在.class文件中，并且在运行时保留"注释"
@Target({ElementType.METHOD})//指定注解只能放在"方法"上ElementType.FIELD,
/**
 * @description  用来标注类方法不需要执行debugLog打印  {@link DebugLogAop}
 * @date  20/04/25 12:09
 * @author  wanghb
 * @edit  .
 */
public @interface NoPrintDebug {
    public String value() default "";
}
