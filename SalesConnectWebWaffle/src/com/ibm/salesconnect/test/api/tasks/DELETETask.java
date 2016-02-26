/**
 * 
 */
package com.ibm.salesconnect.test.api.tasks;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

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
import com.ibm.salesconnect.API.SugarAPI;
import com.ibm.salesconnect.API.TaskRestAPI;
import com.ibm.salesconnect.API.TestDataHolder;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.test.mobile.TaskRestAPITests;

/**
 * @author timlehane
 * @date Sep 29, 2014
 */
public class DELETETask extends ApiBaseTest {
	private static final Logger log = LoggerFactory.getLogger(DELETETask.class);
	
	
	@Parameters({ "apiExtension", "applicationName", "APIM", "apim_environment" })
	private DELETETask(@Optional("tasks") String apiExtension,
			@Optional("SC Auto delete") String applicationName,
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
	private String LinkedTasktaskID = null;
	private String TaskForLinkingtaskID = null;
	private String LinkAccounttaskID = null;
	private String LinkContacttaskID = null;
	private String LinkOpptytaskID = null;
	private String LinkNotetaskID = null;
	private String LinkAdditionalUsertaskID = null;
	private String LinkCalltaskID = null;
	private String TemptaskID = null;
	private String casetaskID = null;
	private String LinkedcasetaskID = null;
	private String noteID = null;
	private String noteSubject = "post task note subject";
	private String callID = null;
	private String callSubject = "post task call subject";
	private String clientID = null;
	private String clientBeanID = null;
	private String assignedUserID = null;
	private String userID = null;
	
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
		opptyID  = sugarAPI.createOppty(baseURL, "", contactID, clientID, user1.getEmail(), user1.getPassword(), GC.emptyArray);
		
		log.info("Creating note");
		NoteRestAPI noteAPI = new NoteRestAPI();
		noteID = noteAPI.createNotereturnBean(testConfig.getBrowserURL(), OAuthToken, noteSubject, "post task note description", assignedUserID);
		
		log.info("Creating call");
		CallRestAPI callRestAPI = new CallRestAPI();
		callID = callRestAPI.createCallreturnBean(testConfig.getBrowserURL(), OAuthToken, callSubject, "2013-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);
		
		log.info("Creating tasks");
		TaskRestAPI taskRestAPI = new TaskRestAPI();
		BasetaskID = TaskRestAPITests.createTaskHelper(user1,"Base task",log,baseURL);
		LinkedTasktaskID = TaskRestAPITests.createTaskHelper(user1,"Link task",log,baseURL);
		TaskForLinkingtaskID = TaskRestAPITests.createTaskHelper(user1,"Link task",log,baseURL);
		LinkAccounttaskID = TaskRestAPITests.createTaskHelper(user1,"Account Link task",log,baseURL);
		LinkContacttaskID = TaskRestAPITests.createTaskHelper(user1,"Contact Link task",log,baseURL);
		LinkOpptytaskID = TaskRestAPITests.createTaskHelper(user1,"Oppty Link task",log,baseURL);
		LinkNotetaskID = TaskRestAPITests.createTaskHelper(user1,"Note Link task",log,baseURL);
		LinkAdditionalUsertaskID = TaskRestAPITests.createTaskHelper(user1,"User Link task",log,baseURL);
		LinkCalltaskID = TaskRestAPITests.createTaskHelper(user1, "Call Link task", log, baseURL);
		TemptaskID = TaskRestAPITests.createTaskHelper(user1, "Call Link task", log, baseURL);
		casetaskID = TaskRestAPITests.createTaskHelper(user1, "Call Link task", log, baseURL);
		LinkedcasetaskID = TaskRestAPITests.createTaskHelper(user1, "Call Link task", log, baseURL);
		taskRestAPI.linkRecordToTaskAndVerify(baseURL, OAuthToken, LinkedTasktaskID, TaskForLinkingtaskID, "Tasks");
		taskRestAPI.linkRecordToTaskAndVerify(baseURL, OAuthToken, LinkAccounttaskID, clientBeanID, "Accounts");
		taskRestAPI.linkRecordToTaskAndVerify(baseURL, OAuthToken, LinkContacttaskID, contactBeanID, "Contacts");
		taskRestAPI.linkRecordToTaskAndVerify(baseURL, OAuthToken, LinkOpptytaskID, opptyID, "Opportunity");
		taskRestAPI.linkRecordToTaskAndVerify(baseURL, OAuthToken, LinkNotetaskID, noteID, "Notes");
		taskRestAPI.linkRecordToTaskAndVerify(baseURL, OAuthToken, LinkAdditionalUsertaskID, userID, "Users");
		//taskRestAPI.linkRecordToTaskAndVerify(baseURL, OAuthToken, LinkCalltaskID, callID, "Calls");
		taskRestAPI.linkRecordToTaskAndVerify(baseURL, OAuthToken, LinkedcasetaskID, TaskForLinkingtaskID, "Tasks");
		taskRestAPI.linkRecordToTaskAndVerify(baseURL, OAuthToken, LinkedcasetaskID, clientBeanID, "Accounts");
		taskRestAPI.linkRecordToTaskAndVerify(baseURL, OAuthToken, LinkedcasetaskID, contactBeanID, "Contacts");
		taskRestAPI.linkRecordToTaskAndVerify(baseURL, OAuthToken, LinkedcasetaskID, opptyID, "Opportunity");
		taskRestAPI.linkRecordToTaskAndVerify(baseURL, OAuthToken, LinkedcasetaskID, noteID, "Notes");
		taskRestAPI.linkRecordToTaskAndVerify(baseURL, OAuthToken, LinkedcasetaskID, userID, "Users");
		//taskRestAPI.linkRecordToTaskAndVerify(baseURL, OAuthToken, LinkedcasetaskID, callID, "Calls");
		
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
    	this.addDataFile("test_config/extensions/api/deleteTask.csv");
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
				linked_item = getlinkedItem(pairs.getValue().toString());	
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
			urlExtension+= "/" + recordID;
		}
        else {
        	if ((link.equals("0")) &&  (link_id.equals("0"))){
					
				}
        	else {
        		urlExtension+= "/";
			}
			
		}
        
        if (!link.equals("0")) {
			urlExtension+="/link/" + link + "/";
		}
        else {
        	if (link_id.equals("0")) {	
			}
        	else {
            	urlExtension+="/link";
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
		
		log.info("Sending DELETE request");
		TaskRestAPI taskRestAPI = new TaskRestAPI();
		String jsonString = taskRestAPI.deleteTaskAPIm(getRequestUrl(urlExtension, null), token, expectedResponseCode);
		
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
					log.info("Deleting all tasks");
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
    
    //@Test
    public void DeleteTask_validid_unauthorizedUser(){
	    	log.info("Starting DeleteTask_validid_unauthorizedUser");
			User user1 = commonUserAllocator.getUser(this);
			User user2 = commonUserAllocator.getUser(this);
			
			log.info("Creating tasks");
			TaskRestAPI taskRestAPI = new TaskRestAPI();
			BasetaskID = TaskRestAPITests.createTaskHelper(user1,"Base task",log,testConfig.getBrowserURL());
			
			log.info("Retrieving OAuth2Token.");	
			String token = getOAuthToken(user2);
			
			log.info("Sending DELETE request");
			taskRestAPI.deleteTaskAPIm(getRequestUrl( "/" + BasetaskID, null), token, "403");
						
			log.info("Ending DeleteTask_validid_unauthorizedUser");	
    }
    
    //@Test
    public void DeleteTaskLink_validitemid_unauthorizedUser(){
	    	log.info("Starting DeleteTaskLink_validitemid_unauthorizedUser");
			User user1 = commonUserAllocator.getUser(this);
			User user2 = commonUserAllocator.getUser(this);
			String baseURL = testConfig.getBrowserURL();
			
			log.info("Creating tasks");
			TaskRestAPI taskRestAPI = new TaskRestAPI();
			String BasetaskID = TaskRestAPITests.createTaskHelper(user1,"Base task",log,baseURL);
			String TaskForLinkingtaskID = TaskRestAPITests.createTaskHelper(user1,"TaskForLinkingtaskID",log,baseURL);
			
			LoginRestAPI loginRestAPI = new LoginRestAPI();
			String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user1.getEmail(), user1.getPassword());
			taskRestAPI.linkRecordToTaskAndVerify(baseURL, OAuthToken, BasetaskID, TaskForLinkingtaskID, "Tasks");
			
			log.info("Retrieving OAuth2Token.");	
			String token = getOAuthToken(user2);
			
			log.info("Sending DELETE request");
			taskRestAPI.deleteTaskAPIm(getRequestUrl("/" +BasetaskID + "/link/tasks/" + TaskForLinkingtaskID, null), token, "403");
			
			log.info("Ending DeleteTaskLink_validitemid_unauthorizedUser");	
    }
    
    //@Test
    public void DeleteAssigneeLink_ValidTaskid_validuserid_unauthorizedUser(){
	    	log.info("Starting DeleteAssigneeLink_ValidTaskid_validuserid_unauthorizedUser");
			User user1 = commonUserAllocator.getUser(this);
			User user2 = commonUserAllocator.getUser(this);
			String baseURL = testConfig.getBrowserURL();
			
			log.info("Creating tasks");
			TaskRestAPI taskRestAPI = new TaskRestAPI();
			String BasetaskID = TaskRestAPITests.createTaskHelper(user1,"Base task",log,baseURL);
			String assignedUserID = new APIUtilities().getUserBeanIDFromEmail(baseURL, user1.getEmail(), user1.getPassword());	
			
			LoginRestAPI loginRestAPI = new LoginRestAPI();
			String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user1.getEmail(), user1.getPassword());
			taskRestAPI.linkRecordToTaskAndVerify(baseURL, OAuthToken, BasetaskID, assignedUserID, "AdditionalAssignees");
			
			log.info("Retrieving OAuth2Token.");
			String token = getOAuthToken(user2);
			
			log.info("Sending DELETE request");
			taskRestAPI.deleteTaskAPIm(getRequestUrl("/" +BasetaskID + "/link/additional_assignees_link/" + assignedUserID, null), token, "403");
			
			log.info("Ending DeleteAssigneeLink_ValidTaskid_validuserid_unauthorizedUser");	
    }
    
    //@Test
    public void DeleteClientLink_ValidTaskid_validitemid_unauthorizedUser(){
	    	log.info("Starting DeleteClientLink_ValidTaskid_validitemid_unauthorizedUser");
			User user1 = commonUserAllocator.getUser(this);
			User user2 = commonUserAllocator.getUser(this);
			String baseURL = testConfig.getBrowserURL();
			
			log.info("Creating tasks");
			TaskRestAPI taskRestAPI = new TaskRestAPI();
			String BasetaskID = TaskRestAPITests.createTaskHelper(user1,"Base task",log,baseURL);
			
			log.info("Getting client");
			LoginRestAPI loginRestAPI = new LoginRestAPI();
			String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user1.getEmail(), user1.getPassword());
			String headers[] = {"OAuth-Token", OAuthToken};

			String clientBeanID = new CollabWebAPI().getbeanIDfromClientID(baseURL, commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID(), 
					headers );

			taskRestAPI.linkRecordToTaskAndVerify(baseURL, OAuthToken, BasetaskID, clientBeanID, "Accounts");
			
			log.info("Retrieving OAuth2Token.");	
			String token = getOAuthToken(user2);
			
			log.info("Sending DELETE request");
			taskRestAPI.deleteTaskAPIm(getRequestUrl("/" +BasetaskID + "/link/accounts/" + clientBeanID, null), token, "403");
			
			log.info("Ending DeleteClientLink_ValidTaskid_validitemid_unauthorizedUser");	
    }
    
    //@Test
    public void DeleteContactLink_ValidTaskid_validitemid_unauthorizedUser(){
	    	log.info("Starting DeleteContactLink_ValidTaskid_validitemid_unauthorizedUser");
			User user1 = commonUserAllocator.getUser(this);
			User user2 = commonUserAllocator.getUser(this);
			String baseURL = testConfig.getBrowserURL();
			
			log.info("Creating tasks");
			TaskRestAPI taskRestAPI = new TaskRestAPI();
			String BasetaskID = TaskRestAPITests.createTaskHelper(user1,"Base task",log,baseURL);
			
			log.info("Getting client");
			LoginRestAPI loginRestAPI = new LoginRestAPI();
			String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user1.getEmail(), user1.getPassword());
			String headers[] = {"OAuth-Token", OAuthToken};
			PoolClient poolClient = commonClientAllocator.getGroupClient(GC.SC,this);
			String clientID = poolClient.getCCMS_ID();

			log.info("Creating contact");
			SugarAPI sugarAPI = new SugarAPI();
			sugarAPI.createContact(testConfig.getBrowserURL(), contactID, clientID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");
			String assignedUserID = new APIUtilities().getUserBeanIDFromEmail(baseURL, user1.getEmail(), user1.getPassword());	
			ContactRestAPI contactAPI = new ContactRestAPI();
			String contactBeanID = contactAPI.createContactreturnBean(baseURL, headers, "ContactFirst", "ContactLast", "US", "(555) 555-5555", 
					assignedUserID);
			
			taskRestAPI.linkRecordToTaskAndVerify(baseURL, OAuthToken, BasetaskID, contactBeanID, "Contacts");
			
			log.info("Retrieving OAuth2Token.");
			String token = getOAuthToken(user2);
			
			log.info("Sending DELETE request");
			taskRestAPI.deleteTaskAPIm(getRequestUrl("/" +BasetaskID + "/link/contacts/" + contactBeanID, null), token, "403");
		
			log.info("Ending DeleteContactLink_ValidTaskid_validitemid_unauthorizedUser");	
    }
       
    //@Test
    public void DeleteNoteLink_ValidTaskid_validitemid_unauthorizedUser(){
	    	log.info("Starting DeleteNoteLink_ValidTaskid_validitemid_unauthorizedUser");
			User user1 = commonUserAllocator.getUser(this);
			User user2 = commonUserAllocator.getUser(this);
			String baseURL = testConfig.getBrowserURL();
			
			log.info("Creating tasks");
			TaskRestAPI taskRestAPI = new TaskRestAPI();
			String BasetaskID = TaskRestAPITests.createTaskHelper(user1,"Base task",log,baseURL);
			log.info("Creating note");
			NoteRestAPI noteAPI = new NoteRestAPI();
			LoginRestAPI loginRestAPI = new LoginRestAPI();
			String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user1.getEmail(), user1.getPassword());
			String assignedUserID = new APIUtilities().getUserBeanIDFromEmail(baseURL, user1.getEmail(), user1.getPassword());	
			String noteID = noteAPI.createNotereturnBean(testConfig.getBrowserURL(), OAuthToken, noteSubject, "post task note description", assignedUserID);
			
			taskRestAPI.linkRecordToTaskAndVerify(baseURL, OAuthToken, BasetaskID, noteID, "Notes");
			
			log.info("Retrieving OAuth2Token.");
			String token = getOAuthToken(user2);
			
			log.info("Sending DELETE request");
			taskRestAPI.deleteTaskAPIm(getRequestUrl("/" +BasetaskID + "/link/notes/" + noteID, null), token, "403");
		
			log.info("Ending DeleteNoteLink_ValidTaskid_validitemid_unauthorizedUser");	
    }
    
    //@Test
    public void DeleteOpptyLink_ValidTaskid_validitemid_unauthorizedUser(){
	    	log.info("Starting DeleteOpptyLink_ValidTaskid_validitemid_unauthorizedUser");
			User user1 = commonUserAllocator.getUser(this);
			User user2 = commonUserAllocator.getUser(this);
			String baseURL = testConfig.getBrowserURL();
			String contactID = "22SC-" + rand;
			
			log.info("Creating tasks");
			TaskRestAPI taskRestAPI = new TaskRestAPI();
			String BasetaskID = TaskRestAPITests.createTaskHelper(user1,"Base task",log,baseURL);
			PoolClient poolClient= commonClientAllocator.getGroupClient(GC.SC,this);

			
			log.info("Getting client");
			LoginRestAPI loginRestAPI = new LoginRestAPI();
			String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user1.getEmail(), user1.getPassword());
			String clientID = poolClient.getCCMS_ID();
			
			log.info("Creating contact");
			SugarAPI sugarAPI = new SugarAPI();
			sugarAPI.createContact(testConfig.getBrowserURL(), contactID, clientID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");

			log.info("Creating oppty");
			String opptyID = sugarAPI.createOppty(baseURL, "", contactID, clientID, user1.getEmail(), user1.getPassword(), GC.emptyArray);
			
			taskRestAPI.linkRecordToTaskAndVerify(baseURL, OAuthToken, BasetaskID, opptyID, "Opportunity");
			
			log.info("Retrieving OAuth2Token.");	
			String token = getOAuthToken(user2);
			
			log.info("Sending DELETE request");
			taskRestAPI.deleteTaskAPIm(getRequestUrl("/" +BasetaskID + "/link/opportunities/" + opptyID, null), token, "403");

			log.info("Ending DeleteOpptyLink_ValidTaskid_validitemid_unauthorizedUser");	
    }
    
    //@Test
    public void DeleteCallLink_ValidTaskid_validitemid_unauthorizedUser(){
	    	log.info("Starting DeleteNoteLink_ValidTaskid_validitemid_unauthorizedUser");
			User user1 = commonUserAllocator.getUser(this);
			User user2 = commonUserAllocator.getGroupUser("readonly", this);
			String baseURL = testConfig.getBrowserURL();
			
			log.info("Creating tasks");
			TaskRestAPI taskRestAPI = new TaskRestAPI();
			String BasetaskID = TaskRestAPITests.createTaskHelper(user1,"Base task",log,baseURL);
			log.info("Creating call");
			LoginRestAPI loginRestAPI = new LoginRestAPI();
			String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user1.getEmail(), user1.getPassword());
			CallRestAPI callRestAPI = new CallRestAPI();
			String callID = callRestAPI.createCallreturnBean(testConfig.getBrowserURL(), OAuthToken, callSubject, "2013-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);
			
			taskRestAPI.linkRecordToTaskAndVerify(baseURL, OAuthToken, BasetaskID, callID, "Calls");
			
			log.info("Retrieving OAuth2Token.");	
			String token = getOAuthToken(user2);
			
			log.info("Sending DELETE request");
			taskRestAPI.deleteTaskAPIm(getRequestUrl("/" +BasetaskID + "/link/calls/" + callID, null), token, "403");
			
			log.info("Ending DeleteNoteLink_ValidTaskid_validitemid_unauthorizedUser");	
    }
    
    @Test
    public void DeleteTaskwrongCase(){
	    	log.info("Starting DeleteTaskwrongCase");
			User user1 = commonUserAllocator.getUser(this);
			String baseURL = testConfig.getBrowserURL();

			log.info("Retrieving OAuth2Token.");		
			String OAuthToken = getOAuthToken(user1);
						
			String callID = new CallRestAPI().createCallreturnBean
			(testConfig.getBrowserURL(), OAuthToken, callSubject, "2013-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);

			log.info("Creating tasks");
			String BasetaskID = TaskRestAPITests.createTaskHelper(user1,"Base task",log,baseURL);

			log.info("Sending DELETE request");
			new TaskRestAPI().deleteTaskAPIm(getRequestUrl("/Tasks/"  +BasetaskID + "/link/calls/" + callID, null), OAuthToken, "404");
				
			log.info("Ending DeleteTaskwrongCase");	
    }
    
    @Test
    public void DeleteLinkedDACHContactDACHUser(){
    	log.info("Starting DeleteLinkedDACHContactDACHUser");
    	User user1 = commonUserAllocator.getGroupUser("dach_users", this);
		log.info("Creating DACHcontact");
		SugarAPI sugarAPI = new SugarAPI();
		sugarAPI.createContact(testConfig.getBrowserURL(), contactID, clientID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");
		assignedUserID = new APIUtilities().getUserBeanIDFromEmail(baseURL, user1.getEmail(), user1.getPassword());
		String OAuthToken = new LoginRestAPI().getOAuth2Token(baseURL, user1.getEmail(), user1.getPassword());
		String headers[] = {"OAuth-Token", OAuthToken};
		ContactRestAPI contactAPI = new ContactRestAPI();
		String contactBeanID = contactAPI.createContactreturnBean(baseURL, headers, "ContactFirst", "ContactLast", "DE", "(555) 555-5555", assignedUserID);
		
		TaskRestAPI taskRestAPI = new TaskRestAPI();
		String taskID = TaskRestAPITests.createTaskHelper(user1,log,testConfig.getBrowserURL());
		String response = taskRestAPI.linkRecordToTaskAndVerifyReturn(baseURL, OAuthToken, taskID, contactBeanID, "Contacts");

		Assert.assertTrue(new APIUtilities().checkIfValuePresentInJson(response, "related_id", contactBeanID),
				"id " + contactBeanID + "is present in related to c fielde");
		
		log.info("Sending DELETE request");
		
		String OAuthTokenNondach = getOAuthToken(user1);
		
		log.info("Sending DELETE request");
		String jsonString = taskRestAPI.deleteTaskAPIm(getRequestUrl("/" + taskID + "/link/contacts/" + contactBeanID, null), OAuthTokenNondach, "200");
	
		Assert.assertTrue(!new APIUtilities().checkIfValuePresentInJson(jsonString, "related_id", contactBeanID),
				"id " + contactBeanID + "is present in related to c field but should not be");
		
		log.info("Ending DeleteLinkedDACHContactDACHUser");		
    }
 
    @Test
    public void DeleteLinkedDACHContactnonDACHUser(){
    	log.info("Starting DeleteLinkedDACHContactnonDACHUser");
    	User user1 = commonUserAllocator.getGroupUser("dach_users", this);
    	User user2 = commonUserAllocator.getUser(this);
		log.info("Creating DACHcontact");
		SugarAPI sugarAPI = new SugarAPI();
		sugarAPI.createContact(testConfig.getBrowserURL(), contactID, clientID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");
		assignedUserID = new APIUtilities().getUserBeanIDFromEmail(baseURL, user1.getEmail(), user1.getPassword());
		String OAuthToken = new LoginRestAPI().getOAuth2Token(baseURL, user1.getEmail(), user1.getPassword());
		String headers[] = {"OAuth-Token", OAuthToken};
		ContactRestAPI contactAPI = new ContactRestAPI();
		String contactBeanID = contactAPI.createContactreturnBean(baseURL, headers, "ContactFirst", "ContactLast", "DE", "(555) 555-5555", assignedUserID);
		
		TaskRestAPI taskRestAPI = new TaskRestAPI();
		String taskID = TaskRestAPITests.createTaskHelper(user1,log,testConfig.getBrowserURL());
		taskRestAPI.linkRecordToTaskAndVerify(baseURL, OAuthToken, taskID, contactBeanID, "Contacts");
		
		String OAuthTokenNondach = getOAuthToken(user2);
		
		log.info("Sending DELETE request");
		taskRestAPI.deleteTaskAPIm(getRequestUrl("/" + taskID + "/link/contacts/" + contactBeanID, null), OAuthTokenNondach, "200");
	
		log.info("Ending DeleteLinkedDACHContactnonDACHUser");		
    }
    
	private String getRecordID(String recordType){
    	if (recordType.equals("0")) {
    		log.info("No record id specified");
    		return  "0";
		}
    	else if (recordType.equals("validtask")) {
    		log.info("Setting record_id to valid value");
			return BasetaskID;
		}
    	else if (recordType.equals("temptask")) {
    		log.info("Setting record_id to valid value");
			return TemptaskID;
		}
    	else if (recordType.equals("invalidtask")) {
    		log.info("Setting record_id to invalid value");
    		return "InvalidTaskID";
		}
    	else if (recordType.equals("deletedtask")) {
			log.info("Setting record_id to deleted task");
			return DeletedtaskID;
		}
    	else if (recordType.equals("tasktask")) {
			log.info("Setting record_id to LinkedTasktaskID: " +LinkedTasktaskID);
			return LinkedTasktaskID;
		}
    	else if (recordType.equals("accounttask")) {
			log.info("Setting record_id to deleted task");
			return LinkAccounttaskID;
		}
    	else if (recordType.equals("contacttask")) {
			log.info("Setting record_id to deleted task");
			return LinkContacttaskID;
		}
    	else if (recordType.equals("opptytask")) {
			log.info("Setting record_id to deleted task");
			return LinkOpptytaskID;
		}
    	else if (recordType.equals("notetask")) {
			log.info("Setting record_id to deleted task");
			return LinkNotetaskID;
		}
    	else if (recordType.equals("usertask")) {
			log.info("Setting record_id to deleted task");
			return LinkAdditionalUsertaskID;
		}
    	else if (recordType.equals("casetask")) {
			log.info("Setting record_id to deleted task");
			return casetaskID.toUpperCase();
		}
    	else if (recordType.equals("calltask")) {
			log.info("Setting record_id to deleted task");
			return LinkCalltaskID;
		}
    	else if (recordType.equals("validcasetask")) {
			log.info("Setting record_id to deleted task");
			return LinkedcasetaskID;
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
    	else if (linked_id.equals("linkedusercaseid")) {
			log.info("Setting linked_id to callID");
			return userID.toUpperCase();
		}
    	else if (linked_id.equals("linkedaccountcaseid")) {
			log.info("Setting linked_id to callID");
			return clientBeanID.toUpperCase();
		}
    	else if (linked_id.equals("linkedcontactcaseid")) {
			log.info("Setting linked_id to callID");
			return contactBeanID.toUpperCase();
		}
    	else if (linked_id.equals("linkednotecaseid")) {
			log.info("Setting linked_id to callID");
			return noteID.toUpperCase();
		}
    	else if (linked_id.equals("linkedopptycaseid")) {
			log.info("Setting linked_id to callID");
			return opptyID.toUpperCase();
		}
    	else if (linked_id.equals("linkedcallcaseid")) {
			log.info("Setting linked_id to callID");
			return callID.toUpperCase();
		}
    	else if (linked_id.contains("invalid")) {
			log.info("Setting linked_id to invalid");
			return "invalid";
		}
    	else {
			log.info("linked_id field in csv file does not contain a valid value was " + linked_id);
			Assert.assertTrue(false);
			return "fail";
		}
	}
	
	private String getlinkedItem(String itemType){
		if (itemType.equals("tasktask")) {
			return TaskForLinkingtaskID;
		}
    	else if (itemType.equals("accounttask")) {
			return clientBeanID;
		}
    	else if (itemType.equals("contacttask")) {
			return contactBeanID;
		}
    	else if (itemType.equals("opptytask")) {
			return opptyID;
		}
    	else if (itemType.equals("notetask")) {
			return noteID;
		}
    	else if (itemType.equals("usertask")) {
			return userID;
		}
    	else if (itemType.equals("calltask")) {
			return callID;
		}
    	else {
			return "getLinkedItemFailed";
		}
	}

}
