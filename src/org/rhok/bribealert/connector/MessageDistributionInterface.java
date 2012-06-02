package org.rhok.bribealert.connector;

import org.apache.http.HttpEntity;

public interface MessageDistributionInterface {
	
	public void distributeMessage(HttpEntity entity);

}
