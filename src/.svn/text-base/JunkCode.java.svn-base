import java.util.Map;

import com.baiwu.dove.util.StatusUtil;


public class JunkCode {
	public int validate(Map<String,String> map) {
		StatusUtil status = new StatusUtil();
		if(map.get("channelNum")==null){
			status.setChannelNum_status(1);
		}
		if(map.get("phoneNum")==null||!map.get("phoneNum").matches("\\d{10,12}")){
			status.setPhoneNumber_status(1);
		}
		if(map.get("content")==null){
			status.setContent_status(1);
		}
		if(map.get("corp_id")==null){
			status.setCorp_id_status(1);
		}
		if(map.get("user_id")==null){
			status.setUser_id_status(1);
		}
		if(map.get("service_code")==null){
			status.setService_code_status(1);
		}
		return status.getStatus();
		
	}
}
