package com.ibm.salesconnect.API;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.salesconnect.common.HttpUtils;

public class TaskRestAPI {
	
	public static final String tasksURLExtension = "rest/v10/Tasks";
	
	
	Logger log = LoggerFactory.getLogger(TaskRestAPI.class);
	
	/*
	 * Creates a task via the SalesConnect REST APIs.
	 */
	public String createTask(String url, String oauthToken, String name, String date, String priority, String status, String type, String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String body = "{\"name\":\"" + name + "\",\"date_due\":\"" + date + "\",\"priority\":\"" + priority + "\",\"status\":\"" + status + "\",\"call_type\":\"" + type + "\",\"assigned_user_id\":\"" + assignedUID + "\"}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + TaskRestAPI.tasksURLExtension, headers, body, contentType);
		
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
	
	public String relateNewContactToTask(String url, String oauthToken, String taskId, String firstName, String lastName, String address, String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String body = "{\"first_name\":\"" + firstName + "\",\"last_name\":\"" + lastName + "\",\"primary_address_country\":\"" + address + "\",\"assigned_user_id\":\"" + assignedUID + "\"}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + TaskRestAPI.tasksURLExtension + "/" + taskId + "/link/contacts", headers, body, contentType);
	    
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
	
	public String relateNewNoteToTask(String url, String oauthToken, String taskId, String name, String description,  String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String body = "{\"name\":\"" + name + "\",\"description\":\"" + description + "\",\"assigned_user_id\":\"" + assignedUID +  "\",\"parent_id\":\"" + taskId + "\",\"parent_type\":\"Tasks\"}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + TaskRestAPI.tasksURLExtension + "/" + taskId + "/link/notes_related_to_task", headers, body, contentType);
	    
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
	
	public String relateNewOpportunityToTask(String url, String oauthToken, String taskId, String description, String source, String salesStage, String date, String clientId, String contactId, String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String body = "{\"description\":\"" + description + "\",\"lead_source\":\"" + source +  "\",\"date_closed\":\"" + date + "\",\"sales_stage\":\"" + salesStage + "\",\"account_id\":\"" + clientId + "\",\"contact_id_c\":\"" + contactId +  "\",\"assigned_user_id\":\"" + assignedUID + "\"}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + TaskRestAPI.tasksURLExtension + "/" + taskId + "/link/opportunities", headers, body, contentType);
	    
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
	
	public String relateNewTaskToTask(String url, String oauthToken, String taskId, String name, String dueDate, String priority, String status, String callType, String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String body = "{\"name\":\"" + name + "\",\"date_due\":\"" + dueDate +  "\",\"priority\":\"" + priority + "\",\"status\":\"" + status + "\",\"call_type\":\"" + callType +  "\",\"assigned_user_id\":\"" + assignedUID + "\",\"parent_id\":\"" + taskId + "\",\"parent_type\":\"Tasks\"}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + TaskRestAPI.tasksURLExtension + "/" + taskId + "/link/tasks", headers, body, contentType);
	    
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
	public String linkRecordToTask(String url, String oauthToken, String taskId, String recordId, String moduleName) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String linkId = getLinkId(moduleName);
		
		String body = "{\"id\":\"" + recordId + "\"}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + TaskRestAPI.tasksURLExtension + "/" + taskId + "/link/" + linkId + "/" + recordId, headers, body, contentType);
		
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
	
	/**
	 * Links a record to a task and verifies that it was successful
	 * @param url
	 * @param oauthToken
	 * @param taskId
	 * @param recordId
	 * @param moduleName
	 */
	public void linkRecordToTaskAndVerify(String url, String oauthToken, String taskId, String recordId, String moduleName) {
		String jsonString = linkRecordToTask(url, oauthToken, taskId, recordId, moduleName);
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Linking task to task failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			//validate the task was related to call as expected
			Assert.assertEquals((String) record.get("id"), taskId, "Parent record was not returned as expected.");
		} catch (Exception e) {
			Assert.assertTrue(false, "Task was not linked to Task as expected.");
		}
	}
	
	/**
	 * Links a record to a task and verifies that it was successful
	 * @param url
	 * @param oauthToken
	 * @param taskId
	 * @param recordId
	 * @param moduleName
	 */
	public String linkRecordToTaskAndVerifyReturn(String url, String oauthToken, String taskId, String recordId, String moduleName) {
		String jsonString = linkRecordToTask(url, oauthToken, taskId, recordId, moduleName);
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Linking task to task failed.");
		} 
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			//validate the task was related to call as expected
			Assert.assertEquals((String) record.get("id"), taskId, "Parent record was not returned as expected.");
		} catch (Exception e) {
			Assert.assertTrue(false, "Task was not linked to Task as expected.");
		}
		return jsonString;
	}
	
	/*
	 * Edits an existing Task using the SalesConnect REST APIs.
	 */
	public String editTask(String url, String oauthToken, String taskId, String name, String priority, String status, String type, String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String body = "{\"id\":\"" + taskId + "\",\"name\":\"" + name + "\",\"priority\":\"" + priority + "\",\"status\":\"" + status + "\",\"call_type\":\"" + type + "\",\"assigned_user_id\":\"" + assignedUID + "\"}";
		HttpUtils restCalls = new HttpUtils();
		String putResponseString = restCalls.putRequest(url + TaskRestAPI.tasksURLExtension + "/" + taskId, headers, body, contentType);
		
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
	 * Favorites or unfavorites an existing Task using the SalesConnect REST APIs.
	 */
	public String favoriteTask(String url, String oauthToken, String taskId, String favorite, String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
	
		String body = "";
		
		HttpUtils restCalls = new HttpUtils();
		String putResponseString = restCalls.putRequest(url + TaskRestAPI.tasksURLExtension + "/" + taskId + "/" + favorite, headers, body, contentType);
		
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
	 * Deletes an existing Task using the SalesConnect REST APIs.
	 */
	public String deleteTask(String url, String oauthToken, String taskId) {
		String headers[]={"OAuth-Token", oauthToken};

		HttpUtils restCalls = new HttpUtils();
		String deleteResponseString = restCalls.deleteRequest(url + TaskRestAPI.tasksURLExtension + "/" + taskId, headers);
		
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
	
	/*
	 * Deletes an existing Task using the SalesConnect REST APIs.
	 */
	public String deleteTaskAPIm(String url, String oauthToken, String expectedResponse) {
		String headers[]={"OAuth-Token", oauthToken};

		HttpUtils restCalls = new HttpUtils();
		String deleteResponseString = restCalls.deleteRequest(url, headers, expectedResponse);
		
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
	
	public String editTaskAPIm(String url, String oauthToken,String body, String expectedResponse) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		HttpUtils restCalls = new HttpUtils();
		String putResponseString = restCalls.putRequest(url, headers, body, contentType, expectedResponse);
		
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
	
	
	/**
	 * Create a task via the rest api specifying the expected response
	 * @param url
	 * @param oauthToken
	 * @param json
	 * @param expectedResponse
	 * @return response as a string
	 */
	public String createTask(String url, String oauthToken, JSONObject json, String expectedResponse) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		log.info("Request String" + json.toString());
		HttpUtils restCalls = new HttpUtils();
		 String postResponseString = restCalls.postRequest(url, headers, json, contentType, expectedResponse);
		 
		try {
			//check for a valid JSON response
			new JSONParser().parse(postResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
			Assert.assertTrue(false, "Parse exception with post response");
		}
		
		log.info("Valid JSON returned.");
		return postResponseString;
	}
	
	/**
	 * Get tasl via the rest api specifying the expected response
	 * @param url
	 * @param oauthToken
	 * @param expectedResponse
	 * @return response as a string
	 */
	public String getTask(String url, String oauthToken, String expectedResponse){
		String headers[]={"OAuth-Token", oauthToken};
		
		HttpUtils restCalls = new HttpUtils();
		 String getResponseString = restCalls.getRequest(url, headers, expectedResponse);
		 
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
	
	public String getTask(String url, String oauthToken, String taskName, String expectedResponse){
		String headers[]={"OAuth-Token", oauthToken};
		
		HttpUtils restCalls = new HttpUtils();
		 String getResponseString = restCalls.getRequest(url + tasksURLExtension + "/" + taskName, headers, expectedResponse);
		 
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
	
	public String postTask(String url, String oauthToken,String body, String expectedResponse){
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
		
		log.info("Valid JSON returned.");
		return PostResponseString;
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
				linkId = "notes";
			} else if (moduleName == "Accounts") {
				linkId = "accounts";
			} else if (moduleName == "Contacts") {
				linkId = "contacts";
			} else if (moduleName == "AdditionalAssignees"){
				linkId = "additional_assignees_link";
			} else if (moduleName == "Users"){
				linkId = "additional_assignees_link";
			} else if (moduleName == "Opportunity"){
				linkId = "opportunities";
			} else if (moduleName == "Assignee"){
				linkId = "additional_assignees_link";  
		} else if (moduleName == "Mail"){
			linkId = "mail";  
		}
		else if (moduleName == "Meetings"){
			linkId = "meetings";  
		}
			
			return linkId;	
		}

}
