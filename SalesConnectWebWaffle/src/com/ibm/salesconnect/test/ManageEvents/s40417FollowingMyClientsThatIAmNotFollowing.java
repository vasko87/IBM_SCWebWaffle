package com.ibm.salesconnect.test.ManageEvents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.SugarAPI;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Client.ClientDetailPage;
import com.ibm.salesconnect.model.standard.Client.ViewClientPage;
import com.ibm.salesconnect.model.standard.Collab.ManageEventsDialog;
import com.ibm.salesconnect.objects.Client;

/**
 * @author Peter Poliwoda
 * @date Aug 18, 2014
 */
public class s40417FollowingMyClientsThatIAmNotFollowing extends ProductBaseTest {
		
		Logger log = LoggerFactory.getLogger(s19282ManageEventsUI.class);
		String clientID = null;
		User user1 = null;
		User user2 = null;
		
		PoolClient poolClient = null;
		Client dc = new Client();
		Client site = new Client();
		
		SugarAPI sugarAPI = new SugarAPI();
		
		@Test(groups = {"ManageEvents"})
		public void Test_s19282FollowingMyClientsThatIAmNotFollowing() {
			log.info("Start test method Test_s19282FollowingMyClientsThatIAmNotFollowing");
			
			log.info("Getting users");
			//Standard user and a business user to create a site to follow
			user1 = commonUserAllocator.getUser(this);
			user2 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
			
			log.info("Get client from CSV file");
			//clientID = commonClientAllocator.getGroupClient(GC.SC,"s40417FollowingMyClientsThatIAmNotFollowing").getCCMS_ID();	
			log.info("User 1 (non-member): " + user1.getEmail());
			
			poolClient = commonClientAllocator.getGroupClient("DC",this);
			log.info("Client selected: " + poolClient.getClientName(testConfig.getBrowserURL(), user1));

			//Create a new client site			
			String URL = testConfig.getBrowserURL();
			String[] team = new String[1];
			team[0] = user1.getEmail();

			dc.sClientName = poolClient.getClientName(testConfig.getBrowserURL(), user1);
	        dc = sugarAPI.selectClient(poolClient.getCCMS_ID(), URL, user1.getEmail(), user1.getPassword());
	        log.info("DC Client selected: " + dc.sClientName + " " + dc.sClientID + " " + dc.toString() + " PoolClient: " + poolClient.getCCMS_ID());
	      
		  	site = sugarAPI.createSite(URL, dc, team, user2.getEmail(), user2.getPassword());
		  	site.sSiteID = site.sBeanID;
		  	clientID = site.sSiteID;
		  	site.sClientName = site.sSiteID;
			site.sSearchIn= GC.showingInClientName;
			site.sSearchFor= GC.searchForAll;
			site.sSearchShowing=GC.showingForSites;
			
		  	//Log in to SalesConnect
			log.info("Launch and log in");
			Dashboard dashboard = launchWithLogin(user1);
			
			log.info("Open Manage Events dialog");
			ManageEventsDialog manageEventsDialog = dashboard.openManageEventsDialog();
		    
			manageEventsDialog.openSubTab(GC.ClientsImNotFollowing);
		    
			Assert.assertTrue(manageEventsDialog.isTabPresent(GC.Clients), "Client tab is not present");		
			Assert.assertTrue(manageEventsDialog.isItemNotFollowed(clientID), "Client is being followed when it should not be");

			//Click on Start Following
			log.info("Clicking on Start Following client " + clientID);
			manageEventsDialog.followItem(clientID);
			manageEventsDialog.openSubTab(GC.ClientsImFollowing);
			Assert.assertTrue(manageEventsDialog.isItemBeingFollowed(clientID), "Client is not being followed when it should be");					
			manageEventsDialog.closeDialog();
			
			log.info("Go to client detail page and see if followed");
			ViewClientPage viewClientPage = dashboard.openViewClient();			
			viewClientPage.searchForClient(site);
			
			ClientDetailPage clientDetailPage = viewClientPage.selectResult(site);
			clientDetailPage.clientUpdates();

			log.info("Verify if following Client");
			Assert.assertTrue(clientDetailPage.isClientBeingFollowed(), "Client is not being followed");
			
			log.info("End of method Test_s19282FollowingMyClientsThatIAmNotFollowing");

			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
}
