package com.musicsharing.dashboard;

import com.musicsharing.utils.ErrorReporter;

import android.app.Application;

public final class App extends Application {

	@Override
	public void onCreate() {
		
		
		super.onCreate();
		ErrorReporter errReporter = new ErrorReporter();
		errReporter.Init(this);
		errReporter.SaveLog(this,"MusicShare");
		
	}

	
}
