package com.game.mj.messagedispatch;


import com.game.mj.model.HeaderAnno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zheng
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GameMessageDispatch {
    public HeaderAnno value();
    public String name() default "";
    public String onUsed() default "true";

}
