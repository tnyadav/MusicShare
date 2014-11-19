package com.musicsharing.connections;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.example.musicsharing.R;
import com.musicsharing.view.StickyListHeadersAdapter;

public class FriendLibraryActivityAdapter extends BaseAdapter implements
		StickyListHeadersAdapter, SectionIndexer {
	private Activity mActivity;
	private LayoutInflater inflater;
	private String mSections = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	List<FriendLibrarySong>  musicList;

	public FriendLibraryActivityAdapter(Activity argActivity, List<FriendLibrarySong> au) {
		 mActivity = argActivity;
		inflater = (LayoutInflater) mActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 this.musicList=au;
		
	}

	@Override
	public int getCount() {
		try {
			return musicList.size();
		} catch (NullPointerException e) {
			return 0;
		}
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	private class ViewHolder {
		TextView tvName;
		TextView tvStatus;
		ImageView ivIcon;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		System.gc();
    convertView = inflater.inflate(R.layout.item_media, parent, false);
    TextView tv = (TextView)convertView.findViewById(R.id.tvMedia);
    tv.setText(musicList.get(position).getName());
    return convertView;
	}

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) { 

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.speaker_main_list_header,
					null);
		}

		TextView textViewDrawerHeader = (TextView) convertView
				.findViewById(R.id.textViewDrawerHeader);
		
		
		try {
//			String[] lhsname = speakersMainData.getSpeakers().get(position)
//					.getName().split(" ");
			String lhslast = musicList.get(position).getName();
					
			textViewDrawerHeader.setText(""
					+ lhslast.charAt(0));
		} catch (IndexOutOfBoundsException e) {
			textViewDrawerHeader.setText("");
		} catch (NullPointerException e) {
			textViewDrawerHeader.setText("");
		}

		return convertView;
	
		
	}
	

	@Override
	public long getHeaderId(int position) {
		try {
//			String[] lhsname = speakersMainData.getSpeakers().get(position)
//					.getName().split(" ");
			String lhslast =  musicList.get(position).getName();
			return lhslast.charAt(0);
		} catch (IndexOutOfBoundsException e) {
			return 0;
		} catch (NullPointerException e) {
			return 0;
		}
	}

	@Override
	public int getPositionForSection(int arg0) {

		for (int i = 0; i < musicList.size(); i++) {
//			String[] lhsname = speakersMainData.getSpeakers().get(i)
//					.getName().split(" ");
			String lhslast = musicList.get(arg0).getName();
				
			char firstChar = lhslast.toUpperCase().charAt(0);
			if (firstChar == arg0) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public int getSectionForPosition(int arg0) {
		return 0;
	}

	@Override
	public Object[] getSections() {
		String[] sections = new String[mSections.length()];
		for (int i = 0; i < mSections.length(); i++)
			sections[i] = String.valueOf(mSections.charAt(i));
		return sections;
	}

}
