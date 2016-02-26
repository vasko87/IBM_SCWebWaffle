/**
 * 
 */
package com.ibm.salesconnect.test.api.tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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
import com.ibm.salesconnect.API.MeetingRestAPI;
import com.ibm.salesconnect.API.NoteRestAPI;
import com.ibm.salesconnect.API.OpportunityRestAPI;
import com.ibm.salesconnect.API.SugarAPI;
import com.ibm.salesconnect.API.TaskRestAPI;
import com.ibm.salesconnect.API.TestDataHolder;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.test.mobile.TaskRestAPITests;

/**
 * @author timlehane
 * @date Sep 17, 2014
 */


public class GETTask extends ApiBaseTest {
	
	private static final Logger log = LoggerFactory.getLogger(GETTask.class);
	
	@Parameters({ "apiExtension", "applicationName", "APIM", "apim_environment" })
	private GETTask(@Optional("tasks") String apiExtension,
			@Optional("SC Auto read") String applicationName,
			@Optional("true") String APIM,
			@Optional("development") String environment) {

		super(apiExtension, applicationName, APIM, environment);

	}
		
	private TestDataHolder testData;
    int rand = new Random().nextInt(100000);
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
	private String LinkEmailtaskID = null;
	private String LinkMeetingtaskID = null;
	private String noteID = null;
	private String noteSubject = "post task note subject";
	private String callID = null;
	private String callSubject = "post task call subject";
	private String emailID = null;
	private String meetingID = null;
	private String clientBeanID = null;
	private String assignedUserID = null;
	private String userID = null;
	private String cnum = "-0505K754";
	private String accountID = null;

	
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
		accountID = poolClient.getCCMS_ID();
		clientBeanID = new CollabWebAPI().getbeanIDfromClientID(baseURL, accountID, headers );
		
		log.info("Creating contact");
		SugarAPI sugarAPI = new SugarAPI();
		sugarAPI.createContact(testConfig.getBrowserURL(), contactID, accountID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");
		assignedUserID = new APIUtilities().getUserBeanIDFromEmail(baseURL, user1.getEmail(), user1.getPassword());	
		ContactRestAPI contactAPI = new ContactRestAPI();
		contactBeanID = contactAPI.createContactreturnBean(baseURL, headers, "ContactFirst", "ContactLast", "US", "(555) 555-5555", assignedUserID);
		
		
		log.info("Creating oppty");
		opptyID = sugarAPI.createOppty(baseURL, "", contactID, accountID, user1.getEmail(), user1.getPassword(), GC.emptyArray);
		
		log.info("Creating note");
		NoteRestAPI noteAPI = new NoteRestAPI();
		noteID = noteAPI.createNotereturnBean(testConfig.getBrowserURL(), OAuthToken, noteSubject, "post task note description", assignedUserID);
		
		log.info("Creating call");
		CallRestAPI callRestAPI = new CallRestAPI();
		callID = callRestAPI.createCallreturnBean(testConfig.getBrowserURL(), OAuthToken, callSubject, "2013-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);
		
		log.info("Creating Email");
		//emailID = new EmailRestAPI().createEmailreturnBean(baseURL, OAuthToken);
		
		log.info("Creating Meeting");
		meetingID = new MeetingRestAPI().createMeetingreturnBean(baseURL, OAuthToken, assignedUserID);
		
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
		LinkEmailtaskID = TaskRestAPITests.createTaskHelper(user1, "Email Link task", log, baseURL);
		LinkMeetingtaskID = TaskRestAPITests.createTaskHelper(user1, "Meeting Link task", log, baseURL);
		taskRestAPI.linkRecordToTaskAndVerify(baseURL, OAuthToken, LinkedTasktaskID, TaskForLinkingtaskID, "Tasks");
		taskRestAPI.linkRecordToTaskAndVerify(baseURL, OAuthToken, LinkAccounttaskID, clientBeanID, "Accounts");
		taskRestAPI.linkRecordToTaskAndVerify(baseURL, OAuthToken, LinkContacttaskID, contactBeanID, "Contacts");
		taskRestAPI.linkRecordToTaskAndVerify(baseURL, OAuthToken, LinkOpptytaskID, opptyID, "Opportunity");
		taskRestAPI.linkRecordToTaskAndVerify(baseURL, OAuthToken, LinkNotetaskID, noteID, "Notes");
		taskRestAPI.linkRecordToTaskAndVerify(baseURL, OAuthToken, LinkAdditionalUsertaskID, userID, "Users");
		taskRestAPI.linkRecordToTaskAndVerify(baseURL, OAuthToken, LinkCalltaskID, callID, "Calls");
		//taskRestAPI.linkRecordToTaskAndVerify(baseURL, OAuthToken, LinkEmailtaskID, emailID, "Email");
		//taskRestAPI.linkRecordToTaskAndVerify(baseURL, OAuthToken, LinkMeetingtaskID, meetingID, "Meetings");
		DeletedtaskID = TaskRestAPITests.createTaskHelper(user1,"Base task",log,testConfig.getBrowserURL());
		
		String jsonString = taskRestAPI.deleteTask(testConfig.getBrowserURL(), OAuthToken, DeletedtaskID);
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
    	this.addDataFile("test_config/extensions/api/getTask.csv");
    	
    	//Return an array of arrays where each item in the array is a HashMap of parameter values
    	//Test content
       return testData.getAllDataRows();
    }
    

    @Test(dataProvider = "DataProvider")
    public void TaskTest(HashMap<String,Object> parameterValues){
		log.info("Start TaskTest test.");
		String expectedResponseCode = "200";
		String recordID = null;
		String link = null;
		String link_id = null;
		String urlExtension = "";
		String linked_item = "";
		String id_field = "";
		String field_name = "";

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
					link = pairs.getValue().toString();
				}
			}
            else if (pairs.getKey().equals("link_id")) {
				link_id = getlinkID(pairs.getValue().toString());
			}
            else if (pairs.getKey().equals("id_field")) {
            	id_field = "id_field";
            }
            else if (pairs.getKey().equals("field_name")) {
            	field_name = pairs.getValue().toString();
            }
        }
        
        if (!recordID.equals("0")) {
			urlExtension+= "/" + recordID ;
		}
        else {
			urlExtension+= "/";
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
        
        if ((id_field.equals("id_field") && (!field_name.equals("")))) {
			urlExtension+= "id_field/" + field_name;
		}
        
		log.info("Getting user.");		
		//User user = commonUserAllocator.getUser();
		User user = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
		
		log.info("Retrieving OAuth2Token.");		
		String token = getOAuthToken(user);
		
		log.info("Sending GET request");
		TaskRestAPI taskRestAPI = new TaskRestAPI();
		String jsonString = taskRestAPI.getTask(getRequestUrl(urlExtension, null), token, expectedResponseCode);
		
		log.info("Checking response");
		if (!expectedResponseCode.equals("200")) {
			//If expected fail do not parse response, response code already checked 
			log.info("Task creation failed as expected");
		} else {
			if (!link_id.equals("0")) {
				log.info("linked item id is not blank so checking that");
				Assert.assertTrue(new APIUtilities().checkIfValuePresentInJson(jsonString, "id", link_id), "ID: " + link_id + " was not returned correctly");
			}
			else {
				if (recordID.equals("0")) {
					log.info("Getting all tasks");
					int count = new APIUtilities().getKeyCountInJsonString(jsonString, "id");
					log.info("id count in result" + new APIUtilities().getKeyCountInJsonString(jsonString, "id"));
					Assert.assertTrue(count >=1, "Count is less than 1");
				}
				else {
					if (link.equals("0")) {
						Assert.assertTrue(new APIUtilities().checkIfValuePresentInJson(jsonString, "id", recordID), "link_id is blank, link is blank, checking record_id: " + recordID);
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
	public void Test_taskWrongClientID(){
		if (APIm)
		{
		log.info("Start test Test_taskWrongClientID");
		log.info("Getting user.");		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");		
		String token = getOAuthToken(user);
		
		log.info("Sending GET request");
		TaskRestAPI taskRestAPI = new TaskRestAPI();
		taskRestAPI.getTask(getApiManagement() + getApiExtension() + "?client_id=wrong&"+getclient_secret(), token, "401");
		
		log.info("End test Test_taskWrongClientID");
		}
	}
	
	@Test
	public void Test_taskMissingClientID(){
		if (APIm)
		{
		log.info("Start test Test_taskMissingClientID");
		log.info("Getting user.");		
		User user = commonUserAllocator.getUser(this);
		
		log.info("Retrieving OAuth2Token.");		
		String token = getOAuthToken(user);
		
		log.info("Sending GET request");
		TaskRestAPI taskRestAPI = new TaskRestAPI();
		taskRestAPI.getTask(getApiManagement() + getApiExtension() + "?"+getclient_secret(), token, "401");
		
		log.info("End test Test_taskMissingClientID");
		}
	}
		
	
	@Test
	public void Test_taskWrongClientSecret() {
		if (APIm) {
			log.info("Start test Test_taskWrongClientSecret");
			log.info("Getting user.");
			User user = commonUserAllocator.getUser(this);

			log.info("Retrieving OAuth2Token.");
			String token = getOAuthToken(user);

			log.info("Sending GET request");
			TaskRestAPI taskRestAPI = new TaskRestAPI();
			taskRestAPI.getTask(getApiManagement() + getApiExtension() + "?"
					+ getclient_ID() + "&clientSecret=wrong", token, "401");

			log.info("End test Test_taskWrongClientSecret");
		}
	}

	@Test
	public void Test_taskMissingClientSecret() {
		if (APIm) {
			log.info("Start test Test_taskMissingClientSecret");
			log.info("Getting user.");
			User user = commonUserAllocator.getUser(this);

			log.info("Retrieving OAuth2Token.");
			String token = getOAuthToken(user);

			log.info("Sending GET request");
			TaskRestAPI taskRestAPI = new TaskRestAPI();
			taskRestAPI.getTask(getApiManagement() + getApiExtension() + "?"
					+ getclient_ID(), token, "401");

			log.info("End test Test_taskMissingClientSecret");
		}
	}

	@Test
	public void TestTask_WrongURI() {
		if (APIm) {
			log.info("Starting TestTask_WrongURI");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,
					this);

			log.info("Retrieving OAuth2Token.");
			String token = getOAuthToken(user1);

			log.info("Sending GET request");
			TaskRestAPI taskRestAPI = new TaskRestAPI();
			taskRestAPI.getTask(addclientIDAndSecret(getApiManagement()
					+ "tasks1"), token, "404");

			log.info("Ending TestTask_WrongURI");
		}
	}

	@Test
	public void GetTaskwrongCase() {
		if (APIm) {
			log.info("Starting GetTaskwrongCase");
			User user1 = commonUserAllocator.getUser(this);

			log.info("Retrieving OAuth2Token.");
			String OAuthToken = getOAuthToken(user1);

			log.info("Sending GET request");
			TaskRestAPI taskRestAPI = new TaskRestAPI();
			taskRestAPI.getTask(addclientIDAndSecret(getApiManagement()
					+ "Tasks"), OAuthToken, "404");

			log.info("Ending GetTaskwrongCase");
		}
	}

//	    @Test
//	    public void GetLinkedDACHContactDACHUser(){
//	    	
//	    	User user1 = commonUserAllocator.getGroupUser("dach_users", this);
//	    	//User user1 = new User("de01@tst.ibm.com", "passw0rd", "de01@tst.ibm.com", "de01", "bde01", "DE", "de01 bde01", "GetLinkedDACHContactDACHUser");
//			log.info("Creating DACHcontact");
//			SugarAPI sugarAPI = new SugarAPI();
//			sugarAPI.createContact(testConfig.getBrowserURL(), contactID, accountID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");
//			assignedUserID = new APIUtilities().getUserBeanIDFromEmail(baseURL, user1.getEmail(), user1.getPassword());
//			String OAuthToken = new LoginRestAPI().getOAuth2Token(baseURL, user1.getEmail(), user1.getPassword());
//			String headers[] = {"OAuth-Token", OAuthToken};
//			ContactRestAPI contactAPI = new ContactRestAPI();
//			String contactBeanID = contactAPI.createContactreturnBean(baseURL, headers, "ContactFirst", "ContactLast", "DE", "(555) 555-5555", assignedUserID);
//			
//			TaskRestAPI taskRestAPI = new TaskRestAPI();
//			String taskID = TaskRestAPITests.createTaskHelper(user1,log,testConfig.getBrowserURL());
//			taskRestAPI.linkRecordToTaskAndVerify(baseURL, OAuthToken, taskID, contactBeanID, "Contacts");
//			
//			log.info("Sending GET request");
//			
//			String token = getOAuthToken(user1);	
//			taskRestAPI.getTask(getRequestUrl("/" +taskID + "/link/contacts/" + contactBeanID, null), token, "200");
//					
//	    }
//	 
//	    @Test
//	    public void GetLinkedDACHContactnonDACHUser(){
//	    	User user1 = commonUserAllocator.getGroupUser("dach_users", this);
//	    	User user2 = commonUserAllocator.getUser(this);
//	    	//User user1 = new User("de01@tst.ibm.com", "passw0rd", "de01@tst.ibm.com", "de01", "bde01", "DE", "de01 bde01", "GetLinkedDACHContactDACHUser");
//			log.info("Creating DACHcontact");
//			SugarAPI sugarAPI = new SugarAPI();
//			sugarAPI.createContact(testConfig.getBrowserURL(), contactID, accountID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");
//			assignedUserID = new APIUtilities().getUserBeanIDFromEmail(baseURL, user1.getEmail(), user1.getPassword());
//			String OAuthToken = new LoginRestAPI().getOAuth2Token(baseURL, user1.getEmail(), user1.getPassword());
//			String headers[] = {"OAuth-Token", OAuthToken};
//			ContactRestAPI contactAPI = new ContactRestAPI();
//			String contactBeanID = contactAPI.createContactreturnBean(baseURL, headers, "ContactFirst", "ContactLast", "DE", "(555) 555-5555", assignedUserID);
//			
//			TaskRestAPI taskRestAPI = new TaskRestAPI();
//			String taskID = TaskRestAPITests.createTaskHelper(user1,log,testConfig.getBrowserURL());
//			taskRestAPI.linkRecordToTaskAndVerify(baseURL, OAuthToken, taskID, contactBeanID, "Contacts");
//			
//			log.info("Sending GET request");
//			
//			String token = getOAuthToken(user2);
//	    	
//			taskRestAPI.getTask(getRequestUrl("/" + taskID + "/link/contacts/" + contactBeanID, null), token, "404");
//					
//	    }
	    
	    
	    @Test
	    public void Getalltasks_withoffsetandmaxResult(){
	    	log.info("Start of test Getalltasks_withoffsetandmaxResult");
	    	User user1 = commonUserAllocator.getUser(this);
	    	   	
	    	TaskRestAPI taskRestAPI = new TaskRestAPI();
	    	
	    	String token = getOAuthToken(user1);
	    	ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
	    	params.add(new BasicNameValuePair("offset","1"));
	    	params.add(new BasicNameValuePair("max_num","10"));
	    	
			String jsonString = taskRestAPI.getTask(getRequestUrl(null, params), token, "200");
					
			Assert.assertTrue(new APIUtilities().checkIfValuePresentInJson(jsonString, "next_offset", 11)); 
			Assert.assertTrue(new APIUtilities().getKeyCountInJsonString(jsonString, "my_favorite") <= 10);
			
	    	log.info("End of test Getalltasks_withoffsetandmaxResult");
	    }
	    
	    @Test
	    public void Getalltasks_withqandfieldsEqualsName(){
	    	log.info("Start of test Getalltasks_withqandfieldsEqualsName");
	    	User user1 = commonUserAllocator.getUser(this);
	    	String Name = "task";
	    		//"Getalltasks_withqandfieldsEqualsName" + String.valueOf(rand);
			TaskRestAPI taskRestAPI = new TaskRestAPI();
			//String taskID = TaskRestAPITests.createTaskHelper(user1,Name,log,testConfig.getBrowserURL());
						
			String token = getOAuthToken(user1);
		  	ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
	    	params.add(new BasicNameValuePair("q","Task%21Name"));
	    	params.add(new BasicNameValuePair("fields","name"));
	    
			String jsonString = taskRestAPI.getTask(getRequestUrl(null, params), token, "200");
							
			Assert.assertTrue(new APIUtilities().checkIfValueContainedInJson(jsonString, "name", Name)); 
			
	    	log.info("End of test Getalltasks_withqandfieldsEqualsName");
	    }
	    
	    @Test
	    public void Getalltasks_multiplePages(){
	    	log.info("Start of test Getalltasks_multiplePages");
	    	User user1 = commonUserAllocator.getUser(this);
	    	
	    	TaskRestAPI taskRestAPI = new TaskRestAPI();
	    	
	    	String token = getOAuthToken(user1);
	      	ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
	    	params.add(new BasicNameValuePair("offset","1"));
	    	params.add(new BasicNameValuePair("max_num","10"));
	   
			String jsonString = taskRestAPI.getTask(getRequestUrl(null, params), token, "200");
		
			Assert.assertTrue(new APIUtilities().checkIfValuePresentInJson(jsonString, "next_offset", 11)); 
			Assert.assertTrue(new APIUtilities().getKeyCountInJsonString(jsonString, "my_favorite") <= 10);
			
			
			params.clear();
		  	params.add(new BasicNameValuePair("offset","11"));
	    	params.add(new BasicNameValuePair("max_num","10"));
	  
	    	String jsonString2 = taskRestAPI.getTask(getRequestUrl(null, params), token, "200");
			
			Assert.assertTrue(new APIUtilities().checkIfValuePresentInJson(jsonString2, "next_offset", 21)); 
			Assert.assertTrue(new APIUtilities().getKeyCountInJsonString(jsonString2, "my_favorite") <= 10);
			
	    	log.info("End of test Getalltasks_multiplePages");
	    }
	    
//	    @Test
	    public void Getalltasks_withorder(){
	    	log.info("Start of test Getalltasks_withorder");
	    	User user1 = commonUserAllocator.getUser(this);
	    	
	    	TaskRestAPI taskRestAPI = new TaskRestAPI();
	    		   	
	    	String token = getOAuthToken(user1);
	    	
	     	ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
	    	params.add(new BasicNameValuePair("q","\'task\'"));
	    	params.add(new BasicNameValuePair("order_by","name:DESC"));
	   
			String jsonString = taskRestAPI.getTask(getRequestUrl(null, params), token, "200");
			    			
			Assert.assertTrue(new APIUtilities().checkIfValueContainedInJson(jsonString, "name", "task")); 
			
	    	log.info("End of test Getalltasks_withorder");
	    }
	    
	    @Test
	    public void Getalltasks_favorites(){
	    	log.info("Start of test Getalltasks_favorites");
	    	User user1 = commonUserAllocator.getUser(this);
	    	
	    	TaskRestAPI taskRestAPI = new TaskRestAPI();
	    	
	    	String token = getOAuthToken(user1);
	    	ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
	    	params.add(new BasicNameValuePair("favorites","true"));
	    	
	    	String jsonString = taskRestAPI.getTask(getRequestUrl(null, params), token, "200");
				    		
			Assert.assertTrue(!new APIUtilities().checkIfValueContainedInJson(jsonString, "my_favorite", "false")); 
			
	    	log.info("End of test Getalltasks_favorites");
	    }
	    
//	    @Test
	    public void Getalltasks_notfavorites(){
	    	log.info("Start of test Getalltasks_notfavorites");
	    	User user1 = commonUserAllocator.getUser(this);
	    	
	    	TaskRestAPI taskRestAPI = new TaskRestAPI();
	    	
	    	String token = getOAuthToken(user1);
	    	ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
	    	params.add(new BasicNameValuePair("favorites","false"));
	    	    	
	    	String jsonString = taskRestAPI.getTask(getRequestUrl(null, params), token, "200");
			
			Assert.assertTrue(!new APIUtilities().checkIfValueContainedInJson(jsonString, "my_favorite", "true")); 
			
	    	log.info("End of test Getalltasks_notfavorites");
	    }
	    
	    @Test
	    public void Getalltasks_deleted(){
	    	log.info("Start of test Getalltasks_deleted");
	    	User user1 = commonUserAllocator.getUser(this);
	    	
			String DeletedtaskID = TaskRestAPITests.createTaskHelper(user1,"Base task",log,testConfig.getBrowserURL());
			String OAuthToken = new LoginRestAPI().getOAuth2Token(baseURL, user1.getEmail(), user1.getPassword());
			String deleteString = new TaskRestAPI().deleteTask(testConfig.getBrowserURL(), OAuthToken, DeletedtaskID);
			if (deleteString.equalsIgnoreCase("false")) {
				Assert.assertTrue(false, "Deleting Task failed.");
			}
			
	    	TaskRestAPI taskRestAPI = new TaskRestAPI();
	    
	    	String token = getOAuthToken(user1);
	    	ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
	    	params.add(new BasicNameValuePair("deleted","true"));
	    	    	
	    	String jsonString = taskRestAPI.getTask(getRequestUrl(null, params), token, "200");
			
	    	Assert.assertTrue(new APIUtilities().checkIfValueContainedInJson(jsonString, "deleted", "true")); 
			
	    	log.info("End of test Getalltasks_deleted");
	    }
	    
//	    @Test
	    public void Getalltasks_notdeleted(){
	    	log.info("Start of test Getalltasks_notdeleted");
	    	User user1 = commonUserAllocator.getUser(this);
	    	
	    	TaskRestAPI taskRestAPI = new TaskRestAPI();
	    	
	    	String token = getOAuthToken(user1);
	    	ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
	    	params.add(new BasicNameValuePair("deleted","false"));
	    	    	
	    	String jsonString = taskRestAPI.getTask(getRequestUrl(null, params), token, "200");
					
			Assert.assertTrue(new APIUtilities().checkIfValueContainedInJson(jsonString, "deleted", "false")); 
			
	    	log.info("End of test Getalltasks_notdeleted");
	    }

	    
	    @Test
	    public void Gettasktasks_withoffsetandmaxResult(){
	    	log.info("Start of test Gettasktasks_withoffsetandmaxResult");
	    	User user1 = commonUserAllocator.getUser(this);
	    	TaskRestAPI taskRestAPI = new TaskRestAPI();
	    	String BasetaskID = TaskRestAPITests.createTaskHelper(user1,"Base task",log,baseURL);
			String LinkedTasktaskID = TaskRestAPITests.createTaskHelper(user1,"Link task",log,baseURL);
			taskRestAPI.linkRecordToTaskAndVerify(baseURL, new LoginRestAPI().getOAuth2Token(baseURL, user1.getEmail(), user1.getPassword()), LinkedTasktaskID, BasetaskID, "Tasks");
	    	sendOffset_MaxNum(user1, "tasks", BasetaskID);
			
	    	log.info("End of test Gettasktasks_withoffsetandmaxResult");
	    }
	    
	    @Test
	    public void Getaccounttasks_withoffsetandmaxResult(){
	    	log.info("Start of test Getaccounttasks_withoffsetandmaxResult");
	    	User user1 = commonUserAllocator.getUser(this);
			LoginRestAPI loginRestAPI = new LoginRestAPI();
			String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user1.getEmail(), user1.getPassword());
			String headers[] = {"OAuth-Token", OAuthToken};
	    	PoolClient poolClient= commonClientAllocator.getGroupClient(GC.SC,this);
	    	String clientBeanID = new CollabWebAPI().getbeanIDfromClientID(baseURL, poolClient.getCCMS_ID(), headers );
	    	
	    	TaskRestAPI taskRestAPI = new TaskRestAPI();
	    	String BasetaskID = TaskRestAPITests.createTaskHelper(user1,"Base task",log,baseURL);
			taskRestAPI.linkRecordToTaskAndVerify(baseURL, new LoginRestAPI().getOAuth2Token(baseURL, user1.getEmail(), user1.getPassword()),
					BasetaskID, clientBeanID, "Accounts");
	    	sendOffset_MaxNum(user1, "accounts", BasetaskID);
	    		
	    	log.info("End of test Getaccounttasks_withoffsetandmaxResult");
	    }
	    
	    @Test
	    public void Getcontactstasks_withoffsetandmaxResult(){
	    	log.info("Start of test Getcontactstasks_withoffsetandmaxResult");
	    	User user1 = commonUserAllocator.getUser(this);
	    	
	    	String contactID = "44SC-" + rand;
			LoginRestAPI loginRestAPI = new LoginRestAPI();
			String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user1.getEmail(), user1.getPassword());
			String headers[] = {"OAuth-Token", OAuthToken};
	    	PoolClient poolClient= commonClientAllocator.getGroupClient(GC.SC,this);
	    	String clientBeanID = new CollabWebAPI().getbeanIDfromClientID(baseURL, poolClient.getCCMS_ID(), headers );
	    	
			log.info("Creating contact");
			SugarAPI sugarAPI = new SugarAPI();
			sugarAPI.createContact(testConfig.getBrowserURL(), contactID, clientBeanID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");
			String assignedUserID = new APIUtilities().getUserBeanIDFromEmail(baseURL, user1.getEmail(), user1.getPassword());	
			ContactRestAPI contactAPI = new ContactRestAPI();
			String contactBeanID = contactAPI.createContactreturnBean(baseURL, headers, "ContactFirst", "ContactLast", "US", "(555) 555-5555", assignedUserID);
	    	
	    	TaskRestAPI taskRestAPI = new TaskRestAPI();
	    	String BasetaskID = TaskRestAPITests.createTaskHelper(user1,"Base task",log,baseURL);
			taskRestAPI.linkRecordToTaskAndVerify(baseURL, new LoginRestAPI().getOAuth2Token(baseURL, user1.getEmail(), user1.getPassword()),
					BasetaskID,contactBeanID, "Contacts");
	    	sendOffset_MaxNum(user1, "contacts", BasetaskID);
	    		
	    	log.info("End of test Getcontactstasks_withoffsetandmaxResult");
	    }
	    
	    
	    @Test
	    public void GetOpptytasks_withoffsetandmaxResult(){
	    	log.info("Start of test GetOpptytasks_withoffsetandmaxResult");
	    	User user1 = commonUserAllocator.getUser(this);
			PoolClient poolClient= commonClientAllocator.getGroupClient(GC.SC,this);
			String opptyID = "33SC-1" + rand;
			log.info("Getting client");
			LoginRestAPI loginRestAPI = new LoginRestAPI();
			String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user1.getEmail(), user1.getPassword());
			String headers[] = {"OAuth-Token", OAuthToken};
			String clientID = poolClient.getCCMS_ID();
			String clientBeanID = new CollabWebAPI().getbeanIDfromClientID(baseURL, clientID, headers );
			
			log.info("Creating contact");
			String contactID = "22SC-" + rand;
			SugarAPI sugarAPI = new SugarAPI();
			sugarAPI.createContact(testConfig.getBrowserURL(), contactID, clientID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");			
			
			log.info("Creating oppty");
			opptyID = new SugarAPI().createOppty(baseURL, "", contactID, clientID, user1.getEmail(), user1.getPassword(), GC.emptyArray);
	    	
	    	TaskRestAPI taskRestAPI = new TaskRestAPI();
	    	String BasetaskID = TaskRestAPITests.createTaskHelper(user1,"Base task",log,baseURL);
			taskRestAPI.linkRecordToTaskAndVerify(baseURL, new LoginRestAPI().getOAuth2Token(baseURL, user1.getEmail(), user1.getPassword()),
					BasetaskID, opptyID, "Opportunity");
	    	sendOffset_MaxNum(user1, "opportunities", BasetaskID);
	    		
	    	log.info("End of test GetOpptytasks_withoffsetandmaxResult");
	    }
		  @Test
		    public void TestTask_GetOtherUsersContact(){
		    	log.info("Starting TestTask_GetOtherUsersContact");
				User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
				User user2 = commonUserAllocator.getUser(this);
				String taskID = TaskRestAPITests.createTaskHelper(user1,log,testConfig.getBrowserURL());

				log.info("Creating contact");
				String headers[] = {"OAuth-Token", new LoginRestAPI().getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};
				String assignedUserID = new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(), user1.getEmail(), 
						user1.getPassword());	
				String contactBeanID = new ContactRestAPI().createContactreturnBean(testConfig.getBrowserURL(), headers, 
						"ContactFirst", "ContactLast", "US", "(555) 555-5555", assignedUserID);
				
				new TaskRestAPI().linkRecordToTaskAndVerify(baseURL, 
						new LoginRestAPI().getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword()), 
						taskID, contactBeanID, "Contacts");
				
				TaskRestAPI taskRestAPI = new TaskRestAPI();
				String token = getOAuthToken(user2);
		    	ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		    	params.add(new BasicNameValuePair("deleted","true"));
		    	    	
		    	taskRestAPI.getTask(getRequestUrl("/" + taskID + "/links/contacts/" + contactBeanID, null), token, "404");
				
				log.info("Ending TestTask_GetOtherUsersContact");
		    }
		  
		  @Test
		    public void TestTask_GetRestrictedOpptyValid(){
		    	log.info("Starting TestTask_GetRestrictedOpptyValid");
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

				opptyID = new OpportunityRestAPI().createRestrictedOpportunitySpecifyID(testConfig.getBrowserURL(), 
						new LoginRestAPI().getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword()), "Restricted Oppty", clientbeanID, contactBeanID,
						"SLSP", "03", "2013-10-28", assignedUserID, "RESTOPTY");
				
				new TaskRestAPI().linkRecordToTaskAndVerify(baseURL, 
						new LoginRestAPI().getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword()), taskID, opptyID, "Opportunity");
				
				
				TaskRestAPI taskRestAPI = new TaskRestAPI();
				String token = getOAuthToken(user2);
		    	ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		    	params.add(new BasicNameValuePair("q",opptyID));
		    	
		    	String postResponseString = taskRestAPI.getTask(getRequestUrl("/" + taskID + "/link/opportunities", params), token, "200");
								
				Assert.assertTrue(new APIUtilities().checkIfValuePresentInJson(postResponseString, "id", opptyID),
							"id " + opptyID + " is not present in related field but should be"); 

				log.info("Ending TestTask_GetRestrictedOpptyValid");
		    }
		  
		  @Test
		    public void TestTask_GetRestrictedclientValidUser(){
		    	log.info("Starting TestTask_GetRestrictedclientValidUser");
				User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
				User user2 = commonUserAllocator.getUser(this);
				String taskID = TaskRestAPITests.createTaskHelper(user1,log,testConfig.getBrowserURL());
				
				log.info("Creating contact");
				String clientID = commonClientAllocator.getGroupClient("RES",this).getCCMS_ID();
				String headers[] = {"OAuth-Token", new LoginRestAPI().getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};

				String clientbeanID = new CollabWebAPI().getbeanIDfromClientID(baseURL, clientID, headers );
				
				new TaskRestAPI().linkRecordToTaskAndVerify(baseURL, 
						new LoginRestAPI().getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword()), taskID, clientbeanID, "Accounts");
				
				TaskRestAPI taskRestAPI = new TaskRestAPI();
				String token = getOAuthToken(user1);
		    	ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		    	params.add(new BasicNameValuePair("q",clientbeanID));
		    	
		    	String postResponseString = taskRestAPI.getTask(getRequestUrl("/" + taskID + "/link/accounts", params), token, "200");
							
				Assert.assertTrue(new APIUtilities().checkIfValuePresentInJson(postResponseString, "id", clientbeanID),
							"id " + clientbeanID + " is not present in related field but should be"); 

				log.info("Ending TestTask_GetRestrictedclientValidUser");
		    }
		  
		  @Test
		    public void TestTask_GetRestrictedclientInValidUser(){
		    	log.info("Starting TestTask_GetRestrictedclientInValidUser");
				User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
				User user2 = commonUserAllocator.getGroupUser("noMem_users", this);
				//User user2 = commonUserAllocator.getUser(this);
				String taskID = TaskRestAPITests.createTaskHelper(user1,log,testConfig.getBrowserURL());
				
				log.info("Creating contact");
				String clientID = commonClientAllocator.getGroupClient("RES",this).getCCMS_ID();
				String headers[] = {"OAuth-Token", new LoginRestAPI().getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};

				String clientbeanID = new CollabWebAPI().getbeanIDfromClientID(baseURL, clientID, headers );
				
				new TaskRestAPI().linkRecordToTaskAndVerify(baseURL,
						new LoginRestAPI().getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword()), taskID, clientbeanID, "Accounts");
				
				TaskRestAPI taskRestAPI = new TaskRestAPI();
				String token = getOAuthToken(user2);
		    
		    	String postResponseString = taskRestAPI.getTask(getRequestUrl("/" + taskID + "/link/accounts", null), token, "200");
						
				Assert.assertFalse(new APIUtilities().checkIfValuePresentInJson(postResponseString, "parent_id", clientbeanID),
						"id " + clientbeanID + " is present in related field but should not be"); 

				log.info("Ending TestTask_GetRestrictedclientInValidUser");
		    }
		  
		  //@Test
		    public void TestTask_GetRestrictedContactValidUser(){
		    	log.info("Starting TestTask_GetRestrictedContactValidUser");
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
				
				new TaskRestAPI().linkRecordToTaskAndVerify(baseURL, 
						new LoginRestAPI().getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword()), 
						taskID, contactBeanID, "Contacts");
				
				TaskRestAPI taskRestAPI = new TaskRestAPI();
				String token = getOAuthToken(user2);
		    	ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		    	params.add(new BasicNameValuePair("q",contactBeanID));
		    	
		    	taskRestAPI.getTask(getRequestUrl("/" + taskID + "/link/contacts", params), token, "200");
		    	
				log.info("Ending TestTask_GetRestrictedContactValidUser");
		    }
		  
		  //@Test
		    public void TestTask_GetRestrictedContactInValidUser(){
		    	log.info("Starting TestTask_GetRestrictedContactInValidUser");
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

				new TaskRestAPI().linkRecordToTaskAndVerify(baseURL, 
						new LoginRestAPI().getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword()),
						taskID, contactBeanID, "Contacts");

				TaskRestAPI taskRestAPI = new TaskRestAPI();
				String token = getOAuthToken(user2);
		    	
		    	String postResponseString = taskRestAPI.getTask(getRequestUrl("/" + taskID + "/link/contacts", null), token, "200");
		    
				Assert.assertFalse(new APIUtilities().checkIfValuePresentInJson(postResponseString, "id", contactBeanID),
						"id " + contactBeanID + " is present in related field but should not be"); 

				log.info("Ending TestTask_GetRestrictedContactInValidUser");
		    }
	 
	    
	    public void sendOffset_MaxNum(User user1, String module, String taskID){    	
	    	TaskRestAPI taskRestAPI = new TaskRestAPI();
	    	
			String token = getOAuthToken(user1);
			
		 	ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
	    	params.add(new BasicNameValuePair("offset","0"));
	    	params.add(new BasicNameValuePair("max_num","10"));
	
	    	String jsonString = taskRestAPI.getTask(getRequestUrl("/" + taskID + "/link/" + module, params), token, "200");
	    		
			Assert.assertTrue(new APIUtilities().checkIfValuePresentInJson(jsonString, "next_offset", -1)); 
			Assert.assertTrue(new APIUtilities().getKeyCountInJsonString(jsonString, "my_favorite") == 1);
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
			return BasetaskID.toUpperCase();
		}
    	else if (recordType.equals("calltask")) {
			log.info("Setting record_id to deleted task");
			return LinkCalltaskID;
		}
    	else if (recordType.equals("emailtask")) {
			log.info("Setting record_id to email task");
			return LinkEmailtaskID;
		}
    	else if (recordType.equals("meetingtask")) {
			log.info("Setting record_id to meeting task");
			return LinkMeetingtaskID;
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
    	else if (linked_id.equals("linkedemailid")) {
			log.info("Setting linked_id to emailID");
			return emailID;
		}
    	else if (linked_id.equals("linkedmeetingid")) {
			log.info("Setting linked_id to meetingID");
			return meetingID;
		}
    	else if (linked_id.equals("linkedcnum")) {
			log.info("Setting linked_id to cnum");
			return cnum;
		}
    	else if (linked_id.equals("linkedccmsID")) {
			log.info("Setting linked_id to ccmsID");
			return accountID;
		}
    	else if (linked_id.equals("linkedCIcontactid")) {
			log.info("Setting linked_id to CIcontactID");
			return contactBeanID;
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
    	else if (itemType.equals("emailtask")) {
			return emailID;
		}
    	else if (itemType.equals("meetingtask")) {
			return meetingID;
		}
    	else {
			return "getLinkedItemFailed";
		}
	}
	
}