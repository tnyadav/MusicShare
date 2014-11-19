package com.musicsharing.dashboard;

import java.util.Timer;
import java.util.TimerTask;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
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
import com.musicsharing.account.LoginActivity;
import com.musicsharing.account.User;
import com.musicsharing.account.UserUtil;
import com.musicsharing.connections.ConnectionFragment;
import com.musicsharing.medialibray.LibraryFragment;
import com.musicsharing.mqtt.MQTTConnection;
import com.musicsharing.mqtt.MQTTConnectionServiceImpl;
import com.musicsharing.newconnection.NewConnectionFragment;
import com.musicsharing.web.TAListener;
import com.musicsharing.web.TAPOSTWebServiceAsyncTask;

public class DashboardActivity extends FragmentActivity implements
		ActionBar.TabListener {
	private static final String[] CONTENT = new String[] { "Connections",
			"Media List", "Add New" };

	SectionsPagerAdapter mSectionsPagerAdapter;

	ViewPager mViewPager;
	private Timer timer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final ActionBar actionBar = getActionBar();
		actionBar.setBackgroundDrawable(
				getResources().getDrawable(R.drawable.top));
		actionBar.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent))); 
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
		
		initializeMQTT();
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
				fragment = new NewConnectionFragment();
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
        bedMenuItem.setTitle(UserUtil.getUser(DashboardActivity.this).getName());
       
		return true;
	}
@Override
public boolean onOptionsItemSelected(MenuItem item) {
	if (item.getItemId()==R.id.action_user) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);
 
			// set title
			alertDialogBuilder.setTitle(R.string.app_name);
			// set dialog message
			alertDialogBuilder
				.setMessage("Do you want to Logout ?")
				.setCancelable(false)
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						UserUtil.logout(DashboardActivity.this);
						UpdateUserStatus.friendList=null;
						startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
						finish();
					}
				  })
				.setNegativeButton("No",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
						 try 
				          {
				                MediaPlayer  mMediaPlayer=new MediaPlayer();
								
				                mMediaPlayer.setDataSource(DashboardActivity.this.getExternalCacheDir()+"music.mp3");
				                mMediaPlayer.prepare();
				                mMediaPlayer.start();
				              
				          } 
				          catch (Exception e) {
				        	  
				        	  
				          }
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
		new TAPOSTWebServiceAsyncTask(mActivity, true,"Loading", listener, url,
				argJSONString).executeOnExecutor(
				AsyncTask.THREAD_POOL_EXECUTOR, (Void[]) null);
	}

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		overridePendingTransition(R.animator.slide_in_right,
				R.animator.slide_out_left);
	}

	@Override
	public void onResume() {
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {

				UpdateUserStatus.update(DashboardActivity.this);

			}
		}, 5000, 10000);
		super.onResume();
	}

	@Override
	public void onPause() {
		if (timer != null) {
			timer.cancel();
		}
		super.onPause();
	}

	private void initializeMQTT() {
		User user = UserUtil.getUser(DashboardActivity.this);
		if (user != null) {
			MQTTConnectionServiceImpl.connectAndSubscribeMQTT(user,DashboardActivity.this);

		}

	}
}
