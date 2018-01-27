package util;

import java.util.LinkedList;

import node.Node;
import exceptions.NodeAlreadyPresentException;
import exceptions.PeerNotFoundException;
import exceptions.TooSmallIdentifierCircleException;
import network.Network;

public class Prova {

	public static void main(String[] args){
		IdentifierCircle ids;
		try {
			ids = IdentifierCircle.getInstance(30);
			Network network = new Network(ids);
			
			for(int i=1; i<100000; i++){
				Node n = new Node(i, ids);
				System.out.println("[iterazione "+i+"] Creato il nodo "+n.getId());
				network.join(n);
				System.out.println("[iterazione "+i+"] aggiunto il nodo "+n.getId());
			}
			
			System.out.println(100000+" joins completed!");
			
			Node[] nodes = network.getNodeList();
			
			for(Node currentNode: nodes){
				System.out.println(currentNode);
			}
			
			
		} 
		catch (TooSmallIdentifierCircleException e) {
			e.printStackTrace();
		} 
		catch (NodeAlreadyPresentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (PeerNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}
