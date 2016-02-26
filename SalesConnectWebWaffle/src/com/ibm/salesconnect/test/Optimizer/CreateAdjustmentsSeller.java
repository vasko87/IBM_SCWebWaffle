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
import com.ibm.salesconnect.model.standard.Forecast.Seller.ViewSellerForecastPage;
import com.ibm.salesconnect.objects.Adjustment;


public class CreateAdjustmentsSeller extends ProductBaseTest{
	Logger log = LoggerFactory.getLogger(CreateAdjustmentsSeller.class);
	
	@Test
	public void test_CreateAdjustmentsSeller() throws Exception{
		log.info("Start of test method CreateAdjustmentsSeller");		
		User user1 = commonUserAllocator.getGroupUser(GC.gbsSel, this);
		Dashboard dashboard = launchWithLogin(user1);
		ViewSellerForecastPage viewSellerForecast =  dashboard.openSellerForecastView();
		viewSellerForecast.selectTimePeriod(1);
		viewSellerForecast.selectTransactional();			
		CreateAdjustmentPage createAdj = dashboard.clickCreateAdjButton();	
		Adjustment adj = Adjustment.getInstance();	
		Map<String, String> adjMap = createAdj.enterAdjustmentInfo(adj,"seller");		
		Assert.assertTrue(createAdj.verifyCreatedAdjustment(adjMap, "seller"), "Verify failed, the adjustment is not created successfully...");		
	}
}
