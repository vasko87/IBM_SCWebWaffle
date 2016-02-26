/**
 * 
 */
package com.ibm.salesconnect.test.WinPlan;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.SugarAPI;
import com.ibm.salesconnect.PoolHandling.Client.PoolClient;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.partials.WinPlanTab;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Opportunity.OpportunityDetailPage;
import com.ibm.salesconnect.model.standard.Opportunity.ViewOpportunityPage;
import com.ibm.salesconnect.objects.Opportunity;

/**
 * @author timlehane
 * @date Nov 12, 2013
 */
public class WinPlanCAW extends ProductBaseTest {
	

	@Test(groups = {"WinPlan"})	
	public void Test_winPlanCAW() {
	Logger logCAW = LoggerFactory.getLogger("WinPlanCAW");

	int rand = new Random().nextInt(100000);
	String contactID = "22SC-" + rand;
	String opptyID = "33SC-" + rand;
	String RLIID = "44SC-" + rand;


	SugarAPI sugarAPI = new SugarAPI();
	Opportunity oppty = new Opportunity();
	
	User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
	PoolClient poolClient = commonClientAllocator.getGroupClient(GC.DC,this);
	String clientID = poolClient.getCCMS_ID();
	String[] opptyTeam = getMultipleUsers(2, this);
	sugarAPI.createRLISOAP(testConfig.getBrowserURL(), RLIID, user1.getEmail(), user1.getPassword());
	sugarAPI.createContact(testConfig.getBrowserURL(), contactID, clientID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");
	sugarAPI.createOpptySOAP(testConfig.getBrowserURL(), opptyID, RLIID, contactID, clientID, user1.getEmail(), user1.getPassword(), opptyTeam);
	oppty.sOpptNumber = opptyID;
	oppty.sPrimaryContact = "ContactFirst ContactLast";
	oppty.sAccID = clientID;
	
	logCAW.info("Logging in");
	Dashboard dashboard = launchWithLogin(user1);
	
	logCAW.info("Open Opportunity Details Page");
	ViewOpportunityPage viewOppPage = dashboard.openViewOpportunity();
	viewOppPage.searchForOpportunity(oppty);
	OpportunityDetailPage oppDetailPage = viewOppPage.selectResult(oppty);
	
	logCAW.info("Following oppty");
	oppDetailPage.openUpdatesTab();
	oppDetailPage.followOppty();
	
	logCAW.info("Open win plan tab");
	WinPlanTab winPlan = oppDetailPage.openWinPlanTab();
	winPlan.isPageLoaded();
	
	winPlan.setIGFOwnerIGF(true);
	winPlan.saveSummary();
	
	logCAW.info("Populate CAW section Oppty owner tasks");
	winPlan.openCAWSection();
	winPlan.isPageLoaded();
	
	winPlan.populateCAW();
	
	logCAW.info("Check oppty owner progress");
	winPlan.checkProgress(winPlan.completedPercentage,"2", "39", "5");
	winPlan.checkProgress(winPlan.completedIV,"1","11","9");
	winPlan.checkProgress(winPlan.completedVQ,"0","15","0");
	winPlan.checkProgress(winPlan.completedQGCA,"0","10","0");
	winPlan.checkProgress(winPlan.completedCAW,"1","3","33");
	
	winPlan.populateIGFCAW();
	
	logCAW.info("Check igf owner progress");
	winPlan.checkProgress(winPlan.completedPercentage,"3", "39", "7");
	winPlan.checkProgress(winPlan.completedIV,"1","11","9");
	winPlan.checkProgress(winPlan.completedVQ,"0","15","0");
	winPlan.checkProgress(winPlan.completedQGCA,"0","10","0");
	winPlan.checkProgress(winPlan.completedCAW,"2","3","66");
	
	winPlan.saveCAW();
	
	winPlan.populateLICAW();
	winPlan.saveCAWLineItem();
	
	logCAW.info("Check li owner progress");
	winPlan.checkProgress(winPlan.completedPercentage,"4", "39", "10");
	winPlan.checkProgress(winPlan.completedIV,"1","11","9");
	winPlan.checkProgress(winPlan.completedVQ,"0","15","0");
	winPlan.checkProgress(winPlan.completedQGCA,"0","10","0");
	winPlan.checkProgress(winPlan.completedCAW,"3","3","100");

	logCAW.info("Verify events");
	String[] eventList = {"marked the contract signed"};
	winPlan.navigateToOpportunityOverview();
	
	oppDetailPage.checkEvents(eventList);

	logCAW.info("Remove the oppty created for this test");
	sugarAPI.deleteOpptySOAP(testConfig.getBrowserURL(), opptyID, user1.getEmail(), user1.getPassword());
	sugarAPI.deleteContactSOAP(testConfig.getBrowserURL(), contactID, user1.getEmail(), user1.getPassword());
}
}
