package com.game.mj.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zheng
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(PackageSelector.class)
public @interface EnablePackage {
//    @AliasFor("value")
    String[] value() default {};

//    @AliasFor("basePackages")
    String[] basePackages() default {};
}
