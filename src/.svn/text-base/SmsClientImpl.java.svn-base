import com.baiwu.dove.client.SmsClient;
import com.changyou.dove.sms.Messenger;


public class SmsClientImpl extends SmsClient implements Messenger{
	public void send(int msgType){
		sendMessage("dsj", "sdklf", "fdsk", 232432);
	}
	
	@Override
	public void onFeedback(String channelNum, String phoneNum, long msgId,
			int result) {
		/*
		 * 状态报告可以用来匹配 、 存储
		 */
		System.out.println("入库");
		super.onFeedback(channelNum, phoneNum, msgId, result);
	}

	@Override
	public void onReceive(String channelNum, String phoneNum, String content) {
		/*
		 * 上行数据根据内容进行相关操作
		 */
		System.out.println("入库");
		super.onReceive(channelNum, phoneNum, content);
	}

}
