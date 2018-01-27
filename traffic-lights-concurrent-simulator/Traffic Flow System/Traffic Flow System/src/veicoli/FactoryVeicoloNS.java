package veicoli;

public class FactoryVeicoloNS extends FactoryVeicolo{

	public FactoryVeicoloNS(){
		pAuto = 0;
		pBus = 0;
		pBici = 0.1;
		pPedone = 0.9;
	}
	
	private double numeroCasuale(){
		return Math.random();
	}
	
	public Veicolo genera(double numeroCasuale){
		if(numeroCasuale <= 0.9)
			return new Pedone();
		else 
			return new Bici();
	}
	
	public Veicolo creaVeicolo(){
		double r = numeroCasuale();
		return genera(r);
	}
	
}
