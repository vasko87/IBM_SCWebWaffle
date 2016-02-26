/**
 * 
 */
package com.ibm.salesconnect.model.standard.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
//import org.openqa.selenium.JavascriptExecutor;
//import org.openqa.selenium.NoSuchElementException;
//import org.openqa.selenium.StaleElementReferenceException;
//import org.openqa.selenium.WebDriver;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchWindowException;
//import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Element;
import com.ibm.atmn.waffle.core.Executor;
import com.ibm.atmn.waffle.core.Executor.Alert;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.StandardPageFrame;
import com.ibm.salesconnect.model.partials.ConnectionsCommunityTab;
import com.ibm.salesconnect.model.partials.ContactSubpanel;
import com.ibm.salesconnect.model.partials.DocumentsSubpanel;
import com.ibm.salesconnect.model.partials.NotesSubpanel;
import com.ibm.salesconnect.model.partials.OpportunitySubpanel;
import com.ibm.salesconnect.model.partials.UserSelectPopup;

/**
 * @author timlehane
 * @date Apr 24, 2013
 */
public class ClientDetailPage extends StandardPageFrame {
	Logger log = LoggerFactory.getLogger(ClientDetailPage.class);
	/**
	 * @param exec
	 */
	public ClientDetailPage(Executor exec) {
		super(exec);
		Assert.assertTrue(isPageLoaded(), "Client detail page has not loaded within 60 seconds");
	}

	/* (non-Javadoc)
	 * @see com.ibm.salesconnect.model.StandardPageFrame#isPageLoaded()
	 */
	@Override
	public boolean isPageLoaded() {
		return waitForPageToLoad(pageLoaded);
	}

	
	//Selectors
	public static String pageLoaded = "//span[@id='name']";
	public static String detailsFrame = "//iframe[@id='bwc-frame']"; 
	public static String tabsFrame = "//iframe[@id='frame_predictive_buying_analytics']";
	
	public static String displayedClientName = "//span[@id='name']";
	public static String displayedBreadcrumbGU = "//span[@id='moduleTitleBreadCrumbLine']/descendant::a[contains(@href,'ccms_id')]";
	public static String addUsersSelectButton = "//*[@id='accounts_users_select_button']";
	public static String documentsLink = "//span[@id='show_link_documents']/a";
	public static String contactsLink = "//span[@id='show_link_contacts']/a";
	public static String contactsOpen = "//span[@id='show_link_contacts' and contains(@style,'none')]";
	public static String clientTeamOpenLink = "//span[@id='show_link_additional_users']/a";
	public static String clientTeamCloseLink = "//span[@id='hide_link_additional_users']/a";
	public static String clientTeamSubpanelNameColumn = "//*[@id='list_subpanel_additional_users']//a[contains(@href, 'CELL_ORDER_BY=full_name')]";
	//Selectors
	
	public static String collapseAdvanced = "//img[@id='detailpanel_1_img_hide']";
	public static String contactSelectButton = "//*[@id='opportunities_contacts_select_button']";
	public static String editOpportunity = "//*[@id='edit_button']";
		
	public static String clientOverviewTab = "//div[@id='Accounts_detailview_tabs']//*[contains(text(),'Client overview')]";
	public static String clientOverviewLoader = "//span[@id='name']";
	public static String clientUpdatesTab = "//div[@id='Accounts_detailview_tabs']//*[contains(text(),'Updates')]/..";
	public static String connectionsOAuthLink = "//*[@id='oAuthQuestion']/a";	
	public static String connectionsCommunityTab = "//div[@id='Accounts_detailview_tabs']//*[contains(text(),'Community')]/..";
	public static String communityTabClientNonMember = "//*[@id='createCommunityDiv' and contains(text(), 'You do not have access to this content because you are not a member of the client team or opportunity team. If you wish to request access please contact the Lead Client Representative.')]";
	public static String communityTabClientMemberNoCommunityMesg = "//*[@id='no_community_screen_description']";
	public static String communityTabClientMemberJoinCommunityMesg = "//*[@id='join_community_screen_description']";
	public static String communityTabClientMemberCommunityImg = "//div[@id='connectionsCommuntiyImage']/img";
	public static String joinCommunity = "//span[@id='scdijit_dijit_form_Button_0_label']";
	public static String createCommunityButton = "//*[@id='scdijit_sc_common_CreateCommunity_0']/descendant::span[@id='scdijit_dijit_form_Button_0']";
	public static String clientUpdatesLoader = "//div[text()='Recent Discussion']";
	public static String clientIBMSpendTab = "//div[@id='Accounts_detailview_tabs']//a//*[text()='Client IBM spend']";
	public static String clientIBMSpendTabLoader = "//a[text()='Software']";
	public static String installBaseTab = "//div[@id='Accounts_detailview_tabs']//a//*[text()='Install base']";
	public static String installBaseTabLoader = "//*[contains(text(),'System SW')]";
	public static String predictiveBuyingAnalyticsTab = "//div[@id='Accounts_detailview_tabs']//a//*[text()='Predictive buying analytics']";
	public static String predictiveBuyingAnalyticsTabLoader = "//table[@class='dojoxGridRowTable']";
	public static String clientTouchPointsTab = "//div[@id='Accounts_detailview_tabs']//a//*[text()='Client touch points']";
	public static String clientTouchPointsTabLoader ="//label[text()='Touch date']";
	public static String complaintsAndPMRSTab = "//div[@id='Accounts_detailview_tabs']//a//*[text()='Complaints and PMRs']";
	public static String complaintsAndPMRSTabLoader = "//th[@id='compType']";
	public static String intelligenceTab = "//div[@id='Accounts_detailview_tabs']//a//*[text()='Intelligence']";
	public static String cancelIntelligenceButton = "//button[@id='dijit_form_Button_1']";
	public static String intelligenceTabLoader = "//*[contains(text(),'Search OneSource directly')]";
	public static String referencesTab = "//div[@id='Accounts_detailview_tabs']//a//*[text()='References']";
	public static String referencesTabLoader = "//div[@class='dojoxGridRow']";
	
	public static String getClientTeamList = "//div[@id='list_subpanel_additional_users']//span//a[contains(@href,'Users&')]";
	public static String followClient = "//span[text()='Follow']";
	public static String stopFollowingClient = "//span[text()='Stop Following']";
	public static String confirmStopFollowing = "//span[@id='scdijit_dijit_form_Button_0_label']";
	
	public static String getRemoveUser(String sUser){ return "//a[contains(text(),'" + sUser +"')]/../../../..//a[contains(text(),'remove')]";}
	public static String checkUser(String sUser){ return "//a[contains(text(),'" + sUser + "')]";}
	public static String nextUserPageDisabled = "//div[@id='list_subpanel_additional_users']/descendant::button[@name='listViewNextButton' and @disabled='']";
	public static String nextUserPageEnabled = "//div[@id='list_subpanel_additional_users']/descendant::button[@name='listViewNextButton' and @onclick and not(@disabled)]";
	public static String createContact = "//a[@id='accounts_contacts_create_button']";
	
	public static String statusTextArea = "//textarea[@id='gadget_MicroblogsActivityStream-postBoxTextArea']";
	public static String postStatus = "//*[@id='gadget_MicroblogsActivityStream-postBoxPostLink']";
	public static String microBlogCreateCommunity = "//div[@id='Accounts_detailview_tabs']//*[contains(text(),'Community')]"; //"//*[@id='scdijit_dijit_form_Button_0_label']";
	
	public static String createOpportunitesLink = "//span[@id='show_link_opportunities']";
	public static String displayedOpportunityNumber = "//span[@sugar='slot1b']";
	
	public static String openCompletedActivities = "//span[@id='show_link_history']/a";
	
	public static String eventTypeSelector = "//span[@id='com_ibm_social_as_filter_FilterMenu_0']";
	public static String connectionsEvents = "//option[contains(text(),'Connections Community')]";
	public static String MyFavouritesIcon = "//span[@id='moduleTitleBreadCrumbLine']/h2/div/div";
	

//Method
	public void addClientToMyFavorites(){
	
		click (MyFavouritesIcon);// clicking on myFavourites Icon
		log.info("Contact Added to My Favorites");
	}
	
	//Tasks
	/**
	 * Returns the displayed client name
	 * @return displayed client name
	 */
	public String getdisplayedClientName(){
		return getObjectText(displayedClientName);
	}
	
	/**
	 * Returns the ccmsId from the breadcrumb
	 * @return displayed ccmsId
	 */
	public String getCcmsIdFromBreadcrumbGU(){
		String ccmsId = getObjectText(displayedBreadcrumbGU);
	    Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(ccmsId);
	    while(m.find()) {
	    	log.info("ccmsId=" + m.group(1));
	    	ccmsId = m.group(1);
	    }
		return ccmsId;
	}

	/**
	 * Adds User to Client Team
	 * 
	 */
	public boolean addUserToClientTeam(String sUser){
		
		UserSelectPopup userSelectPopup = openSelectUser();
		userSelectPopup.searchForUser(sUser);
		
		if(checkForElement(UserSelectPopup.addSelectedUsersButton)) { // added as sometimes doesn't detect it
			userSelectPopup.selectResult(sUser);
			return true;
		}
		else {
			Assert.assertTrue(false, "Failed to find the user");
		}
		return false;
	}
	
	public void removeUserFromClientTeam(String sUser){
		while(true){
			
			if(!checkUserInClientTeam(sUser)){
				if(checkForElement(nextUserPageEnabled)){
					clickWithScrolling(nextUserPageEnabled);
					waitForPageToLoad(pageLoaded);
				}
				else{
					break;
				}
			}
			else{
				click(getRemoveUser(sUser)); // failing
				try{
					acceptAlert();
				}
				catch(NoSuchWindowException nswe){
					log.debug("No accept alert appeared");
				}
				catch(NoAlertPresentException nape){
					log.debug("No accept alert appeared");
				} 
				waitForPageToLoad(pageLoaded);
				break;
			}
		}
	}
	
	public boolean checkUserInClientTeam(String sUser){
		List<String> clientTeamList = getClientTeamList();
		if(clientTeamList.contains(sUser)){
			return true;
		}
		else{
			return false;
		}
	}
	
	public List<String> getClientTeamList(){
		List<Element> clientTeamList = exec.getElements(getClientTeamList);
		List<String> clientTeamNameList = new ArrayList<String>();
		for(int x=0;x<clientTeamList.size();x++){
			clientTeamNameList.add(clientTeamList.get(x).getText());
		}
		
		return clientTeamNameList;
	}
	
	/**
	 * Open the select user popup for Client Team
	 * @return select user object
	 */
	private UserSelectPopup openSelectUser() {
		//Popup sometimes takes a while to load so needs an extra sleep
		//sleep(5);
		waitForElement(addUsersSelectButton);
		scrollElementToMiddleOfBrowser(addUsersSelectButton);
		click(addUsersSelectButton);
		getPopUp();
		return new UserSelectPopup(exec);
	}
	
	public DocumentsSubpanel openDocumentsSubpanel(){
		scrollToBottomOfPage();
		if(checkForElement(documentsLink)){
			try{
				click(documentsLink);
			}
			catch(ElementNotVisibleException enve){
				log.info("Documents subpanel already open");
			}
			log.info("Opening Documents subpanel");
		}
		else{
			log.info("Documents subpanel already open");
		}
		return new DocumentsSubpanel(exec);
	}
	
	public ContactSubpanel openContactsSubpanel(){
		if(!checkForElement(contactsOpen)){
			scrollElementToMiddleOfBrowser(contactsLink);
			click(contactsLink);
			log.info("Opening Contacts subpanel");
		}
		else{
			log.info("Contacts subpanel already open");
		}
		log.info("Checking if contacts subpanel has been opened");
		if(!isVisible(createContact)){
			scrollElementToMiddleOfBrowser(contactsLink);
			click(contactsLink);
		}
		waitForSubpanelToLoad(createContact);
		
		return new ContactSubpanel(exec);
	}
	
	
	public void openClientTeamSubpanel(){
		scrollElementToMiddleOfBrowser(clientTeamOpenLink);
		if(isVisible(clientTeamOpenLink)){
			try{
				click(clientTeamOpenLink);
			}
			catch(ElementNotVisibleException enve){
				log.info("Client Team subpanel already open");
				Assert.assertTrue(false, "Could not open Client Team subpanel, eventhough closed");
			} 
			catch (Exception ex) {
				Assert.assertTrue(false, "Could not open Client Team subpanel, eventhough it's closed");
			}
			log.info("Opening Client Team subpanel");
		}
		else{
			log.info("Client Team subpanel already open");
		}
	}
	
	
	/**
	 * Opens the tab "Client Overview"
	 */
	public void clientOverview(){
		click(clientOverviewTab);
		waitForPageToLoad(clientOverviewLoader);
	}
	
	/**
	 * Opens the tab "Client Updates"
	 */
	public void clientUpdates() {
		click(clientUpdatesTab);
		waitForPageToLoad(clientUpdatesLoader);
	}
	
	/**
	 * Clicks the Connections Authentication link (if displayed) 
	 * to get access to Updates tab content
	 */
	public void clickConnectionsOAuthLinkIfExists(){
		if (isVisible(connectionsOAuthLink)) {
			click(connectionsOAuthLink);
		} 
	}
	
	/**
	 * Opens the tab "Client Community tab"
	 */
	public void clientCommunity(){
		if (checkForElement(connectionsCommunityTab)){
			click(connectionsCommunityTab);
		}
//		if(checkForElement(microBlogCreateCommunity)){
//			clickAt(microBlogCreateCommunity);
//		}
	}
	
	/**
	 * Follows the client under "Client Updates"
	 * Client update should be open
	 */
	public void followClient(){
		if (checkForElement(stopFollowingClient)){
			log.info("Client is already being followed, taking no action");
		} else if(checkForElement(followClient)){
			click(followClient);
			log.info("Clicking to follow client");
		}
	//	else {
	//		log.info("Neither follow nor stop following links are present, failing test");
	//		Assert.assertTrue(false);
	//	}
	}
	
	/**
	 * Opens the completedActivitiesAndNotesSubpanel"
	 */
public NotesSubpanel opencompletedNotesAndActivitiesSubpanel(){
		click(openCompletedActivities);
		return new NotesSubpanel(exec);
	}    
	
	
	/**
	 * Stops following the client under "Client Update"
	 * Client update should be open
	 */
	public void stopFollowingClient(){
		if(checkForElement(stopFollowingClient)){
			click(stopFollowingClient);
			click(confirmStopFollowing);
			log.info("Clicking to stop following client");
		}
		else if(checkForElement(followClient)){
			log.info("Client is already not being followed, taking no action");
		}
		else {
			log.info("Neither follow nor stop following links are present, failing test");
			Assert.assertTrue(false);
		}
	}
	
	public boolean isClientBeingFollowed(){
		if(checkForElement(followClient)){
			log.info("Client is not being followed");
			return false;
		}
		else if(checkForElement(stopFollowingClient)){
			log.info("Client is being followed");
			return true;
		}
		else{
			Assert.assertTrue(false,"Client following status is not visible, aborting test");
		}
		return false;
	}

	/**
	 * Opens the tab "IBM Spend"
	 */
	public void clientIBMSpend(){
		click(clientIBMSpendTab);
		switchToFrame("//iframe[@id='frame_client_ibm_spend']");
		Assert.assertTrue(checkForElement(clientIBMSpendTabLoader), "Client IBM Spend Tab did not load successfully");
		switchToMainWindow();
		switchToFrame(detailsFrame);
	}
	
	/**
	 * Opens the tab "Install Base"
	 */
	public void installBase(){
		click(installBaseTab);
		switchToFrame("//iframe[@id='frame_install_base']");
		Assert.assertTrue(checkForElement(installBaseTabLoader), "Install Base Tab did not load successfully");
		switchToMainWindow();
		switchToFrame(detailsFrame);
	}
	
	/**
	 * Opens the tab "Buying Analytics"
	 */
	public void predictiveBuyingAnalytics(){
		click(predictiveBuyingAnalyticsTab);
		switchToFrame("//iframe[@id='frame_predictive_buying_analytics']");
		Assert.assertTrue(checkForElement(predictiveBuyingAnalyticsTabLoader), "Predictive Buying Analytics Tab did not load successfully");
		switchToMainWindow();
		switchToFrame(detailsFrame);
	}
	
	/**
	 * Opens the tab "Touch Points"
	 */
	public void clientTouchPoints(){
		click(clientTouchPointsTab);
		switchToFrame("//iframe[@id='frame_client_touch_points']");
		Assert.assertTrue(checkForElement(clientTouchPointsTabLoader), "Client Touch Points Tab did not load successfully");
		switchToMainWindow();
		switchToFrame(detailsFrame);
	}
	
	/**
	 * Opens the tab "Complaints and PMRs"
	 */
	public void complaintsAndPMRs(){
		click(complaintsAndPMRSTab);
		switchToFrame("//iframe[@id='frame_complaints_and_pmrs']");
		Assert.assertTrue(checkForElement(complaintsAndPMRSTabLoader), "Complaints and PMRs Tab did not load successfully");
		switchToMainWindow();
		switchToFrame(detailsFrame);
	}
	
	/**
	 * Opens the tab "Intelligence"
	 */
	public void intelligence(){
		click(intelligenceTab);
		switchToFrame("//iframe[@id='frame_intelligence']");
		try{
			Alert alert = exec.switchToAlert();
			alert.dismiss();

		}
		catch(ElementNotVisibleException enve){
			enve.printStackTrace();
		}
		Assert.assertTrue(checkForElement(intelligenceTabLoader), "Intelligence Tab did not load successfully");
		switchToMainWindow();
		switchToFrame(detailsFrame);
		
	}
	
	/**
	 * Opens the tab "References"
	 */
	public void refrences(){
		click(referencesTab);
		switchToFrame("//iframe[@id='frame_references']");
		Assert.assertTrue(checkForElement(referencesTabLoader), "References Tab did not load successfully");
		switchToMainWindow();
		switchToFrame(detailsFrame);
	}
	
	/**
	 * Opens the tab "Connections Community"
	 */
	public ConnectionsCommunityTab openConnectionsCommunityTab(){
		clickAt(connectionsCommunityTab);
		if(checkForElement(joinCommunity)){
			click(joinCommunity);
			click(connectionsCommunityTab);
		}
		return new ConnectionsCommunityTab(exec);
	}

	/**
	 * Clicks CommunityTab->Create Community Button.
	 * @returns: boolean if click was successful or not
	 */
	public boolean clickCommunityTabCreateCommunityButton(){
//		click(connectionsCommunityTab);
//		waitForPageToLoad(createCommunityButton);
		if(checkForElement(createCommunityButton)){
			click(createCommunityButton);
			return true;
		} 
		return false;
	}
	
	/**
	 * Checks is community tab-> Create button is visible
	 * @return: boolean if create button is displayed
	 */
	public boolean isCommunityTabCreateButtonDisplayed(){
		if (checkForElement(createCommunityButton)){
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if user is member of client team, on Community Tab content
	 * @return boolean if user is client team member for Community Tab
	 */
	public boolean isClientMemberCommunityTab(){
		if(checkForElement(communityTabClientNonMember)){
			log.info("User is not member of client team & can't access Community tab content");
			return false;
		}
		else if(checkForElement(communityTabClientMemberNoCommunityMesg)){
			log.info("User is member of client team, can access Community tab->Create a Community");
			return true;
		}
		else if(checkForElement(communityTabClientMemberJoinCommunityMesg)){
			log.info("User is member of client team, can access Community tab->Join Community");
			return true;
		}
		else if(checkForElement(communityTabClientMemberCommunityImg)){
			log.info("User is member of client team, can see Community tab content");
			return true;
		}
		return false;
	}
	
	/**
	 * Checks Community tab is displayed in Client Details Page
	 */
	public boolean isCommunityTabDisplayed(){
		if (checkForElement(connectionsCommunityTab)) {
			return true;
		} 
		return false;
	}	

	/**
	 * Opens the tab "Connections Community"
	 */
	public boolean openCommunityTab(){
		if (checkForElement(connectionsCommunityTab)) {
			click(connectionsCommunityTab);
			return true; // successfully executed
		} 
		return false; // failed to execute
	}
	
	/**
	 * Posts a status in Recent Discussions
	 */
	public void postStatus(String Status){
		click(statusTextArea);
		type(statusTextArea, Status);
		//sendKeys(statusTextArea,Status);
		//type(statusTextArea, Status);
		click(postStatus);

	}

	/**
	 * @return OpportunitySubpanel
	 */
	public OpportunitySubpanel openOpportunitySubpanel() {
		if(!getObjectAttribute(createOpportunitesLink, "style").contains(GC.notDisplayed)){
			click(createOpportunitesLink);
		}
		return new OpportunitySubpanel(exec);
	}
		
	/**
	 * Returns the displayed opportunity number
	 * @return displayed opportunity number
	 */
	public String getdisplayedOpportunityNumber(){
		sleep(5);
		return getObjectText(displayedOpportunityNumber);
	}
	
	/**
	 * Selects Connections Events in the Client Activity Stream
	 */
	public void selectConnectionsEvents(){
		if(checkForElement(eventTypeSelector)){
			click(eventTypeSelector);
			click(connectionsEvents);
		}
	}
}
