package com.baiwu.dove.server;


public class MainServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SmsServer server = new SmsServer();
		server.doStart();
//		for(int i = 0; i < 100000000; i++)
//		{
//			Map<String, String> reportBody = new HashMap<String, String>();
//			
//			reportBody.put("user_id", "sohu" + String.valueOf(i % 2));
//			reportBody.put("channelNum", "10690069");
//			reportBody.put("phoneNum", "13401175123");
//			reportBody.put("msgId", "123456789");
//			reportBody.put("result", "0");
//			
//			int result = server.sendReport(reportBody);
//			if(result < 0){
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		}
	}
}
