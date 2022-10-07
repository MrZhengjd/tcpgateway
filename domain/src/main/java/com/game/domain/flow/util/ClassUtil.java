package com.game.domain.flow.util;


import com.game.domain.flow.model.Request;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ClassUtil {
    public static <E> E newInstance(String className,Class<E> clazz,Object... params){
        Class<?> aClass = forName(className);
        return newInstance(aClass,clazz,params);
    }
    /**
     * 创建新的实例对象
     * @param clazz 类名
     * @param expected 转换目标对象
     * @param params 请求参数
     * @param <E>
     * @return
     */
    public static <E> E newInstance(Class<?> clazz, Class<E> expected, Object... params) {
        if (expected != null && !expected.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException(
                    "'" + clazz.getSimpleName() + "'" + "'must implements interface:'" + expected.getName() + "'");
        }
        if (params == null || params.length == 0) {
            try {
                return (E) clazz.newInstance();
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        } else {
            List<Class<?>> parameterTypes = getParamsType(params);
            try {
                return (E) clazz.getConstructor(parameterTypes.toArray(new Class<?>[0])).newInstance(params);
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    private static Class<?> forName(String className) {
        try {
            return ClassUtils.forName(className,Thread.currentThread().getContextClassLoader());
        }catch (Exception e){
            throw new IllegalArgumentException("cannot find the class "+className);
        }
    }

    public static List<Class<?>> getParamsType(@Nullable Object... params){
        List<Class<?>> parameterType = new ArrayList<>();
        if (params != null && params.length > 0){
            for (Object o : params){
                parameterType.add(o.getClass());
            }
        }
        return parameterType;

    }

    public static <T> Object methodInvoke(String simpleName, String methodName, Request initInput) {
        Class<?> clazz = forName(simpleName);
        Object target ;
        try {
            target = clazz.newInstance();
        } catch (Exception e){
            throw new IllegalArgumentException("cannot find it");
        }
        return methodInvoke(target,methodName,initInput);
    }
    public static <T> Object methodInvoke(Object target,String methodName,@Nullable Object... initInput){
        List<Class<?>> parameterType = getParamsType(initInput);
        Method method = ReflectionUtils.findMethod(target.getClass(),methodName, parameterType.toArray(new Class<?>[0]));
        Object ret = null;
        if (method == null){
            throw new RuntimeException("cannot cast it");
        }
        ret = ReflectionUtils.invokeMethod(method,target,initInput);
        return ret;
    }
}
