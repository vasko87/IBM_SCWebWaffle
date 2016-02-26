/**
 * 
 */
package com.ibm.salesconnect.model.standard.Call;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardPageFrame;
import com.ibm.salesconnect.objects.Call;

/**
 * @author timlehane
 * @date May 15, 2013
 */
public class CreateCallPage extends StandardPageFrame {
	Logger log = LoggerFactory.getLogger(CreateCallPage.class);
	/**
	 * @param exec
	 */
	public CreateCallPage(Executor exec) {
		super(exec);
		Assert.assertTrue(isPageLoaded(), "Create Call page has not loaded within 60 seconds");	
	}

	/* (non-Javadoc)
	 * @see com.ibm.salesconnect.model.StandardPageFrame#isPageLoaded()
	 */
	@Override
	public boolean isPageLoaded() {
		return waitForPageToLoad(pageLoaded);
	}

	//Selectors
	public static String pageLoaded = "//input[@id='name']";
	public static String creationFrame = "//iframe[@id='bwc-frame']";

	
	public static String getText_CallSubject = "//input[@id='name']";

	public static String getText_CallDate = "//input[@id='date_start_date']";
	public static String getList_CallHours = "//select[@id='date_start_hours']";
	public static String getList_CallMinutes = "//select[@id='date_start_minutes']";
	public static String getList_CallMeridiem = "//select[@id='date_start_meridiem']";
	
	public static String getList_CallDuration = "//select[@id='duration_minutes']";
	
	public static String getText_CallSummary = "//textarea[@id='description']";
	public static String getList_CallStatus = "//select[@id='status']";
	public static String getList_CallType = "//select[@id='call_type']";

	public static String getText_CallAssignedUser = "//input[@id='assigned_user_name']";
	public static String getButton_CallSelectAssignedUser = "//button[@id='btn_assigned_user_name']";
	public static String getButton_CallClearAssignedUser = "//input[@id='btn_clr_assigned_user_name']";
	public static String getButton_AdditionalAssignees= "//button[@id='btn_additional_assignees']";
	
	public static String getList_CallRelatedType = "css=select[id^=parent_type]";
	public static String getText_CallRelatedTo = "//input[@id='related_to_c_1']";
	public static String getButton_RelatedToSelect = "css=button[id^=btn_related_to]";
	public static String getButton_CallSelectRelatedTo = "//input[@id='btn_parent_name']";
	public static String getButton_CallClearRelatedTo = "//input[@id='btn_clr_parent_name']";
	
	public static String saveFooter = "//input[@id='SAVE']";
	public static String getLink_Edit = "//*[@id='edit_button']";
	
	
	//Methods
	public CreateCallPage enterCallInfo(Call call){
		
		if (call.sSubject.length()>0){
			type(getText_CallSubject, call.sSubject);
		}

		if (call.sCallDate.length()>0){
			type(getText_CallDate, call.sCallDate);
		}
	
		if (call.sCallHours.length()>0){
			select(getList_CallHours, call.sCallHours);
		}
	
		if (call.sCallMinutes.length()>0){
			select(getList_CallMinutes, call.sCallMinutes);
		}
	
		if (call.sCallMeridiem.length()>0){
			select(getList_CallMeridiem, call.sCallMeridiem);
		}
	
		if (call.sDuration.length()>0){
			select(getList_CallDuration, call.sDuration);
		}
	
		if (call.sAssignedTo.length()>0){
			type(getText_CallAssignedUser, call.sAssignedTo);
		}
	
		if (call.sSummaryOfCall.length()>0){
			type(getText_CallSummary, call.sSummaryOfCall);
		}

		if (call.sCallStatus.length()>0){
			select(getList_CallStatus, call.sCallStatus);
		}
	
		if (call.sCallType.length()>0){
			select(getList_CallType, call.sCallType);
		}
	
		if (call.sRelatedType.length()>0){
			select(getList_CallRelatedType, call.sRelatedType);
		}
	
		if (call.sRelatedTo.length()>0){
			type(getText_CallRelatedTo, call.sRelatedTo);
		}
		
		return this;
	}
	
	/**
	 * Click the save button
	 */
	public void saveCall(){
		click(saveFooter);
	
		if(waitForPageToLoad(getLink_Edit)){
			switchToMainWindow();	
		}
	}
	
	public void editSubject(String newSubject){
		type(getText_CallSubject, newSubject);
	}
}
