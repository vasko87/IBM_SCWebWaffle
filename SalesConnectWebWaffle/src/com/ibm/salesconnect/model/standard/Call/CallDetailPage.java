/**
 * 
 */
package com.ibm.salesconnect.model.standard.Call;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardPageFrame;

/**
 * @author timlehane
 * @date May 15, 2013
 */
public class CallDetailPage extends StandardPageFrame {
	Logger log = LoggerFactory.getLogger(CallDetailPage.class);
	
	/**
	 * @param exec
	 */
	public CallDetailPage(Executor exec) {
		super(exec);
		sleep(10);
		Assert.assertTrue(isPageLoaded(), "Call detail page has not loaded within 60 seconds");	
	}

	@Override
	public boolean isPageLoaded() {
		return waitForPageToLoad(pageLoaded);
	}
	
	//Selectors
	public static String pageLoaded = "//div[@class='moduleTitle']/h2";
	public static String detailsFrame = "//iframe[@id='bwc-frame']";
	
	public static String displayedCallSubject = "//div[@class='moduleTitle']/h2";
	public static String MyFavourites= "//*[@id='content']/div[1]/h2/div/div";
	public static String EditButton = "//a[@id='edit_button']";
	public static String editDropDown = "//ul[@id='detail_header_action_menu']/li/span";
	public static String deleteOption = "//a[@id='delete_button']";
	
	
	
	//Methods
	/**
	 * Returns the displayed call subject
	 * @return displayed call subject
	 */
	public String getdisplayedCallSubject(){
		return getObjectText(displayedCallSubject);
	}

	public void addCallToMyFavorites(){
		click (MyFavourites);
		log.info("Click Call to My Favorites");
	}
	
	/**
	 * Opens the edit call page
	 */
	public CreateCallPage openEditPage(){
		click(EditButton);
		return new CreateCallPage(exec);
	}
	
	public void deleteCall(){
		click(editDropDown);
		click(deleteOption);
		acceptAlert();
	}
	
}
