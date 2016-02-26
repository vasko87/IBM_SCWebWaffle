package com.ibm.appium.model.Contact;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.appium.model.MobilePageFrame;
import com.ibm.appium.model.partials.ChooseLinkTypePage;

public class ContactDetailPage extends MobilePageFrame {
	Logger log = LoggerFactory.getLogger(ContactDetailPage.class);

	// XPath Selectors
	public static String pageLoaded = "//a[@class='title append-root']//span[@class='value']";
	public static String editButton = "//a[@class='icon icon-pencil fast-click-highlighted append-root']";
	public static String plusButton = "//i[@class='icon icon-ellipsis-v icon-md']";
	public static String createNewRelated = "//a[@class='create-relate-link append-root']";
	public static String relateExisting = "//a[@class='link-relate-link append-root']";
	
	
	public ContactDetailPage() {
		Assert.assertTrue(isPageLoaded(), "Contact Detail Page has not loaded");
	}

	/**
	 * Check if new page is loaded by finding element on that page before
	 * starting any actions. PageLoaded is the value of the loaded Contact.
	 */
	@Override
	public boolean isPageLoaded() {

		return waitForPageToLoad(pageLoaded);
	}
	
	/**
	 * Get Contact Full name text located on contact detail page. 
	 */
	public String getDisplayedName() {
		return getText(pageLoaded);
	}

	/**
	 * Open edit contact from contact detail page.
	 * 
	 * @return CreateContactPage
	 */
	public CreateContactPage editContact() {
		isTutorial();
		click(editButton);
		return new CreateContactPage();
	}
	
	/**
	 * Open create related item.
	 * 
	 * @return CreateContactPage
	 */
	public ChooseLinkTypePage openCreateRelatedRecord() {
		click(plusButton);
		click(createNewRelated);
		return new ChooseLinkTypePage();
	}

	/**
	 * Open link existing item.
	 * 
	 * @return CreateContactPage
	 */
	public ChooseLinkTypePage openLinkExistingRecord() {
		click(plusButton);
		click(createNewRelated);
		return new ChooseLinkTypePage();
	}
	
}
