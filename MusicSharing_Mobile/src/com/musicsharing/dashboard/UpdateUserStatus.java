package com.musicsharing.dashboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.musicsharing.account.UserUtil;
import com.musicsharing.connections.ConnectionList;
import com.musicsharing.connections.Connections;
import com.musicsharing.utils.Utils;
import com.musicsharing.web.TAListener;
import com.musicsharing.web.TAWebServiceAsyncTask;
import com.musicsharing.web.WebServiceConstants;

public class UpdateUserStatus {
	public static List<Connections>  friendList;
	
	static {
		friendList=new ArrayList<Connections>();
		Connections connections= new Connections();
		connections.setName("Loading..");
		friendList.add(connections);
	}
	
	
	public static void update(final Activity activity) {

		String userId = UserUtil.getUserId(activity);
		if (userId == null||!Utils.isNetworkAvailable(activity)) {
			return;
		}
		new TAWebServiceAsyncTask(activity, new TAListener() {

			@Override
			public void onTaskFailed(String errorMessage) {

			}

			@Override
			public void onTaskCompleted(String result) {
				 // Log.e("update status",result);
				ConnectionList connectionList = new Gson().fromJson(result,
						ConnectionList.class);
				friendList = connectionList.getUserDTO();
				performSort();
				Intent sendIntent = new Intent();
				sendIntent.setAction("update");
				activity.sendBroadcast(sendIntent);
			

			}
		}, WebServiceConstants.UPDATE_MY_STATUS + userId, false, null)
				.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
						(Void[]) null);
	}

	public static void performSort() {

		Collections.sort(friendList, new Comparator<Connections>() {
			@Override
			public int compare(Connections lhs, Connections rhs) {
				return lhs.getName().compareToIgnoreCase(rhs.getName());
			}
		});

	}
	
	public static String getFriendName(String userId)
	{
		String userName=null;
		Iterator<Connections> iterator=friendList.iterator();
		while (iterator.hasNext()) {
			Connections connections=iterator.next();
			if (connections.getUserId().equals(userId)) {
				userName=connections.getName();
				break;
			}
			
		}
		return userName;
		
	}
}
