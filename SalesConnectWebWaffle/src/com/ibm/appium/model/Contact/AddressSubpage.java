package com.ibm.appium.model.Contact;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.appium.Objects.Contact;
import com.ibm.appium.model.MobilePageFrame;

public class AddressSubpage extends MobilePageFrame {
	Logger log = LoggerFactory.getLogger(AddressSubpage.class);

	public AddressSubpage() {
		Assert.assertTrue(isPageLoaded(), "Address Subpage has not loaded");
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
	public static String pageLoaded = "//label[contains(text(),'Country/Area')]/..//select";
	public static String addressCountryField = "//select[@name='primary_address_country']";
	public static String addressStreetField = "//label[contains(text(),'Street address')]/..//span";
	public static String addressTextArea = "//div[@class'textarea  layout-def']";
	public static String addressCityField = "//label[contains(text(),'City')]/..//input";
	public static String addressStateField = "//select[@name='primary_address_state']";
	public static String addressPostalCodeField = "//label[contains(text(),'Postal code')]/..//input";
	public static String saveAddressButton = "//a[contains(text(),'Done')]";
	public static String cancelAddressButton = "//span[@class='cancelBtn btn-area-more']";

	/**
	 * Populate Address subpage, accessible by clicking Primary Address on
	 * Contact form.
	 */
	public void enterAddressInfo(Contact contact) {
		if (contact.getsCountryArea().length() > 0) {
			log.info("Adding the conuntry " + contact.sCountryArea);
			select(addressCountryField, contact.sCountryArea);
		}
		if (contact.getsStreetAddress().length() > 0) {
			log.info("Adding the street " + contact.sStreetAddress);
			click(addressStreetField);
			type(addressTextArea, contact.sStreetAddress);
		}
		if (contact.getsCity().length() > 0) {
			log.info("Adding the city " + contact.sCity);
			type(addressCityField, contact.sCity);
		}
		if (contact.getsStateProvince().length() > 0) {
			log.info("Adding the state " + contact.sStateProvince);
			select(addressStateField, contact.sStateProvince);
		}
		if (contact.getsPostalCode().length() > 0) {
			log.info("Adding the post code " + contact.sPostalCode);
			type(addressPostalCodeField, contact.sPostalCode);
		}
		return;
	}

	public void enterMinimalAddressInfo(Contact contact) {
		if (contact.getsCountryArea().length() > 0) {
			log.info("Adding the conuntry " + contact.sCountryArea);
			select(addressCountryField, contact.sCountryArea);
		}
		if (contact.getsStateProvince().length() > 0) {
			log.info("Adding the state " + contact.sStateProvince);
			select(addressStateField, contact.sStateProvince);
		}
	}

	/**
	 * Save and exit Address subpage and navigate back to main Contact form.
	 */
	public void saveAddress() {
		log.info("Saving");
		click(saveAddressButton);
		return;
	}
}
