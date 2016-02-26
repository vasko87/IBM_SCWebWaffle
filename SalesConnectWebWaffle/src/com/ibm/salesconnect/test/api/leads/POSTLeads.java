package com.ibm.salesconnect.test.api.leads;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.APIUtilities;
import com.ibm.salesconnect.API.ApiBaseTest;
import com.ibm.salesconnect.API.CallRestAPI;
import com.ibm.salesconnect.API.EmailRestAPI;
import com.ibm.salesconnect.API.LeadsRestAPI;
import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.API.MeetingRestAPI;
import com.ibm.salesconnect.API.NoteRestAPI;
import com.ibm.salesconnect.API.SugarAPI;
import com.ibm.salesconnect.API.TestDataHolder;
import com.ibm.salesconnect.API.YamlUtill;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.test.mobile.TaskRestAPITests;

/**
 * @author aaron fortune	
 * @date Jan 29, 2015
 */

public class POSTLeads extends ApiBaseTest
{
	
	private static final Logger log = LoggerFactory.getLogger(POSTLeads.class);
	@Parameters({ "apiExtension", "applicationName", "APIM", "apim_environment" })
	private POSTLeads(@Optional("leads") String apiExtension,
			@Optional("SC Auto create") String applicationName,
			@Optional("true") String APIM,
			@Optional("development") String environment) {

		super(apiExtension, applicationName, APIM, environment);
	}
	protected String testCaseName = "";


	
	private TestDataHolder testData;
    int rand = new Random().nextInt(100000);
    private HashMap<String, Object> yamlReturnedMap=null;

	private String noteID = null;
	private String noteID1 = null;
	private String noteID2 = null;
	private String noteID3 = null;
	private String taskID = null;
	private String taskID1 = null;
	private String taskID2 = null;
	private String taskID3 = null;
	private String noteSubject = "post task note subject";
	private String callID = null;
	private String callID1 = null;
	private String callID2 = null;
	private String callID3 = null;
	private String callSubject = "post task call subject";
	private String meetingID = null;
	private String meetingID1 = null;
	private String meetingID2 = null;
	private String meetingID3 = null;
	private String emailID = "";
	private String emailID1 = "";
	private String emailID2 = "";
	private String emailID3 = "";
	private String assignedUserID = null;
	private String assignedUserIDYaml = null;
	private String userID = null;
	private String clientID = null;
	private String baseID = null;
	private Object[][] loadYamlFile=null;
	private String expectedResponseCode = "200";
	private String urlLeadID = null;
	private String urlExtension = "";
	private String relatedObject = "";
	private String externalExtension = null;
	private String url = "";
	private String jsonString = "";
	private String yamlContact = "";
	private User user = null;
	private String tokenYaml = null;
	private String token = null;
	
	private void createYamlObjects(){
			user = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			PoolClient poolClient= commonClientAllocator.getGroupClient(GC.SC,this);
			clientID = poolClient.getCCMS_ID();
			log.info("Creating contact");
			SugarAPI sugarAPI = new SugarAPI();
			sugarAPI.createContact(testConfig.getBrowserURL(), yamlContact, clientID, user.getEmail(), user.getPassword(), "ContactFirst", "ContactLast");
			assignedUserIDYaml = new APIUtilities().getUserBeanIDFromEmail(baseURL, user.getEmail(), user.getPassword());
			log.info("Retrieving OAuth2Token.");		
			LoginRestAPI loginRestAPI = new LoginRestAPI();
			if (APIm) {
				tokenYaml = loginRestAPI.getOAuth2TokenViaAPIManager(getApiManagement() + getOAuthExtension(), user.getEmail(), user.getPassword(),"200");
			}
			else {
				tokenYaml = loginRestAPI.getOAuth2Token(baseURL, user.getEmail(), user.getPassword());
			}
		
	}
	
	private void createObjects()
	{
		log.info("Start creating prerequisites");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
		User user2 = commonUserAllocator.getUser(this);
		String baseURL = testConfig.getBrowserURL();
		userID = new APIUtilities().getUserBeanIDFromEmail(baseURL, user2.getEmail(), user2.getPassword());

		LoginRestAPI loginRestAPI = new LoginRestAPI();
		token = loginRestAPI.getOAuth2Token(baseURL, user1.getEmail(), user1.getPassword());

		assignedUserID = new APIUtilities().getUserBeanIDFromEmail(baseURL, user1.getEmail(), user1.getPassword());	

		log.info("Creating note");
		NoteRestAPI noteAPI = new NoteRestAPI();
		noteID = noteAPI.createNotereturnBean(testConfig.getBrowserURL(), token, noteSubject, "post task note description", assignedUserID);
		noteID1 = noteAPI.createNotereturnBean(testConfig.getBrowserURL(), token, noteSubject, "post task note description", assignedUserID);
		noteID2 = noteAPI.createNotereturnBean(testConfig.getBrowserURL(), token, noteSubject, "post task note description", assignedUserID);
		noteID3 = noteAPI.createNotereturnBean(testConfig.getBrowserURL(), token, noteSubject, "post task note description", assignedUserID);
		
		log.info("Creating call");
		CallRestAPI callRestAPI = new CallRestAPI();
		callID = callRestAPI.createCallreturnBean(testConfig.getBrowserURL(), token, callSubject, "2013-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);
		callID1 = callRestAPI.createCallreturnBean(testConfig.getBrowserURL(), token, callSubject, "2013-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);
		callID2 = callRestAPI.createCallreturnBean(testConfig.getBrowserURL(), token, callSubject, "2013-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);
		callID3 = callRestAPI.createCallreturnBean(testConfig.getBrowserURL(), token, callSubject, "2013-10-28T15:14:00.000Z", "Held", "outbound_call", "30", assignedUserID);

		log.info("Creating Meeting");
		MeetingRestAPI meetingRestAPI = new MeetingRestAPI();
		meetingID = meetingRestAPI.createMeetingreturnBean(baseURL, token, assignedUserID);
		meetingID1 = meetingRestAPI.createMeetingreturnBean(baseURL, token, assignedUserID);
		meetingID2 = meetingRestAPI.createMeetingreturnBean(baseURL, token, assignedUserID);
		meetingID3 = meetingRestAPI.createMeetingreturnBean(baseURL, token, assignedUserID);
		
		log.info("Creating task");
		taskID = TaskRestAPITests.createTaskHelper(user1,"Base task",log,baseURL);
		taskID1 = TaskRestAPITests.createTaskHelper(user1,"Base task",log,baseURL);
		taskID2 = TaskRestAPITests.createTaskHelper(user1,"Base task",log,baseURL);
		taskID3 = TaskRestAPITests.createTaskHelper(user1,"Base task",log,baseURL);
		
		emailID1 = new EmailRestAPI().createEmailreturnBean(baseURL, token);
		emailID2 = new EmailRestAPI().createEmailreturnBean(baseURL, token);
		emailID3 = new EmailRestAPI().createEmailreturnBean(baseURL, token);
		
		log.info("Creating lead");
		LeadsRestAPI leadRestAPI = new LeadsRestAPI();
		baseID = leadRestAPI.createLeadreturnBean(testConfig.getBrowserURL(), token, "lead Name", "lead Description", userID);
	}

    public void addDataFile(String filePath){
        if(this.testData== null){
            this.testData = new TestDataHolder();
        }
         this.testData.addDataLocation(filePath, ";");
     }

    
	@DataProvider(name="YamlParameters")
    public Object[][] getYamlData(){
    	this.createYamlObjects();
		loadYamlFile=YamlUtill.loadYamlFile();
		return loadYamlFile;
    }
	
	@DataProvider(name="jsonProvider")
	    public Object[][] getTestData(){
		this.createObjects();
		this.addDataFile("test_config/extensions/api/lead/postLeads.csv");
		return testData.getAllDataRows();
    }
	
	@Test(dataProvider = "jsonProvider")
	public void TestCreateLeadLinking(HashMap<String,Object> parameterValues){
		Iterator<Map.Entry<String, Object>> it = parameterValues.entrySet().iterator();
        log.info("Getting user.");		
        User user1 = commonUserAllocator.getUser(this);
        User user2 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
		String body = "";
		String expectedResponse = "";
		
        while (it.hasNext()) 
        {
          Map.Entry<String, Object> pairs = (Map.Entry<String, Object>)it.next();
          if (pairs.getKey().equalsIgnoreCase("body")) {
        	  body=(String) pairs.getValue();	
        	  body = body.replace("*callID1*", callID1);
        	  body = body.replace("*callID2*", callID2);
        	  body = body.replace("*callID3*", callID3);
        	  body = body.replace("*emailID1*", emailID1);
        	  body = body.replace("*emailID2*", emailID2);
        	  body = body.replace("*emailID3*", emailID3);
        	  body = body.replace("*meetingID1*", meetingID1);
        	  body = body.replace("*meetingID2*", meetingID2);
        	  body = body.replace("*meetingID3*", meetingID3);
        	  body = body.replace("*noteID1*", noteID1);
        	  body = body.replace("*noteID2*", noteID2);
        	  body = body.replace("*noteID3*", noteID3);
        	  body = body.replace("*taskID1*", taskID1);
        	  body = body.replace("*taskID2*", taskID2);
        	  body = body.replace("*taskID3*", taskID3);
        	  body = body.replace("*userID*", new APIUtilities().getUserBeanIDFromEmail(baseURL, user1.getEmail(), user1.getPassword()));
          }
          else if (pairs.getKey().equalsIgnoreCase("expected_response")) {
        	  expectedResponse=(String) pairs.getValue();	
          }
          else if (pairs.getKey().equalsIgnoreCase("URL")) {
			urlExtension = (String) pairs.getValue();
			urlExtension = urlExtension.replace("*lead-id*", baseID);
			urlExtension = urlExtension.replace("*callID*", callID);
			urlExtension = urlExtension.replace("*emailID*", emailID);
			urlExtension = urlExtension.replace("*meetingID*", meetingID);
			urlExtension = urlExtension.replace("*noteID*", noteID);
			urlExtension = urlExtension.replace("*taskID*", taskID);
          }
          else if (pairs.getKey().equalsIgnoreCase("user")) {
        	if (pairs.getValue().equals("user2")) {
        		user = user1;
			}
        	else {
        		user = user2;
			}	
          }
          else if (pairs.getKey().equals("TC_Name")) {
				log.info("This is test " + pairs.getValue().toString());
			}
          
        }
        
		log.info("Sending POST request");
		LeadsRestAPI leadrestAPI = new LeadsRestAPI();
		leadrestAPI.postLead(getRequestUrl(urlExtension, null), getOAuthToken(user), body, expectedResponse);

	}

	
	@Test(dataProvider = "YamlParameters")
	    public void LeadsCreateParameterValues(HashMap<String,Object> parameterValues)
	    {
	        Iterator<Map.Entry<String, Object>> it = parameterValues.entrySet().iterator();
	        
	        Map<String, Object> request = new HashMap();
			//Set user details
	        while (it.hasNext()) 
	        {
	        Map.Entry<String, Object> pairs = (Map.Entry<String, Object>)it.next();
	          if (pairs.getKey().equalsIgnoreCase("assigned_user_id")) {
	        	  request.put(pairs.getKey(), assignedUserIDYaml);
	          }
	          else if (pairs.getKey().equalsIgnoreCase("first_name")) {
	        	  request.put(pairs.getKey(), user.getFirstName());
	          }
	          else if (pairs.getKey().equalsIgnoreCase("last_name")) {
	        	  request.put(pairs.getKey(), user.getLastName());	
	          }
	          else if (pairs.getKey().equalsIgnoreCase("full_name")) {
	        	  request.put(pairs.getKey(), user.getDisplayName());
	          }
	          else if (pairs.getKey().equalsIgnoreCase("reports_to_id")) {
	        	  request.put(pairs.getKey(), yamlContact);
	          }
	          else if (pairs.getKey().equalsIgnoreCase("report_to_name")) {
	        	  request.put(pairs.getKey(), "ContactFirst ContactLast");
	          }
	          else if (pairs.getKey().equalsIgnoreCase("email")) {
					JSONArray jsonArray = new JSONArray();
					JSONObject jsonObject = new JSONObject();
					try{
					jsonObject.put("email_address", user.getEmail());
					jsonObject.put("invalid_email", false);
					jsonObject.put("opt_out", false);
					jsonObject.put("primary_address", true);
					jsonObject.put("reply_to_address", false);
					}
					catch (Exception e) {
						// TODO: handle exception
					}
					
					jsonArray.add(jsonObject);
	        	  request.put(pairs.getKey(), jsonArray);
	          }
	          else if (pairs.getKey().equalsIgnoreCase("team_name")) {
					JSONArray jsonArray = new JSONArray();
					JSONObject jsonObject = new JSONObject();
					try{
					jsonObject.put("id", 1);
					jsonObject.put("name", "Global");
					jsonObject.put("name_2", "");
					jsonObject.put("primary", true);
					}
					catch (Exception e) {
						// TODO: handle exception
					}
					
					jsonArray.add(jsonObject);
	        	  request.put(pairs.getKey(), jsonArray);
	          }
	          else {
	        	  request.put(pairs.getKey(), pairs.getValue());
			 }
	        }
	       
	        JSONObject json = new JSONObject(request);
	        
			log.info("Sending POST request");
			LeadsRestAPI leadrestAPI = new LeadsRestAPI();
			String token = getOAuthToken(user);
			try{
			jsonString = leadrestAPI.postLead(getRequestUrl(url, null), token, json, expectedResponseCode);
			}
			catch (Exception e) {
				jsonString = leadrestAPI.postLead(getRequestUrl(url, null), getOAuthToken(user), json, expectedResponseCode);
			}
		

			//Verify response
			if (expectedResponseCode.equalsIgnoreCase("200"))
			{
				try {
					JSONObject postResponse = (JSONObject)new JSONParser().parse(jsonString);
					log.info(postResponse.toString());
			        Iterator<Map.Entry<String, Object>> postit = request.entrySet().iterator();
			        while (postit.hasNext()) 
			        {
			        	Map.Entry<String, Object> pairs1 = postit.next(); 
			        	//Temp if statement defect 61826
			        	String paramName = pairs1.getKey().toString();
			        	if (!((paramName.equalsIgnoreCase("status_description"))||(paramName.equalsIgnoreCase("account_description"))||(paramName.equalsIgnoreCase("description"))||(paramName.equalsIgnoreCase("lead_source_description")))) {
				        	//Temp for defect 62717
			        		if (!((paramName.contains("primary_address_street"))||(paramName.contains("alt_address_street")))) {
			        			//date is changed on T1 because of the timezone difference of the servers
			        			if (paramName.contains("date")) {
			        				String tempDateResponse = postResponse.get(pairs1.getKey().toString()).toString().toLowerCase();
			        				tempDateResponse=tempDateResponse.substring(0, 10);
									String tempDateInput = pairs1.getValue().toString().toLowerCase();
									tempDateInput=tempDateInput.substring(0, 10);
									Assert.assertEquals(tempDateInput, tempDateResponse,"Date value is incorrect, Expected: " + tempDateInput + " but received " + tempDateResponse);
			        			}
			        			else if(paramName.contains("team_name")){
			  			        //ignore parameter
			        			}
			        			else{
			        				Assert.assertTrue(postResponse.get(pairs1.getKey().toString()).toString().toLowerCase().contains(pairs1.getValue().toString().toLowerCase()), pairs1.getKey() +" was not returned as expected. " + postResponse.get(pairs1.getKey().toString()).toString().toLowerCase() + " instead of "+ pairs1.getValue().toString().toLowerCase() );
			        			}
			        		}
				        }
			        }
				} catch (Exception e) {
					e.printStackTrace();
					log.info(jsonString);
					Assert.assertTrue(false, "Lead was not created as expected.");
				}
				
				log.info("Lead successfully created.");	    
			}
			}  
	
	@Test
    public void convertNoEmail69287(){
      	 
   	   	log.info("Getting User");
       	User user = commonUserAllocator.getUser(this);
       	
    	log.info("Getting token");
   		LoginRestAPI loginRestAPI = new LoginRestAPI();
   		String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user.getEmail(), user.getPassword());
   		String userID = new APIUtilities().getUserBeanIDFromEmail(baseURL, user.getEmail(), user.getPassword());
   		
    	log.info("Creating lead");
   		LeadsRestAPI leadRestAPI = new LeadsRestAPI();
   		String lead = leadRestAPI.createLeadreturnBean(testConfig.getBrowserURL(), OAuthToken, "lead Name", "lead Description", userID);
   		
		log.info("Sending Post Convert request");
		String json ="{\"modules\": {\"Contacts\": { \"account_id\": \"\", \"address_suppressed\": false, \"agreement_num\": \"\", \"all_emails\": \"\", \"alt_address_city\": \"\", \"alt_address_country\": \"\", \"alt_address_postalcode\": \"\", \"alt_address_state\": \"\", \"alt_address_street\": \"\", \"alt_address_street_2\": \"\", \"alt_address_street_3\": \"\",\"assigned_user_id\" :\""+ userID +"\", \"assistant\": \"\", \"assistant_phone\": \"\", \"birthdate\": \"\", \"campaign_id\": \"\", \"client_value_survey_c\": \"\", \"contact_coverage_strategy_c\": \"\", \"contact_source_c\": \"\", \"contact_status_c\": \"P\", \"deleted\": false, \"department\": \"\", \"description\": \"\", \"dnb_principal_id\": \"\", \"do_not_call\": false, \"doc_owner\": \"\", \"email\": [], \"email1\": \"\", \"email2\": \"\", \"email_addresses_non_primary\": \"\", \"email_opt_out\": false, \"ext_ref_id1_c\": \"\", \"facebook\": \"\", \"facet_myitems\": \"\", \"first_name\": \"converttest\", \"full_name\": \"converttest converttest\", \"googleplus\": \"\", \"invalid_email\": false, \"key_contact_c\": false, \"key_contact_relation_c\": false, \"last_interaction_date_c\": \"\", \"last_name\": \"converttest\", \"lead_source\": \"LSCAMP\", \"mkto_sync\": false, \"my_favorite\": false, \"name\": \"converttest converttest\", \"name_backward\": \"converttest converttest\", \"name_forward\": \"converttest converttest\", \"order_id\": \"\", \"phone_fax\": \"\", \"phone_fax_suppressed\": false, \"phone_home\": \"\", \"phone_mobile\": \"123456789\", \"phone_mobile_suppressed\": false, \"phone_other\": \"\", \"phone_work\": \"\", \"phone_work_suppressed\": false, \"picture\": \"\", \"portal_active\": false, \"portal_app\": \"\", \"preferred_language\": \"\", \"primary_address_city\": \"\", \"primary_address_country\": \"US\", \"primary_address_country_values\": \"\", \"primary_address_postalcode\": \"\", \"primary_address_state\": \"\", \"primary_address_street\": \"\", \"primary_address_street_2\": \"\", \"primary_address_street_3\": \"\", \"relate_beans\": \"\", \"reports_to_id\": \"\", \"salutation\": \"\", \"supp_perm_c\": false, \"team_name\": [ { \"id\": 1, \"name\": \"Global\", \"name_2\": null, \"primary\": true } ], \"title\": \"\", \"twitter\": \"\", \"update_last_interaction_date\": true, \"user_favorites\": \"\" },\"Accounts\":{\"doc_owner\":\"\",\"user_favorites\":\"\",\"deleted\":false,\"ext_ref_id1_c\":\"\",\"global_client_ccms_id\" :\"\",\"update_client_hierarchy\":true,\"ccms_level\":\"S\",\"cover_id_rollup\":\"\",\"billing_address_auxiliary\" :false,\"shipping_address_auxiliary\":false,\"parent_site_id\":false,\"restricted\":false,\"gb_buying_group_id\" :\"\",\"buying_group_ccms_id\":\"\",\"gb_buying_group_ccms_id\":\"\",\"ultimate_client_id\":\"\",\"global_client_id\" :\"\",\"ultimate_client_ccms_id\":\"\",\"legal_name\":\"\",\"legal_name_lang_code\":\"\",\"legal_name_sub_lang_code\" :\"\",\"alt_lang_legal_name_lang_code\":\"\"," +
			          "\"alt_lang_legal_name_sub_lang_code\":\"\",\"alt_lang_legal_name\":\"\" ,\"legal_name_kana\":\"\",\"legal_name_kana_lang_code\":\"\",\"legal_name_kana_sub_lang_code\":\"\",\"grandparent_id\" :\"\",\"dc_id\":\"\",\"gc_id\":\"\",\"gu_id\":\"\",\"db_id\":\"\",\"gb_id\":\"\",\"indus_industry\":\"\",\"billing_address_lang_code\" :\"\",\"billing_address_sub_lang_code\":\"\",\"alt_lang_address_street\":\"\",\"alt_lang_address_city\":\"\",\"alt_lang_address_state\" :\"\",\"alt_lang_address_postalcode\":\"\",\"alt_lang_address_country\":\"\",\"alt_lang_address_county\":\"\",\"alt_lang_address_auxiliary\" :\"\",\"alt_lang_address_lang_code\":\"\",\"alt_lang_address_sub_lang_code\":\"\",\"issuing_country\":\"\",\"landed_country\" :\"\",\"enterprise_number\":\"\",\"inac_number\":\"\"," +
			          "\"cur_coverage_id\":\"\",\"mpp_num\":\"\",\"base_coverage_id\":\"\",\"cust_class_code\" :\"\",\"is_prospect\":\"\",\"affiliate_num\":\"\",\"super_inac\":\"\",\"tax_id\":\"\",\"create_code\":\"\",\"create_comments\" :\"\",\"legacy_ccms_id\":\"\",\"indus_sector_rollup\":[\"\"],\"indus_sic_code\":\"7412\",\"indus_class_name\":\"CD\",\"industry_list\" :\"V\",\"indus_isu_name\":\"14\",\"indus_sector_name\":\"\",\"indus_quad_tier_code\":\"6\",\"leadclient_rep\":\"\",\"assigned_user_id\" :\""+ userID +"\",\"name\":\"asdf\",\"billing_address_street\":\"\",\"billing_address_city\" :\"\",\"billing_address_state\":\"\",\"billing_address_postalcode\":\"\",\"billing_address_country\":\"US\",\"shipping_address_street\" :\"\",\"shipping_address_city\":\"\"," +
			          "\"shipping_address_state\":\"\",\"shipping_address_postalcode\":\"\",\"shipping_address_country\" :\"US\",\"phone_office\":\"\",\"my_favorite\":false,\"description\":\"\",\"twitter\":\"\",\"website\":\"\",\"team_name\":[ {\"id\":1,\"name\":\"Global\",\"name_2\":null,\"primary\":true}],\"email1\":\"conver@tst.com\",\"email2\":\"\",\"invalid_email\" :false,\"email_opt_out\":false,\"email_addresses_non_primary\":\"\",\"facebook\":\"\",\"googleplus\":\"\",\"phone_fax\" :\"\",\"campaign_id\":\"\",\"relate_beans\":\"\",\"facet_myitems\":\"\",\"order_id\":\"\",\"agreement_num\":\"\"}"+
			          "}}";
		
		String response = leadRestAPI.postLead(getRequestUrl("/" + lead + "/convert", null), OAuthToken,json, "200");
		
		log.info("Checking response body");
		Assert.assertTrue(new APIUtilities().checkIfValuePresentInJson(response, "_module", "Contacts"));
    }
			
			  @Test
			    public void convertTwice(){
			   	 
			   	   	log.info("Getting User");
			       	User user = commonUserAllocator.getUser(this);
			   		String userID = new APIUtilities().getUserBeanIDFromEmail(baseURL, user.getEmail(), user.getPassword());
			    	log.info("Getting token");
			   		LoginRestAPI loginRestAPI = new LoginRestAPI();
			   		String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user.getEmail(), user.getPassword());
			   		
			    	log.info("Creating lead");
			   		LeadsRestAPI leadRestAPI = new LeadsRestAPI();
			   		String lead = leadRestAPI.createLeadreturnBean(testConfig.getBrowserURL(), OAuthToken, "lead Name", "lead Description", userID);
			   		
					log.info("Sending Post Convert requestrequest");
					String json ="{\"modules\": {\"Contacts\": {\"deleted\": 0,\"do_not_call\": false,\"portal_active\": 0,\"preferred_language\": \"en_us\","+
						          "\"first_name\": \"one\",\"last_name\": \"smith\",\"title\": \"Director Operations\",\"primary_address_country\":\"US\"}}}";
						
					leadRestAPI.postLead(baseURL + "rest/v10/Leads/" + lead + "/convert", OAuthToken,json, "200");
					
					
					log.info("Sending POST request");
					LeadsRestAPI leadrestAPI = new LeadsRestAPI();
					String token = getOAuthToken(user);
					try{
					leadrestAPI.postLead(getRequestUrl("/" + lead + "/convert", null), token, json, "404");
					}
					catch (Exception e) {
						leadrestAPI.postLead(getRequestUrl("/" + lead + "/convert", null), getOAuthToken(user), json, "404");
					}
				
					
					
			    }

	/**
     * Get the oauth string
     */
	
	public String getOAuthExtension(){		
		return "oauth?" + clientIDandSecret;
	}
	   
}