///**
// * 
// */
//package com.ibm.salesconnect.test.DocSharing;
//
//import java.sql.SQLException;
//import java.util.Random;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.testng.Assert;
//import org.testng.annotations.Test;
//
//import com.ibm.atmn.waffle.extensions.user.User;
//import com.ibm.salesconnect.API.SugarAPI;
//import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
//import com.ibm.salesconnect.common.GC;
//import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
//import com.ibm.salesconnect.model.partials.DocumentsSubpanel;
//import com.ibm.salesconnect.model.standard.Dashboard;
//import com.ibm.salesconnect.model.standard.Connections.Community.CommunityDetailPage;
//import com.ibm.salesconnect.model.standard.Connections.Community.CommunityMainPage;
//import com.ibm.salesconnect.model.standard.Connections.Files.FilesMainPage;
//import com.ibm.salesconnect.model.standard.Opportunity.OpportunityDetailPage;
//import com.ibm.salesconnect.model.standard.Opportunity.ViewOpportunityPage;
//import com.ibm.salesconnect.objects.Document;
//import com.ibm.salesconnect.objects.Opportunity;
//
//
///**
// * @author timlehane
// * @date May 29, 2013
// */
//public class s4675_s9492OpportunityShareUnshareDocumentWithOpportunityTeam extends
//		ProductBaseTest {
//	Logger log = LoggerFactory.getLogger(s4675_s9492OpportunityShareUnshareDocumentWithOpportunityTeam.class);
//
//	User user1 = null;
//	User user2 = null;
//	User user3 = null;
//	
//	Document subPanelDoc = new Document();
//	int rand = new Random().nextInt(100000);
//	String contactID = "22SC-" + rand;
//	String opptyID = "33SC-" + rand;
//	PoolClient poolClient = null;
//	String clientID = null;
//	String clientName = null;
//	SugarAPI sugarAPI = new SugarAPI();
//	Opportunity oppty = new Opportunity();
//	
//	@Test(groups = {})
//	public void Test_s4675OpportunityShareDocumentWithOpportunityTeam() throws SQLException {
//		log.info("Start of test setup for Test_s4675OpportunityShareDocumentWithOpportunityTeam");
//		try{
//			user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,"s4675OpportunityShareDocumentWithOpportunityTeam");
//			user2 = commonUserAllocator.getUser("s4675OpportunityShareDocumentWithOpportunityTeam");
//			user3 = commonUserAllocator.getUser("s4675OpportunityShareDocumentWithOpportunityTeam");
//			poolClient = commonClientAllocator.getGroupClient(GC.DC,"s4675OpportunityShareDocumentWithOpportunityTeam");
//			clientID = poolClient.getCCMS_ID();
//			clientName = poolClient.getClientName();
//			String [] opptyTeam = {user2.getEmail()};
//		sugarAPI.createContact(testConfig.getBrowserURL(), contactID, clientID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");
//		sugarAPI.createOppty(testConfig.getBrowserURL(), opptyID, "", contactID, clientID, user1.getEmail(), user1.getPassword(), opptyTeam);
//		oppty.sOpptNumber = opptyID;
//		oppty.sPrimaryContact = "ContactFirst ContactLast";
//		oppty.sAccID = clientID;
//		
//		subPanelDoc.sDocName = "s4675_" + GC.sUniqueNum;
//		subPanelDoc.sDocLocation = testConfig.getParameter("root_folder_name");
//		subPanelDoc.sDescription = "Doc sharing description " + subPanelDoc.sDocName;
//		subPanelDoc.vShareWithOptions.add(GC.gsShareWithOpportunityTeam);
//			
//		log.info("Creating Document " + subPanelDoc.sDocName + "...");
//		Assert.assertEquals(true, createFile(subPanelDoc.sDocLocation, subPanelDoc.sDocName, "Test"), "Document " + subPanelDoc.sDocName + " created successfully");
//		
//		log.info("End of test setup for Test_s4675OpportunityShareDocumentWithOpportunityTeam");
//		log.info("Start of test for Test_s4675OpportunityShareDocumentWithOpportunityTeam");
//		
//		log.info("Logging in");
//		Dashboard dashboard = launchWithLogin(user1);
//		
//		log.info("Open Opportunity detail page");
//		ViewOpportunityPage viewOpportunityPage = dashboard.openViewOpportunity();
//		viewOpportunityPage.searchForOpportunity(oppty);
//		OpportunityDetailPage opportunityDetailPage = viewOpportunityPage.selectResult(oppty);
//		String clientName = opportunityDetailPage.getClientName();
//		DocumentsSubpanel documentsSubpanel = opportunityDetailPage.openDocumentsSubpanel();
//		
//		log.info("Upload Document");
//		documentsSubpanel.enterDocumentInfo(subPanelDoc);
//		documentsSubpanel.saveDocument();
//		
//		log.info("Verify Document visible in subpanel");
//		documentsSubpanel.verifyDocumentPresent(subPanelDoc);
//		exec.quit();
//		
//		log.info("Verify document in connections");
//		CommunityMainPage communityMainPage = launchCnxnCommunity(user1);
//		CommunityDetailPage communityDetailPage = communityMainPage.searchAndOpenCommunity(clientName,testConfig.getParameter(GC.cxnURL)+"/communities");
//		FilesMainPage filesMainPage = communityDetailPage.openFilesPage();
//		filesMainPage.isPageLoaded();
//		Assert.assertTrue(filesMainPage.isFilePresent(subPanelDoc.sDocName), "Document not found in connections community");
//		}
//		catch(Exception e){
//			log.info(e.toString());
//			commonUserAllocator.checkInAllUsersWithToken("s4675OpportunityShareDocumentWithOpportunityTeam");
//			commonUserAllocator.checkInAllGroupUsersWithToken(GC.busAdminGroup, "s4675OpportunityShareDocumentWithOpportunityTeam");
//			commonClientAllocator.checkInAllclientsWithToken("s4675OpportunityShareDocumentWithOpportunityTeam");
//			commonClientAllocator.checkInAllGroupClientWithToken(GC.DC, "s4675OpportunityShareDocumentWithOpportunityTeam");
//			Assert.assertTrue(false);
//		}
//	}
//	
//	@Test(groups = {},dependsOnMethods = {"Test_s4675OpportunityShareDocumentWithOpportunityTeam"})
//	public void Test_s9492OpportunityUnshareAndDeleteDocumentSharedWithOpportunityTeam() {
//		Logger log = LoggerFactory.getLogger(s4675_s9492OpportunityShareUnshareDocumentWithOpportunityTeam.class);
//		log.info("Start of test method Test_s9492OpportunityUnshareAndDeleteDocumentSharedWithOpportunityTeam");
//
//		try {
//			//Navigate to Homepage, workaround for defect 11764. Should be remove after defect fixed
//			log.info("Logging in");
//			Dashboard dashboard = launchWithLogin(user1);
//			
//			subPanelDoc.vShareWithOptions.removeAllElements();
//			
//			ViewOpportunityPage viewOpptyPage = dashboard.openViewOpportunity();
//			viewOpptyPage.searchForOpportunity(oppty);
//			OpportunityDetailPage opptyDetailPage = viewOpptyPage.selectResult(oppty);
//			
//			DocumentsSubpanel docSubpanel = opptyDetailPage.openDocumentsSubpanel();
//			docSubpanel.openEditDocumentSubpanel(subPanelDoc.sDocName);
//			docSubpanel.updateSharingOptions(subPanelDoc);
//
//			viewOpptyPage = dashboard.openViewOpportunity();
//			viewOpptyPage.searchForOpportunity(oppty);
//			opptyDetailPage = viewOpptyPage.selectResult(oppty);
//			
//			docSubpanel = opptyDetailPage.openDocumentsSubpanel();
//			docSubpanel.openEditDocumentSubpanel(subPanelDoc.sDocName);
//			docSubpanel.deleteDocument();
//			docSubpanel.isPageLoaded();
//			exec.quit();
//			
//			FilesMainPage filesMainPage = launchCnxnFiles(user1);
//			filesMainPage.openSharedByMe();
//			Assert.assertFalse(filesMainPage.isFilePresent(subPanelDoc.sDocName), "Document not unshared");
//			
//			log.info("End of test method Test_s9492OpportunityUnshareAndDeleteDocumentSharedWithOpportunityTeam");
//		} finally {
//			commonUserAllocator.checkInAllUsersWithToken("s4675OpportunityShareDocumentWithOpportunityTeam");
//			commonUserAllocator.checkInAllGroupUsersWithToken(GC.busAdminGroup, "s4675OpportunityShareDocumentWithOpportunityTeam");
//			commonClientAllocator.checkInAllclientsWithToken("s4675OpportunityShareDocumentWithOpportunityTeam");
//			commonClientAllocator.checkInAllGroupClientWithToken(GC.DC, "s4675OpportunityShareDocumentWithOpportunityTeam");
//		}
//	}
//}
