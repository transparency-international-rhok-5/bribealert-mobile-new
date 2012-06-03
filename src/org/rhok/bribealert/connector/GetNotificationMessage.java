package org.rhok.bribealert.connector;

import org.apache.http.HttpEntity;

public class GetNotificationMessage implements MessageInterface{

	String TAG = "GetChapterMessage";
	
	HttpEntity content;
	String uri;
	
	public GetNotificationMessage(String secretToken){
		buildURI(secretToken);
	}
	
	private void buildURI(String secrectToken){
		uri = "?secure_token=" + secrectToken;
	}


	public HttpEntity getContent() {
		return null;
	}

	public String getURI() {
		
		return uri;
	}
	
	
	
}
