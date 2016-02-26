/**
 * 
 */
package com.ibm.salesconnect.test.DocSharing;

import java.sql.SQLException;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.SugarAPI;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.partials.DocumentsSubpanel;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Connections.Files.FilesMainPage;
import com.ibm.salesconnect.model.standard.Opportunity.OpportunityDetailPage;
import com.ibm.salesconnect.model.standard.Opportunity.ViewOpportunityPage;
import com.ibm.salesconnect.objects.Document;
import com.ibm.salesconnect.objects.Opportunity;


/**
 * @author evafarrell
 * @date June 12, 2013
 */
public class s4675_s9492OpportunityShareUnshareDocumentWithMultipleUsers extends
		ProductBaseTest {
	Logger log = LoggerFactory.getLogger(s4675_s9492OpportunityShareUnshareDocumentWithMultipleUsers.class);

	User user1 = null;
	User user2 = null;
	User user3 = null;
	
	Document subPanelDoc = new Document();
	int rand = new Random().nextInt(100000);
	String contactID = "22SC-" + rand;
	String opptyID = null;
	String clientID = null;
	SugarAPI sugarAPI = new SugarAPI();
	Opportunity oppty = new Opportunity();

	@Test(groups = {"DocSharing"})
	public void Test_s4675OpportunityShareDocumentWithMultipleUsers() throws SQLException, InterruptedException {
		log.info("Start of test setup for Test_s4675OpportunityShareDocumentWithMultipleUsers");

		try{
			user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
			user2 = commonUserAllocator.getUser(this);
			user3 = commonUserAllocator.getUser(this);
			clientID = commonClientAllocator.getGroupClient(GC.DC,this).getCCMS_ID();
		sugarAPI.createContact(testConfig.getBrowserURL(), contactID, clientID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");
		opptyID = sugarAPI.createOppty(testConfig.getBrowserURL(), "", contactID, clientID, user1.getEmail(), user1.getPassword(), GC.emptyArray);
		oppty.sOpptNumber = opptyID;
		oppty.sPrimaryContact = "ContactFirst ContactLast";
		oppty.sAccID = clientID;
		
		
		subPanelDoc.sDocName = "s4675_" + rand;
		subPanelDoc.sDocLocation = testConfig.getParameter("root_folder_name");
		subPanelDoc.sDescription = "Doc sharing description " + subPanelDoc.sDocName;
		subPanelDoc.vShareWithOptions.add(GC.gsShareWithIndividuals);
		subPanelDoc.vUsers.addElement(user2.getFirstName()+" "+user2.getLastName());
		subPanelDoc.vUsers.addElement(user3.getFirstName()+" "+user3.getLastName());
			
		log.info("Creating Document " + subPanelDoc.sDocName + "...");
		Assert.assertEquals(true, createFile(subPanelDoc.sDocLocation, subPanelDoc.sDocName, "Test"), "Document " + subPanelDoc.sDocName + " created successfully");
		
		log.info("End of test setup for Test_s4675OpportunityShareDocumentWithMultipleUsers");
		log.info("Start of test for Test_s4675OpportunityShareDocumentWithMultipleUsers");
		
		log.info("Logging in");
		Dashboard dashboard = launchWithLogin(user1);
		
		log.info("Open Opportunity detail page");
		ViewOpportunityPage viewOpportunityPage = dashboard.openViewOpportunity();
		viewOpportunityPage.searchForOpportunity(oppty);
		OpportunityDetailPage opportunityDetailPage = viewOpportunityPage.selectResult(oppty);
				
		log.info("Upload Document and Share with Multiple Users");
		opportunityDetailPage.isPageLoaded();
		DocumentsSubpanel documentsSubpanel = opportunityDetailPage.openDocumentsSubpanel();
		documentsSubpanel.enterDocumentInfo(subPanelDoc);
		documentsSubpanel.saveDocument();
		
		log.info("Verify Document visible in subpanel");
		documentsSubpanel.verifyDocumentPresent(subPanelDoc);
	
		log.info("Verify document in connections as User1 - Document Owner");
		exec.quit();
		FilesMainPage filesMainPage = launchCnxnFiles(user1);
		log.info("Verify document found in My Files");
		filesMainPage.isPageLoaded();
		Assert.assertTrue(filesMainPage.isFilePresent(subPanelDoc.sDocName), "Document "+subPanelDoc.sDocName+" not found in Files - My Files");
		
		filesMainPage.openSharedByMe();
		log.info("Verify document found in Shared By Me");
		Assert.assertTrue(filesMainPage.isFilePresent(subPanelDoc.sDocName), "Document "+subPanelDoc.sDocName+" not found in Files - Shared By Me");
		filesMainPage.openFileSharing(subPanelDoc.sDocName);
		filesMainPage.isPageLoaded();
		
		Assert.assertTrue(filesMainPage.isSharedWith(user2.getDisplayName()),"File not shared with " + user2.getDisplayName() + " as expected");
		Assert.assertTrue(filesMainPage.isSharedWith(user3.getDisplayName()),"File not shared with " + user3.getDisplayName() + " as expected");
		
		}
		catch(Exception e){
			log.info(e.getMessage());

			log.info("Remove the oppty created for this test");
			sugarAPI.deleteOpptySOAP(testConfig.getBrowserURL(), opptyID, user1.getEmail(), user1.getPassword());
			sugarAPI.deleteContactSOAP(testConfig.getBrowserURL(), contactID, user1.getEmail(), user1.getPassword());
			Assert.assertTrue(false);
		}
		log.info("End of test method s4675OpportunityShareDocumentWithMultipleUsers");
	}
	
	@Test(groups = {"DocSharing"},dependsOnMethods={"Test_s4675OpportunityShareDocumentWithMultipleUsers"})
	public void Test_s9492OpportunityUnshareAndDeleteDocumentSharedWithMultipleUser() throws SQLException, InterruptedException {
		log.info("Start of test setup for Test_s9492OpportunityUnshareAndDeleteDocumentSharedWithMultipleUser");
		
		try{
		Dashboard dashboard = launchWithLogin(user1);
		
		log.info("Open Opportunity detail page");
		ViewOpportunityPage viewOpportunityPage = dashboard.openViewOpportunity();
		viewOpportunityPage.searchForOpportunity(oppty);
		OpportunityDetailPage opportunityDetailPage = viewOpportunityPage.selectResult(oppty);
		
		log.info("Verify Document visible in subpanel");
		opportunityDetailPage.isPageLoaded();
		DocumentsSubpanel documentsSubpanel = opportunityDetailPage.openDocumentsSubpanel();
		documentsSubpanel.verifyDocumentPresent(subPanelDoc);
		
		log.info("Edit Document and Unshare by deleting the document");
		documentsSubpanel.openEditDocumentSubpanel(subPanelDoc.sDocName);
		documentsSubpanel.deleteDocument();
		
		log.info("Verify document not in connections as User1 - Document Owner");
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
		log.info("End of test method s9492OpportunityUnshareAndDeleteDocumentSharedWithMultipleUser");
	}
}