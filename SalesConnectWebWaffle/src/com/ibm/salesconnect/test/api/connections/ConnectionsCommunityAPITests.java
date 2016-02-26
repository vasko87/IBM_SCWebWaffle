package com.ibm.salesconnect.test.api.connections;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
//import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.API.TestDataHolder;
//import com.ibm.salesconnect.API.CommunityMappingRestAPI;
import com.ibm.salesconnect.API.ConnectionsCommunityAPI;
//import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.GC;
//import com.ibm.salesconnect.common.HttpUtils;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;

/**
 * @author kvnlau@ie.ibm.com
 * @date Mar 13, 2015
 */
public class ConnectionsCommunityAPITests extends ProductBaseTest {
	
	Logger log = LoggerFactory.getLogger(ConnectionsCommunityAPI.class);
	
	private TestDataHolder testData;
	//private String url = "https://w3-dev.api.ibm.com/sales/development";
	private String contentType = "text/xml";
	private HashMap<String,Object> cchMappingList = null;
//	private PoolClient guClient = commonClientAllocator.getGroupClient(GC.GuClient,this);
//	private PoolClient gbClient = commonClientAllocator.getGroupClient(GC.GbClient,this);
//	private PoolClient gbClient2 = commonClientAllocator.getGroupClient(GC.GbClient,this);
//	private User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
//	private User nonCchFnIdUser = commonUserAllocator.getGroupUser(GC.nonCchFnIdGroup, this);

	// Access plans
	//private static String clientIDandSecret_BasicOauth = "client_id=1a053415-6763-4b47-bb51-05cb24df8c07&client_secret=N7vS5sU4yT6uS0tB3vY6jS0qJ6jY7wT0nR7sA3wM7mH8qJ0lT2";
	//private static String clientIDandSecret_ReadWrite = "client_id=9bdc8d7c-9896-40c0-98df-5b7a40caaf2b&client_secret=lR6oB5rU4dY4bE1hL0sI4iB7mE0nF6vW3qA8dJ3gL5jO0tH0oR";
	//private static String clientIDandSecret_ReadOnly = "client_id=3c879be7-7122-428d-924f-73dcdb8c9868&client_secret=bC5dX6tX7bY4iC7dX2qD6uA6fU3wI4lT3jO2oP1aR2fW2hH2nO";
	//private static String clientIDandSecret_NoPlan = "client_id=9da4630b-ac6b-4a40-bb58-aa4a2d6153d2&client_secret=aY5pS1nQ0xQ1tJ6dJ8dY1nV3yE6xE4dO6vR6yX8aB6oI4eO8eJ";
	//private static String clientIDandSecret_BasicOauth = "client_id=0960be8e-2128-4559-9f96-2744f7157fe1&client_secret=wP2iX3aP8jO5gV7hR5oJ0nB4cP8cO3tJ1oJ3oT3mY1vB2nD7tA";
	//private static String clientIDandSecret_ReadWrite = "client_id=75592e90-574a-4659-9e74-22044fdb718f&client_secret=S1uM0qL6pF7yW2hI0kD1tB7yE0qE0kR5xJ0wW7iJ8eD2uA7kH6";
	//private static String clientIDandSecret_ReadOnly = "client_id=55cc9274-588b-405a-b6bc-f50ac598268c &client_secret=bC5dX6tX7bY4iC7dX2qD6uA6fU3wI4lT3jO2oP1aR2fW2hH2nO";
	//private static String clientIDandSecret_NoPlan = "client_id=9da4630b-ac6b-4a40-bb58-aa4a2d6153d2&client_secret=aY5pS1nQ0xQ1tJ6dJ8dY1nV3yE6xE4dO6vR6yX8aB6oI4eO8eJ";
	
	// CCH functional ID
	//private static String userEmailCchFnId = "eve25@tst.ibm.com"; //user.getEmail();
	//private static String userPasswordCchFnId = "passw0rd"; //user.getPassword();
	
	// Non CCH function ID
	//private static String userEmailNonCchFnId = "eve29@tst.ibm.com"; //user.getEmail();
	//private static String userPasswordNonCchFnId = "passw0rd"; //user.getPassword();
	

	/**
	 * @Description: Test creates community
	 * @name: Test_createConnectionsCommunity
	 * @author: kvnlau@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"})
	public void Test_createConnectionsCommunity() {
		String responseString = null;
		
		try {
			// Populate variable from users.cvs, clients.csv files.
			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
			
			ConnectionsCommunityAPI connApi= new ConnectionsCommunityAPI();
					
			responseString = connApi.createConnectionsCommunity(cchFnIdUser.getEmail(), 
																cchFnIdUser.getPassword(), 
																getCnxnCommunity());

			log.info("Verify if community was created");
			try {
				if (responseString!=null) {
					log.info("responseString (containing communityID): " + responseString);
				}
				Assert.assertTrue(responseString!=null && responseString.length()>0, "Connections community was not created");
			} catch (Exception e) {
				e.printStackTrace();
				Assert.assertTrue(false, "Parse exception with post response");
			}
		} finally {
//			log.info("Clean up: delete community mapping: '" + communityId + "'");
//			responseString = new CommunityMappingRestAPI().deleteCommunityMapping(getApiManagement() + getMappingExtension(), 
//												token, communityId, null, clientIDandSecret_ReadWrite, "200");
		}
	}
	

	/**
	 * @Description: Test creates community, adds members
	 * @name: Test_addUserConnectionsCommunity	
	 * @author: kvnlau@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"})
	public void Test_addUserConnectionsCommunity(){
		String communityId = null;
		String responseString = null;
		String[] communityMemberList = null;
		
		try {
			// Populate variable from users.cvs, clients.csv files.
			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
			
			ConnectionsCommunityAPI connApi = new ConnectionsCommunityAPI();
			
			log.info("Creating community");
			communityId = connApi.createConnectionsCommunity(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), getCnxnCommunity());
			
			log.info("Adding member: " + connApi.funcIdEmail);
			responseString = connApi.addUserConnectionsCommunity(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), connApi.funcIdEmail, communityId, getCnxnCommunity());

			log.info("Retrieve members of community: " + communityId);
			communityMemberList = connApi.getUsersConnectionsCommunity(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), communityId, getCnxnCommunity());
			
			String[] newMembers = {connApi.funcIdEmail};
			int expectedMatches = newMembers.length;
			int matchCounter = 0;
			
			log.info("verify user(s) are added to community membership");
			for (int index1=0; index1<=newMembers.length-1; index1++) { //
				for (int index2=0; index2<=communityMemberList.length-1; index2++) { //
					if (newMembers[index1].equals(communityMemberList[index2])){
						log.info("Found expected community member: " + newMembers[index1]);
						matchCounter = matchCounter + 1;
					}
				}
			}
			Assert.assertTrue(matchCounter==expectedMatches, "Not all users were added to community membership");		
		} finally {
//			log.info("Clean up: delete community mapping: '" + communityId + "'");
//			responseString = new CommunityMappingRestAPI().deleteCommunityMapping(getApiManagement() + getMappingExtension(), 
//												token, communityId, null, clientIDandSecret_ReadWrite, "200");
		}
	}
	

	/**
	 * @Description: Test creates community, adds members and retrieves the members
	 * @name: Test_getUsersConnectionsCommunity
	 * @author: kvnlau@ie.ibm.com
	 */
	@Test(groups = {"CCHAPI"})
	public void Test_getUsersConnectionsCommunity(){
		String communityId = null;
		String responseString = null;
		String[] communityMemberList = null;
		
		try {
			// Populate variable from users.cvs, clients.csv files.
			User cchFnIdUser = commonUserAllocator.getGroupUser(GC.cchFnIdGroup, this);
			ConnectionsCommunityAPI connApi = new ConnectionsCommunityAPI();
			
			log.info("Creating community");
			communityId = connApi.createConnectionsCommunity(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), getCnxnCommunity());
			
			log.info("Adding member: " + connApi.funcIdEmail);
			responseString = connApi.addUserConnectionsCommunity(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), connApi.funcIdEmail, communityId, getCnxnCommunity());
			
			log.info("Adding member: eve28@tst.ibm.com");
			responseString = connApi.addUserConnectionsCommunity(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), "eve28@tst.ibm.com", communityId, getCnxnCommunity());
			
			log.info("Adding member: eve27@tst.ibm.com");
			responseString = connApi.addUserConnectionsCommunity(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), "eve27@tst.ibm.com", communityId, getCnxnCommunity());
			
			log.info("Retrieve members of community: " + communityId);
			communityMemberList = connApi.getUsersConnectionsCommunity(cchFnIdUser.getEmail(), cchFnIdUser.getPassword(), communityId, getCnxnCommunity());
						
			String[] newMembers = {connApi.funcIdEmail, "eve28@tst.ibm.com", "eve27@tst.ibm.com"};
			int expectedMatches = newMembers.length;
			int matchCounter = 0;
			
			log.info("verify user(s) are added to community membership");
			for (int index1=0; index1<=newMembers.length-1; index1++) {
				for (int index2=0; index2<=communityMemberList.length-1; index2++) {
					if (newMembers[index1].equals(communityMemberList[index2])){
						log.info("Found expected community member: " + newMembers[index1]);
						matchCounter = matchCounter + 1;
					}
				}
			}
			Assert.assertTrue(matchCounter==expectedMatches, "Not all users were added to community membership");

		
		} finally {
//			log.info("Clean up: delete community mapping: '" + communityId + "'");
//			responseString = new CommunityMappingRestAPI().deleteCommunityMapping(getApiManagement() + getMappingExtension(), 
//												token, communityId, null, clientIDandSecret_ReadWrite, "200");
		}
	}
	
	
	public String getOauthExtension(){
		return "oauth";
	}
	
	public String getMappingExtension(){
		return "collab/communityMappings";
	}
	
	public String getCreateCommunityExtension(){
		return "communities/service/atom/communities/my";
	}

	public String getAddCommunityUserExtension(){
		return "communities/service/atom/community/members";
	}
	
	public String getRetrieveCommunityExtension() {
		return "communities/service/atom/community/instance";
	}
	
	private void createObjects(){
		
	}
	
    @DataProvider(name="DataProvider")
    public Object[][] getTestDataCchMappings(){
    	//Create common objects required by test
    	this.createObjects();
    	//Read all lines into an ArrayList of HashMaps
    	//this.addDataFile("test_config/extensions/api/cchMapping.csv");
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
            
            System.out.println(pairs.getKey());
            System.out.println(pairs.getValue());       
        }
        
        // copy Hashmap to internal variable for use in other local methods.
        cchMappingList = parameterValues;
    }
    
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