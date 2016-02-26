/**
 * 
 */
package com.ibm.salesconnect.test.api.opportunities;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.API.MeetingRestAPI;
import com.ibm.salesconnect.API.NoteRestAPI;
import com.ibm.salesconnect.API.OpportunityRestAPI;
import com.ibm.salesconnect.API.TestDataHolder;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.objects.RevenueItem;
import com.ibm.salesconnect.test.api.tasks.GETclientAndsecret;
import com.ibm.salesconnect.test.mobile.TaskRestAPITests;
/**
 * @author evafarrell
 * @date Jul 17, 2015
 */
public class GETOpportunities extends ApiBaseTest {
	private static final Logger log = LoggerFactory.getLogger(GETOpportunities.class);
	
	@Parameters({ "apiExtension", "applicationName", "APIM", "apim_environment" })
	private GETOpportunities(@Optional("opportunities") String apiExtension,
			@Optional("SC Auto read") String applicationName,
			@Optional("true") String APIM,
			@Optional("development") String environment) {

		super(apiExtension, applicationName, APIM, environment);

	}

	static GETclientAndsecret callMethGET = new GETclientAndsecret();
	private TestDataHolder testData;
    int rand = new Random().nextInt(100000);
	private String contactID = null;
	private String contactID1 = null;
	private String contactID2 = null;
	private String opptyID = null;
	private String noteID = null;
	private String taskID = null;
	private String noteSubject = "post task note subject";
	private String callID = null;
	private String callSubject = "post task call subject";
	private String clientBeanID = null;
	private String assignedUserID = null;
	private String assignedUserName = null;
	private String accountID = null;
	private String baseID = null;
	private String unlinkedID = null;
	private String deletedID = null;
	private String meetingID = null;
	private String rliID = null;
	private String opportunityID = null;
	private String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString();
	private String rliOpptyID;
	
	
	private void createObjects()
	{
		log.info("Start creating prerequisites");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
		PoolClient poolClient= commonClientAllocator.getGroupClient(GC.SC,this);
		String baseURL = testConfig.getBrowserURL();
		assignedUserID = new APIUtilities().getUserBeanIDFromEmail(baseURL, user1.getEmail(), user1.getPassword());	
		assignedUserName = user1.getDisplayName();
		
		log.info("Getting client");
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user1.getEmail(), user1.getPassword());
		String headers[] = {"OAuth-Token", OAuthToken};
		accountID = poolClient.getCCMS_ID();
		clientBeanID = new CollabWebAPI().getbeanIDfromClientID(baseURL, accountID, headers );
		
		log.info("Creating contacts");
		ContactRestAPI contactAPI = new ContactRestAPI();
		contactID = contactAPI.createContactreturnBean(baseURL, headers, "ContactFirst", "ContactLast", "US", "(555) 555-5555", assignedUserID, clientBeanID);
		contactID1 = contactAPI.createContactreturnBean(baseURL, headers, "ContactFirst", "ContactLast", "US", "(555) 555-5555", assignedUserID, clientBeanID);
		contactID2 = contactAPI.createContactreturnBean(baseURL, headers, "ContactFirst", "ContactLast", "US", "(555) 555-5555", assignedUserID, clientBeanID);
		
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
		
		log.info("Creating Opportunities");
   		OpportunityRestAPI opportunityRestAPI = new OpportunityRestAPI();
   		opportunityID = opportunityRestAPI.createOpportunityreturnBean(testConfig.getBrowserURL(), OAuthToken, "BVT API created Oppty to GET Later", clientBeanID, contactID, "SLSP", "03", "2015-10-28", assignedUserID);
   		baseID = opportunityRestAPI.createOpportunityreturnBean(testConfig.getBrowserURL(), OAuthToken, "BVT API created Oppty to GET Later", clientBeanID, contactID, "SLSP", "03", "2015-10-28", assignedUserID);
   		unlinkedID = opportunityRestAPI.createOpportunityreturnBean(testConfig.getBrowserURL(), OAuthToken, "BVT API created Oppty Unlinked to Get Later", clientBeanID, contactID, "SLSP", "03", "2015-10-28", assignedUserID);
		deletedID = opportunityRestAPI.createOpportunityreturnBean(testConfig.getBrowserURL(), OAuthToken, "BVT API created Oppty to Delete and attempt to GET Later", clientBeanID, contactID, "SLSP", "03", "2015-10-28", assignedUserID);
		opportunityRestAPI.deleteOpportunity(testConfig.getBrowserURL(), OAuthToken, deletedID);
		
		Assert.assertTrue(opportunityRestAPI.linkRecordToOpportunityReturnBoolean(testConfig.getBrowserURL(), OAuthToken, baseID, clientBeanID, "Accounts"));
		Assert.assertTrue(opportunityRestAPI.linkRecordToOpportunityReturnBoolean(testConfig.getBrowserURL(), OAuthToken, baseID, callID, "Calls"));
		Assert.assertTrue(opportunityRestAPI.linkRecordToOpportunityReturnBoolean(testConfig.getBrowserURL(), OAuthToken, baseID, contactID1, "Contacts"));
		Assert.assertTrue(opportunityRestAPI.linkRecordToOpportunityReturnBoolean(testConfig.getBrowserURL(), OAuthToken, baseID, noteID, "Notes"));
		Assert.assertTrue(opportunityRestAPI.linkRecordToOpportunityReturnBoolean(testConfig.getBrowserURL(), OAuthToken, baseID, taskID, "Tasks"));
		Assert.assertTrue(opportunityRestAPI.linkRecordToOpportunityReturnBoolean(testConfig.getBrowserURL(), OAuthToken, baseID, meetingID, "Meetings"));
		Assert.assertTrue(opportunityRestAPI.linkRecordToOpportunityReturnBoolean(testConfig.getBrowserURL(), OAuthToken, baseID, opportunityID, "Opportunities"));
		
		log.info("Creating opportunity with RLI's");
		//line item values
		RevenueItem rli = new RevenueItem();
		rli.populate();
		rli.sL10_OfferingType = "B3000";rli.sL15_SubBrand = "PCO";rli.sL17_SegmentLine = "";rli.sL20_BrandCode = "B3U00";rli.sL30_ProductInformation = "B3U04";rli.sL40_MachineType = "5641NV3";
		rliOpptyID = opportunityRestAPI.createOpportunityWithRLIreturnBean(testConfig.getBrowserURL(), OAuthToken, "RLI Linked Oppty", clientBeanID, contactID, "SLSP", "03", date, assignedUserID, assignedUserName, rli);
		rliID = opportunityRestAPI.getRLIFromOppty(testConfig.getBrowserURL(), OAuthToken, rliOpptyID).get("id").toString();
		opportunityRestAPI.relateNewLineItemToOpportunity(testConfig.getBrowserURL(), OAuthToken, rliOpptyID, "500", "10", "", date, "B3000", "PCO", "", "B3U00", "B3U04", "5641NV3","-99", assignedUserName, assignedUserID);


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
    	this.addDataFile("test_config/extensions/api/opportunity/getOpportunity.csv");
    	//Return an array of arrays where each item in the array is a HashMap of parameter values
    	//Test content
       return testData.getAllDataRows();
    }
    
    @Test(dataProvider = "DataProvider")
    public void OpportunitiesGet(HashMap<String,Object> parameterValues)
    {
    
    	String expectedResponseCode = null;
    	String urlOpportunityID = null;
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
            else if (pairs.getKey().equalsIgnoreCase("opportunity")){
				urlOpportunityID = geturlOpportunityID(pairs.getValue().toString());
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
		if (!urlOpportunityID.equalsIgnoreCase(""))
		{
			url+=urlOpportunityID;
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
		
		OpportunityRestAPI opportunityRestAPI = new OpportunityRestAPI();
		String jsonString = opportunityRestAPI.getOpportunity(requestURL, token, expectedResponseCode);
		
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
			else if ((!urlExtension.equalsIgnoreCase("")) && (!relatedObject.equalsIgnoreCase("")) && (!urlOpportunityID.equalsIgnoreCase(""))) 
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
     * return the opportunity id for the request url based on the value in the opportunity column from the csv file
     * @param csvOpportunity value form the opportunity column in the csv
     * @return url Opportunity ID
     */
    private String geturlOpportunityID(String csvOpportunity){
    	if (csvOpportunity.equalsIgnoreCase("base")) {
			return baseID;
		}
    	else if (csvOpportunity.equalsIgnoreCase("rliOpptyID")) {
			return rliOpptyID;
		}
    	else if (csvOpportunity.equalsIgnoreCase("unlinked")) {
			return unlinkedID;
		}
    	else if (csvOpportunity.equalsIgnoreCase("deleted")) {
			return deletedID;
		}
    	else if (csvOpportunity.equalsIgnoreCase("*blank*")) {
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
    	else if (relatedFromCSV.equalsIgnoreCase("validContact")) {
			return contactID1;
		}
    	else if (relatedFromCSV.equalsIgnoreCase("validNote")) {
			return noteID;
		}
    	else if (relatedFromCSV.equalsIgnoreCase("validOpportunity")) {
			return opportunityID;
		}
    	else if (relatedFromCSV.equalsIgnoreCase("validTask")) {
			return taskID;
		}
    	else if (relatedFromCSV.equalsIgnoreCase("validMeeting")) {
			return meetingID;
		}
    	else if (relatedFromCSV.equalsIgnoreCase("validRLI")) {
			return rliID;
		}
    	else if (relatedFromCSV.equalsIgnoreCase("validCCMS")) {
			return accountID;
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
	public void GetOpptyfavourites(){
		log.info("Start test GetOpptyfavourites");
		User user = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);

		log.info("Getting token");
    	LoginRestAPI loginRestAPI = new LoginRestAPI();
   		String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user.getEmail(), user.getPassword());
   		
   		OpportunityRestAPI opportunityRestAPI = new OpportunityRestAPI();
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("favorites","1"));
   		String response = opportunityRestAPI.getOpportunity(getRequestUrl(null, params), OAuthToken, "200");
   		
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

		log.info("End test GetOpptyfavourites");
	}
	
	@Test
	public void GetOpptyMyItems(){
		log.info("Start test GetOpptyMyItems");
		User user = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);

		log.info("Getting token");
    	LoginRestAPI loginRestAPI = new LoginRestAPI();
   		String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user.getEmail(), user.getPassword());
   		
   		OpportunityRestAPI opportunityRestAPI = new OpportunityRestAPI();
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("my_items","1"));
   		String response = opportunityRestAPI.getOpportunity(getRequestUrl(null, params), OAuthToken, "200");
   		
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
			Assert.assertTrue(temp.contains(user.getFirstName()));
		}
		
		log.info("End test GetOpptyMyItems");
	}

	@Test
	public void GetOpptyMyItemsFavorites(){
		log.info("Start test GetOpptyMyItemsFavorites");
		User user = commonUserAllocator.getUser(this);

		log.info("Getting token");
    	LoginRestAPI loginRestAPI = new LoginRestAPI();
   		String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user.getEmail(), user.getPassword());
   		
   		OpportunityRestAPI opportunityRestAPI = new OpportunityRestAPI();
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("my_items","1"));
    	params.add(new BasicNameValuePair("favorites","1"));
   		String response = opportunityRestAPI.getOpportunity(getRequestUrl(null, params), OAuthToken, "200");

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
		
		log.info("End test GetOpptyMyItemsFavorites");
	}
	   
		
		
	@Test
	public void GetOpptyMyItemsFavoritesOrderBy(){
		log.info("Start test GetOpptyMyItemsFavoritesOrderBy");
		User user = commonUserAllocator.getUser(this);

		log.info("Getting token");
    	LoginRestAPI loginRestAPI = new LoginRestAPI();
   		String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user.getEmail(), user.getPassword());
   		
   		OpportunityRestAPI opportunityRestAPI = new OpportunityRestAPI();
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("my_items","1"));
    	params.add(new BasicNameValuePair("favorites","1"));
    	params.add(new BasicNameValuePair("order_by","description:Desc"));
    	params.add(new BasicNameValuePair("fields","name,description"));
   		String response = opportunityRestAPI.getOpportunity(getRequestUrl(null, params), OAuthToken, "200");

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
			Assert.assertFalse(response.contains("date_entered"));
			Assert.assertTrue(response.contains("name"));
			Assert.assertTrue(response.contains("description"));
		}
		
		log.info("End test GetOpptyMyItemsFavoritesOrderBy");
	}
	@Test
	public void GetOpptyFilterSearch(){
		log.info("Start test GetOpptyFilterSearch");
		User user = commonUserAllocator.getUser(this);

		log.info("Getting token");
    	LoginRestAPI loginRestAPI = new LoginRestAPI();
   		String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user.getEmail(), user.getPassword());

   		OpportunityRestAPI opportunityRestAPI = new OpportunityRestAPI();
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("filter[0][$or][0][id][$starts]","3"));
    	params.add(new BasicNameValuePair("filter[0][$or][1][id][$starts]","R"));
    	params.add(new BasicNameValuePair("filter[1][$or][0][description][$starts]","API"));
    	params.add(new BasicNameValuePair("filter[1][$or][1][description][$starts]","BVT"));
    	params.add(new BasicNameValuePair("fields","name,description"));
   		String response = opportunityRestAPI.getOpportunity(getRequestUrl(null, params), OAuthToken, "200");

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
		
		log.info("End test GetOpptyFilterSearch");
	}
	
	@Test
	public void GetOpptyMyItemsOneField(){
		log.info("Start test GetOpptyMyItemsOneField");
		User user = commonUserAllocator.getUser(this);

		log.info("Getting token");
    	LoginRestAPI loginRestAPI = new LoginRestAPI();
   		String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user.getEmail(), user.getPassword());
   		
   		OpportunityRestAPI opportunityRestAPI = new OpportunityRestAPI();
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("my_items","1"));
    	params.add(new BasicNameValuePair("fields","date_entered"));
   		String response = opportunityRestAPI.getOpportunity(getRequestUrl(null, params), OAuthToken, "200");
   		
   		Assert.assertFalse(response.contains("modified_user_id"));
		Assert.assertTrue(response.contains("date_entered"));
		
		log.info("End test GetOpptyMyItemsOneField");
	}
	
	@Test
	public void GetOpptyMyItemsTwoFields(){
		log.info("Start test GetOpptyMyItemsTwoFields");
		User user = commonUserAllocator.getUser(this);
		
		log.info("Getting token");
    	LoginRestAPI loginRestAPI = new LoginRestAPI();
   		String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user.getEmail(), user.getPassword());
   		
   		OpportunityRestAPI opportunityRestAPI = new OpportunityRestAPI();
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("my_items","1"));
    	params.add(new BasicNameValuePair("fields","date_entered,modified_user_id"));
    	String response = opportunityRestAPI.getOpportunity(getRequestUrl(null, params), OAuthToken, "200");

		Assert.assertFalse(response.contains("modified_by_name"));
		Assert.assertTrue(response.contains("date_entered"));
		Assert.assertTrue(response.contains("modified_user_id"));
		
		log.info("End test GetOpptyMyItemsTwoFields");
	}   
    
}
