package edu.jphoebe.demo.springmvc.framework.annotation;

import java.lang.annotation.*;

/**
 * JPResponseBody class
 *
 * @author 蒋时华
 * @date 2017/12/14
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface JPResponseBody {

    String value() default "";

}
