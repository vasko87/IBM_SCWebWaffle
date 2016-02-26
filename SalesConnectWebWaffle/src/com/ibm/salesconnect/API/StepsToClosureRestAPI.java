package com.ibm.salesconnect.API;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.salesconnect.common.HttpUtils;

public class StepsToClosureRestAPI {

	public static final String stepsToClosureURLExtension = "rest/v10/ibm_Stepstoclosure";
	
	
	Logger log = LoggerFactory.getLogger(StepsToClosureRestAPI.class);
	
	/**
	 * Get Steps To Closure via the REST API specifying the expected response
	 * @param url
	 * @param oauthToken
	 * @param expectedResponse
	 * @return response as a JSONObject
	 */
	public JSONArray getStepsToClosure(String url, String oauthToken, String expectedResponse){
		String headers[]={"OAuth-Token", oauthToken};
		
		HttpUtils restCalls = new HttpUtils();
		String getResponseString = restCalls.getRequest(url, headers, expectedResponse);
		 
		JSONArray stepsToClosureResponse = null;
		try {
			//check for a valid JSON response
			stepsToClosureResponse = (JSONArray) new JSONParser().parse(getResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
			Assert.assertTrue(false, "Parse exception with post response");
		}
		
		log.info("Valid JSON returned." + getResponseString);
		return stepsToClosureResponse;
	}
	
	
	/**
	 * Create Steps To Closure via the REST API specifying the expected response
	 * @param url
	 * @param oauthToken
	 * @param expectedResponse
	 * @return response as a JSONObject
	 */
	public JSONObject createStepsToClosure(String url, String oauthToken, String opptyID, String assignedUID, String type, String stepComment, String expectedResponse){ 
		
		String headers[]={"OAuth-Token", oauthToken};
		String body = "{\"opportunity_id\":\"" + opptyID + "\",\"assigned_user_id\":\"" + assignedUID + "\",\"comments\":\""+ stepComment +"\",\"type\":\"" + type + "\"}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url, headers, body, "application/json", expectedResponse);
		 
		JSONObject stepsToClosureResponse = null;
		try {
			//check for a valid JSON response
			stepsToClosureResponse = (JSONObject) new JSONParser().parse(postResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
			Assert.assertTrue(false, "Parse exception with post response");
		}
		
		return stepsToClosureResponse;
	}
	
	/**
	 * Create Steps To Closure via the REST API specifying the expected response
	 * @param url
	 * @param oauthToken
	 * @param expectedResponse
	 * @return response as a JSONObject
	 */
	public JSONObject createStepsToClosure(String url, String oauthToken, String opptyID, String assignedUID, String type, String rliId, String stepComment, String expectedResponse){ 
		
		String headers[]={"OAuth-Token", oauthToken};
		String body = "{\"opportunity_id\":\"" + opptyID + "\",\"assigned_user_id\":\"" + assignedUID + "\",\"comments\":\""+ stepComment +"\",\"type\":\"" + type + "\",\"rli_id\": \"" + rliId + "\"}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url, headers, body,"application/json", expectedResponse);
		 
		JSONObject stepsToClosureResponse = null;
		try {
			//check for a valid JSON response
			stepsToClosureResponse = (JSONObject) new JSONParser().parse(postResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
			Assert.assertTrue(false, "Parse exception with post response");
		}
		
		return stepsToClosureResponse;
	}
	
	/**
	 * Create Steps To Closure Helper via the REST API
	 * @param url
	 * @param oauthToken
	 * @param expectedResponse
	 * @return response as a JSONObject
	 */
	public JSONObject createStepsToClosureHelper(String url, String oauthToken, String opptyID, String assignedUID, String type, String expectedResponse){ 
		String headers[]={"OAuth-Token", oauthToken};
		String body = "{\"opportunity_id\":\"" + opptyID + "\",\"assigned_user_id\":\"" + assignedUID + "\",\"comments\":\"StepsToClosure Comment " + System.currentTimeMillis() +"\",\"type\":\"" + type + "\"}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url, headers, body, "application/json", expectedResponse);
		 
		JSONObject stepsToClosureResponse = null;
		try {
			//check for a valid JSON response
			stepsToClosureResponse = (JSONObject) new JSONParser().parse(postResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
			Assert.assertTrue(false, "Parse exception with post response");
		}
		
		return stepsToClosureResponse;
	}
	
	/**
	 * Create Steps To Closure Helper via the REST API
	 * @param url
	 * @param oauthToken
	 * @param expectedResponse
	 * @return response as a JSONObject
	 */
	public JSONObject createStepsToClosureHelper(String url, String oauthToken, String opptyID, String assignedUID, String type, String rliId, String expectedResponse){
		String headers[]={"OAuth-Token", oauthToken};
		String body = "{\"opportunity_id\":\"" + opptyID + "\",\"assigned_user_id\":\"" + assignedUID + "\",\"comments\":\"StepsToClosure Comment " + System.currentTimeMillis() +"\",\"type\":\"" + type + "\",\"rli_id\": \"" + rliId + "\"}";

		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url, headers, body, "application/json", expectedResponse);
		 
		JSONObject stepsToClosureResponse = null;
		try {
			//check for a valid JSON response
			stepsToClosureResponse = (JSONObject) new JSONParser().parse(postResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
			Assert.assertTrue(false, "Parse exception with post response");
		}
		
		return stepsToClosureResponse;
	}
	
	
	/**
	 * Update Steps To Closure via the REST API specifying the expected response
	 * @param url
	 * @param oauthToken
	 * @param expectedResponse
	 * @return response as a JSONObject
	 */
	public JSONObject updateStepsToClosure(String url, String oauthToken, String body, String expectedResponse){
		String headers[]={"OAuth-Token", oauthToken};

		HttpUtils restCalls = new HttpUtils();
		String putResponseString = restCalls.putRequest(url, headers, body, "application/json", expectedResponse);
		 
		JSONObject stepsToClosureResponse = null;
		try {
			//check for a valid JSON response
			stepsToClosureResponse = (JSONObject) new JSONParser().parse(putResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
			Assert.assertTrue(false, "Parse exception with post response");
		}
		
		return stepsToClosureResponse;
	}
	
	
}
