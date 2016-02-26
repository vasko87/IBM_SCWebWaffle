package com.ibm.salesconnect.test.ManageEvents;

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
import com.ibm.salesconnect.model.standard.Collab.ManageEventsDialog;
import com.ibm.salesconnect.objects.Client;

/**
 * @author timlehane
 * @date Aug 20, 2013
 */
public class s19282FollowingUnfollowingManagedClientsAssignedTo extends ProductBaseTest {
	
	User user1 = null;
	PoolClient poolClient = null;
	Client client = new Client();

	
	@Test(groups = {"ManageEvents","me2"})
	public void Test_s19282FollowingManagedClientsAssignedTo() {
	    Logger log = LoggerFactory.getLogger(s19282FollowingUnfollowingManagedClientsAssignedTo.class);
	    log.info("Start of test method Test_s19282FollowingManagedClientsAssignedTo");
		try{
			user1 = commonUserAllocator.getUser(this);
			poolClient = commonClientAllocator.getGroupClient("SC",this);
	    client.sCCMS_Level = "SC";
		client.sSearchIn= GC.showingInSiteID;
		client.sSearchFor= GC.searchForAll;
		client.sSearchShowing=GC.showingForAll;
		client.sSiteID = poolClient.getCCMS_ID();
		client.sClientName = poolClient.getClientName(testConfig.getBrowserURL(), user1);
		log.info("Client selected: " + client.sClientName);
		
		log.info("Logging in");
		Dashboard dashboard = launchWithLogin(user1);

		log.info("Prerequiste##Start Setting client to be in followed state ");
		dashboard.switchToMainWindow();
		dashboard.openHomePage();
		ManageEventsDialog manageEventsDialog =  dashboard.openManageEventsDialog();
		manageEventsDialog.isPageLoaded();
		if(!manageEventsDialog.isItemNotFollowed(client.sSiteID)){
			manageEventsDialog.stopFollowingAllInTab();
		}
		manageEventsDialog.closeDialog();
		dashboard.switchToMainWindow();
		dashboard.openHomePage();
		ViewClientPage viewClientPage = dashboard.openViewClient();
		viewClientPage.isPageLoaded();
		viewClientPage.searchForClient(client);
		viewClientPage.isPageLoaded();
		sleep(4);
		ClientDetailPage clientDetailPage = viewClientPage.selectResult(client);
		clientDetailPage.isPageLoaded();
		sleep(4);
		clientDetailPage.clientUpdates();
		sleep(4);
		if(!clientDetailPage.isClientBeingFollowed()){
		  clientDetailPage.followClient();
		}
		dashboard.switchToMainWindow();
		dashboard.openHomePage();
		log.info("Prerequiste##Completed client to be in followed State ");
		
		
		log.info("Go to client detail page and ensure that client is not being followed");
		 viewClientPage = dashboard.openViewClient();
		 viewClientPage.isPageLoaded();
		viewClientPage.searchForClient(client);
		viewClientPage.isPageLoaded();
		sleep(5);
		 clientDetailPage = viewClientPage.selectResult(client);
		clientDetailPage.isPageLoaded();
		sleep(5);
		clientDetailPage.clientUpdates();
		sleep(5);
		clientDetailPage.stopFollowingClient();
		sleep(5);
		log.info("Verify that client is not being followed in manage events dialog");
		dashboard.switchToMainWindow();
		dashboard.openHomePage();
		manageEventsDialog =  dashboard.openManageEventsDialog();
		manageEventsDialog.isPageLoaded();
		manageEventsDialog.getSubTab(GC.ClientsImFollowing);
		if(!manageEventsDialog.isItemNotFollowed(client.sSiteID)){
		Assert.assertFalse(true, "Client is being followed when it should not be");
	    }
		manageEventsDialog.closeDialog();
		dashboard.switchToMainWindow();
		dashboard.openHomePage();
		log.info("Follow client");
		viewClientPage = dashboard.openViewClient();
		viewClientPage.isPageLoaded();
		viewClientPage.searchForClient(client);
		viewClientPage.isPageLoaded();
		sleep(5);
		clientDetailPage = viewClientPage.selectResult(client);
		clientDetailPage.isPageLoaded();
		clientDetailPage.clientUpdates();
		sleep(5);
		clientDetailPage.followClient();
		sleep(5);
		dashboard.switchToMainWindow();
	    dashboard.openHomePage();
		manageEventsDialog =  dashboard.openManageEventsDialog();
		manageEventsDialog.isPageLoaded();
		if(!manageEventsDialog.isItemBeingFollowed(client.sSiteID)){
			Assert.assertFalse(true, "Client was not followed when it should be");
		}
		
		manageEventsDialog.closeDialog();
//		clientDetailPage.followClient()
		dashboard.switchToMainWindow();
	    dashboard.openHomePage();
//		manageEventsDialog =  dashboard.openManageEventsDialog();
//		manageEventsDialog.isPageLoaded();
//		   manageEventsDialog.followItem(client.sSiteID);
//		Assert.assertTrue(manageEventsDialog.isItemBeingFollowed(client.sSiteID), "Client was is not being followed when it should not be");
			
		
		log.info("Go to client detail page and ensure that the client is now being followed");
		viewClientPage = dashboard.openViewClient();
		viewClientPage.isPageLoaded();
		viewClientPage.searchForClient(client);
		viewClientPage.isPageLoaded();
		clientDetailPage = viewClientPage.selectResult(client);
		clientDetailPage.isPageLoaded();
		sleep(5);
		clientDetailPage.clientUpdates();
		sleep(5);
		Assert.assertTrue(clientDetailPage.isClientBeingFollowed(), "Client is not being followed as it should be");
		}
		catch(Exception e){
			log.error(e.getMessage());
			commonUserAllocator.checkInAllUsersWithToken("s19282FollowingManagedClientsAssignedTo");
			commonClientAllocator.checkInAllGroupClientWithToken(GC.SC, "s19282FollowingManagedClientsAssignedTo");
		}
	    
	    log.info("End of test method Test_s19282FollowingManagedClientsAssignedTo");
	}
	
	@Test(groups = {"ManageEvents"},dependsOnMethods={"Test_s19282FollowingManagedClientsAssignedTo"})
	public void Test_s19282UnFollowingManagedClientsAssignedTo() {
		 Logger log = LoggerFactory.getLogger(s19282FollowingUnfollowingManagedClientsAssignedTo.class);
		try{
		log.info("Logging in");
		Dashboard dashboard = launchWithLogin(user1);
		
		log.info("Verify that client is being followed in manage events dialog");
		ManageEventsDialog manageEventsDialog =  dashboard.openManageEventsDialog();
		manageEventsDialog.isPageLoaded();
		manageEventsDialog.getSubTab(GC.ClientsImFollowing);
		Assert.assertTrue(manageEventsDialog.isItemBeingFollowed(client.sSiteID), "Client was is not followed when it should be");
		
		log.info("Unfollow client");
		//manageEventsDialog.unfollowItem(client.sSiteID);
		ViewClientPage viewClientPage = dashboard.openViewClient();
		viewClientPage.isPageLoaded();
		viewClientPage.searchForClient(client);
		viewClientPage.isPageLoaded();
		ClientDetailPage clientDetailPage = viewClientPage.selectResult(client);
		clientDetailPage.isPageLoaded();
		sleep(5);
		clientDetailPage.clientUpdates();
		sleep(5);
		clientDetailPage.stopFollowingClient();
		log.info("Verify that client is not being followed in manage events dialog");
		dashboard.switchToMainWindow();
		dashboard.openHomePage();
		manageEventsDialog =  dashboard.openManageEventsDialog();
		manageEventsDialog.isPageLoaded();
		manageEventsDialog.getSubTab(GC.ClientsImFollowing);

		if(!manageEventsDialog.isItemNotFollowed(client.sSiteID)){
		Assert.assertFalse(true, "Client is being followed when it should not be");
	}
		manageEventsDialog.closeDialog();
//		Assert.assertTrue(!manageEventsDialog.isItemBeingFollowed(client.sSiteID), "Client was is being followed when it should not be");
				
		log.info("Go to client detail page and ensure that the client is now not being followed");
		viewClientPage = dashboard.openViewClient();
		viewClientPage.isPageLoaded();
		viewClientPage.searchForClient(client);
		viewClientPage.isPageLoaded();
		sleep(5);
		clientDetailPage = viewClientPage.selectResult(client);
		clientDetailPage.isPageLoaded();
		sleep(5);
		clientDetailPage.clientUpdates();
		sleep(5);
		Assert.assertTrue(!clientDetailPage.isClientBeingFollowed(), "Client is being followed when it should not be");
		}
		finally{
			commonUserAllocator.checkInAllUsersWithToken("s19282FollowingManagedClientsAssignedTo");
			commonClientAllocator.checkInAllGroupClientWithToken(GC.SC, "s19282FollowingManagedClientsAssignedTo");
		}
	}
}
