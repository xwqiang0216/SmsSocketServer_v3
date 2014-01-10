package com.baiwu.dove.server;

import java.util.HashMap;
import java.util.Map;

import com.baiwu.dove.commonbean.SmsMessage;
import com.baiwu.dove.dataCenter.ServerDataCenter;
import com.changyou.dove.sms.ISmsServer;
import com.changyou.dove.sms.IoService;

public class SmsServer implements ISmsServer {
	protected IoService ioService;
	private SmsSocketServer socketServer = null;
	protected int sendCount = 0;
	
	public void doStart(){
		ioService = new IoService();
		socketServer = new SmsSocketServer(this);
		socketServer.start();
	}

	@Override
	public int onSubmitMessage(Map<String, String> map) {
		System.out.println("服务端接收到提交信息的总数：" + ++sendCount);
		return 0;
	}
	
	public int sendReport(Map<String, String> reportMap) {
		int sendResult = 0;

		SmsMessage sms = new SmsMessage();

		sms.setBody(reportMap);

		this.ioService.formSmsMessage(sms, 2);

		if (!ServerDataCenter.putInToClientMessageMap(sms)) {
			sendResult = -1;
		}
		return sendResult;
	}

	public int sendDeliver(Map<String, String> deliverMap) {
		int sendResult = 0;

		SmsMessage sms = new SmsMessage();
		sms.setBody(deliverMap);

		this.ioService.formSmsMessage(sms, 3);

		if (!ServerDataCenter.putInToClientMessageMap(sms)) {
			sendResult = -1;
		}
		return sendResult;
	}

	public int sendDeliver(String channelNum, String phoneNum, String content) {
		int sendResult = 0;
		
		SmsMessage sms = new SmsMessage();
		Map<String,String> map = new HashMap<String,String>();
		
		map.put("channelNum", channelNum);
		map.put("phoneNum", phoneNum);
		map.put("content", content);
		
		sms.setBody(map);
		
		ioService.formSmsMessage(sms, SmsMessage.Deliver);
		
		if(!ServerDataCenter.putInToClientMessageMap(sms)){
			sendResult = -1;
		}
		return sendResult;
	}

	@Override
	public long queryAccount(String username, String password) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int onLoginMessage(Map<String, String> body) {
		System.out.println("校验用户信息，成功返回0，失败返回其他 < 0");
		return 0;
	}
}
