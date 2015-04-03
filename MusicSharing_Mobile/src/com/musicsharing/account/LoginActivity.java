package com.musicsharing.account;

import java.lang.reflect.Type;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.w3axis.sharehub.R;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.musicsharing.dashboard.BaseActivity;
import com.musicsharing.dashboard.DashboardActivity;
import com.musicsharing.utils.CountryJsonConstant;
import com.musicsharing.utils.NotificationUtils;
import com.musicsharing.utils.SharedPreferencesUtil;
import com.musicsharing.utils.Utils;
import com.musicsharing.web.TAListener;
import com.musicsharing.web.WebServiceConstants;

public class LoginActivity extends Activity {

	private EditText editTextLoginUsername;
	private EditText editTextLoginPassword;
	private CheckBox checkBoxSignInRememberMe;
	private Button imageButtonLoginButton;
	private Button buttonLoginForgotPassword;
	private Button buttonLoginSignUp;
	private Spinner spinner;
	private List<Country> countries;
	String mobileNumber = null;

	private static final int RC_SIGN_IN = 0;
	private boolean mIntentInProgress;
	private boolean mSignInClicked;
	private static final String TAG = "LoginActivity";

	private ProgressDialog dialog;

	private static final String LOGGEDIN = "loggedin";

	private boolean isFbRequestProgress;
	protected Activity activity;
	protected Gson gson;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;

		String user = SharedPreferencesUtil.getPreferences(activity,
				SharedPreferencesUtil.USER, null);
		if (user != null) {
			startActivity(new Intent(activity, DashboardActivity.class));
			finish();
			return;
		}
		gson = new Gson();
		/*getActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.color.my_account));*/
		getActionBar().setTitle("Login");
		setContentView(R.layout.activity_login);

		bindContents();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case android.R.id.home:
			this.onBackPressed();

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void bindContents() {

		editTextLoginUsername = (EditText) findViewById(R.id.editTextLoginUsername);
		editTextLoginPassword = (EditText) findViewById(R.id.editTextLoginPassword);
		checkBoxSignInRememberMe = (CheckBox) findViewById(R.id.checkBoxSignInRememberMe);
		imageButtonLoginButton = (Button) findViewById(R.id.imageButtonLoginButton);
		buttonLoginForgotPassword = (Button) findViewById(R.id.buttonLoginForgotPassword);
		buttonLoginSignUp = (Button) findViewById(R.id.buttonLoginSignUp);
		Type type = new TypeToken<List<Country>>() {
		}.getType();
		countries = new Gson().fromJson(CountryJsonConstant.countryJson, type);
		spinner = (Spinner) findViewById(R.id.countryCode);
		spinner.setAdapter(new ArrayAdapter<Country>(activity,
				android.R.layout.simple_spinner_dropdown_item, countries));

		String userName = SharedPreferencesUtil.getPreferences(activity,
				SharedPreferencesUtil.USER_NAME, "");
		String password = SharedPreferencesUtil.getPreferences(activity,
				SharedPreferencesUtil.PASSWORD, "");

		if (!TextUtils.isEmpty(userName)) {
			editTextLoginUsername.setText(userName);
		}

		if (!TextUtils.isEmpty(password)) {
			editTextLoginPassword.setText(password);
			checkBoxSignInRememberMe.setChecked(true);
		}

		imageButtonLoginButton.setOnClickListener(new View.OnClickListener() {
			private String username;
			private String password;

			@Override
			public void onClick(View v) {

				username = editTextLoginUsername.getText().toString();
				password = editTextLoginPassword.getText().toString();

				if (checkBoxSignInRememberMe.isChecked()) {
					SharedPreferencesUtil.savePreferences(activity,
							SharedPreferencesUtil.USER_NAME, username);
					SharedPreferencesUtil.savePreferences(activity,
							SharedPreferencesUtil.PASSWORD, password);

				}

				if ((username == null || TextUtils.isEmpty(username))
						&& (password == null || TextUtils.isEmpty(password))) {
					NotificationUtils.showNotificationToast(activity,
							"Please fill in your Username and Password");
					return;
				} else {
					if (username == null || TextUtils.isEmpty(username)) {
						NotificationUtils.showNotificationToast(activity,
								"Please fill in your Username");
						return;
					}

					if (password == null || TextUtils.isEmpty(password)) {
						NotificationUtils.showNotificationToast(activity,
								"Please fill in your Password");
						return;
					}
				}
				if (!Utils.isNetworkAvailable(activity)) {
					return;
				}
				Country country = (Country) spinner.getSelectedItem();
				final String callingCode = country.getDial_code().substring(1);
				JSONObject jsonObject = new JSONObject();
				try {
					jsonObject.put("mobileNumber", callingCode + username);
					jsonObject.put("password", password);
					jsonObject.put("loginSource", "direct");
				} catch (JSONException e) {
					e.printStackTrace();
					NotificationUtils.showNotificationToast(activity,
							"Please re-enter Username and Password");
				} catch (NullPointerException e) {
					e.printStackTrace();
					NotificationUtils.showNotificationToast(activity,
							"Please re-enter Username and Password");
				}

				TAListener taListener = new TAListener() {

					@Override
					public void onTaskFailed(String errorMessage) {
						NotificationUtils.showNotificationToast(activity,
								"Server not responds");
					}

					@Override
					public void onTaskCompleted(String result) {

						Login login = gson.fromJson(result, Login.class);

						if (null != login) {
							if (login.getStatusCode().equalsIgnoreCase(
									"SUCCESS_003")) {
								User user = new User();
                                user.setUserId(login.getUserId());
								user.setName(login.getUserName());
								
								String userString = gson.toJson(user);
								SharedPreferencesUtil.savePreferences(activity,
										SharedPreferencesUtil.USER, userString);
								startActivity(new Intent(activity,
										DashboardActivity.class));
								finish();
							}
							NotificationUtils.showNotificationToast(activity,
									login.getMessage());

						} else {
							NotificationUtils.showNotificationToast(activity,
									"Server not responds");
						}

					}
				};

				DashboardActivity.registorOrAuthenticate(activity,
						jsonObject.toString(), taListener,
						WebServiceConstants.AUTHENTICATE);

			}
		});

		buttonLoginForgotPassword
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						 startActivity(new Intent(activity,
						 ForgetPasswordActivity.class));
					}
				});

		buttonLoginSignUp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				startActivity(new Intent(activity, VerificationActivity.class));

			}
		});

	}

}
