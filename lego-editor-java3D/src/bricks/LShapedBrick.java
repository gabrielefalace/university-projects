package bricks;

import javax.media.j3d.Appearance;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;
import core.BrickType;
import core.XZPair;

public final class LShapedBrick implements Brick {

	private static final int NUMBER_OF_ELEMENTARYBRICKS = 4;
	
	private TransformGroup tg;
	private ElementaryBrick singleBrick;
	private RectangularMidBrick tripleBrick;
	private Appearance appearance;
	
	private double rotation;
	private Color3f color;
	
	private XZPair[] xzPairList;
	
	/**
	 * Costruisce un mattoncino a forma di L (3x1) combinando un 
	 * mattoncino rettangolari medio (3x1) ed un mattoncino elementare,
	 * allineati lungo l'asse Z, in modo che il cilindro centrale del rettangolo medio rimanga centrato nell'origine.
	 */
	public LShapedBrick(Appearance appearance, double rotationAngle){
		xzPairList = new XZPair[NUMBER_OF_ELEMENTARYBRICKS];
		this.appearance = appearance;
		rotation = rotationAngle;
		tg = new TransformGroup();
		
		singleBrick = new ElementaryBrick(appearance);
		tripleBrick = new RectangularMidBrick(appearance, NO_ROTATION);
		
		// Nessun spostamento
		TransformGroup tripleBrickTG = tripleBrick.getTransformGroup();

		// Spostamento del mattoncino singolo a destra
		TransformGroup singleBrickTG = singleBrick.getTransformGroup();
		Transform3D singleBrickTF = new Transform3D();
		singleBrickTF.setTranslation(new Vector3f(ElementaryBrick.BRICK_SIZE*2, 0.0f, ElementaryBrick.BRICK_SIZE*4));
		singleBrickTG.setTransform(singleBrickTF);
		
		tg.addChild(tripleBrickTG);
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
		tripleBrick.setCylinderData(data);
		String[] dataTokens = data.split("@");
		String[] coordTokens = dataTokens[0].split(",");
		float x = Float.parseFloat(coordTokens[0]);
		float z = Float.parseFloat(coordTokens[1]);
		
		if(rotation==0.00){
			x = x+2*ElementaryBrick.BRICK_SIZE;
			z = z+4*ElementaryBrick.BRICK_SIZE;
		}
		if(rotation==Math.PI/2){
			z = z-2*ElementaryBrick.BRICK_SIZE;
			x = x+4*ElementaryBrick.BRICK_SIZE;
		}
		if(rotation==Math.PI){
			x = x-2*ElementaryBrick.BRICK_SIZE;
			z = z-4*ElementaryBrick.BRICK_SIZE;
		}
		if(rotation==Math.PI*3/2){
			z = z+2*ElementaryBrick.BRICK_SIZE;
			x = x-4*ElementaryBrick.BRICK_SIZE;
		}
		
		
		singleBrick.setCylinderData(x+","+z+"@"+dataTokens[1]);
		
		xzPairList[0] = singleBrick.getXZPairList()[0];
		xzPairList[1] = tripleBrick.getXZPairList()[0];
		xzPairList[2] = tripleBrick.getXZPairList()[1];
		xzPairList[3] = tripleBrick.getXZPairList()[2];
	}

	@Override
	public final XZPair[] getXZPairList() {
		return xzPairList;
	}

	@Override
	public final BrickType getType() {
		return BrickType.l_shaped;
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
		tripleBrick.setRotation(rotation);
		singleBrick.setRotation(rotation);
	}

	@Override
	public final XZPair getReferenceCylinder() {
		return xzPairList[2];
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
