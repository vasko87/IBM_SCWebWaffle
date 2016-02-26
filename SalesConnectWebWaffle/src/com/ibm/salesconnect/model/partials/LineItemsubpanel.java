			/**
 * 
 */
package com.ibm.salesconnect.model.partials;

import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.model.StandardPageFrame;

/**
 * @author timlehane
 * @date Jun 17, 2013
 */
public class LineItemsubpanel extends StandardPageFrame {

	/**
	 * @param exec
	 */
	public LineItemsubpanel(Executor exec) {
		super(exec);
		Assert.assertTrue(isPageLoaded(), "Line Items Subpanel has not loaded within 60 seconds");	
	}

	@Override
	public boolean isPageLoaded() {
		return waitForSubpanelToLoad(pageLoaded);
	}
	
	//Selectors
	public static String pageLoaded = "//div[@id='subpanel_opportun_revenuelineitems']//*[@sugar='slot7b']";
	public String getBusinessCard(String userName) { return "//span[@sugar='slot7b']//a[contains(text(),'" + userName + "')]";};
	
	
	public void verifyBusinessCard(User user){
		ConnectionsBusinessCard connectionsBusinessCard = openConnectionsBusinessCard(getBusinessCard(user.getFirstName()), exec);
		connectionsBusinessCard.verifyBusinessCardContents(user);
	}

}
