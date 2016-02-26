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
public class WinPlanQGCP extends ProductBaseTest {

	@Test(groups = {"WinPlan"})	
	public void Test_winPlanQGCP() {
		Logger logQGCP = LoggerFactory.getLogger("Test_winPlanQGCP");

			int rand = new Random().nextInt(100000);
			String contactID = "22SC-" + rand;
			String opptyID = "33SC-" + rand;
			String RLIID = "44SC-" + rand;
			PoolClient poolClient = null;
			String clientID = null;
			SugarAPI sugarAPI = new SugarAPI();
			Opportunity oppty = new Opportunity();
			
			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
			poolClient = commonClientAllocator.getGroupClient(GC.DC,this);
			clientID = poolClient.getCCMS_ID();
			String[] opptyTeam = getMultipleUsers(2, this);
			sugarAPI.createRLISOAP(testConfig.getBrowserURL(), RLIID, user1.getEmail(), user1.getPassword());
			sugarAPI.createContact(testConfig.getBrowserURL(), contactID, clientID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");
			sugarAPI.createOpptySOAP(testConfig.getBrowserURL(), opptyID, RLIID, contactID, clientID, user1.getEmail(), user1.getPassword(), opptyTeam);
			oppty.sOpptNumber = opptyID;
			oppty.sPrimaryContact = "ContactFirst ContactLast";
			oppty.sAccID = clientID;
			
			logQGCP.info("Logging in");
			Dashboard dashboard = launchWithLogin(user1);
			
			logQGCP.info("Open Opportunity Details Page");
			ViewOpportunityPage viewOppPage = dashboard.openViewOpportunity();
			viewOppPage.searchForOpportunity(oppty);
			viewOppPage.isPageLoaded();
			OpportunityDetailPage oppDetailPage = viewOppPage.selectResult(oppty);
			
			logQGCP.info("Following oppty");
			oppDetailPage.openUpdatesTab();
			oppDetailPage.followOppty();
			
			logQGCP.info("Open win plan tab");
			WinPlanTab winPlan = oppDetailPage.openWinPlanTab();
			
			winPlan.setIGFOwnerIGF(true);
			winPlan.saveSummary();
			
			logQGCP.info("Populate QGCP section Oppty owner tasks");
			winPlan.openQGCPSection();
			
			winPlan.populateQGCP();
			
			logQGCP.info("Check oppty owner progress");
			winPlan.checkProgress(winPlan.completedPercentage,"7", "39", "17");
			winPlan.checkProgress(winPlan.completedIV,"1","11","9");
			winPlan.checkProgress(winPlan.completedVQ,"0","15","0");
			winPlan.checkProgress(winPlan.completedQGCA,"6","10","60");
			winPlan.checkProgress(winPlan.completedCAW,"0","3","0");
			
			winPlan.populateIGFQGCP();
			
			logQGCP.info("Check igf owner progress");
			winPlan.checkProgress(winPlan.completedPercentage,"10", "39", "25");
			winPlan.checkProgress(winPlan.completedIV,"1","11","9");
			winPlan.checkProgress(winPlan.completedVQ,"0","15","0");
			winPlan.checkProgress(winPlan.completedQGCA,"9","10","90");
			winPlan.checkProgress(winPlan.completedCAW,"0","3","0");
			
			winPlan.saveQGCP();
			
			winPlan.populateQGCPLI();
			winPlan.saveQGCPLineItem();
			
			logQGCP.info("Check li owner progress");
			winPlan.checkProgress(winPlan.completedPercentage,"11", "39", "28");
			winPlan.checkProgress(winPlan.completedIV,"1","11","9");
			winPlan.checkProgress(winPlan.completedVQ,"0","15","0");
			winPlan.checkProgress(winPlan.completedQGCA,"10","10","100");
			winPlan.checkProgress(winPlan.completedCAW,"0","3","0");
			
			logQGCP.info("Verify events");
			String[] eventList = {"updated configuration / tech solution", "confirmed resources and skill alignment"};
			winPlan.navigateToOpportunityOverview();
			
			oppDetailPage.checkEvents(eventList);

			logQGCP.info("Remove the oppty created for this test");
			sugarAPI.deleteOpptySOAP(testConfig.getBrowserURL(), opptyID, user1.getEmail(), user1.getPassword());
			sugarAPI.deleteContactSOAP(testConfig.getBrowserURL(), contactID, user1.getEmail(), user1.getPassword());
	}	
}
