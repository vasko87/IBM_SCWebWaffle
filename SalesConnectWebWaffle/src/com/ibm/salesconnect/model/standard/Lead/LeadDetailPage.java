package com.ibm.salesconnect.model.standard.Lead;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.RCLocationExecutor;
import com.ibm.salesconnect.model.StandardDetailPage;
import com.ibm.salesconnect.model.standard.Call.CreateCallPage;
import com.ibm.salesconnect.objects.Lead;

/**
 * 
 * @author Veena H
 * @date May 25, 2015
 */
public class LeadDetailPage extends StandardDetailPage {
	Logger log = LoggerFactory.getLogger(LeadDetailPage.class);



	public LeadDetailPage(RCLocationExecutor exec) {
		super(exec);
		Assert.assertTrue(isPageLoaded(), "View Lead page has not loaded within 60 seconds");	
	}

	@Override
	public boolean isPageLoaded() {
		return waitForElement(pageLoaded);
	}

	public static String pageLoaded = "//span[@data-name='full_name']";
	public static String editIcon = "//div[@id='content']/div/div/div/div/div/div/div/h1/div/span[3]/a/span";
	public static String convertdropdown="//a[contains(text(),'Convert')]";
	public static String associateContactButton="//a[@name='associate_button' and  not(contains(class, 'disabled'))]";
	public static String clientName="//input[@name='name']";	
	public static String indusSolnUnitArrow="//span[@data-fieldname='indus_isu_name']//span[@class='select2-arrow']";	                          
	public static String selectType = "//div[@id='select2-drop']/div/input";
	public static String selectMatch = "//span[@class='select2-match']";	
	public static String indusSolnArrow="//span[@data-fieldname='industry_list']//span[@class='select2-arrow']";	                          
	public static String indusClassArrow="//span[@data-fieldname='indus_class_name']//span[@class='select2-arrow']";	 	
	public static String indusSICArrow="//span[@data-fieldname='indus_sic_code']//span[@class='select2-arrow']";	 	
	public static String indusQuadTierArrow="//span[@data-fieldname='indus_quad_tier_code']//span[@class='select2-arrow']";		
	public static String associateClientButton="//a[contains(text(),'Associate Client')]";
	public static String opportunityDescription="//input[@name='description']";
	public static String oppSourceArrow = "//span[@data-fieldname='lead_source']//span[@class='select2-arrow']";
	public static String associateOpportunityButton="//a[contains(text(),'Associate Opportunity')]";
	public static String saveAndConvertButton="//a[contains(text(),'Save and Convert')]";
	public static String checkLabelStatus="//span[@data-name='badge']//span[@class='detail']";
	public static String oppOwnerTextBox="//span[@data-fieldname='assigned_user_name']//input";
	public String getPrimaryAddressCountry(){return getObjectText("//span[@data-fieldname='primary_address_country]//div");}
	public String getEmail(){return getObjectText("//span[@data-fieldname='email']//a");}
	public String getLeadSource(){return getObjectText("//span[@data-fieldname='lead_source']//div");}
	public String getStatus(){return getObjectText("//span[@data-fieldname='status']//div");}
	public String getMobilePhone(){return getObjectText("//span[@data-fieldname='phone_mobile']//div");}


	public LeadToContactPage clickOnEditAndConvert() {
		click(editIcon);
		click(convertdropdown);
		return new LeadToContactPage(exec);
	}
	
	public void confirmLead(Lead lead){
		if(lead.sEmailId.length() > 0)
			Assert.assertEquals(getEmail(), lead.sEmailId, "The lead email does not match the listed email");
		if(lead.sLeadSource.length() > 0)
			Assert.assertEquals(getLeadSource(), lead.sLeadSource, "The lead source does not match the listed lead source");
		if(lead.sStatus.length() > 0)
			Assert.assertEquals(getStatus(), lead.sStatus);
		//	confirmed = false;
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

	public void enterName(String sClientBN)
	{
		System.out.println(sClientBN);
		type(clientName,sClientBN);
	}
	
	public void enterOppDescription(String sOppDesc)
	{
		type(opportunityDescription,sOppDesc);
	}
	
	public void enterOppOwner(String sOppOwner)
	{
		type(oppOwnerTextBox,sOppOwner);
	}

	public String checkLabel() {
		return getObjectText(checkLabelStatus);
	}

	public void OpenAssociateClientPanel() {
		click("//div[@data-module='Accounts']");
	}

	public void openAssociateOpporutnityPanel() {
		click("//div[@data-module='Contacs']");
	}
	
	public CreateLeadPage openEditPage(){
		click(EditButton);
		return new CreateLeadPage(exec);
	}
	
	public void deleteLead(){
		click(editDropDown);
		click(deleteOption);
		click("//a[@data-action='confirm']");
		//acceptAlert();
	}
	
	public String getLeadID(){
		String url = getCurrentURL();
		log.info("Current URL: " + url);
	      String pattern = "(.*Leads/)(.*)[^&](.*)";
	      String endOfString= "";
	      String LeadID = "";
	      // Create a Pattern object
	      Pattern r = Pattern.compile(pattern);

	      // Now create matcher object.
	      Matcher m = r.matcher(url);
	      if (m.find( )) {
	    	  endOfString = m.group(2);
	      }
	      String pattern2 ="[^&]*";
	      
	      Pattern r1 = Pattern.compile(pattern2);
	      Matcher m1 = r1.matcher(endOfString);
	      if (m1.find( )) {
	    	  LeadID = m1.group(0);
	    	  log.info("Lead found: " + LeadID);
	      }
	      return LeadID;
	}
}
