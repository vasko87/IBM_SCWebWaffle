package com.ibm.salesconnect.test.CommunityMapping;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.ibm.salesconnect.API.TestDataHolder;
import com.ibm.salesconnect.API.CommunityMappingRestAPI;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.common.HttpUtils;

/**
 * @author kvnlau@ie.ibm.com
 * @date Oct 15, 2014
 */
public class CommunityMappingAPIPortalTestsPost extends ApiBaseTest {
	
	Logger log = LoggerFactory.getLogger(CommunityMappingAPIPortalTestsPost.class);
	@Parameters({ "apiExtension", "applicationName", "APIM", "apim_environment" })
	private CommunityMappingAPIPortalTestsPost(@Optional("collab/communityMappings") String apiExtension,
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
	 * @name: update_map	
	 * @param: ClientId, ClientPwd	Oauth token, CommunityId, ccmsIds
	 * @author: peterpoliwoda@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"})
	public void Test_updateMappingReadOnly(){ // Plan dependnet
		String ccmsId1 = null;
		String ccmsId2 = null;
		String communityId = null;
		boolean isCCH = true;
		String token = null;
		String token_ReadOnly = null;
		String responseString = null;
		
		log.info("Starting API Test method Test_updateMappingBasicReadOnlyNoPlan");
		try {
			// Populate variable from users.cvs, clients.csv files.
			PoolClient gbClient = commonClientAllocator.getGroupClient(GC.GbClient,this);
			PoolClient gbClient2 = commonClientAllocator.getGroupClient(GC.GbClient,this);
			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
			
			ccmsId1 = gbClient.getCCMS_ID(); //"GB001DK0"; // existing client 
			ccmsId2 = gbClient2.getCCMS_ID(); //"GB001C1T"; // existing client

			log.info("Retrieving OAuth2Tokens, i.e. 1 with cchFnIdUser & 1 with nonCchFnIdUser");		
			token = getOAuthToken(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), "collabweb_communitymapping_readwrite");
			token_ReadOnly = getOAuthToken(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), "collabweb_communitymapping_readonly");
		
			JSONObject jsonResponse = null;
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
			log.info("// ----------------------- Begin Test -----------------------------------");	
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
				
				log.info("Valid JSON returned.");
				System.out.println(responseString);	
				
				ArrayList<String> ccmsIdsList = (ArrayList)jsonResponse.get("ccmsIds");
				
				Assert.assertEquals((String) jsonResponse.get("communityId"), communityId, "Single community map does not contain expected communityId");
				Assert.assertTrue(ccmsIdsList.contains(ccmsId1), "Single community map does not contain expected sole ccmsId="+ccmsId1);
				Assert.assertTrue(ccmsIdsList.size()==1, "Community map contains more than 1 expected ccmsId");
				
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
			
			log.info("Form body of (POST) update single mapping request");		
			body = "{\"communityId\":\"" + communityId + 
							"\",\"ccmsIds\":[\"" + ccmsId1 + "\",\"" + ccmsId2 +
							"\"],\"isCCH\":\"" + isCCH + "\"}";
				
			// Set Header to Read-only plan try to update mapping -----------------------
			headers[1]= token_ReadOnly;
			log.info("Headers changed to (read-only plan)" + Arrays.deepToString(headers));
			
			log.info("Update a single mapping with 2 ccmsIds (with a read-only plan), so should fail");
			responseString = new HttpUtils().postRequest(getRequestUrl(null, null, clientIDandSecret_ReadOnly), 
															headers, body, contentType, "401"); //new expected response: 401 instead of 403. See defect 57768
			
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
		} finally {
			log.info("Clean up: delete community mapping: '" + communityId + "'");
			responseString = new CommunityMappingRestAPI().deleteCommunityMapping(getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
												token, communityId, null, null, "999"); // '999' ignores internal assert in request
			log.info("End test method Test_updateMappingReadOnly");
		}
	}
	
	/**
	 * @name: update_map	
	 * @param: ClientId, ClientPwd	OAuth token, CommunityId, ccmsIds
	 * @author: peterpoliwoda@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"})
	public void Test_updateMappingBasicOAuthPlan(){ // Plan dependent
		String ccmsId1 = null;
		String ccmsId2 = null;
		String communityId = null;
		boolean isCCH = true;
		String token = null;
		String token_BasicOAuth = null;
		String responseString = null;
		
		try {
			log.info("Starting API Test method Test_updateMappingBasicOAuthPlan");
			
			// Populate variable from users.cvs, clients.csv files.
			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
			PoolClient gbClient = commonClientAllocator.getGroupClient(GC.GbClient,this);
			PoolClient gbClient2 = commonClientAllocator.getGroupClient(GC.GbClient,this);
			
			ccmsId1 = gbClient.getCCMS_ID(); //"GB001DK0"; // existing client 
			ccmsId2 = gbClient2.getCCMS_ID(); //"GB001C1T"; // existing client
			
			log.info("Retrieving OAuth2Tokens, i.e. 1 with READ_WRITE & 1 with BASIC_OAUTH access");		
			token = getOAuthToken(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), "collabweb_communitymapping_readwrite");
			token_BasicOAuth = getOAuthToken(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), "collabweb_communitymapping_basic_oauth");
			
			JSONObject jsonResponse = null;
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
			
			log.info("// ------------------ Begin Test ------------------------------------------");
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
				
				log.info("Valid JSON returned.");
				System.out.println(responseString);	
				
				ArrayList<String> ccmsIdsList = (ArrayList)jsonResponse.get("ccmsIds");
				
				Assert.assertEquals((String) jsonResponse.get("communityId"), communityId, "Single community map does not contain expected communityId");
				Assert.assertTrue(ccmsIdsList.contains(ccmsId1), "Single community map does not contain expected sole ccmsId="+ccmsId1);
				Assert.assertTrue(ccmsIdsList.size()==1, "Community map contains more than 1 expected ccmsId");
				
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
			
			log.info("Form body of (POST) update single mapping request");		
			body = "{\"communityId\":\"" + communityId + 
							"\",\"ccmsIds\":[\"" + ccmsId1 + "\",\"" + ccmsId2 +
							"\"],\"isCCH\":\"" + isCCH + "\"}";
			
			log.info("Update a single mapping with 2 ccmsIds using Basic OAuth Plan, so should fail");
			headers[1]= token_BasicOAuth;

			log.info("Headers changed to (basic OAuth plan): " + Arrays.deepToString(headers));
			responseString = new HttpUtils().postRequest(getRequestUrl(null, null, clientIDandSecret_BasicOauth), 
															headers, body, contentType, "401"); //new expected response 401 not 403. See defect 57768
			
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
		} finally {
			log.info("Clean up: delete community mapping: '" + communityId + "'");
			responseString = new CommunityMappingRestAPI().deleteCommunityMapping(getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
												token, communityId, null, null, "999"); // '999' ignores internal assert in request
			log.info("End test method Test_updateMappingBasicOAuthPlan");
		}
	}
	
	/**
	 * @name: update_map	
	 * @param: ClientId, ClientPwd	OAuth token, CommunityId, ccmsIds
	 * @author: peterpoliwoda@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"})
	public void Test_updateMappingNoPlan(){ // Plan dependent
		String ccmsId1 = null;
		String ccmsId2 = null;
		String communityId = null;
		boolean isCCH = true;
		String token = null;
		String responseString = null;
		
		try {
			log.info("Starting API Test method Test_updateMappingNoPlan");
			// Populate variable from users.cvs, clients.csv files.
			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
			PoolClient gbClient = commonClientAllocator.getGroupClient(GC.GbClient,this);
			PoolClient gbClient2 = commonClientAllocator.getGroupClient(GC.GbClient,this);
			
			ccmsId1 = gbClient.getCCMS_ID(); //"GB001DK0"; // existing client 
			ccmsId2 = gbClient2.getCCMS_ID(); //"GB001C1T"; // existing client
			
			log.info("Retrieving OAuth2Token.");		
			token = getOAuthToken(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), "collabweb_communitymapping_readwrite");
			
			JSONObject jsonResponse = null;
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
			log.info("// ---------------- Begin Test -------------------------------------------");
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
				
				log.info("Valid JSON returned.");
				System.out.println(responseString);	
				
				ArrayList<String> ccmsIdsList = (ArrayList)jsonResponse.get("ccmsIds");
				
				Assert.assertEquals((String) jsonResponse.get("communityId"), communityId, "Single community map does not contain expected communityId");
				Assert.assertTrue(ccmsIdsList.contains(ccmsId1), "Single community map does not contain expected sole ccmsId="+ccmsId1);
				Assert.assertTrue(ccmsIdsList.size()==1, "Community map contains more than 1 expected ccmsId");
				
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
			
			log.info("Form body of (POST) update single mapping request");		
			body = "{\"communityId\":\"" + communityId + 
							"\",\"ccmsIds\":[\"" + ccmsId1 + "\",\"" + ccmsId2 +
							"\"],\"isCCH\":\"" + isCCH + "\"}";
			
			log.info("Update a single mapping with 1 ccmsId with no Plan, so should fail");
			responseString = new HttpUtils().postRequest(getRequestUrl(null, null, clientIDandSecret_NoPlan), 
														headers, body, contentType, "401"); //Expected response is now 401 & not 403 (see defect 57771)
			
			try {
				//check for a valid JSON response
				jsonResponse = (JSONObject)new JSONParser().parse(responseString);
				
				log.info("Valid JSON returned.");
				System.out.println(responseString);	

				String httpMesg = (String) jsonResponse.get("httpMessage");
				String infoMesg = (String) jsonResponse.get("moreInformation");
				
				log.info("Verify no maps returned & got 'error message: No results found'");
				Assert.assertTrue(httpMesg.contains("Unauthorized"), "Did not get expected http message: 'Unauthorized'");
				Assert.assertTrue(infoMesg.contains("Client id not registered."), "Did not get expected info message: 'Not registered to plan' or 'Client id not registered.'");
				
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}		
		} finally {
			log.info("Clean up: delete community mapping: '" + communityId + "'");
			responseString = new CommunityMappingRestAPI().deleteCommunityMapping(getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
												token, communityId, null, null, "999"); // '999' ignores internal assert in request
			log.info("End test method: Test_updateMappingNoPlan()");
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