package edu.jphoebe.demo.postprocess;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;

/**
 * 在bean实例化并且赋值后调用
 * 主要用来执行 init-method 和 afterProtertiesSet 等初始化方法
 */
@Configuration
public class TestBeanPostProcess implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//        System.out.println("BeanPostProcessor -- before ===="+ beanName);

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//        System.out.println("BeanPostProcessor -- after ===="+ beanName);


        return bean;
    }
}

