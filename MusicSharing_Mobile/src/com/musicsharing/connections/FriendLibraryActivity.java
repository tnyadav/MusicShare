package com.musicsharing.connections;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.musicsharing.R;
import com.google.gson.JsonObject;
import com.musicsharing.account.User;
import com.musicsharing.account.UserUtil;
import com.musicsharing.dashboard.StickyListHeadersListViewBaseActivity;
import com.musicsharing.mqtt.MQTTCallbackListener;
import com.musicsharing.mqtt.MQTTConnection;
import com.musicsharing.mqtt.MQTTConnectionServiceImpl;
import com.musicsharing.mqtt.MusicCallbackListener;
import com.musicsharing.web.TAListener;
import com.musicsharing.web.TAWebServiceAsyncTask;
import com.musicsharing.web.WebServiceConstants;

public class FriendLibraryActivity extends StickyListHeadersListViewBaseActivity {
	Cursor musiccursor;
	List<FriendLibrarySong> musicList;
	public static String friendId;
	public static String friendName;

	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getActionBar().setTitle(friendName+"'s Music library");
		setContentView(R.layout.activity_friendlibrary);
		setupUiComponent();
	
	}



	public void performSort() {

		Collections.sort(musicList, new Comparator<FriendLibrarySong>() {
			@Override
			public int compare(FriendLibrarySong lhs, FriendLibrarySong rhs) {
				return lhs.getName().compareToIgnoreCase(
						rhs.getName());

			}
		});

	}

	@Override
	protected void setupUiComponent() {
		super.setupUiComponent();
         new TAWebServiceAsyncTask(activity, new TAListener() {
			
			@Override
			public void onTaskFailed(String errorMessage) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onTaskCompleted(String result) {
				String s=result;
				Log.e("lib", s);
                FriendLibrary friendLibrary=gson.fromJson(result, FriendLibrary.class);
                musicList=friendLibrary.getFriendLibrary();
				//performSort();
				listView.setAdapter(new FriendLibraryActivityAdapter(activity, musicList));
				listView.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						
						FriendLibrarySong friendLibrarySong = musicList.get(position);
						User user= UserUtil.getUser(activity);
						JsonObject jsonObject = new JsonObject();
						jsonObject.addProperty("senderId", user.getUserId());
						jsonObject.addProperty("name", user.getName());
						jsonObject.addProperty("filePath",friendLibrarySong.getFileName());
						MQTTConnectionServiceImpl.sendTextMessage(MQTTConnectionServiceImpl.TOPIC_SONG_REQUEST+friendId, jsonObject.toString());	
					}

				});
				
			}
		}, WebServiceConstants.GET_USER_LIBRARY+friendId, true, "Getting song list").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
					(Void[]) null);
		
		
	}

}
