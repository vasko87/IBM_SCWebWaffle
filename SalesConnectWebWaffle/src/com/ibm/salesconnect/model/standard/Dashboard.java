/**
 * 
 */
package com.ibm.salesconnect.model.standard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardPageFrame;
import com.ibm.salesconnect.model.standard.Call.CreateCallPage;
import com.ibm.salesconnect.model.standard.Call.ViewCallPage;
import com.ibm.salesconnect.model.standard.Client.CreateClientPage;
import com.ibm.salesconnect.model.standard.Client.TerritoryAnalyticsPage;
import com.ibm.salesconnect.model.standard.Client.ViewClientPage;
import com.ibm.salesconnect.model.standard.Collab.ActivityStreamFrame;
import com.ibm.salesconnect.model.standard.Collab.ClientActivityStreamFrame;
import com.ibm.salesconnect.model.standard.Collab.ClientMicroBloggingFrame;
import com.ibm.salesconnect.model.standard.Collab.ManageEventsDialog;
import com.ibm.salesconnect.model.standard.Collab.OpportunityActivityStreamFrame;
import com.ibm.salesconnect.model.standard.Collab.OpportunityMicroBloggingFrame;
import com.ibm.salesconnect.model.standard.Contact.CreateContactPage;
import com.ibm.salesconnect.model.standard.Contact.ViewContactPage;
import com.ibm.salesconnect.model.standard.Forecast.CreateAdjustmentPage;
import com.ibm.salesconnect.model.standard.Forecast.Manager.ViewMgrForecastPage;
import com.ibm.salesconnect.model.standard.Forecast.Seller.ViewSellerForecastPage;
import com.ibm.salesconnect.model.standard.Home.HomePage;
import com.ibm.salesconnect.model.standard.Home.UserProfilePage;
import com.ibm.salesconnect.model.standard.Lead.CreateLeadPage;
import com.ibm.salesconnect.model.standard.Lead.ViewLeadPage;
import com.ibm.salesconnect.model.standard.Note.CreateNotePage;
import com.ibm.salesconnect.model.standard.Note.ViewNotePage;
import com.ibm.salesconnect.model.standard.Opportunity.CreateOpportunityPage;
import com.ibm.salesconnect.model.standard.Opportunity.QuickCreateOpportunityPage;
import com.ibm.salesconnect.model.standard.Opportunity.ViewOpportunityPage;
import com.ibm.salesconnect.model.standard.Task.CreateTaskPage;
import com.ibm.salesconnect.model.standard.Task.SearchEmployeesPage;
import com.ibm.salesconnect.model.standard.Task.ViewTaskPage;

/**
 * @author timlehane
 * @date Apr 22, 2013
 */
public class Dashboard extends StandardPageFrame{
	Logger log = LoggerFactory.getLogger(Dashboard.class);
	
	
	/**
	 * @param exec
	 */
	public Dashboard(Executor exec) {
		super(exec);
		// TODO Auto-generated constructor stub
		isPageLoaded();
	}

	/* (non-Javadoc)
	 * @see com.ibm.ecm.product.model.StandardPageFrame#isPageLoaded()
	 */
	@Override
	public boolean isPageLoaded() {
		// TODO Auto-generated method stub
		
		/**check for element on page**/
		return waitForPageToLoad(pageLoaded);
	}

	
	
	//Selectors
	public static String pageLoaded = "//*[@id='add_page']";
	public static String mainFrame = "//*[@id='bwc-frame']";
	
	public static String openHomePage = ".//*[@class='cube btn btn-invisible btn-link']/img";
		
	public static String quickCreateClientPage = "//*[@track='click:quickCreate-Accounts']";
	public static String createClientPage = "//*[@data-navbar-menu-item='LNK_NEW_ACCOUNT']";
	public static String viewClientPage = "//*[@data-navbar-menu-item='LNK_ACCOUNT_LIST']";
	public static String territoryAnalyticsPage = "//*[@data-navbar-menu-item='LNK_ACCOUNT_TERRITORY_ANALYTICS']";
	public static String industryPage = "//*[@id='moduleTab_Allibm_ISPIndustry']";
	public static String showMore = "//li[@id='moduleMenuOverFlowMoreAll']/a";
	public static String more = "//li[@id='moduleTabExtraMenuAll']/a/span";
	
	public static String createContactPage = "//*[@data-navbar-menu-item='LNK_NEW_CONTACT']";
	public static String viewContactPage = "//*[@data-navbar-menu-item='LNK_CONTACT_LIST']";
	public static String createContactPagelower = "//*[@data-navbar-menu-item='LNK_NEW_CONTACT']";
	
	public static String quickCreateOpportunityPage = "//*[@track='click:quickCreate-Opportunity']";
	public static String createOpportunityPage = "//*[@data-navbar-menu-item='LNK_NEW_OPPORTUNITY']";
	public static String viewOpportunityPage = "//*[@data-navbar-menu-item='LNK_OPPORTUNITY_LIST']";
	
	public static String createTaskPage = "//*[@data-navbar-menu-item='LNK_NEW_TASK']";
	public static String viewTaskPage = "//*[@data-navbar-menu-item='LNK_TASK_LIST']";
	public static String createTaskPagelower = "//*[@data-navbar-menu-item='LNK_NEW_TASK']";
	
	public static String createCallPage = "//*[@data-navbar-menu-item='LNK_NEW_CALL']";
	public static String viewCallPage = "//*[@data-navbar-menu-item='LNK_CALL_LIST']";
	public static String createCallPagelower = "//*[@data-navbar-menu-item='LNK_NEW_CALL']";
	public static String viewCallPagelower = "//*[@data-navbar-menu-item='LNK_CALL_LIST']";
	
	public static String createNotePage = "//*[@data-navbar-menu-item='LNK_NEW_NOTE']";
	public static String viewNotePage = "//*[@data-navbar-menu-item='LNK_NOTE_LIST']";
	public static String createNotePagelower = "//*[@data-navbar-menu-item='LNK_NEW_NOTE']";
	
	public static String createLeadPage = "//*[@data-navbar-menu-item='LNK_NEW_LEAD']";
	public static String viewLeadPage = "//*[@data-navbar-menu-item='LNK_LEAD_LIST']";
	public static String createLeadPagelower = "//*[@data-navbar-menu-item='LNK_NEW_LEAD']";
	public static String viewLeadPagelower = "//*[@data-navbar-menu-item='LNK_LEAD_LIST']";
	public static String leadListItem = "//li[@data-module='Leads']";
	
	
	public static String searchemployeesPage = "//*[@track='click:userAction-LBL_EMPLOYEES']";
	
	public static String userProfilePage = "//*[@track='click:userAction-LBL_PROFILE']";
	
	public static String globalSearchField = "//input[@class='search-query']";
	//public static String globalSearchField = "//*[@class='icon icon-search']";
	
	public static String globalSearchButton = "//*[@class='btn btn-inverse']";
		
	public static String activityStreamGadgetFrame = "//iframe[contains(@id,'__gadget_gadget-site-')]";
	
	public static String activtyStreamFrame = "//iframe[@title='Updates']";
	
	public static String opptyActivityStreamFrame = "//*[@id='__gadget_gadget_OpptyActivityStreamContainer']";
	
	public static String opptyMicroBloggingFrame = "//*[@id='__gadget_gadget_MicroblogsActivityStream']";
	
	public static String clientMicroBloggingFrame = "//div[@id='gadget_MicroblogsActivityStream']//iframe[contains(@id,'__gadget_gadget-site')]";
	
	public static String clientActivityStreamFrame = "//div[@id='gadget_ClientActivityStream']//iframe";
	
	public static String manageEventsButton ="//a[contains(@id,'ConnectionsASdashlet') and contains(text(),'Manage Events')]";
	
	public static String authorizeConnections ="//a[contains(text(),'Click here')]";
	
	//Tasks
	
	//Forecast
	public static String viewSellerForecastPage = "//*[@data-navbar-menu-item='LBL_VIEW_CADENCE']";	
	public static String CreateAdjButton = "//*[@data-original-title='Create']//i";
	public static String viewMgrForecastPage = "//*[@data-navbar-menu-item='LBL_MANAGER_WORKSHEET']";
	
	/**
	 * Opens the home page in the dashboard
	 * @return ViewContactPage object
	 */
	public HomePage openHomePage() {
		
		log.info("Open Home Page");
		click(openHomePage);
		return new HomePage(exec);		
	}
	
	/**
	 * Opens the create client page and returns the page object
	 * @return CreateClientPage
	 */
	public CreateClientPage openCreateClient() {
		switchToMainWindow();
		log.info("URL for Create Client Page" + getObjectAttribute(createClientPage, "href"));
		loadURL(getObjectAttribute(createClientPage, "href"));
		return  new CreateClientPage(exec);		
	}
	
	/**
	 * Opens the view client page and returns the page object
	 * @return ViewClientPage object
	 */
	public ViewClientPage openViewClient(){
		switchToMainWindow();
		log.info("URL for View Client Page" + getObjectAttribute(viewClientPage, "href"));
		loadURL(getObjectAttribute(viewClientPage, "href"));
		return new ViewClientPage(exec);
	}

	/**
	 * Opens the Industry page and returns the page object
	 * @return IndustryPage object
	 */
	public IndustryPage openIndustry(){
		switchToMainWindow();
		log.info("URL for Industry Page" + getObjectAttribute(industryPage, "href"));
		loadURL(getObjectAttribute(industryPage, "href"));
		return new IndustryPage(exec);
	}
	
	
	/**
	 * Opens the Territory Analytics page and returns the page object
	 * @return TerritoryAnalyticsPage object
	 */
	public TerritoryAnalyticsPage openTerritoryAnalytics(){
		switchToMainWindow();
		log.info("URL for Territory Analytics Page" + getObjectAttribute(territoryAnalyticsPage, "href"));
		loadURL(getObjectAttribute(territoryAnalyticsPage, "href"));
		return new TerritoryAnalyticsPage(exec);
	}
	
	/**
	 * Opens the create contact page and returns the page object
	 * @return CreateContactPage object
	 */
	public CreateContactPage openCreateContact() {
		switchToMainWindow();
		if(isPresent(createContactPage)){
		log.info("URL for Create Contact Page" + getObjectAttribute(createContactPage, "href"));
		loadURL(getObjectAttribute(createContactPage, "href"));
		}
		else{
			log.info("URL for Create Contact Page" + getObjectAttribute(createContactPagelower, "href"));
			log.info("Using lower case id");
			
			loadURL(getObjectAttribute(createContactPagelower, "href"));
		}

		return new CreateContactPage(exec);
	}
	
	/**
	 * Opens the view contact page and returns the page object
	 * @return ViewContactPage object
	 */
	public ViewContactPage openViewContact() {
		switchToMainWindow();
		log.info("URL for View Contact Page" + getObjectAttribute(viewContactPage, "href"));
		loadURL(getObjectAttribute(viewContactPage, "href"));
		return new ViewContactPage(exec);
	}
	
	/**
	 * Opens the quick create opportunity page and returns the page object
	 * @return QuickCreateOpportunityPage object
	 */
	public QuickCreateOpportunityPage openQuickCreateOpportunity() {
		switchToMainWindow();
		log.info("URL for Quick Create Opportunity Page" + getObjectAttribute(quickCreateOpportunityPage, "href"));
		click("//*[@id='quickCreate']");
		click(quickCreateOpportunityPage);
		loadURL(getObjectAttribute(quickCreateOpportunityPage, "href"));
		return new QuickCreateOpportunityPage(exec);
	}
	
	/**
	 * Opens the create opportunity page and returns the page object
	 * @return CreateOpportunityPage object
	 */
	public CreateOpportunityPage openCreateOpportunity() {
		switchToMainWindow();
		log.info("URL for Create Opportunity Page" + getObjectAttribute(createOpportunityPage, "href"));
		loadURL(getObjectAttribute(createOpportunityPage, "href"));
		return new CreateOpportunityPage(exec);
	}

	/**
	 * Opens the view opportunity page and returns the page object
	 * @return ViewOpportunityPage object
	 */
	public ViewOpportunityPage openViewOpportunity() {
		switchToMainWindow();
		log.info("URL for View Opportunity Page" + getObjectAttribute(viewOpportunityPage, "href"));
		String url = " ";
		for (int i = 0; i < 60; i++) {
			url = getObjectAttribute(viewOpportunityPage, "href");
			if (!url.equals(" ")) {
				loadURL(url);
				return new ViewOpportunityPage(exec);
			}
			sleep(1);
		}
		
		Assert.assertTrue(false, "Page url could not be found");
		return new ViewOpportunityPage(exec);
		
	}

	/**
	 * Opens create Task Page and returns the page object
	 * @return CreateTaskPage object
	 */
	public CreateTaskPage openCreateTask() {
		switchToMainWindow();
		if(isPresent(createTaskPage)){
		log.info("URL for Create Task Page" + getObjectAttribute(createTaskPage, "href"));
		loadURL(getObjectAttribute(createTaskPage, "href"));
		}
		else{
			log.info("URL for Create Task Page" + getObjectAttribute(createTaskPagelower, "href"));
			loadURL(getObjectAttribute(createTaskPagelower, "href"));
		}
		return new CreateTaskPage(exec);
	}

	/**
	 * Opens the view task page and returns the object
	 * @return ViewTaskPage object
	 */
	public ViewTaskPage openViewTask() {
		switchToMainWindow();
		log.info("URL for View Task Page" + getObjectAttribute(viewTaskPage, "href"));
		loadURL(getObjectAttribute(viewTaskPage, "href"));
		return new ViewTaskPage(exec);
	}
	
	/**
	 * Opens Log Call Page and returns the page object
	 * @return CreateCallPage object
	 */
	public CreateCallPage openCreateCall() {
		switchToMainWindow();
		if(isPresent(createCallPage)){
		log.info("URL for Create Call Page" + getObjectAttribute(createCallPage, "href"));
		loadURL(getObjectAttribute(createCallPage, "href"));
		}
		else{
			log.info("URL for Create Call Page" + getObjectAttribute(createCallPagelower, "href"));
			loadURL(getObjectAttribute(createCallPagelower, "href"));
		}
		return new CreateCallPage(exec);
	}

	/**
	 * Opens the view call page and returns the object
	 * @return ViewCallPage object
	 */
	public ViewCallPage openViewCall() {
		switchToMainWindow();
		if(isPresent(createCallPage)){
			log.info("URL for View Call Page" + getObjectAttribute(viewCallPage, "href"));
			loadURL(getObjectAttribute(viewCallPage, "href"));
			}
			else{
				log.info("URL for View Call Page" + getObjectAttribute(viewCallPagelower, "href"));
				loadURL(getObjectAttribute(viewCallPagelower, "href"));
			}
			return new ViewCallPage(exec);
	}
	
	/**
	 * Open the create note page and return the page object
	 * @return Create Note Page object
	 */
	public CreateNotePage openCreateNote() {
		switchToMainWindow();
		if(isPresent(createNotePage)){
		log.info("URL for Create Call Page" + getObjectAttribute(createNotePage, "href"));
		loadURL(getObjectAttribute(createNotePage, "href"));
		}
		else{
			log.info("URL for Create Call Page" + getObjectAttribute(createNotePagelower, "href"));
			loadURL(getObjectAttribute(createNotePagelower, "href"));
		}
		return new CreateNotePage(exec);
	}
	
	/**
	 * Opens the view note page and returns the object
	 * @return ViewNotePage object
	 */
	public ViewNotePage openViewNote() { 
		switchToMainWindow();
		log.info("URL for View Note Page" + getObjectAttribute(viewNotePage, "href"));
		loadURL(getObjectAttribute(viewNotePage, "href"));
		return new ViewNotePage(exec); 
	}
	
	/**
	 * Opens the Employee Search page and returns the object
	 * @return SearchEmployeesPage object
	 */
	public SearchEmployeesPage openSearchEmployeesPage() {
		switchToMainWindow();
		log.info("URL for Search Employees Page" + getObjectAttribute(searchemployeesPage, "href"));
		loadURL(getObjectAttribute(searchemployeesPage, "href"));
		return new SearchEmployeesPage(exec);
	}

	/**
	 * Opens the User Profile page and returns the object
	 * @return UserProfilePage object
	 */
	public UserProfilePage openUserProfilePage() {
		switchToMainWindow();
		log.info("URL for Search Employees Page" + getObjectAttribute(userProfilePage, "href"));
		loadURL(getObjectAttribute(userProfilePage, "href"));
		return new UserProfilePage(exec);
	}
	
	/**
	 * Types text into Search Field and clicks Enter
	 * @return GlobalSearchResultsPage
	 */
	public GlobalSearchResultsPage performGlobalSearch(String searchTerm) {
		switchToMainWindow();
		//waitForPageToLoad(globalSearchField);
		click(globalSearchField);
		type(globalSearchField, searchTerm);
		//sendKeys(globalSearchField, Keys.RETURN); //NOTE: causes strange behavior 
		click(globalSearchButton);
		return new GlobalSearchResultsPage(exec);
	}


	/**
	 * 
	 */
	public ActivityStreamFrame switchToActivityStreamFrame() {
		switchToMainWindow();	
		switchToFrame(mainFrame);
		scrollElementToMiddleOfBrowser(activtyStreamFrame);
		switchToFrame(activtyStreamFrame);
		return new ActivityStreamFrame(exec);
	}
	
	public OpportunityActivityStreamFrame switchToOpptyActivityStreamFrame() {
		if(isPresent(mainFrame)){
			switchToFrame(mainFrame);
		}
		this.scrollToTopOfPage();
		switchToFrame(opptyActivityStreamFrame);
		return new OpportunityActivityStreamFrame(exec);
	}
	
	public OpportunityMicroBloggingFrame switchToOpptyMicroBloggingFrame() {
		if(isPresent(mainFrame)){
			switchToFrame(mainFrame);
		}
		scrollElementToMiddleOfBrowser(opptyMicroBloggingFrame);
		switchToFrame(opptyMicroBloggingFrame);
		return new OpportunityMicroBloggingFrame(exec);
	}
	
	public ClientMicroBloggingFrame switchToClientMicroBloggingFrame() {
		if(isPresent(mainFrame)){
			switchToFrame(mainFrame);
		}
		scrollElementToMiddleOfBrowser(clientMicroBloggingFrame);
		switchToFrame(clientMicroBloggingFrame);
		return new ClientMicroBloggingFrame(exec);
	}
	
	public ClientActivityStreamFrame switchToClientActivityStreamFrame() {
		if(isPresent(mainFrame)){
			switchToFrame(mainFrame);
		}
		scrollElementToMiddleOfBrowser(clientActivityStreamFrame);
		switchToFrame(clientActivityStreamFrame);
		return new ClientActivityStreamFrame(exec);
	}
	
	public void switchToFrame(){
		if(isPresent(mainFrame)){
			switchToFrame(mainFrame);
		}
		switchToFrame(activtyStreamFrame);
	}
	
	public ManageEventsDialog openManageEventsDialog(){
		if(isPresent(mainFrame)){
			switchToFrame(mainFrame);
		}
		isPresent(manageEventsButton);
		click(manageEventsButton);
		return new ManageEventsDialog(exec);
	}

	
	public boolean clickAuthorizeConnections(){
		if(isPresent(activityStreamGadgetFrame)){
			switchToFrame(activityStreamGadgetFrame);
			if(isPresent(authorizeConnections)){
				click(authorizeConnections);
				// Successfully clicked "Click Here" to Authorize Connections
				return true;
			}
		}
		// Failed to click "Click Here" to Authorize Connections, user may already be authorized
		return false;
	}

	/**
	 * Opens the Create client page and returns the page object
	 * @return CreateLead object
	 */
	
	public CreateLeadPage openCreateLead() {
		switchToMainWindow();
		if(isPresent(createLeadPage)){
		log.info("URL for Create Lead Page" + getObjectAttribute(createLeadPage, "href"));
		loadURL(getObjectAttribute(createLeadPage, "href"));
		}
		else{
			log.info("URL for Create Lead Page" + getObjectAttribute(createLeadPagelower, "href"));
			loadURL(getObjectAttribute(createLeadPagelower, "href"));
		}
		return new CreateLeadPage(exec);
	
	}
	/**
	 * Opens the view lead page and returns the page object
	 * @return ViewLeadPage object
	 */

	public ViewLeadPage openViewLead() {
		switchToMainWindow();
		if(isPresent(viewLeadPage)){
			log.info("URL for View Call Page" + getObjectAttribute(viewLeadPage, "href"));
			loadURL(getObjectAttribute(viewLeadPage, "href"));
			}
			else{
				log.info("URL for View Call Page" + getObjectAttribute(viewLeadPagelower, "href"));
				loadURL(getObjectAttribute(viewLeadPagelower, "href"));
			}
			return new ViewLeadPage(exec);
	}
	
	/**
	 * Opens the Update Seller Forecast Worksheet page and returns the page object
	 * @return ViewSellerForecastPage object
	 */
	public ViewSellerForecastPage openSellerForecast() {
		switchToMainWindow();
		log.info("URL for Update Seller Forecast Worksheet" + getObjectAttribute(viewSellerForecastPage, "href"));
		loadURL(getObjectAttribute(viewSellerForecastPage, "href"));
		return ViewSellerForecastPage.getInstance();		
	}
	
//Forecast	  
	  public ViewSellerForecastPage openSellerForecastView(){
		  switchToMainWindow();
		  log.info("URL for View Seller Forecast Page" + getObjectAttribute(viewSellerForecastPage, "href"));  
		  String url = " ";  
		  url = getObjectAttribute(viewSellerForecastPage, "href");   
		  if (!url.equals(" ")) {
			  loadURL(url); 			  
		      return ViewSellerForecastPage.getInstance();
		  }else{
			  Assert.assertTrue(false, "Page url could not be found");   
		      return null;
		  }
	  }
	  
	  public ViewMgrForecastPage openMgrForecastView() {
		  switchToMainWindow();
		  log.info("URL for View Manager Forecast Page" + getObjectAttribute(viewMgrForecastPage, "href"));  
		  String url = " ";  
		  url = getObjectAttribute(viewMgrForecastPage, "href");   
		  if (!url.equals(" ")) {
			  loadURL(url);			  
			  return ViewMgrForecastPage.getInstance();
		  }else{   
			  Assert.assertTrue(false, "Page url could not be found");   
			  return null;
		  }
	  }
	  
	  public CreateAdjustmentPage clickCreateAdjButton(){
		  log.info("Open Create Adjustment Page");
		  click(CreateAdjButton);  
		  return CreateAdjustmentPage.getInstance();
	  }
	  
	  public void nevToEditLineItem(){
			//switchToWindowUrlContains("action=EditView"); //--svt4
			switchToWindowUrlContains("ibm_RevenueLineItems"); //--svt6		
		}
}
