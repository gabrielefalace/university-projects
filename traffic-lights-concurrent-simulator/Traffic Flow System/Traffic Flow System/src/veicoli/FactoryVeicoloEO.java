package veicoli;

public class FactoryVeicoloEO extends FactoryVeicolo{

	public FactoryVeicoloEO(){
		pAuto = 0.8;
		pBus = 0.1;
		pBici = 0.1;
		pPedone = 0.0;
	}	
	
	private double numeroCasuale(){
		return Math.random();
	}
	
	public Veicolo genera(double numeroCasuale){
		if(numeroCasuale <= 0.8)
			return new Auto();
		else if(numeroCasuale > 0.8 && numeroCasuale <= 0.9)
			return new Bus();
		else return new Bici();
	}
	
	public Veicolo creaVeicolo(){
		double r = numeroCasuale();
		return genera(r);
	}
	
}
