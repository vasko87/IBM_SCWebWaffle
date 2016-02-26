package com.ibm.salesconnect.test.mobile;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.APIUtilities;
import com.ibm.salesconnect.API.CallRestAPI;
import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.API.SugarAPI;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;

/**
<br/><br/>
* Test to validate the create,read,delete,Related TO,Favorite and Unfavorite  functionality of the Call module using API
* <br/><br/>
* */
public class CallRestAPITests extends ProductBaseTest {
	
	public static final String callsURLExtension = "rest/v10/Calls";
	Logger log = LoggerFactory.getLogger(CallRestAPITests.class);
		
	@Test(groups = {"MOBILE"})
	public void Test_createCall(){
		log.info("Start test method Test_createCall.");
		
		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Create a New Call</li>
		 * <li>Verify Call Create as Expected</li>
		 * </ol>
		 */
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		log.info("Creating a call.");
		
		String subject = "A subject";
		String date = "2013-10-28T15:14:00.000Z";
		String status = "Held";
		String type = "outbound_call";
		String durationMin = "30";
		
		SugarAPI sugarAPI = new SugarAPI();
		String sessionID = sugarAPI.getSessionID(baseURL, user.getEmail(), user.getPassword());
		
		CallRestAPI callRestAPI = new CallRestAPI();
		String jsonString = callRestAPI.createCall(testConfig.getBrowserURL(), token, subject, date, status, type, durationMin, new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(), user.getEmail(), user.getPassword(), sessionID));
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Call creation failed.");
		} else {
			JSONObject postResponse = null;
			try {
				postResponse = (JSONObject)new JSONParser().parse(jsonString);
				postResponse.get("id");
			} catch (Exception e) {
				Assert.assertTrue(false, "Call was not created as expected.");
			}		
				//validate the call was created as expected
				Assert.assertEquals((String) postResponse.get("name"), subject, "Subject was not returned as expected.  ");
				Assert.assertEquals((String) postResponse.get("status"), status, "Status was not returned as expected.  ");
				Assert.assertEquals((String) postResponse.get("call_type"), type, "Type was not returned as expected.  ");
				Assert.assertEquals(Long.toString((Long) postResponse.get("duration_minutes")), durationMin, "Duration was not returned as expected.  ");
				Assert.assertEquals((String) postResponse.get("assigned_user_id"), new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword(),sessionID), "Assigned user id was not returned as expected.  ");

			
			log.info("Call successfully created.");
			
		}
		
		log.info("End test method Test_createCall.");
	}
	
	@Test(groups = {"MOBILE"})
	public void Test_relateNewContactToCall(){
		log.info("Start test method Test_relateNewContactToCall.");
		
		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Create a New Contact </li>
		 * <li>Verify Contact Created</li>
		 * * <li>Related to New Contact to Call</li>
		 * </ol>
		 */
		
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		String callId = CallRestAPITests.createCallHelper(user, log, testConfig.getBrowserURL(), this);
		
		String firstName = "Ron";
		String lastName = "Burgandy";
		String address = "US";
		
		CallRestAPI callRestAPI = new CallRestAPI();
		String jsonString = callRestAPI.relateNewContactToCall(testConfig.getBrowserURL(), token, callId, firstName, lastName, address, new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword()));
		log.info(jsonString);
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Relating Contact to Call failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			JSONObject relatedRecord = (JSONObject) postResponse.get("related_record");
			Assert.assertEquals((String) record.get("id"), callId, "Parent record was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Contact was not related to Call as expectd.");
		}
		
		log.info("End test method Test_relateNewContactToCall.");
		
	}
	
	@Test(groups = {"MOBILE"})
	public void Test_relateNewNoteToCall(){
		log.info("Start test method Test_relateNewNoteToCall.");
		
		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Create a New Note</li>
		 * <li>Verify Note Created as Expected</li>
		 * * <li>Related to New Created Note  to Call</li>
		 * </ol>
		 */
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		String callId = CallRestAPITests.createCallHelper(user, log, testConfig.getBrowserURL(), this);
		
		String name = "A Note";
		String description = "Related note";
		
		CallRestAPI callRestAPI = new CallRestAPI();
		String jsonString = callRestAPI.relateNewNoteToCall(testConfig.getBrowserURL(), token, callId, name, description, new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword()));
		log.info(jsonString);
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Relating Note to Call failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			Assert.assertEquals((String) record.get("id"), callId, "Parent record was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Note was not related to Call as expectd.");
		}
		
		log.info("End test method Test_relateNewNoteToCall.");
		
	}
	
	@Test(groups = {"MOBILE"})
	public void Test_relateNewTaskToCall(){
		log.info("Start test method Test_relateNewTaskToCall.");
		
		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Create New Task </li>
		 * <li>Verify Task Created as Expected</li>
		 * * <li>Related to New Task to Call</li>
		 * </ol>
		 */
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		String callId = CallRestAPITests.createCallHelper(user, log, testConfig.getBrowserURL(), this);
		
		String name = "Related Task";
		String dueDate = "2013-10-29T06:00:00.000Z";
		String priority = "High";
		String status = "Not Started";
		String callType = "Close_out_call";
		
		CallRestAPI callRestAPI = new CallRestAPI();
		String jsonString = callRestAPI.relateNewTaskToCall(testConfig.getBrowserURL(), token, callId, name, dueDate, priority, status, callType, new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword()));
		log.info(jsonString);
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Relating Task to Call failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			Assert.assertEquals((String) record.get("id"), callId, "Parent record was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Task was not related to Call as expectd.");
		}
		
		log.info("End test method Test_relateNewTaskToCall.");
		
	}
	
	@Test(groups = {"MOBILE"})
	public void Test_relateNewOpportunityToCall(){
		log.info("Start test method Test_relateNewOpportunityToCall.");
		
		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Search a Client From Cient Pool</li>
		 * <li>Create New Oppty</li>
		 * * <li>Related to New Oppty to Call</li>
		 * </ol>
		 */
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		String callId = CallRestAPITests.createCallHelper(user, log, testConfig.getBrowserURL(), this);
		
		//get a client from the client pool
		String clientId = commonClientAllocator.getGroupClient(GC.SC, this).getCCMS_ID();
		
		SugarAPI sugarAPI = new SugarAPI();
		String sessionID = sugarAPI.getSessionID(baseURL, user.getEmail(), user.getPassword());
		String realClientId = new APIUtilities().getClientBeanIDFromCCMSID(testConfig.getBrowserURL(), clientId, user.getEmail(), user.getPassword(),sessionID);
		
		String contactId = ContactRestAPITests.createContactHelper(user, log, testConfig.getBrowserURL());
		
		String desc = "A description";
		String source = "RLPL";
		String salesStage = "03";
		String date = "2016-10-28";
		
		CallRestAPI callRestAPI = new CallRestAPI();
		String jsonString = callRestAPI.relateNewOpportunityToCall(testConfig.getBrowserURL(), token, callId, desc, source, salesStage, date, realClientId, contactId, new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword(), sessionID));
		log.info(jsonString);
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Relating Opportunity to Call failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			Assert.assertEquals((String) record.get("id"), callId, "Parent record was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Opportunity was not related to Call as expectd.");
		}
		
		log.info("End test method Test_relateNewOpportunityToCall.");
		
	}
	
	@Test(groups = {"MOBILE"})
	public void Test_linkContactToCall(){
		log.info("Start test method Test_linkContactToCall.");
		
		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Create New Contact </li>
		 * <li>Verify Task Conatact created as Expected</li>
		 * * <li>Related to New Contact to Call</li>
		 * </ol>
		 */
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		String callId = CallRestAPITests.createCallHelper(user, log, testConfig.getBrowserURL(), this);
		
		//create a contact to link
		String contactId = ContactRestAPITests.createContactHelper(user, log, testConfig.getBrowserURL());
		
		CallRestAPI callRestAPI = new CallRestAPI();
		String jsonString = callRestAPI.linkRecordToCall(testConfig.getBrowserURL(), token, callId, contactId, "Contacts");
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Relating contact to call failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			//validate the contact was related to call as expected
			Assert.assertEquals((String) record.get("id"), callId, "Parent record was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Contact was not linked to Call as expectd.");
		}
		
		log.info("End test method Test_linkContactToCall.");
	}
	
	@Test(groups = {"MOBILE"})
	public void Test_linkNoteToCall(){
		log.info("Start test method Test_linkNoteToCall.");

		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Create New Note</li>
		 * <li>Verify Note Created as Expected</li>
		 * * <li>Related to New Note to Call</li>
		 * </ol>
		 */
		
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		String callId = CallRestAPITests.createCallHelper(user, log, testConfig.getBrowserURL(), this);
		
		//create a note to link
		String noteId = NoteRestAPITests.createNoteHelper(user, log, testConfig.getBrowserURL());
		
		CallRestAPI callRestAPI = new CallRestAPI();
		String jsonString = callRestAPI.linkRecordToCall(testConfig.getBrowserURL(), token, callId, noteId, "Notes");
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Relating note to call failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			//validate the note was related to call as expected
			Assert.assertEquals((String) record.get("id"), callId, "Parent record was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Note was not linked to Call as expectd.");
		}
		
		log.info("End test method Test_linkNoteToCall.");
	}
	
	@Test(groups = {"MOBILE"})
	public void Test_linkTaskToCall(){
		log.info("Start test method Test_linkTaskToCall.");
		

		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Search any Task </li>
		 * <li>Related to Taks to Call</li>
		 * </ol>
		 */
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		String callId = CallRestAPITests.createCallHelper(user, log, testConfig.getBrowserURL(), this);
		
		//create a task to link
		String taskId = TaskRestAPITests.createTaskHelper(user, log, testConfig.getBrowserURL());
		
		CallRestAPI callRestAPI = new CallRestAPI();
		String jsonString = callRestAPI.linkRecordToCall(testConfig.getBrowserURL(), token, callId, taskId, "Tasks");
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Linking task to call failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			//validate the task was related to call as expected
			Assert.assertEquals((String) record.get("id"), callId, "Parent record was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Task was not linked to Call as expectd.");
		}
		
		log.info("End test method Test_linkTaskToCall.");
	}
	
	@Test(groups = {"MOBILE"})
	public void Test_linkOpportunityToCall(){
		log.info("Start test method Test_linkOpportunityToCall.");
		

		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Create New Oppty  and Link to Call</li>
		 * <li>Verify Task Created as Expected</li>
		 * <li>Related to New Oppty to Call</li>
		 * <li>Verify Oppty Link to Call as Expected</li>
		 * </ol>
		 */
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
	
		String callId = CallRestAPITests.createCallHelper(user, log, testConfig.getBrowserURL(), this);
		
		//create an opportunity to link
		String opportunityId = OpportunityRestApiTests.createOpportunityHelper(user, log, testConfig.getBrowserURL(), this);
		
		log.info("Retrieving OAuth2Token.");
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());
		
		CallRestAPI callRestAPI = new CallRestAPI();
		String jsonString = callRestAPI.linkRecordToCall(testConfig.getBrowserURL(), token, callId, opportunityId, "Opportunity");
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Linking Opportunity to call failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			//validate the opportunity was related to call as expected
			Assert.assertEquals((String) record.get("id"), callId, "Parent record was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Opportunity was not linked to Call as expected.");
		}
		
		log.info("End test method Test_linkOpportunityToCall.");
	}
	
	@Test(groups = {"MOBILE"})
	public void Test_linkUserToCall(){
		log.info("Start test method Test_linkUserToCall.");
		
		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Retrieve New Assigne User</li>
		 * <li>Related to Assigned User  to Call</li>
		 * <li>Verify  USer Line to Call</li>
		 * </ol>
		 */
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		User user2 = commonUserAllocator.getUser(this);

		log.info("Retrieving additional assignees uid.");
		String userId = new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user2.getEmail(),user2.getPassword());
		
		log.info("Retrieving OAuth2Token.");
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		String callId = CallRestAPITests.createCallHelper(user, log, testConfig.getBrowserURL(), this);
		
		log.info("Relating user to call.");
		CallRestAPI callRestAPI = new CallRestAPI();
		String jsonString = callRestAPI.linkRecordToCall(testConfig.getBrowserURL(), token, callId, userId, "Users");
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Linking User to Call failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			//validate the opportunity was related to call as expected
			Assert.assertEquals((String) record.get("id"), callId, "Parent record was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "User was not linked to Call as expectd.");
		}
		
		log.info("End test method Test_linkUserToCall.");
	}
	
	@Test(groups = {"MOBILE"})
	public void Test_linkAdditionalAssigneeToCall(){
		log.info("Start test method Test_linkAdditionalAssigneeToCall.");
		
		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Retrieve AdditionalAssignee User</li>
		 * <li>Related to Additional Assigned User  to Call</li>
		 * <li>Verify  Additional Assignee Link to  Call as Expected</li>
		 * </ol>
		 */
		log.info("Getting users.");
		
		User user = commonUserAllocator.getUser(this);
		User user2 = commonUserAllocator.getUser(this);
		
		log.info("Retrieving additional assignees uid.");
		
		String userId = new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user2.getEmail(),user2.getPassword());
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());
		
		String callId = CallRestAPITests.createCallHelper(user, log, testConfig.getBrowserURL(), this);
		
		log.info("Relating user to call.");
		CallRestAPI callRestAPI = new CallRestAPI();
		String jsonString = callRestAPI.linkRecordToCall(testConfig.getBrowserURL(), token, callId, userId, "Assignee");
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Linking User to Call failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			//validate the opportunity was related to call as expected
			Assert.assertEquals((String) record.get("id"), callId, "Parent record was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "User was not linked to Call as expectd.");
		}
		
		log.info("End test method Test_linkAdditionalAssigneeToCall.");
	}
	
	@Test(groups = {"MOBILE"})
	public void Test_editCall(){
		log.info("Start test method Test_editCall.");
		
		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Creat New Call</li>
		 * <li>Verify Call Created As Expected</li>
		 * <li>Edit Created Call</li>
		 * * <li>Edit Created Call fileds " name ,status, subject....etc and Save</li>
		 * * <li>Verify Call Edited as Expected</li>
		 * </ol>
		 */
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		String callId = CallRestAPITests.createCallHelper(user, log, testConfig.getBrowserURL(), this);
		
		String subject = "A new subject";
		String date = "2013-10-28T15:14:00.000Z";
		String status = "Planned";
		String type = "outbound_call";
		String duration = "15";		
		
		CallRestAPI callRestAPI = new CallRestAPI();
		String jsonString = callRestAPI.editCall(testConfig.getBrowserURL(), token, callId, subject, date, status, type, duration, new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword()));
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Editing Call failed.");
		} 
		JSONObject postResponse = null;
		try {
			postResponse = (JSONObject)new JSONParser().parse(jsonString);
			postResponse.get("id");
		} catch (Exception e) {
			Assert.assertTrue(false, "Call was not edited as expected.");
		}
		
			//validate the call was edited as expected
			Assert.assertEquals((String) postResponse.get("name"), subject, "name was not returned as expected.  ");
			Assert.assertEquals((String) postResponse.get("status"), status, "status was not returned as expected.  ");
			Assert.assertEquals((String) postResponse.get("call_type"), type, "call type was not returned as expected.  ");
			Assert.assertEquals(Long.toString((Long) postResponse.get("duration_minutes")), duration, "Duration was not returned as expected.  ");

		
		log.info("End test method Test_editCall.");
	}
	
	@Test(groups = {"MOBILE"})
	public void Test_favoriteCall(){
		log.info("Start test method Test_favoriteCall.");
		
		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Search Call from the pool</li>
		 * <li>Convert the Call as Favorite Call </li>
		 * <li>Verify Call convert to Favorite Call</li>
		 * </ol>
		 */
		log.info("Getting user.");
		
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		String favorite = "favorite";
		String callId = CallRestAPITests.createCallHelper(user, log, testConfig.getBrowserURL(), this);	
		
		CallRestAPI callRestAPI = new CallRestAPI();
		String jsonString = callRestAPI.favoriteCall(testConfig.getBrowserURL(), token, callId, favorite, new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword()));
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "favoriting Call failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			String id = (String) postResponse.get("id");
			//validate the call was favorited as expected
			Assert.assertTrue((Boolean)postResponse.get("my_favorite"));
		} catch (Exception e) {
			Assert.assertTrue(false, "Call was mark as favorite as expected.");
		}
		
		log.info("End test method Test_favoriteCall.");
	}
	
	@Test(groups = {"MOBILE"})
	public void Test_unFavoriteCall(){
		log.info("Start test method Test_favoriteCall.");
		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Search Call from the pool</li>
		 * <li>Convert the Call as unFavorite Call</li>
		 * <li>Verify Call convert to unFavorite Call</li>
		 * </ol>
		 */
		
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		String favorite = "unfavorite";
		
		String callId = CallRestAPITests.createCallHelper(user, log, testConfig.getBrowserURL(), this);	
		
		CallRestAPI callRestAPI = new CallRestAPI();
		String jsonString = callRestAPI.favoriteCall(testConfig.getBrowserURL(), token, callId, favorite, new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword()));
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "unFavoriting Call failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			String id = (String) postResponse.get("id");
			//validate the call was unfavorited as expected
			Assert.assertFalse((Boolean)postResponse.get("my_favorite"));
		} catch (Exception e) {
			Assert.assertTrue(false, "Call was not marked as not favorite as expected.");
		}
		
		log.info("End test method Test_unFavoriteCall.");
	}
	
	
	@Test(groups = {"MOBILE"})
	public void Test_deleteCall(){
		log.info("Start test method Test_deleteCall.");
		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Search Call from the pool</li>
		 * <li>Delete the Call</li>
		 * <li>Verify Call Deleted Successfully</li>
		 * </ol>
		 */
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		String callId = CallRestAPITests.createCallHelper(user, log, testConfig.getBrowserURL(), this);	
		
		CallRestAPI callRestAPI = new CallRestAPI();
		String jsonString = callRestAPI.deleteCall(testConfig.getBrowserURL(), token, callId);
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Delete Call failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			//validate the call was deleted as expected
			Assert.assertEquals((String) postResponse.get("id"), callId, "Id was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Call was not deleted as expected.");
		}
		
		log.info("End test method Test_deleteCall.");
	}
	
	/*
	 * Call Helper
	 * -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	 */

	
	public static String createCallHelper(User user, Logger log, String url, Object self) {
        log.info("Creating a call.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(url, user.getEmail(), user.getPassword());
		
		String subject = "A subject";
		String date = "2013-10-28T15:14:00.000Z";
		String status = "Held";
		String type = "outbound_call";
		String durationMin = "30";

		
		CallRestAPI callRestAPI = new CallRestAPI();
		String jsonString = callRestAPI.createCall(url, token, subject, date, status, type, durationMin, new APIUtilities().getUserBeanIDFromEmail(url,user.getEmail(),user.getPassword()));
		
        String callId = "";
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			callId = (String) postResponse.get("id");
		} catch (Exception e) {
			Assert.assertTrue(false, "Call was not created as expected.");
		}
		
		return callId;
	}

}
