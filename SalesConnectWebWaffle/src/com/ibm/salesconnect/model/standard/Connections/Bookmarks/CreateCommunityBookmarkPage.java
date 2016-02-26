/**
 * 
 */
package com.ibm.salesconnect.model.standard.Connections.Bookmarks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardPageFrame;
import com.ibm.salesconnect.model.standard.Connections.Forums.CreateCommunityForumPage;
import com.ibm.salesconnect.objects.Connections.*;

/**
 * @author evafarrell
 * @date May 1, 2013
 */
public class CreateCommunityBookmarkPage extends StandardPageFrame{
	Logger log = LoggerFactory.getLogger(CreateCommunityBookmarkPage.class);
	/**
	 * @param exec
	 */
	public CreateCommunityBookmarkPage(Executor exec) {
		super(exec);
//		Assert.assertTrue(isPageLoaded(), "Create Community Bookmark page has not loaded within 60 seconds");	
	}


	/* (non-Javadoc)
	 * @see com.ibm.ecm.product.model.StandardPageFrame#isPageLoaded()
	 */
	@Override
	public boolean isPageLoaded() {
		return waitForSubpanelToLoad(pageLoaded);
	}

	//Selectors
	public static String pageLoaded = "//input[@id='addBookmarkImportant']";
	public static String displayedCommunityBookmarkName = "//table[@id='bookmarksTableContainer']/tbody/tr[2]/td[2]/h4/a";
	public static String getText_BookmarkURL = "css=#addBookmarkUrl";
	public static String getText_BookmarkName = "css=#addBookmarkName";
	public static String getButton_Save = "//input[@value='Save']";
	public static String bookmarkAdded = "The bookmark has been added to your community.";
	public static String getLink_Overview = "//a[contains(text(),'Overview')]";
	public static String StartTheFirstTopic = "//a[contains(text(),'Start the First Topic')]";
	public static String StartATopic = "//a[contains(text(),'Start a Topic')]";
	

	//Tasks
	/**
	 * Returns the displayed community bookmark name
	 * @return displayed community bookmark name
	 */
	public String getdisplayedCommunityBookmarkName(){
		return getObjectText(displayedCommunityBookmarkName);
	}
	
	public CreateCommunityBookmarkPage enterCommunityBookmarkInfo(CommunityBookmark bookmark){

			//set Bookmark URL
			if (bookmark.sBookmarkURL.length()>0){
				type(getText_BookmarkURL, bookmark.sBookmarkURL);
				sleep(2);
			}
	
			//set Bookmark Name
			if (bookmark.sBookmarkName.length()>0){
				type(getText_BookmarkName, bookmark.sBookmarkName);	
				sleep(2);
			}		
		return this;
	}

	public void saveCommunityBookmark(){
		clickJS(getButton_Save);
		sleep(4);
	}
	
	/**
	 * Open Overview Page View in the community
	 * @
	 */
	public void GoToOverviewPageView(){
		log.info("Click Overview");
		waitForElement(getLink_Overview);
		click(getLink_Overview);
		sleep(5);
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

	
}
