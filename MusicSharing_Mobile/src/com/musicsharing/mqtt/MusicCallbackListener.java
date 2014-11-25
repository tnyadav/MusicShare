package com.musicsharing.mqtt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.musicsharing.account.User;
import com.musicsharing.account.UserUtil;
import com.musicsharing.connections.RequestedSong;
import com.musicsharing.dashboard.UpdateUserStatus;
import com.musicsharing.utils.NotificationUtils;

public class MusicCallbackListener implements MQTTCallbackListener {

	private String userId;
	private Activity activity;
	public static boolean isDownloading = false;

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
				final String requestedFriendName = requestedSong.getName();
				String requestedSongPath = requestedSong.getFilePath();
				Log.e("MusicCallbackListener : requestedFriendId : ",
						requestedFriendId);
				// Log.e("MusicCallbackListener : requestedSongPath : ",
				// requestedSongPath);
				activity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						NotificationUtils.showNotificationToast(activity,
								"Song request from " + requestedFriendName);

					}
				});
                User user=UserUtil.getUser(activity);
				respondToSongRequest(activity,requestedFriendId,requestedFriendName, requestedSongPath, user.getName());

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.e("MusicCallbackListener : Exception ", e + "");
			}

		} else if (topic.contains(MQTTConnectionServiceImpl.TOPIC_SONG_RESPONSE
				+ userId)) {
				
			String[] strings = topic.split("/");
			final String name = strings[3];
			String fileName = strings[strings.length - 1];
			activity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					NotificationUtils.showNotificationToast(activity,
							"Song Coming from " + name);

				}
			});
			File sdCard = Environment.getExternalStorageDirectory();
			File dir = new File(sdCard.getAbsolutePath() + "/MusicShare/"
					+ name);
			dir.mkdirs();
			final File music = new File(dir, fileName);

			try {
				FileOutputStream fileOutput = new FileOutputStream(music);
				fileOutput.write(messageBytes);
				fileOutput.flush();
				fileOutput.close();
				activity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						NotificationUtils.showNotificationToast(activity,
								"Song Coming from " + name +"Received and saved in "+music.getAbsolutePath());

					}
				});
			} catch (Exception e) {
				Log.e("MusicCallbackListener TOPIC_SONG_RESPONSE", "" + e);
				e.printStackTrace();
			}

		}
	}

	private void respondToSongRequest(Activity activity,String requestedFriendId,String requestedFriendName,
			String requestedSongPath, String myName) {
		
		MQTTConnectionServiceImpl.sendAudioFile(activity,
				MQTTConnectionServiceImpl.TOPIC_SONG_RESPONSE
						+ requestedFriendId + "/" + myName
						+ requestedSongPath, requestedSongPath,requestedFriendName);
	
	}

}
