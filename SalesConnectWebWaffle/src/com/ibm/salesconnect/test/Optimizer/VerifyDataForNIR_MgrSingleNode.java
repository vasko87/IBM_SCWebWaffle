/**
 * @author Wen xu	
 * @date Nov 05, 2015
 */
package com.ibm.salesconnect.test.Optimizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Forecast.RoadmapSummaryPage;
import com.ibm.salesconnect.model.standard.Forecast.Manager.ViewMgrForecastPage;

public class VerifyDataForNIR_MgrSingleNode extends ProductBaseTest{
	Logger log = LoggerFactory.getLogger(CreateAdjustmentsMgr.class);
	
	@Test
	public void test_VerifyDataForNIR_MgrSingleNode() throws Exception{
		log.info("Start of test method test_VerifyDataForNIR_MgrSingleNode");		
		User user1 = commonUserAllocator.getGroupUser(GC.gbsMgr, this);		
		Dashboard dashboard = launchWithLogin(user1);
		ViewMgrForecastPage viewMgrForecast = dashboard.openMgrForecastView();
		viewMgrForecast.selectNodeUser("F01 (F01) MANAGER_GBS");
		viewMgrForecast.selectTimePeriod(1);
		viewMgrForecast.selectForecastType("Transactional");	
		RoadmapSummaryPage sumPage = RoadmapSummaryPage.getInstance();		
		Assert.assertTrue(sumPage.verifyRoadmapStatusSum("NIR"), "The total amount is not matched, verified fail...");
	}
}
