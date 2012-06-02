package org.rhok.bribealert.connector;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class RestConnector {

	private static final String TAG = "RestConnector";

	private static final String HTTP_PREFIX = "http://";

	//TODO: Set path to rest service
	private static final String PATH_TO_REST_SERVICE = "/notification";

	private URI serverURL;

	private HttpClient httpClient = null;

	private HttpPost httpPost = null;

	public RestConnector(String serverIP) {
		try{
			serverURL = new URI(HTTP_PREFIX + serverIP + PATH_TO_REST_SERVICE);

			if(httpClient == null){

				httpClient = new DefaultHttpClient();
			}

			Log.d(TAG,"Setting serverURL: " + serverURL);

			httpPost = new HttpPost(serverURL);

		}catch (URISyntaxException use) {
			Log.e(TAG, "Can't set URI", use);
		}
	}

	public boolean connected(){
		return httpClient != null;
	}

	public boolean sendData(Notification notificationToSend) throws UnsupportedEncodingException, ClientProtocolException{
		httpPost.setEntity(notificationToSend.getContent());
		return executePostRequest();
	}

	private boolean executePostRequest() throws ClientProtocolException{
		try{
		HttpResponse response = httpClient.execute(httpPost);
		StatusLine statusLine = response.getStatusLine();
		response.getEntity().consumeContent();

		return parseStatusLine(statusLine);
		}catch(IOException ioe){
			Log.e(TAG, "IO error while sending data", ioe);
		}
		return false;
	}


	private boolean parseStatusLine(StatusLine statusLine) {
		switch(statusLine.getStatusCode()){
		case HttpStatus.SC_OK:		
			return true;
		}
		return false;
	}

}
