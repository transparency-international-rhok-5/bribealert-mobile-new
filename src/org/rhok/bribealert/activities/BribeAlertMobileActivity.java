package org.rhok.bribealert.activities;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.rhok.bribealert.R;
import org.rhok.bribealert.connector.GetNotificationMessage;
import org.rhok.bribealert.connector.GetRESTConnector;
import org.rhok.bribealert.connector.MessageDistributionInterface;
import org.rhok.bribealert.services.RecordingService;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class BribeAlertMobileActivity extends Activity {

	private RecordingService mRecService;
	private boolean mIsBound;
	private boolean video;
	
	private static String tag = "BribeAlert MainActivity";

	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {

			mRecService = ((RecordingService.RecordingServiceBinder) service)
					.getService();
		}

		public void onServiceDisconnected(ComponentName className) {

			mRecService = null;
		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
//		checkForNotifications();
		
		doBindService();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, "Settings");
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		startActivity(new Intent(this, Settings.class));
		return super.onOptionsItemSelected(item);
	}
	
	public void startRecording(View v){
		try {
			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
			video = pref.getBoolean(getString(R.string.video_recording), false);
			Log.d(tag, "Video recording: " + video);
			if(video){
				startActivity(new Intent(this,TIVideoCamera.class));
			}else{
				mRecService.startRecording();
				Log.d(tag, "Started recording");
				moveTaskToBack(true);
			}
		} catch (IOException e) {
			Log.d(tag, "Problem starting the recorder " + e.getMessage());
		}
	}
	
	public void startIsThisCorruptionActivity(View v) {

		startActivity(new Intent(BribeAlertMobileActivity.this,
				SurveyActivity.class));
		Log.d(tag, "Started corruption activity");
		// moveTaskToBack(true);
	}

	public void startGetHelpActivity(View v) {
		startActivity(new Intent(BribeAlertMobileActivity.this,
				GetHelpActivity.class));
		Log.d(tag, "Started gethelp activity");
	}

	public void startReportingActivity(View view) {
		startActivity(new Intent(BribeAlertMobileActivity.this,
				ReportingActivity.class));
		Log.d(tag, "Started gethelp activity");
	}
	void doBindService() {
		// Establish a connection with the service. We use an explicit
		// class name because we want a specific service implementation that
		// we know will be running in our own process (and thus won't be
		// supporting component replacement by other applications).
		bindService(new Intent(BribeAlertMobileActivity.this,
				RecordingService.class), mConnection, Context.BIND_AUTO_CREATE);
		mIsBound = true;
	}

	void doUnbindService() {
		if (mIsBound) {
			// Detach our existing connection.
			unbindService(mConnection);
			mIsBound = false;
		}
	}

	protected void onResume() {
		super.onResume();

//		checkForNotifications();
		
		if (mRecService != null) {

			try {
				mRecService.stopRecording();
				Log.d(tag, "Stopped recording");

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void checkForNotifications() {
		SharedPreferences tokens = getSharedPreferences(getString(R.string.token_prefs), 0);
		String secretToken = tokens.getString(getString(R.string.secret_token),"");
		
		GetNotificationMessage notifactionMessage = new GetNotificationMessage(secretToken);
		GetRESTConnector getConnector = new GetRESTConnector(getString(R.string.serverIP), "messages");
		getConnector.setMessageDistribution(new SecretTokenDistrubition());
		
		getConnector.execute(notifactionMessage);
	}
	
	private class SecretTokenDistrubition implements MessageDistributionInterface{
		@Override
		public void distributeMessage(HttpEntity entity) {
			
			
		}
	}
}
