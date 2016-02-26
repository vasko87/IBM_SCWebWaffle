/**
 * 
 */
package com.ibm.salesconnect.test.api.leads;


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
import com.ibm.salesconnect.API.EmailRestAPI;
import com.ibm.salesconnect.API.LeadsRestAPI;
import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.API.MeetingRestAPI;
import com.ibm.salesconnect.API.NoteRestAPI;
import com.ibm.salesconnect.API.SugarAPI;
import com.ibm.salesconnect.API.TestDataHolder;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.test.api.tasks.GETclientAndsecret;
import com.ibm.salesconnect.test.mobile.TaskRestAPITests;
/**
 * @author timlehane
 * @date Jan 26, 2015
 */
public class GETleads extends ApiBaseTest {
	private static final Logger log = LoggerFactory.getLogger(GETleads.class);
	
	@Parameters({ "apiExtension", "applicationName", "APIM", "apim_environment" })
	private GETleads(@Optional("leads") String apiExtension,
			@Optional("SC Auto read") String applicationName,
			@Optional("true") String APIM,
			@Optional("development") String environment) {

		super(apiExtension, applicationName, APIM, environment);

	}

	private Boolean APIm;
	static GETclientAndsecret callMethGET = new GETclientAndsecret();
	private TestDataHolder testData;
    int rand = new Random().nextInt(100000);
	private String contactID = "22SC-" + rand;
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
	private String accountID = null;
	private String baseID = null;
	private String unlinkedID = null;
	private String deletedID = null;
	private String DACHLead = null;
	private String EULead = null;
	private String meetingID = null;
	private String emailID = null;

	
	private void createObjects()
	{
		log.info("Start creating prerequisites");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
   		String userID = new APIUtilities().getUserBeanIDFromEmail(baseURL, user1.getEmail(), user1.getPassword());
		String baseURL = testConfig.getBrowserURL();
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
		CIContactID = contactBeanID;
		
		
		log.info("Creating oppty");
		opptyID = sugarAPI.createOppty(baseURL, "", contactID, accountID, user1.getEmail(), user1.getPassword(), GC.emptyArray);
		
		log.info("Creating note");
		NoteRestAPI noteAPI = new NoteRestAPI();
		noteID = noteAPI.createNotereturnBean(testConfig.getBrowserURL(), OAuthToken, noteSubject, "post task note description", assignedUserID);
		
		log.info("Creating call");
		CallRestAPI callRestAPI = new CallRestAPI();
		callID = callRestAPI.createCallreturnBean(testConfig.getBrowserURL(), OAuthToken, callSubject, "2013-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);
		
		log.info("Creating task");
		taskID = TaskRestAPITests.createTaskHelper(user1,"Base task",log,baseURL);
		
		log.info("Creating meeting");
		MeetingRestAPI meetingRestAPI = new MeetingRestAPI();
		meetingID = meetingRestAPI.createMeetingreturnBean(testConfig.getBrowserURL(), OAuthToken, assignedUserID);
		
		log.info("Creating Email");
		EmailRestAPI emailRestAPI = new EmailRestAPI();
		emailID = emailRestAPI.createEmailreturnBean(testConfig.getBrowserURL(), OAuthToken);
		
		log.info("Creating lead");
		LeadsRestAPI leadRestAPI = new LeadsRestAPI();
		baseID = leadRestAPI.createLeadreturnBean(testConfig.getBrowserURL(), OAuthToken, "lead Name", "lead Description", userID);
		unlinkedID = leadRestAPI.createLeadreturnBean(testConfig.getBrowserURL(), OAuthToken, "lead Name", "lead Description", userID);
		deletedID = leadRestAPI.createLeadreturnBean(testConfig.getBrowserURL(), OAuthToken, "lead Name", "lead Description",userID);
		DACHLead = leadRestAPI.createLeadreturnBean(testConfig.getBrowserURL(), OAuthToken, "lead Name", "lead Description", "DE", user1);
		EULead = leadRestAPI.createLeadreturnBean(testConfig.getBrowserURL(), OAuthToken, "lead Name", "lead Description", "FR", user1);
		leadRestAPI.deleteLead(testConfig.getBrowserURL() + "rest/v10/Leads" + "/" + deletedID, OAuthToken, "200");
		
		Assert.assertTrue(leadRestAPI.linkLeadtoObject(testConfig.getBrowserURL(), OAuthToken, baseID,"accounts",clientBeanID));
		Assert.assertTrue(leadRestAPI.linkLeadtoObject(testConfig.getBrowserURL(), OAuthToken, baseID,"calls",callID));
		Assert.assertTrue(leadRestAPI.linkLeadtoObject(testConfig.getBrowserURL(), OAuthToken, baseID,"contacts",contactBeanID));
		Assert.assertTrue(leadRestAPI.linkLeadtoObject(testConfig.getBrowserURL(), OAuthToken, baseID,"notes",noteID));
		Assert.assertTrue(leadRestAPI.linkLeadtoObject(testConfig.getBrowserURL(), OAuthToken, baseID,"opportunities",opptyID));
		Assert.assertTrue(leadRestAPI.linkLeadtoObject(testConfig.getBrowserURL(), OAuthToken, baseID,"tasks",taskID));
		Assert.assertTrue(leadRestAPI.linkLeadtoObject(testConfig.getBrowserURL(), OAuthToken, baseID,"meetings",meetingID));
		Assert.assertTrue(leadRestAPI.linkLeadtoObject(testConfig.getBrowserURL(), OAuthToken, baseID,"emails",emailID));
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
    	this.addDataFile("test_config/extensions/api/lead/getLead.csv");
    	//Return an array of arrays where each item in the array is a HashMap of parameter values
    	//Test content
       return testData.getAllDataRows();
    }
    
    @Test(dataProvider = "DataProvider")
    public void LeadsGet(HashMap<String,Object> parameterValues)
    {
    
    	String expectedResponseCode = null;
    	String urlLeadID = null;
    	String urlExtension = null;
    	String relatedObject = null;
    	String externalExtension = null;
    	User user = null;
        Iterator<Map.Entry<String, Object>> it = parameterValues.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> pairs = (Map.Entry<String, Object>)it.next();
            
            if(pairs.getKey().equals("expectedResponse"))
            {
            	expectedResponseCode=pairs.getValue().toString();
            	it.remove();
            }
            else if (pairs.getKey().equals("TC_Name")) 
            {
				log.info("This is test " + pairs.getValue().toString());
			}
            else if (pairs.getKey().equalsIgnoreCase("lead")){
				urlLeadID = geturlLeadID(pairs.getValue().toString());
			}
            else if (pairs.getKey().equalsIgnoreCase("urlextension"))
            {
				if (pairs.getValue().toString().equalsIgnoreCase("*blank*")) 
				{
					urlExtension = "";
				}
				else 
				{
					urlExtension = pairs.getValue().toString();
				}
			}
            else if (pairs.getKey().equalsIgnoreCase("related")) {
				if (pairs.getValue().toString().equalsIgnoreCase("*blank*")) 
				{
					relatedObject = "";
				}
				else
				{
					relatedObject = getRelatedObjectID(pairs.getValue().toString());
				}
			}
            else if (pairs.getKey().equalsIgnoreCase("externalExtension")) 
            {
				if (pairs.getValue().toString().equalsIgnoreCase("*blank*"))
				{
					externalExtension = "";
				}
				else {
					externalExtension = pairs.getValue().toString();
				}
			}
            else if (pairs.getKey().equalsIgnoreCase("user")) 
            {
				if (pairs.getValue().toString().equalsIgnoreCase("same"))
				{
					user = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
				}
				else {
					user = commonUserAllocator.getGroupUser("us_users", this);
				}
				
			}
        }			
		
		//Build request URL
		String url = "";
		if (!urlLeadID.equalsIgnoreCase(""))
		{
			url+=urlLeadID;
		}
		if (!urlExtension.equalsIgnoreCase(""))
		{
			url+=urlExtension;
		}
		if (!relatedObject.equalsIgnoreCase("")) 
		{
			url+="/";
			url+=relatedObject;
		}
		if (!externalExtension.equalsIgnoreCase(""))
		{
			url+=externalExtension;
		}		
		
		log.info("Getting oauth token");
		String token = getOAuthToken(user);
		String requestURL = getRequestUrl(url, null);
		if (requestURL.contains("//count")) {
			requestURL = requestURL.replace("//count", "/count");
		}
		log.info("Sending GET request");
		LeadsRestAPI leadrestAPI = new LeadsRestAPI();
		String jsonString = leadrestAPI.getLead(requestURL, token, expectedResponseCode);
		
		
		//Verify response
		if (expectedResponseCode.equalsIgnoreCase("200")) 
		{
			if (!relatedObject.equalsIgnoreCase("")) 
			{
				Assert.assertTrue((new APIUtilities().checkIfValuePresentInJson(jsonString, "id", relatedObject)) || new APIUtilities().checkIfValuePresentInJson(jsonString, "id", clientBeanID));
			}
			else if ((!urlExtension.equalsIgnoreCase("")) && (!relatedObject.equalsIgnoreCase(""))) 
			{
				String module = urlExtension.substring(6);
				Assert.assertTrue(new APIUtilities().checkIfValueContainedInJson(jsonString, "_module", module));
			}
			else if ((!urlExtension.equalsIgnoreCase("")) && (!relatedObject.equalsIgnoreCase("")) && (!urlLeadID.equalsIgnoreCase(""))) 
			{
				Assert.assertTrue(new APIUtilities().returnValuePresentInJson(jsonString, "next_offset").equalsIgnoreCase(Integer.toString(-1)));
			}
			else if (urlExtension.contains("/count")) 
			{
				Assert.assertTrue(Integer.parseInt(new APIUtilities().returnValuePresentInJson(jsonString, "record_count"))>0);
			}
		}	
    }
    
    @Test
    public void convertThenGET(){
   	 
   	   	log.info("Getting User");
       	User user = commonUserAllocator.getUser(this);
   		String userID = new APIUtilities().getUserBeanIDFromEmail(baseURL, user.getEmail(), user.getPassword());
    	log.info("Getting token");
    	
   		LoginRestAPI loginRestAPI = new LoginRestAPI();
   		String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user.getEmail(), user.getPassword());
   		
    	log.info("Creating lead");
   		LeadsRestAPI leadRestAPI = new LeadsRestAPI();
   		String lead = leadRestAPI.createLeadreturnBean(testConfig.getBrowserURL(), OAuthToken, "lead Name", "lead Description", userID);
   		
		log.info("Sending Post Convert request");
		String json ="{\"modules\": {\"Contacts\": {\"deleted\": 0,\"do_not_call\": false,\"portal_active\": 0,\"preferred_language\": \"en_us\","+
			          "\"first_name\": \"one\",\"last_name\": \"smith\",\"title\": \"Director Operations\",\"primary_address_country\":\"US\", \"lead_source\":\"LSCAMP\"}}}";
			
		leadRestAPI.postLead(baseURL + "rest/v10/Leads/" + lead + "/convert", OAuthToken,json, "200");

		
		String token = getOAuthToken(user);
		String requestURL = getRequestUrl("/" + lead, null);
		LeadsRestAPI leadrestAPI = new LeadsRestAPI();
		leadrestAPI.getLead(requestURL, token, "200");
    }
    
    @Test
    public void GETMyItems(){
   	 
   	   	log.info("Getting User");
       	User user = commonUserAllocator.getUser(this);
   		String userID = new APIUtilities().getUserBeanIDFromEmail(baseURL, user.getEmail(), user.getPassword());
    	log.info("Getting token");
    	
   		LoginRestAPI loginRestAPI = new LoginRestAPI();
   		String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user.getEmail(), user.getPassword());
   		
    	log.info("Creating lead");
   		LeadsRestAPI leadRestAPI = new LeadsRestAPI();
   		String lead = leadRestAPI.createLeadreturnBean(testConfig.getBrowserURL(), OAuthToken, "lead Name", "lead Description", userID);
   		
    	ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("my_items","1"));
    	params.add(new BasicNameValuePair("fields","id"));
    	params.add(new BasicNameValuePair("view","list"));

   		String jsonString = leadRestAPI.getLead(getRequestUrl(null, params), getOAuthToken(user), "200");
   		System.out.println(new APIUtilities().returnValuePresentInJson(jsonString, "id"));
   		System.out.println(lead);
   		Assert.assertTrue(new APIUtilities().checkIfValueContainedInJson(jsonString, "id", lead));
   		

		
    }
    
    /**
     * Get the oauth string
     */
	public String getOAuthExtension(){		
		return "oauth?" + clientIDandSecret;
	}
    
    
    /**
     * return the lead id for the request url based on the value in the lead column from the csv file
     * @param csvLead value form the lead column in the csv
     * @return url Lead ID
     */
    private String geturlLeadID(String csvLead){
    	if (csvLead.equalsIgnoreCase("base")) {
			return baseID;
		}
    	else if (csvLead.equalsIgnoreCase("unlinked")) {
			return unlinkedID;
		}
    	else if (csvLead.equalsIgnoreCase("deleted")) {
			return deletedID;
		}
    	else if (csvLead.equalsIgnoreCase("*blank*")) {
			return "";
		}
    	else if (csvLead.equalsIgnoreCase("DACHLead")) {
			return DACHLead;
		}
    	else if (csvLead.equalsIgnoreCase("EULead")) {
			return EULead;
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
    	else if (relatedFromCSV.equalsIgnoreCase("validMeeting")) {
			return meetingID;
		}
    	else if (relatedFromCSV.equalsIgnoreCase("validEmail")) {
			return emailID;
		}
    	else if (relatedFromCSV.equalsIgnoreCase("validCCMS")) {
			return accountID;
		}
    	else if (relatedFromCSV.equalsIgnoreCase("validExtRefID")) {
			return CIContactID;
		}
    	else if (relatedFromCSV.equalsIgnoreCase("invalid")) {
			return "invalidID";
		}
    	else if (relatedFromCSV.equalsIgnoreCase("*blank*")) {
			return "";
		}
    	return null;
    }
    
}
