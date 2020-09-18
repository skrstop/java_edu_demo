package edu.jphoebe.demo.springmvc.framework.annotation;

import java.lang.annotation.*;

/**
 * JPRequestParam class
 *
 * @author 蒋时华
 * @date 2017/12/14
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Documented
public @interface JPRequestParam {

    String value() default "";

    boolean required() default true;

}
