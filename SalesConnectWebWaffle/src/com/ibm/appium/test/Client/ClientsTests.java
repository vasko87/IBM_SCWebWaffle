package com.ibm.appium.test.Client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.appium.MobileBaseTest;
import com.ibm.appium.Objects.Client;
import com.ibm.appium.common.PoolClient;
import com.ibm.appium.model.Dashboard;
import com.ibm.appium.model.Client.ClientDetailPage;
import com.ibm.appium.model.Client.ClientListPage;
import com.ibm.appium.model.Menus.MainMenu;
import com.ibm.atmn.waffle.extensions.user.User;

public class ClientsTests extends MobileBaseTest {
	
	Logger log = LoggerFactory.getLogger(MobileBaseTest.class);
	Client client = new Client();
	
	public static String showMore = "//span[@class='show-more']";
	
	/**
	 * Search for a client in the client list view
	 */
	@Test
	public void s27252ClientListViewSearchMyItems() {

		log.info("Starting method s27252ClientListViewSearchMyItems");		
			log.info("Creating test objects");
			User user = commonUserAllocator.getGroupUser("mobile");
			
			PoolClient poolClient = commonClientAllocator.getGroupClient("MOBILE");
			client.setsName(poolClient.getClientName());
			client.setsCCMSID(poolClient.getCCMS_ID());

			log.info("Using user: " + user.getEmail());
			log.info("Using client: " + client.getsName());
			
		try{
			log.info("Logging in");
			Dashboard dashBoard = launchWithLogin(user);
	
			log.info("Opening Client List View");
			MainMenu mM = dashBoard.openMainMenu();
			ClientListPage clientListPage = mM.openClientListView();
			clientListPage.filterItems(ClientListPage.filterMyClients);
			clientListPage.searchForClient(client);
			sleep(3000);
			log.info("Select the client from the list");
			ClientDetailPage clientDetailPage = clientListPage.selectResult(client);
			
			log.info("Verifying client");
			Assert.assertEquals(clientDetailPage.getBusinessName(), client.getsName(),
					"Client name does not match expected");
			log.info("Client found.");
		}
		finally{
			commonUserAllocator.checkInAllGroupUsers("mobile");
			commonClientAllocator.checkInAllGroupClients("MOBILE");
			log.info("End method s27252ClientListViewSearchMyItems");		

		}
	}

  /**
   * Searches for a client in the Global Search page view
   */	
	@Test
	public void s27252ClientDashboardSearch() {
		log.info("Starting method s27252ClientListViewSearchMyItems");		
		log.info("Creating test objects");
		User user = commonUserAllocator.getGroupUser("mobile");
		
		PoolClient poolClient = commonClientAllocator.getGroupClient("MOBILE");
		client.setsName(poolClient.getClientName());
		client.setsCCMSID(poolClient.getCCMS_ID());

		log.info("Using user: " + user.getEmail());
		log.info("Using client: " + client.getsName());
		
		try{
			
			log.info("Logging in");
			Dashboard dashBoard = launchWithLogin(user);
			
			log.info("Opening Global Search View");
			dashBoard.openGlobalSearchPage();
			
			log.info("Searching for a client");
			ClientDetailPage clientDetailPage = dashBoard.searchForItem(client);
			
			log.info("Verifying client");
			click(showMore);
			Assert.assertEquals(clientDetailPage.getBusinessName(), client.getsName(),
					"Client name does not match expected");
		}
		finally{
			commonUserAllocator.checkInAllGroupUsers("mobile");
			commonClientAllocator.checkInAllGroupClients("MOBILE");
			log.info("End method s27252ClientDashboardSearch");
			
	
		}
	}
	
}
