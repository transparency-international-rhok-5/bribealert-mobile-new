package org.rhok.bribealert.provider;

import java.util.List;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

public class LocationProvider {
	
	private static String tag = "BribeAlert LocationProvider";


	public static double[] getLocation(Context context){
		Geocoder geocoder;
        String bestProvider;
        List<Address> user = null;
        double[] coordinates = new double[2];

       LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        bestProvider = lm.getBestProvider(criteria, false);
        Location location = lm.getLastKnownLocation(bestProvider);

        if (location == null){
            Toast.makeText(context,"Location Not found",Toast.LENGTH_LONG).show();
         }else{
           geocoder = new Geocoder(context);
           try {
               user = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
           coordinates[0]=user.get(0).getLatitude();
           coordinates[1]=user.get(0).getLongitude();
         
           }catch (Exception e) {
                   e.printStackTrace();
           }
           Log.d(tag, "Latitude:" + coordinates[0] + " / Longitude" + coordinates[1]);
       }
        
        return coordinates;
	}
}
