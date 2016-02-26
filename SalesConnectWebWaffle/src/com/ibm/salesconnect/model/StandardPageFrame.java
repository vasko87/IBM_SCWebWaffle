package com.ibm.salesconnect.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.google.common.base.Function;
import com.ibm.atmn.waffle.core.Executor;
import com.ibm.atmn.waffle.core.RCLocationExecutor;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.partials.ConnectionsBusinessCard;
import com.ibm.salesconnect.model.standard.Opportunity.ViewOpportunityPage;



/**
 * Abstract Class that abstracts the basic attributes and operations of a Page.
 * 
 * @author Main
 * 
 */
public abstract class StandardPageFrame {

	private static Logger log = LoggerFactory
			.getLogger(StandardPageFrame.class);
	protected static RCLocationExecutor exec;

	public static String loadingMesssage = "//div[@id='loadingPage']";
	public static String backwardsCompatibleFrame = "//iframe[@id='bwc-frame']";
	public StandardPageFrame(Executor exec) {

		if (exec != null && exec.isLoaded()) {
			this.exec = (RCLocationExecutor) exec;
		} else {
			String message = "A page may not exist without a loaded executor (i.e. a page can only exist in an open browser)";
			log.error(message);
			throw new AssertionError(message);
		}

	}
	
	protected StandardPageFrame getPageFrame() {
		return this;
	}

	/**
	 * Repeatedly performs a test to see if THIS page is loaded in the browser.
	 * Returns true when the page is loaded. Returns false on timeout or other detection of failure.
	 * 
	 * @return true if the current page is as expected and finished loading,
	 *         false otherwise.
	 */
	abstract public boolean isPageLoaded();
	
	/**
	 * Checks if a page is loaded by finding a specified selector on the page
	 * @param selector
	 * @return true if selector found within the timeout setting amount of seconds, false if not
	 * @throws InterruptedException
	 */
	protected boolean waitForPageToLoad(String selector){

	    ((WebDriver) exec.getBackingObject()).switchTo().defaultContent();
		sleep(5);
		if(waitForElement("//iframe[@id='bwc-frame']")){
			sleep(5);
			switchToFrame("//iframe[@id='bwc-frame']");
			log.info("Switching to frame");
		}
		
		return waitForElement(selector);
	}

	/**
	 * Waits for the number of seconds as set in the testng xml for an element to be present and visible on the page
	 * @param selector Selector for the element
	 * @return true if found false if not
	 */
	public Boolean waitForElement(final String selector){
		log.info("Start waiting");
		   Wait<WebDriver> wait = new FluentWait<WebDriver>((WebDriver) exec.getBackingObject())
	       .withTimeout(Integer.parseInt(exec.getTestManager().getTestConfig().getParameter(GC.explicitTimeoutName)), TimeUnit.MILLISECONDS)
	       .pollingEvery(1, TimeUnit.SECONDS)
	       .ignoring(NoSuchElementException.class);

		   turnOffImplicitTimeout();
			   WebElement element;
			   try {
				   element = (WebElement) wait.until(new Function<WebDriver,WebElement>() {
					     public WebElement apply(WebDriver e) {
					       return e.findElement(By.xpath(selector));
					     }
					   });
			   } catch (Exception e) {
				   log.info("Cannot find element" + selector);
				   log.info(e.getLocalizedMessage());
				   turnOnImplicitTimout(Integer.parseInt(exec.getTestManager().getTestConfig().getParameter(GC.implicitTimeoutName)));
				   Assert.assertTrue(false, "element not found");
			   }
		   log.info("Found element");
		   log.info("Checking if displayed");
		   if(wait.until(new Function<WebDriver,Boolean>() {
			     public Boolean apply(WebDriver e) {
			       return (Boolean) e.findElement(By.xpath(selector)).isDisplayed();
			     }
			   }))
			   {
			   turnOnImplicitTimout(Integer.parseInt(exec.getTestManager().getTestConfig().getParameter(GC.implicitTimeoutName)));
				  return true;
			   }
		   else {
			   turnOnImplicitTimout(Integer.parseInt(exec.getTestManager().getTestConfig().getParameter(GC.implicitTimeoutName)));
			return false;
		}
	}

	/**
	 * Waits for an element to be present and visible on the page
	 * @param selector Selector for the element
	 * @param timeout how long to wait in seconds
	 * @return true if found false if not
	 */
	public Boolean waitForElement(final String selector, int timeout){
		log.info("Start waiting");
		   Wait<WebDriver> wait = new FluentWait<WebDriver>((WebDriver) exec.getBackingObject())
	       .withTimeout(timeout, TimeUnit.SECONDS)
	       .pollingEvery(1, TimeUnit.SECONDS)
	       .ignoring(NoSuchElementException.class);

		   turnOffImplicitTimeout();
			   WebElement element;
			   try {
				   element = (WebElement) wait.until(new Function<WebDriver,WebElement>() {
					     public WebElement apply(WebDriver e) {
					       return e.findElement(By.xpath(selector));
					     }
					   });
			   } catch (Exception e) {
				   log.info("Cannot find element" + selector);
				   log.info(e.getLocalizedMessage());
				   turnOnImplicitTimout(Integer.parseInt(exec.getTestManager().getTestConfig().getParameter(GC.implicitTimeoutName)));
				   Assert.assertTrue(false);
			   }
		   log.info("Found");
		   log.info("Checking if displayed");
		   if(wait.until(new Function<WebDriver,Boolean>() {
			     public Boolean apply(WebDriver e) {
			       return (Boolean) e.findElement(By.xpath(selector)).isDisplayed();
			     }
			   }))
			   {
			   turnOnImplicitTimout(Integer.parseInt(exec.getTestManager().getTestConfig().getParameter(GC.implicitTimeoutName)));
				  return true;
			   }
		   else {
			   turnOnImplicitTimout(Integer.parseInt(exec.getTestManager().getTestConfig().getParameter(GC.implicitTimeoutName)));
			return false;
		}
	}
	
	/**
	 * Check for an element to be present for the number of milliseconds specified in xml
	 * @param selector
	 * @return true if found, false if not
	 */
	public Boolean checkForElement(final String selector){
		return checkForElement(selector, Integer.parseInt(exec.getTestManager().getTestConfig().getParameter(GC.explicitTimeoutName)));
	}

	/**
	 * Check for a given time for an element to be present
	 * @param selector Selector for the element
	 * @param timeout how long to wait in seconds
	 * @return true if found false if not
	 */
	public Boolean checkForElement(String selector, int timeout){
		log.info("Start waiting for: " + selector);

		final By bySelector;
		if (selector.contains("css=")) {
			bySelector = By.cssSelector(selector.replaceFirst("^\\s*css=\\s*", ""));
			//bySelector=By.cssSelector(selector);
		}
		else {
			bySelector=By.xpath(selector);
		}
		   Wait<WebDriver> wait = new FluentWait<WebDriver>((WebDriver) exec.getBackingObject())
	       .withTimeout(timeout, TimeUnit.MILLISECONDS)
	       .pollingEvery(1, TimeUnit.SECONDS)
	       .ignoring(NoSuchElementException.class);
		   turnOffImplicitTimeout();
			   WebElement element;
			   try {
				   element = (WebElement) wait.until(new Function<WebDriver,WebElement>() {
					     public WebElement apply(WebDriver e) {
					       return e.findElement(bySelector);
					     }
					   });
			   } catch (Exception e) {
				   log.info("Cannot find element" + selector);
				   log.info(e.getLocalizedMessage());
				   
				   turnOnImplicitTimout(Integer.parseInt(exec.getTestManager().getTestConfig().getParameter(GC.implicitTimeoutName)));
				   return false;
			   }
		   log.info("Found");
		   log.info("Checking if displayed");
		   if(wait.until(new Function<WebDriver,Boolean>() {
			     public Boolean apply(WebDriver e) {
			       return (Boolean) e.findElement(bySelector).isDisplayed();
			     }
			   }))
			   {
			   turnOnImplicitTimout(Integer.parseInt(exec.getTestManager().getTestConfig().getParameter(GC.implicitTimeoutName)));
			   log.info("Element displayed");
				  return true;
			   }
		   else {
			   turnOnImplicitTimout(Integer.parseInt(exec.getTestManager().getTestConfig().getParameter(GC.implicitTimeoutName)));
			   log.info("Element not displayed");
			return false;
		}
	}
	
	/**
	 * Turns off the implicit timeout
	 */
	private static void turnOffImplicitTimeout(){
		((WebDriver) exec.getBackingObject()).manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
	}
	
	/**
	 * Sets implicit timeout to supplied timeout
	 */
	private static void turnOnImplicitTimout(int timeout){
		((WebDriver) exec.getBackingObject()).manage().timeouts().implicitlyWait(timeout, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * Checks if a subpanel is loaded by finding a specified selector on the page
	 * @param selector
	 * @return true if selector found within 60 seconds, false if not
	 * @throws InterruptedException
	 */
	protected boolean waitForSubpanelToLoad(String selector){
		return(checkForElement(selector));
	}
	/**
	 * Checks if the red saved message has appeared at the top of the page
	 * @return true if it has, false if not
	 */
	protected boolean isSaved(String selector){
	if(checkForElement(selector))
		while(getObjectText(selector).contains("Saving"));
		if(getObjectText(selector).contains("Saved"))
			return true;
		return false;
	}
	
	protected void waitWhileLoadingAlert(String selector){
		if(checkForElement(selector))
			while(getObjectText(selector).contains("Loading"));
		else{
			log.info("Loader not found");
			sleep(30);
		}
	}
	
	protected boolean switchToBwcFrame(){
		if(waitForElement(backwardsCompatibleFrame)){
			log.info("Switching to frame");
			switchToFrame(backwardsCompatibleFrame);
			return true;
		}
		return false;
	}

	//Selectors
	public static String isLoading = "//div[@id='loadingPage']";
	
	/**
	 * Checks if an element is present on the page
	 * @param Selector
	 * @return true if element present, false if not
	 */
	public boolean isPresent(String Selector){
		if(exec.getElements(Selector).size()>0){
			return true;
		}
		else {
			return false;
		}
	}

	
	/**
	 * Checks if a string is present on the page
	 * @param Selector
	 * @return true if string present, false if not
	 */
	protected boolean isTextPresent(String text){
		sleep(5);
		if(exec.isTextPresent(text)){
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Click on the element at the given Selector
	 * @param selector
	 */
	public void click(String selector){
			if(checkForElement(selector)){
				exec.getFirstElement(selector).click();
			}
			else {
				Assert.assertTrue(false, "Element: " + selector + " was not found");
			}
	}
	
	public void doubleclick(String selector){
		if(checkForElement(selector)){
			WebDriver wd = (WebDriver) exec.getBackingObject();
			Actions act = new Actions(wd);
			act.doubleClick(wd.findElement(By.xpath(selector)));
		}
		else {
			Assert.assertTrue(false, "Element: " + selector + " was not found");
		}
	}
	
	/**
	 * Click on the element at the given Selector and at the 0, 0 co-ords
	 * @param selector
	 */
	public void clickAt(String selector){
		if(checkForElement(selector)){
				exec.getFirstElement(selector).clickAt(0,0);
		}
		else {
			Assert.assertTrue(false, "Element: " + selector + " was not found");
		}
	}
	
	/**
	 * Alternative method to click on an element, generating a click event
	 * using Javascript.
	 * 
	 * @param Selector to click on

	 */
	public void clickJS(String Selector){
		String code = "var fireOnThis = arguments[0];"
			+ "var evObj = document.createEvent('MouseEvent');"
	    	+ "evObj.initEvent('click', true, true);"
	    	+ "fireOnThis.dispatchEvent(evObj);";
		
		WebDriver wd = (WebDriver) exec.getBackingObject();

		WebElement we = wd.findElement(By.xpath(Selector));
		
		if(checkForElement(Selector)){
			exec.executeScript(code, we);	
		}
	}
	
	/**
	 * Click on the element at the given Selector
	 * @param selector
	 */
	public void clickWithScrolling(String selector){
		double eleHeight = exec.getFirstElement(selector).getLocation().getY();
		Long height = (Long) exec.executeScript("return document.documentElement.clientHeight") - 100;
		if(eleHeight>height){
			double diff = eleHeight - height;
			exec.executeScript("window.scrollBy(0,arguments[0])", diff*4);
			WebDriver wd = (WebDriver) exec.getBackingObject();
			Actions builder = new Actions(wd);
			WebElement tagElement = (WebElement) exec.getFirstElement(selector);
			builder.moveToElement(tagElement).click().perform();
			exec.getFirstElement(selector).click();
		}
		else{
			exec.getFirstElement(selector).click();
		}
	}
	
	/**
	 * Scrolls the browser page to the top of the page. This can be used
	 * as a workaround for when webdriver does not properly scroll itself
	 * to a visible element.
	 */
	public void scrollToTopOfPage() {
		
		exec.executeScript("window.scrollTo(0,0)", "");
	}

	/**
	 * Scrolls the browser page to the top of the page. This can be used
	 * as a workaround for when webdriver does not properly scroll itself
	 * to a visible element.
	 */
	public void scrollToBottomOfPage() {
		
		exec.executeScript("window.scrollTo(0,450)", "");
	}
	
	/**
	 * Locates the element via the locator string and then scrolls it to the middle of
	 * the browser. 
	 * @param selector
	 */
	public void scrollElementToMiddleOfBrowser(String selector) {
		WebDriver wd = (WebDriver) exec.getBackingObject();

		WebElement element;
		try {
			element = wd.findElement(By.xpath(selector));
		} catch (Exception e) {
			element = wd.findElement(By.cssSelector(selector.replaceFirst("^\\s*css=\\s*", "")));
		}

		
		
		Point point = element.getLocation();
		int x = point.getX();
		int y = point.getY();

		exec.executeScript("h=" + y
				+ " - (window.innerHeight/2);window.scrollTo(" + x + ",h);");
	}
	
	/**
	 * Type text into field at location given by Selector
	 * @param selector
	 * @param text
	 */
	protected void type(String selector, String text){
		exec.getFirstElement(selector).clear();
		exec.getFirstElement(selector).type(text);
	}
	

	/**
	 * Clears text from the given field
	 * @param selector
	 */
	protected void clearField(String selector){
		exec.getFirstElement(selector).clear();
	}

	/**
	 * Type text into a non standard Element eg ckeditor
	 * You must click on the element first
	 * @param text
	 */
	protected void typeNative(String text){
		exec.typeNative(text);
	}
	
	
	/**
	 * Check if a selector is checked
	 * @param selector
	 * @return true if checked, false if not
	 */
	protected boolean isChecked(String selector){
		return exec.getFirstElement(selector).isSelected();
	}
	
	/**
	 * Send keys to field at location given by selector 
	 * @param selector
	 * @param keys
	 */
	protected void sendKeys(String selector, Keys keys){
		WebDriver wd = (WebDriver) exec.getBackingObject();

		WebElement we = wd.findElement(By.xpath(selector));
		we.sendKeys(keys);
	}
	
	/**
	 * Send keys to field at location given by selector 
	 * @param selector
	 * @param keys
	 */
	protected void sendKeys(String selector, String keys){
		WebDriver wd = (WebDriver) exec.getBackingObject();

		WebElement we = wd.findElement(By.xpath(selector));
		we.sendKeys(keys);
	}
	
	/**
	 * Send String to field that is currently in focus 
	 * @param keys
	 */
	protected void sendKeys(String keys){
		WebElement element = (WebElement) exec.switchToActiveElement();
		element.sendKeys(keys);
	}
	
	/**
	 * Send Keys to field that is currently in focus 
	 * @param keys
	 */
	protected void sendKeys(Keys keys){
		WebElement element = (WebElement) exec.switchToActiveElement();
		element.sendKeys(keys);
	}
	
	/**
	 * Returns the value of the specified attribute for the given selector
	 * @param Selector
	 * @param attributeName
	 * @return Attribute value
	 */
	protected String getObjectAttribute(String Selector,String attributeName){
		return exec.getFirstElement(Selector).getAttribute(attributeName);
	}
	
	protected void setObjectAttribute(String Selector, String attributeName, String value){

		WebDriver wd = (WebDriver) exec.getBackingObject();
		JavascriptExecutor js = (JavascriptExecutor)wd;
		js.executeScript("document.getElementById('primary_address_country_basic-input').setAttribute('value', 'All countries')");
	}
	
	/**
	 * Returns the text visible at a specified selector
	 * @param Selector
	 * @return text visible at specified selector
	 */
	protected String getObjectText(String selector){
		return exec.getFirstElement(selector).getText();
	}
	
	
	
	/**
	 * Loads the url specified in the parameter
	 * @param url
	 */
	protected void loadURL(String url){
		exec.load(url,true);
	}
	
	protected String getCurrentURL(){
		return exec.getCurrentUrl();
	}
	
	/**
	 * Selects the option specified by the text parameter at the given selector
	 * Note:the selector must be of type 'select'
	 * @param Selector
	 * @param text
	 */
	protected void select(String Selector, String text){
		exec.getFirstElement(Selector).useAsDropdown().selectOptionByVisibleText(text);
	}
	
	/**
	 * Checks if an element is present and visible
	 * @param selector
	 * @return true if visible, false if not
	 */
	protected Boolean isVisible(String selector){
		if (isPresent(selector))
			return	exec.getFirstElement(selector).isVisible();
		else
			return false;
	}
	
	/**
	 * Switch to the first window with popup in the url
	 */
	public void getPopUp(){
		//Wait for a few seconds to allow time for the popup to open
		sleep(5);
		Set<String> handlers = exec.getWindowHandles();  
		if (exec.getWindowHandles().size()>= 1){  
			for(String handler : handlers){  
				exec.switchToWindowByHandle(handler);
				if (exec.getCurrentUrl().contains("Popup"))
					return; 
			}
		}
		Assert.assertTrue(false, "Could not switch to popup window");
	}
	
	/**
	 * Switches driver's focus to first page without "popup" in URL
	 */
	public  void switchToMainWindow(){
		Set<String> handlers = exec.getWindowHandles();  
		if (exec.getWindowHandles().size()>= 1){  
			for(String handler : handlers){  
				exec.switchToWindowByHandle(handler);
				if (!exec.getCurrentUrl().contains("Popup")){  
					return; 
				}
			}
		}
		Assert.assertTrue(false, "Could not switch to main window");
	}
	
	public void switchToWindowUrlContains(String urlContains){
		Set<String> handlers = exec.getWindowHandles();  
		if (exec.getWindowHandles().size()>= 1){  
			for(String handler : handlers){  
				exec.switchToWindowByHandle(handler);
				if (exec.getCurrentUrl().contains(urlContains)){  
					return; 
				}
			}
		}
		Assert.assertTrue(false, "Could not switch to specified window");	
	}
	
	/**
	 * ]Switch driver's focus to a specified frame
	 * @param Selector
	 */
	public void switchToFrame(String Selector) {
		exec.switchToFrame().selectSingleFrameBySelector(Selector); 
	}
	
	/**
	 * If dealing with multiple Windows, use this method to check that
	 * the window you want to work with exists and then switch 
	 * to that window
	 * @param windowTitle 
	 */
	public boolean findAndSwitchToWindow(String windowTitle) {
        Set<String> windows = exec.getWindowHandles();

        for (String window : windows) {
            exec.switchToWindowByName(window);
            if (exec.getTitle().contains(windowTitle)) {
                return true;
            }
        }
        return false;
    }
	
	/**
	 * Accept an alert dialog box
	 */
	protected void acceptAlert() {
			com.ibm.atmn.waffle.core.Executor.Alert alert = exec.switchToAlert();
			alert.accept();
	}
	
	/**
	 * Waits for an alert for a given period of time
	 * @param selector - The location of the alert
	 * @param timeout - How many attempts to check for the alert
	 * @param interval - How long to wait between attempts
	 * @throws InterruptedException 
	 */
	public boolean waitForAlert(String selector, int timeout, int interval)
	{
		log.info("Waiting for Alert " + selector);
		boolean found = false;
		for(int i = 0; i < timeout; i++)
		{
			if(checkForElement(selector))
				found = true;
			else
				sleep(interval);
		}
		return found;
	}

	public void objectHover(String selector) {
		exec.getFirstElement(selector).hover();
	}
	
	@Deprecated
	public void selectSubHover(String topMenu, String subMenu){
		WebDriver wd = (WebDriver) exec.getBackingObject();
		Actions actions = new Actions(wd);
		WebElement topHoverElement = (WebElement) wd.findElement(By.xpath(topMenu));
		WebElement subHoverElement = (WebElement) wd.findElement(By.xpath(subMenu));
		actions.moveToElement(topHoverElement, 1, 1);
		actions.moveToElement(subHoverElement, 1, 1).click().build().perform();
	}

	/**
	 * Alternative method to hover on an element, which may work better than
	 * the built-in WebDriver functionality used in objectHover in some
	 * situations.
	 * 
	 * @param Selector to click on

	 */

	public void mouseHover(String Selector) {
		String code = "var fireOnThis = arguments[0];"
			+ "var evObj = document.createEvent('MouseEvents');"
			+ "evObj.initEvent( 'mouseover', true, true );"
			+ "fireOnThis.dispatchEvent(evObj);";

		WebDriver wd = (WebDriver) exec.getBackingObject();

		WebElement we = wd.findElement(By.xpath(Selector));

		exec.executeScript(code, we);
	}
	

	
	
	/**
	 * Hover that is currently working in SalesConnect
	 * 
	 * @param Selector to hover on

	 */
	public void mouseHoverSalesConnect(String Selector){
		String buisnessCardLink = "//span[@id='semtagmenu' or @id='semtagmenuBox']/a";
		WebElement elems = ((WebDriver) exec.getBackingObject()).findElement(By.xpath(Selector));
		Actions builder = new Actions((WebDriver) exec.getBackingObject());
		Actions hoverOver = builder.moveToElement(elems);
		hoverOver.perform();
		
			if(!checkForElement(buisnessCardLink)){
				log.info("Attempting to hover over user again...");
				hoverOver.perform();
				sleep(2);
				if(!checkForElement(buisnessCardLink)){
					mouseHover(Selector);
				}
			}
	}

	/**
	 * Initiates a Thread.sleep - catches exception so tasks don't have to throw it
	 * @param iWait - time to wait in seconds
	 */
	public static void sleep (int iWait){
		try {
			Thread.sleep(iWait*1000);

		}catch (InterruptedException e) {
			log.error("Exception in sleep");
		}
	}
	
	/**
	 * Open business card for a particular element
	 * @param Selector
	 * @param exec2 
	 */
	public ConnectionsBusinessCard openConnectionsBusinessCard(String Selector, RCLocationExecutor exec2){
		checkForElement(Selector);
		mouseHoverSalesConnect(Selector);
		click("//span[@id='semtagmenu' or @id='semtagmenuBox']/a");
		
		String BusCard = "//table[@id='cardTable']";
		mouseHover(BusCard);
		return new ConnectionsBusinessCard(exec2);
	}
	
	
	/**
	 * Select a particular element of a sidecar style dropdown list
	 * @param Selector - The location of the 'box' when not expanded
	 * @param pick - the string to be selected from the box
	 */
	public void sidecarListBoxSelect(String selector, String pick){
		click(selector);
		if(isVisible("//div[@id='select2-drop']//input"))
		{
			type("//div[@id='select2-drop']//input", pick);
		}
sleep(5);
			click("//div[@id='select2-drop']//li[contains(*, '" + pick + "')]");
		

	}
	
	/**
	 * Input the desired selection into a sidecar style autocomplete text Box
	 * @param Selector - The location of the Text Box
	 * @param Choice - The desired String to be entered (Must be an available option)
	 */
	public void  sidecarAutoTextBoxEntry(String selector, String pick){
		click(selector);
		type(selector,pick);
		sendKeys(selector, Keys.ENTER);
	}
	
	//Frame Selectors
	public static String createContactPage = "//*[@data-navbar-menu-item='LNK_NEW_CONTACT']";
	public static String viewContactPage = "//*[@data-navbar-menu-item='LNK_CONTACT_LIST']";
	public static String createContactPagelower = "//*[@data-navbar-menu-item='LNK_NEW_CONTACT']";
	
	public static String quickCreateOpportunityPage = "//*[@track='click:quickCreate-Opportunity']";
	public static String createOpportunityPage = "//*[@data-navbar-menu-item='LNK_NEW_OPPORTUNITY']";
	public static String viewOpportunityPage = "//*[@data-navbar-menu-item='LNK_OPPORTUNITY_LIST']";
	
	public static String createTaskPage = "//*[@data-navbar-menu-item='LNK_NEW_TASK']";
	public static String viewTaskPage = "//*[@data-navbar-menu-item='LNK_TASK_LIST']";
	public static String createTaskPagelower = "//*[@data-navbar-menu-item='LNK_NEW_TASK']";
	
	public static String createCallPage = "//*[@data-navbar-menu-item='LNK_NEW_CALL']";
	public static String viewCallPage = "//*[@data-navbar-menu-item='LNK_CALL_LIST']";
	public static String createCallPagelower = "//*[@data-navbar-menu-item='LNK_NEW_CALL']";
	public static String viewCallPagelower = "//*[@data-navbar-menu-item='LNK_CALL_LIST']";
	
	public static String createNotePage = "//*[@data-navbar-menu-item='LNK_NEW_NOTE']";
	public static String viewNotePage = "//*[@data-navbar-menu-item='LNK_NOTE_LIST']";
	public static String createNotePagelower = "//*[@data-navbar-menu-item='LNK_NEW_NOTE']";
	
	public static String createLeadPage = "//*[@data-navbar-menu-item='LNK_NEW_LEAD']";
	public static String viewLeadPage = "//*[@data-navbar-menu-item='LNK_LEAD_LIST']";
	public static String createLeadPagelower = "//*[@data-navbar-menu-item='LNK_NEW_LEAD']";
	public static String viewLeadPagelower = "//*[@data-navbar-menu-item='LNK_LEAD_LIST']";
	public static String leadListItem = "//li[@data-module='Leads']";
	
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
		}
		
		Assert.assertTrue(false, "Page url could not be found");
		return new ViewOpportunityPage(exec);		
	}
//  adding 

	protected boolean waitForPageLoad(String selector){
		return waitForElement(selector);
	}
 
 
	public void selectAdjDropDown(String click, String input, String search, String results){
		click(click);
	    type(input, search);
	    sleep(2);
	    click(results); 
	}
 
	public Map<String, String> getCreatedAdjustmentInfo(String sellerOrMgr){
		log.info("Get the Adjustment info from Forecast Page...");
		String headPath, valuePath;
		
		if(sellerOrMgr.contains("manager")){
			headPath = "//*[@id='manager-adjustments']/thead/tr[1]";
			valuePath = "//*[@id='manager-adjustments']/tbody/tr[1]";
		}else{
			headPath = "//*[@class='table table-striped dataTable reorderable-columns']//thead/tr[1]";
			valuePath = "//*[@class='table table-striped dataTable reorderable-columns']//tbody/tr[1]";
		}
		if(waitForElement(valuePath)){
			Map<String, String> adjMap = new HashMap<String, String>();
			WebDriver wd = (WebDriver) exec.getBackingObject();    
			WebElement head = wd.findElement(By.xpath(headPath)); 
			List<WebElement> ths = head.findElements(By.tagName("th")); 
			WebElement tr01 = wd.findElement(By.xpath(valuePath));
			List<WebElement> tds = tr01.findElements(By.tagName("td")); 
			for(int i=0;i<ths.size()-1;i++){
				if(!(ths.get(i).getText().contains("Always Match"))){
					String key = ths.get(i).getText().replaceFirst("\\*", "").trim(); 
					String value = tds.get(i).getText().replaceFirst(",", "");
				    adjMap.put(key, value);   
				}      
			} 
			return adjMap;
		}else{
			return null;
		}
	}
 
	public void waitForAlertExpiration(){
		final String alertBox = "//div[@class='alert alert-process']";		
		log.info("Start waiting for: " + alertBox);	
		
		final By cssSelector = By.xpath(alertBox);
		Wait<WebDriver> wait = new FluentWait<WebDriver>((WebDriver) exec.getBackingObject())
	    .withTimeout(Integer.parseInt(exec.getTestManager().getTestConfig().getParameter(GC.explicitTimeoutName)), TimeUnit.MILLISECONDS)
	    .pollingEvery(1, TimeUnit.SECONDS)
	    .ignoring(NoSuchElementException.class);
		turnOffImplicitTimeout();	  
		wait.until(new Function<WebDriver,Boolean>() {
			public Boolean apply(WebDriver wd) {
				try{
					if(wd.findElement(cssSelector).isDisplayed()){
						log.info("Loading Alter: "+alertBox+" is displaying...");
						return false;
						}
					}catch(Exception e){
						return true;
						}
				return true;
				}
			});
		turnOnImplicitTimout(Integer.parseInt(exec.getTestManager().getTestConfig().getParameter(GC.implicitTimeoutName)));
		log.info("Loading Alter: "+alertBox+" disappear...");		
	}		
	
	public void waitForAdjustmentAlertExpiration(){
		final String alertBox = "//ul[@class='nav results ibm_Adjustments']/li/div[3]/span[2]/span";  
		log.info("Start waiting for: " + alertBox); 
		  
		final By selector = By.xpath(alertBox);
		Wait<WebDriver> wait = new FluentWait<WebDriver>((WebDriver) exec.getBackingObject())
				.withTimeout(Integer.parseInt(exec.getTestManager().getTestConfig().getParameter(GC.explicitTimeoutName)), TimeUnit.MILLISECONDS)
		        .pollingEvery(1, TimeUnit.SECONDS)
		        .ignoring(NoSuchElementException.class);
		turnOffImplicitTimeout();   
		wait.until(new Function<WebDriver,Boolean>() {
			public Boolean apply(WebDriver wd) {
				try{
					if(wd.findElement(selector).isDisplayed()){
						log.info("'Loading' Adjustment Alter: "+alertBox+" is displaying...");
						return false;
						}
					}catch(Exception e){
						return true;
						}
				return true;
				}
			});
		turnOnImplicitTimout(Integer.parseInt(exec.getTestManager().getTestConfig().getParameter(GC.implicitTimeoutName)));
		log.info("'Loading' Adjustment Alter: "+alertBox+" disappear...");
		}
	}
