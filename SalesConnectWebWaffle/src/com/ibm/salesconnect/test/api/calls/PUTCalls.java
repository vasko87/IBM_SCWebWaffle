package com.ibm.salesconnect.test.api.calls;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
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
import com.ibm.salesconnect.API.LeadsRestAPI;
import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.API.NoteRestAPI;
import com.ibm.salesconnect.API.OpportunityRestAPI;
import com.ibm.salesconnect.API.TestDataHolder;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.test.mobile.TaskRestAPITests;

/**
 * @author Eva Farrell		
 * @date Oct 8, 2015
 */

public class PUTCalls extends ApiBaseTest
{
	
	private static final Logger log = LoggerFactory.getLogger(PUTCalls.class);
	@Parameters({ "apiExtension", "applicationName", "APIM", "apim_environment" })
	private PUTCalls(@Optional("calls") String apiExtension,
			@Optional("SC Auto update") String applicationName,
			@Optional("true") String APIM,
			@Optional("development") String environment) {

		super(apiExtension, applicationName, APIM, environment);
	}
	protected String testCaseName = "";


	
	private TestDataHolder testData;
    int rand = new Random().nextInt(100000);
    //private HashMap<String, Object> yamlReturnedMap=null;

	private String noteID = null;
	private String noteID1 = null;
	private String taskID = null;
	private String taskID1 = null;
	private String noteSubject = "put task note subject";
	private String leadID = null;
	private String leadID1 = null;
	private String callSubject = "put task call subject";
	private String contactID = null;
	private String contactID1 = null;
	private String contactID2 = null;
	private String contactID3 = null;
	private String opptyID = null;
	private String opptyID1 = null;
	private String assignedUserID = null;
	private String assignedUserID1 = null;
	private String assignedUserID2 = null;
	private String assignedUserCNUM = null;
	private String assignedUserCNUM1 = null;
	private String assignedUserCNUM2 = null;
	private String userID = null;
	private String accountName = null;
	private String accountID = null;
	private String accountID1 = null;
	private String accountBeanID = null;
	private String accountBeanID1 = null;
	private String callID = null;
	private String expectedResponseCode = "200";
	private String urlCallID = null;
	private String urlExtension = "";
	private String relatedObject = "";
	private String externalExtension = null;
	private String url = "";
	private String jsonString = "";
	private User user = null;
	private User user1 = null;
	private String token = null;
	private String unLinkedTaskID;
	private String unLinkedLeadID;
	private String unLinkedOpptyID;
	private String unLinkedNoteID;
	private String unLinkedContactID;
	private String callID1;
	private String callID2;
	private String callID3;
	private String assignedUserName1;
	private CharSequence campaignID;

	private void createObjects()
	{
		log.info("Start creating prerequisites");
		User user = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
		User user2 = commonUserAllocator.getUser(this);
		String baseURL = testConfig.getBrowserURL();
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		token = loginRestAPI.getOAuth2Token(baseURL, user.getEmail(), user.getPassword());

		assignedUserID = new APIUtilities().getUserBeanIDFromEmail(baseURL, user.getEmail(), user.getPassword());	
		assignedUserID1 = new APIUtilities().getUserBeanIDFromEmail(baseURL, user1.getEmail(), user1.getPassword());	
		assignedUserID2 = new APIUtilities().getUserBeanIDFromEmail(baseURL, user2.getEmail(), user2.getPassword());	
		
		assignedUserCNUM = user.getUid();
		assignedUserCNUM1 = user1.getUid();
		assignedUserCNUM2 = user2.getUid();
		
		assignedUserName1 = user1.getDisplayName();
		campaignID = "56dacb47-bcd1-4929-0304-503883f104ee";
		
		log.info("Getting clients");
		String headers[] = {"OAuth-Token", token};
		PoolClient poolClient= commonClientAllocator.getGroupClient(GC.SC,this);
		PoolClient poolClient1= commonClientAllocator.getGroupClient(GC.SC,this);
		accountID = poolClient.getCCMS_ID();
		accountName = poolClient.getClientName(baseURL, user1);
		accountBeanID = new CollabWebAPI().getbeanIDfromClientID(baseURL, accountID, headers );
		
		log.info("Creating contacts");
		ContactRestAPI contactAPI = new ContactRestAPI();
		contactID = contactAPI.createContactreturnBean(baseURL, headers, "ContactFirst", "ContactLast", "US", "(555) 555-5555", assignedUserID, accountBeanID);
		contactID1 = contactAPI.createContactreturnBean(baseURL, headers, "ContactFirst", "ContactLast", "US", "(555) 555-5555", assignedUserID, accountBeanID);
		contactID2 = contactAPI.createContactreturnBean(baseURL, headers, "ContactFirst", "ContactLast", "US", "(555) 555-5555", assignedUserID, accountBeanID);
		unLinkedContactID = contactAPI.createContactreturnBean(baseURL, headers, "ContactFirst", "ContactLast", "US", "(555) 555-5555", assignedUserID, accountBeanID);
				
		log.info("Creating notes");
		NoteRestAPI noteAPI = new NoteRestAPI();
		noteID = noteAPI.createNotereturnBean(testConfig.getBrowserURL(), token, noteSubject, "put task note description", assignedUserID);
		noteID1 = noteAPI.createNotereturnBean(testConfig.getBrowserURL(), token, noteSubject, "put task note description", assignedUserID);
		unLinkedNoteID = noteAPI.createNotereturnBean(testConfig.getBrowserURL(), token, noteSubject, "put task note description", assignedUserID);
		
		log.info("Creating Opportunity");
   		OpportunityRestAPI opportunityRestAPI = new OpportunityRestAPI();
   		opptyID = opportunityRestAPI.createOpportunityreturnBean(testConfig.getBrowserURL(), token, "API created Oppty to link to another Oppty", accountBeanID, contactID1, "SLSP", "03", "2016-10-28", assignedUserID);
   		opptyID1 = opportunityRestAPI.createOpportunityreturnBean(testConfig.getBrowserURL(), token, "API created Oppty to link to another Oppty", accountBeanID, contactID1, "SLSP", "03", "2016-10-28", assignedUserID);
   		unLinkedOpptyID = opportunityRestAPI.createOpportunityreturnBean(testConfig.getBrowserURL(), token, "API created Oppty to link to another Oppty", accountBeanID, contactID1, "SLSP", "03", "2016-10-28", assignedUserID);
   				
		log.info("Creating lead");
		LeadsRestAPI leadRestAPI = new LeadsRestAPI();
		leadID = leadRestAPI.createLeadreturnBean(testConfig.getBrowserURL(), token, "lead Name", "lead Description", assignedUserID);
		leadID1 = leadRestAPI.createLeadreturnBean(testConfig.getBrowserURL(), token, "lead Name", "lead Description", assignedUserID);
		unLinkedLeadID = leadRestAPI.createLeadreturnBean(testConfig.getBrowserURL(), token, "lead Name", "lead Description", assignedUserID);

		log.info("Creating tasks");
		taskID = TaskRestAPITests.createTaskHelper(user,"Base task",log,baseURL);
		taskID1 = TaskRestAPITests.createTaskHelper(user,"Base task",log,baseURL);
		unLinkedTaskID = TaskRestAPITests.createTaskHelper(user,"Base task",log,baseURL);
				
		log.info("Creating call");
		CallRestAPI callRestAPI = new CallRestAPI();
		callID = callRestAPI.createCallreturnBean(testConfig.getBrowserURL(), token, callSubject, "2016-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);
		callID1 = callRestAPI.createCallreturnBean(testConfig.getBrowserURL(), token, callSubject, "2016-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);
		callID2 = callRestAPI.createCallreturnBean(testConfig.getBrowserURL(), token, callSubject, "2016-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);
		callID3 = callRestAPI.createCallreturnBean(testConfig.getBrowserURL(), token, callSubject, "2016-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);
		
		Assert.assertTrue(callRestAPI.linkRecordToCallReturnBoolean(testConfig.getBrowserURL(), token, callID, contactID, "Contacts"));
		Assert.assertTrue(callRestAPI.linkRecordToCallReturnBoolean(testConfig.getBrowserURL(), token, callID, noteID, "Notes"));
		Assert.assertTrue(callRestAPI.linkRecordToCallReturnBoolean(testConfig.getBrowserURL(), token, callID, taskID, "Tasks"));
		Assert.assertTrue(callRestAPI.linkRecordToCallReturnBoolean(testConfig.getBrowserURL(), token, callID, opptyID, "Opportunities"));
		Assert.assertTrue(callRestAPI.linkRecordToCallReturnBoolean(testConfig.getBrowserURL(), token, callID,leadID, "Leads"));
		Assert.assertTrue(callRestAPI.linkRecordToCallReturnBoolean(testConfig.getBrowserURL(), token, callID,assignedUserID1, "AdditionalAssignees"));
		Assert.assertTrue(callRestAPI.linkRecordToCallReturnBoolean(testConfig.getBrowserURL(), token, callID,assignedUserID1, "AssignedUser"));
		Assert.assertTrue(callRestAPI.linkRecordToCallReturnBoolean(testConfig.getBrowserURL(), token, callID,assignedUserID1, "Users"));
		
	}

    public void addDataFile(String filePath){
        if(this.testData== null){
            this.testData = new TestDataHolder();
        }
         this.testData.addDataLocation(filePath, ";");
     }

	
	@DataProvider(name="jsonProvider")
	    public Object[][] getTestData(){
		this.createObjects();
		this.addDataFile("test_config/extensions/api/call/putCalls.csv");
		return testData.getAllDataRows();
    }
	
	@Test(dataProvider = "jsonProvider")
	public void TestUpdateCall(HashMap<String,Object> parameterValues){
		Iterator<Map.Entry<String, Object>> it = parameterValues.entrySet().iterator();
        log.info("Getting user.");		
        User user1 = commonUserAllocator.getUser(this);
        User user2 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
		String body = "";
		String expectedResponse = "";
		
        while (it.hasNext()) 
        {
          Map.Entry<String, Object> pairs = (Map.Entry<String, Object>)it.next();
          System.out.println(pairs.getKey());
          System.out.println(pairs.getValue());
          if (pairs.getKey().equalsIgnoreCase("body")) {
        	  body=(String) pairs.getValue();	
        	  body = body.replace("*assignedUserID*", assignedUserID);
        	  body = body.replace("*assignedUserID1*", assignedUserID1);
        	  body = body.replace("*callID1*", callID1);
        	  body = body.replace("*callID2*", callID2);
        	  body = body.replace("*callID3*", callID3);
        	  body = body.replace("*assignedUserName1*", assignedUserName1);
          }
          else if (pairs.getKey().equalsIgnoreCase("expected_response")) {
        	  expectedResponse=(String) pairs.getValue();	
          }
          else if (pairs.getKey().equalsIgnoreCase("URL")) {
			urlExtension = (String) pairs.getValue();
			urlExtension = urlExtension.replace("*accountID*", accountBeanID);
			urlExtension = urlExtension.replace("*callID*", callID);
			urlExtension = urlExtension.replace("*leadID*", leadID);
			urlExtension = urlExtension.replace("*leadID1*", leadID1);
			urlExtension = urlExtension.replace("*unLinkedLeadID*", unLinkedLeadID);
			urlExtension = urlExtension.replace("*opptyID*", opptyID);
			urlExtension = urlExtension.replace("*opptyID1*", opptyID1);
			urlExtension = urlExtension.replace("*unLinkedOpptyID*", unLinkedOpptyID);
     	  	urlExtension = urlExtension.replace("*taskID*", taskID);
			urlExtension = urlExtension.replace("*taskID1*", taskID1);
			urlExtension = urlExtension.replace("*unLinkedTaskID*", unLinkedTaskID);
    	  	urlExtension = urlExtension.replace("*noteID*", noteID);
      	  	urlExtension = urlExtension.replace("*noteID1*", noteID1);
      	  	urlExtension = urlExtension.replace("*unLinkedNoteID*", unLinkedNoteID);
      	  	urlExtension = urlExtension.replace("*contactID*", contactID);
			urlExtension = urlExtension.replace("*contactID1*", contactID1);
			urlExtension = urlExtension.replace("*contactID2*", contactID2);
			urlExtension = urlExtension.replace("*unLinkedContactID*", unLinkedContactID);
			urlExtension = urlExtension.replace("*assignedUserID1*", assignedUserID1);
			urlExtension = urlExtension.replace("*assignedUserID2*", assignedUserID2);
			urlExtension = urlExtension.replace("*assignedUserCNUM2*", assignedUserCNUM2);
			urlExtension = urlExtension.replace("*assignedUserCNUM1*", assignedUserCNUM1);
			urlExtension = urlExtension.replace("*campaignID*", campaignID);
          }
          else if (pairs.getKey().equalsIgnoreCase("user")) {
        	if (pairs.getValue().equals("user2")) {
        		user = user1;
			}
        	else {
        		user = user2;
			}	
          }
          else if (pairs.getKey().equals("TC_Name")) {
				log.info("This is test " + pairs.getValue().toString());
			}
          
        }
        
		log.info("Sending PUT request");
		CallRestAPI callrestAPI = new CallRestAPI();
		String putResponseString = callrestAPI.putCall(getRequestUrl(urlExtension, null), getOAuthToken(user), body, expectedResponse);
		//Verify response
				if (expectedResponse.equalsIgnoreCase("200")&&!(body.isEmpty()))
				{
					log.info("Verifying values in Put Request present in Put Response");
					try {
						JSONObject putRequest = (JSONObject)new JSONParser().parse(body);
						Iterator<Map.Entry<String, Object>> it1 = putRequest.entrySet().iterator();
						while (it1.hasNext()) 
				        {
							Map.Entry<String, Object> pairs = it1.next(); 
							String requestKey=pairs.getKey().toString();
							String requestValue=pairs.getValue().toString();
							
							if(requestKey.contains("date_start")||requestKey.contains("related_to_c")||requestKey.contains("date_due")||requestKey.contains("massupdate_params")) {
								//ignore
		        			}
							else if(requestKey.contains("record")) {
								log.info("Verifying PUT Response contains "+requestValue);
								Assert.assertTrue(putResponseString.contains(requestValue)||putResponseString.contains(requestKey)||putResponseString.contains("id")||putResponseString.contains("related_records"),requestValue+" not found in Link Put Response as expected. Put Response: "+putResponseString);
		        			}
							else if (requestKey.contains("link_name")) {
								log.info("Verifying Link PUT Response contains "+requestValue);
								Assert.assertTrue(putResponseString.contains(requestValue)||putResponseString.contains(requestKey)||putResponseString.contains("id")||putResponseString.contains("related_records"),requestValue+" not found in Link Put Response as expected. Put Response: "+putResponseString);
		        			}
							
							else{
								log.info("Verifying PUT Response contains "+requestKey+" and " +requestValue);
								Assert.assertTrue(putResponseString.contains(requestKey)&&putResponseString.contains(requestValue), requestKey+" and "+requestValue+" not found in Put Response as expected. Put Response: "+putResponseString);
							}
				        }
				        	

					} catch (Exception e) {
						e.printStackTrace();
						log.info(body);
						Assert.assertTrue(false, "Call was not updated as expected.");
					}
					
					log.info("Call successfully updated.");	    
				}
				
	}

	

	/**
     * Get the oauth string
     */
	
//	public String getOAuthExtension(){		
//		return "oauth?" + clientIDandSecret;
//	}
	   
}