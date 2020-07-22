package edu.jphoebe.demo.springmvc.framework.servlet;

import lombok.Data;

import java.util.Map;

/**
 * ModelAndView class
 *
 * @author 蒋时华
 * @date 2020/1/11
 */
@Data
public class ModelAndView {

    // 页面末班
    private String view;

    // 要网页面上带过去的值
    private Map<String, Object> model;

    public ModelAndView(String view) {
        this.view = view;
    }

    public ModelAndView(String view, Map<String, Object> model) {
        this.view = view;
        this.model = model;
    }
}
