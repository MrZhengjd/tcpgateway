package com.game.mj.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zheng
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BandKeyAndType {
   int serviceId() default 0;
   int gametype() default 0;
//   Class<?> baseClass() default ;
}