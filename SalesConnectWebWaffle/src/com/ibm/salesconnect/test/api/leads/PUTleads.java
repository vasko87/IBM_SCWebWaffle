/**
 * 
 */
package com.ibm.salesconnect.test.api.leads;

import java.io.FileReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.json.simple.parser.ContainerFactory;
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
import com.ibm.salesconnect.API.LeadsRestAPI;
import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.API.TestDataHolder;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.test.mobile.TaskRestAPITests;

/**
 * @author timlehane
 * @date Feb 5, 2015
 */
public class PUTleads extends ApiBaseTest {
	private static final Logger log = LoggerFactory.getLogger(PUTleads.class);
	
	@Parameters({ "apiExtension", "applicationName", "APIM", "apim_environment" })
	private PUTleads(@Optional("leads") String apiExtension,
			@Optional("SC Auto update") String applicationName,
			@Optional("true") String APIM,
			@Optional("development") String environment) {

		super(apiExtension, applicationName, APIM, environment);
	}
	private TestDataHolder testData;
	
	private String lead1 = null;
	private String lead2 = null;
	private String baseID = null;
	private String taskID = null;
	private String taskLead = null;
	private String token = null;
	private String unLinkedtaskID = null;
	
	private void createObjects()
	{
		log.info("Start creating prerequisites");
		User user = commonUserAllocator.getUser(this);
		String userID = new APIUtilities().getUserBeanIDFromEmail(baseURL, user.getEmail(), user.getPassword());
		String baseURL = testConfig.getBrowserURL();

		LoginRestAPI loginRestAPI = new LoginRestAPI();
		token = loginRestAPI.getOAuth2Token(baseURL, user.getEmail(), user.getPassword());

		log.info("Creating task");
		taskID = TaskRestAPITests.createTaskHelper(user,"Base task",log,baseURL);
		unLinkedtaskID = TaskRestAPITests.createTaskHelper(user,"Base task",log,baseURL);
		
		log.info("Creating lead");
		LeadsRestAPI leadRestAPI = new LeadsRestAPI();
		baseID = leadRestAPI.createLeadreturnBean(baseURL, token, "lead Name", "lead Description", userID);
		taskLead = leadRestAPI.createLeadreturnBean(baseURL, token, "lead Name", "lead Description", userID);
		lead1 = leadRestAPI.createLeadreturnBean(baseURL, token, "lead Name", "lead Description", userID);
		lead2 = leadRestAPI.createLeadreturnBean(baseURL, token, "lead Name", "lead Description", userID);
		Assert.assertTrue(leadRestAPI.linkLeadtoObject(testConfig.getBrowserURL(), token, taskLead,"tasks",taskID));
	}
	
    public void addDataFile(String filePath){
        if(this.testData== null){
            this.testData = new TestDataHolder();
        }
         this.testData.addDataLocation(filePath, ";");
     }
    
	@DataProvider(name="jsonProvider")
    public Object[][] getTestData(){
	this.createObjects();
	this.addDataFile("test_config/extensions/api/lead/putLeads.csv");
	return testData.getAllDataRows();
	}
    
	@Test(dataProvider = "jsonProvider")
	public void TestUpdateLead(HashMap<String,Object> parameterValues){
		Iterator<Map.Entry<String, Object>> it = parameterValues.entrySet().iterator();
        log.info("Getting user.");		
        User user = null;
		String body = "";
		String expectedResponse = "";
		String urlExtension = "";
		String jsonString = "";
		
        while (it.hasNext()) 
        {
          Map.Entry<String, Object> pairs = (Map.Entry<String, Object>)it.next();
          System.out.println(pairs.getKey());
          System.out.println(pairs.getValue());
          if (pairs.getKey().equalsIgnoreCase("body")) {
        	  body=(String) pairs.getValue();	
        	  body = body.replace("*lead1*", lead1);
        	  body = body.replace("*lead2*", lead2);
          }
          else if (pairs.getKey().equalsIgnoreCase("expected_response")) {
        	  expectedResponse=(String) pairs.getValue();	
          }
          else if (pairs.getKey().equalsIgnoreCase("URL")) {
			urlExtension = (String) pairs.getValue();
			urlExtension = urlExtension.replace("*lead-id*", baseID);
			urlExtension = urlExtension.replace("*taskID*", taskID);
			urlExtension = urlExtension.replace("*taskLead*", taskLead);
			urlExtension = urlExtension.replace("*unLinkedtaskID*", unLinkedtaskID);
          }
          else if (pairs.getKey().equalsIgnoreCase("user")) {
        	if (pairs.getValue().equals("user2")) {
        		user = commonUserAllocator.getUser(this);
			}
        	else {
        		user = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
			}	
		}
          
        }
        
		log.info("Sending PUT request");
		LeadsRestAPI leadrestAPI = new LeadsRestAPI();
		String token = getOAuthToken(user);
		
		try{
		leadrestAPI.putLead(getRequestUrl(urlExtension, null), token, body, expectedResponse);
		}
		catch (Exception e) {
			leadrestAPI.putLead(getRequestUrl(urlExtension, null), getOAuthToken(user), body,  expectedResponse);
		}
		
	}
	
	
	@Test
    public void UpdateAllParameters(){
	    	log.info("Starting UpdateAllParameters");
			User user1 = commonUserAllocator.getUser(this);
			String baseURL = testConfig.getBrowserURL();
			String userID = new APIUtilities().getUserBeanIDFromEmail(baseURL, user1.getEmail(), user1.getPassword());
			
			String OAuthToken = getOAuthToken(user1.getEmail(), user1.getPassword());
			
			LeadsRestAPI leadsRestApi = new LeadsRestAPI();
			
			String updateLead = leadsRestApi.createLeadreturnBean(baseURL, OAuthToken, "lead Name", "lead Description", userID);
			
			log.info("Creating lead");
			String before = getPUTInput();
			String token = getOAuthToken(user1.getEmail(), user1.getPassword());
			String after ="";
			log.info("Sending PUT request");
			LeadsRestAPI leadrestAPI = new LeadsRestAPI();
			after = leadrestAPI.putLead(getRequestUrl("/" + updateLead, null), getOAuthToken(user1), before,  "200");

			Map beforeMap = getMap(before);
			Map afterMap = getMap(after);
			
			final Map<String, Boolean> comparisonResult = compareEntries(beforeMap, afterMap);
	
		    compareBeforeAfter(comparisonResult);

			log.info("Ending UpdateAllParameters");	
	}
	
    @Test
    public void convertThenPUT(){
   	 
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
		
		log.info("Updating lead");
		String before = getPUTInput();
		
		String after = "";
		Map beforeMap = getMap(before);
		Iterator<Map.Entry<String, Object>> it = beforeMap.entrySet().iterator();
		
        while (it.hasNext()) 
        {
          Map.Entry<String, Object> pairs = (Map.Entry<String, Object>)it.next();
          if (pairs.getKey().equalsIgnoreCase("status")) {
			pairs.setValue("LEADCONV");
          }
        }
		
		log.info("Sending PUT request");
		LeadsRestAPI leadrestAPI = new LeadsRestAPI();
		after = leadrestAPI.putLead(getRequestUrl("/" + lead, null), getOAuthToken(user), before,  "200");
		
		Map afterMap = getMap(after);
		

		final Map<String, Boolean> comparisonResult = compareEntries(beforeMap, afterMap);
		compareBeforeAfter(comparisonResult);
    }
    
	public static <K extends Comparable<? super K>, V>
	Map<K, Boolean> compareEntries(final Map<K, V> map1,
	    final Map<K, V> map2){
	    final Collection<K> allKeys = new HashSet<K>();
	    allKeys.addAll(map1.keySet());
	    allKeys.addAll(map2.keySet());
	    final Map<K, Boolean> result = new TreeMap<K, Boolean>();
	    for(final K key : allKeys){
	        result.put(key,
	            map1.containsKey(key) == map2.containsKey(key) &&
	            Boolean.valueOf(equal(map1.get(key), map2.get(key))));
	    }
	    return result;
	}
	
	private void compareBeforeAfter(Map<String, Boolean> comparisonResult){
	    for(final Entry<String, Boolean> entry : comparisonResult.entrySet()){

	    	if (!((entry.getKey().toString().equalsIgnoreCase("_acl"))||
	    		(entry.getKey().toString().equalsIgnoreCase("_module"))||
	    		(entry.getKey().toString().equalsIgnoreCase("all_emails"))||
	    		(entry.getKey().toString().equalsIgnoreCase("assigned_user_name"))||
	    		(entry.getKey().toString().equalsIgnoreCase("assigned_user_id"))||
	    		(entry.getKey().toString().equalsIgnoreCase("campaign_name"))||	
	    		(entry.getKey().toString().equalsIgnoreCase("contact_name"))||	
	    		(entry.getKey().toString().equalsIgnoreCase("created_by"))||	
	    		(entry.getKey().toString().equalsIgnoreCase("created_by_name"))||
	    		(entry.getKey().toString().equalsIgnoreCase("date_entered"))||	
	    		(entry.getKey().toString().equalsIgnoreCase("date_modified"))||	
	    		(entry.getKey().toString().equalsIgnoreCase("doc_owner"))||	
	    		(entry.getKey().toString().equalsIgnoreCase("email1"))||
	    		(entry.getKey().toString().equalsIgnoreCase("email2"))||
	    		(entry.getKey().toString().equalsIgnoreCase("email_opt_out"))||
	    		(entry.getKey().toString().equalsIgnoreCase("email_addresses_non_primary"))||
	    		(entry.getKey().toString().equalsIgnoreCase("following"))||
	    		(entry.getKey().toString().equalsIgnoreCase("full_name"))||
	    		(entry.getKey().toString().equalsIgnoreCase("ibm_visibility"))||
	    		(entry.getKey().toString().equalsIgnoreCase("id"))||
	    		(entry.getKey().toString().equalsIgnoreCase("invalid_email"))||
	    		(entry.getKey().toString().equalsIgnoreCase("lead_source_values"))||
	    		(entry.getKey().toString().equalsIgnoreCase("modified_by_name"))||
	    		(entry.getKey().toString().equalsIgnoreCase("modified_user_id"))||
	    		(entry.getKey().toString().equalsIgnoreCase("name"))||
	    		(entry.getKey().toString().equalsIgnoreCase("name_backward"))||
	    		(entry.getKey().toString().equalsIgnoreCase("name_forward"))||
	    		(entry.getKey().toString().equalsIgnoreCase("primary_address_country_values"))||
	    		(entry.getKey().toString().equalsIgnoreCase("relate_beans"))||
	    		(entry.getKey().toString().equalsIgnoreCase("report_to_name"))||
	    		(entry.getKey().toString().equalsIgnoreCase("score_c"))||
	    		(entry.getKey().toString().equalsIgnoreCase("status_detail_values"))||
	    		(entry.getKey().toString().equalsIgnoreCase("status_values"))||
	    		(entry.getKey().toString().equalsIgnoreCase("team_count"))||
	    		(entry.getKey().toString().equalsIgnoreCase("trial_end_date_c"))||
	    		(entry.getKey().toString().equalsIgnoreCase("trial_start_date_c"))||
	    		(entry.getKey().toString().equalsIgnoreCase("user_favorites"))||
	    		(entry.getKey().toString().equalsIgnoreCase("webtolead_email_opt_out"))||
	    		(entry.getKey().toString().equalsIgnoreCase("webtolead_invalid_email")))){
	    			//Defect 
	    			if (!((entry.getKey().toString().equalsIgnoreCase("alt_address_street"))||
	    				(entry.getKey().toString().equalsIgnoreCase("alt_address_street_2"))||
	    				(entry.getKey().toString().equalsIgnoreCase("alt_address_street_3"))||
	    				(entry.getKey().toString().equalsIgnoreCase("primary_address_street"))||
	    				(entry.getKey().toString().equalsIgnoreCase("primary_address_street_2"))||
	    				(entry.getKey().toString().equalsIgnoreCase("primary_address_street_3")))){
	    				//Defect
		    			if (!((entry.getKey().toString().equalsIgnoreCase("account_description"))||
			    				(entry.getKey().toString().equalsIgnoreCase("description"))||
			    				(entry.getKey().toString().equalsIgnoreCase("lead_source_description"))||
			    				(entry.getKey().toString().equalsIgnoreCase("status_description")))){
					    	if (!entry.getValue()) {	
						        Assert.assertTrue(false, "Entry incorrect: " + entry.getKey() + ", value: "
							            + entry.getValue());
							}
		    			}
	    				
	    			}
	    	}
	    }
	}

				
			
	    

	private static boolean equal(final Object obj1, final Object obj2){
	    return obj1 == obj2 || (obj1 != null && obj1.equals(obj2));
	}
	
	private Map getMap(String jsonText){
		  JSONParser parser = new JSONParser();
		  ContainerFactory containerFactory = new ContainerFactory(){
		    public List creatArrayContainer() {
		      return new LinkedList();
		    }

		    public Map createObjectContainer() {
		      return new LinkedHashMap();
		    }
		                        
		  };
		    Map json = null;            
		  try{
		    json = (Map)parser.parse(jsonText, containerFactory);
		    Iterator iter = json.entrySet().iterator();
		    while(iter.hasNext()){
		      Map.Entry entry = (Map.Entry)iter.next();
		    }
		  }
		  catch(Exception e){
		    System.out.println(e);
		  }
		 return json;
	}
    
    
	private String getPUTInput(){
		JSONParser parser = new JSONParser();
		try{
			Object obj = parser.parse(new FileReader("test_config/extensions/api/lead/put.json"));
			org.json.simple.JSONObject jsonObject = (org.json.simple.JSONObject) obj;

			return jsonObject.toString();
		}
		catch (Exception e) {
			log.info("Returning empty string as no states could be found");
		}
		return "";    
	}
    
    /**
     * Get the oauth string
     */
	public String getOAuthExtension(){		
		return "oauth?" + clientIDandSecret;
	}
}
