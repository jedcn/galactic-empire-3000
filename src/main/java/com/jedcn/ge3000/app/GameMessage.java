package com.jedcn.ge3000.app;

public class GameMessage {

	private String type;

	private long timestamp;

	private String data;

	public GameMessage() {
		this.timestamp = System.currentTimeMillis();
	}

	public GameMessage(String type, String data) {
		this();
		this.type = type;
		this.data = data;
	}

	// Getters and setters
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
