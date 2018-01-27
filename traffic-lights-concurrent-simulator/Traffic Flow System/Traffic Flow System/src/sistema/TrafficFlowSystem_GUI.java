package sistema;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import orologio.*;
import semafori.Semaforo;
import veicoli.Veicolo;

class PannelloDati extends JPanel implements TimeObserver{
	
	int tempoCorrente;
	double codaN, codaS, codaO, codaE;
	TrafficFlowSystem tfs;
	Dimension dim;
	
	public PannelloDati(TrafficFlowSystem t, Dimension d){
		tempoCorrente = 0;
		tfs = t;
		dim = d;
		tfs.iscriviOrologio(this);
		setBackground(Color.WHITE);
	}
	
	public void update(Observable o, Object arg){
		update(((Integer)arg).intValue());
	}
	
	public void update(int nuovoTempo){
		tempoCorrente = tfs.getTempoCorrente();
		codaN = tfs.getLunghezzaCoda(Direzioni.NORD);
		codaS = tfs.getLunghezzaCoda(Direzioni.SUD);
		codaO = tfs.getLunghezzaCoda(Direzioni.OVEST);
		codaE = tfs.getLunghezzaCoda(Direzioni.EST);
		repaint();
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.clearRect(getX(), getY(), getWidth(), getHeight());
		g.setFont(new Font("Arial", Font.BOLD, 14));
		g.drawString("Tempo Corrente : "+tempoCorrente, 15, 30);
		g.drawString("Veicoli in coda: ", 15, 70);
		g.drawString("coda Nord: "+codaN, 15, 110);
		g.drawString("coda Sud: "+codaS, 15, 130);
		g.drawString("coda Ovest: "+codaO, 15, 150);
		g.drawString("coda Est: "+codaE, 15, 170);
	}
}

class PannelloIncrocio extends JPanel implements TimeObserver{
	
	TrafficFlowSystem tfs;
	Dimension dim;
	int bH, bW ;
	LinkedList<Veicolo> codaN, codaS, codaO, codaE;
	// coordinate inizio delle code
	int XEST, YEST, XNORD, YNORD, XOVEST, YOVEST, XSUD, YSUD;
	
	public PannelloIncrocio(TrafficFlowSystem t, Dimension d){
		tfs = t;
		dim = d;
		tfs.iscriviOrologio(this);
		setBackground(Color.DARK_GRAY);
		bW = dim.width/3;
		bH = dim.height/3;
		XEST = bW+bW +5; 
		YEST = bH + 15;
		XNORD = bW+10;
		YNORD = bH - 35;
		XOVEST = bW - 20 ;
		YOVEST = bH + bH - 35;
		XSUD = bW+bW-25;
		YSUD = bH+bH+5;
	}
	
	public void update(Observable o, Object arg){
		update(((Integer)arg).intValue());
	}
	
	public void update(int nuovoTempo){
		if(nuovoTempo <= tfs.getTempoLimite())
			repaint();
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		codaN = tfs.getCodaN();
		codaS = tfs.getCodaS();
		codaO = tfs.getCodaO();
		codaE = tfs.getCodaE();
		g.setColor(Color.getHSBColor(1000, 1000, 1000));
		g.fillRect(0, 0, bW, bH);
		g.fillRect(bW+bW, 0, bW, bH);
		g.fillRect(0, bH+bH, bW, bH);
		g.fillRect(bW+bW, bH+bH, bW, bH);
		g.setColor(Color.WHITE);
		for(int X=0; X<dim.width; X=X+20)
			g.fillRect(X, bH+(bH/2), 14, 3);
		for(int Y=0; Y<dim.height; Y=Y+20)
			g.fillRect(bW+(bW/2), Y, 3, 14);
		g.setColor(Color.getHSBColor(75, 300, 140));
		g.fillRoundRect(bW, bH, 30, 30, 3, 3);
		g.fillRoundRect(bW+bW-30, bH+bH-30, 30, 30, 3, 3);
		g.fillRoundRect(bW, bH+bH-30, 30, 30, 3, 3);
		g.fillRoundRect(bW+bW-30, bH, 30, 30, 3, 3);
		
		int s = tfs.getStatoSemaforo("NORD");
		if(s == Semaforo.VERDE){g.setColor(Color.GREEN); g.setFont(new Font("Arial", Font.BOLD, 12));g.drawString("WALK",bW, bH+12);}
		else if (s == Semaforo.GIALLO){g.setColor(Color.YELLOW); g.fillRect(bW+2, bH+2, 28, 15);}
		else{g.setColor(Color.RED); g.setFont(new Font("Arial", Font.BOLD, 12)); g.drawString("STOP", bW, bH+12);}
		
		s = tfs.getStatoSemaforo("SUD");
		if(s == Semaforo.VERDE){g.setColor(Color.GREEN);g.setFont(new Font("Arial", Font.BOLD, 12)); g.drawString("WALK", bW+bW-30, bH+bH-7);}
		else if (s == Semaforo.GIALLO){g.setColor(Color.YELLOW); g.fillRect(bW+bW-30, bH+bH-17, 28, 15);}
		else{g.setColor(Color.RED); g.setFont(new Font("Arial", Font.BOLD, 12)); g.drawString("STOP", bW+bW-30, bH+bH-7);}
		
		s = tfs.getStatoSemaforo("OVEST");
		if(s == Semaforo.VERDE){g.setColor(Color.GREEN); g.fillRect(bW+2, bH+bH-30, 15, 28);}
		else if (s == Semaforo.GIALLO){g.setColor(Color.YELLOW); g.fillRect(bW+2, bH+bH-30, 15, 28);}
		else{g.setColor(Color.RED); g.fillRect(bW+2, bH+bH-30, 15, 28);}
		
		s = tfs.getStatoSemaforo("EST");
		if(s == Semaforo.VERDE){g.setColor(Color.GREEN); g.fillRect(bW+bW-15, bH+2, 15, 28);}
		else if (s == Semaforo.GIALLO){g.setColor(Color.YELLOW); g.fillRect(bW+bW-15, bH+2, 15, 28);}
		else{g.setColor(Color.RED); g.fillRect(bW+bW-15, bH+2, 15, 28);}
	
		g.setColor(Color.lightGray);
		
		for(int i = YNORD, j = 0; i > 0;){
			if(codaN.size()==0 || j>=codaN.size())
				break;
			Veicolo v = codaN.get(j);
			int larghezza = 0;
			double lunghezza = v.getLunghezza();
			if(lunghezza==0){  //pedone
				lunghezza = 10;
				larghezza = 10;
			}
			else if(lunghezza==1.5){ //bici
				larghezza = 10;
				lunghezza = 25;
			}
			else if(lunghezza==6){
				larghezza = 20;
				lunghezza = 30;
			}
			else{ // bus
				larghezza = 30;
				lunghezza = 50;
			}
			g.fillRect(XNORD, i, larghezza, (int)lunghezza);
			i = i-(int)lunghezza - 5;
			j++;
			
		}
		
		for(int i = YSUD, j = 0; j<(YSUD+bH);){
			if(codaS.size()==0 || j>=codaS.size())
				break;
			Veicolo v = codaS.get(j);
			int larghezza = 0;
			double lunghezza = v.getLunghezza();
			if(lunghezza==0){  //pedone
				lunghezza = 10;
				larghezza = 10;
			}
			else if(lunghezza==1.5){ //bici
				larghezza = 10;
				lunghezza = 25;
			}
			else if(lunghezza==6){
				larghezza = 20;
				lunghezza = 30;
			}
			else{ // bus
				larghezza = 30;
				lunghezza = 50;
			}
			g.fillRect(XSUD, i, larghezza, (int)lunghezza);
			i = i+(int)lunghezza + 5;
			j++;
		}
		
		for(int i = XOVEST, j = 0; i>0;){
			if(codaO.size()==0 || j>=codaO.size())
				break;
			Veicolo v = codaO.get(j);
			int larghezza = 0;
			double lunghezza = v.getLunghezza();
			if(lunghezza==0){  //pedone
				lunghezza = 10;
				larghezza = 10;
			}
			else if(lunghezza==1.5){ //bici
				larghezza = 10;
				lunghezza = 25;
			}
			else if(lunghezza==6){
				larghezza = 20;
				lunghezza = 30;
			}
			else{ // bus
				larghezza = 30;
				lunghezza = 50;
			}
			g.fillRect(i-((int)v.getLunghezza()*2), YOVEST, (int)lunghezza, larghezza);
			i = i - (int)lunghezza - 5;
			j++;
		}
		
		for(int i = XEST, j = 0; i <(XEST+bW); ){
			if(codaE.size()==0 || j>=codaE.size())
				break;
			Veicolo v = codaE.get(j);
			int larghezza = 0;
			double lunghezza = v.getLunghezza();
			if(lunghezza==0){  //pedone
				lunghezza = 10;
				larghezza = 10;
			}
			else if(lunghezza==1.5){ //bici
				larghezza = 10;
				lunghezza = 25;
			}
			else if(lunghezza==6){
				larghezza = 20;
				lunghezza = 30;
			}
			else{ // bus
				larghezza = 30;
				lunghezza = 50;
			}
			g.fillRect(i, YEST, (int)lunghezza, larghezza);
			i = i+ (int)lunghezza + 5;
			j++;
		}
		
	}
}

public class TrafficFlowSystem_GUI {
	
	public static void main(String[] args){
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int baseIncrocio = (dim.width*4)/5 ; 
		int baseDati = dim.width - baseIncrocio;
		TrafficFlowSystem tfs = new TrafficFlowSystem();
		JFrame f = new JFrame("Traffic Flow System");
		f.setSize(dim);
		PannelloDati pDati = new PannelloDati(tfs, new Dimension(baseDati, dim.height));
		PannelloIncrocio pIncrocio = new PannelloIncrocio(tfs,new Dimension(baseIncrocio, dim.height));
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pIncrocio, pDati);
		split.setContinuousLayout(true);
		split.setDividerLocation(baseIncrocio);
		f.add(split);
		f.setResizable(false);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
}
