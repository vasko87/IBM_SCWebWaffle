/**
 * 
 */
package com.ibm.salesconnect.test.DocSharing;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
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
 * @author Tyler Clayton
 * @date July 8, 2013
 */
public class s5482ClientShareExternalDocumentWithMultipleGroupsAndConnectionsCommunity extends
		ProductBaseTest {
	Logger log = LoggerFactory.getLogger(s5482ClientShareExternalDocumentWithMultipleGroupsAndConnectionsCommunity.class);

	@Test(groups = {"DocSharing"})
	public void Test_s5482ClientShareExternalDocumentWithMultipleGroupsAndConnectionsCommunity(){
		log.info("Start of test setup for Test_s5482ClientShareExternalDocumentWithMultipleGroupsAndConnectionsCommunity");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
		User user2 = commonUserAllocator.getUser(this);
		Document subPanelDoc = new Document();
		Document subPanelDocExternal = new Document();
		
		subPanelDoc.sDocName = "s5482_" + new Random().nextInt(100000);
		subPanelDoc.sDocLocation = testConfig.getParameter("root_folder_name");
		subPanelDoc.sDescription = "Doc sharing description " + subPanelDoc.sDocName;
		subPanelDoc.vShareWithOptions.add(GC.gsShareWithClientTeam);
		
		subPanelDocExternal.sExternalFileName = subPanelDoc.sDocName;
		subPanelDocExternal.sDocName = subPanelDoc.sDocName;
		subPanelDocExternal.sDescription = subPanelDoc.sDescription;
		subPanelDocExternal.vShareWithOptions.add(GC.gsShareWithIndividualsAndClientTeam);
		subPanelDocExternal.vUsers.addElement(user2.getFirstName()+" "+user2.getLastName());
		
		PoolClient poolClient = commonClientAllocator.getGroupClient(GC.DC, this);
		Client client = new Client();
		client.sClientID = poolClient.getCCMS_ID();
		client.sClientName = poolClient.getClientName(testConfig.getBrowserURL(), user1);
		client.sSearchIn= GC.showingInClientID;
		client.sSearchFor= GC.searchForAll;
		client.sSearchShowing=GC.showingForClients;
			
		log.info("Creating Document " + subPanelDoc.sDocName + "...");
		Assert.assertEquals(true, createFile(subPanelDoc.sDocLocation, subPanelDoc.sDocName, "Test"), "Document " + subPanelDoc.sDocName + " created successfully");
		
		log.info("End of test setup for s5482ClientShareExternalDocumentWithMultipleGroupsAndConnectionsCommunity");
		log.info("Start of test for s5482ClientShareExternalDocumentWithMultipleGroupsAndConnectionsCommunity");
		
		log.info("Logging in");
		Dashboard dashboard = launchWithLogin(user1);
		
		log.info("Open Client detail page");
		ViewClientPage viewClientPage = dashboard.openViewClient();
		viewClientPage.searchForClient(client);
		viewClientPage.isPageLoaded();
		ClientDetailPage clientDetailPage = viewClientPage.selectResult(client);
				
		log.info("Upload Document and Share with Multiple Users");
		DocumentsSubpanel documentsSubpanel = clientDetailPage.openDocumentsSubpanel();
		documentsSubpanel.enterDocumentInfo(subPanelDoc);
		documentsSubpanel.saveDocument();
		
		log.info("Verify Document visible in subpanel");
		documentsSubpanel.verifyDocumentPresent(subPanelDoc);
		
		documentsSubpanel = clientDetailPage.openDocumentsSubpanel();
//		documentsSubpanel.scrollToBottomOfPage();
		documentsSubpanel.isPageLoaded();
		documentsSubpanel.enterDocumentInfo(subPanelDocExternal);
		documentsSubpanel.saveDocument();
		
		log.info("Verify Document visible in subpanel");
		documentsSubpanel.verifyDocumentPresent(subPanelDocExternal);
	
		log.info("Verify document in connections as User1 - Document Owner");
		exec.quit();
		FilesMainPage filesMainPage = launchCnxnFiles(user1);
		log.info("Verify document found in My Files");
		Assert.assertTrue(filesMainPage.isFilePresent(subPanelDoc.sDocName), "Document not found in Files - My Files");
		
		filesMainPage.openFileSharing(subPanelDoc.sDocName);
		Assert.assertTrue(filesMainPage.isSharedWith(user2.getDisplayName()),"File not shared with " + user2.getDisplayName() + " as expected");
		Assert.assertTrue(filesMainPage.isSharedWithReader(client.sClientName),"File not shared with " + client.sClientName + " as expected");


		log.info("End of test method Test_s5482ClientShareExternalDocumentWithMultipleGroupsAndConnectionsCommunity");
	}
}