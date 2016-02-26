package com.ibm.salesconnect.test.BusinessCard;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.SugarAPI;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.partials.ActivitiesHistorySubpanel;
import com.ibm.salesconnect.model.partials.ActivitiesSubpanel;
import com.ibm.salesconnect.model.partials.DocumentsSubpanel;
import com.ibm.salesconnect.model.partials.LineItemsubpanel;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.LineItems.EditLineItem;
import com.ibm.salesconnect.model.standard.Opportunity.OpportunityDetailPage;
import com.ibm.salesconnect.model.standard.Opportunity.ViewOpportunityPage;
import com.ibm.salesconnect.objects.Document;
import com.ibm.salesconnect.objects.Opportunity;
import com.ibm.salesconnect.objects.RevenueItem;
import com.ibm.salesconnect.objects.Task;

/**
 * @author timlehane
 * @date May 27, 2013
 */
@Test(groups = { "BusinessCard"})
public class s9892BusinessCardOpportunities extends ProductBaseTest {
	
	Logger log = LoggerFactory.getLogger(s9892BusinessCardOpportunities.class);

	int rand = new Random().nextInt(100000);
	String contactID = "22SC-" + rand;
	String opptyID = null;
	String RLIID = "44SC-" + rand;
	String clientID = null;
	SugarAPI sugarAPI = new SugarAPI();
	Opportunity oppt = new Opportunity();
	
	public void Test_s9892BusinessCardOpportunities() {
		log.info("Start test method Test_s9892BusinessCardOpportunities");
		
		log.info("Getting users");
		User user1 = commonUserAllocator.getUser(this);
		User user2 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
		String[] team = {user2.getEmail()};
			log.info("Creating Contact & Oppty using API");
			clientID = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();			
//			sugarAPI.createRLI(testConfig.getBrowserURL(), RLIID, user1.getEmail(), user1.getPassword());
			sugarAPI.createContact(testConfig.getBrowserURL(), contactID, clientID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");
//			sugarAPI.createOpptySOAP(testConfig.getBrowserURL(), opptyID, RLIID, contactID, clientID, user1.getEmail(), user1.getPassword(), team);
			opptyID = sugarAPI.createOppty(testConfig.getBrowserURL(), "", contactID, clientID, user1.getEmail(), user1.getPassword(), team);
			oppt.sOpptNumber = opptyID;
			oppt.sPrimaryContact = "ContactFirst ContactLast";
			oppt.sAccID = clientID;
			
			Task task = new Task();
			task.populate();
			
			RevenueItem rli = new RevenueItem();	
			rli.populate();
			rli.sFindOffering = "Lenovo";
			
			Document subPanelDoc = new Document();
			subPanelDoc.sDocName = "s9892_" + new Random().nextInt(100000);
			subPanelDoc.sDocLocation = testConfig.getParameter("root_folder_name");
			subPanelDoc.sDescription = "Doc sharing description " + subPanelDoc.sDocName;
			subPanelDoc.vShareWithOptions.add(GC.gsShareWithEveryone);
		
			log.info("Logging in");
			Dashboard dashboard = launchWithLogin(user1);
			
			log.info("Open opportunity detail page and verify Business Card");
			ViewOpportunityPage viewOpportunityPage = dashboard.openViewOpportunity();
			viewOpportunityPage.searchForOpportunity(oppt);
			sleep(2);
			viewOpportunityPage.verifyBusinessCard(oppt.sOpptNumber, user1);
			
			log.info("Select opportunity and verify Business Card");
			OpportunityDetailPage opportunityDetailPage = viewOpportunityPage.selectResult(oppt);
			opportunityDetailPage.verifyBusinessCard(user1);
			opportunityDetailPage.verifyTeamBusinessCard(user2);
			
			log.info("Create Document in Subpanel and verify Business Card");
			DocumentsSubpanel documentsSubpanel = opportunityDetailPage.openDocumentsSubpanel();
			documentsSubpanel.enterDocumentInfo(subPanelDoc);
			documentsSubpanel.saveDocument();
			documentsSubpanel.verifyBusinessCard(user1);
			
			log.info("Create Task in Activities Subpanel and verify Business Card");
			task.sName = "Oppty Task " + GC.sUniqueNum;
			ActivitiesSubpanel activitiesSubpanel = opportunityDetailPage.openCreateActivitiesSubPanel();
			activitiesSubpanel.openCreateTaskForm();
			activitiesSubpanel.enterTaskInfo(task);
			activitiesSubpanel.saveTask();
			activitiesSubpanel.verifyBusinessCard(user1);
			
			log.info("Complete Task and verify Business Card");
			activitiesSubpanel.completeTask(task.sName);
			Assert.assertTrue(opportunityDetailPage.isPageLoaded(), "Opportunity Detail page has not loaded within 60 seconds");
			
			opportunityDetailPage.closeRLISubpanel();
			ActivitiesHistorySubpanel activitiesHistorySubpanel = opportunityDetailPage.openActivitiesHistorySubpanel();
			activitiesHistorySubpanel.verifyBusinessCard(user1);
			
			log.info("Verify Business Card in RLI Subpanel");
			//Adding this in until SOAP Oppty Create API working again
			opportunityDetailPage.showRLISubpanel();
			EditLineItem editLineItem = opportunityDetailPage.selectCreateRli();
			editLineItem.editLineItemInfo(rli);
			exec.executeScript("window.scrollBy(0,450)", "");
			opportunityDetailPage = editLineItem.saveLineItem();
			sleep(5);
			
			LineItemsubpanel lineItemsubpanel = opportunityDetailPage.openLineItemsSubpanel();
			lineItemsubpanel.verifyBusinessCard(user1);

			log.info("Remove the oppty created for this test");
			sugarAPI.deleteOpptySOAP(testConfig.getBrowserURL(), opptyID, user1.getEmail(), user1.getPassword());
			sugarAPI.deleteContactSOAP(testConfig.getBrowserURL(), contactID, user1.getEmail(), user1.getPassword());
		log.info("End of test method Test_s9892BusinessCardOpportunities");
	}

}
