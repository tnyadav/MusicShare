package com.musicsharing.dashboard;

import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import com.example.musicsharing.R;
import com.musicsharing.view.Sidebar;
import com.musicsharing.view.StickyListHeadersListView;

public abstract class StickyListHeadersListViewBaseActivity extends BaseActivity {


	protected StickyListHeadersListView listView;
	protected Sidebar indexBar;
	protected  TextView indexScroller;



	@Override
	protected void setupUiComponent() {
		listView = (StickyListHeadersListView)findViewById(R.id.stickyListViewSpeakersMain);
		indexBar = (Sidebar) findViewById(R.id.sideBar);
		indexBar.setVisibility(View.GONE);
        indexScroller = (TextView) findViewById(R.id.indexScroller);
        DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager()
				.getDefaultDisplay()
				.getMetrics(displaymetrics);
		int width = displaymetrics.widthPixels;
	    indexBar.setListView(listView, width,indexScroller);
		
	}
public abstract void  performSort();
}
