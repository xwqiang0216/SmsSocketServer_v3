package com.baiwu.dove.commonbean;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

import com.baiwu.dove.dataCenter.ClientDataCenter;

public class SmsMessage {
	public static final int Submit = 0x00000001;
	public static final int Submit_Resp = 0x80000001;
	public static final int Report = 0x00000002;
	public static final int Report_Resp = 0x80000002;
	public static final int Deliver = 0x00000003;
	public static final int Deliver_Resp = 0x80000003;
	public static final int Query = 0x00000004;
	public static final int Query_Resp = 0x80000004;
	public static final int Active = 0x00000005;
	public static final int Active_Resp = 0x80000005;
	public static final int Login = 0x00000006;
	public static final int LoginResp = 0x80000006;
	
	private SmsHeader header;
	private Map<String, String> body;
	
	private static int responseCount = 0;
	
	public SmsMessage(SmsHeader header, ByteBuffer body){
		this.header = header; 
		this.body = toBodyMap(body);
	}
	
	public SmsMessage(){
		
	}

	@SuppressWarnings("unchecked")
	private Map<String, String> toBodyMap(ByteBuffer bufferBody) {
		Map<String, String> result = null;
		byte[] tmpArray = null;
		
		if(bufferBody != null){
			tmpArray = new byte[bufferBody.capacity()];
			bufferBody.get(tmpArray);
		}
		ByteArrayInputStream bais = new ByteArrayInputStream(tmpArray);
		try {
			ObjectInputStream ois = new ObjectInputStream(bais);
			result = (Map<String, String>)ois.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public byte[] toByteArray(Map<String, String> map){
		byte[] result = null;
		ByteArrayOutputStream baos = null;
		ObjectOutputStream dops = null;
		try {
			baos = new ByteArrayOutputStream();
			dops = new ObjectOutputStream(baos);
		
			dops.writeObject(map);
			dops.flush();
			result = baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				baos.close();
				dops.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	@Override
	public String toString() {
		return "SmsMessage [header=" + header + ", body=" + body + "]";
	}

	public void doSomething(SocketChannel socket, ChannelBean channelBean) throws Exception {
		switch(header.getRequestID()){
		
			case Submit : {dealSubmitMessageAndResponse(socket, channelBean);  break;}
			
			case Submit_Resp : {dealSubmitMessageResponse();  break;}
			
			case Report : {dealReportMessageAndResponse(socket); break; }
			
			case Report_Resp : {System.out.println("接收到状态报告接收响应"); break;}
			
			case Deliver : {dealDeliverMessageAndResponse(socket); break;}
			
			case Deliver_Resp : {System.out.println("接收到上行接收响应"); break;}
			
			case Active : {dealActiveMessageAndResponse(socket); break;}
			
			case Active_Resp : {deaActiveResp(channelBean); break;}
		
			case Login : {dealLoginMessageAndResponse(socket, channelBean); break;}
			
			case LoginResp : {dealLoginResp(channelBean); break;}
			
		}
	}

	private void dealLoginResp(ChannelBean channelBean) {
//		System.err.println("处理登录响应");
		if("0".equals(this.body.get("response"))){
			channelBean.setConnected(true);
		}
		dealSubmitMessageResponse();
	}

	private void dealLoginMessageAndResponse(SocketChannel socket, ChannelBean channelBean) throws Exception {
		System.out.println("接收到登录消息");
		Map<String, String> responseMap = new HashMap<String, String>();
		
		//System.out.println("接到提交信息, 做出快速响应处理并给出返回值");
		int response = channelBean.getSmsServer().onLoginMessage(this.getBody());
		
		responseMap.put("response", String.valueOf(response));
		createMessageAndWriteBack(socket, responseMap, SmsMessage.LoginResp);
		
		if(response == 0){
			UserBean userBean = formUserBean(this.getBody());
			channelBean.setUserBean(userBean);
		}else{
			throw new Exception("invalid user " + getBody() + " - response = " + response);
		}
	}

	private UserBean formUserBean(Map<String, String> loginBody) {
		UserBean result = new UserBean();

		result.setCorp_id(loginBody.get("corp_id"));
		result.setUser_id(loginBody.get("user_id"));
		result.setUser_pwd(loginBody.get("user_pwd"));
		
		return result;
	}

	private void dealSubmitMessageResponse() {
		SmsMessage sms = ClientDataCenter.getWaitResponseRecord(String.valueOf(this.getHeader().getSequenceID()));
		if(sms != null){
			for(String key : sms.getBody().keySet()){
				this.body.put(key, sms.getBody().get(key));
			}
			ClientDataCenter.addCallBackMessage(this);
		}else{
			System.err.println("无下发记录的提交响应：" + this);
		}
	}

	private void deaActiveResp(ChannelBean channelBean) {
//		System.out.println("接收到心跳响应, 未响应次数归零");
		channelBean.resetNoResponseActive();
	}

	private void dealDeliverMessageAndResponse(SocketChannel socket) {
//		System.out.println("接收到上行信息, 添加队列并返回响应");
		
		Map<String, String> responseMap = new HashMap<String, String>();
		
		boolean addSuccess = ClientDataCenter.addCallBackMessage(this);
		
		if(addSuccess){
			responseMap.put("response", "0:Success");
		}else{
			responseMap.put("response", "-1:QueueFull");
		}
		
		createMessageAndWriteBack(socket, responseMap, SmsMessage.Deliver_Resp);
	}

	private void dealReportMessageAndResponse(SocketChannel socket) {
//		System.out.println("接收到状态报告，添加队列并返回响应");
		
		Map<String, String> responseMap = new HashMap<String, String>();
		
		boolean addSuccess = ClientDataCenter.addCallBackMessage(this);
		
		if(addSuccess){
			responseMap.put("response", "0:Success");
		}else{
			responseMap.put("response", "-1:QueueFull");
		}
		
		createMessageAndWriteBack(socket, responseMap, SmsMessage.Report_Resp);
	}

	private void dealActiveMessageAndResponse(SocketChannel socket) {
		System.out.println("接收到心跳包, 构建心跳响应并返回");
		Map<String, String> responseMap = new HashMap<String, String>();
		
		responseMap.put("response", "0:Success");
		
		createMessageAndWriteBack(socket, responseMap, SmsMessage.Active_Resp);
	}

	private void dealSubmitMessageAndResponse(SocketChannel socket, ChannelBean channelBean) {
		Map<String, String> responseMap = new HashMap<String, String>();
		
		int response = 0;
		//System.out.println("接到提交信息, 做出快速响应处理并给出返回值");
		if(channelBean.getUserBean() != null){
			response = channelBean.getSmsServer().onSubmitMessage(this.getBody());
		}else{
			response = -1;
		}
		
		responseMap.put("response", String.valueOf(response));
		
		createMessageAndWriteBack(socket, responseMap, SmsMessage.Submit_Resp);
	}

	public void createMessageAndWriteBack(SocketChannel socket,
			Map<String, String> bodyMap, int smsType) {
		SmsMessage backMessage = createSmsMessage(bodyMap, smsType);
		
		ByteBuffer response = backMessage.getByteBuffer();
		
		try {
			socket.write(response);
			if(smsType == SmsMessage.Submit_Resp){
				System.out.println("write submit resp count = " + ++responseCount);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public SmsMessage createSmsMessage(Map<String, String> bodyMap, int smsType) {
		SmsMessage backMessage = new SmsMessage();
		backMessage.setBody(bodyMap);
		byte[] bodyArray = toByteArray(bodyMap);
		
		int packetlength = bodyArray.length + SmsHeader.HeaderLength;
		int requestID = smsType;
		int seq = 0;
		if(this.getHeader() == null){
			seq = SmsHeader.getNextSeq();
		}else{
			seq = this.getHeader().getSequenceID();
		}
		
		backMessage.setHeader(new SmsHeader(packetlength, requestID, seq));
		return backMessage;
	}

	public ByteBuffer getByteBuffer() {
		ByteBuffer result = null;
			
		result = ByteBuffer.allocate(this.header.getPacketLength());
		result.put(this.header.getHeaderByteArray());
		result.put(toByteArray(body));
		result.flip();
		
		return result;
	}

	public SmsHeader getHeader() {
		return header;
	}

	public void setHeader(SmsHeader header) {
		this.header = header;
	}

	public Map<String, String> getBody() {
		return body;
	}

	public void setBody(Map<String, String> body) {
		this.body = body;
	}
	
}
