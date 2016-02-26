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

@Test(groups = {"ManageEvents1"})
public class s19282FollowFromClientShowsInManageNotifications extends ProductBaseTest{


	public void Test_s19282FollowFromClientShowsInManageNotifications() {
		    Logger log = LoggerFactory.getLogger(s19282FollowFromClientShowsInManageNotifications.class);
		    log.info("Start of test method s19282FollowFromClientShowsInManageNotifications");
		    User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);

			PoolClient poolClient = commonClientAllocator.getGroupClient("SC",this);
			Client client = new Client();
			client.sSiteID = poolClient.getCCMS_ID();
			client.sClientName = poolClient.getClientName(testConfig.getBrowserURL(), user1);
			log.info("Client selected: " + client.sClientName);
			
			client.sCCMS_Level = "SC";
			
			//Client variables
			client.sSearchIn= GC.showingInSiteID;
			client.sSearchFor= GC.searchForAll;
			client.sSearchShowing=GC.showingForAll;

			log.info("Logging in");
			Dashboard dashboard = launchWithLogin(user1);
			log.info("Prerequiste##Start Setting the given oppty to be in followed state ");
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
			if(clientDetailPage.isClientBeingFollowed()){
			  clientDetailPage.stopFollowingClient();
			}
			dashboard.switchToMainWindow();
			dashboard.openHomePage();
			log.info("Prerequiste##Completed Setting the given oppty to be in followed State ");
			
			//TESTCASE ENVIRONMENT SETUP COMPLETE

			viewClientPage = dashboard.openViewClient();
			viewClientPage.searchForClient(client);
			viewClientPage.isPageLoaded();
			sleep(4);
			clientDetailPage = viewClientPage.selectResult(client);
			clientDetailPage.isPageLoaded();
			sleep(4);
			clientDetailPage.clientUpdates();
			clientDetailPage.followClient();
			dashboard.switchToMainWindow();
		    dashboard.openHomePage();
		    manageEventsDialog =  dashboard.openManageEventsDialog();
			manageEventsDialog.isPageLoaded();
			if(!manageEventsDialog.isItemBeingFollowed(client.sSiteID)){
				Assert.assertFalse(true, "Client was not followed sucessfully.");
			}
			
			manageEventsDialog.closeDialog();
			
			viewClientPage = dashboard.openViewClient();
			viewClientPage.searchForClient(client);
			viewClientPage.isPageLoaded();
			sleep(4);
			clientDetailPage = viewClientPage.selectResult(client);
			 clientDetailPage.isPageLoaded();
			clientDetailPage.clientUpdates();
			clientDetailPage.stopFollowingClient();
			dashboard.switchToMainWindow();
			dashboard.openHomePage();
			manageEventsDialog =  dashboard.openManageEventsDialog();
			dashboard.isPageLoaded();
			if(!manageEventsDialog.isItemNotFollowed(client.sSiteID)){
				Assert.assertFalse(true, "Client was still followed when it should not be.");
			}
			
			log.info("End of test method s19282FollowFromClientShowsInManageNotifications");
	}
}
