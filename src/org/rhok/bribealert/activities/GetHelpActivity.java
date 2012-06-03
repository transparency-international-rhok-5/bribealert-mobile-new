package org.rhok.bribealert.activities;

import org.apache.http.HttpEntity;
import org.rhok.bribealert.R;
import org.rhok.bribealert.connector.MessageDistributionInterface;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class GetHelpActivity extends Activity {
	
	
	
	private static final String TAG = "GetHelpActivity";
	MessageDistributionInterface msg =  new MessageDistributionInterface() {
		
		@Override
		public void distributeMessage(HttpEntity entity) {
			// TODO Auto-generated method stub
			
		}
	};
	
	public void startReportingActivity(View view) {
		startActivity(new Intent(this,
				ReportingActivity.class));
		Log.d(TAG, "Started gethelp activity");
	}
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
        
        
    }
    


	
	
}
