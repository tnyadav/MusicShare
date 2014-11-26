package com.musicsharing.connections;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.musicsharing.account.User;
import com.musicsharing.account.UserUtil;
import com.musicsharing.dashboard.UpdateUserStatus;
import com.musicsharing.utils.JSONParser;
import com.musicsharing.utils.Utils;
import com.musicsharing.web.WebServiceConstants;

public class UpdateMyStatus extends Service {

	private Looper mServiceLooper;
	private ServiceHandler mServiceHandler;
	private final String DEBUG_TAG = "UpdateLocation::Service";

	// Handler that receives messages from the thread
	private final class ServiceHandler extends Handler {
		public ServiceHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {

			mServiceHandler.post(new Update());
			stopSelf(msg.arg1);

		}
	}

	@Override
	public void onCreate() {
		// Start up the thread running the service. Note that we create a
		// separate thread because the service normally runs in the process's
		// main thread, which we don't want to block. We also make it
		// background priority so CPU-intensive work will not disrupt our UI.
		HandlerThread thread = new HandlerThread("ServiceStartArguments",
				android.os.Process.THREAD_PRIORITY_BACKGROUND);
		thread.start();
		//Log.e(DEBUG_TAG, ">>>onCreate()");
		// Get the HandlerThread's Looper and use it for our Handler
		mServiceLooper = thread.getLooper();
		mServiceHandler = new ServiceHandler(mServiceLooper);

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		// For each start request, send a message to start a job and deliver the
		// start ID so we know which request we're stopping when we finish the
		// job
		Message msg = mServiceHandler.obtainMessage();
		msg.arg1 = startId;
		mServiceHandler.sendMessage(msg);
		// If we get killed, after returning from here, restart
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// We don't provide binding, so return null
		return null;
	}

	@Override
	public void onDestroy() {

		//Log.e(DEBUG_TAG, ">>>onDestroy()");
	}

	private class Update implements Runnable {

		public void run() {

			User user = UserUtil.getUser(UpdateMyStatus.this);
			if (user != null
					&& Utils.isNetworkAvailable(UpdateMyStatus.this)) {

				try {
					String responseJson = new JSONParser()
							.getJSONFromUrl(WebServiceConstants.UPDATE_MY_STATUS
									+ user.getUserId());
					ConnectionList connectionList = new Gson().fromJson(
							responseJson, ConnectionList.class);
					UpdateUserStatus.friendList = connectionList
							.getUserDTO();
					UpdateUserStatus.performSort();
					Intent sendIntent = new Intent();
					sendIntent.setAction("update");
					sendBroadcast(sendIntent);
					Log.e("dashboard", "status updated");
				} catch (Exception e) {
					Log.e("dashboard", "status not updated");
					e.printStackTrace();
				}
			}
		}
	}
}