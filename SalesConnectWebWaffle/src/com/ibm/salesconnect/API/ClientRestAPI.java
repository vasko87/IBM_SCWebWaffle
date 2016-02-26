package com.ibm.salesconnect.API;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.salesconnect.common.HttpUtils;

public class ClientRestAPI {
	
public static final String accountsURLExtension = "rest/v10/Accounts";
	

	Logger log = LoggerFactory.getLogger(ClientRestAPI.class);
	
	
	/*
	 * Creates a new call and relates it to the provided Client using the SalesConnect REST APIs.
	 */
	public String relateNewCallToClient(String url, String oauthToken, String clientId, String callSubject, String callDate, String callStatus, String callType, String callDuration, String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String body = "{\"name\":\"" + callSubject + "\",\"date_start\":\"" + callDate + "\",\"status\":\"" + callStatus + "\",\"call_type\":\"" + callType + "\",\"duration_minutes\":\"" + callDuration + "\",\"duration_hours\":\"0\",\"assigned_user_id\":\"" + assignedUID + "\",\"parent_id\":\"" + clientId + "\",\"parent_type\":\"Accounts\"}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + ClientRestAPI.accountsURLExtension + "/" + clientId + "/link/calls", headers, body, contentType);
		
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
	 * Creates a new note and relates it to the provided client using the SalesConnect REST APIs.
	 */
	public String relateNewNoteToClient(String url, String oauthToken, String clientId, String noteName, String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String body = "{\"name\":\"" + noteName + "\",\"assigned_user_id\":\"" + assignedUID + "\",\"parent_id\":\"" + clientId + "\",\"parent_type\":\"Accounts\"}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + ClientRestAPI.accountsURLExtension + "/" + clientId + "/link/notes", headers, body, contentType);
		
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
	 * Creates a new task and relates it to the provided client using the SalesConnect REST APIs.
	 */
	public String relateNewTaskToClient(String url, String oauthToken, String clientId, String taskName, String taskDate, String taskPriority, String taskStatus, String taskType, String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String body = "{\"name\":\"" + taskName + "\",\"date_due\":\"" + taskDate + "\",\"priority\":\"" + taskPriority + "\",\"status\":\"" + taskStatus + "\",\"call_type\":\"" + taskType + "\",\"assigned_user_id\":\"" + assignedUID + "\",\"parent_id\":\"" + clientId + "\",\"parent_type\":\"Accounts\"}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + ClientRestAPI.accountsURLExtension + "/" + clientId + "/link/tasks", headers, body, contentType);
		
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
	 * Creates a new contact and relates it to the provided client using the SalesConnect REST APIs.
	 */
	public String relateNewContactToClient(String url, String oauthToken, String clientId, String contactFirstName, String contactLastName, String contactCountry, String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String body = "{\"first_name\":\"" + contactFirstName + "\",\"last_name\":\"" + contactLastName + "\",\"primary_address_country\":\"" + contactCountry + "\",\"assigned_user_id\":\"" + assignedUID + "\"}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + ClientRestAPI.accountsURLExtension + "/" + clientId + "/link/contacts", headers, body, contentType);
		
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
	 * Creates a new opportunity and relates it to the provided client using the SalesConnect REST APIs.
	 */
	public String relateNewOpportunityToClient(String url, String oauthToken, String clientId, String contactId, String desc, String source, String salesStage, String date, String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String body = "{\"description\":\"" + desc + "\",\"lead_source\":\"" + source + "\",\"sales_stage\":\"" + salesStage + "\",\"date_closed\":\"" + date + "\",\"account_id\":\"" + clientId + "\",\"contact_id_c\":\"" + contactId + "\",\"assigned_user_id\":\"" + assignedUID + "\"}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + ClientRestAPI.accountsURLExtension + "/" + clientId + "/link/opportunities", headers, body, contentType);
		
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
	 * Links an existing record to the provided client using the SalesConnect REST APIs.
	 */
	public String linkRecordToClient(String url, String oauthToken, String clientId, String recordId, String moduleName) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String linkId = getLinkId(moduleName);
		
		String body = "{\"id\":\"" + recordId + "\"}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + ClientRestAPI.accountsURLExtension + "/" + clientId + "/link/" + linkId + "/" + recordId, headers, body, contentType);
		
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
	
	public String favoriteClient(String url, String oauthToken, String clientId, String favorite, String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};

		String body = "";
		
		HttpUtils restCalls = new HttpUtils();
		String putResponseString = restCalls.putRequest(url + ClientRestAPI.accountsURLExtension + "/" + clientId + "/" + favorite, headers, body, contentType);
		
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
	 *  PRIVATE HELPERS
	 * -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	 */
	
	/*
	 * Returns the client link id for the given module.
	 */
	private String getLinkId(String moduleName) {
		
		String linkId =  "";
		
		if (moduleName == "Calls") {
			linkId = "calls";
		} else if (moduleName == "Tasks") {
			linkId = "tasks";
		} else if (moduleName == "Users") {
			linkId = "rel_additional_users";
		} else if (moduleName == "Notes") {
			linkId = "notes";
		} else if (moduleName == "Contacts") {
			linkId = "contacts";
		}
		else if (moduleName == "Meetings") {
			linkId = "meetings";
		}
		else if (moduleName == "Users") {
			linkId = "users";
		}
		
		return linkId;	
	}

}
