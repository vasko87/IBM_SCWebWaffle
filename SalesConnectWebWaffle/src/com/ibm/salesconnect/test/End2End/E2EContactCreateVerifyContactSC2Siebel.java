
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
public class E2EContactCreateVerifyContactSC2Siebel extends ProductBaseTest {
	/**
	 * Script Name : <b>E2EContactCreateVerifyContactSC2Siebel</b> Generated : <b>25 Jul
	 * 2011 15:32:26</b> Original Host : WinNT Version 6.1 Build 7601 (S)
	 * 
	 * Create contacts with various status and properties:
	 * 1) Create contact1 with required fields only
	 * 2) Create contact2 with all fields populated in full form
	 * 3) Create contact3 as inactive
	 * 4) Create contact4 with email suppression
	 * 
	 * @since 2013/06/19
	 * @author Tyler Clayton
	 */

	public void testMain() {
		Logger log = LoggerFactory.getLogger(E2EContactCreateVerifyContactSC2Siebel.class);
		log.info("Start of test method E2EContactCreateVerifyContactSC2Siebel");
		User db2User = commonUserAllocator.getGroupUser(GC.db2UserGroup,this);

		try {
			//Instantiate desired classes
			Contact contact1 = new Contact();
			Contact contact2 = new Contact();
			Contact contact3 = new Contact();
			Contact contact4 = new Contact();
		
			User e2eUser = commonUserAllocator.getGroupUser(GC.e2eUserGroupAG,this);
			PoolClient poolClient = commonClientAllocator.getGroupClient("E2EAG",this);

			DB2Connection db2 = new DB2Connection(getParameter(GC.db2URL),db2User.getUid(),db2User.getPassword());
			Client client = db2.retrieveClient(poolClient, e2eUser.getEmail(),testConfig.getParameter(GC.testPhase));
			System.out.println("Client returned from DB: " + client.sClientName);
			
			client.sSearchIn= GC.showingInSiteID;
			client.sSearchFor= GC.searchForAll;
			client.sSearchShowing=GC.showingForAll;

			//Define variables for contact with required and conditionally required fields only
			contact1.sFirstName = "E2E_AG" + GC.sUniqueNum;
			contact1.sLastName = "Required_AG" + GC.sUniqueNum;
			contact1.sPreferredName = "E2EPreferred"+ GC.sUniqueNum;
			contact1.sClientName = client.sClientName;
			contact1.sEmail0 = contact1.sLastName + "@SFAE2E.com";
			contact1.sContactStatus = "Active";		

			//Define variables contact2 with all  fields populated in full form
			contact2.sSalutation = "Mr";
			contact2.sFirstName = "E2E_AG" + GC.sUniqueNum;
			contact2.sLastName = "Full_AG" + GC.sUniqueNum;
			contact2.sPreferredName = "E2EPreferred"+ GC.sUniqueNum;
			contact2.sAltFirstName = "Alt "+contact2.sFirstName;
			contact2.sAltLastName = "Alt "+contact2.sLastName;
			contact2.sAltPreferredName = "Alt "+contact2.sPreferredName;
			contact2.sClientName = client.sClientName;
			contact2.sContactStatus = "Pending Marketing Verification";
			contact2.sJobTitle = "E2E Tester";
			contact2.sMobile = "555-111-1111";
			contact2.sOfficePhone = "666-222-2222";
			contact2.sEmail0 = contact2.sLastName + "@SFAE2E.com";
			
			//Addresses for contact2
			contact2.sAddress = "123 E2E Street";
			contact2.sCity = "EtwoE City";
			//contact2.sCountry = gsCountry[49];
			contact2.sCountry = "United States";
			contact2.sState = "Alabama";
			contact2.sAltAddress = "123 Alt E2E Street";
			contact2.sAltCity = "Alt EtwoE city";
			contact2.sAltCountry = "United States";
			contact2.sAltState = "Alaska";	
			
			//Define variables for inactive contact 
			contact3.sFirstName = "E2E_AG" + GC.sUniqueNum;
			contact3.sLastName = "Inactive_AG" + GC.sUniqueNum;
			contact3.sPreferredName = "E2EPreferred"+ GC.sUniqueNum;
			contact3.sClientName = client.sClientName;
			contact3.sContactStatus = "Inactive - General";
			contact3.sEmail0 = contact3.sLastName + "@SFAE2E.com";
			
			//Define variables for inactive contact 
			contact4.sFirstName = "E2E_AG" + GC.sUniqueNum;
			contact4.sLastName = "Supp_AG" + GC.sUniqueNum;
			contact4.sPreferredName = "E2EPreferred"+ GC.sUniqueNum;
			contact4.sClientName = client.sClientName;
			contact4.sEmail0 = contact4.sLastName + "@SFAE2E.com";
			contact4.bEmail0Suppress = true;
			
			//TESTCASE BEGIN
			log.info("Logging in");
			Dashboard dashboard = launchWithLogin(e2eUser);

			log.info("Creating E2E Contact 1 with required and conditionally required fields only for AG CRM");
			CreateContactPage createContactPage = dashboard.openCreateContact();
			createContactPage.enterContactInfo(contact1, client);
			createContactPage.saveContact();
			
			log.info("AG Contact 1 "+contact1.sFirstName+" "+contact1.sLastName+" Successfully Created");
			
			log.info("Creating E2E Contact 2 with all  fields populated in full form for AG CRM");
			createContactPage = dashboard.openCreateContact();
			createContactPage.enterContactInfo(contact2, client);
			createContactPage.saveContact();
			
			log.info("AG Contact 2 "+contact2.sFirstName+" "+contact2.sLastName+" Successfully Created");
			
			log.info("Creating E2E Contact 3, inactive contact for AG CRM");
			createContactPage = dashboard.openCreateContact();
			createContactPage.enterContactInfo(contact3, client);
			createContactPage.saveContact();
			
			log.info("AG Contact 3 "+contact3.sFirstName+" "+contact3.sLastName+" Successfully Created");
			
			log.info("Creating E2E Contact 4 with email suppression for AG CRM");
			createContactPage = dashboard.openCreateContact();
			createContactPage.enterContactInfo(contact4, client);
			createContactPage.saveContact();
			
			log.info("AG Contact 4 "+contact4.sFirstName+" "+contact4.sLastName+" Successfully Created");
						
		} catch (Exception e) {
			log.error(e.toString());
		}
		
		
		try {
			//Instantiate desired classes
			Contact contact1 = new Contact();
			Contact contact2 = new Contact();
			Contact contact3 = new Contact();
			Contact contact4 = new Contact();

			User e2eUser = commonUserAllocator.getGroupUser(GC.e2eUserGroupEU,this);
			PoolClient poolClient = commonClientAllocator.getGroupClient("E2EEU",this);

			DB2Connection db2 = new DB2Connection(getParameter(GC.db2URL),db2User.getUid(),db2User.getPassword());
			Client client = db2.retrieveClient(poolClient, e2eUser.getEmail(),testConfig.getParameter(GC.testPhase));
			System.out.println("Client returned from DB: " + client.sClientName);
			
			client.sSearchIn= GC.showingInSiteID;
			client.sSearchFor= GC.searchForAll;
			client.sSearchShowing=GC.showingForAll;
			
			//Define variables for contact with required and conditionally required fields only
			contact1.sFirstName = "E2E_EU" + GC.sUniqueNum;
			contact1.sLastName = "Required_EU" + GC.sUniqueNum;
			contact1.sPreferredName = "E2EPreferred"+ GC.sUniqueNum;
			contact1.sClientName = client.sClientName;
			contact1.sEmail0 = contact1.sLastName + "@SFAE2E.com";
			contact1.sContactStatus = "Active";		

			//Define variables contact2 with all  fields populated in full form
			contact2.sSalutation = "Mr";
			contact2.sFirstName = "E2E_EU" + GC.sUniqueNum;
			contact2.sLastName = "Full_EU" + GC.sUniqueNum;
			contact2.sPreferredName = "E2EPreferred"+ GC.sUniqueNum;
			contact2.sAltFirstName = "Alt "+contact2.sFirstName;
			contact2.sAltLastName = "Alt "+contact2.sLastName;
			contact2.sAltPreferredName = "Alt "+contact2.sPreferredName;
			contact2.sClientName = client.sClientName;
			contact2.sContactStatus = "Pending Marketing Verification";
			contact2.sJobTitle = "E2E Tester";
			contact2.sMobile = "555-111-1111";
			contact2.sOfficePhone = "666-222-2222";
			contact2.sEmail0 = contact2.sLastName + "@SFAE2E.com";
			
			//Addresses for contact2
			contact2.sAddress = "123 E2E Street";
			contact2.sCity = "EtwoE City";
			//contact2.sCountry = gsCountry[49];
			contact2.sCountry = "United States";
			contact2.sState = "Alabama";
			contact2.sAltAddress = "123 Alt E2E Street";
			contact2.sAltCity = "Alt EtwoE city";
			contact2.sAltCountry = "United States";
			contact2.sAltState = "Alaska";		
			
			//Define variables for inactive contact 
			contact3.sFirstName = "E2E_EU" + GC.sUniqueNum;
			contact3.sLastName = "Inactive_EU" + GC.sUniqueNum;
			contact3.sPreferredName = "E2EPreferred"+ GC.sUniqueNum;
			contact3.sClientName = client.sClientName;
			contact3.sContactStatus = "Inactive - General";
			contact3.sEmail0 = contact3.sLastName + "@SFAE2E.com";
			
			//Define variables for inactive contact 
			contact4.sFirstName = "E2E_EU" + GC.sUniqueNum;
			contact4.sLastName = "Supp_EU" + GC.sUniqueNum;
			contact4.sPreferredName = "E2EPreferred"+ GC.sUniqueNum;
			contact4.sClientName = client.sClientName;
			contact4.sEmail0 = contact4.sLastName + "@SFAE2E.com";
			contact4.bEmail0Suppress = true;
			
			//TESTCASE BEGIN
			log.info("Logging in");
			Dashboard dashboard = launchWithLogin(e2eUser);

			log.info("Creating E2E Contact 1 with required and conditionally required fields only for EU CRM");
			CreateContactPage createContactPage = dashboard.openCreateContact();
			createContactPage.enterContactInfo(contact1, client);
			createContactPage.saveContact();
			
			log.info("EU Contact 1 "+contact1.sFirstName+" "+contact1.sLastName+" Successfully Created");
			
			log.info("Creating E2E Contact 2 with all  fields populated in full form for EU CRM");
			createContactPage = dashboard.openCreateContact();
			createContactPage.enterContactInfo(contact2, client);
			createContactPage.saveContact();

			log.info("EU Contact 2 "+contact2.sFirstName+" "+contact2.sLastName+" Successfully Created");
			
			
			log.info("Creating E2E Contact 3, inactive contact for EU CRM");
			createContactPage = dashboard.openCreateContact();
			createContactPage.enterContactInfo(contact3, client);
			createContactPage.saveContact();
			
			log.info("EU Contact 3 "+contact3.sFirstName+" "+contact3.sLastName+" Successfully Created");
			
			log.info("Creating E2E Contact 4 with email suppression for EU CRM");
			createContactPage = dashboard.openCreateContact();
			createContactPage.enterContactInfo(contact4, client);
			createContactPage.saveContact();

			log.info("EU Contact 4 "+contact4.sFirstName+" "+contact4.sLastName+" Successfully Created");
			
		} catch (Exception e) {
			log.error(e.toString());
		}
		
		try {
			//Instantiate desired classes
			Contact contact1 = new Contact();
			Contact contact2 = new Contact();
			Contact contact3 = new Contact();
			Contact contact4 = new Contact();

			User e2eUser = commonUserAllocator.getGroupUser(GC.e2eUserGroupAP,this);
			PoolClient poolClient = commonClientAllocator.getGroupClient("E2EAP",this);

			DB2Connection db2 = new DB2Connection(getParameter(GC.db2URL),db2User.getUid(),db2User.getPassword());
			Client client = db2.retrieveClient(poolClient, e2eUser.getEmail(),testConfig.getParameter(GC.testPhase));
			System.out.println("Client returned from DB: " + client.sClientName);
					
			client.sSearchIn= GC.showingInSiteID;
			client.sSearchFor= GC.searchForAll;
			client.sSearchShowing=GC.showingForAll;
			
			//Define variables for contact with required and conditionally required fields only
			contact1.sFirstName = "E2E_AP" + GC.sUniqueNum;
			contact1.sLastName = "Required_AP" + GC.sUniqueNum;
			contact1.sPreferredName = "E2EPreferred"+ GC.sUniqueNum;
			contact1.sClientName = client.sClientName;
			contact1.sEmail0 = contact1.sLastName + "@SFAE2E.com";
			contact1.sContactStatus = "Active";	
			contact1.sCountry = "Brazil";

			//Define variables contact2 with all  fields populated in full form
			contact2.sSalutation = "Mr";
			contact2.sFirstName = "E2E_AP" + GC.sUniqueNum;
			contact2.sLastName = "Full_AP" + GC.sUniqueNum;
			contact2.sPreferredName = "E2EPreferred"+ GC.sUniqueNum;
			contact2.sAltFirstName = "Alt "+contact2.sFirstName;
			contact2.sAltLastName = "Alt "+contact2.sLastName;
			contact2.sAltPreferredName = "Alt "+contact2.sPreferredName;
			contact2.sClientName = client.sClientName;
			contact2.sContactStatus = "Pending Marketing Verification";
			contact2.sJobTitle = "E2E Tester";
			contact2.sMobile = "555-111-1111";
			contact2.sOfficePhone = "666-222-2222";
			contact2.sEmail0 = contact2.sLastName + "@SFAE2E.com";
			
			//Addresses for contact2
			contact2.sAddress = "123 E2E Street";
			contact2.sCity = "EtwoE City";
			//contact2.sCountry = gsCountry[49];
			contact2.sCountry = "United States";
			contact2.sState = "Alabama";
			contact2.sAltAddress = "123 Alt E2E Street";
			contact2.sAltCity = "Alt EtwoE city";
			contact2.sAltCountry = "United States";
			contact2.sAltState = "Alaska";		
			
			//Define variables for inactive contact 
			contact3.sFirstName = "E2E_AP" + GC.sUniqueNum;
			contact3.sLastName = "Inactive_AP" + GC.sUniqueNum;
			contact3.sPreferredName = "E2EPreferred"+ GC.sUniqueNum;
			contact3.sClientName = client.sClientName;
			contact3.sContactStatus = "Inactive - General";
			contact2.sCountry = "Brazil";
			contact3.sEmail0 = contact3.sLastName + "@SFAE2E.com";
			
			//Define variables for inactive contact 
			contact4.sFirstName = "E2E_AP" + GC.sUniqueNum;
			contact4.sLastName = "Supp_AP" + GC.sUniqueNum;
			contact4.sPreferredName = "E2EPreferred"+ GC.sUniqueNum;
			contact4.sClientName = client.sClientID;
			contact4.sEmail0 = contact4.sLastName + "@SFAE2E.com";
			contact2.sCountry = "Brazil";
			contact4.bEmail0Suppress = true;
			
			//TESTCASE BEGIN
			log.info("Logging in");
			Dashboard dashboard = launchWithLogin(e2eUser);

			log.info("Creating E2E Contact 1 with required and conditionally required fields only for AP CRM");
			CreateContactPage createContactPage = dashboard.openCreateContact();
			createContactPage.enterContactInfo(contact1, client);
			createContactPage.saveContact();

			log.info("AP Contact 1 "+contact1.sFirstName+" "+contact1.sLastName+" Successfully Created");
			
			log.info("Creating E2E Contact 2 with all  fields populated in full form for AP CRM");
    		createContactPage = dashboard.openCreateContact();
			createContactPage.enterContactInfo(contact2, client);
			createContactPage.saveContact();

			log.info("AP Contact 2 "+contact2.sFirstName+" "+contact2.sLastName+" Successfully Created");
			
			log.info("Creating E2E Contact 3, inactive contact for AP CRM");
			createContactPage = dashboard.openCreateContact();
			createContactPage.enterContactInfo(contact3, client);
			createContactPage.saveContact();

			log.info("AP Contact 3 "+contact3.sFirstName+" "+contact3.sLastName+" Successfully Created");
			
			log.info("Creating E2E Contact 4 with email suppression for AP CRM");
			createContactPage = dashboard.openCreateContact();
			createContactPage.enterContactInfo(contact4, client);
			createContactPage.saveContact();
			
			log.info("AP Contact 4 "+contact4.sFirstName+" "+contact4.sLastName+" Successfully Created");
			
		} catch (Exception e) {
			log.error(e.toString());
		}
		
		log.info("End of test method E2EContactCreateVerifyContactSC2Siebel");
	}
	
}
