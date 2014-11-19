package com.musicsharing.dashboard;

import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.musicsharing.R;
import com.musicsharing.connections.Connections;
import com.musicsharing.view.Sidebar;
import com.musicsharing.view.StickyListHeadersListView;

public abstract class StickyListHeadersListViewBaseFragment extends BaseFragment {


	protected StickyListHeadersListView listView;
	protected Sidebar indexBar;
	List<Connections> friendList;
	public  TextView indexScroller;


	@Override
	protected void setupUiComponent() {
		
		System.out.println("");
		listView = (StickyListHeadersListView) view
				.findViewById(R.id.stickyListViewSpeakersMain);
		indexBar = (Sidebar) view.findViewById(R.id.sideBar);
		indexBar.setVisibility(View.GONE);
        indexScroller = (TextView) view.findViewById(R.id.indexScroller);
        DisplayMetrics displaymetrics = new DisplayMetrics();
		getActivity().getWindowManager()
				.getDefaultDisplay()
				.getMetrics(displaymetrics);
		int width = displaymetrics.widthPixels;
	    indexBar.setListView(listView, width,indexScroller);
		
	}
public abstract void  performSort();
}
