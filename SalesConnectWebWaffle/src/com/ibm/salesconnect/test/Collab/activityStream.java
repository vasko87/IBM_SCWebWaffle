/**
 * 
 */
package com.ibm.salesconnect.test.Collab;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.SugarAPI;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Opportunity.CreateOpportunityPage;
import com.ibm.salesconnect.model.standard.Opportunity.EditOpportunityPage;
import com.ibm.salesconnect.model.standard.Opportunity.OpportunityDetailPage;
import com.ibm.salesconnect.model.standard.Opportunity.ViewOpportunityPage;
import com.ibm.salesconnect.objects.Client;
import com.ibm.salesconnect.objects.Opportunity;
import com.ibm.salesconnect.objects.RevenueItem;

/**
 * @author timlehane
 * @date Nov 27, 2013
 */
public class activityStream extends ProductBaseTest {
	Logger log = LoggerFactory.getLogger(collabWebservices.class);
	
	@Test(groups = { })
	public void Test_OpptyActStream(){
		log.info("Start of test method Test_AT_Opportunity");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
		User user2 = commonUserAllocator.getUser(this);

		Opportunity oppt = new Opportunity();
		RevenueItem rli = new RevenueItem();
		
		oppt.populate();
		rli.populate();
		PoolClient poolClient = commonClientAllocator.getGroupClient(GC.SC,this);
		
		Client client = new Client();
		client.sClientID = poolClient.getCCMS_ID();
		client.sClientName = poolClient.getClientName(testConfig.getBrowserURL(), user1);		
		client.sCCMS_Level = "SC";
		log.info("Original client is: " + client.sClientID);
		
		
		//Client variables
		client.sSearchIn= GC.showingInSiteID;
		client.sSearchFor= GC.searchForAll;
		client.sSearchShowing=GC.showingForSites;
		
		String contactID = "22SC-" + new Random().nextInt(99999);
		String clientID = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
		log.info("Client for change is: " + clientID);
		SugarAPI sugarAPI = new SugarAPI();
		sugarAPI.createContact(testConfig.getBrowserURL(), contactID, clientID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");
		
		oppt.sPrimaryContact = "ContactFirst" + " " + "ContactLast";

		log.info("Logging in");
		Dashboard dashboard = launchWithLogin(user1);
		
		log.info("Creating Opportunity");
		CreateOpportunityPage createOpportunityPage = dashboard.openCreateOpportunity();
		createOpportunityPage.enterOpportunityInfo(oppt,rli);
		ViewOpportunityPage viewOpportunityPage = createOpportunityPage.saveOpportunity(oppt);
		OpportunityDetailPage opportunityDetailPage = viewOpportunityPage.selectResult(oppt);
		oppt.sOpptNumber = opportunityDetailPage.getdisplayedOpportunityNumber();
		log.info("Oppty number :" + oppt.sOpptNumber);
		
		log.info("Following oppty");
		opportunityDetailPage.openUpdatesTab();
		opportunityDetailPage.followOppty();
		
		EditOpportunityPage editOppty = opportunityDetailPage.openEditOpportunity();
		editOppty.editClientName(client);
		editOppty.editOpptyOwner(user2.getDisplayName());
		editOppty.editDecisionDate("06/16/2014");
		editOppty.editSalesStage(GC.gsOppValidating);
		editOppty.saveOpportunity();
		
		opportunityDetailPage.openUpdatesTab();
		
		log.info("Verify events");
		String[] eventList = {user1.getDisplayName()+" created the opportunity " + oppt.sOpptNumber, "updated the sales stage", "updated the opportunity owner", "updated the decision date", oppt.sOpptNumber + " opportunity team", "updated the CMR ID"};
		
		opportunityDetailPage.checkEvents(eventList);
		
		sugarAPI.deleteContactSOAP(testConfig.getBrowserURL(), contactID, user1.getEmail(), user1.getPassword());
		
		log.info("End of test method Test_AT_Opportunity");
	}
}
