package com.baiwu.dove.dataCenter;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

import com.baiwu.dove.commonbean.SmsMessage;

public class ServerDataCenter {
	private static HashMap<String, ArrayBlockingQueue<SmsMessage>> toClientMessageMap = new HashMap<String, ArrayBlockingQueue<SmsMessage>>();
	
	public static boolean putInToClientMessageMap(SmsMessage sms){
		boolean result = false;
		try{
			if(sms.getBody() != null){
				String key = sms.getBody().get("user_id");
				if(key == null){
					throw new Exception("dest user_id is null");
				}else{
					if(!toClientMessageMap.containsKey(key)){
						toClientMessageMap.put(key, new ArrayBlockingQueue<SmsMessage>(2000));
					}
					result = toClientMessageMap.get(key).offer(sms);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	public static SmsMessage getToClientMessage(String key){
		SmsMessage result = null;
		try{
			if(!toClientMessageMap.isEmpty() && toClientMessageMap.containsKey(key) && !toClientMessageMap.get(key).isEmpty()){
				result = toClientMessageMap.get(key).poll();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
}
