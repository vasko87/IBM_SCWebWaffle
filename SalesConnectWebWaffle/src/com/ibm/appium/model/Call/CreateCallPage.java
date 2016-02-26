package com.ibm.appium.model.Call;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.appium.Objects.Call;
import com.ibm.appium.model.MobilePageFrame;

public class CreateCallPage extends MobilePageFrame {
	Logger log = LoggerFactory.getLogger(CreateCallPage.class);

	public CreateCallPage() {
		Assert.assertTrue(isPageLoaded(), "Log Call Page has not loaded");
	}
	
	/**
	 * Check if new page is loaded by finding element on that page before
	 * starting any actions.
	 */
	@Override
	public boolean isPageLoaded() {
		return waitForPageToLoad(pageLoaded);
	}

	// XPath Selectors
	public static String pageLoaded = "//label[contains(text(),'Subject')]/..//input";
	public static String getCallSubjectField = "//label[contains(text(),'Subject')]/..//input";
	public static String callDurationMinsMenu = "//select[@name='duration_minutes']";
	public static String callStatusMenu = "//select[@name='status']";
	public static String callTypeMenu = "//select[@name='call_type']";

	public static String callSubjectField = "//label[contains(text(),'Subject')]/..//span";
	public static String callSummaryField = "//label[contains(text(),'Summary Of Call')]/..//span";
	public static String callSummaryTextArea = "//textarea[@name='somename']";
	public static String callSummarySavebtn = "//span[@track='click:save']";

	public static String saveCallButton = "//span[contains(@class,'saveBtn')]";
	public static String callClearSubjectCSS = ".icon-remove-sign";
	public static String callClearSubject = "//i[@class='icon icon-remove-sign]";

	
	/**
	 * Main method to populate Call form.
	 */
	public CreateCallPage enterCallInfo(Call call) {
		if (call.getsSubject().length() > 0) {
			click(getCallSubjectField);
			clear();
//			if(isPresent(callClearSubject))
//				clear(callClearSubjectCSS);
			type(getCallSubjectField, call.getsSubject());
		}
		if (call.getsDurationMinutes().length() > 0) {
			select(callDurationMinsMenu, call.getsDurationMinutes());
		}
		if (call.getsCallStatus().length() > 0) {
			select(callStatusMenu, call.getsCallStatus());
		}
		if (call.getsCallType().length() > 0) {
			select(callTypeMenu, call.getsCallType());
		}
		if (call.getsSummaryOfCall().length() > 0) {
			click(callSummaryField);
			type(callSummaryTextArea, call.getsSummaryOfCall());
			click(callSummarySavebtn);
		}
		
		return this;
	}
	
	/**
	 * Update existing Call.
	 */
	public void updateCallInfo(Call call) {
		if (call.getsSubjectUPD().length() > 0) {
			type(getCallSubjectField, call.getsSubjectUPD());
		}
		return;
	}

	/**
	 * Save call form and navigate to Call detail page.
	 * 
	 * @return CallDetailPage
	 */
	public CallDetailPage saveCall() {
		click(saveCallButton);
		return new CallDetailPage();
	}
	
	/**
	 * Get related calls details.
	 * 
	 * @return Call
	 */
	
	public Call getRelatedCallDetails(Call call) {
		click(getCallSubjectField);
		call.setsSubject(getText(getCallSubjectField));
		return call;
	}
}
