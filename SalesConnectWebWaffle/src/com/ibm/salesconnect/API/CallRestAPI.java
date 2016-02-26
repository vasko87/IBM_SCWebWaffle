package com.ibm.salesconnect.API;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.salesconnect.common.HttpUtils;
import com.ibm.salesconnect.objects.Call;

public class CallRestAPI {
	
	public static final String callsURLExtension = "rest/v10/Calls";
	
	Logger log = LoggerFactory.getLogger(CallRestAPI.class);
	
	/*
	 * Creates a call via the SalesConnect REST APIs.
	 */
	public String createCall(String url, String oauthToken, String subject, String date, String status, String type, String durationMin,
		String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String body = "{\"name\":\"" + subject + "\",\"date_start\":\"" + date + "\",\"status\":\"" + status + "\",\"call_type\":\"" + type + "\",\"duration_minutes\":\"" + durationMin + "\",\"assigned_user_id\":\"" 

+ assignedUID + "\"}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + CallRestAPI.callsURLExtension, headers, body, contentType);
		
		try {
			//check for a valid JSON response
			JSONObject postResponse = (JSONObject)new JSONParser().parse(postResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
			return "false";
		}
		
		log.info("Valid JSON returned.");
		return postResponseString;
	}
	
	
	public String getCall(String url, String oauthToken, String subject, String expectedResponse){
		String headers[]={"OAuth-Token", oauthToken};
		
		HttpUtils restCalls = new HttpUtils();
		String getResponseString = restCalls.getRequest(url + CallRestAPI.callsURLExtension + "/" + subject, headers, expectedResponse);
		
		try {
			//check for a valid JSON response
			JSONObject getResponse = (JSONObject)new JSONParser().parse(getResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
			return "false";
		}
		System.out.println(getResponseString);
		log.info("Valid JSON returned.");
		return getResponseString;
	}
	
	public String relateNewContactToCall(String url, String oauthToken, String callId, String firstName, String lastName, String address, String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String body = "{\"first_name\":\"" + firstName + "\",\"last_name\":\"" + lastName + "\",\"primary_address_country\":\"" + address + "\",\"assigned_user_id\":\"" + assignedUID + "\"}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + CallRestAPI.callsURLExtension + "/" + callId + "/link/contacts", headers, body, contentType);
	    
		try {
			//check for a valid JSON response
			JSONObject postResponse = (JSONObject)new JSONParser().parse(postResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
			return "false";
		}
		
		log.info("Valid JSON returned.");
		return postResponseString;
	}
	
	public String relateNewNoteToCall(String url, String oauthToken, String callId, String name, String description,  String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String body = "{\"name\":\"" + name + "\",\"description\":\"" + description + "\",\"assigned_user_id\":\"" + assignedUID +  "\",\"parent_id\":\"" + callId + "\",\"parent_type\":\"Calls\"}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + CallRestAPI.callsURLExtension + "/" + callId + "/link/notes_related_to_call", headers, body, contentType);
	    
		try {
			//check for a valid JSON response
			JSONObject postResponse = (JSONObject)new JSONParser().parse(postResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
			return "false";
		}
		
		log.info("Valid JSON returned.");
		return postResponseString;
	}
	
	public String relateNewTaskToCall(String url, String oauthToken, String callId, String name, String dueDate, String priority, String status, String callType, String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String body = "{\"name\":\"" + name + "\",\"date_due\":\"" + dueDate +  "\",\"priority\":\"" + priority + "\",\"status\":\"" + status + "\",\"call_type\":\"" + callType +  "\",\"assigned_user_id\":\"" + 

assignedUID + "\",\"parent_id\":\"" + callId + "\",\"parent_type\":\"Calls\"}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + CallRestAPI.callsURLExtension + "/" + callId + "/link/tasks", headers, body, contentType);
	    
		try {
			//check for a valid JSON response
			JSONObject postResponse = (JSONObject)new JSONParser().parse(postResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
			return "false";
		}
		
		log.info("Valid JSON returned.");
		return postResponseString;
	}
	
	public String relateNewOpportunityToCall(String url, String oauthToken, String callId, String description, String source, String salesStage, String date, String clientId, String contactId, String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		//,\"parent_id\":\"" + callId + "\",\"parent_type\":\"Calls\"
		String body = "{\"description\":\"" + description + "\",\"lead_source\":\"" + source +  "\",\"date_closed\":\"" + date + "\",\"sales_stage\":\"" + salesStage + "\",\"account_id\":\"" + clientId + "\",\"contact_id_c\":\"" + contactId +  "\",\"assigned_user_id\":\"" + assignedUID + "\"}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + CallRestAPI.callsURLExtension + "/" + callId + "/link/opportunities", headers, body, contentType);
	    
		try {
			//check for a valid JSON response
			JSONObject postResponse = (JSONObject)new JSONParser().parse(postResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
			return "false";
		}
		
	
		log.info("Valid JSON returned.");
		return postResponseString;
	}
	
	/*
	 * Links an existing record to the provided Call using the SalesConnect REST APIs.
	 */
	public String linkRecordToCall(String url, String oauthToken, String callId, String recordId, String moduleName) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String linkId = getLinkId(moduleName);
		
		String body = "{\"id\":\"" + recordId + "\"}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + CallRestAPI.callsURLExtension + "/" + callId + "/link/" + linkId + "/" + recordId, headers, body, contentType);
		
		try {
			//check for a valid JSON response
			JSONObject postResponse = (JSONObject)new JSONParser().parse(postResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
			return "false";
		}
		
		log.info("Valid JSON returned.");
		return postResponseString;
	}
	
	/*
	 * Links an existing record to the provided Call using the SalesConnect REST APIs.
	 * @return true if successful, false if not
	 */
	public Boolean linkRecordToCallReturnBoolean(String url, String oauthToken, String callId, String recordId, String moduleName) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String linkId = getLinkId(moduleName);
		
		String body = null;
		if (linkId == "rel_opportunities"){
			body = "{\"id\":\"" + recordId + "\",\"reason_code\":\"GBSSWG" + "\"}";
		}
		else {
			body = "{\"id\":\"" + recordId + "\"}";
		}
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + CallRestAPI.callsURLExtension + "/" + callId + "/link/" + linkId + "/" + recordId, headers, body, contentType);
			
		try {
			//check for a valid JSON response
			new JSONParser().parse(postResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		
		log.info("Valid JSON returned." + postResponseString);
		return true;
	}
	
	/*
	 * Edits an existing Call using the SalesConnect REST APIs.
	 */
	public String editCall(String url, String oauthToken, String callId, String subject, String date, String status, String type, String duration, String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String body = "{\"id\":\"" + callId + "\",\"name\":\"" + subject + "\",\"date_start\":\"" + date + "\",\"status\":\"" + status + "\",\"call_type\":\"" + type + "\",\"duration_minutes\":\"" + duration + "\",\"assigned_user_id\":\"" + assignedUID + "\"}";
		HttpUtils restCalls = new HttpUtils();
		String putResponseString = restCalls.putRequest(url + CallRestAPI.callsURLExtension + "/" + callId, headers, body, contentType);
		
		try {
			//check for a valid JSON response
			JSONObject putResponse = (JSONObject)new JSONParser().parse(putResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
			return "false";
		}
		
		log.info("Valid JSON returned. " + putResponseString);
		return putResponseString;
	}
	
	/*
	 * Favorites or unfavorites an existing Call using the SalesConnect REST APIs.
	 */
	public String favoriteCall(String url, String oauthToken, String callId, String favorite, String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
	
		String body = "";
		
		HttpUtils restCalls = new HttpUtils();
		String putResponseString = restCalls.putRequest(url + CallRestAPI.callsURLExtension + "/" + callId + "/" + favorite, headers, body, contentType);
		
		try {
			//check for a valid JSON response
			JSONObject putResponse = (JSONObject)new JSONParser().parse(putResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
			return "false";
		}
		
		log.info("Valid JSON returned. " + putResponseString);
		return putResponseString;
	}
	
	/*
	 * Deletes an existing Call using the SalesConnect REST APIs.
	 */
	public String deleteCall(String url, String oauthToken, String callId) {
		String headers[]={"OAuth-Token", oauthToken};

		HttpUtils restCalls = new HttpUtils();
		String deleteResponseString = restCalls.deleteRequest(url + CallRestAPI.callsURLExtension + "/" + callId, headers);
		
		try {
			//check for a valid JSON response
			JSONObject deleteResponse = (JSONObject)new JSONParser().parse(deleteResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
			return "false";
		}
		
		log.info("Valid JSON returned. " + deleteResponseString);
		return deleteResponseString;
	}
	
	public String createCallreturnBean(String url, String oauthToken, String subject, String date, String status, String type, String duration, String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String callID = "";
		
		String body = "{\"name\":\"" + subject + "\",\"date_start\":\"" + date + "\",\"status\":\"" + status + "\",\"call_type\":\"" + type + "\",\"duration_minutes\":\"" + duration + "\",\"assigned_user_id\":\"" + 

assignedUID + "\"}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + CallRestAPI.callsURLExtension, headers, body, contentType);
		
		try {
			//check for a valid JSON response
			JSONObject postResponse = (JSONObject)new JSONParser().parse(postResponseString);
			callID = (String) postResponse.get("id");
		} catch (ParseException e) {
			e.printStackTrace();
			return "false";
		}
		
		log.info("Valid JSON returned.");
		return callID;
	}
	
	
    private String getLinkId(String moduleName) {
		
		String linkId =  "";
		
		if (moduleName == "Calls") {
			linkId = "calls";
		} else if (moduleName == "Tasks") {
			linkId = "tasks";
		} else if (moduleName == "Leads") {
			linkId = "leads";	
		}else if (moduleName == "Notes") {
			linkId = "notes";	
		} else if (moduleName == "ibm_RevenueLineItems") {
			linkId = "opportun_revenuelineitems";
		} else if (moduleName == "Notes") {
			linkId = "notes_related_to_call";
		} else if (moduleName == "Contacts") {
			linkId = "contacts";
		} else if (moduleName == "AdditionalAssignees"){
			linkId = "additional_assignees_link";
		} else if (moduleName == "Users"){
			linkId = "users";
		} else if (moduleName == "Opportunity"){
			linkId = "opportunities";	
		} else if (moduleName == "Opportunities"){
			linkId = "opportunities";	
		} else if (moduleName == "Assignee"){
			linkId = "additional_assignees_link";  
		}else if (moduleName == "AssignedUser"){
			linkId = "assigned_user_link";  
		}else if (moduleName == "Accounts") {
			linkId = "accounts";
		}
		
    
		
		return linkId;	
	}

public String createCallFromCall(String url, String oauthToken, Call call) {
	String contentType = "application/json";
	String headers[]={"OAuth-Token", oauthToken};
	
	String callID = "";
	
	String body = "{\"name\":\"" + call.sSubject + "\",\"date_start\":\"" + call.sCallDate + "\",\"status\":\"" + call.sCallStatus + "\",\"call_type\":\"" + call.sCallType + "\",\"duration_minutes\":\"" + call.sDuration 

+ "\",\"assigned_user_id\":\"" + call.sAssignedToID + "\"}";
	HttpUtils restCalls = new HttpUtils();
	String postResponseString = restCalls.postRequest(url + CallRestAPI.callsURLExtension, headers, body, contentType);
	
	try {
		//check for a valid JSON response
		JSONObject postResponse = (JSONObject)new JSONParser().parse(postResponseString);
		callID = (String) postResponse.get("id");
	} catch (ParseException e) {
		e.printStackTrace();
		return "false";
	}
	
	log.info("Valid JSON returned.");
	return callID;
}

/**
 * Post call via the rest api specifying the expected response
 * @param url
 * @param oauthToken
 * @param body of request
 * @param expectedResponse
 * @return response as a string
 */
public String postCall(String url, String oauthToken,String body, String expectedResponse){
	String headers[]={"OAuth-Token", oauthToken};
	
	HttpUtils restCalls = new HttpUtils();
	String PostResponseString = restCalls.postRequest(url,  headers, body,  "application/json ", expectedResponse);
	 
	try {
		//check for a valid JSON response
		new JSONParser().parse(PostResponseString);
	} 
	catch (ParseException e) 
	{
		e.printStackTrace();
		Assert.assertTrue(false, "Parse exception with post response");
	}
	
	log.info("Valid JSON returned." + PostResponseString);
	return PostResponseString;
}

/**
 * Put call via the rest api specifying the expected response
 * @param url
 * @param oauthToken
 * @param body of request
 * @param expectedResponse
 * @return response as a string
 */
public String putCall(String url, String oauthToken,String body, String expectedResponse){
	String headers[]={"OAuth-Token", oauthToken};
	
	HttpUtils restCalls = new HttpUtils();
	String PutResponseString = restCalls.putRequest(url,  headers, body,  "application/json ", expectedResponse);
	 
	try {
		//check for a valid JSON response
		new JSONParser().parse(PutResponseString);
	} 
	catch (ParseException e) 
	{
		e.printStackTrace();
		Assert.assertTrue(false, "Parse exception with Put response");
	}
	
	log.info("Valid JSON returned." + PutResponseString);
	return PutResponseString;
}

/**
 * Get Call via the rest api specifying the expected response
 * @param url
 * @param oauthToken
 * @param expectedResponse
 * @return response as a string
 */
public String getCall(String url, String oauthToken, String expectedResponse){
	String headers[]={"OAuth-Token", oauthToken};
	
	HttpUtils restCalls = new HttpUtils();
	 String getResponseString = restCalls.getRequest(url, headers, expectedResponse);
	 
	try {
		//check for a valid JSON response
		new JSONParser().parse(getResponseString);
	} catch (ParseException e) {
		e.printStackTrace();
		Assert.assertTrue(false, "Parse exception with get response");
	}
	
	log.info("Valid JSON returned." + getResponseString);
	return getResponseString;
}

/**
 * Delete Call using DELETE
 * @param url
 * @param oauthToken
 * @param body
 * @param expectedResponseCode
 */
public void deleteCall(String url, String oauthToken, String body, String expectedResponseCode){
	String headers[]={"OAuth-Token", oauthToken};
	
	HttpUtils restCalls = new HttpUtils();
	String deleteResponseString = restCalls.deleteRequest(url, headers, body, expectedResponseCode);
	
	try {
		//check for a valid JSON response
		new JSONParser().parse(deleteResponseString);
	} catch (ParseException e) {
		e.printStackTrace();
	}
	
	log.info("Valid JSON returned." + deleteResponseString);
}

}
