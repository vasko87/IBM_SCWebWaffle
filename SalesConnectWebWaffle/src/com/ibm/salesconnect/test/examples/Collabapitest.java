/**
 * 
 */
package com.ibm.salesconnect.test.examples;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.API.TestDataHolder;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.common.HttpUtils;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;

/**
 * @author timlehane
 * @date Oct 15, 2014
 */
public class Collabapitest extends ProductBaseTest {
	Logger log = LoggerFactory.getLogger(Collabapitest.class);
	private static String clientIDandSecret = "client_id=9bdc8d7c-9896-40c0-98df-5b7a40caaf2b&client_secret=lR6oB5rU4dY4bE1hL0sI4iB7mE0nF6vW3qA8dJ3gL5jO0tH0oR";
    
	private TestDataHolder testData;
	
	private void createObjects(){
		
	}
	
    @DataProvider(name="DataProvider")
    public Object[][] getTestData(){
    	//Create common objects required by test
    	this.createObjects();
    	//Read all lines into an ArrayList of HashMaps
    	this.addDataFile("test_config/extensions/api/test.csv");
    	//Return an array of arrays where each item in the array is a HashMap of parameter values
    	//Test content
       return testData.getAllDataRows();
    }
    
    public void addDataFile(String filePath){
        if(this.testData== null){
            this.testData = new TestDataHolder();
        }
         this.testData.addDataLocation(filePath);
     }
    
    @Test(dataProvider = "DataProvider")
    public void TaskTest(HashMap<String,Object> parameterValues){

        Iterator<Map.Entry<String, Object>> it = parameterValues.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> pairs = (Map.Entry<String, Object>)it.next();
            
            if (pairs.getKey().equals("column1")) {
				String column1=(String) pairs.getValue();
			}
            
            
            System.out.println(pairs.getKey());
            System.out.println(pairs.getValue());
            
 
            
        }
    }
    
    
	//@Test
	public void Test_getsinglemapping(){
		
		User user = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
		
		log.info("Retrieving OAuth2Token.");		
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2TokenViaAPIManager(getApiManagement() + getOauthExtension() + "?" +clientIDandSecret, user.getEmail(), user.getPassword(),"200");
		
		String headers[]={"OAuth-Token", token};
		
		HttpUtils restCalls = new HttpUtils();
		 String getResponseString = restCalls.getRequest(getApiManagement() + getMappingExtension() + "?" + clientIDandSecret, headers, "200");
		 
		try {
			//check for a valid JSON response
			new JSONParser().parse(getResponseString);
		} catch (ParseException e) {
			e.printStackTrace();
			Assert.assertTrue(false, "Parse exception with post response");
		}
		
		log.info("Valid JSON returned.");
		System.out.println(getResponseString);	
		
	}
	
	
	public String getOauthExtension(){
		return "oauth";
	}
	
	public String getMappingExtension(){
		return "collab/communityMappings";
	}
	
}
