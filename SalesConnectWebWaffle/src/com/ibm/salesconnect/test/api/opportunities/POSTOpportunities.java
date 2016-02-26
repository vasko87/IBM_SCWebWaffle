package com.ibm.salesconnect.test.api.opportunities;


import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.ibm.salesconnect.API.MeetingRestAPI;
import com.ibm.salesconnect.API.NoteRestAPI;
import com.ibm.salesconnect.API.OpportunityRestAPI;
import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.API.TestDataHolder;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.objects.RevenueItem;
import com.ibm.salesconnect.test.mobile.TaskRestAPITests;

/**
 * @author Eva Farrell	
 * @date June 24, 2015
 */

public class POSTOpportunities extends ApiBaseTest
{
	
	private static final Logger log = LoggerFactory.getLogger(POSTOpportunities.class);
	@Parameters({ "apiExtension", "applicationName", "APIM", "apim_environment" })
	private POSTOpportunities(@Optional("opportunities") String apiExtension,
			@Optional("SC Auto create") String applicationName,
			@Optional("true") String APIM,
			@Optional("development") String environment) {

		super(apiExtension, applicationName, APIM, environment);
	}
	protected String testCaseName = "";

		private TestDataHolder testData;
    int rand = new Random().nextInt(100000);

    private String clientBeanID = null;
    private String clientID = null;
    private String clientBeanID1 = null;
    private String clientID1 = null;
    private String contactID = null;
    private String assignedUserID = null;
	private String assignedUserName = null;
	private String baseID = null;
	private String urlExtension = "";
	private String token = null;
	private String rliID = null;
	private String rliID1 = null;
	private String rliID2 = null;
	private String rliID3 = null;
	private String noteID = null;
	private String noteID1 = null;
	private String noteID2 = null;
	private String noteID3 = null;
	private String taskID = null;
	private String taskID1 = null;
	private String taskID2 = null;
	private String taskID3 = null;
	private String noteSubject = "post task note subject";
	private String callID = null;
	private String callID1 = null;
	private String callID2 = null;
	private String callID3 = null;
	private String callSubject = "post task call subject";
	private String meetingID = null;
	private String meetingID1 = null;
	private String meetingID2 = null;
	private String meetingID3 = null;
	private String contactID1 = null;
	private String contactID2 = null;
	private String contactID3 = null;
	private String contactID4 = null;
	private String opportunityID = null;
	private String opportunityID1 = null;
	private String opportunityID2 = null;
	private String opportunityID3 = null;
	private String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString();
	
	private void createObjects()
	{
		log.info("Start creating prerequisites");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
		String baseURL = testConfig.getBrowserURL();
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		token = loginRestAPI.getOAuth2Token(baseURL, user1.getEmail(), user1.getPassword());

		assignedUserID = new APIUtilities().getUserBeanIDFromEmail(baseURL, user1.getEmail(), user1.getPassword());	
		assignedUserName = user1.getDisplayName();
		
		PoolClient poolClient= commonClientAllocator.getGroupClient(GC.SC,this);
		PoolClient poolClient1= commonClientAllocator.getGroupClient(GC.SC,this);
		
		log.info("Getting clients");
		String headers[] = {"OAuth-Token", token};
		clientID = poolClient.getCCMS_ID();
		clientBeanID = new CollabWebAPI().getbeanIDfromClientID(baseURL, clientID, headers );
		clientID1 = poolClient1.getCCMS_ID();
		clientBeanID1 = new CollabWebAPI().getbeanIDfromClientID(baseURL, clientID1, headers );
		
		log.info("Creating contacts");
		ContactRestAPI contactAPI = new ContactRestAPI();
		contactID = contactAPI.createContactreturnBean(baseURL, headers, "ContactFirst", "ContactLast", "US", "(555) 555-5555", assignedUserID, clientBeanID);
		contactID1 = contactAPI.createContactreturnBean(baseURL, headers, "ContactFirst", "ContactLast", "US", "(555) 555-5555", assignedUserID, clientBeanID);
		contactID2 = contactAPI.createContactreturnBean(baseURL, headers, "ContactFirst", "ContactLast", "US", "(555) 555-5555", assignedUserID, clientBeanID);
		contactID3 = contactAPI.createContactreturnBean(baseURL, headers, "ContactFirst", "ContactLast", "US", "(555) 555-5555", assignedUserID, clientBeanID);
		contactID4 = contactAPI.createContactreturnBean(baseURL, headers, "ContactFirst", "ContactLast", "US", "(555) 555-5555", assignedUserID, clientBeanID);
				
		log.info("Creating notes");
		NoteRestAPI noteAPI = new NoteRestAPI();
		noteID = noteAPI.createNotereturnBean(testConfig.getBrowserURL(), token, noteSubject, "post task note description", assignedUserID);
		noteID1 = noteAPI.createNotereturnBean(testConfig.getBrowserURL(), token, noteSubject, "post task note description", assignedUserID);
		noteID2 = noteAPI.createNotereturnBean(testConfig.getBrowserURL(), token, noteSubject, "post task note description", assignedUserID);
		noteID3 = noteAPI.createNotereturnBean(testConfig.getBrowserURL(), token, noteSubject, "post task note description", assignedUserID);
		
		log.info("Creating calls");
		CallRestAPI callRestAPI = new CallRestAPI();
		callID = callRestAPI.createCallreturnBean(testConfig.getBrowserURL(), token, callSubject, "2016-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);
		callID1 = callRestAPI.createCallreturnBean(testConfig.getBrowserURL(), token, callSubject, "2016-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);
		callID2 = callRestAPI.createCallreturnBean(testConfig.getBrowserURL(), token, callSubject, "2016-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);
		callID3 = callRestAPI.createCallreturnBean(testConfig.getBrowserURL(), token, callSubject, "2016-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);

		log.info("Creating Meetings");
		MeetingRestAPI meetingRestAPI = new MeetingRestAPI();
		meetingID = meetingRestAPI.createMeetingreturnBean(baseURL, token, assignedUserID);
		meetingID1 = meetingRestAPI.createMeetingreturnBean(baseURL, token, assignedUserID);
		meetingID2 = meetingRestAPI.createMeetingreturnBean(baseURL, token, assignedUserID);
		meetingID3 = meetingRestAPI.createMeetingreturnBean(baseURL, token, assignedUserID);
		
		log.info("Creating tasks");
		taskID = TaskRestAPITests.createTaskHelper(user1,"Base task",log,baseURL);
		taskID1 = TaskRestAPITests.createTaskHelper(user1,"Base task",log,baseURL);
		taskID2 = TaskRestAPITests.createTaskHelper(user1,"Base task",log,baseURL);
		taskID3 = TaskRestAPITests.createTaskHelper(user1,"Base task",log,baseURL);
	
		log.info("Creating Opportunity");
   		OpportunityRestAPI opportunityRestAPI = new OpportunityRestAPI();
   		baseID = opportunityRestAPI.createOpportunityreturnBean(testConfig.getBrowserURL(), token, "API created Oppty to link components to", clientBeanID, contactID, "SLSP", "03", date, assignedUserID);
   		opportunityID = opportunityRestAPI.createOpportunityreturnBean(testConfig.getBrowserURL(), token, "API created Oppty to link to another Oppty", clientBeanID, contactID, "SLSP", "03", date, assignedUserID);
   		opportunityID1 = opportunityRestAPI.createOpportunityreturnBean(testConfig.getBrowserURL(), token, "API created Oppty to link to another Oppty", clientBeanID, contactID, "SLSP", "03", date, assignedUserID);
   		opportunityID2 = opportunityRestAPI.createOpportunityreturnBean(testConfig.getBrowserURL(), token, "API created Oppty to link to another Oppty", clientBeanID, contactID, "SLSP", "03", date, assignedUserID);
   		opportunityID3 = opportunityRestAPI.createOpportunityreturnBean(testConfig.getBrowserURL(), token, "API created Oppty to link to another Oppty", clientBeanID, contactID, "SLSP", "03", date, assignedUserID);

   		
		log.info("Creating opportunities with RLI's");			
		//line item values
		RevenueItem rli = new RevenueItem();
		rli.populate();
		rli.sL10_OfferingType = "B3000";rli.sL15_SubBrand = "PCO";rli.sL17_SegmentLine = "";rli.sL20_BrandCode = "B3U00";rli.sL30_ProductInformation = "B3U04";rli.sL40_MachineType = "5641NV3";
		
		String RLIOpptyID = opportunityRestAPI.createOpportunityWithRLIreturnBean(testConfig.getBrowserURL(), token, "RLI Linked Oppty", clientBeanID, contactID, "SLSP", "03", date, assignedUserID, assignedUserName, rli);
		rliID = opportunityRestAPI.getRLIFromOppty(testConfig.getBrowserURL(), token, RLIOpptyID).get("id").toString();
		opportunityRestAPI.relateNewLineItemToOpportunity(testConfig.getBrowserURL(), token, RLIOpptyID, "500", "10", "", date, "B3000", "PCO", "", "B3U00", "B3U04", "5641NV3","-99", assignedUserName, assignedUserID);
		
		String RLIOpptyID1 = opportunityRestAPI.createOpportunityWithRLIreturnBean(testConfig.getBrowserURL(), token, "RLI Linked Oppty", clientBeanID, contactID, "SLSP", "03", date, assignedUserID, assignedUserName, rli);
		rliID1 = opportunityRestAPI.getRLIFromOppty(testConfig.getBrowserURL(), token, RLIOpptyID1).get("id").toString();
		opportunityRestAPI.relateNewLineItemToOpportunity(testConfig.getBrowserURL(), token, RLIOpptyID1, "500", "10", "", date, "B3000", "PCO", "", "B3U00", "B3U04", "5641NV3","-99", assignedUserName, assignedUserID);

		String RLIOpptyID2 = opportunityRestAPI.createOpportunityWithRLIreturnBean(testConfig.getBrowserURL(), token, "RLI Linked Oppty", clientBeanID, contactID, "SLSP", "03", date, assignedUserID, assignedUserName, rli);
		rliID2 = opportunityRestAPI.getRLIFromOppty(testConfig.getBrowserURL(), token, RLIOpptyID2).get("id").toString();
		opportunityRestAPI.relateNewLineItemToOpportunity(testConfig.getBrowserURL(), token, RLIOpptyID2, "500", "10", "", date, "B3000", "PCO", "", "B3U00", "B3U04", "5641NV3","-99", assignedUserName, assignedUserID);

		String RLIOpptyID3 = opportunityRestAPI.createOpportunityWithRLIreturnBean(testConfig.getBrowserURL(), token, "RLI Linked Oppty", clientBeanID, contactID, "SLSP", "03", date, assignedUserID, assignedUserName, rli);
		rliID3 = opportunityRestAPI.getRLIFromOppty(testConfig.getBrowserURL(), token, RLIOpptyID3).get("id").toString();
		opportunityRestAPI.relateNewLineItemToOpportunity(testConfig.getBrowserURL(), token, RLIOpptyID3, "500", "10", "", date, "B3000", "PCO", "", "B3U00", "B3U04", "5641NV3","-99", assignedUserName, assignedUserID);

				
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
		this.addDataFile("test_config/extensions/api/opportunity/postOpportunities.csv");
		return testData.getAllDataRows();
    }
	
	@Test(dataProvider = "jsonProvider")
	public void TestCreateOpportunity(HashMap<String,Object> parameterValues){
		Iterator<Map.Entry<String, Object>> it = parameterValues.entrySet().iterator();
        log.info("Getting user.");		
        User user = null;
		String body = "";
		String expectedResponse = "";
		
        while (it.hasNext()) 
        {
          Map.Entry<String, Object> pairs = (Map.Entry<String, Object>)it.next();
          System.out.println(pairs.getKey());
          System.out.println(pairs.getValue());
          if (pairs.getKey().equalsIgnoreCase("body")) {
        	  body=(String) pairs.getValue();	
        	  body = body.replace("*clientBeanID*", clientBeanID);
        	  body = body.replace("*contactID*", contactID);
        	  body = body.replace("*assignedUserID*", assignedUserID);
        	  body = body.replace("*rliID1*", rliID1);
        	  body = body.replace("*rliID2*", rliID2);
        	  body = body.replace("*rliID3*", rliID3);
        	  body = body.replace("*callID1*", callID1);
        	  body = body.replace("*callID2*", callID2);
        	  body = body.replace("*callID3*", callID3);
        	  body = body.replace("*meetingID1*", meetingID1);
        	  body = body.replace("*meetingID2*", meetingID2);
        	  body = body.replace("*meetingID3*", meetingID3);
        	  body = body.replace("*noteID1*", noteID1);
        	  body = body.replace("*noteID2*", noteID2);
        	  body = body.replace("*noteID3*", noteID3);
        	  body = body.replace("*taskID1*", taskID1);
        	  body = body.replace("*taskID2*", taskID2);
        	  body = body.replace("*taskID3*", taskID3);
        	  body = body.replace("*contactID1*", contactID1);
        	  body = body.replace("*contactID2*", contactID2);
        	  body = body.replace("*contactID3*", contactID3);
        	  body = body.replace("*opportunityID1*", opportunityID1);
        	  body = body.replace("*opportunityID2*", opportunityID2);
        	  body = body.replace("*opportunityID3*", opportunityID3);
        	  body = body.replace("*date*", date);
        	  
          }
          else if (pairs.getKey().equalsIgnoreCase("expected_response")) {
        	  expectedResponse=(String) pairs.getValue();	
          }
          else if (pairs.getKey().equalsIgnoreCase("URL")) {
			urlExtension = (String) pairs.getValue();
			urlExtension = urlExtension.replace("*opportunity-id*", baseID);
			urlExtension = urlExtension.replace("*rliID*", rliID);
			urlExtension = urlExtension.replace("*callID*", callID);
			urlExtension = urlExtension.replace("*meetingID*", meetingID);
			urlExtension = urlExtension.replace("*noteID*", noteID);
			urlExtension = urlExtension.replace("*taskID*", taskID);
			urlExtension = urlExtension.replace("*opportunityID*", opportunityID);
			urlExtension = urlExtension.replace("*contactID4*", contactID4);
			urlExtension = urlExtension.replace("*contactID*", contactID);
			urlExtension = urlExtension.replace("*clientBeanID1*", clientBeanID1);
          }
          else if (pairs.getKey().equalsIgnoreCase("user")) {
        	if (pairs.getValue().equals("user2")) {
        		user = commonUserAllocator.getUser(this);
        		
			}
        	else {
        		user = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			}	
          }
          else if (pairs.getKey().equals("TC_Name")) {
				log.info("This is test " + pairs.getValue().toString());
			}
          
        }
        
		log.info("Sending POST request ");
		OpportunityRestAPI opportunityrestAPI = new OpportunityRestAPI();
		String postResponseString = opportunityrestAPI.postOpportunity(getRequestUrl(urlExtension, null), getOAuthToken(user), body, expectedResponse);
		
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
					
					if(requestKey.contains("reason_code")) {
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
				Assert.assertTrue(false, "Opportunity was not created as expected.");
			}
			
			log.info("Opportunity successfully created.");	    
		}
		
	}

}