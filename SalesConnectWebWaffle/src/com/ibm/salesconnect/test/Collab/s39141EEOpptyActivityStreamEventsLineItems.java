package com.ibm.salesconnect.test.Collab;

import java.util.Random;

import org.testng.Assert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.SugarAPI;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.DateTimeUtility;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Client.ClientDetailPage;
import com.ibm.salesconnect.model.standard.Client.ViewClientPage;
import com.ibm.salesconnect.model.standard.Collab.EmbeddedExperience;
import com.ibm.salesconnect.model.standard.Collab.OpportunityActivityStreamFrame;
import com.ibm.salesconnect.model.standard.LineItems.EditLineItem;
import com.ibm.salesconnect.model.standard.Opportunity.OpportunityDetailPage;
import com.ibm.salesconnect.model.standard.Opportunity.ViewOpportunityPage;
import com.ibm.salesconnect.objects.Client;
import com.ibm.salesconnect.objects.Opportunity;
import com.ibm.salesconnect.objects.RevenueItem;

/**
 * @author evafarrell
 * @date Mar 5, 2014
 */


public class s39141EEOpptyActivityStreamEventsLineItems extends ProductBaseTest {
	
	Logger log = LoggerFactory.getLogger(s39141EEOpptyActivityStreamEventsLineItems.class);
	
	@Test(groups = { "Collab","LC"})
	public void test_s39141EEOpptyActivityStreamEventsLineItems() {
		
		log.info("Start of test method s39141EEOpptyActivityStreamEventsLineItems");
		log.info("Starting test set up");
		
		int rand = new Random().nextInt(100000);
		String contactID = "22SC-" + rand;
		String opptyID = "33SC-" + rand;

		SugarAPI sugarAPI = new SugarAPI();
		Opportunity oppty = new Opportunity();
		RevenueItem rli = new RevenueItem();
		
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
		User user2 = commonUserAllocator.getUser(this);
		PoolClient poolClient = commonClientAllocator.getGroupClient(GC.DC,this);
		Client client = new Client();
		client.sClientID = poolClient.getCCMS_ID();
		client.sClientName = poolClient.getClientName(testConfig.getBrowserURL(), user1);		
		client.sCCMS_Level = "DC";
	
		String[] opptyTeam = getMultipleUsers(2, this);
		sugarAPI.createContact(testConfig.getBrowserURL(), contactID, client.sClientID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");
		sugarAPI.createOpptySOAP(testConfig.getBrowserURL(), opptyID, "", contactID, client.sClientID, user1.getEmail(), user1.getPassword(), opptyTeam);
		oppty.sOpptNumber = opptyID;
		oppty.sPrimaryContact = "ContactFirst ContactLast";
		oppty.sAccID = client.sClientID;
		
		//Client variables
		client.sSearchIn= GC.showingInClientID;
		client.sSearchFor= GC.searchForAll;
		client.sSearchShowing=GC.showingForClients;
		
		log.info("Finished test set up");
			
			log.info("Logging in");
			Dashboard dashboard = launchWithLogin(user1);

			log.info("Open Client detail page");
			ViewClientPage viewClientPage = dashboard.openViewClient();
			viewClientPage.searchForClient(client);
			ClientDetailPage clientDetailPage = viewClientPage.selectResult(client);
			
			log.info("Open Updates Tab for Client and follow Client");
			clientDetailPage.clientUpdates();
			clientDetailPage.followClient();
			
			log.info("Open Opportunity detail page");
			ViewOpportunityPage viewOpportunityPage = dashboard.openViewOpportunity();
			viewOpportunityPage.searchForOpportunity(oppty);
			viewOpportunityPage.isPageLoaded();
			OpportunityDetailPage opportunityDetailPage = viewOpportunityPage.selectResult(oppty);
			opportunityDetailPage.isPageLoaded();
			exec.executeScript("window.scrollBy(0,450)", "");
			
			log.info("Follow Opportunity");
			opportunityDetailPage.opptyUpdates();
			opportunityDetailPage.followOpportunity();
			
			log.info("Create RLI");
			rli.populate();
			rli.sFindOffering = "";
			rli.sOfferingType = "IBM Global Technology Services";
			rli.sSubBrand = "Strategic Outsourcing";
			rli.sCompetitor = "Dell";
			rli.sContractType = "PCR/RFS/MCE - ALL";
			rli.sRevenueAmount = "32000";
			rli.sFinancedRevenueAmount = "2000";
			rli.sIGFRoadmapStatus = "At Risk";
			
			EditLineItem editLineItem = opportunityDetailPage.selectCreateRli();
			editLineItem.editLineItemInfo(rli);
			exec.executeScript("window.scrollBy(0,450)", "");
			opportunityDetailPage = editLineItem.saveLineItem();
			
			log.info("Verify RLI Event in Opportunity Activity Stream");
			String event = user1.getDisplayName()+" created the line item "+ rli.sSubBrand;
			String embeddedExpEvent = "Line Item Created";
			opportunityDetailPage.opptyUpdates();
			OpportunityActivityStreamFrame opportunityActivityStreamFrame = dashboard.switchToOpptyActivityStreamFrame();
			Assert.assertTrue(opportunityActivityStreamFrame.verifyEntry(event), "Event "+event+" does not exist in Activity Stream");
			log.info("Verify Updates Tab - Activity Stream - Expand Entries");
			log.info("Opening first event in activity stream");
			EmbeddedExperience embeddedExperience = opportunityActivityStreamFrame.openFirstEventsEmbeddedExperience();
			log.info("Verifying embedded experience");
			Assert.assertEquals(embeddedExperience.getEmbeddedExperienceTitle(),embeddedExpEvent, "EE title does not match event title from Activity Stream");
			embeddedExperience.switchToMainWindow();
			embeddedExperience.close();
			
			log.info("Change the amount in the line item");
			dashboard.switchToMainWindow();
			//Need to set back to null otherwise it will try to edit these fields too
			rli.sFindOffering = "";
			rli.sOfferingType = "";
			rli.sSubBrand = "";
			rli.sCompetitor = "";
			rli.sContractType = "";
			rli.sFinancedRevenueAmount = "";
			rli.sIGFRoadmapStatus = "";

			rli.sRevenueAmount = "88888";
			opportunityDetailPage.selectEditRli();
			editLineItem.editLineItemInfo(rli);
			exec.executeScript("window.scrollBy(0,450)", "");
			opportunityDetailPage = editLineItem.saveLineItem();
			
			log.info("Verify RLI Event in Opportunity Activity Stream");
			rli.sSubBrand = "Strategic Outsourcing";
			event = user1.getDisplayName()+" updated the amount of "+ rli.sSubBrand +" to "+rli.sRevenueAmount;
			embeddedExpEvent = rli.sRevenueAmount;//Line Item Amount Updated
			opportunityDetailPage.opptyUpdates();
			dashboard.switchToOpptyActivityStreamFrame();
			Assert.assertTrue(opportunityActivityStreamFrame.verifyEntry(event), "Event "+event+" does not exist in Activity Stream");
			log.info("Verify Updates Tab - Activity Stream - Expand Entries");
			log.info("Opening first event in activity stream");
			opportunityActivityStreamFrame.openFirstEventsEmbeddedExperience();
			log.info("Verifying embedded experience");
			Assert.assertEquals(embeddedExperience.getEmbeddedExperienceRLIAmount(),embeddedExpEvent, "EE title does not match event title from Activity Stream");
			embeddedExperience.switchToMainWindow();
			embeddedExperience.close();

			log.info("Change the close date in the line item");
			dashboard.switchToMainWindow();
			rli.sCompetitor = "";
			rli.sRevenueAmount = "";
			rli.sDecisionDate = DateTimeUtility.getDateSlashSeparated();
			opportunityDetailPage.selectEditRli();
			editLineItem.editLineItemInfo(rli);
			exec.executeScript("window.scrollBy(0,450)", "");
			opportunityDetailPage = editLineItem.saveLineItem();
			
			log.info("Verify RLI Event in Opportunity Activity Stream");
			rli.sSubBrand = "Strategic Outsourcing";
			event = user1.getDisplayName()+" updated the close date of "+ rli.sSubBrand;
			embeddedExpEvent = "Close Date Updated";
			opportunityDetailPage.opptyUpdates();
			dashboard.switchToOpptyActivityStreamFrame();
			Assert.assertTrue(opportunityActivityStreamFrame.verifyEntry(event), "Event "+event+" does not exist in Activity Stream");
			log.info("Verify Updates Tab - Activity Stream - Expand Entries");
			log.info("Opening Second event in activity stream");
			opportunityActivityStreamFrame.openSecondEventsEmbeddedExperience();
			log.info("Verifying embedded experience");
			Assert.assertEquals(embeddedExperience.getEmbeddedExperienceTitle(),embeddedExpEvent, "EE title does not match event title from Activity Stream");
			embeddedExperience.switchToMainWindow();
			embeddedExperience.close();
			
			log.info("Update the value in the IFG stage in the line item");
			opportunityDetailPage.openOverviewTab();
			rli.sDecisionDate = "";
			rli.sIGFStage = GC.gsIGF08;
			opportunityDetailPage.selectEditRli();
			editLineItem.editLineItemInfo(rli);
			exec.executeScript("window.scrollBy(0,450)", "");
			opportunityDetailPage = editLineItem.saveLineItem();
			
			log.info("Verify RLI Event in Opportunity Activity Stream");
			rli.sSubBrand = "Strategic Outsourcing";
			event = user1.getDisplayName()+" updated the IGF stage of "+ rli.sSubBrand;
			embeddedExpEvent = "IGF Stage Updated";
			opportunityDetailPage.opptyUpdates();
			dashboard.switchToOpptyActivityStreamFrame();
			Assert.assertTrue(opportunityActivityStreamFrame.verifyEntry(event), "Event "+event+" does not exist in Activity Stream");
			log.info("Verify Updates Tab - Activity Stream - Expand Entries");
			log.info("Opening first event in activity stream");
			opportunityActivityStreamFrame.openSecondEventsEmbeddedExperience();
			log.info("Verifying embedded experience");
			Assert.assertEquals(embeddedExperience.getEmbeddedExperienceTitle(),embeddedExpEvent, "EE title does not match event title from Activity Stream");
			embeddedExperience.switchToMainWindow();
			embeddedExperience.close();
			
			log.info("Update the competitor in the line item");
			dashboard.switchToMainWindow();
			rli.sIGFStage = "";
			rli.sCompetitor = "Microsoft";
			opportunityDetailPage.selectEditRli();
			editLineItem.editLineItemInfo(rli);
			exec.executeScript("window.scrollBy(0,450)", "");
			opportunityDetailPage = editLineItem.saveLineItem();
			
			log.info("Verify RLI Event in Opportunity Activity Stream");
			rli.sSubBrand = "Strategic Outsourcing";
			event = user1.getDisplayName()+" updated the competitor of "+ rli.sSubBrand;
			embeddedExpEvent = "Competitor Updated";
			opportunityDetailPage.opptyUpdates();
			dashboard.switchToOpptyActivityStreamFrame();
			Assert.assertTrue(opportunityActivityStreamFrame.verifyEntry(event), "Event "+event+" does not exist in Activity Stream");
			log.info("Verify Updates Tab - Activity Stream - Expand Entries");
			log.info("Opening first event in activity stream");
			opportunityActivityStreamFrame.openFirstEventsEmbeddedExperience();
			log.info("Verifying embedded experience");
			Assert.assertEquals(embeddedExperience.getEmbeddedExperienceTitle(),embeddedExpEvent, "EE title does not match event title from Activity Stream");
			embeddedExperience.switchToMainWindow();
			embeddedExperience.close();
			
			log.info("Update the alliance partner in the line item");
			dashboard.switchToMainWindow();
			rli.sCompetitor = "";
			rli.sAlliancePartner = "Airwatch";
			opportunityDetailPage.selectEditRli();
			editLineItem.editLineItemInfo(rli);
			exec.executeScript("window.scrollBy(0,450)", "");
			opportunityDetailPage = editLineItem.saveLineItem();
			
			log.info("Verify RLI Event in Opportunity Activity Stream");
			rli.sSubBrand = "Strategic Outsourcing";
			event = user1.getDisplayName()+" updated the alliance partner of "+ rli.sSubBrand;
			embeddedExpEvent = "Alliance Partner Updated";
			opportunityDetailPage.opptyUpdates();
			dashboard.switchToOpptyActivityStreamFrame();
			Assert.assertTrue(opportunityActivityStreamFrame.verifyEntry(event), "Event "+event+" does not exist in Activity Stream");
			log.info("Verify Updates Tab - Activity Stream - Expand Entries");
			log.info("Opening first event in activity stream");
			opportunityActivityStreamFrame.openFirstEventsEmbeddedExperience();
			log.info("Verifying embedded experience");
			Assert.assertEquals(embeddedExperience.getEmbeddedExperienceTitle(),embeddedExpEvent, "EE title does not match event title from Activity Stream");
			embeddedExperience.switchToMainWindow();
			embeddedExperience.close();
			
			log.info("Update the IGF financed amount in the line item");
			dashboard.switchToMainWindow();
			rli.sAlliancePartner = "";
			rli.sFinancedRevenueAmount = "3333";
			opportunityDetailPage.selectEditRli();
			editLineItem.editLineItemInfo(rli);
			exec.executeScript("window.scrollBy(0,450)", "");
			opportunityDetailPage = editLineItem.saveLineItem();
			
			log.info("Verify RLI Event in Opportunity Activity Stream");
			rli.sSubBrand = "Strategic Outsourcing";
			event = user1.getDisplayName()+" updated the IGF financed amount of "+ rli.sSubBrand +" to "+rli.sFinancedRevenueAmount;
			embeddedExpEvent = "IGF Financed Amount Updated";
			opportunityDetailPage.opptyUpdates();
			dashboard.switchToOpptyActivityStreamFrame();
			Assert.assertTrue(opportunityActivityStreamFrame.verifyEntry(event), "Event "+event+" does not exist in Activity Stream");
			log.info("Verify Updates Tab - Activity Stream - Expand Entries");
			log.info("Opening first event in activity stream");
			opportunityActivityStreamFrame.openFirstEventsEmbeddedExperience();
			log.info("Verifying embedded experience");
			Assert.assertEquals(embeddedExperience.getEmbeddedExperienceTitle(),embeddedExpEvent, "EE title does not match event title from Activity Stream");
			embeddedExperience.switchToMainWindow();
			embeddedExperience.close();
			
			log.info("Change the owner of the above line item");
			dashboard.switchToMainWindow();
			rli.sFinancedRevenueAmount = "";
			rli.sOwner = user2.getDisplayName();
			opportunityDetailPage.selectEditRli();
			editLineItem.editLineItemInfo(rli);
			exec.executeScript("window.scrollBy(0,450)", "");
			opportunityDetailPage = editLineItem.saveLineItem();
			
			log.info("Verify RLI Event in Opportunity Activity Stream");
			rli.sSubBrand = "Strategic Outsourcing";
			String sOwnerFirstName = user2.getFirstName();
			event = user1.getDisplayName()+" updated the line item owner of "+ rli.sSubBrand +" to "+sOwnerFirstName;
			embeddedExpEvent = "Line Item Owner Updated";
			opportunityDetailPage.opptyUpdates();
			dashboard.switchToOpptyActivityStreamFrame();
			Assert.assertTrue(opportunityActivityStreamFrame.verifyEntry(event), "Event "+event+" does not exist in Activity Stream");
			log.info("Verify Updates Tab - Activity Stream - Expand Entries");
			log.info("Opening first event in activity stream");
			opportunityActivityStreamFrame.openSecondEventsEmbeddedExperience();
			log.info("Verifying embedded experience");
			Assert.assertEquals(embeddedExperience.getEmbeddedExperienceTitle(),embeddedExpEvent, "EE title does not match event title from Activity Stream");
			embeddedExperience.switchToMainWindow();
			embeddedExperience.close();
			
			log.info("Remove the oppty created for this test");
			sugarAPI.deleteOpptySOAP(testConfig.getBrowserURL(), opptyID, user1.getEmail(), user1.getPassword());
			sugarAPI.deleteContactSOAP(testConfig.getBrowserURL(), contactID, user1.getEmail(), user1.getPassword());

		log.info("End of test method s39141EEOpptyActivityStreamEventsLineItems");
		
		commonUserAllocator.checkInAllUsersWithToken(this);
		commonUserAllocator.checkInAllGroupUsersWithToken(GC.db2UserGroup,this);
	}
}
