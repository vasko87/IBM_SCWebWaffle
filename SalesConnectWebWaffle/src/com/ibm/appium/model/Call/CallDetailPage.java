package com.ibm.appium.model.Call;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.appium.model.MobilePageFrame;

public class CallDetailPage extends MobilePageFrame {

	Logger log = LoggerFactory.getLogger(CallDetailPage.class);

	public CallDetailPage() {
		Assert.assertTrue(isPageLoaded(), "Call Detail Page has not loaded");
	}

	
	/**
	 * Check if new page is loaded by finding element on that page before
	 * starting any actions.
	 */
	@Override
	public boolean isPageLoaded() {
		if (isPresent("//a[@title='Done']")) {
			click("//a[@title='Done']");
		}
		return waitForPageToLoad(pageLoaded);
	}

	// XPath Selectors
	public static String pageLoaded = "//a[@class='title append-root']//span[@class='value']";
	public static String editButton = "//a[@class='icon icon-pencil fast-click-highlighted append-root']";

	/**
	 * Get Call details located on call detail page.
	 */
	public String getCallSubject() {
		return getText("//a[@class='title append-root']/child::span/child::div/child::span/following-sibling::*[1]");
	}

	public String getCallType() {
		return getText("//a[@class='title append-root']/following-sibling::*[1]/child::div/child::span/following-sibling::*[1]");
	}

	public String getCallStartTime() {
		return getText("//a[@class='title append-root']/following-sibling::*[2]/child::div/child::span/following-sibling::*[1]");
	}

	public String getCallDuration() {
		return getText("//a[@class='title append-root']/following-sibling::*[3]/child::div/child::span/following-sibling::*[1]");
	}

	public String getCallAssignedTo() {
		return getText("//a[@class='title append-root']/following-sibling::*[4]/child::div/child::span/following-sibling::*[1]");
	}

	public String getCallStatus() {
		return getText("//a[@class='title append-root']/following-sibling::*[5]/child::div/child::span/following-sibling::*[1]");
	}

	public String getCallSummary() {
		return getText("//a[@class='title append-root']/following-sibling::*[6]/child::div/child::span/following-sibling::*[1]");
	}

	/**
	 * Open edit call menu from call detail page.
	 * 
	 * @return CreateCallPage
	 */
	public CreateCallPage editCall() {
		click(editButton);
		return new CreateCallPage();
	}
}
