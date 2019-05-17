package com.brunel.team28.maraudersmap.Geofences;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.brunel.team28.maraudersmap.R;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * ActionBarActivity that displays a single fragment
 */

public class AllGeofencesActivity extends ActionBarActivity {
	private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;

	// Region Overrides
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_geofences);

		setTitle(R.string.app_title);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new AllGeofencesFragment())
					.commit();
		}

		// Initialise GeofenceController when the app starts
		GeofenceController.getInstance().init(this);
	}

	@Override
	protected void onResume() {
		super.onResume();

		int googlePlayServicesCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		Log.i(AllGeofencesActivity.class.getSimpleName(), "googlePlayServicesCode = " + googlePlayServicesCode);

		if (googlePlayServicesCode == 1 || googlePlayServicesCode == 2 || googlePlayServicesCode == 3) {
			GooglePlayServicesUtil.getErrorDialog(googlePlayServicesCode, this, 0).show();
		}
	}

	// Shows a delete menu item if there are existing geofences
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_all_geofences, menu);

		MenuItem item = menu.findItem(R.id.action_delete_all);

		if (GeofenceController.getInstance().getNamedGeofences().size() == 0) {
			item.setVisible(false);
		}

		return true;
	}
}
