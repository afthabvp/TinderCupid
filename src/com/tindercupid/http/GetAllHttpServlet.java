package com.tindercupid.http;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.tindercupid.manager.ListManager;

public class GetAllHttpServlet extends javax.servlet.http.HttpServlet implements Servlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(GetAllHttpServlet.class);
	public GetAllHttpServlet()
	{
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		try {

			String addr   = request.getRemoteHost();
			String src = "http://" + addr;
			logger.info("Address :"+ src);
			HashMap<String, LinkedList<String>> res = ListManager.getInstance().getListMap();
			Gson gson = new Gson(); 
			String json = gson.toJson(res); 
			PrintWriter writer = response.getWriter();
			if(writer != null){
				response.getWriter().write(json.toString());
				response.setHeader("Connection", "Close");
				logger.debug("Send response OK");
				writer.close();
			}
			
		} catch (Exception e) {
			logger.error("Exception in HttpServlet doPost",e);
		}
		finally{
			logger.debug("inside finally, HttpServlet");
		}
	}
}
