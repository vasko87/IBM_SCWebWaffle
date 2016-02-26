package com.ibm.salesconnect.test.DocSharing;

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
import com.ibm.salesconnect.model.partials.DocumentsSubpanel;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Connections.Community.CommunityDetailPage;
import com.ibm.salesconnect.model.standard.Connections.Community.CommunityMainPage;
import com.ibm.salesconnect.model.standard.Connections.Files.FilesMainPage;
import com.ibm.salesconnect.model.standard.Opportunity.OpportunityDetailPage;
import com.ibm.salesconnect.model.standard.Opportunity.ViewOpportunityPage;
import com.ibm.salesconnect.objects.Document;
import com.ibm.salesconnect.objects.Opportunity;


public class s4675_s9492OpportunityShareUnshareDeleteDocumentSharedWithClientTeam extends ProductBaseTest{

	User user1 = null;
	User user2 = null;
	User user3 = null;
	Document subPanelDoc = new Document();
	int rand = new Random().nextInt(100000);
	String contactID = "22SC-" + rand;
	String opptyID = null;
	PoolClient poolClient = null;
	String clientID = null;
	String clientName = null;
	SugarAPI sugarAPI = new SugarAPI();
	Opportunity oppty = new Opportunity();
	
	
	@Test(groups = {"DocSharing"})
	public void Test_s4675OpportunityShareDocumentWithClientTeam() {
		Logger log = LoggerFactory.getLogger(s4675_s9492OpportunityShareUnshareDeleteDocumentSharedWithClientTeam.class);
		log.info("Start of test method s9492OpportunityUnshareAndDeleteDocumentSharedWithClientTeam");

		try{
			user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
			poolClient = commonClientAllocator.getGroupClient(GC.DC,this);
			clientID = poolClient.getCCMS_ID();
			clientName = poolClient.getClientName(testConfig.getBrowserURL(), user1);
			String[] opptyTeam = getMultipleUsers(2, this);
			sugarAPI.createContact(testConfig.getBrowserURL(), contactID, clientID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");
			opptyID = sugarAPI.createOppty(testConfig.getBrowserURL(), "", contactID, clientID, user1.getEmail(), user1.getPassword(), opptyTeam);
			oppty.sOpptNumber = opptyID;
			oppty.sPrimaryContact = "ContactFirst ContactLast";
			oppty.sAccID = clientID;
			
			subPanelDoc.sDocName = "s9492_" + rand;
			subPanelDoc.sDocLocation = testConfig.getParameter("root_folder_name");
			subPanelDoc.sDescription = "Doc sharing description " + subPanelDoc.sDocName;
			subPanelDoc.vShareWithOptions.add(GC.gsShareWithNoOne);
			subPanelDoc.vShareWithOptions.add(GC.gsShareWithClientTeam);
					
			//TESTCASE ENVIRONMENT SETUP
			
			log.info("Creating Document " + subPanelDoc.sDocName + "...");
			Assert.assertEquals(true, createFile(subPanelDoc.sDocLocation, subPanelDoc.sDocName, "Test"), "Document " + subPanelDoc.sDocName + " created successfully");
			
			log.info("Logging in");
			Dashboard dashboard = launchWithLogin(user1);
			
			//TESTCASE ENVIRONMENT SETUP COMPLETE
			
			//Navigate to provisioned Client detail page and create document from sub-panel
			ViewOpportunityPage viewOpptyPage = dashboard.openViewOpportunity();
			viewOpptyPage.searchForOpportunity(oppty);
			OpportunityDetailPage opptyDetailPage = viewOpptyPage.selectResult(oppty);
			opptyDetailPage.isPageLoaded();
				
			//Create Doc (since we are still on the same page)
			DocumentsSubpanel docSubpanel = opptyDetailPage.openDocumentsSubpanel();
			docSubpanel.enterDocumentInfo(subPanelDoc);
			docSubpanel.saveDocument();
			docSubpanel.isPageLoaded();
			docSubpanel.verifyDocumentPresent(subPanelDoc);
			exec.quit();
		
			CommunityMainPage communityMainPage = launchCnxnCommunity(user1);
			CommunityDetailPage communityDetailPage = communityMainPage.searchAndOpenCommunity(clientName,testConfig.getParameter(GC.cxnURL)+"/communities");
			FilesMainPage filesMainPage = communityDetailPage.openFilesPage();
			filesMainPage.isPageLoaded();

			Assert.assertTrue(filesMainPage.isFilePresent(subPanelDoc.sDocName), "Document "+subPanelDoc.sDocName+" not found in commections community");
		}
		catch(Exception e){
			log.info(e.getMessage());

			log.info("Remove the oppty created for this test");
			sugarAPI.deleteOpptySOAP(testConfig.getBrowserURL(), opptyID, user1.getEmail(), user1.getPassword());
			sugarAPI.deleteContactSOAP(testConfig.getBrowserURL(), contactID, user1.getEmail(), user1.getPassword());
																		
			Assert.assertTrue(false);
		}
	}
	
	@Test(groups = {"DocSharing"},dependsOnMethods={"Test_s4675OpportunityShareDocumentWithClientTeam"})	
		public void Test_s9492OpportunityUnshareShareDocumentWithClientTeam() {
			Logger log = LoggerFactory.getLogger(s4675_s9492OpportunityShareUnshareDeleteDocumentSharedWithClientTeam.class);

			try{
			subPanelDoc.vShareWithOptions.removeAllElements();
			subPanelDoc.vShareWithOptions.add(GC.gsShareWithNoOne);
			
			Dashboard dashboard = launchWithLogin(user1);
			
			ViewOpportunityPage viewOpptyPage = dashboard.openViewOpportunity();
			viewOpptyPage.searchForOpportunity(oppty);
			OpportunityDetailPage opptyDetailPage = viewOpptyPage.selectResult(oppty);

			log.info("Upload Document and Share with Client Team");
			opptyDetailPage.isPageLoaded();
			DocumentsSubpanel docSubpanel = opptyDetailPage.openDocumentsSubpanel();
			docSubpanel.openEditDocumentSubpanel(subPanelDoc.sDocName);
			docSubpanel.updateSharingOptions(subPanelDoc);	
			
			docSubpanel = opptyDetailPage.openDocumentsSubpanel();
			docSubpanel.openEditDocumentSubpanel(subPanelDoc.sDocName);
			docSubpanel.deleteDocument();
			exec.quit();
			
			FilesMainPage filesMainPage = launchCnxnFiles(user1);
			log.info("Verify document not found in My Files");
			Assert.assertTrue(!filesMainPage.isFilePresent(subPanelDoc.sDocName), "Document "+subPanelDoc.sDocName+" not found in Files - My Files");
			filesMainPage.openSharedByMe();
			log.info("Verify document not found in Shared By Me");
			Assert.assertTrue(!filesMainPage.isFilePresent(subPanelDoc.sDocName), "Document "+subPanelDoc.sDocName+" not found in Files - Shared By Me");
			}
			finally{

				log.info("Remove the oppty created for this test");
				sugarAPI.deleteOpptySOAP(testConfig.getBrowserURL(), opptyID, user1.getEmail(), user1.getPassword());
				sugarAPI.deleteContactSOAP(testConfig.getBrowserURL(), contactID, user1.getEmail(), user1.getPassword());
			}

			log.info("End of test method s9492OpportunityUnshareAndDeleteDocumentSharedWithClientTeam");
	}
}
