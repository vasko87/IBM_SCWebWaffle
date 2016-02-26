/**
 * 
 */
package com.ibm.salesconnect.API;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.salesconnect.common.HttpUtils;

/**
 * @author timlehane
 * @date Aug 20, 2014
 */
public class ConnectionsAPI {
	Logger log = LoggerFactory.getLogger(ConnectionsAPI.class);
	
	public String getCommunityFileFeed(String URL,String[] connHeaders, String communityID){
		// if supplied URL contains '/' as last character, need to handle it when building path for API call
		if (URL.lastIndexOf('/') > 7) {
			URL+="files/basic/api/communitycollection/"+communityID+"/feed";
		} else {
			URL+="/files/basic/api/communitycollection/"+communityID+"/feed";
		}
		log.info("Sending GET request: " + URL);
		HttpUtils httpUtils = new HttpUtils();
		String getResponseString = httpUtils.getRequest(URL,connHeaders);
		log.info("Received GET response: "+getResponseString);
		return getResponseString;
	}
}
