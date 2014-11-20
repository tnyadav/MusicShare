package com.musicsharing.connections;

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
import com.google.gson.Gson;
import com.musicsharing.account.Login;
import com.musicsharing.account.UserUtil;
import com.musicsharing.utils.NotificationUtils;
import com.musicsharing.web.TAListener;
import com.musicsharing.web.TAPOSTWebServiceAsyncTask;
import com.musicsharing.web.WebServiceConstants;

public class PendingConnectionActivityAdapter extends ArrayAdapter<Connections> {

	private LayoutInflater inflater;
	public  List<Connections> connectionList;
	private Activity activity;

	public PendingConnectionActivityAdapter(Activity activity,List<Connections> connectionList) {
		super(activity, R.layout.item_pendingconnection, connectionList);
		this.activity=activity;
		this.connectionList=connectionList;

		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	}

	private class ViewHolder {
		TextView tvName;
		Button btnRespond;
		ImageView ivIcon;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Connections connections = connectionList.get(position);
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_pendingconnection, parent, false);
			
			
			
			holder.tvName = (TextView) convertView
					.findViewById(R.id.tvName);
			holder.btnRespond = (Button) convertView
					.findViewById(R.id.btnRespond);
	
			holder.ivIcon=(ImageView) convertView.findViewById(R.id.ivIcon);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		
	//	ImageLoader.getInstance().displayImage(connections.getThumbnailUrl(), holder.ivIcon);
		
		holder.tvName.setText(connections.getName());
		
	
		if (connections.getPendingForMyApproval()) {
			holder.btnRespond.setText("Accept");
			holder.btnRespond.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					String userId=UserUtil.getUserId(activity);
					String requesterUserId=connections.getUserId();
			    	if (userId==null) {
						return;
					}
			    	JSONObject json = new JSONObject();
					try {
						json.put("requesterUserId", requesterUserId); //this user has sent an connection request
						json.put("userId", userId); // this user is accepting the request
						json.put("connectionStatus", "ACCEPTED"); //ACCEPTED/REJECTED

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
	               new TAPOSTWebServiceAsyncTask(activity, true, "Please wait..",new TAListener() {
						
						@Override
						public void onTaskFailed(String errorMessage) {
							NotificationUtils.showNotificationToast(activity, "Unable to process Request");
							
						}
						
						@Override
						public void onTaskCompleted(String result) {
							Login login=new Gson().fromJson(result, Login.class);
							if (login.getStatusCode().equals("SUCCESS_001")) {
								connectionList.remove(position);
								notifyDataSetChanged();
							}
							
						}
					}, WebServiceConstants.UPDATE_CONNECTION, json.toString()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
							(Void[]) null);
			
					
				}
			});
			
		}else {
			holder.btnRespond.setText("Pending");
			holder.btnRespond.setOnClickListener(null);
			holder.btnRespond.setClickable(false);
			
		}
		return convertView;
	}

}
