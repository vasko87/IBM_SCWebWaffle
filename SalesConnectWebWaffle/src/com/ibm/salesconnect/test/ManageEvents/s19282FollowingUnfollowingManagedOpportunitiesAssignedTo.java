/**
 * 
 */
package com.ibm.salesconnect.test.ManageEvents;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.SugarAPI;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Collab.ManageEventsDialog;
import com.ibm.salesconnect.model.standard.Opportunity.OpportunityDetailPage;
import com.ibm.salesconnect.model.standard.Opportunity.ViewOpportunityPage;
import com.ibm.salesconnect.objects.Opportunity;

/**
 * @author timlehane
 * @date Aug 20, 2013
 */

//NOTE= This testcase is marked as obsolete=https://nsjazz.raleigh.ibm.com:8048/jazz/web/console/Social%20CRM#action=com.ibm.rqm.planning.home.actionDispatcher&subAction=viewTestCase&id=2974
//The  replacement for this test case at  replacement test case at: https://nsjazz.raleigh.ibm.com:8048/jazz/web/console/Social%20CRM#action=com.ibm.rqm.planning.home.actionDispatcher&subAction=viewTestCase&id=4124

public class s19282FollowingUnfollowingManagedOpportunitiesAssignedTo extends
		ProductBaseTest {
	
	User user1 = null;

	int rand = new Random().nextInt(100000);
	String contactID = "22SC-" + rand;
	String opptyID = null;
	String clientID = null;
	SugarAPI sugarAPI = new SugarAPI();
	Opportunity oppty = new Opportunity();
	
	@Test(groups = {"ManageEvents"})
	public void Test_s19282FollowingManagedOpportunitiesAssignedTo() {
		Logger log = LoggerFactory.getLogger(s19282FollowingUnfollowingManagedOpportunitiesAssignedTo.class);
		log.info("Start of test method Test_s19282FollowingManagedOpportunitiesAssignedTo");
		try{
			user1 = commonUserAllocator.getUser(this);
			clientID = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
		sugarAPI.createContact(testConfig.getBrowserURL(), contactID, clientID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");
		opptyID = sugarAPI.createOppty(testConfig.getBrowserURL(), "", contactID, clientID, user1.getEmail(), user1.getPassword(), GC.emptyArray);
		oppty.sOpptNumber = opptyID;
		oppty.sPrimaryContact = "ContactFirst ContactLast";
		oppty.sAccID = clientID;
		
		log.info("Logging in");
		Dashboard dashboard = launchWithLogin(user1);
		
		log.info("Prerequiste##Start Setting the given oppty to be in followed state ");
		dashboard.switchToMainWindow();
		dashboard.openHomePage();
		dashboard.isPageLoaded();
		ManageEventsDialog manageEventsDialog =  dashboard.openManageEventsDialog();
		manageEventsDialog.isPageLoaded();
		manageEventsDialog.switchToTab("Opportunities");
		manageEventsDialog.openSubTab(GC.OpportunitiesImNotFollowing );
		manageEventsDialog.searchForClientOrOppty(GC.OpportunitiesImNotFollowing,opptyID);
		manageEventsDialog.clickStartFollowing(opptyID);
		log.info("Prerequiste##Completed Setting the given oppty to be in followed State ");
		
		
		log.info("Go to oppty detail page and ensure that the oppty is not being followed");
		ViewOpportunityPage viewOpptyPage = dashboard.openViewOpportunity();
		viewOpptyPage.isPageLoaded();
		viewOpptyPage.searchForOpportunity(oppty);
		viewOpptyPage.isPageLoaded();
		sleep(5);
		OpportunityDetailPage opptyDetailPage = viewOpptyPage.selectResult(oppty);
		opptyDetailPage.isPageLoaded();
		opptyDetailPage.opptyUpdates();
		opptyDetailPage.stopFollowingOppty();
		sleep(5);
		dashboard.switchToMainWindow();
		dashboard.openHomePage();
		 manageEventsDialog = dashboard.openManageEventsDialog();
		manageEventsDialog.isPageLoaded();
		manageEventsDialog.switchToTab("Opportunities");
        sleep(5);
		if(!manageEventsDialog.isItemNotFollowed(opptyID)){
			Assert.assertFalse(true, "Oppty is being followed when it should not be");
		}

		log.info("Follow oppty");

		manageEventsDialog.openSubTab(GC.OpportunitiesImNotFollowing );
		manageEventsDialog.searchForClientOrOppty(GC.OpportunitiesImNotFollowing,opptyID);
		manageEventsDialog.clickStartFollowing(opptyID);
		sleep(5);
		manageEventsDialog.openSubTab(GC.OpportunitiesImFollowing );
		Assert.assertTrue(manageEventsDialog.isItemBeingFollowed(opptyID), "Oppty was is not being followed when it should not be");
		manageEventsDialog.closeDialog();
	
		
		log.info("Go to oppty detail page and ensure that the oppty is now being followed");
		dashboard.switchToMainWindow();
		dashboard.openHomePage();
		viewOpptyPage = dashboard.openViewOpportunity();
		viewOpptyPage.isPageLoaded();
		viewOpptyPage.searchForOpportunity(oppty);
		viewOpptyPage.isPageLoaded();
		sleep(5);
		opptyDetailPage = viewOpptyPage.selectResult(oppty);
		opptyDetailPage.isPageLoaded();
		opptyDetailPage.opptyUpdates();
		sleep(5);
		Assert.assertTrue(opptyDetailPage.isOpptyBeingFollowed(), "Opportunity is not being followed when it should be");
		
		}
		catch(Exception e){
			commonClientAllocator.checkInAllGroupClientWithToken(GC.SC, this);
			log.info("Remove the oppty created for this test");
			sugarAPI.deleteOpptySOAP(testConfig.getBrowserURL(), opptyID, user1.getEmail(), user1.getPassword());
			sugarAPI.deleteContactSOAP(testConfig.getBrowserURL(), contactID, user1.getEmail(), user1.getPassword());
			Assert.assertTrue(false,e.getMessage());
		}
		log.info("End of test method Test_s19282FollowingManagedOpportunitiesAssignedTo");
	}
	
	@Test(groups = {"ManageEvents"}, dependsOnMethods = {"Test_s19282FollowingManagedOpportunitiesAssignedTo"})
	public void Test_s19282UnfollowingManagedOpportunitiesAssignedTo() {
		Logger log = LoggerFactory.getLogger(s19282FollowingUnfollowingManagedOpportunitiesAssignedTo.class);
		log.info("Start of test method Test_s19282UnfollowingManagedOpportunitiesAssignedTo");
		try{
			log.info("Logging in");
			
			Dashboard dashboard = launchWithLogin(user1);
			dashboard.switchToMainWindow();
			dashboard.openHomePage();
			dashboard.isPageLoaded();
			log.info("Verify that oppty is being followed in manage events dialog");
			ManageEventsDialog manageEventsDialog =  dashboard.openManageEventsDialog();
			manageEventsDialog.isPageLoaded();
			manageEventsDialog.getSubTab(GC.OpportunitiesImFollowing);
			
			manageEventsDialog.switchToTab("Opportunities");
			sleep(5);
			Assert.assertTrue(manageEventsDialog.isItemBeingFollowed(opptyID), "Oppty was is not followed when it should be");
			
			log.info("Unfollow oppty");
			sleep(5);
			manageEventsDialog.clickStopFollowing(opptyID);//unfollowItem(opptyID);
			
			Assert.assertTrue(manageEventsDialog.isItemNotFollowed(opptyID), "Oppty was is being followed when it should not be");
			
			manageEventsDialog.closeDialog();
			dashboard.switchToMainWindow();
			dashboard.openHomePage();
			dashboard.isPageLoaded();
			log.info("Go to oppty detail page and ensure that the oppty is now not being followed");
			ViewOpportunityPage viewOpportunityPage = dashboard.openViewOpportunity();
			viewOpportunityPage.isPageLoaded();
			viewOpportunityPage.searchForOpportunity(oppty);
			viewOpportunityPage.isPageLoaded();
			sleep(5);
			OpportunityDetailPage opptyDetailPage = viewOpportunityPage.selectResult(oppty);
			opptyDetailPage.isPageLoaded();
			sleep(5);
			opptyDetailPage.opptyUpdates();
			sleep(5);
			Assert.assertTrue(!opptyDetailPage.isOpptyBeingFollowed(), "Oppty is being followed when it should not be");
		}
		finally{
			log.info("Remove the oppty created for this test");
			sugarAPI.deleteOpptySOAP(testConfig.getBrowserURL(), opptyID, user1.getEmail(), user1.getPassword());
			sugarAPI.deleteContactSOAP(testConfig.getBrowserURL(), contactID, user1.getEmail(), user1.getPassword());
			commonClientAllocator.checkInAllGroupClientWithToken(GC.SC, "s19282FollowingUnfollowingManagedOpportunitiesAssignedTo");
		}
		log.info("End of test method Test_s19282UnfollowingManagedOpportunitiesAssignedTo");
		}
		
}
