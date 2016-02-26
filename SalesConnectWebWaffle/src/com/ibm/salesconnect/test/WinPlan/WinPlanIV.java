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
public class WinPlanIV extends ProductBaseTest {

	@Test(groups = {"WinPlan"})	
	public void Test_winPlanIV() {
		Logger logIV = LoggerFactory.getLogger("Test_winPlanIV");

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
			
			logIV.info("Logging in");
			Dashboard dashboard = launchWithLogin(user1);
			
			logIV.info("Open Opportunity Details Page");
			ViewOpportunityPage viewOppPage = dashboard.openViewOpportunity();
			viewOppPage.searchForOpportunity(oppty);
			viewOppPage.isPageLoaded();
			OpportunityDetailPage oppDetailPage = viewOppPage.selectResult(oppty);
			
			logIV.info("Following oppty");
			oppDetailPage.openUpdatesTab();
			oppDetailPage.followOppty();
			
			logIV.info("Open win plan tab");
			WinPlanTab winPlan = oppDetailPage.openWinPlanTab();
			
			winPlan.setIGFOwnerIGF(true);
			winPlan.saveSummary();
			
			logIV.info("Populate IV section Oppty owner tasks");
			winPlan.openIVSection();
			winPlan.setClientNeeds();
			winPlan.setSOW();
			winPlan.setUniqueBusValue("high");
			winPlan.setUniqueBusValueDesc("Better functionality proven IBM solution");
			winPlan.populateIVOtherComments("IV comment");
			
			logIV.info("Check oppty owner progress");
			winPlan.checkProgress(winPlan.completedPercentage,"5", "39", "12");
			winPlan.checkProgress(winPlan.completedIV,"5","11","45");
			winPlan.checkProgress(winPlan.completedVQ,"0","15","0");
			winPlan.checkProgress(winPlan.completedQGCA,"0","10","0");
			winPlan.checkProgress(winPlan.completedCAW,"0","3","0");
			
			logIV.info("Populate IV section Oppty owner tasks");
			winPlan.setClientFinancing();
			winPlan.setCreditCheck();
			winPlan.selectIGFBusinessValue("Align costs with anticipated benefits");
			winPlan.setFinancingProposal();
			winPlan.setFinancingCompetition("AIB");
			winPlan.selectIGFDirectFinancing("Financial Decision Maker (e.g. CFO, Treasurer)");
			winPlan.populateIVIGFOtherComments("IV IGF Other Comments");
			winPlan.saveIV();
			
			logIV.info("Check IGF owner progress");
			winPlan.checkProgress(winPlan.completedPercentage,"11", "39", "28");
			winPlan.checkProgress(winPlan.completedIV,"11","11","100");
			winPlan.checkProgress(winPlan.completedVQ,"0","15","0");
			winPlan.checkProgress(winPlan.completedQGCA,"0","10","0");
			winPlan.checkProgress(winPlan.completedCAW,"0","3","0");
			
			logIV.info("Complete IV line Item");
			winPlan.openIVEditLineItem();
			winPlan.populateIVLineItemOtherComments("IV Line Item");
			winPlan.saveIVLineItem();
			
			logIV.info("Check Line Item owner progress");
			winPlan.checkProgress(winPlan.completedPercentage,"11", "39", "28");
			winPlan.checkProgress(winPlan.completedIV,"11","11","100");
			winPlan.checkProgress(winPlan.completedVQ,"0","15","0");
			winPlan.checkProgress(winPlan.completedQGCA,"0","10","0");
			winPlan.checkProgress(winPlan.completedCAW,"0","3","0");
			
			logIV.info("Verify events");
			String[] eventList = {"identified client needs", "completed the proposal", "completed credit check"};
			winPlan.navigateToOpportunityOverview();
			
			oppDetailPage.checkEvents(eventList);

			logIV.info("Remove the oppty created for this test");
			sugarAPI.deleteOpptySOAP(testConfig.getBrowserURL(), opptyID, user1.getEmail(), user1.getPassword());
			sugarAPI.deleteContactSOAP(testConfig.getBrowserURL(), contactID, user1.getEmail(), user1.getPassword());
		}
	
	
}
