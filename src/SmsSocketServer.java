import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;


public class SmsSocketServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		boolean running = true;
		
		Selector selector = null;
		
		try{
			selector = Selector.open();
			
			ServerSocketChannel ssc = ServerSocketChannel.open();
			ssc.configureBlocking(false);
			ssc.register(selector, SelectionKey.OP_ACCEPT);
			ssc.socket().bind(new InetSocketAddress(9999));
			
			System.out.println("server is start up!");
			
			while(running){
				int keyCount = selector.select();
				
				if(keyCount > 0){
					Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
					SelectionKey key = null;
					
					while(keys.hasNext()){
						try{
							key = keys.next();
							
							if(key.attachment() == null){
								key.attach(new ChannelBean());
							}
							
							if(key.isReadable()){
								SocketChannel socket = (SocketChannel) key.channel();
								
								ChannelBean channelBean = (ChannelBean)key.attachment();
								
								channelBean.read(socket);
								
								if(channelBean.isReady()){
									System.out.println("读到一个完整的包");
									System.out.println("header = " + channelBean.getSmsHeader());
									channelBean.reset();
								}
							}
							
							if(key.isWritable()){
								
							}
							
							if(key.isAcceptable()){
								
								ServerSocketChannel serverSocketChannel = (ServerSocketChannel)key.channel();
								SocketChannel socketChannel = serverSocketChannel.accept();
								socketChannel.configureBlocking(false);
								socketChannel.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ);
								
							}	
						}catch(Exception e){
							e.printStackTrace();
							key.cancel();
						}finally{
							keys.remove();
						}
					}
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
