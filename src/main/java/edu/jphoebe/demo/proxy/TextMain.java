package edu.jphoebe.demo.proxy;


/**
 * TextMain class
 *
 * @author 蒋时华
 * @date 2019/8/20
 */
public class TextMain {

    public static void main(String[] args) {

        // https://blog.csdn.net/doujinlong1/article/details/80680149

        // spring 如何通过代理创建bean
        // https://www.cnblogs.com/zcmzex/p/8822509.html
        // 创建对象
        // AbstractAutowireCapableBeanFactory.createBean.doCreateBean.createBeanInstance.instantiateBean
        // SimpleInstantiationStrategy.instantiate || CglibSubclassingInstantiationStrategy.instantiate
        // 填充实例化后bean的属性
        // AbstractAutowireCapableBeanFactory.createBean.doCreateBean.populateBean.initializeBean
        // AbstractAutowireCapableBeanFactory.applyBeanPostProcessorsBeforeInitialization.getBeanPostProcessors()
        // 判断是否需要创建代理类
        // InfrastructureAdvisorAutoProxyCreator, AbstractAutoProxyCreator.wrapIfNecessary
        // 为什么有的时候是jdk代理， 有的时候是cglib代理
        // AbstractAutoProxyCreator.createProxy
        // ProxyFactory.createAopProxy, DefaultAopProxyFactory.createAopProxy

        // note: spring boot ValidationAutoConfiguration.methodValidationPostProcessor 默认设置直接代理目标类
        // spring.aop.proxy-target-class: true
        // spring 默认是false  ProxyConfig.proxyTargetClass

        // 疑问：DefaultAopProxyFactory.createAopProxy 通过@autowire注入永远不会使用jdk代理

    }


}
