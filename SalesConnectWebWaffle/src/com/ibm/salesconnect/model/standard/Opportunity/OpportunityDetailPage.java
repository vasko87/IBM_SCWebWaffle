/**
 * 
 */
package com.ibm.salesconnect.model.standard.Opportunity;

import java.util.List;

import org.openqa.selenium.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Element;
import com.ibm.atmn.waffle.core.Executor;
import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.StandardDetailPage;
import com.ibm.salesconnect.model.partials.ActivitiesHistorySubpanel;
import com.ibm.salesconnect.model.partials.ActivitiesSubpanel;
import com.ibm.salesconnect.model.partials.ConnectionsBusinessCard;
import com.ibm.salesconnect.model.partials.ConnectionsCommunityTab;
import com.ibm.salesconnect.model.partials.ContactSelectPopup;
import com.ibm.salesconnect.model.partials.ContactSubpanel;
import com.ibm.salesconnect.model.partials.DocumentsSubpanel;
import com.ibm.salesconnect.model.partials.LineItemDetailPage;
import com.ibm.salesconnect.model.partials.LineItemsubpanel;
import com.ibm.salesconnect.model.partials.RecommendedDocsTab;
import com.ibm.salesconnect.model.partials.WinPlanTab;
import com.ibm.salesconnect.model.standard.LineItems.EditLineItem;
import com.ibm.salesconnect.model.standard.Note.CreateNotePage;
import com.ibm.salesconnect.objects.RevenueItem;

/**
 * @author timlehane
 * @date May 13, 2013
 */
public class OpportunityDetailPage extends StandardDetailPage {
	Logger log = LoggerFactory.getLogger(OpportunityDetailPage.class);

	/**
	 * @param exec
	 */
	public OpportunityDetailPage(Executor exec) {
		super(exec);
		Assert.assertTrue(isPageLoaded(), "Opportunity Detail page has not loaded within 60 seconds");	
	}

	//table[contains(@class,'list view')]//td[contains(text(),'0k')]
	//Selectors
	//public static String pageLoaded = "//table[@id='LBL_OPP_MAIN']";
	//public static String detailsFrame = "//iframe[@id='bwc-frame']";
	
	public static String displayedOpportunityNumber = "//span[@data-field='name']//div";
	public static String displayedOpportunityDescription = "//div[@data-name='description']//span[@class='detail']//div";
	public static String SolutionCreate = "//span[@sugar='slot1b']";
	public static String SolutionEdit = "//div[@id='list_subpanel_opportun_revenuelineitems']//span[@name='LEVEL15-MSC']";
	public static String Amount = "//div[@id='list_subpanel_opportun_revenuelineitems']//span[@sugar='slot3b']";
	public static String documentsLink = "//span[@id='show_link_documents']/a";
	public static String hideDocumentsLink = "//span[@id='hide_link_documents']/a";
	public static String activitiesLink = "//span[@id='show_link_activities']/a";
	public static String activitiesHistoryLink = "//span[@id='show_link_history']/a";
	public static String contactsLink = "//div[@id='subpanel_title_contacts']//span[@id='show_link_contacts']/a";
	public static String contactsSubpanelLoaded = "//a[@id='opportunities_contacts_createcontact_button']";
	public static String createContactSubpanelLoaded = "//input[@id='first_name']";
	
	//Selectors
	public static String editOpportunity = "//*[@id='edit_button']";
	public static String collapseAdvanced = "//img[@id='detailpanel_1_img_hide']";
	public static String contactSelectButton = "//*[@id='opportunities_contacts_select_button']";
	
	//LineItemSubpanelSelectors
	public static String rliSubPanel = "//div[@data-subpanel-link='opportun_revenuelineitems']";
	public static String rliSubPanelTable = rliSubPanel + "//table";
	static String rliCreateLink = rliSubPanel + "//a[@name='create_button']";
	public static String rliEditLink = "//a[@id='opportun_revenuelineitems_edit_1']";
	public static String getFirstLineItem = rliSubPanelTable + "//tr[1]/td[2]//a";
	public static String getLineItemDropdown(int i) {return rliSubPanelTable + "//tr[" + String.valueOf(i) + "]/td[9]//a[@data-toggle='dropdown']";}
	public static String firstLineItemDropdown = getLineItemDropdown(1);
	public static String getLineItemDropdownEditButton(int i){return rliSubPanelTable + "//tr[" + String.valueOf(i) + "]/td[9]//a[@name='edit_button']";}
	public static String firstLineItemDropdownEditButton = getLineItemDropdownEditButton(1);
	public static String getLineItemDropdownDeleteButton(int i){return rliSubPanelTable + "//tr[" + String.valueOf(i) + "]/td[9]//a[@name='delete_button']";}
	public static String firstLineItemDropdownDeleteButton = getLineItemDropdownDeleteButton(1);
	public static String getFirstRoadmapText = rliSubPanelTable + "//tr[1]/td[4]";
	public static String getFirstOfferingText = rliSubPanelTable + "//tr[1]/td[2]//a";
	public static String getFirstOfferingAmount = rliSubPanelTable + "//tr[1]/td[3]/span/div";
	public static String getFirstOfferingAmountEdit = rliSubPanelTable + "//tr[1]/td[3]//input";
	
	//Alert Selectors
	public static String alertConfirmButton = "//div[@id='alerts']//a[@data-action='confirm']";

	public static String clientName = "//span[@id='account_id']";

	public static String ownerBusinessCard = "//span[@id='opportunityOwner' or @id='businesscardOpportunityOwner']";
	public String additionalTeamMember(String userName) {return  "//span[@id='additional_users']//a[contains(text(),'" + userName + "')]";};
	public String allTeamMembers(String number) {return "//span[@id='additional_users']//span["+number+"]//span[@class='vcard']";};
	public static String numTeamMembers = "//span[@id='additional_users']//a";
	public static String updatesTab = "//div[@id='Opportunities_detailview_tabs']//a//*[text()='Updates']";
	public static String feedForTheseEntries = "//a[contains(text(),'Feed for these entries')]";
	public static String statusTextArea = "//textarea[@id='gadget_MicroblogsActivityStream-postBoxTextArea']";
	public static String microBlogStatusTextArea = "//div[@id='mentionstextAreaNode_0']";
	public static String postStatus = "//*[@id='gadget_MicroblogsActivityStream-postBoxPostLink']";
	public static String microBlogPostStatus = "//a[@dojoattachpoint='commentPostActoin']";
	public static String microBlogJoinCommunity = "//*[@id='scdijit_dijit_form_Button_0_label']";
		
	public String status(String status) { return "//div[contains(text(),'"+ status +"')]";};
	public String displayTotalAmountText = "//div[@id='list_subpanel_opportun_revenuelineitems']/table/tbody/tr[3]/td[2]";
	
	public String totalAmount = "//span[@id='amount' and contains(text(),'0k USD')]";
	public String userHyperLink(String userName) { return "//a[@class='fn url']//div[contains(text(),'"+ userName +"')]";};
	
	public static String followOpportunity = "//span[@title='Click to follow the opportunity']";
	public static String unfollowOpportunity = "//span[@title='Click to stop following the opportunity']";
	public static String unfollowOpportunityConfirmation = "//*[@id='scdijit_dijit_form_Button_0_label']";	
	
	public static String connectionsCommunityTab = "//div[@id='Opportunities_detailview_tabs']//a//*[text()='Community']";
	public static String overviewTab = "//div[@id='Opportunities_detailview_tabs']//a//*[text()='Opportunity overview']";
	
	//Updates tab
	public static String opptyUpdatesTab = "//div[@id='Opportunities_detailview_tabs']//li[2]";
	public static String opptyUpdatesLoader = "//div[text()='Recent Discussion']";
	public static String followOppty = "//span[@title = 'Click to follow the opportunity']";
	public static String stopFollowingOppty = "//span[@title = 'Click to stop following the opportunity']";
	public static String confirmStopFollowing = "//span[@id='scdijit_dijit_form_Button_0_label']";
	public static String eventsIFrame = "//iframe[contains(@id,'gadget_OpptyActivityStreamContainer')]";
	public static String refreshStream = "//a[@id='com_ibm_social_as_gadget_refresh_RefreshButton_0']";
	
	//Win Plan Tab
	public static String winPlanTab = "//ul[@id='opportunity_tabs_panel']//a[contains(text(),'Win Plan')]";
	
	//Recommended documents tab	
	public static String recommendedDocsTab = "//*[@id='opportunity_tabs_panel']//a[contains(text(),'Recommended documents')]";
	public static String recommendedDocsTabLoader = "//input[@id='df_searchButton']";
	
	//Favorites
	public static String MyFavouritesCheckbox = "//[@id='favorites_only_basic']";
	
	
	//Methods
		/**
		 * Returns the displayed RLI total amount number
		 * @return displayed RLI total amount number
		 */
		public String getdisplayedTotalAmount(){
			return getObjectText(displayTotalAmountText);
		}
	
	//Methods
	/**
	 * Returns the displayed opportunity number
	 * @return displayed opportunity number
	 */
	public String getdisplayedOpportunityNumber(){
		return getObjectText(displayedOpportunityNumber);
	}
	
	/**
	 * 
	 */
	public String getDisplayedOpportunityDescription(){
		return getObjectText(displayedOpportunityDescription);
	}
	
	public void selectFavoriteOppCheckbox(){
		click (MyFavouritesCheckbox);
		log.info("Check Opportunity to My Favorites");
	}
	public void verifyLineItemCreated(RevenueItem rli){
		Assert.assertTrue(getObjectText(getFirstOfferingText).contains(rli.sFindOffering), "Offering Does not Match");
	}
	
	public void verifyLineItemEdited(RevenueItem rli){
		Assert.assertTrue(getObjectText(getFirstOfferingText).contains(rli.sFindOffering), "Offering Does not Match");
		Assert.assertTrue(getObjectText(getFirstOfferingAmount).contains(rli.getRLIAmount().substring(0, 1)), "Amount Does not Match");
	}
	
	public void verifyLineItemDeleted(RevenueItem rli){
		if(checkForElement(getFirstOfferingText))
			Assert.assertFalse(getObjectText(getFirstOfferingText).contains(rli.sFindOffering), "The first listed offering matches the offering that was supposed to be deleted");
	}
	
	public LineItemDetailPage selectEditRli(){
		showRLISubpanel();
		click(getFirstLineItem);
		return new LineItemDetailPage(exec);
	}
	
	public void EditRli(RevenueItem rli){
		showRLISubpanel();
		enableEditFirstLineItem();
		scrollElementToMiddleOfBrowser(getFirstOfferingAmountEdit);
		type(getFirstOfferingAmountEdit, rli.sRevenueAmount);
		triggerChange( "css=input[name=\"revenue_amount\"]");
	}
	
	public void enableEditFirstLineItem(){
		click(firstLineItemDropdown);
		click(firstLineItemDropdownEditButton);
	}
	
	public EditLineItem selectCreateRli(){
		showRLISubpanel();
		click(rliCreateLink);
		return new EditLineItem(exec);
	}
	
	public void deleteRLI(){
		//waitForPageToLoad(pageLoaded);
		showRLISubpanel();	
		deleteRLI(1);
		//acceptRLIDelete();
		
	}
	
	public void acceptRLIDelete(){
		click(alertConfirmButton);
	}
	
	public void deleteRLI(int i){
		click(firstLineItemDropdown);
		click(firstLineItemDropdownDeleteButton);
		acceptRLIDelete();
		
		sleep(10);
		waitToUnlink();
	}
	
	public DocumentsSubpanel openDocumentsSubpanel(){
		scrollToBottomOfPage();
		if(isPresent(documentsLink)){
			clickJS(documentsLink);
			log.info("Opening Documents subpanel");
		}
		
		else{
			log.info("Documents subpanel already open");
		}
		return new DocumentsSubpanel(exec);
	}
	
	public void closeRLISubpanel(){
		if(isVisible(rliSubPanelTable)){
			click(rliSubPanel);
			log.info("Closing RLI subpanel");
		}
		
		else{
			log.info("RLI subpanel already closed");
		}
	}
	
	public void showRLISubpanel(){
		if(!isVisible(rliSubPanelTable)){
			click(rliSubPanel);
			log.info("Opening RLI subpanel");
		}
		
		else{
			log.info("RLI subpanel already open");
		}
	}
	
	public void closeDoucmentsSubpanel(){
		if(isPresent(hideDocumentsLink)){
			click(hideDocumentsLink);
			log.info("Closing Documents subpanel");
		}
		
		else{
			log.info("Documents subpanel already closed");
		}
	}
	
	public ContactSubpanel openContactsSubpanel(){
		//If Document Subpanel link is off the page then it sometimes cant be found, so need to close RLI Subpanel first
		closeRLISubpanel();
		if(isPresent(contactsLink)){
			clickWithScrolling(contactsLink);
			log.info("Opening Contacts subpanel");
		}
		else{
			log.info("Contacts subpanel already open");
		}
		waitForSubpanelToLoad(contactsSubpanelLoaded);
		return new ContactSubpanel(exec);
	}
	
	/**
	 * @deprecated
	 * @return
	 */
	public EditOpportunityPage openEditOpportunity(){
		scrollElementToMiddleOfBrowser(editOpportunity);
		click(editOpportunity);
		return new EditOpportunityPage(exec);
	}
	
	
	/**
	 * Open the select contact popup
	 * @return select contact object
	 */
	public ContactSelectPopup openSelectContact(){
		click(contactSelectButton);
		getPopUp();
		return  new ContactSelectPopup(exec);
	}
	
	public ActivitiesSubpanel openCreateActivitiesSubPanel(){
		switchToMainWindow();
		scrollElementToMiddleOfBrowser(activitiesLink);
		click(activitiesLink);
		return new ActivitiesSubpanel(exec);
	}

	/**
	 * @param user1
	 */
	public void verifyBusinessCard(User user1) {
		//click(contactsLink);
		ConnectionsBusinessCard connectionsBusinessCard = openConnectionsBusinessCard(ownerBusinessCard,exec);
		connectionsBusinessCard.verifyBusinessCardContents(user1);	
	}
	
	public void verifyTeamBusinessCard(User user) {
		if (isPresent(additionalTeamMember(user.getDisplayName()))) {
			openConnectionsBusinessCard(additionalTeamMember(user.getDisplayName()),exec).verifyBusinessCardContents(user);
		}
		else{
			String userPref = user.getDisplayName().substring(user.getDisplayName().indexOf(" "), user.getDisplayName().length());
		openConnectionsBusinessCard(additionalTeamMember(userPref),exec).verifyBusinessCardContents(user);
		}
	}
	
	public void verifyAllTeamBusinessCard(){
		List<Element> selectors = exec.getElements(numTeamMembers);
						
		for(int x = 0; x<selectors.size();x++){
			Element ele = selectors.get(x);
			String email = ele.getText().substring(0, ele.getText().indexOf(" ")) + "@tst.ibm.com";
			
			openConnectionsBusinessCard(allTeamMembers(String.valueOf(x+1)),exec).verifyBusinessCardContents(email);
			mouseHoverSalesConnect(ownerBusinessCard);
		}
		
	}
	
	public int getTeamMemberRowNumber(String sUser){
		int index = 0;
		
		String start = sUser.substring(0, sUser.indexOf(" "));
		String end = sUser.substring(sUser.indexOf(" ") + 1, sUser.length());
		String sUserFull = start + " " + "(" + start + ") " + end;
		
		List<Element> selectors = exec.getElements(numTeamMembers);
		
		for(int x=0; x<selectors.size(); x++){
			Element ele = selectors.get(x);
			String user = ele.getText();
			
			if(user.equals(sUserFull)){
				index = x+3;
				break;
			}
			if(user.equals(sUser)){
				index=x+3;
				break;
			}
		}
		
		
		return index;
	}

	/**
	 * @return
	 */
	public LineItemsubpanel openLineItemsSubpanel() {
		if(getObjectAttribute(contactsLink, "style").equalsIgnoreCase(GC.notDisplayed)){
			click(contactsLink);
		}
		return new LineItemsubpanel(exec);
	}
	
	/**
	 * Returns the displayed LineItem total amount
	 * @return displayed RLI total amount
	 */
	public String displayTotalAmountText(){
		return getObjectText(displayTotalAmountText);
	}
	
	/**
	 * @return ACtivitiesHistorySubpanelobject
	 */
	public ActivitiesHistorySubpanel openActivitiesHistorySubpanel() {
		if(!getObjectAttribute(activitiesHistoryLink, "style").equalsIgnoreCase(GC.notDisplayed)){
			clickWithScrolling(activitiesHistoryLink);
		}
		return new ActivitiesHistorySubpanel(exec);
	}

	/**
	 * @return ClientName
	 */
	public String getClientName(){
		return getObjectText(clientName);
	}
	
	/**
	 * Opens Opportunity Updates Tab
	 */
	public void openUpdatesTab(){
		scrollToTopOfOpportunity();
		click(updatesTab);
		sleep(2);
		if(isPresent(microBlogJoinCommunity)){
			clickAt(microBlogJoinCommunity);
		}
	}
	
	/**
	 * Opens Opportunity WinPlan Tab
	 */
	public WinPlanTab openWinPlanTab(){
		scrollToTopOfOpportunity();
		click(winPlanTab);
		return new WinPlanTab(exec);
	}
	
	/**
	 * Opens Opportunity Overview Tab
	 */
	public void openOverviewTab(){
		scrollToTopOfOpportunity();
		click(overviewTab);
	}
	
	/**
	 * Opens the tab "Connections Community"
	 */
	public ConnectionsCommunityTab openConnectionsCommunityTab(){
		scrollToTopOfOpportunity();
		click(connectionsCommunityTab);
		return new ConnectionsCommunityTab(exec);
	}
	
	/**
	 * Opens the tab "Recommended documents"
	 */
	public RecommendedDocsTab openRecommendedDocsTab(){
		scrollToTopOfOpportunity();
		click(recommendedDocsTab);
		return new RecommendedDocsTab(exec);
	}

	
	/**
	 * Clicks on Follow to Follow the Opportunity
	 */
	public void followOpportunity(){
		if(!isTextPresent(GC.gsStopFollowing)){
			sleep(2);
			click(followOpportunity);
			if(isPresent(followOpportunity)){
				click(followOpportunity);
			}
		}
	}
	
	/**
	 * Clicks on Stop Following to Unfollow the Opportunity
	 */
	public void unfollowOpportunity(){
		if(isTextPresent(GC.gsStopFollowing)){
			sleep(2);
			click(unfollowOpportunity);
			if(isPresent(unfollowOpportunity)){
				click(unfollowOpportunity);
				click(unfollowOpportunityConfirmation);
			}
		}
	}
	
	/**
	 * Scrolls to the Top of the Page in case some elements are off screen
	 */
	public void scrollToTopOfOpportunity(){
		scrollToTopOfPage();
	}
	
	/**
	 * Verifys that Opportunity AS Loaded Correctly
	 */
	public boolean verifyActivityStream(){
		if (isTextPresent("Opportunity Events")&&isTextPresent("Recent Discussion")){
			return true;
		}return false;
	}
	
	/**
	 * Post a Status in the Main Status Text Box
	 */
	public void postStatus(String Status){
		scrollElementToMiddleOfBrowser(statusTextArea);
		clickAt(statusTextArea);
		type(statusTextArea,Status);
		click(postStatus);

	}
	
	/**
	 * Posts a status in the Micro Blog Status Box
	 */
	public void postStatusInMicroBlog(String Status){
		if(isPresent(microBlogStatusTextArea)){
			log.info("Micro Blog Status Text Area Found");
			mouseHover(microBlogStatusTextArea);
			sendKeys(microBlogStatusTextArea,Status);
			sleep(3);
			sendKeys(microBlogStatusTextArea, Keys.ARROW_DOWN);
			sendKeys(microBlogStatusTextArea, Keys.RETURN);
			click(microBlogPostStatus);
		}
		else log.info("Micro Blog Status Text Area NOT Found");
	}
	
	/**
	 * Verify that Status Message appears 
	 */
	public boolean verifyStatus(String Status){
		sleep(5);
		return isPresent(status(Status));
	}
	
	public boolean verifyTotalAmount(String totalAmount){
		
		if (isTextPresent(totalAmount))	{
			return true;
		}

		return false;
	}
	
	/**
	 * Verify that User Hyper Link appears in Status Message
	 */
	public boolean verifyUserHyperLink(String userName){
		return isPresent(userHyperLink(userName));
	}
	
	public void opptyUpdates(){
		scrollToTopOfOpportunity();
		click(opptyUpdatesTab);
		waitForPageToLoad(opptyUpdatesLoader);
		if(isPresent(microBlogJoinCommunity)){
			clickAt(microBlogJoinCommunity);
		}
	}
	
	public boolean isOpptyBeingFollowed(){
		if(isPresent(followOppty)){
			log.info("Opportunity is not being followed");
			return false;
		}
		else if(isPresent(stopFollowingOppty)){
			log.info("Opportunity is being followed");
			return true;
		}
		else{
			Assert.assertTrue(false,"Opportunity following status is not visible, aborting test");
		}
		return false;
	}
	
	public void followOppty(){
		if(isPresent(followOppty)){
			log.info("Clicking to follow Opportunity");
			scrollElementToMiddleOfBrowser(followOppty);
			click(followOppty);
			scrollElementToMiddleOfBrowser(eventsIFrame);
			switchToFrame(eventsIFrame);
			waitForSubpanelToLoad(refreshStream);
			switchToMainWindow();
		}
		else if(isPresent(stopFollowingOppty)){
			log.info("Opportunity is already being followed, taking no action");
		}
		else {
			log.info("Neither follow nor stop following links are present, failing test");
			Assert.assertTrue(false);
		}
		
	}
	
	public void stopFollowingOppty(){
		if(isPresent(stopFollowingOppty)){
			click(stopFollowingOppty);
			click(confirmStopFollowing);
			log.info("Clicking to stop following Opportunity");
		}
		else if(isPresent(followOppty)){
			log.info("Opportunity is already not being followed, taking no action");
		}
		else {
			log.info("Neither follow nor stop following links are present, failing test");
			Assert.assertTrue(false);
		}
	}
	
	public void checkEvents(String[] eventList) {
		switchToFrame(eventsIFrame);
		if (eventsPresent()) {
			for (int i = 0; i < eventList.length; i++) {
				if (!checkEventInActivityStrem(eventList[i], 11)) {				
					Assert.assertTrue(false, "Event "+eventList[i]+" has not been found in the activity stream");
				}
				else {
					log.info("Event " +eventList[i]+ " has been found in the activity stream");
				}
			}
		}
		else {
			Assert.assertTrue(false, "Events are not visible in activity stream after waiting");
		}
}
	
	public Boolean checkEventInActivityStrem(String Event, int numberOfEvents){
			for (int j = 1; j < numberOfEvents; j++) {
				if (isPresent("//ul[@id='asPermLinkAnchor']/li["+j+"]//div[contains(@class,'lotusPostAction')]")) {
					if(getObjectText("//ul[@id='asPermLinkAnchor']/li["+j+"]//div[contains(@class,'lotusPostAction')]").contains(Event)){
						return true;
					}
				}
			}

		return false;	
	}
	
	public Boolean eventsPresent(){
		for (int i = 0; i < 30; i++) {
			if(isPresent("//ul[@id='asPermLinkAnchor']/li[1]//div[contains(@class,'lotusPostAction')]")){
				return true;
			}
			if (isPresent(refreshStream)) {
				try {
					click(refreshStream);
				
				} catch (Exception e) {
					log.info("Refresh button not clickable, Waiting and trying again");
					sleep(10);
					clickAt(refreshStream);
				}
			
			}
			
		}
		return false;
	}

	public EditLineItem clickOnEditRli() {
		click(rliEditLink);
		return new EditLineItem(exec);
	}
	
	public CreateOpportunityPage openEditPage(){
		click(EditButton);
		return new CreateOpportunityPage(exec);
	}

	
}
