package com.baiwu.dove.client;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import com.baiwu.dove.commonbean.ChannelBean;
import com.baiwu.dove.commonbean.SmsMessage;
import com.baiwu.dove.dataCenter.ClientDataCenter;
import com.baiwu.dove.util.PropUtil;

public class SmsSocketClient implements Runnable {
	private volatile boolean running = true;
	private boolean connected = false;
//	private int connCount = 0;
	
	private SmsClient smsClient;
	
	private SocketChannel socketChannel;
	private Selector selector;
	private SelectionKey key;
	
	private int count = 0;

	public SmsSocketClient(SmsClient smsClient) {
		this.smsClient = smsClient;
		new Thread(this).start();
	}
	
	public void start(){
		running = true;
	}
	public void stop(){
		running = false;
	}
	@Override
	public void run() {
		while (running) {
			try {
				
				if(!connected){
					selector = Selector.open();
					socketChannel = SocketChannel.open();
					socketChannel.configureBlocking(false);
					socketChannel.register(selector, SelectionKey.OP_CONNECT);
					socketChannel.connect(new InetSocketAddress(PropUtil.get("serverIp"), PropUtil.getInt("serverPort")));
				}
//				if(connCount < 5){
//					selector = Selector.open();
//					socketChannel = SocketChannel.open();
//					socketChannel.configureBlocking(false);
//					socketChannel.register(selector, SelectionKey.OP_CONNECT);
//					socketChannel.connect(new InetSocketAddress(PropUtil.get("serverIp"), PropUtil.getInt("serverPort")));
//					connCount++;
//				}
					
				if (selector.select() > 0 && selector.isOpen()) {
					Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

					while (keys.hasNext()) {
						try {
							key = keys.next();
							SocketChannel socketChannel = (SocketChannel) key.channel();
							
							if (key.isConnectable()) {
								if (socketChannel.finishConnect()) {
									System.out.println("... connected to server ...");
									socketChannel.register(selector,SelectionKey.OP_READ | SelectionKey.OP_WRITE);
									connected = true;
									continue;
								}
							}
							
							ChannelBean channelBean = (ChannelBean)key.attachment();
							
							if (key.isReadable() && channelBean != null) {
								channelBean.read(socketChannel);
								
								if(channelBean.isReady()){
									channelBean.getBodyBuffer().flip();
									
									SmsMessage smsMessage = new SmsMessage(channelBean.getSmsHeader(), channelBean.getBodyBuffer());
//									System.out.println("[Client 读到一个完整的包 " + smsMessage + "]");
									smsMessage.doSomething(socketChannel, channelBean);
									
									channelBean.reset();
								}
							}

							if (key.isWritable()) {
								if(channelBean == null){
									
									smsClient.sendLoginMessage(socketChannel);
									key.attach(new ChannelBean());
									
								}else if(channelBean.isConnected()){
									
									SmsMessage sms = ClientDataCenter.getSubmitMessage();
									if(sms != null){
										ByteBuffer outBuffer = sms.getByteBuffer();
//										System.out.println("client write...====================" + outBuffer);
										socketChannel.write(outBuffer);
										outBuffer.clear();
//										System.out.println("write count = " + count++);
										sms.getBody().put("submit_time", String.valueOf(System.currentTimeMillis()));
										ClientDataCenter.putWaitResponseRecord(sms);
									}
								} 
							}
						} catch (Exception e) {
							e.printStackTrace();
							connected = false;
							key.channel().close();
							key.cancel();
							try {
								Thread.sleep(2000);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
						} finally {
							keys.remove();
						}
					}
					Thread.sleep(1);
				}else{
					Thread.sleep(1000);
				}
				
				dealTimeOutSubmitMessage(30 * 1000);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
	}

	private void dealTimeOutSubmitMessage(int gap) {
		if(System.currentTimeMillis() - ClientDataCenter.lastCheckTime > gap){
			ClientDataCenter.checkTimeOutSubmit();
			ClientDataCenter.lastCheckTime = System.currentTimeMillis();
		}
	}
}
