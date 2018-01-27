package basic;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.nilo.plaf.nimrod.NimRODLookAndFeel;

import gui.*;
import basic.*;

public class StartRobot {
	
	public static void main(String[] args){
		try{
			UIManager.setLookAndFeel(new NimRODLookAndFeel());
		}
		catch(UnsupportedLookAndFeelException e){
			e.printStackTrace();
		}
		Log log = new Log();
		EnvironmentGUI gui = new EnvironmentGUI(log);
	}

}
