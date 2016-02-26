/**
 * 
 */
package com.ibm.salesconnect.model.partials;

import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardPageFrame;
import com.ibm.salesconnect.model.standard.Contact.ViewContactPage;
import com.ibm.salesconnect.objects.Client;
import com.ibm.salesconnect.objects.Contact;

/**
 * @author timlehane
 * @date May 30, 2013
 */
public class EditContactSubpanel extends StandardPageFrame {

	/**
	 * @param exec
	 */
	public EditContactSubpanel(Executor exec) {
		super(exec);
		Assert.assertTrue(isPageLoaded(), "Documents Subpanel has not loaded within 60 seconds");	
	}

	@Override
	public boolean isPageLoaded() {
		return waitForSubpanelToLoad(pageLoaded);
	}
	
	
	public static String firstName = "//*[@id='first_name']";
	public static String lastName = "//*[@id='last_name']";
	public static String prefName = "//*[@id='preferred_name_c']";
	public static String altFirstName = "//*[@id='alt_lang_first_c']";
	public static String altLastName = "//*[@id='alt_lang_last_c']";
	public static String altPrefName = "//*[@id='alt_lang_preferred_name_c']";
	public static String clientField = "//*[@id='account_name']";
	public static String getClientPopup = "//*[@id='btn_account_name']";
	public static String clearClientField = "//*[@id='btn_clr_account_name']";
	public static String jobTitle = "//*[@id='title']";
	public static String workPhone = "//*[@id='phone_work']";
	public static String mobilePhone = "//*[@id='phone_mobile']";
	public static String emailAddress0 = "//*[@id='Contacts0emailAddress0']";
	public static String emailAddress1 = "//*[@id='Contacts1emailAddress1']";
	public static String languages = "//*[@id='language_c_ac']";
	public static String cancelButton = "//*[@id='Contacts_dcmenu_cancel_button']";
	
	public static String pageLoaded = "//*[@id='Contacts_dcmenu_save_button']";
	public static String createPageLoaded = "//input[@id='filename_file']";
	public static String addUsersIcon = "//button[@id='btn_documents_selected_users']";
	public String getDocLink(String docName){return "//a[contains(text(),'" + docName + "')]";};
	public static String status = "//div[@id='ajaxStatusDiv']";
	
	//public static String saveButton = "//input[@id='Documents_subpanel_save_button']";
	public static String saveButton = "//*[@id='Contacts_dcmenu_save_button']";
	//xpath=(//input[@id='Documents_subpanel_save_button'])[2]
	
	
	public void saveContact(){
		click(saveButton);
		/*if(!isSaved(status)){
			Assert.assertTrue(false, "Document has not been saved successfully");
		}*/
		
		try{
			Thread.sleep(5000);
		}
		catch(Exception e){
			System.out.println(e.toString());
		}

	}
	
	public ViewContactPage cancel(){
		click(cancelButton);
		return new ViewContactPage(exec);
	}
	
	public void enterContactInfo(Contact contact, Client client){
		type(firstName, contact.sFirstName);
		type(lastName, contact.sLastName);
		type(prefName, contact.sPreferredName);
		type(altFirstName, contact.sAltFirstName);
		type(altLastName, contact.sAltLastName);
		type(altPrefName, contact.sAltPreferredName);
		
		if ((client.sClientID.length()>0) || (contact.sClientName.length()>0)){
			ClientSelectPopup clientSelectPopup = openSelectClient();
			clientSelectPopup.searchForClient(client);
			clientSelectPopup.selectResult(client);
		}
		
		type(jobTitle, contact.sJobTitle);
		type(workPhone, contact.sOfficePhone);
		type(mobilePhone, contact.sMobile);
		type(emailAddress0, contact.sEmail0);
		if(isPresent(emailAddress1)){
			type(emailAddress1, contact.sEmail1);
		}
		type(languages, contact.sLanguage);
	}
	
	public Contact getContact(){
		Contact contact = new Contact();
		
		contact.sFirstName = getObjectAttribute(firstName, "value");
		contact.sLastName = getObjectAttribute(lastName, "value");
		contact.sPreferredName = getObjectAttribute(prefName, "value");
		contact.sAltFirstName = getObjectAttribute(altFirstName, "value");
		contact.sAltLastName = getObjectAttribute(altLastName, "value");
		contact.sAltPreferredName = getObjectAttribute(altPrefName, "value");
		
		contact.sClientName = getObjectAttribute(clientField, "value");
		
		contact.sJobTitle = getObjectAttribute(jobTitle, "value");
		contact.sOfficePhone = getObjectAttribute(workPhone, "value");
		contact.sMobile = getObjectAttribute(mobilePhone, "value");
		contact.sEmail0 = getObjectAttribute(emailAddress0, "value");
		if(isPresent(emailAddress1)){
			contact.sEmail1 = getObjectAttribute(emailAddress1, "value");
		}
		contact.sLanguage = getObjectAttribute(languages, "value");
		
		
		
		
		return contact;
	}
	
	/**
	 * Open the select user popup to Select Individual to Share with
	 * @return select user object
	 */
	public ClientSelectPopup openSelectClient() {
		//Popup sometimes takes a while to load so needs an extra sleep
		sleep(5);
		waitForPageToLoad(getClientPopup);
		click(getClientPopup);
		getPopUp();
		return new ClientSelectPopup(exec);
	}


}
