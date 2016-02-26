
package com.ibm.salesconnect.test.Miscellaneous;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.DB2Connection;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.partials.EditContactSubpanel;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Contact.ContactDetailPage;
import com.ibm.salesconnect.model.standard.Contact.CreateContactPage;
import com.ibm.salesconnect.model.standard.Contact.EditContactPage;
import com.ibm.salesconnect.model.standard.Contact.ViewContactPage;
import com.ibm.salesconnect.objects.Client;
import com.ibm.salesconnect.objects.Contact;


public class EditContactFromSubpanel extends ProductBaseTest {
	/**
	 * Script Name : <b>E2EEdiContactFromSubpanel</b> Generated : <b>17 June
	 * 2013 15:32:26</b> Original Host : WinNT Version 6.1 Build 7601 (S)
	 * 
	 * Create contacts with various status and properties:
	 * 1) Check to see if full edit copies all information
	 * 2) Check to see if subpanel edit copies information
	 * 3) Copy contact, see if it was sucessfully edited from the Subpanel
	 * 
	 * @since 2013/06/17
	 * @author Tyler Clayton
	 */
	@Test(groups = {"Miscellaneous","BVT2"})
	public void testMain() {
		Logger log = LoggerFactory.getLogger(EditContactFromSubpanel.class);
		log.info("Start of test method E2ECopyEditContact");
		User user1 = commonUserAllocator.getUser(this);
		User db2User = commonUserAllocator.getGroupUser(GC.db2UserGroup,this);

		try {
			//Instantiate desired classes
			Contact contact1 = new Contact();
			Contact contactEdit = new Contact();
			
			contact1.populate();
			contactEdit.populate();
			
			
			//String sClientName1;
			//String sClientID1;
			
			//PoolClient poolClient = commonClientAllocator.getGroupClient("testTyler",this);
			PoolClient poolClient = commonClientAllocator.getGroupClient("DC");
			
			DB2Connection db2 = new DB2Connection(getParameter(GC.db2URL),db2User.getUid(),db2User.getPassword());
			Client client = db2.retrieveClient(poolClient, user1.getEmail(),testConfig.getParameter(GC.testPhase));
			System.out.println("Client returned from DB: " + client.sClientName);
			
			client.sSearchIn= GC.showingInClientName;
			client.sSearchFor= GC.searchForAll;
			client.sSearchShowing=GC.showingForAll;

			
//			if (LoadSettings.gsSugarURL.contains("svt3") ){
//				sClientName1 = "Computacenter";
//				sClientID1 = "S003NCZTHA";
//				
//			}
//			else{
//				sClientName1 = "Allianz Plc";
//				sClientID1 = "S006AJ46RF";
//			}

			//Define variables for contact with required and conditionally required fields only
			//String sRand = Randomizefuncs.genDateBasedRandVal(8);
			int sRand = (int) Math.round((Math.random()) * 10000);
			
			//Define variables contact2 with all  fields populated in full form
			contact1.sSalutation = "Mr";
			contact1.sFirstName = "E2E_First_" + sRand ;
			contact1.sLastName = "E2E_Last_" + sRand;
			contact1.sPreferredName = "E2E_Pref_" + sRand;
			contact1.sAltFirstName = "Alt_"+contact1.sFirstName;
			contact1.sAltLastName = "Alt_"+contact1.sLastName;
			contact1.sAltPreferredName = "Alt_"+contact1.sPreferredName;
			contact1.sClientName = client.sClientID;
			contact1.sContactStatus = "Pending Marketing Verification";
			contact1.sJobTitle = "E2E Tester";
			contact1.sMobile = "555-111-1111";
			contact1.sOfficePhone = "666-222-2222";
			contact1.sEmail0 = contact1.sLastName + "@SFAE2E.com";
			contact1.sCountry = "United States";
			contact1.sState = "North Carolina";
			contact1.sCity = "Cary";
			contact1.sPostalCode = "11111";
			contact1.sAddress = "1337 Test Street";
			contact1.sAltCountry = "Italy";
			contact1.sAltState = "Ancona";
			contact1.sAltPostalCode = "22222";
			contact1.sAltAddress = "101 Java Lane";
			contact1.sAltCity = "Also Italy";
			
			contactEdit.sSalutation = "Mr";
			contactEdit.sFirstName = "E2E_First_" + sRand + "_Edit";
			contactEdit.sLastName = "E2E_Last_" + sRand + "_Edit";
			contactEdit.sPreferredName = "E2E_Pref_" + sRand + "_Edit";
			contactEdit.sAltFirstName = "Alt_"+contactEdit.sFirstName;
			contactEdit.sAltLastName = "Alt_"+contactEdit.sLastName;
			contactEdit.sAltPreferredName = "Alt_"+contactEdit.sPreferredName;
			contactEdit.sClientName = client.sClientID;
			contactEdit.sContactStatus = "Pending Marketing Verification";
			contactEdit.sJobTitle = "E2E Tester EDIT";
			contactEdit.sMobile = "555-111-1111";
			contactEdit.sOfficePhone = "666-222-2222";
			contactEdit.sEmail0 = contact1.sLastName + "_Edit@SFAE2E.com";
			contactEdit.sCountry = "United States";
			contactEdit.sState = "North Carolina";
			contactEdit.sCity = "Cary";
			contactEdit.sPostalCode = "11111";
			contactEdit.sAddress = "1337 Test Street";
			contactEdit.sAltCountry = "Italy";
			contactEdit.sAltState = "Ancona";
			contactEdit.sAltPostalCode = "22222";
			contactEdit.sAltAddress = "101 Java Lane";
			contactEdit.sAltCity = "Also Italy";
			
			
			
			
			//TESTCASE BEGIN
			log.info("Logging in");
			Dashboard dashboard = launchWithLogin(user1);

			//Create contact1
			CreateContactPage createContactPage = dashboard.openCreateContact();
			createContactPage.enterContactInfo(contact1, client);
			contact1.sClientID = "";
			contact1.sClientName = client.sClientName;
			createContactPage.saveContact();
			
			
			ViewContactPage viewContactPage = dashboard.openViewContact();
			viewContactPage.isPageLoaded();
			viewContactPage.searchForContact(contact1);
			viewContactPage.isPageLoaded();
			
			//If PrivacyPopup appears, select No and close the dialog
			if (viewContactPage.isPrivacyPopUpPresent()){
				viewContactPage.noPrivacyPopUp();
			}
			
			Assert.assertEquals(viewContactPage.checkResult(contact1), true);
			
			ContactDetailPage contactDetailPage = viewContactPage.selectResult(contact1);
			EditContactPage editContactPage = contactDetailPage.editContact();
			Contact testContactFromFull = editContactPage.getContact();
			
			viewContactPage = dashboard.openViewContact();
			viewContactPage.isPageLoaded();
			viewContactPage.searchForContact(contact1);
			viewContactPage.isPageLoaded();
			
			//If PrivacyPopup appears, select No and close the dialog
			if (viewContactPage.isPrivacyPopUpPresent()){
				viewContactPage.noPrivacyPopUp();
			}
			
//			Assert.assertEquals(viewContactPage.checkResult(contact1), true);

			EditContactSubpanel editContactSubpanel = viewContactPage.selectEditContactSubpanel(contact1);
			Contact testContactFromSubpanel = editContactSubpanel.getContact();
			
			Assert.assertTrue(testContactFromFull.equalsFromSubpanel(testContactFromSubpanel));
			
			editContactSubpanel.enterContactInfo(contactEdit, client);
			editContactSubpanel.saveContact();
			
			viewContactPage = dashboard.openViewContact();
			viewContactPage.isPageLoaded();
			viewContactPage.searchForContact(contactEdit);
			viewContactPage.isPageLoaded();
			//If PrivacyPopup appears, select No and close the dialog
			if (viewContactPage.isPrivacyPopUpPresent()){
				viewContactPage.noPrivacyPopUp();
			}
			
			Assert.assertEquals(viewContactPage.checkResult(contactEdit), true);
			
			editContactSubpanel = viewContactPage.selectEditContactSubpanel(contactEdit);			
			testContactFromSubpanel = editContactSubpanel.getContact();
			
			viewContactPage = editContactSubpanel.cancel();
			contactDetailPage = viewContactPage.selectResult(contactEdit);
			
			editContactPage = contactDetailPage.editContact();
			testContactFromFull = editContactPage.getContact();
			
			Assert.assertTrue(testContactFromFull.equalsFromSubpanel(testContactFromSubpanel));
			
		} catch (Exception e) {
			log.error(e.toString());
		}
		commonClientAllocator.checkInAllGroupClients("DC");
		commonUserAllocator.checkInAllUsersWithToken(this);
		log.info("End of test method E2EEditContactFromSubpanel");
	}
	
}
