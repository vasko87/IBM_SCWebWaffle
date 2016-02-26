/**
 * 
 */
package com.ibm.salesconnect.model.standard.Connections.Community;

import org.openqa.selenium.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardPageFrame;

/**
 * @author evafarrell
 * @date May 2, 2013
 */
public class CommunityMainPage extends StandardPageFrame{

	Logger log = LoggerFactory.getLogger(CommunityMainPage.class);
	
	/**
	 * @param exec
	 */
	public CommunityMainPage(Executor exec) {
		super(exec);
		// TODO Auto-generated constructor stub
		isPageLoaded();
	}

	/* (non-Javadoc)
	 * @see com.ibm.ecm.product.model.StandardPageFrame#isPageLoaded()
	 */
	@Override
	public boolean isPageLoaded() {
		return waitForSubpanelToLoad(pageLoaded);
	}
	
	//Selectors
	public static String pageLoaded = "//a[contains(text(),'Submit Feedback')]";
	public static String StartACommunity = "//a[contains(text(),'Start a Community')]";
	public static String ImAnOwner = "//a[contains(text(),'Owner')]";
	public static String CommunitiesLink = "//a[@id='lotusBannerCommunitiesLink']";
	
	//Community Actions menu items
	public static String getButton_Community_Actions = "//a[@id='displayActionsBtn']";
	public static String getListItem_Customize= "//td[@id='communityMenu_customize_text' or @id='communityMenu_CUSTOMIZE_text']";
	public static String searchScope = "//a[@title='Search scope']";
	public static String searchScope2 = "//a[@class='lotusScope']";
	public static String myCommunities = "//td[contains(text(),'My Communities')]";
	public static String searchField = "//input[@title='Search' and @type='text']";
	public static String searchButton = "//input[@title='Search' and @class='lotusSearchButton']";
	public String getSearchResult(String searchString) { return "//a[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'"+searchString.toLowerCase()+"')]";};
		
	public String getSearchResult2(String searchString) { return "//a[contains(text(),'" +searchString+"')]";};
	public static String getFirstSearchResult = "//a[@aria-describedby='lconn_communities_catalog_widgets_PlaceDisplayWidget_0sourceType']";
	
	//Tasks
	/**
	 * Opens the create Community page and returns the page object
	 * @return CreateCommunityPage
	 */
	public CreateCommunityPage startACommunity() {
		click(StartACommunity);
		return  new CreateCommunityPage(exec);		
	}

	/**
	 * Opens the the I'm An Owner Community Link and returns the community main page object
	 * @return CommunityMainPage
	 */
	public CommunityMainPage openMyCommunities() {
		click(CommunitiesLink);
		click(ImAnOwner);
		return  new CommunityMainPage(exec);		
	}
	
	/**
	 * Opens the the Customize Community Link and returns the community detail page object
	 * @return CommunityDetailPage
	 */
	public CommunityDetailPage customizeCommunity(){
//		waitForElement(getButton_Community_Actions);
		clickJS(getButton_Community_Actions);
		clickJS(getListItem_Customize);
		return  new CommunityDetailPage(exec);	
	}
	
	public Boolean checkForumPresent(){
		CommunityDetailPage communityDetailPage = new CommunityDetailPage(exec);
		isPresent(communityDetailPage.getLink_Overview);
		return checkForElement(communityDetailPage.getLink_Overview);
	}
	
	public Boolean checkWikiPresent(){
		CommunityDetailPage communityDetailPage = new CommunityDetailPage(exec);
		isPresent(communityDetailPage.getLink_Overview);
		return checkForElement(communityDetailPage.getLink_Overview);
	}
	
	public void searchCommunity(String commName, String URL){
		loadURL(URL);
		if(checkForElement(searchScope)){
			click(searchScope);
		}
		else{
			click(searchScope2);
		}
		
		clickAt(myCommunities);
		type(searchField, commName);
		sendKeys(searchField, Keys.RETURN);
		sendKeys(searchField, Keys.RETURN);
		
	}
	
	public boolean verifyCommunitySearchResults(String expectedResults){
		if(checkForElement(getSearchResult(expectedResults))){
			return true;
		}
		else if(checkForElement(getSearchResult2(expectedResults))){
			return true;
		}
		return false;
	}
	
	public CommunityDetailPage searchAndOpenCommunity(String searchString, String URL){
		searchCommunity(searchString, URL);
		for (int i = 0; i < 5; i++) {
			if(checkForElement(getSearchResult(searchString))){
				click(getSearchResult(searchString));
				i=10;
			}
			else if(checkForElement(getSearchResult2(searchString))){
				click(getSearchResult2(searchString));
				i=10;
			}
			else if(checkForElement(getFirstSearchResult)){
				click(getFirstSearchResult);
				i=10;
			}		
			
		}
		
		return new CommunityDetailPage(exec);
	}
	
	public AppsFilePage openAppsFilePage(){
		mouseHover("//a[contains(text(),'Apps')]");
		click("//strong[contains(text(),'Files')]");
		return new AppsFilePage(exec);
	}

}
