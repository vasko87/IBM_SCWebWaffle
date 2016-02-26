package com.ibm.salesconnect.API;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.salesconnect.common.HttpUtils;
import com.ibm.salesconnect.objects.Note;

public class NoteRestAPI {
	
	public static final String notesURLExtension = "rest/v10/Notes";
	
	
	Logger log = LoggerFactory.getLogger(NoteRestAPI.class);
	
	
	/*
	 * Creates a note via the SalesConnect REST APIs.
	 */
	public String createNote(String url, String oauthToken, String subject, String description, String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String body = "{\"name\":\"" + subject + "\",\"description\":\"" + description + "\",\"assigned_user_id\":\"" + assignedUID + "\"}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + NoteRestAPI.notesURLExtension, headers, body, contentType);
		
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
	
	public String createNotereturnBean(String url, String oauthToken, String subject, String description, String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String noteID = "";
		
		String body = "{\"name\":\"" + subject + "\",\"description\":\"" + description + "\",\"assigned_user_id\":\"" + assignedUID + "\"}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + NoteRestAPI.notesURLExtension, headers, body, contentType);
		
		try {
			//check for a valid JSON response
			JSONObject postResponse = (JSONObject)new JSONParser().parse(postResponseString);
			noteID = (String) postResponse.get("id");
		} catch (ParseException e) {
			e.printStackTrace();
			return "false";
		}
		
		log.info("Valid JSON returned.");
		return noteID;
	}
	
	public String getNote(String url, String oauthToken, String noteID, String expectedResponse){
		String headers[]={"OAuth-Token", oauthToken};
		
		HttpUtils restCalls = new HttpUtils();
		 String getResponseString = restCalls.getRequest(url + NoteRestAPI.notesURLExtension + "/" + noteID, headers, expectedResponse);
		 
		try {
			//check for a valid JSON response
			new JSONParser().parse(getResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
			Assert.assertTrue(false, "Parse exception with post response");
		}
		
		log.info("Valid JSON returned.");
		return getResponseString;
	}
	
	public String relateNewContactToNote(String url, String oauthToken, String noteId, String firstName, String lastName, String address, String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String body = "{\"first_name\":\"" + firstName + "\",\"last_name\":\"" + lastName + "\",\"primary_address_country\":\"" + address + "\",\"assigned_user_id\":\"" + assignedUID + "\"}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + NoteRestAPI.notesURLExtension + "/" + noteId + "/link/contacts", headers, body, contentType);
	    
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
	
	public String relateNewOpportunityToNote(String url, String oauthToken, String noteId, String description, String source, String salesStage, String date, String clientId, String contactId, String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String body = "{\"description\":\"" + description + "\",\"lead_source\":\"" + source +  "\",\"date_closed\":\"" + date + "\",\"sales_stage\":\"" + salesStage + "\",\"account_id\":\"" + clientId + "\",\"contact_id_c\":\"" + contactId +  "\",\"assigned_user_id\":\"" + assignedUID + "\"}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + NoteRestAPI.notesURLExtension + "/" + noteId + "/link/opportunities", headers, body, contentType);
	    
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
	
	public String relateNewTaskToNote(String url, String oauthToken, String noteId, String name, String dueDate, String priority, String status, String callType, String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String body = "{\"name\":\"" + name + "\",\"date_due\":\"" + dueDate +  "\",\"priority\":\"" + priority + "\",\"status\":\"" + status + "\",\"call_type\":\"" + callType +  "\",\"assigned_user_id\":\"" + assignedUID + "\",\"parent_id\":\"" + noteId + "\",\"parent_type\":\"Notes\"}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + NoteRestAPI.notesURLExtension + "/" + noteId + "/link/tasks", headers, body, contentType);
	    
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
	public String linkRecordToNote(String url, String oauthToken, String noteId, String recordId, String moduleName) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String linkId = getLinkId(moduleName);
		
		String body = "{\"id\":\"" + recordId + "\"}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + NoteRestAPI.notesURLExtension + "/" + noteId + "/link/" + linkId + "/" + recordId, headers, body, contentType);
		
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
	
	public String favoriteNote(String url, String oauthToken, String noteId, String favorite, String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};

		String body = "";
		
		HttpUtils restCalls = new HttpUtils();
		String putResponseString = restCalls.putRequest(url + NoteRestAPI.notesURLExtension + "/" + noteId + "/" + favorite, headers, body, contentType);
		
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
	 * Edits an existing Note using the SalesConnect REST APIs.
	 */
	public String editNote(String url, String oauthToken, String noteId, String name, String description, String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String body = "{\"id\":\"" + noteId + "\",\"name\":\"" + name + "\",\"description\":\"" + description +  "\",\"assigned_user_id\":\"" + assignedUID + "\"}";
		HttpUtils restCalls = new HttpUtils();
		String putResponseString = restCalls.putRequest(url + NoteRestAPI.notesURLExtension + "/" + noteId, headers, body, contentType);
		
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
	 * Deletes an existing Note using the SalesConnect REST APIs.
	 */
	public String deleteNote(String url, String oauthToken, String noteId) {
		String headers[]={"OAuth-Token", oauthToken};
		
		HttpUtils restCalls = new HttpUtils();
		String deleteResponseString = restCalls.deleteRequest(url + NoteRestAPI.notesURLExtension + "/" + noteId, headers);
		
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
	
	public String createNoteFromNote(String url, String oauthToken, Note note) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String body = "{\"name\":\"" + note.sSubject + "\",\"description\":\"" + note.sDescription + "\",\"assigned_user_id\":\"" + note.sAssignedUID + "\"}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + NoteRestAPI.notesURLExtension, headers, body, contentType);
		
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
		} else if (moduleName == "Client") {
			linkId = "accounts";
		}
    
		
		return linkId;	
	}

}
