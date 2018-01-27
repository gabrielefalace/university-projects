package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import javax.media.j3d.Appearance;
import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;
import table.Table;
import bricks.Brick;
import bricks.ElementaryBrick;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.picking.behaviors.PickMouseBehavior;

public final class ClickHandler extends PickMouseBehavior {
	
	// ampiezza del raggio che colpisce il modello 3D (tolerance)
	public final static float DELTA = 1.4E-43f;
	
	private BranchGroup sceneGraph;
	
	// semantica: brickID, insertHeight+2*BRICK_SIZE
	private HashMap<Integer, Float> constructionState;

	// semantica: insertHeight; lista di coppie occupate
	private HashMap<Float, LinkedList<XZPair>> occupationMap;
	
	private int brickID;
	
	// semantica: brickID, Brick
	private HashMap<Integer, Brick> bricksMap;
	private HashMap<Integer, BranchGroup> brickBranchGroupMap;
	
	private Controller controller;
	
	// Supporto alla move
	private Brick tmpBrick;
	private boolean selectedForMove;
	private double tmpBrickRotation;
	
	
	public ClickHandler(String filePath, Canvas3D canvas, BranchGroup scene, Bounds bounds, TransformGroup tableTG, Controller aController) {
		super(canvas, scene, bounds);
		sceneGraph = scene;
		controller = aController;
		
		tmpBrick = null;
		selectedForMove = false;
		
		constructionState = new HashMap<Integer, Float>();
		occupationMap = new HashMap<Float, LinkedList<XZPair>>();
		
		// verranno popolate con il metodo insertBrickAndBranchGroup()
		bricksMap = new HashMap<Integer, Brick>();
		brickBranchGroupMap = new HashMap<Integer, BranchGroup>();
		
		// metodi che popolano le strutture dati
		loadConstructionState(filePath);
		loadOccupationMap(filePath);
		
		// impostare brickID al più grande brickID di bricksMap
		Set<Integer> brickKeySet = constructionState.keySet();
		int brickMax = -1;
		
		for(int value: brickKeySet){
			if(value>brickMax)
				brickMax = value;
		}
		
		brickID = brickMax+1;
		
//		System.out.println("Il primo id selezionato per iniziare e' "+brickID+" (era stato trovato un massimo di "+brickMax+")");
		
		setTolerance(DELTA);
//		System.out.println("Tolerance set to "+DELTA);
		
		setSchedulingBounds(bounds);
	}
	
	public ClickHandler(Canvas3D canvas, BranchGroup scene, Bounds bounds, TransformGroup tableTG, Controller aController) {
		super(canvas, scene, bounds);
		sceneGraph = scene;
		controller = aController;
		
		tmpBrick = null;
		selectedForMove = false;
		
		constructionState = new HashMap<Integer, Float>();
		occupationMap = new HashMap<Float, LinkedList<XZPair>>();
		bricksMap = new HashMap<Integer, Brick>();
		brickBranchGroupMap = new HashMap<Integer, BranchGroup>();
		
		brickID = 0;
		
		// inseriamo il tavolo nella mappa
		constructionState.put(brickID, Table.height);
		brickID++;
		// Nota: dopo ogni inserimento va incrementato!
		
		setTolerance(DELTA);
//		System.out.println("Tolerance set to "+DELTA);
		
		setSchedulingBounds(bounds);
	}
	
	
	public final void saveToFile(File savePath){
		PrintWriter writer = null;
		FileOutputStream output = null;
		try{
			output = new FileOutputStream(savePath);
			writer = new PrintWriter(output);
			// write game mode
			writer.println(controller.getGameMode());
			
			// write om
			Set<Float> omKeySet = occupationMap.keySet();
			for(Float key: omKeySet){
				LinkedList<XZPair> xzPairList = occupationMap.get(key);
				String rowToWrite = "om:"+key+":";
				String tmpList = "";
				if(xzPairList.isEmpty())
					tmpList += "empty#";
				else{
					for(XZPair xzPair: xzPairList){
						tmpList += xzPair.x+","+xzPair.z+"#";
					}
				}
				String list = "";
				if(tmpList.length() != 0)
					list = tmpList.substring(0, tmpList.length()-1);
				writer.println(rowToWrite+list);
			}
			
			// write cs
			Set<Integer> csKeySet = constructionState.keySet();
			for(Integer key: csKeySet){
				writer.println("cs:"+key+":"+constructionState.get(key));
			}
			
			// write bricks
			Set<Integer> bmKeySet = bricksMap.keySet();
			for(Integer brickID: bmKeySet){
				Brick currBrick = bricksMap.get(brickID);
				Color3f color = currBrick.getColor();
				XZPair pair = currBrick.getReferenceCylinder();
				
				float red = color.x;
				float green = color.y;
				float blue = color.z;
				BrickType type = currBrick.getType();
				double rotAngle = currBrick.getRotation();
				float x = pair.x;
				float z = pair.z;
				
				String brickROW = "brick#"+brickID+"#"+red+":"+green+":"+blue+"#"+type+"#"+rotAngle+"#"+x+"#"+z;
				writer.println(brickROW);
			}
			writer.println("EOF");
		}
		catch(Exception err){
			err.printStackTrace();
		}
		finally{
			if(writer != null)
				writer.close();
			if(output != null)
				try {output.close();} 
				catch (IOException e) {	
					e.printStackTrace();
				}
		}
		
	}
	
	private final void loadOccupationMap(String filePath) {
		FileInputStream fileInputStream = null;
		InputStreamReader inStream = null;
		try {
			File file = new File(filePath);
			fileInputStream = new FileInputStream(file);
			inStream = new InputStreamReader(fileInputStream);
			BufferedReader reader = new BufferedReader(inStream);
			String mapEntry = reader.readLine();
			while(!mapEntry.startsWith("om")){
				mapEntry = reader.readLine();
			}
			while(mapEntry.startsWith("om")){
				String[] mapEntryToken = mapEntry.split(":");
				float height = Float.parseFloat(mapEntryToken[1]);
				String xzPair = mapEntryToken[2];
				LinkedList<XZPair> xzList= new LinkedList<XZPair>();
				if(!xzPair.equals("empty")){
					String[] xzPairList = xzPair.split("#");
					float x;
					float z;
					for(int i=0; i<xzPairList.length; i++){
						x = Float.parseFloat(xzPairList[i].split(",")[0]);
						z = Float.parseFloat(xzPairList[i].split(",")[1]);
						xzList.add(new XZPair(x, z));
					}
				}
				occupationMap.put(height, xzList);
				mapEntry = reader.readLine();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			try{
				if(fileInputStream!=null)
					fileInputStream.close();
				if(inStream!=null)
					inStream.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private final void loadConstructionState(String filePath) {
		FileInputStream fileInputStream = null;
		InputStreamReader inStream = null;
		try {
			File file = new File(filePath);
			fileInputStream = new FileInputStream(file);
			inStream = new InputStreamReader(fileInputStream);
			BufferedReader reader = new BufferedReader(inStream);
			String mapEntry = reader.readLine();
			while(!mapEntry.startsWith("cs")){
				mapEntry = reader.readLine();
			}
			while(mapEntry.startsWith("cs")){
				String[] mapEntryToken = mapEntry.split(":");
				int brickID = Integer.parseInt(mapEntryToken[1]);
				float height = Float.parseFloat(mapEntryToken[2]);
				constructionState.put(brickID, height);
				mapEntry = reader.readLine();
			}
			
			// stampa della mappa acquisita
//			System.out.println("Mappa caricata dal file");
//			Set<Integer> ids = constructionState.keySet();
//			for(Integer id: ids)
//				System.out.println(id+" - "+constructionState.get(id));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			try{
				if(fileInputStream!=null)
					fileInputStream.close();
				if(inStream!=null)
					inStream.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	

	@Override
	public final void updateScene(int xPos, int yPos) {
	
		Primitive clickedCylinder = null;
		Primitive pickedPrimitive = null;
		super.pickCanvas.setShapeLocation(xPos, yPos);
		PickResult[] results = super.pickCanvas.pickAllSorted();
			
		if(results!=null){
			pickedPrimitive = findFirstCylinderHit(results);
			if(pickedPrimitive==null)
				return;	
		}
		else
			return;
		
		if(!(pickedPrimitive instanceof Cylinder) && !(pickedPrimitive instanceof Box))
			return;
		
		// E' stato cliccato un cilindro
		if(pickedPrimitive instanceof Cylinder){
			clickedCylinder = pickedPrimitive;
			if(controller.getOperationSelected()==Controller.INSERT_BRICK){
				insertBrickOn(clickedCylinder);
				selectedForMove = false;
				tmpBrick = null;
			}
			if(controller.getOperationSelected()==Controller.MOVE_BRICK){
				if(selectedForMove==true)
					insertBrickOn(clickedCylinder);
				else
					selectBrickForMove(clickedCylinder);
			}
			if(controller.getOperationSelected()==Controller.DELETE_BRICK)
				deleteBrickOn(clickedCylinder);			
		}		
	}

	
	private final void selectBrickForMove(Primitive clickedCylinder) {
		String userData = (String)clickedCylinder.getUserData();
		String[] tokens = userData.split("@");
		Integer clickedID = Integer.parseInt(tokens[1]);
		
		// i brick del tavolo non possono essere mossi
		if(clickedID == 0) 
			return;
		
		tmpBrick = bricksMap.get(clickedID);
		
		if(!canBeEliminatedOrMoved(clickedID, tmpBrick))
			return;
		
		selectedForMove = true;
		tmpBrickRotation = tmpBrick.getRotation();
		
		deleteBrickOn(clickedCylinder);
	}

	private final void deleteBrickOn(Primitive clickedCylinder) {
		String userData = (String)clickedCylinder.getUserData();
		String[] tokens = userData.split("@");
		Integer clickedID = Integer.parseInt(tokens[1]);
		
		// i brick del tavolo non possono essere eliminati
		if(clickedID == 0) 
			return;
		
		Brick clickedBrick = bricksMap.get(clickedID);
		
		float heightToUpperLevel = constructionState.get(clickedID);
		float insertedHeight = getInsertedHeightFor(heightToUpperLevel-ElementaryBrick.BOX_Y*2);
//		System.out.println("insertedHeight "+insertedHeight);
//		System.out.println("Upper Level Calcolato = "+heightToUpperLevel);
		
		
		boolean canEliminate = canBeEliminatedOrMoved(clickedID, clickedBrick);
		
				 
		if(canEliminate){
			brickBranchGroupMap.get(clickedID).detach();

			LinkedList<XZPair> xzOccupiedList = occupationMap.get(insertedHeight);
//			System.out.println("occupati all'altezza " +insertedHeight+" del mattoncino eliminato "+xzOccupiedList.size());

			XZPair[] xzClickedBrick = clickedBrick.getXZPairList();
			for(int i=0; i<xzClickedBrick.length; i++){
				xzOccupiedList.remove(xzClickedBrick[i]);
			}
			occupationMap.put(insertedHeight, xzOccupiedList);
			
			brickBranchGroupMap.remove(clickedID);
			constructionState.remove(clickedID);
			bricksMap.remove(clickedID);
		}
	}

	private final boolean canBeEliminatedOrMoved(Integer clickedID, Brick clickedBrick) {
		boolean result = true;
		float heightToUpperLevel = constructionState.get(clickedID);
		LinkedList<XZPair> occupiedToUpperLevel = occupationMap.get(heightToUpperLevel);
		
		if(occupiedToUpperLevel == null){
			result = true;
//			System.out.println("lista occupati a livello superiore è NULL");
		}
		else if(occupiedToUpperLevel.isEmpty()){
			result = true;
//			System.out.println("lista occupati a livello superiore è VUOTA");
		}
		else{
			// controllo se le posizioni dei cilindri superiori al blocchetto che voglio eliminare non siano nella mappa
//			System.out.println("lista occupati a livello superiore contiene mattoncini");
			XZPair[] xzClickedBrick = clickedBrick.getXZPairList();
			final float listLength = xzClickedBrick.length; 
			for(int i=0; i<listLength; i++){
				
				if(occupiedToUpperLevel.contains(xzClickedBrick[i])){
//					System.out.println("La lista del livello superiore contiene posizioni occupate in corrispondenza di quelle del " +
//							"mattoncino che si vuole eliminare e cioè "+xzClickedBrick[i]);
					result = false;
					break;
				}
			}
		}
		return result;
	}

	public final float getInsertedHeightFor(float height) {
		final float Y_THRESHOLD = 0.0001f; 
		Set<Float> occupationKeySet = occupationMap.keySet();
		LinkedList<Float> list = new LinkedList<Float>(occupationKeySet);
		
		float result = list.getFirst().floatValue();
		float bestDiff = Math.abs(height - result);
		
		for(Float iter: list){
			float curDiff = Math.abs(iter.floatValue() - height);
			
			if(curDiff < Y_THRESHOLD && curDiff < bestDiff){
				bestDiff = curDiff;
				result = iter;
			}
		}	
		return result;
	}

	public final void insertBrickOn(Primitive clickedCylinder) {
//		System.out.println("Selezionato il cilindro "+clickedCylinder.getUserData()+"\n");
//		System.out.print("----------------------------------------------------------------------\n\n");
				
		// E' necessario creare un nuovo branchGroup (che incapsula il nuovo mattoncino) per far aggiornare la scena
		BranchGroup newBrickBG = new BranchGroup();
		Brick newBrick;
		
		if(selectedForMove==true){
			// si crea il brick con le caratteristiche impostate in tmpBrick
			newBrick = BrickFactory.createBrick(tmpBrick.getType(), tmpBrick.getAppearance());
			newBrick.setColor(tmpBrick.getColor());
		}
		else{
			// si crea il brick con le caratteristiche impostate dall'utente nel controller
			Color3f color;
			Appearance appear;
			
			if(controller.getGameMode()==Controller.PRACTICE){
				appear = AppearanceFactory.createAppearance(AppearanceFactory.RANDOM);
				color = AppearanceFactory.getLastRandomColor();
			}
			else{
				color = controller.getColor();
				appear = AppearanceFactory.createSimpleAppearance(color);
			}
			
			BrickType brickSelected = controller.getBrickType();
			newBrick = BrickFactory.createBrick(brickSelected, appear);
			newBrick.setColor(color);
		}

		TransformGroup newBrickTG = newBrick.getTransformGroup();
		
		Transform3D newBrickTranslation = new Transform3D();
		Transform3D newBrickRotation = new Transform3D();
		
		String userData = (String)clickedCylinder.getUserData();
		final String[] tokens = userData.split("@");
		final String[] coordinates = tokens[0].split(",");
		final float x = Float.parseFloat(coordinates[0]);
		final float z = Float.parseFloat(coordinates[1]);
		final Integer clickedID = Integer.parseInt(tokens[1]);
		
		final float insertHeight = constructionState.get(clickedID);
//		System.out.println("Accedo alla OM: letta l'altezza di inserimento in OM e' "+insertHeight);
		
		newBrickTranslation.setTranslation(new Vector3f(x, insertHeight+ElementaryBrick.BOX_Y, z));

		if(selectedForMove==true){
			newBrickRotation.rotY(tmpBrickRotation);
			newBrick.setRotation(tmpBrickRotation);
		}
		else{
			newBrickRotation.rotY(controller.getRotation());
			newBrick.setRotation(controller.getRotation());
		}
		
		newBrickTranslation.mul(newBrickRotation);
		
		newBrickTG.setTransform(newBrickTranslation);
		
		newBrick.setCylinderData(x+","+z+"@"+brickID);
		
		if(!isPinnable(newBrick, insertHeight)){
			return;
		}
		// segnamo le celle come occupate
		occupy(newBrick, insertHeight);
		final float newInsertHeight = (ElementaryBrick.BOX_Y*2) + insertHeight;
//		System.out.println("Nuovo inserimento in CS ad altezza "+newInsertHeight);
		constructionState.put(brickID, newInsertHeight);
		
		newBrickBG.addChild(newBrickTG);
		newBrickBG.setCapability(BranchGroup.ALLOW_DETACH);
		sceneGraph.addChild(newBrickBG);
		
		
		bricksMap.put(brickID, newBrick);
		brickBranchGroupMap.put(brickID, newBrickBG);
		brickID++;
		
		if(selectedForMove==true){
			tmpBrick = null;
			selectedForMove = false;
			tmpBrickRotation = -1;
		}
	}

	private final void occupy(Brick newBrick, float insertHeight) {
		final XZPair[] xzBrickPair = newBrick.getXZPairList();
		
		LinkedList<XZPair> xzPairList;
		
		if(occupationMap.keySet().contains(insertHeight)){
//			System.out.println("La mappa contiene bricks a "+insertHeight);
			xzPairList = occupationMap.get(insertHeight);
		}
		else{
//			System.out.println("La mappa NON contiene bricks a "+insertHeight+" perciò la instanzio");
			xzPairList = new LinkedList<XZPair>();
		}
		final int listLength = xzBrickPair.length;
		for(int j=0; j<listLength; j++){
			xzPairList.addLast(xzBrickPair[j]);
//			System.out.println(brickID+" Occupato "+xzBrickPair[j].x+","+xzBrickPair[j].z+" ad altezza "+insertHeight);
		}
		
		occupationMap.put(insertHeight, xzPairList);
//		System.out.println(" la lista degli occupati all'altezza "+insertHeight + " contiene "+xzPairList.size()+" mattoncini");
	}

	private final boolean isPinnable(Brick newBrick, float insertHeight) {
		boolean result = true;
		
//		System.out.println("Controllo all'altezza "+insertHeight);
		LinkedList<XZPair> occupiedPairs = occupationMap.get(insertHeight);
		// Se occupiedPairs è null è perché a quell'altezza è tutto libero perciò return true
		
		if(occupiedPairs!=null){
//			System.out.println("A livello "+insertHeight+" sono occupati:");
//			for(XZPair iter: occupiedPairs)
//				System.out.println(iter);
			
			final XZPair[] newBrickPairs = newBrick.getXZPairList();
			final int listLength = newBrickPairs.length;
			for(int i=0; i<listLength; i++){
				if(occupiedPairs.contains(newBrickPairs[i]))
					return false;
			}
		}
		
//		System.out.println("il metodo pinnable() ha ritornato "+result);
		return result;
	}

	private final Primitive findFirstCylinderHit(PickResult[] results) {
		Primitive result = null;
		Primitive currentPrimitive = null;
		
//		System.out.print("----------------------------------------------------------------------\n");
//		System.out.println(" colpiti i seguenti "+results.length+"oggetti:");
		
		final int resultsLength = results.length;
		
	
//		for(int j=0; j<resultsLength; j++){
//			Primitive prim = (Primitive) results[j].getNode(PickResult.PRIMITIVE);
//			if(prim instanceof Cylinder)
//				System.out.println("\t"+j+") Cylinder");
//			else
//				System.out.println("\t"+j+") Box");
//		}
			
		// selezione primo cilindro
		for(int i=0; i<resultsLength; i++){
			currentPrimitive = (Primitive) results[i].getNode(PickResult.PRIMITIVE);
			if(currentPrimitive instanceof Cylinder){
				result = currentPrimitive;
				return result;
			}
		}
		
		return result;
	}

	/**
	 * Da usare per prendere l'insieme dei Brick.
	 * ATTENZIONE: per quanto riguarda salva/carica gli ID possono essere ignorati in quanto vengono riassegnati.
	 * servono solo ad andare nell'altra mappa e prendere l'altezza (coordinata Y) del brick.
	 */
	public final HashMap<Integer, Brick> getBricksMap(){
		return bricksMap;
	}
	
	/**
	 * Consente di accedere alla constructionState, con le altezze post-inserimento dei blocchetti.
	 * Per ottenere l'altezza alla quale il brick è stato inserito, bisogna sottrarre 2*BRICK_SIZE da insertHeight.
	 * @return una HashMap con semantica: brickID, insertHeight+2*BRICK_SIZE
	 */
	public final HashMap<Integer, Float> getConstructionState(){
		return constructionState;
	}
	
	public final void insertBrickAndBranchGroup(int id, Brick brick, BranchGroup brickBG){
		bricksMap.put(id, brick);
		brickBranchGroupMap.put(id, brickBG);
	}
}
