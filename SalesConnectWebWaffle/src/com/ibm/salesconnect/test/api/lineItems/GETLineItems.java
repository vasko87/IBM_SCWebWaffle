/**
 * 
 */
package com.ibm.salesconnect.test.api.lineItems;


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
import com.ibm.salesconnect.API.CollabWebAPI;
import com.ibm.salesconnect.API.ContactRestAPI;
import com.ibm.salesconnect.API.LineItemRestAPI;
import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.API.OpportunityRestAPI;
import com.ibm.salesconnect.API.TestDataHolder;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.objects.RevenueItem;
import com.ibm.salesconnect.test.api.tasks.GETclientAndsecret;
/**
 * @author evafarrell
 * @date Sept 24, 2015
 */
public class GETLineItems extends ApiBaseTest {
	private static final Logger log = LoggerFactory.getLogger(GETLineItems.class);
	
	@Parameters({ "apiExtension", "applicationName", "APIM", "apim_environment" })
	private GETLineItems(@Optional("ibm_RevenueLineItems") String apiExtension,
			@Optional("SC Auto read") String applicationName,
			@Optional("false") String APIM,
			@Optional("development") String environment) {

		super(apiExtension, applicationName, APIM, environment);

	}

	static GETclientAndsecret callMethGET = new GETclientAndsecret();
	private TestDataHolder testData;
    int rand = new Random().nextInt(100000);
	private String contactID = "22SC-" + rand;
	private String clientBeanID = null;
	private String assignedUserID = null;
	private String assignedUserName = null;
	private String accountID = null;
	private String baseOpptyID = null;
	private String unlinkedOpptyID = null;
	private String deletedOpptyID = null;
	private String baseRLIID = null;
	private String deletedRLIID = null;
	private String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString();
	
	private void createObjects()
	{
		log.info("Start creating prerequisites");
		User user = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
		PoolClient poolClient= commonClientAllocator.getGroupClient(GC.SC,this);
		String baseURL = testConfig.getBrowserURL();
		assignedUserID = new APIUtilities().getUserBeanIDFromEmail(baseURL, user.getEmail(), user.getPassword());	
		assignedUserName = user.getDisplayName();
		
		log.info("Getting client");
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user.getEmail(), user.getPassword());
		String headers[] = {"OAuth-Token", OAuthToken};
		accountID = poolClient.getCCMS_ID();
		clientBeanID = new CollabWebAPI().getbeanIDfromClientID(baseURL, accountID, headers );
		
		log.info("Creating contacts");
		ContactRestAPI contactAPI = new ContactRestAPI();
		contactID = contactAPI.createContactreturnBean(baseURL, headers, "ContactFirst", "ContactLast", "US", "(555) 555-5555", assignedUserID, clientBeanID);
				
		log.info("Creating opportunities with RLI's");
		OpportunityRestAPI opportunityRestAPI = new OpportunityRestAPI();
   			
		//line item values
		RevenueItem rli = new RevenueItem();
		rli.populate();
		rli.sL10_OfferingType = "B3000";rli.sL15_SubBrand = "PCO";rli.sL17_SegmentLine = "";rli.sL20_BrandCode = "B3U00";rli.sL30_ProductInformation = "B3U04";rli.sL40_MachineType = "5641NV3";
		baseOpptyID = opportunityRestAPI.createOpportunityWithRLIreturnBean(testConfig.getBrowserURL(), OAuthToken, "RLI Linked Oppty", clientBeanID, contactID, "SLSP", "03", date, assignedUserID, assignedUserName, rli);
		baseRLIID = opportunityRestAPI.getRLIFromOppty(testConfig.getBrowserURL(), OAuthToken, baseOpptyID).get("id").toString();
		
		rli.populate();
		rli.sL10_OfferingType = "B3000";rli.sL15_SubBrand = "PCO";rli.sL17_SegmentLine = "";rli.sL20_BrandCode = "B3U00";rli.sL30_ProductInformation = "B3U04";rli.sL40_MachineType = "5641NV3";
		deletedOpptyID = opportunityRestAPI.createOpportunityWithRLIreturnBean(testConfig.getBrowserURL(), OAuthToken, "RLI Linked Oppty", clientBeanID, contactID, "SLSP", "03", date, assignedUserID, assignedUserName, rli);
		deletedRLIID = opportunityRestAPI.getRLIFromOppty(testConfig.getBrowserURL(), OAuthToken, deletedOpptyID).get("id").toString();

		unlinkedOpptyID = opportunityRestAPI.createOpportunityreturnBean(testConfig.getBrowserURL(), OAuthToken, "BVT API created Oppty Unlinked to Get Later", clientBeanID, contactID, "SLSP", "03", date, assignedUserID);

		
		log.info("Deleting RLI");
		LineItemRestAPI lineItemRestAPI = new LineItemRestAPI();
		lineItemRestAPI.deleteLineItem(testConfig.getBrowserURL(), OAuthToken, deletedRLIID, "200");
		

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
    	this.addDataFile("test_config/extensions/api/lineItems/getLineItem.csv");
    	//Return an array of arrays where each item in the array is a HashMap of parameter values
    	//Test content
       return testData.getAllDataRows();
    }
    
    @Test(dataProvider = "DataProvider")
    public void LineItemsGet(HashMap<String,Object> parameterValues)
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
            else if (pairs.getKey().equalsIgnoreCase("url")){
				urlOpportunityID = geturlOpportunityRLIID(pairs.getValue().toString());
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
		System.out.println("URL is "+url);
		
		log.info("Sending GET request");
		String token = getOAuthToken(user);
		String getResponseString = null;
		String requestURL = getRequestUrl(url, null);
		if (requestURL.contains("//count")) {
			requestURL = requestURL.replace("//count", "/count");
		}
		
		try{
			log.info("Sending GET request ");
			if (urlExtension.contains("opportun_revenuelineitems")||url.contains("opportun_revenuelineitems")){
				OpportunityRestAPI opportunityrestAPI = new OpportunityRestAPI();
				if(super.APIm==false){
					getResponseString = opportunityrestAPI.getOpportunity(baseURL+"rest/v10/Opportunities/"+url, token, expectedResponseCode);
				}
				else if(super.APIm==true){
					getResponseString = opportunityrestAPI.deleteOpportunity(baseURL+"rest/v10/opportunities/"+url, token, expectedResponseCode);
				}
			}
			else {
				LineItemRestAPI lineItemRestAPI = new LineItemRestAPI();
				getResponseString = lineItemRestAPI.getLineItem(requestURL, token, expectedResponseCode).toString();
			}
		}
		catch (Exception e) {
			LineItemRestAPI lineItemRestAPI = new LineItemRestAPI();
			getResponseString = lineItemRestAPI.getLineItem(requestURL, token, expectedResponseCode).toString();
		}
		
		//Verify response
		if (expectedResponseCode.equalsIgnoreCase("200")) 
		{
			if (!relatedObject.equalsIgnoreCase("")) 
			{
				Assert.assertTrue((new APIUtilities().checkIfValuePresentInJson(getResponseString, "id", relatedObject)) || new APIUtilities().checkIfValuePresentInJson(getResponseString, "id", clientBeanID));
			}
			else if ((!urlExtension.equalsIgnoreCase("")) && (!relatedObject.equalsIgnoreCase(""))) 
			{
				String module = urlExtension.substring(6);
				Assert.assertTrue(new APIUtilities().checkIfValueContainedInJson(getResponseString, "_module", module));
			}
			else if ((!urlExtension.equalsIgnoreCase("")) && (!relatedObject.equalsIgnoreCase("")) && (!urlOpportunityID.equalsIgnoreCase(""))) 
			{
				Assert.assertTrue(new APIUtilities().returnValuePresentInJson(getResponseString, "next_offset").equalsIgnoreCase(Integer.toString(-1)));
			}
			else if (urlExtension.contains("/count")) 
			{
				Assert.assertTrue(Integer.parseInt(new APIUtilities().returnValuePresentInJson(getResponseString, "record_count"))>0);
			}
		}	
    }

    /**
     * return the opportunity id for the request url based on the value in the opportunity column from the csv file
     * @param csvOpportunity value form the opportunity column in the csv
     * @return url Opportunity ID
     */
    private String geturlOpportunityRLIID(String csvOpportunity){
    	if (csvOpportunity.equalsIgnoreCase("baseOpptyID")) {
			return baseOpptyID;
		}
    	else if (csvOpportunity.equalsIgnoreCase("baseRLIID")) {
			return baseRLIID;
		}
    	else if (csvOpportunity.equalsIgnoreCase("unlinkedOpptyID")) {
			return unlinkedOpptyID;
		}
    	else if (csvOpportunity.equalsIgnoreCase("deletedRLIID")) {
			return deletedRLIID;
		}
    	else if (csvOpportunity.equalsIgnoreCase("*blank*")) {
			return "";
		}
    	else {
			return null;
		}
    }
    
    private String getRelatedObjectID(String relatedFromCSV){
    	if (relatedFromCSV.equalsIgnoreCase("validRLI")) {
			return baseRLIID;
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
	public void GetRLIfavourites(){
		log.info("Start test GetRLIfavourites");
		User user = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);

		log.info("Getting token");
    	LoginRestAPI loginRestAPI = new LoginRestAPI();
   		String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user.getEmail(), user.getPassword());
   		
   		LineItemRestAPI lineItemRestAPI = new LineItemRestAPI();
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("favorites","1"));
   		String response = lineItemRestAPI.getLineItem(getRequestUrl(null, params), OAuthToken, "200").toString();
   		
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

		log.info("End test GetRLIfavourites");
	}
	
	@Test
	public void GetRLIMyItems(){
		log.info("Start test GetRLIMyItems");
		User user = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);

		log.info("Getting token");
    	LoginRestAPI loginRestAPI = new LoginRestAPI();
   		String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user.getEmail(), user.getPassword());
   		
   		LineItemRestAPI lineItemRestAPI = new LineItemRestAPI();
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("my_items","1"));
   		String response = lineItemRestAPI.getLineItem(getRequestUrl(null, params), OAuthToken, "200").toString();
   		
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
			String temp = (String) jsonObject.get("created_by");
			Assert.assertTrue(temp.contains(user.getUid()));
//			Assert.assertEquals(jsonObject.get("my_items"), true);
			
		}
		
		log.info("End test GetRLIMyItems");
	}

	@Test
	public void GetRLIMyItemsFavorites(){
		log.info("Start test GetRLIMyItemsFavorites");
		User user = commonUserAllocator.getUser(this);

		log.info("Getting token");
    	LoginRestAPI loginRestAPI = new LoginRestAPI();
   		String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user.getEmail(), user.getPassword());
   		
   		LineItemRestAPI lineItemRestAPI = new LineItemRestAPI();
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("my_items","1"));
    	params.add(new BasicNameValuePair("favorites","1"));
   		String response = lineItemRestAPI.getLineItem(getRequestUrl(null, params), OAuthToken, "200").toString();

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
			String temp = (String) jsonObject.get("created_by");
			Assert.assertTrue(temp.contains(user.getUid()));
		}
		
		log.info("End test GetRLIMyItemsFavorites");
	}
	   
		
		
	@Test
	public void GetRLIMyItemsFavoritesOrderBy(){
		log.info("Start test GetRLIMyItemsFavoritesOrderBy");
		User user = commonUserAllocator.getUser(this);

		log.info("Getting token");
    	LoginRestAPI loginRestAPI = new LoginRestAPI();
   		String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user.getEmail(), user.getPassword());
   		
   		LineItemRestAPI lineItemRestAPI = new LineItemRestAPI();
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("my_items","1"));
    	params.add(new BasicNameValuePair("favorites","1"));
    	params.add(new BasicNameValuePair("order_by","level10"));
    	params.add(new BasicNameValuePair("fields","level10,level20"));
   		String response = lineItemRestAPI.getLineItem(getRequestUrl(null, params), OAuthToken, "200").toString();

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
			Assert.assertFalse(response.contains("level15"));
			Assert.assertTrue(response.contains("level10"));
			Assert.assertTrue(response.contains("level20"));
		}
		
		log.info("End test GetRLIMyItemsFavoritesOrderBy");
	}
	@Test
	public void GetRLIFilterSearch(){
		log.info("Start test GetRLIFilterSearch");
		User user = commonUserAllocator.getUser(this);

		log.info("Getting token");
    	LoginRestAPI loginRestAPI = new LoginRestAPI();
   		String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user.getEmail(), user.getPassword());

   		LineItemRestAPI lineItemRestAPI = new LineItemRestAPI();
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("filter[0][$or][0][level10][$starts]","BX"));
    	params.add(new BasicNameValuePair("filter[1][$or][0][level20][$starts]","B"));
    	params.add(new BasicNameValuePair("fields","level10,revenue_amount"));
   		String response = lineItemRestAPI.getLineItem(getRequestUrl(null, params), OAuthToken, "200").toString();

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
			Assert.assertFalse(response.contains("level15"));
			Assert.assertTrue(response.contains("level10"));
			Assert.assertTrue(response.contains("revenue_amount"));
			Assert.assertTrue(response.contains("BX"));
		}
		
		log.info("End test GetRLIFilterSearch");
	}
	
	@Test
	public void GetRLIMyItemsOneField(){
		log.info("Start test GetRLIMyItemsOneField");
		User user = commonUserAllocator.getUser(this);

		log.info("Getting token");
    	LoginRestAPI loginRestAPI = new LoginRestAPI();
   		String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user.getEmail(), user.getPassword());
   		
   		LineItemRestAPI lineItemRestAPI = new LineItemRestAPI();
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("my_items","1"));
    	params.add(new BasicNameValuePair("fields","level10"));
   		String response = lineItemRestAPI.getLineItem(getRequestUrl(null, params), OAuthToken, "200").toString();
   		
   		Assert.assertFalse(response.contains("srv_work_type"));
		Assert.assertTrue(response.contains("level10"));
		
		log.info("End test GetRLIMyItemsOneField");
	}
	
	@Test
	public void GetRLIMyItemsTwoFields(){
		log.info("Start test GetRLIMyItemsTwoFields");
		User user = commonUserAllocator.getUser(this);
		
		log.info("Getting token");
    	LoginRestAPI loginRestAPI = new LoginRestAPI();
   		String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user.getEmail(), user.getPassword());
   		
   		LineItemRestAPI lineItemRestAPI = new LineItemRestAPI();
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("my_items","1"));
    	params.add(new BasicNameValuePair("fields","level10,srv_work_type"));
    	String response = lineItemRestAPI.getLineItem(getRequestUrl(null, params), OAuthToken, "200").toString();

		Assert.assertFalse(response.contains("revenue_amount"));
		Assert.assertTrue(response.contains("level10"));
		Assert.assertTrue(response.contains("srv_work_type"));
		
		log.info("End test GetRLIMyItemsTwoFields");
	}   
    
}
