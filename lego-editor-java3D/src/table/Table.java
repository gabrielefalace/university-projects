package table;

import javax.media.j3d.Appearance;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;
import core.AppearanceFactory;
import bricks.Brick;
import bricks.ElementaryBrick;

public final class Table {
	
	public static final int ROWS_NUMBER = 30;
	public static final int COLUMNS_NUMBER = 50;
	private static final int HEIGHT_DIVISOR = 7;
	
	public static float height;
	private ElementaryBrick[][] table;
	private TransformGroup tableTG;
	
	public Table(){
		// La stessa appearance per tutti i mattoncini che compongono il tavolo.
		Appearance tableAppearance = AppearanceFactory.createAppearance(AppearanceFactory.TABLE_BRICK);
		
		table = new ElementaryBrick[ROWS_NUMBER][COLUMNS_NUMBER];
		for(int i=0; i<ROWS_NUMBER; i++)	
			for(int j=0; j<COLUMNS_NUMBER; j++)
				table[i][j] = new ElementaryBrick(tableAppearance, HEIGHT_DIVISOR, Brick.RED_CYLINDER_YES, Brick.NO_ROTATION);
		
		tableTG = createTransformGroup();
		tableTG.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);

		height = ElementaryBrick.BOX_Y/HEIGHT_DIVISOR+(ElementaryBrick.BOX_Z/(3*HEIGHT_DIVISOR));
	}
	
	private final TransformGroup createTransformGroup() {
		tableTG = new TransformGroup();
		
		Transform3D brickTF;

		final float brickSize = ElementaryBrick.BRICK_SIZE;
		for(int drop = 0; drop<ROWS_NUMBER; drop++){
			for(int trasl = 0; trasl<COLUMNS_NUMBER; trasl++){
				
				brickTF = new Transform3D();
				final float x = 2*brickSize*trasl;
				final float z = 2*brickSize*drop;
				brickTF.setTranslation(new Vector3f(x, 0.0f, z));
				
				table[drop][trasl].setCylinderData(x+","+z+"@"+0);

				table[drop][trasl].getTransformGroup().setTransform(brickTF);
				
				tableTG.addChild(table[drop][trasl].getTransformGroup());
			}
		}
		return tableTG;
	}

	public final TransformGroup getTransformGroup(){
		return tableTG;
	}
	
}
