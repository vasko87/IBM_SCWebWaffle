/**
 * 
 */
package com.ibm.salesconnect.model.partials;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardPageFrame;
import com.ibm.salesconnect.objects.Client;
import com.ibm.salesconnect.objects.Contact;

/**
 * @author evafarrell
 * @date Sept 27, 2013
 */
public class ContactSubpanel extends StandardPageFrame {
	Logger log = LoggerFactory.getLogger(ContactSubpanel.class);

	/**
	 * @param exec
	 */
	public ContactSubpanel(Executor exec) {
		super(exec);
		Assert.assertTrue(isPageLoaded(), "ContactSubpanel has not loaded within 60 seconds");	
	}


	@Override
	public boolean isPageLoaded() {
//		isPresent(pageLoaded);
//		scrollElementToMiddleOfBrowser(pageLoaded);
		return waitForSubpanelToLoad(pageLoaded);
	}

	//Selectors
	public static String pageLoaded = "//div[@id='main']";
//	public static String pageLoaded ="//span[@sugar='slot5']";
//	public static String pageLoaded ="//a[contains(text(),'Office phone')]";
//	public static String createFormLoaded = "//textarea[@id='description']";
//	public static String createOpptyButton = "//a[@id='opportunities_contacts_create_button_create_button']";
	public static String frame = "//iframe[@id='bwc-frame']";
	public static String getTextField_FirstName = "//input[@id='first_name']";
	public static String getTextField_LastName = "//input[@id='last_name']";
	public static String getTextField_PreferredName = "//input[@id='preferred_name_c']";
	public static String getText_AltFirstName = "//input[@id='alt_lang_first_c']";
	public static String getText_AltLastName = "//input[@id='alt_lang_last_c']";
	public static String getText_AltPreferredName = "//input[@id='alt_lang_preferred_name_c']";
	public static String getTextField_JobTitle = "//input[@id='title']";
	public static String getTextField_OfficePhone = "//input[@id='phone_work']";
	public static String getTextField_Mobile = "//input[@id='phone_mobile']";
	
	public String getTextField_EmailAddress(String sRow) {return ("//input[@id='Contacts0emailAddress"+sRow+"']");}
	public String getRadio_EmailPrimary(String sRow) {return ("//input[@id='Contacts0emailAddressPrimaryFlag"+sRow+"']");}	
	public String getCheckBox_EmailSuppressed(String sRow) {return ("//input[@id='Contacts0emailAddressOptOutFlag"+sRow+"']");}
	public static String getLink_AddEmail = "//a[@id='AddEmailLink']";
	
	public static String getTextField_Salutation = "//input[@id='salutation']";
	public static String getListBox_ContactStatus = "//select[@id='contact_status_c']";
	
	public static String getCheckBox_KeyContact = "//input[@id='key_contact_c']";
	public static String getCheckBox_Relationship = "//input[@id='key_contact_relation_c']";
	
	public static String getText_Country = "//input[@id='primary_address_country-input']";
	public static String getDropdown_Country = "//button[@id='primary_address_country-image']";
	public String getPick_Country(String Country) {
		return "//input[@id='primary_address_country-input']/..//ul[@role='listbox']/li[@data-text='"+Country+"']";}
	
	public static String getText_StreetAddress = "//input[@id='primary_address_street']";
	public static String getText_City = "//input[@id='primary_address_city']";
	
	public static String getText_StateProvince = "//input[@id='primary_address_state-input']";
	public static String getDropdown_StateProvince = "//button[@id='primary_address_state-image']";
	public static String getPickStateProvince(String stateProvince) {
		return "//input[@id='primary_address_state-input']/..//ul[@role='listbox']/li[@data-text='"+stateProvince+"']";
	}
	public static String getText_PostalCode = "//input[@id='primary_address_postalcode']";
	
	public static String getText_AltCountry = "//input[@id='alt_address_country-input']";
	public static String getDropdown_AltCountry = "//button[@id='alt_address_country-image']";
	public static String getPickAltCountry(String altCountry) {
		return "//input[@id='alt_address_country-input']/..//ul[@role='listbox']/li[@data-text='"+altCountry+"']";
	}
	public static String getText_AltStreetAddress = "//input[@id='alt_address_street']";
	public static String getText_AltCity = "//input[@id='alt_address_city']";
	public static String getText_AltStateProvince = "//input[@id='alt_address_state-input']";
	public static String getDropdown_AltStateProvince = "//button[@id='alt_address_state-image']";
	public static String getPickAltStateProvince(String altStateProvince) {
		return "//input[@id='alt_address_state-input']/..//ul[@role='listbox']/li[@data-text='"+altStateProvince+"']";
	}
	public static String getText_AltPostalCode = "//input[@id='alt_address_postalcode']";
	
	public static String getText_Tags = "//input[@id='tags']";
	
	public static String additionalInformationPanel = "//div[@id='detailpanel_2']";
	public static String additionalInformationPanelTwisty = "//[@id='detailpanel_2_img_show']";
	public static String getTextField_Fax = "//input[@id='phone_fax']";
	public static String getCheckBox_FaxSuppressed = "//input[@id='phone_fax_suppressed']";
	public static String getTextField_Language = "//input[@id='language_c_ac']";
	
	public static String status = "//div[@id='ajaxStatusDiv']";
	public static String saveFooter = "//div[@class='buttons']//*[@id='Contacts_subpanel_save_button']";
	public static String clientSearchButton = "//button[@id='btn_account_name']";
	
	public static String createContactFromClient = "//a[@id='accounts_contacts_create_button']";
	public static String createContactFromOppty = "//a[@id='opportunities_contacts_createcontact_button']";
	public static String confirmCreate = "//input[@title='Confirm create']";
	public static String contactSubpanelMoreActions = "//div[@id='list_subpanel_contacts']/table/tbody/tr/td/table/tbody/tr/td/ul/li/span";
	public static String select = "//a[contains(text(),'Select')]";
	public static String searchExternalSites = "//a[contains(text(),'Search external sites')]";
	
	public String getContactLink(String contactName){return "//a[contains(text(),'" + contactName + "')]";};
	public static String nextContactPage = "//div[@id='list_subpanel_contacts']//button[@name='listViewNextButton']";
	
	public static String BusinessCard = "//span[@id='semtagmenu']/a";
	
	public void clickContactsMoreActions(){
		click(contactSubpanelMoreActions);
	}
	
	public boolean verifyContactsMoreActions(){
		if(isPresent(select) && !isPresent(searchExternalSites)){
			log.info("Correct Contacts Subpanel More Actions Found");
			return true;
		}
		else 
			log.info("Incorrect Contacts Subpanel More Actions Found");
			return false;
	}
	
	public void openCreateContactForm(){
		if(isPresent(createContactFromClient)){
			click(createContactFromClient);	
		}
		else if(isPresent(createContactFromOppty)){
			click(createContactFromOppty);	
		}
		waitForSubpanelToLoad(getTextField_FirstName);
	}
	public void enterContactInfo(Contact contact, Client client){
		if (contact.sFirstName.length()>0){
			scrollElementToMiddleOfBrowser(getTextField_FirstName);
			click(getTextField_FirstName);
			type(getTextField_FirstName,contact.sFirstName);
		}
	
		if (contact.sLastName.length()>0){
			type(getTextField_LastName,contact.sLastName);
		}
	
		if (contact.sPreferredName.length()>0){
			type(getTextField_PreferredName,contact.sPreferredName);
		}
	
		if (contact.sAltFirstName.length()>0){
			type(getText_AltFirstName,contact.sAltFirstName);
		}
	
		if (contact.sAltLastName.length()>0){
			type(getText_AltLastName,contact.sAltLastName);
		}
	
		if (contact.sAltPreferredName.length()>0){
			type(getText_AltPreferredName,contact.sAltPreferredName);
		}
	
		if ((client.sClientID.length()>0) || (contact.sClientName.length()>0)){
			ClientSelectPopup clientSelectPopup = openSelectClient();
			clientSelectPopup.searchForClient(client);
			clientSelectPopup.selectResult(client);
			switchToFrame(frame);
		}
	
		if (contact.sJobTitle.length()>0){
			type(getTextField_JobTitle,contact.sJobTitle);
		}
	
		if (contact.sOfficePhone.length()>0){
			type(getTextField_OfficePhone,contact.sOfficePhone);
		}
	
		if (contact.sMobile.length()>0){
			type(getTextField_Mobile,contact.sMobile);
		}
	
		//set first valid email field - 0 row
		String sRow = "0";
		if (contact.sEmail0.length()>0){
			type(getTextField_EmailAddress(sRow),contact.sEmail0);
			
			if (contact.bEmail0Primary){ 
				click(getRadio_EmailPrimary(sRow));
			}
			if (contact.bEmail0Suppress){//check the suppress box
				click(getCheckBox_EmailSuppressed(sRow));
			}
	
		}
	
		if (contact.sEmail1.length()>0){
			sRow = "1";
			if(!isPresent(getTextField_EmailAddress(sRow))){
				click(getLink_AddEmail);
			}

			type(getTextField_EmailAddress(sRow),contact.sEmail1);
			
			if (contact.bEmail1Primary){ 
				click(getRadio_EmailPrimary(sRow));
			}
			if (contact.bEmail1Suppress){//check the suppress box
				click(getCheckBox_EmailSuppressed(sRow));
			}
		}
	
		if (contact.sSalutation.length()>0){
			type(getTextField_Salutation,contact.sSalutation);
		}
		
		if (contact.sContactStatus.length()>0){
			select(getListBox_ContactStatus, contact.sContactStatus);
		}
		
		if(contact.sKeyContact.length()>0){
			if(contact.sKeyContact.equalsIgnoreCase("y")){
				click(getCheckBox_KeyContact);
			}
			else if(contact.sKeyContact.equalsIgnoreCase("r")){
				click(getCheckBox_Relationship);
			}
			else {
				Assert.assertTrue(false, "Y, R and '' are the only acceptable values for key contact. Current value:" + contact.sKeyContact);
			}
		}
		if (contact.sCountry.length()>0){
			click(getDropdown_Country);
			click(getPick_Country(contact.sCountry));
		}
	
		if (contact.sAddress.length()>0){
			type(getText_StreetAddress,contact.sAddress);
		}
	
		if (contact.sCity.length()>0){
			type(getText_City,contact.sCity);
		}
	
		if (contact.sState.length()>0){
			click(getDropdown_StateProvince);
			click(getPickStateProvince(contact.sState));
		}
	
		if (contact.sPostalCode.length()>0){
			type(getText_PostalCode, contact.sPostalCode);
		}
	
		if (contact.sAltCountry.length()>0){
			click(getDropdown_AltCountry);
			click(getPickAltCountry(contact.sAltCountry));
		}
		
		if (contact.sAltAddress.length()>0){
			type(getText_AltStreetAddress,contact.sAltAddress);
		}
	
		if (contact.sAltCity.length()>0){
			type(getText_AltCity,contact.sAltCity);
		}
	
		if (contact.sAltState.length()>0){
			click(getDropdown_AltStateProvince);
			click(getPickAltStateProvince(contact.sAltState));
		}
	
		if (contact.sAltPostalCode.length()>0){
			type(getText_AltPostalCode,contact.sAltPostalCode);
		}
	
		if (contact.sTags.length()>0){
			type(getText_Tags,contact.sTags);
		}
	
		//Add information on the additional contact information tab
		if (contact.sPhoneFax.length()>0 || 
				contact.sLanguage.length()>0 ||
				contact.sEmpHistClient.length()>0 || 
				contact.sEmpHistJobTitle.length()>0){
			if(getObjectAttribute(additionalInformationPanel, "class").contains("collapsed")){
				click(additionalInformationPanelTwisty);
			}
			
			if (contact.sPhoneFax.length()>0){
				type(getTextField_Fax,contact.sPhoneFax);
			}
			
			if (contact.bPhoneFaxSuppress){
				click(getCheckBox_FaxSuppressed);
			}
			
			if (contact.sLanguage.length()>0){
				type(getTextField_Language,contact.sLanguage);
			}
		}
	}
	
	/**
	 * Click the save button
	 */
	public void saveContact(){
		scrollElementToMiddleOfBrowser(saveFooter);
		click(saveFooter);
		sleep(2);
		if(getObjectText(status).contains("Saving")){
			if(!isSaved(status)){
				Assert.assertTrue(false, "Contact has not been saved successfully");
			}
		}
		if(isPresent(confirmCreate)){
			click(confirmCreate);
			if(!isSaved(status)){
				Assert.assertTrue(false, "Contact has not been saved successfully");
			}
		}
	}
	
	public ClientSelectPopup openSelectClient(){
		scrollElementToMiddleOfBrowser(clientSearchButton);
		click(clientSearchButton);
		getPopUp();
		return  new ClientSelectPopup(exec);
	}


	/**
	 * @param subPanelContact
	 */
	public void verifyContactPresent(String contactFirstName) {
		while(true){
			
			if(!isPresent(getContactLink(contactFirstName))){
				if(isPresent(nextContactPage)){
					click(nextContactPage);
					waitForPageToLoad(pageLoaded);
				}
				else{
					Assert.assertTrue(isPresent(getContactLink(contactFirstName)), "Contact not found");
					break;
				}
			}
			else{
				Assert.assertTrue(isPresent(getContactLink(contactFirstName)), "Contact not found");
				break;
			}
		}
	}			
	
	public void verifyBusinessCardDoesNotExist(String contactFirstName){
		verifyContactPresent(contactFirstName);
		mouseHoverSalesConnect(getContactLink(contactFirstName));
		Assert.assertFalse(isPresent(BusinessCard),"Business Card Found, this is unexpected, no Business Card should exist for Contact");
	}
}
