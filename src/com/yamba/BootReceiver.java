package com.yamba;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Grabbing data on boot
 * @author Quynh
 *
 */
public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// Notify updater service of changes
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		
		if (prefs.getBoolean("StartAtBoot", false)) {
			//Start UpdaterService
			context.startService(new Intent(context, UpdaterService.class));
		}
		Log.d("BootReceiver", "onReceived");
	}

}
