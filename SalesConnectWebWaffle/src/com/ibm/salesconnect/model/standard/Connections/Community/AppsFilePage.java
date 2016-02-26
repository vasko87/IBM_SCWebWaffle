/**
 * 
 */
package com.ibm.salesconnect.model.standard.Connections.Community;

import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardPageFrame;

/**
 * @author timlehane
 * @date Jun 4, 2013
 */
public class AppsFilePage extends StandardPageFrame {
	
	public static String fileSearchField = "//input[@id='quickSearch_simpleInput']";
	public static String fileSearchSubmit = "//input[@id='quickSearch_submit']";
	public static String searchScope = "//a[@title='Refine search options']";
	public static String publicFiles = "//span[contains(text(),'Public Files')]";

	/**
	 * @param exec
	 */
	public AppsFilePage(Executor exec) {
		super(exec);
		Assert.assertTrue(isPageLoaded(), "Files page has not loaded within 60 seconds");	
	}

	@Override
	public boolean isPageLoaded() {
		return waitForSubpanelToLoad(pageLoaded);
	}

	
	//Selectors
	
	//public static String pageLoaded = "//a[contains(text(),'Accessibility')]";
	public static String pageLoaded = "//a[contains(text(),'Files')]";
	public String getFileName(String fileName){return "//a[@title='" + fileName + "']";};
	
	//Methods
	public boolean isFilePresent(String fileName){
		return checkForElement(getFileName(fileName));
	}
	
	public void searchForFile(String fileName){
		type(fileSearchField, fileName);
		click(fileSearchSubmit);
		
	}
	
	public void openPublicFiles(){
		click(publicFiles);
		Assert.assertTrue(isPageLoaded(), "Files page has not loaded within 60 seconds");	
	}
	
	
}
