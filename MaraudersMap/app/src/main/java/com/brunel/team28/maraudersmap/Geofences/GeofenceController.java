package com.brunel.team28.maraudersmap.Geofences;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.brunel.team28.maraudersmap.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Singleton class used to interact with the GoogleApiClient.
 */

public class GeofenceController {
	private final String TAG = GeofenceController.class.getName();

	private Context context;
	public static GoogleApiClient googleApiClient;
	private Gson gson;
	private SharedPreferences prefs;

	// Decleare the add geofence and connection failed callbacks
	private GeofenceControllerListener listener;
	private GoogleApiClient.ConnectionCallbacks connectionAddListener =
			new GoogleApiClient.ConnectionCallbacks() {
				@Override
				public void onConnected(Bundle bundle) {
					// Create an IntentService PendingIntent
					Intent intent = new Intent(context, AreWeThereIntentService.class);
					PendingIntent pendingIntent =
							PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

					// Associate the service PendingIntent with the geofence and call addGeofences
					if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
						// TODO: Consider calling
						//    ActivityCompat#requestPermissions
						// here to request the missing permissions, and then overriding
						//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
						//                                          int[] grantResults)
						// to handle the case where the user grants the permission. See the documentation
						// for ActivityCompat#requestPermissions for more details.
						return;
					}
					PendingResult<Status> result = LocationServices.GeofencingApi.addGeofences(
							googleApiClient, getAddGeofencingRequest(), pendingIntent);

					// Implement PendingResult callback
					result.setResultCallback(new ResultCallback<Status>() {

						@Override
						public void onResult(Status status) {
							if (status.isSuccess()) {
								// If successful, save the geofence
								saveGeofence();
							} else {
								// If not successful, log and send an error
								Log.e(TAG, "Registering geofence failed: " + status.getStatusMessage() +
										" : " + status.getStatusCode());
								sendError();
							}
						}
					});
				}

				@Override
				public void onConnectionSuspended(int i) {

				}
			};

	private GoogleApiClient.OnConnectionFailedListener connectionFailedListener =
			new GoogleApiClient.OnConnectionFailedListener() {
				@Override
				public void onConnectionFailed(ConnectionResult connectionResult) {

				}
			};

	// Stores current geofences in memory
	private List<NamedGeofence> namedGeofences;

	public List<NamedGeofence> getNamedGeofences() {
		return namedGeofences;
	}

	// Maintains list of geofences to remove
	private List<NamedGeofence> namedGeofencesToRemove;

	private Geofence geofenceToAdd;
	private NamedGeofence namedGeofenceToAdd;


	/**
	 * Adds a property to hold the singleton reference to the GeofenceController class,
	 * as well as a method to create and access the instance.
	 */
	private static GeofenceController INSTANCE;

	public static GeofenceController getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new GeofenceController();
		}
		return INSTANCE;
	}

	// Method for initialising the context and some proprties of the controller
	public void init(Context context) {
		this.context = context.getApplicationContext();

		gson = new Gson();
		namedGeofences = new ArrayList<>();
		namedGeofencesToRemove = new ArrayList<>();
		prefs = this.context.getSharedPreferences(Constants.SharedPrefs.Geofences, Context.MODE_PRIVATE);

		loadGeofences();
	}

	// Calls the first interface method when new geofences are added or removed
	// Calls the second interface method if an error occurs
	public static interface GeofenceControllerListener {
		void onGeofencesUpdated();

		void onError();
	}

	/*
	 * Helper methods for callbacks
	 */
	private GeofencingRequest getAddGeofencingRequest() {
		List<Geofence> geofencesToAdd = new ArrayList<>();
		geofencesToAdd.add(geofenceToAdd);
		GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
		builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
		builder.addGeofences(geofencesToAdd);
		return builder.build();
	}

	private void connectWithCallbacks(GoogleApiClient.ConnectionCallbacks callbacks) {
		googleApiClient = new GoogleApiClient.Builder(context)
				.addApi(LocationServices.API)
				.addConnectionCallbacks(callbacks)
				.addOnConnectionFailedListener(connectionFailedListener)
				.build();
		googleApiClient.connect();
	}

	// Passess along the error
	private void sendError() {
		if (listener != null) {
			listener.onError();
		}
	}

	// Adds a new geofence to the controller's list of geofences and calls the listener's method
	private void saveGeofence() {
		namedGeofences.add(namedGeofenceToAdd);
		if (listener != null) {
			listener.onGeofencesUpdated();
		}

		// Using GSON to convert namedGeofenceToAdd into JSON and store it in shared preferences
		String json = gson.toJson(namedGeofenceToAdd);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(namedGeofenceToAdd.id, json);
		editor.apply();
	}

	// Starts the process of adding a geofence
	public void addGeofence(NamedGeofence namedGeofence, GeofenceControllerListener listener) {
		this.namedGeofenceToAdd = namedGeofence;
		this.geofenceToAdd = namedGeofence.geofence();
		this.listener = listener;

		connectWithCallbacks(connectionAddListener);
	}

	/*
	First, you create a map for all the geofence keys.
	You then loop over all the keys and use Gson to convert the saved JSON back into a NamedGeofence.
	Finally, you sort the geofences by name.
	 */
	private void loadGeofences() {
		// Loop over all geofence keys in prefs and add to namedGeofences
		Map<String, ?> keys = prefs.getAll();
		for (Map.Entry<String, ?> entry : keys.entrySet()) {
			String jsonString = prefs.getString(entry.getKey(), null);
			NamedGeofence namedGeofence = gson.fromJson(jsonString, NamedGeofence.class);
			namedGeofences.add(namedGeofence);
		}

		// Sort namedGeofences by name
		Collections.sort(namedGeofences);
	}

	private GoogleApiClient.ConnectionCallbacks connectionRemoveListener =
			new GoogleApiClient.ConnectionCallbacks() {
				@Override
				public void onConnected(Bundle bundle) {
					// Create a list of geofences to remove
					List<String> removeIds = new ArrayList<>();
					for (NamedGeofence namedGeofence : namedGeofencesToRemove) {
						removeIds.add(namedGeofence.id);
					}

					if (removeIds.size() > 0) {
						// Use GoogleApiClient and the GeofencingApi to remove the geofences
						PendingResult<Status> result = LocationServices.GeofencingApi.removeGeofences(
								googleApiClient, removeIds);
						result.setResultCallback(new ResultCallback<Status>() {

							// Handle the success or failure of the PendingResult
							@Override
							public void onResult(Status status) {
								if (status.isSuccess()) {
									removeSavedGeofences();
								} else {
									Log.e(TAG, "Removing geofence failed: " + status.getStatusMessage());
									sendError();
								}
							}
						});
					}
				}

				@Override
				public void onConnectionSuspended(int i) {
					Log.e(TAG, "Connecting to GoogleApiClient suspended.");
					sendError();
				}
			};

	public void removeGeofences(List<NamedGeofence> namedGeofencesToRemove,
	                            GeofenceControllerListener listener) {
		this.namedGeofencesToRemove = namedGeofencesToRemove;
		this.listener = listener;

		connectWithCallbacks(connectionRemoveListener);
	}

	public void removeAllGeofences(GeofenceControllerListener listener) {
		namedGeofencesToRemove = new ArrayList<>();
		for (NamedGeofence namedGeofence : namedGeofences) {
			namedGeofencesToRemove.add(namedGeofence);
		}
		this.listener = listener;

		connectWithCallbacks(connectionRemoveListener);
	}

	// Removes geofences from the users’ shared preferences
	// then alerts the listener that geofences have been updated
	private void removeSavedGeofences() {
		SharedPreferences.Editor editor = prefs.edit();

		for (NamedGeofence namedGeofence : namedGeofencesToRemove) {
			int index = namedGeofences.indexOf(namedGeofence);
			editor.remove(namedGeofence.id);
			namedGeofences.remove(index);
			editor.apply();
		}

		if (listener != null) {
			listener.onGeofencesUpdated();
		}
	}
}




