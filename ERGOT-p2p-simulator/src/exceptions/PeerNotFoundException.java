package exceptions;

/**
 * Eccezione da lanciare qualora il tentativo di individuare un peer fallisca.
 * 
 * @author Gabriele
 *
 */
public class PeerNotFoundException extends Exception {

	public PeerNotFoundException(){
		super();
	}
	
	public PeerNotFoundException(String msg){
		super(msg);
	}
}
