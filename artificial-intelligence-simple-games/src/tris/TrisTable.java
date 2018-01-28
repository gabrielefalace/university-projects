package tris;

public class TrisTable {

	public static final int X = 1;
	public static final int O = 2;
	public static final int EMPTY = 0;
	
	public static final int DIMENSION = 3;
	
	private int[][] cells;
	private volatile int turn;
	
	public TrisTable(){
		turn = X;
		cells = new int[DIMENSION][DIMENSION];
		for(int i=0; i<DIMENSION; i++)
			for(int j=0; j<DIMENSION; j++)
				cells[i][j] = EMPTY;
	}
	
	
	public boolean isEmpty(int row, int col){
		return (cells[row][col] == EMPTY);
	}
	
	
	public synchronized void play(int row, int col, int player_symbol){
		cells[row][col] = player_symbol;
		if(player_symbol==X)
			turn = O;
		else
			turn = X;
	}
	
	public synchronized int getTurn(){
		return turn;
	}
}
