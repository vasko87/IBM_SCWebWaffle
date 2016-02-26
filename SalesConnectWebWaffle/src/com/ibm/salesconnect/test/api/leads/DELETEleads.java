/**
 * 
 */
package com.ibm.salesconnect.test.api.leads;

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
import com.ibm.salesconnect.API.EmailRestAPI;
import com.ibm.salesconnect.API.LeadsRestAPI;
import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.API.MeetingRestAPI;
import com.ibm.salesconnect.API.NoteRestAPI;
import com.ibm.salesconnect.API.TestDataHolder;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.test.mobile.TaskRestAPITests;

/**
 * @author timlehane
 * @date Feb 2, 2015
 */
public class DELETEleads extends ApiBaseTest {
	private static final Logger log = LoggerFactory.getLogger(DELETEleads.class);
	
	@Parameters({ "apiExtension", "applicationName", "APIM", "apim_environment" })
	private DELETEleads(@Optional("leads") String apiExtension,
			@Optional("SC Auto delete") String applicationName,
			@Optional("true") String APIM,
			@Optional("development") String environment) {

		super(apiExtension, applicationName, APIM, environment);
	}

	private TestDataHolder testData;
    int rand = new Random().nextInt(100000);
	private String contactBeanID = null;
	private String CIContactID = null;
	private String opptyID = null;
	private String noteID = null;
	private String taskID = null;
	private String noteSubject = "post task note subject";
	private String callID = null;
	private String callSubject = "post task call subject";
	private String clientBeanID = null;
	private String assignedUserID = null;
	private String clientID = null;
	private String baseID = null;
	private String favLead = null;
	private String subLead = null;
	private String deletedID = null;
	private String meetingID = null;
	private String emailID = null;
	
	private String leadCallID = null;
	private String leadNegCallID = null;
	private String leadNoteID = null;
	private String leadNegNoteID = null;
	private String leadTaskID = null;
	private String leadNegTaskID = null;
	private String tempLeadID = null;
	private String leadNegMeetingID = null;
	private String leadMeetingID = null;
	private String leadNegEmailID = null;
	private String leadEmailID = null;
	
	User user1 = null;
	User user2 = null;
	
	private void createObjects(){
		log.info("Start creating prerequisites");

		user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
		
		user2 = commonUserAllocator.getUser(this);
		String baseURL = testConfig.getBrowserURL();
		assignedUserID = new APIUtilities().getUserBeanIDFromEmail(baseURL, user1.getEmail(), user1.getPassword());	
		String userID = assignedUserID;

		log.info("Getting client");
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user1.getEmail(), user1.getPassword());
		
		log.info("Creating note");
		NoteRestAPI noteAPI = new NoteRestAPI();
		noteID = noteAPI.createNotereturnBean(testConfig.getBrowserURL(), OAuthToken, noteSubject, "post task note description", assignedUserID);
		
		log.info("Creating call");
		CallRestAPI callRestAPI = new CallRestAPI();
		callID = callRestAPI.createCallreturnBean(testConfig.getBrowserURL(), OAuthToken, callSubject, "2013-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);
		
		log.info("Creating task");
		taskID = TaskRestAPITests.createTaskHelper(user1,"Base task",log,baseURL);
		
		log.info("Creating Email");
		EmailRestAPI emailRestAPI = new EmailRestAPI();
		emailID = emailRestAPI.createEmailreturnBean(testConfig.getBrowserURL(), OAuthToken);
		
		log.info("Creating Meeting");
		MeetingRestAPI meetingRestAPI = new MeetingRestAPI();
		meetingID = meetingRestAPI.createMeetingreturnBean(testConfig.getBrowserURL(), OAuthToken, assignedUserID);
		
		log.info("Creating leads");
		LeadsRestAPI leadRestAPI = new LeadsRestAPI();
		baseID = leadRestAPI.createLeadreturnBean(testConfig.getBrowserURL(), OAuthToken, "lead Name", "lead Description", userID);
		tempLeadID = leadRestAPI.createLeadreturnBean(testConfig.getBrowserURL(), OAuthToken, "lead Name", "lead Description", userID);
		
		favLead = leadRestAPI.createLeadreturnBean(testConfig.getBrowserURL(), OAuthToken,"{\"name\":\"favLead\",\"description\":\"favLeaddescription\",\"first_name\":\"one\",\"last_name\":\"smith\"," +
				"\"phone_mobile\":\"123456789\",\"primary_address_country\":\"US\",\"status\":\"LEADPROG\", \"status_detail_c\":\"PROGINIT\",\"my_favorite\":\"true\"," +
				"\"assigned_user_id\":\""+assignedUserID+"\",\"lead_source\":\"LSCAMP\"}");	
		
		subLead = leadRestAPI.createLeadreturnBean(testConfig.getBrowserURL(), OAuthToken, "lead Name", "lead Description", userID);
		leadRestAPI.postLead(testConfig.getBrowserURL() + "rest/v10/Leads/" + subLead + "/subscribe", OAuthToken,"", "200");
		
		leadCallID = leadRestAPI.createLeadreturnBean(testConfig.getBrowserURL(), OAuthToken, "lead Name", "lead Description", userID);
		leadNegCallID = leadRestAPI.createLeadreturnBean(testConfig.getBrowserURL(), OAuthToken, "lead Name", "lead Description", userID);
		Assert.assertTrue(leadRestAPI.linkLeadtoObject(testConfig.getBrowserURL(), OAuthToken, leadCallID,"calls",callID));
		Assert.assertTrue(leadRestAPI.linkLeadtoObject(testConfig.getBrowserURL(), OAuthToken, leadNegCallID,"calls",callID));
		
		leadNoteID = leadRestAPI.createLeadreturnBean(testConfig.getBrowserURL(), OAuthToken, "lead Name", "lead Description", userID);
		leadNegNoteID = leadRestAPI.createLeadreturnBean(testConfig.getBrowserURL(), OAuthToken, "lead Name", "lead Description", userID);
		Assert.assertTrue(leadRestAPI.linkLeadtoObject(testConfig.getBrowserURL(), OAuthToken, leadNoteID,"notes",noteID));
		Assert.assertTrue(leadRestAPI.linkLeadtoObject(testConfig.getBrowserURL(), OAuthToken, leadNegNoteID,"notes",noteID));
		
		leadTaskID = leadRestAPI.createLeadreturnBean(testConfig.getBrowserURL(), OAuthToken, "lead Name", "lead Description", userID);
		leadNegTaskID = leadRestAPI.createLeadreturnBean(testConfig.getBrowserURL(), OAuthToken, "lead Name", "lead Description", userID);
		Assert.assertTrue(leadRestAPI.linkLeadtoObject(testConfig.getBrowserURL(), OAuthToken, leadTaskID,"tasks",taskID));
		Assert.assertTrue(leadRestAPI.linkLeadtoObject(testConfig.getBrowserURL(), OAuthToken, leadNegTaskID,"tasks",taskID));
		
		leadMeetingID = leadRestAPI.createLeadreturnBean(testConfig.getBrowserURL(), OAuthToken, "lead Name", "lead Description", userID);
		leadNegMeetingID = leadRestAPI.createLeadreturnBean(testConfig.getBrowserURL(), OAuthToken, "lead Name", "lead Description", userID);
		Assert.assertTrue(leadRestAPI.linkLeadtoObject(testConfig.getBrowserURL(), OAuthToken, leadMeetingID,"meetings",meetingID));
		Assert.assertTrue(leadRestAPI.linkLeadtoObject(testConfig.getBrowserURL(), OAuthToken, leadNegMeetingID,"meetings",meetingID));
		
		leadEmailID = leadRestAPI.createLeadreturnBean(testConfig.getBrowserURL(), OAuthToken, "lead Name", "lead Description", userID);
		leadNegEmailID = leadRestAPI.createLeadreturnBean(testConfig.getBrowserURL(), OAuthToken, "lead Name", "lead Description", userID);
		Assert.assertTrue(leadRestAPI.linkLeadtoObject(testConfig.getBrowserURL(), OAuthToken, leadEmailID,"emails",emailID));
		Assert.assertTrue(leadRestAPI.linkLeadtoObject(testConfig.getBrowserURL(), OAuthToken, leadNegEmailID,"emails",emailID));
		
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
     	this.addDataFile("test_config/extensions/api/lead/deleteLead.csv");
     	//Return an array of arrays where each item in the array is a HashMap of parameter values
     	//Test content
        return testData.getAllDataRows();
     }
	
     @Test(dataProvider = "DataProvider")
     public void LeadsDelete(HashMap<String,Object> parameterValues){
     
     	String expectedResponseCode = null;
     	String urlLeadID = null;
     	String urlExtension = null;
     	String relatedObject = null;
     	
     	User user = null;
 		
         Iterator<Map.Entry<String, Object>> it = parameterValues.entrySet().iterator();
         while (it.hasNext()) {
             Map.Entry<String, Object> pairs = (Map.Entry<String, Object>)it.next();
             
             if(pairs.getKey().equals("expectedResponse")){
             	expectedResponseCode=pairs.getValue().toString();
             	it.remove();
             }
             else if (pairs.getKey().equals("TC_Name")) {
 				log.info("This is test " + pairs.getValue().toString());
 			}
             else if (pairs.getKey().equalsIgnoreCase("lead")){
 				urlLeadID = geturlLeadID(pairs.getValue().toString());
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
		if (!urlLeadID.equalsIgnoreCase("")) {
			url+=urlLeadID;
		}
		if (!urlExtension.equalsIgnoreCase("")) {
			url+=urlExtension;
		}
		if (!relatedObject.equalsIgnoreCase("")) {
			url+="/";
			url+=relatedObject;
		}	
		
		log.info("Sending DELETE request");
		LeadsRestAPI leadrestAPI = new LeadsRestAPI();
		String token = getOAuthToken(user);
		
		try{
		leadrestAPI.deleteLead(getRequestUrl(url, null), token, expectedResponseCode);
		}
		catch (Exception e) {
			leadrestAPI.deleteLead(getRequestUrl(url, null), getOAuthToken(user), expectedResponseCode);
		}
		
		//Verify response
		if (expectedResponseCode.equalsIgnoreCase("200")) {
			if (!relatedObject.equalsIgnoreCase("")) {
				new LeadsRestAPI().getLead(baseURL + "rest/v10/Leads/" + url, new LoginRestAPI().getOAuth2Token(baseURL, user.getEmail(), user.getPassword()), "404");
			}
		}	
       }
     
     
     @Test
     public void MassDelete(){
    	log.info("Getting User");
    	User user = commonUserAllocator.getUser(this);
    	String userID = new APIUtilities().getUserBeanIDFromEmail(baseURL, user1.getEmail(), user1.getPassword());
    	
 		log.info("Getting token");
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user.getEmail(), user.getPassword());
		
 		log.info("Creating leads");
		LeadsRestAPI leadRestAPI = new LeadsRestAPI();
		String lead1 = leadRestAPI.createLeadreturnBean(testConfig.getBrowserURL(), OAuthToken, "lead Name", "lead Description", userID);
		String lead2 = leadRestAPI.createLeadreturnBean(testConfig.getBrowserURL(), OAuthToken, "lead Name", "lead Description", userID);
		String lead3 = leadRestAPI.createLeadreturnBean(testConfig.getBrowserURL(), OAuthToken, "lead Name", "lead Description", userID);
		
		String body = "{\"massupdate_params\":{\"uid\":[\""+lead1+"\", \""+lead2+"\",\""+lead3+"\"]}}";
		String token3 = loginRestAPI.getOAuth2Token(baseURL, user.getEmail(), user.getPassword());	
		
		LeadsRestAPI leadrestAPI = new LeadsRestAPI();
		leadrestAPI.deleteLead(getRequestUrl("/MassUpdate", null), getOAuthToken(user),body, "200");
 		
 		log.info("Check delete");
		leadRestAPI.getLead(baseURL + "rest/v10/Leads/" + lead1, token3, "404");
		leadRestAPI.getLead(baseURL + "rest/v10/Leads/" + lead2, token3, "404");
		leadRestAPI.getLead(baseURL + "rest/v10/Leads/" + lead3, token3, "404");	
 		
     }
     
     //@Test
     public void convertThenDelete(){
    	 
    	   	log.info("Getting User");
        	User user = commonUserAllocator.getUser(this);
        	String userID = new APIUtilities().getUserBeanIDFromEmail(baseURL, user1.getEmail(), user1.getPassword());
        	
     		log.info("Getting token");
    		LoginRestAPI loginRestAPI = new LoginRestAPI();
    		String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user.getEmail(), user.getPassword());
    		
     		log.info("Creating lead");
    		LeadsRestAPI leadRestAPI = new LeadsRestAPI();
    		String lead = leadRestAPI.createLeadreturnBean(testConfig.getBrowserURL(), OAuthToken, "lead Name", "lead Description", userID);
    		
			log.info("Sending Post Convert requestrequest");
			String json ="{\"modules\": {\"Contacts\": {\"deleted\": 0,\"do_not_call\": false,\"portal_active\": 0,\"preferred_language\": \"en_us\","+
			          "\"first_name\": \"one\",\"last_name\": \"smith\",\"title\": \"Director Operations\",\"primary_address_country\":\"US\"}}}";
			
			leadRestAPI.postLead(baseURL + "rest/v10/Leads/" + lead + "/convert", OAuthToken,json, "200");
			
			LeadsRestAPI leadrestAPI = new LeadsRestAPI();
			leadrestAPI.deleteLead(getRequestUrl("/MassUpdate", null), getOAuthToken(user), "200");
			
			leadRestAPI.getLead(baseURL + "rest/v10/Leads/" + lead, OAuthToken, "404");
  		
     }
     
     private String geturlLeadID(String csvLead){
     	if (csvLead.equalsIgnoreCase("base")) {
 			return baseID;
 		}
     	else if (csvLead.equalsIgnoreCase("callLead")) {
 			return leadCallID;
 		}
     	else if (csvLead.equalsIgnoreCase("negcallLead")) {
 			return leadNegCallID;
 		}
     	else if (csvLead.equalsIgnoreCase("noteLead")) {
 			return leadNoteID;
 		}
     	else if (csvLead.equalsIgnoreCase("negnoteLead")) {
 			return leadNegNoteID;
 		}
     	else if (csvLead.equalsIgnoreCase("taskLead")) {
 			return leadTaskID;
 		}
     	else if (csvLead.equalsIgnoreCase("negtaskLead")) {
 			return leadNegTaskID;
 		}
     	else if (csvLead.equalsIgnoreCase("meetingLead")) {
 			return leadMeetingID;
 		}
     	else if (csvLead.equalsIgnoreCase("negmeetingLead")) {
 			return leadNegMeetingID;
 		}
     	else if (csvLead.equalsIgnoreCase("emailLead")) {
 			return leadEmailID;
 		}
     	else if (csvLead.equalsIgnoreCase("negemailLead")) {
 			return leadNegEmailID;
 		}
     	else if (csvLead.equalsIgnoreCase("tempLead")) {
 			return tempLeadID;
 		}
     	else if (csvLead.equalsIgnoreCase("deleted")) {
 			return deletedID;
 		}
     	else if (csvLead.equalsIgnoreCase("favLead")) {
 			return favLead;
 		}
     	else if (csvLead.equalsIgnoreCase("subLead")) {
 			return subLead;
 		}
     	else if (csvLead.equalsIgnoreCase("*blank*")) {
 			return "";
 		}
     	else if (csvLead.contains("invalid")) {
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
     	else if (relatedFromCSV.equalsIgnoreCase("validCall")) {
 			return callID;
 		}
     	else if (relatedFromCSV.equalsIgnoreCase("validContact")) {
 			return contactBeanID;
 		}
     	else if (relatedFromCSV.equalsIgnoreCase("validNote")) {
 			return noteID;
 		}
     	else if (relatedFromCSV.equalsIgnoreCase("validOpportunity")) {
 			return opptyID;
 		}
     	else if (relatedFromCSV.equalsIgnoreCase("validtask")) {
 			return taskID;
 		}
     	else if (relatedFromCSV.equalsIgnoreCase("validEmail")) {
 			return emailID;
 		}
     	else if (relatedFromCSV.equalsIgnoreCase("validMeeting")) {
 			return meetingID;
 		}
     	else if (relatedFromCSV.equalsIgnoreCase("validCCMS")) {
 			return clientID;
 		}
     	else if (relatedFromCSV.equalsIgnoreCase("validExtRefID")) {
 			return CIContactID;
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
