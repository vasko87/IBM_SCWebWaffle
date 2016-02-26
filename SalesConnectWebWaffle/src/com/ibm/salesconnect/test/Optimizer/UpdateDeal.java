/**
 * @author Cheng Yue
 *
 */
package com.ibm.salesconnect.test.Optimizer;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.core.webdriver.WebDriverExecutor;
import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Forecast.Manager.ViewMgrForecastPage;
import com.ibm.salesconnect.model.standard.Forecast.Seller.ViewSellerForecastPage;
import com.ibm.salesconnect.model.standard.LineItems.EditLineItem;
import com.ibm.salesconnect.objects.RevenueItem;

public class UpdateDeal extends ProductBaseTest{
	Logger log = LoggerFactory.getLogger(UpdateDeal.class);
	
	@Test(groups = { "SVT6_UpdateDeal"})
	public void Test_SellerUpdateDeal() throws Exception{
		log.info("Start of test method Test_SellerUpdateDeal");
		User user1 = commonUserAllocator.getGroupUser("gbs_Sel");
		
		log.info("Logging in");
		Dashboard dashboard = launchWithLogin(user1);
		ViewSellerForecastPage viewSellerForecastPage = dashboard.openSellerForecastView();		 
		
		log.info("Edit the first record in Seller's Deal List");		
			
		EditLineItem editRLI = viewSellerForecastPage.clickEditLineItem(dashboard);
		
		String oppNum = viewSellerForecastPage.getUpdateOppNum();
						
		RevenueItem rli = new RevenueItem();		
		System.out.println("Editing the first line item in seller's deal list");
		Map<String , String> sellerUpdatedDeal = editRLI.editLineItemInfo1(rli);
		
		//seller log out
		WebDriverExecutor webDriverExecutor = new WebDriverExecutor(manager);
		webDriverExecutor.quit();
		
		//manager log in
		log.info("Start of test method Test_VerifyUpdatedDeal");
		User user2 = commonUserAllocator.getGroupUser("gbs_Mgr");
		
		log.info("Logging in");
		Dashboard dashboard1 = launchWithLogin(user2);
		
		log.info("Search for the updated deal in MgrForecast page");
		dashboard1.openMgrForecastView();
		ViewMgrForecastPage viewMgrForecastPage = new ViewMgrForecastPage(exec); 
		viewMgrForecastPage.searchNoFilter_DL(oppNum); //search the updated deal by oppty no.
		
		log.info("check the AlwaysMatchFCST checkbox in Manager's Deal List.");
		viewMgrForecastPage.checkAlwaysMatchFCST();
		sleep(10);
		
		log.info("Record and verify the updated deal in Mgr worksheet.");
		Map<String, String> MgrDeal = viewMgrForecastPage.getMgrDealList();
		System.out.println("roadmap details in manager's worksheet:" + MgrDeal);
		
		Assert.assertTrue(viewMgrForecastPage.verifyDealList(sellerUpdatedDeal, MgrDeal), "match failed!!!!!!");

		log.info("End testing of Update A Deal");
	
	}
	
		
}