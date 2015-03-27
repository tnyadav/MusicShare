package com.musicsharing.connections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.PhoneLookup;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;

import com.w3axis.sharehub.R;
import com.google.gson.Gson;
import com.musicsharing.account.UserUtil;
import com.musicsharing.dashboard.BaseActivity;
import com.musicsharing.utils.NotificationUtils;
import com.musicsharing.web.TAListener;
import com.musicsharing.web.TAWebServiceAsyncTask;
import com.musicsharing.web.WebServiceConstants;

public final class NewConnectionActivity extends BaseActivity {
	private static final String CONNECTION_LIST = "connection";
	private ListView listConnection;
	// private SearchView searchView;
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
		// searchView = (SearchView) findViewById(R.id.searchView);
		listConnection = (ListView) findViewById(R.id.listConnection);
		listConnection.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

			}
		});

		getConnection(true, "Getting connections");
	}

	private void getConnection(boolean showProgress, String loadingMessage) {

		String userId = UserUtil.getUserId(activity);
		if (userId == null) {
			return;
		}
		new TAWebServiceAsyncTask(
				activity,
				new TAListener() {

					@Override
					public void onTaskFailed(String errorMessage) {

						NotificationUtils.showNotificationToast(activity,
								errorMessage);
					}

					@Override
					public void onTaskCompleted(String result) {

						ConnectionList connectionList = new Gson().fromJson(
								result, ConnectionList.class);

						// filterFriends(connectionList.getUserDTO());
						NewConnectionActivitytAdapter.connectionList = getPhoneFriends(connectionList);
						NewConnectionActivitytAdapter newConnectionFragmentAdapter = new NewConnectionActivitytAdapter(
								activity);
						listConnection.setAdapter(newConnectionFragmentAdapter);
						newConnectionFragmentAdapter.notifyDataSetChanged();
					}
				}, WebServiceConstants.GET_ALL_USERS + userId, showProgress,
				loadingMessage).execute();
	}

	private void filterFriends(List<Connections> connectionList) {
		friendList = new ArrayList<Connections>();
		PendingFriendList = new ArrayList<Connections>();
		Iterator<Connections> iterator = connectionList.iterator();
		while (iterator.hasNext()) {
			Connections connections = iterator.next();
			if (connections.getConnectionStatus().equalsIgnoreCase("accepted")) {
				friendList.add(connections);
			} else {
				PendingFriendList.add(connections);
			}

		}

	}

	private List<Connections> getPhoneFriends(ConnectionList connectionList) {
		List<Connections> filteredList = new ArrayList<Connections>();
		List<Connections> connections = connectionList.getUserDTO();
		for (Connections connections2 : connections) {
			String mobileNo = connections2.getUserMobileNumber();
			if (mobileNo != null && contactExists(mobileNo)) {
				filteredList.add(connections2);
			}
		}
		return filteredList;

	}

	public boolean contactExists(String number) {
		// / number is the phone number
		Uri lookupUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
				Uri.encode(number));
		Cursor cur = null;
		try {
			String[] mPhoneNumberProjection = { PhoneLookup._ID /*
																 * ,
																 * PhoneLookup.
																 * NUMBER,
																 * PhoneLookup
																 * .DISPLAY_NAME
																 */};

			cur = NewConnectionActivity.this.getContentResolver().query(
					lookupUri, mPhoneNumberProjection, null, null, null);

			if (cur.moveToFirst()) {
				return true;
			}

		} catch (Exception e) {

		} finally {

			if (cur != null)
				cur.close();
		}
		return false;
	}

}
