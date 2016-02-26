/**
 * 
 */
package com.ibm.salesconnect.test.api.opportunities;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.common.HttpUtils;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;

/**
 * @author timlehane
 * @date Nov 5, 2014
 */
public class GetOpportunites extends ProductBaseTest {
	private static final Logger log = LoggerFactory.getLogger(GetOpportunites.class);
	private static String clientIdAndSecret = "client_id=b8dd9731-c359-409b-ba53-564e6e197a86&client_secret=V3kL7kV4wH5gE0wP7rG4sI7jX5yB8hH7wI7uB5gB8qA0oT4jR8";
	//t1private static String clientIdAndSecret = "client_id=acb10a8e-03c6-4cc2-a5f4-fbce6f0f0976&client_secret=H3uN7aK0nD7xP4mY0aG7bY4uJ3wS5eP2iP0yI0bU2mV8vT5dH3";

	@Test
	public void Test_GetOpptyfavourites(){
		log.info("Start test Test_GetOpptyfavourites");
		User user = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
		String response = sendRequest(getApiManagement() + "opportunities?favorites=1&" + clientIdAndSecret, user, "200");
		
		JSONObject postResponse = null;
		try {
			postResponse = (JSONObject)new JSONParser().parse(response);
		} catch (ParseException e5) {
			e5.printStackTrace();
		}
		
		JSONArray jsonArray = null;
		try {
			jsonArray = (JSONArray) postResponse.get("records");
		} catch (Exception e) {
			log.info("Token was not returned as expected");
			e.printStackTrace();
		}

		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			Assert.assertEquals(jsonObject.get("my_favorite"), true);

		}

		log.info("End test Test_GetOpptyfavourites");
	}
	
	@Test
	public void Test_GetOpptyMyItems(){
		log.info("Start test Test_GetOpptyMyItems");
		User user = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
		String response = sendRequest(getApiManagement() + "opportunities?my_items=1&" + clientIdAndSecret, user, "200");
		
		JSONObject postResponse = null;
		try {
			postResponse = (JSONObject)new JSONParser().parse(response);
		} catch (ParseException e5) {
			e5.printStackTrace();
		}
		
		JSONArray jsonArray = null;
		try {
			jsonArray = (JSONArray) postResponse.get("records");
		} catch (Exception e) {
			log.info("Token was not returned as expected");
			e.printStackTrace();
		}

		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			String temp = (String) jsonObject.get("assigned_user_name");
			Assert.assertTrue(temp.contains(user.getFirstName()));
		}
		
		log.info("End test Test_GetOpptyMyItems");
	}

	@Test
	public void Test_GetOpptyMyItemsFavorites(){
		log.info("Start test Test_GetOpptyMyItemsFavorites");
		User user = commonUserAllocator.getUser(this);
		String response = sendRequest(getApiManagement() + "opportunities?my_items=1&favorites=1&" + clientIdAndSecret, user, "200");
		
		JSONObject postResponse = null;
		try {
			postResponse = (JSONObject)new JSONParser().parse(response);
		} catch (ParseException e5) {
			e5.printStackTrace();
		}
		
		JSONArray jsonArray = null;
		try {
			jsonArray = (JSONArray) postResponse.get("records");
		} catch (Exception e) {
			log.info("Token was not returned as expected");
			e.printStackTrace();
		}

		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			Assert.assertEquals(jsonObject.get("my_favorite"), true);
			String temp = (String) jsonObject.get("assigned_user_name");
			Assert.assertTrue(temp.contains(user.getFirstName()));
		}
		
		log.info("End test Test_GetOpptyMyItemsFavorites");
	}
	
	@Test
	public void Test_GetOpptyMyItemsOneField(){
		log.info("Start test Test_GetOpptyMyItemsOneField");
		User user = commonUserAllocator.getUser(this);
		String response = sendRequest(getApiManagement() + "opportunities?my_items=1&fields=date_entered&" + clientIdAndSecret, user, "200");

		Assert.assertFalse(response.contains("modified_user_id"));
		Assert.assertTrue(response.contains("date_entered"));
		
		log.info("End test Test_GetOpptyMyItemsOneField");
	}
	
	@Test
	public void Test_GetOpptyMyItemsTwoFields(){
		log.info("Start test Test_GetOpptyMyItemsTwoFields");
		User user = commonUserAllocator.getUser(this);
		String response = sendRequest(getApiManagement() + "opportunities?my_items=1&fields=date_entered,modified_user_id&" + clientIdAndSecret, user, "200");

		Assert.assertFalse(response.contains("modified_by_name"));
		Assert.assertTrue(response.contains("date_entered"));
		Assert.assertTrue(response.contains("modified_user_id"));
		
		log.info("End test Test_GetOpptyMyItemsTwoFields");
	}
	
	
	private String sendRequest(String url, User user, String expectedResponse){
		
		log.info("Retrieving OAuth2Token.");		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String headers[] = {"OAuth-Token", loginRestAPI.getOAuth2TokenViaAPIManager(getApiManagement() + getOAuthExtension(), user.getEmail(), user.getPassword(),"200")};
		
		log.info("Sending GET request");
		HttpUtils httpUtils = new HttpUtils();
		String responseString = httpUtils.getRequest(url, headers, expectedResponse);
		log.info("Response String "+ responseString);
		try {
			//check for a valid JSON response
			new JSONParser().parse(responseString);
		} catch (ParseException e) {
			e.printStackTrace();
			Assert.assertTrue(false, "Parse exception with get response");
		}
		
		return responseString;
	}
	
	public String getOAuthExtension(){		
		return "oauth?" + clientIdAndSecret;
	}
}
