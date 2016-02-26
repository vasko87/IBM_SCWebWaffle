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
//import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.ApiBaseTest;
import com.ibm.salesconnect.API.CommunityMappingRestAPI;
import com.ibm.salesconnect.API.ConnectionsCommunityAPI;
import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.API.TestDataHolder;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.common.HttpUtils;

/**
 * @author kvnlau@ie.ibm.com
 * @date Oct 15, 2014
 */
public class CommunityMappingAPITestsDelete extends ApiBaseTest {
	
	Logger log = LoggerFactory.getLogger(CommunityMappingAPITestsDelete.class);
	@Parameters({ "apiExtension", "applicationName", "APIM", "apim_environment" })
	private CommunityMappingAPITestsDelete(@Optional("collab/communityMappings") String apiExtension,
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
	 * @name: delete_map_member_all
	 * @param:	ClientId, ClientPwd, Invalid oauth, CommunityId
	 * @author: kvnlau@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"})
	public void Test_deleteMappingCommunityIdInvalidToken(){ // Plan independent
		String ccmsId = null;
		String communityId = null;
		boolean isCCH = false;
		String token_ReadWrite = null;
		String token_Invalid = null;
		String responseString = null;
		ArrayList<String> ccmsIdsList = null;
			
		try {
			log.info("Starting API Test method Test_deleteMappingCommunityIdInvalidToken");
			
			log.info("Get client test data");
			PoolClient guClient = commonClientAllocator.getGroupClient(GC.GuClient,this);
			
			// Populate variable from users.cvs, clients.csv files.
			ccmsId = guClient.getCCMS_ID(); // "GU02GQJ5"; // existing Client
			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);	
	

			log.info("Retrieving OAuth-Token from plan ReadWrite");		
			LoginRestAPI loginRestAPI = new LoginRestAPI();
			token_ReadWrite = getOAuthToken(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), "collabweb_communitymapping_readwrite");
			
			int lastIndex = token_ReadWrite.length()-1;
			token_Invalid = token_ReadWrite.substring(0, lastIndex-2) + "xxx"; // invalidate a valid token by replacing last 3 characters with fake ones
			
			log.info("Create token_Invalid: '" + token_Invalid + "'");
			
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
				Assert.assertTrue(ccmsIdsList.contains(ccmsId), "Single community map does not contain expected sole ccmsId="+ccmsId);
				Assert.assertTrue(ccmsIdsList.size()==1, "Community map contains more than 1 expected ccmsId");
				
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
			//------- End of test data creation------------------------
						
			//-------------------------- Begin Test: invalid delete-------------------------------------
			log.info("------------------Begin Test: invalid delete--------------------");
			//Set header to invalid token
			log.info("Set invalid token in header");
			headers[1]=token_Invalid;
	
			log.info("Call delete multiple mapping request, using invalidated token (in header), which should fail");		
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("communityId",communityId));
			responseString = restCalls.deleteRequest(getRequestUrl(null, params, clientIDandSecret_ReadWrite), 
														headers, "401"); // expect 401
			try {
				
				//check for a valid JSON response
				jsonResponse = (JSONObject)new JSONParser().parse(responseString);
				
				log.info("Valid JSON returned.");
				System.out.println(responseString);	

				String error = (String) jsonResponse.get("error");
				String errorMesg = (String) jsonResponse.get("error_message");
								
				log.info("Verify no maps returned & get expected 'invalid grant' notification");
				Assert.assertTrue(error.equals("need_login"), "Did not get expected error: 'need_login'");
				Assert.assertTrue(errorMesg.contains("invalid_grant"), "Expected error message didn't contain: 'invalid_grant'");
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
			
			
			log.info("Update header to use proper ReadWrite token");		
			jsonResponse = null;
			//Set token to Read Write plan.
			headers[1]=token_ReadWrite;
			
			// Verify community mapping was not deleted be retrieving it via GET
			// Note: using only ccmsId may have "200" response if ccmsId is mapped to several communities (i.e. bad data)
			// Note: So best to verify using both ccmsId & communityId for finding specific mapping between ccmsId & communityId
			log.info("Try retrieving single mapping, which should NOT have been deleted");
			params.clear();
			params.add(new BasicNameValuePair("communityId",communityId));
			responseString = restCalls.getRequest(getRequestUrl(null, params, clientIDandSecret_ReadWrite),
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
			log.info("End of test method Test_deleteMappingCommunityIdInvalidToken");
		}
	}
	
	/**
	 * @name: delete_map_member
	 * @param:	ClientId, ClientPwd, Invalid oauth, CommunityId, CcmsId
	 * @author: kvnlau@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"})
	public void Test_deleteMappingCommunityIdCcmsIdInvalidToken(){ // Plan independent
		String ccmsId = null;
		String communityId = null;
		boolean isCCH = false;
		String token_ReadWrite = null;
		String token_Invalid = null;
		String responseString = null;
		ArrayList<String> ccmsIdsList = null;	
		
		try {
			log.info("Starting API Test method Test_deleteMappingCommunityIdCcmsIdInvalidToken");
			
			log.info("Get client test data");
			PoolClient guClient = commonClientAllocator.getGroupClient(GC.GuClient,this);
			
			// Populate variable from users.cvs, clients.csv files.
			ccmsId = guClient.getCCMS_ID(); // "GU02GQJ5"; // existing Client
			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);	

			
			log.info("Retrieving OAuth2Token from plan ReadWrite");		
			token_ReadWrite = getOAuthToken(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), "collabweb_communitymapping_readwrite");
			
			int lastIndex = token_ReadWrite.length()-1;
			token_Invalid = token_ReadWrite.substring(0, lastIndex-2) + "xxx"; // invalidate a valid token by replacing last 3 characters with fake ones
			
			log.info("token_Invalid: '" + token_Invalid + "'");
			
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
					ccmsIdsList = (ArrayList)jsonResponse.get("ccmsIds");
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
			log.info("------------------Begin Test--------------------");
			//Set header to invalid token
			headers[1]=token_Invalid;
	
			log.info("Call delete single mapping request, using invalidated token (in header), which should fail");		
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("ccmsId",ccmsId));
			params.add(new BasicNameValuePair("communityId",communityId));
			responseString = new HttpUtils().deleteRequest(getRequestUrl(null, params, clientIDandSecret_ReadWrite),
																headers, "401"); // expect 401
			try {
				
				//check for a valid JSON response
				jsonResponse = (JSONObject)new JSONParser().parse(responseString);
				
				log.info("Valid JSON returned.");
				System.out.println(responseString);	

				String error = (String) jsonResponse.get("error");
				String errorMesg = (String) jsonResponse.get("error_message");
								
				log.info("Verify no maps returned & get expected 'invalid grant' notification");
				Assert.assertTrue(error.equals("need_login"), "Did not get expected error: 'need_login'");
				Assert.assertTrue(errorMesg.contains("invalid_grant"), "Expected error message didn't contain: 'invalid_grant'");
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
			
			
			log.info("Update header to use proper ReadWrite token");		
			jsonResponse = null;
			//Set token to Read Write plan.
			headers[1]=token_ReadWrite;
			
			// Verify community mapping was not deleted be retrieving it via GET
			// Note: using only ccmsId may have "200" response if ccmsId is mapped to several communities (i.e. bad data)
			// Note: So best to verify using both ccmsId & communityId for finding specific mapping between ccmsId & communityId
			log.info("Try retrieving single mapping, which should NOT have been deleted");
			params.clear();
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
			log.info("End of test method Test_deleteMappingCommunityIdCcmsIdInvalidToken");
		}
	}
	
	
	

	
	/**
	 * @name: delete_map_member
	 * @param:	ClientId, ClientPwd (Plan: Read write), CommunityId, non CCH Functional Id
	 * @author: kvnlau@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"})
	public void Test_deleteMappingCommunityIdNonCchFnId(){ // Plan independent
		String ccmsId = null;
		String communityId = null;
		boolean isCCH = true;
		String token_ReadWrite = null;
		String tokenNonCchFnId = null;
		String responseString = null;
		ArrayList<String> ccmsIdsList = null;
		
		try {
			log.info("Starting API Test method Test_deleteMappingCommunityIdNonCchFnId");
			
			// Populate variable from users.cvs, clients.csv files.
			log.info("Get user & client test data");
			PoolClient gbClient = commonClientAllocator.getGroupClient(GC.GbClient,this);
			ccmsId = gbClient.getCCMS_ID(); //"GB001DK0"; // existing client 
			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
			User nonCchFnIdUser = commonUserAllocator.getGroupUser(GC.nonCchFnIdGroup, this);
			
			log.info("Retrieving OAuth2Token from plan Basic_OAuth");		
			token_ReadWrite = getOAuthToken(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), "collabweb_communitymapping_readwrite");
			tokenNonCchFnId = getOAuthToken(nonCchFnIdUser.getEmail(), nonCchFnIdUser.getPassword(), "collabweb_communitymapping_readwrite");
			
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
					ccmsIdsList = (ArrayList)jsonResponse.get("ccmsIds");
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
			log.info("------------------Begin Test--------------------");
			//Set token with non CCH Fn Id user
			headers[1]=tokenNonCchFnId;
	
			log.info("Call delete single mapping request, using token with non CCH fn Id");		
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("communityId",communityId));
			responseString = new HttpUtils().deleteRequest(getRequestUrl(null, params, clientIDandSecret_BasicOauth), 
																headers, "403"); // expected 403 code. Defect 57472 is only relevant to plans so doesn't affect this.
			try {
				
				//check for a valid JSON response
				jsonResponse = (JSONObject)new JSONParser().parse(responseString);
				
				log.info("Valid JSON returned.");
				System.out.println(responseString);	
				
				if (jsonResponse.containsKey("error") && jsonResponse.containsKey("error_message")) {
					String error = (String) jsonResponse.get("error");
					String errorMsg = (String) jsonResponse.get("error_message");
					
					Assert.assertTrue(error.equals("not_authorized"),"Did not return 'not_authorized' message");
					Assert.assertTrue(errorMsg.contains("Attempt to make a write operation by unauthorized user"), 
														"Did not get error mesg: 'Attempt to make a write operation by unauthorized user");
				} else {
					Assert.assertTrue(false, "Response is missing expected keys 'error' & 'error_message'");
				}
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
			
			
			log.info("Retrieving OAuth2Token from plan: 'salesconnect_v210_communityMappings_read_write'");		
			jsonResponse = null;
			//Set token to Read Write plan.
			log.info("Reset token to Read-Write plan");
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
					
					//ArrayList<String> ccmsIdsList = (ArrayList)jsonResponse.get("ccmsIds");
					
					//Assert.assertTrue(new APIUtilities().checkIfValuePresentInJson(getResponseString, "next_offset", 11));
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
			log.info("End of test method Test_deleteMappingCommunityIdNonCchFnId");
		}
	}
	
	/**
	 * @name: delete_map_member
	 * @param:	ClientId, ClientPwd (Plan: Read write), CommunityId, CcmsIs, non CCH Functional Id
	 * @author: kvnlau@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"})
	public void Test_deleteMappingCommunityIdCcmsIdNonCchFnId(){ // Plan independent
		String ccmsId = null;
		String communityId = null;
		boolean isCCH = true;
		String token_ReadWrite = null;
		String tokenNonCchFnId = null;
		String responseString = null;
		ArrayList<String> ccmsIdsList = null;
		 
		try {
			log.info("Starting API Test method Test_deleteMappingCommunityIdCcmsIdNonCchFnId");
			
			// Populate variable from users.cvs, clients.csv files.
			log.info("Get client & user test data");
			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
			User nonCchFnIdUser = commonUserAllocator.getGroupUser(GC.nonCchFnIdGroup, this);
			PoolClient gbClient = commonClientAllocator.getGroupClient(GC.GbClient,this);
			ccmsId = gbClient.getCCMS_ID(); //"GB001DK0"; // existing client 
	
			
			log.info("Set up token1(CCH FunctionalId & read-write) & token2(non-CCH FunctionalId & read-write) ");		
			token_ReadWrite = getOAuthToken(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), "collabweb_communitymapping_readwrite");
			tokenNonCchFnId = getOAuthToken(nonCchFnIdUser.getEmail(), nonCchFnIdUser.getPassword(), "collabweb_communitymapping_readwrite");
			
			log.info("Set header with token1");
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
					ccmsIdsList = (ArrayList)jsonResponse.get("ccmsIds");
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
			log.info("------------------Begin Test--------------------");
			//Set token with non CCH Fn Id user
			log.info("Set header with token2(non-CCH FunctionalId & read-write");
			headers[1]=tokenNonCchFnId;
	
			log.info("Call delete single mapping request, using token with non CCH fn Id, so should fail");
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("ccmsId",ccmsId));
			params.add(new BasicNameValuePair("communityId",communityId));
			responseString = new HttpUtils().deleteRequest(getRequestUrl(null, params, clientIDandSecret_BasicOauth), 
															headers, "403"); // expected 403 code. Defect 57472 related to plans so no relevant here 
			try {
				
				//check for a valid JSON response
				jsonResponse = (JSONObject)new JSONParser().parse(responseString);
				
				log.info("Valid JSON returned.");
				System.out.println(responseString);	
				
//				if (jsonResponse.containsKey("httpMessage") && jsonResponse.containsKey("moreInformation")) {
//					String httpMesg = (String) jsonResponse.get("httpMessage");
//					String infoMesg = (String) jsonResponse.get("moreInformation");
//					
//					Assert.assertTrue(httpMesg.equals("Unauthorized"),"Did not return 'Unauthorized' message");
//					Assert.assertTrue(infoMesg.contains("Not registered to plan"), "Did not get info mesg: 'Not registered to plan");
//				} else {
//					Assert.assertTrue(false, "Response is missing expected keys 'error' & 'error_message'");
//				}
				if (jsonResponse.containsKey("error") && jsonResponse.containsKey("error_message")) {
					String error = (String) jsonResponse.get("error");
					String errorMsg = (String) jsonResponse.get("error_message");
					
					Assert.assertTrue(error.equals("not_authorized"),"Did not return 'not_authorized' message");
					Assert.assertTrue(errorMsg.contains("Attempt to make a write operation by unauthorized user"), 
														"Did not get error mesg: 'Attempt to make a write operation by unauthorized user");
				} else {
					Assert.assertTrue(false, "Response is missing expected keys 'error' & 'error_message'");
				}				

			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
			
			
			log.info("Retrieving OAuth2Token from plan: 'salesconnect_v210_communityMappings_read_write'");		
			jsonResponse = null;
			
			//Set token to Read Write plan.
			log.info("Set header with token1 (with CCH FunctionalId & read-write");
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
			log.info("Clean up: delete community mapping: '" + communityId + "'");
			responseString = new CommunityMappingRestAPI().deleteCommunityMapping(getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
												token_ReadWrite, communityId, null, null, "999"); // '999' ignores internal assert in request
			log.info("End of test method Test_deleteMappingCommunityIdCcmsIdNonCchFnId");
		}
	}
	
	/**
	 * @name: delete_map_member
	 * @param:	ClientId, ClientPwd, NO CommunityId, ccmsId
	 * @author: kvnlau@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"})
	public void Test_deleteMappingCcmsIdNoCommunityId(){ // Plan independent
		String ccmsId = null;
		String communityId = null;
		boolean isCCH = true;
		String token_ReadWrite = null;
		String responseString = null;
		ArrayList<String> ccmsIdsList = null;
		
		try {
			log.info("Starting API Test method Test_deleteMappingCcmsIdNoCommunityId");
			
			// Populate variable from users.cvs, clients.csv files.
			log.info("Get client & user test data");
			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
			PoolClient gbClient = commonClientAllocator.getGroupClient(GC.GbClient,this);
			ccmsId = gbClient.getCCMS_ID(); // "GB000MGB"; // existing Client
			
			log.info("Retrieving OAuth-Token from plan Read Write");		
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
					ccmsIdsList = (ArrayList)jsonResponse.get("ccmsIds");
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
			log.info("Call delete single mapping request, without community Id, which should fail");		
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("ccmsId",ccmsId));
			params.add(new BasicNameValuePair("communityId","")); // missing value
			responseString = new HttpUtils().deleteRequest(getRequestUrl(null, params, clientIDandSecret_ReadWrite), 
													headers, "400"); // Get bad Array instead of body response. See defect 59244.
			try {
				
				//check for a valid JSON response
				jsonResponse = (JSONObject)new JSONParser().parse(responseString);
				
				log.info("Valid JSON returned.");
				System.out.println(responseString);	
				
				if (jsonResponse.containsKey("error") && jsonResponse.containsKey("error_message")) {
					String error = jsonResponse.get("error").toString();
					String error_message = jsonResponse.get("error_message").toString();
					
					Assert.assertTrue(error.equals("invalid_parameter"),"Did not return 'invalid_parameter' error");
					Assert.assertTrue(error_message.contains("Bad parameters: incorrect parameters passed to service"), "Did not get expected error: 'Bad parameters: incorrect parameters passed to service'");
				} else {
					Assert.assertTrue(false, "Response is missing expected keys 'error' & 'error_message'");
				}
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
			log.info("End of test method Test_deleteMappingCcmsIdNoCommunityId");
		}
	}
	
	/**
	 * @name: delete_map_member
	 * @param:	ClientId, ClientPwd, Non-existent CommunityId, ccmsId
	 * @author: kvnlau@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"})
	public void Test_deleteMappingCcmsIdFakeCommunityId(){ // Plan independent
		String ccmsId = null;
		String communityId = null;
		boolean isCCH = true;
		String token_ReadWrite = null;
		String responseString = null;
		ArrayList<String> ccmsIdsList = null;

		log.info("Starting API Test method Test_deleteMappingCcmsIdFakeCommunityId");
		
		try {
			// Populate variable from users.cvs, clients.csv files.
			log.info("Get client & user test data");
			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
			PoolClient gbClient = commonClientAllocator.getGroupClient(GC.GbClient,this);
			ccmsId = gbClient.getCCMS_ID(); // "GB000MGB"; // existing Client

			log.info("Retrieving OAuth-Token from plan Read Write");		
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
					ccmsIdsList = (ArrayList)jsonResponse.get("ccmsIds");
				}
				
				Assert.assertEquals((String) jsonResponse.get("communityId"), communityId, "Single community map does not contain expected communityId");
				Assert.assertTrue(ccmsIdsList.contains(ccmsId), "Single community map does not contain expected sole ccmsId="+ccmsId);
				Assert.assertTrue(ccmsIdsList.size()==1, "Community map contains more than 1 expected ccmsId");
				
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
			//------- End of test data creation------------------------			
			
			//--------- Begin Test -------------------------------------------
			log.info("------------------Begin Test--------------------");
			log.info("Call delete single mapping request, with non-existent community Id, which should fail");		
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("ccmsId",ccmsId));
			params.add(new BasicNameValuePair("communityId","aa00a000-000b-00cc-d000-e0ee0e0e0ee")); // non-existent value
			responseString = new HttpUtils().deleteRequest(getRequestUrl(null, params, clientIDandSecret_ReadWrite), 
																	headers, "404"); // Expect 404 but get 403 error.  See defect 59633.
			try {
				
				//check for a valid JSON response
				jsonResponse = (JSONObject)new JSONParser().parse(responseString);
				
				log.info("Valid JSON returned.");
				System.out.println(responseString);	
				
				if (jsonResponse.containsKey("error") && jsonResponse.containsKey("error_message")) {
					String error = jsonResponse.get("error").toString();
					String error_message = jsonResponse.get("error_message").toString();
					
					Assert.assertTrue(error.equals("not_found"),"Did not return 'not_found' error");
					Assert.assertTrue(error_message.contains("No mappings found for"), "Did not get expected error: 'No mappings found for...'");
				} else {
					Assert.assertTrue(false, "Response is missing expected keys 'error' & 'error_message'");
				}
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
			
			
			log.info("Using OAuth-Token from plan: 'salesconnect_v210_communityMappings_read_only'");		
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
			log.info("End of test method Test_deleteMappingCcmsIdFakeCommunityId");
		}
	}

	/**
	 * @name: delete_map_member
	 * @param:	ClientId, ClientPwd, Non-existent ccmsId, communityId
	 * @author: kvnlau@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"})
	public void Test_deleteMappingCommunityIdFakeCcmsId(){ // Plan independent
		String ccmsId = null;
		String communityId = null;
		boolean isCCH = true;
		String token_ReadWrite = null;
		String responseString = null;
		ArrayList<String> ccmsIdsList = null;
		
		log.info("Starting API Test method Test_deleteMappingCommunityIdFakeCcmsId");
		
		try {
			// Populate variable from users.cvs, clients.csv files.
			log.info("Get client & user test data");
			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
			PoolClient gbClient = commonClientAllocator.getGroupClient(GC.GbClient,this);
			ccmsId = gbClient.getCCMS_ID(); // "GB000MGB"; // existing Client
			
			log.info("Retrieving OAuth-Token from plan Read Write");		
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
					ccmsIdsList = (ArrayList)jsonResponse.get("ccmsIds");
				}
				
				Assert.assertEquals((String) jsonResponse.get("communityId"), communityId, "Single community map does not contain expected communityId");
				Assert.assertTrue(ccmsIdsList.contains(ccmsId), "Single community map does not contain expected sole ccmsId="+ccmsId);
				Assert.assertTrue(ccmsIdsList.size()==1, "Community map contains more than 1 expected ccmsId");
				
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
			//------- End of test data creation------------------------			
			
			//--------- Begin Test --------------------------------
			log.info("------------------Begin Test--------------------");
			log.info("Call delete single mapping request, with non-existent ccms Id, which should fail");		
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("ccmsId","GBGBGBGB"));
			params.add(new BasicNameValuePair("communityId","communityId"));
			responseString = new HttpUtils().deleteRequest(getRequestUrl(null, params, clientIDandSecret_ReadWrite),
																	headers, "404"); // Expect 404
			try {
				
				//check for a valid JSON response
				jsonResponse = (JSONObject)new JSONParser().parse(responseString);
				
				log.info("Valid JSON returned.");
				System.out.println(responseString);	
				
				if (jsonResponse.containsKey("error") && jsonResponse.containsKey("error_message")) {
					String error = jsonResponse.get("error").toString();
					String error_message = jsonResponse.get("error_message").toString();
					Assert.assertTrue(error.equals("not_found"), "Did not get expected error: 'not_found'");
					Assert.assertTrue(error_message.contains("No mappings found for ccmsId"), "Did not get expected error: 'No mappings found for ccmsId'");
				} else {
					Assert.assertTrue(false, "Response is missing expected keys 'error' & 'error_message'");
				}
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
			log.info("End of test method Test_deleteMappingCommunityIdFakeCcmsId");
		}
	}
	
	/**
	 * @name: delete_map_member_all
	 * @param:	ClientId, ClientPwd, no body parameters (i.e. no communityId, nor ccmsId)
	 * @author: kvnlau@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"})
	public void Test_deleteMappingNoBodyParameter(){ // Plan independent
		String ccmsId = null;
		String communityId = null;
		boolean isCCH = true;
		String token_ReadWrite = null;
		String responseString = null;
		ArrayList<String> ccmsIdsList = null;
		
		log.info("Starting API Test method Test_deleteMappingNoBodyParameter");
		try {
			// Populate variable from users.cvs, clients.csv files.
			log.info("Get client & user test data");
			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
			PoolClient gbClient = commonClientAllocator.getGroupClient(GC.GbClient,this);
			ccmsId = gbClient.getCCMS_ID(); // "GB000MGB"; // existing Client
			
			log.info("Retrieving OAuth-Token from plan Read Write");		
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
					ccmsIdsList = (ArrayList)jsonResponse.get("ccmsIds");
				}
				
				Assert.assertEquals((String) jsonResponse.get("communityId"), communityId, "Single community map does not contain expected communityId");
				Assert.assertTrue(ccmsIdsList.contains(ccmsId), "Single community map does not contain expected sole ccmsId="+ccmsId);
				Assert.assertTrue(ccmsIdsList.size()==1, "Community map contains more than 1 expected ccmsId");
				
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
			//------- End of test data creation------------------------			
			
			//-------- Begin Test ---------------------------------------
			log.info("------------------Begin Test--------------------");
			log.info("Call delete multiple mapping request, with no body parameters, which should fail");		
			responseString = new HttpUtils().deleteRequest(getRequestUrl(null, null, clientIDandSecret_ReadWrite), // missing all body parameters
																	headers, "400"); // Get PHP Array instead of JSON response. See defect 58253.
			try {
				
				//check for a valid JSON response
				jsonResponse = (JSONObject)new JSONParser().parse(responseString);
				
				log.info("Valid JSON returned.");
				System.out.println(responseString);	
				
				if (jsonResponse.containsKey("error") && jsonResponse.containsKey("error_message")) {
					String error = jsonResponse.get("error").toString();
					String error_message = jsonResponse.get("error_message").toString();
					
					Assert.assertTrue(error.equals("invalid_parameter"),"Did not return 'invalid_parameter' error");
					Assert.assertTrue(error_message.contains("Bad parameters: incorrect parameters passed to service"), "Did not get expected error: 'Bad parameters: incorrect parameters passed to service'");
				} else {
					Assert.assertTrue(false, "Response is missing expected keys 'error' & 'error_message'");
				}
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
			
			
			log.info("Using OAuth2Token from plan: 'salesconnect_v210_communityMappings_read_write'");		
			jsonResponse = null;
					
			// Verify community mapping was not deleted be retrieving it via GET
			// Note: using only ccmsId may have "200" response if ccmsId is mapped to several communities (i.e. bad data)
			// Note: So best to verify using both ccmsId & communityId for finding specific mapping between ccmsId & communityId
			log.info("Try retrieving single mapping, which should NOT deleted");
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
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
			log.info("End of test method Test_deleteMappingNoBodyParameter");
		}
	}
	
	/**
	 * @name: delete_map_member
	 * @param:	ClientId, ClientPwd, CommunityId, ccmsId, Oauth token
	 * @author: kvnlau@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"})
	public void Test_deleteMappingCommunityIdCcmsId(){ // Plan independent
		String ccmsId1 = null;
		String ccmsId2 = null;
		String communityId = null;
		boolean isCCH = true;
		String token_ReadWrite = null;
		String responseString = null;
		ArrayList<String> ccmsIdsList = null;
		
		log.info("Start of test method Test_deleteMappingCommunityIdCcmsId");
		
		try {
			// Populate variable from users.cvs, clients.csv files.
			log.info("Get client & user test data");
			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
			PoolClient gbClient = commonClientAllocator.getGroupClient(GC.GbClient,this);
			PoolClient gbClient2 = commonClientAllocator.getGroupClient(GC.GbClient,this);
			ccmsId1 = gbClient.getCCMS_ID(); // "GU02GQJ5"; // existing Client
			ccmsId2 = gbClient2.getCCMS_ID(); 
	
			log.info("Retrieving OAuth2Token.");		
			token_ReadWrite = getOAuthToken(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), "collabweb_communitymapping_readwrite");
			
			JSONObject jsonResponse = null;
			String headers[]={"OAuth-Token", token_ReadWrite};
	
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
			
			//---- Create test data, i.e. mapping --------------------------------
			log.info("form body of (post) create single mapping request");		
			String body = "{\"communityId\":\"" + communityId + 
							"\",\"ccmsIds\":[\"" + ccmsId1 + "\",\"" + ccmsId2 + 
							"\"], \"isCCH\":\"" + isCCH + "\"}";
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
				Assert.assertTrue(ccmsIdsList.contains(ccmsId1), "Single community map does not contain expected sole ccmsId1="+ccmsId1);
				Assert.assertTrue(ccmsIdsList.contains(ccmsId2), "Single community map does not contain expected sole ccmsId2="+ccmsId2);
				Assert.assertTrue(ccmsIdsList.size()==2, "Community map did not have expected count of 2 ccmsIds");
				
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
			//------- End of test data creation------------------------	
			
			//-------- Begin Test --------------------------------------
			log.info("------------------Begin Test--------------------");
			log.info("form body of delete single mapping request to remove ONLY 1st ccmsId1=" + ccmsId1 + 
											" from mapping & leaving ccmsId2="+ccmsId2+" in mapping");		
			body = "{\"communityId\":\"" + communityId + "\",\"ccmsIds\": [\"" + ccmsId1 + "\" ],\"isCCH\":\"" + isCCH + "\"}";
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("ccmsId",ccmsId1));
			params.add(new BasicNameValuePair("communityId",communityId));
			responseString = new HttpUtils().deleteRequest(getRequestUrl(null, params, clientIDandSecret_ReadWrite), 
															headers, "200");
	
			try {
				//check for a valid JSON response
				jsonResponse = (JSONObject)new JSONParser().parse(responseString);
				
				log.info("Valid JSON returned.");
				System.out.println(responseString);	

				Assert.assertEquals((String) jsonResponse.get("communityId"), communityId, "Community map (" + communityId + ") incorrectly deleted");
				Assert.assertTrue(responseString.contains(ccmsId2), "Community map should still contain 2nd ccmsId2=" + ccmsId2 + " but was deleted");
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
		} finally {
			log.info("Clean up: delete community mapping: '" + communityId + "'");
			responseString = new CommunityMappingRestAPI().deleteCommunityMapping(getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
												token_ReadWrite, communityId, null, null, "999"); // '999' ignores internal assert in request
			log.info("End of test method Test_deleteMappingCommunityIdCcmsId");
		}
	}
	
	/**
	 * @name: delete_map_member_all
	 * @param; ClientId, ClientPwd, CommunityId, Oauth token
	 * @author: kvnlau@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"})
	public void Test_deleteMappingCommunityId(){ // Plan independent
		String ccmsId = null;
		String communityId = null;
		boolean isCCH = false;
		String token_ReadWrite = null;
		String responseString = null;
		ArrayList<String> ccmsIdsList = null;

		log.info("Start of test method Test_deleteMappingCommunityId");
		
		try {
			// Populate variable from users.cvs, clients.csv files.
			log.info("Get client test data");
			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
			PoolClient guClient = commonClientAllocator.getGroupClient(GC.GuClient,this);
			ccmsId = guClient.getCCMS_ID(); // "GU02GQJ5"; // existing Client
			
			log.info("Retrieving OAuth-Token.");		
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
			log.info("Test-setup: form body of (post) create single mapping request");		
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
					ccmsIdsList = (ArrayList)jsonResponse.get("ccmsIds");
				}
				
				Assert.assertEquals((String) jsonResponse.get("communityId"), communityId, "Single community map does not contain expected communityId");
				Assert.assertTrue(ccmsIdsList.contains(ccmsId), "Single community map does not contain expected sole ccmsId="+ccmsId);
				Assert.assertTrue(ccmsIdsList.size()==1, "Community map contains more than 1 expected ccmsId");
				
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
			//------- End of test data creation------------------------
			
			//--------- Begin Test ------------------------------------
			log.info("------------------Begin Test--------------------");
			log.info("form body of delete single mapping request (with communityId only is identifier)");
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("communityId",communityId));
			responseString = new HttpUtils().deleteRequest(getRequestUrl(null, params, clientIDandSecret_ReadWrite), 
															headers, "200");
			try {
				//check for a valid JSON response
				jsonResponse = (JSONObject)new JSONParser().parse(responseString);
				
				log.info("Valid JSON returned.");
				System.out.println(responseString);	
				
				Assert.assertTrue(jsonResponse==null, "Did not get expected null response body after deletion");
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
			
			// Verify community mapping is now deleted by trying to retrieve it via GET
			// Note: using only ccmsId may have "200" response if ccmsId is mapped to several communities (i.e. bad data)
			// Note: So best to verify using both ccmsId & communityId for finding specific mapping between ccmsId & communityId
			log.info("Try retrieving deleted single mapping.");
			params.clear();
			params.add(new BasicNameValuePair("communityId",communityId));
			responseString = new HttpUtils().getRequest(getRequestUrl(null, params, clientIDandSecret_ReadWrite),
														headers, "404"); // Not found response code: "404"
			try {
				//check for a valid JSON response
				if (responseString!=null){
					jsonResponse = (JSONObject)new JSONParser().parse(responseString);
					
					log.info("Valid JSON returned.");
					System.out.println(responseString);	
		
					String errorMesg = (String) jsonResponse.get("error_message");
					String errorMesg2 = (String) jsonResponse.get("error");
					
					log.info("Verify no maps returned & got 'error message: No results found'");
					Assert.assertTrue(errorMesg.contains("No results found"), "Did not get expected error message: 'No results found'");
					Assert.assertTrue(errorMesg2.contains("not_found"), "Did not get expected error message: 'not_found'");
					Assert.assertTrue(!responseString.contains(communityId), "Failed to delete community mapping where communityId=" + communityId);
				}
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
			
		} finally {
			log.info("Clean up: delete community mapping (if test failed to delete it): '" + communityId + "'");
			responseString = new CommunityMappingRestAPI().deleteCommunityMapping(getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
												token_ReadWrite, communityId, null, null, "999"); // '999' ignores internal assert in request
			log.info("End of test method Test_deleteMappingCommunityId");
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
