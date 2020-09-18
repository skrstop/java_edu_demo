package edu.jphoebe.demo.springmvc.demo.controller;

import edu.jphoebe.demo.springmvc.demo.service.AService;
import edu.jphoebe.demo.springmvc.demo.service.BService;
import edu.jphoebe.demo.springmvc.framework.annotation.JPAutowired;
import edu.jphoebe.demo.springmvc.framework.annotation.JPController;
import edu.jphoebe.demo.springmvc.framework.annotation.JPRequestMapping;
import edu.jphoebe.demo.springmvc.framework.annotation.JPRequestParam;
import edu.jphoebe.demo.springmvc.framework.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * AController class
 *
 * @author 蒋时华
 * @date 2017/12/14
 */
@JPController
@JPRequestMapping("/conTest")
public class AController {

    @JPAutowired
    private AService aService;

    @JPAutowired("JPService")
    private BService bService;


    @JPRequestMapping("/.*..jp")
    public ModelAndView test1(HttpServletRequest request, HttpServletResponse response, @JPRequestParam("message") String message) throws IOException {
        // response.getWriter().write(message);
        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put("message", message);
        return new ModelAndView("first.jpml", modelMap);
    }

}
