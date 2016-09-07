package com.tindercupid.manager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class TinderCupidListener implements ServletContextListener {
	public void contextInitialized(ServletContextEvent arg0) {
		RemoteManager.getManager().load(arg0.getServletContext());
	}

	public void contextDestroyed(ServletContextEvent arg0) {
	}
}
