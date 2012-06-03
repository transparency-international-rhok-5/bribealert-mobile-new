package org.rhok.bribealert.connector;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

public class PostRESTConnector extends AbstractRESTConnector {

	private static final String TAG = "RestConnector";

	private static final String HTTP_PREFIX = "http://";

	// TODO: Set path to rest service
	private static final String PATH_TO_REST_SERVICE = "/upload/";

	private URI serverURL;

	private HttpClient httpClient = null;

	private HttpPost httpPost = null;

	private MessageDistributionInterface messageDistributor;

	public PostRESTConnector(String serverIP) {
		try {
			serverURL = new URI(HTTP_PREFIX + serverIP + PATH_TO_REST_SERVICE);

			if (httpClient == null) {

				httpClient = new DefaultHttpClient();
			}

			Log.d(TAG, "Setting serverURL: " + serverURL);

			httpPost = new HttpPost(serverURL);

		} catch (URISyntaxException use) {
			Log.e(TAG, "Can't set URI", use);
		}
	}

	public boolean connected() {
		return httpClient != null;
	}



	private boolean executePostRequest() throws ClientProtocolException {
		try {
			HttpResponse response = httpClient.execute(httpPost);
			StatusLine statusLine = response.getStatusLine();
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
			return parseStatusLine(statusLine);
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

	@Override
	protected boolean sendData(MessageInterface msgToSend)
			throws ClientProtocolException {
		httpPost.setEntity(msgToSend.getContent());
		return executePostRequest();
	}

	public void setMessageDistribution(MessageDistributionInterface messageDistributor){
		this.messageDistributor = messageDistributor;
	}

}
