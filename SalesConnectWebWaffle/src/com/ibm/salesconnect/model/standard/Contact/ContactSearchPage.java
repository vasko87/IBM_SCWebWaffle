package com.ibm.salesconnect.model.standard.Contact;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardViewPage;

public class ContactSearchPage extends StandardViewPage {
	Logger log = LoggerFactory.getLogger(ContactSearchPage.class);

	/**
	 * @param exec
	 */
	public ContactSearchPage(Executor exec) {
		super(exec);
		Assert.assertTrue(checkForElement(searchboxInput), "Contact Search Page page has not loaded within 60 seconds");	
	}
	public static String searchboxInput="//input[@placeholder='Search by first name, last name...']";
	public String result(String contactName){return "//td[@data-type='fullname']/span/div[contains(text(),'ContactFirst93290 ContactLast93290']/../../../input[@name='Contacts_select']";}
	public static String pageLoaded=searchboxInput;
	
	
	public void selectContact(String primaryContact){
		
		searchWithFilterContact(primaryContact);
		click("//input[@name='Contacts_select']");
	}

}
