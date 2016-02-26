package com.ibm.salesconnect.model.standard.Lead;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardViewPage;
import com.ibm.salesconnect.objects.Lead;

public class ViewLeadPage extends StandardViewPage {
	Logger log = LoggerFactory.getLogger(ViewLeadPage.class);


	/**
	 * @author Veena H
	 * @param exec
	 */
	public ViewLeadPage(Executor exec) {
		super(exec);
		//Assert.assertTrue(isPageLoaded(), "View Lead page has not loaded within 60 seconds");	
	}

	/* (non-Javadoc)
	 * @see com.ibm.salesconnect.model.StandardPageFrame#isPageLoaded()
	 */
	@Override
	public boolean isPageLoaded() {
		return waitForPageToLoad(pageLoaded);
	}

	//Selectors
	public static String pageLoaded = "//div[@id='content']/div/div/div/div/div[2]/div/div/div/div/div/input";
    public static String searchName="//input[@class='search-name']";
    public static String displayedLeadName = "//div[@id='content']/div/div/div/div/div[2]/div[2]/div/div[3]/div/table/tbody/tr/td[2]/span/div/a";
    public static String NextPageButton = "";
    public static String closeSearchCriterion = "//span[@class='choice-filter-close']";
    public String getLeadSelector(Lead lead) {return "//table//a[contains(text(), '" + lead.sFirstName + "')]";}
    
    
	public void searchForLead(Lead leadToContact) {
		
		clearField(searchName);
		type(searchName,leadToContact.sFullName);
		sleep(10);
		}

	public LeadDetailPage checkLead(Lead lead){
		LeadDetailPage leadDetailPage = getLeadPage(lead);
		sleep(10);
		leadDetailPage.confirmLead(lead);
		return leadDetailPage;
	}
	
	public LeadDetailPage getLeadPage(Lead lead){
		click(closeSearchCriterion);
		type(searchName, lead.sFirstName);
		sleep(10);
		click(getLeadSelector(lead));
		return new LeadDetailPage(exec);
	}
	
}
