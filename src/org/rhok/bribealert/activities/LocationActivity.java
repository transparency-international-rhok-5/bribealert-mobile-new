package org.rhok.bribealert.activities;

import java.util.List;

import org.rhok.bribealert.R;
import org.rhok.bribealert.provider.LocationProvider;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;
import android.widget.TextView;
import android.widget.Toast;

public class LocationActivity extends Activity {
	
	double[] location;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
        location = LocationProvider.getLocation(this);
        TextView txtView1 = (TextView) findViewById(R.id.textView1);
        txtView1.setText(""+location[0]);
        TextView txtView2 = (TextView) findViewById(R.id.textView2);
        txtView2.setText(""+location[1]);
    }
    


	
	
}
