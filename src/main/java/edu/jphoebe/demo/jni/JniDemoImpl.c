#include <jni.h>
#include <stdio.h>
#include <edu_jphoebe_demo_jni_JniDemo.h>

JNIEXPORT void JNICALL Java_edu_jphoebe_demo_jni_JniDemo_printByNative
  (JNIEnv * env, jobject obj, jstring content) {
    const jbyte* str=(const jbyte*)(*env)->GetStringUTFChars(env,content,JNI_FALSE);
    printf("Hello------->%s\n",str);
    (*env)->ReleaseStringUTFChars(env,content,(const char*)str);
    return;
}

JNIEXPORT jint JNICALL Java_edu_jphoebe_demo_jni_JniDemo_add
  (JNIEnv * env, jobject obj, jint data1, jint data2) {
      printf("Hello------->%d+%d\n",data1, data2);
      return data1+data2;
}

JNIEXPORT void JNICALL Java_edu_jphoebe_demo_jni_JniDemo_setCallback
  (JNIEnv * env, jobject obj){
    printf("调用回调函数");
    jclass cls = (*env)->GetObjectClass(env, obj);
	jmethodID jmid = (*env)->GetMethodID(env, cls, "callback", "(Ljava/lang/String;)V");
	jstring info = (*env)->NewStringUTF(env, "回调函数parameter");
    (*env)->CallVoidMethod(env,obj, jmid,info);
}