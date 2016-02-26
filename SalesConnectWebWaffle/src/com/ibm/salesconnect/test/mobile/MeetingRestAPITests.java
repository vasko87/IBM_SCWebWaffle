package com.ibm.salesconnect.test.mobile;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.APIUtilities;
import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.API.MeetingRestAPI;
import com.ibm.salesconnect.API.SugarAPI;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;

/**
<br/><br/>
 * Test to validate the create,read,delete,Related TO,Link ,Create Meeting Helper,Favorite and Unfavorite functionality of the Meeting module using API
 * @author 
 * Srinidhi B Shridhar
<br/><br/>
 * */
public class MeetingRestAPITests extends ProductBaseTest {

	public static final String meetingsURLExtension = "rest/v10/Meetings";
	Logger log = LoggerFactory.getLogger(MeetingRestAPITests.class);
	
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>	 
	 * <li>Create a New Meeting </li>
	 * * <li>Search Meeting</li>
	 * <li>Verify Meeting Created as Expected</li>
	 * </ol>
	 */
	
		@Test(groups = {"MOBILE"})
		public void Test_createmeeting(){
			log.info("Start test method Test_createmeeting.");
			log.info("Getting user.");
			
			User user = commonUserAllocator.getUser(this);
			
			log.info("Retrieving OAuth2Token.");
			
			LoginRestAPI loginRestAPI = new LoginRestAPI();
			String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

			log.info("Creating a meeting.");

			String subject = "A Subject";
			String description = "The meeting.";
			String duration_hours = "1";
			String duration_minutes = "15";
			
			Date startDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");						
			String start_date = sdf.format(startDate).toString();
			String sessionID = new SugarAPI().getSessionID(baseURL, user.getEmail(), user.getPassword());
			MeetingRestAPI meetingRestAPI = new MeetingRestAPI();
			String jsonString = meetingRestAPI.createMeeting(testConfig.getBrowserURL(), token, subject, description, start_date, duration_hours, duration_minutes, new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword(),sessionID));
			
			if (jsonString.equalsIgnoreCase("false")) {
				Assert.assertTrue(false, "meeting creation failed.");
			} else {
				
				try {
					JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
					//validate the meeting was created as expected
					Assert.assertEquals((String) postResponse.get("name"), subject, "Subject was not returned as expected.  ");
					Assert.assertEquals((String) postResponse.get("description"), description, "Description was not returned as expected.  ");
					Assert.assertEquals((String) postResponse.get("assigned_user_id"), new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword(),sessionID), "Assigned user id was not returned as expected.  ");
				} catch (Exception e) {
					Assert.assertTrue(false, "meeting was not created as expected.");
				}
				
				log.info("meeting successfully created.");
				
			}
			
			log.info("End test method Test_createmeeting.");
		}
		
		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Create a New Meeting </li>
		 * <li>Related to New Meeting to Contact</li>
		 * <li>Verify New Meeting is related to Contact</li>
		 * </ol>
		 */
		
		@Test(groups = {"MOBILE"})
		public void Test_relateNewContactToMeeting(){
			log.info("Start test method Test_relateNewContactToMeeting.");
			log.info("Getting user.");
			
			User user = commonUserAllocator.getUser(this);
			
			log.info("Retrieving OAuth2Token.");
			
			LoginRestAPI loginRestAPI = new LoginRestAPI();
			String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

			String meetingId = MeetingRestAPITests.createmeetingHelper(user, log, testConfig.getBrowserURL(), this);
			
			String firstName = "Ron";
			String lastName = "Burgandy";
			String address = "US";
			
			MeetingRestAPI meetingRestAPI = new MeetingRestAPI();
			String jsonString = meetingRestAPI.relateNewContactToMeeting(testConfig.getBrowserURL(), token, meetingId, firstName, lastName, address, new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword()));
			log.info(jsonString);
			if (jsonString.equalsIgnoreCase("false")) {
				Assert.assertTrue(false, "Relating Contact to task failed.");
			} 
			
			try {
				JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
				JSONObject record = (JSONObject)postResponse.get("record");
				Assert.assertEquals((String) record.get("id"), meetingId, "Parent record was not returned as expected.  ");
			} catch (Exception e) {
				Assert.assertTrue(false, "Contact was not related to meeting as expectd.");
			}
			
			log.info("End test method Test_relateNewContactToMeeting.");
			
		}
		
		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Create a New Meeting </li>
		 * <li>Related to New Meeting to Opportunity</li>
		 * <li>Verify New Meeting is related to Opportunity</li>
		 * </ol>
		 */
		@Test(groups = {"MOBILE"})
		public void Test_relateNewOpportunityToMeeting(){
			log.info("Start test method Test_relateNewOpportunityToMeeting.");
			log.info("Getting user.");
			
			User user = commonUserAllocator.getUser(this);
						
			log.info("Retrieving OAuth2Token.");
			
			LoginRestAPI loginRestAPI = new LoginRestAPI();
			String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

			String meetingId = MeetingRestAPITests.createmeetingHelper(user, log, testConfig.getBrowserURL(), this);
			
			//get a client from the client pool
			String clientId = commonClientAllocator.getGroupClient(GC.SC, this).getCCMS_ID();
			String sessionID = new SugarAPI().getSessionID(baseURL, user.getEmail(), user.getPassword());
			String realClientId = new APIUtilities().getClientBeanIDFromCCMSID(testConfig.getBrowserURL(), clientId, user.getEmail(), user.getPassword(),sessionID);
			String contactId = ContactRestAPITests.createContactHelper(user, log, testConfig.getBrowserURL());
			
			String desc = "A description";
			String source = "RLPL";
			String salesStage = "03";
			String date = "2016-10-28";
			
			MeetingRestAPI meetingRestAPI = new MeetingRestAPI();
			log.debug("Relating a new oppty to a meeting");
			String jsonString = meetingRestAPI.relateNewOpportunityToMeeting(testConfig.getBrowserURL(), token, meetingId, desc, source, salesStage, date, realClientId, contactId, new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword(),sessionID));
			log.info(jsonString);
			if (jsonString.equalsIgnoreCase("false")) {
				Assert.assertTrue(false, "Relating Opportunity to meeting failed.");
			} 
			
			try {
				JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
				JSONObject record = (JSONObject)postResponse.get("record");
				Assert.assertEquals((String) record.get("id"), meetingId, "Parent record was not returned as expected.  ");
			} catch (Exception e) {
				Assert.assertTrue(false, "Opportunity was not related to meeting as expectd.");
			}
			
			log.info("End test method Test_relateNewOpportunityToMeeting.");
			
		}
		
		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Create a New Meeting </li>
		 * <li>Related to New Meeting to Note</li>
		 * <li>Verify New Meeting is related to Note</li>
		 * </ol>
		 */
		
		@Test(groups = {"MOBILE"})
		public void Test_relateNewNoteToMeeting(){
			log.info("Start test method Test_relateNewNoteToMeeting.");
			log.info("Getting user.");
			
			User user = commonUserAllocator.getUser(this);
			
			log.info("Retrieving OAuth2Token.");
			
			LoginRestAPI loginRestAPI = new LoginRestAPI();
			String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

			String meetingId = MeetingRestAPITests.createmeetingHelper(user, log, testConfig.getBrowserURL(), this);
			
			String name = "A Note";
			String description = "Related note";
			
			MeetingRestAPI meetingRestAPI = new MeetingRestAPI();
			String jsonString = meetingRestAPI.relateNewNoteToMeeting(testConfig.getBrowserURL(), token, meetingId, name, description, new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword()));
			log.info(jsonString);
			if (jsonString.equalsIgnoreCase("false")) {
				Assert.assertTrue(false, "Relating Note to meeting failed.");
			} 
			
			try {
				JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
				JSONObject record = (JSONObject)postResponse.get("record");
				Assert.assertEquals((String) record.get("id"), meetingId, "Parent record was not returned as expected.  ");
			} catch (Exception e) {
				Assert.assertTrue(false, "Note was not related to meeting as expectd.");
			}
			
			log.info("End test method Test_relateNewNoteToMeeting.");
			
		}
		
		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Search Meeting from the pool </li>
		 * <li>Create a New User to link </li>
		 * <li>Link New User to Meeting</li>
		 * <li>Verify User to Meeting is Linked successfully </li>
		 * </ol>
		 */
	
		@Test(groups={"MOBILE"})
		public void Test_linkUserToMeeting()
		{
		
				log.info("Start test method Test_linkUserToMeeting");
				log.info("Getting user.");
				
				User user = commonUserAllocator.getUser(this);
				User user2 = commonUserAllocator.getUser(this);

				log.info("Retrieving additional assignees uid.");
				String userId = new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user2.getEmail(),user2.getPassword());
				
				log.info("Retrieving OAuth2Token.");
				LoginRestAPI loginRestAPI = new LoginRestAPI();
				String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

				String meetingId = MeetingRestAPITests.createmeetingHelper(user, log, testConfig.getBrowserURL(), this);
				
				log.info("Relating user to call.");
				MeetingRestAPI meetingRestAPI = new MeetingRestAPI();
				String jsonString = meetingRestAPI.linkRecordToMeeting(testConfig.getBrowserURL(), token,meetingId, userId, "Users");
				
				if (jsonString.equalsIgnoreCase("false")) {
					Assert.assertTrue(false, "Linking User to Call failed.");
				} 
				
				try {
					JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
					JSONObject record = (JSONObject)postResponse.get("record");
					//validate the meeting was related to call as expected
					Assert.assertEquals((String) record.get("id"), meetingId, "Parent record was not returned as expected.  ");
				} catch (Exception e) {
					Assert.assertTrue(false, "User was not linked to meeting as expectd.");
				}
				
				log.info("End test method Test_linkUserToMeeting.");
			}
		
		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Search Meeting from the pool </li>
		 * <li>Create a New Contact to link </li>
		 * <li>Link New Contact to Meeting</li>
		 * <li>Verify Contact to Meeting is Linked successfully </li>
		 * </ol>
		 */
		
		@Test(groups = {"MOBILE"})
		public void Test_linkContactToMeeting(){
			log.info("Start test method Test_linkContactToMeeting.");
			log.info("Getting user.");
			
			User user = commonUserAllocator.getUser(this);
			
			log.info("Retrieving OAuth2Token.");
			
			LoginRestAPI loginRestAPI = new LoginRestAPI();
			String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

			String meetingId = MeetingRestAPITests.createmeetingHelper(user, log, testConfig.getBrowserURL(), this);
			
			//create a contact to link
			String contactId = ContactRestAPITests.createContactHelper(user, log, testConfig.getBrowserURL());
			
			MeetingRestAPI meetingRestAPI = new MeetingRestAPI();
			String jsonString = meetingRestAPI.linkRecordToMeeting(testConfig.getBrowserURL(), token, meetingId, contactId, "Contacts");
			
			if (jsonString.equalsIgnoreCase("false")) {
				Assert.assertTrue(false, "Relating contact to meeting failed.");
			} 
			
			try {
				JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
				JSONObject record = (JSONObject)postResponse.get("record");
				//validate the contact was related to meeting as expected
				Assert.assertEquals((String) record.get("id"), meetingId, "Parent record was not returned as expected.  ");
			} catch (Exception e) {
				Assert.assertTrue(false, "Contact was not linked to meeting as expectd.");
			}
			
			log.info("End test method Test_linkContactToMeeting.");
		}
		
		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Create a New Meeting </li>
		 * <li>Related to New Meeting to Task</li>
		 * <li>Verify New Meeting is related to Task</li>
		 * </ol>
		 */
		@Test(groups = {"MOBILE"})
		public void Test_relateNewTaskToMeeting(){
			log.info("Start test method Test_relateNewTaskToMeeting.");
			log.info("Getting user.");
			
			User user = commonUserAllocator.getUser(this);
			
			log.info("Retrieving OAuth2Token.");
			
			LoginRestAPI loginRestAPI = new LoginRestAPI();
			String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

			String meetingId = MeetingRestAPITests.createmeetingHelper(user, log, testConfig.getBrowserURL(), this);
			
			String taskName = "A task.";
			String taskDate = "2013-10-29T06:00:00.000Z";
			String taskPriority = "Medium";
			String taskStatus = "In Progress";
			String taskType = "Technical_Sales_Activity";
			
			MeetingRestAPI meetingRestAPI = new MeetingRestAPI();
			String jsonString = meetingRestAPI.relateNewTaskToMeeting(testConfig.getBrowserURL(), token, meetingId, taskName, taskDate, taskPriority, taskStatus, taskType, 
					new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword()));
			
			if (jsonString.equalsIgnoreCase("false")) {
				Assert.assertTrue(false, "Relating task to meeting failed.");
			} 
			
			try {
				JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
				JSONObject record = (JSONObject)postResponse.get("record");
				JSONObject relatedRecord = (JSONObject) postResponse.get("related_record");
				//validate the task was related to meeting as expected
				Assert.assertEquals((String) record.get("id"), meetingId, "Parent record was not returned as expected.  ");
				Assert.assertEquals((String) relatedRecord.get("parent_id"), meetingId, "Task was not related to meeting as expected.  ");
			} catch (Exception e) {
				Assert.assertTrue(false, "Task was not related to meeting as expectd.");
			}
			
			log.info("End test method Test_relateNewTaskToMeeting.");
		}
		
		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Search Opportunity from the pool </li>
		 * <li>Create a New Opportunity to Meeting </li>
		 * <li>Link New Opportunity to Meeting</li>
		 * <li>Verify Opportunity to Meeting is Linked successfully </li>
		 * </ol>
		 */
		
		@Test(groups = {"MOBILE"})
		public void Test_linkOpportunityToMeeting(){
			log.info("Start test method Test_linkOpportunityToMeeting.");
			log.info("Getting user.");
			
			User user = commonUserAllocator.getUser(this);
			
			log.info("Retrieving OAuth2Token.");
			String opportunityId = OpportunityRestApiTests.createOpportunityHelper(user, log, testConfig.getBrowserURL(), this);
			LoginRestAPI loginRestAPI = new LoginRestAPI();
			String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

			String meetingId = MeetingRestAPITests.createmeetingHelper(user, log, testConfig.getBrowserURL(), this);
			MeetingRestAPI meetingRestAPI = new MeetingRestAPI();
			//create an opportunity to link
			
			
			
			String jsonString = meetingRestAPI.linkRecordToMeeting(testConfig.getBrowserURL(), token, meetingId, opportunityId, "Opportunity");
			
			if (jsonString.equalsIgnoreCase("false")) {
				Assert.assertTrue(false, "Linking Opportunity to meeting failed.");
			} 
			
			try {
				JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
				JSONObject record = (JSONObject)postResponse.get("record");
				//validate the opportunity was related to call as expected
				Assert.assertEquals((String) record.get("id"), meetingId, "Parent record was not returned as expected.  ");
			} catch (Exception e) {
				Assert.assertTrue(false, "Opportunity was not linked to meeting as expectd.");
			}
			
			log.info("End test method Test_linkOpportunityToMeeting.");
		}
		
		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Search Meeting from the pool </li>
		 * <li>Create a New Client to link </li>
		 * <li>Link New Client to Meeting</li>
		 * <li>Verify Client to Meeting is Linked successfully </li>
		 * </ol>
		 */
		@Test(groups={"MOBILE"})
		public void Test_linkClientToMeeting()
		{
		log.info("Start test method test_linkClientToMeeting");
		
		log.info("Getting user");
		
		User user=commonUserAllocator.getUser();
		
		LoginRestAPI loginRestAPI=new LoginRestAPI();
		
		String Token=loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(),user.getEmail(),user.getPassword());
		
		String clientId=commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
		
		String readlClientId=new APIUtilities().getClientBeanIDFromCCMSID(testConfig.getBrowserURL(), clientId, user.getEmail(), user.getPassword());
		log.info("relating meeting to client");
		
		
		MeetingRestAPI meetingRestAPI = new MeetingRestAPI();
		String meetingId = MeetingRestAPITests.createmeetingHelper(user, log, testConfig.getBrowserURL(), this);
		
		
		String jsonString= meetingRestAPI.linkRecordToMeeting(testConfig.getBrowserURL(), Token,meetingId, readlClientId, "Client");
		
		if(jsonString.equalsIgnoreCase("false"))
		{
			Assert.assertTrue(false,"meeting was not linked to client");
		}
		try
		{
			JSONObject postResponse=(JSONObject)new JSONParser().parse(jsonString);
			JSONObject record=(JSONObject)postResponse.get("record");
			Assert.assertEquals((String)record.get("id"),meetingId,"Parent Record was not returned as expected");
		}
		catch(Exception e)
		{
			Assert.assertTrue(false,"meeting was not linked with client as expected");
		}
		log.info("End test Method test_linkMeetingToClient");
		}
		
		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Search Meeting from the pool </li>
		 * <li>Edit the Meeting </li>
		 * <li>Verify the Meeting is edited successfully </li>
		 * </ol>
		 */
		
		@Test(groups = {"MOBILE"})
		public void Test_editMeeting()
		{
			log.info("Start test method Test_editmeeting.");
			log.info("Getting user.");
			
			User user = commonUserAllocator.getUser(this);
			
			log.info("Retrieving OAuth2Token.");
			
			LoginRestAPI loginRestAPI = new LoginRestAPI();
			String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());
			MeetingRestAPI meetingRestAPI = new MeetingRestAPI();
			String meetingId = MeetingRestAPITests.createmeetingHelper(user, log, testConfig.getBrowserURL(), this);
			
			String name = "A Subject updated";
			String description = "The meeting.Edited!";		
			
			String jsonString = meetingRestAPI.editMeeting(testConfig.getBrowserURL(), token, meetingId, name, description, new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword()));
			
			if (jsonString.equalsIgnoreCase("false")) {
				Assert.assertTrue(false, "Editing Task failed.");
			} 
			
			try {
				JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
				//validate the call was edited as expected
				Assert.assertEquals((String) postResponse.get("name"), name, "name was not returned as expected.  ");
				Assert.assertEquals((String) postResponse.get("description"), description, "priority was not returned as expected.  ");
				
			} catch (Exception e) {
				Assert.assertTrue(false, "meeting was not edited as expected.");
			}
			
			log.info("End test method Test_editmeeting.");
		}
		
		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Search Meeting from the pool</li>
		 * <li>Convert the Meeting as Favorite Meeting </li>
		 * <li>Verify Meeting convert to Favorite Meeting</li>
		 * </ol>
		 */
		@Test(groups = {"MOBILE"})
		public void Test_favoritemeeting()
		{
			log.info("Start test method Test_favoritemeeting.");
			log.info("Getting user.");
			
			User user = commonUserAllocator.getUser(this);
			
			log.info("Retrieving OAuth2Token.");
			
			LoginRestAPI loginRestAPI = new LoginRestAPI();
			String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());
			String favorite = "favorite";
			MeetingRestAPI meetingRestAPI = new MeetingRestAPI();
			String meetingId = MeetingRestAPITests.createmeetingHelper(user, log, testConfig.getBrowserURL(), this);	
			
			
			String jsonString = meetingRestAPI.favoriteMeeting(testConfig.getBrowserURL(), token, meetingId, favorite, new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword()));
			
			if (jsonString.equalsIgnoreCase("false")) {
				Assert.assertTrue(false, "favoriting Task failed.");
			} 
			
			try {
				JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
				//validate the meeting was favorited as expected
				Assert.assertTrue((Boolean)postResponse.get("my_favorite"));
			} catch (Exception e) {
				Assert.assertTrue(false, "meeting was not marked as favorite as expected.");
			}
			
			log.info("End test method Test_favoritemeeting.");
		}
		
		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Search Meeting from the pool</li>
		 * <li>Convert the Meeting as unFavorite Meeting </li>
		 * <li>Verify Meeting convert to unFavorite Meeting</li>
		 * </ol>
		 */
		
		@Test(groups = {"MOBILE"})
		public void Test_unFavoritemeeting(){
			log.info("Start test method Test_unFavoritemeeting.");
			log.info("Getting user.");
			
			User user = commonUserAllocator.getUser(this);
			
			log.info("Retrieving OAuth2Token.");
			
			LoginRestAPI loginRestAPI = new LoginRestAPI();
			String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());
			MeetingRestAPI meetingRestAPI = new MeetingRestAPI();
			String meetingId = MeetingRestAPITests.createmeetingHelper(user, log, testConfig.getBrowserURL(), this);	
			String favorite = "unfavorite";
			
			
			String jsonString = meetingRestAPI.favoriteMeeting(testConfig.getBrowserURL(), token, meetingId, favorite, new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword()));
			
			if (jsonString.equalsIgnoreCase("false")) {
				Assert.assertTrue(false, "unfavoriting meeting failed.");
			} 
			
			try {
				JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
				Assert.assertFalse((Boolean)postResponse.get("my_favorite"));
			} catch (Exception e) {
				Assert.assertTrue(false, "meeting was not marked as unFavorite as expected.");
			}
			
			log.info("End test method Test_unFavoritemeeting.");
		}
		

		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Search Meeting from the pool</li>
		 * <li>Delete the Meeting </li>
		 * <li>Verify Meeting is deleted successfully </li>
		 * </ol>
		 */
		
		@Test(groups = {"MOBILE"})
		public void Test_deletemeeting(){
			log.info("Start test method Test_deletemeeting.");
			log.info("Getting user.");
			
			User user = commonUserAllocator.getUser(this);
			
			log.info("Retrieving OAuth2Token.");
			
			LoginRestAPI loginRestAPI = new LoginRestAPI();
			String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());
			MeetingRestAPI meetingRestAPI = new MeetingRestAPI();
			String meetingId = MeetingRestAPITests.createmeetingHelper(user, log, testConfig.getBrowserURL(), this);	
			
		
			String jsonString = meetingRestAPI.deleteMeeting(testConfig.getBrowserURL(), token, meetingId);
			
			if (jsonString.equalsIgnoreCase("false")) {
				Assert.assertTrue(false, "Deleting meeting failed.");
			} 
			
			try {
				JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
				//validate the id was returned as expected
				Assert.assertEquals((String) postResponse.get("id"), meetingId, "Id was not returned as expected.  ");
			} catch (Exception e) {
				Assert.assertTrue(false, "meeting was not deleted as expected.");
			}
			
			log.info("End test method Test_deletemeeting.");
		}
		
		
		/*
		 *  HELPERS
		 * -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
		 */
		
		/*
		 * Creates a meeting and returns the meeting ID.
		 */
		public static String createmeetingHelper(User user, Logger log, String url, MeetingRestAPITests meetingRestAPITest){
			log.info("Retrieving OAuth2Token.");
			
			LoginRestAPI loginRestAPI = new LoginRestAPI();
			String token = loginRestAPI.getOAuth2Token(url, user.getEmail(), user.getPassword());

			log.info("Creating a meeting.");
			MeetingRestAPI meetingRestAPI = new MeetingRestAPI();
			String subject = "A Subject";
			String description = "The meeting.";
			String duration_hours = "1";
			String duration_minutes = "15";
			
			Date startDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");						
			String start_date = sdf.format(startDate).toString();
			
			String jsonString = meetingRestAPI.createMeeting(url, token, subject, description, start_date, duration_hours, duration_minutes, new APIUtilities().getUserBeanIDFromEmail(url,user.getEmail(),user.getPassword()));
			
			String meetingId = "";
				
			try {
				JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
				meetingId = (String) postResponse.get("id");
			} catch (Exception e) {
				Assert.assertTrue(false, "meeting was not created as expected.");
			}
			
			log.info("meeting successfully created.");
				
			return meetingId;
		}


	}



