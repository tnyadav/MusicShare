package com.musicsharing.FriemdSavedMediaLibrary;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musicsharing.R;

public class SavedMediaLibraryFragmentAdapter extends ArrayAdapter<String> {

	private LayoutInflater inflater;
	private List<String> poojaCategoryList;
	private Activity activity;

	public SavedMediaLibraryFragmentAdapter(Activity activity, 
			List<String> newsList) {
		super(activity, R.layout.item_friend, newsList);
		this.poojaCategoryList = newsList;
		this.activity=activity;
		

		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}

	private class ViewHolder {
		TextView tvTitle;
		ImageView ivNewsIcon;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final String name = poojaCategoryList.get(position);
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_friend, parent, false);
			
			
			
			holder.tvTitle = (TextView) convertView
					.findViewById(R.id.tvMedia);
			
			//holder.ivNewsIcon=(ImageView) convertView.findViewById(R.id.tvMedia);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.tvTitle.setText(name);
	/*holder.ivNewsIcon.setImageDrawable(getName(news.toLowerCase()));	
		holder.tvTitle.setText(news);*/
		
		
		return convertView;
	}

}
