/**
 * 
 */
package com.ibm.salesconnect.model.partials;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.atmn.waffle.core.TestConfiguration;
import com.ibm.salesconnect.model.StandardPageFrame;
import com.ibm.salesconnect.model.standard.Connections.ConnectionsLogin;
import com.ibm.salesconnect.model.standard.Connections.Activities.CreateCommunityActivityPage;
import com.ibm.salesconnect.model.standard.Connections.Blogs.CreateCommunityBlogPage;
import com.ibm.salesconnect.model.standard.Connections.Bookmarks.CreateCommunityBookmarkPage;
import com.ibm.salesconnect.model.standard.Connections.Forums.CreateCommunityForumPage;
import com.ibm.salesconnect.model.standard.Connections.Wikis.CreateCommunityWikiPage;

/**
 * @author evafarrell	
 * @date Jul 31, 2013
 */
public class ConnectionsCommunityTab extends StandardPageFrame {
	Logger log = LoggerFactory.getLogger(ConnectionsCommunityTab.class);

	/**
	 * @param exec
	 */
	public ConnectionsCommunityTab(Executor exec) {
		super(exec);
		Assert.assertTrue(isPageLoaded(), "ConnectionsCommunityTab has not loaded within 60 seconds");	
	}


	@Override
	public boolean isPageLoaded() {
		return waitForPageToLoad(pageLoaded);
	}
	
	//Selectors
	protected TestConfiguration testConfig;
	
	public static String pageLoaded = "//img[@id='communityLogo']";
	public static String detailsFrame = "//iframe[@id='bwc-frame']";
	
	public static String joinCommunity = "//span[@id='scdijit_dijit_form_Button_0_label']";
	public static String connectionsCommunityTab = "//div[@id='Accounts_detailview_tabs']//li[3]";
	
	public static String RecentUpdatesArea = "//span[contains(@class,'dijitTreeLabel') and contains(text(),'Recent Updates')]";
	public static String ForumsArea = "//span[contains(@class,'dijitTreeLabel') and contains(text(),'Forums')]";
	public static String BookmarksArea = "//span[contains(@class,'dijitTreeLabel') and contains(text(),'Bookmarks')]";
	public static String ActivitiesArea = "//span[contains(@class,'dijitTreeLabel') and contains(text(),'Activities')]";
	public static String WikisArea = "//span[contains(@class,'dijitTreeLabel') and contains(text(),'Wikis')]";
	public static String BlogsArea = "//span[contains(@class,'dijitTreeLabel') and contains(text(),'Blogs')]";
	public static String RelatedCommunitiesArea = "//span[contains(@class,'dijitTreeLabel') and contains(text(),'Related Communities')]";
	public static String Refresh = "//img[@alt='Click']";
	public static String ViewAll = "//a[contains(text(),'View All')]";
	
	public static String StartTheFirstTopic = "//a[contains(text(),'Start the First Topic')]";
	public static String StartATopic = "//a[contains(text(),'Start a topic')]";
	public static String AddABookmark = "//a[contains(text(),'Add a bookmark')]";
	public static String StartYourFirstConnectionsActivity = "//a[contains(text(),'Start your first Connections Activity')]";
	public static String StartAnActivity = "//a[contains(text(),'Start an Activity')]";
	public static String CreateAWikiPage = "//a[contains(text(),'Create a Wiki Page')]";
	public static String CreateYourFirstBlogEntry = "//a[contains(text(),'Create Your First Entry')]";
	public static String CreateABlogEntry = "//a[contains(text(),'Create a blog entry')]";
	public static String AddACommunity = "//a[contains(text(),'Add a Community')]";
		
	public static String ForumIcon = "//div[@alt='Forum']";
	public static String BookmarkIcon = "//img[@alt='Bookmark']";
	public static String WikiIcon = "//img[@alt='Wiki']";
	public static String ActivityIcon = "//img[@alt='Normal priority activity']";
	public static String BlogIcon = "//img[@alt='Blog']";
	public static String CommunityIcon = "//img[@class='relatedImage']";

	public String createdForumName(String sForumTitle) {return ("//a[contains(text(),'"+sForumTitle+"')]");}
	public String createdBookmarkName(String sBookmarkTitle) {return ("//a[contains(text(),'"+sBookmarkTitle+"')]");}
	public String createdWikiPageName(String sWikiTitle) {return ("//a[contains(text(),'"+sWikiTitle+"')]");}
	public String createdActivityName(String sActivityTitle) {return ("//a[contains(text(),'"+sActivityTitle+"')]");}
	public String createdBlogName(String sBlogTitle) {return ("//a[contains(text(),'"+sBlogTitle+"')]");}
	public String addedRelatedCommunityName(String sRelatedCommunityTitle) {return ("//a[contains(text(),'"+sRelatedCommunityTitle+"')]");}
	
	public String getTextField_UpdatedBy(String sUserUpdatedBy) {return ("//a[contains(text(),'"+sUserUpdatedBy+"')]");}	
	
	public String cnxnUserNameField = "//input[@id='username']";
	
	public void verifyCommunityTab(String userEmail, String userPassword){
		if (checkForElement(detailsFrame)){
			switchToFrame(detailsFrame);
		}
		if(checkForElement(joinCommunity)){
			click(joinCommunity);
			if(connectionsLoginRequired()){
				ConnectionsLogin cnxnLoginPage = new ConnectionsLogin(exec);
				sleep(2);
				cnxnLoginPage.login(userEmail, userPassword);
			}
			sleep(2);
			click(connectionsCommunityTab);
		}

		Assert.assertTrue(checkForElement(RecentUpdatesArea),"Recent Updates Area not found");
		Assert.assertTrue(checkForElement(ForumsArea),"Forums Area not found");
		Assert.assertTrue(checkForElement(BookmarksArea),"Bookmarks Area not found");
		Assert.assertTrue(checkForElement(ActivitiesArea),"Activities Area not found");
		Assert.assertTrue(checkForElement(WikisArea),"Wikis Area not found");
		Assert.assertTrue(checkForElement(RelatedCommunitiesArea),"Related Communities Area not found");
	}
	
	/**
	 * Open the Forums Area in the Connections Community Tab
	 */
	public void openForumsArea(){
		Assert.assertTrue(checkForElement(ForumsArea),"Forums Area not found");
		clickAt(ForumsArea); 		
	}
	
	/**
	 * Open the Bookmarks Area in the Connections Community Tab
	 */
	public void openBookmarksArea(){
		Assert.assertTrue(checkForElement(BookmarksArea),"Bookmarks Area not found");
		clickAt(BookmarksArea); 
	}
	
	/**
	 * Open the Activities Area in the Connections Community Tab
	 */
	public void openActivitiesArea(){
		Assert.assertTrue(checkForElement(ActivitiesArea),"Activities Area not found");
		clickAt(ActivitiesArea); 
	}
	
	/**
	 * Open the Wikis Area in the Connections Community Tab
	 */
	public void openWikisArea(){
		Assert.assertTrue(checkForElement(WikisArea),"Wikis Area not found");
		clickAt(WikisArea); 
	}
	
	/**
	 * Open the Blogs Area in the Connections Community Tab
	 */
	public void openBlogsArea(){
		Assert.assertTrue(checkForElement(BlogsArea),"Blogs Area not found");
		clickAt(BlogsArea); 
	}
	
	/**
	 * Open the Related Communities Area in the Connections Community Tab
	 */
	public void openRelatedCommunitiesArea(){
		Assert.assertTrue(checkForElement(RelatedCommunitiesArea),"Related Communities Area not found");
		clickAt(RelatedCommunitiesArea); 
	}

	
	
	public boolean connectionsLoginRequired(){
		if(findAndSwitchToWindow("IBM Connections")){
			log.info("Switched to IBM Connections Window");
		}
		else log.info("IBM Connections Window Not Found!!");
		
		if(checkForElement(cnxnUserNameField)){
			log.info("Connections Login screen detected, atempt to Login");
			return true;
		}
		log.info("No Connections Login screen detected!!");
		return false;
	}
	
	/**
	 * Open the Create Forum Page in Connections
	 * @return New Create Forum Page
	 */
	public CreateCommunityForumPage createForum() {
		if(checkForElement(StartTheFirstTopic)){
			click(StartTheFirstTopic);	
		}
		else
			click(StartATopic);
		return new CreateCommunityForumPage(exec);
	}
	
	/**
	 * Open the Create Activity Page in Connections
	 * @return New Create Activity Page
	 */
	public CreateCommunityActivityPage createActivity() {
		if(checkForElement(StartYourFirstConnectionsActivity)){
			click(StartYourFirstConnectionsActivity);	
		}
		else
			click(StartAnActivity);
		return new CreateCommunityActivityPage(exec);
	}
	
	/**
	 * Open the Create Bookmark Page in Connections
	 * @return New Create Bookmark Page
	 */
	public CreateCommunityBookmarkPage createBookmark() {
		if(checkForElement(AddABookmark)){
			clickAt(AddABookmark);	
		}
		return new CreateCommunityBookmarkPage(exec);
	}
	
	/**
	 * Open the Create Wiki Page in Connections
	 * @return New Add Wiki Page
	 */
	public CreateCommunityWikiPage createWikiPage() {
		if(checkForElement(CreateAWikiPage)){
			click(CreateAWikiPage);	
		}
		return new CreateCommunityWikiPage(exec);
	}
	
	/**
	 * Open the Create Blog Page in Connections
	 * @return New Create Blog Page
	 */
	public CreateCommunityBlogPage createBlog() {
		if(checkForElement(CreateYourFirstBlogEntry)){
			click(CreateYourFirstBlogEntry);	
		}
		else
			click(CreateABlogEntry);
		return new CreateCommunityBlogPage(exec);
	}
	
	/**
	 * Open the Add a Community Popup
	 * @return Add a Community Popup
	 */
	public AddACommunityPopup addACommunity() {
		if(checkForElement(AddACommunity)){
			click(AddACommunity);	
		}
		return new AddACommunityPopup(exec);
	}
	
	/**
	 * Open the Forums Area in the Connections Community Tab
	 * and verify that the New Forum exists
	 */
	public boolean VerifyForumsArea(String sForumTopicTitle, String sUserUpdatedBy){
		if (checkForElement(detailsFrame)){
			switchToFrame(detailsFrame);
		}
		click(ForumsArea);
		click(Refresh);
		if(checkForElement(ViewAll) && checkForElement(ForumIcon) && checkForElement(createdForumName(sForumTopicTitle)) && isTextPresent("Started by: "+sUserUpdatedBy)){
			log.info("Forum Icon, Forum Title and Started by User Name Found");
			return true;
		}
		log.info("Forum Icon, Forum Title and Started by User Name Not Found");
		return false;

	}
	
	/**
	 * Open the Bookmarks Area in the Connections Community Tab
	 * and verify that the New Bookmark exists
	 */
	public boolean verifyBookmarksArea(String sBookmarkTitle, String sUserUpdatedBy){
		if (checkForElement(detailsFrame)){
			switchToFrame(detailsFrame);
		}
		click(BookmarksArea); 
		click(Refresh);
		if(checkForElement(ViewAll) && checkForElement(BookmarkIcon) && checkForElement(createdBookmarkName(sBookmarkTitle)) && isTextPresent("Updated by: "+sUserUpdatedBy)){
			log.info("Bookmark Icon, Bookmark Title and Updated by User Name Found");
			return true;
		}
		log.info("Bookmark Icon, Bookmark Title and Updated by User Name Not Found");
		return false;
	}
	
	/**
	 * Open the Activities Area in the Connections Community Tab
	 * and verify that the New Activity exists
	 */
	public boolean verifyActivitiesArea(String sActivityTitle, String sUserUpdatedBy){
		if (checkForElement(detailsFrame)){
			switchToFrame(detailsFrame);
		}
		click(ActivitiesArea);
		click(Refresh);
		if(checkForElement(ViewAll) && checkForElement(ActivityIcon) && checkForElement(createdActivityName(sActivityTitle)) && isTextPresent("Updated by: "+sUserUpdatedBy)){
			log.info("Activity Icon, Activity Title and Updated by User Name Found");
			return true;
		}
		log.info("Activity Icon, Activity Title and Updated by User Name Not Found");
		return false;
	}
	
	/**
	 * Open the Wikis Area in the Connections Community Tab
	 * and verify that the New Wiki Page exists
	 */
	public boolean verifyWikisArea(String sWikiPageTitle, String sUserUpdatedBy){
		if (checkForElement(detailsFrame)){
			switchToFrame(detailsFrame);
		}
		click(WikisArea);
		click(Refresh);
		if(checkForElement(ViewAll) && checkForElement(WikiIcon) && checkForElement(createdWikiPageName(sWikiPageTitle)) && isTextPresent("Updated by: "+sUserUpdatedBy)){
			log.info("WiKi Icon, Wiki Page Title and Updated by User Name Found");
			return true;
		}
		log.info("WiKi Icon, Wiki Page Title and Updated by User Name Not Found");
		return false;
	}
	
	/**
	 * Open the Blogs Area in the Connections Community Tab
	 * and verify that the New Blog exists
	 */
	public boolean verifyBlogsArea(String sBlogsTitle, String sUserUpdatedBy){
		if (checkForElement(detailsFrame)){
			switchToFrame(detailsFrame);
		}
		click(BlogsArea); 
		click(Refresh);
		if(checkForElement(ViewAll) && checkForElement(BlogIcon) && checkForElement(createdBlogName(sBlogsTitle)) && isTextPresent("Updated by: "+sUserUpdatedBy)){
			log.info("Blog Icon, Blog Title and Updated by User Name Found");
			return true;
		}
		log.info("Blog Icon, Blog Title and Updated by User Name Not Found");
		return false;
	}
	
	/**
	 * Open the Related Communities Area in the Connections Community Tab
	 * and verify that the Related Community exists
	 */
	public boolean verifyRelatedCommunitiesArea(String sRelatedCommunityName, String sUserUpdatedBy){
		if (checkForElement(detailsFrame)){
			switchToFrame(detailsFrame);
		}
		click(RelatedCommunitiesArea); 
		click(Refresh);
		if(checkForElement(ViewAll) && checkForElement(CommunityIcon) && checkForElement(addedRelatedCommunityName(sRelatedCommunityName))){
		//if(checkForElement(ViewAll) && checkForElement(CommunityIcon) && checkForElement(addedRelatedCommunityName(sRelatedCommunityName)) && isTextPresent("Updated by: "+sUserUpdatedBy)){
			log.info("Community Icon, Community Title Found and Updated by User Name Found");
			return true;
		}
		log.info("Community Icon, Community Title Found and Updated by User Name Not Found");
		return false;
	}
	
}
