/**
 * 
 */
package com.ibm.salesconnect.test.Miscellaneous;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.IndustryPage;
import com.ibm.salesconnect.model.standard.Client.TerritoryAnalyticsPage;


/**
 * @author Eva Farrell	
 * @date Feb 27, 2014
 */
public class sISPTestTerritoryAnalyticsAndIndustry extends
		ProductBaseTest {
	Logger log = LoggerFactory.getLogger(sISPTestTerritoryAnalyticsAndIndustry.class);

	@Test(groups = {"Miscellaneous"})
	public void Test_sISPTestTerritoryAnalyticsAndIndustry() throws SQLException, InterruptedException {
		log.info("Start of test setup for sISPTestTerritoryAnalyticsAndIndustry");
		User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);

		log.info("Start of test for sISPTestTerritoryAnalyticsAndIndustry");
		
		log.info("Logging in");
		Dashboard dashboard = launchWithLogin(user1);
		
		log.info("Open Industry page & Verify");
		IndustryPage industryPage = dashboard.openIndustry();
		industryPage.isPageLoaded();
		industryPage.populateIndustryDetails("Insurance");
		industryPage.verifyIndustryDetails();
		
		log.info("Open Territory Analytics page & Verify");
		TerritoryAnalyticsPage territoryAnalyticsPage = dashboard.openTerritoryAnalytics();
		territoryAnalyticsPage.openUpSellAnalyticsTab();
		territoryAnalyticsPage.verifyUpSellAnalyticsTab();
				
		commonClientAllocator.checkInAllGroupClientWithToken(GC.DC, this);
		
		log.info("End of test method Test_sISPTestTerritoryAnalyticsAndIndustry");
	}
}
