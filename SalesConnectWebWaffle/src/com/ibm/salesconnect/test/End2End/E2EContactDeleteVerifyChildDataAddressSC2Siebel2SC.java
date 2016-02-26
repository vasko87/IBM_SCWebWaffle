
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
public class E2EContactDeleteVerifyChildDataAddressSC2Siebel2SC extends ProductBaseTest {
	/**
	 * Script Name : <b>E2EContactDeleteVerifyChildDataAddressSC2Siebel2SC</b> Generated : <b>17 June
	 * 2013 15:32:26</b> Original Host : WinNT Version 6.1 Build 7601 (S)
	 * 
	 * Create contacts with various status and properties:
	 * 1) Check to see if contact is created with only required information
	 * 2) Pull contact back up to check for same address
	 * 3) Re-enter contact information
	 * 
	 * @since 2013/06/20
	 * @author Tyler Clayton
	 * 
	 * 
	 */

	public void testMain() {
		Logger log = LoggerFactory.getLogger(E2EContactDeleteVerifyChildDataAddressSC2Siebel2SC.class);
		log.info("Start of test method E2EContactDeleteVerifyChildDataAddressSC2Siebel2SC");
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
			contact1.sFirstName = "E2E_AG" + GC.sUniqueNum;
			contact1.sLastName = "E2E_Delete_AG" + GC.sUniqueNum;
			contact1.sMobile = "555-111-1111";
			contact1.sCountry = "United States";
			contact1.sJobTitle = "E2E Tester";
			contact1.sAltFirstName = "E2E_Alt_First_AG" + GC.sUniqueNum;
			contact1.sAddress = "E2E_Address";
			contact1.sAltCountry = "United States";
			contact1.sAltAddress = "E2E_Alt_Address";
			contact1.sClientName = client.sClientName;
			contact1.sEmail0 = contact1.sLastName + "@SFAE2E.com";
			
			
						
			//TESTCASE BEGIN
			log.info("Logging in");
			Dashboard dashboard = launchWithLogin(e2eUser);
			
			log.info("Creating Contact" + contact1.sFirstName + " " + contact1.sLastName
					+ " with First Name, Last Name, Mobile Phone Number, Country, Alternate Country, " +
					"Job Title, Alternate First Name, Primary Address and Alternate Adress to verify on AG Siebel Instance");

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
			contact1.sFirstName = "E2E_AP" + GC.sUniqueNum;
			contact1.sLastName = "E2E_Delete_AP" + GC.sUniqueNum;
			contact1.sMobile = "555-111-1111";
			contact1.sCountry = "United States";
			contact1.sJobTitle = "E2E Tester";
			contact1.sAltFirstName = "E2E_Alt_First_AP" + GC.sUniqueNum;
			contact1.sAddress = "E2E_Address";
			contact1.sAltCountry = "United States";
			contact1.sAltAddress = "E2E_Alt_Address";
			contact1.sClientName = client.sClientName;
			contact1.sEmail0 = contact1.sLastName + "@SFAE2E.com";
			
						
			//TESTCASE BEGIN
			log.info("Logging in");
			Dashboard dashboard = launchWithLogin(e2eUser);
			
			log.info("Creating Contact" + contact1.sFirstName + " " + contact1.sLastName
					+ " with First Name, Last Name, Mobile Phone Number, Country, Alternate Country, " +
					"Job Title, Alternate First Name, Primary Address and Alternate Adress to verify on AP Siebel Instance");

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
			contact1.sFirstName = "E2E_EU" + GC.sUniqueNum;
			contact1.sLastName = "E2E_Delete_EU" + GC.sUniqueNum;
			contact1.sMobile = "555-111-1111";
			contact1.sCountry = "United States";
			contact1.sJobTitle = "E2E Tester";
			contact1.sAltFirstName = "E2E_Alt_First_EU" + GC.sUniqueNum;
			contact1.sAddress = "E2E_Address";
			contact1.sAltCountry = "United States";
			contact1.sAltAddress = "E2E_Alt_Address";
			contact1.sClientName = client.sClientName;
			contact1.sEmail0 = contact1.sLastName + "@SFAE2E.com";
			
						
			//TESTCASE BEGIN
			log.info("Logging in");
			Dashboard dashboard = launchWithLogin(e2eUser);
			
			log.info("Creating Contact" + contact1.sFirstName + " " + contact1.sLastName
					+ " with First Name, Last Name, Mobile Phone Number, Country, Alternate Country, " +
					"Job Title, Alternate First Name, Primary Address and Alternate Adress to verify on EU Siebel Instance");

			//Create contact1
			CreateContactPage createContactPage = dashboard.openCreateContact();
			createContactPage.enterContactInfo(contact1, client);
			createContactPage.saveContact();
			
			log.info("Contact " + contact1.sFirstName + " " + contact1.sLastName +" created and saved.");
			
		} catch (Exception e) {
			log.error(e.toString());
		}
		log.info("End of test method E2EContactDeleteVerifyChildDataAddressSC2Siebel2SC");
	}
	
}
