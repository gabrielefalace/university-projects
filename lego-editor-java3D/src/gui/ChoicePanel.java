package gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.media.j3d.Canvas3D;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.Spring;
import javax.swing.SpringLayout;
import core.BrickType;
import core.Controller;

@SuppressWarnings("serial")
public class ChoicePanel extends JPanel {

	// controller
	private Controller controller;
	
	// etichette
	private JLabel labelOperation;
	private JLabel labelBrick;
	private JLabel labelRotation;
	
	private JLabel elemLabel;
	private JLabel l_shapLabel;
	private JLabel rect_smallLabel;
	private JLabel rect_midLabel;
	private JLabel rect_longLabel;
	private JLabel squaredLabel;
	
	// operazioni sulla struttura
	private JRadioButton insert;
	private JRadioButton move;
	private JRadioButton delete;
	
	// tipi di mattoncino
	private JRadioButton elementary;
	private JRadioButton l_shaped;
	private JRadioButton squared;
	private JRadioButton rectangular_small;
	private JRadioButton rectangular_mid;
	private JRadioButton rectangular_long;
	
	// rotazione
	private JRadioButton noRotation;
	private JRadioButton degree90;
	private JRadioButton degree180;
	private JRadioButton degree270;
	
	// bottone del colore
	private JButton colorButton;
	
	private Canvas3D canvas3D;
	
	public ChoicePanel(Controller aController, Canvas3D canvas){
		controller = aController;
		canvas3D = canvas;
		initComponents();
		addComponents();
		doLayoutSettings();
		setListeners();
		setDefaultControllerSettings();
		if(controller.getGameMode()==Controller.PRACTICE)
			colorButton.setEnabled(false);
	}
	
	private void setDefaultControllerSettings() {
		insert.setSelected(true);
		elementary.setSelected(true);
		noRotation.setSelected(true);
	}

	private void initComponents() {
		labelOperation = new JLabel("Operazioni sulla struttura");
		labelBrick = new JLabel("Tipi di mattoncini");
		labelRotation = new JLabel("Rotazione");
		
		elemLabel = new JLabel(new ImageIcon(".\\img\\elementary.png"));
		l_shapLabel = new JLabel(new ImageIcon(".\\img\\l_shaped.png"));
		rect_smallLabel = new JLabel(new ImageIcon(".\\img\\rectangular_small.png"));
		rect_midLabel = new JLabel(new ImageIcon(".\\img\\rectangular_mid.png"));
		rect_longLabel = new JLabel(new ImageIcon(".\\img\\rectangular_long.png"));
		squaredLabel = new JLabel(new ImageIcon(".\\img\\squared.png"));
		
		insert = new JRadioButton("Inserisci un nuovo mattoncino");
		move = new JRadioButton("Sposta un mattoncino");
		delete = new JRadioButton("Cancella un mattoncino");
		
		elementary = new JRadioButton();
		
		l_shaped = new JRadioButton();
		
		squared = new JRadioButton();
		
		rectangular_small = new JRadioButton();
		
		rectangular_mid = new JRadioButton();
		
		rectangular_long = new JRadioButton();
		
		noRotation = new JRadioButton("Nessuna");
		degree90 = new JRadioButton("90 gradi");
		degree180 = new JRadioButton("180 gradi");
		degree270 = new JRadioButton("270 gradi");
		
		colorButton = new JButton("Scegli Colore");
		
		setAllBackground(Color.WHITE);
	}
	
	private void setAllBackground(Color color) {
		setBackground(color);
		
		insert.setBackground(color);
		move.setBackground(color);
		delete.setBackground(color);
		
		elementary.setBackground(color);
		l_shaped.setBackground(color);
		squared.setBackground(color);
		rectangular_small.setBackground(color);
		rectangular_mid.setBackground(color);
		rectangular_long.setBackground(color);
		
		noRotation.setBackground(color);
		degree90.setBackground(color);
		degree180.setBackground(color);
		degree270.setBackground(color);
	}


	private void addComponents() {
		
		add(labelOperation);
		add(insert);
		add(move);
		add(delete);
		add(labelBrick);
		
		add(elemLabel);
		add(l_shapLabel);
		add(rect_smallLabel);
		add(rect_midLabel);
		add(rect_longLabel);
		add(squaredLabel);
		
		add(elementary);
		add(l_shaped);
		add(squared);
		add(rectangular_small);
		add(rectangular_mid);
		add(rectangular_long);
		
		add(labelRotation);
		add(noRotation);
		add(degree90);
		add(degree180);
		add(degree270);
		add(colorButton);
	}
	
	private void doLayoutSettings() {
		
		final Spring BIG_INTERLINE = Spring.constant(25);
		final Spring SMALL_INTERLINE = Spring.constant(10);
		
		SpringLayout layout = new SpringLayout();
		setLayout(layout);
		
		Spring xInit = Spring.constant(20);
		Spring xIndent = Spring.constant(40);
		Spring yInit = Spring.constant(20);
		
		SpringLayout.Constraints labelOperationC = layout.getConstraints(labelOperation);
		SpringLayout.Constraints insertC = layout.getConstraints(insert);
		SpringLayout.Constraints moveC = layout.getConstraints(move);
		SpringLayout.Constraints deleteC = layout.getConstraints(delete);
		labelOperationC.setX(xInit); labelOperationC.setY(yInit);
		insertC.setX(xIndent); moveC.setX(xIndent); deleteC.setX(xIndent);
		insertC.setY(Spring.sum(labelOperationC.getY(), Spring.sum(labelOperationC.getHeight(), SMALL_INTERLINE)));
		moveC.setY(Spring.sum(insertC.getY(), insertC.getHeight()));
		deleteC.setY(Spring.sum(moveC.getY(), moveC.getHeight()));
		
		SpringLayout.Constraints labelBrickC = layout.getConstraints(labelBrick);
		
		SpringLayout.Constraints elementaryC = layout.getConstraints(elementary);
		SpringLayout.Constraints elemLabelC = layout.getConstraints(elemLabel);
		
		
		SpringLayout.Constraints l_shapedC = layout.getConstraints(l_shaped);
		SpringLayout.Constraints l_shapLabelC = layout.getConstraints(l_shapLabel);
		
		SpringLayout.Constraints squaredC = layout.getConstraints(squared);
		SpringLayout.Constraints squaredLabelC = layout.getConstraints(squaredLabel);
		
		SpringLayout.Constraints rectangularSmallC = layout.getConstraints(rectangular_small);
		SpringLayout.Constraints rect_smallLabelC = layout.getConstraints(rect_smallLabel);
		
		SpringLayout.Constraints rectangularMidC = layout.getConstraints(rectangular_mid);
		SpringLayout.Constraints rect_midLabelC = layout.getConstraints(rect_midLabel);
		
		SpringLayout.Constraints rectangularLongC = layout.getConstraints(rectangular_long);
		SpringLayout.Constraints rect_longLabelC = layout.getConstraints(rect_longLabel);

		
		labelBrickC.setX(xInit);
		labelBrickC.setY(Spring.sum(deleteC.getY(), Spring.sum(deleteC.getHeight(), BIG_INTERLINE)));
		
		elementaryC.setX(xIndent); l_shapedC.setX(xIndent); squaredC.setX(xIndent);
		rectangularSmallC.setX(xIndent); rectangularMidC.setX(xIndent); rectangularLongC.setX(xIndent);
		
		elementaryC.setY(Spring.sum(labelBrickC.getY(), Spring.sum(labelBrickC.getHeight(), SMALL_INTERLINE)));
		elemLabelC.setY(elementaryC.getY()); elemLabelC.setX(Spring.sum(elementaryC.getX(), BIG_INTERLINE));
		
		Spring brickIndent = Spring.constant(100);
		l_shapedC.setY(elementaryC.getY()); l_shapedC.setX(Spring.sum(elemLabelC.getX(), brickIndent));
		l_shapLabelC.setY(l_shapedC.getY()); 
		l_shapLabelC.setX(Spring.sum(l_shapedC.getX(), BIG_INTERLINE));
		
		squaredC.setY(Spring.sum(Spring.sum(Spring.sum(l_shapedC.getY(), l_shapedC.getHeight()), l_shapLabelC.getHeight()), SMALL_INTERLINE));
		squaredLabelC.setY(squaredC.getY()); squaredLabelC.setX(Spring.sum(squaredC.getX(), BIG_INTERLINE));
		
		rectangularSmallC.setY(squaredC.getY()); rectangularSmallC.setX(Spring.sum(elemLabelC.getX(), brickIndent));
		rect_smallLabelC.setY(rectangularSmallC.getY()); rect_smallLabelC.setX(Spring.sum(rectangularSmallC.getX(), BIG_INTERLINE));		
		
		rectangularMidC.setY(Spring.sum(Spring.sum(Spring.sum(rectangularSmallC.getY(), rectangularSmallC.getHeight()), rect_smallLabelC.getHeight()), BIG_INTERLINE));
		rectangularMidC.setX(squaredC.getX());
		rect_midLabelC.setY(rectangularMidC.getY()); rect_midLabelC.setX(elemLabelC.getX());
		
		rectangularLongC.setY(rectangularMidC.getY()); rectangularLongC.setX(Spring.sum(elemLabelC.getX(), brickIndent));
		rect_longLabelC.setY(rectangularLongC.getY()); rect_longLabelC.setX(Spring.sum(rectangularLongC.getX(), BIG_INTERLINE));
		
		SpringLayout.Constraints labelRotC = layout.getConstraints(labelRotation);
		labelRotC.setX(xInit);
		labelRotC.setY(Spring.sum(rectangularLongC.getY(), Spring.sum(rectangularLongC.getHeight(), brickIndent)));
		
		SpringLayout.Constraints noRotC = layout.getConstraints(noRotation);
		noRotC.setX(xIndent);
		noRotC.setY(Spring.sum(labelRotC.getY(), Spring.sum(labelRotC.getHeight(), SMALL_INTERLINE)));
		
		SpringLayout.Constraints deg90C = layout.getConstraints(degree90);
		deg90C.setX(xIndent);
		deg90C.setY(Spring.sum(noRotC.getY(), noRotC.getHeight()));
		
		SpringLayout.Constraints deg180C = layout.getConstraints(degree180);
		deg180C.setX(xIndent);
		deg180C.setY(Spring.sum(deg90C.getY(), deg90C.getHeight()));
		
		SpringLayout.Constraints deg270C = layout.getConstraints(degree270);
		deg270C.setX(xIndent);
		deg270C.setY(Spring.sum(deg180C.getY(), deg180C.getHeight()));

		SpringLayout.Constraints buttonC = layout.getConstraints(colorButton);
		buttonC.setX(xInit);
		buttonC.setY(Spring.sum(deg270C.getY(), Spring.sum(deg270C.getHeight(), BIG_INTERLINE)));
		
	}
		
	private void setListeners() {
		setOperationListeners();
		setBrickTypeListeners();
		setRotationListeners();
		setColorButtonListener();
	}
	
	private void setColorButtonListener() {
		colorButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFrame frame = new JFrame();
				ColorPanel cp = new ColorPanel(frame, controller);
				frame.add(cp);
				frame.setSize(400, 400);
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setVisible(true);
			}
		});
	}

	private void setRotationListeners() {
		noRotation.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				deselectAllRotations();
				noRotation.setSelected(true);
				controller.setRotation(0.0);
			}
		});
		
		degree90.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(degree90.isSelected()){
					deselectAllRotations();
					degree90.setSelected(true);
					controller.setRotation(Math.PI/2);
				}
				else{
					deselectAllRotations();
					noRotation.setSelected(true);
					controller.setRotation(0.0);
				}
			}
		});
		
		degree180.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(degree180.isSelected()){
					deselectAllRotations();
					degree180.setSelected(true);
					controller.setRotation(Math.PI);
				}
				else{
					deselectAllRotations();
					noRotation.setSelected(true);
					controller.setRotation(0.0);
				}
			}
		});
		
		degree270.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(degree270.isSelected()){
					deselectAllRotations();
					degree270.setSelected(true);
					controller.setRotation(Math.PI*3/2);
				}
				else{
					deselectAllRotations();
					noRotation.setSelected(true);
					controller.setRotation(0.0);
				}
			}
		});
	}

	
	/**
	 * Imposta gli ascoltatori per selezionare il tipo di operazione che 
	 * verrà effettuata con il click sul canvas 3D (inserisci, elimina, sposta).
	 */
	private void setOperationListeners(){
		// insert non è de-selezionabile se non scegliendo altro: è di default.
		insert.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(insert.isSelected()){
					controller.setOperationSelected(Controller.INSERT_BRICK);
					move.setSelected(false);
					delete.setSelected(false);
					enableAllBrickTypes();
					canvas3D.setCursor(new Cursor(Cursor.HAND_CURSOR));
				}
				else{
					insert.setSelected(true);
					controller.setOperationSelected(Controller.INSERT_BRICK);
					enableAllBrickTypes();
					canvas3D.setCursor(new Cursor(Cursor.HAND_CURSOR));
				}
			}
		});
		move.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(move.isSelected()){
					controller.setOperationSelected(Controller.MOVE_BRICK);
					insert.setSelected(false);
					delete.setSelected(false);
					disableAllBrickTypes();
					canvas3D.setCursor(new Cursor(Cursor.MOVE_CURSOR));
				}
				else{
					insert.setSelected(true);
					controller.setOperationSelected(Controller.INSERT_BRICK);
					enableAllBrickTypes();
					canvas3D.setCursor(new Cursor(Cursor.HAND_CURSOR));
				}
			}
		});
		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(delete.isSelected()){
					controller.setOperationSelected(Controller.DELETE_BRICK);
					move.setSelected(false);
					insert.setSelected(false);
					disableAllBrickTypes();
					canvas3D.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
				}
				else{
					insert.setSelected(true);
					controller.setOperationSelected(Controller.INSERT_BRICK);
					enableAllBrickTypes();
					canvas3D.setCursor(new Cursor(Cursor.HAND_CURSOR));
				}
			}
		});
	}
	
	/**
	 * Imposta gli ascoltatori per selezionare il tipo di mattoncino da inserire
	 * cliccando sugli appositi radio-button. 
	 */
	private void setBrickTypeListeners() {
		elementary.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(elementary.isSelected()){
					deselectAllBrickTypes(); 
					elementary.setSelected(true);
					controller.setBrickType(BrickType.elementary);
				}
				else{
					deselectAllBrickTypes();
					elementary.setSelected(true);
					controller.setBrickType(BrickType.elementary);
				}
			}
		});
		l_shaped.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(l_shaped.isSelected()){
					deselectAllBrickTypes(); 
					l_shaped.setSelected(true);
					controller.setBrickType(BrickType.l_shaped);
				}
				else{
					deselectAllBrickTypes();
					elementary.setSelected(true);
					controller.setBrickType(BrickType.elementary);
				}
			}
		});
		squared.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(squared.isSelected()){
					deselectAllBrickTypes(); 
					squared.setSelected(true);
					controller.setBrickType(BrickType.squared);
				}
				else{
					deselectAllBrickTypes();
					elementary.setSelected(true);
					controller.setBrickType(BrickType.elementary);
				}
			}
		});
		rectangular_small.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(rectangular_small.isSelected()){
					deselectAllBrickTypes(); 
					rectangular_small.setSelected(true);
					controller.setBrickType(BrickType.rectangular_small);
				}
				else{
					deselectAllBrickTypes();
					elementary.setSelected(true);
					controller.setBrickType(BrickType.elementary);
				}
			}
		});
		rectangular_mid.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(rectangular_mid.isSelected()){
					deselectAllBrickTypes(); 
					rectangular_mid.setSelected(true);
					controller.setBrickType(BrickType.rectangular_mid);
				}
				else{
					deselectAllBrickTypes();
					elementary.setSelected(true);
					controller.setBrickType(BrickType.elementary);
				}
			}
		});
		rectangular_long.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(rectangular_long.isSelected()){
					deselectAllBrickTypes(); 
					rectangular_long.setSelected(true);
					controller.setBrickType(BrickType.rectangular_long);
				}
				else{
					deselectAllBrickTypes();
					elementary.setSelected(true);
					controller.setBrickType(BrickType.elementary);
				}
			}
		});
	}

	/**
	 * Deseleziona tutti i radio-button che rappresentano i tipi di mattoncini.
	 */
	private final void deselectAllBrickTypes(){
		elementary.setSelected(false);
		l_shaped.setSelected(false);
		squared.setSelected(false);
		rectangular_small.setSelected(false);
		rectangular_mid.setSelected(false);
		rectangular_long.setSelected(false);
	}
		
	/**
	 * Disabilita tutti i radio-button che rappresentano i tipi di mattoncini.
	 */
	private final void disableAllBrickTypes(){
		elementary.setEnabled(false);
		l_shaped.setEnabled(false);
		squared.setEnabled(false);
		rectangular_small.setEnabled(false);
		rectangular_mid.setEnabled(false);
		rectangular_long.setEnabled(false);
	}
	
	/**
	 * Abilita tutti i radio-button che rappresentano i tipi di mattoncini.
	 */
	private final void enableAllBrickTypes(){
		elementary.setEnabled(true);
		l_shaped.setEnabled(true);
		squared.setEnabled(true);
		rectangular_small.setEnabled(true);
		rectangular_mid.setEnabled(true);
		rectangular_long.setEnabled(true);
	}
	
	private final void deselectAllRotations(){
		noRotation.setSelected(false);
		degree90.setSelected(false);
		degree180.setSelected(false);
		degree270.setSelected(false);
	}
}
