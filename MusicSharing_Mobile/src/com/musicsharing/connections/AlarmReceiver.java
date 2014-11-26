package com.musicsharing.connections;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String DEBUG_TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
      //  Log.e(DEBUG_TAG, "Recurring alarm; requesting location tracking.");
        // start the service
        Intent tracking = new Intent(context, UpdateMyStatus.class);
        context.startService(tracking);
    }
}