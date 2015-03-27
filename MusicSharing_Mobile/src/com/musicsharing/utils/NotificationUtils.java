package com.musicsharing.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.w3axis.sharehub.R;

/**
 * Utility class to display in-app notifications.
 * 
 * @author Atul Mittal
 * 
 */
public class NotificationUtils {

	// Only one object for progress dialog will be required in complete
	// application.
	private static ProgressDialog mProgressDialog;

	/**
	 * This method shows the application toast messages
	 * 
	 * @param argContext
	 *            Context of the active activity instance.
	 * @param argDisplayText
	 *            Text to be displayed in the notification.
	 */
	public static void showNotificationToast(Context argContext,
			String argDisplayText) {
		try {
			Toast.makeText(argContext, argDisplayText, Toast.LENGTH_LONG)
					.show();
		} catch (NullPointerException e) {
			// TODO: handle exception
		}
	}
	public static void showNetworkStatusToastAndReturn(Activity argContext) {
		
			try {
				Toast.makeText(argContext, "No network connection", Toast.LENGTH_LONG)
						.show();
			} catch (NullPointerException e) {
				// TODO: handle exception
			}
			return;
		
		
	}
	public static void showNetworkStatusToast(Activity argContext) {
		try {
			Toast.makeText(argContext, "No network connection", Toast.LENGTH_LONG)
					.show();
		} catch (NullPointerException e) {
			// TODO: handle exception
		}
		
	}
	public static void showNotificationDialog(final Context argContext,
			String argDisplayText) {
		 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				 argContext);
	 	alertDialogBuilder.setTitle(R.string.app_name);
	 	alertDialogBuilder
					.setMessage(argDisplayText)
					.setCancelable(false)
					.setPositiveButton("OK",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							dialog.cancel();
							((Activity) argContext).finish();
						}
					  });
					
					AlertDialog alertDialog = alertDialogBuilder.create();
	                alertDialog.show();
	}

	/**
	 * Shows an indeterminate progress dialog.
	 * 
	 * @param argContext
	 *            Context of the active activity instance.
	 * @param argTitle
	 *            Title of the dialog box.
	 * @param argMessage
	 *            Message to be displayed in the dialog box.
	 */
	public static void showProgressDialog(Context argContext, String argTitle,
			String argMessage) {

		dismissProgressDialog();

		if (argContext != null)
			mProgressDialog = ProgressDialog.show(argContext, null, ""
					+ argMessage, true, false);
	}

	/**
	 * Dismisses open (if any) progress dialog box.
	 */
	public static void dismissProgressDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
	}

}
