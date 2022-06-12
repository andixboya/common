package com.andi.common.auth.annotation;

import org.springframework.context.annotation.ComponentScan;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ComponentScan(basePackages = {
//        "com.lms.auth.common.config", // previously
        "com.andi.auth.common.config",
})
public @interface EnableAdvancedSecurity {
}
