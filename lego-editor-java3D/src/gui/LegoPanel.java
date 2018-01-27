package gui;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.GraphicsConfiguration;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Locale;
import javax.media.j3d.PhysicalBody;
import javax.media.j3d.PhysicalEnvironment;
import javax.media.j3d.PointLight;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.media.j3d.VirtualUniverse;
import javax.swing.JPanel;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import table.Table;
import bricks.Brick;
import bricks.ElementaryBrick;
import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;
import core.AppearanceFactory;
import core.BrickFactory;
import core.BrickType;
import core.ClickHandler;
import core.Controller;

@SuppressWarnings("serial")
public class LegoPanel extends JPanel {
	
	private static final Point3d BOUNDS_CENTER = new Point3d(0.0,0.0,0.0);
	private static final double BOUNDS_RADIUS = Double.MAX_VALUE;
	
	private VirtualUniverse virtualUniverse;
	private Locale locale;
	private BranchGroup sceneGraph;
	private Canvas3D canvas3D;
	private BranchGroup viewBG;
	private BoundingSphere boundingSphere;
	
	Controller controller;
	ClickHandler clickBehaviour;
	TransformGroup tableTG;
	
	/**
	 * Costruisce un LegoPanel che può conoscere la 
	 * @param aController il Controller cui il pannello si riferisce per la semantica del click.
	 */
	public LegoPanel(Controller aController, Canvas3D canvas, String filePath){
		boundingSphere = new BoundingSphere(BOUNDS_CENTER, BOUNDS_RADIUS);
		controller = aController;
		
		canvas3D = canvas;
		
		virtualUniverse = new VirtualUniverse();
		locale = new Locale(virtualUniverse);
		
		buildSceneGraph(filePath);
		setLayout(new BorderLayout());	
		add("Center", canvas3D);
		
		setView();
		locale.addBranchGraph(sceneGraph);
		addLight();
		canvas3D.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		setVisible(true);
	}

	/**
	 * Aggiunge il BranchGroup delle luci al Locale dell'universo.
	 */
	private final void addLight() {
		BranchGroup bgLight = new BranchGroup();
		final Color3f white = new Color3f(0.8f,0.8f,0.8f);
		
		float x = Table.COLUMNS_NUMBER*ElementaryBrick.BRICK_SIZE/2;
		float y = 35.0f;
		float z = Table.ROWS_NUMBER*ElementaryBrick.BRICK_SIZE/2;
		Point3f position = new Point3f(x, y, z);
		Point3f attenuation = new Point3f(1.0f, 0.0032f, 0.0032f);
		PointLight lightP = new PointLight(true, white, position, attenuation);
		lightP.setInfluencingBounds(boundingSphere);
		
		bgLight.addChild(lightP);
		locale.addBranchGraph(bgLight);
	}

	/**
	 * Costruisce la geometria con il solo tavolo vuoto.
	 */
	private final void buildSceneGraph(String filePath) {
		sceneGraph = new BranchGroup();
		sceneGraph.setCapability(BranchGroup.ALLOW_DETACH);
	
		sceneGraph.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		sceneGraph.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
				
		Table table = new Table();
		tableTG = table.getTransformGroup();
		tableTG.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		 
		if(filePath.equals(""))
			clickBehaviour = new ClickHandler(canvas3D, sceneGraph, boundingSphere, tableTG, controller);
		else
			clickBehaviour = new ClickHandler(filePath, canvas3D, sceneGraph, boundingSphere, tableTG, controller);
		sceneGraph.addChild(clickBehaviour);			
		sceneGraph.addChild(tableTG); 
					
		setWhiteBackground(sceneGraph);
		
		sceneGraph.compile();
	}

	/**
	 * Imposta il BranchGroup View del Locale.
	 */
	private final void setView(){
		viewBG = new BranchGroup();
		TransformGroup viewTG = new TransformGroup();
		
		ViewingPlatform vp = new ViewingPlatform(1);
		vp.getViewPlatformTransform().setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		vp.getViewPlatformTransform().setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		
		vp.getViewPlatform().setViewAttachPolicy(View.RELATIVE_TO_FIELD_OF_VIEW);
		
		KeyNavigatorBehavior keyBehavior = new KeyNavigatorBehavior(vp.getViewPlatformTransform());
		keyBehavior.setSchedulingBounds(boundingSphere);
		keyBehavior.setEnable(true);
		vp.addChild(keyBehavior);
		
		
		viewTG.addChild(vp);
		viewBG.addChild(viewTG);
		
		Transform3D initial = new Transform3D();
		initial.setTranslation(new Vector3f(-20*ElementaryBrick.BRICK_SIZE, 0.0f, 0.0f));
		initial.rotX(-Math.PI/4);
		viewTG.setTransform(initial);
		
		View view = new View();
		view.setPhysicalBody(new PhysicalBody());
		view.setPhysicalEnvironment(new PhysicalEnvironment());
		view.attachViewPlatform(vp.getViewPlatform());
		
		locale.addBranchGraph(viewBG);
		
		view.addCanvas3D(canvas3D);
	}
	
	/**
	 * Imposta il backgroud per lo sceneGraph.
	 * @param sceneGraph lo sceneGraph cui impostare il backGround.
	 */
	public final void setWhiteBackground(BranchGroup sceneGraph){
		Background bg = new Background(new Color3f(1.0f,1.0f,1.0f));
	    bg.setApplicationBounds(boundingSphere);
	    sceneGraph.addChild(bg);
	}

	public final void clear(){
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		canvas3D = new Canvas3D(config);
		buildSceneGraph("");
		setLayout(new BorderLayout());	
		add("Center", canvas3D);
		
		setView();
		locale.addBranchGraph(sceneGraph);
		addLight();
		canvas3D.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	/**
	 * 
	 * @param filePath
	 */
	public final void restoreBricksOnTable(String filePath){
		File f = new File(filePath);
		InputStreamReader inStream = null;
		BufferedReader reader = null;
		try {
			inStream = new InputStreamReader(new FileInputStream(f));
			reader = new BufferedReader(inStream);
			while(true){
				String line = reader.readLine();
				if(line==null)
					return;
				if(line.equals("EOF"))
					return;
				if(line.startsWith("brick")){
					String[] brickToken = line.split("#");
					final int brickID = Integer.parseInt(brickToken[1]);
					
					String[] colorToken = brickToken[2].split(":");
					final float red = Float.parseFloat(colorToken[0]);
					final float green = Float.parseFloat(colorToken[1]);
					final float blue = Float.parseFloat(colorToken[2]);
					Color3f color = new Color3f(red, green, blue);
					
					BrickType brickType = BrickType.elementary;
					String brickTypeString =  brickToken[3];
					if(brickTypeString.equalsIgnoreCase("elementary")) brickType = BrickType.elementary;
					else if(brickTypeString.equalsIgnoreCase("l_shaped")) brickType = BrickType.l_shaped;
					else if(brickTypeString.equalsIgnoreCase("rectangular_long")) brickType = BrickType.rectangular_long;
					else if(brickTypeString.equalsIgnoreCase("rectangular_small")) brickType = BrickType.rectangular_small;
					else if(brickTypeString.equalsIgnoreCase("rectangular_mid")) brickType = BrickType.rectangular_mid;
					else if(brickTypeString.equalsIgnoreCase("squared")) brickType = BrickType.squared;
				
					double rotAngle = Double.parseDouble(brickToken[4]);
					
					float x = Float.parseFloat(brickToken[5]);
					
					float z = Float.parseFloat(brickToken[6]);
					
					HashMap<Integer, Float> constructionState = clickBehaviour.getConstructionState();
					final float insertionHeightTmp = constructionState.get(brickID);
					final float insertionHeight = clickBehaviour.getInsertedHeightFor(insertionHeightTmp-ElementaryBrick.BOX_Y*2);
					 
					restoreBrick(brickID, brickType, color, x, z, insertionHeight+ElementaryBrick.BOX_Y, rotAngle);
				}
			}
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			try{
				if(inStream != null)
					inStream.close();
				if(reader != null)
					reader.close();
			}
			catch(Exception exc){ 
				exc.printStackTrace();
			}
		}
	}
	
	/**
	 * Ripristina un Brick con le caratteritiche precisate nel punto indicato dai parametri.
	 * @param brickType
	 * @param color
	 * @param x
	 * @param z
	 * @param insertHeight
	 * @param rotationAngle
	 */
	public final void restoreBrick(int brickID, BrickType brickType, Color3f color, float x, float z, float insertionHeight, double rotationAngle){
		Appearance appearance = AppearanceFactory.createSimpleAppearance(color);
		Brick aBrick = BrickFactory.createBrick(brickType, appearance);
		BranchGroup aBrickBG = new BranchGroup();
		aBrickBG.setCapability(BranchGroup.ALLOW_DETACH);
		Transform3D rotation = new Transform3D();
		rotation.rotY(rotationAngle);
		
		Transform3D transform3D = new Transform3D(); 
		transform3D.setTranslation(new Vector3f(x, insertionHeight, z));
		transform3D.mul(rotation);
		
		aBrick.getTransformGroup().setTransform(transform3D);
		aBrickBG.addChild(aBrick.getTransformGroup());
		
		// ripristino dei dati del brick 
		aBrick.setCylinderData(x+","+z+"@"+brickID);
		
		// carica la brickMap e la brickBranchgroupMap
		clickBehaviour.insertBrickAndBranchGroup(brickID, aBrick, aBrickBG);
		
		// ripristino del colore del brick
		aBrick.setColor(color);
		aBrick.setRotation(rotationAngle);
		
		sceneGraph.addChild(aBrickBG);
	}
}
