package edu.jphoebe.demo.springmvc.demo.config;

import edu.jphoebe.demo.springmvc.framework.servlet.JPDispatcherServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * DispatcherServletConfig class
 *
 * @author 蒋时华
 * @date 2019/12/14
 */
@Configuration
public class DispatcherServletConfig {

    @Bean
    public ServletRegistrationBean jpDispatcherSelvet() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new JPDispatcherServlet(), "*.jp");
        servletRegistrationBean.setLoadOnStartup(1);
        Map<String, String> initParameters = new HashMap<>();
        initParameters.put("contextConfigLocation", "JPSpring.properties");
        servletRegistrationBean.setInitParameters(initParameters);
        return servletRegistrationBean;
    }

}
