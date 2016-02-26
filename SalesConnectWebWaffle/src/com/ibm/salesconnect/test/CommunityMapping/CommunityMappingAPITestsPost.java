package com.ibm.salesconnect.test.CommunityMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
//import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.ApiBaseTest;
import com.ibm.salesconnect.API.ConnectionsCommunityAPI;
import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.API.TestDataHolder;
import com.ibm.salesconnect.API.CommunityMappingRestAPI;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.common.HttpUtils;

/**
 * @author kvnlau@ie.ibm.com
 * @date Oct 15, 2014
 */
public class CommunityMappingAPITestsPost extends ApiBaseTest {
	
	Logger log = LoggerFactory.getLogger(CommunityMappingAPITestsPost.class);
	@Parameters({ "apiExtension", "applicationName", "APIM", "apim_environment" })
	private CommunityMappingAPITestsPost(@Optional("collab/communityMappings") String apiExtension,
			@Optional("collabweb_communitymapping_readwrite") String applicationName,
			@Optional("true") String APIM,
			@Optional("development") String environment) {

		super(apiExtension, applicationName, APIM, environment);
	}
	
	private TestDataHolder testData;
	private String contentType = "application/json";
	private HashMap<String,Object> cchMappingList = null;

	// Access plans
	private String clientIDandSecret_BasicOauth = getClientIdAndSecret("collabweb_communitymapping_basic_oauth");
	private String clientIDandSecret_ReadWrite = getClientIdAndSecret("collabweb_communitymapping_readwrite");
	private String clientIDandSecret_ReadOnly = getClientIdAndSecret("collabweb_communitymapping_readonly");
	private String clientIDandSecret_NoPlan = getClientIdAndSecret("collabweb_no_plan");
	
	/**
	 * @name: create_map	
	 * @param: ClientId, ClientPwd	Oauth token (non cch fn id)	CommunityId, ccmsIds
	 * @author: kvnlau@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"})
	public void Test_createMappingInvalidCchFnId(){ // Plan independent
		
		String ccmsId = null;
		String communityId = null;
		boolean isCCH = true;
		String token_invalidCchUser = null;
		String token_validCchUser = null;
		String responseString = null;
		
		log.info("Start of test method: Test_createMappingInvalidCchFnId()");
		
		try {
			// Get test data from users.csv & clients.csv file(s)
			PoolClient gbClient = commonClientAllocator.getGroupClient(GC.GbClient,this);
			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
			User nonCchFnIdUser = commonUserAllocator.getGroupUser(GC.nonCchFnIdGroup, this);

			// Populate variable from users.cvs, clients.csv files.
			ccmsId = gbClient.getCCMS_ID(); // "GB001DK0"; // existing Client
			communityId = new ConnectionsCommunityAPI().createConnectionsCommunity(cchFnIdUser.getEmail(),
					cchFnIdUser.getPassword(),getCnxnCommunity()); //"fff88229-4bab-489d-a10c-e1fdb439fc28"; // existing (fake) connections community
			
			log.info("Retrieving OAuth2Token for a non-CCH Functional Id user");		
			token_invalidCchUser = getOAuthToken(nonCchFnIdUser.getEmail(), nonCchFnIdUser.getPassword(), "collabweb_communitymapping_readwrite");

			token_validCchUser = getOAuthToken(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), "collabweb_communitymapping_readwrite");

			
			JSONObject jsonResponse = null;
			String headers[]={"OAuth-Token", token_invalidCchUser};
	
			log.info("create single mapping (token with non-cch Fn Id) should fail with 403 error");	
			log.info("form body of (post) create single mapping request");		
			String body = "{\"communityId\":\"" + communityId + 
							"\",\"ccmsIds\":[\"" + ccmsId + 
							"\"],\"isCCH\":\"" + isCCH + "\"}";

			responseString = new HttpUtils().postRequest(getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
															headers, body, contentType, "403");
			try {
				//check for a valid JSON response
				jsonResponse = (JSONObject)new JSONParser().parse(responseString);
				
				log.info("Valid JSON returned.");
				System.out.println(responseString);	
				
				String error = (String) jsonResponse.get("error");
				String errorMesg = (String) jsonResponse.get("error_message");
				String communityMapping = (String) jsonResponse.get("communityId");
	
				Assert.assertTrue(error.contentEquals("not_authorized"), "Got error:'" + error + "' but expected:'not_authorized'" );
				Assert.assertTrue(errorMesg.contentEquals("Attempt to make a write operation by unauthorized user"), "Got message:'" + errorMesg + "' but expected:'Attempt to make a write operation by unauthorized user'" );
				Assert.assertTrue(communityMapping==null, "community map should not be created where communityId=" + communityId);			
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
		} finally {
			log.info("Clean up: delete community mapping: '" + communityId + "'");
			responseString = new CommunityMappingRestAPI().deleteCommunityMapping(getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
					token_validCchUser, communityId, null, null, "999"); // '999' ignores internal assert in request
																		// Note: expect 404 as mapping should not be created above
			
			log.info("End of test method: Test_createMappingInvalidCchFnId");
		}
		
	}
	
	/*
	 * <Test case>:	
	 * @param: ClientId, ClientPwd (Plan: read/write), CommunityId, ccmsId, isCCH
	 * @author: kvnlau@ie.ibm.com
	 */
//	@Test(groups = {"CCHAPI"})
//	public void Test_postSingleMapping_TEMPLATE(){
//		
//		// Populate variable from users.cvs, clients.csv files.
//		PoolClient gbClient = commonClientAllocator.getGroupClient(GC.GbClient,this);
//		User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
//		
//		String ccmsId = gbClient.getCCMS_ID(); //"GB001DK0"; // existing Client mapped to a community
//		String communityId = new ConnectionsCommunityAPI().createConnectionsCommunity(cchFnIdUser.getEmail(),
//				cchFnIdUser.getPassword(),getCnxnCommunity()); //"9ad423e7-9ee4-4e2b-9d73-f721902167d0"; // existing connections community
//		boolean isCCH = true;
//		
//		log.info("Retrieving OAuth2Token.");		
//		LoginRestAPI loginRestAPI = new LoginRestAPI();
//		String token = loginRestAPI.getOAuth2TokenViaAPIManager(getApiManagement() + getOauthExtension() + "?" +
//																clientIDandSecret_ReadWrite, cchFnIdUser.getEmail(), 
//																cchFnIdUser.getPassword(),"200");
//		
//		JSONObject jsonResponse = null;
//		String headers[]={"OAuth-Token", token};
//
//		
//		log.info("form body of (post) create single mapping request");		
//		String body = "{\"communityId\":\"" + communityId + 
//						"\",\"ccmsIds\":[\"" + ccmsId + 
//						"\"],\"isCCH\":\"" + isCCH + "\"}";
//		
//		HttpUtils restCalls = new HttpUtils();
//		String postResponseString = restCalls.postRequest(getApiManagement() + getMappingExtension() + "?" + 
//														clientIDandSecret_ReadWrite, 
//														headers, body, contentType);
//		 
//		try {
//			//check for a valid JSON response
//			jsonResponse = (JSONObject)new JSONParser().parse(postResponseString);
//			
//			log.info("Valid JSON returned.");
//			System.out.println(postResponseString);	
//			
//			//Assert.assertTrue(new APIUtilities().checkIfValuePresentInJson(getResponseString, "next_offset", 11));
//			Assert.assertEquals((String) jsonResponse.get("communityId"), communityId, "Single community map does not contain expected communityId");
//			Assert.assertEquals((String) jsonResponse.get("ccmsIds"), ccmsId, "Single community map does not contain expected ccmsId");
//			
//		} catch (ParseException e) {
//			e.printStackTrace();
//			Assert.assertTrue(false, "Parse exception with post response");
//		}
//		
//
//		log.info("Verify single mapping was created");
//		//HttpUtils restCalls = new HttpUtils();
//		String getResponseString = restCalls.getRequest(getApiManagement() + getMappingExtension() + "?" + 
//														clientIDandSecret_ReadWrite + "?" + ccmsId, 
//														headers, "200");
//		 
//		try {
//			//check for a valid JSON response
//			jsonResponse = (JSONObject)new JSONParser().parse(getResponseString);
//			
//			log.info("Valid JSON returned.");
//			System.out.println(getResponseString);	
//		
//			Assert.assertEquals((String) jsonResponse.get("communityId"), communityId, "Single community map does not contain expected communityId");
//			Assert.assertEquals((String) jsonResponse.get("ccmsIds"), ccmsId, "Single community map does not contain expected ccmsId");
//		} catch (ParseException e) {
//			e.printStackTrace();
//			Assert.assertTrue(false, "Parse exception with get response");
//		}
//	}
	
	/**
	 * @name: create_map	
	 * @param: ClientId, ClientPwd,	Oauth token, CommunityId, ccmsIds
	 * @author: kvnlau@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"})
	public void Test_createMappingCommunityIdCcmsIds(){ // Plan independent
		String ccmsId = null;
//		String ccmsId2 = null;
		String communityId = null;
		boolean isCCH = true;
		String token = null;
		String responseString = null;
		ArrayList<String> ccmsIdsList = null;
		
		HttpUtils restCalls = new HttpUtils();
		JSONObject jsonResponse = null;
		
		log.info("Start of test method: Test_createMappingCommunityIdCcmsIds()");
		try {
			// Populate variable from users.cvs, clients.csv files.
			PoolClient gbClient = commonClientAllocator.getGroupClient(GC.GbClient,this);
			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
			
			ccmsId = gbClient.getCCMS_ID(); //"GB001DK0"; // existing Client mapped to a community
			
			log.info("Retrieving OAuth2Token.");		
			token = getOAuthToken(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), "collabweb_communitymapping_readwrite");

			String headers[]={"OAuth-Token", token};
			

			// Get existing communityId if ccmsId is mapped to one, or else create new communityId
			communityId = createCommunityIfNoneExistsForCcmsId(clientIDandSecret_ReadWrite, 
															headers, cchFnIdUser,
															new ConnectionsCommunityAPI().funcIdEmail, 
															ccmsId, isCCH);
			
			log.info("ensure no mappings exists for cmmsId=" + ccmsId);
			if (communityId!=null) { 
				log.debug("Test setup: community already exists for ccmsId=" + ccmsId + " so deleting it now");
				deleteMappingCommunityId(clientIDandSecret_ReadWrite, headers, communityId);
			} else {
				log.debug("Test setup: no community exists for ccmsId=" + ccmsId + " so pre-requisites completed");
			}
			
			if (communityId==null) {
				// create new community
				log.info("Create 'Control' community");
				ConnectionsCommunityAPI connApi= new ConnectionsCommunityAPI();
				communityId = connApi.createConnectionsCommunity(cchFnIdUser.getEmail(),
																cchFnIdUser.getPassword(),getCnxnCommunity());
				log.info("Created communityUuId: " + communityId + " with owner " + cchFnIdUser.getEmail());
				
				log.info("Adding new community members: " + connApi.funcIdEmail + " & " + cchFnIdUser.getEmail());
				responseString = connApi.addUserConnectionsCommunity(cchFnIdUser.getEmail(), 
																	cchFnIdUser.getPassword(), 
																	connApi.funcIdEmail,
																	communityId, getCnxnCommunity());
			}
	
			// -------------- Begin test -------------------------------------
			log.info("form body of (post) create single mapping request");		
			String body = "{\"communityId\":\"" + communityId + 
							"\",\"ccmsIds\":[\"" + ccmsId + 
							"\"],\"isCCH\":\"" + isCCH + "\"}";
			
			log.info("Create a single mapping first with ccmsId=" + ccmsId);
			responseString = restCalls.postRequest(getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
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
				Assert.assertTrue(false, "Parse exception with get response");
			}
		} finally {
			log.info("Clean up: delete community mapping: '" + communityId + "'");
			responseString = new CommunityMappingRestAPI().deleteCommunityMapping(getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
												token, communityId, null, null, "999"); // '999' ignores internal assert in request
			log.info("End of test method: Test_createMappingCommunityIdCcmsIds");
		}
	}
	
	/**
	 * @name: create_map	
	 * @param: ClientId, ClientPwd,	Oauth token, CommunityId, ccmsIds, isCCH bad param missing 
	 * @author: peterpoliwoda@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"})
	public void Test_createMappingCommunityIdCcmsIdsNoIsCCH(){ // Plan independent
		String ccmsId = null;
		String communityId = null;
		String token = null;
		String responseString = null;
		String error = null;
		String error_message = null;
		
		JSONObject jsonResponse = null;
		HttpUtils restCalls = new HttpUtils();
		
		log.info("Starting API Test method Test_createMappingCommunityIdCcmsIdsNoIsCCH");
		
		try {
			// Populate variable from users.cvs, clients.csv files.
			PoolClient gbClient = commonClientAllocator.getGroupClient(GC.GbClient,this);
			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
			ccmsId = gbClient.getCCMS_ID(); //"GB001DK0"; // existing Client mapped to a community

			log.info("Retrieving OAuth2Token.");		
			token = getOAuthToken(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), "collabweb_communitymapping_readwrite");
			
			ArrayList<String> ccmsIdsList = null;
			String headers[]={"OAuth-Token", token};

			log.debug("Test setup: checking if community already exists for ccmsId=" + ccmsId);
			communityId = getCommunityIdIfMappingExistsForCcmsId(clientIDandSecret_ReadWrite, 
															headers, cchFnIdUser,
															new ConnectionsCommunityAPI().funcIdEmail, 
															ccmsId);
			
			if (communityId!=null) { 
				log.debug("Test setup: community already exists for ccmsId=" + ccmsId + " so deleting it now");
				deleteMappingCommunityId(clientIDandSecret_ReadWrite, headers, communityId);
			} else {
				log.debug("Test setup: no community exists for ccmsId=" + ccmsId + " so pre-requisites completed");
			}
			
			if (communityId==null) {
				// create new community
				log.info("Create 'Control' community");
				ConnectionsCommunityAPI connApi= new ConnectionsCommunityAPI();
				communityId = connApi.createConnectionsCommunity(cchFnIdUser.getEmail(),
																cchFnIdUser.getPassword(),getCnxnCommunity());
				log.info("Created communityUuId: " + communityId + " with owner " + cchFnIdUser.getEmail());
				
				log.info("Adding new community members: " + connApi.funcIdEmail + " & " + cchFnIdUser.getEmail());
				responseString = connApi.addUserConnectionsCommunity(cchFnIdUser.getEmail(), 
																	cchFnIdUser.getPassword(), 
																	connApi.funcIdEmail,
																	communityId, getCnxnCommunity());
			}
			
			//--------------- Begin test ----------------------------------------
			log.info("form body of (post) create single mapping request (with bad isCCH value)");		
			String body = "{\"communityId\":\"" + communityId + 
				"\",\"ccmsIds\":[\"" + ccmsId + 
				"\"],\"isCCH\":\"z\"}";
			
			log.info("attempt to create single mapping should fail due to invalid isCCH parameter...");	
			responseString = restCalls.postRequest(getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
															headers, body, contentType, "400"); 
			try{
				//check for a valid JSON response
				
				jsonResponse = (JSONObject)new JSONParser().parse(responseString);
				
				log.info("Valid JSON returned.");					
				log.info("JSON Response: "+responseString);
				
				error = jsonResponse.get("error").toString();
				error_message = jsonResponse.get("error_message").toString();
				
				Assert.assertTrue(error.equals("invalid_parameter"),"Error doesn't return invalid parameter");
				Assert.assertTrue(error_message.equals("Bad POST: value for parameter isCCH is invalid: "), "Error message doesn't return BAD POST");
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
			
			log.info("Verify single mapping was not created");
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("ccmsId",ccmsId));
			responseString = restCalls.getRequest(getRequestUrl(null, params, clientIDandSecret_ReadWrite), 
															headers, "404");
			log.info("Verified mapping not created.");
		}
		finally{
			log.info("Clean up: delete community mapping (in case created) for communityId: '" + communityId + "'");
			responseString = new CommunityMappingRestAPI().deleteCommunityMapping(getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
						token, communityId, null, null, "999"); // '999' ignores internal assert in request
																// Note: expect 404 as mapping should not be created above
			log.info("End of test method: Test_createMappingCommunityIdCcmsIdsNoIsCCH");
		}
	}
	
	/**
	 * @name: create_map	
	 * @param: ClientId, ClientPwd,	Bad Oauth token, CommunityId, ccmsIds 
	 * @author: peterpoliwoda@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"})
	public void Test_createMappingCommunityIdCcmsIdsBadOAuth(){ // Plan independent
		String ccmsId = null;
		String communityId = null;
		boolean isCCH = true;
		String token = null;
		String responseString = null;
		String error = null;
		String error_message = null;
		JSONObject jsonResponse = null;
		
		log.info("Starting API Test method Test_createMappingCommunityIdCcmsIdsBadOAuth");
		
		try {
			// Populate variable from users.cvs, clients.csv files.
			PoolClient gbClient = commonClientAllocator.getGroupClient(GC.GbClient,this);
			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);			
			ccmsId = gbClient.getCCMS_ID(); //"GB001DK0"; // existing Client mapped to a community

			log.info("Retrieving OAuth2Token.");		
			token = getOAuthToken(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), "collabweb_communitymapping_readwrite");
			String headers[]={"OAuth-Token", token};

			
			log.debug("Test setup: checking if community already exists for ccmsId=" + ccmsId);
			communityId = getCommunityIdIfMappingExistsForCcmsId(clientIDandSecret_ReadWrite, 
															headers, cchFnIdUser,
															new ConnectionsCommunityAPI().funcIdEmail, 
															ccmsId);
			
			if (communityId!=null) { 
				log.debug("Test setup: community already exists for ccmsId=" + ccmsId + " so deleting it now");
				deleteMappingCommunityId(clientIDandSecret_ReadWrite, headers, communityId);
			} else {
				log.debug("Test setup: no community exists for ccmsId=" + ccmsId + " so pre-requisites completed");
			}
			
			if (communityId==null) {
				log.info("Create 'Control' community");
				ConnectionsCommunityAPI connApi= new ConnectionsCommunityAPI();
				communityId = connApi.createConnectionsCommunity(cchFnIdUser.getEmail(),
																cchFnIdUser.getPassword(),getCnxnCommunity());
				log.info("Created communityUuId: " + communityId + " with owner " + cchFnIdUser.getEmail());
	
				log.info("Adding new community members: " + connApi.funcIdEmail + " & " + cchFnIdUser.getEmail());
				responseString = connApi.addUserConnectionsCommunity(cchFnIdUser.getEmail(), 
																	cchFnIdUser.getPassword(), 
																	connApi.funcIdEmail,
																	communityId, getCnxnCommunity());
			}
			// ------------------- Begin test ---------------------------------------
			String badHeaders[]={"OAuth-Token", "FAKE"};
			
			log.info("form body of (post) create single mapping request");		
			String body = "{\"communityId\":\"" + communityId + 
				"\",\"ccmsIds\":[\"" + ccmsId + 
				"\"],\"isCCH\":\""+ isCCH +"\"}";
			
			log.info("Get community mapping using fake token in header, so should fail");
			responseString = new HttpUtils().postRequest(getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
															badHeaders, body, contentType,"401");
			try{
				//check for a valid JSON response
				jsonResponse = (JSONObject)new JSONParser().parse(responseString);
				
				log.info("Valid JSON returned.");					
				log.info("JSON Response: " + responseString);
				
				error = jsonResponse.get("error").toString();
				error_message = jsonResponse.get("error_message").toString();
				
				Assert.assertTrue(error.equals("need_login"),"Error doesn't return NEED LOGIN");
				Assert.assertTrue(error_message.equals("invalid_grant"), "Error message doesn't return INVALID GRANT");
			
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
			
			log.info("Verify single mapping was not created");
			headers[1] = token; // re-set header with good token

			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("ccmsId",ccmsId));
			responseString = new HttpUtils().getRequest(getRequestUrl(null, params, clientIDandSecret_ReadWrite), 
													headers, "404");			
			log.info("Verified mapping not created.");
		}
		finally{
			log.info("Clean up: delete community mapping (in case created) for communityId: '" + communityId + "'");
			responseString = new CommunityMappingRestAPI().deleteCommunityMapping(getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
					token, communityId, null, null, "999"); // '999' ignores internal assert in request
															// Note: expect 404 as mapping should not be created above
			log.info("End of test method: Test_createMappingCommunityIdCcmsIdsBadOAuth");
		}
	}
	
	/**
	 * @name: update_map	
	 * @param: ClientId, ClientPwd	Oauth token,	CommunityId, ccmsIds
	 * @author: kvnlau@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"})
	public void Test_updateMappingCommunityIdCcmsIds(){ // Plan independent
		String ccmsId1 = null;
		String ccmsId2 = null;
		String communityId = null;
		boolean isCCH = true;
		String token = null;
		String responseString = null;
		
		log.info("Starting API Test method Test_updateMappingCommunityIdCcmsIds");
		try {
			// Populate variable from users.cvs, clients.csv files.
			PoolClient gbClient = commonClientAllocator.getGroupClient(GC.GbClient,this);
			PoolClient gbClient2 = commonClientAllocator.getGroupClient(GC.GbClient,this);
			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
			
			ccmsId1 = gbClient.getCCMS_ID(); //"GB001DK0"; // existing client 
			ccmsId2 = gbClient2.getCCMS_ID(); //"GB001C1T"; // existing client

			log.info("Retrieving OAuth2Token.");		
			token = getOAuthToken(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), "collabweb_communitymapping_readwrite");
			
			JSONObject jsonResponse = null;
			ArrayList<String> ccmsIdsList = null;
			String headers[]={"OAuth-Token", token};
	
			// ensure no mappings exists for cmmsId1 -------------------------------------
			log.debug("Test setup: checking if community already exists for ccmsId1=" + ccmsId1);
			communityId = getCommunityIdIfMappingExistsForCcmsId(clientIDandSecret_ReadWrite, 
															headers, cchFnIdUser,
															new ConnectionsCommunityAPI().funcIdEmail, 
															ccmsId1);
			
			if (communityId!=null) { 
				log.debug("Test setup: community already exists for ccmsId1=" + ccmsId1 + " so deleting it now");
				deleteMappingCommunityId(clientIDandSecret_ReadWrite, headers, communityId);
			} else {
				log.debug("Test setup: checked that no community exists for ccmsId1=" + ccmsId1);
			}
			
			// ensure no mappings exists for cmmsId2 --------------------------------------
			log.debug("Test setup: checking if community already exists for ccmsId2=" + ccmsId2);
			communityId = getCommunityIdIfMappingExistsForCcmsId(clientIDandSecret_ReadWrite, 
															headers, cchFnIdUser,
															new ConnectionsCommunityAPI().funcIdEmail, 
															ccmsId2);
			
			if (communityId!=null) { 
				log.debug("Test setup: community already exists for ccmsId2=" + ccmsId2 + " so deleting it now");
				deleteMappingCommunityId(clientIDandSecret_ReadWrite, headers, communityId);
			} else {
				log.debug("Test setup: checked that no community exists for ccmsId2=" + ccmsId2);
			}
			
			log.debug("Test setup: create new community");
			// Get existing communityId if ccmsId is mapped to one, or else create new communityId
			communityId = createCommunityIfNoneExistsForCcmsId(clientIDandSecret_ReadWrite, 
															headers, cchFnIdUser,
															new ConnectionsCommunityAPI().funcIdEmail, 
															ccmsId1, isCCH);
			
			log.info("------------------------ Begin test ------------------------------------------");
			log.info("form body of (post) create single mapping request");		
			String body = "{\"communityId\":\"" + communityId + 
							"\",\"ccmsIds\":[\"" + ccmsId1 + 
							"\"],\"isCCH\":\"" + isCCH + "\"}";
			
			log.info("Create a single mapping first with 1 ccmsId1");
			HttpUtils restCalls = new HttpUtils();
			responseString = restCalls.postRequest(getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
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
				Assert.assertTrue(ccmsIdsList.contains(ccmsId1), "Single community map does not contain expected sole ccmsId1="+ccmsId1);
				Assert.assertTrue(ccmsIdsList.size()==1, "Community map contains more than 1 expected ccmsId");
				
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
			
			log.info("Update (overwrite) mapping with new list of 2 ccmsIds");
			log.info("form body of (post) update single mapping request");		
			body = "{\"communityId\":\"" + communityId + 
							"\",\"ccmsIds\":[\"" + ccmsId1 + "\",\"" + ccmsId2 +
							"\"],\"isCCH\":\"" + isCCH + "\"}";
			
			log.info("Create a single mapping first with 1 ccmsId");
			responseString = new HttpUtils().postRequest(getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
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
				Assert.assertTrue(ccmsIdsList.contains(ccmsId1), "Updated community map does not contain expected ccmsId1="+ccmsId1);
				Assert.assertTrue(ccmsIdsList.contains(ccmsId2), "Updated community map does not contain expected ccmsId2="+ccmsId2);
				Assert.assertTrue(ccmsIdsList.size()==2, "Community map contains " + ccmsIdsList.size() + " ccmsIds but expected 2");
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with get response");
			}
		} finally {
			log.info("Clean up: delete community mapping: '" + communityId + "'");
			responseString = new CommunityMappingRestAPI().deleteCommunityMapping(getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
												token, communityId, null, null, "999"); // '999' ignores internal assert in request
			log.info("End of test method: Test_updateMappingCommunityIdCcmsIds");
		}
	}
	
	/**
	 * @name: update_map	
	 * @param: ClientId, ClientPwd	Oauth token, CommunityId, ccmsIds, no isCCH parameter
	 * @author: peterpoliwodau@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"}) //Not tested yet, problems with Write APIs
	public void Test_updateMappingCommunityIdCcmsIdsNoIsCCH(){ // Plan independent
		String ccmsId1 = null;
		String ccmsId2 = null;
		String communityId = null;
		boolean isCCH = true;
		String token = null;
		String responseString = null;
		String error = null;
		String error_message = null;
		
		log.info("Starting API Test method Test_updateMappingCommunityIdCcmsIdsNoIsCCH");
		
		try {
			// Populate variable from users.cvs, clients.csv files.
			PoolClient gbClient1 = commonClientAllocator.getGroupClient(GC.GbClient,this);
			PoolClient gbClient2 = commonClientAllocator.getGroupClient(GC.GbClient,this);
			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
			
			ccmsId1 = gbClient1.getCCMS_ID(); //"GB001DK0"; // existing client 
			ccmsId2 = gbClient2.getCCMS_ID(); //"GB001C1T"; // existing client

			log.info("Retrieving OAuth2Token.");		
			LoginRestAPI loginRestAPI = new LoginRestAPI();
			token = getOAuthToken(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), "collabweb_communitymapping_readwrite");
//			token = loginRestAPI.getOAuth2TokenViaAPIManager(getApiManagement() + getOauthExtension() + "?" +
//																	clientIDandSecret_ReadWrite, cchFnIdUser.getEmail(), 
//																	cchFnIdUser.getPassword(),"200");
			
			JSONObject jsonResponse = null;
			ArrayList<String> ccmsIdsList = null;
			String headers[]={"OAuth-Token", token};

			// ensure no mappings exists for cmmsId1 -------------------------------------
			log.debug("Test setup: checking if community already exists for ccmsId1=" + ccmsId1);
			communityId = getCommunityIdIfMappingExistsForCcmsId(clientIDandSecret_ReadWrite, 
															headers, cchFnIdUser,
															new ConnectionsCommunityAPI().funcIdEmail, 
															ccmsId1);
			
			if (communityId!=null) { 
				log.debug("Test setup: community already exists for ccmsId1=" + ccmsId1 + " so deleting it now");
				deleteMappingCommunityId(clientIDandSecret_ReadWrite, headers, communityId);
			} else {
				log.debug("Test setup: checked that no community exists for ccmsId1=" + ccmsId1);
			}
			
			// ensure no mappings exists for cmmsId2 --------------------------------------
			log.debug("Test setup: checking if community already exists for ccmsId2=" + ccmsId2);
			communityId = getCommunityIdIfMappingExistsForCcmsId(clientIDandSecret_ReadWrite, 
															headers, cchFnIdUser,
															new ConnectionsCommunityAPI().funcIdEmail, 
															ccmsId2);
			
			if (communityId!=null) { 
				log.debug("Test setup: community already exists for ccmsId2=" + ccmsId2 + " so deleting it now");
				deleteMappingCommunityId(clientIDandSecret_ReadWrite, headers, communityId);
			} else {
				log.debug("Test setup: checked that no community exists for ccmsId2=" + ccmsId2);
			}
			
			// Get existing communityId if ccmsId1 is mapped to one, or else create new communityId
			log.debug("Test setup: create new community");
			communityId = createCommunityIfNoneExistsForCcmsId(clientIDandSecret_ReadWrite, 
															headers, cchFnIdUser,
															new ConnectionsCommunityAPI().funcIdEmail, 
															ccmsId1, isCCH);
			
			log.info("------------------------------ Begin Test -------------------------------------");
			log.info("form body of (post) create single mapping request");		
			String body = "{\"communityId\":\"" + communityId + 
							"\",\"ccmsIds\":[\"" + ccmsId1 + 
							"\"],\"isCCH\":\"" + isCCH + "\"}";
			
			log.info("Create a single mapping first with 1 ccmsId");
			HttpUtils restCalls = new HttpUtils();
			responseString = restCalls.postRequest(getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
															headers, body, contentType);
			try {
				//check for a valid JSON response
				jsonResponse = (JSONObject)new JSONParser().parse(responseString);
			
				System.out.println(responseString);	
				Assert.assertTrue(jsonResponse!=null, "Expected community mapping was not found");			
				log.info("Valid JSON returned.");
				
				if (jsonResponse!=null) {
					ccmsIdsList = (ArrayList)jsonResponse.get("ccmsIds");
				}
				
				Assert.assertEquals((String) jsonResponse.get("communityId"), communityId, "Single community map does not contain expected communityId");
				Assert.assertTrue(ccmsIdsList.contains(ccmsId1), "Single community map does not contain expected sole ccmsId="+ccmsId1);
				Assert.assertTrue(ccmsIdsList.size()==1, "Community map contains more than 1 expected ccmsId");
				
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
			
			log.info("Update (overwrite) mapping with new list of 2 ccmsIds (but missing isCCH's value), so should fail");
			log.info("form body of (post) update single mapping request");		
			body = "{\"communityId\":\"" + communityId + 
							"\",\"ccmsIds\":[\"" + ccmsId1 + "\",\"" + ccmsId2 +
							"\"],\"isCCH\":\"" + "\"}";
			
			responseString = restCalls.postRequest(getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
															headers, body, contentType,"400");
			try {
				//check for a valid JSON response
				jsonResponse = (JSONObject)new JSONParser().parse(responseString);
				
				System.out.println(responseString);
				Assert.assertTrue(jsonResponse!=null, "Expected community mapping was not found");					
				log.info("Valid JSON returned.");
				
				error = jsonResponse.get("error").toString();
				error_message = jsonResponse.get("error_message").toString();
				
				Assert.assertTrue(error.equals("invalid_parameter"),"Error doesn't return invalid parameter");
				Assert.assertTrue(error_message.contains("Bad parameters: correct POST parameters not passed to service"), "Error message doesn't return bad parameters");

			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with get response");
			}		
		} finally {
			log.info("Clean up: delete community mapping: '" + communityId + "'");
			responseString = new CommunityMappingRestAPI().deleteCommunityMapping(getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
												token, communityId, null, null, "999"); // '999' ignores internal assert in request
			log.info("End of test method: Test_updateMappingCommunityIdCcmsIdsNoIsCCH");
		}
	}
	
	/**
	 * @name: update_map	
	 * @param: ClientId, ClientPwd	Oauth token, CommunityId, ccmsIds, no isCCH parameter
	 * @author: peterpoliwodau@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"}) //Not tested yet, problems with Write APIs
	public void Test_updateMappingCommunityIdCcmsIdsNoCommunityId(){ // Plan independent
		String ccmsId1 = null;
		String ccmsId2 = null;
		String communityId = null;
		boolean isCCH = true;
		String token = null;
		String responseString = null;
		String error = null;
		String error_message = null;
		
		log.info("Starting API Test method Test_updateMappingCommunityIdCcmsIdsNoCommunityId");
		
		try {
			// Populate variable from users.cvs, clients.csv files.
			PoolClient gbClient = commonClientAllocator.getGroupClient(GC.GbClient,this);
			PoolClient gbClient2 = commonClientAllocator.getGroupClient(GC.GbClient,this);
			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
			
			ccmsId1 = gbClient.getCCMS_ID(); //"GB001DK0"; // existing client 
			ccmsId2 = gbClient2.getCCMS_ID(); //"GB001C1T"; // existing client

			log.info("Retrieving OAuth2Token.");		
			token = getOAuthToken(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), "collabweb_communitymapping_readwrite");
			
			JSONObject jsonResponse = null;
			ArrayList<String> ccmsIdsList = null;
			String headers[]={"OAuth-Token", token};
	
			// ensure no mappings exists for cmmsId1 -------------------------------------
			log.debug("Test setup: checking if community already exists for ccmsId1=" + ccmsId1);
			communityId = getCommunityIdIfMappingExistsForCcmsId(clientIDandSecret_ReadWrite, 
															headers, cchFnIdUser,
															new ConnectionsCommunityAPI().funcIdEmail, 
															ccmsId1);
			
			if (communityId!=null) { 
				log.debug("Test setup: community already exists for ccmsId1=" + ccmsId1 + " so deleting it now");
				deleteMappingCommunityId(clientIDandSecret_ReadWrite, headers, communityId);
			} else {
				log.debug("Test setup: checked that no community exists for ccmsId1=" + ccmsId1);
			}
			
			// ensure no mappings exists for cmmsId2 --------------------------------------
			log.debug("Test setup: checking if community already exists for ccmsId2=" + ccmsId2);
			communityId = getCommunityIdIfMappingExistsForCcmsId(clientIDandSecret_ReadWrite, 
															headers, cchFnIdUser,
															new ConnectionsCommunityAPI().funcIdEmail, 
															ccmsId2);
			
			if (communityId!=null) { 
				log.debug("Test setup: community already exists for ccmsId2=" + ccmsId2 + " so deleting it now");
				deleteMappingCommunityId(clientIDandSecret_ReadWrite, headers, communityId);
			} else {
				log.debug("Test setup: checked that no community exists for ccmsId2=" + ccmsId2);
			}
			
			log.debug("Test setup: create new community");
			// Get existing communityId if ccmsId is mapped to one, or else create new communityId
			communityId = createCommunityIfNoneExistsForCcmsId(clientIDandSecret_ReadWrite, 
															headers, cchFnIdUser,
															new ConnectionsCommunityAPI().funcIdEmail, 
															ccmsId1, isCCH);
			
			// ----------------------- Begin Test ------------------------------------
			log.info("form body of (post) create single mapping request");		
			String body = "{\"communityId\":\"" + communityId + 
							"\",\"ccmsIds\":[\"" + ccmsId1 + 
							"\"],\"isCCH\":\"" + isCCH + "\"}";
			
			log.info("Create a single mapping first with 1 ccmsId");
			responseString = new HttpUtils().postRequest(getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
															headers, body, contentType);
			try {
				//check for a valid JSON response
				jsonResponse = (JSONObject)new JSONParser().parse(responseString);
			
				System.out.println(responseString);	
				Assert.assertTrue(jsonResponse!=null, "Expected community mapping was not found");			
				log.info("Valid JSON returned.");
				
				if (jsonResponse!=null) {
					ccmsIdsList = (ArrayList)jsonResponse.get("ccmsIds");
				}
				
				Assert.assertEquals((String) jsonResponse.get("communityId"), communityId, "Single community map does not contain expected communityId");
				Assert.assertTrue(ccmsIdsList.contains(ccmsId1), "Single community map does not contain expected sole ccmsId="+ccmsId1);
				Assert.assertTrue(ccmsIdsList.size()==1, "Community map contains more than 1 expected ccmsId");
				
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
			
			log.info("Update (overwrite) mapping with new list of 2 ccmsIds & blank communityId (which should fail)");
			log.info("form body of (post) update single mapping request");		
			body = "{\"communityId\":\"" +  // omitted communityId with ""
							"\",\"ccmsIds\":[\"" + ccmsId1 + "\",\"" + ccmsId2 +
							"\"],\"isCCH\":\"" + isCCH + "\"}";

			responseString = new HttpUtils().postRequest(getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
															headers, body, contentType,"400");
			try {
				//check for a valid JSON response
				jsonResponse = (JSONObject)new JSONParser().parse(responseString);
				
				System.out.println(responseString);
				Assert.assertTrue(jsonResponse!=null, "Expected community mapping was not found");					
				log.info("Valid JSON returned.");
				
				error = jsonResponse.get("error").toString();
				error_message = jsonResponse.get("error_message").toString();
				
				Assert.assertTrue(error.equals("invalid_parameter"),"Error doesn't return invalid parameter");
				Assert.assertTrue(error_message.contains("Bad parameters: correct POST parameters not passed to service"), "Error message doesn't return bad parameters");

			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with get response");
			}			
		} finally {
			log.info("Clean up: delete community mapping: '" + communityId + "'");
			responseString = new CommunityMappingRestAPI().deleteCommunityMapping(getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
												token, communityId, null, null, "999"); // '999' ignores internal assert in request
			log.info("End of test method: Test_updateMappingCommunityIdCcmsIdsNoCommunityId");
		}
	}
	
	/*
	 * @name: update_map	
	 * @param: ClientId, ClientPwd	Oauth token, CommunityId, ccmsIds, bad CcmsIds parameters
	 * @author: peterpoliwodau@ie.ibm.com
	 */
//	@Test(groups = {"CCHAPI"}) // ========================OBSOLETED: due to resolution of defect 62808
//	public void Test_updateMappingCommunityIdCcmsIdsFakeCcmsIds(){
//		String ccmsId = null;
//		String fakeCcmsId = "abc";
//		String fakeCcmsId2 = "def";
//		String communityId = null;
//		boolean isCCH = true;
//		String token = null;
//		String responseString = null;
//		String error = null;
//		String error_message = null;
//		
//		log.info("Starting API Test method Test_updateMappingCommunityIdCcmsIdsFakeCcmsIds");
//		
//		try {
//			// Populate variable from users.cvs, clients.csv files.
//			PoolClient gbClient = commonClientAllocator.getGroupClient(GC.GbClient,this);
//			
//			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
//			
//			ccmsId = gbClient.getCCMS_ID(); //"GB001DK0"; // existing client 
//			communityId = new ConnectionsCommunityAPI().createConnectionsCommunity(cchFnIdUser.getEmail(),
//					cchFnIdUser.getPassword(),getCnxnCommunity()); //"9ad423e7-9ee4-4e2b-9d73-f721902167d0"; // existing (fake) connections community
//			
//			log.info("Retrieving OAuth2Token.");		
//			LoginRestAPI loginRestAPI = new LoginRestAPI();
//			token = loginRestAPI.getOAuth2TokenViaAPIManager(getApiManagement() + getOauthExtension() + "?" +
//																	clientIDandSecret_ReadWrite, cchFnIdUser.getEmail(), 
//																	cchFnIdUser.getPassword(),"200");
//			JSONObject jsonResponse = null;
//			ArrayList<String> ccmsIdsList = null;
//			String headers[]={"OAuth-Token", token};
//	
//			
//			log.info("form body of (post) create single mapping request");		
//			String body = "{\"communityId\":\"" + communityId + 
//							"\",\"ccmsIds\":[\"" + ccmsId + 
//							"\"],\"isCCH\":\"" + isCCH + "\"}";
//			
//			log.info("Create a single mapping first with 1 ccmsId");
//			HttpUtils restCalls = new HttpUtils();
//			responseString = restCalls.postRequest(getApiManagement() + getMappingExtension() + "?" + 
//															clientIDandSecret_ReadWrite, 
//															headers, body, contentType);
//			try {
//				//check for a valid JSON response
//				jsonResponse = (JSONObject)new JSONParser().parse(responseString);
//			
//				System.out.println(responseString);	
//				Assert.assertTrue(jsonResponse!=null, "Expected community mapping was not found");			
//				log.info("Valid JSON returned.");
//				
//				if (jsonResponse!=null) {
//					ccmsIdsList = (ArrayList)jsonResponse.get("ccmsIds");
//				}
//				
//				Assert.assertEquals((String) jsonResponse.get("communityId"), communityId, "Single community map does not contain expected communityId");
//				Assert.assertTrue(ccmsIdsList.contains(ccmsId), "Single community map does not contain expected sole ccmsId="+ccmsId);
//				Assert.assertTrue(ccmsIdsList.size()==1, "Community map contains more than 1 expected ccmsId");
//				
//			} catch (ParseException e) {
//				e.printStackTrace();
//				Assert.assertTrue(false, "Parse exception with post response");
//			}
//			
//			log.info("Update (overwrite) mapping with new list of 2 ccmsIds");
//			log.info("form body of (post) update single mapping request");		
//			body = "{\"communityId\":\"" +  communityId + 
//							"\",\"ccmsIds\":[\"" + fakeCcmsId + "\",\"" + fakeCcmsId2 +
//							"\"],\"isCCH\":\"" + isCCH + "\"}";
//			
//			//log.info("Create a single mapping first with 1 ccmsId");
//			//HttpUtils restCalls = new HttpUtils();
//			responseString = restCalls.postRequest(getApiManagement() + getMappingExtension() + "?" + 
//															clientIDandSecret_ReadWrite, 
//															headers, body, contentType,"400");
//			try {
//				//check for a valid JSON response
//				jsonResponse = (JSONObject)new JSONParser().parse(responseString);
//				
//				System.out.println(responseString);
//				Assert.assertTrue(jsonResponse!=null, "Expected community mapping was not found");					
//				log.info("Valid JSON returned.");
//				
//				error = jsonResponse.get("error").toString();
//				error_message = jsonResponse.get("error_message").toString();
//				
//				Assert.assertTrue(error.equals("invalid_parameter"),"Error doesn't return invalid parameter");
//				Assert.assertTrue(error_message.contains("Bad parameters: correct POST parameters not passed to service"), "Error message doesn't return bad parameters");
//
//			} catch (ParseException e) {
//				e.printStackTrace();
//				Assert.assertTrue(false, "Parse exception with get response");
//			}
//			log.info("End of test method: Test_updateMappingCommunityIdCcmsIdsFakeCcmsIds");
//			
//		} finally {
//			log.info("Clean up: delete community mapping: '" + communityId + "'");
//			responseString = new CommunityMappingRestAPI().deleteCommunityMapping(getApiManagement() + getMappingExtension(), 
//												token, communityId, null, clientIDandSecret_ReadWrite, "200");
//		}
//	}
	
	/**
	 * @name: update_map	
	 * @param: ClientId, ClientPwd,	fake OAuth token, CommunityId, ccmsIds, CcmsIds
	 * @author: peterpoliwodau@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"}) //Not tested yet, timeout problems with WRITE APIs
	public void Test_updateMappingCommunityIdCcmsIdsInvalidOAuthToken(){ // Plan independent
		String ccmsId1 = null;
		String ccmsId2 = null;
		String communityId = null;
		boolean isCCH = true;
		String token = null;
		String responseString = null;
		String error = null;
		String error_message = null;
		
		log.info("Starting API Test method Test_updateMappingCommunityIdCcmsIdsInvalidOAuthToken");
		
		try {
			// Populate variable from users.cvs, clients.csv files.
			PoolClient gbClient = commonClientAllocator.getGroupClient(GC.GbClient,this);
			PoolClient gbClient2 = commonClientAllocator.getGroupClient(GC.GbClient,this);
			
			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
			
			ccmsId1 = gbClient.getCCMS_ID(); //"GB001DK0"; // existing client 
			ccmsId2 = gbClient2.getCCMS_ID(); //"GB001DK0"; // existing client

			log.info("Retrieving OAuth2Token.");		
			token = getOAuthToken(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), "collabweb_communitymapping_readwrite");
			
			JSONObject jsonResponse = null;
			ArrayList<String> ccmsIdsList = null;
			String headers[]={"OAuth-Token", token};
	
			// ensure no mappings exists for cmmsId1 -------------------------------------
			log.debug("Test setup: checking if community already exists for ccmsId1=" + ccmsId1);
			communityId = getCommunityIdIfMappingExistsForCcmsId(clientIDandSecret_ReadWrite, 
															headers, cchFnIdUser,
															new ConnectionsCommunityAPI().funcIdEmail, 
															ccmsId1);
			
			if (communityId!=null) { 
				log.debug("Test setup: community already exists for ccmsId1=" + ccmsId1 + " so deleting it now");
				deleteMappingCommunityId(clientIDandSecret_ReadWrite, headers, communityId);
			} else {
				log.debug("Test setup: checked that no community exists for ccmsId1=" + ccmsId1);
			}
			
			// ensure no mappings exists for cmmsId2 --------------------------------------
			log.debug("Test setup: checking if community already exists for ccmsId2=" + ccmsId2);
			communityId = getCommunityIdIfMappingExistsForCcmsId(clientIDandSecret_ReadWrite, 
															headers, cchFnIdUser,
															new ConnectionsCommunityAPI().funcIdEmail, 
															ccmsId2);
			
			if (communityId!=null) { 
				log.debug("Test setup: community already exists for ccmsId2=" + ccmsId2 + " so deleting it now");
				deleteMappingCommunityId(clientIDandSecret_ReadWrite, headers, communityId);
			} else {
				log.debug("Test setup: checked that no community exists for ccmsId2=" + ccmsId2);
			}
			
			log.debug("Test setup: create new community");
			// Get existing communityId if ccmsId is mapped to one, or else create new communityId
			communityId = createCommunityIfNoneExistsForCcmsId(clientIDandSecret_ReadWrite, 
															headers, cchFnIdUser,
															new ConnectionsCommunityAPI().funcIdEmail, 
															ccmsId1, isCCH);
			
			log.info("// ------------------- Begin Test ---------------------------------------");
			log.info("form body of (post) create single mapping request");		
			String body = "{\"communityId\":\"" + communityId + 
							"\",\"ccmsIds\":[\"" + ccmsId1 + 
							"\"],\"isCCH\":\"" + isCCH + "\"}";
			
			log.info("Create a single mapping first with 1 ccmsId");
			responseString = new HttpUtils().postRequest(getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
															headers, body, contentType);
			try {
				//check for a valid JSON response
				jsonResponse = (JSONObject)new JSONParser().parse(responseString);
			
				System.out.println(responseString);	
				Assert.assertTrue(jsonResponse!=null, "Expected community mapping was not found");			
				log.info("Valid JSON returned.");
				
				if (jsonResponse!=null) {
					ccmsIdsList = (ArrayList)jsonResponse.get("ccmsIds");
				}
				
				Assert.assertEquals((String) jsonResponse.get("communityId"), communityId, "Single community map does not contain expected communityId");
				Assert.assertTrue(ccmsIdsList.contains(ccmsId1), "Single community map does not contain expected sole ccmsId="+ccmsId1);
				Assert.assertTrue(ccmsIdsList.size()==1, "Community map contains more than 1 expected ccmsId");
				
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
			
			log.info("Update (overwrite) mapping with new list of 2 ccmsIds (with invalid token), so should fail");
			log.info("form body of (post) update single mapping request");		
			body = "{\"communityId\":\"" +  communityId + 
							"\",\"ccmsIds\":[\"" + ccmsId1 + "\",\"" + ccmsId2 +
							"\"],\"isCCH\":\"" + isCCH + "\"}";

			//Updating headers to a fake token
			headers[1] = "FAKE";

			responseString = new HttpUtils().postRequest(getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
															headers, body, contentType,"401");
			try {
				//check for a valid JSON response
				jsonResponse = (JSONObject)new JSONParser().parse(responseString);
				
				log.info("Valid JSON returned.");					
				log.info("JSON Response: " + responseString);
				
				error = jsonResponse.get("error").toString();
				error_message = jsonResponse.get("error_message").toString();
				
				Assert.assertTrue(error.equals("need_login"),"Error doesn't return NEED LOGIN");
				Assert.assertTrue(error_message.equals("invalid_grant"), "Error message doesn't return INVALID GRANT");

			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with get response");
			}			
		} finally {
			log.info("Clean up: delete community mapping: '" + communityId + "'");
			responseString = new CommunityMappingRestAPI().deleteCommunityMapping(
												getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
												token, communityId, null, null, "999"); // '999' ignores internal assert in request
			log.info("End of test method: Test_updateMappingCommunityIdCcmsIdsInvalidOAuthToken");
		}
	}
	
	/**
	 * @name: update_map	
	 * @param: ClientId, ClientPwd	Oauth token, CommunityId, ccmsIds, non CCH Functional Id
	 * @author: kvnlau@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"}) 
	public void Test_updateMappingCommunityIdCcmsIdsNonCchFnId(){ // Plan independent
		String ccmsId1 = null;
		String ccmsId2 = null;
		String communityId = null;
		boolean isCCH = true;
		String token = null;
		String tokenNonCchFnId = null;
		String responseString = null;
		String error = null;
		String error_message = null;
		
		log.info("Starting API Test method Test_updateMappingCommunityIdCcmsIdsNonCchFnId");
		
		try {
			// Populate variable from users.cvs, clients.csv files.
			PoolClient gbClient = commonClientAllocator.getGroupClient(GC.GbClient,this);
			PoolClient gbClient2 = commonClientAllocator.getGroupClient(GC.GbClient,this);
			
			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
			User nonCchFnIdUser = commonUserAllocator.getGroupUser(GC.nonCchFnIdGroup, this);
			
			ccmsId1 = gbClient.getCCMS_ID(); //"GB001DK0"; // existing client 
			ccmsId2 = gbClient2.getCCMS_ID(); //"GB001DK0"; // existing client

			log.info("Retrieving OAuth2Tokens i.e. 1 with CchFnId & 1 with nonCchFnId");		
			token = getOAuthToken(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), "collabweb_communitymapping_basic_oauth");
			tokenNonCchFnId = getOAuthToken(nonCchFnIdUser.getEmail(), nonCchFnIdUser.getPassword(), "collabweb_communitymapping_readwrite");
			
			JSONObject jsonResponse = null;
			ArrayList<String> ccmsIdsList = null;
			String headers[]={"OAuth-Token", token};
	
			// ensure no mappings exists for cmmsId -------------------------------------
			log.debug("Test setup: checking if community already exists for ccmsId1=" + ccmsId1);
			communityId = getCommunityIdIfMappingExistsForCcmsId(clientIDandSecret_ReadWrite, 
															headers, cchFnIdUser,
															new ConnectionsCommunityAPI().funcIdEmail, 
															ccmsId1);
			
			if (communityId!=null) { 
				log.debug("Test setup: community already exists for ccmsId1=" + ccmsId1 + " so deleting it now");
				deleteMappingCommunityId(clientIDandSecret_ReadWrite, headers, communityId);
			} else {
				log.debug("Test setup: checked that no community exists for ccmsId1=" + ccmsId1);
			}
			
			// ensure no mappings exists for cmmsId2 --------------------------------------
			log.debug("Test setup: checking if community already exists for ccmsId2=" + ccmsId2);
			communityId = getCommunityIdIfMappingExistsForCcmsId(clientIDandSecret_ReadWrite, 
															headers, cchFnIdUser,
															new ConnectionsCommunityAPI().funcIdEmail, 
															ccmsId2);
			
			if (communityId!=null) { 
				log.debug("Test setup: community already exists for ccmsId2=" + ccmsId2 + " so deleting it now");
				deleteMappingCommunityId(clientIDandSecret_ReadWrite, headers, communityId);
			} else {
				log.debug("Test setup: checked that no community exists for ccmsId2=" + ccmsId2);
			}
			
			log.debug("Test setup: create new community");
			// Get existing communityId if ccmsId is mapped to one, or else create new communityId
			communityId = createCommunityIfNoneExistsForCcmsId(clientIDandSecret_ReadWrite, 
															headers, cchFnIdUser,
															new ConnectionsCommunityAPI().funcIdEmail, 
															ccmsId1, isCCH);
			
			log.info("// --------------------- Begin Test ------------------------------------");
			log.info("form body of (post) create single mapping request");		
			String body = "{\"communityId\":\"" + communityId + 
							"\",\"ccmsIds\":[\"" + ccmsId1 + 
							"\"],\"isCCH\":\"" + isCCH + "\"}";
			
			log.info("Create a single mapping first with 1 ccmsId");
			responseString = new HttpUtils().postRequest(getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
															headers, body, contentType);
			try {
				//check for a valid JSON response
				jsonResponse = (JSONObject)new JSONParser().parse(responseString);
			
				System.out.println(responseString);	
				Assert.assertTrue(jsonResponse!=null, "Expected community mapping was not found");			
				log.info("Valid JSON returned.");
				
				if (jsonResponse!=null) {
					ccmsIdsList = (ArrayList)jsonResponse.get("ccmsIds");
				}
				
				Assert.assertEquals((String) jsonResponse.get("communityId"), communityId, "Single community map does not contain expected communityId");
				Assert.assertTrue(ccmsIdsList.contains(ccmsId1), "Single community map does not contain expected sole ccmsId="+ccmsId1);
				Assert.assertTrue(ccmsIdsList.size()==1, "Community map contains more than 1 expected ccmsId");
				
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
			
			log.info("Update (overwrite) mapping with new list of 2 ccmsIds, but with non-functionalId User, so should fail");
			log.info("form body of (post) update single mapping request");		
			body = "{\"communityId\":\"" +  communityId + 
							"\",\"ccmsIds\":[\"" + ccmsId1 + "\",\"" + ccmsId2 +
							"\"],\"isCCH\":\"" + isCCH + "\"}";
			
			// Change token to use non CCH Functional Id 
			headers[1]=tokenNonCchFnId;
			responseString = new HttpUtils().postRequest(getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
															headers, body, contentType,"403");
			try {
				//check for a valid JSON response
				jsonResponse = (JSONObject)new JSONParser().parse(responseString);
				
				System.out.println(responseString);
				Assert.assertTrue(jsonResponse!=null, "Expected community mapping was not found");					
				log.info("Valid JSON returned.");
				
				error = jsonResponse.get("error").toString();
				error_message = jsonResponse.get("error_message").toString();
				
				Assert.assertTrue(error.equals("not_authorized"),"Did not return 'not_authorized' error");
				Assert.assertTrue(error_message.contains("Attempt to make a write operation by unauthorized user"), "Did not get expected error: 'Attempt to make a write operation by unauthorized user'");

			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with get response");
			}
		} finally {
			log.info("Clean up: delete community mapping: '" + communityId + "'");
			responseString = new CommunityMappingRestAPI().deleteCommunityMapping(getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
												token, communityId, null, null, "999"); // '999' ignores internal assert in request
			log.info("End of test method: Test_updateMappingCommunityIdCcmsIdsNonCchFnId");
		}
	}
	
	
	/**
	 * post_oauth_token:	ClientId, ClientPwd (Plan: none)
	 * @author: peterpoliwoda@ie.ibm.com
	 */
//	@Test(groups = {"CCHAPI"})
//	public void Test_postOAuthToken(){  /// DUPLICATE of Test_getOAuthNoPlan()
//		
//		User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
//		
//		log.info("Starting API Test method Test_postOAuthToken");
//		LoginRestAPI loginRestAPI = new LoginRestAPI();
//		
//		log.info("Retrieving OAuth Token.");
//		String token = loginRestAPI.getOAuth2TokenViaAPIManager(getApiManagement() + getOauthExtension() + "?" +
//				clientIDandSecret_NoPlan, cchFnIdUser.getEmail(), 
//				cchFnIdUser.getPassword(),"401");
//		
//		log.info("Token request response:" + token);
//		
//		log.info("End test method Test_postOAuthToken");
//	}
	

	

	

	
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
																getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
																headers[1], 
																communityId, 
																null,//ccmsId=null
																null,// clientIdSecret already included in URL above 
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