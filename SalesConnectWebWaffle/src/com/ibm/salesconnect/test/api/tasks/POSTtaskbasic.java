/**
 * 
 */
package com.ibm.salesconnect.test.api.tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.json.simple.JSONArray;
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
import com.ibm.salesconnect.API.SugarAPI;
import com.ibm.salesconnect.API.TaskRestAPI;
import com.ibm.salesconnect.API.TestDataHolder;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.common.HttpUtils;
import com.ibm.salesconnect.test.mobile.TaskRestAPITests;

/**
 * @author timlehane
 * @date Mar 18, 2014
 */
 
@SuppressWarnings("unchecked")
public class POSTtaskbasic extends ApiBaseTest {

	@Parameters({ "apiExtension", "applicationName", "APIM", "apim_environment" })
	private POSTtaskbasic(@Optional("tasks") String apiExtension,
			@Optional("SC Auto create") String applicationName,
			@Optional("true") String APIM,
			@Optional("development") String environment) {

		super(apiExtension, applicationName, APIM, environment);

	}
	

	private static final Logger log = LoggerFactory.getLogger(POSTtaskbasic.class);
	    private TestDataHolder testData;
	    int rand = new Random().nextInt(100000);
		private String contactID = "22SC-" + rand;
		private String contactBeanID = null;
		private String contactName = "ContactFirst ContactLast";
		private String opptyID = null;
		private String taskID = null;
		private String taskName = "Task Name";
		private String noteID = null;
		private String noteSubject = "post task note subject";
		private String accountID = null;
		private String clientBeanID = null;
		private String clientName = null;
		private String assignedUserID = null;
		private String callID = null;

	
		private ArrayList<String> taskArray;
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);

		private void createObjects(){
		
			log.info("Start creating prerequisites");

			PoolClient poolClient= commonClientAllocator.getGroupClient(GC.SC,this);
			
			LoginRestAPI loginRestAPI = new LoginRestAPI();
			String OAuthToken = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword());
			String headers[] = {"OAuth-Token", OAuthToken};
			accountID = poolClient.getCCMS_ID();
			clientBeanID = new CollabWebAPI().getbeanIDfromClientID(testConfig.getBrowserURL(), accountID, headers );
			clientName=poolClient.getClientName(testConfig.getBrowserURL(), user1);
				
			SugarAPI sugarAPI = new SugarAPI();
			sugarAPI.createContact(testConfig.getBrowserURL(), contactID, accountID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");
			
			assignedUserID = new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword());
			
			ContactRestAPI contactAPI = new ContactRestAPI();
			contactBeanID = contactAPI.createContactreturnBean(testConfig.getBrowserURL(), headers, "ContactFirst", "ContactLast", "US", "(555) 555-5555", assignedUserID);
			
			opptyID = sugarAPI.createOppty(testConfig.getBrowserURL(), "", contactID, accountID, user1.getEmail(), user1.getPassword(), GC.emptyArray);
			
			taskID = TaskRestAPITests.createTaskHelper(user1,log,testConfig.getBrowserURL());
			
			NoteRestAPI noteAPI = new NoteRestAPI();
			noteID = noteAPI.createNotereturnBean(testConfig.getBrowserURL(), OAuthToken, noteSubject, "post task note description", assignedUserID);
			
			log.info("Creating call");
			CallRestAPI callRestAPI = new CallRestAPI();
			callID = callRestAPI.createCallreturnBean(testConfig.getBrowserURL(), OAuthToken, "Call Subject", "2013-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);
			
			log.info("Values being used in tests");
	    	log.info("Task Bean ID = " + taskID);
	    	log.info("Task Name = " + taskName);
	    	log.info("ClientID = " + accountID);
	    	log.info("Client Bean ID" + clientBeanID);
	    	log.info("Oppty ID = " + opptyID);
	    	log.info("Contact Bean ID  = " + contactBeanID);
	    	log.info("Contact Name = " + contactName);
	    	log.info("assigned_user_id");
			log.info("Note ID = " + noteID);
			
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
	    	this.addDataFile("test_config/extensions/api/task.csv");
	    	//Return an array of arrays where each item in the array is a HashMap of parameter values
	    	//Test content
	       return testData.getAllDataRows();
	    }


	 
		@Test(dataProvider = "DataProvider")
	    public void TaskTest(HashMap<String,Object> parameterValues){

			log.info("Start test.");
			String expectedResponseCode = "200";
	    	
	    	JSONArray jsonArray = new JSONArray();
	        log.info("Removing Empty value pairs and retrieving expected response code");
	        Iterator<Map.Entry<String, Object>> it = parameterValues.entrySet().iterator();
	        while (it.hasNext()) {
	            Map.Entry<String, Object> pairs = it.next();
	            if (pairs.getValue().toString().isEmpty()||pairs.getValue().toString().equals("''")||pairs.getValue().toString().equals(" ")) {
	            	it.remove();
	            	continue;
	            }
	            if (pairs.getValue().equals("*blank*")) {
					pairs.setValue("");
				}
	            
	            if(pairs.getKey().equals("expected_response")){
	            	expectedResponseCode=pairs.getValue().toString();
	            	it.remove();
	            }
	            else if (pairs.getKey().equals("TC_Name")) {
					log.info("This is test " + pairs.getValue().toString());
					it.remove();
				}
	            else if(pairs.getKey().equals("related_Accounts")){
	            	if (pairs.getValue().equals("1")) {
						jsonArray.add(populateRelated("Accounts", clientBeanID, clientName));
					}
	            	else if (pairs.getValue().equals("2")) {
	            		jsonArray.add(populateRelated("Accounts", "invalidID", clientName));
					}
	            	else if (pairs.getValue().equals("3")) {
	            		jsonArray.add(populateRelated("Accounts", clientBeanID, "invalid name"));
					}
	            	it.remove();
	            }
	            else if (pairs.getKey().equals("related_Contacts")) {
	            	if (pairs.getValue().equals("1")) {
						jsonArray.add(populateRelated("Contacts", contactBeanID, contactName));
					}
	            	else if (pairs.getValue().equals("2")) {
	            		jsonArray.add(populateRelated("Contacts", "invalidID", contactName));
					}
	            	else if (pairs.getValue().equals("3")) {
	            		jsonArray.add(populateRelated("Contacts", contactBeanID, "invalid name"));
					}
					it.remove();
				}
	            else if(pairs.getKey().equals("related_Opportunities")){
	            	if (pairs.getValue().equals("1")) {
						jsonArray.add(populateRelated("Opportunities", opptyID, opptyID));
					}
	            	else if (pairs.getValue().equals("2")) {
	            		jsonArray.add(populateRelated("Opportunities", "invalidID", opptyID));
					}
	            	else if (pairs.getValue().equals("3")) {
	            		jsonArray.add(populateRelated("Opportunities", opptyID, "invalid name"));
					}
					it.remove();
	            }
	            else if (pairs.getKey().equals("related_Tasks")) {
	            	if (pairs.getValue().equals("1")) {
						jsonArray.add(populateRelated("Tasks", taskID, taskName));
					}
	            	else if (pairs.getValue().equals("2")) {
	            		jsonArray.add(populateRelated("Tasks", "invalidID", taskName));
					}
	            	else if (pairs.getValue().equals("3")) {
	            		jsonArray.add(populateRelated("Tasks", taskID, "invalid name"));
					}
					it.remove();
				}
	            else if (pairs.getKey().equals("related_Notes")) {
	            	if (pairs.getValue().equals("1")) {
						jsonArray.add(populateRelated("Notes", noteID, noteSubject));
					}
	            	else if (pairs.getValue().equals("2")) {
	            		jsonArray.add(populateRelated("Notes", "invalidID", noteSubject));
					}
	            	else if (pairs.getValue().equals("3")) {
	            		jsonArray.add(populateRelated("Notes", noteID, "invalid name"));
					}
					it.remove();
				}
	            else if (pairs.getKey().equals("related_Calls")) {
	            	if (pairs.getValue().equals("1")) {
						jsonArray.add(populateRelated("Calls", callID, "CallSubject"));
					}
	            	else if (pairs.getValue().equals("2")) {
	            		jsonArray.add(populateRelated("Calls", "invalidID", "CallSubject"));
					}
	            	else if (pairs.getValue().equals("3")) {
	            		jsonArray.add(populateRelated("Calls", callID, "invalid name"));
					}
					it.remove();
				}
	            else if (pairs.getKey().contains("multiselect")){
	            	String[] split = pairs.getValue().toString().split(",");
	            	JSONArray json = new JSONArray();
	            	for (int i = 0; i < split.length; i++) {
						json.add(split[i]);
					}
	            	pairs.setValue(json);
	            }	
	        }

			log.info("Getting user.");		
			User user = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			
			log.info("Retrieving OAuth2Token.");
			String token = getOAuthToken(user);
					
			log.info("Creating a task.");
			JSONObject taskBody = new JSONObject(parameterValues);	

			if (!jsonArray.toJSONString().equals("[]")) {
				taskBody.put("related_to_c", jsonArray);
			}

			TaskRestAPI taskRestAPI = new TaskRestAPI();
			String jsonString = taskRestAPI.createTask(getRequestUrl(null, null), token, taskBody, expectedResponseCode);
					
			log.info("Checking response");
			if (!expectedResponseCode.equals("200")) {
				//If expected fail do not parse response, response code already checked 
				log.info("Task creation failed as expected");
				log.info(jsonString);
			} else {
				
				try {
					JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
					log.info(postResponse.toString());
			        Iterator<Map.Entry<String, Object>> postit = parameterValues.entrySet().iterator();
			        while (postit.hasNext()) 
			        {
			        	Map.Entry<String, Object> pairs = postit.next();
			        	if (pairs.getKey().equals("id")) 
			        	{
			        		taskArray.add(pairs.getValue().toString());	
						}	        				            
			            //Assert.assertEquals((String) postResponse.get(pairs.getKey().toString().toLowerCase()), pairs.getValue(), pairs.getKey() +" was not returned as expected.  ");
			        }
				} catch (Exception e) {
					e.printStackTrace();
					log.info(jsonString);
					Assert.assertTrue(false, "Task was not created as expected.");
				}
				
				log.info("Task successfully created.");
				
			}
			
			log.info("End test method Test_createTask.");
	    }
	    
	    //@Test
	    public void cleanup(){	
	    	TaskRestAPI taskRest = new TaskRestAPI();
			log.info("Retrieving OAuth2Token.");		
			String token = getOAuthToken(user1);
			
			log.info("Deleting tasks");
	    	while (taskArray.iterator().hasNext()) 
	    	{
				for (String s : taskArray) 
				{
					taskRest.deleteTask(testConfig.getBrowserURL(), token, s);
				}			
			}
	    	
	    }
	    
	    //@Test //Need to get read only user
	    public void TestTask_ByUnauthorizedUser(){
	    	log.info("Starting TestTask_ByUnauthorizedUser");
			log.info("Retrieving OAuth2Token.");		
			LoginRestAPI loginRestAPI = new LoginRestAPI();
			loginRestAPI.getOAuth2TokenViaAPIManager(getApiManagement(), "john@smith.com", "password", "401");	
	    	log.info("Ending TestTask_ByUnauthorizedUser");
	    }
	    
	    @Test
	    public void TestTask_MisformedPayload(){
	    	log.info("Starting TestTask_MisformedPayload");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			
			log.info("Retrieving OAuth2Token.");	
			String headers[]= getOAuthTokenInHeader(user1);
		
			HttpUtils restCalls = new HttpUtils();
							
			String postResponseString = restCalls.postRequest(getRequestUrl(null, null), 
					headers, getValidTaskBody().toString() + "}", "application/json", "500");
			 
			log.info("Response String "+ postResponseString);
			try {
				//check for a valid JSON response
				new JSONParser().parse(postResponseString);
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
				
			log.info("Ending TestTask_MisformedPayload");
	    }
	    
	 @Test
	    public void TestTask_WrongURI(){
	    	log.info("Starting TestTask_MisformedPayload");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			
			log.info("Retrieving OAuth2Token.");		
			String headers[]= getOAuthTokenInHeader(user1);
		
			JSONObject body = new JSONObject(getValidTaskBody());
			HttpUtils restCalls = new HttpUtils();
			String postResponseString = restCalls.postRequest(getRequestUrl(null, null),  
					headers,body.toString() , "application/json", "200");
			 
			log.info("Response String "+ postResponseString);
			try {
				//check for a valid JSON response
				new JSONParser().parse(postResponseString);
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
				
			log.info("Ending TestTask_MisformedPayload");
	    }
	    
	  @Test
	    public void TestTask_Reqd_AdditionalValidNameValuePair_NoOptional(){
	    	log.info("Starting TestTask_Reqd_AdditionalValidNameValuePair_NoOptional");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			
			log.info("Retrieving OAuth2Token.");	
			String headers[]= getOAuthTokenInHeader(user1);	
		
			JSONObject json = new JSONObject(getValidTaskBody());
			String body = json.toString().replace("}", ",\"name\":\"Second Name\"}");

			HttpUtils restCalls = new HttpUtils();
			String postResponseString = restCalls.postRequest(getRequestUrl(null, null),
					headers, body, "application/json", "200");
			 
			log.info("Response String "+ postResponseString);
			try {
				//check for a valid JSON response
				new JSONParser().parse(postResponseString);
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
				
			log.info("Ending TestTask_Reqd_AdditionalValidNameValuePair_NoOptional");
	    }
	    
	  @Test
	    public void TestTask_Reqd_ValidNameValuePair_Optional_AdditionalValidNameValuePair(){
	    	log.info("Starting TestTask_Reqd_ValidNameValuePair_Optional_AdditionalValidNameValuePair");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			
			log.info("Retrieving OAuth2Token.");	
			String headers[]= getOAuthTokenInHeader(user1);	
				
			JSONObject json = new JSONObject(getValidTaskBody());
			json.put("description", "First Description");
			String body = json.toString().replace("}", ",\"description\":\"Second Description\"}");
			
			HttpUtils restCalls = new HttpUtils();
			String postResponseString = restCalls.postRequest(getRequestUrl(null, null),
					headers, body, "application/json", "200");
			 
			log.info("Response String "+ postResponseString);
			try {
				//check for a valid JSON response
				new JSONParser().parse(postResponseString);
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
				
			log.info("Ending TestTask_Reqd_ValidNameValuePair_Optional_AdditionalValidNameValuePair");
	    }
	    
	  @Test
	    public void TestTask_RelatedToInvalidOption_ExistingAccount(){
	    	log.info("Starting TestTask_RelatedToInvalidOption_ExistingAccount");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			
			log.info("Retrieving OAuth2Token.");
			String headers[]= getOAuthTokenInHeader(user1);			
		
			JSONObject json = new JSONObject(getValidTaskBody());
			json.put("related_to_c", populateRelated("Invalid", clientBeanID, clientName));;
			
			HttpUtils restCalls = new HttpUtils();
			String postResponseString = restCalls.postRequest(getRequestUrl(null, null),
					headers, json.toString(), "application/json", "422");
			 
			log.info("Response String "+ postResponseString);
			try {
				//check for a valid JSON response
				new JSONParser().parse(postResponseString);
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
				
			log.info("Ending TestTask_RelatedToInvalidOption_ExistingAccount");
	    }
	  
	  @Test
	    public void TestTask_RelatedToInvalidOption_ExistingOppty(){
	    	log.info("Starting TestTask_RelatedToInvalidOption_ExistingOppty");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			
			log.info("Retrieving OAuth2Token.");	
			String headers[]= getOAuthTokenInHeader(user1);			
					
			JSONObject json = new JSONObject(getValidTaskBody());
			json.put("related_to_c", populateRelated("Invalid", opptyID, opptyID));;
			
			HttpUtils restCalls = new HttpUtils();
			String postResponseString = restCalls.postRequest(getRequestUrl(null, null),
					headers, json.toString(), "application/json", "422");
			 
			log.info("Response String "+ postResponseString);
			try {
				//check for a valid JSON response
				new JSONParser().parse(postResponseString);
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
				
			log.info("Ending TestTask_RelatedToInvalidOption_ExistingOppty");
	    }
	  
	  @Test
	    public void TestTask_RelatedToInvalidOption_ExistingTask(){
	    	log.info("Starting TestTask_RelatedToInvalidOption_ExistingTask");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			
			log.info("Retrieving OAuth2Token.");		
			String headers[]= getOAuthTokenInHeader(user1);			
				
			JSONObject json = new JSONObject(getValidTaskBody());
			json.put("related_to_c", populateRelated("Invalid", taskID, taskName));;
			
			HttpUtils restCalls = new HttpUtils();
			String postResponseString = restCalls.postRequest(getRequestUrl(null, null),
					headers, json.toString(), "application/json", "422");
			 
			log.info("Response String "+ postResponseString);
			try {
				//check for a valid JSON response
				new JSONParser().parse(postResponseString);
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
				
			log.info("Ending TestTask_RelatedToInvalidOption_ExistingTask");
	    }
	  
	  @Test
	    public void TestTask_RelatedToInvalidOption_ExistingContact(){
	    	log.info("Starting TestTask_RelatedToInvalidOption_ExistingContact");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			
			log.info("Retrieving OAuth2Token.");	
			String headers[]= getOAuthTokenInHeader(user1);				
		
			JSONObject json = new JSONObject(getValidTaskBody());
			json.put("related_to_c", populateRelated("Invalid", contactBeanID, contactName));;
			
			HttpUtils restCalls = new HttpUtils();
			String postResponseString = restCalls.postRequest(getRequestUrl(null, null), 
					headers, json.toString(), "application/json", "422");
			 
			log.info("Response String "+ postResponseString);
			try {
				//check for a valid JSON response
				new JSONParser().parse(postResponseString);
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
				
			log.info("Ending TestTask_RelatedToInvalidOption_ExistingContact");
	    }
	  
	  @Test
	    public void TestTask_RelatedToInvalidOption_ExistingNote(){
	    	log.info("Starting TestTask_RelatedToInvalidOption_ExistingNote");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			
			log.info("Retrieving OAuth2Token.");	
			String headers[]= getOAuthTokenInHeader(user1);		
							
			JSONObject json = new JSONObject(getValidTaskBody());
			json.put("related_to_c", populateRelated("Invalid", noteID, noteSubject));;
			
			HttpUtils restCalls = new HttpUtils();
			String postResponseString = restCalls.postRequest(getRequestUrl(null, null), 
					headers, json.toString(), "application/json", "422");
			 
			log.info("Response String "+ postResponseString);
			try {
				//check for a valid JSON response
				new JSONParser().parse(postResponseString);
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
				
			log.info("Ending TestTask_RelatedToInvalidOption_ExistingNote");
	    }
	  
		@Test
		public void Test_taskWrongClientID(){
		
		if (APIm == true) {
			log.info("Start test Test_taskWrongClientID");
			log.info("Getting user.");
			User user = commonUserAllocator.getUser(this);

			log.info("Retrieving OAuth2Token.");
			String headers[] = getOAuthTokenInHeader(user1);

			JSONObject body = new JSONObject(getValidTaskBody());
			HttpUtils restCalls = new HttpUtils();
			String postResponseString = restCalls.postRequest(
					getApiManagement() + getApiExtension() + "?client_id=wrong"
							+ "&clientSecret=" + getclient_secret(), headers,
					body.toString(), "application/json", "401");

			log.info("End test Test_taskWrongClientID");
		}
	}
		
		@Test
		public void Test_taskMissingClientID(){
		
		if (APIm == true) {
			log.info("Start test Test_taskMissingClientID");
			log.info("Getting user.");
			User user = commonUserAllocator.getUser(this);

			log.info("Retrieving OAuth2Token.");
			String headers[] = getOAuthTokenInHeader(user1);

			JSONObject body = new JSONObject(getValidTaskBody());
			HttpUtils restCalls = new HttpUtils();
			String postResponseString = restCalls.postRequest(
					getApiManagement() + getApiExtension() + "?clientSecret="
							+ getclient_secret(), headers, body.toString(),
					"application/json", "401");

			log.info("End test Test_taskMissingClientID");
		}
	}
		
	@Test
	public void Test_taskWrongClientSecret() {

		if (APIm == true) {
			log.info("Start test Test_taskWrongClientSecret");
			log.info("Getting user.");
			User user = commonUserAllocator.getUser(this);

			log.info("Retrieving OAuth2Token.");
			String headers[] = getOAuthTokenInHeader(user1);

			JSONObject body = new JSONObject(getValidTaskBody());
			HttpUtils restCalls = new HttpUtils();
			String postResponseString = restCalls.postRequest(
					getApiManagement() + getApiExtension() + "?client_id="
							+ getclient_ID() + "&clientSecret=wrong", headers,
					body.toString(), "application/json", "401");

			log.info("End test Test_taskWrongClientSecret");
		}

	}
			
	@Test
	public void Test_taskMissingClientSecret() {
		if (APIm == true) {
		
			log.info("Start test Test_taskMissingClientSecret");
			log.info("Getting user.");
			User user = commonUserAllocator.getUser(this);
			log.info("Retrieving OAuth2Token.");
			String headers[] = getOAuthTokenInHeader(user1);

			JSONObject body = new JSONObject(getValidTaskBody());
			HttpUtils restCalls = new HttpUtils();
			String postResponseString = restCalls.postRequest(
					getApiManagement() + getApiExtension() + "?client_id="
							+ accountID, headers, body.toString(),
					"application/json", "401");

			log.info("End test Test_taskMissingClientSecret");
		}
	}

		  @Test
		    public void TestTask_RelateDACHContactNonDACHUser(){
		    	log.info("Starting TestTask_RelateDACHContactNonDACHUser");
				User user1 = commonUserAllocator.getUser(this);

				log.info("Creating contact");
				String headers[] = {"OAuth-Token", new LoginRestAPI().getOAuth2Token(testConfig.getBrowserURL(), "de01@tst.ibm.com", user1.getPassword())};
				String assignedUserID = new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(), "de01@tst.ibm.com", 
						user1.getPassword());	
				String contactBeanID = new ContactRestAPI().createContactreturnBean(testConfig.getBrowserURL(), headers, 
						"ContactFirst", "ContactLast", "DE", "(555) 555-5555", assignedUserID);
				

				log.info("Creating a task.");
				JSONObject taskBody = new JSONObject(getValidTaskBody());	
				JSONArray jsonArray = new JSONArray();
				jsonArray.add(populateRelated("Contacts", contactBeanID, contactName));
				if (!jsonArray.toJSONString().equals("[]")) {
					taskBody.put("related_to_c", jsonArray);
				}
				String token = getOAuthToken(user1);
				TaskRestAPI taskRestAPI = new TaskRestAPI();
				String jsonString = taskRestAPI.createTask(getRequestUrl(null, null), 
						token, 
						taskBody,"200");
			

				log.info("Ending TestTask_RelateDACHContactNonDACHUser");
		    }
		  
		  @Test
		    public void TestTask_RelateDACHContactDACHUser(){
		    	log.info("Starting TestTask_RelateDACHContactDACHUser");
				User user1 = commonUserAllocator.getUser(this);

				log.info("Creating contact");
				String headers[] = {"OAuth-Token", new LoginRestAPI().getOAuth2Token(testConfig.getBrowserURL(), "de01@tst.ibm.com", user1.getPassword())};
				String assignedUserID = new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(), "de01@tst.ibm.com", 
						user1.getPassword());	
				String contactBeanID = new ContactRestAPI().createContactreturnBean(testConfig.getBrowserURL(), headers, 
						"ContactFirst", "ContactLast", "DE", "(555) 555-5555", assignedUserID);
				
				log.info("Creating a task.");
				JSONObject taskBody = new JSONObject(getValidTaskBody());	
				JSONArray jsonArray = new JSONArray();
				jsonArray.add(populateRelated("Contacts", contactBeanID, contactName));
				if (!jsonArray.toJSONString().equals("[]")) {
					taskBody.put("related_to_c", jsonArray);
				}
				
				String token = getOAuthToken(user1);
				TaskRestAPI taskRestAPI = new TaskRestAPI();
				String jsonString = taskRestAPI.createTask(getRequestUrl(null, null), 
						token, 
						taskBody,"200");

				log.info("Ending TestTask_RelateDACHContactDACHUser");
		    }
	  
	    private HashMap<String,String> populateRelated(String type, String ID, String name){
    		HashMap<String,String> map = new HashMap<String, String>();
    		map.put("related_id", ID);
    		map.put("related_type", type);
    		map.put("related_name", name);
    		return map;
	    }
	    
	    private HashMap<String,String> getValidTaskBody(){
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("name", "Task Name");
			map.put("date_due", "2013-10-28T15:14:00.000Z");
			map.put("priority", "High");
			map.put("status", "Not Started");
			//map.put("call_type", "Close_out_call");

	    	return map;
	    }
	
}
