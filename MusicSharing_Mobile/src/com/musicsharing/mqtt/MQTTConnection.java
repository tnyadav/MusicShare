package com.musicsharing.mqtt;

import java.io.File;
import java.io.FileInputStream;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import android.util.Log;

import com.musicsharing.web.WebServiceConstants;

public class MQTTConnection {

	private MqttAsyncClient mqttAsyncClient;
	private MqttConnectOptions connOpt;
	// Mqtt Callback connection
	private IMqttActionListener conListener;
	private boolean isConnected = false;

	private static MQTTConnection mqttConnection;

	public static MQTTConnection getInstance() {
		if (null == mqttConnection) {
			mqttConnection = new MQTTConnection();
		}
		return mqttConnection;
	}

	public boolean isConnected() {
		return isConnected;
	}

	private MQTTConnection() {

		connOpt = new MqttConnectOptions();

		connOpt.setCleanSession(true);
		connOpt.setKeepAliveInterval(300);
		connOpt.setUserName("USERNAME");
		connOpt.setPassword("PASSWORD".toCharArray());

		// Connect to Broker
		try {

			final String brokerURL = WebServiceConstants.MQTT_BROKER_URL;

			mqttAsyncClient = new MqttAsyncClient(brokerURL, System.currentTimeMillis()+"", null);

			conListener = new IMqttActionListener() {

				public void onSuccess(IMqttToken asyncActionToken) {
					// Log.e("MQTT","Connected to " + brokerURL);
					System.out.println("Conected to mqtt");
					isConnected = true;
				}

				public void onFailure(IMqttToken asyncActionToken,
						Throwable exception) {
					Log.e("MQTT",
							"ONFailure Called"
									+ asyncActionToken.getMessageId());
					exception.printStackTrace();
					//reconnectUsingMqtt();
				}
			};
			Log.e("MQTT",
					"before calling connect");
			mqttAsyncClient.connect(connOpt, null, conListener);
			Log.e("MQTT",
					"afetr calling connect");
		} catch (MqttException e) {
			e.printStackTrace();
			Log.e("MQTT",
					"MqttException");
			//System.exit(-1);
		} catch (Exception e) {
			e.printStackTrace();
			//System.exit(-1);
		}
	}

	/**
	 * 
	 * runClient The main functionality of this simple example. Create a MQTT
	 * client, connect to broker, pub/sub, disconnect.
	 * 
	 */
	public void subscribe(String[] topics,
			final MQTTCallbackListener mqttCallbackListener) {

		mqttAsyncClient.setCallback(new MqttCallback() {

			@Override
			public void messageArrived(String topic, MqttMessage message)
					throws Exception {
				try {
					byte[] messageBytes = message.getPayload();
					mqttCallbackListener.processMessage(topic, messageBytes);
				} catch (Exception e) {
					Log.e("MQTT", e.toString());
				}
			}

			@Override
			public void deliveryComplete(IMqttDeliveryToken token) {

			}

			@Override
			public void connectionLost(Throwable cause) {
				reconnectUsingMqtt();

			}
		});

		int[] subQoS = {0,0};
		try {
			// Subscribe for receiving command from client
			mqttAsyncClient.subscribe(topics, subQoS);

		} catch (MqttException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Reconnect using mqtt connection
	 * 
	 * @param conOpt
	 *            Holds the set of options that control how the client connects
	 *            to a server
	 */
	private void reconnectUsingMqtt() {

		try {
			mqttAsyncClient.connect(connOpt, null, conListener);
		} catch (MqttException e) {

			Log.e("MQTT", "connection failed" + e.getMessage());

		}
	}

	/**
	 * To publish command status form device over broker for desktop.
	 * 
	 * @param message
	 */
	public void publishMessage(String message, String topic) {

		int pubQoS = 0;

		// Publish the message
		try {

			MqttMessage commandMessage = new MqttMessage(message.getBytes());
			commandMessage.setQos(pubQoS);
			commandMessage.setRetained(false);
			if (!mqttAsyncClient.isConnected()) {
				reconnectUsingMqtt();
				do {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} while (!mqttAsyncClient.isConnected());
			}
			if (mqttAsyncClient.isConnected()) {
				mqttAsyncClient.publish(topic, commandMessage);
			}
			Log.e("MQTT", "Published Text Message to " + topic);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * To publish command status form device over broker for desktop.
	 * 
	 * @param message
	 */
	public void publishMessage(byte[] message, String topic) {

		int pubQoS = 0;

		// Publish the message
		try {

			MqttMessage commandMessage = new MqttMessage(message);
			commandMessage.setQos(pubQoS);
		    commandMessage.setRetained(false);
			mqttAsyncClient.publish(topic, commandMessage, null, conListener);
			Log.e("MQTT", "published to " + topic + " message length is "
					+ message.length);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
