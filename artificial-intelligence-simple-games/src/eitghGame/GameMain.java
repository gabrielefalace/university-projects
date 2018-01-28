package eitghGame;

public class GameMain {
	
	public static void main(String[] args){
		int[][] matrix = {{2, 3, Table.WHITE},{1, 8, 4},{7, 6, 5}};
		Table t = new Table(matrix);
		new EightGameGUI(t);
	}
	
}
