package org.rhok.bribealert.provider;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class LocationProvider {

	private static String tag = "BribeAlert LocationProvider";

	public static Location getLocation(Context context) {
		String bestProvider;

		LocationManager lm = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);

		Criteria criteria = new Criteria();
		bestProvider = lm.getBestProvider(criteria, false);
		Location location = lm.getLastKnownLocation(bestProvider);
		Log.d(tag, "LocationObject: " + location);
		return location;
	}
}
