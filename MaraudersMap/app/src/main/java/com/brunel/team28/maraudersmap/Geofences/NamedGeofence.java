package com.brunel.team28.maraudersmap.Geofences;

import android.support.annotation.NonNull;

import com.google.android.gms.location.Geofence;

import java.util.UUID;

/**
 * A model class that will be used to serialise the geofences.
 * This stores geofence data in primitives, but will also be used to create Geofence objects.
 */

public class NamedGeofence implements Comparable {

	// Region Properties
	public String id;
	public String name;
	public double latitude;
	public double longitude;
	public float radius;


	// Region Comparable
	@Override
	public int compareTo(@NonNull Object another) {
		NamedGeofence other = (NamedGeofence) another;
		return name.compareTo(other.name);
	}

	/**
	 * Use a builder pattern to instantiate a new geofence object.
	 * Requires a unique ID for the geofence and builds it on the values in NamedGeofence.
	 * Geofence type is specified as GEOFENCE_TRANSITION_ENTER and is set to never expire.
	 */
	public Geofence geofence() {
		id = UUID.randomUUID().toString();
		return new Geofence.Builder()
				.setRequestId(id)
				.setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
				.setCircularRegion(longitude, latitude, radius)
				.setExpirationDuration(Geofence.NEVER_EXPIRE)
				.build();
	}
}

