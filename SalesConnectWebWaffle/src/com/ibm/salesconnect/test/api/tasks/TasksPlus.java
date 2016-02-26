/**
 * 
 */
package com.ibm.salesconnect.test.api.tasks;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
import com.ibm.salesconnect.API.CollabWebAPI;
import com.ibm.salesconnect.API.ContactRestAPI;
import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.API.TaskRestAPI;
import com.ibm.salesconnect.API.TestDataHolder;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.common.HttpUtils;
import com.ibm.salesconnect.test.mobile.TaskRestAPITests;

/**
 * @author timlehane
 * @date Oct 23, 2014
 */
public class TasksPlus extends ApiBaseTest {
	private static final Logger log = LoggerFactory.getLogger(TasksPlus.class);
	
	@Parameters({ "apiExtension", "applicationName", "APIM", "apim_environment" })
	private TasksPlus(@Optional("tasksplus") String apiExtension,
			@Optional("SC Auto tasksplus") String applicationName,
			@Optional("true") String APIM,
			@Optional("development") String environment) {

		super(apiExtension, applicationName, APIM, environment);

	}

	
    private TestDataHolder testData;
    int rand = new Random().nextInt(100000);
	private String contactBeanID = null;
	private String CIContactID = null;
	private String BasetaskID = null;
	private String LinkAccounttaskID = null;
	private String LinkContacttaskID = null;
	private String LinkAdditionalUsertaskID = null;
	private String DeletedTaskID = null;
	private String clientBeanID = null;
	private String assignedUserID = null;
	private String userID = null;
	private String cnum = "-0505K754";
	private String clientID = null;

	private void createObjects(){
		log.info("Start creating prerequisites");

		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
		User user2 = commonUserAllocator.getUser(this);
		String baseURL = testConfig.getBrowserURL();
		userID = new APIUtilities().getUserBeanIDFromEmail(baseURL, user2.getEmail(), user2.getPassword());
		PoolClient poolClient= commonClientAllocator.getGroupClient(GC.SC,this);

		
		log.info("Getting client");
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user1.getEmail(), user1.getPassword());
		String headers[] = {"OAuth-Token", OAuthToken};
		clientID = poolClient.getCCMS_ID();
		clientBeanID = new CollabWebAPI().getbeanIDfromClientID(baseURL, clientID, headers );
		
		log.info("Creating contact");
		assignedUserID = new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(), user1.getEmail(), 
				user1.getPassword());	
		CIContactID = new ContactRestAPI().createContactreturnCIContactID(testConfig.getBrowserURL(), headers, 
				"ContactFirst", "ContactLast", "US", "(555) 555-5555", assignedUserID);
		
		log.info("Creating tasks");
		BasetaskID = TaskRestAPITests.createTaskHelper(user1,"Base task",log,baseURL);
		LinkAccounttaskID = TaskRestAPITests.createTaskHelper(user1,"Account Link task",log,baseURL);
		LinkContacttaskID = TaskRestAPITests.createTaskHelper(user1,"Contact Link task",log,baseURL);
		LinkAdditionalUsertaskID = TaskRestAPITests.createTaskHelper(user1,"User Link task",log,baseURL);
		
		DeletedTaskID = TaskRestAPITests.createTaskHelper(user1,"Deleted Task",log,baseURL);
		new TaskRestAPI().deleteTask(testConfig.getBrowserURL(), OAuthToken, DeletedTaskID);
		
		log.info("Finish creating prerequisites");
	}
	
    public void addDataFile(String filePath){
       if(this.testData== null){
           this.testData = new TestDataHolder();
       }
        this.testData.addDataLocation(filePath);
    }
     
    
    @DataProvider(name="DataProvider")
    public Object[][] getTestData(){
    	//Create common objects required by test
    	this.createObjects();
    	//Read all lines into an ArrayList of HashMaps
    	this.addDataFile("test_config/extensions/api/TasksPlus.csv");
    	//Return an array of arrays where each item in the array is a HashMap of parameter values
    	//Test content
       return testData.getAllDataRows();
    }
    
    @Test(dataProvider = "DataProvider")
    public void TaskTest(HashMap<String,Object> parameterValues){
		log.info("Start test.");
		String expectedResponseCode = "200";
		String recordID = null;
		String link = null;
		String link_id = null;
		String urlExtension = "";
		String linked_item = "";
		String id_field = "";
		String field_name = "";

        Iterator<Map.Entry<String, Object>> it = parameterValues.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> pairs = (Map.Entry<String, Object>)it.next();
            
            if(pairs.getKey().equals("expected_response")){
            	expectedResponseCode=pairs.getValue().toString();
            	it.remove();
            }
            else if (pairs.getKey().equals("TC_Name")) {
				log.info("This is test " + pairs.getValue().toString());
			}
            else if (pairs.getKey().equals("record_id")) {
            	recordID = getRecordID(pairs.getValue().toString());
				linked_item = getlinkedItem(pairs.getValue().toString());	
            }
            else if (pairs.getKey().equals("link")) {
				if (pairs.getValue().equals("0")) {
					log.info("Leaving link section of url blank as csv value set to 0");
					link = "0";
				}
				else {
					link = pairs.getValue().toString();
				}
			}
            else if (pairs.getKey().equals("link_id")) {
				link_id = getlinkID(pairs.getValue().toString());
			}
            else if (pairs.getKey().equals("id_field")) {
            	id_field = "id_field";
            }
            else if (pairs.getKey().equals("field_name")) {
            	field_name = pairs.getValue().toString();
            }
        }
        
        if (!recordID.equals("0")) {
			urlExtension+= "/" + recordID + "/";
		}
        else {
			urlExtension+= "/";
		}
        
        if (!link.equals("0")) {
			urlExtension+="link/" + link + "/";
		}
        else {
        	if (link_id.equals("0")) {	
			}
        	else {
            	urlExtension+="link";
			}
        }
        
        if (!link_id.equals("0")){
        	urlExtension+= link_id;
        }
        else if (link_id.equals("0")) {
			
		}
        
        if (id_field.equals("id_field")){
        	if (field_name.equals("")) {
        		urlExtension+= "/id_field/";
			} else {
				urlExtension+= "/id_field/" + field_name;
			}
		} 
        
		log.info("Getting user.");		
		User user = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
		
		log.info("Sending POST request");
		String jsonString = sendRequest(user.getEmail(), user.getPassword(), getApiManagement() + getTaskExtension() + urlExtension, expectedResponseCode);
		log.info("Checking response");
		if (!expectedResponseCode.equals("200")) {
			//If expected fail do not parse response, response code already checked 
			log.info("Task creation failed as expected");
		} else {
			if (!link_id.equals("0")) {
				if (link.equals("accounts")) {
					link_id = clientBeanID;
				}
				log.info("linked item id is not blank so checking that");
				Assert.assertTrue(new APIUtilities().checkIfValuePresentInJson(jsonString, "id", link_id), "ID: " + link_id + " was not returned correctly");
			}
			else {
				if (recordID.equals("0")) {
					log.info("Getting all tasks");
					int count = new APIUtilities().getKeyCountInJsonString(jsonString, "id");
					log.info("id count in result" + new APIUtilities().getKeyCountInJsonString(jsonString, "id"));
					Assert.assertTrue(count >=1, "Count is less than 1");
				}
				else {
					if (link.equals("0")) {
						Assert.assertTrue(new APIUtilities().checkIfValuePresentInJson(jsonString, "id", recordID), "link_id is blank, link is blank, checking record_id: " + recordID);
					}
					else {
						log.info("linked item id is blank so checking linked_item");
						Assert.assertTrue(new APIUtilities().checkIfValuePresentInJson(jsonString, "id", linked_item), "related: " + linked_item + " was not returned correctly");
					}
				}
			}
		}
    }
    
    @Test
    public void CreateTaskTasksplus(){
    	
		log.info("Getting user.");		
		User user = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user.getEmail(), user.getPassword());
		String headers[] = {"OAuth-Token", OAuthToken};
		log.info("Creating contact");
		String assignedUserID = new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(), user.getEmail(), 
				user.getPassword());	
		String CIContactID = new ContactRestAPI().createContactreturnCIContactID(testConfig.getBrowserURL(), headers, 
				"ContactFirst", "ContactLast", "US", "(555) 555-5555", assignedUserID);
		String taskID = TaskRestAPITests.createTaskHelper(user,"Base task",log,baseURL);
		String body ="{\"ext_tasks_dropdown_10_c\":\"India\",\"priority\":\"Medium\",\"ext_tasks_textfield_11_c\":\"lllllll\",\"ext_tasks_datetime_6_c\":\"2015-03-21T00:00:00.000Z\",\"ext_tasks_dropdown_18_c\":\"prospect\",\"name\":\"New BlueMix Trial Activation Task\",\"contactinfo\":{\"contactcountrycode\":\"US\"},\"status\":\"Not Started\",\"related_to_c\":{\"id_field\":\"ext_ref_id1_c\",\"related_type\":\"Contacts\",\"related_countrycode\":\"US\",\"related_id\":\"55U-2IZ6GGV\"},\"employee_cnum\":\"799707897\",\"ext_tasks_datetime_10_c\":\"2015-02-19T00:00:00.000Z\",\"call_type\":\"Bluemix_New_Trial_Activation\",\"description\":\"BlueMix Task Creation request from MAT\"}";
		//String body = "{\"ext_tasks_dropdown_10_c\":\"UnitedStates\",\"priority\":\"Medium\",\"ext_tasks_textfield_11_c\":\"500312194\",\"ext_tasks_datetime_6_c\":\"2015-03-21T00:00:00.000Z\",\"ext_tasks_dropdown_18_c\":\"prospect\",\"name\":\"New BlueMix Trial Activation Task\",\"contactinfo\":{\"[contactcountrycode\":\"US]\"},\"status\":\"Not Started\",\"related_to_c\":{\"id_field\":\"ext_ref_id1_c\",\"related_type\":\"Contacts\",\"related_countrycode\":\"US\",\"related_id\":\"2-ITQDEI\"},\"employee_cnum\":\"4G6600897\",\"ext_tasks_datetime_10_c\":\"2015-02-19T00:00:00.000Z\",\"call_type\":\"Bluemix_New_Trial_Activation\",\"description\":\"BlueMix Task Creation request from MAT\"}";  
		log.info("Sending POST request");
		//sendRequest(user.getEmail(), user.getPassword(), 
		//		getApiManagement() + getTaskExtension(), body, "200");
		sendRequest(user.getEmail(), user.getPassword(), 
				getApiManagement() + getTaskExtension() + "/" +taskID + "/link/contacts/2-ITQDEI/id_field/ext_ref_id1_c", body, "200");

    }
    
    @Test
    public void createTaskWithCountryCode(){
    	
		log.info("Getting user.");		
		User user = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String OAuthToken = loginRestAPI.getOAuth2Token(baseURL, user.getEmail(), user.getPassword());
		String headers[] = {"OAuth-Token", OAuthToken};
		
		log.info("Creating contact");
		String assignedUserID = new APIUtilities().getUserBeanIDFromEmail(testConfig.getBrowserURL(), user.getEmail(), 
				user.getPassword());	
		String CIContactID = new ContactRestAPI().createContactreturnCIContactID(testConfig.getBrowserURL(), headers, 
				"ContactFirst", "ContactLast", "US", "(555) 555-5555", assignedUserID);
		
		//String taskID = TaskRestAPITests.createTaskHelper(user,"Base task",log,baseURL);
//		String body ="{\"ext_tasks_dropdown_10_c\":\"India\",\"priority\":\"Medium\",\"ext_tasks_textfield_11_c\":\"lllllll\",\"ext_tasks_datetime_6_c\":\"2015-03-21T00:00:00.000Z\",\"ext_tasks_dropdown_18_c\":\"prospect\",\"name\":\"New BlueMix Trial Activation Task\",\"contactinfo\":{\"contactcountrycode\":\"US\"},\"status\":\"Not Started\",\"related_to_c\":{\"id_field\":\"ext_ref_id1_c\",\"related_type\":\"Contacts\",\"related_countrycode\":\"US\",\"related_id\":\"55U-2IZ6GGV\"},\"employee_cnum\":\"799707897\",\"ext_tasks_datetime_10_c\":\"2015-02-19T00:00:00.000Z\",\"call_type\":\"Bluemix_New_Trial_Activation\",\"description\":\"BlueMix Task Creation request from MAT\"}";
		JSONObject related1 = new JSONObject();
		related1.put("related_id", CIContactID);
		related1.put("related_type", "Contacts");
		related1.put("id_field", "ext_ref_id1_c");
		related1.put("related_countrycode", "US");
		related1.put("related_name", "ContactFirst ContactLast");
		JSONArray relatedToC = new JSONArray();
		relatedToC.add(related1);
		JSONObject jsonBody = new JSONObject();
		jsonBody.put("name", "test");
		jsonBody.put("description", "20150408 test1");
		jsonBody.put("status", "In Progress");
		jsonBody.put("date_due", "2015-12-16T17:00:51.786Z");
		jsonBody.put("priority", "High");
		jsonBody.put("related_to_c", relatedToC);

		log.info("Sending POST request");
		TaskRestAPI taskRestAPI = new TaskRestAPI();
		String jsonString = taskRestAPI.postTask(getRequestUrl("", null), getOAuthToken(user), jsonBody.toString(), "200");
		
		Assert.assertTrue(new APIUtilities().checkIfValuePresentInJson(jsonString, "related_id", CIContactID),"Contact id is not present in the post response returned");

    }
    
    
    
    
	private String getRecordID(String recordType){
    	if (recordType.equals("0")) {
    		log.info("No record id specified");
    		return  "0";
		}
    	else if (recordType.equals("validtask")) {
    		log.info("Setting record_id to valid value");
			return BasetaskID;
		}
    	else if (recordType.equals("accounttask")) {
			log.info("Setting record_id to deleted task");
			return LinkAccounttaskID;
		}
    	else if (recordType.equals("contacttask")) {
			log.info("Setting record_id to deleted task");
			return LinkContacttaskID;
		}
    	else if (recordType.equals("usertask")) {
			log.info("Setting record_id to deleted task");
			return LinkAdditionalUsertaskID;
		}
    	else if (recordType.equals("casetask")) {
			log.info("Setting record_id to deleted task");
			return BasetaskID.toUpperCase();
		}
    	else if (recordType.equals("invalidtask")) {
    		log.info("Setting record_id to invalid value");
    		return "InvalidTaskID";
		}
    	else if (recordType.equals("deletedtask")) {
    		log.info("Setting record_id to invalid value");
    		return DeletedTaskID;
		}
    	else {
			Assert.assertTrue(false, "record_id field in csv file does not contain a valid value was " + recordType);
			return "fail";
		}
	}
	
	private String getlinkID(String linked_id){
    	if (linked_id.equals("0")) {
    		log.info("No link id specified");
    		return  "0";
		}
    	else if (linked_id.equals("linkeduserid")) {
    		log.info("Setting linked_id to user id");
    		return userID;
		}
    	else if (linked_id.equals("linkedaccountid")) {
			log.info("Setting linked_id to clientBeanID");
			return clientBeanID;
		}
    	else if (linked_id.equals("linkedcontactid")) {
			log.info("Setting linked_id to contactBeanID");
			return contactBeanID;
		}
    	else if (linked_id.equals("linkedcnum")) {
			log.info("Setting linked_id to cnum");
			return cnum;
		}
    	else if (linked_id.equals("linkedccmsID")) {
			log.info("Setting linked_id to ccmsID");
			return clientID;
		}
    	else if (linked_id.equals("linkedCIcontactid")) {
			log.info("Setting linked_id to CIcontactID");
			return CIContactID;
		}
    	else {
			log.info("linked_id field in csv file does not contain a valid value was " + linked_id);
			Assert.assertTrue(false);
			return "fail";
		}
	}
	
	private String getlinkedItem(String itemType){
		if (itemType.equals("accounttask")) {
			return clientBeanID;
		}
    	else if (itemType.equals("contacttask")) {
			return contactBeanID;
		}
    	else if (itemType.equals("usertask")) {
			return userID;
		}
    	else {
			return "getLinkedItemFailed";
		}
	}
	

	public String getOAuthExtension(){		
		return "oauth?" + getclientIDandSecret();
	}
	
	public String getTaskExtension(){		
		return apiExtension;
	}
    
    private String sendRequest(String userEmail, String userPassword, String url, String expectedResponse){
		log.info("Retrieving OAuth2Token.");		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String headers[]={"OAuth-Token", loginRestAPI.getOAuth2TokenViaAPIManager(getApiManagement() + getOAuthExtension(), userEmail, userPassword, "200")};
		
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + "?" + getclientIDandSecret(), 
				headers, "", "application/json", expectedResponse);
		 
		log.info("Response String "+ postResponseString);
		try {
			//check for a valid JSON response
			new JSONParser().parse(postResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
			Assert.assertTrue(false, "Parse exception with post response");
		}
		
		return postResponseString;
    }
    
    private String sendRequest(String userEmail, String userPassword, String url, String body, String expectedResponse){
		log.info("Retrieving OAuth2Token.");		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String headers[]={"OAuth-Token", loginRestAPI.getOAuth2TokenViaAPIManager(getApiManagement() + getOAuthExtension(), userEmail, userPassword, "200")};
		
		HttpUtils restCalls = new HttpUtils();
		String postResponseString = restCalls.postRequest(url + "?" + getclientIDandSecret(), 
				headers, body, "application/json", expectedResponse);
		 
		log.info("Response String "+ postResponseString);
		try {
			//check for a valid JSON response
			new JSONParser().parse(postResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
			Assert.assertTrue(false, "Parse exception with post response");
		}
		
		return postResponseString;
    }
}
