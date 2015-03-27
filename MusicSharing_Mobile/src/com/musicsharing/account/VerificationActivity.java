package com.musicsharing.account;

import java.lang.reflect.Type;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.w3axis.sharehub.R;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.musicsharing.dashboard.BaseActivity;
import com.musicsharing.utils.CountryJsonConstant;
import com.musicsharing.utils.JainBooksConstants;
import com.musicsharing.utils.NotificationUtils;
import com.musicsharing.web.TAListener;
import com.musicsharing.web.TAWebServiceAsyncTask;
import com.musicsharing.web.WebServiceConstants;


public class VerificationActivity extends BaseActivity {
  
	private EditText etMobileNumber;
	private Button btnVerify;
	private String strCode;
	private String strMobileNumber,callingCode;
	private Spinner spinner;
	private  List<Country> countries;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.color.my_account));
		getActionBar().setTitle("Registration");
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		setContentView(R.layout.activity_varification);
		
		etMobileNumber = (EditText) 
				findViewById(R.id.etMobileNumber);
		
	    btnVerify=(Button)findViewById(R.id.btnVerify);
	    btnVerify.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				verify();
			}
		});
	    
	    Type type = new TypeToken<List<Country>>() {}.getType();
	    countries = new Gson().fromJson(CountryJsonConstant.countryJson,
				type);

	    
	    spinner=(Spinner)findViewById(R.id.countryCode);
	    spinner.setAdapter(new ArrayAdapter<Country>(activity,
                android.R.layout.simple_spinner_dropdown_item,
                countries));


	}
	private void verify() {
		
		 strMobileNumber = etMobileNumber.getText().toString();
		 Country country=(Country) spinner.getSelectedItem();
		 callingCode=country.getDial_code().substring(1);
		if (strMobileNumber == null || TextUtils.isEmpty(strMobileNumber)) {
			NotificationUtils.showNotificationToast(activity,
					"Please fill in your Mobile Number");
			return;
		}
		if (strMobileNumber.length()<10) {
			NotificationUtils.showNotificationToast(activity,
					"Please fill 10 digit Mobile Number");
			return;
		}

		
		TAListener taListener=new TAListener() {

			@Override
			public void onTaskCompleted(String result) {
				Login login = new Gson().fromJson(result,
						Login.class);

				if (null != login) {
					String statusCode =login.getStatusCode();
					if (statusCode.equals("ERROR_005")) {
						NotificationUtils.showNotificationToast(activity,
								login.getMessage());
						return;
					}
					
					strCode=login.getUserId();
					Intent intent2=new Intent(activity, RegistrationActivity.class);
					intent2.putExtra(JainBooksConstants.MOBILE_NUMBER, callingCode+strMobileNumber);
					intent2.putExtra("strCode", strCode);
					intent2.putExtra("callingCode", callingCode);
				    startActivity(intent2);
				    
					NotificationUtils.showNotificationToast(activity,
							"Verification code sent to your mobile number");
					//remove this code
					finish();
					
				} else {
					NotificationUtils.showNotificationToast(activity,
							"Server not responds");
				}
			}

			@Override
			public void onTaskFailed(String errorMessage) {
				NotificationUtils.showNotificationToast(activity,
						errorMessage);
				
			}/*
			
			@Override
			public void onTaskFailed(Bundle argBundle) {
				NotificationUtils.showNotificationToast(activity,
						"Server not responds");
				
			}
			
			@Override
			public void onTaskCompleted(Bundle argBundle) {


				String responseJSON = argBundle
						.getString(TAListener.LISTENER_BUNDLE_STRING_1);
				Log.e("RegistrationActivity", responseJSON);
			
				try {
					Login login = new Gson().fromJson(responseJSON,
							Login.class);

					if (null != login) {
						String statusCode =login.getStatusCode();
						if (statusCode.equals("ERROR_005")) {
							NotificationUtils.showNotificationToast(activity,
									login.getMessage());
							return;
						}
						
						strCode=login.getUserId();
						Intent intent2=new Intent(activity, RegistrationActivity.class);
						intent2.putExtra(JainBooksConstants.MOBILE_NUMBER, callingCode+strMobileNumber);
						intent2.putExtra("strCode", strCode);
						intent2.putExtra("callingCode", callingCode);
					    startActivity(intent2);
					    
						NotificationUtils.showNotificationToast(activity,
								"Verification code sent to your mobile number");
						//remove this code
						finish();
						
					} else {
						NotificationUtils.showNotificationToast(activity,
								"Server not responds");
					}
				} catch (JsonSyntaxException e) {
					NotificationUtils.showNotificationToast(activity,
							"Server not responds");
					e.printStackTrace();
				}

			}
		*/};
		
		new TAWebServiceAsyncTask(activity, taListener, WebServiceConstants.SEND_REGISTRATION_SMS+callingCode+strMobileNumber, true,"Loading").execute();
	}
	
	
@Override
public void onResume() {
	super.onResume();
	/*IntentFilter filter = new IntentFilter(IncomingSms.GET_SMS);
    registerReceiver(codeBroadcastReceiver, filter);*/
	
}
@Override
public void onPause() {
	super.onPause();
	//unregisterReceiver(codeBroadcastReceiver);
}
private BroadcastReceiver codeBroadcastReceiver = new BroadcastReceiver() {
	
	@Override
	public void onReceive(Context context, Intent intent) {
	//	String code=intent.getStringExtra(IncomingSms.CODE);
		/*etCode.setText(text)
		if (code.contains(strCode)) {
			Intent intent2=new Intent(activity, RegistrationActivity.class);
			intent2.putExtra(JainBooksConstants.MOBILE_NUMBER, strMobileNumber);
		    startActivity(intent2);
		}*/
		
	}
};

@Override
protected void setupUiComponent() {
	// TODO Auto-generated method stub
	
}
}
