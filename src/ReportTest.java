import java.util.HashMap;
import java.util.Map;

import com.baiwu.dove.commonbean.SmsHeader;
import com.baiwu.dove.commonbean.SmsMessage;

public class ReportTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SmsMessage report = new SmsMessage();
		
		Map<String, String> reportBody = new HashMap<String, String>();
		reportBody.put("channelNum", "10690069");
		reportBody.put("phoneNum", "13401175123");
		reportBody.put("msgId", "123456789");
		reportBody.put("result", "0");
		
		byte[] bodyBuffer = report.toByteArray(reportBody);
		
		SmsHeader header = new SmsHeader(bodyBuffer.length + SmsHeader.HeaderLength, SmsMessage.Report, 999);
		
		report.setHeader(header);
		report.setBody(reportBody);
		
	}

}
