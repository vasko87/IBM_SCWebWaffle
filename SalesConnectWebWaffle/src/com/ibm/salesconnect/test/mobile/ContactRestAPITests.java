package com.ibm.salesconnect.test.mobile;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.APIUtilities;
import com.ibm.salesconnect.API.ContactRestAPI;
import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.API.MeetingRestAPI;
import com.ibm.salesconnect.API.SugarAPI;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;

/**
<br/><br/>
 * Test to validate the create,read,delete,Related TO,Link ,Create Contact Helper,Favorite and Unfavorite functionality of the Contact module using API
 * @author 
 * Veena H
<br/><br/>
 * */
public class ContactRestAPITests extends ProductBaseTest {

	Logger log = LoggerFactory.getLogger(ContactRestAPITests.class);


	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>	 
	 * <li>Create a New Contact </li>
	 * * <li>Search Contact</li>
	 * <li>Verify Contact Create as Expected</li>
	 * </ol>
	 */
	@Test(groups = {"MOBILE"})
	public void Test_createContact(){
		log.info("Start test method Test_createContact.");
		log.info("Getting user.");

		User user = commonUserAllocator.getUser(this);

		log.info("Retrieving OAuth2Token.");

		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		log.info("Creating a contact.");

		String firstName = "FirstName";
		String lastName = "LastName";
		String country = "US";
		String officePhone = "(555) 555-5555";
		String assignedUserID = new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword());

		ContactRestAPI contactRestAPI = new ContactRestAPI();
		String jsonString = contactRestAPI.createContact(testConfig.getBrowserURL(), token, firstName, lastName, country, officePhone,assignedUserID );

		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Contact creation failed.");
		} else {

			try {
				JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
				//validate the contact was created as expected
				Assert.assertEquals((String) postResponse.get("first_name"), firstName, "First name was not returned as expected.  ");
				Assert.assertEquals((String) postResponse.get("last_name"), lastName, "Last name was not returned as expected.  ");
				Assert.assertEquals((String) postResponse.get("primary_address_country"), country, "Country was not returned as expected.  ");
				Assert.assertEquals((String) postResponse.get("phone_work"), officePhone, "Work phone was not returned as expected.  ");
				Assert.assertEquals((String) postResponse.get("assigned_user_id"), assignedUserID, "Assigned user id was not returned as expected.  ");
			} catch (Exception e) {
				Assert.assertTrue(false, "Contact was not created as expected.");
			}

			log.info("Contact successfully created.");

		}

		log.info("End test method Test_createContact.");
	}

	
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Search Contact from the pool</li>
	 * <li>Convert the client as Favorite Contact </li>
	 * <li>Verify Contact convert to Favorite Contact</li>
	 * </ol>
	 */
	
	@Test(groups = {"MOBILE"})
	public void Test_favoriteContact(){
		log.info("Start test method Test_favoriteContact.");
		log.info("Getting user.");

		User user = commonUserAllocator.getUser(this);

		log.info("Retrieving OAuth2Token.");

		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());
		String favorite = "favorite";

		String contactId = ContactRestAPITests.createContactHelper(user, log, testConfig.getBrowserURL(), token);	

		ContactRestAPI contactRestAPI = new ContactRestAPI();
		String jsonString = contactRestAPI.favoriteContact(testConfig.getBrowserURL(), token, contactId, favorite, new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword()));

		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "favoriting Contact failed.");
		} 

		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			//validate the contact was favorited as expected
			Assert.assertTrue((Boolean)postResponse.get("my_favorite"));
		} catch (Exception e) {
			Assert.assertTrue(false, "Contact was not marked as favorite as expected.");
		}

		log.info("End test method Test_favoriteContact.");
	}

	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Search Contact from the pool</li>
	 * <li>Convert the Contact as unFavorite Contact </li>
	 * <li>Verify Contact convert to unFavorite Contact</li>
	 * </ol>
	 */
	
	@Test(groups = {"MOBILE"})
	public void Test_unFavoriteContact(){
		log.info("Start test method Test_unFavoriteContact.");
		log.info("Getting user.");

		User user = commonUserAllocator.getUser(this);

		log.info("Retrieving OAuth2Token.");

		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		String contactId = ContactRestAPITests.createContactHelper(user, log, testConfig.getBrowserURL(), token);	
		String favorite = "unfavorite";

		ContactRestAPI contactRestAPI = new ContactRestAPI();
		String jsonString = contactRestAPI.favoriteContact(testConfig.getBrowserURL(), token, contactId, favorite, new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword()));

		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "unfavoriting Contact failed.");
		} 

		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			Assert.assertFalse((Boolean)postResponse.get("my_favorite"));
		} catch (Exception e) {
			Assert.assertTrue(false, "Contact was not marked as unFavorite as expected.");
		}

		log.info("End test method Test_unFavoriteContact.");
	}
	
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Search Contact from the pool</li>
	 * <li>Delete the Contact </li>
	 * <li>Verify Contact is deleted successfully </li>
	 * </ol>
	 */
	
	@Test(groups = {"MOBILE"})
	public void Test_deleteContact(){
		log.info("Start test method Test_deleteContact.");
		log.info("Getting user.");

		User user = commonUserAllocator.getUser(this);

		log.info("Retrieving OAuth2Token.");

		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		String contactId = ContactRestAPITests.createContactHelper(user, log, testConfig.getBrowserURL(), token);	

		ContactRestAPI contactRestAPI = new ContactRestAPI();
		String jsonString = contactRestAPI.deleteContact(testConfig.getBrowserURL(), token, contactId);

		if (jsonString.equalsIgnoreCase("false")) 
		{
			Assert.assertTrue(false, "Deleting Contact failed.");
		} 

		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			//validate the contact was deleted as expected
			Assert.assertEquals((String) postResponse.get("id"), contactId, "Id was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Contact was not deleted as expected.");
		}

		log.info("End test method Test_deleteContact.");
	}

	
	
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Search Contact from the pool </li>
	 * <li>Create a New Meeting to link </li>
	 * <li>Link New Meeting to Contact</li>
	 * <li>Verify Meeting to Contact is Linked successfully </li>
	 * </ol>
	 */
	@Test(groups={"MOBILE"})
	public void Test_linkMeetingToContact()
	{

		log.info("Start test method test_linkMeetingtoClient");

		log.info("Getting user");

		User user=commonUserAllocator.getUser();
		String assignedUserID = new APIUtilities().getUserBeanIDFromEmail(baseURL, user.getEmail(), user.getPassword());	

		LoginRestAPI loginRestAPI=new LoginRestAPI();

		String Token=loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(),user.getEmail(),user.getPassword());

		String contactId = ContactRestAPITests.createContactHelper(user, log, testConfig.getBrowserURL(), Token);	

		ContactRestAPI contactRestAPI=new ContactRestAPI();

		MeetingRestAPI meeting=new MeetingRestAPI();

		String meetingid=meeting.createMeetingreturnBean(testConfig.getBrowserURL(), Token, assignedUserID);

		String jsonString= contactRestAPI.linkRecordToContact(testConfig.getBrowserURL(), Token, contactId, meetingid, "Meetings");

		if(jsonString.equalsIgnoreCase("false"))
		{
			Assert.assertTrue(false,"meeting was not linked to client");
		}
		try
		{
			JSONObject postResponse=(JSONObject)new JSONParser().parse(jsonString);
			JSONObject record=(JSONObject)postResponse.get("record");
			Assert.assertEquals((String)record.get("id"),contactId,"Parent Record was not returned as expected");
		}
		catch(Exception e)
		{
			Assert.assertTrue(false,"meeting was not linked with client as expected");
		}
		log.info("End test Method test_linkMeetingToClient");

	}


	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Search Contact from the pool </li>
	 * <li>Create a New Call to link </li>
	 * <li>Link New Call to Contact</li>
	 * <li>Verify Call to Contact is Linked successfully </li>
	 * </ol>
	 */
	@Test(groups = {"MOBILE"})
	public void Test_linkCallToContact(){
		log.info("Start test method Test_linkCallToContact.");
		log.info("Getting user.");

		User user = commonUserAllocator.getUser(this);

		log.info("Retrieving OAuth2Token.");

		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		//get a Contact from the Contact pool


		String contactId = ContactRestAPITests.createContactHelper(user, log, testConfig.getBrowserURL(),token);	
		//create a call to relate
		String callId = CallRestAPITests.createCallHelper(user, log, testConfig.getBrowserURL(), this);

		ContactRestAPI ContactRestAPI = new ContactRestAPI();
		//CallRestAPI callid=new CallRestAPI();

		String jsonString = ContactRestAPI.linkRecordToContact(testConfig.getBrowserURL(), token, contactId, callId, "Calls");

		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Relating call to Contact failed.");
		} 

		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			//validate the call was related to opportunity as expected
			Assert.assertEquals((String) record.get("id"), contactId, "Parent record was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Call was not related to Contact as expectd.");
		}

		log.info("End test method Test_linkCallToContact.");
	}


	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Search Contact from the pool </li>
	 * <li>Create a New Opportunity to link </li>
	 * <li>Link New Opportunity to Contact</li>
	 * <li>Verify Opportunity to Contact is Linked successfully </li>
	 * </ol>
	 */
	@Test(groups = {"MOBILE"})
	public void Test_linkOpportunityToContact(){
		log.info("Start test method Test_linkOpportunityToContact.");
		log.info("Getting user.");

		User user = commonUserAllocator.getUser(this);

		log.info("Retrieving OAuth2Token.");

		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		String contactId = ContactRestAPITests.createContactHelper(user, log, testConfig.getBrowserURL(), token);

		//create an opportunity to link
		String opportunityId = OpportunityRestApiTests.createOpportunityHelper(user, log, testConfig.getBrowserURL(), this);

		ContactRestAPI contactRestAPI = new ContactRestAPI();
		String jsonString = contactRestAPI.linkRecordToContact(testConfig.getBrowserURL(), token, contactId, opportunityId, "Opportunity");

		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Linking Opportunity to Contact failed.");
		} 

		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			//validate the opportunity was related to Contact as expected
			Assert.assertEquals((String) record.get("id"), contactId, "Parent record was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Opportunity was not linked to Contact as expectd.");
		}

		log.info("End test method Test_linkOpportunityToContact.");
	}

	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Search Contact from the pool </li>
	 * <li>Edit the Contact </li>
	 * <li>Verify the Contact is edited successfully </li>
	 * </ol>
	 */	
	//Remove from text bucket for now because always getting a 403 not authorized
	//@Test(groups = {"MOBILE"})
	public void Test_editContact(){
		log.info("Start test method Test_editContact.");
		log.info("Getting user.");

		User user = commonUserAllocator.getUser(this);

		log.info("Retrieving OAuth2Token.");

		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		String contactId = ContactRestAPITests.createContactHelper(user, log, testConfig.getBrowserURL(), token);

		String firstName = "Greg";
		String lastName = "Roberts";
		String country = "US";
		String officePhone = "(555) 555-5555";		

		ContactRestAPI contactRestAPI = new ContactRestAPI();
		String jsonString = contactRestAPI.editContact(testConfig.getBrowserURL(), token, contactId, firstName, lastName, country, officePhone, new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword()));

		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Editing Contact failed.");
		} 

		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			//validate the call was edited as expected
			Assert.assertEquals((String) postResponse.get("first_name"), firstName, "first name was not returned as expected.  ");
			Assert.assertEquals((String) postResponse.get("last_name"), lastName, "last name was not returned as expected.  ");
			Assert.assertEquals((String) postResponse.get("primary_address_country"), country, "country was not returned as expected.  ");
			Assert.assertEquals((String) postResponse.get("phone_office"), officePhone, "office phone was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Contact was not edited as expected.");
		}

		log.info("End test method Test_editContact.");
	}

	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Create a New Contact </li>
	 * <li>Related to New Contact to Opportunity</li>
	 * <li>Verify New Contact is related to Opportunity</li>
	 * </ol>
	 */
	@Test(groups = {"MOBILE"})
	public void Test_relateNewOpportunityToContact(){
		log.info("Start test method Test_relateNewOpportunityToContact.");
		log.info("Getting user.");

		User user = commonUserAllocator.getUser(this);

		log.info("Retrieving OAuth2Token.");

		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		String contactId = ContactRestAPITests.createContactHelper(user, log, testConfig.getBrowserURL(), token);

		//get a client from the client pool
		String clientId = commonClientAllocator.getGroupClient(GC.SC, this).getCCMS_ID();
		String sessionID = new SugarAPI().getSessionID(baseURL, user.getEmail(), user.getPassword());
		String realClientId = new APIUtilities().getClientBeanIDFromCCMSID(testConfig.getBrowserURL(), clientId, user.getEmail(), user.getPassword(),sessionID);
		String contactId2 = ContactRestAPITests.createContactHelper(user, log, testConfig.getBrowserURL(), token);

		String desc = "A description";
		String source = "RLPL";
		String salesStage = "03";
		String date = "2016-10-28";

		ContactRestAPI contactRestAPI = new ContactRestAPI();
		String jsonString = contactRestAPI.relateNewOpportunityToContact(testConfig.getBrowserURL(), token, contactId, desc, source, salesStage, date, realClientId, contactId2, new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword(),sessionID));
		log.info(jsonString);
		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Relating Opportunity to Contact failed.");
		} 

		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			Assert.assertEquals((String) record.get("id"), contactId, "Parent record was not returned as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Opportunity was not related to Contact as expectd.");
		}

		log.info("End test method Test_relateNewOpportunityToContact.");

	}
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Create a New Contact </li>
	 * <li>Related to New Contact to the Call</li>
	 * <li>Verify New Contact is related to Call</li>
	 * </ol
	**/
	@Test(groups = {"MOBILE"})
	public void Test_relateNewCallToContact(){
		log.info("Start test method Test_relateNewCallToContact.");
		log.info("Getting user.");

		User user = commonUserAllocator.getUser(this);

		log.info("Retrieving OAuth2Token.");

		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user.getEmail(), user.getPassword());

		String contactId = ContactRestAPITests.createContactHelper(user, log, testConfig.getBrowserURL(), token);

		String callSubject = "Call with my client.";
		String callDate = "2013-10-29T06:00:00.000Z";
		String callStatus = "Not Held";
		String callType = "face_to_face";
		String callDuration = "45";

		ContactRestAPI contactRestAPI = new ContactRestAPI();
		String jsonString = contactRestAPI.relateNewCallToContact(testConfig.getBrowserURL(), token, contactId, callSubject, callDate, callStatus, callType, callDuration, new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(),user.getEmail(),user.getPassword()));

		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Relating call to Contact failed.");
		} 

		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			JSONObject record = (JSONObject)postResponse.get("record");
			JSONObject relatedRecord = (JSONObject) postResponse.get("related_record");
			//validate the call was related to client as expected
			Assert.assertEquals((String) record.get("id"), contactId, "Parent record was not returned as expected.  ");
			Assert.assertEquals((String) relatedRecord.get("parent_id"), contactId, "Call was not related to contact as expected.  ");
		} catch (Exception e) {
			Assert.assertTrue(false, "Call was not related to Contact as expectd.");
		}

		log.info("End test method Test_relateNewCallToContact.");
	}

	/*
	 *  HELPERS
	 * -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
	 */

	/*
	 * Creates a contact and returns the contact ID.
	 */
	public static String createContactHelper(User user, Logger log, String url) {
		log.info("Creating a contact.");

		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(url, user.getEmail(), user.getPassword());

		String firstName = "FirstName";
		String lastName = "LastName";
		String country = "US";
		String officePhone = "(555) 555-5555";

		ContactRestAPI contactRestAPI = new ContactRestAPI();
		String jsonString = contactRestAPI.createContact(url, token, firstName, lastName, country, officePhone, new APIUtilities().getUserBeanIDFromEmail(url,user.getEmail(),user.getPassword()));

		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Contact creation failed, prior to creating an opportunity.");
		} 

		String contactId = "";

		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			contactId = (String) postResponse.get("id");
		} catch (Exception e) {
			Assert.assertTrue(false, "Contact was not created as expected.");
		}

		return contactId;
	}

	public static String createContactHelper(User user, Logger log, String url, String token) {
		log.info("Creating a contact.");

		String firstName = "FirstName";
		String lastName = "LastName";
		String country = "US";
		String officePhone = "(555) 555-5555";

		ContactRestAPI contactRestAPI = new ContactRestAPI();
		String jsonString = contactRestAPI.createContact(url, token, firstName, lastName, country, officePhone, new APIUtilities().getUserBeanIDFromEmail(url,user.getEmail(),user.getPassword()));

		if (jsonString.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "Contact creation failed, prior to creating an opportunity.");
		} 

		String contactId = "";

		try {
			JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
			contactId = (String) postResponse.get("id");
		} catch (Exception e) {
			Assert.assertTrue(false, "Contact was not created as expected.");
		}

		return contactId;
	}

	/*
	 * Creates a contact and returns the contact ID.
	 */
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
