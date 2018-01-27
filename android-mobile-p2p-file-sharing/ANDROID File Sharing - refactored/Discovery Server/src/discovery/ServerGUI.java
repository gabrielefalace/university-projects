package discovery;

import java.awt.Font;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class ServerGUI extends JFrame{

	
	private static final long serialVersionUID = -3900509973504297511L;
	private JTabbedPane tabbedPanel;
	private LinkedList<JTextArea> areasList;
	
	public ServerGUI(){
		setFont(new Font("Arial", Font.PLAIN, 20));
		setTitle("Server Log");
		setSize(600, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		tabbedPanel = new JTabbedPane();
		tabbedPanel.setFont(new Font("Arial", Font.PLAIN, 16));
		areasList = new LinkedList<JTextArea>();
		setContentPane(tabbedPanel);
		
		setVisible(true);
	}
	
	public int addNewLog(final String loggerName){
		JTextArea area = new JTextArea();
		area.setFont(new Font("Arial", Font.PLAIN, 16));
		JScrollPane scroll = new JScrollPane(area);
		SwingUtilities.invokeLater(new LogAdder(loggerName, scroll));
		areasList.addLast(area);
		return areasList.indexOf(area);
	}
	
	public void writeOnLog(final int index, final String message){
		SwingUtilities.invokeLater(new LogWriter(index, message));
	}
	
	public class LogAdder implements Runnable {
		
		private String loggerName;
		private JScrollPane scroll;
		
		public LogAdder(String loggerName, JScrollPane scroll){
			this.loggerName = loggerName;
			this.scroll = scroll; 
		}
		
		public void run(){
			tabbedPanel.addTab(loggerName, scroll);
		}
	}
	
	public class LogWriter implements Runnable {
		
		private int index;
		private String message;
		
		public LogWriter(int index, String message){
			this.index = index;
			this.message = message; 
		}
		
		public void run(){
			areasList.get(index).append(message+"\n");
		}
	}
}
