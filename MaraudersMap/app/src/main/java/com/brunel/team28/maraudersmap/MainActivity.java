package com.brunel.team28.maraudersmap;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.brunel.team28.maraudersmap.FriendScreen.FriendList;
import com.brunel.team28.maraudersmap.Geofences.GeofenceController;
import com.brunel.team28.maraudersmap.Geofences.NamedGeofence;
import com.brunel.team28.maraudersmap.Login.Login;
import com.brunel.team28.maraudersmap.Screens.AddContacts;
import com.brunel.team28.maraudersmap.Screens.MyProfile;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static com.brunel.team28.maraudersmap.Constants.Geometry.geofenceDatabaseRawOutput;
import static com.brunel.team28.maraudersmap.Constants.Geometry.inputStream;
import static com.brunel.team28.maraudersmap.Constants.values.geofenceArray;
import static com.brunel.team28.maraudersmap.Constants.values.line;
import static com.brunel.team28.maraudersmap.Constants.values.rawGeofenceData;
import static com.brunel.team28.maraudersmap.Geofences.AllGeofencesFragment.geofenceControllerListener;

/**
 * ActionBarActivity that displays a single fragment
 */

public class MainActivity extends AppCompatActivity {
	private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;

	// Region Overrides
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_all_geofences);
		rawGeofenceData = DownloadRawGeofenceData();
		geofenceArray = ParseGeofenceData();
		setTitle(R.string.app_title);

		// Assume this Activity is the current activity
		int permissionCheck = ContextCompat.checkSelfPermission(this,
				Manifest.permission.ACCESS_FINE_LOCATION);

		if (ContextCompat.checkSelfPermission(this,
				Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this,
					new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
					MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
		}

		// Initialise GeofenceController when the app starts
		GeofenceController.getInstance().init(this);
		CreateGeofences(geofenceArray);
		loginstart(null);
	}

	@Override
	protected void onResume() {
		super.onResume();

		int googlePlayServicesCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		Log.i(MainActivity.class.getSimpleName(), "googlePlayServicesCode = " + googlePlayServicesCode);

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

	public static String DownloadRawGeofenceData() {

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://192.168.166.18:5306/GeofenceFetcher.php");
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			inputStream = entity.getContent();
			Log.e("pass 1", "connection success ");
		} catch (Exception e) {
			Log.e("Fail 1", e.toString());
		}

		try {
			BufferedReader reader = new BufferedReader
					(new InputStreamReader(inputStream, "UTF-8"), 8);
			StringBuilder sb = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			inputStream.close();
			geofenceDatabaseRawOutput = sb.toString();   // Stores the super string of all geofence to be processed
			Log.e("pass 2", "connection success ");
		} catch (Exception e) {
			Log.e("Fail 2", e.toString());
		}
		return geofenceDatabaseRawOutput;
	}

	public static String[][] ParseGeofenceData() {
		if (rawGeofenceData != null && !rawGeofenceData.isEmpty()) {
			String outerdelim = "/";
			String innerdelim = ",";

			// outerdelim may be a group of characters
			String[] sOuter = rawGeofenceData.split("[" + outerdelim + "]");
			// one dimension of the array has to be known on declaration:
			String[][] result = new String[26][5];
			int count = 0;
			for (String line : sOuter) {
				result[count] = line.split(innerdelim);
				++count;
			}
			Log.d("Parse Database", "Database Parsing Successful");
			printArray(result);
			return result;
		} else {
			Log.e("Download Error", "Geofence raw data is empty.");
			return null;
		}
	}

	public static void CreateGeofences(String[][] array) {
		String cache;
		String id = null;
		String name = null;
		double longitude = 0;
		double latitude = 0;
		float radius = 0;

		for (int i = 0; i < 25; i++) {
			for (int j = 0; j < 5; j++) {
				if (j == 0) {
					id = array[i][j];
				} else if (j == 1) {
					name = array[i][j];
				} else if (j == 2) {
					cache = array[i][j];
					longitude = Double.parseDouble(cache);
				} else if (j == 3) {
					cache = array[i][j];
					latitude = Double.parseDouble(cache);
				} else if (j == 4) {
					cache = array[i][j];
					radius = Float.valueOf(cache);
				}
			}
			NewGeofence(name, longitude, latitude, radius);
		}
	}

	public static void NewGeofence(String name, double longitude, double latitude, float radius) {
		// Create a named geofence
		NamedGeofence geofence = new NamedGeofence();
		//geofence.id = id;
		geofence.name = name;
		geofence.latitude = latitude;
		geofence.longitude = longitude;
		geofence.radius = radius;

		// Force geofence updates
		GeofenceController.getInstance().addGeofence(geofence, geofenceControllerListener);


		Log.e("I've made a geofence", name + " " + longitude + " " + latitude + " " + radius);
	}

	public void onRequestPermissionResult(int requestCode, String permissions[], int[] grantResults) {
		switch (requestCode) {
			case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
				// If request is cancelled, the result arrays are empty
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					// Do permission related things you want to do
				} else {
					// Permission denied
				}
				return;
			}
		}
	}

	/**
	 * Event Listeners for Cards
	 **/
	public void loginstart(View view) {
		Intent loginIntent = new Intent(this, Login.class);
		startActivity(loginIntent);
	}

	public void myProfileCardClicked(View view) {
		Intent myProfileIntent = new Intent(this, MyProfile.class);
		startActivity(myProfileIntent);
	}

	public void addFriendsCardClicked(View view) {
		Intent addFriendsIntent = new Intent(this, AddContacts.class);
		startActivity(addFriendsIntent);
	}

	public void myFriendsCardClicked(View view) {
		Intent myFriendsIntent = new Intent(this, FriendList.class);
		startActivity(myFriendsIntent);
	}

	public static void showToast(Context context, String condiment) {
		CharSequence text = condiment;
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}

	private static void printArray(String[][] array) {
		for (int i = 0; i < array[0].length; i++) {
			for (int j = 0; j < array[1].length; j++) {
				Log.e("Array Output", array[i][j]);
			}
		}
	}
}

