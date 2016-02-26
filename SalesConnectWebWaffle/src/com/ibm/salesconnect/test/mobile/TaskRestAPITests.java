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
import com.ibm.salesconnect.API.SugarAPI;
import com.ibm.salesconnect.API.TaskRestAPI;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;

public class TaskRestAPITests extends ProductBaseTest {
	
	Logger log = LoggerFactory.getLogger(TaskRestAPITests.class);
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>	 
	 * <li>Create a TASK</li>
	 * * <li>Search Created TASK</li>
	 * <li>Verify TASK Create as Expected</li>
	 * </ol>
	 */
	
	@Test(groups = {"MOBILE"})
	public void Test_createTask(){
		log.info("Start test method Test_createTask.");
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		log.info("Creating a task.");
		
		String name = "Task Name";
		String date = "2013-10-28T15:14:00.000Z";
		String priority = "High";
		String status = "Not Started";
		String type = "Close_out_call";
		String sessionID = new SugarAPI().getSessionID(baseURL, user.getEmail(), user.getPassword());
		TaskRestAPI taskRestAPI = new TaskRestAPI();
		String jsonString = taskRestAPI.createTask(testConfig.getBrowserURL(), token, name, date, priority, status, type, new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword(), sessionID));
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Task creation failed.");
		} else {
			
			try {
				JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
				String taskId = (String) postResponse.get("id");
				//validate the task was created as expected
				Assert.assertEquals((String) postResponse.get("name"), name, "Name was not returned as expected.  ");
				Assert.assertEquals((String) postResponse.get("priority"), priority, "Priority was not returned as expected.  ");
				Assert.assertEquals((String) postResponse.get("status"), status, "Status was not returned as expected.  ");
				Assert.assertEquals((String) postResponse.get("call_type"), type, "Type was not returned as expected.  ");
				Assert.assertEquals((String) postResponse.get("assigned_user_id"), new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword(), sessionID), "Assigned user id was not returned as expected.  ");
			} catch (Exception e) {
				Assert.assertTrue(false, "Task was not created as expected.");
			}
			
			log.info("Task successfully created.");
			
		}
		
		log.info("End test method Test_createTask.");
	}
	
	@Test(groups = {"MOBILE"})
	public void Test_relateNewContactToTask(){
		log.info("Start test method Test_relateNewContactToTask.");
		
		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Create a New Contact </li>
		 * <li>Related to New Contact to Task</li>
		 * <li>Verify New Contact is related to Task</li>
		 * </ol>
		 */
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		String taskId = TaskRestAPITests.createTaskHelper(user, log, testConfig.getBrowserURL());
		
		String firstName = "Ron";
		String lastName = "Burgandy";
		String address = "US";
		
		TaskRestAPI taskRestAPI = new TaskRestAPI();
		String jsonString = taskRestAPI.relateNewContactToTask(testConfig.getBrowserURL(), token, taskId, firstName, lastName, address, new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword()));
		log.info(jsonString);
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Relating Contact to task failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			JSONObject relatedRecord = (JSONObject) postResponse.get("related_record");
			Assert.assertEquals((String) record.get("id"), taskId, "Parent record was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Contact was not related to task as expectd.");
		}
		
		log.info("End test method Test_relateNewContactToTask.");
		
	}
	
	@Test(groups = {"MOBILE"})
	public void Test_relateNewNoteToTask(){
		log.info("Start test method Test_relateNewNoteToTask.");
		
		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Create a New Note </li>
		 * <li>Related to New Note to Task</li>
		 * <li>Verify New Note is related to Task</li>
		 * </ol>
		 */
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		String taskId = TaskRestAPITests.createTaskHelper(user, log, testConfig.getBrowserURL());
		
		String name = "A Note";
		String description = "Related note";
		
		TaskRestAPI taskRestAPI = new TaskRestAPI();
		String jsonString = taskRestAPI.relateNewNoteToTask(testConfig.getBrowserURL(), token, taskId, name, description, new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword()));
		log.info(jsonString);
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Relating Note to Task failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			Assert.assertEquals((String) record.get("id"), taskId, "Parent record was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Note was not related to Task as expectd.");
		}
		
		log.info("End test method Test_relateNewNoteToTask.");
		
	}
	
	@Test(groups = {"MOBILE"})
	public void Test_relateNewOpportunityToTask(){
		log.info("Start test method Test_relateNewOpportunityToTask.");
		
		
		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Create a New Oppty</li>
		 * <li>Related to New Oppty to Task</li>
		 * <li>Verify New Oppty is related to Task</li>
		 * </ol>
		 */
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		String taskId = TaskRestAPITests.createTaskHelper(user, log, testConfig.getBrowserURL());
		
		//get a client from the client pool
		String clientId = commonClientAllocator.getGroupClient(GC.SC, this).getCCMS_ID();
		String sessionID = new SugarAPI().getSessionID(baseURL, user.getEmail(), user.getPassword());
		String realClientId = new APIUtilities().getClientBeanIDFromCCMSID(testConfig.getBrowserURL(), clientId, user.getEmail(), user.getPassword(), sessionID);
		String contactId = ContactRestAPITests.createContactHelper(user, log, testConfig.getBrowserURL());
		
		String desc = "A description";
		String source = "RLPL";
		String salesStage = "03";
		String date = "2016-10-28";
		
		TaskRestAPI taskRestAPI = new TaskRestAPI();
		String jsonString = taskRestAPI.relateNewOpportunityToTask(testConfig.getBrowserURL(), token, taskId, desc, source, salesStage, date, realClientId, contactId, new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword(), sessionID));
		log.info(jsonString);
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Relating Opportunity to Task failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			Assert.assertEquals((String) record.get("id"), taskId, "Parent record was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Opportunity was not related to Task as expectd.");
		}
		
		log.info("End test method Test_relateNewOpportunityToTask.");
		
	}
	
	@Test(groups = {"MOBILE"})
	public void Test_relateNewTaskToTask(){
		log.info("Start test method Test_relateNewTaskToTask.");
		
		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Create a New Task </li>
		 * <li>Related to New Task to Existing Task</li>
		 * <li>Verify New Task is related to Task</li>
		 * </ol>
		 */
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		String taskId = TaskRestAPITests.createTaskHelper(user, log, testConfig.getBrowserURL());
		
		String name = "Related Task";
		String dueDate = "2013-10-29T06:00:00.000Z";
		String priority = "High";
		String status = "Not Started";
		String callType = "Close_out_call";
		
		TaskRestAPI taskRestAPI = new TaskRestAPI();
		String jsonString = taskRestAPI.relateNewTaskToTask(testConfig.getBrowserURL(), token, taskId, name, dueDate, priority, status, callType, new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword()));
		log.info(jsonString);
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Relating Task to Task failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			Assert.assertEquals((String) record.get("id"), taskId, "Parent record was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Task was not related to Task as expectd.");
		}
		
		log.info("End test method Test_relateNewTaskToTask.");
		
	}
	
	@Test(groups = {"MOBILE"})
	public void Test_linkContactToTask(){
		log.info("Start test method Test_linkContactToTask.");
		
		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Search TASK from the pool </li>
		 * <li>Create a New Contact to link </li>
		 * <li>Link New Conatct to Task</li>
		 * <li>Verify Conatct to TASK is Linked successfully </li>
		 * </ol>
		 */
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		String taskId = TaskRestAPITests.createTaskHelper(user, log, testConfig.getBrowserURL());
		
		//create a contact to link
		String contactId = ContactRestAPITests.createContactHelper(user, log, testConfig.getBrowserURL());
		
		TaskRestAPI taskRestAPI = new TaskRestAPI();
		String jsonString = taskRestAPI.linkRecordToTask(testConfig.getBrowserURL(), token, taskId, contactId, "Contacts");
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Relating contact to task failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			//validate the contact was related to call as expected
			Assert.assertEquals((String) record.get("id"), taskId, "Parent record was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Contact was not linked to Task as expectd.");
		}
		
		log.info("End test method Test_linkContactToTask.");
	}
	
	@Test(groups = {"MOBILE"})
	public void Test_linkNoteToTask(){
		log.info("Start test method Test_linkNoteToTask.");
		
		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Search TASK from the pool </li>
		 * <li>Create a New Note to link </li>
		 * <li>Link New Note to Task</li>
		 * <li>Verify Note to TASK is Linked successfully </li>
		 * </ol>
		 */
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		String taskId = TaskRestAPITests.createTaskHelper(user, log, testConfig.getBrowserURL());
		
		//create a note to link
		String noteId = NoteRestAPITests.createNoteHelper(user, log, testConfig.getBrowserURL());
		
		TaskRestAPI taskRestAPI = new TaskRestAPI();
		String jsonString = taskRestAPI.linkRecordToTask(testConfig.getBrowserURL(), token, taskId, noteId, "Notes");
		
		if (jsonString.equalsIgnoreCase("false")) 
		{
			Assert.assertTrue(false, "Relating note to Task failed.");
		} 
		
		try 
		{
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			//validate the note was related to call as expected
			Assert.assertEquals((String) record.get("id"), taskId, "Parent record was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Note was not linked to Task as expectd.");
		}
		
		log.info("End test method Test_linkNoteToTask.");
	}
	
	@Test(groups = {"MOBILE"})
	public void Test_linkTaskToTask(){
		log.info("Start test method Test_linkTaskToTask.");
		
		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Search TASK from the pool </li>
		 * <li>Create a New TASK to link </li>
		 * <li>Link New TASK to Task</li>
		 * <li>Verify TASK to TASK is Linked successfully </li>
		 * </ol>
		 */
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		String taskId1 = TaskRestAPITests.createTaskHelper(user, log, testConfig.getBrowserURL());
		
		//create a task to link
		String taskId2 = TaskRestAPITests.createTaskHelper(user, log, testConfig.getBrowserURL());
		
		TaskRestAPI taskRestAPI = new TaskRestAPI();
		String jsonString = taskRestAPI.linkRecordToTask(testConfig.getBrowserURL(), token, taskId1, taskId2, "Tasks");
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Linking task to task failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			//validate the task was related to call as expected
			Assert.assertEquals((String) record.get("id"), taskId1, "Parent record was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Task was not linked to Task as expectd.");
		}
		
		log.info("End test method Test_linkTaskToTask.");
	}
	
	@Test(groups = {"MOBILE"})
	public void Test_linkCallToTask(){
		log.info("Start test method Test_linkCallToTask.");
		
		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Search TASK from the pool </li>
		 * <li>Create a New Call to link </li>
		 * <li>Link New Callto Task</li>
		 * <li>Verify Call  to TASK is Linked successfully </li>
		 * </ol>
		 */
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		String taskId1 = TaskRestAPITests.createTaskHelper(user, log, testConfig.getBrowserURL());
		
		String callID = CallRestAPITests.createCallHelper(user, log, testConfig.getBrowserURL(), this);
		
		TaskRestAPI taskRestAPI = new TaskRestAPI();
		String jsonString = taskRestAPI.linkRecordToTask(testConfig.getBrowserURL(), token, taskId1, callID, "Calls");
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Linking call to task failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			//validate the task was related to call as expected
			Assert.assertEquals((String) record.get("id"), taskId1, "Parent record was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Call was not linked to Task as expectd.");
		}
		
		log.info("End test method Test_linkCallToTask.");
	}
	
	@Test(groups={"MOBILE"})
	public void Test_linkClientToTask()
	{
		log.info("start test for method Test_linkClientToTask");
		
		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Search TASK from the pool </li>
		 * <li>Create a New Client to link </li>
		 * <li>Link New Client to Task</li>
		 * <li>Verify Client to TASK is Linked successfully </li>
		 * </ol>
		 */
		
		log.info("getting user");
		
		User user=commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		
		LoginRestAPI loginRestAPI=new LoginRestAPI();
		
		String token=loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(),user.getEmail(), user.getPassword());
		
		String taskid=TaskRestAPITests.createTaskHelper(user, log, testConfig.getBrowserURL());
		
		String clientid=commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
		String realClientId=new APIUtilities().getClientBeanIDFromCCMSID(testConfig.getBrowserURL(), clientid, user.getEmail(), user.getPassword());
		
		TaskRestAPI taskRestAPI=new TaskRestAPI();
		String jsonString=taskRestAPI.linkRecordToTask(testConfig.getBrowserURL(), token, taskid, realClientId, "Accounts");
		
		if(jsonString.equalsIgnoreCase("false"))
		{
			Assert.assertTrue(false,"task was not client to a task");
		}
		try
		{
			JSONObject postResponse=(JSONObject)new JSONParser().parse(jsonString);
			
			JSONObject record =(JSONObject)postResponse.get("record");
			Assert.assertEquals((String)record.get("id"),taskid,"Parent record was not returned as expected.");
			JSONObject related_record =(JSONObject)postResponse.get("related_record");
			Assert.assertEquals((String)related_record.get("id"),realClientId,"Parent record was not returned as expected.");

		}
		catch(Exception e)
		{
			Assert.assertTrue(false,"task was not link to client as expected");
		}
		log.info("end test method for the Test_linkClientToTask");
	}
	@Test(groups = {"MOBILE"})
	public void Test_linkAdditionalAssigneeToTask(){
		log.info("Start test method Test_linkAdditionalAssigneeToTask.");
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		User user2 = commonUserAllocator.getUser(this);
		
		log.info("Getting user bean uid.");
		String userId =	new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user2.getEmail(),user2.getPassword());
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		String TaskId = TaskRestAPITests.createTaskHelper(user, log, testConfig.getBrowserURL());
		
		log.info("Linking assignee to Task.");
		TaskRestAPI TaskRestAPI = new TaskRestAPI();
		String jsonString = TaskRestAPI.linkRecordToTask(testConfig.getBrowserURL(), token, TaskId, userId, "Assignee");
				
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Linking Additional assignee to note failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			//validate the opportunity was related to call as expected
			Assert.assertEquals((String) record.get("id"), TaskId, "Parent record was not returned as expected.  ");
		} 
		catch (Exception e) 
		{
			Assert.assertTrue(false, "Assignee was not linked to Note as expectd.");
		}
		
		log.info("End test method Test_linkAdditionalAssigneeToTask.");
	}
	
	@Test(groups = {"MOBILE"})
	public void Test_linkOpportunityToTask(){
		log.info("Start test method Test_linkOpportunityToTask.");
		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Search TASK from the pool </li>
		 * <li>Create a New Oppty to link </li>
		 * <li>Link New Oppty to Task</li>
		 * <li>Verify Oppty to TASK is Linked successfully </li>
		 * </ol>
		 */
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		String taskId = TaskRestAPITests.createTaskHelper(user, log, testConfig.getBrowserURL());
		
		//create an opportunity to link
		String opportunityId = OpportunityRestApiTests.createOpportunityHelper(user, log, testConfig.getBrowserURL(), this);
		
		TaskRestAPI taskRestAPI = new TaskRestAPI();
		String jsonString = taskRestAPI.linkRecordToTask(testConfig.getBrowserURL(), token, taskId, opportunityId, "Opportunity");
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Linking Opportunity to task failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			//validate the opportunity was related to call as expected
			Assert.assertEquals((String) record.get("id"), taskId, "Parent record was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Opportunity was not linked to Task as expectd.");
		}
		
		log.info("End test method Test_linkOpportunityToTask.");
	}
	
	/*
	@Test(groups = {"MOBILE"})
	public void Test_linkUserToTask(){
		log.info("Start test method Test_linkUserToTask.");
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		User user2 = commonUserAllocator.getUser("newAssignee");
		String userId = user2.getUid(); //TODO: Fix - not record id

		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		String taskId = TaskRestAPITests.createTaskHelper(user, log, testConfig.getBrowserURL());
		
		TaskRestAPI taskRestAPI = new TaskRestAPI();
		String jsonString = taskRestAPI.linkRecordToTask(testConfig.getBrowserURL(), token, taskId, userId, "Assignee");
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Linking User to task failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			//validate the opportunity was related to call as expected
			Assert.assertEquals((String) record.get("id"), taskId, "Parent record was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "User was not linked to Task as expectd.");
		}
		
		log.info("End test method Test_linkUserToTask.");
	}
	*/
	
	@Test(groups = {"MOBILE"})
	public void Test_editTask(){
		log.info("Start test method Test_editTask.");
		
		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Search TASK from the pool </li>
		 * <li>Edit the Task  </li>
		  * <li>Verify TASK is Updated successfully </li>
		 * </ol>
		 */
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		String taskId = TaskRestAPITests.createTaskHelper(user, log, testConfig.getBrowserURL());
		
		String name = "Task Name Update";
		String priority = "Medium";
		String status = "Not Started";
		String type = "Technical_Sales_Activity";		
		
		TaskRestAPI taskRestAPI = new TaskRestAPI();
		String jsonString = taskRestAPI.editTask(testConfig.getBrowserURL(), token, taskId, name, priority, status, type, new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword()));
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Editing Task failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			String id = (String) postResponse.get("id");
			//validate the call was edited as expected
			Assert.assertEquals((String) postResponse.get("name"), name, "name was not returned as expected.  ");
			Assert.assertEquals((String) postResponse.get("priority"), priority, "priority was not returned as expected.  ");
			Assert.assertEquals((String) postResponse.get("status"), status, "status was not returned as expected.  ");
			Assert.assertEquals((String) postResponse.get("call_type"), type, "call type was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Task was not edited as expected.");
		}
		
		log.info("End test method Test_editTask.");
	}
	
	@Test(groups = {"MOBILE"})
	public void Test_favoriteTask(){
		log.info("Start test method Test_favoriteTask.");
		
		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Search TASK from the pool</li>
	     * <li>Convert the TASK as Favorite Task </li>
	     * <li>Verify TASK convert to Favorite TASK</li>
		 * </ol>
		 */
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());
		
		String favorite = "favorite";
		
		String taskId = TaskRestAPITests.createTaskHelper(user, log, testConfig.getBrowserURL());	
		
		TaskRestAPI taskRestAPI = new TaskRestAPI();
		String jsonString = taskRestAPI.favoriteTask(testConfig.getBrowserURL(), token, taskId, favorite, new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword()));
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "favoriting Task failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			String id = (String) postResponse.get("id");
			//validate the task was favorited as expected
			Assert.assertTrue((Boolean)postResponse.get("my_favorite"));
		} catch (Exception e) {
			Assert.assertTrue(false, "Task was mark as favorite as expected.");
		}
		
		log.info("End test method Test_favoriteTask.");
	}
	
	@Test(groups = {"MOBILE"})
	public void Test_unFavoriteTask(){
		log.info("Start test method Test_favoriteTask.");
		
		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Search Favorite TASK from the pool</li>
	     * <li>Convert the FAVORITETASK as UnFavorite Task </li>
	     * <li>Verify FAvoriteTask convert as UnFavorite TASK</li>
		 * </ol>
		 */
		
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());
		
		String favorite = "unfavorite";
		
		String taskId = TaskRestAPITests.createTaskHelper(user, log, testConfig.getBrowserURL());	
		
		TaskRestAPI taskRestAPI = new TaskRestAPI();
		String jsonString = taskRestAPI.favoriteTask(testConfig.getBrowserURL(), token, taskId, favorite, new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword()));
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "favoriting Task failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			String id = (String) postResponse.get("id");
			//validate the task was favorited as expected
			Assert.assertFalse((Boolean)postResponse.get("my_favorite"));
		} catch (Exception e) {
			Assert.assertTrue(false, "Task was mark as favorite as expected.");
		}
		
		log.info("End test method Test_favoriteTask.");
	}
	
	
	@Test(groups = {"MOBILE"})
	public void Test_deleteTask(){
		log.info("Start test method Test_deleteTask.");
		/**
		 * <br/><br/>
		 * Test steps
		 * <ol>
		 * <li>Search TASK from the pool</li>
		 * <li>Delete the TASK</li>
		 * <li>Verify TASK is deleted successfully </li>
		 * </ol>
		 */
		log.info("Getting user.");
		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());
		
		String taskId = TaskRestAPITests.createTaskHelper(user, log, testConfig.getBrowserURL());	
		
		TaskRestAPI taskRestAPI = new TaskRestAPI();
		String jsonString = taskRestAPI.deleteTask(testConfig.getBrowserURL(), token, taskId);
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Deleting Task failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			//validate the task was deleted as expected
			Assert.assertEquals((String) postResponse.get("id"), taskId, "Id was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Task was deleted as expected.");
		}
		
		log.info("End test method Test_deleteTask.");
	}
	
	/*
	 *  HELPERS
	 * -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	 */
	
	
	public static String createTaskHelper(User user, Logger log, String url){
		return createTaskHelper(user, "Task Name", log, url);
	}
	/*
	 * Creates a task and returns the task ID.
	 */
	public static String createTaskHelper(User user, String taskName, Logger log, String url){
		log.info("Retrieving OAuth2Token.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(url, user.getEmail(), user.getPassword());

		log.info("Creating a task.");
		
		String name = taskName;
		String date = "2013-10-28T15:14:00.000Z";
		String priority = "High";
		String status = "Not Started";
		String type = "Close_out_call";
		
		TaskRestAPI taskRestAPI = new TaskRestAPI();
		String jsonString = taskRestAPI.createTask(url, token, name, date, priority, status, type, new APIUtilities().getUserBeanIDFromEmail(url,user.getEmail(),user.getPassword()));
		
		String taskId = "";
			
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			taskId = (String) postResponse.get("id");
		} catch (Exception e) {
			Assert.assertTrue(false, "Task was not created as expected.");
		}
		
		log.info("Task successfully created.");
			
		return taskId;
	}

}
