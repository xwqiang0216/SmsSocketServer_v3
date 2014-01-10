import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
//import java.util.Map;1

import com.baiwu.dove.commonbean.SmsHeader;


public class ChannelBean {
	
	private ByteBuffer headerBuffer = ByteBuffer.allocate(8);
	
	private ByteBuffer bodyBuffer;
	
	private SmsHeader smsHeader;
	
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
			//8 为头的规定长度
			bodyBuffer = ByteBuffer.allocate(smsHeader.getPacketLength() - 8);
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

	public SmsHeader getSmsHeader() {
		return smsHeader;
	}

	public void setSmsHeader(SmsHeader smsHeader) {
		this.smsHeader = smsHeader;
	}
	
	
}
