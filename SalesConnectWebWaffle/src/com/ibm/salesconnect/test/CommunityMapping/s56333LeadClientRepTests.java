package com.ibm.salesconnect.test.CommunityMapping;

import java.util.ArrayList;
//import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
//import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.ConnectionsCommunityAPI;
import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.API.TestDataHolder;
import com.ibm.salesconnect.API.CommunityMappingRestAPI;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.common.HttpUtils;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;

/**
 * @author kvnlau@ie.ibm.com
 * @date Oct 15, 2014
 */
public class s56333LeadClientRepTests extends ProductBaseTest {
	
	Logger log = LoggerFactory.getLogger(s56333LeadClientRepTests.class);
	
	private TestDataHolder testData;
	//private String url = "https://w3-dev.api.ibm.com/sales/development";
	private String contentType = "application/json";
	private HashMap<String,Object> cchMappingList = null;
//	private PoolClient guClient = commonClientAllocator.getGroupClient(GC.GuClient,this);
//	private PoolClient gbClient = commonClientAllocator.getGroupClient(GC.GbClient,this);
//	private PoolClient gbClient2 = commonClientAllocator.getGroupClient(GC.GbClient,this);
//	private User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
//	private User nonCchFnIdUser = commonUserAllocator.getGroupUser(GC.nonCchFnIdGroup, this);

	// Access plans
	private static String clientIDandSecret_BasicOauth = "client_id=1a053415-6763-4b47-bb51-05cb24df8c07&client_secret=N7vS5sU4yT6uS0tB3vY6jS0qJ6jY7wT0nR7sA3wM7mH8qJ0lT2";
	private static String clientIDandSecret_ReadWrite = "client_id=9bdc8d7c-9896-40c0-98df-5b7a40caaf2b&client_secret=lR6oB5rU4dY4bE1hL0sI4iB7mE0nF6vW3qA8dJ3gL5jO0tH0oR";
	private static String clientIDandSecret_ReadOnly = "client_id=3c879be7-7122-428d-924f-73dcdb8c9868&client_secret=bC5dX6tX7bY4iC7dX2qD6uA6fU3wI4lT3jO2oP1aR2fW2hH2nO";
	private static String clientIDandSecret_NoPlan = "client_id=9da4630b-ac6b-4a40-bb58-aa4a2d6153d2&client_secret=aY5pS1nQ0xQ1tJ6dJ8dY1nV3yE6xE4dO6vR6yX8aB6oI4eO8eJ";
	//private static String clientIDandSecret_BasicOauth = "client_id=0960be8e-2128-4559-9f96-2744f7157fe1&client_secret=wP2iX3aP8jO5gV7hR5oJ0nB4cP8cO3tJ1oJ3oT3mY1vB2nD7tA";
	//private static String clientIDandSecret_ReadWrite = "client_id=75592e90-574a-4659-9e74-22044fdb718f&client_secret=S1uM0qL6pF7yW2hI0kD1tB7yE0qE0kR5xJ0wW7iJ8eD2uA7kH6";
	//private static String clientIDandSecret_ReadOnly = "client_id=55cc9274-588b-405a-b6bc-f50ac598268c &client_secret=bC5dX6tX7bY4iC7dX2qD6uA6fU3wI4lT3jO2oP1aR2fW2hH2nO";
	//private static String clientIDandSecret_NoPlan = "client_id=9da4630b-ac6b-4a40-bb58-aa4a2d6153d2&client_secret=aY5pS1nQ0xQ1tJ6dJ8dY1nV3yE6xE4dO6vR6yX8aB6oI4eO8eJ";
	
	// CCH functional ID
	//private static String userEmailCchFnId = "eve25@tst.ibm.com"; //user.getEmail();
	//private static String userPasswordCchFnId = "passw0rd"; //user.getPassword();
	
	// Non CCH function ID
	//private static String userEmailNonCchFnId = "eve29@tst.ibm.com"; //user.getEmail();
	//private static String userPasswordNonCchFnId = "passw0rd"; //user.getPassword();
	
	
	/*
	 * @name: create_map	
	 * @param: ClientId, ClientPwd,	Oauth token, CommunityId, ccmsIds
	 * @author: kvnlau@ie.ibm.com
	 * Note: This is a semi-automated test to be used in conjunction with test task 59885.
	 *       It only creates the community mapping and need to manually verify in Connections
	 *       community that lead client rep is added to community's membership.
	 */
	@Test(groups = {"LEAD_CLIENT_REP"})
	public void Test_createMappingCommunityIdCcmsIdsLeadClientRepAddedToGUC(){
		String ccmsId = null;
//		String ccmsId2 = null;
		String communityId = null;
		boolean isCCH = false;
		String token = null;
		String responseString = null;
		
		try {
			log.info("Start of test method: Test_createMappingCommunityIdCcmsIdsLeadClientRepAddedToGUC");
			// Populate variable from users.cvs, clients.csv files.
			
			PoolClient guClient = commonClientAllocator.getGroupClient(GC.GuClient,this);
			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
			
			ccmsId = guClient.getCCMS_ID(); //"GU02FKGQ"; // existing Client mapped to a community
//			communityId = new ConnectionsCommunityAPI().createConnectionsCommunity(cchFnIdUser.getEmail(),
//					cchFnIdUser.getPassword(),getCnxnCommunity()); //"49d9d939-bb3d-4264-a229-11a597dd4b6f"; // existing (fake) connections community
//			
//			log.info("Created communityUuId: " + communityId + " with owner " + cchFnIdUser.getEmail());
//			
//			ConnectionsCommunityAPI connApi= new ConnectionsCommunityAPI();
//			log.info("Adding new community members: " + connApi.funcIdEmail);
//			responseString = connApi.addUserConnectionsCommunity(cchFnIdUser.getEmail(), 
//																cchFnIdUser.getPassword(), 
//																connApi.funcIdEmail,
//																communityId, getCnxnCommunity());
			
			
			log.info("Retrieving OAuth2Token.");		
			LoginRestAPI loginRestAPI = new LoginRestAPI();
			token = loginRestAPI.getOAuth2TokenViaAPIManager(getApiManagement() + getOauthExtension() + "?" +
																	clientIDandSecret_ReadWrite, cchFnIdUser.getEmail(), 
																	cchFnIdUser.getPassword(),"200");
			JSONObject jsonResponse = null;
			ArrayList<String> ccmsIdsList = null;
			String headers[]={"OAuth-Token", token};
	
			
			log.info("form body of (post) create single mapping request");		
			String body = "{\"communityId\":\"" + communityId + 
							"\",\"ccmsIds\":[\"" + ccmsId + 
							"\"],\"isCCH\":\"" + isCCH + "\"}";
			
			HttpUtils restCalls = new HttpUtils();
			responseString = restCalls.postRequest(getApiManagement() + getMappingExtension() + "?" + 
															clientIDandSecret_ReadWrite, 
															headers, body, contentType);
			try {
				//check for a valid JSON response
				jsonResponse = (JSONObject)new JSONParser().parse(responseString);
				
				log.info("Valid JSON returned.");
				System.out.println(responseString);	
				
				Assert.assertTrue(jsonResponse!=null, "Expected community mapping was not found");
				
				if (jsonResponse!=null) {
					ccmsIdsList = (ArrayList)jsonResponse.get("ccmsIds");
				}
				
				Assert.assertEquals((String) jsonResponse.get("communityId"), communityId, "Single community map does not contain expected communityId");
				Assert.assertTrue(ccmsIdsList.contains(ccmsId), "Single community map does not contain expected sole ccmsId="+ccmsId);
				Assert.assertTrue(ccmsIdsList.size()==1, "Community map contains more than 1 expected ccmsId");
				
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
			
	
			log.info("Verify single mapping was created");
			//HttpUtils restCalls = new HttpUtils();
			responseString = restCalls.getRequest(getApiManagement() + getMappingExtension() + "?" + 
															clientIDandSecret_ReadWrite + "&" + "ccmsId=" + ccmsId, 
															headers, "200");
			try {
				//check for a valid JSON response
				jsonResponse = (JSONObject)new JSONParser().parse(responseString);
				
				log.info("Valid JSON returned.");
				System.out.println(responseString);	
				
				Assert.assertTrue(jsonResponse!=null, "Expected community mapping was not found");
				
				if (jsonResponse!=null) {
					ccmsIdsList = (ArrayList)jsonResponse.get("ccmsIds");
				}
			
				Assert.assertEquals((String) jsonResponse.get("communityId"), communityId, "Single community map does not contain expected communityId");
				Assert.assertTrue(ccmsIdsList.contains(ccmsId), "Single community map does not contain expected sole ccmsId="+ccmsId);
				Assert.assertTrue(ccmsIdsList.size()==1, "Community map contains more than 1 expected ccmsId");
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with get response");
			}
			log.info("End of test method: Test_createMappingCommunityIdCcmsIdsLeadClientRepAddedToGUC");
		
		} finally {
			log.info("Clean up: delete community mapping: '" + communityId + "'");
			responseString = new CommunityMappingRestAPI().deleteCommunityMapping(getApiManagement() + getMappingExtension(), 
												token, communityId, null, clientIDandSecret_ReadWrite, "200");
		}
	}
	
	/*
	 * @name: create_map	
	 * @param: ClientId, ClientPwd,	Oauth token, CommunityId, ccmsIds
	 * @author: kvnlau@ie.ibm.com
	 * Note: This is a semi-automated test to be used in conjunction with test task 59885.
	 *       It only creates the community mapping and need to manually verify in Connections
	 *       community that lead client rep is NOT added to CCH community's membership.
	 */
	@Test(groups = {"LEAD_CLIENT_REP"})
	public void Test_createMappingCommunityIdCcmsIdsLeadClientRepNotAddedToCCH(){
		String ccmsId = null;
//		String ccmsId2 = null;
		String communityId = null;
		boolean isCCH = true;
		String token = null;
		String responseString = null;
		
		try {
			log.info("Start of test method: Test_createMappingCommunityIdCcmsIdsLeadClientRepNotAddedToCCH");
			// Populate variable from users.cvs, clients.csv files.
			
			PoolClient gbClient = commonClientAllocator.getGroupClient(GC.GbClient,this);
			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
			
			ccmsId = gbClient.getCCMS_ID(); //"GB000JPX"; // existing Client mapped to a community
			communityId = new ConnectionsCommunityAPI().createConnectionsCommunity(cchFnIdUser.getEmail(),
					cchFnIdUser.getPassword(),getCnxnCommunity()); //"49d9d939-bb3d-4264-a229-11a597dd4b6f"; // existing (fake) connections community
			
			log.info("Created communityUuId: " + communityId + " with owner " + cchFnIdUser.getEmail());
			
			ConnectionsCommunityAPI connApi= new ConnectionsCommunityAPI();
			log.info("Adding new community members: " + connApi.funcIdEmail);
			responseString = connApi.addUserConnectionsCommunity(cchFnIdUser.getEmail(), 
																cchFnIdUser.getPassword(), 
																connApi.funcIdEmail,
																communityId, getCnxnCommunity());
			log.info("Retrieving OAuth2Token.");		
			LoginRestAPI loginRestAPI = new LoginRestAPI();
			token = loginRestAPI.getOAuth2TokenViaAPIManager(getApiManagement() + getOauthExtension() + "?" +
																	clientIDandSecret_ReadWrite, cchFnIdUser.getEmail(), 
																	cchFnIdUser.getPassword(),"200");
			JSONObject jsonResponse = null;
			ArrayList<String> ccmsIdsList = null;
			String headers[]={"OAuth-Token", token};
	
			
			log.info("form body of (post) create single mapping request");		
			String body = "{\"communityId\":\"" + communityId + 
							"\",\"ccmsIds\":[\"" + ccmsId + 
							"\"],\"isCCH\":\"" + isCCH + "\"}";
			
			HttpUtils restCalls = new HttpUtils();
			responseString = restCalls.postRequest(getApiManagement() + getMappingExtension() + "?" + 
															clientIDandSecret_ReadWrite, 
															headers, body, contentType);
			try {
				//check for a valid JSON response
				jsonResponse = (JSONObject)new JSONParser().parse(responseString);
				
				log.info("Valid JSON returned.");
				System.out.println(responseString);	
				
				Assert.assertTrue(jsonResponse!=null, "Expected community mapping was not found");
				
				if (jsonResponse!=null) {
					ccmsIdsList = (ArrayList)jsonResponse.get("ccmsIds");
				}
				
				Assert.assertEquals((String) jsonResponse.get("communityId"), communityId, "Single community map does not contain expected communityId");
				Assert.assertTrue(ccmsIdsList.contains(ccmsId), "Single community map does not contain expected sole ccmsId="+ccmsId);
				Assert.assertTrue(ccmsIdsList.size()==1, "Community map contains more than 1 expected ccmsId");
				
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
			
	
			log.info("Verify single mapping was created");
			//HttpUtils restCalls = new HttpUtils();
			responseString = restCalls.getRequest(getApiManagement() + getMappingExtension() + "?" + 
															clientIDandSecret_ReadWrite + "&" + "ccmsId=" + ccmsId, 
															headers, "200");
			try {
				//check for a valid JSON response
				jsonResponse = (JSONObject)new JSONParser().parse(responseString);
				
				log.info("Valid JSON returned.");
				System.out.println(responseString);	
				
				Assert.assertTrue(jsonResponse!=null, "Expected community mapping was not found");
				
				if (jsonResponse!=null) {
					ccmsIdsList = (ArrayList)jsonResponse.get("ccmsIds");
				}
			
				Assert.assertEquals((String) jsonResponse.get("communityId"), communityId, "Single community map does not contain expected communityId");
				Assert.assertTrue(ccmsIdsList.contains(ccmsId), "Single community map does not contain expected sole ccmsId="+ccmsId);
				Assert.assertTrue(ccmsIdsList.size()==1, "Community map contains more than 1 expected ccmsId");
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with get response");
			}
			log.info("End of test method: Test_createMappingCommunityIdCcmsIdsLeadClientRepNotAddedToCCH");
		
		} finally {
			log.info("Clean up: delete community mapping: '" + communityId + "'");
			responseString = new CommunityMappingRestAPI().deleteCommunityMapping(getApiManagement() + getMappingExtension(), 
												token, communityId, null, clientIDandSecret_ReadWrite, "200");
		}
	}
	

	/**
	 * @name: getCommunityIdIfMappingExistsForCcmsId()
	 * @description: gets communityId if community mapping exists for ccmsId 
	 * @param:	ClientId+ClientSecret, header (with OAuth token, cchFnIduser, funcIdEmail, ccmsId)
	 * @return: String with existing communityId or null (if no community mapping exists)
	 * @author: kvnlau@ie.ibm.com
	 */	
	public String getCommunityIdIfMappingExistsForCcmsId(String clientIdClientSecret, 
												String[] headers, User cchFnIdUser,
												String funcIdEmail, String ccmsId){
		String communityId = null;
		String responseString = null;
		JSONObject jsonResponse = null;
		HttpUtils restCalls = new HttpUtils();
		
		try {
			// check if community mapping exists
			// Note: this is necessary due to defect 69028: Not able to over-write an existing connections community mapping
			responseString = restCalls.getRequest(getApiManagement() + getMappingExtension() + "?" + 
													clientIDandSecret_ReadWrite + "&" + "ccmsId=" + ccmsId, 
													headers, "999"); // 999 aka don't care if pass or fail
			
			if (responseString.contains("communityId") && responseString.contains("ccmsIds")) {
				try {
					//check for a valid JSON response
					jsonResponse = (JSONObject)new JSONParser().parse(responseString);
					communityId = (String)jsonResponse.get("communityId");
					log.debug(ccmsId + " is mapped to existing communityId: " + communityId);
				} catch (ParseException e) {
					e.printStackTrace();
					Assert.assertTrue(false, "Parsed exception with bad response");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Caught exception");
		}
		return communityId;
	}

	/**
	 * @name: createCommunityIfNoneExistsForCcmsId()
	 * @description: creates community ccmsId if mapping doesn't exist
	 * @param:	ClientId+ClientSecret, header (with OAuth token, cchFnIduser, funcIdEmail, ccmsId, isCCH)
	 * @return: String with new or existing communityId
	 * @author: kvnlau@ie.ibm.com
	 */
	public String createCommunityIfNoneExistsForCcmsId(String clientIdClientSecret, 
													String[] headers, User cchFnIdUser,
													String funcIdEmail, String ccmsId, 
													boolean isCCH){
		String communityId = null;
		String responseString = null;
		JSONObject jsonResponse = null;
		HttpUtils restCalls = new HttpUtils();
		
		try {
			// check if community mapping exists, if not will throw exception that must be caught
			// so test method will not stop
			// Note: this is necessary due to defect 69028: Not able to over-write an existing connections community mapping
			responseString = restCalls.getRequest(getApiManagement() + getMappingExtension() + "?" + 
													clientIDandSecret_ReadWrite + "&" + "ccmsId=" + ccmsId, 
													headers, "999"); // 999 aka don't care if pass or fail
			
			if (responseString.contains("communityId") && responseString.contains("ccmsIds")) {
				try {
					//check for a valid JSON response
					jsonResponse = (JSONObject)new JSONParser().parse(responseString);
					communityId = (String)jsonResponse.get("communityId");
					log.debug(ccmsId + " is mapped to existing communityId: " + communityId);
				} catch (ParseException e) {
					e.printStackTrace();
					Assert.assertTrue(false, "Parsed exception with bad response");
				}
			}
			
			if (communityId==null) {
				// create new community
				ConnectionsCommunityAPI connApi= new ConnectionsCommunityAPI();
				communityId = connApi.createConnectionsCommunity(cchFnIdUser.getEmail(),
																cchFnIdUser.getPassword(),getCnxnCommunity());
				log.info("Created communityUuId: " + communityId + " with owner " + cchFnIdUser.getEmail());

				log.info("Adding new community members: " + connApi.funcIdEmail);
				responseString = connApi.addUserConnectionsCommunity(cchFnIdUser.getEmail(), 
																	cchFnIdUser.getPassword(), 
																	connApi.funcIdEmail,
																	communityId, getCnxnCommunity());
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Caught exception");
		}
		return communityId;
	}
	
	/**
	 * @name: deleteMappingCommunityId()
	 * @description: deletes community mapping 
	 * @param:	ClientId+ClientSecret, header (with OAuth token)
	 * @return: boolean if delete was successful
	 * @author: kvnlau@ie.ibm.com
	 */	
	public boolean deleteMappingCommunityId(String clientIdClientSecret, 
												String[] headers, String communityId){
		String responseString = null;
		JSONObject jsonResponse = null;
			
		responseString = new CommunityMappingRestAPI().deleteCommunityMapping(
																getApiManagement() + 
																getMappingExtension() + "?" + 
																clientIDandSecret_ReadWrite + 
																"&communityId=" + communityId, 
																headers[1], 
																communityId, 
																null,//ccmsId=null
																clientIDandSecret_ReadWrite, 
																"999");// '999' ignores assert in deleteRequest()
		try {
			//check for a valid JSON response
			jsonResponse = (JSONObject)new JSONParser().parse(responseString);
			
			log.info("Valid JSON returned.");
			System.out.println(responseString);	
			
			if (jsonResponse==null ){ // delete was successful
				return true; 
			}
			if ( jsonResponse.containsValue("404") ) { // tried to delete non-existent community
				return true; 
			}
			//Assert.assertTrue(jsonResponse==null, "Did not get expected null response body after deletion");
		} catch (ParseException e) {
			e.printStackTrace();
			Assert.assertTrue(false, "Parse exception with post response");
		}
		return false; // deletion of community mapping failed
	}
	
	public String getOauthExtension(){
		return "oauth";
	}
	
	public String getMappingExtension(){
		return "collab/communityMappings";
	}

	private void createObjects(){
		
	}
	
//    @DataProvider(name="DataProvider")
//    public Object[][] getTestDataCchMappings(){
//    	//Create common objects required by test
//    	this.createObjects();
//    	//Read all lines into an ArrayList of HashMaps
//    	//this.addDataFile("test_config/extensions/api/cchMapping.csv");
//    	//Return an array of arrays where each item in the array is a HashMap of parameter values
//    	//Test content
//       return testData.getAllDataRows();
//    }
       
//    public void addDataFile(String filePath){
//        if(this.testData== null){
//            this.testData = new TestDataHolder();
//        }
//         this.testData.addDataLocation(filePath);
//     }
    
//    @Test(dataProvider = "DataProvider")
//    public void TaskTest(HashMap<String,Object> parameterValues){
//        Iterator<Map.Entry<String, Object>> it = parameterValues.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry<String, Object> pairs = (Map.Entry<String, Object>)it.next();
//            
//            System.out.println(pairs.getKey());
//            System.out.println(pairs.getValue());       
//        }
//        
//        // copy Hashmap to internal variable for use in other local methods.
//        cchMappingList = parameterValues;
//    }
    
    public Map.Entry<String, Object> get1stCchMapping(HashMap<String,Object> parameterValues){
        Iterator<Map.Entry<String, Object>> it = parameterValues.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> pairs = (Map.Entry<String, Object>)it.next();
            
            System.out.println(pairs.getKey());
            System.out.println(pairs.getValue());  
            
            if (!pairs.equals(null))
            	return (Map.Entry<String, Object>)pairs;
        	}
        	return null;
        // copy Hashmap to internal variable for use in other local methods.
        //cchMappingList = parameterValues;
    }
    
}