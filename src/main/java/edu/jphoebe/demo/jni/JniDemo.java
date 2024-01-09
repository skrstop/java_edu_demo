package edu.jphoebe.demo.jni;

/**
 * javac -h ./ ./JniDemo 生成 c 头文件
 * <p>
 * gcc -dynamiclib \
 * -I /Library/Java/JavaVirtualMachines/adoptopenjdk-11.jdk/Contents/Home/include/ \
 * -I /Library/Java/JavaVirtualMachines/adoptopenjdk-11.jdk/Contents/Home/include/darwin/ \
 * -I /Users/jphoebe/IdeaProjects/edu/java_edu_demo/src/main/java/edu/jphoebe/demo/soWrapper/ \
 * JniDemoImpl.c -o JniDemoImpl_x86.jnilib
 *
 * @author 蒋时华
 * @date 2022-08-02 14:36:11
 */
public class JniDemo {

    public native int add(int data1, int data2);

    public static native void printByNative(String s);

    /**
     * 回调注册
     */
    public native void setCallback();

    /**
     * 回调接受
     *
     * @param msg
     */
    public void callback(String msg) {
        System.out.println(msg);
    }

}
