
package com.ibm.salesconnect.test.End2End;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.DB2Connection;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Contact.CreateContactPage;
import com.ibm.salesconnect.objects.Client;
import com.ibm.salesconnect.objects.Contact;

@Test(groups = {"End2End"})
public class E2EContactUpdateVerifyContactWithSingleFieldUpdatedSC2Siebel extends ProductBaseTest {
	/**
	 * Script Name : <b>E2EContactUpdateVerifyContactWithSingleFieldUpdatedSC2Siebel</b> Generated : <b>20 June
	 * 2013 15:32:26</b> Original Host : WinNT Version 6.1 Build 7601 (S)
	 * 
	 * Create contacts with various status and properties:
	 * 1) Check to see if contact is created with only required information
	 * 2) Update individual fields by editing them one at a time
	 * 
	 * 
	 * @since 2013/06/20
	 * @author Tyler Clayton
	 */

	public void testMain() {
		Logger log = LoggerFactory.getLogger(E2EContactUpdateVerifyContactWithSingleFieldUpdatedSC2Siebel.class);
		log.info("Start of test method E2EContactUpdateVerifyContactWithSingleFieldUpdatedSC2Siebel");
		User db2User = commonUserAllocator.getGroupUser(GC.db2UserGroup,this);
		
		try {
			//Instantiate desired classes
			Contact contact1 = new Contact();
			User e2eUser = commonUserAllocator.getGroupUser(GC.e2eUserGroupAG,this);
			PoolClient poolClient = commonClientAllocator.getGroupClient("E2EAG",this);

			DB2Connection db2 = new DB2Connection(getParameter(GC.db2URL),db2User.getUid(),db2User.getPassword());
			Client client = db2.retrieveClient(poolClient, e2eUser.getEmail(),testConfig.getParameter(GC.testPhase));
			System.out.println("Client returned from DB: " + client.sClientName);
			
			client.sSearchIn= GC.showingInSiteID;
			client.sSearchFor= GC.searchForAll;
			client.sSearchShowing=GC.showingForAll;
			
			//Define variables contact1 with required fields
			contact1.sFirstName = "E2E_AG" + GC.sUniqueNum ;
			contact1.sLastName = "E2E_Update_AG" + GC.sUniqueNum;
			contact1.sEmail0 = contact1.sLastName + "@SFAE2E.com";
			contact1.sCountry = "United States";
			contact1.sClientName = client.sClientName;
						
			//TESTCASE BEGIN
			log.info("Logging in");
			Dashboard dashboard = launchWithLogin(e2eUser);
			
			log.info("Creating Contact " + contact1.sFirstName + " " + contact1.sLastName
					+ " with First Name, Last Name, Email and Country to verify on AG Siebel Instance");

			//Create contact1
			CreateContactPage createContactPage = dashboard.openCreateContact();
			createContactPage.enterContactInfo(contact1, client);
			createContactPage.saveContact();
			
			
			log.info("Contact " + contact1.sFirstName + " " + contact1.sLastName +" created and saved.");
			
			
			
		} catch (Exception e) {
			log.error(e.toString());
		}

		try {
			//Instantiate desired classes
			Contact contact1 = new Contact();
			User e2eUser = commonUserAllocator.getGroupUser(GC.e2eUserGroupAP,this);
			PoolClient poolClient = commonClientAllocator.getGroupClient("E2EAP",this);

			DB2Connection db2 = new DB2Connection(getParameter(GC.db2URL),db2User.getUid(),db2User.getPassword());
			Client client = db2.retrieveClient(poolClient, e2eUser.getEmail(),testConfig.getParameter(GC.testPhase));
			System.out.println("Client returned from DB: " + client.sClientName);
			
			client.sSearchIn= GC.showingInSiteID;
			client.sSearchFor= GC.searchForAll;
			client.sSearchShowing=GC.showingForAll;
			
			//Define variables contact1 with required fields
			contact1.sFirstName = "E2E_First_AP" + GC.sUniqueNum ;
			contact1.sLastName = "E2E_Last_AP" + GC.sUniqueNum;
			contact1.sEmail0 = contact1.sLastName + "@SFAE2E.com";
			contact1.sCountry = "United States";
			contact1.sClientName = client.sClientName;
						
			//TESTCASE BEGIN
			log.info("Logging in");
			Dashboard dashboard = launchWithLogin(e2eUser);
			
			log.info("Creating Contact " + contact1.sFirstName + " " + contact1.sLastName
					+ " with First Name, Last Name, Email and Country to verify on AP Siebel Instance");

			//Create contact1
			CreateContactPage createContactPage = dashboard.openCreateContact();
			createContactPage.enterContactInfo(contact1, client);
			createContactPage.saveContact();
			
			
			log.info("Contact " + contact1.sFirstName + " " + contact1.sLastName +" created and saved.");
			
			
			
		} catch (Exception e) {
			log.error(e.toString());
		}
		
		try {
			//Instantiate desired classes
			Contact contact1 = new Contact();
			User e2eUser = commonUserAllocator.getGroupUser(GC.e2eUserGroupEU,this);
			PoolClient poolClient = commonClientAllocator.getGroupClient("E2EEU",this);

			DB2Connection db2 = new DB2Connection(getParameter(GC.db2URL),db2User.getUid(),db2User.getPassword());
			Client client = db2.retrieveClient(poolClient, e2eUser.getEmail(),testConfig.getParameter(GC.testPhase));
			System.out.println("Client returned from DB: " + client.sClientName);
			
			client.sSearchIn= GC.showingInSiteID;
			client.sSearchFor= GC.searchForAll;
			client.sSearchShowing=GC.showingForAll;
						
			//Define variables contact1 with required fields
			contact1.sFirstName = "E2E_First_EU" + GC.sUniqueNum ;
			contact1.sLastName = "E2E_Last_EU" + GC.sUniqueNum;
			contact1.sEmail0 = contact1.sLastName + "@SFAE2E.com";
			contact1.sCountry = "United States";
			contact1.sClientName = client.sClientName;
						
			//TESTCASE BEGIN
			log.info("Logging in");
			Dashboard dashboard = launchWithLogin(e2eUser);
			
			log.info("Creating Contact " + contact1.sFirstName + " " + contact1.sLastName
					+ " with First Name, Last Name, Email and Countryto verify on EU Siebel Instance");

			//Create contact1
			CreateContactPage createContactPage = dashboard.openCreateContact();
			createContactPage.enterContactInfo(contact1, client);
			createContactPage.saveContact();
			
			
			log.info("Contact " + contact1.sFirstName + " " + contact1.sLastName +" created and saved.");
			
			
			
		} catch (Exception e) {
			log.error(e.toString());
		}
		log.info("End of test method E2EContactUpdateVerifyContactWithSingleFieldUpdatedSC2Siebel");
	}
	
}
