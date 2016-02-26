package com.ibm.salesconnect.model;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.ibm.atmn.waffle.core.Executor;

public class StandardCreatePage extends StandardPageFrame{
	/**
	 * @param exec
	 */
	public StandardCreatePage(Executor exec) {
		super(exec);
		//Assert.assertTrue(isPageLoaded(), "Create Lead page has not loaded within 60 seconds");	
	}

	@Override
	public boolean isPageLoaded() {
		return waitForElement(pageLoaded);
	}
	
	public void select(String selector, String choice){
		sidecarListBoxSelect(selector, choice);
	}
	
	/**
	 * Saves the Current form and submits the object.
	 * Intended to be used with a typecast or overridden
	 */
	public StandardCreatePage Save(){
		click(saveButton);
		return new StandardCreatePage(exec);
	}
	
	public void typeEnter(String selector, String choice){
		sidecarAutoTextBoxEntry(selector, choice);
	}
	
	public String waitToLoad(){
		if(isPresent(loadingAlertMessage)){
			waitForElement(savingAlertMessage);
			waitForElement(savedAlertMessage);
			return(getObjectText(savedAlertContent));
		}
		else {
			return "";
		}
	}
	
	public void waitToSave(){
		if(isPresent(savingAlertMessage)){
			waitForElement(savedAlertMessage);
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

	public String getOpenDropdownSearchBox = "//div[@id='select2-drop']//input[@class='select2-input']";
	public String saveButton = "//a[@name='save_button' and not(@style='display: none;')]";
	public String cancelButton = "//a[@name='cancel_button']";
	public String pageLoaded = saveButton;
	public String loadingAlertMessage = "//div[@class='alert alert-process']/strong[contains(text(),'Loading')]";

	public String savingAlertMessage = "//div[@class='alert alert-process']/strong[contains(text(),'Saving')]";
	public String deletingAlertMessage = "//div[@class='alert alert-process']/strong[contains(text(),'Deleting')]";
	public String savedAlertMessage = "//div[contains(@class,'alert-success')]";
	public String savedAlertContent = "//div[contains(@class,'alert-success')]//a";
}
