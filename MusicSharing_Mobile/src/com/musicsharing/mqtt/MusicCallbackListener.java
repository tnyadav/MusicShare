package com.musicsharing.mqtt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.musicsharing.account.UserUtil;
import com.musicsharing.connections.RequestedSong;
import com.musicsharing.dashboard.UpdateUserStatus;
import com.musicsharing.utils.NotificationUtils;

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
				
				
				respondToSongRequest(requestedFriendId, requestedSongPath,
						requestedFriendId);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("MusicCallbackListener : Exception ", e+"");
			}

		} else if (topic.contains(MQTTConnectionServiceImpl.TOPIC_SONG_RESPONSE
				+ userId)) {
			Log.e("MusicCallbackListener TOPIC_SONG_RESPONSE", topic);
			
			String[] strings = topic.split("/");
			String name = strings[3];
			String fileName = strings[strings.length - 1];
			//String friendName = UpdateUserStatus.getFriendName(fileName);
			//NotificationUtils.showNotificationToast(activity, "Downloading song from "+name+"'s library");
			File sdCard = Environment.getExternalStorageDirectory();
			File dir = new File(sdCard.getAbsolutePath() + "/MusicShare/"
					+ name);
			dir.mkdirs();
			File music = new File(dir, fileName);

			try {
				FileOutputStream fileOutput = new FileOutputStream(music);
				fileOutput.write(messageBytes);
				fileOutput.flush();
				fileOutput.close();
				Log.e("MusicCallbackListener TOPIC_SONG_RESPONSE",
						"file saved in library");
			} catch (Exception e) {
				Log.e("MusicCallbackListener TOPIC_SONG_RESPONSE", "" + e);
				e.printStackTrace();
			}

		}
	}

	private void respondToSongRequest(String requestedFriendId,
			String requestedSongPath, String friendName) {
		Log.e("MusicCallbackListener", "before Audio File sent on topic"
				+ MQTTConnectionServiceImpl.TOPIC_SONG_RESPONSE
				+ requestedFriendId + requestedSongPath);
		MQTTConnectionServiceImpl.sendAudioFile(
				MQTTConnectionServiceImpl.TOPIC_SONG_RESPONSE
						+ requestedFriendId + "/" +friendName+ requestedSongPath,
				requestedSongPath);
		Log.e("MusicCallbackListener", "Audio File sent on topic"
				+ MQTTConnectionServiceImpl.TOPIC_SONG_RESPONSE
				+ requestedFriendId + requestedSongPath);
	}

}
