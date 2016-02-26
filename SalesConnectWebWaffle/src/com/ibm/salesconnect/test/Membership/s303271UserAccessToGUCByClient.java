/**
 * 
 */
package com.ibm.salesconnect.test.Membership;

import java.sql.SQLException;

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
import com.ibm.salesconnect.model.standard.Connections.Community.CommunityMainPage;
import com.ibm.salesconnect.objects.Client;

/**
 * @author evafarrell
 * @date Aug 22, 2013
 */
public class s303271UserAccessToGUCByClient extends	ProductBaseTest {
	
	User user1 = null;
	User user2 = null;
	PoolClient poolClient = null;
	Client client = new Client();
	
	@Test(groups = {"Membership"})
	public void testMain() throws SQLException, InterruptedException {
		Logger log = LoggerFactory.getLogger(s303271UserAccessToGUCByClient.class);
		
		user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
		user2 = commonUserAllocator.getGroupUser(GC.noMemUserGroup,this);
		poolClient = commonClientAllocator.getGroupClient("SC",this);
		client.sCCMS_Level = "SC";
		client.sSearchIn= GC.showingInSiteID;
		client.sSearchFor= GC.searchForAll;
		client.sSearchShowing=GC.showingForAll;
		client.sSiteID = poolClient.getCCMS_ID();
		client.sClientName = poolClient.getClientName(testConfig.getBrowserURL(), user1);
		log.info("Client selected: " + client.sClientName);

		log.info("Log in to SalesConnet");
		Dashboard dashboard = launchWithLogin(user1);
		
		log.info("Open Client detail page");
		ViewClientPage viewClientPage = dashboard.openViewClient();
		viewClientPage.searchForClient(client);
		ClientDetailPage clientDetailPage = viewClientPage.selectResult(client);
		
		log.info("Add "+user2.getDisplayName()+" to "+client.sClientName+"'s Client Team");
		clientDetailPage.addUserToClientTeam(user2.getDisplayName());
		
		exec.quit();
		
		log.info("Log in to Connections as "+user2.getDisplayName()+" and Verify They cannot search for "+client.sClientName+"'s community");
		CommunityMainPage communityMainPage = launchCnxnCommunity(user2);
		
		communityMainPage.searchCommunity(client.sClientName, testConfig.getParameter(GC.cxnURL)+"/communities");
		Assert.assertFalse(communityMainPage.verifyCommunitySearchResults(client.sClientName),"Community appears in "+user2+ " Community search. This user should not be able to search for this community as they should not be on the Client Team");
				
		log.info("Script Cleanup - Remove User from Client Team");
		log.info("Log in to SalesConnet");
		dashboard = launchWithLogin(user1);
		
		log.info("Open Client detail page");
		dashboard.openViewClient();
		viewClientPage.searchForClient(client);
		viewClientPage.selectResult(client);
		
		log.info("Remove "+user2.getDisplayName()+" from "+client.sClientName+"'s Client Team");
		clientDetailPage.removeUserFromClientTeam(user2.getDisplayName());
		
		commonClientAllocator.checkInAllGroupClientWithToken(GC.SC, this);
		
		log.info("End of test method s303271UserAccessToGUCByClient");
	}
}