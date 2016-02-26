/**
 * 
 */
package com.ibm.salesconnect.model.standard.Contact;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardPageFrame;
import com.ibm.salesconnect.model.partials.ClientSelectPopup;
import com.ibm.salesconnect.objects.Client;
import com.ibm.salesconnect.objects.Contact;

/**
 * @author Administrator
 *
 */
public class CreateContactPage extends StandardPageFrame {
	Logger log = LoggerFactory.getLogger(CreateContactPage.class);

	/**
	 * @param exec
	 */
	public CreateContactPage(Executor exec) {
		super(exec);
		Assert.assertTrue(isPageLoaded(), "Create Client page has not loaded within 60 seconds");	
	}

	@Override
	public boolean isPageLoaded() {
		scrollToBottomOfPage();
		return waitForPageToLoad(pageLoaded);
	}

	//Selectors
	public static String pageLoaded = "//input[@id='first_name']";
	public static String creationFrame = "//iframe[@id='bwc-frame']";

	public static String saveFooter = "//input[@id='SAVE_WITH_SERVER_VALIDATION']";
	public static String getLink_Edit = "//*[@id='edit_button']";
	
	public static String collapseAdvanced = "//img[@id='detailpanel_1_img_hide']";
	public static String clientSearchButton = "//button[@id='btn_account_name']";
	public static String MyFavouritesIcon = "//*[@id='content']/div[1]/h2/div/div";
	
	public static String getTextField_FirstName = "//input[@id='first_name']";
	public static String getTextField_LastName = "//input[@id='last_name']";
	public static String getTextField_PreferredName = "//input[@id='preferred_name_c']";
	public static String getText_AltFirstName = "//input[@id='alt_lang_first_c']";
	public static String getText_AltLastName = "//input[@id='alt_lang_last_c']";
	public static String getText_AltPreferredName = "//input[@id='alt_lang_preferred_name_c']";
	public static String getTextField_JobTitle = "//input[@id='title']";
	public static String getTextField_OfficePhone = "//input[@id='phone_work']";
	public static String getTextField_Mobile = "//input[@id='phone_mobile']";
	public static String getContactStatusText = "//select[@id='contact_status_c']//option[@selected='selected']";
	
	public static String getTextField_ClientName = "//input[@id='account_name']";
	
	public String getTextField_EmailAddress(String sRow) {return ("//input[@id='Contacts0emailAddress"+sRow+"']");}
	public String getRadio_EmailPrimary(String sRow) {return ("//input[@id='Contacts0emailAddressPrimaryFlag"+sRow+"']");}	
	public String getCheckBox_EmailSuppressed(String sRow) {return ("//input[@id='Contacts0emailAddressOptOutFlag"+sRow+"']");}
	public String getCheckBox_EmailInvalid(String sRow) {return ("//input[@id='Contacts0emailAddressInvalidFlag"+sRow+"']");}
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
	public static String getText_AltStreetAddress = "//textarea[@id='alt_address_street']";
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
	public String getSelection(String dropdown, String choice){return dropdown + "/..//li";};
	//public String getCountrySelection(String dropdown, String choice){return dropdown + "/..//*[contains(text(),'"+choice+"')]";};
	
	//Tasks
	/**
	 * Enter contact infomation
	 */
	public CreateContactPage enterContactInfo(Contact contact, Client client){
		
		isPresent(getTextField_FirstName);
		scrollElementToMiddleOfBrowser(getTextField_FirstName);
			
		if (contact.sFirstName.length()>0){
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
			switchToFrame(creationFrame);
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
			type(getText_Country, contact.sCountry);
//			if(isPresent(getSelection(getDropdown_Country, contact.sCountry))){
				click(getSelection(getDropdown_Country, contact.sCountry));
//			}
		}
	
		if (contact.sAddress.length()>0){
			type(getText_StreetAddress,contact.sAddress);
		}
	
		if (contact.sCity.length()>0){
			type(getText_City,contact.sCity);
		}
	
		if (contact.sState.length()>0){
			type(getText_StateProvince, contact.sState);
			if(isPresent(getSelection(getDropdown_StateProvince, contact.sState))){
				click(getSelection(getDropdown_StateProvince, contact.sState));
			}
		}
	
		if (contact.sPostalCode.length()>0){
			type(getText_PostalCode, contact.sPostalCode);
		}
	
		if (contact.sAltCountry.length()>0){
			type(getText_AltCountry, contact.sAltCountry);
			if(isPresent(getSelection(getDropdown_AltCountry, contact.sAltCountry))){
				click(getSelection(getDropdown_AltCountry, contact.sAltCountry));
			}
		}
		
		if (contact.sAltAddress.length()>0){
			type(getText_AltStreetAddress,contact.sAltAddress);
		}
	
		if (contact.sAltCity.length()>0){
			type(getText_AltCity,contact.sAltCity);
		}
	
		if (contact.sAltState.length()>0){
			type(getText_AltStateProvince, contact.sAltState);
			if(isPresent(getSelection(getDropdown_AltStateProvince, contact.sAltState))){
				click(getSelection(getDropdown_AltStateProvince, contact.sAltState));
			}
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

		return this;
	}
	
	public Contact getContact(){
		Contact contact = new Contact();
		
		
		
		contact.sFirstName = getObjectAttribute(getTextField_FirstName, "value");
	
		contact.sLastName = getObjectAttribute(getTextField_LastName, "value");

		contact.sPreferredName = getObjectAttribute(getTextField_PreferredName, "value");
	
		contact.sAltFirstName = getObjectAttribute(getText_AltFirstName, "value");

		contact.sAltLastName = getObjectAttribute(getText_AltLastName, "value");
	
		contact.sAltPreferredName = getObjectAttribute(getText_AltPreferredName, "value");
	
		contact.sClientName = getObjectAttribute(getTextField_ClientName, "value");
	
		contact.sJobTitle = getObjectAttribute(getTextField_JobTitle, "value");
	
		contact.sOfficePhone = getObjectAttribute(getTextField_OfficePhone, "value");
	
		contact.sMobile = getObjectAttribute(getTextField_Mobile, "value");
	
		if(isPresent(getTextField_EmailAddress("0"))){
			contact.sEmail0 = getObjectAttribute(getTextField_EmailAddress("0"), "value");
			
			contact.bEmail0Primary = isChecked(getRadio_EmailPrimary("0"));
			
			contact.bEmail0Suppress = isChecked(getCheckBox_EmailSuppressed("0"));
			
			contact.bEmail0Invalid = isChecked(getCheckBox_EmailInvalid("0"));
		}
		
		if(isPresent(getTextField_EmailAddress("1"))){
			contact.sEmail1 = getObjectAttribute(getTextField_EmailAddress("1"), "value");
			
			contact.bEmail1Primary = isChecked(getRadio_EmailPrimary("1"));
			
			contact.bEmail1Suppress = isChecked(getCheckBox_EmailSuppressed("1"));
			
			contact.bEmail1Invalid = isChecked(getCheckBox_EmailInvalid("1"));
		}

		contact.sSalutation = getObjectAttribute(getTextField_Salutation, "value");
		
		contact.sContactStatus = getObjectAttribute(getContactStatusText, "label");
		
		contact.bKeyContact = isChecked(getCheckBox_KeyContact);
			
		contact.bKeySetMet = isChecked(getCheckBox_Relationship);

		contact.sCountry = getObjectAttribute(getText_Country, "value");
	
		contact.sAddress = getObjectAttribute(getText_StreetAddress, "value");
	
		contact.sCity = getObjectAttribute(getText_City, "value");
		
		if(isPresent(getText_StateProvince)){
	
			contact.sState = getObjectAttribute(getText_StateProvince, "value");
		}
	
		contact.sPostalCode = getObjectAttribute(getText_PostalCode, "value");
		
		if(isPresent(getText_AltCountry)){
		
			contact.sAltCountry = getObjectAttribute(getText_AltCountry, "value");
			
			contact.sAltAddress = getObjectAttribute(getText_AltStreetAddress, "value");
		
			contact.sAltCity = getObjectAttribute(getText_AltCity, "value");
			
			if(isPresent(getText_AltStateProvince)){
		
				contact.sAltState = getObjectAttribute(getText_AltStateProvince, "value");
			}
			
			contact.sAltPostalCode = getObjectAttribute(getText_AltPostalCode, "value");
		}
		
		contact.sTags = getObjectAttribute(getText_Tags, "value");
	
		//click(additionalInformationPanelTwisty);
			
		contact.sPhoneFax = getObjectAttribute(getTextField_Fax, "value");
		
		contact.bPhoneFaxSuppress = isChecked(getCheckBox_FaxSuppressed);
		
		contact.sLanguage = getObjectAttribute(getTextField_Language, "value");
		
		return contact;
	}
	
	/**
	 * Open the select client popup
	 * @return select client object
	 */
	public ClientSelectPopup openSelectClient(){
		sleep(5);//adding extra wait
		scrollElementToMiddleOfBrowser(clientSearchButton);
		click(clientSearchButton);
		sleep(10);//adding extra wait
		switchToMainWindow();
		getPopUp();
		return  new ClientSelectPopup(exec);
	}
	
	/**
	 * Click the save button
	 * @throws InterruptedException 
	 */
	public ContactDetailPage saveContact() {
		if(isPresent(saveFooter)){
			click(saveFooter);
		}
		else{
			click(collapseAdvanced);
			click(saveFooter);
		}
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		switchToMainWindow();
		
		return new ContactDetailPage(exec);
	}
	
	public void editEmailName(String email){
		type(getTextField_EmailAddress("0"),email);
	}
}
