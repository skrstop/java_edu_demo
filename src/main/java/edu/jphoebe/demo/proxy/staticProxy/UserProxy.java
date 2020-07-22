package edu.jphoebe.demo.proxy.staticProxy;

/**
 * 静态代理
 */
public class UserProxy implements StaticPerson {

    public static void main(String[] args) {
        // 静态代理
        StaticUser targetObj = new StaticUser();
        new UserProxy(targetObj).exection();

    }

    public StaticUser user;

    public UserProxy(StaticUser user) {
        this.user = user;
    }

    @Override
    public void exection() {
        System.out.println("执行真正的代码前。。。。。。");

        user.exection();

        System.out.println("执行真正的代码后。。。。。。");
    }

}

interface StaticPerson {
    void exection();
}

class StaticUser implements StaticPerson {
    @Override
    public void exection() {
        System.out.println("实际执行的代码");
    }
}
