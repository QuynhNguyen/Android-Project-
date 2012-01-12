package com.yamba;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class BaseActivity extends Activity {

	YambaApplication yamba;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		yamba = (YambaApplication) getApplication();
	}

	// //////////Menu Stuff
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		// Open XML file call and set all the XML menu to menu object
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		switch (item.getItemId()) {
		case R.id.itemPrefs:
			startActivity(new Intent(this, PrefsActivity.class));
			break;
		case R.id.itemToggleService:
			if (yamba.isServiceRunning) {
				stopService(new Intent(this, UpdaterService.class));
				item.setTitle(R.string.startService);
			} else {
				startService(new Intent(this, UpdaterService.class));
				item.setTitle(R.string.stopService);
			}
			break;
		case R.id.itemTimeLine:
			startActivity(new Intent(this, TimelineActivity.class));
			break;
		case R.id.itemStatus:
			startActivity(new Intent(this, StatusActivity.class));
			break;
		case R.id.itemPurge:
			yamba.statusData.delete();
			Toast.makeText(this, "Database Deleted!", Toast.LENGTH_LONG).show();
			break;
		}

		return true;
	}

}
