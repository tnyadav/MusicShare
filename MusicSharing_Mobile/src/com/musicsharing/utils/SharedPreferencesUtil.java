package com.musicsharing.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreferencesUtil {
	public static final int MODE_PRIVATE = 0;
	public static final String USER_NAME = "username";
	public static final String PASSWORD = "password";
	public static final String USER="user";
	public static final String USER_LIBRARY="userlibrary";
	public static final String USER_CATOGERY="usercatogery";
	public static final String SAVED_HIGHILIGHT_TEXT_COLOR="savedHighilightTextColor";
	public static final String SAVED_BACKGROUND_COLOR="savedBackgroundColor";
	public static final String REG_ID = "registration_id";
	public static final String APP_VERSION = "appVersion";
	public static final String NOTIFICATION_LIST="notificationlist";

	
	//public static final String PAYMENT_STATUS
	public static void savePreferences(Context context, String key,
			String value) {
		SharedPreferences sPrefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sPrefs.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static void savePreferences(Context context, String key,
			Integer value) {
		SharedPreferences sPrefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sPrefs.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static void savePreferences(Context context, String key,
			Boolean value) {
		SharedPreferences sPrefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sPrefs.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static void deletePreferences(Context context, String key) {
		SharedPreferences sPrefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sPrefs.edit();
		editor.remove(key);
		editor.commit();
	}

	public static Boolean getPreferences(Context context, String key,boolean defValue) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		Boolean savedPref = sharedPreferences.getBoolean(key, defValue);
		return savedPref;
	}

	public static String getPreferences(Context context, String key,String defValue) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		String savedPref = sharedPreferences.getString(key, defValue);
		return savedPref;
	}

	public static Integer getPreferences(Context context, String key,int defValue) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		Integer savedPref = sharedPreferences.getInt(key, defValue);
		return savedPref;
	}
	
	public static void clearAllSharedPreferencesList(Context context) {
		SharedPreferences sPrefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor sEdit = sPrefs.edit();
		sEdit.clear();
		sEdit.commit();
	}
}
