package com.ibm.salesconnect.API;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.salesconnect.common.HttpUtils;

public class WinPlanRestAPI {

	public static final String winplanURLExtension = "rest/v10/ibm_Winplans";
	
	
	Logger log = LoggerFactory.getLogger(WinPlanRestAPI.class);
	
	/**
	 * Get WinPlan via the rest api specifying the expected response
	 * @param url
	 * @param oauthToken
	 * @param expectedResponse
	 * @return response as a JSONObject
	 */
	public JSONObject getWinPlan(String url, String oauthToken, String expectedResponse){
		String headers[]={"OAuth-Token", oauthToken};
		
		HttpUtils restCalls = new HttpUtils();
		String getResponseString = restCalls.getRequest(url, headers, expectedResponse);
		 
		JSONObject winplanResponse = null;
		try {
			//check for a valid JSON response
			winplanResponse = (JSONObject) new JSONParser().parse(getResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
			Assert.assertTrue(false, "Parse exception with post response");
		}
		
		log.info("Valid JSON returned." + getResponseString);
		return winplanResponse;
	}
	
	/**
	 * Create WinPlan via the rest api specifying the expected response
	 * @param url
	 * @param oauthToken
	 * @param expectedResponse
	 * @return response as a JSONObject
	 */
	public JSONObject createWinPlan(String url, String oauthToken, String opptyID, String expectedResponse){
		String headers[]={"OAuth-Token", oauthToken};
		String body = "{\"parent_id\":\"" + opptyID +"\",\"parent_name\":\"" + opptyID + "\",\"parent_type\":\"Opportunities\"}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url, headers, body, expectedResponse);
		 
		JSONObject winplanResponse = null;
		try {
			//check for a valid JSON response
			winplanResponse = (JSONObject) new JSONParser().parse(postResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
			Assert.assertTrue(false, "Parse exception with post response");
		}
		
		return (JSONObject) winplanResponse.get("related_record");
	}
	
	/**
	 * Create WinPlan via the rest api
	 * @param url
	 * @param oauthToken
	 * @param expectedResponse
	 * @return response as a JSONObject
	 */
	public JSONObject createWinPlan(String url, String oauthToken, String opptyID){

		return createWinPlan(url, oauthToken, opptyID, "200");
	}
	
	
	/**
	 * Update WinPlan via the rest api specifying the expected response
	 * @param url
	 * @param oauthToken
	 * @param expectedResponse
	 * @return response as a JSONObject
	 */
	public JSONObject updateWinPlan(String url, String oauthToken, String body, String expectedResponse){
		String headers[]={"OAuth-Token", oauthToken};

		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.putRequest(url, headers, body, expectedResponse);
		 
		JSONObject winplanResponse = null;
		try {
			//check for a valid JSON response
			winplanResponse = (JSONObject) new JSONParser().parse(postResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
			Assert.assertTrue(false, "Parse exception with post response");
		}
		
		return winplanResponse;
	}
	
	
}
