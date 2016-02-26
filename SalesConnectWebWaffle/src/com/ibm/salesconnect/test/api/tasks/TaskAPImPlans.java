/**
 * 
 */
package com.ibm.salesconnect.test.api.tasks;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.API.TaskRestAPI;
import com.ibm.salesconnect.API.TestDataHolder;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.common.HttpUtils;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.test.mobile.TaskRestAPITests;

/**
 * @author timlehane
 * @date Oct 2, 2014
 */
public class TaskAPImPlans extends ProductBaseTest {
	private static final Logger log = LoggerFactory.getLogger(TaskAPImPlans.class);
    private TestDataHolder testData;
    static GETclientAndsecret callMethTaskAPI = new GETclientAndsecret();
	private static Map<String, String> findclientIDandSecretTaskAPI = callMethTaskAPI.getClientIDAndSecretData();
	private static Map<String, String> findclientSecretTaskAPI = callMethTaskAPI.getClientSecret();
	private static Map<String, String> findclientIDTaskAPI = callMethTaskAPI.getClientID();
	private static String retclientSecret = null;
	private static String retclientID = null;
//	String createPlan = "?client_id=59ed11d5-9272-468d-af0d-c2cbbd35c3c0&client_secret=E2vH1pD3uM5xQ7vW7bJ7vM3nE6kH2pT8oI8rK8rU6lJ8hG5uL2";
//	String basicPlan = "?client_id=0960be8e-2128-4559-9f96-2744f7157fe1&client_secret=wP2iX3aP8jO5gV7hR5oJ0nB4cP8cO3tJ1oJ3oT3mY1vB2nD7tA";
//	String updatePlan = "?client_id=29be852f-a823-4d7b-a1da-b894b784f654&client_secret=D2lF6bS1yX5mC3kI1fS3vT6yC2hX7kP2rA5kD0sG0aL2lA4gL4";
//	String readPlan = "?client_id=55cc9274-588b-405a-b6bc-f50ac598268c&client_secret=fX3mT7dJ5tX4uV2nK8mH6aF4aD2dS8mH0wG2jC2rO2oI7dU2gM";
//	String deletePlan = "?client_id=e579ff68-ae4b-4734-a87c-e7f56e60d64f&client_secret=V8aL8hS5aC2eF3aI5wE0oQ4wN2nB3aN6oD0jS2kT2jQ4hR8tJ5";
//	String CRUPlan = "?client_id=75592e90-574a-4659-9e74-22044fdb718f&client_secret=S1uM0qL6pF7yW2hI0kD1tB7yE0qE0kR5xJ0wW7iJ8eD2uA7kH6";
//	String CBPlan = "?client_id=f4c489fe-c371-4932-a394-c8fea40e94c3&client_secret=yN3dU6xK6fT8pG5tG4uJ1uE4qL8yY3fW2qJ2fX2gJ1hI1vH0bD";
//	String NoPlan = "?client_id=783e09f2-4742-42ea-9acc-6814e90f271b&client_secret=H1yG6yQ5lJ6qV8iL4jT0oP3rP6eO5tD6oU7iR8aL1rH5pY4fH8";
	
	String createPlan = '?' + findclientIDandSecretTaskAPI.get("SC Auto create");
	String basicPlan = '?' + findclientIDandSecretTaskAPI.get("SC Auto Basic");
	String updatePlan = '?' +  findclientIDandSecretTaskAPI.get("SC Auto update");
	String readPlan = '?' + findclientIDandSecretTaskAPI.get("SC Auto read");
	String deletePlan = '?' + findclientIDandSecretTaskAPI.get("SC Auto delete");
	String CRUPlan = '?' + findclientIDandSecretTaskAPI.get("SC Auto create+read+update");
	String CBPlan = '?' + findclientIDandSecretTaskAPI.get("SC Auto create+basic");
	String NoPlan = '?' + findclientIDandSecretTaskAPI.get("SC Auto no plan");
	String taskID = null;
	String linkID = null;
	User user1 = null;
	
	private void createObjects(){
		log.info("Start creating prerequisites");
		user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
		taskID = TaskRestAPITests.createTaskHelper(user1,"Base task",log,baseURL);
		linkID = TaskRestAPITests.createTaskHelper(user1,"Link task",log,baseURL);
	}
	public String getclient_ID()
    {
    	log.info("getting client_ID for SC Auto Basic in TaskAPImPlus.java");
    	retclientID = findclientIDTaskAPI.get("SC Auto Basic");
    	log.info("The client_ID for Auto Basic is ***********************:"+retclientID);
		return retclientID;
    	
    }
    public String getclient_secret()
    {
    	log.info("getting client_Secret for SC Auto Basic in TaskAPImPlus.java");
    	retclientSecret = findclientSecretTaskAPI.get("SC Auto Basic");
    	log.info("The client_secret for Auto Basic is ***********************:"+retclientSecret);
		return retclientSecret;
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
     	this.addDataFile("test_config/extensions/api/TaskAPImPlans.csv");
     	//Return an array of arrays where each item in the array is a HashMap of parameter values
     	//Test content
        return testData.getAllDataRows();
     }
     
     @Test(dataProvider = "DataProvider")
     public void TaskTest(HashMap<String,Object> parameterValues){
     
    	 String plan = null;
    	 String call = null;
    	 String expectedResponse = null;
    	 
         Iterator<Map.Entry<String, Object>> it = parameterValues.entrySet().iterator();
         while (it.hasNext()) {
             Map.Entry<String, Object> pairs = (Map.Entry<String, Object>)it.next();
             
             if(pairs.getKey().equals("plan")){
            	 if (pairs.getValue().toString().equals("createPlan")) {
					plan = createPlan;
				}
            	 else if (pairs.getValue().toString().equals("basicPlan")) {
					plan = basicPlan;
				}
            	 else if (pairs.getValue().toString().equals("readPlan")) {
 					plan = readPlan;
 				}
            	 else if (pairs.getValue().toString().equals("deletePlan")) {
 					plan = deletePlan;
 				}
            	 else if (pairs.getValue().toString().equals("updatePlan")) {
  					plan = updatePlan;
  				}
            	 else if (pairs.getValue().toString().equals("CRUPlan")) {
 					plan = CRUPlan;
 				}
            	 else if (pairs.getValue().toString().equals("CBPlan")) {
 					plan = CBPlan;
 				}
            	 else if (pairs.getValue().toString().equals("NoPlan")) {
 					plan = NoPlan;
 				}
             }
             else if (pairs.getKey().equals("call")) {
				call = pairs.getValue().toString();
             }
             else if (pairs.getKey().equals("expectedResponse")) {
				expectedResponse = pairs.getValue().toString();
             }
			}
			
        if (call.equals("create")) {
			sendCreateRequest(plan, expectedResponse);
		}
        else if (call.equals("basic")) {
        	sendBasicRequest(plan, expectedResponse);
		}
        else if (call.equals("read")) {
			sendGetRequest(plan, expectedResponse);
		}
        else if (call.equals("delete")) {
			sendDeleteRequest(plan, expectedResponse);
		}
        else if (call.equals("update")) {
			sendUpdateRequest(plan, expectedResponse);
		}
     }
	  
    private void sendCreateRequest(String plan, String expectedResponse){		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String headers[]={"OAuth-Token", 
				loginRestAPI.getOAuth2TokenViaAPIManager(getApiManagement() + getOAuthExtension(), user1.getEmail(), user1.getPassword(), "200")};
		
		new HttpUtils().postRequest(getApiManagement() + "tasks/" + taskID + "/link/tasks/" + linkID + plan, 
				headers, "", "application/json", expectedResponse); 
    }
    
    private void sendGetRequest(String plan, String expectedResponse){
		new TaskRestAPI().getTask(getApiManagement() + "tasks/" +taskID + plan, 
				new LoginRestAPI().getOAuth2TokenViaAPIManager(getApiManagement() + getOAuthExtension(), user1.getEmail(), user1.getPassword(), "200")
				, expectedResponse);
    }
    
    private void sendDeleteRequest(String plan, String expectedResponse){
    	String deleteTaskID = null;
    	if (expectedResponse.equals("200")) {
			deleteTaskID = TaskRestAPITests.createTaskHelper(user1,"Base task",log,baseURL);
		}
    	else {
			deleteTaskID = taskID;
		}
    	 
		new TaskRestAPI().deleteTaskAPIm(getApiManagement() + "tasks/" 
				+ deleteTaskID + plan, 
				new LoginRestAPI().getOAuth2TokenViaAPIManager(getApiManagement() + getOAuthExtension(), user1.getEmail(), user1.getPassword(), "200"),
				expectedResponse);
    }
    
    private void sendUpdateRequest(String plan, String expectedResponse){
		String body = "{\"name\":\"Base task\",\"date_due\":\"2013-10-28T15:14:00.000Z\",\"priority\":\"High\",\"status\":\"Not Started\",\"call_type\":\"Close_out_call\"}";

		new TaskRestAPI().editTaskAPIm(getApiManagement() + "tasks/" + taskID + plan, 
				new LoginRestAPI().getOAuth2TokenViaAPIManager(getApiManagement() + getOAuthExtension(), user1.getEmail(), user1.getPassword(), "200"),
				body, expectedResponse);
    }
    
    private void sendBasicRequest(String plan, String expectedResponse){
		String body = "{\"name\":\"Base task\",\"date_due\":\"2013-10-28T15:14:00.000Z\",\"priority\":\"High\",\"status\":\"Not Started\",\"call_type\":\"Close_out_call\"}";
		String headers[]={"OAuth-Token", 
				new LoginRestAPI().getOAuth2TokenViaAPIManager(getApiManagement() + getOAuthExtension(), user1.getEmail(), user1.getPassword(), "200")};
		
		new HttpUtils().postRequest(getApiManagement() + "tasks" + plan, 
				headers, body, "application/json", expectedResponse);
    }
	
	public String getOAuthExtension(){		
		
		// client secret is prepended with '&'
		return "oauth?" + getclient_ID() + getclient_secret();
	}

}
