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
import com.ibm.salesconnect.common.DB2Connection;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Client.ClientDetailPage;
import com.ibm.salesconnect.model.standard.Client.ViewClientPage;
import com.ibm.salesconnect.model.standard.Connections.Community.CommunityDetailPage;
import com.ibm.salesconnect.model.standard.Connections.Community.CommunityMainPage;
import com.ibm.salesconnect.model.standard.Connections.Community.MembersPage;
import com.ibm.salesconnect.objects.Client;


/**
 * @author Tyler Clayton
 * @date July 26, 2013
 * 
 * Removes a user from the client(site) team and checks to see that the change has not been reflected in the communities page
 */
public class s30358UserRemovedfromConnectionsGUCNotClient extends
		ProductBaseTest {
	Logger log = LoggerFactory.getLogger(s30358UserRemovedfromConnectionsGUCNotClient.class);

	@Test(groups = {"Membership"})
	public void Test_s30358UserRemovedfromConnectionsGUCNotClient() throws SQLException, InterruptedException {
		log.info("Start of test setup for s30358UserRemovedfromConnectionsGUCNotClient");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
		//User user2 = commonUserAllocator.getUser(this);
		User db2User = commonUserAllocator.getGroupUser(GC.db2UserGroup,this);
		DB2Connection db2 = new DB2Connection(getParameter(GC.db2URL),db2User.getUid(),db2User.getPassword());
		
		PoolClient poolClient = commonClientAllocator.getGroupClient(GC.DC, this);
		Client client = db2.retrieveClient(poolClient, user1.getEmail(), testConfig.getParameter(GC.testPhase));
		
		client.sSearchIn= GC.showingInClientID;
		client.sSearchFor= GC.searchForAll;
		client.sSearchShowing=GC.showingForClients;
		
		log.info("End of test setup for s30358UserRemovedfromConnectionsGUCNotClient");
		log.info("Start of test for s30358UserRemovedfromConnectionsGUCNotClient");
		
		log.info("Logging in");
		Dashboard dashboard = launchWithLogin(user1);
		
		log.info("Open Client detail page");
		ViewClientPage viewClientPage = dashboard.openViewClient();
		viewClientPage.searchForClient(client);
		ClientDetailPage clientDetailPage = viewClientPage.selectResult(client);
		
		//clientDetailPage.removeUserFromClientTeam(user2.getDisplayName());
		if(!clientDetailPage.checkUserInClientTeam("in01 bin01")){
			clientDetailPage.addUserToClientTeam("in01 bin01");
		}
		
		exec.quit();
		
		CommunityMainPage communityMainPage = launchCnxnCommunity(user1);
		
		CommunityDetailPage communityDetailPage = communityMainPage.searchAndOpenCommunity(client.sClientName, testConfig.getParameter(GC.cxnURL)+"/communities");
		MembersPage membersPage = communityDetailPage.openMembersPage();
		membersPage.waitForMemberToBeAdded("in01 bin01");
		membersPage.removeMember("in01 bin01");
		
		exec.quit();
		
		dashboard = launchWithLogin(user1);
		
		viewClientPage = dashboard.openViewClient();
		viewClientPage.searchForClient(client);
		clientDetailPage = viewClientPage.selectResult(client);
		int x = 0;
		
		while(clientDetailPage.checkUserInClientTeam("in01 bin01")){
			Thread.sleep(1000*60);
			x++;
			exec.navigate().refresh();
			if(x==10){
				break;
			}
		}
		
		boolean result = clientDetailPage.checkUserInClientTeam("in01 bin01");
		
		exec.quit();
		
		communityMainPage = launchCnxnCommunity(user1);
		
		communityDetailPage = communityMainPage.searchAndOpenCommunity(client.sClientName, testConfig.getParameter(GC.cxnURL)+"/communities");
		membersPage = communityDetailPage.openMembersPage();
		
		membersPage.addMember("in01 bin01");
		
		Assert.assertTrue(result, "DC user removed via connections");
		
		commonClientAllocator.checkInAllGroupClientWithToken(GC.DC, this);
		
		log.info("End of test method s30358UserRemovedfromConnectionsGUCNotClient");
	}
}