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
import com.ibm.salesconnect.API.SugarAPI;
import com.ibm.salesconnect.API.TestDataHolder;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.test.mobile.TaskRestAPITests;

/**
 * @author Eva Farrell		
 * @date Oct 2, 2015
 */

public class POSTCalls extends ApiBaseTest
{
	
	private static final Logger log = LoggerFactory.getLogger(POSTCalls.class);
	@Parameters({ "apiExtension", "applicationName", "APIM", "apim_environment" })
	private POSTCalls(@Optional("calls") String apiExtension,
			@Optional("SC Auto create") String applicationName,
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
	private String noteID2 = null;
	private String noteID3 = null;
	private String taskID = null;
	private String taskID1 = null;
	private String taskID2 = null;
	private String taskID3 = null;
	private String noteSubject = "post task note subject";
	private String leadID = null;
	private String leadID1 = null;
	private String leadID2 = null;
	private String leadID3 = null;
	private String callSubject = "post task call subject";
	private String contactID = null;
	private String contactID1 = null;
	private String contactID2 = null;
	private String contactID3 = null;
	private String contactID4 = null;
	private String opptyID = null;
	private String opptyID1 = null;
	private String opptyID2 = null;
	private String opptyID3 = null;

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
	private String accountID2 = null;
	private String accountID3 = null;
	private String accountBeanID = null;
	private String accountBeanID1 = null;
	private String accountBeanID2 = null;
	private String accountBeanID3 = null;
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
	private String campaignID;
	

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
		
		campaignID = "56dacb47-bcd1-4929-0304-503883f104ee";
		
		log.info("Getting clients");
		String headers[] = {"OAuth-Token", token};
		PoolClient poolClient= commonClientAllocator.getGroupClient(GC.SC,this);
		PoolClient poolClient1= commonClientAllocator.getGroupClient(GC.SC,this);
		PoolClient poolClient2= commonClientAllocator.getGroupClient(GC.SC,this);
		PoolClient poolClient3= commonClientAllocator.getGroupClient(GC.SC,this);
		
		accountID = poolClient.getCCMS_ID();
		accountName = poolClient.getClientName(baseURL, user1);
		accountBeanID = new CollabWebAPI().getbeanIDfromClientID(baseURL, accountID, headers );
		accountID1 = poolClient1.getCCMS_ID();
		accountBeanID1 = new CollabWebAPI().getbeanIDfromClientID(baseURL, accountID1, headers );
		accountID2 = poolClient2.getCCMS_ID();
		accountBeanID2 = new CollabWebAPI().getbeanIDfromClientID(baseURL, accountID2, headers );
		accountID3 = poolClient3.getCCMS_ID();
		accountBeanID3 = new CollabWebAPI().getbeanIDfromClientID(baseURL, accountID3, headers );
		
		
		log.info("Creating contacts");
		contactID4 = "contact-" + rand;
		SugarAPI sugarAPI = new SugarAPI();
		sugarAPI.createContact(testConfig.getBrowserURL(), contactID4, accountBeanID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");
		ContactRestAPI contactAPI = new ContactRestAPI();
		contactID = contactAPI.createContactreturnBean(baseURL, headers, "ContactFirst", "ContactLast", "US", "(555) 555-5555", assignedUserID, accountBeanID);
		contactID1 = contactAPI.createContactreturnBean(baseURL, headers, "ContactFirst", "ContactLast", "US", "(555) 555-5555", assignedUserID, accountBeanID);
		contactID2 = contactAPI.createContactreturnBean(baseURL, headers, "ContactFirst", "ContactLast", "US", "(555) 555-5555", assignedUserID, accountBeanID);
		contactID3 = contactAPI.createContactreturnBean(baseURL, headers, "ContactFirst", "ContactLast", "US", "(555) 555-5555", assignedUserID, accountBeanID);
		
		log.info("Creating Opportunity");
   		OpportunityRestAPI opportunityRestAPI = new OpportunityRestAPI();
   		opptyID = opportunityRestAPI.createOpportunityreturnBean(testConfig.getBrowserURL(), token, "API created Oppty to link to another Oppty", accountBeanID, contactID1, "SLSP", "03", "2016-10-28", assignedUserID);
   		opptyID1 = opportunityRestAPI.createOpportunityreturnBean(testConfig.getBrowserURL(), token, "API created Oppty to link to another Oppty", accountBeanID, contactID1, "SLSP", "03", "2016-10-28", assignedUserID);
   		opptyID2 = opportunityRestAPI.createOpportunityreturnBean(testConfig.getBrowserURL(), token, "API created Oppty to link to another Oppty", accountBeanID, contactID1, "SLSP", "03", "2016-10-28", assignedUserID);
   		opptyID3 = opportunityRestAPI.createOpportunityreturnBean(testConfig.getBrowserURL(), token, "API created Oppty to link to another Oppty", accountBeanID, contactID1, "SLSP", "03", "2016-10-28", assignedUserID);

		log.info("Creating note");
		NoteRestAPI noteAPI = new NoteRestAPI();
		noteID = noteAPI.createNotereturnBean(testConfig.getBrowserURL(), token, noteSubject, "post task note description", assignedUserID);
		noteID1 = noteAPI.createNotereturnBean(testConfig.getBrowserURL(), token, noteSubject, "post task note description", assignedUserID);
		noteID2 = noteAPI.createNotereturnBean(testConfig.getBrowserURL(), token, noteSubject, "post task note description", assignedUserID);
		noteID3 = noteAPI.createNotereturnBean(testConfig.getBrowserURL(), token, noteSubject, "post task note description", assignedUserID);
		
		log.info("Creating lead");
		LeadsRestAPI leadRestAPI = new LeadsRestAPI();
		leadID = leadRestAPI.createLeadreturnBean(testConfig.getBrowserURL(), token, "lead Name", "lead Description", assignedUserID);
		leadID1 = leadRestAPI.createLeadreturnBean(testConfig.getBrowserURL(), token, "lead Name", "lead Description", assignedUserID);
		leadID2 = leadRestAPI.createLeadreturnBean(testConfig.getBrowserURL(), token, "lead Name", "lead Description", assignedUserID);
		leadID3 = leadRestAPI.createLeadreturnBean(testConfig.getBrowserURL(), token, "lead Name", "lead Description", assignedUserID);

		log.info("Creating task");
		taskID = TaskRestAPITests.createTaskHelper(user1,"Base task",log,baseURL);
		taskID1 = TaskRestAPITests.createTaskHelper(user1,"Base task",log,baseURL);
		taskID2 = TaskRestAPITests.createTaskHelper(user1,"Base task",log,baseURL);
		taskID3 = TaskRestAPITests.createTaskHelper(user1,"Base task",log,baseURL);
				
		log.info("Creating call");
		CallRestAPI callRestAPI = new CallRestAPI();
		callID = callRestAPI.createCallreturnBean(testConfig.getBrowserURL(), token, callSubject, "2016-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);
		
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
		this.addDataFile("test_config/extensions/api/call/postCalls.csv");
		return testData.getAllDataRows();
    }
	
	@Test(dataProvider = "jsonProvider")
	public void TestCreateCall(HashMap<String,Object> parameterValues){
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
        	  body = body.replace("*leadID1*", leadID1);
        	  body = body.replace("*leadID2*", leadID2);
        	  body = body.replace("*leadID3*", leadID3);
        	  body = body.replace("*noteID1*", noteID1);
        	  body = body.replace("*noteID2*", noteID2);
        	  body = body.replace("*noteID3*", noteID3);
        	  body = body.replace("*taskID1*", taskID1);
        	  body = body.replace("*taskID2*", taskID2);
        	  body = body.replace("*taskID3*", taskID3);
        	  body = body.replace("*contactID*", contactID);
        	  body = body.replace("*contactID1*", contactID1);
        	  body = body.replace("*contactID2*", contactID2);
        	  body = body.replace("*contactID3*", contactID3);
        	  body = body.replace("*opptyID1*", opptyID1);
        	  body = body.replace("*opptyID2*", opptyID2);
        	  body = body.replace("*opptyID3*", opptyID3);
        	  body = body.replace("*accountName*", accountName);
        	  body = body.replace("*accountID*", accountBeanID);
        	  body = body.replace("*accountID1*", accountBeanID1);
        	  body = body.replace("*accountID2*", accountBeanID2);
        	  body = body.replace("*accountID3*", accountBeanID3);
        	  body = body.replace("*assignedUserID*", assignedUserID);
        	  body = body.replace("*assignedUserID1*", assignedUserID1);
          }
          else if (pairs.getKey().equalsIgnoreCase("expected_response")) {
        	  expectedResponse=(String) pairs.getValue();	
          }
          else if (pairs.getKey().equalsIgnoreCase("URL")) {
			urlExtension = (String) pairs.getValue();
			urlExtension = urlExtension.replace("*callID*", callID);
			urlExtension = urlExtension.replace("*leadID*", leadID);
			urlExtension = urlExtension.replace("*noteID*", noteID);
			urlExtension = urlExtension.replace("*taskID*", taskID);
			urlExtension = urlExtension.replace("*contactID*", contactID);
			urlExtension = urlExtension.replace("*accountID*", accountBeanID);
			urlExtension = urlExtension.replace("*opptyID*", opptyID);
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
        
		log.info("Sending POST request");
		CallRestAPI callrestAPI = new CallRestAPI();
		String postResponseString = callrestAPI.postCall(getRequestUrl(urlExtension, null), getOAuthToken(user), body, expectedResponse);
		//Verify response
				if (expectedResponse.equalsIgnoreCase("200")&&!(body.isEmpty()))
				{
					log.info("Verifying values in Post Request present in Post Response");
					try {
						JSONObject postRequest = (JSONObject)new JSONParser().parse(body);
						Iterator<Map.Entry<String, Object>> it1 = postRequest.entrySet().iterator();
						while (it1.hasNext()) 
				        {
							Map.Entry<String, Object> pairs = it1.next(); 
							String requestKey=pairs.getKey().toString();
							String requestValue=pairs.getValue().toString();
							
							if(requestKey.contains("date_start")||requestKey.contains("related_to_c")||requestKey.contains("date_due")) {
								//ignore
		        			}
							else if(requestKey.contains("ids")||requestKey.contains("record")) {
								log.info("Verifying Link POST Response contains "+requestValue);
								Assert.assertTrue(postResponseString.contains(requestValue)||postResponseString.contains(requestKey)||postResponseString.contains("ids")||postResponseString.contains("related_records"),requestValue+" not found in Link Post Response as expected. Post Response: "+postResponseString);
		        			}
							else if (requestKey.contains("link_name")) {
								log.info("Verifying Link POST Response contains "+requestValue);
								Assert.assertTrue(postResponseString.contains(requestValue)||postResponseString.contains(requestKey)||postResponseString.contains("ids")||postResponseString.contains("related_records"),requestValue+" not found in Link Post Response as expected. Post Response: "+postResponseString);
		        			}
							
							else{
								log.info("Verifying POST Response contains "+requestKey+" and " +requestValue);
								Assert.assertTrue(postResponseString.contains(requestKey)&&postResponseString.contains(requestValue), requestKey+" and "+requestValue+" not found in Post Response as expected. Post Response: "+postResponseString);
							}
				        }
				        	

					} catch (Exception e) {
						e.printStackTrace();
						log.info(body);
						Assert.assertTrue(false, "Call was not created as expected.");
					}
					
					log.info("Call successfully created.");	    
				}
				
	}

	

	/**
     * Get the oauth string
     */
	
//	public String getOAuthExtension(){		
//		return "oauth?" + clientIDandSecret;
//	}
	   
}