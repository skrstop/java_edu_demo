#include "jni.h"
#include "stdio.h"
#include "stdlib.h"
#include "edu_jphoebe_demo_jni_JniExceptionDemo.h"

JNIEXPORT jint JNICALL Java_edu_jphoebe_demo_jni_JniExceptionDemo_exception1
  (JNIEnv * env, jobject obj) {
    int Dividend = 10;
    int Divisor = 0;

    int a= Dividend / Divisor;
    return a;
}

JNIEXPORT jint JNICALL Java_edu_jphoebe_demo_jni_JniExceptionDemo_exception2
  (JNIEnv * env, jobject obj) {
      int *p = NULL;
    int x = 100 + *p;
    return x;
}

JNIEXPORT jint JNICALL Java_edu_jphoebe_demo_jni_JniExceptionDemo_exception3
  (JNIEnv * env, jobject obj){
    int p;
    	int i;
    	int a = 1;
    	int* pp = &p;
    	pp += 128;
    	for (i = 0; i < 512; ++i) {
    		pp[i] = rand();
    	}
    	return p;
}