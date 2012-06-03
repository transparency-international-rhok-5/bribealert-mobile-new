package org.rhok.bribealert.connector;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class GetRESTConnector extends AbstractRESTConnector {

	private static final String TAG = "GetRESTConnector";

	private static final String HTTP_PREFIX = "http://";

	// TODO: Set path to rest service
	private static final String PATH_TO_REST_SERVICE = "/national-chapter/";

	private URI serverURL;

	private HttpClient httpClient = null;

	private HttpGet httpGet = null;

	private MessageDistributionInterface messageDistributor = null;

	public GetRESTConnector(String serverIP) {
		try {
			serverURL = new URI(HTTP_PREFIX + serverIP + PATH_TO_REST_SERVICE);

			if (httpClient == null) {

				httpClient = new DefaultHttpClient();
			}


		} catch (URISyntaxException use) {
			Log.e(TAG, "Can't set URI", use);
		}
	}

	public boolean connected() {
		return httpClient != null;
	}

	@Override
	protected boolean sendData(MessageInterface messageToSend)
			throws ClientProtocolException {
		String uri = serverURL+messageToSend.getURI();
		
		Log.d(TAG, "RequestString: " +  uri);
		
		httpGet = new HttpGet(uri);
		return executeGetRequest();
	}

	private boolean executeGetRequest() throws ClientProtocolException {
		try {
			HttpResponse response = httpClient.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			
			Log.d(TAG,"Status for get-request" + statusLine);
			boolean validResponse = parseStatusLine(statusLine);
			if (validResponse) {
				HttpEntity entity = response.getEntity();
				if (messageDistributor != null) {
					messageDistributor.distributeMessage(entity);
				} else {
					Log.e(TAG, "No distribution interface set");
				}
				entity.consumeContent();
				return validResponse;
			}
		} catch (IOException ioe) {
			Log.e(TAG, "IO error while sending data", ioe);
		}
		return false;
	}

	private boolean parseStatusLine(StatusLine statusLine) {
		switch (statusLine.getStatusCode()) {
		case HttpStatus.SC_OK:
			return true;
		}
		return false;
	}
	
	public void setMessageDistribution(MessageDistributionInterface messageDistributor){
		this.messageDistributor = messageDistributor;
	}

}
