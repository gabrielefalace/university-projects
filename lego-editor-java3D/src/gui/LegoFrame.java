package gui;

import java.awt.EventQueue;
import java.awt.GraphicsConfiguration;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.media.j3d.Canvas3D;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import com.sun.j3d.utils.universe.SimpleUniverse;
import core.Controller;

@SuppressWarnings("serial")
public class LegoFrame extends JFrame {
	
	public static final int DIVIDER_LOCATION = 1000;
	
	LegoPanel legoPanel;
	ChoicePanel choicePanel;
	JSplitPane splitPanel;
	
	Controller controller;
	Canvas3D canvas3D;
	
	JMenuBar bar;
	JMenu file, newGame, options, help;
	JMenuItem open, save, clear, full, practice, guide, about;
	
	int currentGameMode;
	
	
	public LegoFrame(){
		super("Lego 3D Editor");
		controller = new Controller(Controller.PRACTICE);
		currentGameMode = Controller.PRACTICE;
		
		canvas3D = createCanvas3D();
		
		initFrame();
		initComponents();
		addComponents();
		setMenus();
		setListeners();
		
		doLayoutSettings();
	}
	
	
	private void setListeners() {
		open.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// L'utente sceglie il file
				JFileChooser fileChooser = new JFileChooser("C:\\LegoArchive\\");
				String filePath = "";
				int result = fileChooser.showOpenDialog(LegoFrame.this);
				if(result != JFileChooser.APPROVE_OPTION){
					JOptionPane.showMessageDialog(null, "Impossibile caricare il file", "Errore in lettura", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				int currentMode = Controller.PRACTICE;
				
				filePath = fileChooser.getSelectedFile().getAbsolutePath();
				InputStreamReader inStream = null;
				BufferedReader reader = null;
				try {
					// lettura del gameMode dal file
					inStream = new InputStreamReader(new FileInputStream(filePath));
					reader = new BufferedReader(inStream);
					currentMode = Integer.parseInt(reader.readLine());
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
				finally{
					if(inStream != null){
						try {
							inStream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				
				// Settings per riavere un tavolo vuoto ben visualizzato
				controller = new Controller(currentMode); 
				canvas3D = createCanvas3D();
				legoPanel = new LegoPanel(controller, canvas3D, filePath);
				choicePanel = new ChoicePanel(controller, canvas3D);
				
				// fase di ripristino dei bricks sul tavolo 
				legoPanel.restoreBricksOnTable(filePath);
				
				// impostazioni per lo split-panel 
				splitPanel.setLeftComponent(legoPanel);
				splitPanel.setRightComponent(choicePanel);
				doLayoutSettings();
			}
			
		});
		save.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent arg0) {
				File homeDir = new File("C:\\LegoArchive\\");
				if(!homeDir.exists())
					homeDir.mkdir();
				
				JFileChooser fileChooser = new JFileChooser("C:\\LegoArchive\\");
				int result = fileChooser.showSaveDialog(LegoFrame.this);
				
				if(result==JFileChooser.APPROVE_OPTION){
					File savePath = fileChooser.getSelectedFile();
					if(savePath.exists()){
						savePath.delete();
						savePath = fileChooser.getSelectedFile(); 
					}
					legoPanel.clickBehaviour.saveToFile(savePath);
				}
			}
		});
		clear.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller = new Controller(currentGameMode);
				canvas3D = createCanvas3D();
				legoPanel = new LegoPanel(controller, canvas3D, "");
				choicePanel = new ChoicePanel(controller, canvas3D);
				splitPanel.setLeftComponent(legoPanel);
				splitPanel.setRightComponent(choicePanel);
				doLayoutSettings();
			}
		});
		full.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller = new Controller(Controller.FULL);
				currentGameMode = Controller.FULL;
				canvas3D = createCanvas3D();
				legoPanel = new LegoPanel(controller, canvas3D, "");
				choicePanel = new ChoicePanel(controller, canvas3D);
				splitPanel.setLeftComponent(legoPanel);
				splitPanel.setRightComponent(choicePanel);
				doLayoutSettings();
			}
		});
		practice.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller = new Controller(Controller.PRACTICE);
				currentGameMode = Controller.PRACTICE;
				canvas3D = createCanvas3D();
				legoPanel = new LegoPanel(controller, canvas3D, "");
				choicePanel = new ChoicePanel(controller, canvas3D);
				splitPanel.setLeftComponent(legoPanel);
				splitPanel.setRightComponent(choicePanel);
				doLayoutSettings();
			}
		});
		
		guide.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new GuideFrame();
			}
		});
		
		about.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new AboutFrame();
			}
		});
	}


	private void setMenus() {
		JMenuBar bar = new JMenuBar(); 
		setJMenuBar(bar);
		
		file = new JMenu("File");
		bar.add(file);
		
		open = new JMenuItem("Apri");
		file.add(open);
		save = new JMenuItem("Salva");
		file.add(save);
		clear = new JMenuItem("Cancella");
		file.add(clear);

		newGame = new JMenu("Nuovo");
		file.add(newGame);
		
		full = new JMenuItem("Full");
		newGame.add(full);
		
		practice = new JMenuItem("Practice");
		newGame.add(practice);
		
		options = new JMenu("Options");
		bar.add(options);
		
		help = new JMenu("Help");
		bar.add(help);
		
		guide = new JMenuItem("Guida");
		help.add(guide);
		
		about = new JMenuItem("About");
		help.add(about);
	}

	private void initFrame() {
		setSize(Toolkit.getDefaultToolkit().getScreenSize());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private void initComponents() {
		splitPanel = new JSplitPane();
		choicePanel = new ChoicePanel(controller, canvas3D);
		legoPanel = new LegoPanel(controller, canvas3D, "");
	}
	
	private void addComponents() {
		add(splitPanel);
		splitPanel.setLeftComponent(legoPanel);
		splitPanel.setRightComponent(choicePanel);
	}	
	
	private void doLayoutSettings(){
		splitPanel.setDividerLocation(DIVIDER_LOCATION);
		splitPanel.setDividerSize(7);
		splitPanel.setOneTouchExpandable(true);
	}
	
	/**
	 * Crea un Canvas3D per la visualizzazione della geometria.
	 * @return Il Canvas3D creato.
	 */
	private Canvas3D createCanvas3D() {
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		Canvas3D canvas3D = new Canvas3D(config);
		return canvas3D;
	}
	
	public static void main(String[] args){
		
		Runnable runner = new Runnable() {
			@Override
			public void run() {
				LegoFrame legoFrame = new LegoFrame();
				try{
					Thread.sleep(3000);
				}
				catch(InterruptedException err){
					err.printStackTrace();
				}
				finally{
					legoFrame.setVisible(true);
				}
			}
		};
		
		EventQueue.invokeLater(runner);
		
	}
}
