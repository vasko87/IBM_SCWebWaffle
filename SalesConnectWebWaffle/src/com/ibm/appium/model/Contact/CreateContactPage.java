package com.ibm.appium.model.Contact;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.appium.Objects.Contact;
import com.ibm.appium.model.MobilePageFrame;
import com.ibm.appium.model.Contact.AddressSubpage;
import com.ibm.appium.model.partials.ClientSelectPage;

public class CreateContactPage extends MobilePageFrame {
	Logger log = LoggerFactory.getLogger(CreateContactPage.class);

	public CreateContactPage() {		
		Assert.assertTrue(isPageLoaded(), "Create Contact Page has not loaded");
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
	public static String pageLoaded = "//label[contains(text(),'First')]/..//input";
	public static String contactSalutationField = "//label[contains(text(),'Salutation')]/..//input";
	public static String contactFirstNameField = "//label[contains(text(),'First')]/..//input";
	public static String contactLastNameField = "//label[contains(text(),'Last')]/..//input";
	public static String contactPreferredNameField = "//label[contains(text(),'Preferred')]/..//input";
	public static String contactJobTitleField = "//label[contains(text(),'Job title')]/..//input";
	public static String contactClientNameSelector = "//label[contains(text(), 'Client')]";
	public static String contactPrimaryAddressField = "//label[contains(text(),'Primary Address')]";
	public static String contactOfficePhoneField = "//label[contains(text(),'Office')]/..//input";
	public static String contactMobileField = "//label[contains(text(),'Mobile')]/..//input";
	public static String contactEmailAddressField = "//label[contains(text(),'Email Address')]/..//input";
	public static String contactKeyContactCheckbox = "//label[contains(test(), 'Key contact')]/..//input";

	public static String clearContactFirstNameField = "//label[contains(text(),'First name')]/..i";
	public static String clearContactLastNameField = "//label[contains(text(),'Last name')]/..i";

	public static String saveContactButton = "//span[@class='saveBtn btn-area-more']";
	public static String cancleContactButton = "//span[@class='cancelBtn btn-area-more']";

	/**
	 * Populate contact form using only required fields.
	 */
	public void enterMinimumContactInfo(Contact contact) {
		if (contact.getsFirstName().length() > 0) {
			log.info("Adding Firstname " + contact.getsFirstName());
			type(contactFirstNameField, contact.getsFirstName());
		}

		if (contact.getsLastName().length() > 0) {
			log.info("Adding Last name " + contact.getsLastName());
			type(contactLastNameField, contact.getsLastName());
		}

		if (contact.getsPreferredName().length() > 0) {
			log.info("Adding Preferred name " + contact.getsPreferredName());
			type(contactPreferredNameField, contact.getsPreferredName());
		}
		
		if (contact.getsPrimaryAddress().length() == 0) {
			log.info("Navigate to Primary address subpage and fill in mandatory fields");
			AddressSubpage addressSubpage = openSelectAddress();
			addressSubpage.enterMinimalAddressInfo(contact);
			addressSubpage.saveAddress();
		}
		
		if (contact.getsOfficePhone().length() > 0) {
			log.info("Adding Office phone number " + contact.getsOfficePhone());
			type(contactOfficePhoneField, contact.getsOfficePhone());
		}

		if (contact.getsMobile().length() > 0) {
			log.info("Adding Mobile phone number " + contact.getsMobile());
			type(contactMobileField, contact.getsMobile());
		}
		return;
	}

	/**
	 * Populate contact form by filling all the details (full form).
	 */
	public void enterFullContactInfo(Contact contact) {
		if (contact.getsSalutation().length() > 0) {
			log.info("Adding Salutation " + contact.getsSalutation());
			type(contactSalutationField, contact.getsSalutation());
		}

		if (contact.getsFirstName().length() > 0) {
			log.info("Adding Firstname " + contact.getsFirstName());
			type(contactFirstNameField, contact.getsFirstName());
		}

		if (contact.getsLastName().length() > 0) {
			log.info("Adding Last name " + contact.getsLastName());
			type(contactLastNameField, contact.getsLastName());
		}

		if (contact.getsPreferredName().length() > 0) {
			log.info("Adding Prefered name " + contact.getsPreferredName());
			type(contactPreferredNameField, contact.getsPreferredName());
		}

		log.info("Adding contact to a Client ");
		ClientSelectPage clientSelectPage = openSelectClient();
		clientSelectPage.selectContactClient(contact);

		if (AddressSubpage.addressCountryField == null) {
			log.info("Navigate to Primary address subpage and fill in mandatory fields");
			AddressSubpage addressSubpage = openSelectAddress();
			addressSubpage.enterAddressInfo(contact);
			addressSubpage.saveAddress();
		}

		if (contact.getsOfficePhone().length() > 0) {
			log.info("Adding Office phone number " + contact.getsOfficePhone());
			type(contactOfficePhoneField, contact.getsOfficePhone());
		}

		if (contact.getsMobile().length() > 0) {
			log.info("Adding Mobile phone number " + contact.getsMobile());
			type(contactMobileField, contact.getsMobile());
		}

		if (contact.getsEmailAddress().length() > 0) {
			log.info("Adding Email Address " + contact.getsEmailAddress());
			type(contactEmailAddressField, contact.getsEmailAddress());
		}

		if (contact.getsJobTitle().length() > 0) {
			log.info("Adding Job title " + contact.getsJobTitle());
			type(contactJobTitleField, contact.getsJobTitle());
		}
		return;
	}

	/**
	 * Open Address primary submenu
	 * 
	 * @return AddressSubpage
	 */
	public AddressSubpage openSelectAddress() {
		click(contactPrimaryAddressField);
		return new AddressSubpage();
	}

	/**
	 * Open Client search submenu
	 * 
	 * @return ClientSelectPage
	 */
	public ClientSelectPage openSelectClient() {
		click(contactClientNameSelector);
		return new ClientSelectPage();
	}

	/**
	 * Save contact form and navigate to Contact detail page.
	 * 
	 * @return ContactDetailPage
	 */
	public ContactDetailPage saveContact() {
		log.info("Saving new contact");
		click(saveContactButton);
		return new ContactDetailPage();
	}

	/**
	 * Update existing Contact.
	 */
	public void updateContactInfo(Contact contact) {
		if (contact.getsFirstNameUPD().length() > 0) {
			type(contactFirstNameField, contact.getsFirstNameUPD());
			type(contactLastNameField, contact.getsLastNameUPD());
		}
		return;
	}
}
