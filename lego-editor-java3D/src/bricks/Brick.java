package bricks;

import javax.media.j3d.Appearance;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import core.BrickType;
import core.XZPair;

public interface Brick {
	
	public static final double NO_ROTATION = 0.0000;
	
	public static final float BRICK_SHININESS = 10.0f;
	
	public static final boolean RED_CYLINDER_NO = false;
	
	public static final boolean RED_CYLINDER_YES = true;
	
	/**
	 * Consente di ottenere un handler per manipolare la TransformGroup del mattoncino.  
	 * @return La TransformGroup dell'intero mattoncino.
	 */
	public TransformGroup getTransformGroup();
	
	/**
	 * Imposta i dati di tutti i cilindri di un mattoncino.
	 * @param data
	 */
	public void setCylinderData(String data);

	/**
	 * Consente di ottenere il vettore di coppie (x,z) di ciascun cilindro costituente il brick.
	 * @return il vettore di coppie (x,z).
	 */
	public XZPair[] getXZPairList();
	
	public BrickType getType();
	
	public Appearance getAppearance();
	
	public double getRotation();

	public void setRotation(double rotation);
	
	public XZPair getReferenceCylinder();
	
	public Color3f getColor();
	
	public void setColor(Color3f color);
}
