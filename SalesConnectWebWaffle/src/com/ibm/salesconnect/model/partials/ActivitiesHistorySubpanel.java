/**
 * 
 */
package com.ibm.salesconnect.model.partials;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.model.StandardPageFrame;

/**
 * @author timlehane
 * @date Jun 18, 2013
 */
public class ActivitiesHistorySubpanel extends StandardPageFrame {
	Logger log = LoggerFactory.getLogger(ActivitiesHistorySubpanel.class);

	/**
	 * @param exec
	 */
	public ActivitiesHistorySubpanel(Executor exec) {
		super(exec);
		Assert.assertTrue(isPageLoaded(), "ActivitiesHistorySubpanell has not loaded within 60 seconds");	
	}


	@Override
	public boolean isPageLoaded() {
		return waitForSubpanelToLoad(pageLoaded);
	}
	
	//Selectors
	public static String pageLoaded = "//div[@id='list_subpanel_history']//a[contains(text(),'Assigned')]";
	public String getBusinessCard(String userName) { return "//div[@id='list_subpanel_history']//.[contains(text(),'"+userName+"')]";};
	
	
	public void verifyBusinessCard(User user){
		ConnectionsBusinessCard connectionsBusinessCard = openConnectionsBusinessCard(getBusinessCard(user.getFirstName()), exec);
		connectionsBusinessCard.verifyBusinessCardContents(user);
	}

}
