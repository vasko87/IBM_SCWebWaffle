package com.ibm.salesconnect.API;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.salesconnect.common.HttpUtils;

public class LineItemRestAPI {
	public static final String lineItemURLExtension = "rest/v10/ibm_RevenueLineItems";
	
	Logger log = LoggerFactory.getLogger(LineItemRestAPI.class);
	
	
	/*
	 * Creates a line item via the SalesConnect REST APIs.
	 */
	public String createLineItem(String url, String oauthToken, String amount, String probability, String liContractType, String date, String level10, String level15, String level17, String level20, String level30, String level40, String currency, String assignedUserName, String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String body = "{\"revenue_amount\":\"" + amount + "\",\"probability\":\"" + probability + "\",\"srv_work_type\":\"" + liContractType + "\",\"fcast_date_sign\":\"" + date + "\",\"level10\":\"" + level10 + "\",\"level15\":\"" + level15 + "\",\"level17\":\"" + level17 + "\",\"level20\":\"" + level20 + "\",\"level30\":\"" + level30 + "\",\"level40\":\"" + level40 + "\",\"currency_id\":\"" + currency + "\",\"assigned_user_name\":\"" + assignedUserName + "\",\"assigned_user_id\":\"" + assignedUID + "\"}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + LineItemRestAPI.lineItemURLExtension, headers, body, contentType);
		
		try {
			//check for a valid JSON response
			JSONObject postResponse = (JSONObject)new JSONParser().parse(postResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
			return "false";
		}
		
		log.info("Valid JSON returned.");
		return postResponseString;
	}

	/*
	 * Creates a line item via the SalesConnect REST APIs and returns Line Item ID
	 */
	public String createLineItemReturnBean(String url, String oauthToken, String amount, String probability, String liContractType, String date, String level10, String level15, String level17, String level20, String level30, String level40, String currency, String assignedUserName, String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		String lineItemID = "";
		
		String body = "{\"revenue_amount\":\"" + amount + "\",\"probability\":\"" + probability + "\",\"srv_work_type\":\"" + liContractType + "\",\"fcast_date_sign\":\"" + date + "\",\"level10\":\"" + level10 + "\",\"level15\":\"" + level15 + "\",\"level17\":\"" + level17 + "\",\"level20\":\"" + level20 + "\",\"level30\":\"" + level30 + "\",\"level40\":\"" + level40 + "\",\"currency_id\":\"" + currency + "\",\"assigned_user_name\":\"" + assignedUserName + "\",\"assigned_user_id\":\"" + assignedUID + "\"}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + LineItemRestAPI.lineItemURLExtension, headers, body, contentType);
		
		try {
			//check for a valid JSON response
			JSONObject postResponse = (JSONObject)new JSONParser().parse(postResponseString);
			lineItemID = (String) postResponse.get("id");
		} catch (ParseException e) {
			e.printStackTrace();
			return "false";
		}
		
		log.info("Valid JSON returned. " + postResponseString);
		return lineItemID;
	}
	
	public String postLineItem(String url, String oauthToken,String body, String expectedResponse){
		String headers[]={"OAuth-Token", oauthToken};
		
		HttpUtils restCalls = new HttpUtils();
		String PostResponseString = restCalls.postRequest(url,  headers, body,  "application/json ", expectedResponse);
		 
		try {
			//check for a valid JSON response
			new JSONParser().parse(PostResponseString);
		} 
		catch (ParseException e) 
		{
			e.printStackTrace();
			Assert.assertTrue(false, "Parse exception with post response");
		}
		
		log.info("Valid JSON returned." + PostResponseString);
		return PostResponseString;
	}
	
	/**
	 * Delete Line Item using DELETE
	 * @param url
	 * @param oauthToken
	 * @param body
	 * @param expectedResponseCode
	 */
	public void deleteLineItem(String url, String oauthToken, String body, String expectedResponseCode){
		String headers[]={"OAuth-Token", oauthToken};
		
		HttpUtils restCalls = new HttpUtils();
		String deleteResponseString = restCalls.deleteRequest(url, headers, body, expectedResponseCode);
		
		try {
			//check for a valid JSON response
			new JSONParser().parse(deleteResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		log.info("Valid JSON returned." + deleteResponseString);
	}

	/**
	 * Get Line Item via the rest api specifying the expected response
	 * @param url
	 * @param oauthToken
	 * @param expectedResponse
	 * @return response as a JSONObject
	 */
	public JSONObject getLineItem(String url, String oauthToken, String expectedResponse){
		String headers[]={"OAuth-Token", oauthToken};
		
		HttpUtils restCalls = new HttpUtils();
		String getResponseString = restCalls.getRequest(url, headers, expectedResponse);
		 
		JSONObject rliResponse = null;
		try {
			//check for a valid JSON response
			rliResponse = (JSONObject) new JSONParser().parse(getResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
			Assert.assertTrue(false, "Parse exception with post response");
		}
		
		log.info("Valid JSON returned." + getResponseString);
		return rliResponse;
	}

	/**
	 * Put Line Item via the rest api specifying the expected response
	 * @param url
	 * @param oauthToken
	 * @param body of request
	 * @param expectedResponse
	 * @return response as a JSONObject
	 */
	public JSONObject putLineItem(String url, String oauthToken,String body, String expectedResponse){
		String headers[]={"OAuth-Token", oauthToken};
		
		HttpUtils restCalls = new HttpUtils();
		String PutResponseString = restCalls.putRequest(url,  headers, body,  "application/json ", expectedResponse);
		 
		JSONObject rliResponse = null;
		try {
			//check for a valid JSON response
			rliResponse = (JSONObject) new JSONParser().parse(PutResponseString);
		} 
		catch (ParseException e) 
		{
			e.printStackTrace();
			Assert.assertTrue(false, "Parse exception with post response");
		}
		
		log.info("Valid JSON returned." + PutResponseString);
		return rliResponse;
	}
	


}
