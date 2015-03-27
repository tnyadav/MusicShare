package com.musicsharing.MyMedialibray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;

import com.w3axis.sharehub.R;
import com.google.gson.Gson;
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
	// syncAllSongs();
	/* add=(Button)view.findViewById(R.id.add);
	 add.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
		Intent intent=new Intent(dashboardActivity, AddMusicToLibraryActivity.class);
	    startActivityForResult(intent, REQUEST_CODE);
			
		}
	});*/
	
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
/*
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
		
		
		
	}*/

	private void saveMusicListToServer(final List<AudioFile> savedMusicList,String json) {

		

		new TAPOSTWebServiceAsyncTask(dashboardActivity, false, "",
				new TAListener() {

					@Override
					public void onTaskFailed(String errorMessage) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onTaskCompleted(String result) {
						String result1 = result;
						Log.e("resul of add library", result1);
						NotificationUtils.showNotificationToast(
								dashboardActivity, "Library updated");
						String savedMusicJSON = new Gson()
								.toJson(savedMusicList);
						SharedPreferencesUtil.savePreferences(
								dashboardActivity,
								SharedPreferencesUtil.USER_LIBRARY,
								savedMusicJSON);

					}
				}, WebServiceConstants.ADD_TO_MY_LIBRARY, json)
				.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
						(Void[]) null);

	}
	

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
	
	private void syncAllSongs() {
		
		new AsyncTask<Void, Void, String>() {
			 ArrayList<AudioFile> savedMusicList;

			@Override
			protected String doInBackground(Void... params) {
				 System.gc();
				 savedMusicList = new ArrayList<AudioFile>();
				 try{
				        ContentResolver contentResolver =dashboardActivity.getContentResolver();
				         Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				         musiccursor = contentResolver.query(uri, null, null, null, null);
				        
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
									String thisTitle = musiccursor.getString(titleColumn);
									thisTitle = thisTitle.substring(0, 1).toUpperCase()
											+ thisTitle.substring(1);
									String strPath = musiccursor.getString(path);
									musicList.add(new AudioFile(null, thisTitle, strPath));
									musiccursor.moveToNext();
								}
							}
				         
				        
				        }
				        catch(Exception e){
				        
				        }
				 
				 
				    String userId = UserUtil.getUserId(dashboardActivity);
					
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
				 saveMusicListToServer(savedMusicList,result);
				
			}
		};

	    
	 
	        

	    
		
	}
}
