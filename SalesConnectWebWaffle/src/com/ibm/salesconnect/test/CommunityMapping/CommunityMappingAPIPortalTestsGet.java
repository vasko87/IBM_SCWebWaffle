package com.ibm.salesconnect.test.CommunityMapping;

//import java.io.IOException;	// for demo test: Test_getSingleMappingCcmsIdDemo
//import java.sql.SQLException;	// for demo test: Test_getSingleMappingCcmsIdDemo
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
//import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.ApiBaseTest;
import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.API.TestDataHolder;
//import com.ibm.salesconnect.common.DB2Connection;	// for demo test: Test_getSingleMappingCcmsIdDemo
//import com.ibm.salesconnect.common.FileUtility;	// for demo test: Test_getSingleMappingCcmsIdDemo
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.common.HttpUtils;

/**
 * @author kvnlau@ie.ibm.com
 * @date Oct 15, 2014
 */
public class CommunityMappingAPIPortalTestsGet extends ApiBaseTest {
	
	Logger log = LoggerFactory.getLogger(CommunityMappingAPIPortalTestsGet.class);
	@Parameters({ "apiExtension", "applicationName", "APIM", "apim_environment" })
	private CommunityMappingAPIPortalTestsGet(@Optional("collab/communityMappings") String apiExtension,
			@Optional("collabweb_communitymapping_readwrite") String applicationName,
			@Optional("true") String APIM,
			@Optional("development") String environment) {

		super(apiExtension, applicationName, APIM, environment);
	}
	
	private TestDataHolder testData;
	//private String url = "https://w3-dev.api.ibm.com/sales/development";
	private String contentType = "application/json";
	private HashMap<String,Object> cchMappingList = null;
	
	// Access plans
	private String clientIDandSecret_BasicOauth = getClientIdAndSecret("collabweb_communitymapping_basic_oauth");
	private String clientIDandSecret_ReadWrite = getClientIdAndSecret("collabweb_communitymapping_readwrite");
	private String clientIDandSecret_ReadOnly = getClientIdAndSecret("collabweb_communitymapping_readonly");
	private String clientIDandSecret_NoPlan = getClientIdAndSecret("collabweb_no_plan");
	
	/**
	 * get_oauth_token:	ClientId, ClientPwd, (Plan: none)
	 * @author: kvnlau@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"})
	public void Test_getOAuthNoPlan(){ // Plan dependent
		
		log.info("Starting API Test method Test_getOAuthNoPlan");
		try {
			// Populate variable from users.csv file(s).
			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
			
			log.info("Try retrieving OAuth Token (without a plan). Test only valid API portal & not directly via SalesConnect");
			LoginRestAPI loginRestAPI = new LoginRestAPI();
			String responseString = loginRestAPI.getOAuth2TokenViaAPIManager(getApiManagement() + getOauthExtension() + 
																	"?" +clientIDandSecret_NoPlan, 
																	cchFnIdUser.getEmail(), 
																	cchFnIdUser.getPassword(),"401");
			JSONObject jsonResponse = null;
			try {
				//check for a valid JSON response
				jsonResponse = (JSONObject)new JSONParser().parse(responseString);
				
				log.info("Valid JSON returned.");
				System.out.println(responseString);	
				
				String responseCode = (String) jsonResponse.get("httpCode"); 
				String httpMessage = (String) jsonResponse.get("httpMessage");
				String moreInfo = (String) jsonResponse.get("moreInformation");
				
				log.info("Verify no token returned & got expect error messages");
				Assert.assertTrue(responseCode.contains("401"), "Did not get expected response code: 401");
				Assert.assertTrue(httpMessage.contains("Unauthorized"), "Did not get HttpMessage: 'Unauthorized'");
				Assert.assertTrue(moreInfo.contains("Client id not registered."), "Did not get message 'Client id not registered.'");
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			log.info("End test method Test_getOAuthNoPlan");
		}
	}
	
	/**
	 * get_oauth_token:	ClientId, ClientPwd (Plan: Basic)
	 * @author: peterpoliwoda@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"})
	public void Test_getMapMultiBasicOAuth(){ // Plan dependent
		log.info("Starting API Test method Test_getMapMultiBasicOAuth");
		
		try {
			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
			
			log.info("Retrieving OAuth2Token.");		
			String token = getOAuthToken(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), "collabweb_communitymapping_readwrite");
			
			log.info("Token received:" + token);
			
			String headers[]={"OAuth-Token", token};		
			
			log.info("Try get multi mappings with basic plan.");
			String responseString = new HttpUtils().getRequest(getRequestUrl(null, null, clientIDandSecret_BasicOauth),
																headers, "401"); // new expected 401 & not 403. see defect 57771
			log.info("getResponseString: \n" + responseString);
			
			try {
				JSONObject jsonResponse = null;
				
				//check for a valid JSON response
				jsonResponse = (JSONObject)new JSONParser().parse(responseString);
				
				log.info("Valid JSON returned.");
				System.out.println(responseString);	
				
				if (jsonResponse.containsKey("httpMessage") && jsonResponse.containsKey("moreInformation")) {
					String httpMesg = (String) jsonResponse.get("httpMessage");
					String infoMesg = (String) jsonResponse.get("moreInformation");
					
					Assert.assertTrue(httpMesg.equals("Unauthorized"),"Did not return 'Unauthorized' message");
					Assert.assertTrue(infoMesg.contains("Not registered to plan"), "Did not get info mesg: 'Not registered to plan");
				} else {
					Assert.assertTrue(false, "Response is missing expected keys 'error' & 'error_message'");
				}
	
			} catch (ParseException e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
		} finally {
			log.info("End test method Test_getMapMultiBasicOAuth");
		}
	}
	
	/**
	 * @name: get_map_multi
	 * @param:	ClientId, ClientPwd, No Plan
	 * @author: kvnlau@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"})
	public void Test_getMapMultiNoPlan(){ // Plan dependent
		
		log.info("Starting API Test method Test_getMapMultiNoPlan");
		User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
		
		HttpUtils restCalls = new HttpUtils();
		
		log.info("Retrieving OAuth2Token.");		
		String token = getOAuthToken(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), "collabweb_communitymapping_readwrite");
		
		log.info("Token received:" + token);
		
		String headers[]={"OAuth-Token", token};		
		
		log.info("Try get multi mappings with no plan");
		String getResponseString = restCalls.getRequest(getRequestUrl(null, null, clientIDandSecret_NoPlan),
														headers, "401"); // now Expect 401 & not 403. See defect 57771
		
		log.info("getResponseString: \n" + getResponseString);
		
		log.info("End test method Test_getMapMultiNoPlan");
	}
	
	
	public String getOauthExtension(){
		return "oauth";
	}
	
	public String getMappingExtension(){
		return "collab/communityMappings";
	}

	private void createObjects(){
		
	}
	
//    @DataProvider(name="DataProvider")
//    public Object[][] getTestDataCchMappings(){
//    	//Create common objects required by test
//    	this.createObjects();
//    	//Read all lines into an ArrayList of HashMaps
//    	this.addDataFile("test_config/extensions/api/cchMapping.csv");
//    	//Return an array of arrays where each item in the array is a HashMap of parameter values
//    	//Test content
//       return testData.getAllDataRows();
//    }
//       
//    public void addDataFile(String filePath){
//        if(this.testData== null){
//            this.testData = new TestDataHolder();
//        }
//         this.testData.addDataLocation(filePath);
//     }
//    
//    @Test(dataProvider = "DataProvider")
//    public void TaskTest(HashMap<String,Object> parameterValues){
//        Iterator<Map.Entry<String, Object>> it = parameterValues.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry<String, Object> pairs = (Map.Entry<String, Object>)it.next();
//            
//            System.out.println(pairs.getKey());
//            System.out.println(pairs.getValue());       
//        }
//        
//        // copy Hashmap to internal variable for use in other local methods.
//        cchMappingList = parameterValues;
//    }
    
    public Map.Entry<String, Object> get1stCchMapping(HashMap<String,Object> parameterValues){
        Iterator<Map.Entry<String, Object>> it = parameterValues.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> pairs = (Map.Entry<String, Object>)it.next();
            
            System.out.println(pairs.getKey());
            System.out.println(pairs.getValue());  
            
            if (!pairs.equals(null))
            	return (Map.Entry<String, Object>)pairs;
        	}
        	return null;
        // copy Hashmap to internal variable for use in other local methods.
        //cchMappingList = parameterValues;
    }
    
}
