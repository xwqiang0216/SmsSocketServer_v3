package com.baiwu.dove.client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

import com.baiwu.dove.commonbean.SmsMessage;
import com.baiwu.dove.dataCenter.ClientDataCenter;
import com.baiwu.dove.util.PropUtil;
import com.changyou.dove.sms.IoService;
import com.changyou.dove.sms.Messenger;


public class SmsClient implements Messenger {
	protected IoService ioService;
	private CallBackDealThread callBackDealThread;
	private int count = 0;
	
	public void doStart(){
		//初始化IoSocketClient
		new SmsSocketClient(this);
		callBackDealThread = new CallBackDealThread(this);
		callBackDealThread.start();
	}
	
	public void doStop(){
		//关闭IoSerice
	}
	
	public SmsClient(){
		ioService = new IoService();
	}
	
	public void sendLoginMessage(SocketChannel socketChannel) {
		SmsMessage sms = new SmsMessage();
		Map<String,String> map = new HashMap<String,String>();
		
		addExtMessage(map);
		
		sms.setBody(map);
		
		ioService.formSmsMessage(sms, SmsMessage.Login);
		
		
		ByteBuffer outBuffer = sms.getByteBuffer();
		System.out.println("Login ... " + outBuffer);
		try {
			socketChannel.write(outBuffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		outBuffer.clear();
		ClientDataCenter.putWaitResponseRecord(sms);
//		ClientDataCenter.addSubmitMessage(sms);
	}
	
	@Override
	public int sendMessage(String channelNum, String phoneNum, String content, long msgId) {
		int result = 0;
		
		SmsMessage sms = new SmsMessage();
		Map<String,String> map = new HashMap<String,String>();
		
		map.put("channelNum", channelNum);
		map.put("phoneNum", phoneNum);
		map.put("content", content);
		map.put("msgId", String.valueOf(msgId));
		
		addExtMessage(map);
		
		sms.setBody(map);
		
		ioService.formSmsMessage(sms, SmsMessage.Submit);
		
		if(!ClientDataCenter.addSubmitMessage(sms)){
			result = -1;
		}
		return result;
	}

	

	@Override
	public void onFeedback(String channelNum, String phoneNum, long msgId,
			int result) {
		System.out.println("状态报告回调函数执行");

	}

	@Override
	public void onReceive(String channelNum, String phoneNum, String content) {
//		System.out.println(channelNum+phoneNum+content+"******************************");
//		System.out.println("上行回调函数执行");

	}

	@Override
	public long queryAccount(String username, String password) {
		long balance = 0;
//		HttpClient httpclient =new DefaultHttpClient();
//		InputStream in = null;  
//        try {
//            //构造一个post对象
//         HttpPost httpPost = new HttpPost(PropUtil.get("balance_url"));
//         //添加所需要的post内容
//         List<NameValuePair> nvps = new ArrayList<NameValuePair>();
//         nvps.add(new BasicNameValuePair("corp_id", PropUtil.get("corp_id")));
//         nvps.add(new BasicNameValuePair("user_id", PropUtil.get("user_id")));
//         nvps.add(new BasicNameValuePair("pwd", PropUtil.get("user_pwd")));
//         
//         httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
//
//         HttpResponse response = httpclient.execute(httpPost);
//         HttpEntity entity = response.getEntity();
//        
//         if (entity != null) {  
//             entity = new BufferedHttpEntity(entity);  
//             in = entity.getContent();  
//             byte[] read = new byte[1024];  
//             byte[] all = new byte[0];  
//             int num;  
//             while ((num = in.read(read)) > 0) {  
//                 byte[] temp = new byte[all.length + num];  
//                 System.arraycopy(all, 0, temp, 0, all.length);  
//                 System.arraycopy(read, 0, temp, all.length, num);  
//                 all = temp;  
//             }  
//             String result = new String(all);
//             if(result.trim().startsWith("ok:")){
//            	 balance = Long.parseLong(result.substring(result.indexOf("ok:") + 3));
//             }
//         }  
//     } catch (ClientProtocolException e) {
//		e.printStackTrace();
//	} catch (IOException e) {
//		e.printStackTrace();
//	} finally {  
//         if (in != null)
//			try {
//				in.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}  
//     }
		return balance;
	}
	
	private void addExtMessage(Map<String,String> map){
		if(map != null){
			map.put("corp_id", PropUtil.get("corp_id"));
			map.put("user_id", PropUtil.get("user_id"));
			map.put("user_pwd", PropUtil.get("user_pwd"));
			map.put("service_code", PropUtil.get("service_code"));
		}
	}

	public void onSubmitResp(Map<String, String> map) {
		if(map.get("response").equals("-2")){
			System.out.println("收到服务端返回的响应 = " + map.get("response") + "  根据成功失败作出相应处理, 接收数量 = " + ++count + " -- " + ClientDataCenter.getWaitSubmitRespQueueSize());
		}
		
	}
	
	
}
