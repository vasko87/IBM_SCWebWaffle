/**
 * 
 */
package com.ibm.salesconnect.test.api.tasks;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

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
import com.ibm.salesconnect.API.APIUtilities;
import com.ibm.salesconnect.API.ApiBaseTest;
import com.ibm.salesconnect.API.CallRestAPI;
import com.ibm.salesconnect.API.CollabWebAPI;
import com.ibm.salesconnect.API.ContactRestAPI;
import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.API.NoteRestAPI;
import com.ibm.salesconnect.API.OpportunityRestAPI;
import com.ibm.salesconnect.API.SugarAPI;
import com.ibm.salesconnect.API.TaskRestAPI;
import com.ibm.salesconnect.API.TestDataHolder;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.common.HttpUtils;
import com.ibm.salesconnect.test.mobile.TaskRestAPITests;

/**
 * @author timlehane
 * @date Oct 2, 2014
 */
public class UPDATETask extends ApiBaseTest {
	private static final Logger log = LoggerFactory.getLogger(UPDATETask.class);

	
	@Parameters({ "apiExtension", "applicationName", "APIM", "apim_environment" })
	private UPDATETask(@Optional("tasks") String apiExtension,
			@Optional("SC Auto update") String applicationName,
			@Optional("true") String APIM,
			@Optional("development") String environment) {

		super(apiExtension, applicationName, APIM, environment);

	}
	
    private TestDataHolder testData;
    int rand = new Random().nextInt(1000000);
	private String contactID = "22SC-" + rand;
	private String contactBeanID = null;
	private String opptyID = null;
	private String BasetaskID = null;
	private String DeletedtaskID = null;
	private String TaskForLinkingtaskID = null;
	private String casetaskID = null;
	private String noteID = null;
	private String noteSubject = "post task note subject";
	private String callID = null;
	private String callSubject = "post task call subject";
	private String clientID = null;
	private String clientBeanID = null;
	private String assignedUserID = null;
	private String userID = null;
	private String unlinkedvalidTaskID = null;
	private String unlinkedvalidTaskIDOneUse = null;
	
	private void createObjects(){
		log.info("Start creating prerequisites");

		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
		User user2 = commonUserAllocator.getUser(this);
		String baseURL = testConfig.getBrowserURL();
		userID = new APIUtilities().getUserBeanIDFromEmail(baseURL, user2.getEmail(), user2.getPassword());
		PoolClient poolClient= commonClientAllocator.getGroupClient(GC.SC,this);

		
		log.info("Getting client");
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user1.getEmail(), user1.getPassword());
		String headers[] = {"OAuth-Token", OAuthToken};
		clientID = poolClient.getCCMS_ID();
		clientBeanID = new CollabWebAPI().getbeanIDfromClientID(baseURL, clientID, headers );
		
		log.info("Creating contact");
		SugarAPI sugarAPI = new SugarAPI();
		sugarAPI.createContact(testConfig.getBrowserURL(), contactID, clientID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");
		assignedUserID = new APIUtilities().getUserBeanIDFromEmail(baseURL, user1.getEmail(), user1.getPassword());	
		ContactRestAPI contactAPI = new ContactRestAPI();
		contactBeanID = contactAPI.createContactreturnBean(baseURL, headers, "ContactFirst", "ContactLast", "US", "(555) 555-5555", assignedUserID);
		
		log.info("Creating oppty");
		opptyID = sugarAPI.createOppty(baseURL, "", contactID, clientID, user1.getEmail(), user1.getPassword(), GC.emptyArray);
		
		log.info("Creating note");
		NoteRestAPI noteAPI = new NoteRestAPI();
		noteID = noteAPI.createNotereturnBean(testConfig.getBrowserURL(), OAuthToken, noteSubject, "post task note description", assignedUserID);
		
		log.info("Creating call");
		CallRestAPI callRestAPI = new CallRestAPI();
		callID = callRestAPI.createCallreturnBean(testConfig.getBrowserURL(), OAuthToken, callSubject, "2013-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);
		
		log.info("Creating tasks");
		TaskRestAPI taskRestAPI = new TaskRestAPI();
		BasetaskID = TaskRestAPITests.createTaskHelper(user1,"Base task",log,baseURL);
		unlinkedvalidTaskID = TaskRestAPITests.createTaskHelper(user1,"unlinkedvalidTaskID task",log,baseURL);
		unlinkedvalidTaskIDOneUse = TaskRestAPITests.createTaskHelper(user1,"unlinkedvalidTaskIDOneUse task",log,baseURL);
		TaskForLinkingtaskID = TaskRestAPITests.createTaskHelper(user1,"Link task",log,baseURL);
		casetaskID = TaskRestAPITests.createTaskHelper(user1, "Call Link task", log, baseURL);
		taskRestAPI.linkRecordToTaskAndVerify(baseURL, OAuthToken, BasetaskID, TaskForLinkingtaskID, "Tasks");
		taskRestAPI.linkRecordToTaskAndVerify(baseURL, OAuthToken, BasetaskID, clientBeanID, "Accounts");
		taskRestAPI.linkRecordToTaskAndVerify(baseURL, OAuthToken, BasetaskID, contactBeanID, "Contacts");
		taskRestAPI.linkRecordToTaskAndVerify(baseURL, OAuthToken, BasetaskID, opptyID, "Opportunity");
		taskRestAPI.linkRecordToTaskAndVerify(baseURL, OAuthToken, BasetaskID, noteID, "Notes");
		taskRestAPI.linkRecordToTaskAndVerify(baseURL, OAuthToken, BasetaskID, userID, "Users");
		taskRestAPI.linkRecordToTaskAndVerify(baseURL, OAuthToken, BasetaskID, callID, "Calls");
		DeletedtaskID = TaskRestAPITests.createTaskHelper(user1,"Base task",log,testConfig.getBrowserURL());
		
		String jsonString = new TaskRestAPI().deleteTask(testConfig.getBrowserURL(), OAuthToken, DeletedtaskID);
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Deleting Task failed.");
		}
		
		log.info("Finish creating prerequisites");
	}
	
    public void addDataFile(String filePath){
        if(this.testData== null){
            this.testData = new TestDataHolder();
        }
         this.testData.addDataLocation(filePath);
     }
      
     
     @DataProvider(name="DataProvider")
     public Object[][] getTestData(){
     	//Create common objects required by test
     	this.createObjects();
     	//Read all lines into an ArrayList of HashMaps
     	this.addDataFile("test_config/extensions/api/updateTask.csv");
     	//Return an array of arrays where each item in the array is a HashMap of parameter values
     	//Test content
        return testData.getAllDataRows();
     }
     
     @Test(dataProvider = "DataProvider")
     public void TaskTest(HashMap<String,Object> parameterValues){
 		log.info("Start test.");
 		String expectedResponseCode = "200";
 		String recordID = null;
 		String link = null;
 		String link_id = null;
 		String urlExtension = "";
 		String linked_item = "";
	
        Iterator<Map.Entry<String, Object>> it = parameterValues.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> pairs = (Map.Entry<String, Object>)it.next();
            
            if(pairs.getKey().equals("expected_response")){
            	expectedResponseCode=pairs.getValue().toString();
            	it.remove();
            }
            else if (pairs.getKey().equals("TC_Name")) {
				log.info("This is test " + pairs.getValue().toString());
			}
            else if (pairs.getKey().equals("record_id")) {
            	recordID = getRecordID(pairs.getValue().toString());
				//linked_item = getlinkedItem(pairs.getValue().toString());	
            }
            else if (pairs.getKey().equals("link")) {
				if (pairs.getValue().equals("0")) {
					log.info("Leaving link section of url blank as csv value set to 0");
					link = "0";
				}
				else {
					log.info("Setting link section of url to " + pairs.getValue());
					link = pairs.getValue().toString();
				}
			}
            else if (pairs.getKey().equals("link_id")) {
				link_id = getlinkID(pairs.getValue().toString());
			}
        }
        
        if (!recordID.equals("0")) {
			urlExtension+= "/" + recordID + "/";
		}
        else {
			urlExtension+= "/";
		}
        
        if (!link.equals("0")) {
			urlExtension+="link/" + link + "/";
		}
        else {
        	if (link_id.equals("0")) {	
			}
        	else {
            	//urlExtension+="link/";
			}
        }
        
        if (!link_id.equals("0")){
        	urlExtension+= link_id;
        }
        else if (link_id.equals("0")) {
			
		}
        
		log.info("Getting user.");		
		//User user = commonUserAllocator.getUser();
		User user = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
		
		log.info("Retrieving OAuth2Token.");		
		String token = getOAuthToken(user);
		
		log.info("Sending UPDATE request");
		TaskRestAPI taskRestAPI = new TaskRestAPI();
		String jsonString = taskRestAPI.editTaskAPIm(getRequestUrl(urlExtension, null), token, "", expectedResponseCode);
		
		log.info("Checking response");
		if (!expectedResponseCode.equals("200")) {
			//If expected fail do not parse response, response code already checked 
			log.info("Task creation failed as expected");
		} else {
			if (!link_id.equals("0")) {
				log.info("linked item id is not blank so checking that");
				for (int i = 0; i < 5; i++) {
					String temp = (String) new APIUtilities().returnValuePresentInJson(jsonString, "related_to_c_"+i);
					Assert.assertTrue(!new APIUtilities().checkIfValuePresentInJson(temp, "related_id", link_id),
							"id " + link_id + "is present in related to c field but should not be"); 
				}
			}
			else {
				if (recordID.equals("0")) {
					log.info("Updating all tasks");
					int count = new APIUtilities().getKeyCountInJsonString(jsonString, "id");
					log.info("id count in result" + new APIUtilities().getKeyCountInJsonString(jsonString, "id"));
					Assert.assertTrue(count >=1, "Count is less than 1");
				}
				else {
					if (link.equals("0")) {
						Assert.assertTrue(new APIUtilities().checkIfValuePresentInJson(jsonString, "id", recordID.toLowerCase()), "link_id is blank, link is blank, checking record_id: " + recordID);
					}
					else {
						log.info("linked item id is blank so checking linked_item");
						Assert.assertTrue(new APIUtilities().checkIfValuePresentInJson(jsonString, "id", linked_item), "related: " + linked_item + " was not returned correctly");
					}
				}
			}
		}
     }
     
     @Test
     public void UpdateTaskwrongCase(){
 	    	log.info("Starting UpdateTaskwrongCase");
 			User user1 = commonUserAllocator.getUser(this);
 			String baseURL = testConfig.getBrowserURL();
 			
 			log.info("Creating tasks");
 			TaskRestAPI taskRestAPI = new TaskRestAPI();
 			String BasetaskID = TaskRestAPITests.createTaskHelper(user1,"Base task",log,baseURL);
 			String body = "{\"name\":\"Base task\",\"date_due\":\"2013-10-28T15:14:00.000Z\",\"priority\":\"High\",\"status\":\"Not Started\",\"call_type\":\"Close_out_call\",\"assigned_user_id\":\"43c30ef7-f0a2-bd4a-b1b4-538df1fb77d4\"}";

 			log.info("Retrieving OAuth2Token.");	
 			String token = getOAuthToken(user1);
 			 			
 			log.info("Sending UPDATE request");
 			taskRestAPI.editTaskAPIm(addclientIDAndSecret(getApiManagement() + "Tasks/" + BasetaskID), token, body, "404");
 		
 			log.info("Ending UpdateTaskwrongCase");	
     }
     
     @Test
     public void UpdateTasklinktosametask(){
 	    	log.info("Starting UpdateTasklinktosametask");
 			User user1 = commonUserAllocator.getUser(this);
 			String baseURL = testConfig.getBrowserURL();
 			
 			log.info("Creating tasks");
 			TaskRestAPI taskRestAPI = new TaskRestAPI();
 			String BasetaskID = TaskRestAPITests.createTaskHelper(user1,"Base task",log,baseURL);
 			
 			log.info("Retrieving OAuth2Token.");		
 			String token = getOAuthToken(user1);
 				
 			log.info("Sending UPDATE request");
 			taskRestAPI.editTaskAPIm(getRequestUrl("/" + BasetaskID + "/link/tasks/" + BasetaskID, null),token, "", "200");
		
 			log.info("Ending UpdateTasklinktosametask");	
     }
     
	  @Test
	    public void TestTask_RelateAdditionalAssigneesInValidUserCNUM(){
	    	log.info("Starting TestTask_RelateAdditionalAssigneesInValidUserCNUM");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			
			sendRequest(user1.getEmail(), user1.getPassword(), BasetaskID, 
					"additional_assignees_link/field/employee_cum", "invaliduser", "404");
				
			log.info("Ending TestTask_RelateAdditionalAssigneesInValidUserCNUM");
	    }
	  
	  @Test
	    public void TestTask_RelateAdditionalAssigneesValidUserCNUM(){
	    	log.info("Starting TestTask_RelateAdditionalAssigneesValidUserCNUM");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			String taskID = TaskRestAPITests.createTaskHelper(user1,log,testConfig.getBrowserURL());
			
			sendRequest(user1.getEmail(), user1.getPassword(), taskID, 
					"additional_assignees_link/field/employee_cnum", "-0505K754", "404");

			log.info("Ending TestTask_RelateAdditionalAssigneesValidUserCNUM");
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
			
			sendRequest(user1.getEmail(), user1.getPassword(), taskID, "contacts", contactBeanID,"405");

			log.info("Ending TestTask_RelateDACHContact");
	    }
	  
	  //not currently allowed to update linked contact
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

			sendRequest("de01@tst.ibm.com", user1.getPassword(), taskID, "contacts", contactBeanID,"405");

			log.info("Ending TestTask_RelateDACHContact");
	    }
	  
	  @Test
	  public void TestUpdateTaskParameters(){
		  String initialTask = "{\"date_entered\":\"2019-10-28T15:14:00+00:00\",\"team_set_id\":\"1\",\"ext_tasks_textfield_1_c\":\"Contact NameXX\",\"ext_tasks_textfield_3_c\":\"FlexSystem\"                                        ,\"ext_tasks_dropdown_9_c\":\"StrategicOutsourcing\",\"ext_tasks_integer_2_c\":\"54321\",\"ext_tasks_check_10_c\":\"0\",\"ext_tasks_dropdown_7_c\":\"GTS\",\"date_start_flag\":\"0\",\"version\":\"Technical_Sales_Activity\",\"ext_tasks_check_5_c\":\"0\",\"date_due_flag\":\"0\",\"priority\":\"Medium\",\"description\":\"Make time for meeting\",\"name\":\"Auto_Task2\",\"ext_tasks_dropdown_8_c\":\"BPSWSalesHelp\",\"ext_tasks_dropdown_10_c\":\"Anguilla\",\"ext_tasks_textfield_4_c\":\"US\",\"ext_tasks_check_1_c\":\"0\",\"ext_tasks_check_4_c\":\"0\",\"ext_tasks_integer_1_c\":\"12345\",\"ext_tasks_dropdown_3_c\":\"ReviewIBMTechnicalSupportBenefits\",\"ext_tasks_check_7_c\":\"0\",\"ext_tasks_integer_5_c\":\"Dell,EMC,Fujitsu,Hitachi,HP\",\"ext_tasks_datetime_1_c\":\"2019-10-28T15:14:00+00:00\",\"ext_tasks_check_8_c\":\"0\",\"status\":\"In Progress\",\"date_modified\":\"2019-10-28T15:14:00+00:00\",\"ext_tasks_check_9_c\":\"0\",\"ext_tasks_dropdown_1_c\":\"Demonstration\",\"ext_tasks_check_6_c\":\"0\",\"ext_tasks_dropdown_4_c\":\"BillableServices\",\"deleted\":\"0\",\"ext_tasks_dropdown_2_c\":\"NewWorkloads\",\"ext_tasks_integer_4_c\":\"IndustrySolutions\",\"ext_tasks_check_2_c\":\"0\",\"date_due\":\"2019-10-28T15:14:00+00:00\",\"ext_tasks_dropdown_5_c\":\"ATSSCON\",\"call_type\":\"Close_out_call\",\"related_to_c\":[{\"related_name\":\"ANTUNA EXILUS\",\"related_id\":\"a79dc8f9-3973-54f9-17dc-5038c02ca738\",\"related_type\":\"Accounts\"}],\"ext_tasks_dropdown_6_c\":\"Analytics\",\"ext_tasks_textfield_2_c\":\"test@email.com\",\"ext_tasks_check_3_c\":\"0\",\"team_id\":\"1\"}";
		  String changesTask = "{\"date_entered\":\"2019-10-28T15:14:00+00:00\",\"team_set_id\":\"1\",\"ext_tasks_textfield_1_c\":\"Contact NameXY\",										   \"ext_tasks_integer_3_c\":\"123456789\",\"ext_tasks_dropdown_9_c\":\"\",\"ext_tasks_integer_2_c\":\"12345\",\"ext_tasks_check_10_c\":\"0\",\"ext_tasks_dropdown_7_c\":\"GTS\",\"date_start_flag\":\"0\",\"version\":\"Technical_Sales_Activity\",\"ext_tasks_check_5_c\":\"0\",\"date_due_flag\":\"0\",\"priority\":\"Medium\",\"description\":\"Make time for meeting\",\"name\":\"Auto_Task2\",\"ext_tasks_dropdown_8_c\":\"BPSWSalesHelp\",\"ext_tasks_dropdown_10_c\":\"Anguilla\",\"ext_tasks_textfield_4_c\":\"US\",\"ext_tasks_check_1_c\":\"0\",\"ext_tasks_check_4_c\":\"0\",\"ext_tasks_integer_1_c\":\"12345\",\"ext_tasks_dropdown_3_c\":\"ReviewIBMTechnicalSupportBenefits\",\"ext_tasks_check_7_c\":\"0\",\"ext_tasks_integer_5_c\":\"Dell,EMC,Fujitsu,Hitachi,HP\",\"ext_tasks_datetime_1_c\":\"2019-10-28T15:14:00+00:00\",\"ext_tasks_check_8_c\":\"0\",\"status\":\"In Progress\",\"date_modified\":\"2019-10-28T15:14:00+00:00\",\"ext_tasks_check_9_c\":\"0\",\"ext_tasks_dropdown_1_c\":\"Demonstration\",\"ext_tasks_check_6_c\":\"0\",\"ext_tasks_dropdown_4_c\":\"BillableServices\",\"deleted\":\"0\",\"ext_tasks_dropdown_2_c\":\"NewWorkloads\",\"ext_tasks_integer_4_c\":\"IndustrySolutions\",\"ext_tasks_check_2_c\":\"0\",\"date_due\":\"2019-10-28T15:14:00+00:00\",\"ext_tasks_dropdown_5_c\":\"AvayaSolution\",\"call_type\":\"Close_out_call\",\"related_to_c\":[{\"related_name\":\"ANTUNA EXILUS\",\"related_id\":\"a79dc8f9-3973-54f9-17dc-5038c02ca738\",\"related_type\":\"Accounts\"}],\"ext_tasks_dropdown_6_c\":\"Analytics\",\"ext_tasks_textfield_2_c\":\"test@email.com\",\"ext_tasks_check_3_c\":\"0\",\"team_id\":\"1\"}";
		  
			log.info("Getting user.");		
			User user = commonUserAllocator.getUser();
		
			log.info("Retrieving OAuth2Token.");				
			String headers[]= getOAuthTokenInHeader(user);
		
			log.info("Sending initial task creation request");
			HttpUtils httpUtils = new HttpUtils();
			
			// TODO - why baseurl here and then apim below? 
			String response = httpUtils.postRequest(baseURL + "rest/v10/Tasks", headers, initialTask, "application/json", "200");
			String taskId = null;
			try {
				JSONObject postResponse = (JSONObject)new JSONParser().parse(response);
				taskId = (String) postResponse.get("id");
			} catch (Exception e) {
				Assert.assertTrue(false, "Task was not created as expected.");
			}
			
			log.info("Sending UPDATE request");
			log.info("Retrieving OAuth2Token.");
			String token = getOAuthToken(user);
						
			TaskRestAPI taskRestAPI = new TaskRestAPI();
			
		
			String jsonString = taskRestAPI.editTaskAPIm(getRequestUrl("/" + taskId, null), token, changesTask, "200");
			APIUtilities apiUtilities = new APIUtilities();
			Assert.assertTrue(apiUtilities.checkIfValuePresentInJson(jsonString, "ext_tasks_integer_3_c", 123456789));
			Assert.assertTrue(apiUtilities.checkIfValuePresentInJson(jsonString, "ext_tasks_textfield_1_c", "Contact NameXY"));
			Assert.assertTrue(apiUtilities.checkIfValuePresentInJson(jsonString, "ext_tasks_textfield_3_c", "FlexSystem"));
			Assert.assertTrue(apiUtilities.checkIfValuePresentInJson(jsonString, "ext_tasks_integer_2_c", 12345));
			Assert.assertTrue(apiUtilities.checkIfValuePresentInJson(jsonString, "ext_tasks_dropdown_9_c", ""));
	  }
	  //not currently allowed to update linked contact
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
			
			String postResponseString = sendRequest(user2.getEmail(), user2.getPassword(), taskID, "contacts", contactBeanID, "405");


			log.info("Ending TestTask_RelateOtherUsersContact");
	    }
	  
	  //not currently allowed to update linked oppty
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
			
			String postResponseString = sendRequest(user2.getEmail(), user2.getPassword(), taskID, "opportunities", opptyID,"405");
			
		//	Assert.assertTrue(new APIUtilities().checkIfValuePresentInJson(postResponseString, "related_id", opptyID),
		//				"id " + opptyID + " is not present in related field but should be"); 

			log.info("Ending TestTask_RelateRestrictedOpptyValid");
	    }
	  
	  //not currently allowed to update linked client
	  //@Test
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
	  
	  //not currently allowed to update linked client
	  //@Test
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
	  
	  //not currently allowed to update linked contact
	  //@Test
	    public void TestTask_RelateRestrictedContactValidUser(){
	    	log.info("Starting TestTask_RelateRestrictedContactValidUser");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			User user2 = commonUserAllocator.getUser(this);
			String taskID = TaskRestAPITests.createTaskHelper(user1,log,testConfig.getBrowserURL());
			
			log.info("Creating contact");
			String clientID = commonClientAllocator.getGroupClient("RES",this).getCCMS_ID();
			String headers[] = {"OAuth-Token", new LoginRestAPI().getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};

			String clientbeanID = new CollabWebAPI().getbeanIDfromClientID(baseURL, clientID, headers );
			
			log.info("Creating contact");
			String assignedUserID = new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(), user1.getEmail(), 
					user1.getPassword());	
			String contactBeanID = new ContactRestAPI().createContactreturnBean(testConfig.getBrowserURL(), headers, 
					"ContactFirst", "ContactLast", "US", "(555) 555-5555", assignedUserID, clientbeanID);
			
			String postResponseString = sendRequest(user1.getEmail(), user1.getPassword(), taskID, "contacts", contactBeanID,"200");

			log.info("Ending TestTask_RelateRestrictedContactValidUser");
	    }
	  
	  //not currently allowed to update linked contact
	  //@Test
	    public void TestTask_RelateRestrictedContactInValidUser(){
	    	log.info("Starting TestTask_RelateRestrictedContactInValidUser");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			User user2 = commonUserAllocator.getUser(this);
			String taskID = TaskRestAPITests.createTaskHelper(user1,log,testConfig.getBrowserURL());
			
			log.info("Creating contact");
			String clientID = commonClientAllocator.getGroupClient("RES",this).getCCMS_ID();
			String headers[] = {"OAuth-Token", new LoginRestAPI().getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};

			String clientbeanID = new CollabWebAPI().getbeanIDfromClientID(baseURL, clientID, headers );
			
			log.info("Creating contact");
			String assignedUserID = new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(), user1.getEmail(), 
					user1.getPassword());	
			String contactBeanID = new ContactRestAPI().createContactreturnBean(testConfig.getBrowserURL(), headers, 
					"ContactFirst", "ContactLast", "US", "(555) 555-5555", assignedUserID, clientbeanID);
			
			String postResponseString = sendRequest(user2.getEmail(), user2.getPassword(), taskID, "contacts", contactBeanID,"404");

			log.info("Ending TestTask_RelateRestrictedContactInValidUser");
	    }
	    
 	private String getRecordID(String recordType){
    	if (recordType.equals("0")) {
    		log.info("No record id specified");
    		return  "0";
		}
    	else if (recordType.equals("validtask")) {
    		log.info("Setting record_id to valid value");
			return unlinkedvalidTaskID;
		}
    	else if (recordType.equals("unlinkedvalidtask")) {
    		log.info("Setting record_id to unlinked valid value");
			return BasetaskID;
		}
    	else if (recordType.equals("unlinkedvalidtaskOneUse")) {
    		log.info("Setting record_id to unlinked valid value");
			return unlinkedvalidTaskIDOneUse;
		}
    	else if (recordType.equals("invalidtask")) {
    		log.info("Setting record_id to invalid value");
    		return "InvalidTaskID";
		}
    	else if (recordType.equals("deletedtask")) {
			log.info("Setting record_id to deleted task");
			return DeletedtaskID;
		}
    	else if (recordType.equals("casetask")) {
			log.info("Setting record_id to deleted task");
			return casetaskID.toUpperCase();
		}
    	else {
			Assert.assertTrue(false, "record_id field in csv file does not contain a valid value was " + recordType);
			return "fail";
		}
	}
	
	private String getlinkID(String linked_id){
    	if (linked_id.equals("0")) {
    		log.info("No link id specified");
    		return  "0";
		}
    	else if (linked_id.equals("linkedtaskid")) {
    		log.info("Setting linked_id to linkedtaskid");
			return TaskForLinkingtaskID;
		}
    	else if (linked_id.equals("linkeduserid")) {
    		log.info("Setting linked_id to user id");
    		return userID;
		}
    	else if (linked_id.equals("linkedaccountid")) {
			log.info("Setting linked_id to clientBeanID");
			return clientBeanID;
		}
    	else if (linked_id.equals("linkedcontactid")) {
			log.info("Setting linked_id to contactBeanID");
			return contactBeanID;
		}
    	else if (linked_id.equals("linkednoteid")) {
			log.info("Setting linked_id to noteID");
			return noteID;
		}
    	else if (linked_id.equals("linkedopptyid")) {
			log.info("Setting linked_id to opptyID");
			return opptyID;
		}
    	else if (linked_id.equals("linkedcallid")) {
			log.info("Setting linked_id to callID");
			return callID;
		}
    	else if (linked_id.equals("linkedtaskcaseid")) {
			log.info("Setting linked_id to callID");
			return TaskForLinkingtaskID.toUpperCase();
		}
    	else if (linked_id.equals("linkeddeletedtaskid")) {
			log.info("Setting linked_id to callID");
			return DeletedtaskID;
		}
    	else if (linked_id.contains("invalid")) {
			log.info("Setting linked_id to invalid");
			return "invalid";
		}
    	else if (linked_id.contains("favorite1")) {
			log.info("Setting linked_id to favorite1");
			return "favorite1";
		}
    	else if (linked_id.contains("favorite")) {
			log.info("Setting linked_id to favorite");
			return "favorite";
		}
    	else {
			log.info("linked_id field in csv file does not contain a valid value was " + linked_id);
			Assert.assertTrue(false);
			return "fail";
		}
	}
	
	private String sendRequest(String userEmail, String userPassword, String task, String module, String recordID){
    	return sendRequest(userEmail, userPassword, task, module, recordID, "200");
    }
	
    private String sendRequest(String userEmail, String userPassword, String task, String module, String recordID, String expectedResponse){
		
    	log.info("Retrieving OAuth2Token.");
		String headers[]= getOAuthTokenInHeader(userEmail, userPassword);
		
		
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(getRequestUrl("/link/" + module + "/" + recordID, null), headers, "", "application/json", expectedResponse);
		
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
