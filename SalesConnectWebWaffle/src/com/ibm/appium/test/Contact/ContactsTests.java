package com.ibm.appium.test.Contact;

/*	
 * Write test case structure in this class
 * Lay out steps for each calling on methods in other classes
 * eg. Class, Method, Login > Calls Login from create class. <return back to test class
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.appium.MobileBaseTest;
import com.ibm.appium.Objects.Call;
import com.ibm.appium.Objects.Contact;
import com.ibm.appium.Objects.Opportunity;
import com.ibm.appium.model.Dashboard;
import com.ibm.appium.model.Call.CallDetailPage;
import com.ibm.appium.model.Call.CreateCallPage;
import com.ibm.appium.model.Contact.ContactDetailPage;
import com.ibm.appium.model.Contact.ContactListPage;
import com.ibm.appium.model.Contact.CreateContactPage;
import com.ibm.appium.model.Menus.MainMenu;
import com.ibm.appium.model.Menus.QuickCreateMenu;
import com.ibm.appium.model.Opportunity.CreateOpportunityPage;
import com.ibm.appium.model.Opportunity.OpportunityDetailPage;
import com.ibm.appium.model.Recents.RecentsListPage;
import com.ibm.appium.model.partials.ChooseLinkTypePage;
import com.ibm.atmn.waffle.extensions.user.User;

public class ContactsTests extends MobileBaseTest {

	public static String showMore = "//span[@class='show-more']";

	public Logger log = LoggerFactory.getLogger(ContactsTests.class);
	public Contact contact;
	
	/**
	 * Create new contact using quick menu using minimum details in the form
	 */
	@Test(groups={"Mobile"})
	public void s2506ContactCreate_required() {
		
		log.info("Starting method s2506ContactCreate_required");		
		log.info("Creating test objects");
		User user = commonUserAllocator.getGroupUser("mobile");		
		
		try {
			contact = new Contact();
			contact.populate();
			
			log.info("Logging in");
			Dashboard dashBoard = launchWithLogin(user);
	
			log.info("Opening Quick Create menu");
			QuickCreateMenu qCM = dashBoard.openQuickCreateMenu();
	
			log.info("Creating minimal contact with Quick create menu");
			CreateContactPage createContactPage = qCM.openCreateContactPage();
			createContactPage.enterMinimumContactInfo(contact);
			ContactDetailPage contactDetailPage = createContactPage.saveContact();
	
			log.info("Verifying contact creation");
			
			Assert.assertEquals(contactDetailPage.getDisplayedName(),
					contact.getsFirstName() + " ("
							+ contact.getsPreferredName() + ") " + contact.getsLastName(),
					"Contact name does not match expected");
			log.info("Contact created.");
		}
		finally{
			commonUserAllocator.checkInAllGroupUsers("mobile");
			log.info("End method s2506ContactCreate_required");		
		}
	}

	/**
	 * Search for existing contact in My Items
	 */
	@Test(groups={"Search"},dependsOnMethods = { "s2506ContactCreate_required" })
	public void s2506ContactListViewSearchMyItems() {
		
		log.info("Starting method s2506ContactListViewSearchMyItems");		
		log.info("Creating test objects");
		User user = commonUserAllocator.getGroupUser("mobile");		
		
		try {
			log.info("Logging in");
			Dashboard dashBoard = launchWithLogin(user);
	
			log.info("Navigating to contacts list view");
			MainMenu mM = dashBoard.openMainMenu();
			ContactListPage contactListPage = mM.openContactListView();
			
			log.info("Searching for contact");
			ContactDetailPage contactDetailPage = contactListPage.searchForContact(contact);
	
			log.info("Verifying Contact contents");
			
			Assert.assertEquals(contactDetailPage.getDisplayedName(),
					contact.getsFirstName() + " ("
							+ contact.getsPreferredName() + ") " + contact.getsLastName(),
					"Contact name does not match expected");
			log.info("Contact found.");
		}
		finally{
			commonUserAllocator.checkInAllGroupUsers("mobile");
			log.info("End method s2506ContactListViewSearchMyItems");		
		}
	}
	
	/**
	 * Search for existing contact in My Items Dashboard (Disabled)
	 */
//	@Test(dependsOnMethods = { "s2506ContactCreate_required" })
//	public void s2506ContactDashboardSearch() {
//		log.info("Starting method s2506ContactDashboardSearch");		
//		log.info("Creating test objects");
//		User user = commonUserAllocator.getGroupUser("mobile");		
//		
//		try {
//			log.info("Logging in");
//			Dashboard dashBoard = launchWithLogin(user);
//			
//			log.info("Searching for contact");
//			ContactDetailPage contactDetailPage = dashBoard.searchForItem(contact);
//		
//			log.info("Verifying Contact contents");
//			click(showMore);
//			Assert.assertEquals(contactDetailPage.getDisplayedName(),
//					contact.getsFirstName() + " ("
//							+ contact.getsPreferredName() + ") " + contact.getsLastName(),
//					"Contact name does not match expected");
//			log.info("Contact found.");
//		}
//		finally{
//			commonUserAllocator.checkInAllGroupUsers("mobile");
//			log.info("End method s2506ContactDashboardSearch");		
//		}
//	}
	
	/**
	 * Edit existing contact
	 */

	@Test(groups={"Mobile"},dependsOnMethods = { "s2506ContactCreate_required" })
	public void s2627ContactEdit() {
		
		log.info("Starting method s2627ContactEdit");		
		log.info("Creating test objects");
		User user = commonUserAllocator.getGroupUser("mobile");		
		
		try {
			log.info("Logging in");
			Dashboard dashBoard = launchWithLogin(user);
	
			log.info("Navigating to contacts list view");
			MainMenu mM = dashBoard.openMainMenu();
			ContactListPage contactListPage = mM.openContactListView();
			
			log.info("Searching for contact");
			ContactDetailPage contactDetailPage = contactListPage.searchForContact(contact);
	
			log.info("Editing Contact");
			CreateContactPage createContactPage = contactDetailPage.editContact();
			createContactPage.updateContactInfo(contact);
			createContactPage.saveContact();
	
			log.info("Verifying Contact creation");
			
			Assert.assertEquals(
					contactDetailPage.getDisplayedName(),
					contact.getsFirstName() + " - Update" + " (" + contact.getsPreferredName()  + ") "
							+ contact.getsLastName() + " - Update",
					"Contact name does not match expected");
			log.info("Contact edited.");
			contact.updateDetails();
		}
		finally{
			commonUserAllocator.checkInAllGroupUsers("mobile");
			log.info("End method s2627ContactEdit");		
		}
	}


	
	/**
	 * Create related call for a contact
	 */
	@Test(groups={"Mobile"},dependsOnMethods = { "s2506ContactCreate_required" })
	public void s27253ContactCreateRelatedCall() {
		
		log.info("Starting method s27253ContactCreateRelatedCall");		
		
		log.info("Creating test objects");
		User user = commonUserAllocator.getGroupUser("mobile");		
		Call call = new Call();
		call.populate();
		
		try {			
			log.info("Logging in to the app");
			Dashboard dashBoard = launchWithLogin(user);
			
			log.info("Navigating to contacts list view");
			MainMenu mM = dashBoard.openMainMenu();
			ContactListPage contactListPage = mM.openContactListView();
			
			log.info("Searching for contact");
			ContactDetailPage contactDetailPage = contactListPage.searchForContact(contact);

			log.info("Relate new call");
			ChooseLinkTypePage chooseLinkTypePage = contactDetailPage.openCreateRelatedRecord();
			CreateCallPage createCallPage = chooseLinkTypePage.linkNewCall();
			createCallPage.enterCallInfo(call);
			//call = createCallPage.getRelatedCallDetails(call);
			CallDetailPage callDetailPage = createCallPage.saveCall();
	
			log.info("Verifying call creation");
			isTutorial();
			click(showMore);
			
			Assert.assertEquals(callDetailPage.getCallSubject(),
					call.getsSubject(), "Call subject does not match expected");
			
		}
		finally{
			commonUserAllocator.checkInAllGroupUsers("mobile");
			log.info("End method s27253ContactCreateRelatedCall");		
		}
	}
	
	/**
	 * Create related call for a contact
	 */
	@Test(groups={"Mobile"},dependsOnMethods = { "s2506ContactCreate_required" })
	public void s27253ContactCreateRelatedOpportunity() {
		
		log.info("Starting method s27253ContactCreateRelatedOpportunity");		
		
		log.info("Creating test objects");
		User user = commonUserAllocator.getGroupUser("mobile");		
		Opportunity oppty = new Opportunity();
		oppty.populate();
		
		try {			
			log.info("Logging in to the app");
			Dashboard dashBoard = launchWithLogin(user);
			
			log.info("Navigating to contacts list view");
			MainMenu mM = dashBoard.openMainMenu();
			ContactListPage contactListPage = mM.openContactListView();
			
			log.info("Searching for contact");
			ContactDetailPage contactDetailPage = contactListPage.searchForContact(contact);

			log.info("Relate new opportunity");
			ChooseLinkTypePage chooseLinkTypePage = contactDetailPage.openCreateRelatedRecord();
			CreateOpportunityPage createOpportunityPage = chooseLinkTypePage.linkNewOpportunity();
			
			createOpportunityPage.enterOpportunityInfo(oppty);
			OpportunityDetailPage opportunityDetailPage = createOpportunityPage
					.saveOpportunity();
	
			log.info("Verifying opportunity creation");
			Assert.assertEquals(opportunityDetailPage.getOpportunityDescription(),
					oppty.getsDescription(),
					"Opportunty description does not match expected");
			
		}
		finally{
			commonUserAllocator.checkInAllGroupUsers("mobile");
			log.info("End method s27253ContactCreateRelatedOpportunity");		
		}
	}
	
	/**
	 * Create new contact using quick menu using FULL details in the form
	 */
	@Test
	public void s2506ContactCreate_full() {
		
		log.info("Starting method s2506ContactCreate_full");		
		log.info("Creating test objects");
		User user = commonUserAllocator.getGroupUser("mobile");		
		
		try {
			contact = new Contact();
			contact.populate();
	
			log.info("Logging in");
			Dashboard dashBoard = launchWithLogin(user);
	
			log.info("Opening Quick Create menu");
			QuickCreateMenu qCM = dashBoard.openQuickCreateMenu();
	
			log.info("Creating contact - " + contact);
			CreateContactPage createContactPage = qCM.openCreateContactPage();
			createContactPage.enterFullContactInfo(contact);
			ContactDetailPage contactDetailPage = createContactPage.saveContact();
	
			log.info("Verifying contact creation");
			
			Assert.assertEquals(
					contactDetailPage.getDisplayedName(),
					contact.getsFirstName() + " " + "("
							+ contact.getsPreferredName() + ")" + " "
							+ contact.getsLastName(),
					"Contact name does not match expected");
			
		}
		finally{
			commonUserAllocator.checkInAllGroupUsers("mobile");
			log.info("End method s2506ContactCreate_full");		
		}
	}

	/**
	 * Edit existing contact
	 */
	@Test(dependsOnMethods = { "s2506ContactCreate_full" })
	public void s2627FullContactEdit() {
		
		log.info("Starting method s2627FullContactEdit");		
		log.info("Creating test objects");
		User user = commonUserAllocator.getGroupUser("mobile");		
		
		try {
			log.info("Logging in");
			Dashboard dashBoard = launchWithLogin(user);
	
			log.info("Navigating to contacts list view");
			MainMenu mM = dashBoard.openMainMenu();
			RecentsListPage recentsListPage = mM.openRecentsListView();
			
			log.info("Opening previously created contact");
			ContactDetailPage contactDetailPage = recentsListPage.selectContact(contact);
	
			log.info("Editing Contact");
			CreateContactPage createContactPage = contactDetailPage.editContact();
			createContactPage.updateContactInfo(contact);
			createContactPage.saveContact();
	
			log.info("Verifying Contact creation");
			
			Assert.assertEquals(
					contactDetailPage.getDisplayedName(),
					contact.getsFirstName() + " - Update" + " ("
							+ contact.getsPreferredName() + ") "
							+ contact.getsLastName() + " - Update",
					"Contact name does not match expected");
		}
		finally{
			commonUserAllocator.checkInAllGroupUsers("mobile");
			log.info("End method s2627FullContactEdit");		
		}
	}
	
}
