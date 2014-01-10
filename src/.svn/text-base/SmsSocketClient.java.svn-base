import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;



public class SmsSocketClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ByteBuffer headBuffer = ByteBuffer.allocate(8);
		ByteBuffer outBuffer = ByteBuffer.allocate(1024);
		
		boolean running = true;
		boolean isFirst = true;
		
		Selector selector = null;
		SelectionKey key = null;
		
		try {
			selector = Selector.open();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while(running){
			new HashMap<String, String>();
			
			try {
				if(isFirst){
					SocketChannel socketChannel = SocketChannel.open();
					socketChannel.configureBlocking(false);
					socketChannel.register(selector, SelectionKey.OP_CONNECT);
					socketChannel.connect(new InetSocketAddress("127.0.0.1", 9999));
					isFirst = false;
				}
				if(selector.select(1000) > 0){
					Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
					
					while(keys.hasNext()){
						try{
							key = keys.next();
							
							SocketChannel socketChannel = (SocketChannel)key.channel();
							
							if(key.isConnectable()){
								if(socketChannel.finishConnect()){
									System.out.println("... connected to server ...");
									socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
									continue;
								}
							}
							
							if(key.isReadable()){
								socketChannel.read(headBuffer);
								byte[] b = headBuffer.array();
								for(byte e : b){
									System.err.println(e);
								}
//								headBuffer.flip();	
								
//								System.out.println(headBuffer.get());
								headBuffer.flip();
								headBuffer.clear();
							}
							
							if(key.isWritable()){
								fillOutputData(outBuffer);
//								outBuffer.put(new byte[]{10,20,30,40,50});
								outBuffer.flip();
								socketChannel.write(outBuffer);
								outBuffer.clear();
							}
						}catch(Exception e){
							e.printStackTrace();
							key.channel().close();
							key.cancel();
						}finally{
							keys.remove();
						}
					}
					Thread.sleep(1000);
				}
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		

	}

	private static void fillOutputData(ByteBuffer outBuffer) {
		ByteArrayOutputStream ais = new ByteArrayOutputStream();
		Map<String, String> map = new HashMap<String, String>();
		map.put("1", "value1");
		map.put("2", "value2");
		map.put("3", "value3");
		map.put("4", "value4");
		
		ObjectOutputStream dops;
		try {
			dops = new ObjectOutputStream(ais);
			dops.writeObject(map);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		byte[] mapByte = ais.toByteArray();
		
		int packetLength = 8;
		
		if(mapByte != null){
			packetLength += mapByte.length;
		}
		
		outBuffer.putInt(packetLength);
		outBuffer.putInt(1);
		outBuffer.put(mapByte);
		
		System.out.println(outBuffer);
	}

	public static void getTransferData(){
		
	}
}
