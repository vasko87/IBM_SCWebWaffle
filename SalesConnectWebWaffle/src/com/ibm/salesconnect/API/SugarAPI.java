/**
 * 
 */
package com.ibm.salesconnect.API;

import java.util.Random;

import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPMessage;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.common.HttpUtils;
import com.ibm.salesconnect.objects.Client;

/**
 * @author timlehane
 * @date Jul 31, 2013
 */
public class SugarAPI {
	Logger log = LoggerFactory.getLogger(SugarAPI.class);
	
	/**
	 * Creates an oppty using the sugar api
	 * @param URL base URL for the system under test
	 * @param opptyID
	 * @param RLIID
	 * @param contactExtRefID
	 * @param clientID
	 * @param userID
	 * @param opptyTeamMembers
	 */
	public String createOppty(String URL, String RLIID, String contactExtRefID, String clientID, String userID, String password, String[] opptyTeamMembers ){

		//create oppty
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(URL, userID, password);

		String realClientId = new APIUtilities().getClientBeanIDFromCCMSID(URL, clientID, userID, password);
		String headers[]={"OAuth-Token", token};
		String userBeanID =  new APIUtilities().getUserBeanIDFromEmail(URL, userID, password);	
		OpportunityRestAPI opportunityRestAPI = new OpportunityRestAPI();
		String jsonString2 = opportunityRestAPI.createOpportunity(URL, token, "Oppty created description", realClientId, contactExtRefID, "SLSP", "03", "2016-10-28", userBeanID); 
		
		String opptyID = "";
		
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString2);
			opptyID = (String) postResponse.get("id");
		} catch (Exception e) {
			Assert.assertTrue(false, "Opportunity was not created as expectd.");
		}
		
		if (jsonString2.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Opportunity creation failed.");
		} 
		
		//If RLI exists, related it to created Opportunity
		if( RLIID != "") {
			String jsonString = opportunityRestAPI.linkRecordToOpportunity(URL, token, opptyID, RLIID, "ibm_RevenueLineItems");
		
			if (jsonString.equalsIgnoreCase("false")) {
				Assert.assertTrue(false, "Relating line item to opportunity failed.");
			} 
		}
		
		for (int i = 0; i < opptyTeamMembers.length; i++) {
			String jsonString3 = opportunityRestAPI.linkRecordToOpportunity(URL, token, opptyID, getUserID(URL, opptyTeamMembers[i], password), "Users");
			
			if (jsonString3.equalsIgnoreCase("false")) {
				Assert.assertTrue(false, "Relating User to opportunity failed.");
			} 
		}		
		
		//If Opportunity was not successfully created with REST, use SOAP
		SugarAPI sugarAPI = new SugarAPI();
		if (sugarAPI.checkOpptySOAP(URL, opptyID, userID, password).equals("100")) {
			log.info("Opportunity was created successfully via REST API");
			}
		else {
			log.info("Opportunity was not created successfully via rest api, attempting via SOAP api");
			createOpptySOAP(URL, opptyID, RLIID, contactExtRefID, realClientId, userID, password, opptyTeamMembers);
			
			if (sugarAPI.checkOpptySOAP(URL, opptyID, userID, password).equals("100")) {
				log.info("Opportunity was created successfully via SOAP API");
			}
			else {
				log.info("Opportunity was not created successfully via SOAP API");
				Assert.assertTrue(false);
			}	
		}
		
		return opptyID;
	}
	
	/**
	 * Creates an oppty using the sugar api
	 * @param URL base URL for the system under test
	 * @param opptyID
	 * @param RLIID
	 * @param contactExtRefID
	 * @param clientID
	 * @param userID
	 * @param opptyTeamMembers
	 */
	public String createRestOppty(String URL, String RLIID, String contactExtRefID, String clientID, String userID, String password, String[] opptyTeamMembers ){

		//create oppty
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(URL, userID, password);
		
		String realClientId = new APIUtilities().getClientBeanIDFromCCMSID(URL, clientID, userID, password);

		OpportunityRestAPI opportunityRestAPI = new OpportunityRestAPI();
		String jsonResponse = opportunityRestAPI.createOpportunity(URL, token, "Oppty created description", realClientId, contactExtRefID, "SLSP", "03", "2013-10-28", getUserID(URL, userID, password));
		String opptyID = null;
		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonResponse);
			opptyID = (String) postResponse.get("id");
		} catch (Exception e) {
			Assert.assertTrue(false, "Opportunity was not created as expectd.");
		}
		//If RLI exists, related it to created Opportunity
		if( RLIID != "") {
			String jsonString = opportunityRestAPI.linkRecordToOpportunity(URL, token, opptyID, RLIID, "ibm_RevenueLineItems");
		
			if (jsonString.equalsIgnoreCase("false")) {
				Assert.assertTrue(false, "Relating line item to opportunity failed.");
			} 
		}
		
		for (int i = 0; i < opptyTeamMembers.length; i++) {
			String jsonString3 = opportunityRestAPI.linkRecordToOpportunity(URL, token, opptyID, getUserID(URL, opptyTeamMembers[i], password), "Users");
			
			if (jsonString3.equalsIgnoreCase("false")) {
				Assert.assertTrue(false, "Relating User to opportunity failed.");
			} 
		}		
		
		//If Opportunity was not successfully created with REST, use SOAP
		SugarAPI sugarAPI = new SugarAPI();
		if (sugarAPI.checkOpptySOAP(URL, opptyID, userID, password).equals("100")) {
			log.info("Opportunity was created successfully via REST API");
			}
		else {
			log.info("Opportunity was not created successfully via rest api, attempting via SOAP api");
			createOpptySOAP(URL, opptyID, RLIID, contactExtRefID, realClientId, userID, password, opptyTeamMembers);
			
			if (sugarAPI.checkOpptySOAP(URL, opptyID, userID, password).equals("100")) {
				log.info("Opportunity was created successfully via SOAP API");
			}
			else {
				log.info("Opportunity was not created successfully via SOAP API");
				Assert.assertTrue(false);
			}	
		}
		
		return opptyID;
	}
	/**
	 * Creates an oppty using the sugar api
	 * @param URL base URL for the system under test
	 * @param opptyID
	 * @param RLIID
	 * @param contactExtRefID
	 * @param clientID
	 * @param userID
	 * @param opptyTeamMembers
	 */
	public void createOpptySOAP(String URL, String opptyID, String RLIID, String contactExtRefID, String clientID, String userID, String password, String[] opptyTeamMembers ){
		HttpUtils httpCalls = new HttpUtils();
		OpportunityAPITemplates oppty = new OpportunityAPITemplates();
		
		log.info("Logging in to get session ID");
		String session = getSessionID(URL, userID, password);
		String test;

		if( RLIID != "") {
			log.info("Creating opportunity with RLI");
			test = oppty.createOpptyWithRLI(opptyID, RLIID, contactExtRefID, clientID, userID, opptyTeamMembers, (String) session);
		}
		else {
			log.info("Creating opportunity without RLI");
			test = oppty.createOppty(opptyID, contactExtRefID, clientID, userID, opptyTeamMembers, (String) session);
		}
		
		log.info("Request: " + test);
		
		String response = httpCalls.postRequest(URL+GC.soapURLExtension, GC.sugarAPIHeaders, test,"text/xml;charset=utf-8");
		log.info("Response: " + response);
		
		if (checkOpptySOAP(URL, opptyID, userID, password).equals("100")) {
			log.info("Opportunity was created successfully via SOAP API");
		}
		else {
			log.info("Opportunity was not created successfully via SOAP API");
			Assert.assertTrue(false);
		}
	}
	
	/**
	 * Checks that a specified oppty has been created via the SOAP API
	 * @param URL
	 * @param opptyID
	 * @param userID
	 * @param password
	 * @return
	 */
	public String checkOpptySOAP(String URL, String opptyID,String userID, String password){
		HttpUtils httpCalls = new HttpUtils();
		OpportunityAPITemplates oppty = new OpportunityAPITemplates();
		log.info("Checking that opportunity was created");
		
		log.info("Logging in to get session ID");
		String session = getSessionID(URL, userID, password);

		log.info("Checking opportunity");
		String test = oppty.checkOppty(opptyID, session);
		log.info("Request: " + test);
		
		String response = httpCalls.postRequest(URL+GC.soapURLExtension, GC.sugarAPIHeaders, test,"text/xml;charset=utf-8");
		log.info("Response: " + response);
		
		return parseCheckOppty(response);
	}
	
	public String checkOpptySOAPInProgress(String URL, String opptyID,String userID, String password){
		log.info("Checking that opportunity was created");
		APIUtilities apiUtilities = new APIUtilities();
		
		log.info("Logging in to get session ID");
		String session = getSessionID(URL, userID, password);
		
		log.info("Checking opportunity");
		try{
        // Create SOAP Connection
			log.info("Creating factory");
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        log.info("Creating connection");
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();
        log.info("Sending message");
        // Send SOAP Message to SOAP Server
        SOAPMessage soapResponse = soapConnection.call(apiUtilities.createSOAPSelectRequest(session, opptyID, "id", "Opportunities"), URL+GC.soapURLExtension);

        // print SOAP Response
        System.out.print("Response SOAP Message:");
        soapResponse.writeTo(System.out);

        soapConnection.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false,"Check of Oppty failed");
		}
        return "100";
	}
	
	/**
	 * Deletes a specified oppty via the SOAP API
	 * @param URL
	 * @param opptyID
	 * @param userID
	 * @param password
	 * @return error response code
	 */
	public String deleteOpptySOAP(String URL, String opptyID,String userID, String password){
		HttpUtils httpCalls = new HttpUtils();
		OpportunityAPITemplates oppty = new OpportunityAPITemplates();
		log.info("Deleting Oppty");
		
		log.info("Logging in to get session ID");
		String session = getSessionID(URL, userID, password);

		log.info("Deleting opportunity");
		String test = oppty.deleteOppty(opptyID, session);
		log.info("Request: " + test);
		
		String response = httpCalls.postRequest(URL+GC.soapURLExtension, GC.sugarAPIHeaders, test,"text/xml;charset=utf-8");
		log.info("Response: " + response);
		
		return parseCheckOppty(response);
	}
	
	/**
	 * Creates a RLI using the sugar API
	 * @param URL
	 * @param RLIID
	 * @param userID
	 * @param password
	 */
	public void createRLISOAP(String URL, String RLIID, String userID, String password){
		HttpUtils httpCalls = new HttpUtils();
		RLIAPITemplates rli = new RLIAPITemplates();
		
		log.info("Logging in to get session ID");
		String session = getSessionID(URL, userID, password);

		log.info("Creating RLI");
		String rliString = rli.createRLI(RLIID, userID, session);
		log.info("Request: " + rliString);
		
		String rliResponse = httpCalls.postRequest(URL+GC.soapURLExtension, GC.sugarAPIHeaders, rliString,"text/xml;charset=utf-8");
		log.info("Response: " + rliResponse);
	}
	
	/**
	 * Creates a RLI using the sugar API
	 * @param URL
	 * @param RLIID
	 * @param userID
	 * @param password
	 */
	public void createRLI(String URL, String RLIID, String userID, String password, String productLevel, String product){
		HttpUtils httpCalls = new HttpUtils();
		RLIAPITemplates rli = new RLIAPITemplates();
		
		log.info("Logging in to get session ID");
		String session = getSessionID(URL, userID, password);

		log.info("Creating RLI");
		String rliString = rli.createRLI(RLIID, userID, session, productLevel, product);
		log.info("Request: " + rliString);
		
		String rliResponse = httpCalls.postRequest(URL+GC.soapURLExtension, GC.sugarAPIHeaders, rliString,"text/xml;charset=utf-8");
		log.info("Response: " + rliResponse);
	}
	
	
	/**
	 * Creates a contact using the sugar API
	 * @param URL
	 * @param contactID
	 * @param clientID
	 * @param userID
	 * @param password
	 */
//	public void createContact(String URL, String contactID, String clientID, String userID, String password, String firstName, String lastName){
//		HttpUtils httpCalls = new HttpUtils();
//		ContactAPITemplate con = new ContactAPITemplate();
//		
//		log.info("Logging in to get session ID");
//		String session = getSessionID(URL, userID, password);
//		
//		log.info("Creating Contact");
//		String conString = con.createContact(contactID, clientID, session, firstName, lastName);
//		log.info("Request: " + conString);
//		
//		String conResponse = httpCalls.postRequest(URL+GC.soapURLExtension, GC.sugarAPIHeaders, conString, "text/xml;charset=utf-8");
//		log.info("Response: " + conResponse);
//	}
	
	/**
	 * Creates a contact using the sugar API
	 * @param URL
	 * @param contactID
	 * @param clientID
	 * @param userID
	 * @param password
	 */
	public void createContact(String URL, String contactID, String clientID, String userID, String password, String firstName, String lastName){
		HttpUtils httpCalls = new HttpUtils();
		ContactAPITemplate con = new ContactAPITemplate();
		
		log.info("Logging in to get session ID");
		String session = getSessionID(URL, userID, password);
		
		log.info("Creating Contact");
		String conString = con.createContact(contactID, clientID, session, firstName, lastName);
		log.info("Request: " + conString);
		
		String conResponse = httpCalls.postRequest(URL+GC.soapURLExtension, GC.sugarAPIHeaders, conString, "text/xml;charset=utf-8");
		log.info("Response: " + conResponse);
			
		return;
	}
	
	/**
	 * Deletes a specified contact via the SOAP API
	 * @param URL
	 * @param opptyID
	 * @param userID
	 * @param password
	 * @return
	 */
	public String deleteContactSOAP(String URL, String contactID,String userID, String password){
		HttpUtils httpCalls = new HttpUtils();
		ContactAPITemplate contact = new ContactAPITemplate();
		
		log.info("Logging in to get session ID");
		String sessionID = getSessionID(URL, userID, password);

		log.info("Deleting contact");
		String test = contact.deleteContact(contactID, sessionID);
		log.info("Request: " + test);
		
		String response = httpCalls.postRequest(URL+GC.soapURLExtension, GC.sugarAPIHeaders, test,"text/xml;charset=utf-8");
		log.info("Response: " + response);
		
		return parseDeleteContact(response);
	}
	
	/**
	 * Gets a session id by logging in to the sugar API
	 * @param URL
	 * @param userID
	 * @param password
	 * @return session ID
	 */
	public String getSessionID(String URL, String userID, String password){
		HttpUtils httpCalls = new HttpUtils();
		JSONObject response = null;
	
		System.out.println("method=login&input_type=json&response_type=json&rest_data={\"user_auth\":{\"password\":\"" + password + "\",\"user_name\":\""+userID+"\"}}");
		String responseString = httpCalls.postRequest(URL + GC.restURLExtension, GC.emptyArray, 
				"method=login&input_type=json&response_type=json&rest_data={\"user_auth\":{\"password\":\"" + password + "\",\"user_name\":\""+userID+"\"}}","application/x-www-form-urlencoded");
		try {
			response = (JSONObject)new JSONParser().parse(responseString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		log.info("Response: " +responseString);
		log.info("Session ID: " + (String) response.get("id"));
		return (String) response.get("id");
	}
	
	public Client selectClient(String clientID, String URL, String userID, String password){
		HttpUtils httpCalls = new HttpUtils();
		ClientAPITemplates cli = new ClientAPITemplates();
		
		log.info("Logging in to get session ID");
		String session = getSessionID(URL, userID, password);
		
		log.info("Selecting Client");
		String conString = cli.selectClient(clientID, session, userID);
		log.info("Request: " + conString);
		
		String conResponse = httpCalls.postRequest(URL+GC.soapURLExtension, GC.sugarAPIHeaders, conString, "text/xml;charset=utf-8");
		log.info("Response: " + conResponse);
		
		log.info("Parsing response and populating client object");
		Client client = parseSelectClient(conResponse);
		
		return client;
	}
	
	public String getUserID(String URL, String userID, String password){
		HttpUtils httpCalls = new HttpUtils();
		UserAPITemplates userTemp = new UserAPITemplates();
		
		log.info("Logging in to get session ID");
		String session = getSessionID(URL, userID, password);
		
		log.info("Selecting User");
		String userString = userTemp.selectUser(session, userID);
		log.info("Request: " + userString);
		
		String conResponse = httpCalls.postRequest(URL+GC.soapURLExtension, GC.sugarAPIHeaders, userString, "text/xml;charset=utf-8");
		log.info("Response: " + conResponse);
		
		 Document document = null;
			try {
				document = DocumentHelper.parseText(conResponse);
			} catch (DocumentException e) {
				log.info("User select response parsing has failed: " + e.getLocalizedMessage());
			}
			
			 return getSpecificContent(document, "id");
	}
	
	/**
	 * Get the user bean by calling the rest/v10/Users api
	 */
	public String getUserID(String userID,String URL, String[] headers){
		String user = userID.substring(0, userID.indexOf("@tst.ibm.com", 0));
		HttpUtils httpCalls = new HttpUtils();
		
		String response = httpCalls.getRequest(URL+"rest/v10/Users?q="+user, headers);

		return new APIUtilities().returnValuePresentInJson(response, "id");
	}
	
	/**
	 * Create a site via the SOAP API
	 * @param URL
	 * @param dc
	 * @param team
	 * @param userID
	 * @param password
	 * @return Client
	 */
	public Client createSite(String URL, Client dc, String[] team, String userID, String password){
		Client site = new Client();
		site.sClientID = "55SC-" + new Random().nextInt(999999);
		site.sClientName = site.sClientID;
		site.sParentName = dc.sClientName;
		site.sParentID = dc.sBeanID;
		
		for(String teamMember : team) {
		 site.vClientTeam.add(teamMember);
		}
		
		HttpUtils httpCalls = new HttpUtils();
		ClientAPITemplates cli = new ClientAPITemplates();
		
		log.info("Logging in to get session ID");
		String session = getSessionID(URL, userID, password);
		
		log.info("Creating Site");
		String conString = cli.createClient(site, session, userID);
		log.info("Request: " + conString);
		
		String response = httpCalls.postRequest(URL+GC.soapURLExtension, GC.sugarAPIHeaders, conString, "text/xml;charset=utf-8");
		log.info("Response: " + response);
		
		return parseSelectClient(response, site);
	}
	
	/**
	 * This is not possible using a bus admin user. Maybe via the functional id but we cannot save that password into rtc. Leaving method in case anyone tries to implement it again.
	 * Delete a site via the SOAP API
	 * @param URL
	 * @param siteID
	 * @param userID
	 * @param password
	 * @return error code from SOAP response
	 */
	@Deprecated
	public String deleteSiteSOAP(String URL, String siteID,String userID, String password){
		HttpUtils httpCalls = new HttpUtils();
		ClientAPITemplates client = new ClientAPITemplates();
		
		log.info("Logging in to get session ID");
		String sessionID = getSessionID(URL, userID, password);

		log.info("Deleting site");
		String test = client.deleteSite(siteID, sessionID);
		log.info("Request: " + test);
		
		String response = httpCalls.postRequest(URL+GC.soapURLExtension, GC.sugarAPIHeaders, test,"text/xml;charset=utf-8");
		log.info("Response: " + response);
		
		return parseDeleteSite(response);
	}
	
	
	private Client parseSelectClient(String response, Client client){
		 Document document = null;
		try {
			document = DocumentHelper.parseText(response);
		} catch (DocumentException e) {
			log.info("Client select response parsing has failed: " + e.getLocalizedMessage());
		}
		 
		 client.sClientID = getContent(document, "ccms_id");
		 client.sClientName = getContent(document, "name");
		 client.sBeanID = getContent(document, "id");
		 client.sParentName = getContent(document, "parent_name");
		 client.sParentID = getContent(document, "parent_id");
		 client.sGlobalClientName = getContent(document, "global_client_name");
		 client.sGlobalClientID = getContent(document, "global_client_id");
		 client.sUltimateClientCCMS_ID = getContent(document, "ultimate_client_ccms_id");
		 client.sGlobalClientCCMS_ID = getContent(document, "global_client_ccms_id");
		 client.sDefaultSiteID = getContent(document, "default_site_id");
		        
		return client; 
	}
	
	public String parseCheckOppty(String response){
		 Document document = null;
			try {
				document = DocumentHelper.parseText(response);
			} catch (DocumentException e) {
				log.info("Oppty select response parsing has failed: " + e.getLocalizedMessage());
			}
			
			return getContentOppty(document);
	}
	
	public String parseDeleteContact(String response){
		 Document document = null;
			try {
				document = DocumentHelper.parseText(response);
			} catch (DocumentException e) {
				log.info("Contact has failed to be deleted: " + e.getLocalizedMessage());
			}
			
			return getContentContact(document);
	}
	
	/**
	 * Check that no error was returned when deleting site
	 * @param response
	 * @return
	 */
	public String parseDeleteSite(String response){
		 Document document = null;
			try {
				document = DocumentHelper.parseText(response);
			} catch (DocumentException e) {
				log.info("Site has failed to be deleted: " + e.getLocalizedMessage());
			}
			
			return getContentSite(document);
	}
	
	private Client parseSelectClient(String response){
		Client client = new Client();
		return parseSelectClient(response, client);
	}
	
	private String getContentOppty(Document document) {
		  return document.selectSingleNode("//error_code").getText();
	}
	
	private String getContentSite(Document document) {
		  return document.selectSingleNode("//error_code").getText();
	}
	
	private String getContentContact(Document document) {
		  return document.selectSingleNode("//error_code").getText();
	}
	
	private String getContent(Document document, String attribute) {
		  return document.selectSingleNode("//return_data/item/name[contains(text(),'"+attribute+"')]/../value").getText();
	}
	
	private String getSpecificContent(Document document, String attribute) {
		  return document.selectSingleNode("//return_data/item/name[text() = '"+attribute+"']/../value").getText();
	}

}
