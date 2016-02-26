/**
 * 
 */
package com.ibm.salesconnect.test.Collab;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.SugarAPI;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.partials.LineItemDetailPage;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Collab.ActivityStreamFrame;
import com.ibm.salesconnect.model.standard.Collab.OpportunityActivityStreamFrame;
import com.ibm.salesconnect.model.standard.Opportunity.OpportunityDetailPage;
import com.ibm.salesconnect.model.standard.Opportunity.ViewOpportunityPage;
import com.ibm.salesconnect.objects.Opportunity;
import com.ibm.salesconnect.objects.RevenueItem;

/**
 * @author evafarrell
 * @date Jan 9, 2014
 */


public class s1816UnfollowOpptyActivityStream extends ProductBaseTest {
	
	Logger log = LoggerFactory.getLogger(s1816UnfollowOpptyActivityStream.class);
	
	int rand = new Random().nextInt(100000);
	String contactID = "22SC-" + rand;
	String opptyID = "33SC-" + rand;
	String rliID = "44SC-" + rand;
	String clientID = null;
	SugarAPI sugarAPI = new SugarAPI();
	Opportunity oppt = new Opportunity();
	RevenueItem rli = new RevenueItem();	
	
	@Test(groups = {})
	public void testMain() {
		
		log.info("Start of test method s1816UnfollowOpptyActivityStream");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
		User user2 = commonUserAllocator.getUser(this);
		clientID = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();

		sugarAPI.createRLISOAP(testConfig.getBrowserURL(), rliID, user1.getEmail(), user1.getPassword());
		sugarAPI.createContact(testConfig.getBrowserURL(), contactID, clientID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");
		sugarAPI.createOpptySOAP(testConfig.getBrowserURL(), opptyID, rliID, contactID, clientID, user1.getEmail(), user1.getPassword(), GC.emptyArray);oppt.sOpptNumber = opptyID;
		oppt.sPrimaryContact = "ContactFirst ContactLast";
		oppt.sAccID = clientID;


		try {

			log.info("Logon to SalesConnect with UserB, Check UserB not not receiving events from this Oppty and then Follow the Oppty");
			Dashboard dashboard =  launchWithLogin(user2);
			
			log.info("Open Opportunity detail page");
			ViewOpportunityPage viewOpportunityPage = dashboard.openViewOpportunity();
			viewOpportunityPage.searchForOpportunity(oppt);
			OpportunityDetailPage opportunityDetailPage = viewOpportunityPage.selectResult(oppt);
						
			log.info("Open Updates Tab, Verify no events showing, Follow Oppty");
			opportunityDetailPage.opptyUpdates();
			opportunityDetailPage.isOpptyBeingFollowed();
			opportunityDetailPage.followOpportunity();
			Assert.assertTrue(opportunityDetailPage.verifyActivityStream(), "Activity Stream did not load successfully");
			
			log.info("Logon to SalesConnect with UserA, Update RLI Amount to trigger Oppty Event");
			exec.close();
			dashboard = launchWithLogin(user1);
			
			log.info("Open Opportunity detail page");
			viewOpportunityPage = dashboard.openViewOpportunity();
			viewOpportunityPage.searchForOpportunity(oppt);
			opportunityDetailPage = viewOpportunityPage.selectResult(oppt);
			
			log.info("Verify Updates Tab - Activity Stream Displays Correctly");
			opportunityDetailPage.opptyUpdates();
			opportunityDetailPage.followOpportunity();
			Assert.assertTrue(opportunityDetailPage.verifyActivityStream(), "Activity Stream did not load successfully");
			opportunityDetailPage.openOverviewTab();
			
			log.info("Update RLI");
			rli.sRevenueAmount = "2525";
			rli.sFindOffering = "Lenovo";
			rli.sOwner = user1.getDisplayName();
			
			LineItemDetailPage lineItemDetailPage = opportunityDetailPage.selectEditRli();
			lineItemDetailPage.editLineItemInfo(rli);

			
			log.info("Logon to SalesConnect with UserB, Verify that an Event exists in the Oppty AS for the RLI Update");
			exec.close();
			dashboard = launchWithLogin(user2);
			
			log.info("Open Opportunity detail page");
			viewOpportunityPage = dashboard.openViewOpportunity();
			viewOpportunityPage.searchForOpportunity(oppt);
			opportunityDetailPage = viewOpportunityPage.selectResult(oppt);
			
			log.info("Open Updates Tab, Verify RLI Update Event Exists");
			opportunityDetailPage.opptyUpdates();
			opportunityDetailPage.isOpptyBeingFollowed();
			opportunityDetailPage.followOpportunity();
			Assert.assertTrue(opportunityDetailPage.verifyActivityStream(), "Activity Stream did not load successfully");
						
			log.info("Refresh Activity Stream and verify that RLI Update Event shows up");
			opportunityDetailPage.scrollToTopOfOpportunity();
			OpportunityActivityStreamFrame opportunityActivityStreamFrame = dashboard.switchToOpptyActivityStreamFrame();
			opportunityActivityStreamFrame.refreshEventsStream();
			Assert.assertTrue(opportunityActivityStreamFrame.verifyEntry(rli.sRevenueAmount), "RLI Update Event does not exist in Activity Stream");
			
			log.info("Verify Event appears in Homepage Recent Events");
			dashboard.switchToMainWindow();
			dashboard.openHomePage();
			ActivityStreamFrame activityStreamFrame = dashboard.switchToActivityStreamFrame();
			Assert.assertTrue(activityStreamFrame.verifyEntry(rli.sRevenueAmount), "RLI Update Event does not exist in Activity Stream");
			
			log.info("As UserB Go back to Oppty Updates Tab and Unfollow the Opportunity");
			dashboard.switchToMainWindow();
			viewOpportunityPage = dashboard.openViewOpportunity();
			viewOpportunityPage.searchForOpportunity(oppt);
			opportunityDetailPage = viewOpportunityPage.selectResult(oppt);
			
			log.info("Open Updates Tab, Unfollow Oppty");
			opportunityDetailPage.opptyUpdates();
			opportunityDetailPage.unfollowOpportunity();
					
			log.info("Logon to SalesConnect with UserA, Update RLI Amount to trigger Oppty Event");
			exec.close();
			dashboard = launchWithLogin(user1);
			
			log.info("Open Opportunity detail page");
			viewOpportunityPage = dashboard.openViewOpportunity();
			viewOpportunityPage.searchForOpportunity(oppt);
			opportunityDetailPage = viewOpportunityPage.selectResult(oppt);
			
			log.info("Update RLI");
			rli.sRevenueAmount = "6565";
			lineItemDetailPage = opportunityDetailPage.selectEditRli();
			opportunityDetailPage = lineItemDetailPage.editLineItemInfo(rli);
			opportunityDetailPage.isPageLoaded();
			
			log.info("Logon to SalesConnect with UserB, Verify that the Oppty Events no longer show up");
			exec.close();
			dashboard = launchWithLogin(user2);
			
			log.info("Open Opportunity detail page");
			viewOpportunityPage = dashboard.openViewOpportunity();
			viewOpportunityPage.searchForOpportunity(oppt);
			opportunityDetailPage = viewOpportunityPage.selectResult(oppt);
			
			log.info("Open Updates Tab, Verify RLI Update Event does Not Exists");
			opportunityDetailPage.opptyUpdates();
						
			log.info("Refresh Activity Stream and verify that RLI Update Event shows up");
			opportunityDetailPage.scrollToTopOfOpportunity();
			opportunityActivityStreamFrame = dashboard.switchToOpptyActivityStreamFrame();
			opportunityActivityStreamFrame.refreshEventsStream();
			Assert.assertFalse(opportunityActivityStreamFrame.verifyEntry(rli.sRevenueAmount), "RLI Update Event exists in Activity Stream and should not");
			
			log.info("Verify Event appears in Homepage Recent Events");
			dashboard.switchToMainWindow();
			dashboard.openHomePage();
			activityStreamFrame = dashboard.switchToActivityStreamFrame();
			Assert.assertTrue(activityStreamFrame.verifyEntryNotExist(rli.sRevenueAmount), "RLI Update Event still exists in Activity Stream and should not");
			
		} catch (Exception e) {
			e.printStackTrace();

			log.info("Remove the oppty created for this test");
			sugarAPI.deleteOpptySOAP(testConfig.getBrowserURL(), opptyID, user1.getEmail(), user1.getPassword());
			
			Assert.assertTrue(false);
		}
		
		log.info("Remove the oppty created for this test");
		sugarAPI.deleteOpptySOAP(testConfig.getBrowserURL(), opptyID, user1.getEmail(), user1.getPassword());
		sugarAPI.deleteContactSOAP(testConfig.getBrowserURL(), contactID, user1.getEmail(), user1.getPassword());
		
		log.info("End of test method s1816UnfollowOpptyActivityStream");
		
		commonUserAllocator.checkInAllUsersWithToken(this);
		commonUserAllocator.checkInAllGroupUsersWithToken(GC.db2UserGroup,this);
	}
}
