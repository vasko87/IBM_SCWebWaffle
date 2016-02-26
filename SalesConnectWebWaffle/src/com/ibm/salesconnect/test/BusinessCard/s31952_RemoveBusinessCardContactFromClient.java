package com.ibm.salesconnect.test.BusinessCard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.partials.ContactSubpanel;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Client.ClientDetailPage;
import com.ibm.salesconnect.model.standard.Client.ViewClientPage;
import com.ibm.salesconnect.objects.Client;
import com.ibm.salesconnect.objects.Contact;

/**
 * @author evafarrell
 * @date Sept 27, 2013
 */
@Test(groups = { "BusinessCard"})
public class s31952_RemoveBusinessCardContactFromClient extends ProductBaseTest {
	
	Logger log = LoggerFactory.getLogger(s31952_RemoveBusinessCardContactFromClient.class);

	
	public void Test_s31952_RemoveBusinessCardContactFromClient() {
		log.info("Start test method Test_s31952_RemoveBusinessCardContactFromClient");
		
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);

		PoolClient poolClient = commonClientAllocator.getGroupClient("SC",this);
		Client client = new Client();
		client.sClientID = poolClient.getCCMS_ID();
		client.sClientName = poolClient.getClientName(testConfig.getBrowserURL(), user1);		
		client.sCCMS_Level = "SC";
		
		//Client variables
		client.sSearchIn= GC.showingInSiteID;
		client.sSearchFor= GC.searchForAll;
		client.sSearchShowing=GC.showingForSites;
		
		// Contact variables
		Contact contact = new Contact();
		contact.sClientName = client.sClientName;
		contact.sFirstName = "ClientSubPanelContact " + GC.sUniqueNum;
		contact.sLastName = "Last " + GC.sUniqueNum;
		contact.sPreferredName = "Pref " + GC.sUniqueNum;
		contact.sJobTitle = "Sales";
		contact.sMobile = "555-555-5555";
		contact.sOfficePhone = "666-666-6666";
		contact.sEmail0 = "email1"+GC.sUniqueNum+"@tst.ibm.com";
		
		log.info("Logging in to SalesConnect");
		Dashboard dashboard = launchWithLogin(user1);
			
		log.info("Open Client detail page");
		ViewClientPage viewClientPage = dashboard.openViewClient();
		viewClientPage.searchForClient(client);
		viewClientPage.isPageLoaded();
		ClientDetailPage clientDetailPage = viewClientPage.selectResult(client);
		clientDetailPage.isPageLoaded();
		
		ContactSubpanel contactSubpanel = clientDetailPage.openContactsSubpanel();
			
		log.info("Creating contact on client subpanel");
		contactSubpanel.openCreateContactForm();
		contactSubpanel.enterContactInfo(contact, client);
		contactSubpanel.saveContact();
		
		log.info("Verify that Hovering over new Contact does not find a Business Card");
		contactSubpanel.verifyBusinessCardDoesNotExist(contact.sFirstName);
			
		log.info("End of test method Test_s31952_RemoveBusinessCardContactFromClient");
		}

	}
