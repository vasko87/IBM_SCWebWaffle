package com.ibm.salesconnect.model.standard.Forecast;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardViewPage;
import com.ibm.salesconnect.objects.Adjustment;

public class CreateAdjustmentPage extends StandardViewPage{
	Logger log = LoggerFactory.getLogger(CreateAdjustmentPage.class);
		
	protected static CreateAdjustmentPage createAdj;	

	//Selectors
	public static String pageLoaded = "//*[@aria-label='Adjustment Description']";
	public static String adj_dropDown_results = "//*[@id='select2-drop']/ul/li[1]";
	public static String adj_dropDown_input = "css=div[id='select2-drop'] input";
	public static String seller_adj_description = "//*[@aria-label='Adjustment Description']";	
	public static String seller_adj_type = "css=span[data-fieldname='type'] a";		
	public static String seller_adj_client = "css=span[data-fieldname='account_name'] a";	
	public static String seller_adj_date = "//*[@aria-label='Date']";
	public static String seller_adj_roadmapstatus = "css=span[data-fieldname='roadmap_status'] a";		
	public static String seller_adj_amount = "css=span[data-fieldname='revenue_amount']  input[name='revenue_amount']";	
	public static String seller_adj_select = "css=span[data-fieldname='level_search'] a";		
	public static String adj_save = "//*[@name='save_button']";
	public static String save = "//*[@name='commit_button']";
	
	public static String mgr_adj_amount = "css=span[data-fieldname='man_revenue_amount']  input[name='man_revenue_amount']";
	public static String mgr_adj_select = "css=span[data-fieldname='level_name'] a";
	public static String mgr_adj_roadmapstatus = "css=span[data-fieldname='man_roadmap_status'] a";
	public static String mgr_adj_geography = "css=span[data-fieldname='geography'] a";	
	public static String mgr_adj_industry = "css=span[data-fieldname='search_industry'] a";
	public static String mgr_adj_gbsunit = "css=span[data-fieldname='gbsunit'] a";	
	public static String mgr_adj_contract = "css=span[data-fieldname='contract_type'] a";
	public static String mgr_adj_channel = "css=span[data-fieldname='channel'] a";
	public static String mgr_refresh = "//*[@class='manager-headerpane']";
	
	
	public CreateAdjustmentPage(Executor exec) {
		super(exec);		
		Assert.assertTrue(isPageLoaded(), "Seller Forecast page has not loaded within 60 seconds");
	}

	@Override
	public boolean isPageLoaded() {
		return waitForPageLoad(pageLoaded);		
	}
	
	public static CreateAdjustmentPage getInstance(){
		if(createAdj == null){
			createAdj = new CreateAdjustmentPage(exec);
		}
		return createAdj;			
	}
	
	//Method
	public Map<String, String> enterAdjustmentInfo(Adjustment adj, String sellerOrMgr) throws Exception{
		Map<String, String> adjData = new HashMap<String, String>();
				
		if(adj.adj_description.length()>0){
			type(seller_adj_description, adj.adj_description);
			adjData.put("Description", adj.adj_description);
		}
		if(adj.adj_type.length()>0){					
			selectAdjDropDown(seller_adj_type, adj_dropDown_input, adj.adj_type, adj_dropDown_results);
			adjData.put("Type", adj.adj_type);
		}
		if(adj.adj_client.length()>0){		
			selectAdjDropDown(seller_adj_client, adj_dropDown_input, adj.adj_client, adj_dropDown_results);
			adjData.put("Client Name", adj.adj_client);
		}	
		if(adj.adj_date.length()>0){		
			type(seller_adj_date, adj.adj_date);
			adjData.put("Date", adj.adj_date);
		}
		if(adj.adj_roadmapstatus.length()>0){		
			if(sellerOrMgr.contains("seller")){
				click(seller_adj_roadmapstatus);				
				click("//div[@id='select2-drop']/ul/li/div[contains(text(), '"+adj.adj_roadmapstatus+"')]");
				adjData.put("Roadmap Status", adj.adj_roadmapstatus);
			}else{
				click(mgr_adj_roadmapstatus);				
				click("//div[@id='select2-drop']/ul/li/div[contains(text(), '"+adj.adj_roadmapstatus+"')]");	
				adjData.put("Roadmap Status", adj.adj_roadmapstatus);
			}
		}		
		
		if(adj.adj_amount.length()>0){		
			if(sellerOrMgr.contains("seller")){
				type(seller_adj_amount, adj.adj_amount);				
				adjData.put("Amount", adj.adj_amount.substring(0,adj.adj_amount.length()-3));
			}else{
				type(mgr_adj_amount, adj.adj_amount);
				adjData.put("Amount", adj.adj_amount.substring(0,adj.adj_amount.length()-3));
			}			
		}
		if(adj.adj_select.length()>0){		
			if(sellerOrMgr.contains("seller")){
				selectAdjDropDown(seller_adj_select, adj_dropDown_input, adj.adj_select, adj_dropDown_results);
				adjData.put("Offering/Solution", adj.adj_select);
				sleep(3);
			}else{
				selectAdjDropDown(mgr_adj_select, adj_dropDown_input, adj.adj_select, adj_dropDown_results);
				adjData.put("Offering/Solution", adj.adj_select);
				adjData.put("Creator", "F01 MANAGER_GBS");
			}			
		}
		
		if(sellerOrMgr.contains("manager")){			
			if(adj.adj_geography.length()>0){
				selectAdjDropDown(mgr_adj_geography, adj_dropDown_input, adj.adj_geography, adj_dropDown_results);
			}
			if(adj.adj_industry.length()>0){
				selectAdjDropDown(mgr_adj_industry, adj_dropDown_input, adj.adj_industry, adj_dropDown_results);
			}
			if(adj.adj_gbsunit.length()>0){
				selectAdjDropDown(mgr_adj_gbsunit, adj_dropDown_input, adj.adj_gbsunit, adj_dropDown_results);
			}
			if(adj.adj_contract.length()>0){
				click(mgr_adj_contract);
				click("//div[@id='select2-drop']/ul/li/div[contains(text(), '"+adj.adj_contract+"')]");				
			}
			if(adj.adj_channel.length()>0){
				click(mgr_adj_channel);
				click("//div[@id='select2-drop']/ul/li/div[contains(text(), '"+adj.adj_channel+"')]");				
			}
		}
		this.adjSave();			
		return adjData;
	}
	
	public void adjSave(){		
		click(adj_save);
		waitForAlertExpiration();
		log.info("Adjustment is saved...");	
	}
	
	public boolean verifyCreatedAdjustment(Map<String, String> adjData, String sellerOrMgr){
		log.info("Compare with data get from Forecast Page...");
		int flag = 0;		
		Map<String, String> map = this.getCreatedAdjustmentInfo(sellerOrMgr);			
		for(String key:map.keySet()){				
			if(!adjData.get(key).isEmpty()){
				if(!map.get(key).contains(adjData.get(key))){
					flag = flag+1;
				}					
			}
		}
		if(flag==0){
			log.info("All elements are matched, adjustment is created...");
			return true;
		}
		else{
			log.info("Totally "+flag+" element(s) are not matched...");
			return false;		
		}
	}	
}
