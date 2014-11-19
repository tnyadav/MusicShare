package com.musicsharing.connections;

import java.util.List;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;

import com.musicsharing.dashboard.BaseActivity;

public class ListenMusicActivity extends BaseActivity {
	Cursor musiccursor;
	List<FriendLibrarySong> musicList;
	public static String friendId;
	public static String friendName;

	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setupUiComponent();
	
	

	}

	@Override
	protected void setupUiComponent() {
		
		
	}

}
