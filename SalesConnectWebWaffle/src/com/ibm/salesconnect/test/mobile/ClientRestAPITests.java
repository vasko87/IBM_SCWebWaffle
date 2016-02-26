package com.ibm.salesconnect.test.mobile;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.APIUtilities;
import com.ibm.salesconnect.API.ClientRestAPI;
import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.API.MeetingRestAPI;
import com.ibm.salesconnect.API.SugarAPI;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;

/**
 * <strong>Description:</strong>
 * <br/><br/>
 * Test to validate the create and read functionality of the Clients module using API
 * <br/><br/>
 * 
 * @author 
 * Christeena J Prabhu
 * 
 */

public class ClientRestAPITests extends ProductBaseTest {
		Logger log = LoggerFactory.getLogger(ClientRestAPITests.class);
		
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Search Client</li>
	 * <li>Create a New Call </li>
	 * <li>Relate New call to Client</li>
	 * </ol>
	 */
	@Test(groups = {"MOBILE"})
	public void Test_relateNewCallToClient(){
		log.info("Start test method Test_relateNewCallToClient.");
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		//get a client from the client pool
		String clientId = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();

		SugarAPI sugarAPI = new SugarAPI();
		String sessionID = sugarAPI.getSessionID(baseURL, user.getEmail(), user.getPassword());
		
		String realClientId = new APIUtilities().getClientBeanIDFromCCMSID(testConfig.getBrowserURL(), clientId, user.getEmail(), user.getPassword(),sessionID);
		
		String callSubject = "Call with my client.";
		String callDate = "2013-10-29T06:00:00.000Z";
		String callStatus = "Not Held";
		String callType = "face_to_face";
		String callDuration = "45";
		
		ClientRestAPI clientRestAPI = new ClientRestAPI();
		String jsonString = clientRestAPI.relateNewCallToClient(testConfig.getBrowserURL(), token, realClientId, callSubject, callDate, callStatus, callType, callDuration, new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword(),sessionID));
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Relating call to client failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			JSONObject relatedRecord = (JSONObject) postResponse.get("related_record");
			//validate the call was related to client as expected
			Assert.assertEquals((String) record.get("id"), realClientId, "Parent record was not returned as expected.  ");
			Assert.assertEquals((String) relatedRecord.get("parent_id"), realClientId, "Call was not related to client as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Call was not related to client as expectd.");
		}
		
		log.info("End test method Test_relateNewCallToClient.");
	}
	
	@Test(groups = {"MOBILE"})
	public void Test_relateNewNoteToClient(){
		log.info("Start test method Test_relateNewNoteToClient.");
		
		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Search Client</li>
		 * <li>Create a New Note </li>
		 * <li>Relate New Note to Client</li>
		 * </ol>
		 */
		
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		//get a client from the client pool
		String clientId = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();

		String sessionID = new SugarAPI().getSessionID(baseURL, user.getEmail(), user.getPassword());
		
		String realClientId = new APIUtilities().getClientBeanIDFromCCMSID(testConfig.getBrowserURL(), clientId, user.getEmail(), user.getPassword(),sessionID);
				
		String noteName = "A note.";
		
		ClientRestAPI clientRestAPI = new ClientRestAPI();
		String jsonString = clientRestAPI.relateNewNoteToClient(testConfig.getBrowserURL(), token, realClientId, noteName, new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword(),sessionID));
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Relating note to client failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			//validate the note was related to client as expected
			Assert.assertEquals((String) record.get("id"), realClientId, "Parent record was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Note was not related to client as expectd.");
		}
		
		log.info("End test method Test_relateNewNoteToClient.");
	}
	
	@Test(groups = {"MOBILE"})
	public void Test_relateNewTaskToClient(){
		log.info("Start test method Test_relateNewTaskToClient.");
		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Search Client</li>
		 * <li>Create a New Task </li>
		 * <li>Relate New Task to Client</li>
		 * </ol>
		 */
		
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		//get a client from the client pool
		String clientId = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
		String sessionID = new SugarAPI().getSessionID(baseURL, user.getEmail(), user.getPassword());
		String realClientId = new APIUtilities().getClientBeanIDFromCCMSID(testConfig.getBrowserURL(), clientId, user.getEmail(), user.getPassword(),sessionID);
		
		String taskName = "A task.";
		String taskDate = "2013-10-29T06:00:00.000Z";
		String taskPriority = "Medium";
		String taskStatus = "In Progress";
		String taskType = "Technical_Sales_Activity";
		
		ClientRestAPI clientRestAPI = new ClientRestAPI();
		String jsonString = clientRestAPI.relateNewTaskToClient(testConfig.getBrowserURL(), token, realClientId, taskName, taskDate, taskPriority, taskStatus, taskType, new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword(),sessionID));
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Relating task to client failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			JSONObject relatedRecord = (JSONObject) postResponse.get("related_record");
			//validate the task was related to client as expected
			Assert.assertEquals((String) record.get("id"), realClientId, "Parent record was not returned as expected.  ");
			Assert.assertEquals((String) relatedRecord.get("parent_id"), realClientId, "Task was not related to client as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Task was not related to client as expectd.");
		}
		
		log.info("End test method Test_relateNewTaskToClient.");
	}
	
	@Test(groups = {"MOBILE"})
	public void Test_relateNewContactToClient(){
		log.info("Start test method Test_relateNewContactToClient.");
		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Search Client</li>
		 * <li>Create a New Contact </li>
		 * <li>Relate New Contact to Client</li>
		 * </ol>
		 */
		
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		//get a client from the client pool
		String clientId = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
		String sessionID = new SugarAPI().getSessionID(baseURL, user.getEmail(), user.getPassword());
		String realClientId = new APIUtilities().getClientBeanIDFromCCMSID(testConfig.getBrowserURL(), clientId, user.getEmail(), user.getPassword(),sessionID);
				
		String contactFirstName = "Frodo";
		String contactLastName = "Baggins";
		String contactCountry = "AU";
		
		ClientRestAPI clientRestAPI = new ClientRestAPI();
		String jsonString = clientRestAPI.relateNewContactToClient(testConfig.getBrowserURL(), token, realClientId, contactFirstName, contactLastName, contactCountry, new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword(),sessionID));
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Relating contact to client failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			//validate the contact was related to client as expected
			Assert.assertEquals((String) record.get("id"), realClientId, "Parent record was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Contact was not related to client as expected.");
		}
		
		log.info("End test method Test_relateNewContactToClient.");
	}
	
	
	@Test(groups = {"MOBILE"})
	public void Test_relateNewOpportunityToClient(){
		log.info("Start test method Test_relateNewOpportunityToClient.");
		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Search Client</li>
		 * <li>Create a New Opportunity </li>
		 * <li>Relate New Opportunity to Client</li>
		 * </ol>
		 */
		
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		//get a client from the client pool
		String clientId = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
		String sessionID = new SugarAPI().getSessionID(baseURL, user.getEmail(), user.getPassword());
		String realClientId = new APIUtilities().getClientBeanIDFromCCMSID(testConfig.getBrowserURL(), clientId, user.getEmail(), user.getPassword(),sessionID);
				
		String contactId = ContactRestAPITests.createContactHelper(user, log, testConfig.getBrowserURL());
		
		String desc = "A description";
		String source = "RLPL";
		String salesStage = "03";
		String date = "2016-10-28";
		
		ClientRestAPI clientRestAPI = new ClientRestAPI();
		String jsonString = clientRestAPI.relateNewOpportunityToClient(testConfig.getBrowserURL(), token, realClientId, contactId, desc, source, salesStage, date, new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword(),sessionID));
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Relating opportunity to client failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			//validate the opportunity was related to client as expected
			Assert.assertEquals((String) record.get("id"), realClientId, "Parent record was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Opportunity was not related to client as expected.");
		}
		
		log.info("End test method Test_relateNewOpportunityToClient.");
	}
	
	@Test(groups = {"MOBILE"})
	public void Test_linkNoteToClient(){
		log.info("Start test method Test_linkNoteToClient.");
		
		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Search Client from the pool </li>
		 * <li>Create a New Note to link </li>
		 * <li>Link New Note to Client</li>
		 * </ol>
		 */
		
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		//get a client from the client pool
		String clientId = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();

		String realClientId = new APIUtilities().getClientBeanIDFromCCMSID(testConfig.getBrowserURL(), clientId, user.getEmail(), user.getPassword());
				
		//create a note to relate
		String noteId = NoteRestAPITests.createNoteHelper(user, log, testConfig.getBrowserURL());
		
		ClientRestAPI clientRestAPI = new ClientRestAPI();
		String jsonString = clientRestAPI.linkRecordToClient(testConfig.getBrowserURL(), token, realClientId, noteId, "Notes");
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Relating note to client failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			//validate the note was related to client as expected
			Assert.assertEquals((String) record.get("id"), realClientId, "Parent record was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Note was not related to client as expectd.");
		}
		
		log.info("End test method Test_linkNoteToClient.");
	}
	
	
	@Test(groups = {"MOBILE"})
	public void Test_linkContactToClient(){
		log.info("Start test method Test_linkContactToClient.");
		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Search Client from the pool</li>
		 * <li>Create a Contact to link </li>
		 * <li>Link New Contact to Client</li>
		 * </ol>
		 */
		
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		//get a client from the client pool
		String clientId = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();

		String realClientId = new APIUtilities().getClientBeanIDFromCCMSID(testConfig.getBrowserURL(), clientId, user.getEmail(), user.getPassword());

		//create a contact to relate
		String contactId = ContactRestAPITests.createContactHelper(user, log, testConfig.getBrowserURL());
		
		ClientRestAPI clientRestAPI = new ClientRestAPI();
		String jsonString = clientRestAPI.linkRecordToClient(testConfig.getBrowserURL(), token, realClientId, contactId, "Contacts");
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Relating contact to client failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			//validate the contact was related to client as expected
			Assert.assertEquals((String) record.get("id"), realClientId, "Parent record was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Contact was not related to client as expectd.");
		}
		
		log.info("End test method Test_linkContactToClient.");
	}
	
	
	
	@Test(groups = {"MOBILE"})
	public void Test_linkCallToClient(){
		log.info("Start test method Test_linkCallToClient.");
		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Search Client from the pool</li>
		 * <li>Create a Call to link </li>
		 * <li>Link New Call to Client</li>
		 * </ol>
		 */		
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		//get a client from the client pool
		String clientId = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();

		String realClientId = new APIUtilities().getClientBeanIDFromCCMSID(testConfig.getBrowserURL(), clientId, user.getEmail(), user.getPassword());

		//create a call to relate
		String callId = CallRestAPITests.createCallHelper(user, log, testConfig.getBrowserURL(), this);
		
		ClientRestAPI clientRestAPI = new ClientRestAPI();
		String jsonString = clientRestAPI.linkRecordToClient(testConfig.getBrowserURL(), token, realClientId, callId, "Calls");
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Relating call to client failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			//validate the call was related to opportunity as expected
			Assert.assertEquals((String) record.get("id"), realClientId, "Parent record was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Call was not related to client as expectd.");
		}
		
		log.info("End test method Test_linkCallToClient.");
	}
	
	@Test(groups = {"MOBILE"})
	public void Test_linkTaskToClient(){
		log.info("Start test method Test_linkTaskToClient.");
		
		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Search Client from the pool</li>
		 * <li>Create a Task to link </li>
		 * <li>Link New Task to Client</li>
		 * </ol>
		 */
		
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		//get a client from the client pool
		String clientId = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();

		String realClientId = new APIUtilities().getClientBeanIDFromCCMSID(testConfig.getBrowserURL(), clientId, user.getEmail(), user.getPassword());

		//create a task to relate
		String taskId = TaskRestAPITests.createTaskHelper(user, log, testConfig.getBrowserURL());
		
		ClientRestAPI clientRestAPI = new ClientRestAPI();
		String jsonString = clientRestAPI.linkRecordToClient(testConfig.getBrowserURL(), token, realClientId, taskId, "Tasks");
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Relating task to client failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			//validate the task was related to client as expected
			Assert.assertEquals((String) record.get("id"), realClientId, "Parent record was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Task was not related to client as expectd.");
		}
		
		log.info("End test method Test_linkTaskToClient.");
	}
	

	
	
		@Test(groups={"MOBILE"})
	public void Test_linkMeetingToClient()
{
		log.info("Start test method test_linkMeetingtoClient");
		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Search Client from the pool</li>
		 * <li>Create a Meeting to link </li>
		 * <li>Link Meeting to Client</li>
		 * </ol>
		 */
		
		log.info("Getting user");
		User user=commonUserAllocator.getUser();
		
		LoginRestAPI loginRestAPI=new LoginRestAPI();
		
		String Token=loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(),user.getEmail(),user.getPassword());
		
		SugarAPI sugarAPI = new SugarAPI();
		String sessionID = sugarAPI.getSessionID(baseURL, user.getEmail(), user.getPassword());
		
		String clientId=commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
		String assignedUserID = new APIUtilities().getUserBeanIDFromEmail(baseURL, user.getEmail(), user.getPassword(), sessionID);	
		
		
		String readlClientId=new APIUtilities().getClientBeanIDFromCCMSID(testConfig.getBrowserURL(), clientId, user.getEmail(), user.getPassword(), sessionID);
		log.info("relating meeting to client");
		
		ClientRestAPI clientRestAPI=new ClientRestAPI();
		
		MeetingRestAPI meeting=new MeetingRestAPI();
		
		String meetingid=meeting.createMeetingreturnBean(testConfig.getBrowserURL(), Token, assignedUserID);
				
		String jsonString= clientRestAPI.linkRecordToClient(testConfig.getBrowserURL(), Token, readlClientId, meetingid, "Meetings");
		
		if(jsonString.equalsIgnoreCase("false"))
		{
			Assert.assertTrue(false,"meeting was not linked to client");
		}
		try
		{
			JSONObject postResponse=(JSONObject)new JSONParser().parse(jsonString);
			JSONObject record=(JSONObject)postResponse.get("record");
			Assert.assertEquals((String)record.get("id"),readlClientId,"Parent Record was not returned as expected");
		}
		catch(Exception e)
		{
			Assert.assertTrue(false,"meeting was not linked with client as expected");
		}
		log.info("End test Method test_linkMeetingToClient");
		}
	
	@Test(groups = {"MOBILE"})
	public void Test_favoriteClient(){
		log.info("Start test method Test_favoriteClient.");
		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Search Client from the pool</li>
		 * <li>Convert the client as Favorite Client </li>
		 * <li>Verify Client convert to Favorite Client</li>
		 * </ol>
		 */
		
		log.info("Getting user.");
		User user = commonUserAllocator.getUser(this);
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());
		String favorite = "favorite";
		
		//get a client from the client pool
		String clientId = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();

		String sessionID = new SugarAPI().getSessionID(baseURL, user.getEmail(), user.getPassword());
		
		//borrow the collab web APIs to get the bean id for the pool client
		String realClientId = new APIUtilities().getClientBeanIDFromCCMSID(testConfig.getBrowserURL(), clientId, user.getEmail(), user.getPassword(),sessionID);
		
		ClientRestAPI clientRestAPI = new ClientRestAPI();
		String jsonString = clientRestAPI.favoriteClient(testConfig.getBrowserURL(), token, realClientId, favorite, new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword(),sessionID));
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "favoriting Client failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			String id = (String) postResponse.get("id");
			//validate the client was favorited as expected
			Assert.assertTrue((Boolean)postResponse.get("my_favorite"));
		} catch (Exception e) {
			Assert.assertTrue(false, "Client was not marked as favorite as expected.");
		}
		
		log.info("End test method Test_favoriteClient.");
	}
	
	@Test(groups = {"MOBILE"})
	public void Test_unFavoriteClient(){
		log.info("Start test method Test_unFavoriteClient.");
		
		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Search Client from the pool</li>
		 * <li>Convert the client as unFavorite Client </li>
		 * <li>Verify Client convert to unFavorite Client</li>
		 * </ol>
		 */
		
		log.info("Getting user.");
		User user = commonUserAllocator.getUser(this);
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		//get a client from the client pool
		String clientId = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
		String sessionID = new SugarAPI().getSessionID(baseURL, user.getEmail(), user.getPassword());
		String realClientId = new APIUtilities().getClientBeanIDFromCCMSID(testConfig.getBrowserURL(), clientId, user.getEmail(), user.getPassword(),sessionID);
		
		String favorite = "unfavorite";
		
		ClientRestAPI clientRestAPI = new ClientRestAPI();
		String jsonString = clientRestAPI.favoriteClient(testConfig.getBrowserURL(), token, realClientId, favorite, new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword(), sessionID));
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "unfavoriting Client failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			String id = (String) postResponse.get("id");
			Assert.assertFalse((Boolean)postResponse.get("my_favorite"));
		} catch (Exception e) {
			Assert.assertTrue(false, "Client was not marked as unFavorite as expected.");
		}
		
		log.info("End test method Test_unFavoriteClient.");
	}
	
	
	/*@Test(groups = {"MOBILE"})
	public void Test_linkUserToClient(){
		log.info("Start test method Test_linkUserToClient.");
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		User user2 = commonUserAllocator.getUser(this);
		String userId = user2.getUid();
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		//get a client from the client pool
		String clientId = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
		String headers[]={"OAuth-Token", token};
		//borrow the collab web APIs to get the bean id for the pool client
		String realClientId = new CollabWebAPI().getbeanIDfromClientID(testConfig.getBrowserURL(), clientId, headers);	
		
		ClientRestAPI clientRestAPI = new ClientRestAPI();
		String jsonString = clientRestAPI.linkRecordToClient(testConfig.getBrowserURL(), token, realClientId, userId, "Users");
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Linking User to client failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			//validate the user was related to client as expected
			Assert.assertEquals((String) record.get("id"), realClientId, "Parent record was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "User was not linked to client as expectd.");
		}
		
		log.info("End test method Test_linkUserToClient.");
	}*/
	

}
