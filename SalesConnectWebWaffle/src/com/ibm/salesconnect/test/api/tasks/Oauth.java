/**
 * 
 */
package com.ibm.salesconnect.test.api.tasks;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.ApiBaseTest;
import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.API.TestDataHolder;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.common.HttpUtils;

/**
 * @author timlehane
 * @date Apr 2, 2014
 */
public class Oauth extends ApiBaseTest {
	private static final Logger log = LoggerFactory.getLogger(Oauth.class);
	
	@Parameters({ "apiExtension", "applicationName", "APIM", "apim_environment" })
	private Oauth(@Optional("leads") String apiExtension,
			@Optional("SC Auto create") String applicationName,
			@Optional("true") String APIM,
			@Optional("development") String environment) {
		super(apiExtension, applicationName, APIM, environment);
	}

	protected String testCaseName = "";

	static GETclientAndsecret callMethGET = new GETclientAndsecret();
	private static String retclientSecret = null;
	private static String retclientID = null;
	private TestDataHolder testData;
	
    public void addDataFile(String filePath){
        if(this.testData== null){
            this.testData = new TestDataHolder();
        }
         this.testData.addDataLocation(filePath, ";");
     }
	
	@DataProvider(name="jsonProvider")
    public Object[][] getTestData(){
	this.addDataFile("test_config/extensions/api/oauth.csv");
	return testData.getAllDataRows();
	}
	
	@Test(dataProvider = "jsonProvider")
	public void TestOauthKeySecret(HashMap<String,Object> parameterValues){
		Iterator<Map.Entry<String, Object>> it = parameterValues.entrySet().iterator();
		String c_key = "";
		String c_secret = "";
		String expected_response = "";
        
		log.info("Getting user.");		
        User user = commonUserAllocator.getUser(this);
		
		//Get values for c_key and c_secret
        while (it.hasNext()) 
        {
          Map.Entry<String, Object> pairs = (Map.Entry<String, Object>)it.next();
          if (pairs.getKey().equalsIgnoreCase("c_key")) {
        	  c_key = (String) pairs.getValue();
          }
          else if (pairs.getKey().equalsIgnoreCase("c_secret")) {
			  c_secret = (String) pairs.getValue();
          }
          else if (pairs.getKey().equalsIgnoreCase("expected_response")) {
			  expected_response = (String) pairs.getValue();
          }
        }
        
		String contentType = "application/json";
		String body = "{\"username\":\"" + user.getEmail() + "\",\"password\":\"" + user.getPassword() + "\",\"client_id\":\""+ c_key + "\",\"platform\":\"apiclient\",\"client_secret\":\""+ c_secret +"\",\"client_info\":{\"app\":{\"name\":\"nomad\",\"isNative\":false,\"version\":\"3.0.0\",\"build\":\"1\"},\"device\":{},\"browser\":{\"webkit\":true,\"version\":\"600.2.5\",\"userAgent\":\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_1) AppleWebKit/600.2.5 (KHTML, like Gecko) Version/8.0.2 Safari/600.2.5\"}},\"grant_type\":\"password\"}";
		
		
		String postResponseString = "";	
		HttpUtils restCalls = new HttpUtils();
		if (APIm){
			postResponseString = restCalls.postRequest(addclientIDAndSecret(getApiManagement()+"oauth"), GC.emptyArray, body, contentType, expected_response);
		}
		else {
			postResponseString = restCalls.postRequest(baseURL + GC.authURLExtension, GC.emptyArray, body, contentType, expected_response);
		}	
		
		JSONObject postResponse = null;
		
		try {
			//confirm the response is valid JSON
			postResponse = (JSONObject)new JSONParser().parse(postResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
			Assert.assertTrue(false, "Error with response from oauth request");
		}
		
		String token = "";
		
		try {
			//get the oauth token
			token = (String) postResponse.get("access_token");
		} catch (Exception e) {
			log.info("Token was not returned as expected");
			Assert.assertTrue(false, "Token was not returned as expected");
		}
		log.info("Token returned: " + token);
        
	}
	
	

	@Test
	public void Test_getOauthToken(){
		log.info("Start test Test_getOauthToken");
		log.info("Getting user.");		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");		
		LoginRestAPI loginRestAPI = new LoginRestAPI();

		if (APIm) {
			loginRestAPI.getOAuth2TokenViaAPIManager(getApiManagement() + getOAuthExtension(), user.getEmail(), user.getPassword(), "200");
		}
		else {
			loginRestAPI.getOAuth2Token(baseURL, user.getEmail(), user.getPassword());
		}
		log.info("End test Test_getOauthToken");
	}

	@Test
	public void Test_getOauthTokenInvalidUsername(){
		log.info("Start test Test_getOauthTokenInvalidUsername");
		log.info("Getting user.");		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		if (APIm) {
			loginRestAPI.getOAuth2TokenViaAPIManager(getApiManagement() + getOAuthExtension(), "invalid@ibm.com", user.getPassword(),"401");
		}
		else {
			loginRestAPI.getOAuth2Token(baseURL, "invalid@ibm.com", user.getPassword(), "401");
		}
		
		log.info("End test Test_getOauthTokenInvalidUsername");
	}
	
	@Test
	public void Test_getOauthTokenInvalidPassword(){
		log.info("Start test Test_getOauthTokenInvalidPassword");
		log.info("Getting user.");		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		if (APIm) {
			loginRestAPI.getOAuth2TokenViaAPIManager(getApiManagement() + getOAuthExtension(), user.getEmail(), "wrong password","401");
		}
		else {
			loginRestAPI.getOAuth2Token(baseURL, user.getEmail(), "wrong password", "401");
		}
		
		log.info("End test Test_getOauthTokenInvalidPassword");
	}

	@Test
	public void Test_wrongClientID(){
		log.info("Start test Test_wrongClientID");
		log.info("Getting user.");		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		if (APIm) {
		loginRestAPI.getOAuth2TokenViaAPIManager(getApiManagement() + "oauth?client_id=wrong"+"&clientSecret="+getclient_secret(), user.getEmail(), user.getPassword(),"401");
		}
		else {
			log.info("Test only valid for APIm");
		}
		log.info("End test Test_wrongClientID");
	}
	
	@Test
	public void Test_missingClientID(){
		log.info("Start test Test_missingClientID");
		log.info("Getting user.");		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		loginRestAPI.getOAuth2TokenViaAPIManager(getApiManagement() + "oauth?clientSecret="+getclient_secret(), user.getEmail(), user.getPassword(),"401");
		
		log.info("End test Test_missingClientID");
	}
	
	@Test
	public void Test_wrongClientSecret(){
		log.info("Start test Test_wrongClientSecret");
		log.info("Getting user.");		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		loginRestAPI.getOAuth2TokenViaAPIManager(getApiManagement() + "oauth?client_id="+getclient_ID()+"&clientSecret=wrong", user.getEmail(), user.getPassword(),"401");
		
		log.info("End test Test_wrongClientSecret");
	}
	
	@Test
	public void Test_missingClientSecret(){
		log.info("Start test Test_missingClientSecret");
		log.info("Getting user.");		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		loginRestAPI.getOAuth2TokenViaAPIManager(getApiManagement() + "oauth?client_id="+getclient_ID(), user.getEmail(), user.getPassword(),"401");
		
		log.info("End test Test_missingClientSecret");
	}
	
	@Test
	public void Test_getOauthTokenNoClientInfo(){
		log.info("Start test Test_getOauthTokenNoClientInfo");
		log.info("Getting user.");		
		User user = commonUserAllocator.getUser(this);
		String contentType = "application/json";
		
		HashMap<String,Object> hmap = new HashMap<String, Object>();
		hmap.put("grant_type", "password");
		hmap.put("username", user.getEmail());
		hmap.put("password", user.getPassword());
		hmap.put("client_id", "sugar");
		hmap.put("platform","apiclient");
		hmap.put("client_secret", "");
		
		JSONObject json = new JSONObject(hmap);
		log.info("Retrieving OAuth2Token.");		
		String postResponseString = "";
		HttpUtils restCalls = new HttpUtils();
		if (APIm){
			postResponseString = restCalls.postRequest(addclientIDAndSecret(getApiManagement()+"oauth"), GC.emptyArray, json, contentType, "200");
		}
		else {
			postResponseString = restCalls.postRequest(baseURL + GC.authURLExtension, GC.emptyArray, json, contentType, "200");
		}
		log.info("End test Test_getOauthTokenNoClientInfo");
	}
	
	@Test
	public void Test_getOauthTokenNoPlatformSpecified(){
		log.info("Start test Test_getOauthTokenNoPlatformSpecified");
		log.info("Getting user.");		
		User user = commonUserAllocator.getUser(this);
		String expectedResponse = "200";
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
		hmap.put("username", user.getEmail());
		hmap.put("password", user.getPassword());
		hmap.put("client_id", "sugar");
		hmap.put("client_secret", "");
		hmap.put("client_info", client_info);
		
		JSONObject json = new JSONObject(hmap);
		log.info("Request url: " + getApiManagement() + getOAuthExtension());
		log.info("Request body: " + json.toString());
		String postResponseString = "";
		HttpUtils restCalls = new HttpUtils();
		if (APIm){
			postResponseString = restCalls.postRequest(addclientIDAndSecret(getApiManagement()+"oauth"), GC.emptyArray, json, contentType, "401");
		}
		else {
			postResponseString = restCalls.postRequest(baseURL + GC.authURLExtension, GC.emptyArray, json, contentType, "401");
		}
		JSONObject postResponse = null;
		
			try {
				//confirm the response is valid JSON
				postResponse = (JSONObject)new JSONParser().parse(postResponseString);
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false,"Parsing exception from token request: " + postResponseString);
			}
		
		
		String token = "";
		
		try {
			//get the oauth token
			token = (String) postResponse.get("access_token");
		} catch (Exception e) {
			log.info("Token was not returned as expected");
			Assert.assertTrue(false);
		}
		log.info("Token returned: " + token);
		log.info("End test Test_getOauthTokenNoPlatformSpecified");
	}
	
	@Test
	public void Test_getOauthTokenBluemixClientIDSecret(){
		log.info("Start test Test_getOauthTokenBluemixClientIDSecret");
		log.info("Getting user.");		
		User user = commonUserAllocator.getUser(this);
		String expectedResponse = "200";
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
		hmap.put("username", user.getEmail());
		hmap.put("password", user.getPassword());
		hmap.put("client_id", "bluemix_apim");
		hmap.put("platform","apiclient");
		hmap.put("client_secret", "secret4BM2014-08-15");
		hmap.put("client_info", client_info);
		
		JSONObject json = new JSONObject(hmap);
		log.info("Request url: " + getApiManagement() + getOAuthExtension());
		log.info("Request body: " + json.toString());
		String postResponseString = "";
		HttpUtils restCalls = new HttpUtils();
		if (APIm){
			postResponseString = restCalls.postRequest(addclientIDAndSecret(getApiManagement()+"oauth"), GC.emptyArray, json, contentType, "200");
		}
		else {
			postResponseString = restCalls.postRequest(baseURL + GC.authURLExtension, GC.emptyArray, json, contentType, "200");
		}	
		
		JSONObject postResponse = null;
		
			try {
				//confirm the response is valid JSON
				postResponse = (JSONObject)new JSONParser().parse(postResponseString);
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false,"Parsing exception from token request: " + postResponseString);
			}
		
		
		String token = "";
		
		try {
			//get the oauth token
			token = (String) postResponse.get("access_token");
		} catch (Exception e) {
			log.info("Token was not returned as expected");
			Assert.assertTrue(false);
		}
		log.info("Token returned: " + token);
		log.info("End test Test_getOauthTokenBluemixClientIDSecret");
	}
	
	@Test
	public void Test_getOauthTokenBluemixClientIDWrongSecret(){
		log.info("Start test Test_getOauthTokenBluemixClientIDWrongSecret");
		log.info("Getting user.");		
		User user = commonUserAllocator.getUser(this);
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
		hmap.put("username", user.getEmail());
		hmap.put("password", user.getPassword());
		hmap.put("client_id", "bluemix_apim");
		hmap.put("platform","apiclient");
		hmap.put("client_secret", "INVALID");
		hmap.put("client_info", client_info);
		
		JSONObject json = new JSONObject(hmap);
		log.info("Request url: " + getApiManagement() + getOAuthExtension());
		log.info("Request body: " + json.toString());
		String postResponseString = "";
		HttpUtils restCalls = new HttpUtils();
		if (APIm){
			postResponseString = restCalls.postRequest(addclientIDAndSecret(getApiManagement()+"oauth"), GC.emptyArray, json, contentType, "400");
		}
		else {
			postResponseString = restCalls.postRequest(baseURL + GC.authURLExtension, GC.emptyArray, json, contentType, "400");
		}	
		JSONObject postResponse = null;
		
			try {
				//confirm the response is valid JSON
				postResponse = (JSONObject)new JSONParser().parse(postResponseString);
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false,"Parsing exception from token request: " + postResponseString);
			}
		
		
		String token = "";
		
		try {
			//get the oauth token
			token = (String) postResponse.get("access_token");
		} catch (Exception e) {
			log.info("Token was not returned as expected");
			Assert.assertTrue(false);
		}
		log.info("Token returned: " + token);
		log.info("End test Test_getOauthTokenBluemixClientIDWrongSecret");
	}


}
