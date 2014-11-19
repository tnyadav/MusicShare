package com.musicsharing.account;

import android.app.Activity;

import com.google.gson.Gson;
import com.musicsharing.utils.SharedPreferencesUtil;

public class UserUtil {

	public static String getUserId(Activity activity) {
		String user = SharedPreferencesUtil.getPreferences(activity, SharedPreferencesUtil.USER, null);
		if (user!=null) {
			User user2=new Gson().fromJson(user, User.class);
			return user2.getUserId();
		}else {
			return null;
		}
	
	}
	public static void logout(Activity activity) {
		SharedPreferencesUtil.deletePreferences(activity, SharedPreferencesUtil.USER);
	}
	public static User getUser(Activity activity) {
		String user = SharedPreferencesUtil.getPreferences(activity, SharedPreferencesUtil.USER, null);
		if (user!=null) {
			User user2=new Gson().fromJson(user, User.class);
			return user2;
		}else {
			return null;
		}
	
	}
}
