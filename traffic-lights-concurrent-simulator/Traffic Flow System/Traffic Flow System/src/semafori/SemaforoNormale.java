package semafori;

public class SemaforoNormale extends Semaforo{

	
	public SemaforoNormale(){
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
