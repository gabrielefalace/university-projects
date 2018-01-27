package bricks;

import javax.media.j3d.Appearance;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;
import core.BrickType;
import core.XZPair;

public final class RectangularSmallBrick implements Brick {

	private static final int NUMBER_OF_ELEMENTARYBRICKS = 2;

	private TransformGroup tg;
	private ElementaryBrick brickFront;
	private ElementaryBrick brickBack;
	
	private XZPair[] xzPairList;
	private Appearance appearance;
	
	private double rotation;
	private Color3f color;
	
	/**
	 * Costruisce un mattoncino rettangolare piccolo (2x1) mettendo due 
	 * mattoncini elementari (1x1) allineati lungo l'asse Z.
	 */
	public RectangularSmallBrick(Appearance	appearance, double rotationAngle){
		xzPairList = new XZPair[NUMBER_OF_ELEMENTARYBRICKS];
		this.appearance = appearance;
		rotation = rotationAngle;
		// Spostamento del primo mattoncino elementare in avanti in modo che il secondo sia centrato nell'origine
		brickFront = new ElementaryBrick(appearance);
		Transform3D brickFrontTF = new Transform3D();
		brickFrontTF.setTranslation(new Vector3f(0.0f, 0.0f, ElementaryBrick.BRICK_SIZE*2));
		TransformGroup brickFrontTG = brickFront.getTransformGroup();
		brickFrontTG.setTransform(brickFrontTF);
		
		
		brickBack = new ElementaryBrick(appearance);
		TransformGroup brickBackTG = brickBack.getTransformGroup();

		tg = new TransformGroup();
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

	@Override
	public final void setCylinderData(String data){
		brickBack.setCylinderData(data);
		
		String[] coords_ID = data.split("@");
		String[] coords = coords_ID[0].split(",");
		
		float x = Float.parseFloat(coords[0]);
		float z = Float.parseFloat(coords[1]);

		if(rotation==0.00)
			z = z + 2*ElementaryBrick.BRICK_SIZE;
		if(rotation==Math.PI/2)
			x = x + 2*ElementaryBrick.BRICK_SIZE;
		if(rotation==Math.PI)
			z = z - 2*ElementaryBrick.BRICK_SIZE;
		if(rotation==Math.PI*3/2)
			x = x - 2*ElementaryBrick.BRICK_SIZE;
		
		brickFront.setCylinderData(x+","+z+"@"+coords_ID[1]);
		
		xzPairList[0] = brickBack.getXZPairList()[0];
		xzPairList[1] = brickFront.getXZPairList()[0];
	}

	@Override
	public final XZPair[] getXZPairList() {
		return xzPairList;
	}


	@Override
	public final BrickType getType() {
		return BrickType.rectangular_small;
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
