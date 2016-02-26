package com.ibm.appium.model.Opportunity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.appium.Objects.Opportunity;
//import com.ibm.appium.common.GC;
import com.ibm.appium.model.MobilePageFrame;
import com.ibm.appium.model.partials.ClientSelectPage;
import com.ibm.appium.model.partials.ContactSelectPage;

public class CreateOpportunityPage extends MobilePageFrame {
	Logger log = LoggerFactory.getLogger(CreateOpportunityPage.class);

	public CreateOpportunityPage() {
		Assert.assertTrue(isPageLoaded(), "Create Opportunity Page has not loaded");
	}

	/**
	 * Check if new page is loaded by finding element on that page before
	 * starting any actions.
	 */
	@Override
	public boolean isPageLoaded() {
		return waitForPageToLoad(pageLoaded);
	}

	// XPath Selectors
	public static String pageLoaded = "//label[contains(text(),'Description')]/../descendant::input";
	public static String opptyDescription = "//label[contains(text(),'Description')]/../descendant::input";
	public static String opptyDescriptionClear = "//label[contains(text(),'Description')]/../descendant::i";
	public static String opptyClientName = "//label[contains(text(),'Client name')]";
	public static String opptyPrimaryContact = "//label[contains(text(),'Primary')]";
	public static String saveOpportunityButton = "//span[@class='saveBtn btn-area-more']";
	public static String opptyTagInput = "//label[contains(text(),'Tags')]/../descendant::input";
	public static String opptyCodeInput = "//label[contains(text(),'Opportunity codes')]/../descendant::input";
	/**
	 * Main method to populate Opportunity form.
	 */
	public void enterOpportunityInfo(Opportunity oppty) {
		// Adding the Opportunity Description
		if (oppty.getsDescription().length() > 0) {
			type(opptyDescription, oppty.getsDescription());
		}

		// Selecting the Opportunity Client
		log.info("Navigate to Client name and select existing client");
		ClientSelectPage clientSelectPage = openSelectClient();
		clientSelectPage.filterItems(ClientSelectPage.filterMyClients);
		clientSelectPage.selectFirstClient(oppty);

		// Adding a Contact to the Opportunity
		log.info("Navigate to Primary Contact and select existing contact");
		ContactSelectPage contactSelectPage = openSelectContact();
		contactSelectPage.filterItems(ContactSelectPage.filterMyContacts);
		contactSelectPage.selectFirstContact(oppty);

		// Adding a tag to the opportunity. 3 Options available.
		log.info("Adding a Tag to the Opportunity");
		type(opptyTagInput, oppty.getsOpportunityTag1());

		return;
	}

	/**
	 * Update existing Opportunity.
	 */
	public void updateOpportunityInfo(Opportunity oppty) {
		if (oppty.getsDescriptionUPD().length() > 0) {
			type(opptyDescription, oppty.getsDescriptionUPD());
		}
		return;
	}

	/**
	 * Open Client select submenu
	 * 
	 * @return ClientSelectPage
	 */
	public ClientSelectPage openSelectClient() {
		click(opptyClientName);
		return new ClientSelectPage();
	}

	/**
	 * Open Contact select submenu
	 * 
	 * @return ContactSelectPage
	 */
	public ContactSelectPage openSelectContact() {
		click(opptyPrimaryContact);
		return new ContactSelectPage();
	}

	/**
	 * Save opportunity form and navigate to Opportunity detail page.
	 * 
	 * @return OpportunityDetailPage
	 */
	public OpportunityDetailPage saveOpportunity() {
		click(saveOpportunityButton);
		return new OpportunityDetailPage();
	}

}
