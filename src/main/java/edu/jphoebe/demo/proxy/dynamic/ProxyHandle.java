package edu.jphoebe.demo.proxy.dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 动态代理
 */
public class ProxyHandle implements InvocationHandler {
    public static void main(String[] args) {
        // 参数：-Dsun.misc.ProxyGenerator.saveGeneratedFiles 可以让jdk生成代理后的class文件， 源码：ProxyGenerator.saveGeneratedFiles
        DynamicUser targetObj = new DynamicUser();
        ProxyHandle proxyHandle = new ProxyHandle(new DynamicUser());
        DynamicPerson proxyObject = (DynamicPerson) Proxy.newProxyInstance(
                DynamicUser.class.getClassLoader(),
                DynamicUser.class.getInterfaces(),
                proxyHandle);
        proxyObject.exection();
        // 动态代理
        // 源码：Proxy.getProxyClass0
        // 源码：WeakCache.Factory.get
        // 源码：Proxy.ProxyClassFactory
        // 源码：ProxyGenerator.generateProxyClass
    }

    private Object proxyObj;

    public ProxyHandle(Object targetObj) {
        this.proxyObj = targetObj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        System.out.println("DynamicProxy类对" + methodName + "进行了增强处理,begin....");
        // 执行原有方法
        method.invoke(proxyObj, args);
        System.out.println("DynamicProxy类对" + methodName + "进行了增强处理,end....");
        return null;
    }
}

interface DynamicPerson {
    void exection();
}

class DynamicUser implements DynamicPerson {
    @Override
    public void exection() {
        System.out.println("实际执行的代码");
    }
}
