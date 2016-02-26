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



public class s62309VerifyMicroblogUIPost  extends ProductBaseTest {
	Logger log = LoggerFactory.getLogger(s62309VerifyMicroblogUIPost.class);

	@Test(groups = {"BVT","BVT1"})	
	public void Test_s62309VerifyMicroblogUIPost() {
			
		log.info("Start of test method s62309VerifyMicroblogUIPost");
		
		
		// Navigate to Client -- pulled from existing client pool from client.csv 
		
//		//TODO:Log into UI
		log.info("Getting users");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
		PoolClient poolClient = commonClientAllocator.getGroupClient("DC",this);
		Client client = new Client();
		client.sClientID = poolClient.getCCMS_ID();
		client.sClientName = poolClient.getClientName(testConfig.getBrowserURL(), user1);		
		client.sCCMS_Level = "DC";
		client.sSearchIn= GC.showingInClientID;
		client.sSearchFor= GC.searchForAll;
		client.sSearchShowing=GC.showingForClients;
		int sRand = (int) Math.round((Math.random()) * 10000);
		String StatusTextArea = "User A Status "+sRand;
				
		log.info("Launch and log in");
		Dashboard dashboard = launchWithLogin(user1);

		ViewClientPage viewClientPage = dashboard.openViewClient();
		ClientDetailPage clientDetailPage;
		if (!viewClientPage.checkResult(client)) {
			viewClientPage.isPageLoaded();
			viewClientPage.searchForClient(client);
			clientDetailPage = viewClientPage.selectResult(client);
		}
		else {
			clientDetailPage = viewClientPage.selectResult(client);
		}
		
	
		
//		//  Create a microblog post 
		
		log.info("In Recent Discussion, post some comments");
		clientDetailPage.clientUpdates();
		clientDetailPage.postStatus(StatusTextArea);
		//ClientMicroBloggingFrame clientMicroBloggingFrame = dashboard.switchToClientMicroBloggingFrame();
		//clientMicroBloggingFrame.refreshEventsStream();


//		 // Verify that post is visible 
//		
		log.info("Verify MicroBlogPost in Recent Discussion");
		//clientDetailPage.clientUpdates();
		 String post= StatusTextArea;
		ClientMicroBloggingFrame clientMicroBloggingFrame = dashboard.switchToClientMicroBloggingFrame();
		clientMicroBloggingFrame.refreshEventsStream();
		
		Boolean found = false;
		
		for (int i = 0; i < 6; i++) {
			clientMicroBloggingFrame.refreshEventsStream();
			
			log.info("Searching for MicroBlog User A Status Post");
			if(clientMicroBloggingFrame.verifyStatus(StatusTextArea)){
				log.info("Found Post");
				found = true;
				i=6;
			}
			sleep(10);
		}
		
		if (!found) {
			Assert.assertTrue(false, "User A Status Post does not exist");
		}
		
		log.info("End test method Test_s62309VerifyMicroblogUIPost");
	
	}		
		
}

