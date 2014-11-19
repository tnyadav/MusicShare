package com.musicsharing.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Utility preference storage class to facilitate offline storage
 * 
 * @author Atul Mittal
 * 
 */
public class PreferenceStorage {
	private SharedPreferences mAppSharedPrefs;
	private Editor mPrefsEditor;
	private static String SHARED_PREF_NAME;

	public static final String PREF_LOGIN_MAIL_KEY = "FP_PREF_LOGIN_MAIL_KEY";

	/**
	 * Initialize the PreferenceStorage
	 * 
	 * @param argContext
	 */
	public PreferenceStorage(Context argContext) {
		try {
			SHARED_PREF_NAME = argContext.getApplicationInfo().packageName
					.toString();
			this.mAppSharedPrefs = argContext.getSharedPreferences(
					SHARED_PREF_NAME, Activity.MODE_PRIVATE);
			this.mPrefsEditor = mAppSharedPrefs.edit();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Synchronized method to get data from shared preferences of the
	 * application
	 * 
	 * @param argKey
	 *            Key to be queried in shared preferences
	 * @param argDefault
	 *            the default value is key is absent
	 * @return
	 */
	synchronized public String getData(String argKey, String argDefault) {
		String result = null;
		result = mAppSharedPrefs.getString(argKey, argDefault);
		return result;
	}

	/**
	 * Synchronized method to get data from shared preferences of the
	 * application
	 * 
	 * @param argKey
	 *            Key to be queried in shared preferences
	 * @param argDefault
	 *            the default value is key is absent
	 * @return
	 */
	synchronized public int getData(String argKey, int argDefault) {
		int result = 0;
		result = mAppSharedPrefs.getInt(argKey, argDefault);
		return result;
	}
	
	synchronized public boolean getData(String argKey, boolean argDefault) {
		boolean result = argDefault;
		result = mAppSharedPrefs.getBoolean(argKey, argDefault);
		return result;
	}

	/**
	 * Synchronized method to save data to shared preferences of the application
	 * 
	 * @param argKey
	 *            Key with which the data is to be stored
	 * @param argValue
	 *            Data
	 */
	synchronized public void saveData(String argKey, String argValue) {
		try {
			mPrefsEditor.putString(argKey, argValue);
			mPrefsEditor.commit();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Synchronized method to save data to shared preferences of the application
	 * 
	 * @param argKey
	 *            Key with which the data is to be stored
	 * @param argValue
	 *            Data
	 */
	synchronized public void saveData(String argKey, int argValue) {
		try {
			mPrefsEditor.putInt(argKey, argValue);
			mPrefsEditor.commit();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
	
	synchronized public void saveData(String argKey, boolean argValue) {
		try {
			mPrefsEditor.putBoolean(argKey, argValue);
			mPrefsEditor.commit();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Synchronized method to remove data from shared preferences of the
	 * application
	 * 
	 * @param argKey
	 *            Key with which the data is to be removed
	 * @return
	 */
	synchronized public boolean removeData(String argKey) {
		mPrefsEditor.remove(argKey);
		mPrefsEditor.commit();
		return true;
	}
}
