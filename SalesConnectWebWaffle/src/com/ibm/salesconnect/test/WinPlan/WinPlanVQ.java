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
public class WinPlanVQ extends ProductBaseTest {

	@Test(groups = {"WinPlan"})	
	public void Test_winPlanVQ() {
		Logger logVQ = LoggerFactory.getLogger("Test_winPlanVQ");

			int rand = new Random().nextInt(100000);
			String contactID = "22SC-" + rand;
			String opptyID = "33SC-" + rand;
			String RLIID = "44SC-" + rand;
			PoolClient poolClient = null;
			String clientID = null;
			SugarAPI sugarAPI = new SugarAPI();
			Opportunity oppty = new Opportunity();
			
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
			String userName = user1.getDisplayName();
			poolClient = commonClientAllocator.getGroupClient(GC.DC,this);
			clientID = poolClient.getCCMS_ID();
			String[] opptyTeam = getMultipleUsers(2, this);
			sugarAPI.createRLISOAP(testConfig.getBrowserURL(), RLIID, user1.getEmail(), user1.getPassword());
			sugarAPI.createContact(testConfig.getBrowserURL(), contactID, clientID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");
			sugarAPI.createOpptySOAP(testConfig.getBrowserURL(), opptyID, RLIID, contactID, clientID, user1.getEmail(), user1.getPassword(), opptyTeam);
			oppty.sOpptNumber = opptyID;
			oppty.sPrimaryContact = "ContactFirst ContactLast";
			oppty.sAccID = clientID;
			
			logVQ.info("Logging in");
			Dashboard dashboard = launchWithLogin(user1);
			
			logVQ.info("Open Opportunity Details Page");
			ViewOpportunityPage viewOppPage = dashboard.openViewOpportunity();
			viewOppPage.searchForOpportunity(oppty);
			viewOppPage.isPageLoaded();
			OpportunityDetailPage oppDetailPage = viewOppPage.selectResult(oppty);
			
			logVQ.info("Following oppty");
			oppDetailPage.openUpdatesTab();
			oppDetailPage.followOppty();
			
			logVQ.info("Open win plan tab");
			WinPlanTab winPlan = oppDetailPage.openWinPlanTab();
			
			winPlan.setIGFOwnerIGF(true);
			winPlan.saveSummary();
			
			logVQ.info("Populate VQ section Oppty owner tasks");
			winPlan.openVQSection();
			
			winPlan.populateVQOpptyOwnerSection(userName);
			
			logVQ.info("Check oppty owner progress");
			winPlan.checkProgress(winPlan.completedPercentage,"12", "39", "30");
			winPlan.checkProgress(winPlan.completedIV,"1","11","9");
			winPlan.checkProgress(winPlan.completedVQ,"11","15","73");
			winPlan.checkProgress(winPlan.completedQGCA,"0","10","0");
			winPlan.checkProgress(winPlan.completedCAW,"0","3","0");
			
			winPlan.populateVQIGFOwnerSection(userName);
			
			logVQ.info("Check igf owner progress");
			winPlan.checkProgress(winPlan.completedPercentage,"15", "39", "38");
			winPlan.checkProgress(winPlan.completedIV,"1","11","9");
			winPlan.checkProgress(winPlan.completedVQ,"14","15","93");
			winPlan.checkProgress(winPlan.completedQGCA,"0","10","0");
			winPlan.checkProgress(winPlan.completedCAW,"0","3","0");
			
			winPlan.saveVQ();
			
			winPlan.populateVQLI();
			winPlan.saveVQLineItem();
			
			logVQ.info("Check li owner progress");
			winPlan.checkProgress(winPlan.completedPercentage,"16", "39", "41");
			winPlan.checkProgress(winPlan.completedIV,"1","11","9");
			winPlan.checkProgress(winPlan.completedVQ,"15","15","100");
			winPlan.checkProgress(winPlan.completedQGCA,"0","10","0");
			winPlan.checkProgress(winPlan.completedCAW,"0","3","0");
			
			logVQ.info("Verify events");
			String[] eventList = {"updated access to power to", "created initial benefits", "confirmed client decision performance", "marked RFP released", 
					"Winplan progress now"};
			winPlan.navigateToOpportunityOverview();
			
			oppDetailPage.checkEvents(eventList);

			logVQ.info("Remove the oppty created for this test");
			sugarAPI.deleteOpptySOAP(testConfig.getBrowserURL(), opptyID, user1.getEmail(), user1.getPassword());
			sugarAPI.deleteContactSOAP(testConfig.getBrowserURL(), contactID, user1.getEmail(), user1.getPassword());
	}	
}
