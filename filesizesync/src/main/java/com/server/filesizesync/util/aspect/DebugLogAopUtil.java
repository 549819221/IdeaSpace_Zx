package com.server.filesizesync.util.aspect;

import com.alibaba.fastjson.JSON;
import com.server.filesizesync.util.PowerUtil;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @description  本地调试切面,正式环境默认不开启
 * @date  20/04/20 14:05
 * @author  wanghb
 * @edit  
 */
public class DebugLogAopUtil {


    /**
     * @description  用于打印入参
     * @param  joinPoint
     * @param config
     * @return  void
     * @date  20/04/25 11:29
     * @author  wanghb
     * @edit
     */
    public static void advice(JoinPoint joinPoint,DebugLogConfig config) {
        if(config.isOpne  && config.isOpneIn && isPrint(joinPoint)){
            StackTraceElement stackTraceElement = getStackTraceElement(joinPoint);
            //类名
            String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
            Object[] args = joinPoint.getArgs();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < args.length; i++) {
                sb.append("第" + (i+1) + "个参数为:");
                Object arg = args[i];
                if(arg instanceof HttpServletRequest){
                    HttpServletRequest temp = (HttpServletRequest) arg;
                    sb.append( JSON.toJSONString(toMap( temp )));
                }else if(arg instanceof HttpServletResponse){

                }else if(!(arg instanceof Class) && PowerUtil.getString( arg ).indexOf( config.entityMark ) != -1){
                    sb.append( JSON.toJSONString( arg ) );
                }else{
                    sb.append( JSON.toJSONString( arg )) ;
                }
                sb.append("   ");
            }
            if((className.indexOf( "Controller" ) != -1 && config.isOpneController) || (className.indexOf( "Service" ) != -1 && config.isOpneServe)){
                System.err.println(stackTraceElement + sb.toString());
            }
        }
    }

    /**
     * @description  用于打印返回值
     * @param  joinPoint
     * @return
     * @date  20/04/25 11:29
     * @author  wanghb
     * @edit
     */
    public static Object around(ProceedingJoinPoint joinPoint,DebugLogConfig config) throws Throwable {
        Object result = "";
        Object[] args = joinPoint.getArgs();
        if(config.isOpne && config.isOpneOut && isPrint(joinPoint)){
            StringBuffer sb = new StringBuffer("返回值为:");
            //类名
            String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
            StackTraceElement stackTraceElement = getStackTraceElement(joinPoint);
            result = joinPoint.proceed(args);
            if(result instanceof HttpServletRequest){
                HttpServletRequest temp = (HttpServletRequest) result;
                sb.append( JSON.toJSONString(toMap( temp )));
            }else if(PowerUtil.getString( result ).indexOf( config.entityMark ) != -1){
                sb.append( JSON.toJSONString( result ));
            }else{
                sb.append( ( PowerUtil.getString( result ).equals( "" ) ? "void" : PowerUtil.getString( result )));
            }
            if((className.indexOf( "Controller" ) != -1 && config.isOpneController) || (className.indexOf( "Service" ) != -1 && config.isOpneServe)){
                System.err.println(stackTraceElement + sb.toString());
            }
        }else{
            result = joinPoint.proceed(args);
        }
        return result;
    }

    /**
     * @description  判断方法是否包含不打印注解
     * @param  joinPoint
     * @return  boolean 判断结果
     * @date  20/04/26 11:11
     * @author  wanghb
     * @edit  .
     */
    public static boolean isPrint(JoinPoint joinPoint){
        String classNameStr = joinPoint.getSignature().getDeclaringType().getSimpleName();
        if(classNameStr.indexOf( "Controller" )  == -1 && classNameStr.indexOf( "Serve" )  == -1 ){
            return false;
        }
        //方法名
        String methodName = joinPoint.getSignature().getName();
        Class<?> className = joinPoint.getTarget().getClass();
        Method[] methods = className.getMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            if(method.getName().equals(  methodName)){
                return !method.isAnnotationPresent(NoPrintDebug.class);
            }
        }
        return false;
    }

    /**
     * @description  ProceedingJoinPoint 转 StackTraceElement
     * @param  joinPoint 待转类型
     * @return  stackTraceElement  转换结果
     * @date  20/04/24 19:09
     * @author  wanghb
     * @edit
     */
    public static StackTraceElement getStackTraceElement(JoinPoint joinPoint) {
        //完整类名
        String declaringClass = (joinPoint.getSignature().getDeclaringType() + "").replace( "class ","" );
        //类名
        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        //方法名
        String methodName = joinPoint.getSignature().getName();
        //文件名
        String fileName = new StringBuilder(className).append( ".java" ).toString();

        ClassPool pool = ClassPool.getDefault();
        CtMethod methodX = null;
        try {
            CtClass cc = pool.get(declaringClass);
            methodX = cc.getDeclaredMethod(methodName);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        int xlineNumber = methodX.getMethodInfo().getLineNumber(0);
        return new StackTraceElement(className, methodName, fileName, xlineNumber - 1);
    }

    /**
     * @description  Object转Map
     * @param  bean  待转类型
     * @@return  map  Map数据
     * @date  20/05/20 17:53
     * @author  wanghb
     * @edit
     */
    public static Map<String, Object> toMap(Object bean){

        if(bean == null){
            return new HashMap<>();
        }
        Class type = bean.getClass();
        Map<String,Object> returnMap = new HashMap<>();
        BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(type);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i < propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();
                if (!propertyName.equals("class")) {
                    Method readMethod = descriptor.getReadMethod();
                    Object result = readMethod.invoke(bean, new Object[0]);
                    if (result != null) {
                        returnMap.put(propertyName, result);
                    } else {
                        returnMap.put(propertyName, "");
                    }
                }
            }
        }catch (IntrospectionException e){
        }catch (InvocationTargetException e){
        }catch (IllegalAccessException e){}

        return  returnMap;
    }

    /**
     * @description  Object转字符串
     * @param  value  待转字段
     * @@return  String  String字符串
     * @date  20/05/20 17:52
     * @author  wanghb
     * @edit
     */
    public static String getString(Object value){
       return value == null ? "" : String.valueOf(value);
    }
}
