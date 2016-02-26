package com.ibm.salesconnect.model.standard;

import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardPageFrame;

public class GlobalSearchResultsPage extends StandardPageFrame {

	public GlobalSearchResultsPage(Executor exec) {
		super(exec);
		Assert.assertTrue(isPageLoaded(), "Global Search Results page has not loaded within 60 seconds");	
	}

	@Override
	public boolean isPageLoaded() {
		return waitForPageToLoad(pageLoaded);
	}
	
	//Selectors
	public static String pageLoaded = "//input[@id='ftsSearchButton']";

	public static String searchField = "//input[@id='ftsSearchField']";
	
	public static String searchButton = "//input[@id='ftsSearchButton']";
	
	
	//Tasks
	/**
	 * Verify that the item you searched for is returned in the results
	 * 
	 * @param searchResult
	 */	
	public boolean verifySearchResults(String searchResult) {
		//Assert.assertTrue(this.isTextPresent(searchResult), " Search Results does not contain item searched for");
		boolean result = false;
		for(int x = 0; x<20; x++){
			if(this.isTextPresent(searchResult)){
				result = true;
				break;
			}
			sleep(60);
			searchAgainForResult();
		}
		if(result==false){
			Assert.assertTrue(false, "Search Results does not contain item searched for");
		}
		return true;
	}
	
	public void searchAgainForResult(){
		click(searchButton);
		isPageLoaded();
	}

}
