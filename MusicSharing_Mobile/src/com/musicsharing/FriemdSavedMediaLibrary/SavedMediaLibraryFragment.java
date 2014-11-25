package com.musicsharing.FriemdSavedMediaLibrary;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.musicsharing.R;
import com.musicsharing.dashboard.BaseFragment;

public class SavedMediaLibraryFragment extends BaseFragment {

	private List<String> stringList = null;
	private ListView listSavedFriends;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_savedfriends, null);
		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setupUiComponent();
	}

	@Override
	protected void setupUiComponent() {

		try {
			File sdCard = Environment.getExternalStorageDirectory();
			File dir = new File(sdCard.getAbsolutePath() + "/MusicShare/");
			String[] files = dir.list();
			stringList = new ArrayList<String>(Arrays.asList(files));

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// final List<Pooja> poja;
		listSavedFriends = (ListView) view.findViewById(R.id.listSavedFriends);
		if (stringList != null && stringList.size() > 0) {
			listSavedFriends.setAdapter(new SavedMediaLibraryFragmentAdapter(
					getActivity(), stringList));
			listSavedFriends.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {

					Intent intent = new Intent(getActivity(),
							FriendSavedMediaListActivity.class);
					FriendSavedMediaListActivity.friendName = stringList
							.get(arg2);
					getActivity().startActivity(intent);

				}
			});
		}

	}

}
