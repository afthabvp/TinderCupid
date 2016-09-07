package com.tindercupid.manager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import com.tindercupid.utils.SettingsLoader;

public class RemoteManager {


	private static Logger logger = Logger.getLogger(RemoteManager.class);
	private static RemoteManager manager = new RemoteManager();
	private static HashMap<String, String> map = new HashMap<>();


	private RemoteManager() {
	}

	private void init(ServletContext arg0) {
		
		map.put("home", arg0.getRealPath(""));
		
		try {

			if(startObjects())
			{

				logger.info("started, ");


			}
		} catch(Exception ex) {
			logger.error("load, ",ex);
		}
	}

	private boolean startObjects() throws ClassNotFoundException {
		boolean flag = false;

		Class.forName(ListManager.class.getName());
		flag = true;


		return flag;
	}




	public void load(ServletContext ctx) {
		manager.init(ctx);
	}

	public static RemoteManager getManager() {
		return manager;
	}


	public static HashMap<String, String> getMap() {
		return map;
	}

	// The application home path set and the remote device manager is loaded
	public static void main(String[] args) throws InterruptedException {
		RemoteManager.getManager().init(null);
	}
}
