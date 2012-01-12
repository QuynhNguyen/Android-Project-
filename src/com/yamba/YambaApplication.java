package com.yamba;

import winterwell.jtwitter.Twitter;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.Log;

public class YambaApplication extends Application implements OnSharedPreferenceChangeListener {

	SharedPreferences prefs;
	StatusData statusData;
	private Twitter twitter = null;
	boolean isServiceRunning = false;

	@Override
	public void onCreate() {
		super.onCreate();
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
		statusData = new StatusData(this);
	}
	
	

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
		statusData.close();
	}



	/**
	 * Lazily initializes the connection to the online service.
	 * @return Twitter object representing connection to online service.
	 */
	public synchronized Twitter getTwitter(){
		if(twitter == null){
			String username = prefs.getString("username", "");
			String password = prefs.getString("password", "");
			String server = prefs.getString("server", "");
			twitter = new Twitter(username, password);
			twitter.setAPIRootUrl(server);
			Log.d("YamboApplication", String.format("Getting twitter information %s/%s@/%s", username, password, server));
		}
		
		return twitter;
	}

	//Called when preferences change
	@Override
	public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
		//invalidate Twitter
		twitter = null;
	}
}
