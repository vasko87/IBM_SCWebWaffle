/**
 * 
 */
package com.ibm.salesconnect.model.standard.Connections.Community;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardPageFrame;
import com.ibm.salesconnect.model.standard.Connections.Activities.CreateCommunityActivityPage;
import com.ibm.salesconnect.model.standard.Connections.Blogs.CreateCommunityBlogPage;
import com.ibm.salesconnect.model.standard.Connections.Bookmarks.CreateCommunityBookmarkPage;
import com.ibm.salesconnect.model.standard.Connections.Files.FilesMainPage;
import com.ibm.salesconnect.model.standard.Connections.Forums.CreateCommunityForumPage;
import com.ibm.salesconnect.model.standard.Connections.Wikis.CreateCommunityWikiPage;


/**
 * @author Administrator
 *
 */
public class CommunityDetailPage extends StandardPageFrame{
	Logger log = LoggerFactory.getLogger(CommunityDetailPage.class);

	/**
	 * @param exec
	 */
	public CommunityDetailPage(Executor exec) {
		super(exec);
		Assert.assertTrue(isPageLoaded(), "View Community Detail page has not loaded within 60 seconds");	
	}

	/* (non-Javadoc)
	 * @see com.ibm.salesconnect.model.StandardPageFrame#isPageLoaded()
	 */
	@Override
	public boolean isPageLoaded() {
		return waitForSubpanelToLoad(pageLoaded);
	}

	
	//Selectors
	public static String pageLoaded ="//div[@id='lotusFooter']";
	
	//To Add Widgets to communities
	public static String getLink_WidgetBlog = "//a[contains(text(),'Blog')]";
	public static String getLink_WidgetActivities = "//a[contains(text(),'Activities')]";
	public static String getLink_WidgetWikis = "//a[contains(text(),'Wiki')]";
	public static String getImage_WidgetSectionClose = "//a[@id='closePalette']/img";
	public static String getText_AddingWidget = "Adding Widget";
	
	//Text to verify that Widget had been added to Community
	public static String getText_CreateYourFirstEntry = "Create Your First Entry";
	public static String getText_CreateYourFirstActivity = "Create Your First Activity";
	public static String getText_CreateAWikiPage = "Create a Wiki Page";
	
	
	//Create links for Activity, Blogs, Forums & Wiki
	public static String getLink_CreateYourFirstActivity = "//a[contains(text(),'Create Your First Activity')]";
	public static String getLink_CreateYourFirstEntry = "//a[contains(text(),'Create Your First Entry')]";
	public static String getLink_AddYourFirstBookmark = "//a[contains(text(),'Add Your First Bookmark')]";
	public static String getLink_StartTheFirstTopic = "//a[contains(text(),'Start the First Topic')]";
	public static String getLink_CreateAWikiPage = "//a[contains(text(),'Create a Wiki Page')]";
	
	//Community Left Nav options
	public String getLink_Overview = "//a[contains(text(),'Overview')]";
	public static String getLink_Members = "//li[@id='Members_navItem']";
	public static String getLink_Files = "//ul[@id='lotusNavBar']//a[contains(text(),'Files')]";
	public static String getLink_RecentUpdates = "//li[@id='RecentUpdates_navItem']";
	
	public static String displayedCommunityName = "id=communityName";
	public static String getLink_MyCommunityView = "link=My Communities";
	public static String getLink_CommunityLink = "//span//a[@class='lotusLink'][1]";
	public static String CommunitiesLink = "css=h2.lotusHeading span.lotusText a:contains(Communities)";
	
	public String status(String status) { return "//div[contains(text(),'"+ status +"')]";};
	
	//Methods
	/**
	 * Returns the displayed community name
	 * @return displayed community name
	 */
	public String getdisplayedCommunityName(){
		return getObjectText(displayedCommunityName);
	}

	/**
	 * Add the Activity, Blog & Wiki Widget to the community and confirm they appears afterwards
	 */
	public void AddWidgetsToCommunity()  {
		//Add the Activity widget
		click(getLink_WidgetActivities);
		Assert.assertFalse(isTextPresent("We've encountered a problem"), "Could not Add Activities Widgit to Community, Failing Connections BVT as Activities App must be down!");	
		
		//Add the Blog widget
		click(getLink_WidgetBlog);
		Assert.assertFalse(isTextPresent("We've encountered a problem"), "Could not Add Blog Widgit to Community, Failing Connections BVT as Blog App must be down!");
		
		//Add the Wiki widget
		isPresent(getLink_WidgetWikis);
		click(getLink_WidgetWikis);
		Assert.assertFalse(isTextPresent("We've encountered a problem"), "Could not Add Wiki Widgit to Community, Failing Connections BVT as Wiki App must be down!");
		
		//Close the widget section
		click(getImage_WidgetSectionClose);
	}

	
	/**
	 * Click on "Create Your First Activity" to add Activity to the Community
	 */
	public CreateCommunityActivityPage CreateYourFirstActivity(){
		click(getLink_CreateYourFirstActivity);
		if(checkForElement(getLink_CreateYourFirstActivity)){
			click(getLink_CreateYourFirstActivity);
		}
		return  new CreateCommunityActivityPage(exec);	
	}

	/**
	 * Click on "Create Your First Entry" to add Blog Entry to the Community
	 */
	public CreateCommunityBlogPage CreateYourFirstEntry(){
		clickJS(getLink_CreateYourFirstEntry);
	return  new CreateCommunityBlogPage(exec);	
	}

	/**
	 * Click on "Add Your First Bookmark" to add Bookmark to the Community
	 */
	public CreateCommunityBookmarkPage AddYourFirstBookmark(){
		click(getLink_AddYourFirstBookmark);
	return  new CreateCommunityBookmarkPage(exec);	
	}
	
	/**
	 * Click on "Start The First Topic" to add Forum Topic to the Community
	 */
	public CreateCommunityForumPage StartTheFirstTopic(){
		click(getLink_StartTheFirstTopic);
	return  new CreateCommunityForumPage(exec);	
	}
	
	/**
	 * Click on "Create A Wiki Page" to add Wiki Page to the Community
	 */
	public CreateCommunityWikiPage CreateAWikiPage(){
		click(getLink_CreateAWikiPage);
	return  new CreateCommunityWikiPage(exec);	
	}
	
	/**
	 * Open Overview Page View in the community
	 * @
	 */
	public void GoToOverviewPageView(){
		log.info("Click Overview");
		checkForElement(getLink_Overview);
		click(getLink_Overview);
		sleep(2);
	}
	
	/**
	 * Click on Community Name to open Community
	 * @
	 */
	public void clickCommunity(String sCommunityName){
		click(getLink_CommunityLink);
	}
	
	public MembersPage openMembersPage(){
		click(getLink_Members);
		return new MembersPage(exec);
	}
	
	public FilesMainPage openFilesPage(){
		click(getLink_Files);
		return new FilesMainPage(exec);
	}

	public void openRecentUpdates(){
		click(getLink_Overview);
		click(getLink_RecentUpdates);
	}

	/**
	 * Verify that Status Message appears 
	 */
	public boolean verifyStatusExists(String Status){
		sleep(5);
		return checkForElement(status(Status));
	}
}
