package com.ibm.salesconnect.test.Collab;

import java.util.Random;

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
import com.ibm.salesconnect.model.standard.Collab.OpportunityMicroBloggingFrame;
import com.ibm.salesconnect.model.standard.Connections.Community.CommunityDetailPage;
import com.ibm.salesconnect.model.standard.Connections.Community.CommunityMainPage;
import com.ibm.salesconnect.model.standard.Opportunity.OpportunityDetailPage;
import com.ibm.salesconnect.model.standard.Opportunity.ViewOpportunityPage;
import com.ibm.salesconnect.objects.Client;
import com.ibm.salesconnect.objects.Opportunity;

/**
 * @author evafarrell
 * @date Nov 13, 2013
 */


public class s19282OpptyUpdateTabAtMentions extends ProductBaseTest {
	
	Logger log = LoggerFactory.getLogger(s19282OpptyUpdateTabAtMentions.class);
	int rand = new Random().nextInt(100000);
	String contactID = "22SC-" + rand;
	String opptyID = null;
	SugarAPI sugarAPI = new SugarAPI();
	Opportunity oppt = new Opportunity();
	
	@Test(groups = { "Collab","LC"})
	public void test_s19282OpptyUpdateTabAtMentions() {
		
		log.info("Start of test method s19282OpptyUpdateTabAtMentions");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
		User user2 = commonUserAllocator.getUser(this);
		PoolClient poolClient = commonClientAllocator.getGroupClient("DC",this);
		Client client = new Client();
		client.sClientID = poolClient.getCCMS_ID();
		client.sClientName = poolClient.getClientName(testConfig.getBrowserURL(), user1);		
		client.sCCMS_Level = "DC";

		sugarAPI.createContact(testConfig.getBrowserURL(), contactID, client.sClientID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");
		opptyID = sugarAPI.createOppty(testConfig.getBrowserURL(), "", contactID, client.sClientID, user1.getEmail(), user1.getPassword(), GC.emptyArray);
		oppt.sOpptNumber = opptyID;
		oppt.sPrimaryContact = "ContactFirst ContactLast";
		oppt.sAccID = client.sClientID;


			String Status = "Hello "+GC.sUniqueNum+" "+"@"+user2.getLastName()+" "+user2.getFirstName();
			
			log.info("Logging in as User A");
			Dashboard dashboard = launchWithLogin(user1);

			log.info("Open Opportunity detail page");
			ViewOpportunityPage viewOpportunityPage = dashboard.openViewOpportunity();
			viewOpportunityPage.searchForOpportunity(oppt);
			OpportunityDetailPage opportunityDetailPage = viewOpportunityPage.selectResult(oppt);
			
			log.info("Open Updates Tab");
			opportunityDetailPage.opptyUpdates();
			
			log.info("Need to Post a Status in the main Status Textbox to populate the Micro Blog");
			opportunityDetailPage.postStatus(Status);
			
			log.info("In Updates tab->Recent Discussion (column), enter post @userB");
			OpportunityMicroBloggingFrame opportunityMicroBloggingFrame = dashboard.switchToOpptyMicroBloggingFrame();
			opportunityDetailPage.postStatusInMicroBlog(Status);
			Assert.assertTrue(opportunityMicroBloggingFrame.verifyStatus(Status), "Status does not exist in Activity Stream");
			
			log.info("In Updates tab->Recent Discussion (column), verify posted message contains hyper link to userB.");
			opportunityDetailPage.verifyUserHyperLink(user2.getFirstName()+" "+user2.getLastName());
			exec.quit();
			
			log.info("Logon to SalesConnect with UserB, verify Homepage AS contains post from userA");
			dashboard = launchWithLogin(user2);
			
			log.info("Open Opportunity detail page");
			viewOpportunityPage = dashboard.openViewOpportunity();
			viewOpportunityPage.searchForOpportunity(oppt);
			opportunityDetailPage = viewOpportunityPage.selectResult(oppt);
			
			log.info("Open Updates Tab");
			opportunityDetailPage.opptyUpdates();
			sleep(2);//Sleep to allow the AS to populate
			dashboard.switchToOpptyMicroBloggingFrame();
			Assert.assertTrue(opportunityMicroBloggingFrame.verifyStatus(Status), "Status does not exist in Activity Stream");
			exec.quit();
			
			log.info("Logon to Connections with userB and navigate to GUC community (associated with the same oppty in SaleConnect)");
			CommunityMainPage communityMainPage = launchCnxnCommunity(user2);
			CommunityDetailPage communityDetailPage = communityMainPage.searchAndOpenCommunity(client.sClientName,testConfig.getParameter(GC.cxnURL)+"/communities");
			
			log.info("As userB, in GUC community->Recent Updates, verify userA's comments (associated with oppty) are displayed");
			communityDetailPage.openRecentUpdates();
			communityDetailPage.verifyStatusExists(Status);

			log.info("Remove the oppty created for this test");
			sugarAPI.deleteOpptySOAP(testConfig.getBrowserURL(), opptyID, user1.getEmail(), user1.getPassword());
			sugarAPI.deleteContactSOAP(testConfig.getBrowserURL(), contactID, user1.getEmail(), user1.getPassword());
			
		log.info("End of test method s19282OpptyUpdateTabAtMentions");
		
		commonUserAllocator.checkInAllUsersWithToken(this);
		commonUserAllocator.checkInAllGroupUsersWithToken(GC.db2UserGroup,this);
	}
}
