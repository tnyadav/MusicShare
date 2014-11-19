package com.musicsharing.dashboard;

import com.google.gson.Gson;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.SearchView;

public abstract class BaseFragment extends Fragment{
	
	protected DashboardActivity dashboardActivity;
	protected View view;
	protected SearchView searchView;
	protected Gson gson=new Gson();


	@Override
	public void onAttach(Activity activity) {
		
		super.onAttach(activity);
		dashboardActivity=(DashboardActivity) activity;
		
		
	}

	protected abstract void setupUiComponent(); 

}
