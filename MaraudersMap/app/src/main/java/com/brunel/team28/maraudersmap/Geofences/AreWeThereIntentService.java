package com.brunel.team28.maraudersmap.Geofences;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.brunel.team28.maraudersmap.Constants;
import com.brunel.team28.maraudersmap.MainActivity;
import com.brunel.team28.maraudersmap.R;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.brunel.team28.maraudersmap.Constants.values.currentLocationName;

/**
 * An IntentService that will send a notification to the user when they enter a geofence
 */

public class AreWeThereIntentService extends IntentService {

	// Region Properties
	private final String TAG = AreWeThereIntentService.class.getName();

	private SharedPreferences prefs;
	private Gson gson;

	// Region Constructors
	public AreWeThereIntentService() {
		super("AreWeThereIntentService");
	}

	// Region Overrides
	@Override
	protected void onHandleIntent(Intent intent) {
		prefs = getApplicationContext().getSharedPreferences(
				Constants.SharedPrefs.Geofences, Context.MODE_PRIVATE);
		gson = new Gson();

		// Get the event
		GeofencingEvent event = GeofencingEvent.fromIntent(intent);
		if (event != null) {
			if (event.hasError()) {
				onError(event.getErrorCode());
			} else {
				// Get the transition type
				int transition = event.getGeofenceTransition();
				if (transition == Geofence.GEOFENCE_TRANSITION_ENTER ||
						transition == Geofence.GEOFENCE_TRANSITION_DWELL ||
						transition == Geofence.GEOFENCE_TRANSITION_EXIT) {
					List<String> geofenceIds = new ArrayList<>();

					// Accumulate a list of event geofences
					for (Geofence geofence : event.getTriggeringGeofences()) {
						geofenceIds.add(geofence.getRequestId());
					}

					if (transition == Geofence.GEOFENCE_TRANSITION_ENTER ||
							transition == Geofence.GEOFENCE_TRANSITION_DWELL) {
						// Pass the geofence list to the notification method
						onEnteredGeofences(geofenceIds);
					}
				}
			}
		}
	}

	// This shows the notification
	// Region Private
	private void onEnteredGeofences(List<String> geofenceIds) {
		// Outer loop over all geofenceIds
		for (String geofenceId : geofenceIds) {
			String geofenceName = "";

			// Loop over all geofence keys in prefs and retrieve NamedGeofence from SharedPreferences
			Map<String, ?> keys = prefs.getAll();
			for (Map.Entry<String, ?> entry : keys.entrySet()) {
				String jsonString = prefs.getString(entry.getKey(), null);
				NamedGeofence namedGeofence = gson.fromJson(jsonString, NamedGeofence.class);
				if (namedGeofence.id.equals(geofenceId)) {
					geofenceName = namedGeofence.name;
					currentLocationName = namedGeofence.name;
					LocationIDFetch.UserLocationID();
					LocationIDPost.UserLocationIDPost();
					break;
				}
			}

			// Set the notification text and send the notification
			String contextText =
					String.format(this.getResources().getString(R.string.Notification_Text), geofenceName);

			// Create a NotificationManager
			NotificationManager notificationManager =
					(NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

			// Create a PendingIntent for MainActivity
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			PendingIntent pendingNotificationIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

			// Create and send a notification
//			Notification notification = new NotificationCompat.Builder(this)
//					  .setSmallIcon(R.mipmap.ic_launcher)
//					  .setContentTitle(this.getResources().getString(R.string.Notification_Title))
//					  .setContentText(contextText)
//					  .setContentIntent(pendingNotificationIntent)
//					  .setStyle(new NotificationCompat.BigTextStyle().bigText(contextText))
//					  .setPriority(NotificationCompat.PRIORITY_HIGH)
//					  .setAutoCancel(true)
//					  .build();
//			notificationManager.notify(0, notification);
		}
	}

	private void onError(int i) {
		Log.e(TAG, "Geofencing Error: " + i);
	}
}

