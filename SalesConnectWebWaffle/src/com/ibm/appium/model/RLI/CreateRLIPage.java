package com.ibm.appium.model.RLI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.appium.Objects.LineItem;
//import com.ibm.appium.common.GC;
import com.ibm.appium.model.MobilePageFrame;
import com.ibm.appium.model.partials.L10_OfferingTypeSelectPage;
import com.ibm.appium.model.partials.L15_SubBrandSelectPage;
import com.ibm.appium.model.partials.L17_SegmentLineSelectPage;
import com.ibm.appium.model.partials.L20_BrandCodeSelectPage;
import com.ibm.appium.model.partials.L30_ProductInformationSelectPage;
import com.ibm.appium.model.partials.L40_MachineTypeSelectPage;

public class CreateRLIPage extends MobilePageFrame {
	Logger log = LoggerFactory.getLogger(CreateRLIPage.class);

	public CreateRLIPage() {
		Assert.assertTrue(isPageLoaded(), "Create Opportunity Page has not loaded");
	}

	/**
	 * Check if new page is loaded by finding element on that page before
	 * starting any actions.
	 */
	@Override
	public boolean isPageLoaded() {
		return waitForPageToLoad(pageLoaded);
	}

	// XPath Selectors
	public static String pageLoaded = "//label[contains(text(),'Offering Type (L-10)')]";
	public static String lineItemOfferingTypeL10 = "//label[contains(text(),'Offering Type (L-10)')]";	
	public static String lineItemSubBrandL15 = "//label[contains(text(),'Sub-Brand (L-15)')]";	
	public static String lineItemSegmentLineL17 = "//label[contains(text(),'Segment/Line (L-17)')]";	
	public static String lineItemBrandCodeL20 = "//label[contains(text(),'Brand Code (L-20)')]";	
	public static String lineItemProductInformationL30 = "//label[contains(text(),'Product Information (L-30)')]";	
	public static String lineItemMachineTypeL40 = "//label[contains(text(),'Machine Type (L-40)')]";	
	
	public static String lineItemRoadmapStatus = "//select[@name='roadmap_status']"; 
	public static String lineItemProbability = "//select[@name='probability']";
	public static String lineItemFulfillmentType = "//select[@name='stg_fulfill_type']";
	public static String lineItemGreenBlueRevenue = "//select[@name='green_blue_revenue']";
	
	public static String lineItemBookingType = "//select[@name='swg_book_new']";
	public static String lineItemAmount = "div.currency_edit input"; 
	
	//"//label[contains(text(),'Amount')]/..//input";	

	
	public static String saveRLIButton = "//span[@class='saveBtn btn-area-more']";
	
	/**
	 * Main method to populate Opportunity form.
	 */
	public void enterRLIInfo(LineItem lineItem) {

		// Selecting Offering Type
		log.info("Navigate to Offering Type L-10 and select product");
		L10_OfferingTypeSelectPage L10SelectPage = openSelectL10();
		L10SelectPage.searchForProduct(lineItem);
		L10SelectPage.selectProduct(lineItem);

		// Selecting Offering Type
		log.info("Navigate to L-15 and select product");
		L15_SubBrandSelectPage L15SelectPage = openSelectL15();
		L15SelectPage.searchForProduct(lineItem);
		L15SelectPage.selectProduct(lineItem);
		
		// Selecting Offering Type
		log.info("Navigate to L-17 and select product");
		L17_SegmentLineSelectPage L17SelectPage = openSelectL17();
		L17SelectPage.searchForProduct(lineItem);
		L17SelectPage.selectProduct(lineItem);

		// Selecting Offering Type
		log.info("Navigate to L-20 and select product");
		L20_BrandCodeSelectPage L20SelectPage = openSelectL20();
		L20SelectPage.searchForProduct(lineItem);
		L20SelectPage.selectProduct(lineItem);

		if(lineItem.getsL30_ProductInformation().length() > 0){
			// Selecting Product Information
			log.info("Navigate to L-30 and select product");
			L30_ProductInformationSelectPage L30SelectPage = openSelectL30();
			L30SelectPage.searchForProduct(lineItem);
			L30SelectPage.selectProduct(lineItem);
		}
		
		if(lineItem.getsL40_MachineType().length() > 0){
			// Selecting Machine Type
			log.info("Navigate to L-40 and select product");
			L40_MachineTypeSelectPage L40SelectPage = openSelectL40();
			L40SelectPage.searchForProduct(lineItem);
			L40SelectPage.selectProduct(lineItem);
		}
		
		if(lineItem.getsBookingType().length() > 0){
			log.info("Setting the booking type");
			select(lineItemBookingType,lineItem.getsBookingType());
		}
		
		if(lineItem.getsRoadmapStatus().length() > 0){
			log.info("Changing Roadmap status to " + lineItem.getsRoadmapStatus());
			select(lineItemRoadmapStatus,lineItem.getsRoadmapStatus());
		}
		
		if(lineItem.getsProbability().length() > 0){
			log.info("Changing probability to " + lineItem.getsProbability());
			select(lineItemProbability,lineItem.getsProbability());
		}
		
		if(lineItem.getsFulfillmentType().length() > 0){
			log.info("Changing fulfillment type to " + lineItem.getsFulfillmentType());
			select(lineItemFulfillmentType,lineItem.getsFulfillmentType());
		}
		
		if(lineItem.getsGreenBlueRevenue().length() > 0){
			log.info("Changing Green/Blue revenue to " + lineItem.getsGreenBlueRevenue());
			select(lineItemGreenBlueRevenue,lineItem.getsGreenBlueRevenue());
		}
		
		log.info("Typing in the amount");
		clear(lineItemAmount);
		typeByCSS(lineItemAmount,lineItem.getsAmount());
		
	}

	/**
	 * Save RLI form and navigate to RLI detail page.
	 * 
	 * @return OpportunityDetailPage
	 */
	public RLIDetailPage saveRLI() {
		click(saveRLIButton);
		return new RLIDetailPage();
	}
	
	private L10_OfferingTypeSelectPage openSelectL10(){
		click(lineItemOfferingTypeL10);
		return new L10_OfferingTypeSelectPage();
	}

	private L15_SubBrandSelectPage openSelectL15(){
		click(lineItemSubBrandL15);
		return new L15_SubBrandSelectPage();
	}
	
	private L17_SegmentLineSelectPage openSelectL17(){
		click(lineItemSegmentLineL17);
		return new L17_SegmentLineSelectPage();
	}
	
	private L20_BrandCodeSelectPage openSelectL20(){
		click(lineItemBrandCodeL20);
		return new L20_BrandCodeSelectPage();
	}
	
	private L30_ProductInformationSelectPage openSelectL30(){
		click(lineItemProductInformationL30);
		return new L30_ProductInformationSelectPage();
	}
	
	private L40_MachineTypeSelectPage openSelectL40(){
		click(lineItemMachineTypeL40);
		return new L40_MachineTypeSelectPage();
	}
	
}
