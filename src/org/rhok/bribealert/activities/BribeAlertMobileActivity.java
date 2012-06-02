package org.rhok.bribealert.activities;

import java.io.IOException;

import org.rhok.bribealert.R;
import org.rhok.bribealert.services.RecordingService;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class BribeAlertMobileActivity extends Activity {

	private RecordingService mRecService;
	private boolean mIsBound;

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

		doBindService();

		Button recBtn = (Button) findViewById(R.id.button1);
		recBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				try {
					mRecService.startRecording();
					Log.d(tag, "Started recording");
					moveTaskToBack(true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		Button vidRecBtn = (Button) findViewById(R.id.button2);
		vidRecBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				startActivity(new Intent(BribeAlertMobileActivity.this,
						CameraActivity.class));
				Log.d(tag, "Started video activity");
				// moveTaskToBack(true);

			}
		});
	}

	public void startGetHelpActivity(View v) {
		startActivity(new Intent(BribeAlertMobileActivity.this,
				GetHelpActivity.class));
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// doUnbindService();
	}

	protected void onResume() {
		super.onResume();

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

}
