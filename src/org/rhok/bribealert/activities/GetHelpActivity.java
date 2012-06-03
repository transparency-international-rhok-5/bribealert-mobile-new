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
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

public class GetHelpActivity extends Activity {

	private final static String TAG = "GetHelpActivity";

	String messageString;

	TextView txtView = null;

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
			
			// call setText here
		}
	};
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);

		txtView = (TextView) findViewById(R.id.textView1);

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
