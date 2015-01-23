package com.musicsharing.dashboard;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.example.musicsharing.R;
import com.google.gson.Gson;
import com.musicsharing.FriemdSavedMediaLibrary.SavedMediaLibraryFragment;
import com.musicsharing.MyMedialibray.AudioFile;
import com.musicsharing.MyMedialibray.LibraryFragment;
import com.musicsharing.account.Login;
import com.musicsharing.account.LoginActivity;
import com.musicsharing.account.User;
import com.musicsharing.account.UserUtil;
import com.musicsharing.connections.AlarmReceiver;
import com.musicsharing.connections.ConnectionFragment;
import com.musicsharing.mqtt.MQTTConnectionServiceImpl;
import com.musicsharing.utils.NotificationUtils;
import com.musicsharing.utils.SharedPreferencesUtil;
import com.musicsharing.utils.Utils;
import com.musicsharing.web.TAListener;
import com.musicsharing.web.TAPOSTWebServiceAsyncTask;
import com.musicsharing.web.WebServiceConstants;

public class DashboardActivity extends FragmentActivity implements
		ActionBar.TabListener {
	private static final String[] CONTENT = new String[] { "Connections",
			"Media List", "Downloads" };

	SectionsPagerAdapter mSectionsPagerAdapter;

	ViewPager mViewPager;
	//private Timer timer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.top));
		actionBar.setIcon(new ColorDrawable(getResources().getColor(
				android.R.color.transparent)));
		actionBar.setTitle("");
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}

		// connect to mqtt
		/*timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {

				// UpdateUserStatus.update(DashboardActivity.this);
				User user = UserUtil.getUser(DashboardActivity.this);
				if (user != null
						&& Utils.isNetworkAvailable(DashboardActivity.this)) {

					try {
						String responseJson = new JSONParser()
								.getJSONFromUrl(WebServiceConstants.UPDATE_MY_STATUS
										+ user.getUserId());
						ConnectionList connectionList = new Gson().fromJson(
								responseJson, ConnectionList.class);
						UpdateUserStatus.friendList = connectionList
								.getUserDTO();
						UpdateUserStatus.performSort();
						Intent sendIntent = new Intent();
						sendIntent.setAction("update");
						sendBroadcast(sendIntent);
						Log.e("dashboard", "status updated");
					} catch (JsonSyntaxException e) {
						Log.e("dashboard", "status not updated");
						e.printStackTrace();
					}
				}
			}
		}, 5000, 10000);*/
		
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				initializeMQTT();
				return null;
			}
		}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[]) null);
		startUpdate();
		syncAllSongs();
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {

		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = null;
			switch (position) {
			case 0:
				fragment = new ConnectionFragment();
				break;
			case 1:
				fragment = new LibraryFragment();
				break;
			case 2:
				fragment = new SavedMediaLibraryFragment();
				break;

			default:
				break;
			}

			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {

			return CONTENT[position];

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.blank, menu);
		MenuItem bedMenuItem = menu.findItem(R.id.action_user);
		bedMenuItem
				.setTitle(UserUtil.getUser(DashboardActivity.this).getName());

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_user) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);

			// set title
			alertDialogBuilder.setTitle(R.string.app_name);
			// set dialog message
			alertDialogBuilder
					.setMessage("Do you want to Logout ?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									UserUtil.logout(DashboardActivity.this);
									UpdateUserStatus.friendList = null;
									startActivity(new Intent(
											DashboardActivity.this,
											LoginActivity.class));
									finish();
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// if this button is clicked, just close
									// the dialog box and do nothing
									dialog.cancel();
									
								}
							});

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();
		}
		return super.onOptionsItemSelected(item);
	}

	public static void registorOrAuthenticate(Activity mActivity,
			String argJSONString, TAListener listener, String url) {
		new TAPOSTWebServiceAsyncTask(mActivity, true, "Loading", listener,
				url, argJSONString).executeOnExecutor(
				AsyncTask.THREAD_POOL_EXECUTOR, (Void[]) null);
	}

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		overridePendingTransition(R.animator.slide_in_right,
				R.animator.slide_out_left);
	}

	@Override
	protected void onDestroy() {
		/*if (timer != null) {
			timer.cancel();
		}*/
		stopUpdate();
		super.onDestroy();
	}

	private void initializeMQTT() {
		User user = UserUtil.getUser(DashboardActivity.this);
		if (user != null && Utils.isNetworkAvailable(DashboardActivity.this)) {
			MQTTConnectionServiceImpl.connectAndSubscribeMQTT(user,
					DashboardActivity.this);

		}

	}

	private void startUpdate() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, 1);

		Intent intent = new Intent(DashboardActivity.this,
				AlarmReceiver.class);

		PendingIntent tracking = PendingIntent.getBroadcast(
				DashboardActivity.this, 0, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);

		AlarmManager alarms = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarms.setInexactRepeating(AlarmManager.RTC_WAKEUP,
				cal.getTimeInMillis(), 10000, tracking);

	}

	private void stopUpdate() {
		Intent intent = new Intent(DashboardActivity.this,
				AlarmReceiver.class);
		PendingIntent tracking = PendingIntent.getBroadcast(
				DashboardActivity.this, 0, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager alarms = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarms.cancel(tracking);
	}

	private void syncAllSongs() {

		new AsyncTask<Void, Void, String>() {
			ArrayList<AudioFile> savedMusicList;

			@Override
			protected String doInBackground(Void... params) {
				System.gc();
				savedMusicList = new ArrayList<AudioFile>();
				try {
					ContentResolver contentResolver = getContentResolver();
					Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
					Cursor musiccursor = contentResolver.query(uri, null, null,
							null, null);

					if (musiccursor == null) {
						// query failed, handle error.
					} else if (!musiccursor.moveToFirst()) {

					} else {
						int titleColumn = musiccursor
								.getColumnIndex(android.provider.MediaStore.Audio.Media.DISPLAY_NAME);
						int idColumn = musiccursor
								.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
						int path = musiccursor
								.getColumnIndex(android.provider.MediaStore.Audio.Media.DATA);
						for (int i = 0; i < musiccursor.getCount(); i++) {
							String thisTitle = musiccursor
									.getString(titleColumn);
							thisTitle = thisTitle.substring(0, 1).toUpperCase()
									+ thisTitle.substring(1);
							String strPath = musiccursor.getString(path);
							savedMusicList.add(new AudioFile(null, thisTitle,
									strPath));
							musiccursor.moveToNext();
						}
					}

				} catch (Exception e) {

				}

				String userId = UserUtil.getUserId(DashboardActivity.this);

				JSONArray libArray = new JSONArray();
				JSONObject json = new JSONObject();
				try {
					if (savedMusicList.size() > 0) {

						for (int i = 0; i < savedMusicList.size(); i++) {

							AudioFile audioFile = savedMusicList.get(i);
							JSONObject json1 = new JSONObject();
							json1.put("name", audioFile.getDiaplayName());
							json1.put("fileName", audioFile.getPath());
							libArray.put(i, json1);
						}

					}

					json.put("userId", userId);
					json.put("userLibraryDTOList", libArray);

				} catch (JSONException e) {
					e.printStackTrace();
				}
				return json.toString();
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				if (Utils.isNetworkAvailable(DashboardActivity.this)) {
					saveMusicListToServer(savedMusicList, result);
				}
				

			}
		}.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[]) null);

	}

	private void saveMusicListToServer(final List<AudioFile> savedMusicList,
			String json) {

		new TAPOSTWebServiceAsyncTask(DashboardActivity.this, false, "",
				new TAListener() {

					@Override
					public void onTaskFailed(String errorMessage) {
						syncAllSongs();
					}

					@Override
					public void onTaskCompleted(String result) {
						Login login = new Gson().fromJson(result, Login.class);
						if (login.getStatusCode().equals("SUCCESS_012")) {
							NotificationUtils.showNotificationToast(
									DashboardActivity.this, "Library updated");
							String savedMusicJSON = new Gson()
									.toJson(savedMusicList);
							SharedPreferencesUtil.savePreferences(
									DashboardActivity.this,
									SharedPreferencesUtil.USER_LIBRARY,
									savedMusicJSON);
						}
					}
				}, WebServiceConstants.ADD_TO_MY_LIBRARY, json)
				.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
						(Void[]) null);

	}
}
