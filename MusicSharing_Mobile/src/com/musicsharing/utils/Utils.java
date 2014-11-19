package com.musicsharing.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Html;
import android.util.DisplayMetrics;
import android.widget.LinearLayout.LayoutParams;

public class Utils {



	/**
	 * Utility method to check Internet connection availability
	 * 
	 * @return boolean value indicating the presence of Internet connection
	 *         availability
	 */
	public static boolean isNetworkAvailable(Activity argActivity) {
		if (argActivity == null) {
			return false;
		}

		ConnectivityManager connectivityManager;
		NetworkInfo activeNetworkInfo = null;
		try {
			connectivityManager = (ConnectivityManager) argActivity
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return activeNetworkInfo != null;
	}

	/**
	 * Validates an email based on regex -
	 * "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
	 * "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	 * 
	 * @param email
	 *            String containing email address
	 * @return True if the email is valid; false otherwise.
	 */
	public static boolean isEmailValid(String email) {
		boolean isValid = false;
		try {
			// Initialize reg ex for email.
			String expression = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
					+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
			CharSequence inputStr = email;
			// Make the comparison case-insensitive.
			Pattern pattern = Pattern.compile(expression,
					Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(inputStr);
			if (matcher.matches()) {
				isValid = true;
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		return isValid;
	}

	/**
	 * Show device native message composer application.
	 * 
	 * @param argContext
	 * @param argEmail
	 *            email address. Currently only support one
	 * @param argSubject
	 *            email subject
	 * @param argBody
	 *            email body
	 */
	public static void showEmailComposer(Context argContext, String argEmail,
			String argSubject, String argBody) {
		try {
			Intent emailIntent = new Intent(Intent.ACTION_SEND);
			emailIntent.setType("plain/text");
			emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
					new String[] { argEmail });
			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
					argSubject);
			emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, argBody);
			argContext
					.startActivity(Intent.createChooser(emailIntent, "Share via"));
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

	}
	public static int getScreenWidth(Context context) {
		 Resources res = context.getResources();
		 DisplayMetrics metrics = res.getDisplayMetrics();
		int displayWidth = res.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? Math
				.max(metrics.widthPixels, metrics.heightPixels) : Math.min(
				metrics.widthPixels, metrics.heightPixels);
		return displayWidth;
		
	}
	public static int convertDensityPixelToPixel(Context context, int i) {
		return (int) ((i * context.getResources().getDisplayMetrics().density) + 0.5);
	}
	
	public static LayoutParams getImageLayoutParam(Activity activity) {
		int width=Utils.getScreenWidth(activity);
		int margin=Utils.convertDensityPixelToPixel(activity, 50);
		int bookWidth = (width-margin)/3;
		int bookHeight = (int) (bookWidth*1.4);
		LayoutParams layoutParams=new LayoutParams(bookWidth, bookHeight);
		return layoutParams;
	}
	
	public static File getPdfFile(Activity mActivity,String filename) {
		File cacheDir = mActivity.getExternalCacheDir();
        File pdfFile = new File(cacheDir, filename+".pdf");
		return pdfFile;
        
	}
	public static File getCuverImage(Activity mActivity,String filename) {
		File cacheDir = mActivity.getExternalCacheDir();
        File cuverImage = new File(cacheDir, filename+".png");
		return cuverImage;
	}
	
	public static String getDecodedString(String text) {
		String name = "";
		try {
		    name = new String(text.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {

		    e.printStackTrace();
		}

		String decodedName = Html.fromHtml(name).toString();
		return decodedName;
		
	}
	public static String getDate(String date) {
		String resultDate = "Date not found";

		SimpleDateFormat dateFormatter = new SimpleDateFormat(
				"yyyy-MM-dd hh:mm:ss");
		dateFormatter.setTimeZone(TimeZone.getTimeZone("UST"));
		try {
			Date newDate = dateFormatter.parse(date);
			SimpleDateFormat dateFormatterFinal = new SimpleDateFormat(
					"E, d MMM h:m a");

			resultDate = dateFormatterFinal.format(newDate);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return resultDate;

	}
	public static String getRemovedExtensionName(String name){
	    String baseName;
	    if(name.lastIndexOf(".")==-1){
	      baseName=name;
	    
	    }else{
	      int index=name.lastIndexOf(".");
	      baseName=name.substring(0,index);
	    }
	    return baseName;
	  }


		  public static boolean isJSONValid(String JSON_STRING) {
		      try {
		          new Gson().fromJson(JSON_STRING, Object.class);
		          return true;
		      } catch(com.google.gson.JsonSyntaxException ex) { 
		          return false;
		      }
		  
		}
}
