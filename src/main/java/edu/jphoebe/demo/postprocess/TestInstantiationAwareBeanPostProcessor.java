package edu.jphoebe.demo.postprocess;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestInstantiationAwareBeanPostProcessor implements InstantiationAwareBeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("InstantiationAwareBeanPostProcessor -- before ====" + beanName);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("InstantiationAwareBeanPostProcessor -- before ====" + beanName);
        return bean;
    }
}
