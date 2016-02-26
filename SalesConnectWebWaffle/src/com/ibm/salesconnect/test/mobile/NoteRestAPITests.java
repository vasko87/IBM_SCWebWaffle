package com.ibm.salesconnect.test.mobile;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.APIUtilities;
import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.API.NoteRestAPI;
import com.ibm.salesconnect.API.SugarAPI;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;

/**
<br/><br/>
 * Test to validate the create,read,delete,Related TO,Link ,Create Meeting Helper,Favorite and Unfavorite functionality of the Note module using API
 * @author 
 * Srinidhi B Shridhar
<br/><br/>
 * */
public class NoteRestAPITests extends ProductBaseTest {
	
	Logger log = LoggerFactory.getLogger(NoteRestAPITests.class);
	
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>	 
	 * <li>Create a New Note </li>
	 * * <li>Search Note</li>
	 * <li>Verify Note Created as Expected</li>
	 * </ol>
	 */
	
	@Test(groups = {"MOBILE"})
	public void Test_createNote(){
		log.info("Start test method Test_createNote.");
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		log.info("Creating a note.");
		
		String subject = "A Subject";
		String description = "The note.";
		String sessionID = new SugarAPI().getSessionID(baseURL, user.getEmail(), user.getPassword());
		NoteRestAPI noteRestAPI = new NoteRestAPI();
		String jsonString = noteRestAPI.createNote(testConfig.getBrowserURL(), token, subject, description, new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword(), sessionID));
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Note creation failed.");
		} else {
			
			try {
				JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
				String noteId = (String) postResponse.get("id");
				//validate the note was created as expected
				Assert.assertEquals((String) postResponse.get("name"), subject, "Subject was not returned as expected.  ");
				Assert.assertEquals((String) postResponse.get("description"), description, "Description was not returned as expected.  ");
				Assert.assertEquals((String) postResponse.get("assigned_user_id"), new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword(),sessionID), "Assigned user id was not returned as expected.  ");
			} catch (Exception e) {
				Assert.assertTrue(false, "Note was not created as expected.");
			}
			
			log.info("Note successfully created.");
			
		}
		
		log.info("End test method Test_createNote.");
	}
	
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Create a New Note </li>
	 * <li>Related to New Note to Contact</li>
	 * <li>Verify New Note is related to Contact</li>
	 * </ol>
	 */
	@Test(groups = {"MOBILE"})
	public void Test_relateNewContactToNote(){
		log.info("Start test method Test_relateNewContactToNote.");
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		String noteId = NoteRestAPITests.createNoteHelper(user, log, testConfig.getBrowserURL());
		
		String firstName = "Ron";
		String lastName = "Burgandy";
		String address = "US";
		
		NoteRestAPI noteRestAPI = new NoteRestAPI();
		String jsonString = noteRestAPI.relateNewContactToNote(testConfig.getBrowserURL(), token, noteId, firstName, lastName, address, new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword()));
		log.info(jsonString);
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Relating Contact to task failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			JSONObject relatedRecord = (JSONObject) postResponse.get("related_record");
			Assert.assertEquals((String) record.get("id"), noteId, "Parent record was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Contact was not related to note as expectd.");
		}
		
		log.info("End test method Test_relateNewContactToNote.");
		
	}
	
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Create a New Note </li>
	 * <li>Related to New Note to Opportunity</li>
	 * <li>Verify New Note is related to Opportunity</li>
	 * </ol>
	 */
	@Test(groups = {"MOBILE"})
	public void Test_relateNewOpportunityToNote(){
		log.info("Start test method Test_relateNewOpportunityToNote.");
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		String noteId = NoteRestAPITests.createNoteHelper(user, log, testConfig.getBrowserURL());
		
		//get a client from the client pool
		String clientId = commonClientAllocator.getGroupClient(GC.SC, this).getCCMS_ID();
		String sessionID = new SugarAPI().getSessionID(baseURL, user.getEmail(), user.getPassword());
		String realClientId = new APIUtilities().getClientBeanIDFromCCMSID(testConfig.getBrowserURL(), clientId, user.getEmail(), user.getPassword(),sessionID);
		String contactId = ContactRestAPITests.createContactHelper(user, log, testConfig.getBrowserURL());
		
		String desc = "A description";
		String source = "RLPL";
		String salesStage = "03";
		String date = "2016-10-28";
		
		NoteRestAPI noteRestAPI = new NoteRestAPI();
		String jsonString = noteRestAPI.relateNewOpportunityToNote(testConfig.getBrowserURL(), token, noteId, desc, source, salesStage, date, realClientId, contactId, new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword(),sessionID));
		log.info(jsonString);
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Relating Opportunity to Note failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			Assert.assertEquals((String) record.get("id"), noteId, "Parent record was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Opportunity was not related to NOte as expectd.");
		}
		
		log.info("End test method Test_relateNewOpportunityToNote.");
		
	}
	
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Create a New Note </li>
	 * <li>Related to New Note to Task</li>
	 * <li>Verify New Note is related to Task</li>
	 * </ol>
	 */
	@Test(groups = {"MOBILE"})
	public void Test_relateNewTaskToNote(){
		log.info("Start test method Test_relateNewTaskToTask.");
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		String noteId = NoteRestAPITests.createNoteHelper(user, log, testConfig.getBrowserURL());
		
		String name = "Related Task";
		String dueDate = "2013-10-29T06:00:00.000Z";
		String priority = "High";
		String status = "Not Started";
		String callType = "Close_out_call";
		
		NoteRestAPI noteRestAPI = new NoteRestAPI();
		String jsonString = noteRestAPI.relateNewTaskToNote(testConfig.getBrowserURL(), token, noteId, name, dueDate, priority, status, callType, new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword()));
		log.info(jsonString);
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Relating Task to Note failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			Assert.assertEquals((String) record.get("id"), noteId, "Parent record was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Task was not related to Note as expectd.");
		}
		
		log.info("End test method Test_relateNewTaskToNote.");
		
	}
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Search Note from the pool </li>
	 * <li>Create a Additional Assignee to link </li>
	 * <li>Link Additional Assignee to Meeting</li>
	 * <li>Verify Additional Assignee to Note is Linked successfully </li>
	 * </ol>
	 */
	
	@Test(groups = {"MOBILE"})
	public void Test_linkAdditionalAssigneeToNote(){
		log.info("Start test method Test_linkAdditionalAssigneeToNote.");
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		User user2 = commonUserAllocator.getUser(this);
		
		log.info("Getting user bean uid.");
		String userId =	new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user2.getEmail(),user2.getPassword());
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		String noteId = NoteRestAPITests.createNoteHelper(user, log, testConfig.getBrowserURL());
		
		log.info("Linking assignee to note.");
		NoteRestAPI noteRestAPI = new NoteRestAPI();
		String jsonString = noteRestAPI.linkRecordToNote(testConfig.getBrowserURL(), token, noteId, userId, "Assignee");
				
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Linking Additional assignee to note failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			//validate the opportunity was related to call as expected
			Assert.assertEquals((String) record.get("id"), noteId, "Parent record was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Assignee was not linked to Note as expectd.");
		}
		
		log.info("End test method Test_linkAdditionalAssigneeToNote.");
	}
	
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Search Note from the pool </li>
	 * <li>Create a New Contact to link </li>
	 * <li>Link New Contact to Note</li>
	 * <li>Verify Contact to Note is Linked successfully </li>
	 * </ol>
	 */
	
	@Test(groups = {"MOBILE"})
	public void Test_linkContactToNote(){
		log.info("Start test method Test_linkContactToNote.");
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		String noteId = NoteRestAPITests.createNoteHelper(user, log, testConfig.getBrowserURL());
		
		//create a contact to link
		String contactId = ContactRestAPITests.createContactHelper(user, log, testConfig.getBrowserURL());
		
		NoteRestAPI noteRestAPI = new NoteRestAPI();
		String jsonString = noteRestAPI.linkRecordToNote(testConfig.getBrowserURL(), token, noteId, contactId, "Contacts");
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Relating contact to note failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			//validate the contact was related to note as expected
			Assert.assertEquals((String) record.get("id"), noteId, "Parent record was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Contact was not linked to Note as expectd.");
		}
		
		log.info("End test method Test_linkContactToNote.");
	}
	
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Search Note from the pool </li>
	 * <li>Create a New Task to link </li>
	 * <li>Link New Task to Note</li>
	 * <li>Verify Task to Note is Linked successfully </li>
	 * </ol>
	 */
	@Test(groups = {"MOBILE"})
	public void Test_linkTaskToNote(){
		log.info("Start test method Test_linkTaskToNote.");
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		String noteId = NoteRestAPITests.createNoteHelper(user, log, testConfig.getBrowserURL());
		
		//create a task to link
		String taskId = TaskRestAPITests.createTaskHelper(user, log, testConfig.getBrowserURL());
		
		NoteRestAPI noteRestAPI = new NoteRestAPI();
		String jsonString = noteRestAPI.linkRecordToNote(testConfig.getBrowserURL(), token, noteId, taskId, "Tasks");
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Linking task to Note failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			//validate the task was related to Note as expected
			Assert.assertEquals((String) record.get("id"), noteId, "Parent record was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Task was not linked to Note as expectd.");
		}
		
		log.info("End test method Test_linkTaskToNote.");
	}
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Search Note from the pool </li>
	 * <li>Create a New Opportunity to link </li>
	 * <li>Link New Opportunity to Note</li>
	 * <li>Verify Opportunity to Note is Linked successfully </li>
	 * </ol>
	 */
	@Test(groups = {"MOBILE"})
	public void Test_linkOpportunityToNote(){
		log.info("Start test method Test_linkOpportunityToNote.");
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		String noteId = NoteRestAPITests.createNoteHelper(user, log, testConfig.getBrowserURL());
		
		//create an opportunity to link
		String opportunityId = OpportunityRestApiTests.createOpportunityHelper(user, log, testConfig.getBrowserURL(), this);
		
		NoteRestAPI noteRestAPI = new NoteRestAPI();
		String jsonString = noteRestAPI.linkRecordToNote(testConfig.getBrowserURL(), token, noteId, opportunityId, "Opportunity");
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Linking Opportunity to note failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			//validate the opportunity was related to call as expected
			Assert.assertEquals((String) record.get("id"), noteId, "Parent record was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Opportunity was not linked to Note as expectd.");
		}
		
		log.info("End test method Test_linkOpportunityToNote.");
	}
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Search Note from the pool </li>
	 * <li>Create a New Client to link </li>
	 * <li>Link New Client to Note</li>
	 * <li>Verify Client to Note is Linked successfully </li>
	 * </ol>
	 */
	@Test(groups = {"MOBILE"})
	public void Test_linkClientToNote(){
		log.info("Start test method Test_linkClientToNote.");
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		String noteId = NoteRestAPITests.createNoteHelper(user, log, testConfig.getBrowserURL());
		
		//get a client from the client pool
		String clientId = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();

		String realClientId = new APIUtilities().getClientBeanIDFromCCMSID(testConfig.getBrowserURL(), clientId, user.getEmail(), user.getPassword());
		
		NoteRestAPI noteRestAPI = new NoteRestAPI();
		String jsonString = noteRestAPI.linkRecordToNote(testConfig.getBrowserURL(), token, noteId, realClientId, "Client");
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Linking Client to note failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			//validate the opportunity was related to call as expected
			Assert.assertEquals((String) record.get("id"), noteId, "Parent record was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Clienbt was not linked to Note as expectd.");
		}
		
		log.info("End test method Test_linkClientToNote.");
	}
	
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Search Note from the pool </li>
	 * <li>Edit the Note </li>
	 * <li>Verify the Note is edited successfully </li>
	 * </ol>
	 */
	@Test(groups = {"MOBILE"})
	public void Test_editNote(){
		log.info("Start test method Test_editNote.");
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		String noteId = NoteRestAPITests.createNoteHelper(user, log, testConfig.getBrowserURL());
		
		String name = "A Subject updated";
		String description = "The note was updated.";		
		
		NoteRestAPI noteRestAPI = new NoteRestAPI();
		String jsonString = noteRestAPI.editNote(testConfig.getBrowserURL(), token, noteId, name, description, new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword()));
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Editing Task failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			String id = (String) postResponse.get("id");
			//validate the call was edited as expected
			Assert.assertEquals((String) postResponse.get("name"), name, "name was not returned as expected.  ");
			Assert.assertEquals((String) postResponse.get("description"), description, "priority was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Note was not edited as expected.");
		}
		
		log.info("End test method Test_editNote.");
	}
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Search Note from the pool</li>
	 * <li>Convert the Meeting as Favorite Note </li>
	 * <li>Verify Note convert to Favorite Note</li>
	 * </ol>
	 */
	
	@Test(groups = {"MOBILE"})
	public void Test_favoriteNote(){
		log.info("Start test method Test_favoriteNote.");
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());
		String favorite = "favorite";
		
		String noteId = NoteRestAPITests.createNoteHelper(user, log, testConfig.getBrowserURL());	
		
		NoteRestAPI noteRestAPI = new NoteRestAPI();
		String jsonString = noteRestAPI.favoriteNote(testConfig.getBrowserURL(), token, noteId, favorite, new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword()));
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "favoriting Task failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			String id = (String) postResponse.get("id");
			//validate the note was favorited as expected
			Assert.assertTrue((Boolean)postResponse.get("my_favorite"));
		} catch (Exception e) {
			Assert.assertTrue(false, "Note was not marked as favorite as expected.");
		}
		
		log.info("End test method Test_favoriteNote.");
	}
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Search Note from the pool</li>
	 * <li>Convert the Note as unFavorite Note </li>
	 * <li>Verify Note convert to unFavorite Note</li>
	 * </ol>
	 */
	
	@Test(groups = {"MOBILE"})
	public void Test_unFavoriteNote(){
		log.info("Start test method Test_unFavoriteNote.");
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		String noteId = NoteRestAPITests.createNoteHelper(user, log, testConfig.getBrowserURL());	
		String favorite = "unfavorite";
		
		NoteRestAPI noteRestAPI = new NoteRestAPI();
		String jsonString = noteRestAPI.favoriteNote(testConfig.getBrowserURL(), token, noteId, favorite, new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword()));
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "unfavoriting Note failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			String id = (String) postResponse.get("id");
			Assert.assertFalse((Boolean)postResponse.get("my_favorite"));
		} catch (Exception e) {
			Assert.assertTrue(false, "Note was not marked as unFavorite as expected.");
		}
		
		log.info("End test method Test_unFavoriteNote.");
	}
	
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Search Note from the pool</li>
	 * <li>Delete the Note </li>
	 * <li>Verify Note is deleted successfully </li>
	 * </ol>
	 */
	@Test(groups = {"MOBILE"})
	public void Test_deleteNote(){
		log.info("Start test method Test_deleteNote.");
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		String noteId = NoteRestAPITests.createNoteHelper(user, log, testConfig.getBrowserURL());	
		
		NoteRestAPI noteRestAPI = new NoteRestAPI();
		String jsonString = noteRestAPI.deleteNote(testConfig.getBrowserURL(), token, noteId);
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Deleting Note failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			//validate the id was returned as expected
			Assert.assertEquals((String) postResponse.get("id"), noteId, "Id was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Note was not deleted as expected.");
		}
		
		log.info("End test method Test_deleteNote.");
	}
	
	
	/*
	 *  HELPERS
	 * -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	 */
	
	/*
	 * Creates a note and returns the note ID.
	 */
	public static String createNoteHelper(User user, Logger log, String url){
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(url, user.getEmail(), user.getPassword());

		log.info("Creating a note.");
		
		String subject = "A Subject";
		String description = "The note.";
		
		NoteRestAPI noteRestAPI = new NoteRestAPI();
		String jsonString = noteRestAPI.createNote(url, token, subject, description, new APIUtilities().getUserBeanIDFromEmail(url,user.getEmail(),user.getPassword()));
		
		String noteId = "";
			
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			noteId = (String) postResponse.get("id");
		} catch (Exception e) {
			Assert.assertTrue(false, "Note was not created as expected.");
		}
		
		log.info("Note successfully created.");
			
		return noteId;
	}


}
