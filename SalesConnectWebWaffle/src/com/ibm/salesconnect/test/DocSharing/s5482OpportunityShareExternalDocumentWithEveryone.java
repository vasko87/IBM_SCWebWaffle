package com.ibm.salesconnect.test.DocSharing;
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
import com.ibm.salesconnect.model.standard.Connections.Community.AppsFilePage;
import com.ibm.salesconnect.model.standard.Connections.Community.CommunityMainPage;
import com.ibm.salesconnect.model.standard.Opportunity.OpportunityDetailPage;
import com.ibm.salesconnect.model.standard.Opportunity.ViewOpportunityPage;
import com.ibm.salesconnect.objects.Document;
import com.ibm.salesconnect.objects.Opportunity;

/**
/**
 * Description   : Rational Functional Tester Script
 * 
 */

public class s5482OpportunityShareExternalDocumentWithEveryone extends ProductBaseTest {
	/**
	 * Script Name   : <b>s5482OpportunityShareExternalDocumentWithEveryone</b>
	 * Generated     : <b>19 Feb 2012 00:57:09</b>
	 * Original Host : WinNT Version 6.1  Build 7601 (S)
	 * 
	 * @since  2012/02/19
	 * @author mxu
	 */
	
	@Test(groups = {"DocSharing"})
	public void Test_s5482OpportunityShareExternalDocumentWithEveryone(){
		Logger log = LoggerFactory.getLogger(s5482OpportunityShareExternalDocumentWithEveryone.class);
		log.info("Start of test method s5482OpportunityShareExternalDocumentWithEveryone");
		
		log.info("Getting users");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);

		int rand = new Random().nextInt(100000);
		String contactID = "22SC-" + rand;
		String opptyID = null;
		String clientID = null;
		SugarAPI sugarAPI = new SugarAPI();
		Opportunity oppt = new Opportunity();
		
			try {
				// Get a client
				clientID = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
			
				//Create Contact and Oppty
				sugarAPI.createContact(testConfig.getBrowserURL(), contactID, clientID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");
				opptyID = sugarAPI.createOppty(testConfig.getBrowserURL(), "", contactID, clientID, user1.getEmail(), user1.getPassword(), GC.emptyArray);
				oppt.sOpptNumber = opptyID;
				oppt.sPrimaryContact = "ContactFirst ContactLast";
				oppt.sAccID = clientID;
				
				//Document variables
				Document subPanelDoc = new Document();
				subPanelDoc.sDocName = "s5482_" + new Random().nextInt(100000)+".txt";
				subPanelDoc.sDocLocation = testConfig.getParameter("root_folder_name");
				subPanelDoc.sDescription = "Doc sharing description " + subPanelDoc.sDocName;
				subPanelDoc.vShareWithOptions.add(GC.gsShareWithEveryone);
				
				Assert.assertEquals(true, createFile(subPanelDoc.sDocLocation, subPanelDoc.sDocName, "Test"), "Document " + subPanelDoc.sDocName + " created successfully");
				
				// Login Sugar as User
				log.info("Logging in");
				Dashboard dashboard = launchWithLogin(user1);
				
				log.info("Open Opportunity Details Page");
				ViewOpportunityPage viewOppPage = dashboard.openViewOpportunity();
				viewOppPage.searchForOpportunity(oppt);
				viewOppPage.isPageLoaded();
				OpportunityDetailPage oppDetailPage = viewOppPage.selectResult(oppt);
				oppDetailPage.isPageLoaded();
				
				log.info("Create Document From Subpanel");
				DocumentsSubpanel docSubpanel = oppDetailPage.openDocumentsSubpanel();
				docSubpanel.enterDocumentInfo(subPanelDoc);
				docSubpanel.saveDocument();
				docSubpanel.isPageLoaded();
				docSubpanel.verifyDocumentPresent(subPanelDoc);	
				
				log.info("Create Another Document Selecting the Doc you just created");
				subPanelDoc.sExternalFileName = subPanelDoc.sDocName;
				subPanelDoc.sDocLocation = "";
				docSubpanel.enterDocumentInfo(subPanelDoc);
				docSubpanel.saveDocument();
				docSubpanel.isPageLoaded();
				docSubpanel.verifyDocumentPresent(subPanelDoc);	
				
				exec.close();
				log.info("Verify document in connections");
				log.info("Login to connections");
				CommunityMainPage communityMainPage = launchCnxnCommunity(user1);

				log.info("Click on Files under Apps tab");
				AppsFilePage filesPage = communityMainPage.openAppsFilePage();
				//filesPage.searchForFile(subPanelDoc.sExternalFileName);
				
				log.info("Verify if uploaded file exists in My Files");
				Assert.assertTrue(filesPage.isFilePresent(subPanelDoc.sExternalFileName), "Document not found in my files");
				
				log.info("Click on Public Files");
				filesPage.openPublicFiles();
				filesPage.isPageLoaded();
		
				//Verify if uploaded file exists in Public Files
				//filesPage.searchForFile(subPanelDoc.sExternalFileName);
				
				//Click on file link
				Assert.assertTrue(filesPage.isFilePresent(subPanelDoc.sExternalFileName), "Document not found in public files search");
				
				log.info("End of test method s5482OpportunityShareExternalDocumentWithEveryone");

				log.info("Remove the oppty created for this test");
				sugarAPI.deleteOpptySOAP(testConfig.getBrowserURL(), opptyID, user1.getEmail(), user1.getPassword());
				sugarAPI.deleteContactSOAP(testConfig.getBrowserURL(), contactID, user1.getEmail(), user1.getPassword());
				commonUserAllocator.checkInAllGroupUsersWithToken(GC.busAdminGroup, "s5482OpportunityShareExternalDocumentWithEveryone");
			}
		catch(Exception e){
			log.error(e.toString());

			log.info("Remove the oppty created for this test");
			sugarAPI.deleteOpptySOAP(testConfig.getBrowserURL(), opptyID, user1.getEmail(), user1.getPassword());
			sugarAPI.deleteContactSOAP(testConfig.getBrowserURL(), contactID, user1.getEmail(), user1.getPassword());
			Assert.assertTrue(false);
		}
	}
}
