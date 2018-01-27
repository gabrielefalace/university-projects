package bricks;

import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Cylinder;
import core.BrickType;
import core.XZPair;

public final class ElementaryBrick implements Brick {
	
	private static final int NUMBER_OF_ELEMENTARYBRICKS = 1;
	private static final int NO_DIVISION = 1;
	
	public static final float BOX_X = 0.026f;
	public static final float BOX_Y = 0.03f;
	public static final float BOX_Z = 0.003f;
	
	public static final float BRICK_SIZE = BOX_X + BOX_Z;
	
	private static final float CYLINDER_Y = 0.011f;
	private static final float CYLINDER_RADIUS = 0.02f;	
	
	private TransformGroup tg;
	
	private Box boxFront, boxBack, boxRight, boxLeft, boxTop;
	private Cylinder cylinder;
	
	private XZPair[] xzPairList;
	
	private Appearance appearance;
	
	private double rotation;
	
	private Color3f color;

	/**
	 * Costruisce un mattoncino elementare (1x1) con altezza unitaria utilizzando le primitive Box e Cylinder.
	 * @param appearance la definizione dell'aspetto del mattoncino.
	 */
	public ElementaryBrick(Appearance appearance){
		this(appearance, NO_DIVISION, RED_CYLINDER_NO, NO_ROTATION);
	}
	
	
	/**
	 * Costruisce un mattoncino elementare (1x1) con altezza scalata utilizzando le primitive Box e Cylinder.
	 * @param appearance la definizione dell'aspetto del mattoncino.
	 * @param divisor il fattore di scalatura dell'altezza.
	 */
	public ElementaryBrick(Appearance appearance, float divisor, boolean redCylinder, double rotationAngle){
		xzPairList = new XZPair[NUMBER_OF_ELEMENTARYBRICKS];
		this.appearance = appearance;
		rotation = rotationAngle;
		
		boxBack = new Box(BOX_X, BOX_Y/divisor, BOX_Z, appearance);
		boxFront = new Box(BOX_X, BOX_Y/divisor, BOX_Z, appearance);
		boxRight = new Box(BOX_X, BOX_Y/divisor, BOX_Z, appearance);
		boxLeft = new Box(BOX_X, BOX_Y/divisor, BOX_Z, appearance);
		
		boxTop = new Box(BOX_X+BOX_Z, BOX_X+BOX_Z, (BOX_Z/3)/divisor, appearance);
		
		// Back Box Settings
		Transform3D boxBackTF = new Transform3D();
		boxBackTF.setTranslation(new Vector3f(BOX_Z, 0.0f, -BOX_X));
		TransformGroup boxBackTG = new TransformGroup(boxBackTF);
		boxBackTG.addChild(boxBack);
		
		// Front Box Settings
		Transform3D boxFrontTF = new Transform3D();
		boxFrontTF.setTranslation(new Vector3f(-BOX_Z, 0.0f, BOX_X));
		TransformGroup boxFrontTG = new TransformGroup(boxFrontTF);
		boxFrontTG.addChild(boxFront);

		// Right Box Settings
		Transform3D boxRightTF = new Transform3D();
		Transform3D boxRightRotation = new Transform3D();
		boxRightTF.setTranslation(new Vector3f(BOX_X, 0.0f, BOX_Z));
		boxRightRotation.rotY(Math.PI/2);
		boxRightTF.mul(boxRightRotation);
		TransformGroup boxRightTG = new TransformGroup(boxRightTF);
		boxRightTG.addChild(boxRight);
		
		// Left Box Settings
		Transform3D boxLeftTF = new Transform3D();
		Transform3D boxLeftRotation = new Transform3D();
		boxLeftTF.setTranslation(new Vector3f(-BOX_X, 0.0f, -BOX_Z));
		boxLeftRotation.rotY(Math.PI/2);
		boxLeftTF.mul(boxLeftRotation);
		TransformGroup boxLeftTG = new TransformGroup(boxLeftTF);
		boxLeftTG.addChild(boxLeft);
		
		// Top Box Settings
		Transform3D boxTopTF = new Transform3D();
		Transform3D boxTopRotation = new Transform3D();
		boxTopTF.setTranslation(new Vector3f(0.0f, BOX_Y/divisor, 0.0f));
		boxTopRotation.rotX(Math.PI/2);
		boxTopTF.mul(boxTopRotation);
		TransformGroup boxTopTG = new TransformGroup(boxTopTF);
		boxTopTG.addChild(boxTop);
		
		TransformGroup brickTG = new TransformGroup();
		brickTG.addChild(boxBackTG);
		brickTG.addChild(boxFrontTG);
		brickTG.addChild(boxRightTG);
		brickTG.addChild(boxLeftTG);
		brickTG.addChild(boxTopTG);
		
		// Cylinder settings
		if(redCylinder==true){
			Appearance redApp = new Appearance();
			Color3f redColor = new Color3f(1f, 0.0f, 0.0f);
			redApp.setMaterial(new Material(redColor, redColor, redColor, redColor, BRICK_SHININESS));
			cylinder = new Cylinder(CYLINDER_RADIUS, CYLINDER_Y/divisor, redApp);
		}
		else
			cylinder = new Cylinder(CYLINDER_RADIUS, CYLINDER_Y/divisor, appearance);
		
		Transform3D cylinderTF = new Transform3D();
		Float yOffset = boxFront.getYdimension()+boxTop.getZdimension()+cylinder.getHeight()/2;
		cylinderTF.setTranslation(new Vector3f(0.0f, yOffset, 0.0f));
		TransformGroup cylinderTG = new TransformGroup(cylinderTF);
		cylinderTG.addChild(cylinder);
		brickTG.addChild(cylinderTG);
	
		tg = brickTG;
		
		if(rotationAngle!=0){
			Transform3D rotationTransf = new Transform3D();
			rotationTransf.rotY(rotationAngle);
			tg.setTransform(rotationTransf);
		}
	}
	
	@Override
	public final TransformGroup getTransformGroup() {
		return tg;
	}
	
	@Override
	public final void setCylinderData(String data){
		XZPair xzPair = new XZPair();
		String[] tokens = data.split("@");
		String[] coordinates = tokens[0].split(",");
		xzPair.x = Float.parseFloat(coordinates[0]);
		xzPair.z = Float.parseFloat(coordinates[1]);
		cylinder.setUserData(data);
		xzPairList[0] = xzPair;
	}


	@Override
	public final XZPair[] getXZPairList() {
		return xzPairList;
	}


	@Override
	public final BrickType getType() {
		return BrickType.elementary;
	}


	@Override
	public final Appearance getAppearance() {
		return appearance;
	}
	
	@Override
	public final double getRotation() {
		return rotation;
	}
	
	@Override
	public final void setRotation(double rotation) {
		this.rotation = rotation;
	}


	@Override
	public final XZPair getReferenceCylinder() {
		return xzPairList[0];
	}


	@Override
	public final Color3f getColor() {
		return color;
	}


	@Override
	public final void setColor(Color3f color) {
		this.color = color;
	}
}
