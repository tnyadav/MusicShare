package com.musicsharing.mqtt;

public interface MQTTCallbackListener {

	public void processMessage(String topic, byte[] messageBytes);
	
}
