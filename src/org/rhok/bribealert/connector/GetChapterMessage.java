package org.rhok.bribealert.connector;

import org.apache.http.HttpEntity;

import android.location.Location;

public class GetChapterMessage implements MessageInterface{

	Location location;
	
	String uri;
	
	public GetChapterMessage(Location location){
		this.location=location;

	}

	public HttpEntity getContent() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getURI() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
