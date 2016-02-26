/**
 * 
 */
package com.ibm.salesconnect.model.partials;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardPageFrame;

/**
 * @author evafarrell
 *
 */
public class AddACommunityPopup extends StandardPageFrame {
	Logger log = LoggerFactory.getLogger(AddACommunityPopup.class);

	/**
	 * @param exec
	 */
	public AddACommunityPopup(Executor exec) {
		super(exec);
		Assert.assertTrue(isPageLoaded(), "Add a Communiity Popup has not loaded within 60 seconds");	
	}

	/* (non-Javadoc)
	 * @see com.ibm.salesconnect.model.StandardPageFrame#isPageLoaded()
	 */
	@Override
	public boolean isPageLoaded() {
		return waitForElement(pageLoaded);
	}
	
	//Selectors
	public static String pageLoaded = "//span[@id='scdijit_dijit_form_Button_0_label']";
	//public static String AddCommunityImage = "(//div[@id='scMyCommunitiesDialog']//a[@title='Add to related communities'])[position()=1]";
	public static String AddCommunityLink = "//div[@id='scMyCommunitiesDialog']//a[@title='Add to related communities']";
	public static String RemoveCommunityLink = "//div[@id='scMyCommunitiesDialog']//a[@title='Remove from related communities']";
	public static String closePopup = "//span[@id='scdijit_dijit_form_Button_0_label']";
	public static String displayedCommunityName = "//table[@class='myCommunityBodyTable']//td[@class='columnTitle']";
	
	//Tasks

	/**
	 * Selects a Community
	 */
	public void selectCommunity() {
		if(isPresent(RemoveCommunityLink)){
			click(RemoveCommunityLink);
			this.acceptAlert();
			log.info("Removed "+getdisplayedCommunityName()+" Community");
			click(AddCommunityLink);
			log.info("Added "+getdisplayedCommunityName()+" Community");
		}
		else{ 
			click(AddCommunityLink);
			log.info("Added "+getdisplayedCommunityName()+" Community");
		}
	}

	/**
	 * Returns the displayed community name
	 * @return displayed community name
	 */
	public String getdisplayedCommunityName(){
		return getObjectText(displayedCommunityName);
	}
	
	/**
	 * Close Add Related Community Popup
	 */
	public void closeAddCommunityPopup(){
		click(closePopup);
	}
	
}
