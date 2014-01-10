package com.baiwu.dove.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropUtil {
	
	private static Properties prop = new Properties();
	private static long last_load_time = 0L;
	
	private static void load(){
		InputStream is = null;
		try {
			is = new FileInputStream(new File(System.getProperty("user.dir")+System.getProperty("file.separator")+"SMSConfig.properties"));
			prop.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(is!=null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public static String get(String key){
		if(System.currentTimeMillis() - last_load_time > 60 * 1000){
			load();
			last_load_time = System.currentTimeMillis();
		}
		return prop.getProperty(key);
	}
	public static int getInt(String key){
		String val = get(key);
		return Integer.parseInt(val);
	}
	
	public static String propDetail(){
		return prop.toString();
	}
	
	public static void main(String[] args){
		PropUtil.load();
		String ss = PropUtil.get("serverIp");
		System.out.println(ss+".");
	}
}
