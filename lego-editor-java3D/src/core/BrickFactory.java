package core;

import javax.media.j3d.Appearance;
import bricks.Brick;
import bricks.ElementaryBrick;
import bricks.LShapedBrick;
import bricks.RectangularLongBrick;
import bricks.RectangularMidBrick;
import bricks.RectangularSmallBrick;
import bricks.SquaredBrick;

public final class BrickFactory {

	public static final Brick createBrick(BrickType type, Appearance appearance){
		Brick brick = null;
		switch(type){
		case elementary:
			brick = new ElementaryBrick(appearance);
			break;
		case rectangular_small:
			brick = new RectangularSmallBrick(appearance, 0.00);
			break;
		case rectangular_mid:
			brick = new RectangularMidBrick(appearance, 0.00);
			break;
		case squared:
			brick = new SquaredBrick(appearance, 00);
			break;
		case rectangular_long:
			brick = new RectangularLongBrick(appearance, 0.00);
			break;
		case l_shaped:
			brick = new LShapedBrick(appearance, 0);
			break;
		}
		return brick;
	}
	
	
}
