package com.ibm.salesconnect.test.BusinessCard;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.SugarAPI;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.partials.ContactSubpanel;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Opportunity.OpportunityDetailPage;
import com.ibm.salesconnect.model.standard.Opportunity.ViewOpportunityPage;
import com.ibm.salesconnect.objects.Client;
import com.ibm.salesconnect.objects.Contact;
import com.ibm.salesconnect.objects.Opportunity;

/**
 * @author evafarrell
 * @date Sept 26, 2013
 */
@Test(groups = { "BusinessCard"})
public class s31952_RemoveBusinessCardContactFromOppty extends ProductBaseTest {
	
	Logger log = LoggerFactory.getLogger(s31952_RemoveBusinessCardContactFromOppty.class);

	
	public void Test_s31952_RemoveBusinessCardContactFromOppty() {
		log.info("Start test method Test_s31952_RemoveBusinessCardContactFromOppty");
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
		
		Contact contact = new Contact();
				
		// Contact variables 
		contact.sClientName = client.sClientName;
		contact.sFirstName = "OpptySubPanelContact " + GC.sUniqueNum;
		contact.sLastName = "Last " + GC.sUniqueNum;
		contact.sPreferredName = "Pref " + GC.sUniqueNum;
		contact.sJobTitle = "Sales";
		contact.sMobile = "333-333-3333";
		contact.sOfficePhone = "444-444-4444";
		contact.sEmail0 = "email0"+GC.sUniqueNum+"@tst.ibm.com";
		
		//TESTCASE BEGIN
		log.info("Logging in");
		Dashboard dashboard = launchWithLogin(user1);
		
		log.info("Open Opportunity detail page");
		ViewOpportunityPage viewOpportunityPage = dashboard.openViewOpportunity();
		viewOpportunityPage.isPageLoaded();
		viewOpportunityPage.searchForOpportunity(oppty);
		OpportunityDetailPage opportunityDetailPage = viewOpportunityPage.selectResult(oppty);
		
		ContactSubpanel contactSubpanel = opportunityDetailPage.openContactsSubpanel();
		
		log.info("Creating Contact on Opportunity Subpanel");
		contactSubpanel.openCreateContactForm();
		contactSubpanel.isPageLoaded();
		contactSubpanel.enterContactInfo(contact, client);
		contactSubpanel.saveContact();

		log.info("Verify that Hovering over new Contact does not find a Business Card");
		contactSubpanel.verifyBusinessCardDoesNotExist(contact.sFirstName);
		
		log.info("Remove the oppty created for this test");
		sugarAPI.deleteOpptySOAP(testConfig.getBrowserURL(), opptyID, user1.getEmail(), user1.getPassword());
		sugarAPI.deleteContactSOAP(testConfig.getBrowserURL(), contactID, user1.getEmail(), user1.getPassword());
		log.info("End of test method Test_s31952_RemoveBusinessCardContactFromOppty");
	}

}
