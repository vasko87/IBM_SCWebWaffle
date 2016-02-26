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
 * @date Jun 6, 2013
 */
public class ConnectionsBusinessCard extends StandardPageFrame {
	Logger log = LoggerFactory.getLogger(ConnectionsBusinessCard.class);
	/**
	 * @param exec
	 */
	public ConnectionsBusinessCard(Executor exec) {
		super(exec);
		Assert.assertTrue(isPageLoaded(), "Connections Business Card page has not loaded within 60 seconds");
	}


	@Override
	public boolean isPageLoaded() {
		return waitForSubpanelToLoad(pageLoaded);
	}
	
	//Selectors
	public static String pageLoaded = "//table[@id='cardTable']//a[@class='email']";
	public static String Email(String userEmail) { return "//a[contains(@href, 'mailto:"+userEmail+"')]";};
	public static String userEmail = "//div[@class='lotusPersonInfo']/p/a";
	public String featureLink(String feature) {return "//tr[@id='cardHeader']//a[contains(text(),'" + feature + "')]";};
	public static String[] featureList = {"Profile", "Communities", "Blogs", "Forums","Wikis", "Files","Bookmarks", "Activities"};
	public String getBusinessCard(String userName) { return "//a[@class='fn url hasHover']//.[contains(text(),'"+userName+"')]";};
	
	public void verifyBusinessCardContents(User user){
		Assert.assertTrue(checkForElement(Email(user.getEmail())), "User Email was not displayed");
		for (int i = 0; i < featureList.length; i++) {
			Assert.assertTrue(checkForElement(featureLink(featureList[i])), "Business Card " + featureList[i] + " Link was not displayed");
		}
	}
	
	public void verifyBusinessCardContents(String emailAddress){
		Assert.assertEquals(getObjectText(userEmail), emailAddress, "Email address is not correct");
		
		for (int i = 0; i < featureList.length; i++) {
			Assert.assertTrue(checkForElement(featureLink(featureList[i])), "Business Card " + featureList[i] + " Link was not displayed");
		}
	}
		
	public void getProfile(){
		click(featureLink(featureList[0]));
	}
}
