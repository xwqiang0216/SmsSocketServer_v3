package com.baiwu.dove.client;

import java.util.Map;

import com.baiwu.dove.commonbean.SmsMessage;
import com.baiwu.dove.dataCenter.ClientDataCenter;

public class CallBackDealThread extends Thread {
	private boolean isRunnable = false;
	private SmsClient smsClient;
	
	public CallBackDealThread(SmsClient smsClient){
		this.isRunnable = true;
		this.smsClient = smsClient;
	}
	
	public void run(){
		while(isRunnable){
			try{
				SmsMessage sms = ClientDataCenter.getCallBackMessage();
				if(sms != null){
//					System.err.println(sms.getHeader());
					switch(sms.getHeader().getRequestID()){
						
						case SmsMessage.Report : dealReport(sms); break;
						
						case SmsMessage.Deliver : dealDeliver(sms); break;
						
						case SmsMessage.Submit_Resp : dealSubmitResp(sms); break;
						
						case SmsMessage.LoginResp : dealSubmitResp(sms); break;
						
						default : break;
					}
				}else{
					Thread.sleep(1000);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	private void dealSubmitResp(SmsMessage sms) {
		Map<String, String> body = sms.getBody();
		
		if(body != null){
			smsClient.onSubmitResp(body);
		}
	}

	private void dealDeliver(SmsMessage sms) {
		Map<String, String> body = sms.getBody();
		if(body != null){
			String channelNum = body.get("channelNum");
			String phoneNum = body.get("phoneNum");
			String content = body.get("content");
			
			smsClient.onReceive(channelNum, phoneNum, content);
		}
		
	}

	private void dealReport(SmsMessage sms) {
		Map<String, String> body = sms.getBody();
		if(body != null){
			String channelNum = body.get("channelNum");
			String phoneNum = body.get("phoneNum");
			Long msgId = Long.parseLong(body.get("msgId"));
			Integer result = Integer.parseInt(body.get("result"));
			
			smsClient.onFeedback(channelNum, phoneNum, msgId, result);
		}
	}
}
