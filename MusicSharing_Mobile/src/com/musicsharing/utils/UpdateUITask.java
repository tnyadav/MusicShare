package com.musicsharing.utils;

import android.app.ProgressDialog;
import android.os.AsyncTask;

public abstract class UpdateUITask extends AsyncTask<Void, Void, Void>{
	private ProgressDialog progressDialog;

	@Override
	protected Void doInBackground(Void... arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

}
