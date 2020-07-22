package edu.jphoebe.demo.postprocess;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Configuration;

/**
 * 在扫包之后得到所有BeanDefinition后执行，主要用来处理自定义BeanDefinition
 */
@Configuration
public class TestBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
//        System.out.println("BeanFactoryPostProcessor ===="+ configurableListableBeanFactory.toString());
    }
}

/**
 * BeanDefinitionRegistryPostProcessor继承自BeanFactoryPostProcessor
 * 主要添加registry方法，在BeanDefinition被保存之前执行
 */
@Configuration
class TestBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
//        System.out.println("BeanDefinitionRegistryPostProcessor -- registry ===="+ beanDefinitionRegistry.toString());
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
//        System.out.println("BeanDefinitionRegistryPostProcessor -- beanFactory ===="+ configurableListableBeanFactory.toString());
    }
}

