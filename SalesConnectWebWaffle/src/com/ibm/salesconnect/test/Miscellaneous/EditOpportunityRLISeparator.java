package com.ibm.salesconnect.test.Miscellaneous;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.SugarAPI;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.partials.LineItemDetailPage;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.LineItems.EditLineItem;
import com.ibm.salesconnect.model.standard.Opportunity.OpportunityDetailPage;
import com.ibm.salesconnect.model.standard.Opportunity.ViewOpportunityPage;
import com.ibm.salesconnect.objects.Opportunity;
import com.ibm.salesconnect.objects.RevenueItem;



/**
 * Description   : [R1.2] Verify the recommended doc ranking for Sales kit types are raised to top when doc matches L20 product level of oppty.
 * @author SCole
 * @date 13/02/14
 */

public class EditOpportunityRLISeparator extends ProductBaseTest {

	Logger log = LoggerFactory.getLogger(EditOpportunityRLISeparator.class);

	int rand = new Random().nextInt(100000);
	String contactID = "22SC-" + rand;
	String opptyID = null;
	String clientID = null;
	SugarAPI sugarAPI = new SugarAPI();
	Opportunity oppt = new Opportunity();
	RevenueItem rli = new RevenueItem();

	@Test(groups = { "Miscellaneous"})
	public void test_EditOpportunityRLISeparator() {

		log.info("Start of test method EditOpportunityRLI");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup, this);
		clientID = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();

		sugarAPI.createContact(testConfig.getBrowserURL(), contactID, clientID, user1.getEmail(), user1.getPassword(), "ContactFirst", "ContactLast");

		log.info("Create L40 RLI (i.e. offering 'Level 40: Miscellaneous - Other/Unk IBM HW (483813V)' " ); 
		String sUrl = testConfig.getBrowserURL();
		String sRliID = "44SC-" + rand;
		sugarAPI.createRLI(sUrl, sRliID, user1.getEmail(), user1.getPassword(), "level40","483813V");

		opptyID = sugarAPI.createOppty(testConfig.getBrowserURL(),  sRliID, contactID, clientID, user1.getEmail(), user1.getPassword(), GC.emptyArray);
		log.info("Creating oppty (with RLI): " + opptyID.toString());
		oppt.sOpptNumber = opptyID;
		oppt.sPrimaryContact = "ContactFirst ContactLast";
		oppt.sAccID = clientID;

		try {			
			log.info("Logging in");
			Dashboard dashboard = launchWithLogin(user1);
			//launchWithoutLogin();

			log.info("Open Opportunity detail page");
			ViewOpportunityPage viewOpportunityPage = dashboard.openViewOpportunity();
			viewOpportunityPage.searchForOpportunity(oppt);
			OpportunityDetailPage opportunityDetailPage = viewOpportunityPage.selectResult(oppt);

			log.info("This test is for defect 42840");
			log.info("Edit Opportunity Line Item Amount & verify you can use . or , as preferences separator");
			
			log.info("Use ,(comma) as the separator in the Line Item amount");
			String sTotalAmount = ("100k USD");			
			log.info("Update RLI");
			RevenueItem rli = new RevenueItem();
			rli.sRevenueAmount = "100,000";
			LineItemDetailPage editLineItem = opportunityDetailPage.selectEditRli();
			editLineItem.editLineItemInfo(rli);
			opportunityDetailPage.isPageLoaded();				
			String sGetDisplayTotalAmount = opportunityDetailPage.getdisplayedTotalAmount();		
			log.info("comma as separator 'Total amount should = " + sTotalAmount + "  Amount actually displayed = " + sGetDisplayTotalAmount);				
			Assert.assertTrue(opportunityDetailPage.getdisplayedTotalAmount().equals(sTotalAmount), "Opportunity Detail Page 'Total amount' did not equal " + sTotalAmount); 


			log.info("Use .(period) as the separator in the Line Item amount");
			log.info("Update RLI");
			rli.sRevenueAmount = "100000.00";
			EditLineItem editLineItem2 =opportunityDetailPage.clickOnEditRli();
			editLineItem2.editLineItemInfo(rli);
			exec.executeScript("window.scrollBy(0,450)", "");
			opportunityDetailPage = editLineItem2.saveLineItem();
			opportunityDetailPage.isPageLoaded();				
			sGetDisplayTotalAmount = opportunityDetailPage.getdisplayedTotalAmount();				
			log.info("period as separator 'Total amount should = " + sTotalAmount + "  Amount actually displayed = " + sGetDisplayTotalAmount);
			Assert.assertTrue(opportunityDetailPage.getdisplayedTotalAmount().equals(sTotalAmount), "Opportunity Detail Page 'Total amount' did not equal " + sTotalAmount); 

			exec.close();	

		} catch (Exception e) {
			e.printStackTrace();
							log.info("Remove the oppty created for this test");
							sugarAPI.deleteOpptySOAP(testConfig.getBrowserURL(), opptyID, user1.getEmail(), user1.getPassword());
							sugarAPI.deleteContactSOAP(testConfig.getBrowserURL(), contactID, user1.getEmail(), user1.getPassword());
							Assert.assertTrue(false);
		}
		sugarAPI.deleteContactSOAP(testConfig.getBrowserURL(), contactID, user1.getEmail(), user1.getPassword());
		log.info("End of test method EditOpportunityRLISeparator");
	}
}