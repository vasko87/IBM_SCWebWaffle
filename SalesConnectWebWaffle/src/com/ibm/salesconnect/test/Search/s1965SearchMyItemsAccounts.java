package com.ibm.salesconnect.test.Search;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Client.CreateClientPage;
import com.ibm.salesconnect.model.standard.Client.ViewClientPage;
import com.ibm.salesconnect.objects.Client;

/**
 * @author evafarrell
 * @date May 28, 2013
 */
public class s1965SearchMyItemsAccounts extends ProductBaseTest {

	Logger log = LoggerFactory.getLogger(s1965SearchMyItemsAccounts.class);

	@Test(groups = { "Search"})
	public void Test_s1965SearchMyItemsAccounts() {
		log.info("Start test method Test_s1965SearchMyItemsAccounts");

		try{
			log.info("Getting users");
			User baUser1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
			User user1 = commonUserAllocator.getUser(this);

			log.info("Getting client where user is a member");
			PoolClient poolClient = commonClientAllocator.getGroupClient(GC.SC,this);
			Client client = new Client();
			client.sSiteID = poolClient.getCCMS_ID();
			client.sClientName = poolClient.getClientName(testConfig.getBrowserURL(), user1);
			client.sSearchIn= GC.showingInSiteID;
			client.sSearchFor= GC.searchForAll;
			client.sSearchShowing=GC.showingForAll;
			client.myClients=true;

			log.info("Logging in as Business Admin");
			Dashboard dashboard = launchWithLogin(baUser1);

			Client client2 = new Client();		
			client2.populate();
			client2.sSearchIn= GC.showingInClientName;
			client2.sSearchFor= GC.searchForAll;
			client2.sSearchShowing=GC.showingForAll;
			client2.myClients = true;

			log.info("Creating Client where user is not a member");
			dashboard.isPageLoaded();
			CreateClientPage createClientPage = dashboard.openCreateClient();
			createClientPage.enterClientInfo(client2);
			createClientPage.isPageLoaded();
			createClientPage.isPageLoaded();
			createClientPage.saveClient();

			log.info("Close all Browsers and Login as User");
			exec.quit();

			dashboard = launchWithLogin(user1);

			log.info("Search for Client where user is a member");
			ViewClientPage viewClientPage = dashboard.openViewClient();
			viewClientPage.isPageLoaded();
			viewClientPage.searchForClient(client);
			viewClientPage.isPageLoaded();
			viewClientPage.isPresent(viewClientPage.getClientSelection(client.sSiteID, client.sClientName));
//			viewClientPage.scrollElementToMiddleOfBrowser(viewClientPage.getClientSelection(client.sSiteID, client.sClientName));
			Assert.assertTrue(viewClientPage.isPresent(viewClientPage.getClientSelection(client.sSiteID, client.sClientName)),
					"Client was not returned as expected, searched for "+ client.sSiteID + ", " + client.sClientName + " with user " + user1.getDisplayName());

			log.info("Search for Client2 where user is not a member");
			viewClientPage.searchForClient(client2);
			viewClientPage.isPageLoaded();
			Assert.assertFalse(viewClientPage.isPresent(viewClientPage.getClientSelection(client2.sClientName)), "Client was return but not expected");

			log.info("End of test method s1965SearchMyItemsAccounts");
		}
		catch(Exception e){
			log.info(e.getMessage());
			commonUserAllocator.checkInAllUsersWithToken("s1965SearchMyItemsAccounts");
			commonUserAllocator.checkInAllGroupUsersWithToken(GC.busAdminGroup, "s1965SearchMyItemsAccounts");
			commonClientAllocator.checkInAllclientsWithToken("s1965SearchMyItemsAccounts");
			commonClientAllocator.checkInAllGroupClientWithToken(GC.SC, "s1965SearchMyItemsAccounts");
			Assert.assertTrue(false);
		}

	}
}
