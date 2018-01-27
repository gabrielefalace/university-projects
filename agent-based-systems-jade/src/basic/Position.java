package basic;

import java.io.*;
import exceptions.IllegalMapOperationException;

public class Position implements Serializable{

	/**
	 * Coordinata x.
	 */
	public int row;
	
	/**
	 * Coordinata y.
	 */
	public int col;
	
	/**
	 * Costruisce una posizione in 2D (x,y).
	 * @param x
	 * @param y
	 */
	public Position(int r, int c){
		row = r;
		col = c;
	}
	
	
	/**
	 * Codice hash univoco ottenuto concatenando le due coordinate.
	 */
	public int hashCode(){
		return Integer.parseInt(row+""+col);
	}
	
	/**
	 * Confronta due oggetti Position: questi sono ritenuti uguali se hanno stesse coordinate.
	 */
	public boolean equals(Object o){
		if(!(o instanceof Position))
			return false;
		Position p = (Position)o;
		return (this.row==p.row && this.col == p.col);
	}
	
	/**
	 * rappresentazione a stringa di una position, come coppia 
	 * parentesizzata (<i>x</i>,<i>y</i>).
	 */
	public String toString(){
		return "("+row+","+col+")";
	}

}
