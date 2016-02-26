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
 * @author Peter Poliwoda
 * @date February 04, 2014
 */
public class s19282FollowingManagedOpportunitiesNotAssignedTo extends ProductBaseTest {
	
	User user1 = null; //User doing the test
	User user2 = null; //User who owns the Oppty
	
	int rand = new Random().nextInt(100000);
	String contactID = "22SC-" + rand;
	String opptyID = null;
	String clientID = null;
	SugarAPI sugarAPI = new SugarAPI();
	Opportunity oppty = new Opportunity();
	//String[] team = new String[4];
	
	@Test(groups = {"ManageEvents","mee5"})
	public void Test_s19282FollowingManagedOpportunitiesNotAssignedTo() {
		Logger log = LoggerFactory.getLogger(s19282FollowingManagedOpportunitiesNotAssignedTo.class);
		log.info("Start of test method Test_s19282FollowingManagedOpportunitiesNotAssignedTo");
		try{
			user1 = commonUserAllocator.getGroupUser(GC.noMemUserGroup,this);
			user2 = commonUserAllocator.getUser(this);
			
			log.info("Get client from CSV file");
			clientID = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
						
			log.info("Create contact in client");	  		
			sugarAPI.createContact(testConfig.getBrowserURL(), contactID, clientID, user2.getEmail(), user2.getPassword(), "ContactFirst", "ContactLast");
			
			log.info("Create oppty");	  					
			opptyID = sugarAPI.createOppty(testConfig.getBrowserURL(), "", contactID, clientID, user2.getEmail(), user2.getPassword(), GC.emptyArray);

			oppty.sOpptNumber = opptyID;
			oppty.sPrimaryContact = "ContactFirst ContactLast";
			oppty.sAccID = clientID;
		
			log.info("Logging in");
			Dashboard dashboard = launchWithLogin(user1);

			ManageEventsDialog manageEventsDialog = dashboard.openManageEventsDialog();
			manageEventsDialog.isPageLoaded();
			log.info("Locating Opportunities I'm Not Assigned To");
			manageEventsDialog.switchToTab("Opportunities");

		    manageEventsDialog.openSubTab(GC.OpportunitiesImNotFollowing);
		    
			if(!manageEventsDialog.isItemNotFollowed(opptyID)){
				Assert.assertFalse(true, "Oppty is being followed when it should not be");
			} 
			
			manageEventsDialog.closeDialog();
			dashboard.switchToMainWindow();
			dashboard.openHomePage();
			dashboard.isPageLoaded();
			log.info("Go to oppty detail page and follow the oppty ");
			
			ViewOpportunityPage viewOpptyPage = dashboard.openViewOpportunity();
			viewOpptyPage.isPageLoaded();
			viewOpptyPage.searchForOpportunity(oppty);
			viewOpptyPage.isPageLoaded();
			sleep(5);
			OpportunityDetailPage opptyDetailPage = viewOpptyPage.selectResult(oppty);
			opptyDetailPage.isPageLoaded();
			sleep(5);
			log.info("Opening Update tab");
			opptyDetailPage.opptyUpdates();
			log.info("Follow Opportunity");
			opptyDetailPage.followOpportunity();
			
			log.info("Go back to Home Page");
			dashboard.switchToMainWindow();
			dashboard.openHomePage();
			dashboard.isPageLoaded();
			manageEventsDialog = dashboard.openManageEventsDialog();
			manageEventsDialog.isPageLoaded();
			log.info("Opening Opportunities tab");
			manageEventsDialog.switchToTab("Opportunities");
			log.info("Switching to Subtab O" +
					"ppties No Assigned To");
			
			manageEventsDialog.openSubTab(GC.OpportunitiesImNotFollowing);
		    
			log.info("Asserting if Oppty is being followed");
			Assert.assertTrue(manageEventsDialog.isItemBeingFollowed(opptyID), "Oppty is not being followed when it should be");
	
		}
		catch(Exception e){
			commonClientAllocator.checkInAllGroupClientWithToken(GC.SC, this);
			sugarAPI.deleteContactSOAP(testConfig.getBrowserURL(), contactID, user1.getEmail(), user1.getPassword());
			Assert.assertTrue(false,e.getMessage());
		}
		sugarAPI.deleteContactSOAP(testConfig.getBrowserURL(), contactID, user1.getEmail(), user1.getPassword());
		log.info("End of test method Test_s19282FollowingManagedOpportunitiesNotAssignedTo");
	}
}
