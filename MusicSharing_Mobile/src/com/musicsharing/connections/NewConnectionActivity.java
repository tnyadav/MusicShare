package com.musicsharing.connections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.musicsharing.R;
import com.google.gson.Gson;
import com.musicsharing.account.UserUtil;
import com.musicsharing.dashboard.BaseActivity;
import com.musicsharing.dashboard.BaseFragment;
import com.musicsharing.utils.NotificationUtils;
import com.musicsharing.utils.SharedPreferencesUtil;
import com.musicsharing.web.TAListener;
import com.musicsharing.web.TAPOSTWebServiceAsyncTask;
import com.musicsharing.web.TAWebServiceAsyncTask;
import com.musicsharing.web.WebServiceConstants;

public final class NewConnectionActivity extends BaseActivity {
    private static final String CONNECTION_LIST = "connection";
    private ListView listConnection;
    private SearchView searchView;
    private NewConnectionActivitytAdapter newConnectionActivityAdapter;
    List<Connections> friendList;
    List<Connections> PendingFriendList;

@Override
@SuppressLint("NewApi")
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.fragment_newconnections);
	setupUiComponent();
}


    @Override
    protected void setupUiComponent() {
    	searchView=(SearchView)findViewById(R.id.searchView);
    	listConnection=(ListView)findViewById(R.id.listConnection);
    	listConnection.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				
			}
		});
 
    	
    	getConnection(false, "Getting connections");
    }
    
    private void getConnection(boolean showProgress,String loadingMessage) {
    	
    	String userId=UserUtil.getUserId(activity);
    	if (userId==null) {
			return;
		}
		new TAWebServiceAsyncTask(activity, new TAListener() {
			
			@Override
			public void onTaskFailed(String errorMessage) {
				
				NotificationUtils.showNotificationToast(activity, errorMessage);		
				}
			
			@Override
			public void onTaskCompleted(String result) {
				
				ConnectionList connectionList=new Gson().fromJson(result, ConnectionList.class);
				//filterFriends(connectionList.getUserDTO());
				NewConnectionActivitytAdapter.connectionList=connectionList.getUserDTO();
				NewConnectionActivitytAdapter newConnectionFragmentAdapter=new NewConnectionActivitytAdapter(activity);
		    	listConnection.setAdapter(newConnectionFragmentAdapter);
		    	newConnectionFragmentAdapter.notifyDataSetChanged();
			}
		}, WebServiceConstants.GET_ALL_USERS+userId, showProgress, loadingMessage).execute();
	}
    
   private void  filterFriends(List<Connections> connectionList) {
	   friendList=new ArrayList<Connections>();
	   PendingFriendList=new ArrayList<Connections>();
	   Iterator<Connections> iterator = connectionList.iterator();
	   while (iterator.hasNext()) {
		Connections connections = iterator.next();
		if (connections.getConnectionStatus().equalsIgnoreCase("accepted")) {
			friendList.add(connections);
		}else {
			PendingFriendList.add(connections);
		}
		
	}
	   
	
}
}
