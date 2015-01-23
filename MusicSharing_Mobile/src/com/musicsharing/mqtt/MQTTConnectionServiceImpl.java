package com.musicsharing.mqtt;

import java.io.File;
import java.io.FileInputStream;

import android.app.Activity;
import android.util.Log;

import com.musicsharing.account.User;
import com.musicsharing.utils.NotificationUtils;

public class MQTTConnectionServiceImpl {
	public static final String TOPIC_SONG_REQUEST = "musicshare/song_request/";
	public static final String TOPIC_SONG_RESPONSE = "musicshare/song_response/";

	public static boolean connectAndSubscribeMQTT(User user, Activity activity) {

		MQTTConnection mqttConnection = MQTTConnection.getInstance();

		int waitCount = 0;

		do {
			Log.e("mqtt", "Connecting "+waitCount);;
			if (waitCount > 50) {
				break;
			}
			waitCount++;
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} while (!mqttConnection.isConnected());
		MusicCallbackListener musicCallbackListener = new MusicCallbackListener(
				user.getUserId(), activity);
		Log.e("MQTT", "Befoe calling subscribe");
		String[] topics = new String[2];
		topics[0] = TOPIC_SONG_REQUEST + user.getUserId();
		topics[1] = TOPIC_SONG_RESPONSE + user.getUserId() + "/#";
		mqttConnection.subscribe(topics, musicCallbackListener);
		return true;
	}

	public static void sendTextMessage(String topic, String message) {
		MQTTConnection.getInstance().publishMessage(message, topic);
	}

	public static void sendAudioFile(final Activity activity,String topic, final String filePath,final String friendName) {

		FileInputStream fileInputStream = null;
		Log.e("MusicCallbackListener", "filePath is "+filePath);
		if (null == filePath) {
			return;
		}
		File file = new File(filePath);

		byte[] bFile = new byte[(int) file.length()];

		try {
			// convert file into array of bytes
			fileInputStream = new FileInputStream(file);
			fileInputStream.read(bFile);
			fileInputStream.close();
			MQTTConnection.getInstance().publishMessage(bFile, topic);
			activity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					NotificationUtils.showNotificationToast(activity, "Song sent to "+friendName+" ("+filePath+")");
					
				}
			});
		} catch (Exception e) {
			NotificationUtils.showNotificationToast(activity, "Unabe to  sent Song to "+friendName);
			e.printStackTrace();
		}
	}

}
