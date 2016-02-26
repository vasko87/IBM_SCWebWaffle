/**
 * 
 */
package com.ibm.salesconnect.API;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.common.HttpUtils;
import com.ibm.salesconnect.objects.Lead;

/**
 * @author timlehane
 * @date Jan 26, 2015
 */
public class LeadsRestAPI {
	public static final String leadsURLExtension = "rest/v10/Leads";
	
	
	Logger log = LoggerFactory.getLogger(LeadsRestAPI.class);
	
	/*
	 * Creates a lead via the SalesConnect REST APIs.
	 */
	public String createLeadreturnBean(String url, String oauthToken, String name, String description, String userID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String leadID=null;
		String assignedUserID = userID;//new APIUtilities().getUserBeanIDFromEmail(url, user.getEmail(), user.getPassword());
		
		String body = "{\"name\":\""+name+"\",\"description\":\""+description+"\",\"first_name\":\"one\",\"last_name\":\"smith\"," +
		"\"phone_mobile\":\"123456789\",\"primary_address_country\":\"US\",\"status\":\"LEADPROG\", \"status_detail_c\":\"PROGINIT\"," +
		"\"assigned_user_id\":\""+ assignedUserID +"\",  \"lead_source\":\"LSCAMP\"}";
			
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + LeadsRestAPI.leadsURLExtension, headers, body, contentType);
		
		try {
			//check for a valid JSON response
			JSONObject postResponse = (JSONObject)new JSONParser().parse(postResponseString);
			leadID = (String) postResponse.get("id");
		} catch (ParseException e) {
			e.printStackTrace();
			return "false";
		}
		
		log.info("Valid JSON returned.");
		return leadID;
	}
	
	public String createLeadreturnBean(String url, String oauthToken, String body) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String leadID=null;
			
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + LeadsRestAPI.leadsURLExtension, headers, body, contentType);
		
		try {
			//check for a valid JSON response
			JSONObject postResponse = (JSONObject)new JSONParser().parse(postResponseString);
			leadID = (String) postResponse.get("id");
		} catch (ParseException e) {
			e.printStackTrace();
			return "false";
		}
		
		log.info("Valid JSON returned.");
		return leadID;
	}
	
	public String createLeadreturnBean(String url, String oauthToken, String name, String description, String country, User user) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String leadID=null;
		String assignedUserID = new APIUtilities().getUserBeanIDFromEmail(url, user.getEmail(), user.getPassword());
		
		String body = "{\"name\":\""+name+"\",\"description\":\""+description+"\",\"first_name\":\"one\",\"last_name\":\"smith\"," +
		"\"phone_mobile\":\"123456789\",\"primary_address_country\":\""+country+"\",\"status\":\"LEADPROG\", \"status_detail_c\":\"PROGINIT\"," +
				"\"assigned_user_id\":\""+ assignedUserID +"\", \"lead_source\":\"LSCAMP\"}";
			
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + LeadsRestAPI.leadsURLExtension, headers, body, contentType);
		
		try {
			//check for a valid JSON response
			JSONObject postResponse = (JSONObject)new JSONParser().parse(postResponseString);
			leadID = (String) postResponse.get("id");
		} catch (ParseException e) {
			e.printStackTrace();
			return "false";
		}
		
		log.info("Valid JSON returned.");
		return leadID;
	}
	
	/**
	 * Links an existing lead to an existing object(account/call/etc)
	 * @param url
	 * @param oauthToken
	 * @param leadID
	 * @param type
	 * @param objectID
	 * @return true if successful, false if not
	 */
	public Boolean linkLeadtoObject(String url, String oauthToken, String leadID, String type, String objectID){
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + LeadsRestAPI.leadsURLExtension + "/" + leadID+ "/link/"+type + "/" + objectID, headers, "", contentType);
		
		try {
			//check for a valid JSON response
			new JSONParser().parse(postResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		
		log.info("Valid JSON returned.");
		return true;
	}
	
	/**
	 * Delete the lead specified by leadID
	 * @param url
	 * @param oauthToken
	 * @param leadID
	 */
	public void deleteLead(String url, String oauthToken, String expectedResponseCode){
		String headers[]={"OAuth-Token", oauthToken};
		
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.deleteRequest(url, headers, expectedResponseCode);
		
		try {
			//check for a valid JSON response
			new JSONParser().parse(postResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		log.info("Valid JSON returned.");
	}
	
	public void deleteLead(String url, String oauthToken, String body, String expectedResponseCode){
		String headers[]={"OAuth-Token", oauthToken};
		
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.deleteRequest(url, headers, body, expectedResponseCode);
		
		try {
			//check for a valid JSON response
			new JSONParser().parse(postResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		log.info("Valid JSON returned.");
	}
	
	/**
	 * Get lead via the rest api specifying the expected response
	 * @param url
	 * @param oauthToken
	 * @param expectedResponse
	 * @return response as a string
	 */
	public String getLead(String url, String oauthToken, String expectedResponse){
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
	
	public String getLead(String url, String oauthToken, String leadID, String expectedResponse){
		String headers[]={"OAuth-Token", oauthToken};
		
		HttpUtils restCalls = new HttpUtils();
		 String getResponseString = restCalls.getRequest(url + LeadsRestAPI.leadsURLExtension + "/" + leadID, headers, expectedResponse);
		 
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
	
	public String postLead(String url, String oauthToken,JSONObject json, String expectedResponse){
		String headers[]={"OAuth-Token", oauthToken};
		
		HttpUtils restCalls = new HttpUtils();
		String PostResponseString = restCalls.postRequest(url,  headers, json,  "application/json ", expectedResponse);
		 
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
	
	public String postLead(String url, String oauthToken,String body, String expectedResponse){
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
	
	public String putLead(String url, String oauthToken,String body, String expectedResponse){
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
			Assert.assertTrue(false, "Parse exception with post response");
		}
		
		log.info("Valid JSON returned.");
		return PutResponseString;
	}
	
	public String createLeadPostwithDefaults(Lead lead){
		String temp = "";
		
		if(lead.sTitle.length()>0)
			temp = temp + "{\"name\":\""+lead.sTitle+"\",";
		else
			temp = temp + "{\"name\":\""+"default"+"\",";
		
		if(lead.sOppDesc.length()>0)
			temp = temp + "\"description\":\""+lead.sOppDesc+"\",";
		else
			temp = temp + "\"description\":\""+"default"+"\",";
		
		if(lead.sFirstName.length()>0)
			temp = temp + "\"first_name\":\""+lead.sFirstName+"\",";
		else
			temp = temp + "\"first_name\":\""+"one"+"\",";
		
		if(lead.sLastName.length()>0)
			temp = temp + "\"last_name\":\""+lead.sLastName+"\",";
		else
			temp = temp + "\"last_name\":\""+"smith"+"\",";
		
		if(lead.sMobilePhone.length()>0)
			temp = temp + "\"phone_mobile\":\""+lead.sMobilePhone+"\",";
		else
			temp = temp + "\"phone_mobile\":\""+"123456789"+"\",";
		
		if(lead.sPrimaryAddressCountry.equalsIgnoreCase("United States"))
			temp = temp + "\"primary_address_country\":\""+"US"+"\",";
		else if(lead.sPrimaryAddressCountry.length()>0)
			temp = temp + "\"primary_address_country\":\""+lead.sPrimaryAddressCountry+"\",";
		else
			temp = temp + "\"primary_address_country\":\""+"US"+"\",";
		
		if(lead.sStatus.equals("Converted")){
			temp = temp + "\"status\":\""+"LEADCONV"+"\",";
			temp = temp + "\"status_detail_c\":\""+"CONTACT"+"\",";
		}
		else{
			if(lead.sStatus.contains("New"))
				temp = temp + "\"status\":\""+ "LEADNEW" +"\",";
			else if(lead.sStatus.length()>0)
				temp = temp + "\"status\":\""+lead.sStatus+"\",";
			else
				temp = temp + "\"status\":\""+"LEADPROG"+"\",";
			if(lead.sStatusDetail.equals("Converted"))
				temp = temp + "\"status_detail_c\":\""+"Contact"+"\",";
			else if(lead.sStatusDetail.length()>0)
				temp = temp + "\"status_detail_c\":\""+lead.sStatusDetail+"\",";
			else
				temp = temp + "\"status_detail_c\":\""+"NEWTBA"+"\",";
		}
		
		if(lead.sLeadSource.equals("Campaign"))
			temp = temp + "\"lead_source\":\""+"LSCAMP"+"\",";
		else if(lead.sLeadSource.length()>0)
			temp = temp + "\"lead_source\":\""+lead.sLeadSource+"\",";
		else
			temp = temp + "\"lead_source\":\""+"LSCAMP"+"\",";
		
		if(lead.sAssignedUserID.length()>0)
			temp = temp + "\"assigned_user_id\":\""+lead.sAssignedUserID+"\",";
		
		if(lead.sEmailId.length()>0){
			temp = temp +"\"email\": [{\"email_address\":\"" + lead.sEmailId + "\", \"primary_address\":true}],";
		}
		
		temp = temp.substring(0, temp.length()-1) + "}";
		
		return temp;
	}
	
	public String generateExpectedResponse(Lead lead)
	{
		String temp = "";
		return temp;
	}
	
	public String createLeadfromLead(String url, String oauthToken, Lead lead) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String leadID=null;
		
		String body = createLeadPostwithDefaults(lead);
			
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + LeadsRestAPI.leadsURLExtension, headers, body, contentType);
		
		try {
			//check for a valid JSON response
			JSONObject postResponse = (JSONObject)new JSONParser().parse(postResponseString);
			leadID = (String) postResponse.get("id");
		} catch (ParseException e) {
			e.printStackTrace();
			return "false";
		}
		
		log.info("Valid JSON returned.");
		return leadID;
	}
}
