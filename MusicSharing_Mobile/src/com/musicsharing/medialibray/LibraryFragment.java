package com.musicsharing.medialibray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
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

import com.example.musicsharing.R;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.musicsharing.account.UserUtil;
import com.musicsharing.dashboard.StickyListHeadersListViewBaseFragment;
import com.musicsharing.utils.NotificationUtils;
import com.musicsharing.utils.SharedPreferencesUtil;
import com.musicsharing.web.TAListener;
import com.musicsharing.web.TAPOSTWebServiceAsyncTask;
import com.musicsharing.web.WebServiceConstants;

public class LibraryFragment extends StickyListHeadersListViewBaseFragment {
   
   private final int REQUEST_CODE=100;	
   private  Cursor musiccursor;
   private List<AudioFile> musicList;
   private Button add;

    private void init_phone_music_grid() {
    	try {
			String savedMusicJSON =SharedPreferencesUtil.getPreferences(dashboardActivity, SharedPreferencesUtil.USER_LIBRARY, null);
			Type type = new TypeToken<List<AudioFile>>() {
			}.getType();
			musicList=gson.fromJson(savedMusicJSON, type);
			if (musicList!=null) {
				 performSort();
			}
			
		} catch (JsonSyntaxException e) {
			musicList=new ArrayList<AudioFile>();
			e.printStackTrace();
		}
 
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	setHasOptionsMenu(true);
    }
	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container, Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.fragment_library, null);
		return view;
	}


@Override
public void onActivityCreated( Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);
	 setupUiComponent();
	 add=(Button)view.findViewById(R.id.add);
	 add.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
		Intent intent=new Intent(dashboardActivity, AddMusicToLibraryActivity.class);
	    startActivityForResult(intent, REQUEST_CODE);
			
		}
	});
	
	// indexBar.setVisibility(View.VISIBLE);
}
@Override
public void onResume() {
	      init_phone_music_grid();
		  LibraryFragmentAdapter libraryFragmentAdapter=new LibraryFragmentAdapter(dashboardActivity, musicList);
		  listView.setAdapter(libraryFragmentAdapter);
		  listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String path=musicList.get(arg2).getPath();
				 /* try 
		          {
		                MediaPlayer  mMediaPlayer=new MediaPlayer();
						
		                mMediaPlayer.setDataSource(path);
		                mMediaPlayer.prepare();
		                mMediaPlayer.start();
		              
		          } 
		          catch (Exception e) {}*/
			}
		});
	super.onResume();
}
	@Override
	public void performSort() {


		Collections.sort(musicList,
				new Comparator<AudioFile>() {
			@Override
			public int compare(AudioFile lhs,
					AudioFile rhs) {
				return lhs.getDiaplayName().compareToIgnoreCase(rhs.getDiaplayName());
	
			}
		});


	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		 List<AudioFile> savedMusicList = null;
		if (requestCode==REQUEST_CODE&&resultCode==Activity.RESULT_OK) {
			try {
				String savedMusicJSON =SharedPreferencesUtil.getPreferences(dashboardActivity, SharedPreferencesUtil.USER_LIBRARY, null);
				Type type = new TypeToken<List<AudioFile>>() {
				}.getType();
				savedMusicList=gson.fromJson(savedMusicJSON, type);
				if (musicList!=null) {
					 performSort();
				}
				
			} catch (JsonSyntaxException e) {
				e.printStackTrace();
			}
			if (savedMusicList!=null) {
				saveMusicListToServer(savedMusicList);
			}
			
		}
		
		
		
	}
	private void saveMusicListToServer(List<AudioFile> savedMusicList) {
		
		String userId=UserUtil.getUserId(dashboardActivity);
    	if (userId==null) {
			return;
		}
		JSONArray libArray = new JSONArray();
		JSONObject json = new JSONObject();
		try {
		if (savedMusicList.size()>0) {
			
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
		
				new TAPOSTWebServiceAsyncTask(dashboardActivity, false, "",new TAListener() {
					
					@Override
					public void onTaskFailed(String errorMessage) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onTaskCompleted(String result) {
						String result1=result;
						Log.e("resul of add library", result1);
						NotificationUtils.showNotificationToast(dashboardActivity, "Library updated");
						
					}
				}, WebServiceConstants.ADD_TO_MY_LIBRARY, json.toString()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
						(Void[]) null);
		
		
	}
}
