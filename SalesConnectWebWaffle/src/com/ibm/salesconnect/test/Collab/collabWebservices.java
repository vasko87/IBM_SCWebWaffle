/**
 * 
 */
package com.ibm.salesconnect.test.Collab;

import java.io.IOException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.internal.Base64Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.CollabWebAPI;
import com.ibm.salesconnect.API.SugarAPI;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;

/**
 * @author timlehane
 * @date Jul 29, 2013
 */
public class collabWebservices extends ProductBaseTest {
	Logger log = LoggerFactory.getLogger(collabWebservices.class);

	@Test(groups = {"LCAPI","Collab", "LC"})
	public void Test_s30329ObtainOAuth2Token(){
		log.info("Start test method Test_s30329ObtainOAuth2Token");
		
		log.info("Getting users");
		User user1 = commonUserAllocator.getUser(this);
		
		log.info("Getting OAuthToken");
		CollabWebAPI collabWebAPI = new CollabWebAPI();
		String token = collabWebAPI.getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword());

		if (token.equalsIgnoreCase("false")) {
			Assert.assertTrue(false, "OauthToken not returned");
		}
		
		log.info("End test method Test_s30329ObtainOAuth2Token");
	}
	
	@Test(groups = {"LCAPI","LC"})
	public void Test_s30329ObtainOAuth2TokenWithIncorrectPassword(){
		log.info("Start test method Test_s30329ObtainOAuth2TokenWithIncorrectPassword");
		
		log.info("Getting users");
		User user1 = commonUserAllocator.getUser(this);
		
		log.info("Getting OAuthToken");
		CollabWebAPI collabWebAPI = new CollabWebAPI();
		collabWebAPI.getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), "wrong","401");
		
		log.info("End test method Test_s30329ObtainOAuth2TokenWithIncorrectPassword");
	}
	
	@Test(groups = {"LCAPI","LC"})
	public void Test_s30329ObtainOAuth2TokenWithIncorrectUserName(){
		log.info("Start test method Test_s30329ObtainOAuth2TokenWithIncorrectUserName");
		
		log.info("Getting OAuthToken");
		CollabWebAPI collabWebAPI = new CollabWebAPI();
		collabWebAPI.getOAuth2Token(testConfig.getBrowserURL(), "ie01@tst.ibm.com", "wrong","401");

		log.info("End test method Test_s30329ObtainOAuth2TokenWithIncorrectUserName");
	}
	
	@Test(groups = {"LCAPI","LC"})
	public void Test_s30329CheckCommunityStatusForClient(){
		log.info("Start test method Test_s30329CheckCommunityStatusForClient");

		log.info("Getting user");
		User user1 = commonUserAllocator.getUser(this);
		
		log.info("Getting OAuthToken");
		CollabWebAPI collabWebAPI = new CollabWebAPI();
		String token = collabWebAPI.getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword());
		String headers[]={"OAuth-Token",token};
		
		log.info("Getting community status");
		Object subscribedCommunities=collabWebAPI.checkSubscribeCommunityStatus
		(testConfig.getBrowserURL(), headers, commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID(),
				GC.accountsModule);

		if ((Boolean) subscribedCommunities) {
			log.info("Community status was returned, GUC is following Client");
		}
		else {
			log.info("Community status was returned, GUC is not following Client");
		}
		
		log.info("End test method Test_s30329CheckCommunityStatusForClient");
	}
	
	@Test(groups = {"LCAPI","LC"})
	public void Test_s30329CheckCommunityStatusForOppty(){
		log.info("Start test method Test_s30329CheckCommunityStatusForOppty");

		log.info("Getting user, client and creating contact and oppty");
		User user1 = commonUserAllocator.getUser(this);
		String clientID = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
		String contactID = "22SC-" + new Random().nextInt(99999);
		String opptyID = null;
		
		SugarAPI sugarAPI = new SugarAPI();
		sugarAPI.createContact(testConfig.getBrowserURL(), contactID, clientID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");
		opptyID = sugarAPI.createOppty(testConfig.getBrowserURL(), "", contactID, clientID, user1.getEmail(), user1.getPassword(), GC.emptyArray);
		
		log.info("Getting OAuthToken");
		CollabWebAPI collabWebAPI = new CollabWebAPI();
		String headers[]={"OAuth-Token",collabWebAPI.getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};
		
		log.info("Getting community status");
		Object subscribedCommunities=collabWebAPI.checkSubscribeCommunityStatus
		(testConfig.getBrowserURL(), headers, opptyID,
				GC.opptysModule);

		if ((Boolean) subscribedCommunities) {
			log.info("Community status was returned, GUC is following Opportunity");
		}
		else {
			log.info("Community status was returned, GUC is not following Opportunity");
		}
		
		log.info("Remove the oppty created for this test");
		sugarAPI.deleteOpptySOAP(testConfig.getBrowserURL(), opptyID, user1.getEmail(), user1.getPassword());
		
		log.info("End test method Test_s30329CheckCommunityStatusForOppty");
	}
	
	@Test(groups = {"LCAPI","LC"})
	public void Test_s30329CheckCommunityStatusForDifferentClient(){
		log.info("Start test method Test_s30329CheckCommunityStatusForDifferentClient");

		log.info("Getting user");
		User user1 = commonUserAllocator.getUser(this);
		String clientID = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
		String clientID2 = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
		
		log.info("Getting OAuthToken");
		CollabWebAPI collabWebAPI = new CollabWebAPI();
		SugarAPI sugarAPI = new SugarAPI();
		String token = collabWebAPI.getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword());
		String headers[]={"OAuth-Token",token};
		
		log.info("Getting session ID");
		String cookieHeaders[] = {"Cookie","PHPSESSID="+ sugarAPI.getSessionID(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};
		
		log.info("Getting community ID for second client");
		String communityID2=collabWebAPI.getcommunityIDFromBeanID(testConfig.getBrowserURL(), collabWebAPI.getbeanIDfromClientID(testConfig.getBrowserURL(), clientID2, headers),GC.accountsModule, cookieHeaders);
		
		log.info("Getting community status");
		Object subscribedCommunities=collabWebAPI.checkSubscribeCommunityStatus
		(testConfig.getBrowserURL(), headers, communityID2, clientID,
				GC.accountsModule);

		if ((Boolean) subscribedCommunities) {
			log.info("Community status was returned, GUC is following Client");
		}
		else {
			log.info("Community status was returned, GUC is not following Client");
		}
		
		log.info("End test method Test_s30329CheckCommunityStatusForDifferentClient");
	}
	
	@Test(groups = {"LCAPI","LC"})
	public void Test_s30329CheckCommunityStatusForDifferentOppty(){
		log.info("Start test method Test_s30329CheckCommunityStatusForDifferentOppty");

		log.info("Getting user, client and creating contact and oppty");
		User user1 = commonUserAllocator.getUser(this);
		String clientID = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
		String clientID2 = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
		String contactID = "22SC-" + new Random().nextInt(99999);
		String contactID2 = "22SC-" + new Random().nextInt(99999);
		String opptyID = null;
		String opptyID2 = null;
		
		SugarAPI sugarAPI = new SugarAPI();
		sugarAPI.createContact(testConfig.getBrowserURL(), contactID, clientID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");
		opptyID = sugarAPI.createOppty(testConfig.getBrowserURL(), "", contactID, clientID, user1.getEmail(), user1.getPassword(), GC.emptyArray);
		sugarAPI.createContact(testConfig.getBrowserURL(), contactID2, clientID2, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");
		opptyID2 = sugarAPI.createOppty(testConfig.getBrowserURL(), "", contactID2, clientID2, user1.getEmail(), user1.getPassword(), GC.emptyArray);
		
		log.info("Getting OAuthToken");
		CollabWebAPI collabWebAPI = new CollabWebAPI();
		String headers[]={"OAuth-Token",collabWebAPI.getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};
		
		log.info("Getting session ID");
		String cookieHeaders[] = {"Cookie","PHPSESSID="+ sugarAPI.getSessionID(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};
		
		log.info("Getting community ID for second oppty");
		String communityID2=collabWebAPI.getcommunityIDFromBeanID(testConfig.getBrowserURL(), opptyID2,GC.opptysModule, cookieHeaders);
	
		log.info("Getting community status");
		Object subscribedCommunities=collabWebAPI.checkSubscribeCommunityStatus
		(testConfig.getBrowserURL(), headers, communityID2, opptyID,
				GC.opptysModule);

		if ((Boolean) subscribedCommunities) {
			log.info("Community status was returned, Different GUC is following Opportunity");
		}
		else {
			log.info("Community status was returned, Different GUC is not following Opportunity");
		}
		
		log.info("Remove the oppty created for this test");
		sugarAPI.deleteOpptySOAP(testConfig.getBrowserURL(), opptyID, user1.getEmail(), user1.getPassword());
		sugarAPI.deleteOpptySOAP(testConfig.getBrowserURL(), opptyID2, user1.getEmail(), user1.getPassword());
		sugarAPI.deleteContactSOAP(testConfig.getBrowserURL(), contactID, user1.getEmail(), user1.getPassword());
		sugarAPI.deleteContactSOAP(testConfig.getBrowserURL(), contactID2, user1.getEmail(), user1.getPassword());
		
		log.info("End test method Test_s30329CheckCommunityStatusForDifferentOppty");
	}
	
	@Test(groups = {"LCAPI","LC"})
	public void Test_s30329SubscribeCommunityClient(){
		log.info("Start test method Test_s30329SubscribeCommunityClient");
		
		log.info("Getting user and client name");
		User user1 = commonUserAllocator.getUser(this);
		String clientID = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
		log.info("User: " + user1.getDisplayName() +", Client: "+ clientID);

		log.info("Getting OAuthToken");
		CollabWebAPI collabWebAPI = new CollabWebAPI();
		String headers[]={"OAuth-Token",collabWebAPI.getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};
		
		log.info("Getting community status");
		Object subscribedCommunities=collabWebAPI.checkSubscribeCommunityStatus
		(testConfig.getBrowserURL(), headers, clientID,
				GC.accountsModule);

		if ((Boolean) subscribedCommunities) {
			log.info("Community already subscribed, unsubscribing before testing subscribe functionality");
			collabWebAPI.unsubscribeCommunity(testConfig.getBrowserURL(), headers, clientID, GC.accountsModule);
		}
		
		log.info("Subscribing community");
		collabWebAPI.subscribeCommunity(testConfig.getBrowserURL(), headers, clientID, GC.accountsModule);
		
		log.info("Getting community status after subscription");
		subscribedCommunities=collabWebAPI.checkSubscribeCommunityStatus
		(testConfig.getBrowserURL(), headers, clientID,
				GC.accountsModule);
		
		Assert.assertTrue((Boolean) subscribedCommunities, "Community has not been successfully subscribed"); 
		
		log.info("End test method Test_s30329SubscribeCommunityClient");
	}
	
	@Test(groups = {"LCAPI","LC"})
	public void Test_s30329SubscribeCommunityToDifferentClient(){
		log.info("Start test method Test_s30329SubscribeCommunityToDifferentClient");
		
		log.info("Getting user and client name");
		User user1 = commonUserAllocator.getUser(this);
		String clientID = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
		String clientID2 = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();

		log.info("Getting OAuthToken");
		CollabWebAPI collabWebAPI = new CollabWebAPI();
		String headers[]={"OAuth-Token",collabWebAPI.getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};
		
		log.info("Getting session ID");
		SugarAPI sugarAPI = new SugarAPI();
		String cookieHeaders[] = {"Cookie","PHPSESSID="+ sugarAPI.getSessionID(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};
		
		log.info("Getting community ID for second client");
		String communityID2=collabWebAPI.getcommunityIDFromBeanID(testConfig.getBrowserURL(), collabWebAPI.getbeanIDfromClientID(testConfig.getBrowserURL(), clientID2, headers),GC.accountsModule,cookieHeaders);
		
		log.info("Getting community status");
		Object subscribedCommunities=collabWebAPI.checkSubscribeCommunityStatus
		(testConfig.getBrowserURL(), headers, communityID2, clientID,
				GC.accountsModule);

		if ((Boolean) subscribedCommunities) {
			log.info("Community already subscribed, unsubscribing before testing subscribe functionality");
			collabWebAPI.unsubscribeCommunity(testConfig.getBrowserURL(), headers, communityID2, clientID, GC.accountsModule);
		}
		
		log.info("Subscribing community");
		collabWebAPI.subscribeCommunity(testConfig.getBrowserURL(), headers, communityID2, clientID, GC.accountsModule);
		
		log.info("Getting community status after subscription");
		subscribedCommunities=collabWebAPI.checkSubscribeCommunityStatus
		(testConfig.getBrowserURL(), headers, communityID2, clientID,
				GC.accountsModule);
		
		Assert.assertTrue((Boolean) subscribedCommunities, "Community has not been successfully subscribed"); 
		
		log.info("End test method Test_s30329SubscribeCommunityToDifferentClient");
	}
	
	@Test(groups = {"LCAPI","LC"})
	public void Test_s30329UnsubscribeCommunityClient(){
		log.info("Start test method Test_s30329UnsubscribeCommunityClient");
		
		log.info("Getting user and client name");
		User user1 = commonUserAllocator.getUser(this);
		String clientID = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
		log.info("User: " + user1.getDisplayName() +", Client: "+ clientID);
		
		log.info("Getting OAuthToken");
		CollabWebAPI collabWebAPI = new CollabWebAPI();
		String headers[]={"OAuth-Token",collabWebAPI.getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};
		
		log.info("Getting community status");
		Object subscribedCommunities=collabWebAPI.checkSubscribeCommunityStatus
		(testConfig.getBrowserURL(), headers, clientID,
				GC.accountsModule);

		if (!(Boolean) subscribedCommunities) {
			log.info("Community not subscribed, subscribing now");
			collabWebAPI.subscribeCommunity(testConfig.getBrowserURL(), headers, clientID, GC.accountsModule);
		}
		
		log.info("Unsubscribing community");
		collabWebAPI.unsubscribeCommunity(testConfig.getBrowserURL(), headers, clientID, GC.accountsModule);
		
		log.info("Getting community status after subscription");
		subscribedCommunities=collabWebAPI.checkSubscribeCommunityStatus
		(testConfig.getBrowserURL(), headers, clientID,
				GC.accountsModule);
		
		Assert.assertTrue(!(Boolean) subscribedCommunities, "Community has not been successfully unsubscribed"); 
		
		log.info("End test method Test_s30329UnsubscribeCommunityClient");
	}
	
	@Test(groups = {"LCAPI","LC"})
	public void Test_s30329UnsubscribeCommunityFromDifferentClient(){
		log.info("Start test method Test_s30329UnsubscribeCommunityFromDifferentClient");
		
		log.info("Getting user and client name");
		User user1 = commonUserAllocator.getUser(this);
		String clientID = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
		String clientID2 = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();

		log.info("Getting OAuthToken");
		CollabWebAPI collabWebAPI = new CollabWebAPI();
		String headers[]={"OAuth-Token",collabWebAPI.getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};
		
		log.info("Getting session ID");
		SugarAPI sugarAPI = new SugarAPI();
		String cookieHeaders[] = {"Cookie","PHPSESSID="+ sugarAPI.getSessionID(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};
		
		log.info("Getting community ID for second client");
		String communityID2=collabWebAPI.getcommunityIDFromBeanID(testConfig.getBrowserURL(), collabWebAPI.getbeanIDfromClientID(testConfig.getBrowserURL(), clientID2, headers),GC.accountsModule, cookieHeaders);
		
		log.info("Getting community status");
		Object subscribedCommunities=collabWebAPI.checkSubscribeCommunityStatus
		(testConfig.getBrowserURL(), headers, communityID2, clientID,
				GC.accountsModule);

		if (!(Boolean) subscribedCommunities) {
			log.info("Community not subscribed, subscribing now");
			collabWebAPI.subscribeCommunity(testConfig.getBrowserURL(), headers, communityID2, clientID, GC.accountsModule);
		}
		
		log.info("Unsubscribing community");
		collabWebAPI.unsubscribeCommunity(testConfig.getBrowserURL(), headers, communityID2, clientID, GC.accountsModule);
		
		log.info("Getting community status after subscription");
		subscribedCommunities=collabWebAPI.checkSubscribeCommunityStatus
		(testConfig.getBrowserURL(), headers, communityID2, clientID,
				GC.accountsModule);
		
		Assert.assertTrue(!(Boolean) subscribedCommunities, "Community has not been successfully unsubscribed"); 
		
		log.info("End test method Test_s30329UnsubscribeCommunityFromDifferentClient");
	}
	

	@Test(groups = {"LCAPI","LC"})
	public void Test_s30329SubscribeCommunityOpportunity(){
		log.info("Start test method Test_s30329SubscribeCommunityOpportunity");
		
		log.info("Getting user, client and creating contact and oppty");
		User user1 = commonUserAllocator.getUser(this);
		String clientID = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
		String contactID = "22SC-" + new Random().nextInt(99999);
		String opptyID = null;
		
		SugarAPI sugarAPI = new SugarAPI();
		sugarAPI.createContact(testConfig.getBrowserURL(), contactID, clientID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");
		opptyID = sugarAPI.createOppty(testConfig.getBrowserURL(), "", contactID, clientID, user1.getEmail(), user1.getPassword(), GC.emptyArray);


		log.info("Getting OAuthToken");
		CollabWebAPI collabWebAPI = new CollabWebAPI();
		String headers[]={"OAuth-Token",collabWebAPI.getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};
		
		log.info("Getting community status");
		Object subscribedCommunities=collabWebAPI.checkSubscribeCommunityStatus
		(testConfig.getBrowserURL(), headers, opptyID,
				GC.opptysModule);

		if ((Boolean) subscribedCommunities) {
			log.info("Community already subscribed, unsubscribing before testing subscribe functionality");
			collabWebAPI.unsubscribeCommunity(testConfig.getBrowserURL(), headers, opptyID, GC.opptysModule);
		}
		
		log.info("Subscribing community");
		collabWebAPI.subscribeCommunity(testConfig.getBrowserURL(), headers, opptyID, GC.opptysModule);
		
		log.info("Getting community status after subscription");
		subscribedCommunities=collabWebAPI.checkSubscribeCommunityStatus
		(testConfig.getBrowserURL(), headers, opptyID,
				GC.opptysModule);
		
		Assert.assertTrue((Boolean) subscribedCommunities, "Community has not been successfully subscribed"); 
		
		log.info("Remove the oppty created for this test");
		sugarAPI.deleteOpptySOAP(testConfig.getBrowserURL(), opptyID, user1.getEmail(), user1.getPassword());
		sugarAPI.deleteContactSOAP(testConfig.getBrowserURL(), contactID, user1.getEmail(), user1.getPassword());
		
		log.info("End test method Test_s30329SubscribeCommunityOpportunity");
	}
	
	@Test(groups = {"LCAPI","LC"})
	public void Test_s30329SubscribeCommunityToDifferentOpportunity(){
		log.info("Start test method Test_s30329SubscribeCommunityToDifferentOpportunity");
		
		log.info("Getting user, client and creating contact and oppty");
		User user1 = commonUserAllocator.getUser(this);
		String clientID = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
		String clientID2 = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
		String contactID = "22SC-" + new Random().nextInt(99999);
		String contactID2 = "22SC-" + new Random().nextInt(99999);
		String opptyID = null;
		String opptyID2 = null;
		
		SugarAPI sugarAPI = new SugarAPI();
		sugarAPI.createContact(testConfig.getBrowserURL(), contactID, clientID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");
		opptyID = sugarAPI.createOppty(testConfig.getBrowserURL(), "", contactID, clientID, user1.getEmail(), user1.getPassword(), GC.emptyArray);
		sugarAPI.createContact(testConfig.getBrowserURL(), contactID2, clientID2, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");
		opptyID2 = sugarAPI.createOppty(testConfig.getBrowserURL(), "", contactID2, clientID2, user1.getEmail(), user1.getPassword(), GC.emptyArray);


		log.info("Getting OAuthToken");
		CollabWebAPI collabWebAPI = new CollabWebAPI();
		String headers[]={"OAuth-Token",collabWebAPI.getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};
		
		log.info("Getting session ID");
		String cookieHeaders[] = {"Cookie","PHPSESSID="+ sugarAPI.getSessionID(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};
		
		log.info("Getting community ID for second opportunity");
		String communityID2=collabWebAPI.getcommunityIDFromBeanID(testConfig.getBrowserURL(), opptyID2,GC.opptysModule, cookieHeaders);

		log.info("Getting community status");
		Object subscribedCommunities=collabWebAPI.checkSubscribeCommunityStatus
		(testConfig.getBrowserURL(), headers, communityID2, opptyID,
				GC.opptysModule);

		if ((Boolean) subscribedCommunities) {
			log.info("Community already subscribed, unsubscribing before testing subscribe functionality");
			collabWebAPI.unsubscribeCommunity(testConfig.getBrowserURL(), headers, communityID2, opptyID, GC.opptysModule);
		}
		
		log.info("Subscribing community");
		collabWebAPI.subscribeCommunity(testConfig.getBrowserURL(), headers, communityID2, opptyID, GC.opptysModule);
		
		log.info("Getting community status after subscription");
		subscribedCommunities=collabWebAPI.checkSubscribeCommunityStatus
		(testConfig.getBrowserURL(), headers, communityID2, opptyID,
				GC.opptysModule);
		
		Assert.assertTrue((Boolean) subscribedCommunities, "Community has not been successfully subscribed"); 
		
		log.info("Remove the oppty created for this test");
		sugarAPI.deleteOpptySOAP(testConfig.getBrowserURL(), opptyID, user1.getEmail(), user1.getPassword());
		sugarAPI.deleteOpptySOAP(testConfig.getBrowserURL(), opptyID2, user1.getEmail(), user1.getPassword());
		sugarAPI.deleteContactSOAP(testConfig.getBrowserURL(), contactID, user1.getEmail(), user1.getPassword());
		sugarAPI.deleteContactSOAP(testConfig.getBrowserURL(), contactID2, user1.getEmail(), user1.getPassword());
		
		log.info("End test method Test_s30329SubscribeCommunityToDifferentOpportunity");
	}
	
	@Test(groups = {"LCAPI","LC"})
	public void Test_s30329UnsubscribeCommunityOppty(){
		log.info("Start test method Test_s30329UnsubscribeCommunityOppty");
		
		log.info("Getting user, client and creating contact and oppty");
		User user1 = commonUserAllocator.getUser(this);
		String clientID = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
		String contactID = "22SC-" + new Random().nextInt(99999);
		String opptyID = null;
		
		SugarAPI sugarAPI = new SugarAPI();
		sugarAPI.createContact(testConfig.getBrowserURL(), contactID, clientID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");
		opptyID = sugarAPI.createOppty(testConfig.getBrowserURL(), "", contactID, clientID, user1.getEmail(), user1.getPassword(), GC.emptyArray);

		log.info("Getting OAuthToken");
		CollabWebAPI collabWebAPI = new CollabWebAPI();
		String headers[]={"OAuth-Token",collabWebAPI.getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};
		
		log.info("Getting community status");
		Object subscribedCommunities=collabWebAPI.checkSubscribeCommunityStatus
		(testConfig.getBrowserURL(), headers, opptyID,
				GC.opptysModule);

		if (!(Boolean) subscribedCommunities) {
			log.info("Community not subscribed, subscribing now");
			collabWebAPI.subscribeCommunity(testConfig.getBrowserURL(), headers, opptyID, GC.opptysModule);
		}
		
		log.info("Unsubscribing community");
		collabWebAPI.unsubscribeCommunity(testConfig.getBrowserURL(), headers, opptyID, GC.opptysModule);
		
		log.info("Getting community status after subscription");
		subscribedCommunities=collabWebAPI.checkSubscribeCommunityStatus
		(testConfig.getBrowserURL(), headers, opptyID,
				GC.opptysModule);
		
		Assert.assertTrue(!(Boolean) subscribedCommunities, "Community has not been successfully unsubscribed"); 
		
		log.info("Remove the oppty created for this test");
		sugarAPI.deleteOpptySOAP(testConfig.getBrowserURL(), opptyID, user1.getEmail(), user1.getPassword());
		sugarAPI.deleteContactSOAP(testConfig.getBrowserURL(), contactID, user1.getEmail(), user1.getPassword());
		
		log.info("End test method Test_s30329UnsubscribeCommunityOppty");
	}
	
	@Test(groups = {"LCAPI","LC"})
	public void Test_s30329UnsubscribeCommunityFromDifferentOppty(){
		log.info("Start test method Test_s30329UnsubscribeCommunityFromDifferentOppty");
		
		log.info("Getting user, client and creating contact and oppty");
		User user1 = commonUserAllocator.getUser(this);
		String clientID = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
		String clientID2 = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
		String contactID = "22SC-" + new Random().nextInt(99999);
		String contactID2 = "22SC-" + new Random().nextInt(99999);
		String opptyID = null;
		String opptyID2 = null;
		
		SugarAPI sugarAPI = new SugarAPI();
		sugarAPI.createContact(testConfig.getBrowserURL(), contactID, clientID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");
		opptyID = sugarAPI.createOppty(testConfig.getBrowserURL(), "", contactID, clientID, user1.getEmail(), user1.getPassword(), GC.emptyArray);
		sugarAPI.createContact(testConfig.getBrowserURL(), contactID2, clientID2, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");
		opptyID2 = sugarAPI.createOppty(testConfig.getBrowserURL(), "", contactID2, clientID2, user1.getEmail(), user1.getPassword(), GC.emptyArray);

		log.info("Getting OAuthToken");
		CollabWebAPI collabWebAPI = new CollabWebAPI();

		
		log.info("Getting session ID");
		String cookieHeaders[] = {"Cookie","PHPSESSID="+ sugarAPI.getSessionID(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};
		
		log.info("Getting community ID for second opportunity");
		String communityID2=collabWebAPI.getcommunityIDFromBeanID(testConfig.getBrowserURL(), opptyID2,GC.opptysModule,cookieHeaders);

		log.info("Getting community status");
		String headers[]={"OAuth-Token",collabWebAPI.getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};
		Object subscribedCommunities=collabWebAPI.checkSubscribeCommunityStatus
		(testConfig.getBrowserURL(), headers, communityID2, opptyID,
				GC.opptysModule);

		if (!(Boolean) subscribedCommunities) {
			log.info("Community not subscribed, subscribing now");
			collabWebAPI.subscribeCommunity(testConfig.getBrowserURL(), headers, communityID2, opptyID, GC.opptysModule);
		}
		
		String headers2[]={"OAuth-Token",collabWebAPI.getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};
		log.info("Unsubscribing community");
		collabWebAPI.unsubscribeCommunity(testConfig.getBrowserURL(), headers2, communityID2, opptyID, GC.opptysModule);
		
		log.info("Getting community status after subscription");
		subscribedCommunities=collabWebAPI.checkSubscribeCommunityStatus
		(testConfig.getBrowserURL(), headers2, communityID2, opptyID,
				GC.opptysModule);
		
		Assert.assertTrue(!(Boolean) subscribedCommunities, "Community has not been successfully unsubscribed"); 
		
		log.info("Remove the oppty created for this test");
		sugarAPI.deleteOpptySOAP(testConfig.getBrowserURL(), opptyID, user1.getEmail(), user1.getPassword());
		sugarAPI.deleteOpptySOAP(testConfig.getBrowserURL(), opptyID2, user1.getEmail(), user1.getPassword());
		sugarAPI.deleteContactSOAP(testConfig.getBrowserURL(), contactID, user1.getEmail(), user1.getPassword());
		sugarAPI.deleteContactSOAP(testConfig.getBrowserURL(), contactID2, user1.getEmail(), user1.getPassword());
		
		log.info("End test method Test_s30329UnsubscribeCommunityFromDifferentOppty");
	}
	
	@Test(groups = {"LCAPI","LC"})
	public void Test_s30329ListCommunitySubscriptions(){
		
		log.info("Start test method Test_s30329ListCommunitySubscriptions");
		CollabWebAPI collabWebAPI = new CollabWebAPI();
		SugarAPI sugarAPI = new SugarAPI();
		
		log.info("Getting user and client");
		User user1 = commonUserAllocator.getUser(this);
		String clientID = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
		
		log.info("Getting OAuthToken");
		String headers[]={"OAuth-Token",collabWebAPI.getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};
		
		log.info("Getting session ID");
		String cookieHeaders[] = {"Cookie","PHPSESSID="+ sugarAPI.getSessionID(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};
		
		log.info("Getting community id and beanID  from client id");
		String beanID = collabWebAPI.getbeanIDfromClientID(testConfig.getBrowserURL(), clientID, headers);
		String communityID = collabWebAPI.getcommunityIDFromBeanID(testConfig.getBrowserURL(), beanID,GC.accountsModule, cookieHeaders);
		
		
		log.info("Ensuring community is subscribed");
		if (!collabWebAPI.checkSubscribeCommunityStatus(testConfig.getBrowserURL(), headers, clientID, GC.accountsModule)) {
			log.info("Community not subscribed, subscribing now");
			collabWebAPI.subscribeCommunity(testConfig.getBrowserURL(), headers, clientID, GC.accountsModule);
		}
		
		log.info("Getting list of communitysubscriptions");
		JSONArray listofCommunitySubscriptions = collabWebAPI.getListofCommunitySubscriptions(testConfig.getBrowserURL(),
				headers, communityID);
		
		log.info("Checking that client appears on list of subscriptions");
		Assert.assertTrue(collabWebAPI.isClientonSubscriptionList(listofCommunitySubscriptions, beanID), "Client Bean ID is not present in the list of community subscriptions when it should be");
		
		log.info("Unsubscribing community");
		collabWebAPI.unsubscribeCommunity(testConfig.getBrowserURL(), headers, clientID, GC.accountsModule);
		
		log.info("Getting list of communitysubscriptions");
		JSONArray listofCommunitySubscriptions2 = collabWebAPI.getListofCommunitySubscriptions(testConfig.getBrowserURL(),
				headers, communityID);
		
		log.info("Checking that client appears on list of subscriptions");
		Assert.assertFalse(collabWebAPI.isClientonSubscriptionList(listofCommunitySubscriptions2, beanID), "Client Bean ID is present in the list of community subscriptions when it should not be");

		log.info("End test method Test_s30329ListCommunitySubscriptions");
	}
	
	@Test(groups = {"LCAPI","LC"})
	public void Test_s30329ClientEventRetrieveActivitystreamFilter(){
		log.info("Start test method Test_s30329ClientEventRetrieveActivitystreamFilter");
		
		CollabWebAPI collabWebAPI = new CollabWebAPI();
		
		log.info("Getting user and client");
		User user1 = commonUserAllocator.getUser(this);
		String clientID = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
		
		log.info("Getting OAuthToken");
		String headers[]={"OAuth-Token", collabWebAPI.getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};
		
		log.info("Getting session ID");
		SugarAPI sugarAPI = new SugarAPI();
		String cookieHeaders[] = {"Cookie","PHPSESSID="+ sugarAPI.getSessionID(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};
		
		String beanID = collabWebAPI.getbeanIDfromClientID(testConfig.getBrowserURL(), clientID, headers);
		String communityID = collabWebAPI.getcommunityIDFromBeanID(testConfig.getBrowserURL(), beanID,GC.accountsModule,cookieHeaders);
		
		log.info("Getting activity Stream Filter for client");
		JSONObject json = collabWebAPI.getActivityStreamEventsFilter(testConfig.getBrowserURL(), headers, clientID, GC.accountsModule);
		
		log.info("Comparing the received response with the expected");
		Pattern pattern = collabWebAPI.activityStreamEventsFilter(beanID,communityID);
		log.info("Expected: "+pattern);
		Matcher matcher = pattern.matcher(json.toJSONString());
		log.info("Actual: "+json.toJSONString());
		Assert.assertTrue(matcher.find());
		
		log.info("End test method Test_s30329ClientEventRetrieveActivitystreamFilter");
	}
	
	
	@Test(groups = {"LCAPI","LC"})
	public void Test_s30329OpportunityEventRetrieveActivitystreamFilter(){
		log.info("Start test method Test_s30329OpportunityEventRetrieveActivitystreamFilter");
		
		log.info("Getting user, client and oppty");
		User user1 = commonUserAllocator.getUser(this);
		String clientID = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();

		String contactID = "22SC-" + new Random().nextInt(99999);
		String opptyID = null;
		
		SugarAPI sugarAPI = new SugarAPI();
		sugarAPI.createContact(testConfig.getBrowserURL(), contactID, clientID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");
		opptyID = sugarAPI.createOppty(testConfig.getBrowserURL(), "", contactID, clientID, user1.getEmail(), user1.getPassword(), GC.emptyArray);
		
		log.info("Getting OAuthToken");
		CollabWebAPI collabWebAPI = new CollabWebAPI();
		String headers[]={"OAuth-Token", collabWebAPI.getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};
		
		log.info("Getting activity Stream Filter for opportunity");
		JSONObject json = collabWebAPI.getActivityStreamEventsFilter(testConfig.getBrowserURL(), headers, opptyID, GC.opptysModule);
		
		log.info("Comparing the received response with the expected");
		Pattern pattern = collabWebAPI.activityStreamEventsFilterOppty(opptyID);
		log.info("Expected: "+pattern);
		Matcher matcher = pattern.matcher(json.toJSONString());
		log.info("Actual: "+json.toJSONString());
		Assert.assertTrue(matcher.find());
		
		log.info("Remove the oppty created for this test");
		sugarAPI.deleteOpptySOAP(testConfig.getBrowserURL(), opptyID, user1.getEmail(), user1.getPassword());
		sugarAPI.deleteContactSOAP(testConfig.getBrowserURL(), contactID, user1.getEmail(), user1.getPassword());
		
		log.info("End test method Test_s30329OpportunityEventRetrieveActivitystreamFilter");
	}
	
	@Test(groups = {"LCAPI","LC"})
	public void Test_s30329ClientMicroblogRetrieveActivitystreamFilter(){
		log.info("Start test method Test_s30329ClientMicroblogRetrieveActivitystreamFilter");
		
		CollabWebAPI collabWebAPI = new CollabWebAPI();
		
		log.info("Getting user and client");
		User user1 = commonUserAllocator.getUser(this);
		String clientID = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
		
		log.info("Getting OAuthToken");
		String headers[]={"OAuth-Token", collabWebAPI.getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};
		
		log.info("Getting session ID");
		SugarAPI sugarAPI = new SugarAPI();
		String cookieHeaders[] = {"Cookie","PHPSESSID="+ sugarAPI.getSessionID(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};
		
		String beanID = collabWebAPI.getbeanIDfromClientID(testConfig.getBrowserURL(), clientID, headers);
		String communityID = collabWebAPI.getcommunityIDFromBeanID(testConfig.getBrowserURL(), beanID,GC.accountsModule,cookieHeaders);
		
		log.info("Getting activity Stream Filter for client");
		JSONObject json = collabWebAPI.getActivityStreamMicroBlogFilter(testConfig.getBrowserURL(), headers, clientID, GC.accountsModule);

		log.info("Comparing the received response with the expected");
		Pattern pattern = collabWebAPI.activityStreamMicroBlogsFilter(communityID);
		log.info("Expected: "+pattern);
		Matcher matcher = pattern.matcher(json.toJSONString());
		log.info("Actual: "+json.toJSONString());
		Assert.assertTrue(matcher.find());		
		
		log.info("End test method Test_s30329ClientMicroblogRetrieveActivitystreamFilter");
	}
	
	@Test(groups = {"LCAPI","LC"})
	public void Test_s30329OpportunityMicroblogRetrieveActivitystreamFilter(){
		log.info("Start test method Test_s30329OpportunityMicroblogRetrieveActivitystreamFilter");
		CollabWebAPI collabWebAPI = new CollabWebAPI();
				
		log.info("Getting user and client");
		User user1 = commonUserAllocator.getUser(this);
		String clientID = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
		
		log.info("Getting session ID");
		SugarAPI sugarAPI = new SugarAPI();
		String cookieHeaders[] = {"Cookie","PHPSESSID="+ sugarAPI.getSessionID(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};
		
		log.info("Getting OAuthToken");
		String headers[]={"OAuth-Token", collabWebAPI.getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};
		String beanID = collabWebAPI.getbeanIDfromClientID(testConfig.getBrowserURL(), clientID, headers);
		String communityID = collabWebAPI.getcommunityIDFromBeanID(testConfig.getBrowserURL(), beanID,GC.accountsModule,cookieHeaders);

		
		String contactID = "22SC-" + new Random().nextInt(99999);
		String opptyID = null;
		
		sugarAPI.createContact(testConfig.getBrowserURL(), contactID, clientID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");
		opptyID = sugarAPI.createOppty(testConfig.getBrowserURL(), "", contactID, clientID, user1.getEmail(), user1.getPassword(), GC.emptyArray);
		
		log.info("Getting activity Stream Filter for opportunity");
		log.info("Getting OAuthToken");
		String headers2[]={"OAuth-Token", collabWebAPI.getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};
		JSONObject json = collabWebAPI.getActivityStreamMicroBlogFilter(testConfig.getBrowserURL(), headers2, opptyID, GC.opptysModule);
		
		
		log.info("Comparing the received response with the expected");
		log.info("Comparing the received response with the expected");
		Pattern pattern = collabWebAPI.activityStreamMicroBlogsFilter(communityID);
		log.info("Expected: "+pattern);
		Matcher matcher = pattern.matcher(json.toJSONString());
		log.info("Actual: "+json.toJSONString());
		Assert.assertTrue(matcher.find());
		
		log.info("Remove the oppty created for this test");
		sugarAPI.deleteOpptySOAP(testConfig.getBrowserURL(), opptyID, user1.getEmail(), user1.getPassword());
		sugarAPI.deleteContactSOAP(testConfig.getBrowserURL(), contactID, user1.getEmail(), user1.getPassword());
		log.info("End test method Test_s30329OpportunityMicroblogRetrieveActivitystreamFilter");
	}
	
	@Test(groups = {"LCAPI","LC"})
	public void Test_s30329ClientPostMicroblog() throws IOException{
		log.info("Start test method Test_s30329ClientPostMicroblog");
		CollabWebAPI collabWebAPI = new CollabWebAPI();
		SugarAPI sugarAPI = new SugarAPI();
		
		log.info("Getting user and client");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
		String clientID = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
		String message = "AutoTest s30329ClientPostMicroblog" + new Random().nextInt(99999);

		Base64Encoder b64e = new Base64Encoder();
		String userPass = user1.getEmail()+":"+user1.getPassword();
		String Connheaders[]={"Authorization","Basic "+b64e.encode(userPass.getBytes())};
		
		log.info("Getting session ID");
		String cookieHeaders[] = {"Cookie","PHPSESSID="+ sugarAPI.getSessionID(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};
		
		log.info("Getting OAuthToken");
		String OAuthheaders[]={"OAuth-Token", collabWebAPI.getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};
		
		log.info("Getting beanID");
		String beanID = collabWebAPI.getbeanIDfromClientID(testConfig.getBrowserURL(), clientID, OAuthheaders);
		
		log.info("Getting community ID");
		String communityId = collabWebAPI.getcommunityIDFromBeanID(testConfig.getBrowserURL(), beanID, GC.accountsModule, cookieHeaders);
		
		log.info("Getting community status");
		Object subscribedCommunities=collabWebAPI.checkSubscribeCommunityStatus
		(testConfig.getBrowserURL(), OAuthheaders, clientID,
				GC.accountsModule);

		log.info("Subscribing community if not already");
		if (!(Boolean) subscribedCommunities) {
			log.info("Community not subscribed, subscribing now");
			collabWebAPI.subscribeCommunity(testConfig.getBrowserURL(), OAuthheaders, clientID, GC.accountsModule);
		}
		
		log.info("Join user to community if not already joined");
		if (!collabWebAPI.checkCommunityMembership(testConfig.getParameter("cxnurl"), communityId, Connheaders, user1.getEmail())) {
			log.info("Joining community");
			collabWebAPI.joinCommunity(testConfig.getBrowserURL(), beanID, GC.accountsModule, cookieHeaders);	
		}
		
		log.info("Posting to client microblog");
		JSONObject response = collabWebAPI.postToMicroBlog(testConfig.getBrowserURL(), OAuthheaders,message, beanID, GC.accountsModule);

		System.out.println((String) response.get("masterPost"));
		log.info("Sleep to allow time for connections to process the post");
		sleep(10);
		
		log.info("Checking that microblog post is available from connections");
		String microBlogFromConnections = collabWebAPI.getMicroBlog(testConfig.getParameter(GC.cxnURL),Connheaders,(String) response.get("masterPost"));
		Assert.assertTrue(microBlogFromConnections.contains(message), "Message: "+message+" does not appear in response: " + microBlogFromConnections);

		log.info("End test method Test_s30329ClientPostMicroblog");
	}
	
	@Test(groups = {"LCAPI","LC"})
	public void Test_s30329OpportunitiesPostMicroblog() throws IOException{
		log.info("Start test method Test_s30329OpportunitiesPostMicroblog");
		CollabWebAPI collabWebAPI = new CollabWebAPI();
		
		log.info("Getting user and client");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
		String clientID = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
		String message = "AutoTest s30329OpportunitiesPostMicroblog" + new Random().nextInt(99999);

		Base64Encoder b64e = new Base64Encoder();
		String userPass = user1.getEmail()+":"+user1.getPassword();

		String contactID = "22SC-" + new Random().nextInt(99999);
		String opptyID = null;
		
		SugarAPI sugarAPI = new SugarAPI();
		sugarAPI.createContact(testConfig.getBrowserURL(), contactID, clientID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");
		opptyID = sugarAPI.createOppty(testConfig.getBrowserURL(), "", contactID, clientID, user1.getEmail(), user1.getPassword(), GC.emptyArray);
		
		
		log.info("Getting OAuthToken");
		String OAuthheaders[]={"OAuth-Token", collabWebAPI.getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};
		
		log.info("Getting community status");
		Object subscribedCommunities=collabWebAPI.checkSubscribeCommunityStatus
		(testConfig.getBrowserURL(), OAuthheaders, opptyID,
				GC.opptysModule);

		if (!(Boolean) subscribedCommunities) {
			log.info("Community not subscribed, subscribing now");
			collabWebAPI.subscribeCommunity(testConfig.getBrowserURL(), OAuthheaders, opptyID, GC.opptysModule);
		}

		String OAuthheaders2[]={"OAuth-Token", collabWebAPI.getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword())};
		log.info("Posting to opportunites microblog");
		JSONObject response = collabWebAPI.postToMicroBlog(testConfig.getBrowserURL(), OAuthheaders2,message, opptyID, GC.opptysModule);

		log.info("Sleep to allow time for connections to process the post");
		sleep(10);
		
		log.info("Checking that microblog post is available from connections");
		String Connheaders[]={"Authorization","Basic "+b64e.encode(userPass.getBytes())};
		String microBlogFromConnections = collabWebAPI.getMicroBlog(testConfig.getParameter(GC.cxnURL),Connheaders,(String) response.get("masterPost"));
		Assert.assertTrue(microBlogFromConnections.contains(message), "Message: "+message+" does not appear in response: " + microBlogFromConnections);
		//Assert.assertTrue(collabWebAPI.getMicroBlog(testConfig.getParameter(GC.cxnURL),Connheaders).contains(message),(String) response.get("masterPost"));

		log.info("Remove the oppty created for this test");
		sugarAPI.deleteOpptySOAP(testConfig.getBrowserURL(), opptyID, user1.getEmail(), user1.getPassword());
		sugarAPI.deleteContactSOAP(testConfig.getBrowserURL(), contactID, user1.getEmail(), user1.getPassword());
		
		log.info("End test method Test_s30329OpportunitiesPostMicroblog");
	}
	 
}
