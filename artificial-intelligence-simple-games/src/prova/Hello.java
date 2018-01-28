package prova;

public class Hello {

	public native void sayHello();
	
	static {
		System.loadLibrary("hello");
	}
	
	
	public static void main(String[] args){
		Hello greeting = new Hello();
		greeting.sayHello();
		System.out.println("Finita esecuzione funzione nativa...");
	}
	
}
