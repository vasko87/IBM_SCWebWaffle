package com.ibm.salesconnect.test.CommunityMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
//import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.ApiBaseTest;
import com.ibm.salesconnect.API.CommunityMappingRestAPI;
import com.ibm.salesconnect.API.ConnectionsCommunityAPI;
import com.ibm.salesconnect.API.TestDataHolder;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.common.HttpUtils;

/**
 * @author kvnlau@ie.ibm.com
 * @date Oct 15, 2014
 */
public class CommunityMappingAPIPortalTestsDelete extends ApiBaseTest {
	
	Logger log = LoggerFactory.getLogger(CommunityMappingAPIPortalTestsDelete.class);
	@Parameters({ "apiExtension", "applicationName", "APIM", "apim_environment" })
	private CommunityMappingAPIPortalTestsDelete(@Optional("collab/communityMappings") String apiExtension,
			@Optional("collabweb_communitymapping_readwrite") String applicationName,
			@Optional("true") String APIM,
			@Optional("development") String environment) {

		super(apiExtension, applicationName, APIM, environment);
	}
	
	private TestDataHolder testData;
	//private String url = "https://w3-dev.api.ibm.com/sales/development";
	private String contentType = "application/json";
	private HashMap<String,Object> cchMappingList = null;
	
	// Access plans
	private String clientIDandSecret_BasicOauth = getClientIdAndSecret("collabweb_communitymapping_basic_oauth");
	private String clientIDandSecret_ReadWrite = getClientIdAndSecret("collabweb_communitymapping_readwrite");
	private String clientIDandSecret_ReadOnly = getClientIdAndSecret("collabweb_communitymapping_readonly");
	private String clientIDandSecret_NoPlan = getClientIdAndSecret("collabweb_no_plan");
	

	/**
	 * @name: delete_map_member
	 * @param:	ClientId, ClientPwd (Plan: Basic oauth), CommunityId, ccmsId
	 * @author: kvnlau@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"})
	public void Test_deleteMappingCommunityIdRestrictedToken(){ // Plan dependent
		String ccmsId = null;
		String communityId = null;
		boolean isCCH = true;
		String token_ReadWrite = null;
		String token_restrictedUser = null;
		String responseString = null;
		
		try {
			log.info("Starting API Test method Test_deleteMappingCommunityIdRestrictedToken");
			
			log.info("Get client test data");
			// Populate variable from users.cvs, clients.csv files.
			PoolClient guClient = commonClientAllocator.getGroupClient(GC.GuClient,this);
			ccmsId = guClient.getCCMS_ID(); // "GU02GQJ5"; // existing Client
			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
			User nonCchFnIdUser = commonUserAllocator.getGroupUser(GC.nonCchFnIdGroup, this);

			
			log.info("Retrieving OAuth2Token from plan Basic_OAuth");		
			token_restrictedUser = getOAuthToken(nonCchFnIdUser.getEmail(), nonCchFnIdUser.getPassword(), "collabweb_communitymapping_readwrite");
			token_ReadWrite = getOAuthToken(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), "collabweb_communitymapping_readwrite");
			
			
			JSONObject jsonResponse = null;
			ArrayList<String> ccmsIdsList = null;
			String headers[]={"OAuth-Token", token_ReadWrite};
			
			// ensure no mappings exists for cmmsId -------------------------------------
			log.debug("Test setup: checking if community already exists for ccmsId=" + ccmsId);
			communityId = getCommunityIdIfMappingExistsForCcmsId(clientIDandSecret_ReadWrite, 
															headers, cchFnIdUser,
															new ConnectionsCommunityAPI().funcIdEmail, 
															ccmsId);
			
			if (communityId!=null) { 
				log.debug("Test setup: community already exists for ccmsId1=" + ccmsId + " so deleting it now");
				deleteMappingCommunityId(clientIDandSecret_ReadWrite, headers, communityId);
			} else {
				log.debug("Test setup: checked that no community exists for ccmsId=" + ccmsId);
			}
			
			log.debug("Test setup: create new community");
			// Get existing communityId if ccmsId is mapped to one, or else create new communityId
			communityId = createCommunityIfNoneExistsForCcmsId(clientIDandSecret_ReadWrite, 
															headers, cchFnIdUser,
															new ConnectionsCommunityAPI().funcIdEmail, 
															ccmsId, isCCH);
			
			//---- Create test data, i.e. community mapping --------------------------------
			log.info("form body of (post) create single mapping request");		
			String body = "{\"communityId\":\"" + communityId + 
							"\",\"ccmsIds\":[\"" + ccmsId + 
							"\"],\"isCCH\":\"" + isCCH + "\"}";
			
			log.info("Create a single mapping first with 1 ccmsId1");
			responseString = new HttpUtils().postRequest(getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
															headers, body, contentType);
			
			try {
				//check for a valid JSON response
				jsonResponse = (JSONObject)new JSONParser().parse(responseString);
				
				log.info("Valid JSON returned.");
				System.out.println(responseString);	
				
				Assert.assertTrue(jsonResponse!=null, "Expected community mapping was not found");
				
				if (jsonResponse!=null) {
					ccmsIdsList = (ArrayList<String>) jsonResponse.get("ccmsIds");
				}
				
				Assert.assertEquals((String) jsonResponse.get("communityId"), communityId, "Single community map does not contain expected communityId");
				Assert.assertTrue(ccmsIdsList.contains(ccmsId), "Single community map does not contain expected sole ccmsId="+ccmsId);
				Assert.assertTrue(ccmsIdsList.size()==1, "Community map contains more than 1 expected ccmsId");
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
			//------- End of test data creation------------------------
						
			//------ Test invalid delete-------------------------------------
			log.info("------------------Begin Test: invlaid delete--------------------");
			//Set token to restricted Basic OAuth plan.
			headers[1]=token_restrictedUser;
	
			log.info("Call delete single mapping request, using restricted token (i.e. containing nonCchFnId user), which should fail");		
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("ccmsId",ccmsId));
			params.add(new BasicNameValuePair("communityId",communityId));
			responseString = new HttpUtils().deleteRequest(getRequestUrl(null, params, clientIDandSecret_ReadWrite), 
																headers, "403"); // Expect 403 for token with nonCchFnId user
			try {
				
				//check for a valid JSON response
				jsonResponse = (JSONObject)new JSONParser().parse(responseString);
				
				log.info("Valid JSON returned.");
				System.out.println(responseString);	
					
				String error = (String) jsonResponse.get("error");
				String errorMesg = (String) jsonResponse.get("error_message");
				
				log.info("Verify no maps returned & got 'error message: No results found'");
				Assert.assertTrue(error.contains("not_authorized"), "Did not get expected http message: 'not_authorized'");
				Assert.assertTrue(errorMesg.contains("Attempt to make a write operation by unauthorized user"), 
													"Did not get expected info message: 'Attempt to make a write operation by unauthorized user'");
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
			
			
			log.info("Retrieving OAuth2Token from plan: collabweb_communitymapping_readwrite");		
			jsonResponse = null;
			//Set token to Read Write plan.
			headers[1]=token_ReadWrite;
			
			// Verify community mapping was not deleted be retrieving it via GET
			// Note: using only ccmsId may have "200" response if ccmsId is mapped to several communities (i.e. bad data)
			// Note: So best to verify using both ccmsId & communityId for finding specific mapping between ccmsId & communityId
			log.info("Try retrieving single mapping, which should NOT have been deleted");
			params.clear();
			params.add(new BasicNameValuePair("ccmsId",ccmsId));
			params.add(new BasicNameValuePair("communityId",communityId));
			responseString = new HttpUtils().getRequest(getRequestUrl(null, params, clientIDandSecret_ReadWrite),
														headers, "200"); // Success response code: "200"
			
			try {			
				//check for a valid JSON response
				if (responseString!=null){
					jsonResponse = (JSONObject)new JSONParser().parse(responseString);
					
					log.info("Valid JSON returned.");
					System.out.println(responseString);	

					Assert.assertEquals((String) jsonResponse.get("communityId"), communityId, "Single community map does not contain expected communityId");
					Assert.assertTrue(responseString.contains(ccmsId), "Community map is missing ccmsId=" + ccmsId + ". It shouldn't have been deleted");
				}
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
		} finally {
			log.info("Clean up: delete community mapping: '" + communityId + "'");
			responseString = new CommunityMappingRestAPI().deleteCommunityMapping(getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
																				token_ReadWrite, communityId, null, null, "999"); // '999' ignores internal assert in request
			log.info("End of test method Test_deleteMappingCommunityIdRestrictedToken");
		}
	}

	/**
	 * @name: delete_map_member
	 * @param:	ClientId, ClientPwd (Plan: Read Only), CommunityId, ccmsId
	 * @author: peterpoliwoda@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"})
	public void Test_deleteMappingCommunityIdReadOnlyPlan(){ // Plan dependent
		String ccmsId = null;
		String communityId = null;
		boolean isCCH = false;
		String token_ReadWrite = null;
		String token_ReadOnly = null;
		String responseString = null;
		ArrayList<String> ccmsIdsList = null;
		
		
		try {
			log.info("Starting API Test method Test_deleteMappingCommunityIdReadOnlyPlan");
			
			log.info("Get client test data");
			PoolClient guClient = commonClientAllocator.getGroupClient(GC.GuClient,this);
			
			// Populate variable from users.cvs, clients.csv files.
			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
			ccmsId = guClient.getCCMS_ID(); // "GU02GQJ5"; // existing Client
			
			log.info("Retrieving OAuth2Token(s) from Read Only & Read Write plans");		
			token_ReadOnly = getOAuthToken(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), "collabweb_communitymapping_readonly");
			token_ReadWrite = getOAuthToken(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), "collabweb_communitymapping_readwrite");
			
			JSONObject jsonResponse = null;
			String headers[]={"OAuth-Token", token_ReadWrite};
	
			
			// ensure no mappings exists for cmmsId -------------------------------------
			log.debug("Test setup: checking if community already exists for ccmsId=" + ccmsId);
			communityId = getCommunityIdIfMappingExistsForCcmsId(clientIDandSecret_ReadWrite, 
															headers, cchFnIdUser,
															new ConnectionsCommunityAPI().funcIdEmail, 
															ccmsId);
			
			if (communityId!=null) { 
				log.debug("Test setup: community already exists for ccmsId1=" + ccmsId + " so deleting it now");
				deleteMappingCommunityId(clientIDandSecret_ReadWrite, headers, communityId);
			} else {
				log.debug("Test setup: checked that no community exists for ccmsId=" + ccmsId);
			}
			
			log.debug("Test setup: create new community");
			// Get existing communityId if ccmsId is mapped to one, or else create new communityId
			communityId = createCommunityIfNoneExistsForCcmsId(clientIDandSecret_ReadWrite, 
															headers, cchFnIdUser,
															new ConnectionsCommunityAPI().funcIdEmail, 
															ccmsId, isCCH);
			
			//---- Create test data, i.e. mapping --------------------------------
			log.info("form body of (post) create single mapping request");		
			String body = "{\"communityId\":\"" + communityId + 
							"\",\"ccmsIds\":[\"" + ccmsId + 
							"\"],\"isCCH\":\"" + isCCH + "\"}";
			responseString = new HttpUtils().postRequest(getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
															headers, body, contentType);
			try {
				//check for a valid JSON response
				jsonResponse = (JSONObject)new JSONParser().parse(responseString);
				
				log.info("Valid JSON returned.");
				System.out.println(responseString);	
				
				Assert.assertTrue(jsonResponse!=null, "Expected community mapping was not found");
				
				if (jsonResponse!=null) {
					ccmsIdsList = (ArrayList<String>)jsonResponse.get("ccmsIds");
				}
				
				Assert.assertEquals((String) jsonResponse.get("communityId"), communityId, "Single community map does not contain expected communityId");
				Assert.assertTrue(ccmsIdsList.contains(ccmsId), "Single community map does not contain expected sole ccmsId="+ccmsId);
				Assert.assertTrue(ccmsIdsList.size()==1, "Community map contains more than 1 expected ccmsId");
				
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
			//------- End of test data creation------------------------
			
			log.info("------------------Begin Test--------------------");
			log.info("Call delete single mapping request, using Read-Only plan, which should fail");		
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("ccmsId",ccmsId));
			params.add(new BasicNameValuePair("communityId",communityId));
			responseString = new HttpUtils().deleteRequest(getRequestUrl(null, params, clientIDandSecret_ReadOnly), 
															headers, "401"); //New expected response 401 & not 403. See defect 57472.
			try {
				
				//check for a valid JSON response
				jsonResponse = (JSONObject)new JSONParser().parse(responseString);
				
				log.info("Valid JSON returned.");
				System.out.println(responseString);	
					
				String httpMesg = (String) jsonResponse.get("httpMessage");
				String infoMesg = (String) jsonResponse.get("moreInformation");
				
				log.info("Verify no maps returned & got 'error message: No results found'");
				Assert.assertTrue(httpMesg.contains("Unauthorized"), "Did not get expected http message: 'Unauthorized'");
				Assert.assertTrue(infoMesg.contains("Not registered to plan"), "Did not get expected info message: 'Not registered to plan'");
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
			
			
			log.info("Using OAuth2Token from plan: 'salesconnect_v210_communityMappings_read_only'");		
			jsonResponse = null;
			
			headers[1]= token_ReadOnly;
			
			// Verify community mapping was not deleted be retrieving it via GET
			// Note: using only ccmsId may have "200" response if ccmsId is mapped to several communities (i.e. bad data)
			// Note: So best to verify using both ccmsId & communityId for finding specific mapping between ccmsId & communityId
			log.info("Try retrieving single mapping, which should NOT deleted");
			params.clear();
			params.add(new BasicNameValuePair("ccmsId",ccmsId));
			params.add(new BasicNameValuePair("communityId",communityId));
			responseString = new HttpUtils().getRequest(getRequestUrl(null, params, clientIDandSecret_ReadOnly),
													headers, "200"); // Success response code: "200"
			try {			
				//check for a valid JSON response
				if (responseString!=null){
					jsonResponse = (JSONObject)new JSONParser().parse(responseString);
					
					log.info("Valid JSON returned.");
					System.out.println(responseString);	
					
					Assert.assertEquals((String) jsonResponse.get("communityId"), communityId, "Single community map does not contain expected communityId");
					Assert.assertTrue(responseString.contains(ccmsId), "Community map is missing ccmsId=" + ccmsId + ". It shouldn't have been deleted");
				}
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}		
		} finally {
			log.info("Clean up: delete community mapping: '" + communityId + "'");
			responseString = new CommunityMappingRestAPI().deleteCommunityMapping(getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
																				token_ReadWrite, communityId, null, null, "999"); // '999' ignores internal assert in request
			log.info("End of test method Test_deleteMappingCommunityIdReadOnlyPlan");
		}
	}
	
	/**
	 * @name: delete_map_member
	 * @param:	ClientId, ClientPwd (Plan: Read Only), CommunityId, ccmsId
	 * @author: peterpoliwoda@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"})
	public void Test_deleteMappingCommunityIdNoPlan(){ // Plan dependent
		String ccmsId = null;
		String communityId = null;
		boolean isCCH = true;
		String token_ReadWrite = null;
		String responseString = null;
		ArrayList<String> ccmsIdsList = null;
		
		try {
			log.info("Starting API Test method Test_deleteMappingCommunityIdNoPlan");
			
			log.info("Get client test data");
			PoolClient gbClient = commonClientAllocator.getGroupClient(GC.GbClient,this);

			// Populate variable from users.cvs, clients.csv files.
			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
			ccmsId = gbClient.getCCMS_ID(); // "GB000MGB"; // existing Client

			
			log.info("Retrieving OAuth2Token from plan Read Write");		
			token_ReadWrite = getOAuthToken(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), "collabweb_communitymapping_readwrite");
			
			JSONObject jsonResponse = null;
			String headers[]={"OAuth-Token", token_ReadWrite};
	
			
			// ensure no mappings exists for cmmsId -------------------------------------
			log.debug("Test setup: checking if community already exists for ccmsId=" + ccmsId);
			communityId = getCommunityIdIfMappingExistsForCcmsId(clientIDandSecret_ReadWrite, 
															headers, cchFnIdUser,
															new ConnectionsCommunityAPI().funcIdEmail, 
															ccmsId);
			
			if (communityId!=null) { 
				log.debug("Test setup: community already exists for ccmsId1=" + ccmsId + " so deleting it now");
				deleteMappingCommunityId(clientIDandSecret_ReadWrite, headers, communityId);
			} else {
				log.debug("Test setup: checked that no community exists for ccmsId=" + ccmsId);
			}
			
			log.debug("Test setup: create new community");
			// Get existing communityId if ccmsId is mapped to one, or else create new communityId
			communityId = createCommunityIfNoneExistsForCcmsId(clientIDandSecret_ReadWrite, 
															headers, cchFnIdUser,
															new ConnectionsCommunityAPI().funcIdEmail, 
															ccmsId, isCCH);
			
			//---- Create test data, i.e. mapping --------------------------------
			log.info("form body of (post) create single mapping request");		
			String body = "{\"communityId\":\"" + communityId + 
							"\",\"ccmsIds\":[\"" + ccmsId + 
							"\"],\"isCCH\":\"" + isCCH + "\"}";
			responseString = new HttpUtils().postRequest(getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
															headers, body, contentType);
			try {
				//check for a valid JSON response
				jsonResponse = (JSONObject)new JSONParser().parse(responseString);
				
				log.info("Valid JSON returned.");
				System.out.println(responseString);	
				
				Assert.assertTrue(jsonResponse!=null, "Expected community mapping was not found");
				
				if (jsonResponse!=null) {
					ccmsIdsList = (ArrayList<String>)jsonResponse.get("ccmsIds");
				}
				
				Assert.assertEquals((String) jsonResponse.get("communityId"), communityId, "Single community map does not contain expected communityId");
				Assert.assertTrue(ccmsIdsList.contains(ccmsId), "Single community map does not contain expected sole ccmsId="+ccmsId);
				Assert.assertTrue(ccmsIdsList.size()==1, "Community map contains more than 1 expected ccmsId");
				
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
			//------- End of test data creation------------------------			
			
			// ------ Being Test ------------------------------------------
			log.info("------------------Begin Test--------------------");
			log.info("Call delete single mapping request, using no plan, which should fail");
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("ccmsId",ccmsId));
			params.add(new BasicNameValuePair("communityId",communityId));
			responseString = new HttpUtils().deleteRequest(getRequestUrl(null, params, clientIDandSecret_NoPlan), 
															headers, "401"); //new expected 401 instead of 403. See defect 57472.
			try {
				
				//check for a valid JSON response
				jsonResponse = (JSONObject)new JSONParser().parse(responseString);
				
				log.info("Valid JSON returned.");
				System.out.println(responseString);	
					
				String httpMesg = (String) jsonResponse.get("httpMessage");
				String infoMesg = (String) jsonResponse.get("moreInformation");
				
				log.info("Verify no maps returned & got 'error message: No results found'");
				log.info("Verify no maps returned & got 'error message: No results found'");
				Assert.assertTrue(httpMesg.contains("Unauthorized"), "Did not get expected http message: 'Unauthorized'");
				Assert.assertTrue(infoMesg.contains("Client id not registered."), "Did not get expected info message: 'Client id not registered.'");
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
			
			
			log.info("Using OAuth2Token from plan: 'salesconnect_v210_communityMappings_read_only'");		
			jsonResponse = null;
					
			// Verify community mapping was not deleted be retrieving it via GET
			// Note: using only ccmsId may have "200" response if ccmsId is mapped to several communities (i.e. bad data)
			// Note: So best to verify using both ccmsId & communityId for finding specific mapping between ccmsId & communityId
			log.info("Try retrieving single mapping, which should NOT deleted");
			params.clear();
			params.add(new BasicNameValuePair("ccmsId",ccmsId));
			params.add(new BasicNameValuePair("communityId",communityId));
			responseString = new HttpUtils().getRequest(getRequestUrl(null, params, clientIDandSecret_ReadWrite),
														headers, "200"); // Success response code: "200"
			try {			
				//check for a valid JSON response
				if (responseString!=null){
					jsonResponse = (JSONObject)new JSONParser().parse(responseString);
					
					log.info("Valid JSON returned.");
					System.out.println(responseString);	
					
					Assert.assertEquals((String) jsonResponse.get("communityId"), communityId, "Single community map does not contain expected communityId");
					Assert.assertTrue(responseString.contains(ccmsId), "Community map is missing ccmsId=" + ccmsId + ". It shouldn't have been deleted");
				}
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
		} finally {
			log.info("Clean up: delete community mapping: '" + communityId + "'");
			responseString = new CommunityMappingRestAPI().deleteCommunityMapping(getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
												token_ReadWrite, communityId, null, null, "999"); // '999' ignores internal assert in request
			log.info("End of test method Test_deleteMappingCommunityIdNoPlan");
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
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("ccmsId",ccmsId));
			responseString = restCalls.getRequest(getRequestUrl(null, params, clientIDandSecret_ReadWrite), 
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
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("ccmsId",ccmsId));
			responseString = restCalls.getRequest(getRequestUrl(null, params, clientIdClientSecret), 
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
	public boolean deleteMappingCommunityId(String clientIdSecret, 
												String[] headers, String communityId){
		String responseString = null;
		JSONObject jsonResponse = null;
		
//		responseString = new CommunityMappingRestAPI().deleteCommunityMapping(
//						getApiManagement() + 
//						getMappingExtension() + "?" + 
//						clientIDandSecret_ReadWrite + 
//						"&communityId=" + communityId, 
//						headers[1], 
//						communityId, 
//						null,//ccmsId=null
//						clientIDandSecret_ReadWrite, 
//						"999");// '999' ignores assert in deleteRequest()
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("communityId",communityId));
		responseString = new CommunityMappingRestAPI().deleteCommunityMapping(
																getRequestUrl(null, params, clientIdSecret), 
																headers[1], 
																communityId, 
																null,//ccmsId=null
																null,//clientIdSecret=null
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
//    	
//    	//Read all lines into an ArrayList of HashMaps
//    	//this.addDataFile("test_config/extensions/api/cchMapping.csv");
//    	
//    	//Return an array of arrays where each item in the array is a HashMap of parameter values
//    	//Test content
//       return testData.getAllDataRows();
//    }
//       
//    public void addDataFile(String filePath){
//        if(this.testData== null){
//            this.testData = new TestDataHolder();
//        }
//         this.testData.addDataLocation(filePath);
//     }
//    
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
            Map.Entry<String, Object> pairs = it.next();
            
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
