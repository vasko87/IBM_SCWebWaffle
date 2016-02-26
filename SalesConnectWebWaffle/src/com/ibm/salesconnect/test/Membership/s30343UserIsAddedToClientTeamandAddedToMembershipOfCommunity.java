/**
 * 
 */
package com.ibm.salesconnect.test.Membership;

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
import com.ibm.salesconnect.model.standard.Connections.Community.CommunityDetailPage;
import com.ibm.salesconnect.model.standard.Connections.Community.CommunityMainPage;
import com.ibm.salesconnect.model.standard.Connections.Community.MembersPage;
import com.ibm.salesconnect.objects.Client;


/**
 * @author Tyler Clayton
 * @date July 26, 2013
 * 
 * Removes a user from the client team and checks to see that the change has been reflected in the communities page
 */
public class s30343UserIsAddedToClientTeamandAddedToMembershipOfCommunity extends
		ProductBaseTest {
	Logger log = LoggerFactory.getLogger(s30343UserIsAddedToClientTeamandAddedToMembershipOfCommunity.class);

	User user1 = null;
	User user2 = null;
	PoolClient poolClient = null;
	Client client = new Client();
	
	@Test(groups = {"Membership"})
	public void Test_s30351UserRemovedFromClientNoAccessToCommunity() {
		log.info("Start of test setup for Test_s30351UserRemovedFromClientNoAccessToCommunity");

			user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
			user2 = commonUserAllocator.getUser(this);
			poolClient = commonClientAllocator.getGroupClient(GC.DC, this);
			client.sCCMS_Level = "DC";
			client.sClientID = poolClient.getCCMS_ID();
			client.sClientName = poolClient.getClientName(testConfig.getBrowserURL(), user1);
			client.sSearchIn= GC.showingInClientID;
			client.sSearchFor= GC.searchForAll;
			client.sSearchShowing=GC.showingForClients;
		
		log.info("End of test setup for s30351UserRemovedFromClientNoAccessToCommunity");
		log.info("Start of test for s30351UserRemovedFromClientNoAccessToCommunity");
		
		log.info("Logging in");
		Dashboard dashboard = launchWithLogin(user1);
		
		log.info("Open Client detail page");
		ViewClientPage viewClientPage = dashboard.openViewClient();
		viewClientPage.searchForClient(client);
		ClientDetailPage clientDetailPage = viewClientPage.selectResult(client);
		
		clientDetailPage.removeUserFromClientTeam(user2.getDisplayName());
		
		exec.quit();
		
		CommunityMainPage communityMainPage = launchCnxnCommunity(user1);
		
		CommunityDetailPage communityDetailPage = communityMainPage.searchAndOpenCommunity(client.sClientName, testConfig.getParameter(GC.cxnURL)+"/communities");
		MembersPage membersPage = communityDetailPage.openMembersPage();
		int x = 0;
		
		while(membersPage.checkForMember(user2.getDisplayName())){
			sleep(5);
			x++;
			exec.navigate().refresh();
			if(x==300){
				Assert.assertFalse(true, "Member not removed from list");
			}
		}
		
		
		log.info("User has been removed from client team in connections");
	}
	
		@Test(groups = {"Level3"},dependsOnMethods={"Test_s30351UserRemovedFromClientNoAccessToCommunity"})
		public void Test_s30343UserIsAddedToClientTeamandAddedToMembershipOfCommunity() {
			log.info("Start of test setup for Test_s30343UserIsAddedToClientTeamandAddedToMembershipOfCommunity");
		
		Dashboard dashboard = launchWithLogin(user1);
		
		ViewClientPage viewClientPage = dashboard.openViewClient();
		viewClientPage.searchForClient(client);
		ClientDetailPage clientDetailPage = viewClientPage.selectResult(client);
		
		clientDetailPage.addUserToClientTeam(user2.getDisplayName());
		
		exec.quit();
		
		CommunityMainPage communityMainPage = launchCnxnCommunity(user1);
		
		CommunityDetailPage communityDetailPage = communityMainPage.searchAndOpenCommunity(client.sClientName, testConfig.getParameter(GC.cxnURL)+"/communities");
		MembersPage membersPage = communityDetailPage.openMembersPage();
		int x = 0;
		
		while(!membersPage.checkForMember(user2.getDisplayName())){
			sleep(5);
			x++;
			exec.navigate().refresh();
			if(x==300){
				Assert.assertFalse(true, "Member not removed from list");
			}
		}

			

	}
}