package com.musicsharing.newconnection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

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
import com.musicsharing.connections.ConnectionList;
import com.musicsharing.connections.Connections;
import com.musicsharing.dashboard.BaseFragment;
import com.musicsharing.utils.NotificationUtils;
import com.musicsharing.utils.SharedPreferencesUtil;
import com.musicsharing.web.TAListener;
import com.musicsharing.web.TAPOSTWebServiceAsyncTask;
import com.musicsharing.web.TAWebServiceAsyncTask;
import com.musicsharing.web.WebServiceConstants;

public final class NewConnectionFragment extends BaseFragment {
    private static final String CONNECTION_LIST = "connection";
    private ListView listConnection;
    private SearchView searchView;
    private Button pending;
    private NewConnectionFragmentAdapter newConnectionFragmentAdapter;
    List<Connections> friendList;
    List<Connections> PendingFriendList;

    public static NewConnectionFragment newInstance(String content) {
        NewConnectionFragment fragment = new NewConnectionFragment();

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       
          view=inflater.inflate(R.layout.fragment_newconnections, null);
        return view;
    }

  
    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        setupUiComponent();
    	super.onActivityCreated(savedInstanceState);
    }
    @Override
    protected void setupUiComponent() {
    	searchView=(SearchView)view.findViewById(R.id.searchView);
    	listConnection=(ListView)view.findViewById(R.id.listConnection);
    	listConnection.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				
			}
		});
 
    	
    	getConnection(false, "Getting connections");
    }
    
    private void getConnection(boolean showProgress,String loadingMessage) {
    	
    	String userId=UserUtil.getUserId(dashboardActivity);
    	if (userId==null) {
			return;
		}
		new TAWebServiceAsyncTask(dashboardActivity, new TAListener() {
			
			@Override
			public void onTaskFailed(String errorMessage) {
				
				NotificationUtils.showNotificationToast(dashboardActivity, errorMessage);		
				}
			
			@Override
			public void onTaskCompleted(String result) {
				
				ConnectionList connectionList=new Gson().fromJson(result, ConnectionList.class);
				//filterFriends(connectionList.getUserDTO());
				NewConnectionFragmentAdapter.connectionList=connectionList.getUserDTO();
				NewConnectionFragmentAdapter newConnectionFragmentAdapter=new NewConnectionFragmentAdapter(dashboardActivity);
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
