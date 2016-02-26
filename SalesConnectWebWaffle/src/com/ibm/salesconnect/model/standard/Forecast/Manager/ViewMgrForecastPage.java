package com.ibm.salesconnect.model.standard.Forecast.Manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardViewPage;


public class ViewMgrForecastPage extends StandardViewPage {
		Logger log = LoggerFactory.getLogger(ViewMgrForecastPage.class);
		protected static ViewMgrForecastPage viewMgrForecast;
		
		public static ViewMgrForecastPage getInstance(){
			if(viewMgrForecast == null){
				viewMgrForecast = new ViewMgrForecastPage(exec);
			}
			return viewMgrForecast;
		}
	
		public ViewMgrForecastPage(Executor exec) {
			super(exec);			
			Assert.assertTrue(isPageLoaded(), "View Mgr Forecast page has not loaded within 60 seconds");	
		}
		
		//Selectors
		
		public static String pageLoaded = "//ul[@class='nav results ibm_Cadences']";
				
		// Mgr Forecast Tabs
		public static String MGR_TRANSACTIONAL="//div[class^='headerpane']//a[data-tabid='Transactional']";
		public static String MGR_SIGNING="//div[class^='headerpane']//a[data-tabid='Signings']";
		
		// Buttons on Manager Forecasts View
		public static String MGR_BTN_FORECASTS_SAVE = "//div[class^='headerpane']//a[name='commit_button']";
		public static String MGR_BTN_FORECASTS_DISCARD_CHANGES = "//div[class^='headerpane']//a[name='discard_button']";
						
		//Filter section
		public static String MGR_BTN_FILTER_SAVE = "//div[class='filter-header']//a[class='btn btn-primary save_button']";
		public static String MGR_BTN_ADD_FILTER = "//div[class='filter-actions btn-group']//button[data-action='add']";
		public static String MGR_ENTER_FILTER_NAME = "//div[class='filter-header']//input";		
		public static String MGR_INPUT_DL_OPPNUM = "//div[@class='filter-view search']//input[@class='search-name']";

		//Fields on Deal list 
		public static String MGR_DL_SELLER = "//div[class='flex-list-view-content']";
		public static String MGR_DL_ALWMATCH_FCST1 = ".//*[@id='manager-roadmaps-stg']/tbody/tr/td[9]/span/input"; //for the deal record after search in deal list
		
		// Buttons on Manager Adjustment create view
		public static String MGR_BTN_ADJ_CREATE = "//span[@class='manager-panel-top']/a";
		public static String MGR_BTN_ADJ_SAVE = "//a[@name='save_button']";
		public static String MGR_BTN_ADJ_CANCEL = "//a[@name='cancel_button']";
		public static String MGR_BTN_ADJ_OFFERING_SAVE = "//div[@class='drawer transition active']//a[name='save_button']";
		public static String MGR_BTN_ADJ_OFFERING_CANCEL = "//div[@class='drawer transition active']//a[name='cancel_button']";
		public static String MGR_BTN_ADJ_INDUSTRY_SAVE = "//div[class='drawer transition active']//a[name='save_button']";
		public static String MGR_BTN_ADJ_GEO_SAVE = "//div[class='drawer transition active']//a[name='save_button']";
		
		// Fields on adjustments create forms
		public static String MGR_ADJ_DESCRIPTION = "//span[data-fieldname='description']";
		public static String MGR_ADJ_TYPE = "//span[data-fieldname='type']";
		public static String MGR_ADJ_DATE = "//span[data-fieldname='man_forecast_date']";
		public static String MGR_ADJ_ROADMAP_STATUS = "//span[data-fieldname='man_roadmap_status']";
		public static String MGR_ADJ_CLIENT_NAME = "//span[data-fieldname='account_name']";
		public static String MGR_ADJ_REVENUE_AMOUNT = "//span[data-fieldname='man_revenue_amount']";
		public static String MGR_ADJ_CONTRACT_TYPE = "//span[data-fieldname='contract_type']";
		public static String MGR_ADJ_CHANNEL = "//span[data-fieldname='channel']";
		public static String MGR_ADJ_BRANCH_COV = "//span[data-fieldname='current_coverage_id']";
		public static String MGR_ADJ_INDUSTRY= "//span[data-fieldname='search_industry']";
		public static String MGR_ADJ_SECTOR= "//span[data-fieldname='search_sector']";
		public static String MGR_ADJ_GEO = "//span[data-fieldname='geography']";
		public static String MGR_ADJ_RADIO_INDUSTRY = "//input[value='industry']";
		public static String MGR_ADJ_RADIO_SECTOR = "//input[value='sector']";
	
		//Fields on adjustment - offering/solution toggle drawer	
		public static String LINK_MGR_ADJ_BROWSE_OFFERING_SOLUTION = "//a[contains(text(),'Browse Offerings/Solutions')]";
		public static String MGR_ADJ_BROWSE_RADIO_OFFERING = "//input[value='offering']";
		public static String MGR_ADJ_BROWSE_RADIO_SOLUTION = "//input[value='solution']";	
		public static String MGR_ADJ_BROWSE_LEVEL10 = "//span[data-fieldname='level10']";
		public static String MGR_ADJ_BROWSE_LEVEL15 = "//span[data-fieldname='level15']";
		public static String MGR_ADJ_BROWSE_LEVEL20 = "//span[data-fieldname='level20']";
		public static String MGR_ADJ_BROWSE_LEVEL30 = "//span[data-fieldname='level30']";
		public static String MGR_ADJ_BROWSE_LEVEL40 = "//span[data-fieldname='level40']";
		
		//Fields on adjustment - browse industry/industry class toggle drawer
		public static String LINK_MGR_BROWSE_INDUSTRY = "//a[contains(text(),'Browse Industry/Industry Class')]";
		public static String MGR_BROWSE_INDUSTRY = "//span[data-fieldname='industry']";
		public static String MGR_BROWSE_INDUSTRY_CLASS = "//span[data-fieldname='industry_class']";
		
		//Fields on adjustment - browse geographies toggle drawer
		public static String LINK_MGR_BROWSE_GEOGRAPHY = "//a[contains(text(),'Browse Geographies')]";
		public static String MGR_BROWSE_GEO_IOT = "//span[data-fieldname='iot']";
		public static String MGR_BROWSE_GEO_IMT = "//span[data-fieldname='imt']";
		public static String MGR_BROWSE_GEO_SUBIMT = "//span[data-fieldname='sub_region']";
		public static String MGR_BROWSE_GEO_COUNTRY = "//span[data-fieldname='country']";
		
		//Time period and forecasts type arrows
		public static String NODE_USER_ARROW = "//div[@class='pull-left first']/span[@class='edit']//a[@class='select2-choice']";
		public static String TIME_PERIOD_ARROW = "//span[@class='forecastsTimeperiod edit']//span[@class='select2-arrow']";
		public static String TYPE_ARROW = "//div[@class='pull-left item']/span[@class='edit']//span[@class='select2-arrow']";
		
		// Manager Adjustments panel
		public static String MGR_ADJ_PANEL = "//div[class='flex-list-view-content']//th[data-fieldname='type']";
		public static String MGR_ADJ_TOGGLE_PANEL = "//ul[class='nav results ibm_MGR_ADJustments']//a[data-original-title='Toggle Visibility']";

		// Manager Deal List panel
		public static String MGR_DL_PANEL = "//div[class='flex-list-view-content']//th[data-fieldname='opportunity_id']";
		public static String MGR_DL_TOGGLE_PANEL = "//ul[class='nav results ibm_Roadmaps']//a[data-original-title='Toggle Visibility']";
		public static String EXTEND_ADJUSTMENT= "//ul[@class='nav results ibm_Roadmaps']//li[@class='subpanel clearfix']//div[4]//button[@class='btn btn-link btn-invisible more padded btn-next btn-width-auto'";
		
		public static String SUCCESSFULLY_ALERT = "//div[@class='alert alert-success alert-block']//strong[contains(text(),'Success')]";
		public static String MAIN_WINDOW_TAB = "//div[class^='main-pane']//ul[class='nav results ibm_Cadences']";
		public static String EXTEND_DEAL_LIST = "//ul[@class='nav results ibm_Adjustments']//li[@class='subpanel clearfix']//div[3]//button[@class='btn btn-link btn-invisible more padded btn-next btn-width-auto'";		
		public static String LOADING_ALERT= "//div[@class='alert alert-process']/strong[contains(text(),'Loading')]";
		
		
		@Override
		public boolean isPageLoaded() {
			return this.waitForElement(pageLoaded);
		}

		    
	    public void selectNodeUser(String nodeUser) throws Exception {	    	
			click(NODE_USER_ARROW);			
			click("//div[@id='select2-drop']/ul/li/div[contains(text(), '" + nodeUser + "')]");
			waitForAlertExpiration();
		}
	    
	    /* 
	     * index=1 current Q
	     * index=2 current Q+1
	     * ......
	     * index=6 current Q+5
	     * */
	    public void selectTimePeriod(int index) throws Exception{	    	
	    	click(TIME_PERIOD_ARROW);
	    	click("//div[@id='select2-drop']/ul/li[" + index + "]/div/div");
	    	waitForAlertExpiration();
	    }
	    
		/**
		 * Selects "Forecast Type"
		 * 
		 * @param forecastType - Transactional, Signing, Volumn, Financing etc.
		 * @throws Exception
		 */
	    public void selectForecastType(String forecastType) throws Exception{	    	
	    	click(TYPE_ARROW);	    		
	    	click("//div[@id='select2-drop']/ul/li/div[contains(text(), '" + forecastType + "')]");
	    	waitForAlertExpiration();
	    }
	
	    public Map<String, String> getMgrDealList(){
	    	String headPath = "//table[@id='manager-roadmaps-stg']/thead/tr[1]";
	    	String valuePath = "//table[@id='manager-roadmaps-stg']/tbody/tr[1]";    	  
	    	
	    	  if(waitForElement(valuePath)){
	    		  Map<String, String> dealMap = new HashMap<String, String>();
	    		  WebDriver wd = (WebDriver) exec.getBackingObject();  
	    	   
	    		  WebElement head = wd.findElement(By.xpath(headPath)); 
	    		  List<WebElement> ths = head.findElements(By.tagName("th")); 
	    		  WebElement tr01 = wd.findElement(By.xpath(valuePath));
	    		  List<WebElement> tds = tr01.findElements(By.tagName("td")); 
	    	   
	    	   for(int i=0;i<ths.size()-1;i++){
	    		   if(!(ths.get(i).getText().contains("Always Match"))){
	    			   // String key = ths.get(i).getText().replaceFirst("\\*", "").trim(); 
	    			   String key = ths.get(i).getText();
	    			   //String value = tds.get(i).getText().replaceFirst(",", "");
	    			   String value = tds.get(i).getText();
	    			   dealMap.put(key, value);   
	    		   }      
	    	   } 
	    	   System.out.println(dealMap);
	    	   return dealMap;
	    	  }else{
	    		  return null;
	    	  }
	    }
	    
	    /**
		 * Check AlwaysMatchSellerFCST icon
		 */	 
	    public void clickAlwaysMatch(){
			switchToMainWindow();
			log.info("Click Always Match FCST in Mgr worksheet.");
			click(MGR_DL_ALWMATCH_FCST1);			
		}
	    
	    //Make sure AlwaysMatchSellerFCST icon is checked
	    public void checkAlwaysMatchFCST(){	    	
	    	ViewMgrForecastPage AlwaysMatchFCST = new ViewMgrForecastPage(exec);
	    	if(!AlwaysMatchFCST.isChecked(MGR_DL_ALWMATCH_FCST1)){
	    		AlwaysMatchFCST.clickAlwaysMatch();
	    	}else{
	    		return;
	    	}
	    }
	    
	    public boolean verifyDealList(Map<String, String> sellerUpdatedDeal, Map<String, String> mgrDeal){
	    	int flag = 0;
	    	
	    	for(String key:sellerUpdatedDeal.keySet()){
	    		System.out.println("==================");
	    		System.out.println(sellerUpdatedDeal.get(key));
	    		System.out.println(mgrDeal.get(key));
	    		System.out.println("==================");
	    		boolean boo = mgrDeal.get(key).contains(sellerUpdatedDeal.get(key));
	    		if(!boo){
	    			flag = flag+1;
	    		}
	    	}
	    	if(flag==0)
	    		return true;
	    	else
	    		return false;
	    }

}
