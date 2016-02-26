/**
 * 
 */
package com.ibm.salesconnect.model.standard.Client;

import java.util.ListIterator;

import org.openqa.selenium.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardPageFrame;
import com.ibm.salesconnect.objects.Client;

/**
 * @author timlehane
 * @date Apr 22, 2013
 */
public class CreateClientPage extends StandardPageFrame{
	Logger log = LoggerFactory.getLogger(CreateClientPage.class);
	/**
	 * @param exec
	 */
	public CreateClientPage(Executor exec) {
		super(exec);
		Assert.assertTrue(isPageLoaded(), "Create Client page has not loaded within 60 seconds");	
	}

	/* (non-Javadoc)
	 * @see com.ibm.ecm.product.model.StandardPageFrame#isPageLoaded()
	 */
	@Override
	public boolean isPageLoaded() {
		return waitForPageToLoad(pageLoaded);
	}

	//Selectors
	public static String getTextField_ClientName = "//*[@id='name']";
	
	//Physical Address
	public static String getTextField_PhysicalCountry = "//input[@id='billing_address_country-input']";
	public static String getButton_PhysicalCountryDrop = "//button[@id='billing_address_country-image']";
	public String getPhysicalCountryPick(String sPhysicalCountry){
		return "//ul[@role='listbox']//li[@data-text='"+sPhysicalCountry+"']";
	}
	public static String getTextArea_PhysicalStreet = "//textarea[@id='billing_address_street']";
	public static String getTextField_PhysicalCity = "//input[@id='billing_address_city']";
	public static String getTextField_PhysicalPostal = "//input[@id='billing_address_postalcode']";
	public static String getListBox_PhysicalState = "//select[@id='billing_address_state']";
	public static String getTextField_PhysicalState = "//input[@id='billing_address_state-input']";
	public static String getButton_PhysicalStateDrop = "//button[@id='billing_address_state-image']";	
	public static String getPhysicalStatePick(String sPhysicalState) {
		return "//input[@id='billing_address_state-input']/..//ul[@role='listbox']/li[@data-text='"+sPhysicalState+"']";
	}
	
	//Contact Info
	public static String getTextField_Website = "//*[@id='website']";
	public static String getTextField_MainPhone = "//input[@id='phone_office']";
	public static String getTextField_Fax = "//input[@id='phone_fax']";
	public static String getTextField_Tags = "//input[@id='tags']";
	
	public static String getCheckbox_CopyAddress = "//input[@id='shipping_address_checkbox']";
	
	//Mailing address
	public String getMailCountryPick(String sMailCountry){
		return "//div/input[@id='shipping_address_country-input']/../descendant::ul/descendant::li[@role='option'][@data-text='"+sMailCountry+"']";
	}
	public static String getButton_MailCountryDrop = "//button[@id='shipping_address_country-image']";
	public static String getTextArea_MailStreet = "//textarea[@id='shipping_address_street']";
	public static String getTextField_MailState = "//input[@id='shipping_address_state-input']";
	public static String getTextField_MailCity = "//input[@id='shipping_address_city']";
	public static String getTextField_MailPostal = "//input[@id='shipping_address_postalcode']";
	
	//Industry
	public static String getdrop_ISU = "//input[@id='indus_isu_name-input']";
	public static String getdrop_Industry = "//input[@id='industry_list-input']";
	public static String getdrop_IndustryClass = "//input[@id='indus_class_name-input']";
	public static String getdrop_ISIC = "//input[@id='indus_sic_code-input']";
	public static String getdrop_GBQuad = "//input[@id='indus_quad_tier_code-input']";
	public String getSelection(String dropdown, String choice){return dropdown + "/..//li";};
	//public String getCountrySelection(String dropdown, String choice){return dropdown + "/..//*[contains(text(),'"+choice+"')]";};
	
	public static String getLink_Save = "//input[@id='SAVE_FOOTER']";
	public static String confirm_Create = "//button[text()='Confirm Create']";
	//public static String getView_ChangeLog = "//a[@id='btn_view_change_log']";
	public static String getUpdates= "//*[@id='Accounts_detailview_tabs']/ul/li[2]/a/em";
	public static String pageLoaded = "//*[@id='name']";
	public static String creationFrame = "//iframe[@id='bwc-frame']";

	
	//Tasks
	public CreateClientPage enterClientInfo(Client client){
			//set Business name
			if (client.sClientName.length()>0){
				type(getTextField_ClientName, client.sClientName);	
			}
			
			//physical country
			if (client.sPhysicalCountry.length()>0){
				type(getTextField_PhysicalCountry, client.sPhysicalCountry);
				if(isPresent(getSelection(getTextField_PhysicalCountry, client.sPhysicalCountry))){
					click(getSelection(getTextField_PhysicalCountry, client.sPhysicalCountry));
				}
			}
			
			//*** Physical Address
			//physical street
			if (client.sPhysicalStreet.length()>0){
				type(getTextArea_PhysicalStreet,client.sPhysicalStreet);
			}

			//physical city
			if (client.sPhysicalCity.length()>0){
				type(getTextField_PhysicalCity,client.sPhysicalCity);
			}


			//physical postal
			if (client.sPhysicalPostal.length()>0){
				type(getTextField_PhysicalPostal,client.sPhysicalPostal);
			}

			//physical state
			if (client.sPhysicalState.length() > 0){
				type(getTextField_PhysicalState, client.sPhysicalState);
				if(isPresent(getSelection(getTextField_PhysicalState, client.sPhysicalState))){
					click(getSelection(getTextField_PhysicalState, client.sPhysicalState));
				}
			}

			// set website
			if (client.sWebsite.length()>0){
				type(getTextField_Website,client.sWebsite);
			}


			// set office phone
			if (client.sPhoneNumber.length()>0){
				type(getTextField_MainPhone,client.sPhoneNumber);
			}

			//set fax
			if (client.sPhoneFax.length()>0){
				type(getTextField_Fax,client.sFaxNumber);
			}


			//tags
			if(client.sTags.length()>0){
				type(getTextField_Tags,client.sTags);
			}


			//copy physical address
			if (client.bCopyPhysical){
				if (!isChecked(getCheckbox_CopyAddress)){
					click(getCheckbox_CopyAddress);
				}
			}else {
					//mail country - this needs to be set before state
				if (client.sMailCountry.length()>0){
					click(getButton_MailCountryDrop);
					click(getMailCountryPick(client.sMailCountry));
				}
	
				//mail street
				if (client.sMailStreet.length()>0){
					type(getTextArea_MailStreet,client.sMailStreet);
				}
	
				//mail city
				if (client.sMailCity.length()>0){
					type(getTextField_MailCity,client.sMailCity);
				}
						
				//mail state
				if (client.sMailState.length()>0){
					type(getTextField_MailState,client.sMailState);
					sleep(2);
					sendKeys(getTextField_MailState, Keys.ENTER);
				}
				
				//mail postal
				if (client.sMailPostal.length()>0){
					type(getTextField_MailPostal,client.sMailPostal);
				}
			}

			if (client.sCity.length()>0){
				type(getTextField_PhysicalCity,client.sCity);
			}

			if (client.sCountry.length()>0){
				click(getButton_PhysicalCountryDrop);
				click(getPhysicalCountryPick(client.sCountry));			
			}

			if(!client.vTags.isEmpty()){
				ListIterator<String> iTag = client.vTags.listIterator();		
				while (iTag.hasNext()) {
					type(getTextField_Tags,iTag.next());
				}
			}	
			
			if(client.sISU.length()>0){
				type(getdrop_ISU,client.sISU);
				if(isPresent(getSelection(getdrop_ISU, client.sISU))){
					click(getSelection(getdrop_ISU, client.sISU));
				}
			}
			
			if(client.sIndustry.length()>0){
				type(getdrop_Industry,client.sIndustry);
				if(isPresent(getSelection(getdrop_Industry, client.sIndustry))){
					click(getSelection(getdrop_Industry, client.sIndustry));
				}
			}
			
			if (client.sIndustryClass.length()>0) {
				type(getdrop_IndustryClass, client.sIndustryClass);
				if(isPresent(getSelection(getdrop_IndustryClass, client.sIndustryClass))){
					click(getSelection(getdrop_IndustryClass, client.sIndustryClass));
				}
			}
			
			if (client.sISIC.length()>0) {
				type(getdrop_ISIC, client.sISIC);
				if(isPresent(getSelection(getdrop_ISIC, client.sISIC))){
					click(getSelection(getdrop_ISIC, client.sISIC));
				}
			}
			
			if (client.sGBQuad.length()>0) {
				type(getdrop_GBQuad, client.sGBQuad);
				if(isPresent(getSelection(getdrop_GBQuad, client.sGBQuad))){
					click(getSelection(getdrop_GBQuad, client.sGBQuad));
				}
			}
		
		return this;
	}

	public void saveClient(){
		click(getLink_Save);
		try{
			exec.switchToAlert();
		}
		catch(Exception e){
			log.info("Did not switch to confirm create alert");
		}
		try{
			waitForElement(confirm_Create, 120000);
			click(confirm_Create);
		}
		catch(Exception e){
			log.info("Confirm create was not required, continuing with test");
		}
		
		if(waitForPageToLoad(getUpdates)){
			log.info("Client Created");
		}
		
	}

	
}
