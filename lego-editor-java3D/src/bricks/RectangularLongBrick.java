package bricks;

import javax.media.j3d.Appearance;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;
import core.BrickType;
import core.XZPair;

public final class RectangularLongBrick implements Brick {
	
	private static final int NUMBER_OF_ELEMENTARYBRICKS = 4;
	
	private TransformGroup tg;
	private RectangularSmallBrick brickFront;
	private RectangularSmallBrick brickBack;
	private XZPair[] xzPairList;
	private Appearance appearance;
	private double rotation;
	private Color3f color;
		
	/**
	 * Costruisce un mattoncino rettangolare lungo (4x1) combinando due 
	 * mattoncini rettangolari corti (2x1), allineati lungo l'asse Z.
	 */
	public RectangularLongBrick(Appearance	appearance, double rotationAngle){
		xzPairList = new XZPair[NUMBER_OF_ELEMENTARYBRICKS];
		rotation = rotationAngle;
		tg = new TransformGroup();
		this.appearance = appearance;
		
		// Spostamento del primo mattoncino in modo che aggiungendo il secondo possa quest'ultimo essere messo centrato nell'origine
		brickFront = new RectangularSmallBrick(appearance, NO_ROTATION);
		Transform3D brickFrontTF = new Transform3D();
		brickFrontTF.setTranslation(new Vector3f(0.0f, 0.0f, ElementaryBrick.BRICK_SIZE*4));
		TransformGroup brickFrontTG = brickFront.getTransformGroup();
		brickFrontTG.setTransform(brickFrontTF);
		
		brickBack = new RectangularSmallBrick(appearance, NO_ROTATION);

		TransformGroup brickBackTG = brickBack.getTransformGroup();
		
		tg.addChild(brickFrontTG);
		tg.addChild(brickBackTG);
		
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

	public final void setCylinderData(String data){
		brickBack.setCylinderData(data);
		
		String[] dataTokens = data.split("@");
		String[] coordTokens = dataTokens[0].split(",");
		float x = Float.parseFloat(coordTokens[0]);
		float z = Float.parseFloat(coordTokens[1]);
		
		if(rotation==0.00)
			z = z+4*ElementaryBrick.BRICK_SIZE;
		if(rotation==Math.PI/2)
			x = x+4*ElementaryBrick.BRICK_SIZE;
		if(rotation==Math.PI)
			z = z-4*ElementaryBrick.BRICK_SIZE;
		if(rotation==Math.PI*3/2)
			x = x-4*ElementaryBrick.BRICK_SIZE;
		
		brickFront.setCylinderData(x+","+z+"@"+dataTokens[1]);
		
		xzPairList[0] = brickBack.getXZPairList()[0];
		xzPairList[1] = brickBack.getXZPairList()[1];
		xzPairList[2] = brickFront.getXZPairList()[0];
		xzPairList[3] = brickFront.getXZPairList()[1];
		
	}

	@Override
	public final XZPair[] getXZPairList() {
		return xzPairList;
	}
	
	@Override
	public final BrickType getType() {
		return BrickType.rectangular_long;
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
		brickBack.setRotation(rotation);
		brickFront.setRotation(rotation);
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
