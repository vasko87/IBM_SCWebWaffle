package com.ibm.salesconnect.model.partials;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardDetailPage;
import com.ibm.salesconnect.model.standard.LineItems.EditLineItem;
import com.ibm.salesconnect.model.standard.Opportunity.OpportunityDetailPage;
import com.ibm.salesconnect.objects.RevenueItem;

public class LineItemDetailPage extends StandardDetailPage{

Logger log = LoggerFactory.getLogger(EditLineItem.class);
	
	/**
	 * @param exec
	 */
	public LineItemDetailPage(Executor exec) {
		super(exec);
		Assert.assertTrue(isPageLoaded(), "Edit Line Item page has not loaded within 60 seconds");	
	}
	
	/**
	 * @param RLI
	 */
	public OpportunityDetailPage editLineItemInfo(RevenueItem rli){
		click(editButton);
		if(rli.sRoadmapStatus.length()>0)
			select(RoadmapStatus, rli.sRoadmapStatus);
		if(rli.sProbability.length()>0)
			select(Probability, rli.sProbability);
		if(rli.sFindOffering.length()>0)
			select(findOffering, rli.sFindOffering);

		click(saveButton);
		return new OpportunityDetailPage(exec);
	}
	
	public String Probability = "//div[@data-name='probability']//span[@class='select2-chosen']";
	public String Source = "//div[@data-name='lead_source']//span[@class='select2-chosen']";
	public String RoadmapStatus = "//div[@data-name='roadmap_status']//span[@class='select2-chosen']";
	public String findOffering = "//div[@data-name='level_search']//span[@class='select2-chosen']";
	public String saveButton = "//a[@name='save_button' and not(@style='display: none;')]";
	public String editButton = "//a[@name='edit_button' and not(@style='display: none;')]";
}
