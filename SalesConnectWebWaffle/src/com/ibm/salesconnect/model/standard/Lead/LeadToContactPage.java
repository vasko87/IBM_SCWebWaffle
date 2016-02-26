package com.ibm.salesconnect.model.standard.Lead;

import org.testng.Assert;

import com.ibm.atmn.waffle.core.RCLocationExecutor;
import com.ibm.salesconnect.objects.Lead;

public class LeadToContactPage extends LeadDetailPage{

	public static String pageLoaded = "//a[@name='associate_button' and contains(text(),'Associate Contact') and @class!='btn disabled']";
	public static String editIcon = "//div[@id='content']/div/div/div/div/div/div/div/h1/div/span[3]/a/span";
	public static String convertdropdown="//a[contains(text(),'Convert')]";
	public static String associateContactButton="//a[@name='associate_button' and contains(text(),'Associate Contact') and @class!='btn disabled']";
	public static String clientName="//span[@data-name='name']//input";	
	public static String indusSolnUnitArrow="//span[@data-fieldname='indus_isu_name']//span[@class='select2-arrow']";	                          
	public static String selectType = "//div[@id='select2-drop']/div/input";
	public static String selectMatch = "//span[@class='select2-match']";	
	public static String selectIndustry = "//div[@data-module='Accounts']//div[@data-name='fieldset-left-top']//div[4]//div//span[@class='select2-chosen']";
	public static String selectIndustryClass = "//div[@data-module='Accounts']//div[@data-name='fieldset-left-top']//div[6]//div//span[@class='select2-chosen']";
	public static String selectIndustryClass1 = "//div[2]/div/div[2]/span/span/div[3]/span/div/a/span";
	public static String associateClientButton="//a[@name='associate_button' and contains(text(),'Associate Client') and @class!='btn disabled']";
	public static String opportunityDescription="//input[@name='description']";
	public static String oppSourceArrow = "//span[@data-fieldname='lead_source']//span[@class='select2-arrow']";
	public static String associateOpportunityButton="//a[@name='associate_button' and contains(text(),'Associate Opportunity') and @class!='btn disabled']";
	public static String saveAndConvertButton="//a[contains(text(),'Save and Convert')]";
	public static String checkLabelStatus="//span[@data-name='badge']//span[@class='label label-success']";
	public static String oppOwnerTextBox="//span[@data-fieldname='assigned_user_name']//input";
	public static String openClientPanel="//div[@data-module='Accounts']";
	public static String openContactPanel="//div[@data-module='Contacts']";
	public static String sBillingAddressStreet="//textarea[@name='billing_address_street']";
	public static String sBillingAddressCity = "//input[@name='billing_address_city']";
	public static String sBillingAddressPincode = "//input[@name='billing_address_postalcode']";
	public static String selectBillingAddressCountrydropdown = "//div[@data-module='Accounts']//div[@data-name='billing_address']//span/div[3]/span/div/a/span[@class='select2-chosen']";





	public LeadToContactPage(RCLocationExecutor exec) {
		super(exec);
		Assert.assertTrue(isPageLoaded(), "View Lead page has not loaded within 60 seconds");	
	}

	@Override
	public boolean isPageLoaded() {
		return waitForElement(pageLoaded);
	}

	public void clickAssociateClient(Lead lead) {
		if(!waitForElement(associateClientButton))
			click(openClientPanel);
		type(clientName,lead.sClientBN);
		select(selectIndustry, lead.sIndustry);
		select(selectIndustryClass, lead.sIndustryClass);
		type(sBillingAddressStreet, lead.sBillingAddress);		
		type(sBillingAddressCity, lead.sBillingAddressCity);		
		type(sBillingAddressPincode, lead.sBillingAddressPincode);
		select(selectBillingAddressCountrydropdown,lead.sBillingAddressCountry);
		click(associateClientButton);
	}

	public void clickAssociateContact() {
		if(!waitForElement(associateContactButton))
			click(openContactPanel);
		if(checkForElement("//div[@data-module='Contacts']//a[contains(text(),'Ignore and create new')]"))
			click("//div[@data-module='Contacts']//a[contains(text(),'Ignore and create new')]");
		click(associateContactButton);
	}

	public void clickAssociateOpportunity(Lead lead)
	{
		enterOppDescription(lead.sOppDesc);
		enterOppOwner(lead.sClientBN);
		selectSource(lead);
		click(associateOpportunityButton);
	}

	public void selectSource(Lead lead) {
		click(oppSourceArrow);
		type(selectType, lead.sOppSource);
		click(selectMatch);
	}

	public void selectIndusSolnUnit(String sIndustrySolutionUnit) {
		click(indusSolnUnitArrow);
		type(selectType, sIndustrySolutionUnit);
		click(selectMatch);
	}

	public void selectIndusSoln(String sIndustrySolution) {
		click(indusSolnArrow);
		type(selectType, sIndustrySolution);
		click(selectMatch);
	}

	public void selectIndustryClass(String sIndustryClass) {
		click(indusClassArrow);
		type(selectType, sIndustryClass);
		click(selectMatch);
	}

	public void selectQuadTier(String sGBQuad) {
		click(indusQuadTierArrow);
		type(selectType, sGBQuad);
		click(selectMatch);
	}

	public void selectISIC(String sISIC) {
		click(indusSICArrow);
		type(selectType, sISIC);
		click(selectMatch);
	}

	public void enterOppDescription(String sOppDesc)
	{
		type(opportunityDescription,sOppDesc);
	}

	public void enterOppOwner(String sOppOwner)
	{
		type(oppOwnerTextBox,sOppOwner);
	}

	public LeadDetailPage clickOnSave() {
		click(saveAndConvertButton);
		return new LeadDetailPage(exec);
	}


}
