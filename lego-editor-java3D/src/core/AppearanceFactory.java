package core;

import java.util.Random;
import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
import javax.vecmath.Color3f;

/* 
 * PROMEMORIA COLORI - costruttore classe Material
 * 1. ambientColor
 * 2. emissiveColor
 * 3. diffuseColor
 * 4. specularColor 
 */
public final class AppearanceFactory {

	public static final float SHININESS = 120.0f;
	
	public static final byte TABLE_BRICK = 0;
	public static final byte RANDOM = 1;

	private static Random random = new Random();
	
	private static Color3f lastRandomColor;
	
	
	public static final Appearance createSimpleAppearance(Color3f color){
		Appearance result = new Appearance();
		result.setMaterial(new Material(color, color, color, color, SHININESS));
		return result;
	}
	
	
	public static final Appearance createAppearance(byte appearanceType){
		Appearance result = new Appearance();
		switch(appearanceType){
		case TABLE_BRICK:
			result = createTableBrickAppearance();
			break;
		case RANDOM:
			result = createRandomAppearance();
			break;
		default:
			result = createRandomAppearance();
			break;
		}
		return result;
	}
	
	
	
	private static final Appearance createTableBrickAppearance(){
		Appearance result = new Appearance();
		
		Color3f greyCol = new Color3f(0.7f, 0.7f, 0.7f);
		result.setMaterial(new Material(greyCol, greyCol, greyCol, greyCol, SHININESS));
		
		return result;
	}
	
	private static final Appearance createRandomAppearance(){
		Appearance result = new Appearance();
		
		// aspetto del nuovo mattoncino
		float red = random.nextFloat();
		float green = random.nextFloat();
		float blue = random.nextFloat();

		
		Color3f boxCol = new Color3f(red, green, blue);
		lastRandomColor = boxCol;
		
		result.setMaterial(new Material(boxCol, boxCol, boxCol, boxCol, SHININESS));
		return result;
	}
	
	public static final Color3f getLastRandomColor(){
		return lastRandomColor;
	}
}
