package com.yamba;

import java.util.List;


import winterwell.jtwitter.Twitter.Status;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class UpdaterService extends Service {

	private static final String TAG = UpdaterService.class.getSimpleName();
	private Updater updater;
	YambaApplication yamba;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public synchronized void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		yamba = (YambaApplication)getApplication();
		updater = new Updater();

		Log.d(TAG, "onCreated");
	}

	@Override
	public synchronized void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		if (!yamba.isServiceRunning) {
			yamba.isServiceRunning = true;
			updater.start();
		}
		Log.d(TAG, "OnStart");
	}

	@Override
	public synchronized void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d(TAG, "onDestroyed");

		if (yamba.isServiceRunning) {
			yamba.isServiceRunning = false;
			updater.interrupt();
		}

		updater = null;

	}

	class Updater extends Thread {

		static final long DELAY = 3000;

		public Updater() {
			super("Updater");
		}

		@Override
		public void run() {
			boolean hasNewStatuses = false;
			while (yamba.isServiceRunning) {
				try {
					Log.d(TAG, "Updater running");

					// OPen Database
					

					// Get friends status

					List<Status> statuses = yamba.getTwitter().getFriendsTimeline();
					ContentValues values = new ContentValues();
					for (Status status : statuses) {
						values.put(StatusData.C_ID, status.id);
						values.put(StatusData.C_CREATED_AT,
								status.createdAt.getTime());
						values.put(StatusData.C_USER, status.user.getName());
						values.put(StatusData.C_TEXT, status.text);

						// Insert data
						long ret = yamba.statusData.insert(values);	
						if(ret >= 0){
							hasNewStatuses = true;
						}

						// INSERT INTO statuses(_id, yamba_createdAt,
						// yamba_user, yamba_text)
						// VALUES(234324324324, 123123213123, 'quynh', 'Hellow')

						Log.d(TAG, String.format("%s : %s", status.user.name,
								status.text));
					}
					
					//Check if there are new statuses
					if(hasNewStatuses){
						sendBroadcast(new Intent("Yamba.NewStatus"));
					}
					
					
					// SLEEP
					Thread.sleep(DELAY);
				} catch (InterruptedException e) {
					// Interrupted
					yamba.isServiceRunning = false;
					e.printStackTrace();

				}
			}// END WHILE
		}
	}

}
