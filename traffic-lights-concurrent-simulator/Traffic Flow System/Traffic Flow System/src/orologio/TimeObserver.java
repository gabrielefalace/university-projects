package orologio;

import java.util.Observer;

public interface TimeObserver extends Observer{
	public void update(int nuovoTempo);
}
