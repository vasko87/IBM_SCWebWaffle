package com.ibm.salesconnect.test.mobile;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;

/**
<br/><br/>
 * Test to validate the oAuth2Token, User Related information, MetaData, Lables and Logout using LoginREST API
 * @author 
 * Srinidhi B Shridhar
<br/><br/>
 * */
public class LoginRestAPITests extends ProductBaseTest {
	
	Logger log = LoggerFactory.getLogger(LoginRestAPITests.class);

	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>	 
	 * <li>Login with a user </li>
	 * * <li>Get OauthToken</li>
	 * <li>Verify OauthToken as Expected</li>
	 * </ol>
	 */
	
	@Test(groups = {"", "API_AT"})
	public void Test_obtainOAuth2Token(){
		log.info("Start test method Test_obtainOAuth2Token.");
		log.info("Getting users");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Requesting OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		if (token.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "OauthToken not returned.");
		}
		
		log.info("End test method Test_obtainOAuth2Token.");
	}
	
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>	 
	 * <li>Login with a user </li>
	 * li>Get OauthToken</li>
	 * * <li>Get User related information</li>
	 * <li>Verify User related information as Expected</li>
	 * </ol>
	 */
	@Test(groups = {"MOBILE"})
	public void Test_obtainMe(){
		log.info("Start test method Test_obtainMe.");
		log.info("Getting users");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Requesting OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		String jsonString = loginRestAPI.getMe(testConfig.getBrowserURL(), token);
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "User preference retrieval failed.");
		} else {
			
			try {
				JSONObject response = (JSONObject)new JSONParser().parse(jsonString);
				JSONObject currentUser = (JSONObject) response.get("current_user");
				//validate the data was returned as expected
				Assert.assertNotNull((String) currentUser.get("full_name"), "Full name was not returned as expected.  ");
				Assert.assertNotNull((String) currentUser.get("id"), "User id was not returned as expected.  ");
				Assert.assertNotNull((JSONArray) currentUser.get("module_list"), "Module list was not returned as expected.  ");
				Assert.assertNotNull((JSONArray) currentUser.get("my_teams"), "Team list was not returned as expected.  ");
				Assert.assertNotNull((JSONObject) currentUser.get("preferences"), "User preferences were not returned as expected.  ");
				Assert.assertNotNull((String) currentUser.get("type"), "Type was not returned as expected.  ");
				Assert.assertNotNull((String) currentUser.get("user_name"), "User name was not returned as expected.  ");
			} catch (Exception e) {
				e.printStackTrace();
				Assert.assertTrue(false, "User data was not returned as expected.");
			}
			
			log.info("User data successfully returned");
			
		}
		
		log.info("End test method Test_obtainMe.");
	}
	
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>	 
	 * <li>Login with a user </li>
	 * li>Get OauthToken</li>
	 * * <li>Get MetaData</li>
	 * <li>Verify MetaData as Expected</li>
	 * </ol>
	 */
	@Test(groups = {"MOBILE"})
	public void Test_obtainMetadata(){
		log.info("Start test method Test_obtainMetadata.");
		log.info("Getting users");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Requesting OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		String jsonString = loginRestAPI.getMetadata(testConfig.getBrowserURL(), token);
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Metadata retrieval failed.");
		} else {
			
			try {
				JSONObject response = (JSONObject)new JSONParser().parse(jsonString);
				//validate the data was returned as expected
				Assert.assertNotNull((JSONObject) response.get("currencies"), "Currencies was not returned as expected.  ");
				Assert.assertNotNull((JSONObject) response.get("labels"), "Labels were not returned as expected.  ");
				Assert.assertNotNull((JSONObject) response.get("modules"), "Module list was not returned as expected.  ");
				Assert.assertNotNull((JSONObject) response.get("relationships"), "Relationships were not returned as expected.  ");
				Assert.assertNotNull((JSONObject) response.get("server_info"), "Server info was not returned as expected.  ");
			} catch (Exception e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Metadata was not returned as expected.");
			}
			
			log.info("Metadata successfully returned");
			
		}
		
		log.info("End test method Test_obtainMetadata.");
	}
	
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>	 
	 * <li>Login with a user </li>
	 * li>Get OauthToken</li>
	 * * <li>Get MetaData</li>
	 *  <li>Get Labels</li>
	 * <li>Verify Labels as Expected</li>
	 * </ol>
	 */
	
	@Test(groups = {"MOBILE"})
	public void Test_obtainLabels(){
		log.info("Start test method Test_obtainLabels.");
		log.info("Getting users");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Requesting OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		String jsonString = loginRestAPI.getMetadata(testConfig.getBrowserURL(), token);
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Metadata retrieval failed.");
		} else {
			
			String labelExtension = "";
			
			try {
				JSONObject response = (JSONObject)new JSONParser().parse(jsonString);
				JSONObject labelOptions = (JSONObject)response.get("labels");
				labelExtension = (String)labelOptions.get("en_us");
			} catch (Exception e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Problem retrieving label extension.");
			}
			
			log.info("Retrieving labels with extension " + labelExtension);
			
			String labelString = loginRestAPI.getLabels(testConfig.getBrowserURL(), token, labelExtension);
			
			try {
				JSONObject response = (JSONObject) new JSONParser().parse(labelString);
				Assert.assertNotNull((JSONObject) response.get("app_list_strings"), "App list strings not returned as expected.  ");
				Assert.assertNotNull((JSONObject) response.get("app_strings"), "App strings not returned as expected.  ");
				Assert.assertNotNull((JSONObject) response.get("mod_strings"), "Module strings not returned as expected.  ");
			} catch (Exception e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Labels not retrieved as expected.");
			}
			
			log.info("Labels successfully returned");
			
		}
		
		log.info("End test method Test_obtainLabels.");
	}
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>	 
	 * <li>Login with a user </li>
	 * * <li>Get OauthToken</li>
	 * <li>LogOut and Verify logged Out Successfully</li>
	 * </ol>
	 */
	@Test(groups = {"MOBILE"})
	public void Test_logout(){
		log.info("Start test method Test_logout.");
		log.info("Getting users");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Requesting OAuth2Token.");
		
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		String jsonString = loginRestAPI.logout(testConfig.getBrowserURL(), token);
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Logout unsuccessful.");
		} else {
			try {
				JSONObject response = (JSONObject) new JSONParser().parse(jsonString);
				Assert.assertTrue((Boolean) response.get("success"), "Logout did not have a successful response.  ");
			} catch (Exception e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Logout failed.");
			}
		}
		
		log.info("End test method Test_logout.");
	}
}
