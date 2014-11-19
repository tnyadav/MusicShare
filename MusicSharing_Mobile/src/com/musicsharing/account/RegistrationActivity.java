package com.musicsharing.account;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.example.musicsharing.R;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.musicsharing.dashboard.BaseActivity;
import com.musicsharing.dashboard.DashboardActivity;
import com.musicsharing.utils.JainBooksConstants;
import com.musicsharing.utils.NotificationUtils;
import com.musicsharing.utils.SharedPreferencesUtil;
import com.musicsharing.utils.Utils;
import com.musicsharing.web.TAListener;
import com.musicsharing.web.WebServiceConstants;


public class RegistrationActivity extends BaseActivity {
  
	private EditText etMobileNumber,etCode,etProfileName,etPassword1,etPassword2,countryCode;
	private Button btnRegister;
	private String strMobileNumber,strCode;
	private RadioGroup radioGroupTutorials;
	private String callingCode,paymentType="0";
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.color.my_account));
		getActionBar().setTitle("Registration");
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		setContentView(R.layout.activity_registration);
		strMobileNumber=getIntent().getStringExtra(JainBooksConstants.MOBILE_NUMBER);
		strCode=getIntent().getStringExtra("strCode");
		callingCode=getIntent().getStringExtra("callingCode");
		etMobileNumber = (EditText) 
				findViewById(R.id.etMobileNumber);
		etMobileNumber.setText(strMobileNumber);
		
		etCode = (EditText) 
				findViewById(R.id.etCode);
		etProfileName = (EditText) 
				findViewById(R.id.etProfileName);
		etPassword1 = (EditText) 
				findViewById(R.id.etPassword1);
		etPassword2 = (EditText) 
				findViewById(R.id.etPassword2);
		countryCode = (EditText) 
				findViewById(R.id.countryCode);
	//	countryCode.setText(callingCode);
		
		btnRegister=(Button)findViewById(R.id.btnRegister);
		btnRegister.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				register();
			}
		});
		  radioGroupTutorials = (RadioGroup) findViewById(R.id.operator);
		  radioGroupTutorials.check(R.id.rdOther);
		  
		  if (!callingCode.equalsIgnoreCase("91")) {
			  radioGroupTutorials.setVisibility(View.GONE);
			  paymentType="0";
		}
		  radioGroupTutorials.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId==R.id.rdOther) {
					paymentType="0";
				}else {
					paymentType="1";
				}
				

			}
		});
		
	}
	private void register() {


		if (!Utils.isNetworkAvailable(activity)) {
			NotificationUtils.showNotificationToast(activity, "No network connection");
			//return;
		}

		final String strProfileName;
		String strpassword1, strpassword2;
		strProfileName = etProfileName.getText().toString();
		String strCode1 = etCode.getText().toString();
		strpassword1 = etPassword1.getText().toString();
		strpassword2 = etPassword2.getText().toString();
		
		if (strProfileName == null || TextUtils.isEmpty(strProfileName)) {
			NotificationUtils.showNotificationToast(activity,
					"Please fill in your Profile Name");
			return;
		} else if (strpassword1 == null || TextUtils.isEmpty(strpassword1)) {
			NotificationUtils.showNotificationToast(activity,
					"Please fill in your Password");
			return;
		}else if (!strpassword1.equals(strpassword2)) {
				NotificationUtils.showNotificationToast(activity,
						"Password did not match");
				return;
			}
		else if (!strpassword1.equals(strpassword2)) {
			NotificationUtils.showNotificationToast(activity,
					"Password did not match");
			return;
		}
		 else if (strCode1 == null || TextUtils.isEmpty(strCode1)) {
				NotificationUtils.showNotificationToast(activity,
						"Please fill in your Verification code");
				return;
			}
		else if (!strCode.equals(strCode1)) {
			NotificationUtils.showNotificationToast(activity,
					"Verification code did not match");
			return;
		}
		

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("mobileNumber", strMobileNumber);
			jsonObject.put("name", strProfileName);
			jsonObject.put("password", strpassword1);
			/*jsonObject.put("loginSource", "direct");
			jsonObject.put("paymentType", paymentType);*/
			
		} catch (JSONException e) {
			e.printStackTrace();
			NotificationUtils.showNotificationToast(activity,
					"Please re-enter Username and Password");
		} catch (NullPointerException e) {
			e.printStackTrace();
			NotificationUtils.showNotificationToast(activity,
					"Please re-enter Username and Password");
		}

	/*	TAListener taListener = new TAListener() {

			@Override
			public void onTaskFailed(Bundle argBundle) {

			}

			@Override
			public void onTaskCompleted(Bundle argBundle) {

				try {
					String responseJSON = argBundle
							.getString(TAListener.LISTENER_BUNDLE_STRING_1);
					Log.e("RegistrationActivity", responseJSON);
					Gson gson=new Gson();
					Login login = gson.fromJson(responseJSON,
							Login.class);

					if (null != login) {

						if (login.getStatusCode().equalsIgnoreCase(
								"SUCCESS_001")) {
							User user = new User();
							user.setUserId(login.getUserId());
							String userString = gson.toJson(user);
							SharedPreferencesUtil.savePreferences(activity,
									SharedPreferencesUtil.USER, userString);
						
							finish();

						}
						NotificationUtils.showNotificationToast(activity,
								login.getMessage());
						finish();

					} else {
						NotificationUtils.showNotificationToast(activity,
								"Server not responds");
					}
				} catch (JsonSyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					NotificationUtils.showNotificationToast(activity,
							"Server not responds");
				}

			}
		};
*/
		TAListener taListener=new TAListener() {
			
			@Override
			public void onTaskFailed(String errorMessage) {
				NotificationUtils.showNotificationToast(activity,
						errorMessage);
			}
			
			@Override
			public void onTaskCompleted(String result) {
						Gson gson=new Gson();
				Login login = gson.fromJson(result,
						Login.class);

				if (null != login) {

					if (login.getStatusCode().equalsIgnoreCase(
							"SUCCESS_001")) {
						User user = new User();
						user.setUserId(login.getUserId());
						String userString = gson.toJson(user);
						SharedPreferencesUtil.savePreferences(activity,
								SharedPreferencesUtil.USER, userString);
					
						finish();

					}
					NotificationUtils.showNotificationToast(activity,
							login.getMessage());
					finish();

				} else {
					NotificationUtils.showNotificationToast(activity,
							"Server not responds");
				}

				
			}
		};
		DashboardActivity.registorOrAuthenticate(activity,
				jsonObject.toString(), taListener,
				WebServiceConstants.REGISTRATION);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	
		return true;
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
	}
	
	@Override
	protected void setupUiComponent() {
		// TODO Auto-generated method stub
		
	}
}
