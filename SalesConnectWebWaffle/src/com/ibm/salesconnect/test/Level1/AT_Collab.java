package com.ibm.salesconnect.test.Level1;

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
import com.ibm.salesconnect.model.partials.ConnectionsCommunityTab;
import com.ibm.salesconnect.model.partials.DocumentsSubpanel;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Client.ClientDetailPage;
import com.ibm.salesconnect.model.standard.Client.ViewClientPage;
import com.ibm.salesconnect.model.standard.Opportunity.OpportunityDetailPage;
import com.ibm.salesconnect.model.standard.Opportunity.ViewOpportunityPage;
import com.ibm.salesconnect.model.standard.Task.SearchEmployeesPage;
import com.ibm.salesconnect.objects.Client;
import com.ibm.salesconnect.objects.Document;
import com.ibm.salesconnect.objects.Opportunity;

public class AT_Collab extends ProductBaseTest {

	Logger log = LoggerFactory.getLogger(AT_Collab.class);

	int rand = new Random().nextInt(100000);
	String contactID = "22SC-" + rand;
	String opptyID = "33SC-" + rand;
	SugarAPI sugarAPI = new SugarAPI();
	Opportunity oppt = new Opportunity();
	Document subPanelDoc = new Document();

	@Test(groups = { "Level1"})
	public void Test_AT_Collab() {
		log.info("Start test method AT_Collab");
		User user1 = commonUserAllocator.getUser(this);
		User user2 = commonUserAllocator.getUser(this);
		PoolClient poolClient = commonClientAllocator.getGroupClient("DC",this);
		Client client = new Client();
		client.sClientID = poolClient.getCCMS_ID();
		client.sClientName = poolClient.getClientName(testConfig.getBrowserURL(), user1);		
		client.sCCMS_Level = "DC";
		
		//Client variables
		client.sSearchIn= GC.showingInClientID;
		client.sSearchFor= GC.searchForAll;
		client.sSearchShowing=GC.showingForClients;

		subPanelDoc.sDocName = "s9492_" + rand;
		subPanelDoc.sDocLocation = testConfig.getParameter("root_folder_name");
		subPanelDoc.sDescription = "Doc sharing description " + subPanelDoc.sDocName;
		subPanelDoc.vShareWithOptions.add(GC.gsShareWithNoOne);
		subPanelDoc.vShareWithOptions.add(GC.gsShareWithClientTeam);
				
		//TESTCASE ENVIRONMENT SETUP
		
		log.info("Creating Document " + subPanelDoc.sDocName + "...");
		Assert.assertEquals(true, createFile(subPanelDoc.sDocLocation, subPanelDoc.sDocName, "Test"), "Document " + subPanelDoc.sDocName + " created successfully");
		

		sugarAPI.createContact(testConfig.getBrowserURL(), contactID, client.sClientID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");
		opptyID = sugarAPI.createOppty(testConfig.getBrowserURL(), "", contactID, client.sClientID, user1.getEmail(), user1.getPassword(), GC.emptyArray);
		oppt.sOpptNumber = opptyID;
		oppt.sPrimaryContact = "ContactFirst ContactLast";
		oppt.sAccID = client.sClientID;
		
		
		try {
			log.info("STARTING SETUP FOR " + log.getName());
			
			log.info("Logging in");
			Dashboard dashboard = launchWithLogin(user1);

			log.info("Verify Employee Business Card");
			SearchEmployeesPage searchEmployeesPage = dashboard.openSearchEmployeesPage();
			searchEmployeesPage.searchForEmployee(user2.getFirstName());
			searchEmployeesPage.verifyBusinessCard(user2);
			
			log.info("Open Client detail page");
			ViewClientPage viewClientPage = dashboard.openViewClient();
			viewClientPage.searchForClient(client);
			ClientDetailPage clientDetailPage = viewClientPage.selectResult(client);
						
			log.info("Verify Connections Community Tab for Client");
			ConnectionsCommunityTab connectionsCommunityTab = clientDetailPage.openConnectionsCommunityTab();			
			connectionsCommunityTab.verifyCommunityTab(user1.getEmail(), user1.getPassword());
									
			log.info("Open Opportunity detail page");
			ViewOpportunityPage viewOpportunityPage = dashboard.openViewOpportunity();
			viewOpportunityPage.searchForOpportunity(oppt);
			OpportunityDetailPage opportunityDetailPage = viewOpportunityPage.selectResult(oppt);

			//Community Tab no longer in Opportunity will leave this here in case it comes back for 1.3			
//			log.info("Verify Connections Community Tab for Opportunity");
//			opportunityDetailPage.openConnectionsCommunityTab();			
//			Assert.assertTrue(connectionsCommunityTab.verifyCommunityTab(), "All Connections Areas are not present in the Connections Community Tab");
									
			log.info("Create Document in Opportunity Documents Subpanel and verify Document Present in Subpanel after creation");
			DocumentsSubpanel docSubpanel = opportunityDetailPage.openDocumentsSubpanel();
			docSubpanel.enterDocumentInfo(subPanelDoc);
			docSubpanel.saveDocument();
			docSubpanel.verifyDocumentPresent(subPanelDoc);
			
		} catch (Exception e) {
			e.printStackTrace();

			log.info("Remove the oppty created for this test");
			sugarAPI.deleteOpptySOAP(testConfig.getBrowserURL(), opptyID, user1.getEmail(), user1.getPassword());
			sugarAPI.deleteContactSOAP(testConfig.getBrowserURL(), contactID, user1.getEmail(), user1.getPassword());
			Assert.assertTrue(false);
		}

		log.info("Remove the oppty created for this test");
		sugarAPI.deleteOpptySOAP(testConfig.getBrowserURL(), opptyID, user1.getEmail(), user1.getPassword());
		sugarAPI.deleteContactSOAP(testConfig.getBrowserURL(), contactID, user1.getEmail(), user1.getPassword());
		commonUserAllocator.checkInAllUsersWithToken(this);
		log.info("End of test method AT_Collab");
	}
}