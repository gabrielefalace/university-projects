package bricks;

import javax.media.j3d.Appearance;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;

import core.BrickType;
import core.XZPair;


public final class RectangularMidBrick implements Brick {

	private static final int NUMBER_OF_ELEMENTARYBRICKS = 3;
	private TransformGroup tg;
	
	private ElementaryBrick singleBrick;
	private RectangularSmallBrick doubleBrick;
	private XZPair[] xzPairList;
	private Appearance appearance;
	
	private double rotation;
	private Color3f color;
	
	/**
	 * Costruisce un mattoncino rettangolare medio (3x1) mettendo un mattoncino elementare
	 * dietro uno rettangolare piccolo (2x1). I due mattoncini risultano affiancati lungo l'asse Z.
	 */
	public RectangularMidBrick(Appearance appearance, double rotationAngle){
		xzPairList = new XZPair[NUMBER_OF_ELEMENTARYBRICKS];
		rotation = rotationAngle;
		tg = new TransformGroup();
		this.appearance = appearance;
		singleBrick = new ElementaryBrick(appearance);
		doubleBrick = new RectangularSmallBrick(appearance, NO_ROTATION);
		
		// Spostamento del mattoncino single 
		TransformGroup singleBrickTG = singleBrick.getTransformGroup();
		Transform3D singleBrickTF = new Transform3D();
		singleBrickTF.setTranslation(new Vector3f(0.0f, 0.0f, ElementaryBrick.BRICK_SIZE*4));
		singleBrickTG.setTransform(singleBrickTF);
		
		TransformGroup doubleBrickTG = doubleBrick.getTransformGroup();

		tg.addChild(doubleBrickTG);
		tg.addChild(singleBrickTG);
		
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
		String[] dataTokens = data.split("@");
		String[] coordTokens = dataTokens[0].split(",");
		float x = Float.parseFloat(coordTokens[0]);
		float z = Float.parseFloat(coordTokens[1]);
		
		doubleBrick.setCylinderData(data);
		
		if(rotation==0.00)
			z = z+ElementaryBrick.BRICK_SIZE*4;
		if(rotation==Math.PI/2)
			x = x+ElementaryBrick.BRICK_SIZE*4;
		if(rotation==Math.PI)
			z = z-ElementaryBrick.BRICK_SIZE*4;
		if(rotation==Math.PI*3/2)
			x = x-ElementaryBrick.BRICK_SIZE*4;
		
		
		singleBrick.setCylinderData(x+","+z+"@"+dataTokens[1]);
		
		xzPairList[0] = singleBrick.getXZPairList()[0];
		xzPairList[1] = doubleBrick.getXZPairList()[0];
		xzPairList[2] = doubleBrick.getXZPairList()[1];
	}

	@Override
	public final XZPair[] getXZPairList() {
		return xzPairList;
	}
	

	@Override
	public final BrickType getType() {
		return BrickType.rectangular_mid;
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
		singleBrick.setRotation(rotation);
		doubleBrick.setRotation(rotation);
	}

	@Override
	public final XZPair getReferenceCylinder() {
		return xzPairList[1];
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
