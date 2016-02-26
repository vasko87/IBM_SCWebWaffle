package com.ibm.salesconnect.test.Level0;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.ContactRestAPI;
import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Contact.ContactDetailPage;
import com.ibm.salesconnect.model.standard.Contact.CreateContactPage;
import com.ibm.salesconnect.model.standard.Contact.ViewContactPage;
import com.ibm.salesconnect.objects.Client;
import com.ibm.salesconnect.objects.Contact;

/**
 * <strong>Description:</strong>
 * <br/><br/>
 * Test to validate the create, read update and delete functionality of the Contacts module(Specifically an eu contact to test the federation)
 * <br/><br/>
 * 
 * @author 
 * Tim Lehane
 * 
 */
public class AT_EUContact extends ProductBaseTest {
	Logger log = LoggerFactory.getLogger(AT_EUContact.class);
	

	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Open browser and log in</li>
	 * <li>Create Contact</li>
	 * <li>Search for created Contact</li>
	 * <li>Update the contact email</li>
	 * <li>Delete the contact</li>
	 * <li>Confirm that the contact was deleted via the API</li>
	 * </ol>
	 */
	@Test(groups = { "Level1","AT_Sugar","BVT","BVT0"})
	public void Test_AT_EUContact(){
		log.info("Start of test method Test_AT_EUContact");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);

		Contact contact = new Contact();

		PoolClient poolClient = commonClientAllocator.getGroupClient("SC");

		Client client = new Client();
		client.sClientName = poolClient.getClientName(testConfig.getBrowserURL(), user1);
		client.sSiteID  = poolClient.getCCMS_ID();
		client.sSearchIn= GC.showingInSiteID;
		client.sSearchFor= GC.searchForAll;
		client.sSearchShowing=GC.showingForAll;

		contact.populate();

		contact.sAddress = "15 Beir Way";
		contact.sCity = "Berlin";
		contact.sPostalCode = "11111";
		contact.sCountry = "Germany";
		contact.sState = "Berlin";
		//Mailing Address
		contact.sAltAddress = "";
		contact.sAltCity = "";
		contact.sAltPostalCode = "";
		contact.sAltCountry = "";
		contact.sAltState = "";

		log.info("Logging in");
		Dashboard dashboard = launchWithLogin(user1);
		
		log.info("Creating Contact");
		CreateContactPage createContactPage = dashboard.openCreateContact();
		createContactPage.enterContactInfo(contact, client);
		createContactPage.saveContact();

		log.info("Searching for created contact");
		ViewContactPage viewContactPage = dashboard.openViewContact();
		viewContactPage.searchForContact(contact);

		ContactDetailPage contactDetailPage = viewContactPage.selectResult(contact);

		Assert.assertEquals(contactDetailPage.getdisplayedContactName(),contact.sFirstName + " (" + contact.sPreferredName + ") " + contact.sLastName, "Incorrect contact detail page displayed");
		String contactID = contactDetailPage.getContactID();
		
		log.info("Editing contact");
		CreateContactPage editContactPage = contactDetailPage.openEditPage();
		contact.setContactEmail(contact.sEmail0 + "Edited");
		editContactPage.editEmailName(contact.sEmail0);
		editContactPage.saveContact();
		
		log.info("Searching for edited Contact");
		ViewContactPage viewEditedContactPage = dashboard.openViewContact();
		viewEditedContactPage.searchForContact(contact);
		ContactDetailPage contactEditDetailPage = viewEditedContactPage.selectResult(contact);
		
		Assert.assertEquals(contactDetailPage.getdisplayedContactName(),contact.sFirstName + " (" + contact.sPreferredName + ") " + contact.sLastName, "Incorrect contact detail page displayed");
		
		
		contactEditDetailPage.deleteContact();
	
		log.info("Confirming contact has been deleted via API");
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword());
		
		ContactRestAPI contactRestAPI = new ContactRestAPI();
		contactRestAPI.getContact(testConfig.getBrowserURL(), token, contactID, "404");

		log.info("End of test method Test_AT_Contact");
	}
}
