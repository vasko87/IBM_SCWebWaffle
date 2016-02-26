package com.ibm.salesconnect.test.Miscellaneous;

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
import com.ibm.salesconnect.model.partials.ContactSubpanel;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Client.ClientDetailPage;
import com.ibm.salesconnect.model.standard.Client.ViewClientPage;
import com.ibm.salesconnect.model.standard.Contact.ViewContactPage;
import com.ibm.salesconnect.model.standard.Opportunity.OpportunityDetailPage;
import com.ibm.salesconnect.model.standard.Opportunity.ViewOpportunityPage;
import com.ibm.salesconnect.objects.Client;
import com.ibm.salesconnect.objects.Contact;
import com.ibm.salesconnect.objects.Opportunity;

/*
 * Creates Contact from Client Page SubPanel and Opportunity Page Subpanel
 * 1951 Select Contacts View and click Advanced Search and Tags Appears on Advanced Search page
 */

public class s1952CreateContactFromSubpanel extends ProductBaseTest {
	
	Logger log = LoggerFactory.getLogger(s1952CreateContactFromSubpanel.class);

	@Test(groups = { "Miscellaneous"})
	public void CreatContactFromClientSubpanel() {
		
			log.info("Start of test method CreatContactFromClientSubpanel");
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
		
			int rand = new Random().nextInt(100000);
			String contactID = "22SC-" + rand;
			String opptyID = null;

			SugarAPI sugarAPI = new SugarAPI();
			Opportunity oppty = new Opportunity();
			Client client = new Client();
			//Check out a client from the pool
			PoolClient poolClient = commonClientAllocator.getGroupClient(GC.SC,this);
			client.sSiteID = poolClient.getCCMS_ID();
			client.sClientName = poolClient.getClientName(testConfig.getBrowserURL(), user1);
			client.sCCMS_Level = "SC";
			client.sSearchIn = GC.showingInSiteID;
			client.sSearchFor = GC.searchForAll;
			client.sSearchShowing = GC.gsShowingFor[2];

			sugarAPI.createContact(testConfig.getBrowserURL(), contactID, client.sSiteID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");
			opptyID = sugarAPI.createOppty(testConfig.getBrowserURL(), "", contactID, client.sSiteID, user1.getEmail(), user1.getPassword(), GC.emptyArray);
			oppty.sOpptNumber = opptyID;
			oppty.sPrimaryContact = "ContactFirst ContactLast";
			oppty.sAccID = client.sSiteID;
			
			Contact contact1 = new Contact();
			Contact contact2 = new Contact();
			
			// Contact variables used in opportunity subpanel
			int sRand = (int) Math.round((Math.random()) * 10000);
			contact1.sClientName = client.sClientName;
			contact1.sFirstName = "OpptySubPanelContact " + sRand;
			contact1.sLastName = "Last " + sRand;
			contact1.sPreferredName = "Pref " + sRand;
			contact1.sJobTitle = "Sales";
			contact1.sMobile = "333-333-3333";
			contact1.sOfficePhone = "444-444-4444";
			contact1.sEmail0 = "email0"+sRand+"@tst.ibm.com";

			// Contact variables used in client subpanel
			contact2.sClientName = client.sClientName;
			contact2.sFirstName = "ClientSubPanelContact " + sRand;
			contact2.sLastName = "Last " + sRand;
			contact2.sPreferredName = "Pref " + sRand;
			contact2.sJobTitle = "Sales";
			contact2.sMobile = "555-555-5555";
			contact2.sOfficePhone = "666-666-6666";
			contact2.sEmail0 = "email1"+sRand+"@tst.ibm.com";
			
			//TESTCASE BEGIN
			log.info("Logging in");
			Dashboard dashboard = launchWithLogin(user1);
			
			log.info("Open Opportunity detail page");
			ViewOpportunityPage viewOpportunityPage = dashboard.openViewOpportunity();
			viewOpportunityPage.searchForOpportunity(oppty);
			OpportunityDetailPage opportunityDetailPage = viewOpportunityPage.selectResult(oppty);
			ContactSubpanel contactSubpanel = opportunityDetailPage.openContactsSubpanel();
			
			log.info("Creating Contact 1 on Opportunity Subpanel");
			contactSubpanel.openCreateContactForm();
			contactSubpanel.scrollToBottomOfPage();
			contactSubpanel.enterContactInfo(contact1, client);
			contactSubpanel.saveContact();
			
			log.info("Confirm that contact has been created");
			ViewContactPage viewContactPage = dashboard.openViewContact();
			viewContactPage.searchForContact(contact1);
			if (viewContactPage.isPrivacyPopUpPresent()){
				viewContactPage.noPrivacyPopUp();
			}
			Assert.assertEquals(viewContactPage.checkResult(contact1), true);
			
			log.info("Opening client detail page");
			ViewClientPage viewClientPage = dashboard.openViewClient();
			viewClientPage.searchForClient(client);
			
			
			ClientDetailPage clientDetailPage = viewClientPage.selectResult(client);
			clientDetailPage.openContactsSubpanel();
			
			log.info("Creating contact 2 on client subpanel");
			contactSubpanel.openCreateContactForm();
			contactSubpanel.enterContactInfo(contact2, client);
			contactSubpanel.saveContact();
			
			log.info("Confirm that contact has been created");
			dashboard.openViewContact();
			viewContactPage.searchForContact(contact2);
			if (!viewContactPage.checkResult(contact2)){
				viewContactPage.searchForContact(contact2);
			}
			Assert.assertEquals(viewContactPage.checkResult(contact2), true);

			log.info("Remove the oppty created for this test");
			sugarAPI.deleteOpptySOAP(testConfig.getBrowserURL(), opptyID, user1.getEmail(), user1.getPassword());
			sugarAPI.deleteContactSOAP(testConfig.getBrowserURL(), contactID, user1.getEmail(), user1.getPassword());
			
		log.info("End of test method CreatContactFromClientSubpanel");
	}
}
