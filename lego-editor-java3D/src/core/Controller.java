package core;

import javax.vecmath.Color3f;

/**
 * Oggetto condiviso tra il pannello 3D ed il pannello dei comandi.
 * Serve per trasferire i dati relativi alle decisioni dell'utente.
 */
public final class Controller {

	// codici delle modalità di gioco
	public static final int FULL = 0;
	public static final int PRACTICE = 1;
	
	
	// codici delle operazioni sulla struttura
	public static final byte INSERT_BRICK = 0;
	public static final byte MOVE_BRICK = 1;
	public static final byte DELETE_BRICK = 2;
	
	// imposta l'operazione correntemente selezionata dall'utente
	private byte operationSelected;
	
	// indica il tipo di mattoncino che viene inserito
	private BrickType brickType;
	
	private double rotation;
	
	private Color3f color;
	
	private int gameMode;
	
	public Controller(int gameMode){
		setOperationSelected(INSERT_BRICK);
		setBrickType(BrickType.elementary);
		setRotation(0.0f);
		Color3f defaultColor = new Color3f(0.0f, 0.0f, 0.999f); 
		setColor(defaultColor);
		setGameMode(gameMode);
	}

	public final void setOperationSelected(byte anOperation) {
		operationSelected = anOperation;
	}

	public final byte getOperationSelected() {
		return operationSelected;
	}

	public final void setBrickType(BrickType aBrickType) {
		brickType = aBrickType;
	}

	public final BrickType getBrickType() {
		return brickType;
	}

	public final void setRotation(double d) {
		this.rotation = d;
	}

	public final double getRotation() {
		return rotation;
	}

	public final void setColor(Color3f color) {
		this.color = color;
	}

	public final Color3f getColor() {
		return color;
	}

	public final void setGameMode(int gameMode) {
		this.gameMode = gameMode;
	}

	public final int getGameMode() {
		return gameMode;
	}
}
