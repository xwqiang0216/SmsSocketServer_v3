package com.baiwu.dove.commonbean;
import java.nio.ByteBuffer;


public class SmsHeader {
	public static final int HeaderLength = 12;
	public static int seq = 0;
	
	private int packetLength;
	private int requestID;
	private int sequenceID;
	
	public SmsHeader(){
		
	}
	
	public SmsHeader(int packetLength, int requestID, int sequenceID){
		this.packetLength = packetLength;
		this.requestID = requestID;
		this.sequenceID = sequenceID;
	}
	
	public SmsHeader(ByteBuffer headerBuffer){
		if(headerBuffer.hasRemaining()){
			this.packetLength = headerBuffer.getInt();
			this.requestID = headerBuffer.getInt();
			this.sequenceID = headerBuffer.getInt();
		}
	}
	
	public static int getNextSeq() {
		seq++;
		if(seq >= Integer.MAX_VALUE){
			seq = Integer.MIN_VALUE;
		} 
		return seq;
	}
	
	public byte[] getHeaderByteArray(){
		ByteBuffer buffer = ByteBuffer.allocate(HeaderLength);
		buffer.putInt(packetLength);
		buffer.putInt(requestID);
		buffer.putInt(sequenceID);
		buffer.flip();
		
		return buffer.array();
	}
	
	@Override
	public String toString() {
		return "SmsHeader [packetLength=" + packetLength + ", requestID="
				+ requestID + ", sequenceID=" + sequenceID + "]";
	}

	public int getPacketLength() {
		return packetLength;
	}

	public void setPacketLength(int packetLength) {
		this.packetLength = packetLength;
	}

	public int getRequestID() {
		return requestID;
	}

	public void setRequestID(int requestID) {
		this.requestID = requestID;
	}

	public int getSequenceID() {
		return sequenceID;
	}

	public void setSequenceID(int sequenceID) {
		this.sequenceID = sequenceID;
	}
	
}
