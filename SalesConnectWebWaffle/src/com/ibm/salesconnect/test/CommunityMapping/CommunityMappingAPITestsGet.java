package com.ibm.salesconnect.test.CommunityMapping;

//import java.io.IOException;	// for demo test: Test_getSingleMappingCcmsIdDemo
//import java.sql.SQLException;	// for demo test: Test_getSingleMappingCcmsIdDemo
//import com.ibm.salesconnect.API.LoginRestAPI;
//import com.ibm.salesconnect.common.DB2Connection;	// for demo test: Test_getSingleMappingCcmsIdDemo
//import com.ibm.salesconnect.common.FileUtility;	// for demo test: Test_getSingleMappingCcmsIdDemo

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
import org.testng.annotations.Test;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

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
public class CommunityMappingAPITestsGet extends ApiBaseTest {
	
	Logger log = LoggerFactory.getLogger(CommunityMappingAPITestsGet.class);
	@Parameters({ "apiExtension", "applicationName", "APIM", "apim_environment" })
	private CommunityMappingAPITestsGet(@Optional("collab/communityMappings") String apiExtension,
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
	 * @name: Test_getSingleMappingCcmsIdDemo
	 * @description: get_map_single - prototype test method
	 * @param:	ClientId, ClientPwd, ccmsid, Oauth token
	 * @author: kvnlau@ie.ibm.com
	 */
//	@Test(groups = {"DEMO"})    // DEMO TEST & NOT REAL TEST METHOD
//	public void Test_getSingleMappingCcmsIdDemo(){
//		
//		String ccmsId = null;
//		String communityId = null;
//		boolean isCCH = true;
//		String token = null;
//		String responseString = null;
//		String[] memberList = null;
//		User db2User = commonUserAllocator.getGroupUser(GC.db2UserGroup,this);
//		String[] gbClientList = null;
//		String[] guClientList = null;
//		String filePath = "C:\\Users\\Administrator\\RTCWorkspace\\SalesConnect\\SalesConnectWebWaffle\\test_config\\extensions\\client\\clients.csv";
//
//		try {
//			log.info("retrieve 3 GBClients and 3 GUClients from DB");
//			DB2Connection dbConn = new DB2Connection(getParameter(GC.db2URL),db2User.getUid(),db2User.getPassword());
//			guClientList = dbConn.retrieveGuClientIds(8);
//			gbClientList = dbConn.retrieveGbClientIds(8);
//			
//			// Update the Client.csv file with 3 GB & GU client & deleting any existing old ones
//			FileUtility.updateClientCsvForCchApi(filePath, "," , "GBCLIENT", "GUCLIENT", guClientList, gbClientList);
//		} catch (IOException ioEx) {
//			ioEx.printStackTrace();
//			Assert.assertTrue(false, "File access exception");
//		} catch (SQLException sqlEx) {
//			sqlEx.printStackTrace();
//			Assert.assertTrue(false, "SQL DB exception");
//		}
//		
//		try {
//			PoolClient gbClient = commonClientAllocator.getGroupClient(GC.GbClient,this);		
//			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
//			User noncchFnIdUser = commonUserAllocator.getGroupUser(GC.nonCchFnIdGroup, this);
//			
//			// Populate variable from users.cvs, clients.csv files.
//			ccmsId = gbClient.getCCMS_ID(); //"GB0012AS"; // existing Client mapped to a community
//			
//			// create new community
//			ConnectionsCommunityAPI connApi= new ConnectionsCommunityAPI();
//			communityId = connApi.createConnectionsCommunity(cchFnIdUser.getEmail(),
//															cchFnIdUser.getPassword(),getCnxnCommunity());
//			log.info("Created communityUuId: " + communityId);
//
//			log.info("Adding new community member: " + connApi.funcIdEmail);
//			responseString = connApi.addUserConnectionsCommunity(cchFnIdUser.getEmail(), 
//																cchFnIdUser.getPassword(), 
//																connApi.funcIdEmail, 
//																communityId, getCnxnCommunity());
//
//			log.info("Get update members of community: " + communityId);
//			memberList = connApi.getUsersConnectionsCommunity(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), communityId, getCnxnCommunity());
//					
//			log.info("Retrieving OAuth2Token.");		
//			LoginRestAPI loginRestAPI = new LoginRestAPI();
//			token = loginRestAPI.getOAuth2TokenViaAPIManager(getApiManagement() + getOauthExtension() + 
//															"?" +clientIDandSecret_ReadWrite, 
//															cchFnIdUser.getEmail(), cchFnIdUser.getPassword(),"200");
//	
//			JSONObject jsonResponse = null;
//			String headers[]={"OAuth-Token", token};
//			
//			log.info("SETUP: Create a mapping ");
//			log.info("form body of (post) create single mapping request");		
//			String body = "{\"communityId\":\"" + communityId + 
//							"\",\"ccmsIds\":[\"" + ccmsId + 
//							"\"],\"isCCH\":\"" + isCCH + "\"}";
//			
//			HttpUtils restCalls = new HttpUtils();
//			responseString = restCalls.postRequest(getApiManagement() + getMappingExtension() + "?" + 
//															clientIDandSecret_ReadWrite, 
//															headers, body, contentType); //ERROR 520, see defect 68127
//			
//			log.info("Retrieving single mapping.");
//			
//			String getResponseString = restCalls.getRequest(getApiManagement() + getMappingExtension() + "?" + 
//															clientIDandSecret_ReadWrite + "&" + "ccmsId=" + ccmsId, 
//															headers, "200");
//			try {
//				//check for a valid JSON response
//				jsonResponse = (JSONObject)new JSONParser().parse(getResponseString);
//				log.info("Valid JSON returned.");
//				System.out.println(getResponseString);	
//			} catch (Exception e) {
//				e.printStackTrace();
//				Assert.assertTrue(false, "Parse exception with post response");
//			}
//			
//			ArrayList<String> ccmsIdsList = (ArrayList)jsonResponse.get("ccmsIds");
//			
//			//Assert.assertTrue(new APIUtilities().checkIfValuePresentInJson(getResponseString, "next_offset", 11));
//			Assert.assertEquals((String) jsonResponse.get("communityId"), communityId, "Single community map does not contain expected communityId");
//			Assert.assertTrue(ccmsIdsList.contains(ccmsId), "Single community map does not contain expected sole ccmsId="+ccmsId);
//			Assert.assertTrue(ccmsIdsList.size()==1, "Community map contains more than 1 expected ccmsId");
//		
//		} catch (Exception e) {
//			e.printStackTrace();
//			Assert.assertTrue(false, "exception with post calls");
//		} finally {
//			log.info("Clean up: delete community mapping: '" + ccmsId +  " | " + communityId + "'");
//			responseString = new CommunityMappingRestAPI().deleteCommunityMapping(getApiManagement() + getMappingExtension(), 
//												token, communityId, null, clientIDandSecret_ReadWrite, "200");
//		}
//		
//		log.info("End test method Test_getSingleMappingCcmsIdDemo");
//	}
	
	/**
	 * @name: get_map_single
	 * @param:	ClientId, ClientPwd, ccmsid, Oauth token
	 * @author: kvnlau@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"})
	public void Test_getSingleMappingCcmsId(){ // Plan independent
		
		String ccmsId = null;
		String communityId = null;
		boolean isCCH = true;
		String token = null;
		String responseString = null;
		
		log.info("Start test method Test_getSingleMappingCcmsId");
		try {
			HttpUtils restCalls = new HttpUtils();
			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
			PoolClient gbClient = commonClientAllocator.getGroupClient(GC.GbClient,this);
			ccmsId = gbClient.getCCMS_ID();
			
			log.info("Retrieving OAuth2Token.");		
			token = getOAuthToken(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), "collabweb_communitymapping_readwrite");
			
			JSONObject jsonResponse = null;
			String headers[]={"OAuth-Token", token};
			
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
					} catch (ParseException e) {
						e.printStackTrace();
						Assert.assertTrue(false, "Parse exception with response");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				log.info("Caught exception");
			} finally {
				log.info("Finally do nothing");
			}
			
			if (communityId==null) {
				// create new community
				ConnectionsCommunityAPI connApi= new ConnectionsCommunityAPI();
				communityId = connApi.createConnectionsCommunity(cchFnIdUser.getEmail(),
																cchFnIdUser.getPassword(),getCnxnCommunity());
				log.info("Created communityUuId: " + communityId + " with owner " + cchFnIdUser.getEmail());
	
				log.info("Adding new community members: " + connApi.funcIdEmail + " & " + cchFnIdUser.getEmail());
				responseString = connApi.addUserConnectionsCommunity(cchFnIdUser.getEmail(), 
																	cchFnIdUser.getPassword(), 
																	connApi.funcIdEmail,
																	communityId, getCnxnCommunity());

				log.info("SETUP: Create a mapping ");
				log.info("form body of (post) create single mapping request");		
				String body = "{\"communityId\":\"" + communityId + 
								"\",\"ccmsIds\":[\"" + ccmsId + 
								"\"],\"isCCH\":\"" + isCCH + "\"}";
				
				responseString = restCalls.postRequest(getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
																headers, body, contentType);
			}
			
			log.info("Retrieving single mapping.");
	    	ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
	    	params.add(new BasicNameValuePair("ccmsId",ccmsId));
			responseString = restCalls.getRequest(getRequestUrl(null, params, clientIDandSecret_ReadWrite), 
													headers, "200");
			try {
				//check for a valid JSON response
				jsonResponse = (JSONObject)new JSONParser().parse(responseString);
				log.info("Valid JSON returned.");
				System.out.println(responseString);	
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			} 
			
			ArrayList<String> ccmsIdsList = (ArrayList)jsonResponse.get("ccmsIds");
			Assert.assertEquals((String) jsonResponse.get("communityId"), communityId, "Single community map does not contain expected communityId");
			Assert.assertTrue(ccmsIdsList.contains(ccmsId), "Single community map does not contain expected sole ccmsId="+ccmsId);
			Assert.assertTrue(ccmsIdsList.size()==1, "Community map contains more than 1 expected ccmsId");
		
		} finally {
			log.info("Clean up: delete community mapping: '" + ccmsId +  " | " + communityId + "'");
			responseString = new CommunityMappingRestAPI().deleteCommunityMapping(getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
																					token, communityId, null, null, "999"); // '999' ignores internal assert in request
			log.info("End test method Test_getSingleMappingCcmsId");
		}
		
	}
	
	/**
	 * @name: get_map_single
	 * @param:	ClientId, ClientPwd, ccmsId, Oauth token
	 * @author: peterpoliwoda@ie.ibm.com
	 **/
	@Test(groups = {"CCHAPI"})
	public void Test_getSingleMappingInvalidCcmsId(){ // Plan independent
		String ccmsId = "GBXXXXXX"; // non-existing Client mapped to a community
		JSONObject jsonResponse = null;
		Integer mapCount = 0;
		
		log.info("Starting API Test method Test_getSingleMappingInvalidCcmsId");
		try {
			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
			
			log.info("Retrieving OAuth2Token.");		
			String token = getOAuthToken(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), "collabweb_communitymapping_readwrite");
			
			String headers[]={"OAuth-Token", token};
			
			log.info("Retrieving single mapping, but expect 404 response, due to invalid communityId");
			HttpUtils restCalls = new HttpUtils();
	    	ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
	    	params.add(new BasicNameValuePair("ccmsId",ccmsId));
			String getResponseString = restCalls.getRequest(getRequestUrl(null, params,clientIDandSecret_ReadWrite), 
															headers, "404");
			try {
				//check for a valid JSON response
				jsonResponse = (JSONObject)new JSONParser().parse(getResponseString);
				log.info("Valid JSON returned.");
				System.out.println(getResponseString);
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
			
			if (jsonResponse.containsKey("count")) {
				mapCount = (Integer) jsonResponse.get("count");
				//System.out.println("It's true");
			}
			
			String errorMesg = (String) jsonResponse.get("error_message");
			String errorMesg2 = (String) jsonResponse.get("error");
			
			log.info("Verify no maps returned & got 'error message: No results found'");
			Assert.assertTrue(errorMesg.contains("No results found"), "Did not get expected error message: 'No results found'");
			Assert.assertTrue(errorMesg2.contains("not_found"), "Did not get expected error message: 'not_found'");
			Assert.assertTrue(mapCount==0, "No community maps should be returned when using invalid communityId");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			log.info("End test method Test_getSingleMappingInvalidCcmsId");
		}
	}

	/*
	 * @name: get_map_single
	 * @param:	ClientId, ClientPwd, ccmsId, count, Oauth token
	 * @author: peterpoliwoda@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"})
	public void Test_getSingleMappingInvalidIndex(){ // Plan independent
		
		JSONObject jsonResponse = null;
		Integer mapCount = 0;
		
		log.info("Starting API Test method Test_getSingleMappingInvalidIndex");
		try {
			// Populate variable from users.cvs file(s).
			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);

			log.info("Retrieving OAuth2Token.");		
			String token = getOAuthToken(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), "collabweb_communitymapping_readwrite");
			
			String headers[]={"OAuth-Token", token};
			
			log.info("Retrieving single mapping, but expect 422 response, due to invalid index parameter");
			HttpUtils restCalls = new HttpUtils();
	    	ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
	    	params.add(new BasicNameValuePair("count","1"));
	    	params.add(new BasicNameValuePair("startIndex","z"));
			String getResponseString = restCalls.getRequest(getRequestUrl(null, params, clientIDandSecret_ReadWrite), 
															headers, "422");
			try {
				//check for a valid JSON response
				jsonResponse = (JSONObject)new JSONParser().parse(getResponseString);
				log.info("Valid JSON returned.");
				System.out.println(getResponseString);
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
			
			if (jsonResponse.containsKey("count")) {
				mapCount = (Integer) jsonResponse.get("count");
			}
			
			String errorMesg = (String) jsonResponse.get("error_message");
			
			log.info("Verify no maps returned & got 'error message: Bad request'");
			Assert.assertTrue(errorMesg.contains("Bad request"), "Did not get expected error message: 'Bad request'");
			Assert.assertTrue(mapCount==0, "No community maps should be returned when using invalid communityId");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			log.info("End test method Test_getSingleMappingInvalidIndex");
		}
	}
	
	/**
	 * @name: get_map_single
	 * @param:	ClientId, ClientPwd, CommunityId, Oauth token
	 * @author: kvnlau@ie.ibm.com
	 **/
	@Test(groups = {"CCHAPI"})
	public void Test_getSingleMappingCommunityId(){ // Plan independent
		
		String ccmsId = null;
		String communityId = null;
		boolean isCCH = true;
		String token = null;
		String responseString = null;

		log.info("Start test method: Test_getSingleMappingCommunityId()");
		try {
			// Populate variable from users.cvs & clients.csv file(s).
			PoolClient gbClient = commonClientAllocator.getGroupClient(GC.GbClient,this);		
			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
			ccmsId = gbClient.getCCMS_ID();

			log.info("Retrieving OAuth2Token.");		
			token = getOAuthToken(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), "collabweb_communitymapping_readwrite");
			
			JSONObject jsonResponse = null;
			String headers[]={"OAuth-Token", token};
			
			try {
				// check if community mapping exists, if not will throw exception that must be caught
				// so test method will not stop
				// Note: this is necessary due to defect 69028: Not able to over-write an existing connections community mapping
				// Note: this try will jump to finally & then outer finally if ccmsId does not exist
		    	ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		    	params.add(new BasicNameValuePair("ccmsId",ccmsId));
				responseString = new HttpUtils().getRequest(getRequestUrl(null, params, clientIDandSecret_ReadWrite),
														headers, "999"); // '999' ignores internal assert in request
				
				if (responseString.contains("communityId") && responseString.contains("ccmsIds")) {
					try {
						//check for a valid JSON response
						jsonResponse = (JSONObject)new JSONParser().parse(responseString);
						communityId = (String)jsonResponse.get("communityId");
					} catch (ParseException e) {
						e.printStackTrace();
						Assert.assertTrue(false, "Parse exception with response");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} 

			if (communityId==null) {
				// create new community
				ConnectionsCommunityAPI connApi= new ConnectionsCommunityAPI();
				communityId = connApi.createConnectionsCommunity(cchFnIdUser.getEmail(),
																cchFnIdUser.getPassword(),getCnxnCommunity());
				log.info("Created communityUuId: " + communityId + " with owner " + cchFnIdUser.getEmail());
				
				log.info("Adding new community members: " + connApi.funcIdEmail + " & " + cchFnIdUser.getEmail());
				responseString = connApi.addUserConnectionsCommunity(cchFnIdUser.getEmail(), 
																	cchFnIdUser.getPassword(), 
																	connApi.funcIdEmail,
																	communityId, getCnxnCommunity());

				log.info("SETUP: Create a mapping ");
				log.info("form body of (post) create single mapping request");		
				String body = "{\"communityId\":\"" + communityId + 
								"\",\"ccmsIds\":[\"" + ccmsId + 
								"\"],\"isCCH\":\"" + isCCH + "\"}";
				
				responseString = new HttpUtils().postRequest(getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
																headers, body, contentType);
			}
			
			log.info("Retrieving single mapping.");
	    	ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
	    	params.add(new BasicNameValuePair("communityId",communityId));
			String getResponseString = new HttpUtils().getRequest(getRequestUrl(null, params, clientIDandSecret_ReadWrite), 
															headers, "200");
			try {
				//check for a valid JSON response
				jsonResponse = (JSONObject)new JSONParser().parse(getResponseString);
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
			
			log.info("Valid JSON returned.");
			System.out.println(getResponseString);	
			
			ArrayList<String> ccmsIdsList = (ArrayList)jsonResponse.get("ccmsIds");
			Assert.assertEquals((String) jsonResponse.get("communityId"), communityId, "Single community map does not contain expected communityId");
			Assert.assertTrue(ccmsIdsList.size()>0, "Community map contains no ccmsId");
		} finally {
			log.info("End test method Test_getSingleMappingCcmsId");
			log.info("Clean up: delete community mapping: '" + ccmsId +  " | " + communityId + "'");
			responseString = new CommunityMappingRestAPI().deleteCommunityMapping(getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
												token, communityId, null, null, "999"); // '999' ignores internal assert in request
		}
	}

	/*
	 * @name: get_map_single
	 * @param:	ClientId, ClientPwd, CommunityId, ccmsId (ignored), Oauth token
	 * @author: kvnlau@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"})
	public void Test_getSingleMappingCommunityIdIgnoreCcmsId(){ // Plan independent

		String ccmsId = null; // existing Client "GU02FKGQ" mapped to a community, but use fake one
		String communityId = null;
		boolean isCCH = true;
		String token = null;
		String responseString = null;
		
		try {
			// Populate variable from users.csv & clients.csv file(s).
			PoolClient gbClient = commonClientAllocator.getGroupClient(GC.GbClient,this);
			ccmsId = gbClient.getCCMS_ID(); //"GB0012AS"; // existing Client 
			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);

			log.info("Retrieving OAuth2Token.");		
			token = getOAuthToken(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), "collabweb_communitymapping_readwrite");	
			
			JSONObject jsonResponse = null;
			String headers[]={"OAuth-Token", token};
			
			try {
				// check if community mapping exists, if not will throw exception that must be caught
				// so test method will not stop
				// Note: this is necessary due to defect 69028: Not able to over-write an existing connections community mapping
				log.info("Checking if community exists for ccmsId=" + ccmsId);
		    	ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		    	params.add(new BasicNameValuePair("ccmsId",ccmsId));
		    	
				responseString = new HttpUtils().getRequest(getRequestUrl(null, params, clientIDandSecret_ReadWrite),
														headers, "999"); // '999' ignores internal assert in request
				
				if (responseString.contains("communityId") && responseString.contains("ccmsIds")) {
					try {
						//check for a valid JSON response
						jsonResponse = (JSONObject)new JSONParser().parse(responseString);
						communityId = (String)jsonResponse.get("communityId");
					} catch (ParseException e) {
						e.printStackTrace();
						Assert.assertTrue(false, "Parse exception with response");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}
			
			if (communityId==null) {
				// create new community
				ConnectionsCommunityAPI connApi= new ConnectionsCommunityAPI();
				communityId = connApi.createConnectionsCommunity(cchFnIdUser.getEmail(),
																cchFnIdUser.getPassword(),getCnxnCommunity());
				log.info("Created communityUuId: " + communityId + " with owner " + cchFnIdUser.getEmail());
				
				log.info("Adding new community members: " + connApi.funcIdEmail + " & " + cchFnIdUser.getEmail());
				responseString = connApi.addUserConnectionsCommunity(cchFnIdUser.getEmail(), 
																	cchFnIdUser.getPassword(), 
																	connApi.funcIdEmail,
																	communityId, getCnxnCommunity());

				log.info("SETUP: Create a mapping ");
				log.info("form body of (post) create single mapping request");		
				String body = "{\"communityId\":\"" + communityId + 
								"\",\"ccmsIds\":[\"" + ccmsId + 
								"\"],\"isCCH\":\"" + isCCH + "\"}";
		    	
				responseString = new HttpUtils().postRequest(getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
																headers, body, contentType);
			}
			
			ccmsId = "GU02FAKE"; // existing Client "GU02FKGQ" mapped to a community, but use fake one
			
			log.info("Retrieving single mapping using communityId & ccmdsId (contains fake value but should be ignored)");

			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("ccmsId",ccmsId));
			params.add(new BasicNameValuePair("communityId",communityId));
			String getResponseString = new HttpUtils().getRequest(getRequestUrl(null, params, clientIDandSecret_ReadWrite), 
															headers, "200");
			try {
				//check for a valid JSON response
				jsonResponse = (JSONObject)new JSONParser().parse(getResponseString);
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
			
			log.info("Valid JSON returned.");
			System.out.println(getResponseString);	
			
			ArrayList<String> ccmsIdsList = (ArrayList)jsonResponse.get("ccmsIds");
			Assert.assertEquals((String) jsonResponse.get("communityId"), communityId, "Single community map does not contain expected communityId");
			Assert.assertTrue(!ccmsIdsList.contains(ccmsId), "Community map should not contain our fake ccmsId");
		} finally {
			log.info("Clean up: delete community mapping: '" + ccmsId +  " | " + communityId + "'");
			responseString = new CommunityMappingRestAPI().deleteCommunityMapping(getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
												token, communityId, null, null, "999"); // '999' ignores internal assert in request
			log.info("End test method Test_getSingleMappingCommunityIdIgnoreCcmsId");
		}
		
	}
	
	/**
	 * @name: get_map_multi
	 * @param:	ClientId, ClientPwd, Oauth token
	 * @author: kvnlau@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"})
	public void Test_getMapMultiDefault(){ // Plan independent
		
		log.info("Start test method Test_getMapMulti.");
		log.info("Getting user.");
		long mapCount = 0;
		
		try {
			// Populate variable from users.csv file(s).
			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
			
			log.info("Retrieving OAuth2Token.");		
			String token = getOAuthToken(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), "collabweb_communitymapping_readwrite");
			
			JSONObject jsonResponse = null;
			String headers[]={"OAuth-Token", token};
			
			// Application: collabweb_communitymapping_readwrite
			log.info("Retrieving multiple maps");
			HttpUtils restCalls = new HttpUtils();
			String getResponseString = restCalls.getRequest(getRequestUrl(null, null, clientIDandSecret_ReadWrite),
															headers, "200");
			
			try {
				//check for a valid JSON response
				jsonResponse = (JSONObject)new JSONParser().parse(getResponseString);
				
				if (jsonResponse.containsKey("count")) {
					mapCount = (Long) jsonResponse.get("count");
				}
				
				// check that multiple community maps are returned
				Assert.assertTrue(mapCount == 50, "Default of 50 community maps were not found");
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
			
			log.info("Valid JSON returned.");
			System.out.println(getResponseString);	
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			log.info("End test method Test_getMapMulti");
		}
	}
	
	/** 
	 * @name: get_map_multi
	 * @param:	ClientId, ClientPwd, Count=300, Oauth token
	 * Pre-requisite: the over 100 mappings must exist in SalesConnect DB
	 * @author: kvnlau@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"})
	public void Test_getMapMultiMax(){ // Plan independent
		log.info("Start test method Test_getMapMultiMax");
		log.info("Getting user.");
		
		long mapCount = 0;
		int count = 300;
	
		try {
			// Populate variable from users.csv file(s).
			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
		
			log.info("Retrieving OAuth2Token.");		
			String token = getOAuthToken(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), "collabweb_communitymapping_readwrite");
	
			JSONObject jsonResponse = null;
			String headers[]={"OAuth-Token", token};
			
			// Application: collabweb_communitymapping_readwrite
			log.info("Retrieving multiple maps");
	    	ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
	    	params.add(new BasicNameValuePair("count",Integer.toString(count)));
			String getResponseString = new HttpUtils().getRequest(getRequestUrl(null, params, clientIDandSecret_ReadWrite),
															headers, "200");
			try {
				//check for a valid JSON response
				jsonResponse = (JSONObject)new JSONParser().parse(getResponseString);
				
				if (jsonResponse.containsKey("count")) {
					mapCount = (Long) jsonResponse.get("count");
				}
				
				// check that multiple community maps are returned
				Assert.assertTrue(mapCount <= 100, "Returned more than max of 100 community maps");
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
			
			log.info("Valid JSON returned.");
			System.out.println(getResponseString);	
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			log.info("End test method Test_getMapMultiMax");
		}
	}

	
	/** 
	 * @name: get_map_multi
	 * @param:	ClientId, ClientPwd, Count=3 and Count=1, startIndex=0 and startIndex=1, OAuth token
	 * Pre-requisite: At least 3 mappings in the database
	 * @author: peterpoliwoda@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"})
	public void Test_getMapMultiStartIndexCount(){ // Plan independent
		log.info("Start test method Test_getMapMultiStartIndexCount");
		log.info("Getting user.");
		User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
		long mapCount = 0;
		long countMulti = 3;
		long countSingle = 1;
		long startIndexMulti = 0;
		long startIndexSingle = 1;
		JSONArray ccmsIdsList = null;
		ArrayList<String> ccmsIdArrayList = new ArrayList<String>();
		
		String ccmsId = null;

		log.info("Retrieving OAuth2Token.");		
		String token = getOAuthToken(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), "collabweb_communitymapping_readwrite");

		JSONObject jsonResponse = null;
		String headers[]={"OAuth-Token", token};
		
		// Application: collabweb_communitymapping_readwrite
		log.info("Retrieving multiple maps");

		HttpUtils restCalls = new HttpUtils();
    	ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("count",String.valueOf(countMulti)));
    	params.add(new BasicNameValuePair("startIndex",String.valueOf(startIndexMulti)));
		String getResponseStringMulti = restCalls.getRequest(getRequestUrl(null, params, clientIDandSecret_ReadWrite),														
														headers, "200");
		
		params.clear();
		params.add(new BasicNameValuePair("count",String.valueOf(countSingle)));
    	params.add(new BasicNameValuePair("startIndex",String.valueOf(startIndexSingle)));
		String getResponseStringSingle = restCalls.getRequest(getRequestUrl(null, params, clientIDandSecret_ReadWrite),														
														headers, "200");
		try {
			//check for a valid JSON response
			jsonResponse = (JSONObject)new JSONParser().parse(getResponseStringMulti);
			//ArrayList<String> mappings = (ArrayList)jsonResponse.get("mappings");
			ccmsIdsList = (JSONArray)jsonResponse.get("mappings");
			
			for(Object o : ccmsIdsList){
				 JSONObject jsonLineItem = (JSONObject) o;
				 JSONArray ja = (JSONArray) jsonLineItem.get("ccmsIds");
				 String number = (String) ja.get(0); 
				 log.debug("Number: " + number);
				 ccmsIdArrayList.add(number);	 
			}
			
			if (jsonResponse.containsKey("count")) {
				mapCount = (Long) jsonResponse.get("count");
				startIndexMulti = (Long) jsonResponse.get("startIndex");
			}
			
			log.debug("Map Count for multiple: " + mapCount);
			// check that multiple community maps are returned
			Assert.assertTrue(mapCount <= countMulti, "Returned more than " + countMulti + "community maps");

			jsonResponse = (JSONObject)new JSONParser().parse(getResponseStringSingle);
			JSONArray mappings = (JSONArray) jsonResponse.get("mappings");
			
			JSONObject ccmsIdSingle1 = (JSONObject) mappings.get(0);
			JSONArray ccmsIdsArray = (JSONArray) ccmsIdSingle1.get("ccmsIds");
			ccmsId = (String) ccmsIdsArray.get(0);

			
			log.info("CCMS single: " + ccmsId);
			
			if (jsonResponse.containsKey("count")) {
				mapCount = (Long) jsonResponse.get("count");
				startIndexSingle = (Long) jsonResponse.get("startIndex");	
			}
			log.debug("Map Count for single: " + mapCount);
			// check that a single community maps is returned
			Assert.assertTrue(mapCount <= 1, "Returned more than 1 community maps for single get");
			
		} catch (ParseException e) {
			e.printStackTrace();
			Assert.assertTrue(false, "Parse exception with post response");
		}
		
		log.info("Valid JSON returned for Multi: ");
		System.out.println(getResponseStringMulti);
		log.info("Valid JSON returned for Single: ");
		System.out.println(getResponseStringSingle);
		
		log.info("Checking if single ccmsId is included in the list of multiple mappings. ");
		log.info("ccmsIdsList array dump: " + ccmsIdArrayList);
		Assert.assertTrue(ccmsIdArrayList.contains(ccmsId),"CcmsId not found in the collected list.");
		
		log.info("End test method Test_getMapMultiStartIndexCount");
	}

	/** 
	 * @name: get_map_multi
	 * @param:	ClientId, ClientPwd, Count=3 and Count=1, startIndex=99999, OAuth token
	 * @author: peterpoliwoda@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"})
	public void Test_getMapMultiStartIndexTooBig(){ // Plan independent
		log.info("Start test method Test_getMapMultiStartIndexTooBig");
		log.info("Getting user.");
		User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
		long mapCount = 0;
		long countMulti = 3;
		long startIndexMultiTooBig = 99999;

		log.info("Retrieving OAuth2Token.");		
		String token = getOAuthToken(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), "collabweb_communitymapping_readwrite");
		
		JSONObject jsonResponse = null;
		String headers[]={"OAuth-Token", token};
		
		// Application: collabweb_communitymapping_readwrite
		log.info("Retrieving multiple maps");

    	ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("count",String.valueOf(countMulti)));
    	params.add(new BasicNameValuePair("startIndex",String.valueOf(startIndexMultiTooBig)));
		String getResponseStringMulti = new HttpUtils().getRequest(getRequestUrl(null, params, clientIDandSecret_ReadWrite),														
														headers, "404");
		try {
			//check for a valid JSON response
			jsonResponse = (JSONObject)new JSONParser().parse(getResponseStringMulti);
			log.info("Valid JSON error returned for Multi: ");
			log.info(getResponseStringMulti);
			
			if (jsonResponse.containsKey("count")) {
				mapCount = (Long) jsonResponse.get("count");
			}
		} catch (ParseException e) {
			e.printStackTrace();
			Assert.assertTrue(false, "Parse exception with post response");
		}
		
		String errorMesg = (String) jsonResponse.get("error_message");
		String errorMesg2 = (String) jsonResponse.get("error");
		
		log.info("Verify no maps returned & got 'error message: No results found'");
		Assert.assertTrue(errorMesg.contains("No results found"), "Did not get expected error message: 'No results found'");
		Assert.assertTrue(errorMesg2.contains("not_found"), "Did not get expected error message: 'not_found'");
		Assert.assertTrue(mapCount==0, "No community maps should be returned when using invalid communityId");
		
		log.info("End test method Test_getMapMultiStartIndexTooBig");
	}
	
	/** 
	 * @name: get_map_multi
	 * @param:	ClientId, ClientPwd, Count=3 and Count=1, startIndex=99999, OAuth token
	 * @author: peterpoliwoda@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"})
	public void Test_getMapMultiStartIndexInvalidIsCCH(){ // Plan independent
		log.info("Start test method Test_getMapMultiStartIndexInvalidIsCCH");
		log.info("Getting user.");
		User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
		long mapCount = 0;
		long countMulti = 3;
		long startIndexMultiTooBig = 1;
		String isCCH = "z";

		log.info("Retrieving OAuth2Token.");		
		String token = getOAuthToken(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), "collabweb_communitymapping_readwrite");
		
		JSONObject jsonResponse = null;
		String headers[]={"OAuth-Token", token};
		
		// Application: collabweb_communitymapping_readwrite
		log.info("Retrieving multiple maps with invalid isCCH");

    	ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("count",String.valueOf(countMulti)));
    	params.add(new BasicNameValuePair("startIndex",String.valueOf(startIndexMultiTooBig)));
    	params.add(new BasicNameValuePair("isCCH",isCCH));
		String getResponseStringMulti = new HttpUtils().getRequest(getRequestUrl(null, params, clientIDandSecret_ReadWrite),														
																	headers, "422");
		try {
			//check for a valid JSON response
			jsonResponse = (JSONObject)new JSONParser().parse(getResponseStringMulti);
			log.info("Valid JSON error returned for Multi: ");
			log.info(getResponseStringMulti);
			if (jsonResponse.containsKey("count")) {
				mapCount = (Long) jsonResponse.get("count");
			}
			
		} catch (ParseException e) {
			e.printStackTrace();
			Assert.assertTrue(false, "Parse exception with post response");
		}
		
		String errorMesg = (String) jsonResponse.get("error_message");
		String error = (String) jsonResponse.get("error");
		
		log.info("Verify no maps returned & got 'error message: No results found'");
		Assert.assertTrue(errorMesg.contains("Bad request: value for parameter isCCH is invalid"), "Did not get expected error message: 'Bad request: value for parameter isCCH is invalid'");
		Assert.assertTrue(error.contains("invalid_parameter"), "Did not get expected error message: 'invalid_parameter'");
		Assert.assertTrue(mapCount==0, "No community maps should be returned when using invalid isCCH=z value");
		
		log.info("End test method Test_getMapMultiStartIndexInvalidIsCCH");
	}
	
	/** 
	 * @name: get_map_multi
	 * @param:	ClientId, ClientPwd, Count=3 and Count=1, startIndex=0 and startIndex=1, IsCCH, OAuth token
	 * Pre-requisite: At least 3 mappings in the database
	 * @author: kvnlau@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"})
	public void Test_getMapMultiStartIndexCountIsCch(){ // Plan independent
		log.info("Start test method Test_getMapMultiStartIndexCountIsCch");
		log.info("Getting user.");
		User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
		long mapCount = 0;
		long countMulti = 3;
		long countSingle = 1;
		long startIndexMulti = 0;
		long startIndexSingle = 1;
		JSONArray ccmsIdsList = null;
		ArrayList<String> ccmsIdArrayList = new ArrayList<String>();
		Boolean isCCH = false; // GUC mapping
		
		String ccmsId = null;

		log.info("Retrieving OAuth2Token.");		
		String token = getOAuthToken(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), "collabweb_communitymapping_readwrite");

		JSONObject jsonResponse = null;
		String headers[]={"OAuth-Token", token};
		
		// Application: collabweb_communitymapping_readwrite
		log.info("Retrieving multiple maps");
		
    	ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("count",String.valueOf(countMulti)));
    	params.add(new BasicNameValuePair("startIndex",String.valueOf(startIndexMulti)));
    	params.add(new BasicNameValuePair("isCCH",String.valueOf(isCCH)));
		String getResponseStringMulti = new HttpUtils().getRequest(getRequestUrl(null, params, clientIDandSecret_ReadWrite),														
																	headers, "200");
		params.clear();
    	params.add(new BasicNameValuePair("count",String.valueOf(countSingle)));
    	params.add(new BasicNameValuePair("startIndex",String.valueOf(startIndexSingle)));
    	params.add(new BasicNameValuePair("isCCH",String.valueOf(isCCH)));
		String getResponseStringSingle = new HttpUtils().getRequest(getRequestUrl(null, params, clientIDandSecret_ReadWrite),														
																	headers, "200");

		try {
			//check for a valid JSON response
			jsonResponse = (JSONObject)new JSONParser().parse(getResponseStringMulti);
			ccmsIdsList = (JSONArray)jsonResponse.get("mappings");
			
			for(Object o : ccmsIdsList){
				 JSONObject jsonLineItem = (JSONObject) o;
				 JSONArray ja = (JSONArray) jsonLineItem.get("ccmsIds");
				 String number = (String) ja.get(0); 
				 log.debug("Number: " + number);
				 ccmsIdArrayList.add(number);	 
			}
			
			if (jsonResponse.containsKey("count")) {
				mapCount = (Long) jsonResponse.get("count");
				startIndexMulti = (Long) jsonResponse.get("startIndex");
			}
			
			log.debug("Map Count for multiple: " + mapCount);
			// check that multiple community maps are returned
			Assert.assertTrue(mapCount <= countMulti, "Returned more than " + countMulti +"community maps");
			
			// check that all community maps returns are GUC only as requested
			Assert.assertTrue(!getResponseStringMulti.contains("\"isCCH\":"+!isCCH), "Asked for GUC but also got CCH community mappings");
			
			jsonResponse = (JSONObject)new JSONParser().parse(getResponseStringSingle);
			JSONArray mappings = (JSONArray) jsonResponse.get("mappings");
			
			JSONObject ccmsIdSingle1 = (JSONObject) mappings.get(0);
			JSONArray ccmsIdsArray = (JSONArray) ccmsIdSingle1.get("ccmsIds");
			ccmsId = (String) ccmsIdsArray.get(0);

			
			log.info("CCMS single: " + ccmsId);
			
			if (jsonResponse.containsKey("count")) {
				mapCount = (Long) jsonResponse.get("count");
				startIndexSingle = (Long) jsonResponse.get("startIndex");	
			}
			log.debug("Map Count for single: " + mapCount);
			// check that a single community maps is returned
			Assert.assertTrue(mapCount <= 1, "Returned more than 1 community maps for single get");
			
		} catch (ParseException e) {
			e.printStackTrace();
			Assert.assertTrue(false, "Parse exception with post response");
		}
		
		log.info("Valid JSON returned for Multi: ");
		System.out.println(getResponseStringMulti);
		log.info("Valid JSON returned for Single: ");
		System.out.println(getResponseStringSingle);
		
		log.info("Checking if single ccmsId is included in the list of multiple mappings. ");
		log.info("ccmsIdsList array dump: " + ccmsIdArrayList);
		Assert.assertTrue(ccmsIdArrayList.contains(ccmsId),"CcmsId not found in the collected list.");
		
		log.info("End test method Test_getMapMultiStartIndexCountIsCch");
	
	}

	/** 
	 * @name: get_map_multi
	 * @param:	ClientId, ClientPwd, startIndex=1, OAuth token
	 * Pre-requisite: At least 3 mappings in the database
	 * Tests: that map at index=1 is included in response to multiple map request where start index=0
	 *        and results contain both CCH & non-CCH mappings 
	 * @author: kvnlau@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"})
	public void Test_getMapMultiStartIndex(){ // Plan independent
		log.info("Start test method Test_getMapMultiStartIndex");
		log.info("Getting user.");
		User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
		long mapCount = 0;
		long countSingle = 1;
		long startIndexMulti = 0;
		long startIndexSingle = 1;
		JSONArray ccmsIdsList = null;
		ArrayList<String> ccmsIdArrayList = new ArrayList<String>();
		String ccmsId = null;

		log.info("Retrieving OAuth2Token.");		
		String token = getOAuthToken(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), "collabweb_communitymapping_readwrite");

		JSONObject jsonResponse = null;
		String headers[]={"OAuth-Token", token};
		
		// Application: collabweb_communitymapping_readwrite
		log.info("Retrieving multiple maps where start index=1 & default count=50");

		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    	params.add(new BasicNameValuePair("startIndex",String.valueOf(startIndexMulti)));
		String getResponseStringMulti = new HttpUtils().getRequest(getRequestUrl(null, params, clientIDandSecret_ReadWrite),														
																	headers, "200");
		params.clear();
		params.add(new BasicNameValuePair("count",String.valueOf(countSingle)));
		params.add(new BasicNameValuePair("startIndex",String.valueOf(startIndexSingle)));
		String getResponseStringSingle = new HttpUtils().getRequest(getRequestUrl(null, params, clientIDandSecret_ReadWrite),														
																	headers, "200");
		
		try {
			//check for a valid JSON response
			jsonResponse = (JSONObject)new JSONParser().parse(getResponseStringMulti);
			ccmsIdsList = (JSONArray)jsonResponse.get("mappings");
			
			for(Object o : ccmsIdsList){
				 JSONObject jsonLineItem = (JSONObject) o;
				 JSONArray ja = (JSONArray) jsonLineItem.get("ccmsIds");
				 String number = (String) ja.get(0); 
				 log.debug("Number: " + number);
				 ccmsIdArrayList.add(number);	 
			}
			
			if (jsonResponse.containsKey("count")) {
				mapCount = (Long) jsonResponse.get("count");
				startIndexMulti = (Long) jsonResponse.get("startIndex");
			}
			
			log.debug("Map Count for multiple: " + mapCount);
			// check that multiple community maps are returned (& within default of 50)
			Assert.assertTrue(mapCount <= 50, "Returned more than 50 community maps");
			
			// NOTE; Disable isCCH check as this sample of 50 mappings may not necessarily contain a CCH community, unless we created some. 
//			log.debug("Check that both CCH & GUC types are present in multiple mappings result");
//			Assert.assertTrue(getResponseStringMulti.contains("\"isCCH\":true"), "Expected community mapping to include CCH types");
//			Assert.assertTrue(getResponseStringMulti.contains("\"isCCH\":false"), "Expected community mapping to include GUC types");
			
			jsonResponse = (JSONObject)new JSONParser().parse(getResponseStringSingle);
			JSONArray mappings = (JSONArray) jsonResponse.get("mappings");
			
			JSONObject ccmsIdSingle1 = (JSONObject) mappings.get(0);
			JSONArray ccmsIdsArray = (JSONArray) ccmsIdSingle1.get("ccmsIds");
			ccmsId = (String) ccmsIdsArray.get(0);

			
			log.info("CCMS single: " + ccmsId);
			
			if (jsonResponse.containsKey("count")) {
				mapCount = (Long) jsonResponse.get("count");
				startIndexSingle = (Long) jsonResponse.get("startIndex");	
			}
			log.debug("Map Count for single: " + mapCount);
			// check that a single community maps is returned
			Assert.assertTrue(mapCount <= 1, "Returned more than 1 community maps for single get");
			
		} catch (ParseException e) {
			e.printStackTrace();
			Assert.assertTrue(false, "Parse exception with post response");
		}
		
		log.info("Valid JSON returned for Multi: ");
		System.out.println(getResponseStringMulti);
		log.info("Valid JSON returned for Single: ");
		System.out.println(getResponseStringSingle);
		
		log.info("Checking if single ccmsId is included in the list of multiple mappings. ");
		log.info("ccmsIdsList array dump: " + ccmsIdArrayList);
		Assert.assertTrue(ccmsIdArrayList.contains(ccmsId),"CcmsId not found in the collected list.");
		
		log.info("End test method Test_getMapMultiStartIndex");
	}
	
	/** 
	 * @name: get_map_multi
	 * @param:	ClientId, ClientPwd, isCCH, Oauth token
	 * Pre-requisite: the over 50 mappings must exist in SalesConnect DB
	 * @author: kvnlau@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"})
	public void Test_getMapMultiCch(){ // Plan independent
		log.info("Start test method Test_getMapMultiCch");
		log.info("Getting user.");
		//User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
		long mapCount = 0;
		boolean isCCH = true; // used to create mapping (where isCCH=true) for test data
		String ccmsId = null;
		String communityId = null;
		ArrayList<String> ccmsIdsList = null;
		String responseString = null;
		String token = null;

		
		try {
			// Populate variable from users.cvs, clients.csv files.
			PoolClient gbClient = commonClientAllocator.getGroupClient(GC.GbClient,this);
			ccmsId = gbClient.getCCMS_ID(); //"GB001DK0"; // existing Client mapped to a community
			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
			
			log.info("Retrieving OAuth2Token.");		
			token = getOAuthToken(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), "collabweb_communitymapping_readwrite");
			
			JSONObject jsonResponse = null;
			String headers[]={"OAuth-Token", token};
			
			
			log.info("Create a mapping where isCCH=true, to ensure such a mapping exists");
			log.info("Get existing communityId if ccmsId is mapped to one, or else create new communityId");
			communityId = createCommunityIfNoneExistsForCcmsId(clientIDandSecret_ReadWrite, 
															headers, cchFnIdUser,
															new ConnectionsCommunityAPI().funcIdEmail, 
															ccmsId, isCCH);


			log.info("form body of (post) create single mapping request");		
			String body = "{\"communityId\":\"" + communityId + 
							"\",\"ccmsIds\":[\"" + ccmsId + 
							"\"],\"isCCH\":\"" + isCCH + "\"}";
			
			log.info("Create a single mapping first with ccmsId where isCCH=true");
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
				Assert.assertTrue(!responseString.contains("\"isCCH\":false"), "Returned community mapping where CCH=false. See defect 68904"); 
				Assert.assertTrue(ccmsIdsList.size()==1, "Community map contains more than 1 expected ccmsId");
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with get response");
			}
		
		
			log.info("/======================= Begin test ============================");
			
			// Application: collabweb_communitymapping_readwrite
			log.info("Retrieving multiple maps that are only CCH");
	
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("isCCH",String.valueOf(isCCH)));
			String getResponseString = new HttpUtils().getRequest(getRequestUrl(null, params, clientIDandSecret_ReadWrite),
																headers, "200");
			try {
				//check for a valid JSON response
				jsonResponse = (JSONObject)new JSONParser().parse(getResponseString);
				
				log.info("Valid JSON returned.");
				System.out.println(getResponseString);	
				
				if (jsonResponse.containsKey("count")) {
					mapCount = (Long) jsonResponse.get("count");
				}
				
				// check that multiple community maps are returned
				Assert.assertTrue(mapCount <= 50, "Returned more than 50 (default) community maps");
				// check that all maps returned are only CCH type mappings not GUC type mappings. See defect 68904 if fails
				Assert.assertFalse(getResponseString.contains("\"isCCH\":false"), "Returned community mapping where CCH=false. See defect 68904"); 
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
		} finally {
			log.info("Clean up: delete community mapping: '" + communityId + "'");
			responseString = new CommunityMappingRestAPI().deleteCommunityMapping(getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
												token, communityId, null, null, "999"); // '999' ignores internal assert in request
			log.info("End test method Test_getMapMultiCch");	
		}
	}

	/** 
	 * @name: get_map_multi
	 * @param:	ClientId, ClientPwd, Count=80, IsCCH, OAuth token
	 * Pre-requisite: At least 80 mappings in the database
	 * Test: will check that only CCH multiple community mappings are returned, up to max of 80
	 * @author: kvnlau@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"})
	public void Test_getMapMultiCountIsCch(){ // Plan independent
		log.info("Start test method Test_getMapMultiCountIsCch");
		log.info("Getting user.");
		

		long countMulti = 80;
		long mapCount = 0;
		boolean isCCH = true; // used to create a mapping (where isCCH=true) for test data
		String ccmsId = null;
		String communityId = null;
		ArrayList<String> ccmsIdsList = null;
		String responseString = null;
		String token = null;
		
		try {
			// Populate variable from users.cvs, clients.csv files.
			PoolClient gbClient = commonClientAllocator.getGroupClient(GC.GbClient,this);
			ccmsId = gbClient.getCCMS_ID(); //"GB001DK0"; // existing Client mapped to a community
			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
		
			log.info("Retrieving OAuth2Token.");		
			token = getOAuthToken(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), "collabweb_communitymapping_readwrite");
			
			JSONObject jsonResponse = null;
			String headers[]={"OAuth-Token", token};
			
			
			log.info("Create a mapping where isCCH=true, to ensure such a mapping exists");
			log.info("Get existing communityId if ccmsId is mapped to one, or else create new communityId");
			communityId = createCommunityIfNoneExistsForCcmsId(clientIDandSecret_ReadWrite, 
															headers, cchFnIdUser,
															new ConnectionsCommunityAPI().funcIdEmail, 
															ccmsId, isCCH);


			log.info("form body of (post) create single mapping request");		
			String body = "{\"communityId\":\"" + communityId + 
							"\",\"ccmsIds\":[\"" + ccmsId + 
							"\"],\"isCCH\":\"" + isCCH + "\"}";
			
			log.info("Create a single mapping first with ccmsId where isCCH=true");
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
				Assert.assertTrue(!responseString.contains("\"isCCH\":false"), "Returned community mapping where CCH=false. See defect 68904"); 
				Assert.assertTrue(ccmsIdsList.size()==1, "Community map contains more than 1 expected ccmsId");
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with get response");
			}
			
			
			log.info("/==================== Begin Test ===============================");
			// Application: collabweb_communitymapping_readwrite
			log.info("Retrieving multiple maps");
	
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("count",String.valueOf(countMulti)));
			params.add(new BasicNameValuePair("isCCH",String.valueOf(isCCH)));
			String getResponseStringMulti = new HttpUtils().getRequest(getRequestUrl(null, params, clientIDandSecret_ReadWrite),														
																		headers, "200");
			try {
				//check for a valid JSON response
				jsonResponse = (JSONObject)new JSONParser().parse(getResponseStringMulti);
				
				if (jsonResponse.containsKey("count")) {
					mapCount = (Long) jsonResponse.get("count");
				}
				
				log.debug("Map Count for multiple: " + mapCount);
				// check that multiple community maps (up to 80) are returned
				Assert.assertTrue(mapCount <= countMulti, "Returned more than " + countMulti + "community maps");
				
				// check that all community maps returns are CCH only as requested. See defect 68904 if fails
				Assert.assertFalse(getResponseStringMulti.contains("\"isCCH\":false"), "Returned community mapping where CCH=false. See defect 68904");
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
		} finally {
			log.info("Clean up: delete community mapping: '" + communityId + "'");
			responseString = new CommunityMappingRestAPI().deleteCommunityMapping(getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
												token, communityId, null, null, "999"); // '999' ignores internal assert in request
			log.info("End test method Test_getMapMultiCountIsCch");
		}

	}
	
	/** 
	 * @name: get_map_single
	 * @param:	ClientId, ClientPwd, CommunityId, Invalid Oauth token
	 * @author: kvnlau@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"})
	public void Test_getSingleMappingCommunityIdInvalidToken(){ // Plan independent
		JSONObject jsonResponse = null;
		HttpUtils restCalls = new HttpUtils();
		String ccmsId = null;
		boolean isCCH = true;
		String responseString = null;
		String communityId = null; //gbClient.getClientName(); //"003b9878-32f4-4aea-bb4e-642ccc215803"; // existing (fake) connections community
		String token = null;
		long mapCount = 0;
		
		log.info("Starting API Test method Test_getSingleMappingCommunityIdInvalidToken");

		try {
			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
			PoolClient gbClient = commonClientAllocator.getGroupClient(GC.GbClient,this);	
			ccmsId = gbClient.getCCMS_ID();

			log.info("Retrieving OAuth2Token.");		
			token = getOAuthToken(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), "collabweb_communitymapping_readwrite");
			
			String headers[]={"OAuth-Token", token};
	
			// check if community mapping exists, if not will throw exception that must be caught
			// so test method will not stop
			// Note: this is necessary due to defect 69028: Not able to over-write an existing connections community mapping
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("ccmsId",ccmsId));
			responseString = restCalls.getRequest(getRequestUrl(null, params, clientIDandSecret_ReadWrite), 
													headers, "999"); // 999 skips internal assert in getRequest(...)
			
			if (responseString.contains("communityId") && responseString.contains("ccmsIds")) {
				try {
					//check for a valid JSON response
					jsonResponse = (JSONObject)new JSONParser().parse(responseString);
					communityId = (String)jsonResponse.get("communityId");
				} catch (ParseException e) {
					e.printStackTrace();
					Assert.assertTrue(false, "Blocked, bad format of existing community found");
				}
			}
			
			// Create 'control' community for test
			if (communityId==null) {
				// create new community
				ConnectionsCommunityAPI connApi= new ConnectionsCommunityAPI();
				communityId = connApi.createConnectionsCommunity(cchFnIdUser.getEmail(),
																cchFnIdUser.getPassword(),getCnxnCommunity());
				log.info("Created communityUuId: " + communityId + " with owner " + cchFnIdUser.getEmail());
				
				log.info("Adding new community members: " + connApi.funcIdEmail + " & " + cchFnIdUser.getEmail());
				responseString = connApi.addUserConnectionsCommunity(cchFnIdUser.getEmail(), 
																	cchFnIdUser.getPassword(), 
																	connApi.funcIdEmail,
																	communityId, getCnxnCommunity());
				
				log.info("SETUP: Create a mapping ");
				log.info("form body of (post) create single mapping request");		
				String body = "{\"communityId\":\"" + communityId + 
								"\",\"ccmsIds\":[\"" + ccmsId + 
								"\"],\"isCCH\":\"" + isCCH + "\"}";
				
				responseString = restCalls.postRequest(getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
														headers, body, contentType);
			}
			
			//-------------------------Begin Test----------------------------------
			
			log.info("Reset token to invalid for negative Get request test");
			jsonResponse = null;
			headers[1] = "FAKE"+token; // create INVALID token
			
			log.info("Retrieving single mapping, but expect 401 response");
			params.clear();
			params.add(new BasicNameValuePair("communityId",communityId));
			responseString = new HttpUtils().getRequest(getRequestUrl(null, params, clientIDandSecret_ReadWrite), 
															headers, "401");
			//check for a valid JSON response
			jsonResponse = (JSONObject)new JSONParser().parse(responseString);
			
			log.info("Valid JSON returned.");
			System.out.println(responseString);	
			
			if (jsonResponse.containsKey("count")) {
				mapCount = (Long) jsonResponse.get("count");
				//System.out.println("It's true");
			}
			
			String errorMesg = (String) jsonResponse.get("error_message");
			
			log.info("Verify no maps returned & got 'invalid_grant' error");
			Assert.assertTrue(errorMesg.contains("invalid_grant"), "Do not get expected error message:'invalid_grant' from invalid token");
			Assert.assertTrue(mapCount==0, "No community maps should be returned when using invalid token");
		} catch (ParseException e) {
			e.printStackTrace();
			Assert.assertTrue(false, "Blocked, unexpected format of response from Get single map");
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false, "Exception occurred");
		} finally {
			log.info("Clean up: delete community mapping: '" + ccmsId +  " | " + communityId + "'");
			responseString = new CommunityMappingRestAPI().deleteCommunityMapping(getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
												token, communityId, null, null, "999"); // '999' ignores internal assert in request
		}
		log.info("End test method Test_getSingleMappingCommunityIdInvalidToken");
	}
	
	/** 
	 * @name: get_map_multi
	 * @param:	ClientId, ClientPwd, CommunityId, No Oauth token
	 * @author: kvnlau@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"})
	public void Test_getSingleMappingCommunityIdNoToken(){ // Plan independent
		String ccmsId = null;
		boolean isCCH = true;
		String responseString = null;
		String communityId = null;
		String token = null;
		long mapCount = 0;
		
		JSONObject jsonResponse = null;
		HttpUtils restCalls = new HttpUtils();
		
		log.info("Starting API Test method Test_getSingleMappingCommunityIdNoToken");

		try {
			// get test data from users.csv & clients.csv file(s)
			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
			PoolClient gbClient = commonClientAllocator.getGroupClient(GC.GbClient,this);	
			ccmsId = gbClient.getCCMS_ID();

			log.info("Retrieving OAuth2Token.");		
			token = getOAuthToken(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), "collabweb_communitymapping_readwrite");
	
			String headers[]={"OAuth-Token", token};
	
			// check if community mapping exists, if not will throw exception that must be caught
			// so test method will not stop
			// Note: this is necessary due to defect 69028: Not able to over-write an existing connections community mapping
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("ccmsId",ccmsId));
			responseString = restCalls.getRequest(getRequestUrl(null, params, clientIDandSecret_ReadWrite), 
													headers, "999"); // 999 skips internal assert in getRequest(...)
		
			if (responseString.contains("communityId") && responseString.contains("ccmsIds")) {
				try {
					//check for a valid JSON response
					jsonResponse = (JSONObject)new JSONParser().parse(responseString);
					communityId = (String)jsonResponse.get("communityId");
				} catch (ParseException e) {
					e.printStackTrace();
					Assert.assertTrue(false, "Blocked, bad format of existing community found");
				}
			}
			
			// Create 'control' community for test
			if (communityId==null) {
				// create new community
				ConnectionsCommunityAPI connApi= new ConnectionsCommunityAPI();
				communityId = connApi.createConnectionsCommunity(cchFnIdUser.getEmail(),
																cchFnIdUser.getPassword(),getCnxnCommunity());
				log.info("Created communityUuId: " + communityId + " with owner " + cchFnIdUser.getEmail());
				
				log.info("Adding new community members: " + connApi.funcIdEmail + " & " + cchFnIdUser.getEmail());
				responseString = connApi.addUserConnectionsCommunity(cchFnIdUser.getEmail(), 
																	cchFnIdUser.getPassword(), 
																	connApi.funcIdEmail,
																	communityId, getCnxnCommunity());
				
				log.info("SETUP: Create a mapping ");
				log.info("form body of (post) create single mapping request");		
				String body = "{\"communityId\":\"" + communityId + 
								"\",\"ccmsIds\":[\"" + ccmsId + 
								"\"],\"isCCH\":\"" + isCCH + "\"}";
				responseString = restCalls.postRequest(getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
																headers, body, contentType);
			}
			
			log.info("Verify single mapping exists, before testing with no token below");	
			params.clear();
			params.add(new BasicNameValuePair("ccmsId",ccmsId));
			responseString = restCalls.getRequest(getRequestUrl(null, params, clientIDandSecret_ReadWrite), 
															headers, "200");
			try {
				//check for a valid JSON response
				jsonResponse = (JSONObject)new JSONParser().parse(responseString);
				log.info("Valid JSON returned.");
				System.out.println(responseString);	
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with get response");
			} 
			
			ArrayList<String> ccmsIdsList = (ArrayList)jsonResponse.get("ccmsIds");
			
			// Check that now community exists
			Assert.assertEquals((String) jsonResponse.get("communityId"), communityId, "Single community map does not contain expected communityId");
			Assert.assertTrue(ccmsIdsList.contains(ccmsId), "Single community map does not contain expected sole ccmsId="+ccmsId);			
			
			
			// -------------- begin test ------------------------------------
			// reset variables
			jsonResponse = null;
			headers = null; //{"OAuth-Token", "FAKE"+token}; // create empty header withou token 
			
			log.info("Retrieving single mapping, but expect 401 response");
			params.clear();
			params.add(new BasicNameValuePair("communityId",communityId));
			responseString = restCalls.getRequest(getRequestUrl(null, params, clientIDandSecret_ReadWrite),
															headers, "401"); //no token supplied

			//check for a valid JSON response
			jsonResponse = (JSONObject)new JSONParser().parse(responseString);
			
			log.info("Valid JSON returned.");
			System.out.println(responseString);	
			
			if (jsonResponse.containsKey("count")) {
				mapCount = (Long) jsonResponse.get("count");
			}
			
			String errorMesg = (String) jsonResponse.get("error_message");
			
			log.info("Verify no maps returned & got 'invalid_grant' error");
			Assert.assertTrue(errorMesg.contains("No valid authentication for user."), "Do not get expected error message:'No valid authentication for user.' when no token supplied");
			Assert.assertTrue(mapCount==0, "No community maps should be returned when no token is supplied");
		} catch (ParseException e) {
			e.printStackTrace();
			Assert.assertTrue(false, "Parse exception with post response");
		} catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false, "Exception occurred");
		} finally {
			log.info("Clean up: delete community mapping: '" + " | " + communityId + "'");
			responseString = new CommunityMappingRestAPI().deleteCommunityMapping(getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
												token, communityId, null, null, "999"); // '999' ignores internal assert in request
			log.info("End test method Test_getSingleMappingCommunityIdNoToken");
		}
	}
	
	/** 
	 * @name: get_map_multi
	 * @param:	ClientId, ClientPwd, Count, Start index, No Oauth token
	 * @author: kvnlau@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"})
	public void Test_getMultiMappingCountStartNoToken(){ // Plan independent
		String ccmsId = null;
		boolean isCCH = true;
		String responseString = null;
		String communityId = null; //gbClient.getClientName(); //"003b9878-32f4-4aea-bb4e-642ccc215803"; // existing (fake) connections community
		String token = null;
		long mapCount = 0;
		
		JSONObject jsonResponse = null;
		HttpUtils restCalls = new HttpUtils();
		
		log.info("Starting API Test method Test_getMultiMappingCountStartNoToken");

		try {		
			// Get test data from users.csv & clients.csv file(s)
			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
			PoolClient gbClient = commonClientAllocator.getGroupClient(GC.GbClient,this);
			ccmsId = gbClient.getCCMS_ID();
			
			log.info("Retrieving OAuth2Token.");		
			token = getOAuthToken(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), "collabweb_communitymapping_readwrite");
			
			String headers[]={"OAuth-Token", token};
			
			// check if community mapping exists, if not will throw exception that must be caught
			// so test method will not stop
			// Note: this is necessary due to defect 69028: Not able to over-write an existing connections community mapping
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("ccmsId",ccmsId));
			responseString = restCalls.getRequest(getRequestUrl(null, params, clientIDandSecret_ReadWrite), 
													headers, "999"); // 999 skips internal assert in getRequest(...)
			
			// Extract communityId if found in response
			if (responseString.contains("communityId") && responseString.contains("ccmsIds")) {
				try {
					//check for a valid JSON response
					jsonResponse = (JSONObject)new JSONParser().parse(responseString);
					communityId = (String)jsonResponse.get("communityId");
				} catch (ParseException e) {
					e.printStackTrace();
					Assert.assertTrue(false, "Blocked, bad format of existing community found");
				}
			}
			 
			// Create 'control' community for test
			if (communityId==null) {
				// create new community (if no community exists)
				ConnectionsCommunityAPI connApi= new ConnectionsCommunityAPI();
				communityId = connApi.createConnectionsCommunity(cchFnIdUser.getEmail(),
																cchFnIdUser.getPassword(),getCnxnCommunity());
				log.info("Created communityUuId: " + communityId + " with owner " + cchFnIdUser.getEmail());
				
				log.info("Adding new community members: " + connApi.funcIdEmail + " & " + cchFnIdUser.getEmail());
				responseString = connApi.addUserConnectionsCommunity(cchFnIdUser.getEmail(), 
																	cchFnIdUser.getPassword(), 
																	connApi.funcIdEmail,
																	communityId, getCnxnCommunity());
				
				log.info("SETUP: Create a mapping ");
				log.info("form body of (post) create single mapping request");		
				String body = "{\"communityId\":\"" + communityId + 
								"\",\"ccmsIds\":[\"" + ccmsId + 
								"\"],\"isCCH\":\"" + isCCH + "\"}";				
				responseString = restCalls.postRequest(getRequestUrl(null, null) + "&" + 
																clientIDandSecret_ReadWrite, 
																headers, body, contentType);
			}
			
			log.info("Verify single mapping exists, before testing with no token below");
			params.clear();
			params.add(new BasicNameValuePair("ccmsId",ccmsId));
			responseString = restCalls.getRequest(getRequestUrl(null, params, clientIDandSecret_ReadWrite), 
															headers, "200");
			try {
				//check for a valid JSON response
				jsonResponse = (JSONObject)new JSONParser().parse(responseString);
				log.info("Valid JSON returned.");
				System.out.println(responseString);	
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with get response");
			} 
			
			ArrayList<String> ccmsIdsList = (ArrayList)jsonResponse.get("ccmsIds");
			
			// Check that now community exists
			Assert.assertEquals((String) jsonResponse.get("communityId"), communityId, "Single community map does not contain expected communityId");
			Assert.assertTrue(ccmsIdsList.contains(ccmsId), "Single community map does not contain expected sole ccmsId="+ccmsId);

		
			// ------------- Begin test ---------------------------------------
			log.info("Remove Token from Header to setup test");
			jsonResponse = null;
			headers=null; // create empty header without token 

			
			log.info("Retrieving multi-mappings (without token), should expect 401 response");
			params.clear();
			params.add(new BasicNameValuePair("communityId",communityId));
			params.add(new BasicNameValuePair("count",String.valueOf(0)));
			String getResponseString = restCalls.getRequest(getRequestUrl(null, params, clientIDandSecret_ReadWrite),
														headers, "401"); // no token supplied

			//check for a valid JSON response
			jsonResponse = (JSONObject)new JSONParser().parse(getResponseString);
			
			log.info("Valid JSON returned.");
			System.out.println(getResponseString);	
			
			if (jsonResponse.containsKey("count")) {
				mapCount = (Long) jsonResponse.get("count");
				//System.out.println("It's true");
			}
			
			String errorMesg = (String) jsonResponse.get("error_message");
			
			log.info("Verify no maps returned & got 'invalid_grant' error");
			Assert.assertTrue(errorMesg.contains("No valid authentication for user."), "Do not get expected error message:'No valid authentication for user.' when no token supplied");
			Assert.assertTrue(mapCount==0, "No community maps should be returned when no token is supplied");
		} catch (ParseException e) {
			e.printStackTrace();
			Assert.assertTrue(false, "Parse exception with post response");
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			log.info("End test method Test_getMultiMappingCountStartNoToken");
			log.info("Clean up: delete community mapping: '" + ccmsId +  " | " + communityId + "'");
			responseString = new CommunityMappingRestAPI().deleteCommunityMapping(getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
												token, communityId, null, null, "999");	// '999' ignores internal assert in request		
		}
	}
	
	/** 
	 * @name: get_map_multi
	 * @param:	ClientId, ClientPwd, Count, Start index, Is CCH, No Oauth token
	 * @author: kvnlau@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"})
	public void Test_getMultiMappingCountStartIsCchNoToken(){ // Plan independent
		long mapCount = 0;
		String ccmsId = null;
		String communityId = null;
		boolean isCCH = true;
		String token = null;
		String responseString = null;
		
		JSONObject jsonResponse = null;
		HttpUtils restCalls = new HttpUtils();
		
		log.info("Starting API Test method Test_getMultiMappingCountStartIsCchNoToken");
		
		try {		
			// Get test data from users.csv & clients.csv file(s)
			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
			PoolClient gbClient = commonClientAllocator.getGroupClient(GC.GbClient,this);
			ccmsId = gbClient.getCCMS_ID();
			
			log.info("Retrieving OAuth2Token.");		
			token = getOAuthToken(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), "collabweb_communitymapping_readwrite");
			
			String headers[]={"OAuth-Token", token};
			

			// check if community mapping exists, if not will throw exception that must be caught
			// so test method will not stop
			// Note: this is necessary due to defect 69028: Not able to over-write an existing connections community mapping
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("ccmsId",ccmsId));
			responseString = restCalls.getRequest(getRequestUrl(null, params, clientIDandSecret_ReadWrite), 
													headers, "999"); // 999 skips internal assert in getRequest(...)
			
			if (responseString.contains("communityId") && responseString.contains("ccmsIds")) {
				try {
					//check for a valid JSON response
					jsonResponse = (JSONObject)new JSONParser().parse(responseString);
					communityId = (String)jsonResponse.get("communityId");
				} catch (ParseException e) {
					e.printStackTrace();
					Assert.assertTrue(false, "Blocked, bad format of existing community found");
				}
			}

			// create new 'control' CCH community for test (so we know CCH community exists)
			if (communityId==null) {
				ConnectionsCommunityAPI connApi= new ConnectionsCommunityAPI();
				communityId = connApi.createConnectionsCommunity(cchFnIdUser.getEmail(),
																cchFnIdUser.getPassword(),getCnxnCommunity());
				log.info("Created communityUuId: " + communityId + " with owner " + cchFnIdUser.getEmail());
				
				log.info("Adding new community members: " + connApi.funcIdEmail + " & " + cchFnIdUser.getEmail());
				responseString = connApi.addUserConnectionsCommunity(cchFnIdUser.getEmail(), 
																	cchFnIdUser.getPassword(), 
																	connApi.funcIdEmail,
																	communityId, getCnxnCommunity());
				
				log.info("SETUP: Create a mapping ");
				log.info("form body of (post) create single mapping request");		
				String body = "{\"communityId\":\"" + communityId + 
								"\",\"ccmsIds\":[\"" + ccmsId + 
								"\"],\"isCCH\":\"" + isCCH + "\"}";				
				responseString = restCalls.postRequest(getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
																headers, body, contentType);
			}
			
			log.info("Verify single CCH mapping exists, before testing with no token & isCCH below");
			params.clear();
			params.add(new BasicNameValuePair("ccmsId",ccmsId));
			params.add(new BasicNameValuePair("isCCH",String.valueOf(isCCH)));
			responseString = restCalls.getRequest(getRequestUrl(null, params, clientIDandSecret_ReadWrite), 
															headers, "200");
			try {
				//check for a valid JSON response
				jsonResponse = (JSONObject)new JSONParser().parse(responseString);
				log.info("Valid JSON returned.");
				System.out.println(responseString);	
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			} 
			
			ArrayList<String> ccmsIdsList = (ArrayList)jsonResponse.get("ccmsIds");
			
			// Check that now community exists
			Assert.assertEquals((String) jsonResponse.get("communityId"), communityId, "Single community map does not contain expected communityId");
			Assert.assertTrue(ccmsIdsList.contains(ccmsId), "Single community map does not contain expected sole ccmsId="+ccmsId);

		
			// -------------- begin test ------------------------------------
			log.info("Remove Token from Header to setup test");
			jsonResponse = null;
			headers=null; // create empty header without token 

		
			log.info("Retrieving multi CCH(only) mapping, but expect 401 response");
			params.clear();
			params.add(new BasicNameValuePair("communityId",communityId));
			params.add(new BasicNameValuePair("count",String.valueOf(0)));
			params.add(new BasicNameValuePair("isCCH",String.valueOf(isCCH)));
			String getResponseString = new HttpUtils().getRequest(getRequestUrl(null, params, clientIDandSecret_ReadWrite),
														headers, "401"); // no token supplied in header

			//check for a valid JSON response
			jsonResponse = (JSONObject)new JSONParser().parse(getResponseString);
			
			log.info("Valid JSON returned.");
			System.out.println(getResponseString);	
			
			if (jsonResponse.containsKey("count")) {
				mapCount = (Long) jsonResponse.get("count");
				//System.out.println("It's true");
			}
			
			String errorMesg = (String) jsonResponse.get("error_message");
			
			log.info("Verify no maps returned & got 'invalid_grant' error");
			Assert.assertTrue(errorMesg.contains("No valid authentication for user."), "Do not get expected error message:'No valid authentication for user.' when no token supplied");
			Assert.assertTrue(mapCount==0, "No CCH community maps should be returned when no token is supplied");
		} catch (ParseException e) {
			e.printStackTrace();
			Assert.assertTrue(false, "Parse exception with post response");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			log.info("Clean up: delete community mapping: '" + ccmsId +  " | " + communityId + "'");
			responseString = new CommunityMappingRestAPI().deleteCommunityMapping(getRequestUrl(null, null, clientIDandSecret_ReadWrite), 
												token, communityId, null, null, "999"); // '999' ignores internal assert in request
			log.info("End test method Test_getMultiMappingCountStartIsCchNoToken");
		}
	}
	
	/** 
	 * @name: get_map_single
	 * @param:	ClientId, ClientPwd, CommunityId=[non existant ID], Oauth token
	 * @author: kvnlau@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"})
	public void Test_getSingleMappingInvalidCommunityId(){ // Plan independent
		try {
			// get test date from users.csv file
			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
			String communityId = "FAKE440c8-5a08-4064-8a32-ed3e2b556bd2"; // fake non-existent connections community
			long mapCount = 0;
			
			log.info("Retrieving OAuth2Token.");		
			String token = getOAuthToken(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), "collabweb_communitymapping_readwrite");
			
			JSONObject jsonResponse = null;
			String headers[]={"OAuth-Token", token};
			
			log.info("Retrieving single mapping, but expect 404 response, due to invalid communityId");
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("communityId",communityId));
			String getResponseString = new HttpUtils().getRequest(getRequestUrl(null, params, clientIDandSecret_ReadWrite), 
															headers, "404");
			try {
				//check for a valid JSON response
				jsonResponse = (JSONObject)new JSONParser().parse(getResponseString);
				
				log.info("Valid JSON returned.");
				System.out.println(getResponseString);	
				
				if (jsonResponse.containsKey("count")) {
					mapCount = (Long) jsonResponse.get("count");
					//System.out.println("It's true");
				}
				
				String errorMesg = (String) jsonResponse.get("error_message");
				String errorMesg2 = (String) jsonResponse.get("error");
				
				log.info("Verify no maps returned & got 'error message: No results found'");
				Assert.assertTrue(errorMesg.contains("No results found"), "Did not get expected error message: 'No results found'");
				Assert.assertTrue(errorMesg2.contains("not_found"), "Did not get expected error message: 'not_found'");
				Assert.assertTrue(mapCount==0, "No community maps should be returned when using invalid communityId");
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			log.info("End test method Test_getSingleMappingInvalidCommunityId");
		}
	}
	
	/** 
	 * @name: get_map_multi
	 * @param:	ClientId, ClientPwd, count=z, Oauth token
	 * @author: kvnlau@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"})
	public void Test_getMultiMappingInvalidCount(){ // Plan independent
		log.info("Start test method: Test_getMultiMappingInvalidCount()");
		
		try {
			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
			long mapCount = 0;
			
			log.info("Retrieving OAuth2Token.");		
			String token = getOAuthToken(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), "collabweb_communitymapping_readwrite");
			
			JSONObject jsonResponse = null;
			String headers[]={"OAuth-Token", token};
			
			log.info("Retrieving multi mappings, but expect 422 response, due to invalid count=z");
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("count","z"));
			String getResponseString = new HttpUtils().getRequest(getRequestUrl(null, params, clientIDandSecret_ReadWrite), 
																	headers, "422");
			try {
				//check for a valid JSON response
				jsonResponse = (JSONObject)new JSONParser().parse(getResponseString);
				
				log.info("Valid JSON returned.");
				System.out.println(getResponseString);	
				
				if (jsonResponse.containsKey("count")) {
					mapCount = (Long) jsonResponse.get("count");
					//System.out.println("It's true");
				}
				
				String errorMesg = (String) jsonResponse.get("error_message");
				
				log.info("Verify no maps returned & got 'Bad request: value for parameter count is invalid' error mesg");
				Assert.assertTrue(errorMesg.contains("Bad request: value for parameter count is invalid"), "Do not get expected error message:'Bad request: value for parameter count is invalid' from invalid param count=z");
				Assert.assertTrue(mapCount==0, "No community maps should be returned when using invalid token");
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			log.info("End test method Test_getMultiMappingInvalidCount");
		}
	}
	
	/**
	 * @name: get_map_single
	 * @param:	ClientId, ClientPwd, ccmsId, start index, count=3, invalid IsCCH,  Oauth token
	 * @author: kvnlau@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"})
	public void Test_getMultiMappingCountStartInvalidIsCCH(){ // Plan independent
		
		log.info("Starting API Test method Test_getMultiMappingCountStartInvalidIsCCH");
		log.info("Getting user.");
		User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
		long mapCount = 0;
		long countMulti = 3;
		long startIndexMulti = 0;

		log.info("Retrieving OAuth2Token.");		
		String token = getOAuthToken(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), "collabweb_communitymapping_readwrite");
		
		JSONObject jsonResponse = null;
		String headers[]={"OAuth-Token", token};
		
		// Application: collabweb_communitymapping_readwrite
		log.info("Try retrieving multiple maps, but should fail due to invalid isCCH parameter");
		
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("count",String.valueOf(countMulti)));
		params.add(new BasicNameValuePair("startIndex",String.valueOf(startIndexMulti)));
		params.add(new BasicNameValuePair("isCCH","z"));// invalid boolean value	
		String getResponseString = new HttpUtils().getRequest(getRequestUrl(null, params, clientIDandSecret_ReadWrite),		 					
															headers, "422"); // invalid parameter value
		try {
			//check for a valid JSON response
			jsonResponse = (JSONObject)new JSONParser().parse(getResponseString);
			
			log.info("Valid JSON returned.");
			System.out.println(getResponseString);	
			
			if (jsonResponse.containsKey("count")) {
				mapCount = (Long) jsonResponse.get("count");
			}
			
			String errorMesg = (String) jsonResponse.get("error_message");
			
			log.info("Verify no maps returned & got 'Bad request: value for parameter count is invalid' error mesg");
			Assert.assertTrue(errorMesg.contains("Bad request: value for parameter isCCH is invalid"), "Do not get expected error message:'Bad request: value for parameter count is invalid' from invalid param count=z");
			Assert.assertTrue(mapCount==0, "No community maps should be returned when using invalid token");
		} catch (ParseException e) {
			e.printStackTrace();
			Assert.assertTrue(false, "Parse exception with post response");
		}
		
		log.info("Valid JSON returned for Multi: ");
		
		log.info("End test method Test_getMultiMappingCountStartInvalidIsCCH");
	}
	
	/** 
	 * @name: get_map_multi
	 * @param:	ClientId, ClientPwd, Count=3, startIndex=900000, OAuth token
	 * Pre-requisite: At least 3 mappings in the database
	 * Test: try to retrieve multiple mappings where start index which does not exist
	 * @author: kvnlau@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"})
	public void Test_getMapMultiCountStartIndexExceeded(){ // Plan independent
		long mapCount = 0;
		long countMulti = 3;
		long startIndexMulti = 900000;
		
		try {
			log.info("Start test method Test_getMapMultiCountStartIndexExceeded");
			log.info("Getting user.");
			User user = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
			
			log.info("Retrieving OAuth2Token.");
			String token = getOAuthToken(user.getEmail(), user.getPassword(), "collabweb_communitymapping_readwrite");
			
			JSONObject jsonResponse = null;
			String headers[]={"OAuth-Token", token};
			
			// Application: collabweb_communitymapping_readwrite
			log.info("Try retrieving multiple maps, where count=3, but start index (e.g. 900000) exceeds what is in database");
	
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		    params.add(new BasicNameValuePair("count",String.valueOf(countMulti)));
		    params.add(new BasicNameValuePair("startIndex",String.valueOf(startIndexMulti)));
			String getResponseString = new HttpUtils().getRequest(getRequestUrl(null, params, clientIDandSecret_ReadWrite),														
															headers, "404"); // start index exceeded
			try {
				//check for a valid JSON response
				jsonResponse = (JSONObject)new JSONParser().parse(getResponseString);
				
				log.info("Valid JSON returned.");
				System.out.println(getResponseString);	
				
				if (jsonResponse.containsKey("count")) {
					mapCount = (Long) jsonResponse.get("count");
					//System.out.println("It's true");
				}
				String error = (String) jsonResponse.get("error");
				String errorMesg = (String) jsonResponse.get("error_message");
				
				log.info("Verify no maps returned & got 'No results found' error mesg");
				Assert.assertTrue(error.contains("not_found"), "Do not get expected error:'Not_found");
				Assert.assertTrue(errorMesg.contains("No results found"), "Do not get expected error message:'No results found");
				Assert.assertTrue(mapCount==0, "No community maps should be returned when using invalid token");
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
			
			log.info("Valid JSON returned for Multi: ");
			System.out.println(getResponseString);
		} finally {
			log.info("End test method Test_getMapMultiCountStartIndexExceeded");
		}
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
//    	this.addDataFile("test_config/extensions/api/cchMapping.csv");
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
