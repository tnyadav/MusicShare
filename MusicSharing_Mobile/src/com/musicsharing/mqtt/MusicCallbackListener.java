package com.musicsharing.mqtt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.musicsharing.connections.RequestedSong;

public class MusicCallbackListener implements MQTTCallbackListener {

	private String userId;
	private Activity activity;

	public MusicCallbackListener(String userId, Activity activity) {
		this.userId = userId;
		this.activity = activity;
	}

	@Override
	public void processMessage(String topic, byte[] messageBytes) {
		if (topic.equals(MQTTConnectionServiceImpl.TOPIC_SONG_REQUEST + userId)) {
			String message = new String(messageBytes);
			Log.e("MusicCallbackListener", topic);
			Log.e("MusicCallbackListener : message: ", message);

			try {
				RequestedSong requestedSong = new Gson().fromJson(message,
						RequestedSong.class);
				Log.e("MusicCallbackListener : requestedSong : ",
						requestedSong.toString());
				String requestedFriendId = requestedSong.getSenderId();
				String requestedSongPath = requestedSong.getFilePath();
				Log.e("MusicCallbackListener : requestedFriendId : ",
						requestedFriendId);
				// Log.e("MusicCallbackListener : requestedSongPath : ",
				// requestedSongPath);
				respondToSongRequest(requestedFriendId, requestedSongPath);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (topic.equals(MQTTConnectionServiceImpl.TOPIC_SONG_RESPONSE
				+ userId)) {
			Log.e("MusicCallbackListener TOPIC_SONG_RESPONSE", topic);
			File sdCard = Environment.getExternalStorageDirectory();
			File dir = new File(sdCard.getAbsolutePath() + "/MusicShare");
			dir.mkdirs();
			File music = new File(dir, System.currentTimeMillis()+".mp3");

			try {
				FileOutputStream fileOutput = new FileOutputStream(music);
				fileOutput.write(messageBytes);
				fileOutput.flush();
				fileOutput.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private void respondToSongRequest(String requestedFriendId,
			String requestedSongPath) {
		MQTTConnectionServiceImpl.sendAudioFile(
				MQTTConnectionServiceImpl.TOPIC_SONG_RESPONSE
						+ requestedFriendId, requestedSongPath);
		Log.e("MusicCallbackListener", "Audio File sent on topic"
				+ MQTTConnectionServiceImpl.TOPIC_SONG_RESPONSE
				+ requestedFriendId);
	}

}
