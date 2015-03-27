package com.musicsharing.connections;

import java.util.Timer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import com.w3axis.sharehub.R;
import com.musicsharing.MyMedialibray.AddMusicToLibraryActivity;
import com.musicsharing.dashboard.StickyListHeadersListViewBaseFragment;
import com.musicsharing.dashboard.UpdateUserStatus;
import com.musicsharing.utils.NotificationUtils;

public class ConnectionFragment extends StickyListHeadersListViewBaseFragment {

	private ConnectionFragmentAdapter adapter;
	private Timer timer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (savedInstanceState == null) {

		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_connections, null);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);

		setupUiComponent();

		adapter = new ConnectionFragmentAdapter(getActivity());
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				Connections connections = UpdateUserStatus.friendList.get(arg2);
				if (connections.getUserId()!=null&& connections.getUserStatus().equals("online")) {
					FriendLibraryActivity.friendId = connections.getUserId();
					FriendLibraryActivity.friendName = connections.getName();
					Intent intent = new Intent(dashboardActivity,
							FriendLibraryActivity.class);
					startActivity(intent);
				} else {
					NotificationUtils.showNotificationToast(dashboardActivity,
							connections.getName() + " is offline");
				}

			}
		});
		Button pending = (Button) view.findViewById(R.id.pending);
		pending.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dashboardActivity.startActivity(new Intent(dashboardActivity,
						PendingConnectionActivity.class));

			}
		});
		Button getAll = (Button) view.findViewById(R.id.getAll);
		getAll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dashboardActivity.startActivity(new Intent(dashboardActivity,
						NewConnectionActivity.class));

			}
		});

	}

	public void refreshViews() {
		// indexBar.setVisibility(View.VISIBLE);
		adapter.notifyDataSetChanged();

	}

	@Override
	public void performSort() {

	}

	@Override
	public void onResume() {

		IntentFilter filter = new IntentFilter("update");
		dashboardActivity.registerReceiver(myBroadcastReceiver, filter);

		super.onResume();
	}

	@Override
	public void onPause() {
		dashboardActivity.unregisterReceiver(myBroadcastReceiver);
		super.onPause();
	}

	private BroadcastReceiver myBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			refreshViews();
		}
	};
}
