package com.brunel.team28.maraudersmap;

import java.io.InputStream;

/**
 * A class to hold some static constants
 */

public class Constants {
	public static class values {
		public static String Username;
		public static String Password;
		public static InputStream is = null;
		public static String result = null;
		public static String line = null;
		public static int code;
		public static String UserID;
		public static String Fname;
		public static String Lname;
		public static String FriendName;
		public static String FriendFname;
		public static String FriendLname;
		public static String FriendID;
		public static String[] FriendIDArrayvalues;
		public static String[] FriendUsernameArray;
		public static String ValueHolder;
		public static String UserStatus;
		public static String[][] geofenceArray;
		public static String rawGeofenceData;
		public static String currentLocationName;   //stores current location name
		public static String BuldingID;
		public static String FriendBuldingID;
		public static String FriendcurrentLocation;



	}

	public static class Geometry {
		public static double MinLatitude = -90.0;
		public static double MaxLatitude = 90.0;
		public static double MinLongitude = -180.0;
		public static double MaxLongitude = 180.0;
		public static double MinRadius = 0.01;                   // kilometers
		public static double MaxRadius = 20.0;                   // kilometers
		public static InputStream inputStream = null;
		public static String geofenceDatabaseRawOutput = null;
	}

	public static class SharedPrefs {
		public static String Geofences = "SHARED_PREFS_GEOFENCES";
	}
}
