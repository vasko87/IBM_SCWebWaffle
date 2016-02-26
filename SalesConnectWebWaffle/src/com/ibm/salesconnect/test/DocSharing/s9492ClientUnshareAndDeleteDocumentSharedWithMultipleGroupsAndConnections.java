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

/**
 * @author Stephen Cole
 * @date July 8, 2013
 * Not ready for Regression yet...
 */
public class s9492ClientUnshareAndDeleteDocumentSharedWithMultipleGroupsAndConnections extends
		ProductBaseTest {
	Logger log = LoggerFactory.getLogger(s9492ClientUnshareAndDeleteDocumentSharedWithMultipleGroupsAndConnections.class);

	//Not ready for Regression yet... SCole
	@Test(groups = {""})
	public void Test_s9492ClientUnshareAndDeleteDocumentSharedWithMultipleGroupsAndConnections() throws SQLException, InterruptedException {
		log.info("Start of test setup for Test_s9492ClientUnshareAndDeleteDocumentSharedWithMultipleGroupsAndConnections");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
		User user2 = commonUserAllocator.getUser(this);
		User user3 = commonUserAllocator.getUser(this);
		User db2User = commonUserAllocator.getGroupUser(GC.db2UserGroup,this);
		DB2Connection db2 = new DB2Connection(getParameter(GC.db2URL),db2User.getUid(),db2User.getPassword());
		Document subPanelDoc = new Document();
		Document subPanelDocExternal = new Document();
		
		subPanelDoc.sDocName = "s9492_" + new Random().nextInt(100000);
		subPanelDoc.sDocLocation = testConfig.getParameter("root_folder_name");
		subPanelDoc.sDescription = "Doc sharing description " + subPanelDoc.sDocName;
		subPanelDoc.vShareWithOptions.add(GC.gsShareWithClientTeam);
		
		subPanelDocExternal.sExternalFileName = subPanelDoc.sDocName;
		subPanelDocExternal.sDocName = subPanelDoc.sDocName;
		subPanelDocExternal.sDescription = subPanelDoc.sDescription;
		subPanelDocExternal.vShareWithOptions.add(GC.gsShareWithIndividuals);
		subPanelDocExternal.vUsers.addElement(user2.getFirstName()+" "+user2.getLastName());
		subPanelDocExternal.vUsers.addElement(user3.getFirstName()+" "+user3.getLastName());
		
		PoolClient poolClient = commonClientAllocator.getGroupClient(GC.DC, this);
		Client client = db2.retrieveClient(poolClient, user1.getEmail(), testConfig.getParameter(GC.testPhase));
		
		client.sSearchIn= GC.showingInClientID;
		client.sSearchFor= GC.searchForAll;
		client.sSearchShowing=GC.showingForClients;
		
		log.info("Creating Document " + subPanelDoc.sDocName + "...");
		Assert.assertEquals(true, createFile(subPanelDoc.sDocLocation, subPanelDoc.sDocName, "Test"), "Document " + subPanelDoc.sDocName + " created successfully");
		
		log.info("End of test setup for s9492ClientUnshareAndDeleteDocumentSharedWithMultipleGroupsAndConnections");
		log.info("Start of test for s9492ClientUnshareAndDeleteDocumentSharedWithMultipleGroupsAndConnections");
		
		log.info("Logging in");
		Dashboard dashboard = launchWithLogin(user1);
		
		log.info("Open Client detail page");
		ViewClientPage viewClientPage = dashboard.openViewClient();
		viewClientPage.searchForClient(client);
		ClientDetailPage clientDetailPage = viewClientPage.selectResult(client);
				
		log.info("Upload Document and Share with Multiple Groups");
		DocumentsSubpanel documentsSubpanel = clientDetailPage.openDocumentsSubpanel();
		documentsSubpanel.enterDocumentInfo(subPanelDoc);
		documentsSubpanel.saveDocument();
		
		log.info("Share document with Client team");
		
		
		
		log.info("Verify Document visible in subpanel");
		documentsSubpanel.verifyDocumentPresent(subPanelDoc);
	
		log.info("Verify document in connections as User1 - Document Owner");
		exec.quit();
		FilesMainPage filesMainPage = launchCnxnFiles(user1);
		log.info("Verify document found in My Files");
		Assert.assertTrue(filesMainPage.isFilePresent(subPanelDoc.sDocName), "Document not found in Files - My Files");
		filesMainPage.openSharedByMe();
		filesMainPage.isPageLoaded();
		log.info("Verify document found in Shared By Me");
		Assert.assertTrue(filesMainPage.isFilePresent(subPanelDoc.sDocName), "Document not found in Files - Shared By Me");
		
		log.info("Verify document in connections as User2 - Document Shared With");
		exec.quit();
		filesMainPage = launchCnxnFiles(user2);
		log.info("Verify document found in My Files");
		Assert.assertTrue(!filesMainPage.isFilePresent(subPanelDoc.sDocName), "Document should not be found in Files - My Files");
		filesMainPage.openSharedWithMe();
		filesMainPage.isPageLoaded();
		log.info("Verify document not found in Shared With Me");
		Assert.assertTrue(!filesMainPage.isFilePresent(subPanelDoc.sDocName), "Document not found in Files - Shared With Me");
		
		log.info("Verify document in connections as User3 - Document Shared With Group Member");
		exec.quit();
		filesMainPage = launchCnxnFiles(user3);
		log.info("Verify document not found in My Files");
		Assert.assertTrue(!filesMainPage.isFilePresent(subPanelDoc.sDocName), "Document should not be found in Files - My Files");
		filesMainPage.openSharedWithMe();
		log.info("Verify document found in Shared With Me");
		Assert.assertTrue(filesMainPage.isFilePresent(subPanelDoc.sDocName), "Document found in Files - Shared With Me");
		
		log.info("Logging in");
		dashboard = launchWithLogin(user1);
		
		log.info("Open Client detail page");
		viewClientPage = dashboard.openViewClient();
		viewClientPage.searchForClient(client);
		clientDetailPage = viewClientPage.selectResult(client);
				
		log.info("Upload Document and Share with Multiple Users");
		documentsSubpanel = clientDetailPage.openDocumentsSubpanel();
		documentsSubpanel.enterDocumentInfo(subPanelDoc);
		documentsSubpanel.saveDocument();
		
		log.info("Verify Document visible in subpanel");
		documentsSubpanel.verifyDocumentPresent(subPanelDoc);
		
		commonUserAllocator.checkInAllGroupUsersWithToken(GC.busAdminGroup, "s9492ClientUnshareAndDeleteDocumentSharedWithMultipleGroupsAndConnections");
		
		commonClientAllocator.checkInAllGroupClientWithToken(GC.DC, this);
		
		log.info("End of test method Test_s9492ClientUnshareAndDeleteDocumentSharedWithMultipleGroupsAndConnections");
	}
}