package org.rhok.bribealert.activities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.http.HttpEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.rhok.bribealert.R;
import org.rhok.bribealert.connector.GetChapterMessage;
import org.rhok.bribealert.connector.GetRESTConnector;
import org.rhok.bribealert.connector.MessageDistributionInterface;
import org.rhok.bribealert.provider.LocationProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class GetHelpActivity extends Activity {

	private final static String TAG = "GetHelpActivity";

	private static final String TELEPHONE_KEY = "telephone";
	private static final String CITY_KEY = "city";
	private static final String FAX_KEY = "fax";
	private static final String URL_KEY = "url";
	private static final String COUNTRY_KEY = "country";
	private static final String ZIPCODE_KEY = "zipcode";
	private static final String STREET_KEY = "street";
	private static final String EMAIL_KEY = "email";
	
	String messageString;

	private TextView telephoneNumberView = null;
	private TextView tiChapterView = null;
	private TextView faxNumberView = null;
	private TextView zipcodeView = null;
	private TextView urlView = null;
	private TextView streetView = null;
	private TextView emailView = null;

	JSONObject valuesObject = null;

	MessageDistributionInterface msg = new MessageDistributionInterface() {
		@Override
		public void distributeMessage(HttpEntity entity) {
			parseChapter(entity);
		}
	};

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			
			Bundle data = msg.getData();
			
			tiChapterView.setText("TIChapter: " + data.getString(COUNTRY_KEY) + " (" + data.getString(CITY_KEY) + ")");
			telephoneNumberView.setText(data.getString(TELEPHONE_KEY));
			faxNumberView.setText(data.getString(FAX_KEY));
			zipcodeView.setText(data.getString(ZIPCODE_KEY));
			urlView.setText(data.getString(URL_KEY));
			streetView.setText(data.getString(STREET_KEY));
			emailView.setText(data.getString(EMAIL_KEY));
			
			
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

		telephoneNumberView = (TextView) findViewById(R.id.telephoneNumber);
		faxNumberView = (TextView)findViewById(R.id.faxNumber);
		zipcodeView = (TextView)findViewById(R.id.zipCode);
		urlView = (TextView)findViewById(R.id.url);
		streetView = (TextView)findViewById(R.id.streetName);
		emailView = (TextView)findViewById(R.id.email);
		tiChapterView = (TextView)findViewById(R.id.tiChapterText);
		
		sendGetChapter();

	}

	private void sendGetChapter() {
		Location location = LocationProvider.getLocation(this);
		String serverIp = getString(R.string.serverIP);
		GetChapterMessage chapterMsg = new GetChapterMessage(location);
		GetRESTConnector connector = new GetRESTConnector(serverIp);
		connector.setMessageDistribution(msg);
		connector.execute(chapterMsg);

	}

	private void parseChapter(HttpEntity entity) {
		InputStream inputStream = null;
		try {
			inputStream = entity.getContent();
		} catch (IllegalStateException e1) {
			Log.e(TAG, "Illegal input stream state from http entity");
			e1.printStackTrace();
		} catch (IOException e1) {
			Log.e(TAG, "IO input stream expection from http entity");
			e1.printStackTrace();
		}
		String content = convertStreamToString(inputStream);

		try {
			valuesObject = new JSONObject(content);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Message msg = new Message();
		Bundle bundle = new Bundle(); 

		for ( Iterator<String> i = valuesObject.keys(); i.hasNext(); ){
		    String name = i.next(); 
			String value = null;
			try {
				value = (String)valuesObject.get(name);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    bundle.putString(name, value);
		}
		bundle.putString("content", content);
		msg.setData(bundle);
		mHandler.sendMessage(msg);

	}

	public String convertStreamToString(java.io.InputStream is) {
		try {
			return new java.util.Scanner(is).useDelimiter("\\A").next();
		} catch (java.util.NoSuchElementException e) {
			return "";
		}
	}

}
