package com.ibm.salesconnect.test.Level0;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.SugarAPI;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.LineItems.EditLineItem;
import com.ibm.salesconnect.model.standard.Opportunity.OpportunityDetailPage;
import com.ibm.salesconnect.model.standard.Opportunity.ViewOpportunityPage;
import com.ibm.salesconnect.objects.Opportunity;
import com.ibm.salesconnect.objects.RevenueItem;

/**
 * <strong>Description:</strong>
 * <br/><br/>
 * Test to validate the create, read, update and delete functionality of the RLI module
 * <br/><br/>
 * 
 * @author 
 * Tim Lehane
 * 
 */
public class AT_RLI extends ProductBaseTest {
	Logger log = LoggerFactory.getLogger(AT_RLI.class);

	User user1 = null;
	String clientID = null;
	String opptyID = null;
	Opportunity oppty = new Opportunity();
	RevenueItem rli = new RevenueItem();	
	
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Create oppty via api</li>
	 * <li>Open browser and log in</li>
	 * <li>Open opportunity and create rli</li>
	 * <li>Verify RLI created</li>
	 * <li>Edit the created rli</li>
	 * <li>Verify RLI edited</li>
	 * <li>Delete the rli</li>
	 * <li>Verify RLI deleted</li>
	 * </ol>
	 */	
	@Test(groups = {"Miscellaneous","BVT","AT", "Sidecar","BVT0"})
	public void Test_AT_RLI(){
			log.info("Starting test set up");
		user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
		clientID = commonClientAllocator.getGroupClient(GC.SC,this).getCCMS_ID();
		SugarAPI sugarAPI = new SugarAPI();
		String URL = testConfig.getBrowserURL();

		oppty.populate();
		rli.populate();
		rli.sFindOffering = GC.gsLenovo;
		rli.setRLIAmount("25000");
		
		int rand = new Random().nextInt(99999);
		String contactID = "22SC-" + rand;
		String opptyID = null;
			
		sugarAPI.createContact(URL, contactID, clientID, user1.getEmail(), user1.getPassword(), "ContactFirst_"+rand, "ContactLast_"+rand);
		opptyID = sugarAPI.createOppty(URL, "", contactID, clientID, user1.getEmail(), user1.getPassword(), GC.emptyArray);
		oppty.sOpptNumber = opptyID;
		oppty.sPrimaryContact = "ContactFirst ContactLast";
		oppty.sAccID = clientID;
		log.info("Finished test set up");
		
		log.info("Logging in");
		Dashboard dashboard = launchWithLogin(user1);
		
		log.info("Open Opportunity detail page");
		ViewOpportunityPage viewOpportunityPage = dashboard.openViewOpportunity();
		OpportunityDetailPage opportunityDetailPage = viewOpportunityPage.selectResultfromID(oppty);
		
		EditLineItem editLineItem = opportunityDetailPage.selectCreateRli();
		editLineItem.editLineItemInfo(rli);
		opportunityDetailPage = editLineItem.saveLineItem();
		opportunityDetailPage.verifyLineItemCreated(rli);
		
		
		rli.setRLIAmount("26000");
		log.info("Edit rli");
		opportunityDetailPage.EditRli(rli);
		editLineItem.saveInlineLineItem();
		opportunityDetailPage.verifyLineItemEdited(rli);
		
		log.info("Delete rli");
		opportunityDetailPage.deleteRLI(1);
		opportunityDetailPage.verifyLineItemDeleted(rli);
	}
}
