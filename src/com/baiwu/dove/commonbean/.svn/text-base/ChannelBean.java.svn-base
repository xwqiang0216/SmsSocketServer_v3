package com.baiwu.dove.commonbean;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
//import java.util.Map;1
import java.util.HashMap;
import java.util.Map;

import com.baiwu.dove.dataCenter.ServerDataCenter;
import com.baiwu.dove.server.SmsServer;


public class ChannelBean {
	
	private ByteBuffer headerBuffer = ByteBuffer.allocate(SmsHeader.HeaderLength);
	
	private ByteBuffer bodyBuffer;
	
	private SmsHeader smsHeader;
	
	private boolean isConnected = false;
	
	private long lastActiveTime = 0;
	
	private long noRespActiveCount = 0;
	
	private SmsServer smsServer;
	
	private UserBean userBean;
	
//	private Map<String, String> bodyMap;
	
	public void read(SocketChannel socketChannel) throws Exception{
		if(headerBuffer.hasRemaining() && socketChannel.read(headerBuffer) == -1){
			//头还未读完，channel断掉了（到达流末尾）
			throw new Exception("invalid header packet length");
		}else if (!headerBuffer.hasRemaining() && bodyBuffer == null){
			headerBuffer.flip();
			smsHeader = new SmsHeader(headerBuffer);
			
			if(smsHeader.getPacketLength() > 65536){
				//有可能会引起后续的内存溢出问题
				throw new Exception("invalid packet length : " + smsHeader.getPacketLength());
			}
			bodyBuffer = ByteBuffer.allocate(smsHeader.getPacketLength() - SmsHeader.HeaderLength);
		}
		
		if(bodyBuffer.hasRemaining() && socketChannel.read(bodyBuffer) == -1){
			throw new Exception("invalid body packet length");
		}
	}
	
	public boolean isReady(){
		boolean result = false;
		if(bodyBuffer != null && !bodyBuffer.hasRemaining()){
			result = true;
		}
		return result;
	}
	
	public void reset(){
		headerBuffer.clear();
		bodyBuffer = null;
	}

	@SuppressWarnings("unchecked")
	public Map<String,String> getSmsBody(){
		InputStream input = null;
		ObjectInputStream oi = null;
		Map<String, String> body = null;
		try {
			input = new ByteArrayInputStream(bodyBuffer.array());  
			oi = new ObjectInputStream(input);  
			body = (Map<String,String> )oi.readObject();
			bodyBuffer.clear();  
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				input.close();  
				oi.close();
			} catch (IOException e) {
				e.printStackTrace();
			}  
			
		}
		return body;  
	}
	
	public void doSomething(SelectionKey key) throws Exception{
		doDisconnent();
		
		doSendDliver(key);
		
		doSendActiveMessage(key);
		
	}
	
	private void doDisconnent() throws Exception{
		if(this.noRespActiveCount >= 3){
			throw new Exception("no active response more than 3 times!");
		}
		
	}

	private void doSendActiveMessage(SelectionKey key) {
		long time = System.currentTimeMillis();
		if(time - lastActiveTime > 1000 * 30){
			//write active sms
			Map<String, String> bodyMap = new HashMap<String, String>();
			bodyMap.put("active_time", String.valueOf(time));
			
			new SmsMessage().createMessageAndWriteBack((SocketChannel)key.channel(), bodyMap, SmsMessage.Active);
			lastActiveTime = time;
			noRespActiveCount++;
		}
	}

	private void doSendDliver(SelectionKey key) throws Exception{
		if(userBean != null){
			SmsMessage sms = ServerDataCenter.getToClientMessage(userBean.getUser_id());
			if(sms != null){
				ByteBuffer byteBuffer = sms.getByteBuffer();
				SocketChannel socket = (SocketChannel)key.channel();
				socket.write(byteBuffer);
			}
		}
	}

	public void resetNoResponseActive(){
		this.noRespActiveCount = 0;
	}
	
	public SmsHeader getSmsHeader() {
		return smsHeader;
	}

	public void setSmsHeader(SmsHeader smsHeader) {
		this.smsHeader = smsHeader;
	}

	public ByteBuffer getHeaderBuffer() {
		return headerBuffer;
	}

	public void setHeaderBuffer(ByteBuffer headerBuffer) {
		this.headerBuffer = headerBuffer;
	}

	public ByteBuffer getBodyBuffer() {
		return bodyBuffer;
	}

	public void setBodyBuffer(ByteBuffer bodyBuffer) {
		this.bodyBuffer = bodyBuffer;
	}

	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}

	public boolean isConnected() {
		return isConnected;
	}

	public void setSmsServer(SmsServer smsServer) {
		this.smsServer = smsServer;
	}

	public SmsServer getSmsServer() {
		return smsServer;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	public UserBean getUserBean() {
		return userBean;
	}
	
	public long getNoRespActiveCount() {
		return noRespActiveCount;
	}
	
	public void setNoRespActiveCount(long noRespActiveCount) {
		this.noRespActiveCount = noRespActiveCount;
	}
}
