
package com.baiwu.dove.client;


public class ClientTest {

//	public static long getMsgId(){
//		String msgId =  Long.toString(System.currentTimeMillis())+(int)(Math.random()*10000);
//		return Long.parseLong(msgId);
//	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		SmsClient client = new SmsClient();
		client.doStart();
//		System.out.println(client.queryAccount("", ""));;
		int count = 0;
		long t = System.currentTimeMillis();
		while(count < 250000){
			try {
				int result = 0;
				do{
					result = client.sendMessage("0001","13012222222","test",324424);
					if(result == -1){
						Thread.sleep(1000);
						System.out.println("--------提交队列满" + count);
					}
				}while(result == -1);
				
				count++;
//				System.out.println("提交" + count);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		t = System.currentTimeMillis() - t;
		System.out.println("------------- SEND TIME = " + t);
//		System.exit(0);
	}

}
