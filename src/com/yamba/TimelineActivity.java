package com.yamba;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;

public class TimelineActivity extends BaseActivity {

	ListView listStatus;
	SimpleCursorAdapter adapter;
	Cursor c;
	TimeLineReceiver receiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timeline);
		listStatus = (ListView) findViewById(R.id.listStatus);

		refreshData();
		
		// //Ouput it
		// final int USER_COLUMN_INDEX = c.getColumnIndex(StatusData.C_USER);
		// final int TEXT_COLUMN_INDEX = c.getColumnIndex(StatusData.C_TEXT);
		//
		// String user, text, output;
		//
		// while(c.moveToNext()){
		// user = c.getString(USER_COLUMN_INDEX);
		// text = c.getString(TEXT_COLUMN_INDEX);
		// output = user + ": " + text + " \n";
		// statuses.append(output);
		// }

	}

	/**
	 * Time line receiver wake up once there is a new status
	 * 
	 */
	
	private void refreshData(){
		// Get data
				c = yamba.statusData.query();
				startManagingCursor(c);

				// Setup Adapter
				String[] from = { StatusData.C_USER, StatusData.C_TEXT,
						StatusData.C_CREATED_AT };
				int[] to = { R.id.textUser, R.id.textText, R.id.textDate };
				adapter = new SimpleCursorAdapter(this, R.layout.row, c, from, to);

				/**
				 * setting up business logic to inject adapter having it convert unix
				 * timestamp to normal time stamp i.e. 12930000 -> 10 minutes ago
				 */
				adapter.setViewBinder(new ViewBinder() {

					@Override
					public boolean setViewValue(View view, Cursor c, int columnIndex) {
						if (c.getColumnIndex(StatusData.C_CREATED_AT) != columnIndex) {
							// Not dealing with it if its not created_at column
							return false;
						} else {
							long timestamp = c.getLong(columnIndex);
							String relativeTime = (String) DateUtils
									.getRelativeTimeSpanString(timestamp);
							((TextView) view).setText(relativeTime + " ago");
							return true;
						}

					}

				});

				// Plug adapter into the plug/list
				listStatus.setAdapter(adapter);

	}

	private class TimeLineReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			refreshData();
		}

	}

	protected void onResume() {
		super.onResume();

		// Register Time Line Receiver
		if (receiver == null) {
			receiver = new TimeLineReceiver();
		}
		
		registerReceiver(receiver, new IntentFilter("Yamba.NewStatus"));
	}

	protected void onPause() {
		super.onPause();
		
		// Unregister Time Line Receiver
		unregisterReceiver(receiver);
	}

}
