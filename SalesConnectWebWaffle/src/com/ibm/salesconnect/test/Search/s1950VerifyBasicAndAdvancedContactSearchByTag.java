package com.ibm.salesconnect.test.Search;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.DB2Connection;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Contact.ContactDetailPage;
import com.ibm.salesconnect.model.standard.Contact.CreateContactPage;
import com.ibm.salesconnect.model.standard.Contact.ViewContactPage;
import com.ibm.salesconnect.objects.Client;
import com.ibm.salesconnect.objects.Contact;


public class s1950VerifyBasicAndAdvancedContactSearchByTag extends ProductBaseTest {


	@Test(groups = {"Search"})
	public void Test_s1950VerifyBasicAndAdvancedContactSearchByTag() throws SQLException {
		Logger log = LoggerFactory.getLogger(s1950VerifyBasicAndAdvancedContactSearchByTag.class);
		log.info("Start of test method s1950VerifyBasisAndAdvancedContactSearchByTag");	
		log.info("Logging in as Business Admin");

		log.info("Start of test method Test_AT_Contact");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
		User db2User = commonUserAllocator.getGroupUser(GC.db2UserGroup,this);

		Contact contact = new Contact();
		PoolClient poolClient = commonClientAllocator.getGroupClient("SC");
		DB2Connection db2 = new DB2Connection(getParameter(GC.db2URL),db2User.getUid(),db2User.getPassword());
		Client client = db2.retrieveClient(poolClient, user1.getEmail(),testConfig.getParameter(GC.testPhase));
		client.sClientName = poolClient.getClientName(testConfig.getBrowserURL(), user1);
		client.sSearchIn= GC.showingInSiteID;
		client.sSearchFor= GC.searchForAll;
		client.sSearchShowing=GC.showingForAll;

		contact.populate();

		log.info("Logging in");
		Dashboard dashboard = launchWithLogin(user1);

		log.info("Creating Contact");
		CreateContactPage createContactPage = dashboard.openCreateContact();
		createContactPage.enterContactInfo(contact, client);
		createContactPage.saveContact();

		ViewContactPage viewContactPage = dashboard.openViewContact();


		for(int i=0;i<2;i++)
		{

			if(viewContactPage.isBasicSearch())
			{
				viewContactPage.searchForContact(contact);
				ContactDetailPage contactDetailPage = viewContactPage.selectResult(contact);
				contactDetailPage.isPageLoaded();
				log.info("Searching for created contact using basic search");
				Assert.assertEquals(contactDetailPage.getdisplayedContactName(),contact.sFirstName + " (" + contact.sPreferredName + ") " + contact.sLastName, "Incorrect contact detail page displayed");
				log.info("changing basic to advanced type for next iteration");
				viewContactPage = dashboard.openViewContact();
				viewContactPage.setSearchTypeToAdvanced();
			}
			else if(viewContactPage.isAdvancedSearch())
			{

				viewContactPage.searchForContact(contact);
				ContactDetailPage contactDetailPage = viewContactPage.selectResult(contact);
				contactDetailPage.isPageLoaded();
				log.info("Searching for created contact using advanced search");
				Assert.assertEquals(contactDetailPage.getdisplayedContactName(),contact.sFirstName + " (" + contact.sPreferredName + ") " + contact.sLastName, "Incorrect contact detail page displayed");
				log.info("changing advanced to basic type for next iteration");
				viewContactPage = dashboard.openViewContact();
				viewContactPage.setSearchTypeToBasic();
			}
		}
	}


}



