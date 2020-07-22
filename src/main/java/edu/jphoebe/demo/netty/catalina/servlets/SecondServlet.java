package edu.jphoebe.demo.netty.catalina.servlets;


import com.alibaba.fastjson.JSON;
import edu.jphoebe.demo.netty.catalina.http.JPRequest;
import edu.jphoebe.demo.netty.catalina.http.JPResponse;
import edu.jphoebe.demo.netty.catalina.http.JPServlet;

public class SecondServlet extends JPServlet {

	@Override
	public void doGet(JPRequest request, JPResponse response) {
		doPost(request, response);
	}

	@Override
	public void doPost(JPRequest request, JPResponse response) {
		String str = JSON.toJSONString(request.getParameters(), true);
		response.write(str, 200);
	}

}
