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
import com.ibm.salesconnect.API.OpportunityRestAPI;
import com.ibm.salesconnect.API.SugarAPI;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;

/**
<br/><br/>
 * Test to validate the Create,Related TO,Link to functionality of the Opportunity module using API
 * @author 
 * Veena H
<br/><br/>
 * */
public class OpportunityRestApiTests extends ProductBaseTest {
	
	Logger log = LoggerFactory.getLogger(OpportunityRestApiTests.class);
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>	 
	 * <li>Create a New Opportunity </li>
	 * <li>Search Opportunity</li>
	 * <li>Verify Opportunity is created as Expected</li>
	 * </ol>
	 */
	
	@Test(groups = {"MOBILE"})
	public void Test_createOpportunity(){
		log.info("Start test method Test_createOpportunity.");
		log.info("Getting user and client.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		String contactId = ContactRestAPITests.createContactHelper(user, log, testConfig.getBrowserURL());
		
		log.info("Creating an opportunity.");
		
		String desc = "A description";
		String source = "RLPL";
		String salesStage = "03";
		String date = "2016-10-28";
		
		//get a client from the client pool
		String clientId = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
		String sessionID = new SugarAPI().getSessionID(baseURL, user.getEmail(), user.getPassword());
		String realClientId = new APIUtilities().getClientBeanIDFromCCMSID(testConfig.getBrowserURL(), clientId, user.getEmail(), user.getPassword(),sessionID);
		
		OpportunityRestAPI opportunityRestAPI = new OpportunityRestAPI();
		String jsonString2 = opportunityRestAPI.createOpportunity(testConfig.getBrowserURL(), token, desc, realClientId, contactId, source, salesStage, date, new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword()));
		
		if (jsonString2.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Opportunity creation failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString2);
			String opportunityId = (String) postResponse.get("id");
			log.debug("OpportunityId: " + opportunityId);
			//validate the opportunity was created as expected
			Assert.assertEquals((String) postResponse.get("account_id"), realClientId, "Client id was not returned as expected.  ");
			Assert.assertEquals((String) postResponse.get("contact_id_c"), contactId, "Contact id was not returned as expected.  ");
			Assert.assertEquals((String) postResponse.get("date_closed"), date, "Date closed was not returned as expected.  ");
			Assert.assertEquals((String) postResponse.get("description"), desc, "Description was not returned as expected.  ");
			Assert.assertEquals((String) postResponse.get("lead_source"), source, "Lead source was not returned as expected.  ");
			Assert.assertEquals((String) postResponse.get("sales_stage"), salesStage, "Sales stage was not returned as expected.  ");
			Assert.assertEquals((String) postResponse.get("assigned_user_id"), new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword(),sessionID), "Assigned user id was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Opportunity was not created as expected.");
		}
		
		log.info("End test method Test_createOpportunity.");
	}
	
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Create a New Opportunity </li>
	 * <li>Related to New Opportunity to the Call</li>
	 * <li>Verify New Opportunity is related to Call</li>
	 * </ol>
	 */
	@Test(groups = {"MOBILE"})
	public void Test_relateNewCallToOpportunity(){
		log.info("Start test method Test_relateNewCallToOpportunity.");
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		//opportunities from the pool aren't real, so we have to create a contact and an opportunity here
		String opportunityId = OpportunityRestApiTests.createOpportunityHelper(user, log, testConfig.getBrowserURL(), this, token);
		
		String callSubject = "A subject.";
		String callDate = "2016-10-29T06:00:00.000Z";
		String callStatus = "Held";
		String callType = "outbound_call";
		String callDuration = "15";
		
		OpportunityRestAPI opportunityRestAPI = new OpportunityRestAPI();
		String jsonString = opportunityRestAPI.relateNewCallToOpportunity(testConfig.getBrowserURL(), token, opportunityId, callSubject, callDate, callStatus, callType, callDuration, new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword()));
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Relating call to opportunity failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			JSONObject relatedRecord = (JSONObject) postResponse.get("related_record");
			//validate the call was related to opportunity as expected
			Assert.assertEquals((String) record.get("id"), opportunityId, "Parent record was not returned as expected.  ");
			Assert.assertEquals((String) relatedRecord.get("parent_id"), opportunityId, "Call was not related to opportunity as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Call was not related to opportunity as expectd.");
		}
		
		log.info("End test method Test_relateNewCallToOpportunity.");
	}
	
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Create a New Opportunity </li>
	 * <li>Related to New Opportunity to the Task</li>
	 * <li>Verify New Opportunity is related to Task</li>
	 * </ol>
	 */
	@Test(groups = {"MOBILE"})
	public void Test_relateNewTaskToOpportunity(){
		log.info("Start test method Test_relateNewTaskToOpportunity.");
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		//opportunities from the pool aren't real, so we have to create a contact and an opportunity here
		String opportunityId = OpportunityRestApiTests.createOpportunityHelper(user, log, testConfig.getBrowserURL(), this, token);
		
		String taskName = "A task.";
		String taskDate = "2016-10-29T06:00:00.000Z";
		String taskPriority = "Medium";
		String taskStatus = "In Progress";
		String taskType = "Technical_Sales_Activity";
		
		OpportunityRestAPI opportunityRestAPI = new OpportunityRestAPI();
		String jsonString = opportunityRestAPI.relateNewTaskToOpportunity(testConfig.getBrowserURL(), token, opportunityId, taskName, taskDate, taskPriority, taskStatus, taskType, new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword()));
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Relating task to opportunity failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			JSONObject relatedRecord = (JSONObject) postResponse.get("related_record");
			//validate the task was related to opportunity as expected
			Assert.assertEquals((String) record.get("id"), opportunityId, "Parent record was not returned as expected.  ");
			Assert.assertEquals((String) relatedRecord.get("parent_id"), opportunityId, "Task was not related to opportunity as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Task was not related to opportunity as expectd.");
		}
		
		log.info("End test method Test_relateNewTaskToOpportunity.");
	}
	
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Create a New Opportunity </li>
	 * <li>Related to New Opportunity to the New Line item</li>
	 * <li>Verify New Opportunity is related to New Line item</li>
	 * </ol>
	 */
	@Test(groups = {"MOBILE"})
	public void Test_relateNewLineItemToOpportunity(){
		log.info("Start test method Test_relateNewTaskToOpportunity.");
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		//opportunities from the pool aren't real, so we have to create a contact and an opportunity here
		String opportunityId = OpportunityRestApiTests.createOpportunityHelper(user, log, testConfig.getBrowserURL(), this, token);
		
		String liAmount = "500";
		String liDate = "2016-09-29";
		String liProbability = "10";
		String liContractType = "";
		String liLevel10 = "B3000";
		String liLevel15 = "MOB";
		String liLevel17 = "17MOB";
		String liLevel20 = "B3M00";
		String liLevel30 = "B3ME4";
		String liLevel40 = "";
		String liCurrency = "-99";

		OpportunityRestAPI opportunityRestAPI = new OpportunityRestAPI();
		String jsonString = opportunityRestAPI.relateNewLineItemToOpportunity(testConfig.getBrowserURL(), token, opportunityId, liAmount, liProbability, liContractType, liDate, liLevel10, liLevel15, liLevel17, liLevel20, liLevel30, liLevel40,liCurrency, user.getDisplayName(), new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword()));
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Relating line item to opportunity failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			//validate the line item was related to opportunity as expected
			Assert.assertEquals((String) record.get("id"), opportunityId, "Parent record was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Line item was not related to opportunity as expectd.");
		}
		
		log.info("End test method Test_relateNewLineItemToOpportunity.");
	}
	
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Create a New Opportunity </li>
	 * <li>Related to New Opportunity to the New note</li>
	 * <li>Verify New Opportunity is related to New note</li>
	 * </ol>
	 */
	@Test(groups = {"MOBILE"})
	public void Test_relateNewNoteToOpportunity(){
		log.info("Start test method Test_relateNewNoteToOpportunity.");
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		//opportunities from the pool aren't real, so we have to create a contact and an opportunity here
		String opportunityId = OpportunityRestApiTests.createOpportunityHelper(user, log, testConfig.getBrowserURL(), this, token);
		
		String noteName = "A note.";
		
		OpportunityRestAPI opportunityRestAPI = new OpportunityRestAPI();
		String jsonString = opportunityRestAPI.relateNewNoteToOpportunity(testConfig.getBrowserURL(), token, opportunityId, noteName, new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword()));
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Relating note to opportunity failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			//validate the note was related to opportunity as expected
			Assert.assertEquals((String) record.get("id"), opportunityId, "Parent record was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Note was not related to opportunity as expectd.");
		}
		
		log.info("End test method Test_relateNewNoteToOpportunity.");
	}
	
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Create a New Opportunity </li>
	 * <li>Related to New Opportunity to the New Contact</li>
	 * <li>Verify New Opportunity is related to New Contact</li>
	 * </ol>
	 */
	@Test(groups = {"MOBILE"})
	public void Test_relateNewContactToOpportunity(){
		log.info("Start test method Test_relateNewContactToOpportunity.");
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		//opportunities from the pool aren't real, so we have to create a contact and an opportunity here
		String opportunityId = OpportunityRestApiTests.createOpportunityHelper(user, log, testConfig.getBrowserURL(), this, token);
		
		String contactFirstName = "Frodo";
		String contactLastName = "Baggins";
		String contactCountry = "AU";
		
		OpportunityRestAPI opportunityRestAPI = new OpportunityRestAPI();
		String jsonString = opportunityRestAPI.relateNewContactToOpportunity(testConfig.getBrowserURL(), token, opportunityId, contactFirstName, contactLastName, contactCountry, new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword()));
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Relating contact to opportunity failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			//validate the contact was related to opportunity as expected
			Assert.assertEquals((String) record.get("id"), opportunityId, "Parent record was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Contact was not related to opportunity as expectd.");
		}
		
		log.info("End test method Test_relateNewNoteToOpportunity.");
	}
	
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Create a New Opportunity </li>
	 * <li>Link New Opportunity to the Task</li>
	 * <li>Verify New Opportunity is linked to the Task</li>
	 * </ol>
	 */
	@Test(groups = {"MOBILE"})
	public void Test_linkTaskToOpportunity(){
		log.info("Start test method Test_linkTaskToOpportunity.");
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		//opportunities from the pool aren't real, so we have to create a contact and an opportunity here
		String opportunityId = OpportunityRestApiTests.createOpportunityHelper(user, log, testConfig.getBrowserURL(), this, token);
		
		//create a task to relate
		String taskId = TaskRestAPITests.createTaskHelper(user, log, testConfig.getBrowserURL());
		
		OpportunityRestAPI opportunityRestAPI = new OpportunityRestAPI();
		String jsonString = opportunityRestAPI.linkRecordToOpportunity(testConfig.getBrowserURL(), token, opportunityId, taskId, "Tasks");
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Relating task to opportunity failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			//validate the task was related to opportunity as expected
			Assert.assertEquals((String) record.get("id"), opportunityId, "Parent record was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Task was not related to opportunity as expectd.");
		}
		
		log.info("End test method Test_linkTaskToOpportunity.");
	}

	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Create a New Opportunity </li>
	 * <li>Link New Opportunity to the Note</li>
	 * <li>Verify New Opportunity is linked to the Note</li>
	 * </ol>
	 */
	@Test(groups = {"MOBILE"})
	public void Test_linkNoteToOpportunity(){
		log.info("Start test method Test_linkNoteToOpportunity.");
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		//opportunities from the pool aren't real, so we have to create a contact and an opportunity here
		String opportunityId = OpportunityRestApiTests.createOpportunityHelper(user, log, testConfig.getBrowserURL(), this, token);
		
		//create a note to relate
		String noteId = NoteRestAPITests.createNoteHelper(user, log, testConfig.getBrowserURL());
		
		OpportunityRestAPI opportunityRestAPI = new OpportunityRestAPI();
		String jsonString = opportunityRestAPI.linkRecordToOpportunity(testConfig.getBrowserURL(), token, opportunityId, noteId, "Notes");
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Relating note to opportunity failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			//validate the note was related to opportunity as expected
			Assert.assertEquals((String) record.get("id"), opportunityId, "Parent record was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Note was not related to opportunity as expectd.");
		}
		
		log.info("End test method Test_linkNoteToOpportunity.");
	}
	
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Create a New Opportunity </li>
	 * <li>Link New Opportunity to the Contact</li>
	 * <li>Verify New Opportunity is linked to the Contact</li>
	 * </ol>
	 */
	@Test(groups = {"MOBILE"})
	public void Test_linkContactToOpportunity(){
		log.info("Start test method Test_linkContactToOpportunity.");
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		//opportunities from the pool aren't real, so we have to create a contact and an opportunity here
		String opportunityId = OpportunityRestApiTests.createOpportunityHelper(user, log, testConfig.getBrowserURL(), this, token);
		
		//create a contact to relate
		String contactId = ContactRestAPITests.createContactHelper(user, log, testConfig.getBrowserURL());
		
		OpportunityRestAPI opportunityRestAPI = new OpportunityRestAPI();
		String jsonString = opportunityRestAPI.linkRecordToOpportunity(testConfig.getBrowserURL(), token, opportunityId, contactId, "Contacts");
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Relating contact to opportunity failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			//validate the contact was related to opportunity as expected
			Assert.assertEquals((String) record.get("id"), opportunityId, "Parent record was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Contact was not related to opportunity as expectd.");
		}
		
		log.info("End test method Test_linkContactToOpportunity.");
	}
	
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Create a New Opportunity </li>
	 * <li>Link New Opportunity to the Call</li>
	 * <li>Verify New Opportunity is linked to the Call</li>
	 * </ol>
	 */
	@Test(groups = {"MOBILE"})
	public void Test_linkCallToOpportunity(){
		log.info("Start test method Test_linkCallToOpportunity.");
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		//opportunities from the pool aren't real, so we have to create a contact and an opportunity here
		String opportunityId = OpportunityRestApiTests.createOpportunityHelper(user, log, testConfig.getBrowserURL(), this, token);
		
		//create a call to relate
		String callId = CallRestAPITests.createCallHelper(user, log, testConfig.getBrowserURL(), this);
		
		OpportunityRestAPI opportunityRestAPI = new OpportunityRestAPI();
		String jsonString = opportunityRestAPI.linkRecordToOpportunity(testConfig.getBrowserURL(), token, opportunityId, callId, "Calls");
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Relating call to opportunity failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			//validate the call was related to opportunity as expected
			Assert.assertEquals((String) record.get("id"), opportunityId, "Parent record was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Call was not related to opportunity as expectd.");
		}
		
		log.info("End test method Test_linkCallToOpportunity.");
	}
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Create a New Opportunity </li>
	 * <li>Edit the Opportunity </li>
	 * <li>Verify the Opportunity is edited successfully</li>
	 * </ol>
	 */
	@Test(groups = {"MOBILE"})
	public void Test_editOpportunity(){
		log.info("Start test method Test_editOpportunity.");
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		log.info("Retrieving OAuth2Token.");	
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());
		
		//opportunities from the pool aren't real, so we have to create an opportunity and contact here
		String opportunityId = OpportunityRestApiTests.createOpportunityHelper(user, log, testConfig.getBrowserURL(), this, token);
		String contactId = ContactRestAPITests.createContactHelper(user, log, testConfig.getBrowserURL(), token);
		
		//get a client from the client pool
		String clientId = commonClientAllocator.getGroupClient(GC.SC, this).getCCMS_ID();
		String sessionID = new SugarAPI().getSessionID(baseURL, user.getEmail(), user.getPassword());
		String realClientId = new APIUtilities().getClientBeanIDFromCCMSID(testConfig.getBrowserURL(), clientId, user.getEmail(), user.getPassword(),sessionID);
		
		String desc = "A new description.";
		String source = "RLPL";
		String salesStage = "03";
		String date = "2016-11-04";		
		

		OpportunityRestAPI opportunityRestAPI = new OpportunityRestAPI();
		String jsonString = opportunityRestAPI.editOpportunity(testConfig.getBrowserURL(), token, opportunityId, desc, realClientId, contactId, source, salesStage, date, new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword(),sessionID));
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Editing opportunity failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			String id = (String) postResponse.get("id");
			log.debug("Id: " + id);
			//validate the opportunity was edited as expected
			Assert.assertEquals((String) postResponse.get("account_id"), realClientId, "Client id was not returned as expected.  ");
			Assert.assertEquals((String) postResponse.get("contact_id_c"), contactId, "Contact id was not returned as expected.  ");
			Assert.assertEquals((String) postResponse.get("date_closed"), date, "Date closed was not returned as expected.  ");
			Assert.assertEquals((String) postResponse.get("description"), desc, "Description was not returned as expected.  ");
			Assert.assertEquals((String) postResponse.get("lead_source"), source, "Lead source was not returned as expected.  ");
			Assert.assertEquals((String) postResponse.get("sales_stage"), salesStage, "Sales stage was not returned as expected.  ");
			Assert.assertEquals((String) postResponse.get("assigned_user_id"), new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword(),sessionID), "Assigned user id was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Opportunity was not edited as expected.");
		}
		
		log.info("End test method Test_editOpportunity.");
	}
	
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Search Opportunity from the pool</li>
	 * <li>Convert the Opportunity as Favorite Opportunity </li>
	 * <li>Verify Contact convert to Favorite Opportunity</li>
	 * </ol>
	 */
	@Test(groups = {"MOBILE"})
	public void Test_favoriteOpportunity(){
		log.info("Start test method Test_favoriteOpportunity.");
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());
		String favorite = "favorite";
		
		String opportunityId = OpportunityRestApiTests.createOpportunityHelper(user, log, testConfig.getBrowserURL(), this, token);	
		
		OpportunityRestAPI opportunityRestAPI = new OpportunityRestAPI();
		String jsonString = opportunityRestAPI.favoriteOpportunity(testConfig.getBrowserURL(), token, opportunityId, favorite, new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword()));
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "favoriting Contact failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			String id = (String) postResponse.get("id");
			log.debug("Id: " + id);
			//validate the contact was favorited as expected
			Assert.assertTrue((Boolean)postResponse.get("my_favorite"));
		} catch (Exception e) {
			Assert.assertTrue(false, "Contact was not marked as favorite as expected.");
		}
		
		log.info("End test method Test_favoriteContact.");
	}
	
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Search Opportunity from the pool</li>
	 * <li>Convert the Opportunity as unFavorite Opportunity </li>
	 * <li>Verify Opportunity convert to unFavorite Opportunity</li>
	 * </ol>
	 */
	@Test(groups = {"MOBILE"})
	public void Test_unFavoriteOpportunity(){
		log.info("Start test method Test_unFavoriteOpportunity.");
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		String opportunityId = OpportunityRestApiTests.createOpportunityHelper(user, log, testConfig.getBrowserURL(), this, token);	
		String favorite = "unfavorite";
		
		OpportunityRestAPI opportunityRestAPI = new OpportunityRestAPI();
		String jsonString = opportunityRestAPI.favoriteOpportunity(testConfig.getBrowserURL(), token, opportunityId, favorite, new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword()));
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "unfavoriting Opportunity failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			String id = (String) postResponse.get("id");
			log.debug("Id: " + id);
			Assert.assertFalse((Boolean)postResponse.get("my_favorite"));
		} catch (Exception e) {
			Assert.assertTrue(false, "Opportunity was not marked as unFavorite as expected.");
		}
		
		log.info("End test method Test_unFavoriteOpportunity.");
	}


	/*
	 * HELPERS
	 * -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	 */
	
	
	/*
	 * Creates an opportunity and returns the opportunity ID.
	 */
	public static String createOpportunityHelper(User user, Logger log, String url, Object self) {
		log.info("Creating an opportunity.");
		String opportunityId = "";
		try{
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(url, user.getEmail(), user.getPassword());
				
		String desc = "A description";
		String source = "RLPL";
		String salesStage = "03";
		String date = "2016-10-28";
		
		//get a client from the client pool
		String clientId = commonClientAllocator.getGroupClient(GC.SC, self).getCCMS_ID();
		String sessionID = new SugarAPI().getSessionID(baseURL, user.getEmail(), user.getPassword());
		//borrow the collab web APIs to get the bean id for the pool client
		String realClientId = new APIUtilities().getClientBeanIDFromCCMSID(url, clientId, user.getEmail(), user.getPassword(), sessionID);
		
		OpportunityRestAPI opportunityRestAPI = new OpportunityRestAPI();
		String jsonString = opportunityRestAPI.createOpportunity(url, token, desc, realClientId, ContactRestAPITests.createContactHelper(user, log, url), source, salesStage, date, new APIUtilities().getUserBeanIDFromEmail(url,user.getEmail(),user.getPassword(), sessionID));
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Opportunity creation failed.");
		} 
		
			try {
				JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
				opportunityId = (String) postResponse.get("id");
			} catch (Exception e) {
				Assert.assertTrue(false, "Opportunity was not created as expectd.");
			}
		}
		finally{
			commonClientAllocator.checkInAllGroupClientWithToken(GC.SC, self);	
		}
		return opportunityId;
	}
	
	public static String createOpportunityHelper(User user, Logger log, String url, Object self, String token) {
		log.info("Creating an opportunity.");
		String opportunityId = "";
		try{
				
		String desc = "A description";
		String source = "RLPL";
		String salesStage = "03";
		String date = "2016-10-28";
		
		//get a client from the client pool
		String clientId = commonClientAllocator.getGroupClient(GC.SC, self).getCCMS_ID();
		String sessionID = new SugarAPI().getSessionID(baseURL, user.getEmail(), user.getPassword());
		//borrow the collab web APIs to get the bean id for the pool client
		String realClientId = new APIUtilities().getClientBeanIDFromCCMSID(url, clientId, user.getEmail(), user.getPassword(), sessionID);
		
		OpportunityRestAPI opportunityRestAPI = new OpportunityRestAPI();
		String jsonString = opportunityRestAPI.createOpportunity(url, token, desc, realClientId, ContactRestAPITests.createContactHelper(user, log, url), source, salesStage, date, new APIUtilities().getUserBeanIDFromEmail(url,user.getEmail(),user.getPassword(), sessionID));
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Opportunity creation failed.");
		} 
		
			try {
				JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
				opportunityId = (String) postResponse.get("id");
			} catch (Exception e) {
				Assert.assertTrue(false, "Opportunity was not created as expectd.");
			}
		}
		finally{
			commonClientAllocator.checkInAllGroupClientWithToken(GC.SC, self);	
		}
		return opportunityId;
	}

	
}
