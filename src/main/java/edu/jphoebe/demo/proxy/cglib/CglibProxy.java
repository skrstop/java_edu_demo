package edu.jphoebe.demo.proxy.cglib;


import net.sf.cglib.core.DebuggingClassWriter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * CglibProxy class
 *
 * @author 蒋时华
 * @date 2019/8/29
 */
public class CglibProxy implements MethodInterceptor {

    public static void main(String[] args) {
        // cglib 代理
        // 代理类class文件存入本地磁盘方便我们反编译查看源码
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "E:\\prod\\edu-demo");
        // 通过CGLIB动态代理获取代理对象的过程
        Enhancer enhancer = new Enhancer();
        // 设置enhancer对象的父类
        enhancer.setSuperclass(CglibUser.class);
        // 设置enhancer的回调对象
        enhancer.setCallback(new CglibProxy());
        // 创建代理对象
        CglibUser proxy = (CglibUser) enhancer.create();
        // 通过代理对象调用目标方法
        proxy.exection();


        // 源码
        // MethodInterceptor
        // Enhancer.createHelper()
        // AbstractClassGenerator.create()
        // AbstractClassGenerator.generate()
        // KeyFactory.generateClass()
        // BeanGenerator.nextInstance()
    }

    /**
     * sub：cglib生成的代理对象
     * method：被代理对象方法
     * objects：方法入参
     * methodProxy: 代理方法
     */
    /**
     * FastClass f1; // net.sf.cglib.test.Target的fastclass
     * FastClass f2; // Target$$EnhancerByCGLIB$$788444a0 的fastclass
     * int i1; //方法g在f1中的索引
     * int i2; //方法CGLIB$g$0在f2中的索引
     **/
    @Override
    public Object intercept(Object sub, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("======插入前置通知======");
        Object object = methodProxy.invokeSuper(sub, objects);
        System.out.println("======插入后者通知======");
        return object;
    }
}

class CglibUser {
    public void exection() {
        System.out.println("实际执行的代码");
    }
}
