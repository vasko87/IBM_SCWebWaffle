/**
 * 
 */
package com.ibm.salesconnect.test.api.calls;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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
import com.ibm.salesconnect.API.LeadsRestAPI;
import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.API.NoteRestAPI;
import com.ibm.salesconnect.API.OpportunityRestAPI;
import com.ibm.salesconnect.API.TestDataHolder;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.test.api.tasks.GETclientAndsecret;
import com.ibm.salesconnect.test.mobile.TaskRestAPITests;
/**
 * @author evafarrell
 * @date Oct 8, 2015
 */
public class GETCalls extends ApiBaseTest {
	private static final Logger log = LoggerFactory.getLogger(GETCalls.class);
	
	@Parameters({ "apiExtension", "applicationName", "APIM", "apim_environment" })
	private GETCalls(@Optional("calls") String apiExtension,
			@Optional("SC Auto read") String applicationName,
			@Optional("true") String APIM,
			@Optional("development") String environment) {

		super(apiExtension, applicationName, APIM, environment);

	}

	static GETclientAndsecret callMethGET = new GETclientAndsecret();
	private TestDataHolder testData;
    int rand = new Random().nextInt(100000);
	private String contactID = "22SC-" + rand;
	//private String contactBeanID = null;
	private String noteID = null;
	private String taskID = null;
	private String noteSubject = "post task note subject";
	private String callID = null;
	private String clientBeanID = null;
	private String assignedUserID = null;
	private String assignedUserName = null;
	private String accountID = null;
	private String baseID = null;
	private String unlinkedID = null;
	private String deletedID = null;
	private String opportunityID = null;
	private String assignedUserID2;
	private String assignedUserName2;
	private String leadID;
	private String assignedUserID3;
	
	
	private void createObjects()
	{
		log.info("Start creating prerequisites");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
		User user2 = commonUserAllocator.getUser(this);
		User user3 = commonUserAllocator.getUser(this);
		
		PoolClient poolClient= commonClientAllocator.getGroupClient(GC.SC,this);
		String baseURL = testConfig.getBrowserURL();
		
		log.info("Getting user details");
		assignedUserID = new APIUtilities().getUserBeanIDFromEmail(baseURL, user1.getEmail(), user1.getPassword());	
		assignedUserName = user1.getDisplayName();
		assignedUserID2 = new APIUtilities().getUserBeanIDFromEmail(baseURL, user2.getEmail(), user2.getPassword());	
		assignedUserName2 = user2.getDisplayName();
		assignedUserID3 = new APIUtilities().getUserBeanIDFromEmail(baseURL, user3.getEmail(), user3.getPassword());

		log.info("Getting client");
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user1.getEmail(), user1.getPassword());
		String headers[] = {"OAuth-Token", OAuthToken};
		accountID = poolClient.getCCMS_ID();
		clientBeanID = new CollabWebAPI().getbeanIDfromClientID(baseURL, accountID, headers );
		
		log.info("Creating contact");
		ContactRestAPI contactAPI = new ContactRestAPI();
		contactID = contactAPI.createContactreturnBean(baseURL, headers, "ContactFirst", "ContactLast", "US", "(555) 555-5555", assignedUserID);
				
		log.info("Creating note");
		NoteRestAPI noteAPI = new NoteRestAPI();
		noteID = noteAPI.createNotereturnBean(testConfig.getBrowserURL(), OAuthToken, noteSubject, "post task note description", assignedUserID);
				
		log.info("Creating task");
		taskID = TaskRestAPITests.createTaskHelper(user1,"Base task",log,baseURL);
		
		log.info("Creating Opportunity");
   		OpportunityRestAPI opportunityRestAPI = new OpportunityRestAPI();
   		opportunityID = opportunityRestAPI.createOpportunityreturnBean(testConfig.getBrowserURL(), OAuthToken, "BVT API created Oppty to GET Later", clientBeanID, contactID, "SLSP", "03", "2015-10-28", assignedUserID);
  
		log.info("Creating lead");
		LeadsRestAPI leadRestAPI = new LeadsRestAPI();
		leadID = leadRestAPI.createLeadreturnBean(testConfig.getBrowserURL(), OAuthToken, "lead Name", "lead Description", assignedUserID);
		
		log.info("Creating calls");
		CallRestAPI callRestAPI = new CallRestAPI();
		baseID = callRestAPI.createCallreturnBean(testConfig.getBrowserURL(), OAuthToken, "BVT API created Base Call to GET Later", "2016-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);
   		unlinkedID = callRestAPI.createCallreturnBean(testConfig.getBrowserURL(), OAuthToken, "BVT API created Call Unlinked to Get Later", "2016-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);
		deletedID = callRestAPI.createCallreturnBean(testConfig.getBrowserURL(), OAuthToken, "BVT API created Call to Delete and attempt to GET Later", "2016-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);
		callRestAPI.deleteCall(testConfig.getBrowserURL(), OAuthToken, deletedID);
		
		Assert.assertTrue(callRestAPI.linkRecordToCallReturnBoolean(testConfig.getBrowserURL(), OAuthToken, baseID, contactID, "Contacts"));
		Assert.assertTrue(callRestAPI.linkRecordToCallReturnBoolean(testConfig.getBrowserURL(), OAuthToken, baseID, noteID, "Notes"));
		Assert.assertTrue(callRestAPI.linkRecordToCallReturnBoolean(testConfig.getBrowserURL(), OAuthToken, baseID, taskID, "Tasks"));
		Assert.assertTrue(callRestAPI.linkRecordToCallReturnBoolean(testConfig.getBrowserURL(), OAuthToken, baseID, opportunityID, "Opportunities"));
		Assert.assertTrue(callRestAPI.linkRecordToCallReturnBoolean(testConfig.getBrowserURL(), OAuthToken, baseID,leadID, "Leads"));
		Assert.assertTrue(callRestAPI.linkRecordToCallReturnBoolean(testConfig.getBrowserURL(), OAuthToken, baseID,assignedUserID2, "AdditionalAssignees"));
		Assert.assertTrue(callRestAPI.linkRecordToCallReturnBoolean(testConfig.getBrowserURL(), OAuthToken, baseID,assignedUserID2, "AssignedUser"));
		Assert.assertTrue(callRestAPI.linkRecordToCallReturnBoolean(testConfig.getBrowserURL(), OAuthToken, baseID,assignedUserID2, "Users"));
		
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
    	this.addDataFile("test_config/extensions/api/call/getCall.csv");
    	//Return an array of arrays where each item in the array is a HashMap of parameter values
    	//Test content
       return testData.getAllDataRows();
    }
    
    @Test(dataProvider = "DataProvider")
    public void OpportunitiesGet(HashMap<String,Object> parameterValues)
    {
    
    	String expectedResponseCode = null;
    	String urlCallID = null;
    	String urlExtension = null;
    	String relatedObject = null;
    	String externalExtension = null;
    	User user = null;
        Iterator<Map.Entry<String, Object>> it = parameterValues.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> pairs = (Map.Entry<String, Object>)it.next();
            System.out.println(pairs.getKey());
            System.out.println(pairs.getValue());
            if(pairs.getKey().equals("expectedResponse"))
            {
            	expectedResponseCode=pairs.getValue().toString();
            	it.remove();
            }
            else if (pairs.getKey().equals("TC_Name")) 
            {
				log.info("This is test " + pairs.getValue().toString());
			}
            else if (pairs.getKey().equalsIgnoreCase("call")){
				urlCallID = geturlCallID(pairs.getValue().toString());
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
		if (!urlCallID.equalsIgnoreCase(""))
		{
			url+=urlCallID;
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
		
		CallRestAPI callRestAPI = new CallRestAPI();
		String jsonString = callRestAPI.getCall(requestURL, token, expectedResponseCode);
		
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
			else if ((!urlExtension.equalsIgnoreCase("")) && (!relatedObject.equalsIgnoreCase("")) && (!urlCallID.equalsIgnoreCase(""))) 
			{
				Assert.assertTrue(new APIUtilities().returnValuePresentInJson(jsonString, "next_offset").equalsIgnoreCase(Integer.toString(-1)));
			}
			else if (urlExtension.contains("/count")) 
			{
				Assert.assertTrue(Integer.parseInt(new APIUtilities().returnValuePresentInJson(jsonString, "record_count"))>0);
			}
		}	
    }

    /**
     * return the call id for the request url based on the value in the call column from the csv file
     * @param csvCall value form the call column in the csv
     * @return url Call ID
     */
    private String geturlCallID(String csvCall){
    	if (csvCall.equalsIgnoreCase("base")) {
			return baseID;
		}
    	else if (csvCall.equalsIgnoreCase("unlinked")) {
			return unlinkedID;
		}
    	else if (csvCall.equalsIgnoreCase("deleted")) {
			return deletedID;
		}
    	else if (csvCall.equalsIgnoreCase("*blank*")) {
			return "";
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
    	else if (relatedFromCSV.equalsIgnoreCase("validNote")) {
			return noteID;
		}
    	else if (relatedFromCSV.equalsIgnoreCase("validOppty")) {
			return opportunityID;
		}
    	else if (relatedFromCSV.equalsIgnoreCase("validTask")) {
			return taskID;
		}
    	else if (relatedFromCSV.equalsIgnoreCase("validLead")) {
			return leadID;
		}
    	else if (relatedFromCSV.equalsIgnoreCase("validContact")) {
			return contactID;
		}
    	else if (relatedFromCSV.equalsIgnoreCase("validUser")) {
			return assignedUserID2;
		}
    	else if (relatedFromCSV.equalsIgnoreCase("validAssignedUser")) {
			return assignedUserID2;
		}
    	else if (relatedFromCSV.equalsIgnoreCase("validNotAssignedUser")) {
			return assignedUserID3;
		}
    	else if (relatedFromCSV.equalsIgnoreCase("validAdditionalAssignee")) {
			return assignedUserID2;
		}
    	else if (relatedFromCSV.equalsIgnoreCase("invalid")) {
			return "invalidID";
		}
    	else if (relatedFromCSV.equalsIgnoreCase("*blank*")) {
			return "";
		}
    	return null;
    }

	@Test
	public void GetCallfavourites(){
		log.info("Start test GetCallfavourites");
		User user = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);

		log.info("Getting token");
    	LoginRestAPI loginRestAPI = new LoginRestAPI();
   		String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user.getEmail(), user.getPassword());
   		
   		CallRestAPI callRestAPI = new CallRestAPI();
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("favorites","1"));
   		String response = callRestAPI.getCall(getRequestUrl(null, params), OAuthToken, "200");
   		
		JSONObject postResponse = null;
		try {
			postResponse = (JSONObject)new JSONParser().parse(response);
		} catch (ParseException e5) {
			e5.printStackTrace();
		}
		
		JSONArray jsonArray = null;
		try {
			jsonArray = (JSONArray) postResponse.get("records");
		} catch (Exception e) {
			log.info("Token was not returned as expected");
			e.printStackTrace();
		}

		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			Assert.assertEquals(jsonObject.get("my_favorite"), true);

		}

		log.info("End test GetCallfavourites");
	}
	
	@Test
	public void GetCallMyItems(){
		log.info("Start test GetCallMyItems");
		User user = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);

		log.info("Getting token");
    	LoginRestAPI loginRestAPI = new LoginRestAPI();
   		String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user.getEmail(), user.getPassword());
   		
   		CallRestAPI callRestAPI = new CallRestAPI();
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("my_items","1"));
   		String response = callRestAPI.getCall(getRequestUrl(null, params), OAuthToken, "200");
   		
		JSONObject postResponse = null;
		try {
			postResponse = (JSONObject)new JSONParser().parse(response);
		} catch (ParseException e5) {
			e5.printStackTrace();
		}
		
		JSONArray jsonArray = null;
		try {
			jsonArray = (JSONArray) postResponse.get("records");
		} catch (Exception e) {
			log.info("Token was not returned as expected");
			e.printStackTrace();
		}

		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			String temp = (String) jsonObject.get("assigned_user_name");
			System.out.println("************************** JSON assigned_user_name is "+temp );
			System.out.println("************************** User FirstName is "+user.getFirstName());
			Assert.assertTrue(temp.contains(user.getFirstName()));
		}
		
		log.info("End test GetCallMyItems");
	}

	@Test
	public void GetCallMyItemsFavorites(){
		log.info("Start test GetCallMyItemsFavorites");
		User user = commonUserAllocator.getUser(this);

		log.info("Getting token");
    	LoginRestAPI loginRestAPI = new LoginRestAPI();
   		String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user.getEmail(), user.getPassword());
   		
   		CallRestAPI callRestAPI = new CallRestAPI();
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("my_items","1"));
    	params.add(new BasicNameValuePair("favorites","1"));
   		String response = callRestAPI.getCall(getRequestUrl(null, params), OAuthToken, "200");

		JSONObject postResponse = null;
		try {
			postResponse = (JSONObject)new JSONParser().parse(response);
		} catch (ParseException e5) {
			e5.printStackTrace();
		}
		
		JSONArray jsonArray = null;
		try {
			jsonArray = (JSONArray) postResponse.get("records");
		} catch (Exception e) {
			log.info("Token was not returned as expected");
			e.printStackTrace();
		}

		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			Assert.assertEquals(jsonObject.get("my_favorite"), true);
			String temp = (String) jsonObject.get("assigned_user_name");
			Assert.assertTrue(temp.contains(user.getFirstName()));
		}
		
		log.info("End test GetCallMyItemsFavorites");
	}
	   
		
		
	@Test
	public void GetCallMyItemsFavoritesOrderBy(){
		log.info("Start test GetCallMyItemsFavoritesOrderBy");
		User user = commonUserAllocator.getUser(this);

		log.info("Getting token");
    	LoginRestAPI loginRestAPI = new LoginRestAPI();
   		String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user.getEmail(), user.getPassword());
   		
   		CallRestAPI callRestAPI = new CallRestAPI();
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("my_items","1"));
    	params.add(new BasicNameValuePair("favorites","1"));
    	params.add(new BasicNameValuePair("order_by","description:Desc"));
    	params.add(new BasicNameValuePair("fields","description,date_entered"));
   		String response = callRestAPI.getCall(getRequestUrl(null, params), OAuthToken, "200");

		JSONObject postResponse = null;
		try {
			postResponse = (JSONObject)new JSONParser().parse(response);
		} catch (ParseException e5) {
			e5.printStackTrace();
		}
		
		JSONArray jsonArray = null;
		try {
			jsonArray = (JSONArray) postResponse.get("records");
		} catch (Exception e) {
			log.info("Token was not returned as expected");
			e.printStackTrace();
		}

		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			Assert.assertEquals(jsonObject.get("my_favorite"), true);
			String temp = (String) jsonObject.get("assigned_user_name");
			Assert.assertTrue(temp.contains(user.getFirstName()));
			Assert.assertFalse(response.contains("name"));
			Assert.assertTrue(response.contains("date_entered"));
			Assert.assertTrue(response.contains("description"));
		}
		
		log.info("End test GetCallMyItemsFavoritesOrderBy");
	}
	@Test
	public void GetCallFilterSearch(){
		log.info("Start test GetCallFilterSearch");
		User user = commonUserAllocator.getUser(this);

		log.info("Getting token");
    	LoginRestAPI loginRestAPI = new LoginRestAPI();
   		String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user.getEmail(), user.getPassword());

   		CallRestAPI callRestAPI = new CallRestAPI();
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("filter[0][$or][0][id][$starts]","3"));
    	params.add(new BasicNameValuePair("filter[0][$or][1][id][$starts]","R"));
    	params.add(new BasicNameValuePair("filter[1][$or][0][description][$starts]","API"));
    	params.add(new BasicNameValuePair("filter[1][$or][1][description][$starts]","BVT"));
    	params.add(new BasicNameValuePair("fields","name,description"));
   		String response = callRestAPI.getCall(getRequestUrl(null, params), OAuthToken, "200");

		JSONObject postResponse = null;
		try {
			postResponse = (JSONObject)new JSONParser().parse(response);
		} catch (ParseException e5) {
			e5.printStackTrace();
		}
		
		JSONArray jsonArray = null;
		try {
			jsonArray = (JSONArray) postResponse.get("records");
		} catch (Exception e) {
			log.info("Token was not returned as expected");
			e.printStackTrace();
		}

		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			Assert.assertFalse(response.contains("date_entered"));
			Assert.assertTrue(response.contains("name"));
			Assert.assertTrue(response.contains("description"));
			Assert.assertTrue(response.contains("API"));
			Assert.assertTrue(response.contains("BVT"));
		}
		
		log.info("End test GetCallFilterSearch");
	}
	
	@Test
	public void GetCallMyItemsOneField(){
		log.info("Start test GetCallMyItemsOneField");
		User user = commonUserAllocator.getUser(this);

		log.info("Getting token");
    	LoginRestAPI loginRestAPI = new LoginRestAPI();
   		String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user.getEmail(), user.getPassword());
   		
   		CallRestAPI callRestAPI = new CallRestAPI();
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("my_items","1"));
    	params.add(new BasicNameValuePair("fields","date_entered"));
   		String response = callRestAPI.getCall(getRequestUrl(null, params), OAuthToken, "200");
   		
   		Assert.assertFalse(response.contains("modified_user_id"));
		Assert.assertTrue(response.contains("date_entered"));
		
		log.info("End test GetCallMyItemsOneField");
	}
	
	@Test
	public void GetCallMyItemsTwoFields(){
		log.info("Start test GetCallMyItemsTwoFields");
		User user = commonUserAllocator.getUser(this);
		
		log.info("Getting token");
    	LoginRestAPI loginRestAPI = new LoginRestAPI();
   		String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user.getEmail(), user.getPassword());
   		
   		CallRestAPI callRestAPI = new CallRestAPI();
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("my_items","1"));
    	params.add(new BasicNameValuePair("fields","date_entered,modified_user_id"));
    	String response = callRestAPI.getCall(getRequestUrl(null, params), OAuthToken, "200");

		Assert.assertFalse(response.contains("modified_by_name"));
		Assert.assertTrue(response.contains("date_entered"));
		Assert.assertTrue(response.contains("modified_user_id"));
		
		log.info("End test GetCallMyItemsTwoFields");
	}   
    
}