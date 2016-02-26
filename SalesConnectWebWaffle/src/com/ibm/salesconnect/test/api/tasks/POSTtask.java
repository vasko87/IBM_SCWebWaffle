/**
 * 
 */
package com.ibm.salesconnect.test.api.tasks;

import java.util.HashMap;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.APIUtilities;
import com.ibm.salesconnect.API.ApiBaseTest;
import com.ibm.salesconnect.API.CallRestAPI;
import com.ibm.salesconnect.API.CollabWebAPI;
import com.ibm.salesconnect.API.ContactRestAPI;
import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.API.NoteRestAPI;
import com.ibm.salesconnect.API.OpportunityRestAPI;
import com.ibm.salesconnect.API.SugarAPI;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.common.HttpUtils;
import com.ibm.salesconnect.test.mobile.TaskRestAPITests;

/**
 * @author timlehane
 * @date Sep 25, 2014
 */
public class POSTtask extends ApiBaseTest {
	private static final Logger log = LoggerFactory.getLogger(POSTtask.class);
	
	@Parameters({ "apiExtension", "applicationName", "APIM", "apim_environment" })
	private POSTtask(@Optional("tasks") String apiExtension,
			@Optional("SC Auto create") String applicationName,
			@Optional("true") String APIM,
			@Optional("development") String environment) {

		super(apiExtension, applicationName, APIM, environment);
}
	
	static GETclientAndsecret callMethPOST = new GETclientAndsecret();	

    int rand = new Random().nextInt(1000000);
    String taskIDForFailures = baseURL;//TaskRestAPITests.createTaskHelper(commonUserAllocator.getUser(this),log,testConfig.getBrowserURL());; 
	
	  @Test
	    public void TestTask_RelateAdditionalAssigneesValidUser(){
	    	log.info("Starting TestTask_RelateAdditionalAssigneesValidUser");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			String assignedUserID = new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword());
			String taskID = TaskRestAPITests.createTaskHelper(user1,log,testConfig.getBrowserURL());
			
			String postResponseString = sendRequest(user1.getEmail(), user1.getPassword(), taskID, 
					"additional_assignees_link", assignedUserID);

			Assert.assertTrue(new APIUtilities().checkIfValuePresentInJson(postResponseString, "id", assignedUserID),
					"id " + assignedUserID + " is not present in related to c field but should be"); 
				
			log.info("Ending TestTask_RelateAdditionalAssigneesValidUser");
	    }
	  
	  @Test
	    public void TestTask_ValidateDate_Due(){
	    	log.info("Starting TestTask_ValidateDate_Due");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("name", "Task Name");
			map.put("date_due", "not a date");
			//map.put("date_due", "2013-10-28T15:14:00.000Z");
			map.put("priority", "High");
			map.put("status", "Not Started");
			
			String[] headers = {"OAuth-Token", getOAuthToken(user1)};
			
			HttpUtils restCalls = new HttpUtils();
			String postResponseString = restCalls.postRequest(getRequestUrl(null, null), 
					headers,new JSONObject(map), "application/json", "422");

				
			log.info("Ending TestTask_ValidateDate_Due");
	    }
	  
	  @Test
	    public void TestTask_ValidateDate_entered(){
	    	log.info("Starting TestTask_ValidateDate_entered");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("name", "Task Name");
			map.put("date_entered", "not a date");
			//map.put("date_due", "2013-10-28T15:14:00.000Z");
			map.put("priority", "High");
			map.put("status", "Not Started");
			
			String[] headers = {"OAuth-Token", getOAuthToken(user1)};
			
			HttpUtils restCalls = new HttpUtils();
			String postResponseString = restCalls.postRequest(getRequestUrl(null, null), 
					headers,new JSONObject(map), "application/json", "422");

				
			log.info("Ending TestTask_ValidateDate_entered");
	    }
	  
	  @Test
	    public void TestTask_ValidateDate_Modified(){
	    	log.info("Starting TestTask_ValidateDate_Modified");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("name", "Task Name");
			map.put("date_modified", "not a date");
			//map.put("date_due", "2013-10-28T15:14:00.000Z");
			map.put("priority", "High");
			map.put("status", "Not Started");
			
			String[] headers = {"OAuth-Token", getOAuthToken(user1)};
			
			HttpUtils restCalls = new HttpUtils();
			String postResponseString = restCalls.postRequest(getRequestUrl(null, null), 
					headers,new JSONObject(map), "application/json", "422");

				
			log.info("Ending TestTask_ValidateDate_Modified");
	    }
	  
	  @Test
	    public void TestTask_ValidateDate_Start(){
	    	log.info("Starting TestTask_ValidateDate_Start");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("name", "Task Name");
			map.put("date_start", "not a date");
			//map.put("date_due", "2013-10-28T15:14:00.000Z");
			map.put("priority", "High");
			map.put("status", "Not Started");
			
			String[] headers = {"OAuth-Token", getOAuthToken(user1)};
			
			HttpUtils restCalls = new HttpUtils();
			String postResponseString = restCalls.postRequest(getRequestUrl(null, null), 
					headers,new JSONObject(map), "application/json", "422");

				
			log.info("Ending TestTask_ValidateDate_Start");
	    }
	  
	  @Test
	    public void TestTask_ValidateExtTasksDate(){
	    	log.info("Starting TestTask_ValidateExtTasksDate");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			
			String[] headers = {"OAuth-Token", getOAuthToken(user1)};
			for (int i = 1; i < 21; i++) {
				
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("name", "Task Name");
			map.put("ext_tasks_date_"+i+"_c", "not a date");
			//map.put("date_due", "2013-10-28T15:14:00.000Z");
			map.put("priority", "High");
			map.put("status", "Not Started");
			
			HttpUtils restCalls = new HttpUtils();
			String postResponseString = restCalls.postRequest(getRequestUrl(null, null), 
					headers,new JSONObject(map), "application/json", "422");
			}

				
			log.info("Ending TestTask_ValidateExtTasksDate");
	    }
	  
	  @Test
	    public void TestTask_ValidateExtTasksDateTime(){
	    	log.info("Starting TestTask_ValidateExtTasksDateTime");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			String[] headers = {"OAuth-Token", getOAuthToken(user1)};
			for (int i = 1; i < 21; i++) {
				
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("name", "Task Name");
			map.put("ext_tasks_datetime_"+i+"_c", "not a date");
			map.put("priority", "High");
			map.put("status", "Not Started");
			
			HttpUtils restCalls = new HttpUtils();
			String postResponseString = restCalls.postRequest(getRequestUrl(null, null), 
					headers,new JSONObject(map), "application/json", "422");
			}
				
			log.info("Ending TestTask_ValidateExtTasksDateTime");
	    }
	  
	  @Test
	    public void TestTask_RelateAdditionalAssigneesInValidUser(){
	    	log.info("Starting TestTask_RelateAdditionalAssigneesInValidUser");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
				
			sendRequest(user1.getEmail(), user1.getPassword(), taskIDForFailures, 
					"additional_assignees_link", "invalid", "404");
				
			log.info("Ending TestTask_RelateAdditionalAssigneesInValidUser");
	    }
	  
	  @Test
	    public void TestTask_RelateAdditionalAssigneesInValidOriginalTask(){
	    	log.info("Starting TestTask_RelateAdditionalAssigneesInValidOriginalTask");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			String assignedUserID = new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword());
			
			sendRequest(user1.getEmail(), user1.getPassword(), "invalidTask", "additional_assignees_link", assignedUserID, "404");
				
			log.info("Ending TestTask_RelateAdditionalAssigneesInValidOriginalTask");
	    }

	  //@Test
	    public void TestTask_RelateAdditionalAssigneesValidUserCNUM(){
	    	log.info("Starting TestTask_RelateAdditionalAssigneesValidUserCNUM");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			String taskID = TaskRestAPITests.createTaskHelper(user1,log,testConfig.getBrowserURL());
			
			log.info("Retrieving OAuth2Token.");					
			String headers[]= getOAuthTokenInHeader(user1);
					
			HttpUtils restCalls = new HttpUtils();
			String postResponseString = restCalls.postRequest(getRequestUrl("/" + taskID + "/link/" + user1.getUid() + "/additional_assignees_link/", null), headers, "", "application/json", "200");
				 
			log.info("Response String "+ postResponseString);
			try {
				//check for a valid JSON response
				new JSONParser().parse(postResponseString);
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
			//sendRequest(user1.getEmail(), user1.getPassword(), taskID, 
			//		"additional_assignees_link/id_field/employee_cnum", user1.getUid());


			log.info("Ending TestTask_RelateAdditionalAssigneesValidUserCNUM");
	    }
	  
	  
	  
	  @Test
	    public void TestTask_RelateAdditionalAssigneesValidUserInBodyCNUM(){
	    	log.info("Starting TestTask_RelateAdditionalAssigneesValidUserInBodyCNUM");

			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			String assignedUserID = new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword());

			User user2 = commonUserAllocator.getUser(this);
			HashMap <String, Object> map = getValidTaskBody();
			map.put("employee_cnum", user1.getUid());
			String postResponseString = sendRequest(user2.getEmail(), user2.getPassword(), new JSONObject(map), "200");
			
			Assert.assertTrue(new APIUtilities().checkIfValuePresentInJson(postResponseString, "assigned_user_id", assignedUserID),
						"assigned user " + assignedUserID + " is not present in related field but should be"); 

			log.info("Ending TestTask_RelateAdditionalAssigneesValidUserInBodyCNUM");
	    }
	  
	  //@Test
	    public void TestTask_RelateAdditionalAssigneesInValidUserInBodyCNUM(){
	    	log.info("Starting TestTask_RelateAdditionalAssigneesInValidUserInBodyCNUM");

			User user2 = commonUserAllocator.getUser(this);
			
			HashMap <String, Object> map = getValidTaskBody();
			map.put("employee_cnum", "InvalidCNUM");
			String postResponseString = sendRequest(user2.getEmail(), user2.getPassword(), new JSONObject(map), "404");

			log.info("Ending TestTask_RelateAdditionalAssigneesInValidUserInBodyCNUM");
	    }
	  
	  @Test
	    public void TestTask_RelateAdditionalAssigneesInValidUserCNUM(){
	    	log.info("Starting TestTask_RelateAdditionalAssigneesInValidUserCNUM");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			
			sendRequest(user1.getEmail(), user1.getPassword(), taskIDForFailures, 
					"additional_assignees_link/id_field/employee_cum", "invaliduser", "404");
				
			log.info("Ending TestTask_RelateAdditionalAssigneesInValidUserCNUM");
	    }
	  
	  @Test
	    public void TestTask_RelateAdditionalAssigneesInValidOriginalTaskCNUM(){
	    	log.info("Starting TestTask_RelateAdditionalAssigneesInValidOriginalTaskCNUM");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			
			sendRequest(user1.getEmail(), user1.getPassword(), "invalidTask", "additional_assignees_link/id_field/employee_cum",
					"-0505K754", "404");
				
			log.info("Ending TestTask_RelateAdditionalAssigneesInValidOriginalTaskCNUM");
	    }
	  
	  @Test
	    public void TestTask_RelateTaskValid(){
	    	log.info("Starting TestTask_RelateTaskValid");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			String taskID = TaskRestAPITests.createTaskHelper(user1,log,testConfig.getBrowserURL());
			String LinktaskID = TaskRestAPITests.createTaskHelper(user1,log,testConfig.getBrowserURL());
			
			String postResponseString = sendRequest(user1.getEmail(), user1.getPassword(), taskID, "tasks", LinktaskID);
			
			Assert.assertTrue(new APIUtilities().checkIfValuePresentInJson(postResponseString, "related_id", LinktaskID),
						"id " + LinktaskID + " is not present in related field but should be"); 

			log.info("Ending TestTask_RelateTaskValid");
	    }
	  
	  @Test
	    public void TestTask_RelateTaskInValid(){
	    	log.info("Starting TestTask_RelateTaskInValid");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
					
			sendRequest(user1.getEmail(), user1.getPassword(), taskIDForFailures, "tasks", "invalidTask", "404");
				
			log.info("Ending TestTask_RelateTaskInValid");
	    }
	  
	  @Test
	    public void TestTask_RelateTaskInValidOriginalTask(){
	    	log.info("Starting TestTask_RelateTaskInValid");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			String taskID = TaskRestAPITests.createTaskHelper(user1,log,testConfig.getBrowserURL());
			
			sendRequest(user1.getEmail(), user1.getPassword(), "invalidTask", "tasks", taskID, "404");
				
			log.info("Ending TestTask_RelateTaskInValid");
	    }
	  
	  @Test
	    public void TestTask_RelateAccountValid(){
	    	log.info("Starting TestTask_RelateAccountValid");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			String taskID = TaskRestAPITests.createTaskHelper(user1,log,testConfig.getBrowserURL());
			String headers[] = {"OAuth-Token", new LoginRestAPI().getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};
			String clientBeanID = new CollabWebAPI().getbeanIDfromClientID(testConfig.getBrowserURL(), commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID(), 
					headers );
			
			String postResponseString = sendRequest(user1.getEmail(), user1.getPassword(), taskID, "accounts", clientBeanID);
			
			Assert.assertTrue(new APIUtilities().checkIfValuePresentInJson(postResponseString, "related_id", clientBeanID),
						"id " + clientBeanID + " is not present in related field but should be"); 

			log.info("Ending TestTask_RelateAccountValid");
	    }
	  
	  //@Test
	    public void TestTask_RelateAccountCCMS_IDValid(){
	    	log.info("Starting TestTask_RelateAccountCCMS_IDValid");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			PoolClient client = commonClientAllocator.getGroupClient(GC.DC,this);
			String headers[] = {"OAuth-Token", new LoginRestAPI().getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};
			
			String clientBeanID = new CollabWebAPI().getbeanIDfromClientID(testConfig.getBrowserURL(), client.getCCMS_ID(), 
					headers );

			String[] array = {"related_id",client.getCCMS_ID(), "related_type", "Accounts", "related_name", client.getClientName(testConfig.getBrowserURL(), user1), "id_field", "ccms_id"};
			HashMap<String, String> relatedToC = getRelatedToC(array);
			HashMap <String, Object> map = getValidTaskBody();
			JSONArray jsonArray = new JSONArray();
			jsonArray.add(relatedToC);
			map.put("related_to_c", jsonArray);
			String postResponseString = sendRequest(user1.getEmail(), user1.getPassword(), new JSONObject(map), "200");
			
			Assert.assertTrue(new APIUtilities().checkIfValuePresentInJson(postResponseString, "related_id", clientBeanID),
						"id " + clientBeanID + " is not present in related field but should be"); 

			log.info("Ending TestTask_RelateAccountCCMS_IDValid");
	    }
	  
	  //@Test
	    public void TestTask_RelateAccountCCMS_IDSendBeanID(){
	    	log.info("Starting TestTask_RelateAccountCCMS_IDSendBeanID");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			PoolClient client = commonClientAllocator.getGroupClient(GC.DC,this);
			String headers[] = {"OAuth-Token", new LoginRestAPI().getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};
			
			String clientBeanID = new CollabWebAPI().getbeanIDfromClientID(testConfig.getBrowserURL(), client.getCCMS_ID(), 
					headers );

			String[] array = {"related_id",clientBeanID, "related_type", "Accounts", "related_name", client.getClientName(testConfig.getBrowserURL(), user1), "id_field", "ccms_id"};
			HashMap<String, String> relatedToC = getRelatedToC(array);
			HashMap <String, Object> map = getValidTaskBody();
			JSONArray jsonArray = new JSONArray();
			jsonArray.add(relatedToC);
			map.put("related_to_c", jsonArray);
			String postResponseString = sendRequest(user1.getEmail(), user1.getPassword(), new JSONObject(map), "404");

			log.info("Ending TestTask_RelateAccountCCMS_IDSendBeanID");
	    }
	  
	  @Test
	    public void TestTask_RelateAccountInValid(){
	    	log.info("Starting TestTask_RelateAccountInValid");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
					
			sendRequest(user1.getEmail(), user1.getPassword(), taskIDForFailures, "accounts", "invalidAccount", "404");
				
			log.info("Ending TestTask_RelateAccountInValid");
	    }
	  
	  @Test
	    public void TestTask_RelateAccountInValidOriginalTask(){
	    	log.info("Starting TestTask_RelateAccountInValidOriginalTask");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			String headers[] = {"OAuth-Token", new LoginRestAPI().getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};
			String clientBeanID = new CollabWebAPI().getbeanIDfromClientID(testConfig.getBrowserURL(), commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID(), 
					headers );
			
			sendRequest(user1.getEmail(), user1.getPassword(), "invalidTask", "accounts", clientBeanID, "404");
				
			log.info("Ending TestTask_RelateAccountInValidOriginalTask");
	    }
	  
	  @Test
	    public void TestTask_RelateContactValid(){
	    	log.info("Starting TestTask_RelateContactValid");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			User user2 = commonUserAllocator.getUser(this);
			String taskID = TaskRestAPITests.createTaskHelper(user1,log,testConfig.getBrowserURL());

			log.info("Creating contact");
			String headers[] = {"OAuth-Token", new LoginRestAPI().getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};
			String assignedUserID = new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(), user1.getEmail(), 
					user1.getPassword());	
			String contactBeanID = new ContactRestAPI().createContactreturnBean(testConfig.getBrowserURL(), headers, 
					"ContactFirst", "ContactLast", "US", "(555) 555-5555", assignedUserID);
			
			String postResponseString = sendRequest(user1.getEmail(), user1.getPassword(), taskID, "contacts", contactBeanID);
			
			Assert.assertTrue(new APIUtilities().checkIfValuePresentInJson(postResponseString, "related_id", contactBeanID),
						"id " + contactBeanID + " is not present in related field but should be"); 

			log.info("Ending TestTask_RelateContactValid");
	    }
	  
	  @Test
	    public void TestTask_RelateAccountCIConctactIDValid(){
	    	log.info("Starting TestTask_RelateAccountCIConctactIDValid");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);

			log.info("Creating contact");
			String headers[] = {"OAuth-Token", new LoginRestAPI().getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};
			String assignedUserID = new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(), user1.getEmail(), 
					user1.getPassword());	
			String CIContactID = new ContactRestAPI().createContactreturnCIContactID(testConfig.getBrowserURL(), headers, 
					"ContactFirst", "ContactLast", "US", "(555) 555-5555", assignedUserID);

			String[] array = {"related_id",CIContactID, "related_type", "Contacts", "related_name", "ContactFirst ContactLast", "id_field", "ext_ref_id1_c"};
			HashMap<String, String> relatedToC = getRelatedToC(array);
			HashMap <String, Object> map = getValidTaskBody();
			JSONArray jsonArray = new JSONArray();
			jsonArray.add(relatedToC);
			map.put("related_to_c", jsonArray);
			String postResponseString = sendRequest(user1.getEmail(), user1.getPassword(), new JSONObject(map), "200");
			
			Assert.assertTrue(new APIUtilities().checkIfValuePresentInJson(postResponseString, "related_id", CIContactID),
						"id " + CIContactID + " is not present in related field but should be"); 

			log.info("Ending TestTask_RelateAccountCIConctactIDValid");
	    }
	  
	  @Test
	    public void TestTask_RelateOtherUsersContact(){
	    	log.info("Starting TestTask_RelateOtherUsersContact");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			User user2 = commonUserAllocator.getUser(this);
			String taskID = TaskRestAPITests.createTaskHelper(user1,log,testConfig.getBrowserURL());

			log.info("Creating contact");
			String headers[] = {"OAuth-Token", new LoginRestAPI().getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};
			String assignedUserID = new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(), user1.getEmail(), 
					user1.getPassword());	
			String contactBeanID = new ContactRestAPI().createContactreturnBean(testConfig.getBrowserURL(), headers, 
					"ContactFirst", "ContactLast", "US", "(555) 555-5555", assignedUserID);
			
			String postResponseString = sendRequest(user2.getEmail(), user2.getPassword(), taskID, "contacts", contactBeanID,"200");			

			log.info("Ending TestTask_RelateOtherUsersContact");
	    }
	  
	  @Test
	    public void TestTask_RelateContactInValid(){
	    	log.info("Starting TestTask_RelateContactInValid");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
					
			sendRequest(user1.getEmail(), user1.getPassword(), taskIDForFailures, "contacts", "invalidContact", "404");
				
			log.info("Ending TestTask_RelateContactInValid");
	    }
	  
	  @Test
	    public void TestTask_RelateContactInValidOriginalTask(){
	    	log.info("Starting TestTask_RelateContactInValidOriginalTask");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			
			log.info("Creating contact");
			String headers[] = {"OAuth-Token", new LoginRestAPI().getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};
			String assignedUserID = new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(), user1.getEmail(), 
					user1.getPassword());	
			String contactBeanID = new ContactRestAPI().createContactreturnBean(testConfig.getBrowserURL(), headers, 
					"ContactFirst", "ContactLast", "US", "(555) 555-5555", assignedUserID);
			
			sendRequest(user1.getEmail(), user1.getPassword(), "invalidTask", "contacts", contactBeanID, "404");
				
			log.info("Ending TestTask_RelateContactInValidOriginalTask");
	    }
	  
	  @Test
	    public void TestTask_RelateDACHContactNonDACHUser(){
	    	log.info("Starting TestTask_RelateDACHContact");
			User user1 = commonUserAllocator.getUser(this);
			String taskID = TaskRestAPITests.createTaskHelper(user1,log,testConfig.getBrowserURL());

			log.info("Creating contact");
			String headers[] = {"OAuth-Token", new LoginRestAPI().getOAuth2Token(testConfig.getBrowserURL(), "de01@tst.ibm.com", user1.getPassword())};
			String assignedUserID = new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(), "de01@tst.ibm.com", 
					user1.getPassword());	
			String contactBeanID = new ContactRestAPI().createContactreturnBean(testConfig.getBrowserURL(), headers, 
					"ContactFirst", "ContactLast", "DE", "(555) 555-5555", assignedUserID);
			
			sendRequest(user1.getEmail(), user1.getPassword(), taskID, "contacts", contactBeanID,"200");

			log.info("Ending TestTask_RelateDACHContact");
	    }
	  
	  @Test
	    public void TestTask_RelateDACHContactDACHUser(){
	    	log.info("Starting TestTask_RelateDACHContact");
			User user1 = commonUserAllocator.getUser(this);
			String taskID = TaskRestAPITests.createTaskHelper(user1,log,testConfig.getBrowserURL());

			log.info("Creating contact");
			String headers[] = {"OAuth-Token", new LoginRestAPI().getOAuth2Token(testConfig.getBrowserURL(), "de01@tst.ibm.com", user1.getPassword())};
			String assignedUserID = new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(), "de01@tst.ibm.com", 
					user1.getPassword());	
			String contactBeanID = new ContactRestAPI().createContactreturnBean(testConfig.getBrowserURL(), headers, 
					"ContactFirst", "ContactLast", "DE", "(555) 555-5555", assignedUserID);

			sendRequest("de01@tst.ibm.com", user1.getPassword(), taskID, "contacts", contactBeanID,"200");

			log.info("Ending TestTask_RelateDACHContact");
	    }
	  
	  @Test
	    public void TestTask_RelateNoteValid(){
	    	log.info("Starting TestTask_RelateNoteValid");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			String taskID = TaskRestAPITests.createTaskHelper(user1,log,testConfig.getBrowserURL());

			log.info("Creating note");
			String assignedUserID = new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword());	
			String noteID = new NoteRestAPI().createNotereturnBean(testConfig.getBrowserURL(), 
					new LoginRestAPI().getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword()), "Note Subject", 
					"post task note description", assignedUserID);

			
			String postResponseString = sendRequest(user1.getEmail(), user1.getPassword(), taskID, "notes", noteID);
			
			Assert.assertTrue(new APIUtilities().checkIfValuePresentInJson(postResponseString, "id", noteID),
						"id " + noteID + " is not present in related field but should be"); 

			log.info("Ending TestTask_RelateNoteValid");
	    }
	  
	  @Test
	    public void TestTask_RelateNoteInValid(){
	    	log.info("Starting TestTask_RelateNoteInValid");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
					
			sendRequest(user1.getEmail(), user1.getPassword(), taskIDForFailures, "notes", "invalidNote", "404");
				
			log.info("Ending TestTask_RelateNoteInValid");
	    }
	  
	  @Test
	    public void TestTask_RelateNoteInValidOriginalTask(){
	    	log.info("Starting TestTask_RelateNoteInValidOriginalTask");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			
			log.info("Creating note");
			String assignedUserID = new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword());	
			String noteID = new NoteRestAPI().createNotereturnBean(testConfig.getBrowserURL(), 
					new LoginRestAPI().getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword()), "Note Subject", 
					"post task note description", assignedUserID);
			
			sendRequest(user1.getEmail(), user1.getPassword(), "invalidTask", "notes", noteID, "404");
				
			log.info("Ending TestTask_RelateNoteInValidOriginalTask");
	    }
	  
	  @Test
	    public void TestTask_RelateCallValid(){
	    	log.info("Starting TestTask_RelateCallValid");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			String taskID = TaskRestAPITests.createTaskHelper(user1,log,testConfig.getBrowserURL());

			log.info("Creating call");
			String assignedUserID = new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword());	
			String callID = new CallRestAPI().createCallreturnBean(testConfig.getBrowserURL(), 
					new LoginRestAPI().getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword()), 
					"call subject", "2013-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);
			
			String postResponseString = sendRequest(user1.getEmail(), user1.getPassword(), taskID, "calls", callID);
			
			Assert.assertTrue(new APIUtilities().checkIfValuePresentInJson(postResponseString, "id", callID),
						"id " + callID + " is not present in related field but should be"); 

			log.info("Ending TestTask_RelateCallValid");
	    }
	  
	  @Test
	    public void TestTask_RelateCallInValid(){
	    	log.info("Starting TestTask_RelateCallInValid");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
					
			sendRequest(user1.getEmail(), user1.getPassword(), taskIDForFailures, "calls", "invalidCall", "404");
				
			log.info("Ending TestTask_RelateCallInValid");
	    }
	  
	  @Test
	    public void TestTask_RelateCallInValidOriginalTask(){
	    	log.info("Starting TestTask_RelateCallInValidOriginalTask");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			
			log.info("Creating call");
			String assignedUserID = new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword());	
			String callID = new CallRestAPI().createCallreturnBean(testConfig.getBrowserURL(), 
					new LoginRestAPI().getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword()), 
					"call subject", "2013-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);
			
			sendRequest(user1.getEmail(), user1.getPassword(), "invalidTask", "calls", callID, "404");
				
			log.info("Ending TestTask_RelateCallInValidOriginalTask");
	    }
	  
	  @Test
	    public void TestTask_RelateOpptyValid(){
	    	log.info("Starting TestTask_RelateOpptyValid");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			String taskID = TaskRestAPITests.createTaskHelper(user1,log,testConfig.getBrowserURL());
			String opptyID = null;
			
			log.info("Creating contact");
			String clientID = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
			String headers[] = {"OAuth-Token", new LoginRestAPI().getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};
			String assignedUserID = new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(), user1.getEmail(), 
					user1.getPassword());	
			String contactBeanID = new ContactRestAPI().createContactreturnBean(testConfig.getBrowserURL(), headers, 
					"ContactFirst", "ContactLast", "US", "(555) 555-5555", assignedUserID);
			
			log.info("Creating oppty");
			opptyID = new SugarAPI().createOppty(testConfig.getBrowserURL(),"", contactBeanID, clientID, user1.getEmail(), user1.getPassword(), GC.emptyArray);
			
			String postResponseString = sendRequest(user1.getEmail(), user1.getPassword(), taskID, "opportunities", opptyID);
			
			Assert.assertTrue(new APIUtilities().checkIfValuePresentInJson(postResponseString, "related_id", opptyID),
						"id " + opptyID + " is not present in related field but should be"); 

			log.info("Ending TestTask_RelateOpptyValid");
	    }
	  
	  @Test
	    public void TestTask_RelateRestrictedOpptyValid(){
	    	log.info("Starting TestTask_RelateRestrictedOpptyValid");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			User user2 = commonUserAllocator.getUser(this);
			String taskID = TaskRestAPITests.createTaskHelper(user1,log,testConfig.getBrowserURL());
			String opptyID = null;
			
			log.info("Creating contact");
			String clientID = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
			String headers[] = {"OAuth-Token", new LoginRestAPI().getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};
			String assignedUserID = new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(), user1.getEmail(), 
					user1.getPassword());	
			String clientbeanID = new CollabWebAPI().getbeanIDfromClientID(baseURL, clientID, headers );
			String contactBeanID = new ContactRestAPI().createContactreturnBean(testConfig.getBrowserURL(), headers, 
					"ContactFirst", "ContactLast", "US", "(555) 555-5555", assignedUserID);
			
			log.info("Creating oppty");

			opptyID = new OpportunityRestAPI().createRestrictedOpportunitySpecifyID(testConfig.getBrowserURL(), new LoginRestAPI().getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword()), "Restricted Oppty", clientbeanID, contactBeanID,
					"SLSP", "03", "2013-10-28", assignedUserID, "RESTOPTY");
			
			String postResponseString = sendRequest(user2.getEmail(), user2.getPassword(), taskID, "opportunities", opptyID,"200");
			
			Assert.assertTrue(new APIUtilities().checkIfValuePresentInJson(postResponseString, "related_id", opptyID),
						"id " + opptyID + " is not present in related field but should be"); 

			log.info("Ending TestTask_RelateRestrictedOpptyValid");
	    }
	  
	  @Test
	    public void TestTask_RelateRestrictedclientValidUser(){
	    	log.info("Starting TestTask_RelateRestrictedclientValidUser");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			User user2 = commonUserAllocator.getUser(this);
			String taskID = TaskRestAPITests.createTaskHelper(user1,log,testConfig.getBrowserURL());
			
			log.info("Creating contact");
			String clientID = commonClientAllocator.getGroupClient("RES",this).getCCMS_ID();
			String headers[] = {"OAuth-Token", new LoginRestAPI().getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};

			String clientbeanID = new CollabWebAPI().getbeanIDfromClientID(baseURL, clientID, headers );
			
			String postResponseString = sendRequest(user1.getEmail(), user1.getPassword(), taskID, "accounts", clientbeanID,"200");
			
			Assert.assertTrue(new APIUtilities().checkIfValuePresentInJson(postResponseString, "related_id", clientbeanID),
						"id " + clientbeanID + " is not present in related field but should be"); 

			log.info("Ending TestTask_RelateRestrictedclientValidUser");
	    }
	  
	  @Test
	    public void TestTask_RelateRestrictedclientInValidUser(){
	    	log.info("Starting TestTask_RelateRestrictedclientInValidUser");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			User user2 = commonUserAllocator.getUser(this);
			String taskID = TaskRestAPITests.createTaskHelper(user1,log,testConfig.getBrowserURL());
			
			log.info("Creating contact");
			String clientID = commonClientAllocator.getGroupClient("RES",this).getCCMS_ID();
			String headers[] = {"OAuth-Token", new LoginRestAPI().getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};

			String clientbeanID = new CollabWebAPI().getbeanIDfromClientID(baseURL, clientID, headers );
			
			String postResponseString = sendRequest(user2.getEmail(), user2.getPassword(), taskID, "accounts", clientbeanID,"404");

			log.info("Ending TestTask_RelateRestrictedclientInValidUser");
	    }
	  
	  @Test
	    public void TestTask_RelateOpptyInValid(){
	    	log.info("Starting TestTask_RelateOpptyInValid");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
					
			sendRequest(user1.getEmail(), user1.getPassword(), taskIDForFailures, "opportunities", "invalidOppty", "404");
				
			log.info("Ending TestTask_RelateOpptyInValid");
	    }
	  
	  @Test
	    public void TestTask_RelateOpptyInValidOriginalTask(){
	    	log.info("Starting TestTask_RelateOpptyInValidOriginalTask");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			String opptyID = null;
			
			log.info("Creating contact");
			String clientID = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
			String headers[] = {"OAuth-Token", new LoginRestAPI().getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};
			String assignedUserID = new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(), user1.getEmail(), 
					user1.getPassword());	
			String contactBeanID = new ContactRestAPI().createContactreturnBean(testConfig.getBrowserURL(), headers, 
					"ContactFirst", "ContactLast", "US", "(555) 555-5555", assignedUserID);
			
			log.info("Creating oppty");
			opptyID = new SugarAPI().createOppty(testConfig.getBrowserURL(), "", contactBeanID, clientID, user1.getEmail(), user1.getPassword(), GC.emptyArray);

			sendRequest(user1.getEmail(), user1.getPassword(), "invalidTask", "opportunities", opptyID, "404");
				
			log.info("Ending TestTask_RelateOpptyInValidOriginalTask");
	    }
	  
	  @Test
	    public void TestTask_LinkNewTaskToTask(){
	    	log.info("Starting TestTask_LinkNewTaskToTask");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			String taskID = TaskRestAPITests.createTaskHelper(user1,log,testConfig.getBrowserURL());
			
			log.info("Retrieving OAuth2Token.");			
			String headers[]= getOAuthTokenInHeader(user1);
		
			JSONObject body = new JSONObject(getValidTaskBody());
						
			HttpUtils restCalls = new HttpUtils();
			String postResponseString = restCalls.postRequest(getRequestUrl("/" + taskID + "/link/tasks", null) ,headers, body.toString(), "application/json", "200");
			 
			try {
				//check for a valid JSON response
				new JSONParser().parse(postResponseString);
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
			
			Assert.assertTrue(!new APIUtilities().returnValuePresentInJson(postResponseString, "related_id").equals(""));
				
			log.info("Ending TestTask_LinkNewTaskToTask");
	    }
	  
	  @Test
	    public void TestTask_LinkNewAccountToTask(){
	    	log.info("Starting TestTask_LinkNewAccountToTask");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			
			log.info("Retrieving OAuth2Token.");	
			String headers[]= getOAuthTokenInHeader(user1);
					
			HttpUtils restCalls = new HttpUtils();
			String postResponseString = restCalls.postRequest(getRequestUrl("/" + taskIDForFailures +  "/link/accounts", null) ,headers, "", "application/json", "405");
			 
			try {
				//check for a valid JSON response
				new JSONParser().parse(postResponseString);
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
			
			Assert.assertTrue(!new APIUtilities().returnValuePresentInJson(postResponseString, "related_id").equals(""));
				
			log.info("Ending TestTask_LinkNewAccountToTask");
	    }
	  
	  @Test
	    public void TestTask_LinkNewContactToTask(){
	    	log.info("Starting TestTask_LinkNewContactToTask");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			
			log.info("Retrieving OAuth2Token.");		
			String headers[]= getOAuthTokenInHeader(user1);
					
			HttpUtils restCalls = new HttpUtils();
			String postResponseString = restCalls.postRequest(getRequestUrl("/" + taskIDForFailures +  "/link/contacts", null) ,headers, "", "application/json", "405");
			 
			try {
				//check for a valid JSON response
				new JSONParser().parse(postResponseString);
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
			
			Assert.assertTrue(!new APIUtilities().returnValuePresentInJson(postResponseString, "related_id").equals(""));
				
			log.info("Ending TestTask_LinkNewContactToTask");
	    }
	  
	  @Test
	    public void TestTask_LinkNewNoteToTask(){
	    	log.info("Starting TestTask_LinkNewNoteToTask");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			
			log.info("Retrieving OAuth2Token.");			
			String headers[]= getOAuthTokenInHeader(user1);
					
			HttpUtils restCalls = new HttpUtils();
			String postResponseString = restCalls.postRequest(getRequestUrl("/" + taskIDForFailures + "/link/notes", null) ,headers, "", "application/json", "405");
			 
			try {
				//check for a valid JSON response
				new JSONParser().parse(postResponseString);
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
			
			Assert.assertTrue(!new APIUtilities().returnValuePresentInJson(postResponseString, "related_id").equals(""));
				
			log.info("Ending TestTask_LinkNewNoteToTask");
	    }
	  
	  @Test
	    public void TestTask_LinkNewCallToTask(){
	    	log.info("Starting TestTask_LinkNewCallToTask");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			
			log.info("Retrieving OAuth2Token.");
			String headers[]= getOAuthTokenInHeader(user1);
					
			HttpUtils restCalls = new HttpUtils();
			String postResponseString = restCalls.postRequest(getRequestUrl("/" + taskIDForFailures + "/link/calls", null) ,headers, "", "application/json", "405");
			 
			try {
				//check for a valid JSON response
				new JSONParser().parse(postResponseString);
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
			
			Assert.assertTrue(!new APIUtilities().returnValuePresentInJson(postResponseString, "related_id").equals(""));
				
			log.info("Ending TestTask_LinkNewCallToTask");
	    }
	  
	  @Test
	    public void TestTask_LinkNewAdditionalAssigneesToTask(){
	    	log.info("Starting TestTask_LinkNewAdditionalAssigneesToTask");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			
			log.info("Retrieving OAuth2Token.");		
			String headers[]= getOAuthTokenInHeader(user1);
					
			HttpUtils restCalls = new HttpUtils();
			String postResponseString = restCalls.postRequest(getRequestUrl("/" + taskIDForFailures + "/link/additional_assigness_link", null) ,headers, "", "application/json", "405");
		 		 
			try {
				//check for a valid JSON response
				new JSONParser().parse(postResponseString);
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
			
			Assert.assertTrue(!new APIUtilities().returnValuePresentInJson(postResponseString, "related_id").equals(""));
				
			log.info("Ending TestTask_LinkNewAdditionalAssigneesToTask");
	    }
	  
	  @Test
	    public void TestTask_LinkNewTaskToInvalidTask(){
	    	log.info("Starting TestTask_LinkNewTaskToTask");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			
			log.info("Retrieving OAuth2Token.");		
			String headers[]= getOAuthTokenInHeader(user1);
					
			JSONObject body = new JSONObject(getValidTaskBody());
			
			HttpUtils restCalls = new HttpUtils();
			String postResponseString = restCalls.postRequest(getRequestUrl("/" + "invalid" + "/link/tasks", null) ,headers, body.toString(), "application/json", "404");
		 
			try {
				//check for a valid JSON response
				new JSONParser().parse(postResponseString);
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
				
			log.info("Ending TestTask_LinkNewTaskToTask");
	    }
	  
	  @Test
	    public void TestTask_LinkNewTaskToTaskInvalidBody(){
	    	log.info("Starting TestTask_LinkNewTaskToTask");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			String taskID = TaskRestAPITests.createTaskHelper(user1,log,testConfig.getBrowserURL());
			
			log.info("Retrieving OAuth2Token.");				
	 		String headers[] = getOAuthTokenInHeader(user1);			
							
			JSONObject body = new JSONObject(getInValidTaskBody());
			
			HttpUtils restCalls = new HttpUtils();
			String postResponseString = restCalls.postRequest(getRequestUrl("/" + taskID + "/link/tasks", null) ,headers, body.toString(), "application/json", "422");
					
			try {
				//check for a valid JSON response
				new JSONParser().parse(postResponseString);
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
				
			log.info("Ending TestTask_LinkNewTaskToTask");
	    }
	  
	     @Test
	     public void CreateTaskwrongCase(){
	 	    	log.info("Starting CreateTaskwrongCase");
	 			User user1 = commonUserAllocator.getUser(this);
	 			String baseURL = testConfig.getBrowserURL();
	 			
	 			log.info("Creating tasks");
	 			String BasetaskID = TaskRestAPITests.createTaskHelper(user1,"Base task",log,baseURL);

	 			log.info("Retrieving OAuth2Token.");	 			
				String headers[] = getOAuthTokenInHeader(user1);
										
				JSONObject body = new JSONObject(getValidTaskBody());
				
				HttpUtils restCalls = new HttpUtils();
				
				String postResponseString = restCalls.postRequest(getRequestUrl("/" + BasetaskID + "/link/TASKS", null) ,headers, body.toString(), "application/json", "405");
						
				try {
					//check for a valid JSON response
					new JSONParser().parse(postResponseString);
				} catch (ParseException e) {
					e.printStackTrace();
					Assert.assertTrue(false, "Parse exception with post response");
				}
	 			log.info("Ending CreateTaskwrongCase");	
	     }
	  
		public String getOAuthExtension(){		
			return "oauth?" + getclient_ID() + "&" + getclient_secret();
		}
		
		
		
	    private HashMap<String,Object> getValidTaskBody(){
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("name", "Task Name");
			map.put("date_due", "2013-10-28T15:14:00.000Z");
			map.put("priority", "High");
			map.put("status", "Not Started");
			map.put("call_type", "Close_out_call");

	    	return map;
	    }
	    
	    private HashMap<String,String> getRelatedToC(String[] array){
			HashMap<String, String> map = new HashMap<String, String>();
			for (int i = 0; i < array.length; i+=2) {
				map.put(array[i], array[i+1]);
			}
	    	return map;
	    }
	    
	    private HashMap<String,String> getInValidTaskBody(){
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("name", "Task Name");

	    	return map;
	    }
	    
	    private String sendRequest(String userEmail, String userPassword, String task, String module, String recordID){
	    	return sendRequest(userEmail, userPassword, task, module, recordID, "200");
	    }
	    
	    
	    private String sendRequest(String userEmail, String userPassword, String task, String module, String recordID, String expectedResponse){
	    	
			log.info("Retrieving OAuth2Token.");					
			String headers[]= getOAuthTokenInHeader(userEmail, userPassword);
			
			HttpUtils restCalls = new HttpUtils();
			String postResponseString = restCalls.postRequest(getRequestUrl("/" + task + "/link/" + module + "/" + recordID, null), headers, "", "application/json", expectedResponse);
			
			log.info("Response String "+ postResponseString);
			try {
				//check for a valid JSON response
				new JSONParser().parse(postResponseString);
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
			
			return postResponseString;
	    }
	    
	    private String sendRequest(String userEmail, String userPassword, JSONObject body, String expectedResponse){
			log.info("Retrieving OAuth2Token.");	
			
			String headers[]= getOAuthTokenInHeader(userEmail, userPassword);
							
			HttpUtils restCalls = new HttpUtils();
			String postResponseString = restCalls.postRequest(getRequestUrl(null, null),headers, body, "application/json", expectedResponse);
		
			log.info("Response String "+ postResponseString);
			try {
				//check for a valid JSON response
				new JSONParser().parse(postResponseString);
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
			
			return postResponseString;
	    }
	  
}
