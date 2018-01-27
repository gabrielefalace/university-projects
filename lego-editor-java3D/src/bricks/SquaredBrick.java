package bricks;

import javax.media.j3d.Appearance;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;
import core.BrickType;
import core.XZPair;

public final class SquaredBrick implements Brick {
	
	private static final int NUMBER_OF_ELEMENTARYBRICKS = 4;
	
	private TransformGroup tg;
	private RectangularSmallBrick brickRight;
	private RectangularSmallBrick brickLeft;
	private XZPair[] xzPairList;
	private Appearance appearance;
	private double rotation;
	private Color3f color;
	
	/**
	 * Costruisce un mattoncino quadrato (2x2) composto da due mattoncini 
	 * rettangolari piccoli (2x1).
	 */
	public SquaredBrick(Appearance	appearance, double rotationAngle){
		xzPairList = new XZPair[NUMBER_OF_ELEMENTARYBRICKS];
		tg = new TransformGroup();
		this.appearance = appearance;
		rotation = rotationAngle;
		
		// Spostamento del primo rettangolo lungo X 
		brickRight  = new RectangularSmallBrick(appearance, NO_ROTATION);
		Transform3D brickRightTF = new Transform3D();
		brickRightTF.setTranslation(new Vector3f(ElementaryBrick.BRICK_SIZE*2, 0.0f, 0.0f));
		TransformGroup brickRightTG = brickRight.getTransformGroup();
		brickRightTG.setTransform(brickRightTF);
		
		
		brickLeft = new RectangularSmallBrick(appearance, NO_ROTATION);

		TransformGroup brickLeftTG = brickLeft.getTransformGroup();

		tg.addChild(brickLeftTG);
		tg.addChild(brickRightTG);
		
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
		brickLeft.setCylinderData(data);		
		String[] coords_ID = data.split("@");
		String[] coords = coords_ID[0].split(",");
		
		float x = Float.parseFloat(coords[0]);
		float z = Float.parseFloat(coords[1]);
		
		if(rotation==0.00)
			x = x + 2*ElementaryBrick.BRICK_SIZE;
		if(rotation==Math.PI/2)
			z = z - 2*ElementaryBrick.BRICK_SIZE;
		if(rotation==Math.PI)
			x = x - 2*ElementaryBrick.BRICK_SIZE;
		if(rotation==Math.PI*3/2)
			z = z + 2*ElementaryBrick.BRICK_SIZE;
		
		brickRight.setCylinderData(x+","+z+"@"+coords_ID[1]);
		
		xzPairList[0] = brickLeft.getXZPairList()[0];
		xzPairList[1] = brickLeft.getXZPairList()[1];
		xzPairList[2] = brickRight.getXZPairList()[0];
		xzPairList[3] = brickRight.getXZPairList()[1];
	}

	@Override
	public final XZPair[] getXZPairList() {
		return xzPairList;
	}
	

	@Override
	public final BrickType getType() {
		return BrickType.squared;
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
		brickLeft.setRotation(rotation);
		brickRight.setRotation(rotation);
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
