package com.ibm.salesconnect.test.Collab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Client.ClientDetailPage;
import com.ibm.salesconnect.model.standard.Client.ViewClientPage;
import com.ibm.salesconnect.model.standard.Collab.ClientMicroBloggingFrame;
import com.ibm.salesconnect.objects.Client;

/**
 * @author evafarrell
 * @date Nov 13, 2013
 */


public class s19282ClientUpdateTabLikeComments extends ProductBaseTest {
	
	Logger log = LoggerFactory.getLogger(s19282ClientUpdateTabLikeComments.class);
		
	@Test(groups = { "Collab","LC"})
	public void test_s19282ClientUpdateTabLikeComments() {
		
		log.info("Start of test method s19282ClientUpdateTabLikeComments");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
		User user2 = commonUserAllocator.getUser(this);
		PoolClient poolClient = commonClientAllocator.getGroupClient("DC",this);
		Client client = new Client();
		client.sClientID = poolClient.getCCMS_ID();
		client.sClientName = poolClient.getClientName(testConfig.getBrowserURL(), user1);		
		client.sCCMS_Level = "DC";

		//Client variables
		client.sSearchIn= GC.showingInClientID;
		client.sSearchFor= GC.searchForAll;
		client.sSearchShowing=GC.showingForClients;
		
			String Status = "User A Status "+GC.sUniqueNum;
			
			log.info("Logging in as User A");
			Dashboard dashboard = launchWithLogin(user1);

			log.info("Open Client detail page");
			ViewClientPage viewClientPage = dashboard.openViewClient();
			viewClientPage.searchForClient(client);
			ClientDetailPage clientDetailPage = viewClientPage.selectResult(client);
						
			log.info("Open Updates Tab for Client");
			clientDetailPage.clientUpdates();
			clientDetailPage.followClient();
			
			log.info("In Recent Discussion, post some comments");
			clientDetailPage.postStatus(Status);
			ClientMicroBloggingFrame clientMicroBloggingFrame = dashboard.switchToClientMicroBloggingFrame();
			clientMicroBloggingFrame.refreshEventsStream();
			Assert.assertTrue(clientMicroBloggingFrame.verifyStatus(Status), "Status does not exist in Activity Stream");
			
			log.info("In Recent Discussion, like a comment  and verify that it increments like counter of comment");
			sleep(3);
			clientMicroBloggingFrame.likeStatus(Status);
			clientMicroBloggingFrame.verifyLikeCount(Status, 1);
			
			exec.quit();
			
			log.info("Logon in separate browser as userB & navigate to same client Update tab");
			launchWithLogin(user2);
			
			log.info("Open Client detail page");
			dashboard.openViewClient();
			viewClientPage.searchForClient(client);
			viewClientPage.selectResult(client);
						
			log.info("Open Updates Tab for Client");
			clientDetailPage.clientUpdates();
			clientDetailPage.followClient();
			
			log.info("As userB, in Recent Discussios, like same comment of userA which increments like counter of comment");
			sleep(3);
			dashboard.switchToClientMicroBloggingFrame();
			Assert.assertTrue(clientMicroBloggingFrame.verifyStatus(Status), "Status does not exist in Activity Stream");

			log.info("As userB, in Recent Discussion, like a comment  and verify that it increments like counter of comment");
			sleep(3);
			clientMicroBloggingFrame.likeStatus(Status);
			sleep(3);
			clientMicroBloggingFrame.verifyLikeCount(Status, 2);


		log.info("End of test method s19282ClientUpdateTabLikeComments");
		
		commonUserAllocator.checkInAllUsersWithToken(this);
		commonUserAllocator.checkInAllGroupUsersWithToken(GC.db2UserGroup,this);
	}
}
