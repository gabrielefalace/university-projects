package semafori;

public class SemaforoPedoni extends Semaforo {

	public SemaforoPedoni(){
		currentState = ROSSO;
		passaggioRichiesto = false;
	}
	
	public String getMessaggio(){
		if(getStato() == ROSSO)
			return "ROSSO";
		else if (getStato() == GIALLO)
			return "GIALLO";
		else
			return "VERDE";
	}
	
}
