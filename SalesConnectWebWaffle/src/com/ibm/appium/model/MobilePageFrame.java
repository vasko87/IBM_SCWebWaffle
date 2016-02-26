package com.ibm.appium.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.appium.TestBase;

public abstract class MobilePageFrame extends TestBase {
	Logger log = LoggerFactory.getLogger(MobilePageFrame.class);

	public static String overlay = "//div[@id='overlay']";
	public static String tutorialDone = "//a[@title='Done']";
	
	public MobilePageFrame() {}
	
	abstract public boolean isPageLoaded();

	/**
	 * Checks if a page is loaded by finding a specified selector on the page
	
	 * @param selector
	 * @return true if selector found within 180 seconds, false if not
	 * @throws InterruptedException
	 */
	protected boolean waitForPageToLoad(String selector) {
		if(waitForElementPresent(selector, 180))
			return true;
		else{
			log.error("Page failed to load. Selector: " + selector + " not found.");
			return false;
		}
	}
	
	protected boolean waitForPageToLoad(String selector, boolean overlay) {	
		if (waitForElementPresent(selector, 3)) {
			return true;
		}
		for (int i = 0; i < 3; i++) {
			try {
				Thread.sleep(5000); // increased the sleep by '2' - initial was
									// 1000
			} catch (InterruptedException e) {
				e.printStackTrace();
				}			
				if (waitForElementPresent(selector, 3))
					  return true;
			}
		return false;
	}
	
	protected void isTutorial(){
		if (isPresent(tutorialDone,3))
			click(tutorialDone);
	}
}
