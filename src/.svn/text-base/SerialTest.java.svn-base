import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;


public class SerialTest {
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		ByteArrayOutputStream ais = new ByteArrayOutputStream();
		
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("1", "value1");
		map.put("2", "value2");
		map.put("3", "value3");
		map.put("4", "value4");
		
		try{
			ObjectOutputStream dops = new ObjectOutputStream(ais);
			dops.writeObject(map);
			
			byte[] mapByte = ais.toByteArray();
//			for(byte e : mapByte){
//				System.out.println(e);
//			}
			
			ByteArrayInputStream bais = new ByteArrayInputStream(mapByte);
			ObjectInputStream ois = new ObjectInputStream(bais);
			Map<String, String> outMap = (Map<String, String>)ois.readObject();
			
			System.out.println(outMap);
//			ais.reset();
			outMap.remove("1");
			System.out.println(outMap);
			dops.writeObject(outMap);
			dops.flush();
			mapByte = ais.toByteArray();
			ByteArrayInputStream bais2 = new ByteArrayInputStream(mapByte);
			ObjectInputStream ois2 = new ObjectInputStream(bais2);
			Map<String, String> outMap2 = (Map<String, String>)ois2.readObject();
			
			System.out.println(outMap2);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
}
