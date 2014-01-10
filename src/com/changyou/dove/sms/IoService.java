package com.changyou.dove.sms;

import com.baiwu.dove.commonbean.SmsHeader;
import com.baiwu.dove.commonbean.SmsMessage;

public class IoService {
	
	public int formSmsMessage(SmsMessage sms, int smsType) {
		if(sms == null){
			sms = new SmsMessage();
		}
		SmsHeader header = new SmsHeader();
		header.setRequestID(smsType);
		header.setPacketLength(sms.toByteArray(sms.getBody()).length + SmsHeader.HeaderLength);
		header.setSequenceID(SmsHeader.getNextSeq());
		sms.setHeader(header);
		
		return 0;
	}

	

}
