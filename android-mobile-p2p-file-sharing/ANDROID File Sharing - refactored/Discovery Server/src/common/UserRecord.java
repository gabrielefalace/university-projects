package common;

import java.io.Serializable;

public class UserRecord implements Serializable {

	private String ip;
	
	private int port;
	
	private long lastTimestamp;
	
	private String logicalName;
	
	public UserRecord(){}
	
	public UserRecord(String ip, int port, String logicalName){
		this.ip = ip;
		this.port = port;
		this.logicalName = logicalName;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getIp() {
		return ip;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getPort() {
		return port;
	}

	public void setLastTimestamp(long lastTimestamp) {
		this.lastTimestamp = lastTimestamp;
	}

	public long getLastTimestamp() {
		return lastTimestamp;
	}

	public void setLogicalName(String logicalName) {
		this.logicalName = logicalName;
	}

	public String getLogicalName() {
		return logicalName;
	}
	
}
