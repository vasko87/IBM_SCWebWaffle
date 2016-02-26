/**
 * 
 */
package com.ibm.salesconnect.test.Search;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Contact.ViewContactPage;

/**
 * @author timlehane
 * @date Sep 6, 2013
 */
public class s32590SearchingPrivacyContactsGlobalSearchPopUp extends
		ProductBaseTest {
	Logger log = LoggerFactory.getLogger(s32590SearchingPrivacyContactsGlobalSearchPopUp.class);

	@Test(groups = {"Search", "BVT2"})
	public void Test_s32590SearchingPrivacyContactsGlobalSearchPopUp() {
		User user = commonUserAllocator.getGroupUser(GC.noGBL_SearchGroup,this);

		log.info("Logging in");
		Dashboard dashboard = launchWithLogin(user);
		
		log.info("Checking with privacy country and name");
		ViewContactPage viewContactPage = dashboard.openViewContact();
		viewContactPage.setSearchTypeToBasic();
		viewContactPage.clearSearchForm();
		viewContactPage.enterSearchTerm("a");
		viewContactPage.selectCountry("Germany");
		viewContactPage.clickSearchButton();
		Assert.assertTrue(viewContactPage.isPrivacyPopUpPresent(), "Privacy pop up is not present when searching with name in privacy country");
		viewContactPage.closePrivacyPopUp();
		
		log.info("Checking with privacy country, name and email");
		viewContactPage.enterAnyEmail("a");
		viewContactPage.clickSearchButton();
		Assert.assertTrue(viewContactPage.isPrivacyPopUpPresent(), "Privacy pop up is not present when searching with name and email in privacy country");
		viewContactPage.closePrivacyPopUp();
		
		log.info("Checking with privacy country and email");
		viewContactPage.clearSearchTerm();
		viewContactPage.clickSearchButton();
		Assert.assertTrue(viewContactPage.isPrivacyPopUpPresent(), "Privacy pop up is not present when searching with email in privacy country");
		viewContactPage.closePrivacyPopUp();
		
		log.info("Checking with all countries and name");
		viewContactPage.clearSearchForm();
		viewContactPage.enterSearchTerm("a");
		viewContactPage.selectCountry("All countries");
		viewContactPage.clickSearchButton();
		Assert.assertTrue(viewContactPage.isPrivacyPopUpPresent(), "Privacy pop up is not present when searching with name in all countries");
		viewContactPage.closePrivacyPopUp();
		
		log.info("Checking with all countries, name and email");
		viewContactPage.enterAnyEmail("a");
		viewContactPage.clickSearchButton();
		Assert.assertTrue(viewContactPage.isPrivacyPopUpPresent(), "Privacy pop up is not present when searching with name and email in all countries");
		viewContactPage.closePrivacyPopUp();
		
		log.info("Checking with all countries and email");
		viewContactPage.clearSearchTerm();
		viewContactPage.clickSearchButton();
		Assert.assertTrue(viewContactPage.isPrivacyPopUpPresent(), "Privacy pop up is not present when searching with email in all countries");
		viewContactPage.closePrivacyPopUp();
	}

}
