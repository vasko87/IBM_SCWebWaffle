package com.ibm.appium.model.Menus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.appium.model.MobilePageFrame;
import com.ibm.appium.model.Call.CreateCallPage;
import com.ibm.appium.model.Contact.CreateContactPage;
import com.ibm.appium.model.Note.CreateNotePage;
import com.ibm.appium.model.Opportunity.CreateOpportunityPage;
import com.ibm.appium.model.Task.CreateTaskPage;

public class QuickCreateMenu extends MobilePageFrame {
	Logger log = LoggerFactory.getLogger(QuickCreateMenu.class);

	public QuickCreateMenu() {
		Assert.assertTrue(isPageLoaded(), "Quick create menu has not loaded");
	}

	/**
	 * Check if new page is loaded by finding element on that page before
	 * starting any actions.
	 */
	@Override
	public boolean isPageLoaded() {
		return waitForPageToLoad(pageLoaded,false);
	}

	// XPath Selectors
	public static String pageLoaded = "//div[@class='right-menu']";

	public static String createContactButton = "//a[@href='#Search/Contacts/create']";
	public static String createOpptyButton = "//a[@href='#Search/Opportunities/create']";
	public static String createCallButton = "//a[@href='#Search/Calls/create']";
	public static String createTaskButton = "//a[@href='#Search/Tasks/create']";
	public static String createNoteButton = "//a[@href='#Search/Notes/create']";

	/**
	 * Initiate create Contact form from quick create menu.
	 * 
	 * @return CreateContactPage
	 */
	public CreateContactPage openCreateContactPage() {
		click(createContactButton);
		return new CreateContactPage();
	}

	/**
	 * Initiate create Opportunity form from quick create menu.
	 * 
	 * @return CreateOpportunityPage
	 */
	public CreateOpportunityPage openCreateOpportunityPage() {
		click(createOpptyButton);
		return new CreateOpportunityPage();
	}

	/**
	 * Initiate create Call form from quick create menu.
	 * 
	 * @return CreateCallPage
	 */
	public CreateCallPage openCreateCallPage() {
		click(createCallButton);
		return new CreateCallPage();
	}

	/**
	 * Initiate create Task form from quick create menu.
	 * 
	 * @return CreateTaskPage
	 */
	public CreateTaskPage openCreateTaskPage() {
		click(createTaskButton);
		return new CreateTaskPage();
	}

	/**
	 * Initiate create Note form from quick create menu.
	 * 
	 * @return CreateNotePage
	 */
	public CreateNotePage openCreateNotePage() {
		click(createNoteButton);
		return new CreateNotePage();
	}
}
