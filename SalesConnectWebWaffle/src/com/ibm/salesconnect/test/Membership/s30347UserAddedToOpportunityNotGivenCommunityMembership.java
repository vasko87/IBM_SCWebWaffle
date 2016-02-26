/**
 * 
 */
package com.ibm.salesconnect.test.Membership;

import java.sql.SQLException;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.SugarAPI;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.DB2Connection;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.partials.EditTeamMembersSubpanel;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Client.ClientDetailPage;
import com.ibm.salesconnect.model.standard.Client.ViewClientPage;
import com.ibm.salesconnect.model.standard.Connections.Community.CommunityDetailPage;
import com.ibm.salesconnect.model.standard.Connections.Community.CommunityMainPage;
import com.ibm.salesconnect.model.standard.Connections.Community.MembersPage;
import com.ibm.salesconnect.model.standard.Opportunity.EditOpportunityPage;
import com.ibm.salesconnect.model.standard.Opportunity.OpportunityDetailPage;
import com.ibm.salesconnect.model.standard.Opportunity.ViewOpportunityPage;
import com.ibm.salesconnect.objects.Client;
import com.ibm.salesconnect.objects.Opportunity;


/**
 * @author Tyler Clayton
 * @date July 11, 2013
 */
public class s30347UserAddedToOpportunityNotGivenCommunityMembership extends
		ProductBaseTest {
	Logger log = LoggerFactory.getLogger(s30347UserAddedToOpportunityNotGivenCommunityMembership.class);

	int rand = new Random().nextInt(100000);
	String contactID = "22SC-" + rand;
	String opptyID = null;
	String clientID = null;
	SugarAPI sugarAPI = new SugarAPI();
	Opportunity oppt = new Opportunity();
	
	@Test(groups = {"Membership"})
	public void Test_s30347UserAddedToOpportunityNotGivenCommunityMembership() throws SQLException, InterruptedException {
		log.info("Start of test setup for s30347UserAddedToOpportunityNotGivenCommunityMembership");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
		User db2User = commonUserAllocator.getGroupUser(GC.db2UserGroup,this);
		DB2Connection db2 = new DB2Connection(getParameter(GC.db2URL),db2User.getUid(),db2User.getPassword());
		
		PoolClient poolClient = commonClientAllocator.getGroupClient(GC.DC, this);
		Client client = db2.retrieveClient(poolClient, user1.getEmail(), testConfig.getParameter(GC.testPhase));

		sugarAPI.createContact(testConfig.getBrowserURL(), contactID, clientID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");
		opptyID = sugarAPI.createOppty(testConfig.getBrowserURL(), "", contactID, clientID, user1.getEmail(), user1.getPassword(), GC.emptyArray);
		oppt.sOpptNumber = opptyID;
		oppt.sPrimaryContact = "ContactFirst ContactLast";
		oppt.sAccID = clientID;
		
		log.info("End of test setup for s30347UserAddedToOpportunityNotGivenCommunityMembership");
		log.info("Start of test for s30347UserAddedToOpportunityNotGivenCommunityMembership");
		
		client.sSearchIn= GC.showingInClientID;
		client.sSearchFor= GC.searchForAll;
		client.sSearchShowing=GC.showingForClients;
		
		log.info("Logging in");
		Dashboard dashboard = launchWithLogin(user1);
		
		log.info("Open client detail page");
		ViewClientPage viewClientPage = dashboard.openViewClient();
		viewClientPage.searchForClient(client);
		ClientDetailPage clientDetailPage = viewClientPage.selectResult(client);
		
		clientDetailPage.removeUserFromClientTeam("in01 bin01");
		
		log.info("Open Opportunity detail page");
		ViewOpportunityPage viewOpptyPage = dashboard.openViewOpportunity();
		viewOpptyPage.searchForOpportunity(oppt);
		OpportunityDetailPage opptyDetailPage = viewOpptyPage.selectResult(oppt);
		
		int rowNumber = opptyDetailPage.getTeamMemberRowNumber("in01 bin01");
		
		log.info("Edit Opportunity");
		EditOpportunityPage editOpptyPage = opptyDetailPage.openEditOpportunity();
		EditTeamMembersSubpanel editTeamMemSub = new EditTeamMembersSubpanel(exec);
		
		editTeamMemSub.removeOpportunityTeamMember(rowNumber);
		editTeamMemSub.addOpportunityTeamMember("in01 bin01", rowNumber+1, GC.gsTeamMember);
		
		editOpptyPage.saveOpportunity();
		
		exec.quit();
		
		/*check to see if user is showing up in communities*/
		CommunityMainPage communityMainPage = launchCnxnCommunity(user1);
		communityMainPage.openMyCommunities();
		CommunityDetailPage communityDetailPage = communityMainPage.searchAndOpenCommunity(client.sClientName,testConfig.getParameter(GC.cxnURL)+"/communities");
		
		MembersPage membersPage = communityDetailPage.openMembersPage();
		int x=0;
		while(membersPage.checkForMember("in01 bin01")){
			x++;
			exec.navigate().refresh();
			Thread.sleep(60*1000);
			if(x==10){
				break;
			}
		}
		Assert.assertFalse(membersPage.checkForMember("in01 bin01"), "User was found in membership when not a part of client team.");
		exec.quit();
		
		log.info("Logging in");
		dashboard = launchWithLogin(user1);
		
		log.info("Open client detail page");
		viewClientPage = dashboard.openViewClient();
		viewClientPage.searchForClient(client);
		clientDetailPage = viewClientPage.selectResult(client);
		
		clientDetailPage.addUserToClientTeam("in01 bin01");
		
		log.info("Remove the oppty created for this test");
		sugarAPI.deleteOpptySOAP(testConfig.getBrowserURL(), opptyID, user1.getEmail(), user1.getPassword());
		sugarAPI.deleteContactSOAP(testConfig.getBrowserURL(), contactID, user1.getEmail(), user1.getPassword());
		log.info("End of test method s30347UserAddedToOpportunityNotGivenCommunityMembership");
	}
}