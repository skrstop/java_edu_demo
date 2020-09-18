package edu.jphoebe.demo.springmvc.framework.context;

import edu.jphoebe.demo.springmvc.framework.annotation.JPAutowired;
import edu.jphoebe.demo.springmvc.framework.annotation.JPController;
import edu.jphoebe.demo.springmvc.framework.annotation.JPService;
import lombok.Getter;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * JPApplicationContext class
 *
 * @author 蒋时华
 * @date 2017/12/14
 */
public class JPApplicationContext {

    public Map<String, Object> instanceMapping = new ConcurrentHashMap<>();

    // 简单模拟beanDefindtion
    private List<String> classCache = new ArrayList<>();

    @Getter
    private Properties config;

    public JPApplicationContext(String localtionConfig) {
        // 定位、载入、注册、初始化、注入

        try {
            // 定位
            // 寻找配置文件
            InputStream is = null;
            is = this.getClass().getClassLoader().getResourceAsStream(localtionConfig);

            // 载入
            // 加载配置文件
            config = new Properties();
            config.load(is);

            // 注册
            // 将配置项转换为bean: bean -> beanDefindetion 、 aop -> AopConfig等等
            // 扫描所有的class，和类和类之间的关系（Map/list/set/ref/parent）
            String scanPackage = config.getProperty("scanPackage");
            doRegister(scanPackage);

            // 初始化
            // 实例化对象
            doCreateBean();

            // 注入
            // 依赖属性注入
            populate();

        } catch (Exception e) {

        }
        System.out.println("啊蒋版的ioc容器初始化完成！！！");
    }

    private void doRegister(String scanPackage) {
        URL resource = this.getClass().getResource("/" + scanPackage.replaceAll("\\.", "/"));

        File dir = new File(resource.getFile());
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                doRegister(scanPackage + "." + file.getName());
            } else {
                classCache.add(scanPackage + "." + file.getName().replace(".class", "").trim());
            }
        }
    }

    private void doCreateBean() {
        // 检查有没有注册信息
        if (classCache.isEmpty()) {
            return;
        } else {
            for (String className : classCache) {
                // 代理判断和创建，省略

                // 为了方便直接使用反射
                try {
                    Class<?> aClass = Class.forName(className);
                    // 扫描注解
                    // beanName 默认首字母小写类名
                    if (aClass.isAnnotationPresent(JPController.class)) {
                        instanceMapping.put(this.getBeanName(aClass.getSimpleName()), aClass.newInstance());
                    } else if (aClass.isAnnotationPresent(JPService.class)) {
                        // 设置了自定义名字
                        JPService annotation = aClass.getAnnotation(JPService.class);
                        String value = annotation.value();
                        if (!"".equals(value.trim())) {
                            instanceMapping.put(value, aClass.newInstance());
                        } else {
                            // 接口
                            if (aClass.isInterface()) {
                                Class<?>[] interfaces = aClass.getInterfaces();
                                // 如果是接口，就用接口的类型作为beanName
                                for (Class<?> anInterface : interfaces) {
                                    instanceMapping.put(anInterface.getName(), aClass.newInstance());
                                }
                            } else {
                                // 类
                                instanceMapping.put(this.getBeanName(aClass.getSimpleName()), aClass.newInstance());
                            }
                        }
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void populate() {
        if (instanceMapping.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Object> entry : instanceMapping.entrySet()) {
            Field[] declaredFields = entry.getValue().getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                if (!field.isAnnotationPresent(JPAutowired.class)) {
                    return;
                }
                JPAutowired annotation = field.getAnnotation(JPAutowired.class);
                String value = annotation.value();
                String id;
                if (!"".equals(value.trim())) {
                    id = value;
                } else {
                    // 默认使用类型名字注入
                    id = this.getBeanName(field.getType().getName());
                }
                field.setAccessible(true);
                try {
                    field.set(entry.getValue(), instanceMapping.get(id));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public Object getBean(String name) {
        return null;
    }

    public Map<String, Object> getAll() {
        return instanceMapping;
    }

    // 将首字母小写
    private String getBeanName(String className) {
        char[] chars = className.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }


}
