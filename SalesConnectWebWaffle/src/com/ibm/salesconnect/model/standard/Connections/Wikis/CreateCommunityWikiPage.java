/**
 * 
 */
package com.ibm.salesconnect.model.standard.Connections.Wikis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardPageFrame;
import com.ibm.salesconnect.model.standard.Connections.Community.CommunityMainPage;
import com.ibm.salesconnect.objects.Connections.CommunityWiki;

/**
 * @author evafarrell
 * @date May 1, 2013
 */
public class CreateCommunityWikiPage extends StandardPageFrame{
	Logger log = LoggerFactory.getLogger(CreateCommunityWikiPage.class);
	/**
	 * @param exec
	 */
	public CreateCommunityWikiPage(Executor exec) {
		super(exec);
		//Assert.assertTrue(isPageLoaded(), "Create Community Wiki page has not loaded within 60 seconds");	
	}


	/* (non-Javadoc)
	 * @see com.ibm.ecm.product.model.StandardPageFrame#isPageLoaded()
	 */
	@Override
	public boolean isPageLoaded() {
		return waitForSubpanelToLoad(pageLoaded);
	}

	//Selectors
	public static String pageLoaded = "//input[@id='name']";
	public static String displayedCommunityWikiName = "css=a.entry-title";
	public static String getText_WikiTitle = "//input[@id='name' and @class='lotusText wikiPageTitle']";
	public static String getButton_SaveAndCloseWiki = "//div[@id='pageDetails']//input[@value='Save and Close']";
	
	//Tasks
	/**
	 * Returns the displayed community Wiki name
	 * @return displayed community Wiki name
	 */
	public String getdisplayedCommunityWikiName(){
		checkForElement(displayedCommunityWikiName);
		return getObjectText(displayedCommunityWikiName);
	}
	
	public CreateCommunityWikiPage enterCommunityWikiInfo(CommunityWiki wiki){

			//set Wiki Name
			if (wiki.sWikiTitle.length()>0){
				type(getText_WikiTitle, wiki.sWikiTitle);	
			}		
		return this;
	}

	public void saveCommunityWiki(){
		clickJS(getButton_SaveAndCloseWiki);
		sleep(2);
	}
	
	public CommunityMainPage openPublicCommunityPage(){
		mouseHover("//a[@id='lotusBannerCommunitiesLink']");
		click("//a[contains(text(),'Public Communities')]");
		return new CommunityMainPage(exec);
	}
}
