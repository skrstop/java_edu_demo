package edu.jphoebe.demo.test.impl;

import edu.jphoebe.demo.test.DemoService3;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * DemoServiceImpl class
 *
 * @author 蒋时华
 * @date 2019/8/21
 */
@Service
@Scope(proxyMode = ScopedProxyMode.INTERFACES)
public class DemoServiceImpl3 implements DemoService3 {

    @Transactional
    @Override
    public void update() {

    }
}
