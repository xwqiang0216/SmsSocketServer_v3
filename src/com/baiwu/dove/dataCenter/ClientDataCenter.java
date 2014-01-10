package com.baiwu.dove.dataCenter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

import com.baiwu.dove.commonbean.SmsMessage;
/**
 * 为客户端的交互信息提供缓冲队列
 * @author 1111182
 *
 */
public class ClientDataCenter {
	
	private static ArrayBlockingQueue<SmsMessage> SubmitMessageQueue = new ArrayBlockingQueue<SmsMessage>(2000);
	private static Map<String, SmsMessage> WaitSubmitRespQueue = new HashMap<String, SmsMessage>();
	private static ArrayBlockingQueue<SmsMessage> callBackQueue = new ArrayBlockingQueue<SmsMessage>(2000);
	
	public static long lastCheckTime = 0l;
	
	public static boolean addSubmitMessage(SmsMessage smsMessage){
		boolean result = false;
		try{
			if(smsMessage != null){
				result = SubmitMessageQueue.offer(smsMessage);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	public static boolean addSubmitMessage(List<SmsMessage> smsList){
		boolean result = false;
		try{
			if(smsList != null){
				result = SubmitMessageQueue.addAll(smsList);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	public static SmsMessage getSubmitMessage(){
		SmsMessage result = null;
		try{
			if(!SubmitMessageQueue.isEmpty()){
				result = SubmitMessageQueue.remove();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	public static synchronized boolean putWaitResponseRecord(SmsMessage sms){
		SmsMessage result = null;
		try{
			if(sms != null){
				result = WaitSubmitRespQueue.put(String.valueOf(sms.getHeader().getSequenceID()), sms);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result == null;
	}
	
	public static synchronized SmsMessage getWaitResponseRecord(String key){
		SmsMessage result = null;
		try{
			result = WaitSubmitRespQueue.remove(key);
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	public static synchronized int getWaitSubmitRespQueueSize(){
		return WaitSubmitRespQueue.size();
	}
	
	public static synchronized void checkTimeOutSubmit(){
		Iterator<SmsMessage> its = WaitSubmitRespQueue.values().iterator();
		while(its.hasNext()){
			SmsMessage tmpSms = its.next();
			if(System.currentTimeMillis() - Long.parseLong(tmpSms.getBody().get("submit_time")) > 30000){
				tmpSms.getBody().put("response", "-2");
				SmsMessage submitResp = tmpSms.createSmsMessage(tmpSms.getBody(), SmsMessage.Submit_Resp);
				ClientDataCenter.addCallBackMessage(submitResp);
				its.remove();
			}
		}
	}
	
	public static boolean addCallBackMessage(SmsMessage smsMessage){
		boolean result = false;
		try{
			if(smsMessage != null){
				result = callBackQueue.offer(smsMessage);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	public static boolean addCallBackMessage(List<SmsMessage> smsList){
		boolean result = false;
		try{
			if(smsList != null){
				result = callBackQueue.addAll(smsList);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	public static SmsMessage getCallBackMessage(){
		SmsMessage result = null;
		try{
			if(!callBackQueue.isEmpty()){
				result = callBackQueue.remove();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
}
