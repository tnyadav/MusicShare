package com.musicsharing.web;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.musicsharing.utils.JSONParser;
import com.musicsharing.utils.NotificationUtils;
import com.musicsharing.utils.Utils;

/**
 * Custom AsyncTask responsible for encryption, API request/response handling.
 * 
 * @author Optimus
 * 
 */
public class TAWebServiceAsyncTask extends AsyncTask<Void, Void, String> {

	private TAListener mListener;
	private Activity mActivity;
	private String mRequestURL;
	private String mLoadingMessage;
	private boolean mShowDialog;

	public TAWebServiceAsyncTask(Activity argActivity,
			TAListener argListener, String requestURL, boolean showDialog,String loadingMessage) {
		mActivity = argActivity;
		mListener = argListener;
		mRequestURL = requestURL;
		mShowDialog = showDialog;
		mLoadingMessage=loadingMessage;
		
		
		
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		// Check Internet access
		if (!Utils.isNetworkAvailable(mActivity)) {
			NotificationUtils.showNotificationToast(mActivity,
					"No Network Connection");
			cancel(true);
			return;
		}

		if (mShowDialog) {
			// Show loading progress dialog in the beginning of AsyncTask.
			NotificationUtils.showProgressDialog(mActivity,null, mLoadingMessage);
		}
	}

	@Override
	protected String doInBackground(Void... arg0) {
		String responseJson = null;

		try {
			
			responseJson = new JSONParser().getJSONFromUrl(mRequestURL);
			//Log.e("connection list",responseJson);
		} catch (NullPointerException ex) {
			ex.printStackTrace();
		}
		return responseJson;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);

			// Dismiss all progress dialog on task complete
			if(mShowDialog)
			NotificationUtils.dismissProgressDialog();

			if (result != null && !TextUtils.isEmpty(result)&&Utils.isJSONValid(result)) {
				mListener.onTaskCompleted(result);
			} else {
				mListener.onTaskFailed("Server not responds");
			}

		
	
		}
	//}
}
