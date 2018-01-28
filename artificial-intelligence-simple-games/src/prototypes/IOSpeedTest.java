package prototypes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class IOSpeedTest {
	
	public static void main(String[] args){
		String[] list = {"Gabriele", "Daniela", "Giuseppe", "Stefano", "Salvatore", "Mario", "Francesco", "Ignazio"};
		File f = new File("C:\\Users\\Gabriele\\Desktop\\listNames.dat");
		long initialTime = -1;
		long finalTime = -1;
		try{
			FileOutputStream fos = new FileOutputStream(f);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			initialTime = System.currentTimeMillis();
			for(int i=0; i<10000; i++){
				list[0] = list[0]+""+i;
				oos.writeObject(list);
			}
			finalTime = System.currentTimeMillis();
			
			if(initialTime >= 0 && finalTime >= 0){
			
				float averageTime = (finalTime-initialTime)/10000;
			
				System.out.println("tempo medio di 10000 scritture è: "+averageTime);
			
				System.out.println("10000 scritture in: "+(finalTime-initialTime));
			}
			else
				System.out.println("Errore");
		}
		catch(IOException err){err.printStackTrace();}
	}
}
