/* package com.ibm.salesconnect.test.examples;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.internal.Base64Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.APIUtilities;
import com.ibm.salesconnect.API.CollabWebAPI;
import com.ibm.salesconnect.API.SugarAPI;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.common.HttpUtils;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.objects.Client;


public class CreateSiteAddUsersJoinCommunity extends ProductBaseTest {

	Logger log = LoggerFactory.getLogger(CreateSiteAddUsersJoinCommunity.class);

	User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,"CreateSiteAddUsersJoinCommunity");
	User user2 = commonUserAllocator.getGroupUser(GC.noGBL_SearchGroup,"CreateSiteAddUsersJoinCommunity");
	User user3 = commonUserAllocator.getUser("CreateSiteAddUsersJoinCommunity");
	User user4 = commonUserAllocator.getUser("CreateSiteAddUsersJoinCommunity");
	User user5 = commonUserAllocator.getUser("CreateSiteAddUsersJoinCommunity");
//	User user6 = commonUserAllocator.getGroupUser(GC.noMemUserGroup,"CreateSiteAddUsersJoinCommunity");
	String clientID1 = commonClientAllocator.getGroupClient(GC.DC,"CreateSiteAddUsersJoinCommunity").getCCMS_ID();
	String clientID2 = commonClientAllocator.getGroupClient(GC.DC,"CreateSiteAddUsersJoinCommunity").getCCMS_ID();
	String clientID3 = commonClientAllocator.getGroupClient(GC.DC,"CreateSiteAddUsersJoinCommunity").getCCMS_ID();
	
	@Test
	public void Test_CreateSite(){
		log.info("Start of test method Test_CreateSite");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
		SugarAPI sugarAPI = new SugarAPI();
		String URL = testConfig.getBrowserURL();
		Client dc = new Client();
		Client site = new Client();
		
		String[] teamMembers = getMultipleUsers(3, this);
		String[] team = new String[4];
		team[0]=teamMembers[0];
		team[1]=teamMembers[1];
		team[2]=teamMembers[2];
		team[3]=user2.getEmail();
				
		String [] clients ={clientID1, clientID2, clientID3};
		for( String client : clients ) {
        	  log.info("Create Site for Client " + client);
	  		dc = sugarAPI.selectClient(client, URL, user1.getEmail(), user1.getPassword());
	  		site = sugarAPI.createSite(URL, dc, team, user1.getEmail(), user1.getPassword());
	  		System.out.println("Client Site ID: " + site.sClientID);
	      }
	      
		log.info("End of test method Test_CreateSite");
	}

	@Test
	public void Test_JoinCommunityViaAPI(){
		log.info("Start of test to join commujnity via api");
		
		SugarAPI sugarAPI = new SugarAPI();
		CollabWebAPI collabWebAPI = new CollabWebAPI();
		Base64Encoder b64e = new Base64Encoder();
		String userPass = user1.getEmail()+":"+user1.getPassword();
		
		log.info("Getting OAuthToken");
		String token = collabWebAPI.getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword());
		String headers[]={"OAuth-Token",token};

		String [] clients ={clientID1, clientID2, clientID3};
	      for( String client : clients ) {
	    	log.info("Adding Users to Community for Client "+client);
	  		
	    	log.info("Getting session ID");
	  		String cookieHeaders[] = {"Cookie","PHPSESSID="+ sugarAPI.getSessionID(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};
	  		String beanId=collabWebAPI.getbeanIDfromClientID(testConfig.getBrowserURL(), client, headers);
	  		String communityId=collabWebAPI.getcommunityIDFromBeanID(testConfig.getBrowserURL(), beanId,GC.accountsModule, cookieHeaders);
	  		
	  		String Connheaders[]={"Authorization","Basic "+b64e.encode(userPass.getBytes())};
	  		
	  		String [] users ={user1.getEmail(),user2.getEmail(),user3.getEmail(),user4.getEmail(),user5.getEmail()};
	  	    for( String user : users ) {
	  	    	log.info("Checking if user "+user+" has joined the community");
	  	    	Boolean check = collabWebAPI.checkCommunityMembership(testConfig.getParameter(GC.cxnURL), communityId, Connheaders, user);
	  	  		System.out.println(check);
	  	    }

	  	    // Leaving in for debug purposes 
			//	  	    for( String user : users ) {
			//	  	    	log.info(user+" Leaving Community");
			//	  	    	collabWebAPI.leaveCommunity(testConfig.getParameter(GC.cxnURL), communityId, Connheaders, user );
			//	  	    }
			//	  	  
			//	  	    for( String user : users ) {
			//	  	    	log.info("Checking again to see if user "+user+" has joined the community");
			//  	    	  	Boolean check3 = collabWebAPI.checkCommunityMembership(testConfig.getParameter(GC.cxnURL), communityId, Connheaders, user);
			//  	  			System.out.println(check3);
			//	  	    }
	  	  
	  	    log.info("Running Join Community API");  
	        Boolean check2 = collabWebAPI.joinCommunity(testConfig.getBrowserURL(), beanId, GC.accountsModule, cookieHeaders);	
	  		System.out.println(check2);  
	  		
	  	    for( String user : users ) {
	  	    	log.info("Checking again to see if user "+user+" has joined the community");
	  	    	Boolean check3 = collabWebAPI.checkCommunityMembership(testConfig.getParameter(GC.cxnURL), communityId, Connheaders, user);
	  	  	  	System.out.println(check3);
	  	    }

	      }

	}

	@Test
	public void Test_FirstTimeLogin(){
		log.info("Start of test method Test_FirstTimeLogin");
		
		User [] users = {user1,user2, user3, user4, user5};
		for( User user : users ) {
  	    	log.info("Logging in as "+user.getEmail());
			Dashboard dashboard = launchWithLogin(user);
			dashboard.isPageLoaded();
			exec.close();
  	    }
	      
		log.info("End of test method Test_FirstTimeLogin");
	}
	
	
	@Test
	public void Test_Create201Sites(){
		log.info("Start of test method Test_CreateSite");
		SugarAPI sugarAPI = new SugarAPI();
		String URL = testConfig.getBrowserURL();
		Client dc = new Client();
		dc.sBeanID = "8f2b0316-bfa5-209b-a663-5175f6911f82";
		dc.sClientName ="DC022DQV";
		String team[] = {};
		for( int i =0; i < 201 ;i++) {
			Client site = new Client();
	  		site = sugarAPI.createSite(URL, dc, team, "ie01@tst.ibm.com", "passw0rd");
	  		System.out.println("Client Site ID: " + site.sClientID);
	      }
	      
		log.info("End of test method Test_CreateSite");
	}
	
	@Test
	public void Test_FollowSites201Sites(){
		log.info("Getting OAuthToken");
		CollabWebAPI collabWebAPI = new CollabWebAPI();
		String token = collabWebAPI.getOAuth2Token(testConfig.getBrowserURL(), "peterpol13@tst.ibm.com", "passw0rd");
		
		log.info("Getting user bean");
		APIUtilities apiUtils = new APIUtilities();
		String userBean = apiUtils.getUserBeanIDFromEmail(testConfig.getBrowserURL(), "peterpol13@tst.ibm.com", "passw0rd");
		
		String headers[]={"OAuth-Token",token};
		log.info("Get list of 201 sites where user is on client team");
		HttpUtils httpUtils = new HttpUtils();
		String siteList = httpUtils.getRequest("https://svt5lb01a.rtp.raleigh.ibm.com/sales/salesconnect/rest/v10/Accounts?filter[0][created_by]="+userBean+"&fields=id,created_by&max_num=201", headers);
		
		log.info("Get cookie header");
		SugarAPI sugarAPI = new SugarAPI();
		String cookieHeaders[] = {"Cookie","PHPSESSID="+ sugarAPI.getSessionID(testConfig.getBrowserURL(), "peterpol13@tst.ibm.com", "passw0rd")};

		log.info("Parsing client list to extract ids");
		JSONObject postResponse = null;
		try {
			postResponse = (JSONObject)new JSONParser().parse(siteList);
		} catch (ParseException e5) {
			e5.printStackTrace();
		}
		JSONArray jsonArray = null;
		try {
			jsonArray = (JSONArray) postResponse.get("records");
		} catch (Exception e) {
			log.info("Token was not returned as expected");
			e.printStackTrace();
		}
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			list.add((String) jsonObject.get("id"));
		}
		
		log.info("Follow sites");
		for (String string : list) {
			String post = httpUtils.postRequest("https://svt5lb01a.rtp.raleigh.ibm.com/sales/salesconnect/custom/service/IBMConnections/rest.php",cookieHeaders, "method=startFollow&input_type=json&response_type=json&rest_data={\"module\":\"Accounts\",\"beanId\":\"" + string +"\"}","application/x-www-form-urlencoded");
		}
	}
	
}
*/