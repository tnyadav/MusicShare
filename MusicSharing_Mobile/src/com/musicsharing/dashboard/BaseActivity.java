package com.musicsharing.dashboard;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.w3axis.sharehub.R;
import com.google.gson.Gson;
import com.musicsharing.account.UserUtil;
import com.musicsharing.connections.ConnectionList;
import com.musicsharing.utils.JSONParser;
import com.musicsharing.utils.NotificationUtils;
import com.musicsharing.utils.Utils;
import com.musicsharing.web.TAListener;
import com.musicsharing.web.TAWebServiceAsyncTask;
import com.musicsharing.web.WebServiceConstants;

public abstract class BaseActivity extends Activity{

	protected Activity activity;
	protected Gson gson ;
	//private Timer timer;
	
	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		
		getActionBar().setIcon(R.drawable.logo);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.color.white));
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		activity = this;
		gson =new Gson();
	}
	@Override
	public void onBackPressed() {
		
		super.onBackPressed();
		overridePendingTransition(R.animator.slide_out_right,
				R.animator.slide_in_left);
	}
	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		overridePendingTransition(R.animator.slide_in_right,
				R.animator.slide_out_left);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
		case android.R.id.home:
			this.onBackPressed();
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	protected void showLog(String msg) {
		Log.e("mywill", msg);
	}
	protected abstract void setupUiComponent();
	   @Override
	   public void onResume() {
	   	/*timer=new Timer();
	   	timer.scheduleAtFixedRate(new TimerTask() {
	   		
	   		@Override
	   		public void run() {
	   			
	   			UpdateUserStatus.update(activity);
	   			
	   		}
	   	},5000, 10000);*/
	   	super.onResume();
	   }
	      @Override
	   public void onPause() {
	/*   if (timer!=null) {
	   	timer.cancel();
	   }*/
	   	super.onPause();
	   }


	
	
}
