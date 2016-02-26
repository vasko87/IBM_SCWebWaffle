package com.ibm.salesconnect.test.DocSharing;

import java.sql.SQLException;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.DB2Connection;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.partials.DocumentsSubpanel;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Client.ClientDetailPage;
import com.ibm.salesconnect.model.standard.Client.ViewClientPage;
import com.ibm.salesconnect.model.standard.Connections.Files.FilesMainPage;
import com.ibm.salesconnect.objects.Client;
import com.ibm.salesconnect.objects.Document;

@Test(groups = {"DocSharing","Regression"})
public class s4675ClientShareDocumentWithEveryone extends ProductBaseTest{

	@Test(groups = {"DocSharing"})
	public void testMain() {
		Logger log = LoggerFactory.getLogger(s4675ClientShareDocumentWithEveryone.class);
		log.info("Start of test method s4675ClientShareDocumentWithEveryone");
		User db2User = commonUserAllocator.getGroupUser(GC.db2UserGroup,this);
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
		
		DB2Connection db2 = new DB2Connection(getParameter(GC.db2URL),db2User.getUid(),db2User.getPassword());
		PoolClient poolClient = commonClientAllocator.getGroupClient("DC",this);
		Client clientReturned = new Client();
		try {
			clientReturned = db2.retrieveClient(poolClient, user1.getEmail(),testConfig.getParameter(GC.testPhase));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Client returned from DB: " + clientReturned.sClientName);
		
		//User user2 = commonUserAllocator.getUser(this);
		//User user3 = commonUserAllocator.getUser(this);
		//User user4 = commonUserAllocator.getUser(this);
		
		clientReturned.sCCMS_Level = "DC";
		
		//Client variables
		clientReturned.sSearchIn= GC.showingInClientID;
		clientReturned.sSearchFor= GC.searchForAll;
		clientReturned.sSearchShowing=GC.showingForClients;

		Document subPanelDoc = new Document();
		subPanelDoc.sDocName = "s4675_" + new Random().nextInt(100000);
		subPanelDoc.sDocLocation = testConfig.getParameter("root_folder_name");
		subPanelDoc.sDescription = "Doc sharing description " + subPanelDoc.sDocName;
		subPanelDoc.vShareWithOptions.add(GC.gsShareWithEveryone);
				
		//TESTCASE ENVIRONMENT SETUP
		
		log.info("Creating Document " + subPanelDoc.sDocName + "...");
		Assert.assertEquals(true, createFile(subPanelDoc.sDocLocation, subPanelDoc.sDocName, "Test"), "Document " + subPanelDoc.sDocName + " not created successfully");
		
		//Navigate to Homepage, workaround for defect 11764. Should be remove after defect fixed
		log.info("Logging in");
		Dashboard dashboard = launchWithLogin(user1);
		
		//TESTCASE ENVIRONMENT SETUP COMPLETE
			
			//Navigate to provisioned Client detail page and create document from sub-panel
			ViewClientPage viewClientPage = dashboard.openViewClient();
			ClientDetailPage clientDetailPage;
			if (!viewClientPage.checkResult(clientReturned)) {
				viewClientPage.isPageLoaded();
				viewClientPage.searchForClient(clientReturned);
				clientDetailPage = viewClientPage.selectResult(clientReturned);
			}
			else {
				clientDetailPage = viewClientPage.selectResult(clientReturned);
			}
				
			//Create Doc (since we are still on the same page)
			DocumentsSubpanel docSubpanel = clientDetailPage.openDocumentsSubpanel();
			docSubpanel.enterDocumentInfo(subPanelDoc);
			docSubpanel.saveDocument();

			docSubpanel.verifyDocumentPresent(subPanelDoc);
			exec.quit();
		
			log.info("Verify document in connections");
			FilesMainPage filesMainPage = launchCnxnFiles(user1);
			filesMainPage.openPublicFiles();
			filesMainPage.isPageLoaded();
			Assert.assertTrue(filesMainPage.isFilePresent(subPanelDoc.sDocName), "Document not found in Files - Public Files");
			
			log.info("End of test method s4675ClientShareDocumentWithEveryone");
	}
}
