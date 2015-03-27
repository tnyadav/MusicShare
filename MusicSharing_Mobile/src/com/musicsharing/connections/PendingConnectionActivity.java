package com.musicsharing.connections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import com.w3axis.sharehub.R;
import com.google.gson.Gson;
import com.musicsharing.account.UserUtil;
import com.musicsharing.dashboard.BaseActivity;
import com.musicsharing.web.TAListener;
import com.musicsharing.web.TAWebServiceAsyncTask;
import com.musicsharing.web.WebServiceConstants;

public class PendingConnectionActivity extends BaseActivity{
	ListView listPendingConnection;
	public  List<Connections> pendingConnection;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pendingconnections);
		getActionBar().setTitle("Friend Requests");
		
		setupUiComponent();
	}

	@Override
	protected void setupUiComponent() {
		listPendingConnection=(ListView)findViewById(R.id.listPendingConnection);
		String userId = UserUtil.getUserId(activity);
		if (userId == null) {
			return;
		}
		new TAWebServiceAsyncTask(activity, new TAListener() {

			@Override
			public void onTaskFailed(String errorMessage) {

			}

			@Override
			public void onTaskCompleted(String result) {

				ConnectionList connectionList = new Gson().fromJson(result,
						ConnectionList.class);
				filterFriends(connectionList.getUserDTO());
				listPendingConnection.setAdapter(new PendingConnectionActivityAdapter(activity,pendingConnection));
				
			

			}
		}, WebServiceConstants.GET_USER_CONNECTIONS + userId, true, "Getting pending request")
				.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
						(Void[]) null);
	
	}
	   private void  filterFriends(List<Connections> connectionList) {
		  
		   pendingConnection=new ArrayList<Connections>();
		   Iterator<Connections> iterator = connectionList.iterator();
		   while (iterator.hasNext()) {
			Connections connections = iterator.next();
			if (connections.getConnectionStatus().equalsIgnoreCase("accepted")) {
				
			}else {
				pendingConnection.add(connections);
			}
			
		}
		   
		
	}
}
