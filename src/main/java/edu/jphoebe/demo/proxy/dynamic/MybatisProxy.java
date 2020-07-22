package edu.jphoebe.demo.proxy.dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * ProxyFactory class
 */
public class MybatisProxy implements InvocationHandler {

    public static void main(String[] args) {
        // mybatis mapper代理的实现
        MybatisProxy mybatisProxy = new MybatisProxy(PersonMapper.class);
        PersonMapper proxyObject = (PersonMapper) Proxy.newProxyInstance(
                PersonMapper.class.getClassLoader(),
                new Class[]{PersonMapper.class},
                mybatisProxy);
        proxyObject.exection();
    }

    private Object proxyObj;

    public MybatisProxy(Object targetObj) {
        this.proxyObj = targetObj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        System.out.println("执行与该接口对应的mapping xml文件中的sql");
        // 执行sql的操作
        return null;
    }
}

interface PersonMapper {
    void exection();
}
