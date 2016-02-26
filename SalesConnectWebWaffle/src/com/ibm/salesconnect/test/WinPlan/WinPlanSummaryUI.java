
package com.ibm.salesconnect.test.WinPlan;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.APIUtilities;
import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.API.OpportunityRestAPI;
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
 * @date Nov 4, 2013
 */
public class WinPlanSummaryUI extends ProductBaseTest {

	@Test(groups = {"WinPlan","BVT","BVT1"})
	public void Test_WinPlanSummaryUI() {
		Logger log = LoggerFactory.getLogger("Test_WinPlanSummaryUI");
		log.info("Start of test method Test_WinPlanSummaryUI");

			int rand = new Random().nextInt(100000);
			String contactID = "22SC-" + rand;
			String RLIID = "44SC-" + rand;
			PoolClient poolClient = null;
			String clientID = null;
			SugarAPI sugarAPI = new SugarAPI();
			Opportunity oppty = new Opportunity();

			User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
			String userName = user1.getDisplayName();
			poolClient = commonClientAllocator.getGroupClient(GC.SC,this);
			clientID = poolClient.getCCMS_ID();
			String URL = testConfig.getBrowserURL();
			LoginRestAPI loginRestAPI = new LoginRestAPI();
			String token = loginRestAPI.getOAuth2Token(URL, user1.getEmail(), user1.getPassword());

			String realClientId = new APIUtilities().getClientBeanIDFromCCMSID(URL, clientID, user1.getEmail(), user1.getPassword());
			String headers[]={"OAuth-Token", token};
			String[] opptyTeam = getMultipleUsers(2, this);
			//sugarAPI.createRLI(testConfig.getBrowserURL(), RLIID, user1.getEmail(), user1.getPassword(), "level20", "BD330");
			sugarAPI.createContact(testConfig.getBrowserURL(), contactID, clientID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");
			String opptyID = new OpportunityRestAPI().createOpportunityWithRLIreturnBean(testConfig.getBrowserURL(), token, "Oppty created description", realClientId, contactID, "SLSP", "03", "2013-10-28", sugarAPI.getUserID(user1.getEmail(), URL, headers), user1.getDisplayName());
			oppty.sOpptNumber = opptyID;
			oppty.sPrimaryContact = "ContactFirst ContactLast";
			oppty.sAccID = clientID;

		log.info("Logging in");
		Dashboard dashboard = launchWithLogin(user1);

		log.info("Open Opportunity Details Page");
		ViewOpportunityPage viewOppPage = dashboard.openViewOpportunity();
		viewOppPage.searchForOpportunity(oppty);
		OpportunityDetailPage oppDetailPage = viewOppPage.selectResult(oppty);
		WinPlanTab winPlan = oppDetailPage.openWinPlanTab();

		log.info("Check completed percentage before starting");
		winPlan.checkProgress(winPlan.completedPercentage,"0","30","0");
		
	
		winPlan.checkProgress(winPlan.completedIV,"0","6","0");
		winPlan.checkProgress(winPlan.completedVQ,"0","16","0");
		winPlan.checkProgress(winPlan.completedQGCA,"0","7","0");
		winPlan.checkProgress(winPlan.completedCAW,"0","1","0");

		log.info("Complete Summary section");
		winPlan.addIBMExecSponsor(userName);
		winPlan.addIBMBrandSponsor(userName);
		winPlan.addIBMSDSponsor(userName);
		winPlan.setOwnerIGF(true);
		winPlan.setInternationalDeal(true);
		winPlan.setIGFOwnerIGF(true);
		winPlan.addIBMExec(userName);

		log.info("Check that progress has not changed after both owners add IGF but have not saved");
		winPlan.checkProgress(winPlan.completedPercentage,"0","30","0");
		winPlan.checkProgress(winPlan.completedIV,"0","6","0");
		winPlan.checkProgress(winPlan.completedVQ,"0","16","0");
		winPlan.checkProgress(winPlan.completedQGCA,"0","7","0");
		winPlan.checkProgress(winPlan.completedCAW,"0","1","0");

		winPlan.saveSummary();

		log.info("Check that progress has changed after save");
		winPlan.checkProgress(winPlan.completedPercentage,"1", "47", "2");
		winPlan.checkProgress(winPlan.completedIV,"1","13","7");
		winPlan.checkProgress(winPlan.completedVQ,"0","20","0");
		winPlan.checkProgress(winPlan.completedQGCA,"0","11","0");
		winPlan.checkProgress(winPlan.completedCAW,"0","3","0");


		log.info("Remove the oppty created for this test");
		sugarAPI.deleteOpptySOAP(testConfig.getBrowserURL(), opptyID, user1.getEmail(), user1.getPassword());
		sugarAPI.deleteContactSOAP(testConfig.getBrowserURL(), contactID, user1.getEmail(), user1.getPassword());
		}	
}
 