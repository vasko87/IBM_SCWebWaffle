package com.ibm.salesconnect.test.Level1;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Client.ClientDetailPage;
import com.ibm.salesconnect.model.standard.Client.CreateClientPage;
import com.ibm.salesconnect.model.standard.Client.ViewClientPage;
import com.ibm.salesconnect.objects.Client;

/**
 * <strong>Description:</strong>
 * <br/><br/>
 * Test to validate the View, Read, Convert to Favorite and Search for Favorite Client functionality of the Clients module
 * <br/><br/>
 * 
 * @author 
 * Christeena J Prabhu
 * 

 */
public class AT_FavoriteClient extends ProductBaseTest {
	Logger log = LoggerFactory.getLogger(AT_Sugar.class);
	Client favoriteClient;
	
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Open browser and log in</li>
	 * <li>Create Client</li>
	 * <li>Search for created Client</li>
	 * <li>Open Client detail page</li>
	 * <li>Convert as Favorite</li>
	 * <li>Search for Favorite Client</li>
	 * </ol>
	 */
	
	@Test(groups = { "Level1","AT_Sugar","BVT","BVT1"})//,dependsOnMethods={"Test_AT_Client"}) 
	public void Test_AT_FavoriteClient() throws SQLException{

		log.info("Start of test method Test_AT_FavoriteClient");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
		
		Client client = new Client();
		client.populate();
		favoriteClient=client;
		
		log.info("Logging in");
		Dashboard dashboard = launchWithLogin(user1);

		log.info("Creating Client");
		CreateClientPage createClientPage = dashboard.openCreateClient();
		createClientPage.enterClientInfo(client);
		createClientPage.saveClient();
		
		log.info("Search for created Client");
		ViewClientPage viewClientPage = dashboard.openViewClient();
		viewClientPage.searchForClient(favoriteClient);

		ClientDetailPage clientDetailPage = viewClientPage.selectResult(favoriteClient);
		clientDetailPage.addClientToMyFavorites();
		favoriteClient.bMyFavorites = true;

		log.info("Searching for created client");
		viewClientPage = dashboard.openViewClient();
		viewClientPage.searchForClient(favoriteClient);

		clientDetailPage = viewClientPage.selectResult(favoriteClient);

		Assert.assertEquals(clientDetailPage.getdisplayedClientName(),favoriteClient.sClientName,"Incorrect Client Detail Page was opened");

		commonClientAllocator.checkInAllGroupClients("SC");
		log.info("End of test method Test_AT_FavoriteClient");
	}
	

}
