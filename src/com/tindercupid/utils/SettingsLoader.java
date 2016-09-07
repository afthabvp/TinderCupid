package com.tindercupid.utils;


/**
 * 
 * @author afthab
 * 8-FEB-2016
 */
import java.io.File;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.tindercupid.manager.RemoteManager;


public class SettingsLoader {
	private static Logger logger = Logger.getLogger(SettingsLoader.class.getName());
	private static SettingsLoader instance = null;
	private static Object lockInstance = new Object();
	private Hashtable<String, String> nodemap = null;

	private SettingsLoader() {

		String home = RemoteManager.getMap().get("home");
		String configPath = home.concat(File.separator)+"config"+File.separator+"config.xml";
		if (configPath != null) {
			logger.info((Object)("Loading configuration file [ " + configPath + " ]"));
			logger.debug((Object)"SettingsLoader, calling init method");
			this.init(configPath);
		}

	}

	private void init(String file) {
		this.loadXml(file);
	}

	private String namespace(Node node) {
		String namespace = "";
		Node parent = node.getParentNode();
		Stack<String> collection = new Stack<String>();
		boolean wasIn = false;
		try {
			while (parent != null && !parent.getNodeName().equals("#document")) {
				collection.push(parent.getNodeName());
				parent = parent.getParentNode();
			}
			while (!collection.isEmpty()) {
				namespace = String.valueOf(namespace) + (String)collection.pop() + ".";
				wasIn = true;
			}
			if (wasIn) {
				namespace = namespace.substring(0, namespace.length() - 1);
			}
		}
		catch (Exception ex) {
			logger.error((Object)"namespace, ", (Throwable)ex);
		}
		logger.debug((Object)("namespace, the name space : " + namespace));
		return namespace;
	}

	private Hashtable<String, String> index(Document xml) {
		logger.debug((Object)"index, about to index the the leaf nodes of the xml");
		Hashtable<String, String> map = new Hashtable<String, String>();
		Element root = xml.getDocumentElement();
		LinkedList<Node> queue = new LinkedList<Node>();
		queue.add(root);
		while (!queue.isEmpty()) {
			Node node = (Node)queue.remove();
			int nChilds = node.getChildNodes().getLength();
			int i = 0;
			while (i < nChilds) {
				Node child = node.getChildNodes().item(i);
				if (!child.getNodeName().equals("#text")) {
					queue.add(child);
				}
				if (child.getParentNode() != null && child.getChildNodes().getLength() == 0 && child.getParentNode().getChildNodes().getLength() == 1) {
					String namespc = this.namespace(child);
					String val = child.getNodeValue();
					logger.debug((Object)("index, Hash key : " + namespc + " Value : " + val));
					map.put(namespc, val);
				}
				++i;
			}
		}
		return map;
	}

	private void loadXml(String file) {
		try {
			logger.debug((Object)"loadXml, loading xml from file and indexing it");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document xml = builder.parse(new File(file));
			this.nodemap = this.index(xml);
		}
		catch (Exception e) {
			logger.error((Object)"loadXml, ", (Throwable)e);
		}
	}

	public static String getProperty(String key) {
		String value = null;
		if (instance == null) {
			Object object = lockInstance;
			synchronized (object) {
				logger.debug((Object)"getProperty, inside the singleton function for the first time");
				if (instance == null) {
					instance = new SettingsLoader();
				}
			}
		}
		logger.debug((Object)("getProperty, getting the value for key : " + key));
		value = instance.property(key);
		return value;
	}




	public String property(String key) {
		return this.nodemap.get(key);
	}
}

