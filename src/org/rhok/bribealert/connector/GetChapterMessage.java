package org.rhok.bribealert.connector;

import org.apache.http.HttpEntity;

import android.location.Location;

public class GetChapterMessage implements MessageInterface{

	String TAG = "GetChapterMessage";
	
	HttpEntity content;
	String uri;
	
	public GetChapterMessage(Location location){
		buildURI(location);
		
	 
	}
	
	private void buildURI(Location location){
		uri = "?lat="+location.getLatitude()+"&lon="+location.getLongitude();
	}


	public HttpEntity getContent() {
		return null;
	}

	public String getURI() {
		
		return uri;
	}
	
	
	
}
