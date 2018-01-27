package orologio;

public final class Timer implements Runnable{
	
	private long timeout;
	private transient boolean attivo=false;
	private transient boolean periodico=false;
	private transient boolean toStop=false;
	private Sveglia s=null;
	Thread t=null;
	
	
	public Timer( Sveglia s ){
		this.s=s;
		t=new Thread(this); t.start();
	}
	
	public void set( long timeout ){
		if( attivo ){
			this.timeout=timeout; periodico=false;
			t.interrupt();
		}
		else{
			attivo=true;
			this.timeout=timeout; periodico=false;
			synchronized( this ){
    	       notify();
			}
		}
	}//set
	
	public void set( long timeout, boolean periodico ){
		if( attivo ){
			this.timeout=timeout;
			this.periodico=periodico;
			t.interrupt();
		}
		else{
			attivo=true;
			this.timeout=timeout;
			this.periodico=periodico;
			synchronized( this ){
    	       notify();
			}
		}
	}//set
	
	public void reset(){
		if( attivo ){
			attivo=false; t.interrupt();
		}
	}//reset
	
	public void cancel(){
		if( attivo ){
			toStop=true;
			t.interrupt();
		}
		else{
			toStop=true;
			synchronized(this){
			   notify();
			}
		}
	}//cancel
	
	public void run(){
		while( true ){
			synchronized(this){
			   if( !attivo )
			      try{
					wait();
				  }catch(InterruptedException e){}
			}
			if( toStop ) break;
			try{
				Thread.currentThread().sleep( timeout );
			}catch( InterruptedException e ){ continue; }
			if( toStop ) break;
			if( attivo ) s.suona();
			if( !periodico ) attivo=false;
		}
		//System.out.println("Il timer ha terminato...");
	}//run
	
}//Timer