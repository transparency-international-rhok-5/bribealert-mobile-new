package org.rhok.bribealert.activities;

import org.rhok.bribealert.R;

import android.app.Activity;
import android.os.Bundle;

public class LocationActivity extends Activity {

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        getLocation();
    }
    
    public void getLocation(){
    	
    }
	
	
}
