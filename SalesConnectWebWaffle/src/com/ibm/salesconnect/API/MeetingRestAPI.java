
package com.ibm.salesconnect.API;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.salesconnect.common.HttpUtils;

/**
 * @author aaronFortune
 * @date mar 2, 2015
 */
public class MeetingRestAPI {

	public static final String meetingURLExtension = "rest/v10/Meetings";

	Logger log = LoggerFactory.getLogger(EmailRestAPI.class);

	
	public String createMeeting(String url, String oauthToken, String subject, String description, String startDate, String duration_hours, String duration_minutes, String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String body = "{\"name\":\"" + subject + "\",\"description\":\"" + description + "\",\"assigned_user_id\":\"" + assignedUID + "\",\"date_start\":\"" + startDate + "\",\"duration_hours\":\"" + duration_hours + "\",\"duration_minutes\":\"" + duration_minutes + "\"}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + MeetingRestAPI.meetingURLExtension, headers, body, contentType);
		
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
	
	public String createMeetingreturnBean(String url, String oauthToken,
			String assignedUID) {
		String contentType = "application/json";
		String headers[] = { "OAuth-Token", oauthToken };

		String meetingID = "";

		String body = getValidMeetingBody(assignedUID);
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url
				+ meetingURLExtension, headers, body, contentType);

		try {
			// check for a valid JSON response
			JSONObject postResponse = (JSONObject) new JSONParser()
					.parse(postResponseString);
			meetingID = (String) postResponse.get("id");
		} catch (ParseException e) {
			e.printStackTrace();
			return "false";
		}

		log.info("Valid JSON returned.");
		return meetingID;
	}

	public String relateNewContactToMeeting(String url, String oauthToken,
			String meetingId, String firstName, String LastName,
			String address, String assignedUID) {
		String contentType = "application/json";
		String headers[] = { "OAuth-token", oauthToken };

		String body = "{\"first_name\":\"" + firstName + "\",\"last_name\":\""
				+ LastName + "\",\"primary_address_country\":\"" + address
				+ "\",\"assigned_user_id\":\"" + assignedUID + "\"}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url
				+ MeetingRestAPI.meetingURLExtension + "/" + meetingId
				+ "/link/contacts", headers, body, contentType);

		try {
			// check for a valid JSON response
			JSONObject postResponse = (JSONObject) new JSONParser()
					.parse(postResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
			return "false";
		}

		log.info("Valid JSON returned.");
		return postResponseString;
	}
	
	/**
	 * @param url
	 * @param oauthToken
	 * @param contactId
	 * @param description
	 * @param source
	 * @param salesStage
	 * @param date
	 * @param clientId
	 * @param meetingId
	 * @param assignedUID
	 * @return
	 */
	public String relateNewOpportunityToMeeting(String url, String oauthToken,
			String meetingId, String description, String source, String salesStage, String date, 
			String clientId, String contactId, String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String body = "{\"description\":\"" + description + "\",\"lead_source\":\"" + source +  "\",\"date_closed\":\"" + date + "\",\"sales_stage\":\"" + salesStage + "\",\"account_id\":\"" + clientId + "\",\"contact_id_c\":\"" + contactId + "\",\"assigned_user_id\":\"" + assignedUID +  "\"}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + MeetingRestAPI.meetingURLExtension + "/" + meetingId + "/link/opportunities", headers, body, contentType);
	    
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

	public String relateNewTaskToMeeting(String url, String oauthToken, String meetingId, String taskName, String taskDate, String taskPriority, String taskStatus, String taskType, String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String body = "{\"name\":\"" + taskName + "\",\"date_due\":\"" + taskDate + "\",\"priority\":\"" + taskPriority + "\",\"status\":\"" + taskStatus + "\",\"call_type\":\"" + taskType + "\",\"assigned_user_id\":\"" + assignedUID + "\",\"parent_id\":\"" + meetingId + "\",\"parent_type\":\"Accounts\"}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + MeetingRestAPI.meetingURLExtension + "/" + meetingId + "/link/tasks", headers, body, contentType);
		
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
	
	public String relateNewNoteToMeeting(String url, String oauthToken, String meetingId, String name, String description,  String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String body = "{\"name\":\"" + name + "\",\"description\":\"" + description + "\",\"assigned_user_id\":\"" + assignedUID +  "\",\"parent_id\":\"" + meetingId + "\",\"parent_type\":\"meetings\"}";
		HttpUtils restmeetings = new HttpUtils();
		String postResponseString = restmeetings.postRequest(url + MeetingRestAPI.meetingURLExtension + "/" + meetingId + "/link/notes_related_to_meeting", headers, body, contentType);
	    
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
	public String getValidMeetingBody(String assignedUID) {
		return "{\"date_end\": \"2019-10-28T15:13:00.000Z\",\"date_start\": \"2019-10-28T15:14:00.000Z\",\"duration_hours\": \"1\",\"duration_minutes\": \"15\",\"description\": \"hello world\","
				+ "\"location\": \"Dublin\",\"name\": \"Test\",\"team_id\": \"1\",\"assigned_user_id\":\""
				+ assignedUID + "\",\"type\": \"Sugar\"}";
	}

	public String linkRecordToMeeting(String url, String oauthToken, String meetingId, String recordId, String moduleName) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String linkId = getLinkId(moduleName);
		
		String body = "{\"id\":\"" + recordId + "\"}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + MeetingRestAPI.meetingURLExtension + "/" + meetingId + "/link/" + linkId + "/" + recordId, headers, body, contentType);
		try 
		{
			//check for a valid JSON response
			JSONObject postResponse = (JSONObject)new JSONParser().parse(postResponseString);
		} 
		catch (ParseException e) 
		{
			e.printStackTrace();
			return "false";
		}
		
		log.info("Valid JSON returned.");
		return postResponseString;
	}
	

	public String favoriteMeeting(String url, String oauthToken, String meetingId, String favorite, String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};

		String body = "";
		
		HttpUtils restCalls = new HttpUtils();
		String putResponseString = restCalls.putRequest(url +MeetingRestAPI.meetingURLExtension + "/" + meetingId + "/" + favorite, headers, body, contentType);
		
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


	public String editMeeting(String url,String oauthToken,String meetId,String name,String description,String assignedUID)
	{
		String contectType="application/json";
		String headers[]={"OAuth-Token",oauthToken};
		String body="{\"id\":\""+meetId+"\",\"name\":\""+name+"\",\"description\":\""+description+"\",\"assigned_user_id\":\""+assignedUID+"\"}";
		HttpUtils restCalls = new HttpUtils();
		String putResponseString = restCalls.putRequest(url+MeetingRestAPI.meetingURLExtension+"/"+meetId, headers, body, contectType);
		try
		{
		JSONObject putResponse=(JSONObject)new JSONParser().parse(putResponseString);
		}
		catch(ParseException e)
		{
			e.printStackTrace();
			return "false";
		}
		log.info("Valid JSON returned. " + putResponseString);
		return putResponseString;
		}
	
	public String deleteMeeting(String url, String oauthToken, String meetingId) {
		String headers[]={"OAuth-Token", oauthToken};
		
		HttpUtils restCalls = new HttpUtils();
		String deleteResponseString = restCalls.deleteRequest(url + MeetingRestAPI.meetingURLExtension + "/" + meetingId, headers);
		
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

	private String getLinkId(String moduleName) {
		String LinkId = "";

		if (moduleName == "Calls") {
			LinkId = "calls";
		} else if (moduleName == "Task") {
			LinkId = "tasks";
		} else if (moduleName == "Users") {
			LinkId = "users";
		} else if (moduleName == "Contacts") {
			LinkId = "contacts";
		} else if (moduleName == "Client") {
			LinkId = "accounts";
		} else if (moduleName == "Opportunity") {
			LinkId = "opportunity";
		} else if (moduleName == "Note") {
			LinkId = "notes";
		}
		
		return LinkId;
	}

}
