package veicoli;

public abstract class Veicolo {
	
	protected int maxSpeed;
	protected int speed;
	protected double length;
	
	public int getMaxSpeed(){
		return maxSpeed;
	}
	
	public int getSpeed(){
		return speed;
	}
	
	public double getLunghezza(){
		return length;
	}
	
	public void accelera(int deltaV){
		if((speed+deltaV) <= maxSpeed )
			speed += deltaV;
		else
			speed = maxSpeed;
	}
	
	public void decelera(int deltaV){
		if((speed-deltaV) >0)
			speed -= deltaV;
		else
			speed = 0;
	}

	
	public boolean checkInvariant(){
		if(speed < 0 || speed > maxSpeed || length < 0 || length > 18)
			return false;
		return true;
	}
}
