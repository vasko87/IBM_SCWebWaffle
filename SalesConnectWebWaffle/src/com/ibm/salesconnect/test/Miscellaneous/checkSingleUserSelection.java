/**
 * 
 */
package com.ibm.salesconnect.test.Miscellaneous;

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
import com.ibm.salesconnect.objects.Client;
import com.ibm.salesconnect.objects.Document;

/**
 * @author timlehane
 * @date Aug 21, 2013
 */
public class checkSingleUserSelection extends ProductBaseTest {
	Logger log = LoggerFactory.getLogger(checkSingleUserSelection.class);
	
	@Test(groups = {"Miscellaneous"})
	public void Test_checkSingleUserSelection(){
		log.info("Start of test Test_checkSingleUserSelection");

		try{
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
		User user2 = commonUserAllocator.getUser(this);
		
		PoolClient poolClient = commonClientAllocator.getGroupClient(GC.DC, this);
		Client client = new Client();
		client.sClientID = poolClient.getCCMS_ID();
		client.sClientName = poolClient.getClientName(testConfig.getBrowserURL(), user1);
		client.sSearchIn= GC.showingInClientID;
		client.sSearchFor= GC.searchForAll;
		client.sSearchShowing=GC.showingForClients;
		
		Document subPanelDoc = new Document();
		subPanelDoc.sDocName = "s5482_" + new Random().nextInt(100000);
		subPanelDoc.sDocLocation = testConfig.getParameter("root_folder_name");
		subPanelDoc.sDescription = "Doc sharing description " + subPanelDoc.sDocName;
		subPanelDoc.vShareWithOptions.add(GC.gsShareWithIndividualUserNameSelect);
		subPanelDoc.vUsers.addElement(user2.getFirstName()+" "+user2.getLastName());
		
		log.info("Creating Document " + subPanelDoc.sDocName + "...");
		Assert.assertEquals(true, createFile(subPanelDoc.sDocLocation, subPanelDoc.sDocName, "Test"), "Document " + subPanelDoc.sDocName + " created successfully");
			
		Dashboard dashboard = launchWithLogin(user1);
		
		ViewClientPage viewClientPage = dashboard.openViewClient();
		viewClientPage.searchForClient(client);
		viewClientPage.isPageLoaded();
		ClientDetailPage clientDetailPage = viewClientPage.selectResult(client);
		clientDetailPage.isPageLoaded();
		
		log.info("Upload Document and Share with user");
		DocumentsSubpanel documentsSubpanel = clientDetailPage.openDocumentsSubpanel();
		documentsSubpanel.enterDocumentInfo(subPanelDoc);
		documentsSubpanel.saveDocument();
		documentsSubpanel.isPageLoaded();
		documentsSubpanel.openEditDocumentSubpanel(subPanelDoc.sDocName);
		
		log.info("Confirm that user is on shared list");
		Assert.assertTrue(documentsSubpanel.isSharedWith(user2.getDisplayName()), "Document is not shared with user as expected");
		log.info("End of test Test_checkSingleUserSelection");
	
	}
	catch(Exception e){
		log.info(e.getMessage());
		commonUserAllocator.checkInAllUsersWithToken("checkSingleUserSelection");
		commonUserAllocator.checkInAllGroupUsersWithToken(GC.busAdminGroup, "checkSingleUserSelection");
		commonClientAllocator.checkInAllclientsWithToken("checkSingleUserSelection");
		commonClientAllocator.checkInAllGroupClientWithToken(GC.SC, "checkSingleUserSelection");
		Assert.assertTrue(false);
	}

}
}