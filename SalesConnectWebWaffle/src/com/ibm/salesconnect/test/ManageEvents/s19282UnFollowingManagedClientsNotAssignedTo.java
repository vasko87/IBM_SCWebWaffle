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
 * @date Mar 3, 2014
 */
public class s19282UnFollowingManagedClientsNotAssignedTo extends ProductBaseTest {
			 
	User user1 = null;
	User user2 = null;
	
	PoolClient poolClient = null;
	Client dc = new Client();
	Client site = new Client();
	
	SugarAPI sugarAPI = new SugarAPI();
	
	@Test(groups = {"ManageEvents"})
	public void Test_s19282UnFollowingManagedClientsNotAssignedTo() {
	    Logger log = LoggerFactory.getLogger(s19282UnFollowingManagedClientsNotAssignedTo.class);
	    log.info("Start of test method Test_s19282UnFollowingManagedClientsNotAssignedTo");
		try{
			
			//Get a standard user from the pool
			user1 = commonUserAllocator.getGroupUser(GC.noMemUserGroup,this);
			user2 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
			
			log.info("User 1 (non-member): " + user1.getEmail());
			log.info("User 2 (busAdmin): " + user2.getEmail());
			
			poolClient = commonClientAllocator.getGroupClient("DC",this);

			log.info("Client selected: " + poolClient.getClientName(testConfig.getBrowserURL(), user1));

			//Create a new client site			
			
			String URL = testConfig.getBrowserURL();

			String[] team = new String[1];
			team[0] = user2.getEmail();

			dc.sClientName = poolClient.getClientName(testConfig.getBrowserURL(), user1);
			        
	        dc = sugarAPI.selectClient(poolClient.getCCMS_ID(), URL, user2.getEmail(), user2.getPassword());
	        log.info("DC Client selected: " + dc.sClientName + " " + dc.sClientID + " " + dc.toString() + " PoolClient: " + poolClient.getCCMS_ID());
	        log.info("User creating the site: " + user2.getEmail());
		  	site = sugarAPI.createSite(URL, dc, GC.emptyArray, user2.getEmail(), user2.getPassword());
		  	
			//site.sClientName = poolClient.getClientName();
			site.sSearchIn= GC.showingInSiteID;
			site.sSearchFor= GC.searchForAll;
			site.sSearchShowing=GC.showingForSites;
			site.sSiteID = site.sBeanID;			
			site.sClientName = site.sBeanID;
			site.sClientID = site.sBeanID;
			
		  	//System.out.println("Site number created: " + site.sClientID);
		  	log.info("Client site selected -  SiteID: " + site.sSiteID + " ClientID: " + site.sClientID + " - " + site.sClientName);
		  	
			//Log into SalesConnect as a Standard user with at least 2 clients and opportunities followed
			log.info("Logging in");
			
			Dashboard dashboard = launchWithLogin(user1);
	
			log.info("Go to client detail page and ensure that client is not being followed");
			ViewClientPage viewClientPage = dashboard.openViewClient();
			viewClientPage.searchForClient(site);
			

			ClientDetailPage clientDetailPage = viewClientPage.selectResult(site);
			
			clientDetailPage.clientUpdates();

			//Find a client and FOLLOW the client from the Client page
			clientDetailPage.followClient();

			//Go to the Homepage Recent Events widget
			log.info("Verify that client is not being followed in manage events dialog");
			dashboard.switchToMainWindow();
			dashboard.openHomePage();

			//Click on the Manage Events
			ManageEventsDialog manageEventsDialog =  dashboard.openManageEventsDialog();
			
			//Click on 'Clients I'm Not Assigned To' getSubTab just returns the value
			manageEventsDialog.openSubTab(GC.ClientsImNotFollowing);
			
			//Verify that the Client the User is not a member of  is listed
			Assert.assertTrue(manageEventsDialog.isItemBeingFollowed(site.sClientID), "Client is not being followed when it should be");
			manageEventsDialog.closeDialog();
			//-----------//
			
			//Click the Stop Following buttons next to the client								
			log.info("UnFollow client");
//			manageEventsDialog.unfollowItem(site.sClientID);
//			manageEventsDialog.closeDialog();
			viewClientPage = dashboard.openViewClient();
			viewClientPage.searchForClient(site);
			clientDetailPage = viewClientPage.selectResult(site);
			clientDetailPage.clientUpdates();
			clientDetailPage.stopFollowingClient();
						
			
			//Navigate to the Client's detail view
			log.info("Go to client detail page and ensure that the client is NOT being followed");
			viewClientPage = dashboard.openViewClient();
			viewClientPage.searchForClient(site);
			clientDetailPage = viewClientPage.selectResult(site);
			
			clientDetailPage.clientUpdates();
			//Assert.assertTrue(clientDetailPage.isClientBeingFollowed(), "Client is not being followed as it should be");
			
			//Verify you are no longer following the Client
			if(manageEventsDialog.isItemBeingFollowed(site.sClientID)){
				Assert.assertFalse(true, "Client is being followed when it should not be");
			}
		}
		catch(Exception e){
			log.error(e.getMessage());
			commonUserAllocator.checkInAllUsersWithToken(this);
			commonClientAllocator.checkInAllGroupClientWithToken(GC.DC, this);
		}
	    
	    log.info("End of test method Test_s19282UnFollowingManagedClientsNotAssignedTo");
	}
	
}
