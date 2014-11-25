package com.musicsharing.FriemdSavedMediaLibrary;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.musicsharing.R;
import com.google.gson.JsonObject;
import com.musicsharing.account.UserUtil;
import com.musicsharing.connections.FriendLibrary;
import com.musicsharing.connections.FriendLibrarySong;
import com.musicsharing.dashboard.StickyListHeadersListViewBaseActivity;
import com.musicsharing.mqtt.MQTTCallbackListener;
import com.musicsharing.mqtt.MQTTConnection;
import com.musicsharing.mqtt.MQTTConnectionServiceImpl;
import com.musicsharing.mqtt.MusicCallbackListener;
import com.musicsharing.web.TAListener;
import com.musicsharing.web.TAWebServiceAsyncTask;
import com.musicsharing.web.WebServiceConstants;

public class FriendSavedMediaListActivity extends
		StickyListHeadersListViewBaseActivity {
	Cursor musiccursor;
	List<String> musicList;
	public static String friendId;
	public static String friendName;

	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getActionBar().setTitle(friendName);
		setContentView(R.layout.activity_friendlibrary);
		setupUiComponent();

	}

	public void performSort() {

		IgnoreCaseComparator icc = new IgnoreCaseComparator();
		java.util.Collections.sort(musicList, icc);

	}

	@Override
	protected void setupUiComponent() {
		super.setupUiComponent();
		File sdCard = Environment.getExternalStorageDirectory();
		final File dir = new File(sdCard.getAbsolutePath() + "/MusicShare/"
				+ friendName);
		String[] files = dir.list();
		musicList = new ArrayList<String>(Arrays.asList(files));
		performSort();
		listView.setAdapter(new FriendSavedMediaListActivityAdapter(activity,
				musicList));
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				/*Intent intent = new Intent(activity, AudioPlayer.class);
				intent.putExtra(AudioPlayer.AUDIO_FILE_NAME,
						dir.getAbsolutePath() + "/" + musicList.get(position));
				startActivity(intent);*/
				Intent intent = new Intent(Intent.ACTION_VIEW);  
				//intent.setAction(android.content.Intent.ACTION_VIEW);  
				File file = new File(dir.getAbsolutePath() + "/" + musicList.get(position));  
				intent.setDataAndType(Uri.fromFile(file), "audio/*");  
				startActivity(intent);
			}

		});

	}

	class IgnoreCaseComparator implements Comparator<String> {
		public int compare(String strA, String strB) {
			return strA.compareToIgnoreCase(strB);
		}

	}
}
