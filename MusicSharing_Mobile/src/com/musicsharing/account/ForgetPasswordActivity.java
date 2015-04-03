package com.musicsharing.account;

import java.lang.reflect.Type;
import java.util.List;

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

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.musicsharing.dashboard.BaseActivity;
import com.musicsharing.utils.CountryJsonConstant;
import com.musicsharing.utils.NotificationUtils;
import com.musicsharing.web.TAListener;
import com.musicsharing.web.TAWebServiceAsyncTask;
import com.musicsharing.web.WebServiceConstants;
import com.w3axis.sharehub.R;


public class ForgetPasswordActivity extends BaseActivity {
  
	private EditText etMobileNumber;
	private Button btnVerify;
	private String strCode;
	private String strMobileNumber,callingCode;
	private Spinner spinner;
	private  List<Country> countries;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getActionBar().setTitle("Password Recovery");
		setContentView(R.layout.activity_forgetpassword);
		
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
			public void onTaskFailed(String argBundle) {
				NotificationUtils.showNotificationToast(activity,
						"Server not responds");
				
			}
			
			@Override
			public void onTaskCompleted(String responseJSON) {


			
				Log.e("RegistrationActivity", responseJSON);
				try {
					Login login = new Gson().fromJson(responseJSON,
							Login.class);

					if (null != login) {
						String statusCode =login.getStatusCode();
						if (statusCode.equals("SUCCESS_050")) {
							NotificationUtils.showNotificationToast(activity,
									login.getMessage());
							finish();
						}else {
							NotificationUtils.showNotificationToast(activity,
									login.getMessage());
						}
						
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
		};
		
		new TAWebServiceAsyncTask(activity, taListener,WebServiceConstants.FORGET_PASSWORD+callingCode+strMobileNumber,true, "Loading..").execute();
	}
	
	



@Override
public boolean onCreateOptionsMenu(Menu menu) {
	return true;
}


@Override
protected void setupUiComponent() {
	// TODO Auto-generated method stub
	
}
}
