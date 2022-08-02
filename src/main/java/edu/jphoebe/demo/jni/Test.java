package edu.jphoebe.demo.jni;

/**
 * @author 蒋时华
 * @date 2022-08-02 14:30:23
 */
public class Test {


    static {
        // 平台类库目录下
//        System.loadLibrary("JniDemoImpl");
        // 自定义
        System.load("/Users/jphoebe/IdeaProjects/edu/java_edu_demo/src/main/java/edu/jphoebe/demo/jni/JniDemoImpl.jnilib");
    }

    public static void main(String[] args) {
        String s = "111";
        JniDemo.printByNative(s);
        JniDemo jniDemo = new JniDemo() {
            @Override
            public void callback(String msg) {
                super.callback(msg);
                System.out.println("重写jni回调");
            }
        };
        int add = jniDemo.add(1, 2);
        System.out.println(add);
        // 回调监听注册
        jniDemo.setCallback();
    }

}
