package com.musicsharing.newconnection;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musicsharing.R;
import com.musicsharing.account.UserUtil;
import com.musicsharing.connections.Connections;
import com.musicsharing.utils.NotificationUtils;
import com.musicsharing.web.TAListener;
import com.musicsharing.web.TAPOSTWebServiceAsyncTask;
import com.musicsharing.web.WebServiceConstants;

public class NewConnectionFragmentAdapter extends ArrayAdapter<Connections> {

	private LayoutInflater inflater;
	public static List<Connections> connectionList;
	private Activity activity;

	public NewConnectionFragmentAdapter(Activity activity) {
		super(activity, R.layout.item_newconnection, connectionList);
		this.activity=activity;
		

		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}

	private class ViewHolder {
		TextView tvName;
		Button btnSendRequest;
		ImageView ivIcon;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Connections connections = connectionList.get(position);
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_newconnection, parent, false);
			
			
			
			holder.tvName = (TextView) convertView
					.findViewById(R.id.tvName);
			holder.btnSendRequest = (Button) convertView
					.findViewById(R.id.btnSendRequest);
			holder.btnSendRequest.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					String userId=UserUtil.getUserId(activity);
					String to=connections.getUserId();
			    	if (userId==null) {
						return;
					}
			    	JSONObject json = new JSONObject();
					try {
						json.put("fromUserId", userId);
						json.put("toUserId",to);

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
	               new TAPOSTWebServiceAsyncTask(activity, true, "Please wait..",new TAListener() {
						
						@Override
						public void onTaskFailed(String errorMessage) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onTaskCompleted(String result) {
							String result1=result;
							NotificationUtils.showNotificationToast(activity, "Request sent");
							
						}
					}, WebServiceConstants.ADD_CONNECTION, json.toString()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
							(Void[]) null);
			
					
				}
			});
			holder.ivIcon=(ImageView) convertView.findViewById(R.id.ivIcon);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		
	//	ImageLoader.getInstance().displayImage(connections.getThumbnailUrl(), holder.ivIcon);
		
		holder.tvName.setText(connections.getName());
		
		return convertView;
	}

}
