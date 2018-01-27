package veicoli;

public abstract class FactoryVeicolo {

	protected double pAuto;
	protected double pBus;
	protected double pBici;
	protected double pPedone;
	
	
	public abstract Veicolo creaVeicolo();
	
	public boolean checkInvariant(){
		return ((pAuto+pBus+pBici+pPedone)==1);
	}
}
