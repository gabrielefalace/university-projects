package controller;

public class FiniteStateMachine {

	private State currentState;
	
	public State currentState(){
		return currentState;
	}
	
	public void transition(State s){
		currentState = s;
		s.action();
	}
	
}
