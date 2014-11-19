package com.musicsharing.web;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonParseException;
import com.musicsharing.utils.NotificationUtils;
import com.musicsharing.utils.Utils;

public class TAPOSTWebServiceAsyncTask extends AsyncTask<Void, Void, String> {

	private InputStream inputStream;
	// private static BasicHttpContext httpContext = new BasicHttpContext();
	private TAListener mListener;
	private Activity mActivity;
	private String mJSONString;
	private String mRequestURL;
	private boolean mShowDialog;
	private String mLoadingMessage;
	public TAPOSTWebServiceAsyncTask(Activity argActivity, boolean showDialog,String loadingMessage,
			TAListener argListener, String argRequestURL, String argJSONString) {
		mActivity = argActivity;
		mListener = argListener;
		mShowDialog = showDialog;
		mLoadingMessage=loadingMessage;
		mRequestURL = argRequestURL;
		mJSONString = argJSONString;

		
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		// Check Internet access
		if (!Utils.isNetworkAvailable(mActivity)) {

			cancel(true);
			return;
		}

		if (mShowDialog) {
			// Show loading progress dialog in the beginning of AsyncTask.
			NotificationUtils.showProgressDialog(mActivity,null, mLoadingMessage);
		}
	}

	@Override
	protected String doInBackground(Void... params) {

		String mResponseJson = null;

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(mRequestURL);

		try {

			
			StringEntity se = new StringEntity(mJSONString);

			// httppost.setEntity(new UrlEncodedFormEntity(mPOSTParams));
			httppost.setEntity(se);

			// 7. Set some headers to inform server about the type of the
			// content
			httppost.setHeader("Accept", "application/json");
			httppost.setHeader("Content-type", "application/json");

			HttpResponse response = httpclient.execute(httppost);
			HttpEntity httpEntity = response.getEntity();
			mResponseJson = EntityUtils.toString(httpEntity);
			//Log.e("API response", mResponseJson);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mResponseJson;

	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);

			NotificationUtils.dismissProgressDialog();

			if (result != null && !TextUtils.isEmpty(result)&& Utils.isJSONValid(result)) {
				mListener.onTaskCompleted(result);
			} else {
				mListener.onTaskFailed("Server not responds");
			}

		
	}

}