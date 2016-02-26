package com.ibm.salesconnect.model.partials;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardCreatePage;
import com.ibm.salesconnect.objects.RevenueItem;

public class OpportunityLineItemPanel extends StandardCreatePage{
	Logger log = LoggerFactory.getLogger(OpportunityLineItemPanel.class);
	public OpportunityLineItemPanel(Executor exec, int i){
		super(exec);
		Item = i;
		standardExtension = getIthOpptySubform(Item);
		Assert.assertTrue(isPageLoaded(), "Create RLI has not loaded within 60 seconds" );
	}
	
	public void enterInfo(RevenueItem rli){
		if(rli.sFindOffering.length()>0)
			select(getOffering(), rli.sFindOffering);
		if(rli.sGreenBlueRevenue.length()>0)
			select(getGreenBlueRevenue(), rli.sGreenBlueRevenue);
		
		if(rli.sProbability.length()>0)
			select(getProbability(), rli.sProbability);
		
		if(rli.sRevenueAmount.length()>0){
			type(getRevenueAmount(), rli.sRevenueAmount);
		}		
		triggerChange("css=input[aria-label=\"Amount\"]");
			
		if(rli.sCompetitor.length()>0)
			typeEnter(getCompetitors(), rli.sCompetitor);
		if(rli.sOwner.length()>0)
			select(getOwner(), rli.sOwner);
		if(rli.sRoadmapStatus.length()>0)
			select(getRoadMapStatus(), rli.sRoadmapStatus);
		if(rli.sContractType.length()>0)
			select(getContractType(), rli.sContractType);
		click(pageBackground);
		if(rli.sFulfillmentType.length()>0)
			select(getFulfillmentType(), rli.sFulfillmentType);
//		if(rli.sCurrencyType.length()>0)
//			select(getCurrency(), rli.sCurrencyType);
	}
	
	
	public String getIthOpptySubform(int i) {return "//div[@id='oppty-rli-subform']/div["+String.valueOf(i)+"]";}
	String standardExtension;
	String pageLoaded() {return standardExtension + "//div[@data-name='level_search']//span[@class='select2-chosen']";}
	String getOffering() {return standardExtension + "//div[@data-name='level_search']//span[@class='select2-chosen']";}
	String getCompetitors() {return standardExtension + "//div[@data-name='competitor']//input[not(@type='hidden')]";}
	String getRoadMapStatus() {return standardExtension + "//div[@data-name='roadmap_status']//span[@class='select2-chosen']";}
	String getGreenBlueRevenue() {return standardExtension + "//div[@data-name='green_blue_revenue']//span[@class='select2-chosen']";}
	String getProbability() {return standardExtension + "//div[@data-name='probability']//span[@class='select2-chosen']";}
	String getOwner() {return standardExtension + "//div[@data-name='assigned_user_name']//span[@class='select2-chosen']";}
	String getContractType() {return standardExtension + "//div[@data-name='revenue_type']//span[@class='select2-chosen']";}
	String getAmount() {return standardExtension + "//div[@data-name='revenue_amount']//input";}
	String getCurrency() {return standardExtension + "//div[@data-name='revenue_amount']//a//span[@class='select2-chosen']";}
	String getDuration() {return standardExtension + "//div[@data-name='duration']//input";}
	String getForecastCloseDate() {return standardExtension + "//div[@data-name='fcast_date_sign']//input";}
	String getFulfillmentType() {return standardExtension + "//div[@data-name='stg_fulfill_type']//span[@class='select2-chosen']";}
	String getRevenueAmount() {return standardExtension + "//span[@data-fieldname='revenue_amount']/span/div/input";}
	String getRevenueTest() { return "/html/body/div[1]/div/div[5]/div/div/div[1]/div[1]/div[1]/div/div[3]/div[7]/div/div/div/div[2]/div[5]/div[2]/span/span/div/input";}
	//String getCurrency() {return standardExtension + "//div[@data-name='revenue_amount']//span[@class='select2-chosen']";}
	String pageBackground="//div[contains(@class,'oppty-rli-create-item')]";
	
	int Item;
}
