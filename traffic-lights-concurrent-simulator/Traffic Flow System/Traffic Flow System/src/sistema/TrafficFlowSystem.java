package sistema;

import java.util.LinkedList;

import orologio.*;
import codeVeicoli.*;
import semafori.*;
import veicoli.*;
import controller.*;

public class TrafficFlowSystem {

	private Orologio orologio;
	private CodaVeicoli cNORD;
	private CodaVeicoli cSUD;
	private CodaVeicoli cOVEST;
	private CodaVeicoli cEST;
	private Semaforo sNORD;
	private SemaforoNonVedenti sSUD;
	private Semaforo sOVEST;
	private Semaforo sEST;
	private Controller controller;
	private static final int tempoLimite = 700;
	

	public TrafficFlowSystem()
	{
		orologio = Orologio.getIstanza(tempoLimite);
		sNORD = new SemaforoPedoni();
		sSUD = new SemaforoNonVedenti();
		sOVEST = new SemaforoNormale();
		sEST = new SemaforoNormale();
		cNORD = new CodaVeicoli(orologio, sNORD, new FactoryVeicoloNS(), 0.01, Direzioni.NORD);
		cSUD = new CodaVeicoli(orologio, sSUD, new FactoryVeicoloNS(), 0.02, Direzioni.SUD);
		cOVEST = new CodaVeicoli(orologio, sOVEST, new FactoryVeicoloEO(), 0.0333, Direzioni.OVEST);
		cEST = new CodaVeicoli(orologio, sEST, new FactoryVeicoloEO(), 0.05, Direzioni.EST);
		controller = new Controller(sNORD, sSUD, sOVEST, sEST, orologio, cNORD, cSUD, cOVEST, cEST);
	}
	
	
	
	public double getLunghezzaCoda(String direzione){
		if(direzione.equals(Direzioni.NORD)) return cNORD.getSize();
		else if(direzione.equals(Direzioni.SUD)) return cSUD.getSize();
		else if(direzione.equals(Direzioni.OVEST)) return cOVEST.getLunghezza();
		else return cEST.getLunghezza();	
	}
	
	public int getStatoSemaforo(String direzione){
		if(direzione.equals(Direzioni.NORD)) return sNORD.getStato();
		else if(direzione.equals(Direzioni.SUD)) return sSUD.getStato();
		else if(direzione.equals(Direzioni.OVEST)) return sOVEST.getStato();
		else return sEST.getStato();
	}
	
	public int getTempoCorrente(){	return controller.getTempoSistema();	} 

	public int getTempoLimite(){	return orologio.getTempoLimite(); }

	public void iscriviOrologio(TimeObserver o){
		orologio.addObserver(o);
	}
	
	public LinkedList<Veicolo> getCodaN(){
		return cNORD.getCoda();
	}
	
	public LinkedList<Veicolo> getCodaS(){
		return cSUD.getCoda();
	}

	public LinkedList<Veicolo> getCodaO(){
		return cOVEST.getCoda();
	}
	
	public LinkedList<Veicolo> getCodaE(){
		return cEST.getCoda();
	}
}
