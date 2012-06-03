package org.rhok.bribealert.activities;

import java.io.File;
import java.sql.Array;

import org.rhok.bribealert.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SpinnerAdapter;

public class ReportingActivity extends Activity{

	private static final String TAG = "ReportingActivity";
	private boolean publish = false;
	Button uploadDataButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reporting);
		
		uploadDataButton = (Button)findViewById(R.id.uploadButton);
		loadDataIntoSpinner();
	}
	
	private void loadDataIntoSpinner() {
		ArrayAdapter<File> adapter = new ArrayAdapter<File>(this, R.layout.file_item);
		
	}

	public void publish(View view){
		if(publish){
			publish = false;
			uploadDataButton.setText(getString(R.string.upload_button_text));
		}else{
			uploadDataButton.setText(getString(R.string.publish_button_text));
			publish = true;
		}
		
		Log.d(TAG, "User wishes to publish the bride alert public: " + publish);
	}
	
	public void uploadData(View view){
		
	}
	
}
