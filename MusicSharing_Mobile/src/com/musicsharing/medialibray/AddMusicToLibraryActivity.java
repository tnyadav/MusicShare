package com.musicsharing.medialibray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.musicsharing.R;
import com.google.gson.Gson;
import com.musicsharing.dashboard.BaseActivity;
import com.musicsharing.dashboard.StickyListHeadersListViewBaseActivity;
import com.musicsharing.utils.SharedPreferencesUtil;
import com.musicsharing.view.StickyListHeadersListView;

public class AddMusicToLibraryActivity extends BaseActivity {
     Cursor musiccursor;
     List<AudioFile> musicList;
      ListView listView;
      List<AudioFile> savedMusicList;
    private void init_phone_music_grid() {
     System.gc();
 
        try{
        ContentResolver contentResolver =getContentResolver();
         Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
         musiccursor = contentResolver.query(uri, null, null, null, null);
         musicList=new ArrayList<AudioFile>();
         if (musiccursor == null) {
             // query failed, handle error.
         } else if (!musiccursor.moveToFirst()) {
             // no media on the device
          Toast.makeText(this, "No sdcard presents", Toast.LENGTH_SHORT).show();
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
        System.out.println("No sdcard presents");
        }

    }
    @Override
    @SuppressLint("NewApi")
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_addlibrary);
    	setupUiComponent();
    }
         

@Override
public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.addmusictolibrary, menu);
	return super.onCreateOptionsMenu(menu);
}
@Override
public boolean onOptionsItemSelected(MenuItem item) {

if (item.getItemId()==R.id.action_addmusic) {
	
	String savedMusicJSON =new Gson().toJson(savedMusicList);
    SharedPreferencesUtil.savePreferences(this, SharedPreferencesUtil.USER_LIBRARY, savedMusicJSON);
    setResult( RESULT_OK,getIntent());		

	onBackPressed();
}
	
	return super.onOptionsItemSelected(item);
}
	

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
/*	class IgnoreCaseComparator implements Comparator<String> {
		  public int compare(String strA, String strB) {
		    return strA.compareToIgnoreCase(strB);
		  }
		  IgnoreCaseComparator icc = new IgnoreCaseComparator();
	         java.util.Collections.sort(musicList,icc);
	}*/
	@Override
	protected void setupUiComponent() {
listView = (ListView)findViewById(R.id.stickyListViewSpeakersMain);
    	
    	init_phone_music_grid();
    	performSort();
    	listView.setAdapter(new ArrayAdapter<AudioFile>(this,
				R.layout.list_item, musicList));
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		 savedMusicList = new ArrayList<AudioFile>();
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				AudioFile audioFile = musicList.get(position);
				if (savedMusicList.contains(audioFile)) {
					savedMusicList.remove(audioFile);
				}else {
					savedMusicList.add(audioFile);
				}    
	                
	
			
			}

		});
   
    
	}
}
