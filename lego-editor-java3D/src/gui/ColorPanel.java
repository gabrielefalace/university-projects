package gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.vecmath.Color3f;

import core.Controller;

@SuppressWarnings("serial")
public class ColorPanel extends JPanel {

	private JColorChooser chooser;
	private Color selectedColor;
	private JFrame hostingFrame;
	
	private Controller controller;
	
	public ColorPanel(JFrame frame, Controller aController){
		super();
		hostingFrame = frame;
		controller = aController;
		JButton button = new JButton("Scegli Colore");
		chooser = new JColorChooser();
		setLayout(new FlowLayout());
		
		add(chooser);
		add(button);
		
		chooser.remove(1);		
		
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				selectedColor = chooser.getColor();
				float r = selectedColor.getRed()/255f;
				float g = selectedColor.getGreen()/255f;
				float b = selectedColor.getBlue()/255f;
				Color3f col = new Color3f(r, g, b);
				controller.setColor(col);
				hostingFrame.dispose();
			}
		});
	}

	public final Color getSelectedColor() {
		return selectedColor;
	}	
}
