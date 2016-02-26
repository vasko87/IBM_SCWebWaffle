/**
 * @author Chan Ting He	
 * @date Sept 17, 2015
 */
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
import com.ibm.salesconnect.model.standard.Forecast.Seller.ViewSellerForecastPage;

public class VerifySellerSummary extends ProductBaseTest{
	
	Logger log = LoggerFactory.getLogger(VerifySellerSummary.class);
	
	//Seller Verify Data
	@Test(groups = {"Seller_Forecast"})
	public void Seller_VerifyData(){
		
		log.info("Start Test Seller_VerifyData");
		
		User user1 = commonUserAllocator.getGroupUser(GC.gbsSel);
		
		Dashboard dashboard = launchWithLogin(user1);
		ViewSellerForecastPage viewForecastPage = dashboard.openSellerForecastView();
		viewForecastPage.selectTransactional();
		viewForecastPage.selectTimePeriod(1);
		
		Map<String, String> roadmapSummary = viewForecastPage.getRoadmapSummary();
		
		Map<String, Integer> summaryNumber = viewForecastPage.convertSumToNumber(roadmapSummary);
			
		log.debug("Start veriySummary");
		viewForecastPage.verifySummary(summaryNumber);
		log.debug("Complete veriySummary");		

		int plusAmount=5000;

		viewForecastPage.editRILDealAmount(GC.s_solid, plusAmount);		
		Map<String, String> roadmapSummary2= viewForecastPage.getRoadmapSummary();	
		Map<String, Integer> summaryNumber2 = viewForecastPage.convertSumToNumber(roadmapSummary2);		
		Integer new_Solid_Sum=summaryNumber.get(GC.s_solid)+plusAmount/1000;
		Integer dis_Solid_Sum=summaryNumber2.get(GC.s_solid);
		
		log.debug("Displayed value: "+dis_Solid_Sum+"; Expected value: "+new_Solid_Sum);
		Assert.assertEquals(dis_Solid_Sum, new_Solid_Sum, GC.s_solid+"updated number is not correct");
		
		viewForecastPage.verifySummary(summaryNumber2);		
		
		log.info("Test Completed - Seller_VerifyData");
	
	}
	
}
