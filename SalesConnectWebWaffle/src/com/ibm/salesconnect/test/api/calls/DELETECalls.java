
/**
 * 
 */
package com.ibm.salesconnect.test.api.calls;

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
import com.ibm.salesconnect.API.LeadsRestAPI;
import com.ibm.salesconnect.API.OpportunityRestAPI;
import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.API.NoteRestAPI;
import com.ibm.salesconnect.API.TestDataHolder;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.test.mobile.TaskRestAPITests;

/**
 * @author evafarrell
 * @date Oct 8, 2015
 */
public class DELETECalls extends ApiBaseTest {
	private static final Logger log = LoggerFactory.getLogger(DELETECalls.class);
	
	@Parameters({ "apiExtension", "applicationName", "APIM", "apim_environment" })
	private DELETECalls(@Optional("calls") String apiExtension,
			@Optional("SC Auto delete") String applicationName,
			@Optional("true") String APIM,
			@Optional("development") String environment) {

		super(apiExtension, applicationName, APIM, environment);
	}

	private TestDataHolder testData;
    int rand = new Random().nextInt(100000);
	private String contactID1 = null;
	private String opptyID = null;
	private String noteID = null;
	private String taskID = null;
	private String noteSubject = "post task note subject";
	private String callID = null;
	private String clientBeanID = null;
	private String assignedUserID = null;
	private String clientID = null;
	private String baseID = null;
	private String favCall = null;
	private String subCall = null;
	private String deletedID = null;
	private String leadID = null;
	
	private String CallCallID = null;
	private String CallNegCallID = null;
	private String CallNoteID = null;
	private String CallNegNoteID = null;
	private String CallTaskID = null;
	private String CallNegTaskID = null;
	private String tempCallID = null;
	private String CallID = null;
	private String CallNegOpptyID  = null;
	private String CallOpptyID = null;
	
	
	User user1 = null;
	User user2 = null;
	private String assignedUserName;
	private String assignedUserID2;
	private String assignedUserName2;
	private String CallNegUserID;
	private String CallUserID;
	private String CallAssignedUserID;
	private String CallNegAssignedUserID;
	private String CallNegAdditionalAssigneeID;
	private String CallAdditionalAssigneeID;
	private String CallNegContactID;
	private String CallContactID;
	private String CallNegLeadID;
	private String CallLeadID;
	
	private void createObjects(){
		log.info("Start creating prerequisites");

		user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
		user2 = commonUserAllocator.getUser(this);
		String baseURL = testConfig.getBrowserURL();
		assignedUserID = new APIUtilities().getUserBeanIDFromEmail(baseURL, user1.getEmail(), user1.getPassword());	
		assignedUserName = user1.getDisplayName();
		assignedUserID2 = new APIUtilities().getUserBeanIDFromEmail(baseURL, user2.getEmail(), user2.getPassword());	
		assignedUserName2 = user2.getDisplayName();
		PoolClient poolClient= commonClientAllocator.getGroupClient(GC.SC,this);
		
		log.info("Getting client");
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user1.getEmail(), user1.getPassword());
		String headers[] = {"OAuth-Token", OAuthToken};
		
		clientID = poolClient.getCCMS_ID();
		clientBeanID = new CollabWebAPI().getbeanIDfromClientID(baseURL, clientID, headers );

		log.info("Creating contact");
		ContactRestAPI contactAPI = new ContactRestAPI();
		contactID1 = contactAPI.createContactreturnBean(baseURL, headers, "ContactFirst", "ContactLast", "US", "(555) 555-5555", assignedUserID, clientBeanID);

		log.info("Creating note");
		NoteRestAPI noteAPI = new NoteRestAPI();
		noteID = noteAPI.createNotereturnBean(testConfig.getBrowserURL(), OAuthToken, noteSubject, "post task note description", assignedUserID);
		
		log.info("Creating lead");
		LeadsRestAPI leadRestAPI = new LeadsRestAPI();
		leadID = leadRestAPI.createLeadreturnBean(testConfig.getBrowserURL(), OAuthToken, "lead Name", "lead Description", assignedUserID);

		log.info("Creating Opportunity");
   		OpportunityRestAPI opportunityRestAPI = new OpportunityRestAPI();
   		opptyID = opportunityRestAPI.createOpportunityreturnBean(testConfig.getBrowserURL(), OAuthToken, "API created Oppty to link to another Oppty", clientBeanID, contactID1, "SLSP", "03", "2016-10-28", assignedUserID);

		log.info("Creating task");
		taskID = TaskRestAPITests.createTaskHelper(user1,"Base task",log,baseURL);
		
		log.info("Creating Calls");
		CallRestAPI callRestAPI = new CallRestAPI();
		baseID = callRestAPI.createCallreturnBean(testConfig.getBrowserURL(), OAuthToken, "API created Base Call to test DELETE Call", "2016-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);
		callID = callRestAPI.createCallreturnBean(testConfig.getBrowserURL(), OAuthToken, "API created Base Call to test DELETE Call", "2016-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);

		tempCallID = callRestAPI.createCallreturnBean(testConfig.getBrowserURL(), OAuthToken, "API created Temp Call to test DELETE Call", "2016-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);
		
		favCall = callRestAPI.createCallreturnBean(testConfig.getBrowserURL(), OAuthToken, "API created Favorite Call to test DELETE Call", "2016-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);
		
		subCall = callRestAPI.createCallreturnBean(testConfig.getBrowserURL(), OAuthToken, "API created Subscribe Call to test DELETE Call", "2016-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);
		callRestAPI.postCall(testConfig.getBrowserURL()+"/rest/v10/Calls/" + subCall+"/subscribe",OAuthToken,"", "200");
				
		CallNoteID = callRestAPI.createCallreturnBean(testConfig.getBrowserURL(), OAuthToken, "API created Note Linked Call to test DELETE Call", "2016-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);
		CallNegNoteID = callRestAPI.createCallreturnBean(testConfig.getBrowserURL(), OAuthToken, "API created Note Linked Call to test DELETE Call", "2016-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);
		Assert.assertTrue(callRestAPI.linkRecordToCallReturnBoolean(testConfig.getBrowserURL(), OAuthToken, CallNoteID,noteID,"Notes"));
		Assert.assertTrue(callRestAPI.linkRecordToCallReturnBoolean(testConfig.getBrowserURL(), OAuthToken, CallNegNoteID,noteID,"Notes"));
		
		CallTaskID = callRestAPI.createCallreturnBean(testConfig.getBrowserURL(), OAuthToken, "API created Task Linked Call to test DELETE Call", "2016-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);
		CallNegTaskID = callRestAPI.createCallreturnBean(testConfig.getBrowserURL(), OAuthToken, "API created Task Linked Call to test DELETE Call", "2016-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);
		Assert.assertTrue(callRestAPI.linkRecordToCallReturnBoolean(testConfig.getBrowserURL(), OAuthToken, CallTaskID,taskID,"Tasks"));
		Assert.assertTrue(callRestAPI.linkRecordToCallReturnBoolean(testConfig.getBrowserURL(), OAuthToken, CallNegTaskID,taskID,"Tasks"));
				
		CallOpptyID = callRestAPI.createCallreturnBean(testConfig.getBrowserURL(), OAuthToken, "API created Oppty Linked Call to test DELETE Call", "2016-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);
		CallNegOpptyID = callRestAPI.createCallreturnBean(testConfig.getBrowserURL(), OAuthToken, "API created Oppty Linked Call to test DELETE Call", "2016-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);
		Assert.assertTrue(callRestAPI.linkRecordToCallReturnBoolean(testConfig.getBrowserURL(), OAuthToken, CallOpptyID,opptyID, "Opportunities"));
		Assert.assertTrue(callRestAPI.linkRecordToCallReturnBoolean(testConfig.getBrowserURL(), OAuthToken, CallNegOpptyID,opptyID, "Opportunities"));
		
		CallLeadID = callRestAPI.createCallreturnBean(testConfig.getBrowserURL(), OAuthToken, "API created Lead Linked Call to test DELETE Call", "2016-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);
		CallNegLeadID = callRestAPI.createCallreturnBean(testConfig.getBrowserURL(), OAuthToken, "API created Lead Linked Call to test DELETE Call", "2016-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);
		Assert.assertTrue(callRestAPI.linkRecordToCallReturnBoolean(testConfig.getBrowserURL(), OAuthToken, CallLeadID,leadID, "Leads"));
		Assert.assertTrue(callRestAPI.linkRecordToCallReturnBoolean(testConfig.getBrowserURL(), OAuthToken, CallNegLeadID,leadID, "Leads"));
		
		CallContactID = callRestAPI.createCallreturnBean(testConfig.getBrowserURL(), OAuthToken, "API created Contact Linked Call to test DELETE Call", "2016-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);
		CallNegContactID = callRestAPI.createCallreturnBean(testConfig.getBrowserURL(), OAuthToken, "API created Contact Linked Call to test DELETE Call", "2016-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);
		Assert.assertTrue(callRestAPI.linkRecordToCallReturnBoolean(testConfig.getBrowserURL(), OAuthToken, CallContactID,contactID1, "Contacts"));
		Assert.assertTrue(callRestAPI.linkRecordToCallReturnBoolean(testConfig.getBrowserURL(), OAuthToken, CallNegContactID,contactID1, "Contacts"));
		
		CallAdditionalAssigneeID = callRestAPI.createCallreturnBean(testConfig.getBrowserURL(), OAuthToken, "API created Additional Assignee Linked Call to test DELETE Call", "2016-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);
		CallNegAdditionalAssigneeID = callRestAPI.createCallreturnBean(testConfig.getBrowserURL(), OAuthToken, "API created Additional Assignee Linked Call to test DELETE Call", "2016-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);
		Assert.assertTrue(callRestAPI.linkRecordToCallReturnBoolean(testConfig.getBrowserURL(), OAuthToken, CallAdditionalAssigneeID,assignedUserID2, "AdditionalAssignees"));
		Assert.assertTrue(callRestAPI.linkRecordToCallReturnBoolean(testConfig.getBrowserURL(), OAuthToken, CallNegAdditionalAssigneeID,assignedUserID2, "AdditionalAssignees"));
		
		CallAssignedUserID = callRestAPI.createCallreturnBean(testConfig.getBrowserURL(), OAuthToken, "API created Assigned User Linked Call to test DELETE Call", "2016-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);
		CallNegAssignedUserID = callRestAPI.createCallreturnBean(testConfig.getBrowserURL(), OAuthToken, "API created Assigned User Linked Call to test DELETE Call", "2016-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);
		Assert.assertTrue(callRestAPI.linkRecordToCallReturnBoolean(testConfig.getBrowserURL(), OAuthToken, CallAssignedUserID,assignedUserID2, "AssignedUser"));
		Assert.assertTrue(callRestAPI.linkRecordToCallReturnBoolean(testConfig.getBrowserURL(), OAuthToken, CallNegAssignedUserID,assignedUserID2, "AssignedUser"));
		
		CallUserID = callRestAPI.createCallreturnBean(testConfig.getBrowserURL(), OAuthToken, "API created User Linked Call to test DELETE Call", "2016-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);
		CallNegUserID = callRestAPI.createCallreturnBean(testConfig.getBrowserURL(), OAuthToken, "API created User Linked Call to test DELETE Call", "2016-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);
		Assert.assertTrue(callRestAPI.linkRecordToCallReturnBoolean(testConfig.getBrowserURL(), OAuthToken, CallUserID,assignedUserID2, "Users"));
		Assert.assertTrue(callRestAPI.linkRecordToCallReturnBoolean(testConfig.getBrowserURL(), OAuthToken, CallNegUserID,assignedUserID2, "Users"));
		
		log.info("Finish creating prerequisites");
	}
	
    public void addDataFile(String filePath){
        if(this.testData== null){
            this.testData = new TestDataHolder();
        }
         this.testData.addDataLocation(filePath);
     }
    
     

     	@DataProvider(name="jsonProvider")
	    public Object[][] getTestData(){
		this.createObjects();
		this.addDataFile("test_config/extensions/api/call/deleteCall.csv");
		return testData.getAllDataRows();
    }
	
	@Test(dataProvider = "jsonProvider")
	public void TestDeleteCall(HashMap<String,Object> parameterValues){
		Iterator<Map.Entry<String, Object>> it = parameterValues.entrySet().iterator();
        log.info("Getting user.");		
     	String expectedResponseCode = null;
     	String urlCallID = null;
     	String urlExtension = null;
     	String relatedObject = null;
     	
     	User user = null;
		
         while (it.hasNext()) {
             Map.Entry<String, Object> pairs = (Map.Entry<String, Object>)it.next();
             System.out.println(pairs.getKey());
             System.out.println(pairs.getValue());
             if(pairs.getKey().equals("expectedResponse")){
             	expectedResponseCode=(String) pairs.getValue();	
             	it.remove();
             }
             else if (pairs.getKey().equals("TC_Name")) {
 				log.info("This is test " + pairs.getValue().toString());
 			}
             else if (pairs.getKey().equalsIgnoreCase("call")){
 				urlCallID = geturlCallID((String) pairs.getValue());
 			}
             else if (pairs.getKey().equalsIgnoreCase("user")){
  				if (pairs.getValue().toString().equalsIgnoreCase("same")) {
					user = user1;
				}
  				else if (pairs.getValue().toString().equalsIgnoreCase("other")) {
					user = user2;
				}
  			}
             else if (pairs.getKey().equalsIgnoreCase("urlextension")) {
 				if (pairs.getValue().toString().equalsIgnoreCase("*blank*")) {
 					urlExtension = "";
 				}
 				else {
 					urlExtension = pairs.getValue().toString();
 				}
 			}
             else if (pairs.getKey().equalsIgnoreCase("related")) {
 				if (pairs.getValue().toString().equalsIgnoreCase("*blank*")) {
 					relatedObject = "";
 				}
 				else {
 					relatedObject = getRelatedObjectID(pairs.getValue().toString());
 				}
 				
 			}
         }  
		
		//Build request URL
		String url = "";
		if (!urlCallID.equalsIgnoreCase("")) {
			url+=urlCallID;
		}
		if (!urlExtension.equalsIgnoreCase("")) {
			url+=urlExtension;
		}
		if (!relatedObject.equalsIgnoreCase("")) {
			url+="/";
			url+=relatedObject;
		}	
		System.out.println("URL is "+url);
		
		log.info("Sending DELETE request");
		CallRestAPI CallrestAPI = new CallRestAPI();
		String token = getOAuthToken(user);
		
		try{
			CallrestAPI.deleteCall(getRequestUrl(url, null), token,"", expectedResponseCode);
		}
		catch (Exception e) {
			CallrestAPI.deleteCall(getRequestUrl(url, null), token,"", expectedResponseCode);
		}
		
		log.info("Verify Response");
		if (expectedResponseCode.equalsIgnoreCase("200")) {
			if (!relatedObject.equalsIgnoreCase("")) {
				//new CallRestAPI().getCall(getRequestUrl(url, null), new LoginRestAPI().getOAuth2Token(baseURL, user.getEmail(), user.getPassword()), "404");
				new CallRestAPI().getCall(testConfig.getBrowserURL()+"/rest/v10/Calls/"+ url,new LoginRestAPI().getOAuth2Token(baseURL, user.getEmail(), user.getPassword()), "404");
				
			}
		}	
       }
     
     @Test
     public void MassDelete(){
    	log.info("Getting User");
    	User user = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
    	PoolClient poolClient= commonClientAllocator.getGroupClient(GC.SC,this);
		
    	log.info("Getting token");
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user.getEmail(), user.getPassword());
		String headers[] = {"OAuth-Token", OAuthToken};
		assignedUserID = new APIUtilities().getUserBeanIDFromEmail(baseURL, user.getEmail(), user.getPassword());	
		
		log.info("Getting client");
		clientID = poolClient.getCCMS_ID();
		clientBeanID = new CollabWebAPI().getbeanIDfromClientID(baseURL, clientID, headers );
					
 		log.info("Creating calls for MASS Delete");
		CallRestAPI callRestAPI = new CallRestAPI();
		String Call1 = callRestAPI.createCallreturnBean(testConfig.getBrowserURL(), OAuthToken, "API created Call to test MASS DELETE Call", "2016-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);
		String Call2 = callRestAPI.createCallreturnBean(testConfig.getBrowserURL(), OAuthToken, "API created Call to test MASS DELETE Call", "2016-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);
		String Call3 = callRestAPI.createCallreturnBean(testConfig.getBrowserURL(), OAuthToken, "API created Call to test Mass DELETE Call", "2016-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);
		
		String body = "{\"massupdate_params\":{\"uid\":[\""+Call1+"\", \""+Call2+"\",\""+Call3+"\"]}}";
		String token3 = loginRestAPI.getOAuth2Token(baseURL, user.getEmail(), user.getPassword());	
		
		CallRestAPI CallrestAPI = new CallRestAPI();
		CallrestAPI.deleteCall(getRequestUrl("/MassUpdate", null), getOAuthToken(user),body, "200");
 		
 		log.info("Check Mass Delete");
 		callRestAPI.getCall(testConfig.getBrowserURL()+"/rest/v10/Calls/"+ Call1, token3, "404");
		callRestAPI.getCall(testConfig.getBrowserURL()+"/rest/v10/Calls/"+ Call2, token3, "404");
		callRestAPI.getCall(testConfig.getBrowserURL()+"/rest/v10/Calls/"+ Call3, token3, "404");
     }
     
     
     private String geturlCallID(String csvCall){
     	if (csvCall.equalsIgnoreCase("base")) {
 			return baseID;
 		}
     	else if (csvCall.equalsIgnoreCase("noteCall")) {
 			return CallNoteID;
 		}
     	else if (csvCall.equalsIgnoreCase("negnoteCall")) {
 			return CallNegNoteID;
 		}
     	else if (csvCall.equalsIgnoreCase("taskCall")) {
 			return CallTaskID;
 		}
     	else if (csvCall.equalsIgnoreCase("negtaskCall")) {
 			return CallNegTaskID;
 		}
     	else if (csvCall.equalsIgnoreCase("leadCall")) {
 			return CallLeadID;
 		}
     	else if (csvCall.equalsIgnoreCase("negleadCall")) {
 			return CallNegLeadID;
 		}
     	else if (csvCall.equalsIgnoreCase("contactCall")) {
 			return CallContactID;
 		}
     	else if (csvCall.equalsIgnoreCase("negcontactCall")) {
 			return CallNegContactID;
 		}
     	else if (csvCall.equalsIgnoreCase("additionalassigneeCall")) {
 			return CallAdditionalAssigneeID;
 		}
     	else if (csvCall.equalsIgnoreCase("negadditionalassigneeCall")) {
 			return CallNegAdditionalAssigneeID;
 		}
     	else if (csvCall.equalsIgnoreCase("assigneduserCall")) {
 			return CallAssignedUserID;
 		}
     	else if (csvCall.equalsIgnoreCase("negassigneduserCall")) {
 			return CallNegAssignedUserID;
 		}
     	else if (csvCall.equalsIgnoreCase("userCall")) {
 			return CallUserID;
 		}
     	else if (csvCall.equalsIgnoreCase("neguserCall")) {
 			return CallNegUserID;
 		}
     	else if (csvCall.equalsIgnoreCase("opptyCall")) {
 			return CallOpptyID;
 		}
     	else if (csvCall.equalsIgnoreCase("negopptyCall")) {
 			return CallNegOpptyID;
 		}
     	else if (csvCall.equalsIgnoreCase("tempCall")) {
 			return tempCallID;
 		}
     	else if (csvCall.equalsIgnoreCase("deleted")) {
 			return deletedID;
 		}
     	else if (csvCall.equalsIgnoreCase("favCall")) {
 			return favCall;
 		}
     	else if (csvCall.equalsIgnoreCase("subCall")) {
 			return subCall;
 		}
     	else if (csvCall.equalsIgnoreCase("*blank*")) {
 			return "";
 		}
     	else if (csvCall.contains("invalid")) {
 			return "invalid";
 		}
     	else {
 			return null;
 		}
     }
     
     private String getRelatedObjectID(String relatedFromCSV){
     	if (relatedFromCSV.equalsIgnoreCase("validAccount")) {
 			return clientBeanID;
 		}
     	else if (relatedFromCSV.equalsIgnoreCase("validNote")) {
 			return noteID;
 		}
     	else if (relatedFromCSV.equalsIgnoreCase("validOppty")) {
 			return opptyID;
 		}
     	else if (relatedFromCSV.equalsIgnoreCase("validtask")) {
 			return taskID;
 		}
     	else if (relatedFromCSV.equalsIgnoreCase("validLead")) {
 			return leadID;
 		}
     	else if (relatedFromCSV.equalsIgnoreCase("validContact")) {
 			return contactID1;
 		}
     	else if (relatedFromCSV.equalsIgnoreCase("validAdditionalAssignee")) {
 			return assignedUserID2;
 		}
     	else if (relatedFromCSV.equalsIgnoreCase("validAssignedUser")) {
 			return assignedUserID2;
 		}
     	else if (relatedFromCSV.equalsIgnoreCase("validUser")) {
 			return assignedUserID2;
 		}
     	else if (relatedFromCSV.contains("invalid")) {
 			return "invalidID";
 		}
     	else if (relatedFromCSV.equalsIgnoreCase("*blank*")) {
 			return "";
 		}
     	return null;
     }
     
     /**
      * Get the oauth string
      */
 	public String getOAuthExtension(){		
 		return "oauth?" + clientIDandSecret;
 	}
     
}