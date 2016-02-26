package com.ibm.salesconnect.API;

//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;
//import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
//import org.testng.Assert;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.common.HttpUtils;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
//import com.ibm.salesconnect.test.CommunityMapping.CommunityMappingAPITests;

public class CommunityMappingRestAPI {
	
	public static final String mappingURLExtension = "salesconnect_v210/collab/communityMappings";
	public static final String noPortalPath = "rest/v10/";
	public static final String contentType = "application/json";
	Logger log = LoggerFactory.getLogger(CommunityMappingRestAPI.class);
	

	
	/**
	 * @Description: Get community mappings via the SalesConnect REST APIs (bypassing API portal)
	 * @param: url of SalesConnect, i.e. "https://<SalesConnect host>/sales/salesconnect"
	 * @param: oauthToken containing access privilege
	 * @param: communityId of community mapping to be retrieved	- optional
	 * @param: ccmsId in community mapping to be retrieved		- optional
	 * @param: responseCode that is expected. Note: code 999 means we don't care about the response
	 */
	public String getCommunityMapping(String url, String oauthToken, String communityId, String ccmsId, String responseCode) {
		//String contentType = "application/json";
		String responseString = null;
		String headers[]={"OAuth-Token", oauthToken};
		
		//log.info("Request String" + json.toString());
		HttpUtils restCalls = new HttpUtils();
		 //String postResponseString = restCalls.postRequest(url, headers, json, contentType, expectedResponse);
		
		if (communityId==null && ccmsId==null) {
			log.info("Attempt retrieval of unspecified community mapping == get multi-mappings");
			responseString = restCalls.getRequest(url + noPortalPath + 
												getMappingExtension(), 
												headers, "999"); // 999 aka don't care if pass or fail
		} else if (communityId!=null && ccmsId==null){
			log.info("Retrieve community mapping where communityId='" + communityId + "'");
			responseString = restCalls.getRequest(url + noPortalPath + 
												getMappingExtension() + "?" + 
												"communityId=" + communityId, 
												headers, "999"); // 999 aka don't care if pass or fail
		} else if (communityId==null && ccmsId!=null) {
			log.info("Retrieve community mapping where ccmsId='" + ccmsId + "'");
			responseString = restCalls.getRequest(url + noPortalPath + 
												getMappingExtension() + "?" + 
												"ccmsId=" + ccmsId,
												headers, "999"); // 999 aka don't care if pass or fail			
		} else if (communityId!=null && ccmsId!=null) {
			log.info("Retrieve community mapping where communityId='" + communityId + "& ccmsId='" + ccmsId + "'");
			responseString = restCalls.getRequest(url + noPortalPath + 
												getMappingExtension() + "?" + 
												"ccmsId=" + ccmsId +
												"communityId=" + communityId, 
												headers, "999"); // 999 aka don't care if pass or fail			
		}
		
		if (responseString!=null) {
			System.out.println(responseString);	
		}
		return responseString;
	}
	
	/**
	 * @name: getCommunityIdIfMappingExistsForCcmsId()
	 * @description: gets communityId if community mapping exists for ccmsId 
	 * @param:	url, header (with OAuth token, cchFnIduser, funcIdEmail, ccmsId)
	 * @param: url of SalesConnect, i.e. "https://<SalesConnect host>/sales/salesconnect"
	 * @param: oauthToken containing access privilege
	 * @param: cchFnIdUser is who is already in SalesConnect config (for accessing CCH APIs)
	 * @param: ccmsId 
	 * @return: String with existing communityId or null (if no community mapping exists)
	 * @author: kvnlau@ie.ibm.com
	 */	
	public String getCommunityIdIfMappingExistsForCcmsId(String url, 
												String oauthToken, User cchFnIdUser,
												//String funcIdEmail, 
												String ccmsId){
		String communityId = null;
		String responseString = null;
		String headers[] = {"OAuth-Token", oauthToken};
		JSONObject jsonResponse = null;
		HttpUtils restCalls = new HttpUtils();
		CommunityMappingRestAPI communityApi = new CommunityMappingRestAPI(); // for path extension methods
		
		try {
			// check if community mapping exists
			// Note: this is necessary due to defect 69028: Not able to over-write an existing connections community mapping
//			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
//			params.add(new BasicNameValuePair("ccmsId",ccmsId));
//			responseString = restCalls.getRequest(getRequestUrl(null, params, clientIDandSecret_ReadWrite), 
//													headers, "999"); // 999 aka don't care if pass or fail
			// check if community mapping exists, if not will throw exception that must be caught
			// so test method will not stop
			// Note: this is necessary due to defect 69028: Not able to over-write an existing connections community mapping
			
//			responseString = restCalls.getRequest(getApiManagement() + getMappingExtension() + "?" + 
//													clientIDandSecret_ReadWrite + "&" + "ccmsId=" + ccmsId,
//													"ccmsId=" + ccmsId, 
//													headers, "999"); // 999 aka don't care if pass or fail
			
//			responseString = restCalls.getRequest(this.getBrowserUrl() +  
//													communityApi.getMappingExtension() + "?" + 
//													"ccmsId=" + ccmsId, 
//													headers, "999"); // 999 aka don't care if pass or fail
			
			responseString = communityApi.getCommunityMapping(url, oauthToken, //headers[1], 
												null, ccmsId, "999");// 999 aka don't care if pass or fail
			
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
	 * @name: createCommunityMappingForCcmsId()
	 * @description: creates community mapping for ccmsId
	 * @param: url of SalesConnect, i.e. "https://<SalesConnect host>/sales/salesconnect"
	 * @param: token containing access privilege
	 * @param: cchFnIdUser is who is already in SalesConnect config (for accessing CCH APIs)
	 * @param: commmunityId of existing community in Connections
	 * @param: ccmsId that we want to map to the existing community 
	 * @param: isCCH indicating if we want to create a GUC or CCH community mapping
	 * @return: responseString containing response from creation of community mapping
	 * @author: kvnlau@ie.ibm.com
	 */
	public String createCommunityMappingForCcmsId(String url, 
													String token, User cchFnIdUser,
													String funcIdEmail, 
													String communityId, String ccmsId, 
													boolean isCCH){

		String headers[] = {"OAuth-Token", token};
		String responseString = null;
		//JSONObject jsonResponse = null;
		//HttpUtils restCalls = new HttpUtils();
		//CommunityMappingRestAPI communityApi = new CommunityMappingRestAPI(); // for path extension methods
		
		log.info("form body of (post) create single mapping request");		
		String body = "{\"communityId\":\"" + communityId + 
						"\",\"ccmsIds\":[\"" + ccmsId + 
						"\"],\"isCCH\":\"" + isCCH + "\"}";
		
		log.info("Create a single mapping first with ccmsId=" + ccmsId);
		responseString = new HttpUtils().postRequest(url + noPortalPath + getMappingExtension(), 
													headers, body, contentType, 
													"999"); // 999 aka don't care if pass or fail		
		return responseString;
	}	
		
	/**
	 * @name: createCommunityIfNoneExistsForCcmsId()
	 * @description: creates community if mapping doesn't exist for ccmsId
	 * @param:	ClientId+ClientSecret, header (with OAuth token), cchFnIduser, funcIdEmail, ccmsId, isCCH)
	 * @return: String with new or existing communityId
	 * @author: kvnlau@ie.ibm.com
	 */
	public String createCommunityIfNoneExistsForCcmsId(String url, 
													String token, User cchFnIdUser,
													String funcIdEmail, String ccmsId, 
													boolean isCCH){
		String communityId = null;
		String responseString = null;
		JSONObject jsonResponse = null;
		HttpUtils restCalls = new HttpUtils();
		CommunityMappingRestAPI communityApi = new CommunityMappingRestAPI(); // for path extension methods
		
		try {
			responseString = communityApi.getCommunityMapping(url, token, 
												null, ccmsId, "999"); // 999 aka don't care if pass or fail
			
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
																cchFnIdUser.getPassword(),
																new ProductBaseTest().getCnxnCommunity());//getCnxnCommunity()
																
				log.info("Created communityUuId: " + communityId + " with owner " + cchFnIdUser.getEmail());

				log.info("Adding new community members: " + connApi.funcIdEmail);
				responseString = connApi.addUserConnectionsCommunity(cchFnIdUser.getEmail(), 
																	cchFnIdUser.getPassword(), 
																	connApi.funcIdEmail,
																	communityId, 
																	new ProductBaseTest().getCnxnCommunity());//getCnxnCommunity()
																	
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Caught exception");
		}
		return communityId;
	}
	
	/**
	 * @Description: Delete community mappings via the SalesConnect REST APIs (bypassing API portal)
	 * @param: url of SalesConnect, i.e. "https://<SalesConnect host>/sales/salesconnect"
	 * @param: oauthToken
	 * @param: communityId of community mapping to be deleted
	 * @param: ccmsId in community mapping to be deleted
	 * @param: responseCode that is expected. Note: code 999 means we don't care about the response
	 */
	public String deleteCommunityMapping(String url, String oauthToken, String communityId, String ccmsId, String responseCode) {
		//String contentType = "application/json";
		String responseString = null;
		String headers[]={"OAuth-Token", oauthToken};
		
		//log.info("Request String" + json.toString());
		HttpUtils restCalls = new HttpUtils();
		 //String postResponseString = restCalls.postRequest(url, headers, json, contentType, expectedResponse);
		
		if (communityId==null && ccmsId==null) {
			log.info("Attempt delete of unspecified community mapping");
			responseString = restCalls.deleteRequest(url + noPortalPath + getMappingExtension(), 
															headers, responseCode);				

		} else if (communityId!=null && ccmsId==null){
			log.info("Delete community mapping='" + communityId + "'");
			responseString = restCalls.deleteRequest(url + noPortalPath + getMappingExtension() +   
															"&communityId=" + communityId, 
															headers, responseCode);				
		} else if (communityId==null && ccmsId!=null) {
			log.info("Attempt delete of ccmsId='" + ccmsId + "' from unspecified community mapping");
			responseString = restCalls.deleteRequest(url + noPortalPath + getMappingExtension() + 
															"&ccmsId=" + ccmsId, 
															headers, responseCode);				
		} else if (communityId!=null && ccmsId!=null) {
			log.info("Delete community mapping='" + communityId + "'");
			responseString = restCalls.deleteRequest(url + noPortalPath + getMappingExtension() + 
															"&communityId=" + communityId +
															"&ccmsId=" + ccmsId, 
															headers, responseCode);				
		}
		
		if (responseString!=null) {
			System.out.println(responseString);	
		}
		return responseString;
	}
	
	/**
	 * @Description: Delete community mappings via the SalesConnect REST APIs (via API portal)
	 * @param: url of SalesConnect, i.e. "https://<SalesConnect host>/sales/salesconnect"
	 * @param: oauthToken
	 * @param: communityId of community mapping to be deleted
	 * @param: ccmsId in community mapping to be deleted
	 * @param: clientIdSecret to identify plan via the API portal, for delete operation.
	 * @param: responseCode that is expected. Note: code 999 means we don't care about the response
	 */
	public String deleteCommunityMapping(String url, String oauthToken, String communityId, String ccmsId, String clientIdSecret, String responseCode) {
		//String contentType = "application/json";
		String responseString = null;
		String headers[]={"OAuth-Token", oauthToken};
		
		//log.info("Request String" + json.toString());
		HttpUtils restCalls = new HttpUtils();
		 //String postResponseString = restCalls.postRequest(url, headers, json, contentType, expectedResponse);
		
		if (communityId==null && ccmsId==null) {
			log.info("Attempt delete of unspecified community mapping");
			if (clientIdSecret!=null) {
				responseString = restCalls.deleteRequest(url + "?" + clientIdSecret, 
															headers, responseCode);
			} else {
				responseString = restCalls.deleteRequest(url, headers, responseCode);				
			}
		} else if (communityId!=null && ccmsId==null){
			log.info("Delete community mapping='" + communityId + "'");
			if (clientIdSecret!=null) {
				responseString = restCalls.deleteRequest(url + "?" + clientIdSecret + 
															"&communityId=" + communityId, 
															headers, responseCode);
			} else {
				responseString = restCalls.deleteRequest(url +   
															"&communityId=" + communityId, 
															headers, responseCode);				
			}
		} else if (communityId==null && ccmsId!=null) {
			log.info("Attempt delete of ccmsId='" + ccmsId + "' from unspecified community mapping");
			if (clientIdSecret!=null) {
				responseString = restCalls.deleteRequest(url + "?" + clientIdSecret + 
															"&ccmsId=" + ccmsId, 
															headers, responseCode);
			} else {
				responseString = restCalls.deleteRequest(url + 
															"&ccmsId=" + ccmsId, 
															headers, responseCode);				
			}
		} else if (communityId!=null && ccmsId!=null) {
			log.info("Delete community mapping='" + communityId + "'");
			if (clientIdSecret!=null) {
				responseString = restCalls.deleteRequest(url + "?" + clientIdSecret + 
															"&communityId=" + communityId +
															"&ccmsId=" + ccmsId, 
															headers, responseCode);
			} else {
				responseString = restCalls.deleteRequest(url + 
															"&communityId=" + communityId +
															"&ccmsId=" + ccmsId, 
															headers, responseCode);				
			}
		}
		
		if (responseString!=null) {
			System.out.println(responseString);	
		}
		return responseString;
	}
	
	private String getLinkId(String moduleName) {
			
		String linkId =  "";
		
		if (moduleName == "Calls") {
			linkId = "calls";
		} else if (moduleName == "Tasks") {
			linkId = "tasks";
		} else if (moduleName == "ibm_RevenueLineItems") {
			linkId = "opportun_revenuelineitems";
		} else if (moduleName == "Notes") {
			linkId = "notes_related_to_task";
		} else if (moduleName == "Contacts") {
			linkId = "contacts";
		} else if (moduleName == "AdditionalAssignees"){
			linkId = "additional_assignees_link";
		} else if (moduleName == "Users"){
			linkId = "users";
		} else if (moduleName == "Opportunity"){
			linkId = "opportunities";
		} else if (moduleName == "Assignee"){
			linkId = "additional_assignees_link";  
		}
		return linkId;	
	}
	 
	public String getOauthExtension(){
		return "oauth";
	}
	
	public String getMappingExtension(){
		return "collab/communityMappings";
	}


}
