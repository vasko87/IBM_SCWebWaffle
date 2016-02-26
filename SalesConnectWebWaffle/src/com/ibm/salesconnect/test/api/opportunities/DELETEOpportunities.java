
/**
 * 
 */
package com.ibm.salesconnect.test.api.opportunities;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.ibm.salesconnect.API.OpportunityRestAPI;
import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.API.MeetingRestAPI;
import com.ibm.salesconnect.API.NoteRestAPI;
import com.ibm.salesconnect.API.SugarAPI;
import com.ibm.salesconnect.API.TestDataHolder;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.objects.RevenueItem;
import com.ibm.salesconnect.test.mobile.TaskRestAPITests;

/**
 * @author evafarrell
 * @date Aug 20, 2015
 */
public class DELETEOpportunities extends ApiBaseTest {
	private static final Logger log = LoggerFactory.getLogger(DELETEOpportunities.class);
	
	@Parameters({ "apiExtension", "applicationName", "APIM", "apim_environment" })
	private DELETEOpportunities(@Optional("opportunities") String apiExtension,
			@Optional("SC Auto delete") String applicationName,
			@Optional("true") String APIM,
			@Optional("development") String environment) {

		super(apiExtension, applicationName, APIM, environment);
	}

	private TestDataHolder testData;
    int rand = new Random().nextInt(100000);
	private String contactID = null;
	private String contactID1 = null;
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
	private String favOpportunity = null;
	private String subOpportunity = null;
	private String deletedID = null;
	private String meetingID = null;
	private String rliID = null;
	
	private String opportunityCallID = null;
	private String opportunityNegCallID = null;
	private String opportunityNoteID = null;
	private String opportunityNegNoteID = null;
	private String opportunityTaskID = null;
	private String opportunityNegTaskID = null;
	private String tempOpportunityID = null;
	private String opportunityNegMeetingID = null;
	private String opportunityMeetingID = null;
	private String opportunityNegRLIID = null;
	private String opportunityRLIlID = null;
	private String opportunityID = null;
	private String opportunityNegOpptyID  = null;
	private String opportunityOpptyID = null;
	private String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString();
	
	
	User user1 = null;
	User user2 = null;
	private String assignedUserName;
	private String opportunityRLIlID1;
	private String rliID1;
	
	private void createObjects(){
		log.info("Start creating prerequisites");

		user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
		user2 = commonUserAllocator.getUser(this);
		String baseURL = testConfig.getBrowserURL();
		assignedUserID = new APIUtilities().getUserBeanIDFromEmail(baseURL, user1.getEmail(), user1.getPassword());	
		assignedUserName = user1.getDisplayName();
		PoolClient poolClient= commonClientAllocator.getGroupClient(GC.SC,this);
		
		log.info("Getting client");
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user1.getEmail(), user1.getPassword());
		String headers[] = {"OAuth-Token", OAuthToken};
		
		clientID = poolClient.getCCMS_ID();
		clientBeanID = new CollabWebAPI().getbeanIDfromClientID(baseURL, clientID, headers );
		
		log.info("Creating note");
		NoteRestAPI noteAPI = new NoteRestAPI();
		noteID = noteAPI.createNotereturnBean(testConfig.getBrowserURL(), OAuthToken, noteSubject, "post task note description", assignedUserID);
		
		log.info("Creating call");
		CallRestAPI callRestAPI = new CallRestAPI();
		callID = callRestAPI.createCallreturnBean(testConfig.getBrowserURL(), OAuthToken, callSubject, "2013-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);
		
		log.info("Creating task");
		taskID = TaskRestAPITests.createTaskHelper(user1,"Base task",log,baseURL);
		
		log.info("Creating contacts");
		ContactRestAPI contactAPI = new ContactRestAPI();
		contactID = contactAPI.createContactreturnBean(baseURL, headers, "ContactFirst", "ContactLast", "US", "(555) 555-5555", assignedUserID, clientBeanID);
		contactID1 = contactAPI.createContactreturnBean(baseURL, headers, "ContactFirst", "ContactLast", "US", "(555) 555-5555", assignedUserID, clientBeanID);
		
		log.info("Creating Meeting");
		MeetingRestAPI meetingRestAPI = new MeetingRestAPI();
		meetingID = meetingRestAPI.createMeetingreturnBean(testConfig.getBrowserURL(), OAuthToken, assignedUserID);
		
		log.info("Creating opportunities");
		OpportunityRestAPI opportunityRestAPI = new OpportunityRestAPI();
		baseID = opportunityRestAPI.createOpportunityreturnBean(testConfig.getBrowserURL(), OAuthToken, "API created Base Oppty to test DELETE Opportunity", clientBeanID, contactID, "SLSP", "03", "2015-10-28", assignedUserID);
		opptyID = opportunityRestAPI.createOpportunityreturnBean(testConfig.getBrowserURL(), OAuthToken, "API created Base Oppty to test DELETE Opportunity", clientBeanID, contactID, "SLSP", "03", "2015-10-28", assignedUserID);

		opportunityID = opportunityRestAPI.createOpportunityreturnBean(testConfig.getBrowserURL(), OAuthToken, "API created Base Oppty to test DELETE Opportunity", clientBeanID, contactID, "SLSP", "03", "2015-10-28", assignedUserID);
		tempOpportunityID = opportunityRestAPI.createOpportunityreturnBean(testConfig.getBrowserURL(), OAuthToken, "API created Temp Oppty to test DELETE Opportunity", clientBeanID, contactID, "SLSP", "03", "2015-10-28", assignedUserID);
		
		favOpportunity = opportunityRestAPI.createOpportunityreturnBean(testConfig.getBrowserURL(), OAuthToken,"API created Favourite Oppty to test DELETE Opportunity", clientBeanID, contactID, "SLSP", "03", "2015-10-28", assignedUserID);
		
		subOpportunity = opportunityRestAPI.createOpportunityreturnBean(testConfig.getBrowserURL(), OAuthToken, "API created Subscribe Oppty to test DELETE Opportunity", clientBeanID, contactID, "SLSP", "03", "2015-10-28", assignedUserID);
		opportunityRestAPI.postOpportunity(getRequestUrl(subOpportunity+"/subscribe", null),OAuthToken,"", "200");
				
		opportunityCallID = opportunityRestAPI.createOpportunityreturnBean(testConfig.getBrowserURL(), OAuthToken, "API created Call Linked Oppty to test DELETE Opportunity", clientBeanID, contactID, "SLSP", "03", "2015-10-28", assignedUserID);
		opportunityNegCallID = opportunityRestAPI.createOpportunityreturnBean(testConfig.getBrowserURL(), OAuthToken, "API created Call Linked Oppty to test DELETE Opportunity", clientBeanID, contactID, "SLSP", "03", "2015-10-28", assignedUserID);
		Assert.assertTrue(opportunityRestAPI.linkRecordToOpportunityReturnBoolean(testConfig.getBrowserURL(), OAuthToken, opportunityCallID,callID,"Calls"));
		Assert.assertTrue(opportunityRestAPI.linkRecordToOpportunityReturnBoolean(testConfig.getBrowserURL(), OAuthToken, opportunityNegCallID,callID,"Calls"));
		
		opportunityNoteID = opportunityRestAPI.createOpportunityreturnBean(testConfig.getBrowserURL(), OAuthToken, "API created Note Linked Oppty to test DELETE Opportunity", clientBeanID, contactID, "SLSP", "03", "2015-10-28", assignedUserID);
		opportunityNegNoteID = opportunityRestAPI.createOpportunityreturnBean(testConfig.getBrowserURL(), OAuthToken, "API created Note Linked Oppty to test DELETE Opportunity", clientBeanID, contactID, "SLSP", "03", "2015-10-28", assignedUserID);
		Assert.assertTrue(opportunityRestAPI.linkRecordToOpportunityReturnBoolean(testConfig.getBrowserURL(), OAuthToken, opportunityNoteID,noteID,"Notes"));
		Assert.assertTrue(opportunityRestAPI.linkRecordToOpportunityReturnBoolean(testConfig.getBrowserURL(), OAuthToken, opportunityNegNoteID,noteID,"Notes"));
		
		opportunityTaskID = opportunityRestAPI.createOpportunityreturnBean(testConfig.getBrowserURL(), OAuthToken, "API created Task Linked Oppty to test DELETE Opportunity", clientBeanID, contactID, "SLSP", "03", "2015-10-28", assignedUserID);
		opportunityNegTaskID = opportunityRestAPI.createOpportunityreturnBean(testConfig.getBrowserURL(), OAuthToken, "API created Task Linked Oppty to test DELETE Opportunity", clientBeanID, contactID, "SLSP", "03", "2015-10-28", assignedUserID);
		Assert.assertTrue(opportunityRestAPI.linkRecordToOpportunityReturnBoolean(testConfig.getBrowserURL(), OAuthToken, opportunityTaskID,taskID,"Tasks"));
		Assert.assertTrue(opportunityRestAPI.linkRecordToOpportunityReturnBoolean(testConfig.getBrowserURL(), OAuthToken, opportunityNegTaskID,taskID,"Tasks"));
		
		opportunityMeetingID = opportunityRestAPI.createOpportunityreturnBean(testConfig.getBrowserURL(), OAuthToken, "API created Meeting Linked Oppty to test DELETE Opportunity", clientBeanID, contactID, "SLSP", "03", "2015-10-28", assignedUserID);
		opportunityNegMeetingID = opportunityRestAPI.createOpportunityreturnBean(testConfig.getBrowserURL(), OAuthToken, "API created Meeting Linked Oppty to test DELETE Opportunity", clientBeanID, contactID, "SLSP", "03", "2015-10-28", assignedUserID);
		Assert.assertTrue(opportunityRestAPI.linkRecordToOpportunityReturnBoolean(testConfig.getBrowserURL(), OAuthToken, opportunityMeetingID,meetingID,"Meetings"));
		Assert.assertTrue(opportunityRestAPI.linkRecordToOpportunityReturnBoolean(testConfig.getBrowserURL(), OAuthToken, opportunityNegMeetingID,meetingID,"Meetings"));
		
		opportunityOpptyID = opportunityRestAPI.createOpportunityreturnBean(testConfig.getBrowserURL(), OAuthToken, "API created Oppty Linked Oppty to test DELETE Opportunity", clientBeanID, contactID, "SLSP", "03", "2015-10-28", assignedUserID);
		opportunityNegOpptyID = opportunityRestAPI.createOpportunityreturnBean(testConfig.getBrowserURL(), OAuthToken, "API created Oppty Linked Oppty to test DELETE Opportunity", clientBeanID, contactID, "SLSP", "03", "2015-10-28", assignedUserID);
		Assert.assertTrue(opportunityRestAPI.linkRecordToOpportunityReturnBoolean(testConfig.getBrowserURL(), OAuthToken, opportunityOpptyID,opportunityID, "Opportunities"));
		Assert.assertTrue(opportunityRestAPI.linkRecordToOpportunityReturnBoolean(testConfig.getBrowserURL(), OAuthToken, opportunityNegOpptyID,opportunityID, "Opportunities"));

		log.info("Creating opportunities with RLI's");
		//line item values
		RevenueItem rli = new RevenueItem();
		rli.populate();
		rli.sL10_OfferingType = "B3000";rli.sL15_SubBrand = "PCO";rli.sL17_SegmentLine = "";rli.sL20_BrandCode = "B3U00";rli.sL30_ProductInformation = "B3U04";rli.sL40_MachineType = "5641NV3";
		
		opportunityRLIlID = opportunityRestAPI.createOpportunityWithRLIreturnBean(testConfig.getBrowserURL(), OAuthToken, "RLI Linked Oppty", clientBeanID, contactID, "SLSP", "03", date, assignedUserID, assignedUserName, rli);
		rliID = opportunityRestAPI.getRLIFromOppty(testConfig.getBrowserURL(), OAuthToken, opportunityRLIlID).get("id").toString();
		opportunityRestAPI.relateNewLineItemToOpportunity(testConfig.getBrowserURL(), OAuthToken, opportunityRLIlID, "500", "10", "", date, "B3000", "PCO", "", "B3U00", "B3U04", "5641NV3","-99", assignedUserName, assignedUserID);

		opportunityRLIlID1 = opportunityRestAPI.createOpportunityWithRLIreturnBean(testConfig.getBrowserURL(), OAuthToken, "RLI Linked Oppty", clientBeanID, contactID, "SLSP", "03", date, assignedUserID, assignedUserName, rli);
		rliID1 = opportunityRestAPI.getRLIFromOppty(testConfig.getBrowserURL(), OAuthToken, opportunityRLIlID1).get("id").toString();
		opportunityRestAPI.relateNewLineItemToOpportunity(testConfig.getBrowserURL(), OAuthToken, opportunityRLIlID1, "500", "10", "", date, "B3000", "PCO", "", "B3U00", "B3U04", "5641NV3","-99", assignedUserName, assignedUserID);

		opportunityNegRLIID = opportunityRestAPI.createOpportunityWithRLIreturnBean(testConfig.getBrowserURL(), OAuthToken, "RLI Linked Oppty", clientBeanID, contactID, "SLSP", "03", date, assignedUserID, assignedUserName, rli);
		opportunityRestAPI.getRLIFromOppty(testConfig.getBrowserURL(), OAuthToken, opportunityNegRLIID).get("id").toString();
		opportunityRestAPI.relateNewLineItemToOpportunity(testConfig.getBrowserURL(), OAuthToken, opportunityNegRLIID, "500", "10", "", date, "B3000", "PCO", "", "B3U00", "B3U04", "5641NV3","-99", assignedUserName, assignedUserID);

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
		this.addDataFile("test_config/extensions/api/opportunity/deleteOpportunity.csv");
		return testData.getAllDataRows();
    }
	
	@Test(dataProvider = "jsonProvider")
	public void TestDeleteOpportunity(HashMap<String,Object> parameterValues){
		Iterator<Map.Entry<String, Object>> it = parameterValues.entrySet().iterator();
        log.info("Getting user.");		
     	String expectedResponseCode = null;
     	String urlOpportunityID = null;
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
             else if (pairs.getKey().equalsIgnoreCase("opportunity")){
 				urlOpportunityID = geturlOpportunityID((String) pairs.getValue());
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
		if (!urlOpportunityID.equalsIgnoreCase("")) {
			url+=urlOpportunityID;
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
		OpportunityRestAPI opportunityrestAPI = new OpportunityRestAPI();
		String token = getOAuthToken(user);
		
		try{
			opportunityrestAPI.deleteOpportunity(getRequestUrl(url, null), token,"", expectedResponseCode);
		}
		catch (Exception e) {
			opportunityrestAPI.deleteOpportunity(getRequestUrl(url, null), token,"", expectedResponseCode);
		}
		
		log.info("Verify Response");
		if (expectedResponseCode.equalsIgnoreCase("200")) {
			if (!relatedObject.equalsIgnoreCase("")) {
				new OpportunityRestAPI().getOpportunity(getRequestUrl(url, null), new LoginRestAPI().getOAuth2Token(baseURL, user.getEmail(), user.getPassword()), "404");
				
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
			
		log.info("Creating contact");
		SugarAPI sugarAPI = new SugarAPI();
		String contactID2 = "Contact"+rand;			
		sugarAPI.createContact(testConfig.getBrowserURL(), contactID2, clientID, user.getEmail(), user.getPassword(), "ContactFirst", "ContactLast");
		
 		
 		log.info("Creating opportunities");
		OpportunityRestAPI opportunityRestAPI = new OpportunityRestAPI();
		String opportunity1 = opportunityRestAPI.createOpportunityreturnBean(testConfig.getBrowserURL(), OAuthToken, "API created Base Oppty to test MASS DELETE Opportunity", clientBeanID, contactID2, "SLSP", "03", "2015-10-28", assignedUserID);
		String opportunity2 = opportunityRestAPI.createOpportunityreturnBean(testConfig.getBrowserURL(), OAuthToken, "API created Base Oppty to test MASS DELETE Opportunity", clientBeanID, contactID2, "SLSP", "03", "2015-10-28", assignedUserID);
		String opportunity3 = opportunityRestAPI.createOpportunityreturnBean(testConfig.getBrowserURL(), OAuthToken, "API created Base Oppty to test MASS DELETE Opportunity", clientBeanID, contactID2, "SLSP", "03", "2015-10-28", assignedUserID);
		
		String body = "{\"massupdate_params\":{\"uid\":[\""+opportunity1+"\", \""+opportunity2+"\",\""+opportunity3+"\"]}}";
		String token3 = loginRestAPI.getOAuth2Token(baseURL, user.getEmail(), user.getPassword());	
		
		OpportunityRestAPI opportunityrestAPI = new OpportunityRestAPI();
		opportunityrestAPI.deleteOpportunity(getRequestUrl("/MassUpdate", null), getOAuthToken(user),body, "200");
 		
 		log.info("Check delete");
 		opportunityRestAPI.getOpportunity(getRequestUrl(opportunity1, null), token3, "404");
		opportunityRestAPI.getOpportunity(getRequestUrl(opportunity2, null), token3, "404");
		opportunityRestAPI.getOpportunity(getRequestUrl(opportunity3, null), token3, "404");
     }
     
     
     private String geturlOpportunityID(String csvOpportunity){
     	if (csvOpportunity.equalsIgnoreCase("base")) {
 			return baseID;
 		}
     	else if (csvOpportunity.equalsIgnoreCase("callOpportunity")) {
 			return opportunityCallID;
 		}
     	else if (csvOpportunity.equalsIgnoreCase("negcallOpportunity")) {
 			return opportunityNegCallID;
 		}
     	else if (csvOpportunity.equalsIgnoreCase("noteOpportunity")) {
 			return opportunityNoteID;
 		}
     	else if (csvOpportunity.equalsIgnoreCase("negnoteOpportunity")) {
 			return opportunityNegNoteID;
 		}
     	else if (csvOpportunity.equalsIgnoreCase("taskOpportunity")) {
 			return opportunityTaskID;
 		}
     	else if (csvOpportunity.equalsIgnoreCase("negtaskOpportunity")) {
 			return opportunityNegTaskID;
 		}
     	else if (csvOpportunity.equalsIgnoreCase("meetingOpportunity")) {
 			return opportunityMeetingID;
 		}
     	else if (csvOpportunity.equalsIgnoreCase("negmeetingOpportunity")) {
 			return opportunityNegMeetingID;
 		}
     	else if (csvOpportunity.equalsIgnoreCase("rliOpportunity")) {
 			return opportunityRLIlID;
 		}
     	else if (csvOpportunity.equalsIgnoreCase("rliOpportunity1")) {
 			return opportunityRLIlID1;
 		}
     	else if (csvOpportunity.equalsIgnoreCase("negrliOpportunity")) {
 			return opportunityNegRLIID;
 		}
     	else if (csvOpportunity.equalsIgnoreCase("opptyOpportunity")) {
 			return opportunityOpptyID;
 		}
     	else if (csvOpportunity.equalsIgnoreCase("negopptyOpportunity")) {
 			return opportunityNegOpptyID;
 		}
     	else if (csvOpportunity.equalsIgnoreCase("tempOpportunity")) {
 			return tempOpportunityID;
 		}
     	else if (csvOpportunity.equalsIgnoreCase("deleted")) {
 			return deletedID;
 		}
     	else if (csvOpportunity.equalsIgnoreCase("favOpportunity")) {
 			return favOpportunity;
 		}
     	else if (csvOpportunity.equalsIgnoreCase("subOpportunity")) {
 			return subOpportunity;
 		}
     	else if (csvOpportunity.equalsIgnoreCase("*blank*")) {
 			return "";
 		}
     	else if (csvOpportunity.contains("invalid")) {
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
 			return contactID;
 		}
     	else if (relatedFromCSV.equalsIgnoreCase("validNote")) {
 			return noteID;
 		}
     	else if (relatedFromCSV.equalsIgnoreCase("validOpportunity")) {
 			return opptyID;
 		}
     	else if (relatedFromCSV.equalsIgnoreCase("validTask")) {
 			return taskID;
 		}
     	else if (relatedFromCSV.equalsIgnoreCase("validRLI")) {
 			return rliID;
 		}
     	else if (relatedFromCSV.equalsIgnoreCase("validRLI1")) {
 			return rliID1;
 		}
     	else if (relatedFromCSV.equalsIgnoreCase("validMeeting")) {
 			return meetingID;
 		}
     	else if (relatedFromCSV.equalsIgnoreCase("validCCMS")) {
 			return clientID;
 		}
     	else if (relatedFromCSV.equalsIgnoreCase("validExtRefID")) {
 			return contactID1;
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