package com.ibm.salesconnect.API;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.common.HttpUtils;

public class ContactRestAPI {
	
	public static final String contactsURLExtension = "rest/v10/Contacts";
	
	Logger log = LoggerFactory.getLogger(ContactRestAPI.class);
	
	/*
	 * Creates a contact via the SalesConnect REST APIs.
	 */
	public String createContact(String url, String oauthToken, String firstName, String lastName, String country, String officePhone, String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String body = "{\"first_name\":\"" + firstName + "\",\"last_name\":\"" + lastName + "\",\"primary_address_country\":\"" + country + "\",\"phone_work\":\"" + officePhone + "\",\"assigned_user_id\":\"" + assignedUID + "\"}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + ContactRestAPI.contactsURLExtension, headers, body, contentType);
		
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
	
	public String getContactFilter(String url, String oauthToken, String searchTerm, String fieldName, String expectedResponse){
		String headers[]={"OAuth-Token", oauthToken};
		
		HttpUtils restCalls = new HttpUtils();
		String getResponseString = restCalls.getRequest(url + ContactRestAPI.contactsURLExtension + "?q=" + searchTerm + "&fields=" + fieldName, headers, expectedResponse);
		
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
	
	public String getContact(String url, String oauthToken, String contactID, String expectedResponse){
		String headers[]={"OAuth-Token", oauthToken};
		
		HttpUtils restCalls = new HttpUtils();
		String getResponseString = restCalls.getRequest(url + ContactRestAPI.contactsURLExtension + "/" + contactID, headers, expectedResponse);
		
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
	
	/**
	 * Method to create a contact and return the bean id for that contact
	 * @param url
	 * @param oauthToken
	 * @param firstName
	 * @param lastName
	 * @param country
	 * @param officePhone
	 * @param assignedUID
	 * @return
	 */
	public String createContactreturnBean(String url, String headers[], String firstName, String lastName, String country, String officePhone, String assignedUID) {
		String contentType = "application/json";
		
		String body = "{\"first_name\":\"" + firstName + "\",\"last_name\":\"" + lastName + "\",\"primary_address_country\":\"" + country + "\",\"phone_work\":\"" + officePhone + "\",\"assigned_user_id\":\"" + assignedUID + "\"}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + ContactRestAPI.contactsURLExtension, headers, body, contentType);
		
		String contactId = "";
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(postResponseString);
			contactId = (String) postResponse.get("id");
		} catch (Exception e) {
			Assert.assertTrue(false, "Contact was not created as expected.");
		}
		
		log.info("Contact successfully created.");
		

		return contactId;
	}
	
	public String createContactreturnCIContactID(String url, String headers[], String firstName, String lastName, String country, String officePhone, String assignedUID) {
		String contentType = "application/json";
		
		String body = "{\"first_name\":\"" + firstName + "\",\"last_name\":\"" + lastName + "\",\"primary_address_country\":\"" + country + "\",\"phone_work\":\"" + officePhone + "\",\"assigned_user_id\":\"" + assignedUID + "\"}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + ContactRestAPI.contactsURLExtension, headers, body, contentType);
		
		String contactId = "";
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(postResponseString);
			contactId = (String) postResponse.get("ext_ref_id1_c");
		} catch (Exception e) {
			Assert.assertTrue(false, "Contact was not created as expected.");
		}
		
		log.info("Contact successfully created.");
		

		return contactId;
	}
	
	public String getContactBeanIDFromExtRefID(String url, String headers[], String extRefID){
		String contentType = "application/json";
		
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.getRequest(url + ContactRestAPI.contactsURLExtension, headers, contentType);
		
		String contactId = "";
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(postResponseString);
			contactId = (String) postResponse.get("ext_ref_id1_c");
		} catch (Exception e) {
			Assert.assertTrue(false, "Contact was not created as expected.");
		}
		
		log.info("Contact successfully created.");
		

		return contactId;
	}
	
	/**
	 * Method to create a contact specifying client id and return the bean id for that contact
	 * @param url
	 * @param oauthToken
	 * @param firstName
	 * @param lastName
	 * @param country
	 * @param officePhone
	 * @param assignedUID
	 * @return
	 */
	public String createContactreturnBean(String url, String headers[], String firstName, String lastName, String country, String officePhone, String assignedUID, String clientBeanID) {
		String contentType = "application/json";
		
		String body = "{\"first_name\":\"" + firstName + "\",\"last_name\":\"" + lastName + "\",\"primary_address_country\":\"" + country + "\",\"phone_work\":\"" + officePhone +
		"\",\"assigned_user_id\":\"" + assignedUID + "\",\"account_id\":\"" + clientBeanID + "\"}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + ContactRestAPI.contactsURLExtension, headers, body, contentType);
		
		String contactId = "";
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(postResponseString);
			contactId = (String) postResponse.get("id");
		} catch (Exception e) {
			Assert.assertTrue(false, "Contact was not created as expected.");
		}
		
		log.info("Contact successfully created.");
		

		return contactId;
	}
	
	/*
	 * Links an existing record to the provided Contact using the SalesConnect REST APIs.
	 */
	public String linkRecordToContact(String url, String oauthToken, String contactId, String recordId, String moduleName) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String body = "{\"id\":\"" + recordId + "\"}";
		HttpUtils restCalls = new HttpUtils();
		String linkId = getLinkId(moduleName);
		String postResponseString = restCalls.postRequest(url + ContactRestAPI.contactsURLExtension + "/" + contactId + "/link/" + linkId + "/" + recordId, headers, body, contentType);
		
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
		else if(moduleName=="Meetings")
		{
			linkId="meetings";
		}
    
		
		return linkId;	
	}

	public String favoriteContact(String url, String oauthToken, String contactId, String favorite, String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};

		String body = "";
		
		HttpUtils restCalls = new HttpUtils();
		String putResponseString = restCalls.putRequest(url + ContactRestAPI.contactsURLExtension + "/" + contactId + "/" + favorite, headers, body, contentType);
		
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
	 * Deletes an existing Contact using the SalesConnect REST APIs.
	 */
	public String deleteContact(String url, String oauthToken, String contactId) {
		String headers[]={"OAuth-Token", oauthToken};

		HttpUtils restCalls = new HttpUtils();
		String deleteResponseString = restCalls.deleteRequest(url + ContactRestAPI.contactsURLExtension + "/" + contactId, headers);
		
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
	 * Edits an existing Contact using the SalesConnect REST APIs.
	 */
	public String editContact(String url, String oauthToken, String contactId, String firstName, String lastName, String country, String officePhone, String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String body = "{\"id\":\"" + contactId + "\",\"first_name\":\"" + firstName + "\",\"last_name\":\"" + lastName + "\",\"assigned_user_id\":\"" + assignedUID + "\"}";
		HttpUtils restCalls = new HttpUtils();
		String putResponseString = restCalls.putRequest(url + ContactRestAPI.contactsURLExtension + "/" + contactId, headers, body, contentType);
		
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
	
	public String relateNewOpportunityToContact(String url, String oauthToken, String contactId, String description, String source, String salesStage, String date, String clientId, String contactId2, String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String body = "{\"description\":\"" + description + "\",\"lead_source\":\"" + source +  "\",\"date_closed\":\"" + date + "\",\"sales_stage\":\"" + salesStage + "\",\"account_id\":\"" + clientId + "\",\"contact_id_c\":\"" + contactId2 +  "\",\"assigned_user_id\":\"" + assignedUID + "\"}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + ContactRestAPI.contactsURLExtension + "/" + contactId + "/link/opportunities", headers, body, contentType);
	    
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
	
	public String relateNewCallToContact(String url, String oauthToken, String contactId, String callSubject, String callDate, String callStatus, String callType, String callDuration, String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String body = "{\"name\":\"" + callSubject + "\",\"date_start\":\"" + callDate + "\",\"status\":\"" + callStatus + "\",\"call_type\":\"" + callType + "\",\"duration_minutes\":\"" + callDuration + "\", \"duration_hours\": \"0\",\"assigned_user_id\":\"" + assignedUID + "\",\"parent_id\":\"" + contactId + "\",\"parent_type\":\"Accounts\"}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + ContactRestAPI.contactsURLExtension + "/" + contactId + "/link/calls", headers, body, contentType);
		
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

	
	public static String createContactHelperGetName(User user, Logger log, String url) {
		log.info("Creating a contact.");
		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(url, user.getEmail(), user.getPassword());
		
		String firstName = "FirstName" + System.currentTimeMillis();
		String lastName = "LastName" + System.currentTimeMillis();
		String country = "US";
		String officePhone = "(555) 555-5555";
		
		ContactRestAPI contactRestAPI = new ContactRestAPI();
		String jsonString = contactRestAPI.createContact(url, token, firstName, lastName, country, officePhone, new APIUtilities().getUserBeanIDFromEmail(url,user.getEmail(),user.getPassword()));
		
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Contact creation failed, prior to creating an opportunity.");
		} 
		
		String contactName = "";
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			log.debug("Contact Helper response: " + jsonString);
			contactName = (String) postResponse.get("name");
		} catch (Exception e) {
			Assert.assertTrue(false, "Contact was not created as expected.");
		}
		
		return contactName;
	}

}
