package edu.jphoebe.demo.netty.catalina.servlets;


import edu.jphoebe.demo.netty.catalina.http.JPRequest;
import edu.jphoebe.demo.netty.catalina.http.JPResponse;
import edu.jphoebe.demo.netty.catalina.http.JPServlet;

public class FirstServlet extends JPServlet {


    @Override
    public void doGet(JPRequest request, JPResponse response) {
        doPost(request, response);
    }


    @Override
    public void doPost(JPRequest request, JPResponse response) {
        String param = "name";
        String str = request.getParameter(param);
        response.write(param + ":" + str, 200);
    }

}
