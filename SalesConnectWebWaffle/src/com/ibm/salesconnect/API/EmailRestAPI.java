/**
 * 
 */
package com.ibm.salesconnect.API;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.salesconnect.common.HttpUtils;

/**
 * @author timlehane
 * @date Oct 7, 2014
 */
public class EmailRestAPI {

	public static final String emailURLExtension = "rest/v10/Emails";
	
	Logger log = LoggerFactory.getLogger(EmailRestAPI.class);
	
	public String createEmailreturnBean(String url, String oauthToken) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String emailID = "";
		
		String body = getValidMailBody();
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + emailURLExtension, headers, body, contentType);
		
		try {
			//check for a valid JSON response
			JSONObject postResponse = (JSONObject)new JSONParser().parse(postResponseString);
			emailID = (String) postResponse.get("id");
		} catch (ParseException e) {
			e.printStackTrace();
			return "false";
		}
		
		log.info("Valid JSON returned.");
		return emailID;
	}
	
	
	public String getValidMailBody(){
			 return  "{\"from_addr\":\"ie01@tst.ibm.com\",\"created_by\":\"ie01@tst.ibm.com\",\"to_addrs\":\"john_doe@foo.com\",\"name\":\"John Doe\"," +
			  "\"status\":\"ready\",\"description\":\"Description\",\"type\":\"out\"}";
	}
	
	public String getEmail(String url,String oauthToken){
	
//		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String emailID = "";
		
//		String body = getValidMailBody();
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.getRequest(url + emailURLExtension+"/recipients/find", headers, "200");
		// + "/100065db-c76d-4002-5c53-54301fb6cb82"
		try {
			//check for a valid JSON response
			JSONObject postResponse = (JSONObject)new JSONParser().parse(postResponseString);
			emailID = (String) postResponse.get("id");
		} catch (ParseException e) {
			e.printStackTrace();
			return "false";
		}
		
		log.info("Valid JSON returned.");
		return emailID;
	}
}
