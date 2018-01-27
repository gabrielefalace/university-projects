package test;

import java.util.Scanner;

import mobileApp.MobileApp;


public class TesterApp {


	public static void main(String[] args){
		try{
			System.out.println("IP - PORT - Name");
			Scanner scanner = new Scanner(System.in);
			String ip = scanner.next();
			int port = scanner.nextInt();
			String name = scanner.next();
			new MobileApp(ip, port, name);
		}
		catch(Exception e){e.printStackTrace();}
	}
}
