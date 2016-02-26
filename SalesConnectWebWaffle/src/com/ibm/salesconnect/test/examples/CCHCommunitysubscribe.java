/**
 * 
 */
package com.ibm.salesconnect.test.examples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.CollabWebAPI;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;

/**
 * @author timlehane
 * @date Oct 21, 2013
 */
public class CCHCommunitysubscribe extends ProductBaseTest {
	Logger log = LoggerFactory.getLogger(CCHCommunitysubscribe.class);
	
	@Test(groups = {})
	public void Test_CCHCommunitysubscribe(){
		log.info("Start test method Test_s30329SubscribeCommunityClient");
		
		log.info("Getting user and client name");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
		String clientID = "DC28MKKH";
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

}
