package com.game.mj.messagedispatch;


import com.game.mj.model.GameMessage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zheng
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GameMessageListener {
    public Class<? extends GameMessage> value();
    public String name() default "";
    public String onUsed() default "true";

}
