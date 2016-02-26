/**
 * @author Chan Ting He	
 * @date Sept 17, 2015
 */
package com.ibm.salesconnect.model.standard.Forecast.Seller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import java.util.Map.Entry;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.StandardViewPage;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.LineItems.EditLineItem;

public class ViewSellerForecastPage extends StandardViewPage {
	Logger log = LoggerFactory.getLogger(ViewSellerForecastPage.class);
	
	protected static ViewSellerForecastPage viewSellerForecast;
	/**
	 * @param exec
	 */
	public ViewSellerForecastPage(Executor exec){
		super(exec);		
	}
	
	public static ViewSellerForecastPage getInstance(){
		if(viewSellerForecast == null){
			viewSellerForecast = new ViewSellerForecastPage(exec);
		}
		return viewSellerForecast;
	}
	// OPP Forecast Tabs
	public final String TAB_OPP_TRANSACTIONAL="//div[@class='headerpane list-headerpane']//a[@data-tabid='Transactional']";
	public final String TAB_OPP_SIGNING="//div[@class='headerpane list-headerpane']//a[@data-tabid='Signings']";
	
	
	// IGF Forecast Tabs
	public final String TAB_IGF_FINANCED_AMOUNT="//div[class^='headerpane']//a[data-tabid='Volumes']";
	public final String TAB_IGF_TOTAL_CONTRACT_VALUE="//div[class^='headerpane']//a[data-tabid='Financing']";
	
	// Buttons on Forecast View
	public final String BTN_FORECAST_SAVE="//div[@class='headerpane list-headerpane']//a[@name='commit_button']";
	public final String BTN_FORECAST_DISCARD_CHANGES="//div[@class='headerpane list-headerpane']//a[@name='discard_button']";
	public final String LABEL_NODE_NAME ="//div[@class='headerpane list-headerpane']//span[@class='module-title pull-left']";
	
	// Buttons on Adjustments create view
	public final String BTN_ADJ_CREATE="span.create-button.panel-top > span > a";
	public final String BTN_ADJ_SAVE="a[name='save_button']";
	public final String BTN_ADJ_CANCEL="a[name='cancel_button']";

	public final String BTN_IGF_DL_EDIT_SAVE="//div[class='nav results ibm_Roadmaps']//a[name='commit_button']";
	public final String BTN_ADD_FINANCING_SAVE="//div[class='headerpane']//a[name='save_button']";
	public final String BTN_VIEW_ROADMAP_CANCEL="//div[class='headerpane']//a[name='cancel_button']";
	public final String BTN_VIEW_ROADMAP="//div[@id='list_subpanel_opportun_revenuelineitems']//input[@title='View roadmap']";
	public final String BTN_ADD_FINANCING="//a[@name='add_my_roadmap_igf']";
	
	public final String addDLAmountIGF="//div[@class='pull-right']//input[@name='revenue_amount']";
	public final String addDLRoadmapStatusIGF="//div[@class='pull-right']//span[@class='select igf-roadmap-status edit-roadmap-igf']//a";
	public final String addDLTermIGF="//div[@class='pull-right']//input[@name='duration']";
	public final String addDLSalesStageIGF="//div[@class='pull-left']//span[@class='select igf-sales-stage edit-roadmap-igf']//a";
	public final String addDLOddsIGF="//div[@class='pull-left']//span[@class='select igf-odds edit-roadmap-igf']//a";

	public final String TIME_PERIOD_ARROW="//span[@class='select2-arrow']";
	
	// Adjustments panel
	public final String ADJ_PANEL="//div[class='flex-list-view-content']//th[data-fieldname='type']";
	public final String ADJ_TOGGLE_PANEL="//ul[class='nav results ibm_Adjustments']//a[data-original-title='Toggle Visibility']";
	
	// Deal List panel
	public final String DL_PANEL="//div[class='flex-list-view-content']//th[data-fieldname='opportunity_id']";
	public final String DL_TOGGLE_PANEL="//ul[class='nav results ibm_Roadmaps']//a[data-original-title='Toggle Visibility']";
	public final String DL_STATUS_FILTER = "//ul[@class='nav results ibm_Roadmaps']//form[@class='form-search-related']//div[@class='filter-view search']/div[@id='s2id_view137']//input";
	public final String DL_STATUS_FILTER_CLOSE ="//ul[@class='nav results ibm_Roadmaps']//form[@class='form-search-related']//div[@class='filter-view search']/div[@id='s2id_view137']//a[@class='select2-search-choice-close']";	
	public final String DL_ACTION_BUTTON = "//ul[@class='nav results ibm_Roadmaps']//tbody/tr[1]/td[10]/span//a[@class='btn dropdown-toggle']";
	public final String DL_EDITRLI = ".//*[@id='content']//table/tbody/tr[1]/td[10]//ul/li/span/a[@name='lineitem_button']";
	// TCV Deal List
	public final String TCV_OPP="//ul[@class='nav results ibm_Roadmaps']//tbody/tr[1]/td[1]//div[@class='ellipsis_inline']//a";
	public final String TCV_ACCOUNT="//ul[@class='nav results ibm_Roadmaps']//tbody/tr[1]/td[2]//div[@class='ellipsis_inline']//a";
	public final String TCV_WON="//ul[@class='nav results ibm_Roadmaps']//tbody/tr[1]/td[3]/span/div";
	public final String TCV_SOLID="//ul[@class='nav results ibm_Roadmaps']//tbody/tr[1]/td[4]/span/div";
	public final String TCV_ATRISK="//ul[@class='nav results ibm_Roadmaps']//tbody/tr[1]/td[5]/span/div";
	public final String TCV_KEYSTRETCH="//ul[@class='nav results ibm_Roadmaps']//tbody/tr[1]/td[6]/span/div";
	public final String TCV_STRETCH="//ul[@class='nav results ibm_Roadmaps']//tbody/tr[1]/td[7]/span/div";
	public final String TCV_NIR="//ul[@class='nav results ibm_Roadmaps']//tbody/tr[1]/td[8]/span/div";
	public final String TCV_DL_EDIT_BTN_ARROW="//ul[@class='nav results ibm_Roadmaps']//tbody/tr[1]/td[9]/span//a[@class='btn dropdown-toggle']";
	
	//Deal List Table line item view
	public final String RLI_OPP = "//ul[@class='nav results ibm_Roadmaps']//tbody/tr[1]/td[1]//div[@class='ellipsis_inline']//a";
	public final String RLI_CLIENT = "//ul[@class='nav results ibm_Roadmaps']//tbody/tr[1]/td[2]//div[@class='ellipsis_inline']/a";
	public final String RLI_OFFERING = "//ul[@class='nav results ibm_Roadmaps']//tbody/tr[1]/td[3]//div[@class='ellipsis_inline']/a";
	public final String RLI_DATE = "//ul[@class='nav results ibm_Roadmaps']//tbody/tr[1]/td[4]//div[@class='ellipsis_inline']";
	public final String RLI_DATE_INPUT = "//ul[@class='nav results ibm_Roadmaps']//tbody/tr[1]/td[4]//div[@class='input-append date']/input";
	public final String RLI_PROBABILITY ="//ul[@class='nav results ibm_Roadmaps']//tbody/tr[1]/td[5]//div[@class='ellipsis_inline']";
	public final String RLI_PROBABILITY_ARROW ="//ul[@class='nav results ibm_Roadmaps']//tbody/tr[1]/td[5]//span[@class='select2-arrow']/input";
	public final String RLI_STAGE = "//ul[@class='nav results ibm_Roadmaps']//tbody/tr[1]/td[6]//div[@class='ellipsis_inline']";
	public final String RLI_P2C = "//ul[@class='nav results ibm_Roadmaps']//tbody/tr[1]/td[7]//div[@class='ellipsis_inline']";
	public final String RLI_STATUS ="//ul[@class='nav results ibm_Roadmaps']//tbody/tr[1]/td[8]//div[@class='ellipsis_inline']";
	public final String RLI_STATUS_ARROW="//ul[@class='nav results ibm_Roadmaps']//tbody/tr[1]/td[8]//span[@class='select2-arrow']/input";
	public final String RLI_AMOUNT ="//ul[@class='nav results ibm_Roadmaps']//tbody/tr[1]/td[9]//div[@class='clickToEdit']";
	public final String RLI_AMOUNT_INPUT ="//ul[@class='nav results ibm_Roadmaps']//tbody/tr[1]/td[9]//input";
	
	
	//SWG ACV 
	public final String SWG_ACV_EDIT_DISABLED="//ul[@class='nav results ibm_Roadmaps']//tbody/tr[1]/td[9]/span[@class='isEditable disabled']";
	public final String LINK_ADJ_BROWSE_OFFERING_SOLUTION="//a[contains(text(),'Browse Offerings/Solutions')]";
	public final String MAIN_WINDOW_TAB="//div[@class='main-pane row-fluid']//ul[@class='nav results ibm_Roadmaps']";
	public final String SUCCESSFULLY_ALERT="//div[@class='alert alert-success alert-block']//strong[contains(text(),'Success')]";
	public final String LOADING_ALERT= "//div[@class='alert alert-process']/strong[contains(text(),'Loading')";
	public final String WARNING_ALERT="//div[@class='alert alert-warning alert-block']";
	public final String WARNING_CONFIRM_BTN="//div[@class='alert alert-warning alert-block']//a[@class='span6 alert-btn-confirm']";
	public final String WARNING_CANCEL_BTN="//div[@class='alert alert-warning alert-block']//a[@class='span6 alert-btn-cancel']";
	

	//Click Save button
    public void saveUpdates(){
    	sleep(1);
    	
    	if(isPresent(WARNING_ALERT)){
    		click(WARNING_CONFIRM_BTN);
    		click(BTN_FORECAST_SAVE);
    		sleep(3);
    	}else{
    		click(BTN_FORECAST_SAVE);
    		sleep(3);
    	}    	
    }
	
    /* 
     * index=1 current Q
     * index=2 current Q+1
     * ......
     * index=6 current Q+5
     * */
    public void selectTimePeriod(int index){
    	click(this.TIME_PERIOD_ARROW);
    	click("//div[@id='select2-drop']/ul/li[" + index + "]/div/div");
    	waitForAlertExpiration();
    }
    
  
    public void selectTransactional(){
		this.click(TAB_OPP_TRANSACTIONAL);
		log.info("Switch to Transactional tab");
		waitForAlertExpiration();
    }
   

    public void selectSignings(){    	
		this.click(TAB_OPP_SIGNING);
		log.info("Switch to Signings tab");
    }
   
  /*  
    public Map<String, String> getRoadmapSummary(){
    	Map<String, String> roadmapSummary= new HashMap<String, String>();
    	
    	for(int i=1;checkForElement("//div[@class='row-fluid topline']//div[@class='pull-right']/span["+i+"]/div");i++ ){
    		
    		String str = getObjectText("//div[@class='row-fluid topline']//div[@class='pull-right']/span["+i+"]/div");
    		int idx = str.indexOf("\n");
    		str = str.substring(0, idx);
    		roadmapSummary.put(str, getObjectText("//div[@class='row-fluid topline']//div[@class='pull-right']/span["+i+"]/div/h2"));
    		log.debug("Line: "+i);
    	}
    	
    	return roadmapSummary;
    }
  */
    public Map<String, String> getRoadmapSummary(){
    	Map<String, String> roadmapSummary= new HashMap<String, String>();
    	WebDriver wd = (WebDriver) exec.getBackingObject();
    	WebElement head = wd.findElement(By.xpath("//*[@class='pull-right']"));
    	List<WebElement> heads = head.findElements(By.tagName("span"));
    	List<WebElement> values = head.findElements(By.tagName("h2"));    	
    	for(int i=0; i<heads.size();i++){      		
    		String key = heads.get(i).getText();
    		String value = values.get(i).getText();
    		int idx = key.indexOf("\n");
    		key = key.substring(0, idx);
    		roadmapSummary.put(key, value);    		
    	} 
    	return roadmapSummary;
    }
    	
    
    public Map<String, Integer> convertSumToNumber(Map<String, String> roadmapSummary){
    	
    	Map<String, Integer> summaryNumber = new HashMap<String, Integer>();
    	
    	Iterator<Entry<String, String>> iterator1 = roadmapSummary.entrySet().iterator();
    	while(iterator1.hasNext()){
    		Map.Entry<String, String> entry = (Map.Entry<String, String>)iterator1.next();
    		Object key = entry.getKey();
    		Object value = entry.getValue();
    		String valueString = value.toString();
    		String newvalueString="";
    		int number = 0;
    		
    		if(valueString.equals("0")){
    			summaryNumber.put(key.toString(), 0);
    			
    		} 
    		else if(valueString.startsWith("(")){
    			newvalueString = valueString.substring(1, valueString.length()-2); 
    			if(newvalueString.contains(","))
    				newvalueString = newvalueString.replace(",", "");
    			number = 0 - Integer.parseInt(newvalueString);
    			summaryNumber.put(key.toString(), number);
    			newvalueString= "";
    			number = 0;
    		
    		} else	{
    			newvalueString = valueString.substring(0, valueString.length()-1);
    			if(newvalueString.contains(","))
    				newvalueString = newvalueString.replace(",", "");
    			number = Integer.parseInt(newvalueString);
    			summaryNumber.put(key.toString(), number);
    		}
    	}
    	
    	return summaryNumber;
    }
    
    public void verifySummary(Map<String, Integer> summaryNumber){
    	
    	Integer WSR = summaryNumber.get(GC.s_won) + summaryNumber.get(GC.s_solid) + summaryNumber.get(GC.s_at_risk);
		Integer BestCase = summaryNumber.get(GC.s_won)+ summaryNumber.get(GC.s_solid) + summaryNumber.get(GC.s_at_risk) + summaryNumber.get(GC.s_key_stretch);
		Integer WorstCase = summaryNumber.get(GC.s_won) + summaryNumber.get(GC.s_solid);
		Integer DeltaTarget = summaryNumber.get(GC.s_WSA)-summaryNumber.get(GC.s_target);
		
		log.debug("Before Assert WSR");
		Assert.assertEquals(WSR, summaryNumber.get(GC.s_WSA),"WSR doesn't equal");
		log.debug("Before Assert Best Case");
		Assert.assertEquals(BestCase, summaryNumber.get(GC.s_best_case), "BestCase doesn't equal");
		log.debug("Before Assert WorstCase");
		Assert.assertEquals(WorstCase, summaryNumber.get(GC.s_worst_case),"WorstCase doesn't equal");
		log.debug("Before Assert DeltatoTarget");
		Assert.assertEquals(DeltaTarget, summaryNumber.get(GC.s_delta_to_target),"DeltaToTarget doesn't equal");
		log.debug("Complete Assert DeltatoTarget");
		
    }
    
    public void editRILDealAmount(String roadmapStatus, int plusAmount){
    	
    	//this.select(DL_STATUS_FILTER, roadmapStatus);
    	click(DL_STATUS_FILTER);				
		click("//*[@id='select2-drop']/ul/li/div[contains(text(), '"+roadmapStatus+"')]");
    	if(this.isPresent(RLI_AMOUNT)){
    		this.click(this.RLI_AMOUNT);
    		int amount=(int)Integer.parseInt(this.getObjectAttribute(RLI_AMOUNT_INPUT, "value").replace(",", ""))+plusAmount;
    		String amountString = ""+amount;
    		super.clearField(RLI_AMOUNT_INPUT);
    		type(RLI_AMOUNT_INPUT, amountString); 
    		click("//ul[@class='nav results ibm_Roadmaps']//tbody/tr[1]/td[9]");
    	
    		this.saveUpdates();
    	} else {
    		log.warn("No deal in status of "+roadmapStatus);
    	}
    }
    
    public void type(String selector, String text){
    	
    	super.type(selector, text);
    	
    }    
    
  //Click Action-Edit Line Item from Deal list section
    public EditLineItem clickEditLineItem(Dashboard dashBoard) throws Exception{
		log.info("Open Edit Line Item Page");
		click(DL_ACTION_BUTTON);
		click(DL_EDITRLI);
		waitForAlertExpiration();
		sleep(10);
		System.out.println("=====================1=====================");
		dashBoard.nevToEditLineItem();
		System.out.println("=====================2=====================");
		EditLineItem editRLI = new EditLineItem(exec);		
		System.out.println("=====================3=====================");
		return editRLI;
	}
    
    public String getUpdateOppNum(){
    	String oppNum =  getObjectText("//div[@data-name='opportunities_name']/span/span/a");
    	return oppNum;
    }
    
}
