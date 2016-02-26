package com.ibm.salesconnect.test.Level1;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.APIUtilities;
import com.ibm.salesconnect.API.LeadsRestAPI;
import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Lead.LeadDetailPage;
import com.ibm.salesconnect.model.standard.Lead.LeadToContactPage;
import com.ibm.salesconnect.model.standard.Lead.ViewLeadPage;
import com.ibm.salesconnect.objects.Lead;
/**
 * <strong>Description:</strong>
 * <br/><br/>
 * Test to validate the Create a lead, Search for the lead & Convert the lead to Contact functionality of the Leads module
 * <br/><br/>
 * 
 * @author 
 * Veena Hurukadli
 * 

 */
public class AT_LeadToContact extends ProductBaseTest {
	Logger log = LoggerFactory.getLogger(AT_Sugar.class);

	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Open browser and log in</li>
	 * <li>Create Lead</li>
	 * <li>Search for created Lead</li>
	 * <li>Open Call detail page</li>
	 * <li>Convert lead to a Contact</li>
	 * <li>Search for Converted lead</li>
	 * </ol>
	 */

	@Test(groups = { "Level1","AT_Sugar","BVT","BVT1"})
	public void Test_AT_LeadToContact() throws SQLException{

		log.info("Start of test method Test_AT_LeadToContact");

		User user1 = commonUserAllocator.getUser(this);
		Dashboard dashboard = launchWithLogin(user1);
		Lead leadToContact = new Lead();
		leadToContact.populate();
		leadToContact.sAssignedUserID = new APIUtilities().getUserBeanIDFromEmail(baseURL, user1.getEmail(), user1.getPassword());
		log.info("Create the lead");
		new LeadsRestAPI().createLeadfromLead(testConfig.getBrowserURL(), new LoginRestAPI().getOAuth2Token(baseURL, user1.getEmail(), user1.getPassword()), leadToContact);

		log.info("Search for created Lead");
		ViewLeadPage viewLeadPage = dashboard.openViewLead();

		LeadDetailPage leadDetailPage = viewLeadPage.getLeadPage(leadToContact);
		LeadToContactPage leadToContactPage = leadDetailPage.clickOnEditAndConvert();
		leadToContactPage.clickAssociateContact();
		leadToContactPage.clickAssociateClient(leadToContact);
		leadToContactPage.clickOnSave();
		sleep(10);
		Assert.assertEquals(leadDetailPage.checkLabel(),leadToContact.sConvertedStatus,"Incorrect lead name displayed");
		log.info("End of test method Test_AT_Lead");
		log.info("End of test method Test_AT_FavoriteClient");
	}
}