package org.rhok.bribealert.activities;

import org.apache.http.HttpEntity;
import org.rhok.bribealert.R;
import org.rhok.bribealert.connector.MessageDistributionInterface;

import android.app.Activity;
import android.os.Bundle;

public class GetHelpActivity extends Activity {
	
	
	
	MessageDistributionInterface msg =  new MessageDistributionInterface() {
		
		@Override
		public void distributeMessage(HttpEntity entity) {
			// TODO Auto-generated method stub
			
		}
	};
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);
        
        
    }
    


	
	
}
