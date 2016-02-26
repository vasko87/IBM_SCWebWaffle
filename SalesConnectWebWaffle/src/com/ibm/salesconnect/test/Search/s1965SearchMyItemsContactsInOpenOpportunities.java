package com.ibm.salesconnect.test.Search;

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
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Contact.ViewContactPage;
import com.ibm.salesconnect.model.standard.Opportunity.EditOpportunityPage;
import com.ibm.salesconnect.model.standard.Opportunity.OpportunityDetailPage;
import com.ibm.salesconnect.model.standard.Opportunity.ViewOpportunityPage;
import com.ibm.salesconnect.objects.Contact;
import com.ibm.salesconnect.objects.Opportunity;


public class s1965SearchMyItemsContactsInOpenOpportunities extends ProductBaseTest {

	@Test(groups = {"Search"})
	public void Test_s1965SearchMyItemsContactsInOpenOpportunities() {
		
		Logger log = LoggerFactory.getLogger(s1965SearchMyItemsContactsInOpenOpportunities.class);
		log.info("Start of test method s1965SearchMyItemsContacatsInOpenOpportunities");

			User user1 = commonUserAllocator.getUser(this);
			SugarAPI sugarAPI = new SugarAPI();
			int rand = new Random().nextInt(100000);

			log.info("Getting client");
			System.out.println("1");
			PoolClient poolClient = commonClientAllocator.getGroupClient(GC.SC,this);
			String clientID = poolClient.getCCMS_ID();
			System.out.println("2");
			log.info("Creating contact");
			Contact contact = new Contact();
			String contactID = "22SC-" + rand;
			contact.sFirstName = "ContactFirst"+rand;
			contact.sLastName = "ContactLast"+rand;
			sugarAPI.createContact(testConfig.getBrowserURL(), contactID, clientID, user1.getEmail(), user1.getPassword(),contact.sFirstName , contact.sLastName );
			System.out.println("3");
			log.info("Creating oppty");
			Opportunity oppty = new Opportunity();
			String opptyID = null;
			oppty.sOpptNumber = opptyID;
			oppty.sPrimaryContact = contact.sFirstName + " " + contact.sLastName;
			oppty.sAccID = clientID;
			opptyID = sugarAPI.createOppty(testConfig.getBrowserURL(), "", contactID, clientID, user1.getEmail(), user1.getPassword(), GC.emptyArray);
			System.out.println("4");
			log.info("Change contact search variable to My Contacts");
			contact.bMyItems = true;
			
			log.info("Logging in");
			Dashboard dashboard = launchWithLogin(user1);
			System.out.println("5");
			log.info("Search for contact with My Contacts");
			ViewContactPage viewContactPage = dashboard.openViewContact();
			viewContactPage.isPageLoaded();
			viewContactPage.isPageLoaded();
			viewContactPage.searchForContact(contact);
			viewContactPage.isPageLoaded();
			System.out.println("6");
			//If PrivacyPopup appears, select No and close the dialog
			if (viewContactPage.isPrivacyPopUpPresent()){
				viewContactPage.noPrivacyPopUp();
			}
			if(!viewContactPage.checkResult(contact)){
				viewContactPage.searchForContact(contact);
			}
			Assert.assertEquals(viewContactPage.checkResult(contact), true);
			
			log.info("Open Opportunity");
			ViewOpportunityPage viewOpportunityPage = dashboard.openViewOpportunity();
			viewOpportunityPage.isPageLoaded();
			viewOpportunityPage.searchForOpportunity(oppty);
			viewOpportunityPage.isPageLoaded();
			OpportunityDetailPage oppDetailPage = viewOpportunityPage.selectResult(oppty);
			System.out.println("7");
			log.info("Edit Opportunity with new variables");
			oppty.sSalesStage = GC.gsOppLostToCompetition;
			oppty.sReasonWonLost = GC.gsOppLSoleOnPrice;
			EditOpportunityPage editOpportunityPage = oppDetailPage.openEditOpportunity();
			editOpportunityPage.enterOpportunityInfo(oppty);
			editOpportunityPage.saveOpportunity();
			sleep(500);
			System.out.println("8");
			log.info("Now that opportunity is closed, ensure contact is still visible");
			viewContactPage = dashboard.openViewContact();
			viewContactPage.isPageLoaded();
			viewContactPage.isPageLoaded();
			viewContactPage.isPageLoaded();
			//sleep(10000);
			viewContactPage.searchForContact(contact);
			System.out.println("9");
			//If PrivacyPopup appears, select No and close the dialog
			if (viewContactPage.isPrivacyPopUpPresent()){
				viewContactPage.noPrivacyPopUp();
			}
			
			if(!viewContactPage.checkResult(contact)){
				viewContactPage.searchForContact(contact);
			}
			Assert.assertEquals(viewContactPage.checkResult(contact), true);
			System.out.println("10");
			log.info("Now that opportunity is closed, ensure search does not show it");
			oppty.bMyOpportunities = true;
			viewOpportunityPage = dashboard.openViewOpportunity();
			viewOpportunityPage.isPageLoaded();
			viewOpportunityPage.searchForOpportunity(oppty);
			viewOpportunityPage.isPageLoaded();
			Assert.assertEquals(viewOpportunityPage.checkResult(oppty), false);
			System.out.println("11");
			log.info("Remove the oppty created for this test");
			sugarAPI.deleteOpptySOAP(testConfig.getBrowserURL(), opptyID, user1.getEmail(), user1.getPassword());
			sugarAPI.deleteContactSOAP(testConfig.getBrowserURL(), contactID, user1.getEmail(), user1.getPassword());
			
			log.info("End of test method s1965SearchMyItemsContactsInOpenOpportunities");
	}
}
