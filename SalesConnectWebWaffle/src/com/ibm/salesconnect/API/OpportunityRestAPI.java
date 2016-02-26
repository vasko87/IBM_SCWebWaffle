package com.ibm.salesconnect.API;

import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.salesconnect.common.HttpUtils;
import com.ibm.salesconnect.objects.RevenueItem;

/*
 * Simulates the use of the SalesConnect server REST APIs from the mobile client.
 */
public class OpportunityRestAPI {
	
	public static final String opportunitiesURLExtension = "rest/v10/Opportunities";
	
	Logger log = LoggerFactory.getLogger(OpportunityRestAPI.class);

	public String postOpportunity(String url, String oauthToken,JSONObject json, String expectedResponse){
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
	
	public String postOpportunity(String url, String oauthToken,String body, String expectedResponse){
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
	 * Delete Opportunity using DELETE
	 * @param url
	 * @param oauthToken
	 * @param body
	 * @param expectedResponseCode
	 */
	public void deleteOpportunity(String url, String oauthToken, String body, String expectedResponseCode){
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

	/**
	 * Check a specified oppty has been created via the RST API
	 * @param url
	 * @param opptyID
	 * @param oauthToken
	 * @return
	 */
	public String checkOpportunity(String url, String opptyID, String oauthToken){
//		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String body= "{\"id\":\"" + opptyID + "\"}";
		
		HttpUtils restCalls = new HttpUtils();
		
		String getResponseString = restCalls.getRequest(url + OpportunityRestAPI.opportunitiesURLExtension + "/" + opptyID, headers);
		System.out.println(getResponseString);
		try {
			//check for a valid JSON response
			JSONObject postResponse = (JSONObject)new JSONParser().parse(getResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
			return "false";
		}
		
		return getResponseString;
	}
	

	
	/*
	 * Creates an opportunity via the SalesConnect REST APIs.
	 */
	public String createOpportunity(String url, String oauthToken, String desc, String clientId, String contactId, String source, String salesStage, String date, String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String body = "{\"description\":\"" + desc + "\",\"lead_source\":\"" + source + "\",\"sales_stage\":\"" + salesStage + "\",\"date_closed\":\"" + date + "\",\"account_id\":\"" + clientId + "\",\"contact_id_c\":\"" + contactId + "\",\"assigned_user_id\":\"" + assignedUID + "\"}";

		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + OpportunityRestAPI.opportunitiesURLExtension, headers, body, contentType);
		
		try {
			//check for a valid JSON response
			JSONObject postResponse = (JSONObject)new JSONParser().parse(postResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
			return "false";
		}
		
		log.info("Valid JSON returned. " + postResponseString);
		return postResponseString;
	}
	
	/*
	 * Creates an opportunity via the SalesConnect REST APIs and returns Oppty ID
	 */
	public String createOpportunityreturnBean(String url, String oauthToken, String desc, String clientId, String contactId, String source, String salesStage, String date, String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};

		String opptyID = "";
		
		String body = "{\"description\":\"" + desc + "\",\"lead_source\":\"" + source + "\",\"sales_stage\":\"" + salesStage + "\",\"date_closed\":\"" + date + "\",\"account_id\":\"" + clientId + "\",\"contact_id_c\":\"" + contactId + "\",\"assigned_user_id\":\"" + assignedUID + "\"}";

		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + OpportunityRestAPI.opportunitiesURLExtension, headers, body, contentType);
		
		try {
			//check for a valid JSON response
			JSONObject postResponse = (JSONObject)new JSONParser().parse(postResponseString);
			opptyID = (String) postResponse.get("id");
		} catch (ParseException e) {
			e.printStackTrace();
			return "false";
		}
		
		log.info("Valid JSON returned. " + postResponseString);
		return opptyID;
	}

public String createOpportunityWithRLIreturnBean(String url, String oauthToken, String desc, String clientId, String contactId, String source, String salesStage, String date, String assignedUID, String assignedUserName) {
	String contentType = "application/json";
	String headers[]={"OAuth-Token", oauthToken};

	String opptyID = "";
	String body = "{\"description\":\"" + desc + "\",\"lead_source\":\"" + source + "\",\"sales_stage\":\"" + salesStage + "\",\"date_closed\":\"" + date + "\",\"account_id\":\"" + clientId + "\",\"contact_id_c\":\"" + contactId + "\",\"assigned_user_id\":\"" + assignedUID + "\"" +
			",\"opportun_revenuelineitems\":{\"create\":[{\"doc_owner\": \"\",\"user_favorites\": \"\",\"deleted\": false,\"search_type\": \"product\",\"probability\": \"10\",\"green_blue_revenue\": \"Green\",\"revenue_type\": \"Transactional\",\"duration\": 1,\"assigned_bp_id\": \"\",\"alliance_partners\": \"\",\"igf_odds\": \"0\",\"igf_term\": \"1\",\"quantity\": 1,\"assigned_user_id\": \"" + assignedUID + "\",\"assigned_user_name\": \"" + assignedUserName + "\",\"currency_id\": \"-99\", \"fcast_date_tran\": \"2016-02-03\",\"fcast_date_sign\": \"2016-02-03\",\"srv_inqtr_total\": \"0.000000\",\"swg_annual_value\": \"\",\"monthly_recurring_revenue\": \"\",\"decline_in_acv\": \"\",\"level15_name\": \"Lenovo Servers\",\"level17_name\": \"Lenovo Servers\",\"level20_name\": \"Lenovo Servers\",\"level30_name\": \"\",\"level40_name\": \"\",\"offering_type\": \"HW\",\"swg_sign_det\": \"\",\"contract_extn\": \"\",\"contract_extn_status\": \"\",\"expiration_date\": \"\",\"renew_date\": \"\",\"provision_date\": \"\",\"contract_extension_value\": \"\",\"acv_amount\": \"\",\"contract_extn_det\": \"\",\"base_rate\": \"45.000045\",\"stg_signings_type\": \"\",\"level20\": \"B3W00\",\"stg_fulfill_type\": \"DIRECT\",\"level_search\": \"Lenovo Servers\",\"level17\": \"17LSE\",\"level10\": \"B3000\",\"level15\": \"LSE\",\"level30\": \"\",\"level40\": \"\",\"level10_name\": \"Lenovo\",\"revenue_amount\": \"888888\"}]}" +
		",  \"additional_users\": [{\"employee_job_role\": \"\", \"first_name\": \""+assignedUserName+"\",\"id\": \""+assignedUID + "\", \"last_name\": \"\",\"module\": \"User\", \"role\": \"IGF\",\"withheld_link\": false},"+ 
		"{\"employee_job_role\": \"\", \"first_name\": \""+assignedUserName+"\",\"id\": \""+assignedUID + "\", \"last_name\": \"\",\"module\": \"User\", \"role\": \"SFANOT\",\"withheld_link\": false},"+
				"{\"employee_job_role\": \"\", \"first_name\": \""+assignedUserName+"\",\"id\": \""+assignedUID + "\", \"last_name\": \"\",\"module\": \"User\", \"role\": \"SFAIDN\",\"withheld_link\": false}]}";



	HttpUtils restCalls = new HttpUtils();
	String postResponseString = restCalls.postRequest(url + OpportunityRestAPI.opportunitiesURLExtension, headers, body, contentType);
	
	try {
		//check for a valid JSON response
		JSONObject postResponse = (JSONObject)new JSONParser().parse(postResponseString);
		opptyID = (String) postResponse.get("id");
	} catch (ParseException e) {
		e.printStackTrace();
		return "false";
	}
	
	log.info("Valid JSON returned. " + postResponseString);
	return opptyID;
}

	/**
	 * Create and opportunity with a related rli and return the opportunity id
	 * @param url
	 * @param oauthToken
	 * @param desc
	 * @param clientId
	 * @param contactId
	 * @param source
	 * @param salesStage
	 * @param date
	 * @param assignedUID
	 * @param assignedUserName
	 * @return opportunity ID
	 */
	public String createOpportunityWithRLIreturnBean(String url, String oauthToken, String desc, String clientId,
			String contactId, String source, String salesStage, String date, String assignedUID,
			String assignedUserName, RevenueItem rli) {
		String contentType = "application/json";
		String headers[] = { "OAuth-Token", oauthToken };
		
		String opptyID = "";
		String body = "{\"description\":\"" + desc + "\",\"lead_source\":\"" + source + "\",\"sales_stage\":\""
				+ 3 + "\",\"date_closed\":\"" + date + "\",\"account_id\":\"" + clientId
				+ "\",\"contact_id_c\":\"" + contactId + "\",\"assigned_user_id\":\"" + assignedUID + "\""
				+ ",\"opportun_revenuelineitems\":{\"create\":[{\"doc_owner\":\"\",\"user_favorites\":\"\",\"deleted\":false,\"search_type\":\"product\",\"probability\":\""+ rli.sProbability +"\",\"green_blue_revenue\":\""+ rli.sGreenBlueRevenue + "\",\"revenue_type\":\"Transactional\",\"duration\":\"12\",\"assigned_bp_id\":\"\",\"alliance_partners\":\"\",\"igf_odds\":\"0\",\"igf_term\":\"1\",\"assigned_user_id\":\""
				+ assignedUID + "\",\"assigned_user_name\":\"" + assignedUserName
				+ "\",\"srv_inqtr_total\":\"0.000000\",\"swg_annual_value\":\"\",\"fcast_date_tran\":\"2016-10-05\",\"fcast_date_sign\":\"2016-10-05\",\"currency_id\":\"-99\",\"base_rate\":1,\"offering_type\":\"SAAS\",\"level10\":\""+ rli.sL10_OfferingType + "\",\"level15\":\""+ rli.sL15_SubBrand + "\",\"level17\":\""+ rli.sL17_SegmentLine + "\",\"level20\":\""+ rli.sL20_BrandCode + "\",\"level30\":\""+ rli.sL30_ProductInformation + "\",\"level40\":\""+ rli.sL40_MachineType + "\",\"swg_contract\":\"NEW\",\"swg_book_new\":\"TRANS\",\"swg_tran_det\":\"ONE\",\"srv_work_type\":\"NEWNEW\","/* \"undefined\":\"BQT00\", */
				+ "\"level_search\":\"Tivoli Automation EM (Open) - SaaS\",\"revenue_amount\":\"" + rli.sRevenueAmount + "\"}]}"
				+ ",  \"additional_users\": [{\"employee_job_role\": \"\", \"first_name\": \"" + assignedUserName
				+ "\",\"id\": \"" + assignedUID
				+ "\", \"last_name\": \"\",\"module\": \"User\", \"role\": \"IGF\",\"withheld_link\": false},"
				+ "{\"employee_job_role\": \"\", \"first_name\": \"" + assignedUserName + "\",\"id\": \"" + assignedUID
				+ "\", \"last_name\": \"\",\"module\": \"User\", \"role\": \"SFANOT\",\"withheld_link\": false},"
				+ "{\"employee_job_role\": \"\", \"first_name\": \"" + assignedUserName + "\",\"id\": \"" + assignedUID
				+ "\", \"last_name\": \"\",\"module\": \"User\", \"role\": \"SFAIDN\",\"withheld_link\": false}]}";

		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + OpportunityRestAPI.opportunitiesURLExtension, headers,
				body, contentType);

		try {
			// check for a valid JSON response
			JSONObject postResponse = (JSONObject) new JSONParser().parse(postResponseString);
			opptyID = (String) postResponse.get("id");
		} catch (ParseException e) {
			e.printStackTrace();
			return "false";
		}

		log.info("Valid JSON returned. " + postResponseString);
		return opptyID;
	}
	
	/*
	 * Creates an opportunity via the SalesConnect REST APIs specifying an ID.
	 */
	public String createOpportunitySpecifyID(String url, String opptyID, String oauthToken, String desc, String clientId, String contactId, String source, String salesStage, String date, String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String body = "{\"id\":\"" + opptyID + "\",\"description\":\"" + desc + "\",\"lead_source\":\"" + source + "\",\"sales_stage\":\"" + salesStage + "\",\"date_closed\":\"" + date + "\",\"account_id\":\"" + clientId + "\",\"contact_id_c\":\"" + contactId + "\",\"assigned_user_id\":\"" + assignedUID + "\"}";

		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + OpportunityRestAPI.opportunitiesURLExtension, headers, body, contentType);
		
		try {
			//check for a valid JSON response
			JSONObject postResponse = (JSONObject)new JSONParser().parse(postResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
			return "false";
		}
		
		log.info("Valid JSON returned. " + postResponseString);
		return postResponseString;
	}
	
	/*
	 * Creates an opportunity via the SalesConnect REST APIs specifying an ID.
	 */
	public String createRestrictedOpportunitySpecifyID(String url, String oauthToken, String desc, String clientId, String contactId, String source, String salesStage, String date, String assignedUID, String restricted) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		String opptyID = null;
		String body = "{\"description\":\"" + desc + "\",\"lead_source\":\"" + source + "\",\"sales_stage\":\"" + salesStage + "\",\"date_closed\":\"" + date + "\",\"account_id\":\"" + clientId 
		+ "\",\"contact_id_c\":\"" + contactId + "\",\"assigned_user_id\":\"" + assignedUID + "\",\"restricted\":\"" + restricted+ "\"}";

		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + OpportunityRestAPI.opportunitiesURLExtension, headers, body, contentType);
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(postResponseString);
			opptyID = (String) postResponse.get("id");
		} catch (Exception e) {
			Assert.assertTrue(false, "Opportunity was not created as expectd.");
		}
		
		log.info("Valid JSON returned. " + postResponseString);
		return opptyID;
	}
	
	/*
	 * Creates an opportunity via the SalesConnect REST APIs specifying an ID.
	 */
	@Deprecated
	public String createOpptyRestOpportunitySpecifyID(String url, String oauthToken, String desc, String clientId, String contactId, String source, String salesStage, String date, String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		String opptyID = null;
		
		String body = "{\"description\":\"" + desc + "\",\"restricted\":\"RESTOPTY\",\"lead_source\":\"" + source + "\",\"sales_stage\":\"" + salesStage + "\",\"date_closed\":\"" + date + "\",\"account_id\":\"" + clientId + "\",\"contact_id_c\":\"" + contactId + "\",\"assigned_user_id\":\"" + assignedUID + "\"}";

		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + OpportunityRestAPI.opportunitiesURLExtension, headers, body, contentType);
		
		try {
			//check for a valid JSON response
			JSONObject postResponse = (JSONObject)new JSONParser().parse(postResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
			return "false";
		}
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(postResponseString);
			opptyID = (String) postResponse.get("id");
		} catch (Exception e) {
			Assert.assertTrue(false, "Opportunity was not created as expectd.");
		}
		
		
		log.info("Valid JSON returned. " + postResponseString);
		return opptyID;
	}
	/*
	 * Creates a new call and relates it to the provided Opportunity using the SalesConnect REST APIs.
	 */
	public String relateNewCallToOpportunity(String url, String oauthToken, String opportunityId, String callSubject, String callDate, String callStatus, String callType, String callDuration, String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String body = "{\"name\":\"" + callSubject + "\",\"date_start\":\"" + callDate + "\",\"status\":\"" + callStatus + "\",\"call_type\":\"" + callType + "\",\"duration_minutes\":\"" + callDuration + "\",\"duration_hours\":\"0\",\"assigned_user_id\":\"" + assignedUID + "\",\"parent_id\":\"" + opportunityId + "\",\"parent_type\":\"Opportunities\"}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + OpportunityRestAPI.opportunitiesURLExtension + "/" + opportunityId + "/link/calls", headers, body, contentType);
		
		try {
			//check for a valid JSON response
			JSONObject postResponse = (JSONObject)new JSONParser().parse(postResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
			return "false";
		}
		
		log.info("Valid JSON returned." + postResponseString);
		return postResponseString;
		
	}
	
	
	/*
	 * Creates a new task and relates it to the provided Opportunity using the SalesConnect REST APIs.
	 */
	public String relateNewTaskToOpportunity(String url, String oauthToken, String opportunityId, String taskName, String taskDate, String taskPriority, String taskStatus, String taskType, String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String body = "{\"name\":\"" + taskName + "\",\"date_due\":\"" + taskDate + "\",\"priority\":\"" + taskPriority + "\",\"status\":\"" + taskStatus + "\",\"call_type\":\"" + taskType + "\",\"assigned_user_id\":\"" + assignedUID + "\",\"parent_id\":\"" + opportunityId + "\",\"parent_type\":\"Opportunities\"}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + OpportunityRestAPI.opportunitiesURLExtension + "/" + opportunityId + "/link/tasks", headers, body, contentType);
		
		try {
			//check for a valid JSON response
			JSONObject postResponse = (JSONObject)new JSONParser().parse(postResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
			return "false";
		}
		
		log.info("Valid JSON returned." + postResponseString);
		return postResponseString;
		
	}
	
	
	/*
	 * Creates a new line item and relates it to the provided Opportunity using the SalesConnect REST APIs.
	 */
	public String relateNewLineItemToOpportunity(String url, String oauthToken, String opportunityId, String liAmount, String liProbability, String liContractType, String liDate, String liLevel10, String liLevel15, String liLevel17, String liLevel20, String liLevel30, String liLevel40, String liCurrency, String assignedName, String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String body = "{\"revenue_amount\":\"" + liAmount + "\",\"probability\":\"" + liProbability + "\",\"srv_work_type\":\"" + liContractType +  "\",\"fcast_date_sign\":\"" + liDate + "\",\"level10\":\"" + liLevel10 + "\",\"level15\":\"" + liLevel15 + "\",\"level17\":\"" + liLevel17 + "\",\"level20\":\"" + liLevel20 + "\",\"level30\":\"" + liLevel30 + "\",\"level40\":\"" + liLevel40 + "\",\"currency_id\":\"" + liCurrency + "\",\"assigned_user_name\":\"" + assignedName + "\",\"assigned_user_id\":\"" + assignedUID + "\"}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + OpportunityRestAPI.opportunitiesURLExtension + "/" + opportunityId + "/link/opportun_revenuelineitems", headers, body, contentType);
		
		try {
			//check for a valid JSON response
			JSONObject postResponse = (JSONObject)new JSONParser().parse(postResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
			return "false";
		}
		
		log.info("Valid JSON returned." + postResponseString);
		return postResponseString;
		
	}
	
	
	/*
	 * Creates a new note and relates it to the provided Opportunity using the SalesConnect REST APIs.
	 */
	public String relateNewNoteToOpportunity(String url, String oauthToken, String opportunityId, String noteName, String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String body = "{\"name\":\"" + noteName + "\",\"assigned_user_id\":\"" + assignedUID + "\",\"parent_id\":\"" + opportunityId + "\",\"parent_type\":\"Opportunities\"}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + OpportunityRestAPI.opportunitiesURLExtension + "/" + opportunityId + "/link/notes", headers, body, contentType);
		
		try {
			//check for a valid JSON response
			JSONObject postResponse = (JSONObject)new JSONParser().parse(postResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
			return "false";
		}
		
		log.info("Valid JSON returned." + postResponseString);
		return postResponseString;
		
	}
	
	
	/*
	 * Creates a new contact and relates it to the provided Opportunity using the SalesConnect REST APIs.
	 */
	public String relateNewContactToOpportunity(String url, String oauthToken, String opportunityId, String contactFirstName, String contactLastName, String contactCountry, String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String body = "{\"first_name\":\"" + contactFirstName + "\",\"last_name\":\"" + contactLastName + "\",\"primary_address_country\":\"" + contactCountry + "\",\"assigned_user_id\":\"" + assignedUID + "\"}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + OpportunityRestAPI.opportunitiesURLExtension + "/" + opportunityId + "/link/contacts", headers, body, contentType);
		
		try {
			//check for a valid JSON response
			JSONObject postResponse = (JSONObject)new JSONParser().parse(postResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
			return "false";
		}
		
		log.info("Valid JSON returned." + postResponseString);
		return postResponseString;
		
	}
	
	
	/*
	 * Links an existing record to the provided Opportunity using the SalesConnect REST APIs.
	 */
	public String linkRecordToOpportunity(String url, String oauthToken, String opportunityId, String recordId, String moduleName) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String linkId = getLinkId(moduleName);
		
		String body = "{\"id\":\"" + recordId + "\"}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + OpportunityRestAPI.opportunitiesURLExtension + "/" + opportunityId + "/link/" + linkId + "/" + recordId, headers, body, contentType);
		
		try {
			//check for a valid JSON response
			JSONObject postResponse = (JSONObject)new JSONParser().parse(postResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
			return "false";
		}
		
		log.info("Valid JSON returned." + postResponseString);
		return postResponseString;
	}
	
	/*
	 * Links an existing record to the provided Opportunity using the SalesConnect REST APIs, allowing extra values in the Request Body.
	 * @return true if successful, false if not
	 */
	public Boolean linkRecordToOpportunityReturnBoolean(String url, String oauthToken, String opportunityId, String recordId, String moduleName, String requestBody) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String linkId = getLinkId(moduleName);
		
		String body = "{\"id\":\"" + recordId + requestBody + "\"}";
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + OpportunityRestAPI.opportunitiesURLExtension + "/" + opportunityId + "/link/" + linkId + "/" + recordId, headers, body, contentType);
			
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
	 * Links an existing record to the provided Opportunity using the SalesConnect REST APIs.
	 * @return true if successful, false if not
	 */
	public Boolean linkRecordToOpportunityReturnBoolean(String url, String oauthToken, String opportunityId, String recordId, String moduleName) {
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
		String postResponseString = restCalls.postRequest(url + OpportunityRestAPI.opportunitiesURLExtension + "/" + opportunityId + "/link/" + linkId + "/" + recordId, headers, body, contentType);
			
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
	 * Edits an existing Opportunity using the SalesConnect REST APIs.
	 */
	public String editOpportunity(String url, String oauthToken, String opportunityId, String desc, String clientId, String contactId, String source, String salesStage, String date, String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};
		
		String body = "{\"id\":\"" + opportunityId + "\",\"description\":\"" + desc + "\",\"lead_source\":\"" + source + "\",\"sales_stage\":\"" + salesStage + "\",\"date_closed\":\"" + date + "\",\"account_id\":\"" + clientId + "\",\"contact_id_c\":\"" + contactId + "\",\"assigned_user_id\":\"" + assignedUID + "\"}";
		HttpUtils restCalls = new HttpUtils();
		String putResponseString = restCalls.putRequest(url + OpportunityRestAPI.opportunitiesURLExtension + "/" + opportunityId, headers, body, contentType);
		
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
	
	public String favoriteOpportunity(String url, String oauthToken, String opportunityId, String favorite, String assignedUID) {
		String contentType = "application/json";
		String headers[]={"OAuth-Token", oauthToken};

		String body = "";
		
		HttpUtils restCalls = new HttpUtils();
		String putResponseString = restCalls.putRequest(url + OpportunityRestAPI.opportunitiesURLExtension + "/" + opportunityId + "/" + favorite, headers, body, contentType);
		
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
	 * Deletes an oppty via the rest api, user used to get Oauth token must be sys admin
	 * Will fail test if request is unsuccessful
	 * @param url Base SalesConnect URL
	 * @param oauthToken Oauth token for sys admin
	 * @param opptyID Oppty to be deleted
	 * @return response as a string
	 */
	public String deleteOpportunity(String url, String oauthToken, String opptyID){
		return deleteOpportunity(url, oauthToken, opptyID, true);
	}
	
	/**
	 * Deletes an oppty via the rest api, user used to get Oauth token must be sys admin
	 * @param url Base SalesConnect URL
	 * @param oauthToken Oauth token for sys admin
	 * @param opptyID Oppty to be deleted
	 * @param stopIfFail true if unsuccessful request should cause test failure, false if the test should continue
	 * @return response as a string
	 */
	public String deleteOpportunity(String url, String oauthToken, String opptyID, Boolean stopIfFail) {
		String headers[]={"OAuth-Token", oauthToken};
		
		HttpUtils restCalls = new HttpUtils();
		String deleteResponseString = restCalls.deleteRequest(url + OpportunityRestAPI.opportunitiesURLExtension + "/" + opptyID, headers);
		JSONObject deleteResponse;
		try {
			//check for a valid JSON response
			deleteResponse = (JSONObject)new JSONParser().parse(deleteResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
			return "false";
		}

		if (stopIfFail) {
			Assert.assertTrue((deleteResponse.get("id")).equals(opptyID), "returned id does not mathc sent id and set to stop if failure");
		}
	
		log.info("Valid JSON returned. " + deleteResponseString);
		return deleteResponseString;
	}
	
	/**
	 * Get opportunity via the rest api specifying the expected response
	 * @param url
	 * @param oauthToken
	 * @param expectedResponse
	 * @return response as a string
	 */
	public String getOpportunity(String url, String oauthToken, String expectedResponse){
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
		
		log.info("Valid JSON returned." + getResponseString);
		return getResponseString;
	}
	
	/**
	 * Get opportunity via the rest api specifying the expected response
	 * @param url
	 * @param oauthToken
	 * @param opptyID
	 * @param expectedResponse
	 * @return response as a string
	 */
	public String getOpportunity(String url, String oauthToken, String opptyID, String expectedResponse){
		String headers[]={"OAuth-Token", oauthToken};
		
		HttpUtils restCalls = new HttpUtils();
		 String getResponseString = restCalls.getRequest(url + OpportunityRestAPI.opportunitiesURLExtension + "/" + opptyID, headers, expectedResponse);
		 
		try {
			//check for a valid JSON response
			new JSONParser().parse(getResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
			Assert.assertTrue(false, "Parse exception with post response");
		}
		
		log.info("Valid JSON returned." + getResponseString);
		return getResponseString;
	}

	/**
	 * Returns the id of the first rli linked to the specified oppty
	 * @param url
	 * @param oauthToken
	 * @param opptyID
	 * @return rli ID
	 */
	public JSONObject getRLIFromOppty(String url, String oauthToken, String opptyID){
		String headers[]={"OAuth-Token", oauthToken};
		
		HttpUtils restCalls = new HttpUtils();
		String getResponseString = restCalls.getRequest(url + OpportunityRestAPI.opportunitiesURLExtension + "/" + opptyID +"/link/opportun_revenuelineitems", headers, "200");
		
		JSONObject response= null;
		//check for a valid JSON response
		try {
			response = (JSONObject)new JSONParser().parse(getResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
			Assert.assertTrue(false, "Parse exception with post response");
		}
	
		JSONObject itemRecord = null;
		
		Iterator<Map.Entry<String, Object>> it1 = response.entrySet().iterator();
		while (it1.hasNext()) 
        {
			Map.Entry<String, Object> pairs = it1.next();
			if(pairs.getKey().contains("records")) {
				JSONArray jsonArray = (JSONArray) pairs.getValue();
					itemRecord = (JSONObject) jsonArray.get(0);
			}
        }	
		return itemRecord;
	}
	
	/**
	 * Returns the id of the winplan linked to the specified oppty
	 * @param url
	 * @param oauthToken
	 * @param opptyID
	 * @return winplan ID
	 */
	public JSONObject getWinplanFromOppty(String url, String oauthToken, String opptyID){
		String headers[]={"OAuth-Token", oauthToken};
		
		HttpUtils restCalls = new HttpUtils();
		String getResponseString = restCalls.getRequest(url + OpportunityRestAPI.opportunitiesURLExtension + "/" + opptyID +"/link/opportun_winplans", headers, "200");
		
		JSONObject response= null;
		//check for a valid JSON response
		try {
			response = (JSONObject)new JSONParser().parse(getResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
			Assert.assertTrue(false, "Parse exception with post response");
		}
	
		JSONObject relatedRecord = null;
		
		Iterator<Map.Entry<String, Object>> it1 = response.entrySet().iterator();
		while (it1.hasNext()) 
        {
			Map.Entry<String, Object> pairs = it1.next();
			if(pairs.getKey().contains("related_record")) {
				JSONArray jsonArray = (JSONArray) pairs.getValue();
					relatedRecord = (JSONObject) jsonArray.get(0);
			}
        }
		
		return relatedRecord;
	}

	/**
	 * Put opportunity via the rest api specifying the expected response
	 * @param url
	 * @param oauthToken
	 * @param body of request
	 * @param expectedResponse
	 * @return response as a string
	 */
	public String putOpportunity(String url, String oauthToken,String body, String expectedResponse){
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
		
		log.info("Valid JSON returned." + PutResponseString);
		return PutResponseString;
	}

	/*
	 *  PRIVATE HELPERS
	 * -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	 */
	
	/*
	 * Returns the Opportunity link id for the given module.
	 */
	private String getLinkId(String moduleName) 
	{
		
		String linkId =  "";
		
		if (moduleName == "Calls") 
		{
			linkId = "calls";
		} 
		else if (moduleName == "Tasks") 
		{
			linkId = "tasks";
		} 
		else if (moduleName == "ibm_RevenueLineItems") 
		{
			linkId = "opportun_revenuelineitems";
		} 
		else if (moduleName == "Notes") 
		{
			linkId = "notes";
		} 
		else if (moduleName == "Contacts")
		{
			linkId = "contacts";
		} 
		else if (moduleName == "Users") 
		{
			linkId = "rel_additional_users";
		}
		else if (moduleName == "Meetings")
		{
			linkId = "meetings";  
		}
		else if (moduleName == "Accounts")
		{
			linkId = "accounts";  
		}
		else if (moduleName == "Opportunities")
		{
			linkId = "rel_opportunities";  
		}
		
	
		return linkId;	
	}
}
