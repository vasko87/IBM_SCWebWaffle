package com.ibm.salesconnect.model;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.standard.Opportunity.ViewOpportunityPage;

public class StandardDetailPage extends StandardPageFrame{

	Logger log = LoggerFactory.getLogger(this.getClass());
	
	public StandardDetailPage(Executor exec){
		super(exec);
	}
	
	@Override
	public boolean isPageLoaded() {
		return waitForElement(pageLoaded);
	}
	
	public void addToFavorite(){
		click (MyFavourites);
		log.info("Click Opportunity to My Favorites");
	}
	
	/**
	 * @param selector - use the //div[@data-name='fieldname']
	 * @param entry - The desired field
	 */
	public void editTextEntry(String selector, String entry){
		click(selector);
		type(selector + "//textarea", entry);
		click(selector);
		click(selector);
	}
	
	/**
	 * @param selector - use the //div[@data-name='fieldname']
	 * @param entry - the desired field
	 */
	public void editDropdownEntry(String selector, String entry){
		click(selector);
		select(selector + "//span(@class='select2-chosen')",entry);
		click(selector);
		click(selector);
	}
	
	public void select(String selector, String choice)
	{
		sidecarListBoxSelect(selector, choice);
	}
	
	/**
	 * Opens the view opportunity page and returns the page object
	 * @return ViewOpportunityPage object
	 */
	public ViewOpportunityPage openViewOpportunity() {
		switchToMainWindow();
		log.info("URL for View Opportunity Page" + getObjectAttribute(viewOpportunityPage, "href"));
		String url = " ";
		for (int i = 0; i < 60; i++) {
			url = getObjectAttribute(viewOpportunityPage, "href");
			if (!url.equals(" ")) {
				loadURL(url);
				return new ViewOpportunityPage(exec);
			}
			sleep(1);
		}
		
		Assert.assertTrue(false, "Page url could not be found");
		return new ViewOpportunityPage(exec);
		
	}
	
	public void waitToUnlink(){
		if (isPresent(unlinkAlertMessage)) {
			waitForElement(unlinkAlertSuccessMessage);
		}		
}
	
	public void triggerChange(String selector){
		String cssSelector = selector.replaceFirst("^\\s*css=\\s*", "");
		System.out.println(cssSelector);
		String code = "return $('"+ cssSelector + "').trigger('change');";
		System.out.println(code);
		
		WebDriver wd = (WebDriver) exec.getBackingObject();

		WebElement we = wd.findElement(By.cssSelector(cssSelector));
			
		if(checkForElement(selector)){
			exec.executeScript(code, we);	
		}
	}
	
	public void deleteItem(){
		click(editDropDown);
		click(deleteOption);
		click("//a[@data-action='confirm']");
	}
	
	public void confirmDelete(){
		if (isPresent(deletingAlertMessage)) {
			waitForElement(savedAlertMessage,90);
		}
	}
	
	public String viewOpportunityPage = "";
	public String pageLoaded = "//a[@name='edit_button' and not(@style='display: none;')]";
	//public static String MyFavourites= "//span[@data-fieldname='favorite']/span";
	public static String MyFavourites= "//span/i";
	public String unlinkAlertMessage = "//div[@class='alert alert-process']/strong[contains(text(),'Unlink')]";
	public String unlinkAlertSuccessMessage = "//div[contains(@class,'alert-success')]//a";
	//public static String EditButton = "//a[@id='edit_button']";
	public static String EditButton = "//a[@name='edit_button']";
	public static String editDropDown = "//div[contains(@class,'main-pane')]//h1//a[@data-original-title='Actions']";
	public static String deleteOption = "//a[@name='delete_button']";
	public String deletingAlertMessage = "//div[@class='alert alert-process']/strong[contains(text(),'Deleting')]";
	public String savedAlertMessage = "//div[contains(@class,'alert-success')]";
}
