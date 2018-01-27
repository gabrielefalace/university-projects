package common;

import java.io.Serializable;

public class Message implements Serializable {
	
	private String messageType;
	
	public Message(String type){
		setMessageType(type);
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getMessageType() {
		return messageType;
	}

	
	
}
