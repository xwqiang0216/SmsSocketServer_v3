package com.baiwu.dove.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import com.baiwu.dove.commonbean.ChannelBean;
import com.baiwu.dove.commonbean.SmsMessage;
import com.baiwu.dove.util.PropUtil;

public class SmsSocketServer extends Thread{
	private boolean running = true;
	private final int port = PropUtil.getInt("serverPort");
	
	private SmsServer smsServer;
	
	public SmsSocketServer(SmsServer smsServer) {
		this.smsServer = smsServer;
	}

	public void run(){
		Selector selector = null;
		
		try{
			selector = Selector.open();
			
			ServerSocketChannel ssc = ServerSocketChannel.open();
			ssc.configureBlocking(false);
			ssc.register(selector, SelectionKey.OP_ACCEPT);
			ssc.socket().bind(new InetSocketAddress(port));
			
			System.out.println(" ---------- < server is start up! > ----------- ");
			
			while(running){
				int keyCount = selector.select();
				System.out.println(keyCount+"......keycount..................");
				if(keyCount > 0){
					Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
					SelectionKey key = null;
					
					while(keys.hasNext()){
						try{
							key = keys.next();
							if(key.attachment() == null){
								ChannelBean cb = new ChannelBean();
								cb.setSmsServer(smsServer);
								
								key.attach(cb);
							}
							
							if(key.isReadable()){
								SocketChannel socket = (SocketChannel)key.channel();
								
								ChannelBean channelBean = (ChannelBean)key.attachment();
								
								channelBean.read(socket);
								
								if(channelBean.isReady()){
//									System.out.println("读到一个完整的包");
									channelBean.getBodyBuffer().flip();
									
									SmsMessage smsMessage = new SmsMessage(channelBean.getSmsHeader(), channelBean.getBodyBuffer());
//									System.out.println("smsMessage = " + smsMessage);
									smsMessage.doSomething(socket, channelBean);
									
									channelBean.reset();
								}
							}
							
							if(key.isWritable()){
								ChannelBean channelBean = (ChannelBean)key.attachment();
								channelBean.doSomething(key);
							}
							
							if(key.isAcceptable()){
								
								ServerSocketChannel serverSocketChannel = (ServerSocketChannel)key.channel();
								SocketChannel socketChannel = serverSocketChannel.accept();
								socketChannel.configureBlocking(false);
								socketChannel.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ);
								
							}	
						}catch(Exception e){
							e.printStackTrace();
							try{
								key.cancel();
							}catch(Exception e1){
								e1.printStackTrace();
							}
							
							try{
								((SocketChannel)key.channel()).socket().close();
							}catch(Exception e2){
								e2.printStackTrace();
							}
							
							try{
								key.channel().close();
							}catch(Exception e2){
								e2.printStackTrace();
							}
						}finally{
							keys.remove();
						}
					}
					Thread.sleep(1);
				}else{
					Thread.sleep(1);
				}
			}
			ssc.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				selector.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
