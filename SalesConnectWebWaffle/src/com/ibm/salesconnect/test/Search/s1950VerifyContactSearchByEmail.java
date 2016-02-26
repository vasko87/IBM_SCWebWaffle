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
import com.ibm.salesconnect.objects.Contact;

/**
 * @author evafarrell
 * @date June 05, 2013
 */

public class s1950VerifyContactSearchByEmail extends ProductBaseTest {

	Logger log = LoggerFactory.getLogger(s1950VerifyContactSearchByEmail.class);

	@Test(groups = {"Search"})
	public void Test_s1950VerifyContactSearchByEmail(){
		log.info("Start of test method s1950VerifyContactSearchByEmail");	
		
			User user1 = commonUserAllocator.getUser(this);
			SugarAPI sugarAPI = new SugarAPI();
			int rand = new Random().nextInt(100000);

			log.info("Getting client");
			PoolClient poolClient = commonClientAllocator.getGroupClient(GC.SC,this);
			String clientID = poolClient.getCCMS_ID();

			log.info("Creating contact");
			Contact contact = new Contact();
			String contactID = "22SC-" + rand;
			contact.sFirstName = "ContactFirst"+rand;
			contact.sLastName = "ContactLast"+rand;
			contact.sEmail0 = contactID+"@tst.ibm.com";
			sugarAPI.createContact(testConfig.getBrowserURL(), contactID, clientID, user1.getEmail(), user1.getPassword(),contact.sFirstName , contact.sLastName );

			log.info("Logging in");
			Dashboard dashboard = launchWithLogin(user1);
			
			log.info("Setting Search Criteria to Search for Contact by Email Address");
			contact.sSearchName = false;
			contact.sSearchAnyEmail = true;
			
			dashboard.isPageLoaded();
			
			log.info("Searching for created contact by Email Address");
			ViewContactPage viewContactPage = dashboard.openViewContact();
			viewContactPage.isPageLoaded();
			viewContactPage.searchForContact(contact);
			viewContactPage.isPageLoaded();
			
			//If PrivacyPopup appears, select No and close the dialog
			if (viewContactPage.isPrivacyPopUpPresent()){
				viewContactPage.noPrivacyPopUp();
			}
			
			log.info("Verify Correct Contact exists in Search Results");
			Assert.assertEquals(viewContactPage.checkResult(contact), true, "Incorrect contact displayed in Search Results");
			sugarAPI.deleteContactSOAP(testConfig.getBrowserURL(), contactID, user1.getEmail(), user1.getPassword());
			
		log.info("End of test method s1950VerifyContactSearchByEmail");
	}

}
