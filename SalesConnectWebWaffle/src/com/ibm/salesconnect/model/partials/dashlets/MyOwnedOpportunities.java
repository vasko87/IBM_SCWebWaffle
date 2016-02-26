/**
 * 
 */
package com.ibm.salesconnect.model.partials.dashlets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardPageFrame;
import com.ibm.salesconnect.model.standard.Opportunity.OpportunityDetailPage;

/**
 * @author louisemoloney
 * @date Sept 24, 2013
 */
public class MyOwnedOpportunities extends StandardPageFrame {
	Logger log = LoggerFactory.getLogger(MyOwnedOpportunities.class);

	/**
	 * @param exec
	 */
	public MyOwnedOpportunities(Executor exec) {
		super(exec);
		Assert.assertTrue(isPageLoaded(), "Home page has not loaded within 60 seconds");
	}

	/* (non-Javadoc)
	 * @see com.ibm.salesconnect.model.StandardPageFrame#isPageLoaded()
	 */
	@Override
	public boolean isPageLoaded() {
		scrollToBottomOfPage();
		return waitForPageToLoad(pageLoaded);
	}


	//Selectors
	public static String pageLoaded = "//a[contains(text(),'Back to top')]";
	public static String sDashletTitle = "//span[contains(text(),'My owned opportunities')]/ancestor::*[contains(@id,'dashlet_entire')]";
	public static String sDashletEntry = "";
	//public static String home = "https://svt5lb01a.rtp.raleigh.ibm.com/sales/salesconnect/";
	public static String home = "//a[@id='moduleTab_AllHome']";
	public static String editRLI = "//a[@id='opportun_revenuelineitems_edit_1']";
	public static String editWinPlan = "//em[contains(text(),'Win Plan')]";

	
	/**
	 * Return WebLink Object containing dashlet entry. 
	 * @parm sDashletTitle String containing the header name of dashlet
	 * @return WebLink Object containing dashlet entry
	 */
	public static String getLink_Dashlet(String sDashletEntry){
		return ("//span[contains(text(),'My owned opportunities')]/ancestor::*[contains(@id,'dashlet_entire')]/descendant::tr[descendant::*[contains(., '" + sDashletEntry + "')] ]/descendant::a[contains(text(),'" + sDashletEntry + "') and contains(@href,'DetailView')]");
		
	}
	
	/**
	 * Return true if a dashlet with the specified title exists on the current page
	 * @parm sDashletTitle String containing the header name of dashlet
	 * @return boolean true for present, false for not present
	 */
	public boolean verifyDashletExists(String sDashletTitle){
		if (!isPresent(sDashletTitle)){
			log.info("Could not find a dashlet: My owned opportunities");
			return false; //could not find the dashlet
		}		
		return true;
	}

	//Click on oppty from owned oppties dashlet
	public String getOpptyFromRow(int row){
		row+=2;
		return "//span[contains(text(),'My owned opportunities')]/../../../../../../../..//tr[position()=" + row + "]//td[position()=1]/b/a";
	}

	public OpportunityDetailPage openOpptyInMyOpenOpptysDashlet(int row){
	click(getOpptyFromRow(row));
	OpportunityDetailPage odp = new OpportunityDetailPage(exec);
	return odp;
	}

}
