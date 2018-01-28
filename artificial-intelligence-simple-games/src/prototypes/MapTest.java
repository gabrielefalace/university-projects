package prototypes;

import java.util.HashMap;
import java.util.Hashtable;

public class MapTest {

	public static void main(String[] args){
		
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("129109", "Daniela Martino");
		hashMap.put("127599", "Ciccio Piro");
		hashMap.put("127599", "Gabriele Falace");
		
		String entry = hashMap.get("129109");
		System.out.println(entry);
		Class[] interfaces = entry.getClass().getInterfaces();
		for(Class c: interfaces)
			System.out.println(c);
		
		 Hashtable<String, String> table = new Hashtable<String, String>();
		 table.put("129109", "Daniela Martino");
		 table.put("127599", "Ciccio Piro");
		 table.put("127599", "Gabriele Falace");
		 
		 String teble_entry = table.get("127599");
		 System.out.println(teble_entry);
		 Class[] interfaces2 = teble_entry.getClass().getInterfaces();
		 for(Class c: interfaces2)
			System.out.println(c);
	}
}
