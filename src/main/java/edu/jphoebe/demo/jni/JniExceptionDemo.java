package edu.jphoebe.demo.jni;

/**
 * javac -h ./ ./JniDemo 生成 c 头文件
 * <p>
 * gcc -dynamiclib \
 * -I /Users/jphoebe/.asdf/installs/java/liberica-21.0.1+12/include/ \
 * -I /Users/jphoebe/.asdf/installs/java/liberica-21.0.1+12/include/darwin/ \
 * -I /Users/jphoebe/opt/code/IdeaProjects/edu/java_edu_demo/src/main/java/edu/jphoebe/demo/jni/cSource/demo \
 * ./JniDemoImpl.c -o JniDemoImpl.jnilib
 *
 * gcc -dynamiclib \
 * -I /Users/jphoebe/.asdf/installs/java/liberica-21.0.1+12/include/ \
 * -I /Users/jphoebe/.asdf/installs/java/liberica-21.0.1+12/include/darwin/ \
 * -I /Users/jphoebe/opt/code/IdeaProjects/edu/java_edu_demo/src/main/java/edu/jphoebe/demo/jni/cSource/exception \
 * ./JniExceptionImpl.c -o JniExceptionImpl.jnilib
 *
 * @author 蒋时华
 * @date 2022-08-02 14:36:11
 */
public class JniExceptionDemo {

    // 除0
    public native int exception1();

    // 空指针
    public native int exception2();

    // 破坏堆栈
    public native int exception3();


}
