package com.baiwu.dove.util;

public class StatusUtil {
	private int channelNum_status;
	private int phoneNumber_status;
	private int content_status;
	private int corp_id_status;
	private int user_id_status;
	private int passwd_status;
	private int service_code_status;
	private int ip_status;
	private int port_status;

	public int getChannelNum_status() {
		return channelNum_status;
	}

	public void setChannelNum_status(int channelNum_status) {
		this.channelNum_status = channelNum_status;
	}

	public int getPhoneNumber_status() {
		return phoneNumber_status;
	}

	public void setPhoneNumber_status(int phoneNumber_status) {
		this.phoneNumber_status = phoneNumber_status;
	}

	public int getContent_status() {
		return content_status;
	}

	public void setContent_status(int content_status) {
		this.content_status = content_status;
	}

	public int getCorp_id_status() {
		return corp_id_status;
	}

	public void setCorp_id_status(int corp_id_status) {
		this.corp_id_status = corp_id_status;
	}

	public int getPasswd_status() {
		return passwd_status;
	}

	public void setPasswd_status(int passwd_status) {
		this.passwd_status = passwd_status;
	}

	public int getService_code_status() {
		return service_code_status;
	}

	public void setService_code_status(int service_code_status) {
		this.service_code_status = service_code_status;
	}

	public int getIp_status() {
		return ip_status;
	}

	public void setIp_status(int ip_status) {
		this.ip_status = ip_status;
	}

	public int getPort_status() {
		return port_status;
	}

	public void setPort_status(int port_status) {
		this.port_status = port_status;
	}

	public int getUser_id_status() {
		return user_id_status;
	}

	public void setUser_id_status(int user_id_status) {
		this.user_id_status = user_id_status;
	}

	/**
	 * 更具情况定义不同的算法获取状态
	 * 
	 * @return
	 */
	public int getStatus() {
		String status = "" + channelNum_status + phoneNumber_status
				+ content_status + corp_id_status + user_id_status
				+ passwd_status + service_code_status + ip_status + port_status;
		int x = 0;

		for (char c : status.toCharArray())
			x = x * 2 + (c == '1' ? 1 : 0);
		return x;
	}

	public String getDesc() {
		return null;
	}
	public StatusUtil convertToStatus(int status){
		String binary = Integer.toBinaryString(status); 
		char[] ch = new char[9];
		ch = binary.toCharArray();
		for(char c: ch){
			System.out.print(c+"\t");
		}
		return null;
	}
	public static void main(String[] aas){
		StatusUtil s = new StatusUtil();
		s.setPort_status(0);
		s.setIp_status(1);
		int i = s.getStatus();
		System.out.println(i);
		
		s.convertToStatus(32);
	}
	
}
