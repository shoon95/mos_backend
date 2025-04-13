package com.mos.backend.common.annotation;

import org.springframework.messaging.handler.annotation.ValueConstants;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Sender {
    String value() default ValueConstants.DEFAULT_NONE;
}
