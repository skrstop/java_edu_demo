package edu.jphoebe.demo.springmvc.framework.servlet;

import com.edu.spring.spring.framework.annotation.JPController;
import com.edu.spring.spring.framework.annotation.JPRequestMapping;
import com.edu.spring.spring.framework.annotation.JPRequestParam;
import com.edu.spring.spring.framework.context.JPApplicationContext;
import lombok.Data;
import org.springframework.core.DefaultParameterNameDiscoverer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * JPDispatcherServlet class
 *
 * @author 蒋时华
 * @date 2019/12/14
 */
public class JPDispatcherServlet extends HttpServlet {

    public static String LOCATION = "contextConfigLocation";

    //    private Map<String, Handler> handlerMapping = new ConcurrentHashMap<>();
    // 为什么源码中会使用list呢？为什么不使用map呢
    // 因为url并不是固定的，可能是动态的，无法通过map.get获取
    // 可以是代码看起来更精简
    private List<Handler> handlerMappingList = new ArrayList<>();


    //    private Map<Handler, handlerAdapter> handlerhandlerAdapterMap = new ConcurrentHashMap<>();
    // 为什么源码中会使用list呢？为什么不使用map呢
    private List<handlerAdapter> handlerhandlerAdapterList = new ArrayList<>();

    private List<JPViewResolver> viewResolvers = new ArrayList<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        System.out.println("有参初始化啊蒋版的springmvc");
        // 初始化ioc容器
        JPApplicationContext jpApplicationContext = new JPApplicationContext(config.getInitParameter(LOCATION));
        // 请求解析
//        this.initMultipartResolver(jpApplicationContext);
        // 国际化
//        this.initLocaleResolver(jpApplicationContext);
        // 主题
//        this.initThemeResolver(jpApplicationContext);

        // url mapping映射关系
        this.initHandlerMappings(jpApplicationContext);
        // 适配器
        this.initHandlerAdapters(jpApplicationContext);

        // 异常处理
//        this.initHandlerExceptionResolvers(jpApplicationContext);
        // 视图转发
//        this.initRequestToViewNameTranslator(jpApplicationContext);
        // 解析模板中的内容
        this.initViewResolvers(jpApplicationContext);
//        this.initFlashMapManager(jpApplicationContext);

    }

    private void initMultipartResolver(JPApplicationContext context) {
        // 请求解析
    }

    private void initLocaleResolver(JPApplicationContext context) {
        // 国际化
    }

    private void initThemeResolver(JPApplicationContext context) {
        // 主题
    }

    private void initHandlerMappings(JPApplicationContext context) {
        // url mapping映射关系
        // 解析controller类
        Map<String, Object> instanceMapping = context.instanceMapping;
        if (instanceMapping.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Object> entry : instanceMapping.entrySet()) {
            if (!entry.getValue().getClass().isAnnotationPresent(JPController.class)) {
                continue;
            }
            JPRequestMapping annotation = entry.getValue().getClass().getAnnotation(JPRequestMapping.class);
            String url = "";
            if (annotation != null) {
                if (annotation.value().startsWith("/")) {
                    url += annotation.value();
                } else {
                    url += ("/" + annotation.value());
                }
            }
            for (Method method : entry.getValue().getClass().getMethods()) {
                if (!method.isAnnotationPresent(JPRequestMapping.class)) {
                    continue;
                }
                JPRequestMapping methodAnnotation = method.getAnnotation(JPRequestMapping.class);
                if (annotation.value().startsWith("/")) {
                    url += methodAnnotation.value();
                } else {
                    url += ("/" + methodAnnotation.value());
                }

                String regex = url.replaceAll("/+", "/");
                Pattern pattern = Pattern.compile(regex);

                System.out.println("Mapping: " + regex + method.toString());
//                handlerMapping.put(url, new Handler(entry.getValue(), method, url));
                handlerMappingList.add(new Handler(entry.getValue(), method, pattern));
            }

        }

    }

    private void initHandlerAdapters(JPApplicationContext context) {
        // 适配器
        // 主要是用来动态匹配参数
        // 动态赋值
        if (handlerMappingList.isEmpty()) {
            return;
        }
        for (Handler handler : handlerMappingList) {
            // 参数类型作为key，参数的索引号作为值
            Map<String, Integer> paramMapping = new HashMap<>();
            Class<?>[] parameterTypes = handler.getMethod().getParameterTypes();
            // 参数是有顺序的，但是用反射，是没有办法拿到参数的名字的
            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> parameterType = parameterTypes[i];
                if (parameterType == HttpServletRequest.class || parameterType == HttpServletResponse.class) {
                    paramMapping.put(parameterType.getName(), i);
                }
            }
            Annotation[][] parameterAnnotations = handler.getMethod().getParameterAnnotations();
            Parameter[] parameters = handler.getMethod().getParameters();
            DefaultParameterNameDiscoverer discover = new DefaultParameterNameDiscoverer();
            for (int i = 0; i < parameterAnnotations.length; i++) {
                for (Annotation annotation : parameterAnnotations[i]) {
                    if (annotation instanceof JPRequestParam) {
                        String value = ((JPRequestParam) annotation).value();
                        if (!"".equals(value.trim())) {
                            paramMapping.put(value, i);
                        } else {
                            paramMapping.put(parameters[i].getName(), i);
                        }
                    } else {
                        paramMapping.put(parameters[i].getName(), i);
                    }
                }
            }
//            handlerhandlerAdapterMap.put(handler, new handlerAdapter(paramMapping));
            handlerhandlerAdapterList.add(new handlerAdapter(paramMapping, handler));
        }
    }

    private void initHandlerExceptionResolvers(JPApplicationContext context) {
        // 异常处理
    }

    private void initRequestToViewNameTranslator(JPApplicationContext context) {
        // 视图转发
    }

    private void initViewResolvers(JPApplicationContext context) {
        // 解析模板中的内容
        // 模板一般不会放在WebRoot下，而是放在Web-inf或者class下，这样避免了用户直接请求到模板文件
        String templateRoot = context.getConfig().getProperty("templateRoot");
        // 末班初始化，加载所有末班，存储到缓存中
        // 检查模板中的语法错误
        String rootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();
        this.listTemplate(rootPath);
    }

    private void listTemplate(String path) {
        File rootDir = new File(path);
        for (File file : rootDir.listFiles()) {
            if (file.isDirectory()) {
                this.listTemplate(file.getPath());
            } else {
                viewResolvers.add(new JPViewResolver(file.getName(), file.getPath()));
            }
        }
    }

    private void initFlashMapManager(JPApplicationContext context) {
        //
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            System.out.println("请求调用");
            this.doDispatch(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("500, 出错啦, Message: " + e.getMessage());
        }
    }

    private void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 找到url对应的类和方法
        Handler handler = getHandler(request);
        if (handler == null) {
            response.getWriter().write("404, 找不到啦");
        }
        // 在通过适配器执行调用
        handlerAdapter handlerAdapter = getHandlerAdapter(handler);
        ModelAndView mv = handlerAdapter.handler(request, response, handler);

        // 搞个模板框架
        // veloctiy #
        // freemark #
        // themlef #
        // jsp ${}
        // 啊蒋版的 @{}
        applyDefaultViewName(response, mv);
    }

    private void applyDefaultViewName(HttpServletResponse response, ModelAndView mv) throws Exception {
        // 找到模板文件
        if (mv == null) {
            return;
        }
        if (viewResolvers.isEmpty()) {
            return;
        }
        for (JPViewResolver viewResolver : viewResolvers) {
            if (mv.getView().equals(viewResolver.getViewName())) {
                String parse = viewResolver.parse(mv);
                if (parse != null) {
                    response.getWriter().write(parse);
                    break;
                }
            }
        }
    }

    private Handler getHandler(HttpServletRequest request) {
        // 遍历handlerMapping
        if (handlerMappingList.isEmpty()) {
            return null;
        }
        String url = request.getRequestURI();
        String contextPath = request.getContextPath();
        url = url.replace(contextPath, "").replaceAll("/+", "/");

        for (Handler handler : handlerMappingList) {
            Matcher matcher = handler.getUrl().matcher(url);
            if (matcher.matches()) {
                return handler;
            }
        }
//        return handlerMapping.get(url);
//        for (Map.Entry<String, Handler> entry : handlerMapping.entrySet()) {
//            if (url.equals(entry.getKey())){
//                return entry.getValue();
//            }
//        }
        return null;
    }

    private handlerAdapter getHandlerAdapter(Handler handler) {

        for (handlerAdapter handlerAdapter : handlerhandlerAdapterList) {
            if (handler.getUrl().equals(handlerAdapter.getHandler().url)) {
                return handlerAdapter;
            }
        }
        return null;
//        return handlerhandlerAdapterMap.get(handler);
    }


}

@Data
class Handler {

    protected Object controller;
    protected Method method;
    protected Pattern url;

    public Handler(Object controller, Method method, Pattern url) {
        this.controller = controller;
        this.method = method;
        this.url = url;
    }
}

@Data
class handlerAdapter {

    Map<String, Integer> paramMapping = new HashMap<>();
    Handler handler;

    public handlerAdapter(Map<String, Integer> paramMapping, Handler handler) {
        this.paramMapping = paramMapping;
        this.handler = handler;
    }

    public ModelAndView handler(HttpServletRequest request, HttpServletResponse response, Handler handler) throws InvocationTargetException, IllegalAccessException {
        // 为什么需要request、response、handler
        // 开始赋值的操作
        Class<?>[] parameterTypes = handler.getMethod().getParameterTypes();

        Object[] paramValueTemp = new Object[parameterTypes.length];
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> requestParam : parameterMap.entrySet()) {
            String value = Arrays.toString(requestParam.getValue()).replaceAll("\\[|\\]", "").replaceAll(",\\s", ",");
            if (!this.paramMapping.containsKey(requestParam.getKey())) {
                continue;
            }
            Integer index = this.paramMapping.get(requestParam.getKey());
            paramValueTemp[index] = this.castStringValue(value, parameterTypes[index]);
        }

        // 单独处理request/response
        if (this.paramMapping.containsKey(HttpServletRequest.class.getName())) {
            paramValueTemp[this.paramMapping.get(HttpServletRequest.class.getName())] = request;
        }
        if (this.paramMapping.containsKey(HttpServletResponse.class.getName())) {
            paramValueTemp[this.paramMapping.get(HttpServletResponse.class.getName())] = response;
        }

        boolean isModelAndView = handler.getMethod().getReturnType() == ModelAndView.class;
        Object result = handler.getMethod().invoke(handler.getController(), paramValueTemp);
        if (isModelAndView) {
            return (ModelAndView) result;
        }
        return null;
    }

    private Object castStringValue(String value, Class<?> clazz) {
        if (clazz == String.class) {
            return value;
        } else if (clazz == Integer.class) {
            return Integer.valueOf(value);
        } else if (clazz == int.class) {
            return Integer.valueOf(value).intValue();
        }
        return null;
    }

}

@Data
class JPViewResolver {

    private String viewName;
    private String path;

    protected JPViewResolver(String viewName) {
        this.viewName = viewName;
    }

    public JPViewResolver(String viewName, String path) {
        this.viewName = viewName;
        this.path = path;
    }

    protected String parse(ModelAndView mv) throws Exception {

        // 输出文件内容
        // 数据解析
        // 模板语法是很复杂的，但是原理都是一样的
        // 都是用正则表达式来处理字符串
        StringBuffer sb = new StringBuffer();
        RandomAccessFile ra = null;
        try {
            ra = new RandomAccessFile(new File(this.path), "r");
            String line;
            while (null != (line = ra.readLine())) {
                Matcher matcher = this.matcher(line);
                while (matcher.find()) {
                    // 找到语法符号
                    String paramName = matcher.group(1);
                    Object value = mv.getModel().get(paramName);
                    if (value != null) {
                        line = line.replaceAll("@\\{" + paramName + "\\}", value.toString());
                    }
                }
                sb.append(line);
            }
        } finally {
            if (ra != null) {
                ra.close();
            }
        }

        return sb.toString();
    }

    private Matcher matcher(String str) {
        String regex = "@\\{(.*?)\\}";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        return pattern.matcher(str);
    }

}

