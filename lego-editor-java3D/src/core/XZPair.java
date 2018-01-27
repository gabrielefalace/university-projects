package core;

import java.io.Serializable;

@SuppressWarnings("serial")
public final class XZPair implements Serializable{

	public float x;
	public float z;
	
	public XZPair(){
		x = 0.0f;
		z = 0.0f;
	}
	
	public XZPair(float x, float z){
		this.x = x;
		this.z = z;
	}
	
	public final boolean equals(Object o){
		XZPair other = (XZPair)o;
		
		float deltaX = Math.abs(other.x - this.x);
		float deltaZ = Math.abs(other.z - this.z);
		
		if(deltaX <= 0.0001f && deltaZ <= 0.01f)
			return true;
		else
			return false;
	}	
}
