package org.rhok.bribealert.connector;

import java.io.UnsupportedEncodingException;

import org.apache.http.client.ClientProtocolException;

import android.os.AsyncTask;


public abstract class AbstractRESTConnector extends AsyncTask<MessageInterface, Boolean, Boolean> {

	private static final String TAG = "AbstractRESTConnector";
	
	abstract protected boolean sendData(MessageInterface msgToSend) throws ClientProtocolException;
	
	@Override
	protected Boolean doInBackground(MessageInterface... params) {
		if(params.length == 1){
			try {
				return sendData(params[0]);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

}
