package com.tindercupid.manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedList;

import org.apache.log4j.Logger;

public class ListManager {
	private static ListManager instance = null;
	private static HashMap<String,LinkedList<String>> listMap = null;
	private static Object lockInstance = new Object();
	private static Logger logger = Logger.getLogger(ListManager.class);




	private ListManager() {
	}

	public void listLoader(){
		File folder = null;
		BufferedReader br = null;
		listMap = new HashMap<>();
		try{
			String home = RemoteManager.getMap().get("home");
			folder = new File(home.concat(File.separator)+"file");
			File[] listOfFiles = folder.listFiles();
			for (File file : listOfFiles) {
				LinkedList<String> linklist = new LinkedList<>();
				if (file.isFile()) {
					String sCurrentLine  = null;
					br = new BufferedReader(new FileReader(file));
					while ((sCurrentLine = br.readLine()) != null) {
						linklist.add(sCurrentLine);

					}
					listMap.put(file.getName(), linklist);
				}
			}
		} catch (Exception e) {
			System.out.println("Excepyion in File Reading : " +e.getMessage());
			logger.info("Excepyion in File Reading : " +e.getMessage());

		}

	}


public static  ListManager getInstance() {
	if (instance == null) {
		Object object = lockInstance;
		synchronized (object) {
			logger.debug((Object)"getProperty, inside the singleton function for the first time");
			if (instance == null) {
				instance = new ListManager();
				instance.listLoader();
			}
		}
	}
	return instance;
}

public HashMap<String, LinkedList<String>> getListMap() {
	return listMap;
}



}
