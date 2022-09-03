package com.surge.vms.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
 

 
public class CamundaIntegrator {
	public static void main(String args[]) throws Exception {
 
		String[] hostList = {"http://localhost:8080/engine-rest/engine"};
 
		for (int i = 0; i < hostList.length; i++) {
 
			String url = hostList[i];
			//getStatus(url);
			getServerStatus(url);
 
		}
 
		System.out.println("Task completed...");
	}
 
	public static int getStatus(String url) throws IOException {
 
		String result = "";
		String serverURL = "http://localhost:8080/engine-rest/engine";
		int code = 200;
		try {
			URL siteURL = new URL(serverURL);
			HttpURLConnection connection = (HttpURLConnection) siteURL.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(3000);
			connection.connect();
 
			code = connection.getResponseCode();
			
		} catch (Exception e) {
			result = "-> Red <-\t" + "Wrong domain - Exception: " + e.getMessage();
			code = 404;
			System.out.println(result);
 
		}
		
		return code;
	}
	
	public static boolean getServerStatus(String url) {
		 
		boolean serverStatus = false;
		System.out.println("url:"+url);
		int code = 200;
		try {
			URL siteURL = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) siteURL.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(3000);
			connection.connect();
 
			code = connection.getResponseCode();
			if(code == 200) {
				serverStatus = true;
			}
			
			
			
		} catch (Exception e) {
			String errMsg = "Wrong domain - Exception: " + e.getMessage();
			serverStatus = false;
			System.out.println(errMsg);
 
		}
		
		return serverStatus;
	}
 
}