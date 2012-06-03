package org.rhok.bribealert.connector;

import org.apache.http.HttpEntity;

public interface MessageInterface {
	
	public HttpEntity getContent();
	
	public String getURI();

}
