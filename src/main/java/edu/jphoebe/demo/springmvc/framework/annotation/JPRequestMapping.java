package edu.jphoebe.demo.springmvc.framework.annotation;

import java.lang.annotation.*;

/**
 * JPRequestMapping class
 *
 * @author 蒋时华
 * @date 2017/12/14
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface JPRequestMapping {

    String value() default "";

}
