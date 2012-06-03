package org.rhok.bribealert.activities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.rhok.bribealert.R;
import org.rhok.bribealert.connector.MessageDistributionInterface;
import org.rhok.bribealert.connector.PostRESTConnector;
import org.rhok.bribealert.connector.UploadMessage;
import org.rhok.bribealert.provider.LocationProvider;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ReportingActivity extends Activity{

	private static final String TAG = "ReportingActivity";
	private static final String RECORDINGS_DIR = "recordings";
	
	private boolean publish = false;
	Button uploadDataButton;
	EditText description;
	Spinner fileSpinner;
	
	private Handler msgHandler = new Handler(){
		
		public void handleMessage(Message msg) {
			SharedPreferences secretToken = getSharedPreferences(getString(R.string.token_prefs), 0);
			SharedPreferences.Editor edit = secretToken.edit();
			edit.putString(getString(R.string.secret_token), msg.getData().getString(getString(R.string.secret_token)));
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reporting);
		
		uploadDataButton = (Button)findViewById(R.id.uploadButton);
		fileSpinner = (Spinner)findViewById(R.id.filePicker);
		description = (EditText)findViewById(R.id.descriptionInputField);
		loadDataIntoSpinner();
	}
	
	private void loadDataIntoSpinner() {
		ArrayAdapter<File> adapter = new ArrayAdapter<File>(this, R.layout.file_item);
		checkDirectory(adapter);
		
		fileSpinner.setAdapter(adapter);
	}
	
	private void checkDirectory(ArrayAdapter<File> adapter) {
		File recordings = new File(Environment.getExternalStorageDirectory() + File.separator + RECORDINGS_DIR);
		if(recordings.exists() && recordings.isDirectory()){
			addFilesToAdapter(recordings,adapter);
		}else{
			return;
		}
	}

	private void addFilesToAdapter(File recordings, ArrayAdapter<File> adapter) {
		for(String audioFile : recordings.list()){
			adapter.add(new File(recordings.getAbsolutePath() + File.separator + audioFile));
		}
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
		File file = (File) fileSpinner.getAdapter().getItem(fileSpinner.getFirstVisiblePosition());
		try {
			UploadMessage uploadMessage = new UploadMessage(LocationProvider.getLocation(this), new Date(System.currentTimeMillis()), file);
			uploadMessage.addPublish(publish);
			addDescriptionIfAvaible(uploadMessage);
			
			PostRESTConnector postRestConnector = new PostRESTConnector(getString(R.string.serverIP));
			postRestConnector.setMessageDistribution(new SecretTokenDistributionInterface());
			postRestConnector.execute(uploadMessage);
			this.finish();
			
		} catch (FileNotFoundException e) {
			Toast.makeText(this, "Data was not send: " + e.getMessage(), Toast.LENGTH_LONG).show();
		} catch (UnsupportedEncodingException e) {
			Toast.makeText(this, "Data was not send: " + e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	private void addDescriptionIfAvaible(UploadMessage uploadMessage) throws UnsupportedEncodingException{
		String descriptionText = description.getText().toString();
		
		if(descriptionText != null && !descriptionText.equals("")){
			uploadMessage.addDescription(descriptionText);
		}
	}
	
	private class SecretTokenDistributionInterface implements MessageDistributionInterface{

		@Override
		public void distributeMessage(HttpEntity entity) {
			parseReturnEntity(entity);
		}

		private void parseReturnEntity(HttpEntity entity) {
			Message message = new Message();
			Bundle bundle = new Bundle();
			String secretToken;

			try {
				secretToken = new Scanner(entity.getContent()).useDelimiter("\\A").next();
				Log.d(TAG, "Got new secure token: " + secretToken);
				bundle.putString(getString(R.string.secret_token), secretToken);
				message.setData(bundle);
				msgHandler.sendMessage(message);
			} catch (IllegalStateException e) {
				Log.d(TAG,"Couldn't parse server response");
			} catch (IOException e) {
				Log.d(TAG,"Couldn't parse server response");
			}
		}
	}
	
}
