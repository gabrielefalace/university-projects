package eitghGame;


public class Table {

	
	int[][] table;
	static final int WHITE = 9;
	static final int DIMENSION = 3;
	static final int BRANCHING_FACTOR = 4;
	
	public Table(int[][] t){
		table = t;
	}
		
	public boolean isFeasible(Direction direction){
		switch(direction){
		case UP:
			if(getWhiteRow()==0) return false; else return true;
		case DOWN: 
			if(getWhiteRow()==2) return false; else return true;
		case RIGHT:
			if(getWhiteColumn()==2) return false; else return true;
		case LEFT:
			if(getWhiteColumn()==0) return false; else return true;
		default:
			return true;
		}
	}
	
	public Table move(Direction direction){
		
		Table otherTable = (Table)clone();
		
		int whiteRow = getWhiteRow();
		int whiteCol = getWhiteColumn();
		
		switch(direction){		
		case UP:
			otherTable.table[whiteRow][whiteCol] = otherTable.table[whiteRow-1][whiteCol];
			otherTable.table[whiteRow-1][whiteCol] = WHITE;
			break;
		case DOWN: 
			otherTable.table[whiteRow][whiteCol] = otherTable.table[whiteRow+1][whiteCol];
			otherTable.table[whiteRow+1][whiteCol] = WHITE;
			break;
		case RIGHT:
			otherTable.table[whiteRow][whiteCol] = otherTable.table[whiteRow][whiteCol+1];
			otherTable.table[whiteRow][whiteCol+1] = WHITE;
			break;
		case LEFT:
			otherTable.table[whiteRow][whiteCol] = otherTable.table[whiteRow][whiteCol-1];
			otherTable.table[whiteRow][whiteCol-1] = WHITE;
			break;
		}
		return otherTable;
	}
	
	
	public void copyValues(Table t){
		for(int i=0; i<DIMENSION; i++)
			for(int j=0; j<DIMENSION; j++)
				table[i][j] = t.table[i][j];
	}
	
	
	
	
	public int getWhiteRow(){
		int rowIndex = -1;
		for(int i=0; i<table.length; i++){
			for(int j=0; j<table.length; j++){
				if(table[i][j]==WHITE){
					rowIndex = i;
				}
			}
		}
		return rowIndex;
	}
	
	public int getWhiteColumn(){
		int colIndex = -1;
		for(int i=0; i<table.length; i++){
			for(int j=0; j<table.length; j++){
				if(table[i][j]==WHITE){
					colIndex = j;
				}
			}
		}
		return colIndex;
	}
	
	
	public boolean equals(Object o){
		return (this.hashCode() == ((Table)o).hashCode()); 
	}
	
	
	public Object clone(){
		int[][] matrix = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}}; 
		Table t = new Table(matrix);
		for(int i=0; i<DIMENSION; i++)
			for(int j=0; j<DIMENSION; j++)
				t.table[i][j] = this.table[i][j];
		return t;
	}
	
	
	public int hashCode(){
		String code = "";
		for(int i=0; i<DIMENSION; i++)
			for(int j=0; j<DIMENSION; j++)
				code = code + table[i][j];
		return Integer.parseInt(code);
	}
	
	public String toString(){
		String s = "";
		for(int i=0; i<DIMENSION; i++){
			for(int j=0; j<DIMENSION; j++){
				s += " "+table[i][j]+" ";
			}
			s += " | ";
		}
		return s;
	}
}
