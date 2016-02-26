package com.ibm.salesconnect.API;

import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.common.HttpUtils;

public class LoginRestAPI {
	
	public static final String meURLExtension = "rest/v10/me";
	public static final String metadataURLExtension = "rest/v10/metadata?type_filter=server_info,labels,modules,relationships,currencies&module_filter=&platform=mobile";
	public static final String logoutExtension = "rest/v10/oauth2/logout";
	
	Logger log = LoggerFactory.getLogger(LoginRestAPI.class);
	
	/*
	 * Retrieves the oauth2 token from SalesConnect via REST.
	 */
	public String getOAuth2Token(String url, String userName, String password){
		
		return getOAuth2Token(url, userName, password, "200");
	}
	
	/*
	 * Retrieves the oauth2 token from SalesConnect via REST.
	 */
	public String getOAuth2Token(String url, String userName, String password, String expectedResponse){
		
		String contentType = "application/x-www-form-urlencoded";
		String body = "{\"grant_type\":\"password\",\"username\":\"" + userName + "\",\"password\":\"" + password + "\",\"client_id\":\"sugar\",\"platform\":\"mobile\",\"client_secret\":\"\",\"client_info\":{\"app\":{\"name\":\"nomad\",\"isNative\":false,\"version\":\"3.0.0\",\"build\":\"{build}\"},\"device\":{},\"browser\":{\"webkit\":true,\"version\":\"600.2.5\",\"userAgent\":\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_1) AppleWebKit/600.2.5 (KHTML, like Gecko) Version/8.0.2 Safari/600.2.5\"}}}";
		//String body = "{\"grant_type\":\"password\",\"username\":\"" + userName + "\",\"password\":\"" + password + "\",\"client_id\":\"scm_ios\",\"platform\":\"mobile\",\"client_secret\":\"\",\"app\":{\"name\":\"nomad\",\"isNative\":true,\"version\":\"2.2.0\",\"build\":\"15009\"},\"device\":{\"model\":\"iPhone5,2\",\"timeZone\":{\"offset\":0,\"name\":\"Europe/Dublin\",\"code\":\"GMT\"},\"ios\":true,\"carrier\":\"O2\",\"osVersion\":\"7.1.2\",\"modelName\":\"iPhone 5 Global\",\"locale\":\"en_IE\",\"platform\":\"ios\",\"uuid\":\"3DABF865-166C-45C7-897E-922261883C41\"},\"browser\":{\"webkit\":true,\"version\":\"537.51.2\",\"safari\":true,\"webview\":true,\"chrome\":false,\"userAgent\":\"Mozilla/5.0 (iPhone; CPU iPhone OS 7_1_2 like Mac OS X) AppleWebKit/537.51.2 (KHTML, like Gecko) Mobile/11D257 (366355232) IBM/SalesConnect\"},\"current_language\":\"en_us\",\"client_info\":{\"app\":{\"name\":\"nomad\",\"isNative\":true,\"version\":\"2.2.0\",\"build\":\"15009\"},\"device\":{\"model\":\"iPhone5,2\",\"timeZone\":{\"offset\":0,\"name\":\"Europe/Dublin\",\"code\":\"GMT\"},\"ios\":true,\"carrier\":\"O2\",\"osVersion\":\"7.1.2\",\"modelName\":\"iPhone 5 Global\",\"locale\":\"en_IE\",\"platform\":\"ios\",\"uuid\":\"3DABF865-166C-45C7-897E-922261883C41\"},\"browser\":{\"webkit\":true,\"version\":\"537.51.2\",\"safari\":true,\"webview\":true,\"chrome\":false,\"userAgent\":\"Mozilla/5.0 (iPhone; CPU iPhone OS 7_1_2 like Mac OS X) AppleWebKit/537.51.2 (KHTML, like Gecko) Mobile/11D257 (366355232) IBM/SalesConnect\"},\"current_language\":\"en_us\"}}"; 
			
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + GC.authURLExtension, GC.emptyArray, body, contentType, expectedResponse);
		
		JSONObject postResponse = null;
		
		try {
			//confirm the response is valid JSON
			postResponse = (JSONObject)new JSONParser().parse(postResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
			return "false";
		}
		
		String token = "";
		
		try {
			//get the oauth token
			token = (String) postResponse.get("access_token");
		} catch (Exception e) {
			log.info("Token was not returned as expected");
			return "false";
		}
		log.info("Token returned: " + token);
		return token;
	}
	
	/**
	 * Get Oauth token via api manager
	 * @param url
	 * @param userName
	 * @param password
	 * @param expectedResponse
	 * @return
	 */
	public String getOAuth2TokenViaAPIManager(String url, String userName, String password, String expectedResponse){
		
		String contentType = "application/json";
		
		HashMap<String,Object> browser = new HashMap<String, Object>();
		browser.put("webkit", true);
		browser.put("version", "600.2.5");
		browser.put("userAgent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_1) AppleWebKit/600.2.5 (KHTML, like Gecko) Version/8.0.2 Safari/600.2.5");
		HashMap<String, Object> app = new HashMap<String, Object>();
		app.put("name", "nomad");
		app.put("isNative", false);
		app.put("version", "3.0.0");
		app.put("build", "1");
		HashMap<String,Object> client_info = new HashMap<String, Object>();
		client_info.put("app", app);
		client_info.put("browser", browser);
		client_info.put("device", new HashMap<String, Object>());
		HashMap<String,Object> hmap = new HashMap<String, Object>();
		hmap.put("grant_type", "password");
		hmap.put("username", userName);
		hmap.put("password", password);
		hmap.put("client_id", "sugar");
		hmap.put("platform","apiclient");
		hmap.put("client_secret", "");
		hmap.put("client_info", client_info);
		
		JSONObject json = new JSONObject(hmap);
		log.info("Request url: " + url);
		log.info("Request body: " + json.toString());
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url, GC.emptyArray, json, contentType,expectedResponse);
		
		JSONObject postResponse = null;
		
		if (expectedResponse.equals("200") || postResponseString.contains("access_token")) {	
			try {
				//confirm the response is valid JSON
				postResponse = (JSONObject)new JSONParser().parse(postResponseString);
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false,"Parsing exception from token request: " + postResponseString);
			}
		}
		else {
			log.info("Token request failed as expected");
			return postResponseString;
		}
		
		
		String token = "";
		
		try {
			//get the oauth token
			token = (String) postResponse.get("access_token");
		} catch (Exception e) {
			log.info("Token was not returned as expected");
			return "false";
		}
		log.info("Token returned: " + token);
		return token;
	}
	
//	/**
//	 * Get OAuth token via API Management tool without sending the client info
//	 * @param url
//	 * @param userName
//	 * @param password
//	 * @param expectedResponse
//	 * @return token
//	 */
//	public String getOAuth2TokenViaAPIManagerNoClientInfo(String url, String userName, String password, String expectedResponse){
//		
//		String contentType = "application/json";
//		
//		HashMap<String,Object> hmap = new HashMap<String, Object>();
//		hmap.put("grant_type", "password");
//		hmap.put("username", userName);
//		hmap.put("password", password);
//		hmap.put("client_id", "sugar");
//		hmap.put("platform","apiclient");
//		hmap.put("client_secret", "");
//		
//		JSONObject json = new JSONObject(hmap);
//		log.info("Request url: " + url);
//		log.info("Request body: " + json.toString());
//		HttpUtils restCalls = new HttpUtils();
//		String postResponseString = restCalls.postRequest(url, GC.emptyArray, json, contentType,expectedResponse);
//		
//		JSONObject postResponse = null;
//		
//		if (expectedResponse.equals("200") || postResponseString.contains("access_token")) {	
//			try {
//				//confirm the response is valid JSON
//				postResponse = (JSONObject)new JSONParser().parse(postResponseString);
//			} catch (ParseException e) {
//				e.printStackTrace();
//				Assert.assertTrue(false,"Parsing exception from token request: " + postResponseString);
//			}
//		}
//		else {
//			log.info("Token request failed as expected");
//			return postResponseString;
//		}
//		
//		
//		String token = "";
//		
//		try {
//			//get the oauth token
//			token = (String) postResponse.get("access_token");
//		} catch (Exception e) {
//			log.info("Token was not returned as expected");
//			return "false";
//		}
//		log.info("Token returned: " + token);
//		return token;
//	}
	
	
public String getOAuth2TokenViaAPIManager(String url, String userName, String password, String expectedResponse, String appSecretID){
		
		String contentType = "application/json";
		
		HashMap<String,Object> browser = new HashMap<String, Object>();
		browser.put("webkit", true);
		browser.put("version", "600.2.5");
		browser.put("userAgent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_1) AppleWebKit/600.2.5 (KHTML, like Gecko) Version/8.0.2 Safari/600.2.5");
		HashMap<String, Object> app = new HashMap<String, Object>();
		app.put("name", "nomad");
		app.put("isNative", false);
		app.put("version", "3.0.0");
		app.put("build", "1");
		HashMap<String,Object> client_info = new HashMap<String, Object>();
		client_info.put("app", app);
		client_info.put("browser", browser);
		client_info.put("device", new HashMap<String, Object>());
		HashMap<String,Object> hmap = new HashMap<String, Object>();
		hmap.put("grant_type", "password");
		hmap.put("username", userName);
		hmap.put("password", password);
		hmap.put("client_id", "sugar");
		hmap.put("platform","apiclient");
		hmap.put("client_secret", "");
		hmap.put("client_info", client_info);
		
		JSONObject json = new JSONObject(hmap);
		log.info("Request url: " + url + appSecretID);
		log.info("Request body: " + json.toString());
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + appSecretID, GC.emptyArray, json, contentType,expectedResponse);
		
		JSONObject postResponse = null;
		
		if (expectedResponse.equals("200") || postResponseString.contains("access_token")) {	
			try {
				//confirm the response is valid JSON
				postResponse = (JSONObject)new JSONParser().parse(postResponseString);
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false,"Parsing exception from token request: " + postResponseString);
			}
		}
		else {
			log.info("Token request failed as expected");
			return postResponseString;
		}
		
		
		String token = "";
		
		try {
			//get the oauth token
			token = (String) postResponse.get("access_token");
		} catch (Exception e) {
			log.info("Token was not returned as expected");
			return "false";
		}
		log.info("Token returned: " + token);
		return token;
	}
	/*
	 * Retrieves the user preferences from SalesConnect via REST.
	 */
	public String getMe(String url, String oauthToken){
		
		String headers[]={"OAuth-Token", oauthToken};
		
		HttpUtils restCalls = new HttpUtils();
		String getResponseString = restCalls.getRequest(url + LoginRestAPI.meURLExtension, headers);
		
		try {
			//confirm the response is valid JSON
			JSONObject getResponse = (JSONObject)new JSONParser().parse(getResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
			return "false";
		}
		
		return getResponseString;
	}
	
	/*
	 * Retrieves the mobile metadata from SalesConnect via REST.
	 */
	public String getMetadata(String url, String oauthToken){
		
		String headers[]={"OAuth-Token", oauthToken};
		
		HttpUtils restCalls = new HttpUtils();
		String getResponseString = restCalls.getRequest(url + LoginRestAPI.metadataURLExtension, headers);
		
		try {
			//confirm the response is valid JSON
			JSONObject getResponse = (JSONObject)new JSONParser().parse(getResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
			return "false";
		}
		
		return getResponseString;
	}
	
	
	/*
	 * Retrieves the mobile labels from SalesConnect via REST.
	 */
	public String getLabels(String url, String oauthToken, String labelExtension){
		
		String headers[]={"OAuth-Token", oauthToken};
		
		HttpUtils restCalls = new HttpUtils();
		String getResponseString = restCalls.getRequest(url + labelExtension, headers);
		
		try {
			//confirm the response is valid JSON
			JSONObject getResponse = (JSONObject)new JSONParser().parse(getResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
			return "false";
		}
		
		return getResponseString;
	}
	
	
	/*
	 * Logs out of SalesConnect via REST.
	 */
	public String logout(String url, String oauthToken){
		
		String headers[]={"OAuth-Token", oauthToken};
		
		String contentType = "application/json";
		String body = "{\"token\":\"" + oauthToken + "\"}";
		
		
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + LoginRestAPI.logoutExtension, headers, body, contentType);
		
		try {
			//confirm the response is valid JSON
			JSONObject postResponse = (JSONObject)new JSONParser().parse(postResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
			return "false";
		}
		
		return postResponseString;
	}


}
