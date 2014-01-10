

import com.baiwu.dove.commonbean.SmsMessage;
import com.changyou.dove.sms.IoService;

public class IoServiceProxy{
	private IoService ioService;
	public IoServiceProxy(){
		this.ioService = new IoService();
	}
	
	public int formSendMessage(SmsMessage sms) {
		int status = 0;
		ioService.formSmsMessage(sms, SmsMessage.Submit);
		return status;
	}
}
