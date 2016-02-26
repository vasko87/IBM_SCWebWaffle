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
 * @date June 5, 2013
 */
public class s4675OpportunityShareDocumentWithEveryone extends
		ProductBaseTest {
	Logger log = LoggerFactory.getLogger(s4675OpportunityShareDocumentWithEveryone.class);

	int rand = new Random().nextInt(100000);
	String contactID = "22SC-" + rand;
	String opptyID = null;
	String clientID = null;
	SugarAPI sugarAPI = new SugarAPI();
	Opportunity oppt = new Opportunity();

	
	@Test(groups = {"DocSharing","Regression"})
	public void Test_s4675OpportunityShareDocumentWithEveryone() throws SQLException, InterruptedException {
		log.info("Start of test setup for Test_s4675OpportunityShareDocumentWithEveryone");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
		Document subPanelDoc = new Document();
		
		subPanelDoc.sDocName = "s4675_" + new Random().nextInt(100000);
		subPanelDoc.sDocLocation = testConfig.getParameter("root_folder_name");
		subPanelDoc.sDescription = "Doc sharing description " + subPanelDoc.sDocName;
		subPanelDoc.vShareWithOptions.add(GC.gsShareWithEveryone);
		
		clientID = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();

		sugarAPI.createContact(testConfig.getBrowserURL(), contactID, clientID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");
		opptyID = sugarAPI.createOppty(testConfig.getBrowserURL(), "", contactID, clientID, user1.getEmail(), user1.getPassword(), GC.emptyArray);
		oppt.sOpptNumber = opptyID;
		oppt.sPrimaryContact = "ContactFirst ContactLast";
		oppt.sAccID = clientID;
		
		log.info("Creating Document " + subPanelDoc.sDocName + "...");
		Assert.assertEquals(true, createFile(subPanelDoc.sDocLocation, subPanelDoc.sDocName, "Test"), "Document " + subPanelDoc.sDocName + " not created successfully");
		
		log.info("End of test setup for Test_s4675OpportunityShareDocumentWithEveryone");
		log.info("Start of test for Test_s4675OpportunityShareDocumentWithEveryone");
		
		log.info("Logging in");
		Dashboard dashboard = launchWithLogin(user1);
		
		log.info("Open Opportunity detail page");
		ViewOpportunityPage viewOpportunityPage = dashboard.openViewOpportunity();
		viewOpportunityPage.searchForOpportunity(oppt);
		OpportunityDetailPage opportunityDetailPage = viewOpportunityPage.selectResult(oppt);
				
		log.info("Upload Document and Share with Everyone");
		opportunityDetailPage.isPageLoaded();
		DocumentsSubpanel documentsSubpanel = opportunityDetailPage.openDocumentsSubpanel();
		documentsSubpanel.enterDocumentInfo(subPanelDoc);
		documentsSubpanel.saveDocument();
		
		log.info("Verify Document visible in subpanel");
		documentsSubpanel.isPageLoaded();
		documentsSubpanel.verifyDocumentPresent(subPanelDoc);
		
		exec.quit();
		
		log.info("Verify document in connections");
		FilesMainPage filesMainPage = launchCnxnFiles(user1);
		Assert.assertTrue(filesMainPage.isFilePresent(subPanelDoc.sDocName), "Document not found in Files - My Files");
		filesMainPage.openPublicFiles();
		filesMainPage.isPageLoaded();
		Assert.assertTrue(filesMainPage.isFilePresent(subPanelDoc.sDocName), "Document not found in Files - Public Files");
		

		log.info("Remove the oppty created for this test");
		sugarAPI.deleteOpptySOAP(testConfig.getBrowserURL(), opptyID, user1.getEmail(), user1.getPassword());
		sugarAPI.deleteContactSOAP(testConfig.getBrowserURL(), contactID, user1.getEmail(), user1.getPassword());
		
		commonUserAllocator.checkInAllGroupUsersWithToken(GC.busAdminGroup, "s4675OpportunityShareDocumentWithEveryone");
	}
}
