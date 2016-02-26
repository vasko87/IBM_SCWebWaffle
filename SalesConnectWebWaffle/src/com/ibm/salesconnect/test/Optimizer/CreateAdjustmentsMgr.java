package com.ibm.salesconnect.test.Optimizer;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Forecast.CreateAdjustmentPage;
import com.ibm.salesconnect.model.standard.Forecast.Manager.ViewMgrForecastPage;
import com.ibm.salesconnect.objects.Adjustment;


public class CreateAdjustmentsMgr extends ProductBaseTest{
	Logger log = LoggerFactory.getLogger(CreateAdjustmentsMgr.class);
	
	@Test
	public void test_CreateAdjustmentsMgr() throws Exception{
		log.info("Start of test method CreateAdjustmentsMgr");		
		User user1 = commonUserAllocator.getGroupUser(GC.gbsMgr, this);		
		Dashboard dashboard = launchWithLogin(user1);
		ViewMgrForecastPage viewMgrForecast = dashboard.openMgrForecastView();
		viewMgrForecast.selectNodeUser("F01 (F01) MANAGER_GBS");
		viewMgrForecast.selectTimePeriod(1);
		viewMgrForecast.selectForecastType("Transactional");			
		CreateAdjustmentPage createAdjPage = dashboard.clickCreateAdjButton();			
		Adjustment adj = Adjustment.getInstance();		  
		Map<String, String> adjMap = createAdjPage.enterAdjustmentInfo(adj,"manager");		
		Assert.assertTrue(createAdjPage.verifyCreatedAdjustment(adjMap, "manager"), "Verify failed, adjustment is not created successfully...");		
	}
}
