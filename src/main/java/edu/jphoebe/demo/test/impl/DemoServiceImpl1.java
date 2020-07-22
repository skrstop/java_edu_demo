package edu.jphoebe.demo.test.impl;

import edu.jphoebe.demo.test.DemoService1;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

/**
 * DemoServiceImpl class
 *
 * @author 蒋时华
 * @date 2019/8/21
 */
@Service
public class DemoServiceImpl1 implements DemoService1, InitializingBean {

    @Override
    @Transactional
    public void update() {

    }

    @PostConstruct
    public void init() {
        System.out.println("DemoService1执行初始化方法");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("DemoService1执行afterProtertiesSet方法");
    }
}
