/**
 * 
 */
package com.ibm.salesconnect.model.standard.Home;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardPageFrame;

/**
 * @author stephenryan
 * @date May 27, 2013
 */
public class HomePage extends StandardPageFrame {
	Logger log = LoggerFactory.getLogger(HomePage.class);

	/**
	 * @param exec
	 */
	public HomePage(Executor exec) {
		super(exec);
		Assert.assertTrue(isPageLoaded(), "Home page has not loaded within 60 seconds");	
	}


	@Override
	public boolean isPageLoaded() {
		return waitForPageToLoad(pageLoaded);
	}
	
	//Selectors
	public static String pageLoaded = "//a[@title='Add Dashlets']";

	//Methods


}
