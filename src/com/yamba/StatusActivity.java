package com.yamba;

import winterwell.jtwitter.TwitterException;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class StatusActivity extends BaseActivity {

	EditText editStatus;
	Button buttonUpdate;
	ProgressDialog postingDialog;
	static final int DIALOG_ID = 999;
	YambaApplication yamba;
	LocationManager locationManager;
	Location location;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.status);

		editStatus = (EditText) findViewById(R.id.editStatus);
		buttonUpdate = (Button) findViewById(R.id.buttonUpdate);

		// get Location manager
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		buttonUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String status = editStatus.getText().toString();

				// show diaglog
				showDialog(DIALOG_ID);

				new PostToTwitter().execute(status);

			}

		});
	}

	// DIALOG STUFF

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				60000, 1000, new LocationListener() {

					@Override
					public void onLocationChanged(Location arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onProviderDisabled(String arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onProviderEnabled(String arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onStatusChanged(String arg0, int arg1,
							Bundle arg2) {
						// TODO Auto-generated method stub

					}

				});
	}

	@Override
	protected Dialog onCreateDialog(int id) {

		switch (id) {
		case DIALOG_ID: {
			ProgressDialog dialog = new ProgressDialog(this);
			dialog.setTitle("Posting to Twitter");
			dialog.setMessage("Please wait...");
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			return dialog;
		}
		}

		return null;

	}

	// Make sure look over TraceView to see how long the progress are and fix it
	// Using AsyncTask
	private class PostToTwitter extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... status) {
			String result;

			try {
				Log.d("Edit Status", "On Click with status " + status);
				((YambaApplication) StatusActivity.this.getApplication())
						.getTwitter().setStatus(status[0]);
				result = StatusActivity.this
						.getString(R.string.msgPostedSuccesfully);
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				result = StatusActivity.this
						.getString(R.string.msgPostedFailed);
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				result = "must be less than 140 characters";
			}

			return result;
		}

		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);
			// In this case, our base context is Status Activity because its the
			// main UI
			// As oppose to being in private class
			dismissDialog(DIALOG_ID);
			editStatus.setText("");
			Toast.makeText(StatusActivity.this, result, Toast.LENGTH_LONG)
					.show();

		}

	}

}