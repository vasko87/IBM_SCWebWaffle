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
public class s30364UserRemovedFromClientTeamStillInGUC extends
		ProductBaseTest {
	Logger log = LoggerFactory.getLogger(s30364UserRemovedFromClientTeamStillInGUC.class);

	@Test(groups = {"Membership"})
	public void Test_s30364UserRemovedFromClientTeamStillInGUC() throws SQLException, InterruptedException {
		log.info("Start of test setup for s30364UserRemovedFromClientTeamStillInGUC");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
		//User user2 = commonUserAllocator.getUser(this);
		User db2User = commonUserAllocator.getGroupUser(GC.db2UserGroup,this);
		DB2Connection db2 = new DB2Connection(getParameter(GC.db2URL),db2User.getUid(),db2User.getPassword());
		
		PoolClient poolClient = commonClientAllocator.getGroupClient(GC.SC, this);
		Client client = db2.retrieveClient(poolClient, user1.getEmail(), testConfig.getParameter(GC.testPhase));
		
		client.sSearchIn= GC.showingInSiteID;
		client.sSearchFor= GC.searchForAll;
		client.sSearchShowing=GC.showingForSites;
		
		log.info("End of test setup for s30351UserRemovedFromClientNoAccessToCommunity");
		log.info("Start of test for s30351UserRemovedFromClientNoAccessToCommunity");
		
		log.info("Logging in");
		Dashboard dashboard = launchWithLogin(user1);
		
		log.info("Open Client detail page");
		ViewClientPage viewClientPage = dashboard.openViewClient();
		viewClientPage.searchForClient(client);
		ClientDetailPage clientDetailPage = viewClientPage.selectResult(client);
		
		//clientDetailPage.removeUserFromClientTeam(user2.getDisplayName());
		clientDetailPage.removeUserFromClientTeam("in01 bin01");
		
		exec.quit();
		
		CommunityMainPage communityMainPage = launchCnxnCommunity(user1);
		
		CommunityDetailPage communityDetailPage = communityMainPage.searchAndOpenCommunity(client.sClientName, testConfig.getParameter(GC.cxnURL)+"/communities");
		MembersPage membersPage = communityDetailPage.openMembersPage();
		int x = 0;
		
		while(membersPage.checkForMember("in01 bin01")){
			Thread.sleep(1000*60);
			x++;
			exec.navigate().refresh();
			if(x==10){
				break;
			}
		}
		
		if(!membersPage.checkForMember("in01 bin01")){
			Assert.assertTrue(false,"User was removed from GUC");
		}
		
		exec.quit();
		
		dashboard = launchWithLogin(user1);
		
		viewClientPage = dashboard.openViewClient();
		viewClientPage.searchForClient(client);
		clientDetailPage = viewClientPage.selectResult(client);
		
		clientDetailPage.addUserToClientTeam("in01 bin01");
		
		
		commonClientAllocator.checkInAllGroupClientWithToken(GC.SC, this);
		
		log.info("End of test method s30364UserRemovedFromClientTeamStillInGUC");
	}
}