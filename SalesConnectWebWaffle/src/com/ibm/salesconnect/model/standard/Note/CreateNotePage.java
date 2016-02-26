/**
 * 
 */
package com.ibm.salesconnect.model.standard.Note;

import org.openqa.selenium.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardCreatePage;
import com.ibm.salesconnect.model.standard.Lead.CreateLeadPage;
import com.ibm.salesconnect.objects.Note;

/**
 * @author timlehane
 * @date May 15, 2013
 */
public class CreateNotePage extends StandardCreatePage {
	Logger log = LoggerFactory.getLogger(CreateNotePage.class);
	
	/**
	 * @param exec
	 */
	public CreateNotePage(Executor exec) {
		super(exec);
		//Assert.assertTrue(isPageLoaded(), "Create Note page has not loaded within 60 seconds");	
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
	
	public static String getTextField_Subject = "//input[@name='name']";
	public static String getTextField_AssignedTo = "//input[@id='assigned_user_name']";
	public static String getButton_AssignedTo_Search = "//button[@id='btn_assigned_user_name']";
	public static String getButton_AssignedTo_Clear = "//button[@id='btn_clr_assigned_user_name']";
	public static String getButton_AdditionalAssignees= "//button[@id='btn_additional_assignees']";
	public static String getListBox_RelatedTo = "css=select[id^=parent_type]";
	public static String getTextArea_RelatedTo = "//input[@id='parent_name']";
	public static String getButton_RelatedTo_Search = "//button[@id='btn_parent_name']";
	public static String getButton_RelatedTo_Clear = "//button[@id='btn_clr_parent_name']";
	public static String getLink_Save ="//div[@id='drawers']//a[@name='save_button']";
	public static String getLink_SaveEdit ="//a[@name='save_button']";
	public static String getLink_Edit = "//*[@id='edit_button']";
	public static String getButton_AdditionalAssignees_dropdown="//span[@data-fieldname='additional_assignees']//span[@class='select2-arrow']";
    public static String getAdditional_assignees_Search=" //div[contains(@class,'select2-drop-active')]//input[contains(@id,'_search')]";
	//public static String getDropDown_AdditionalAssignee = "//div[@data-name='additional_assignees']//a";
	public static String selectType = "//div[@id='select2-drop']/div/input";
	public static String selectMatch = "//span[@class='select2-match']";
	public static String getTextField_Description="//textarea[@name='description']";
	
	public static String getTextField_AdditionalAssignee = "//input[@placeholder='Type to make a selection']";
	//Methods
	/**
	 * Enter information for the note to be created
	 * @return CreateNotePage object 
	 */
	public CreateNotePage enterNoteInfo(Note note){

		if (note.sSubject.length() > 0)
		{
			type(getTextField_Subject, note.sSubject);
			triggerChange("css=input[name=\"name\"]");
		}

        if (note.sRelatedToType.length() > 0) {
        	select(getListBox_RelatedTo, note.sRelatedToType);
        }

        if (note.sRelatedToName.length() > 0) {
        	type(getTextArea_RelatedTo, note.sRelatedToName);
        }

		if (note.sDescription.length() > 0)
		{
			type(getTextField_Description, note.sDescription);
		}
		
		if (note.sAssignedTo.length() > 0){
			type(getTextField_AssignedTo, note.sAssignedTo);
		}
		if (note.sAdditional_assignees.length()>0) {
			click(getButton_AdditionalAssignees_dropdown);
			type(getTextField_AdditionalAssignee, note.sAdditional_assignees);
			sendKeys(Keys.ENTER);
			
		}
		
		
		return this;
	}
	
	
	
	/**
	 * Save the current note
	 * @return ViewNotePage
	 */
	public ViewNotePage saveNote(){
		click(getLink_Save);
		sleep(10);
		return new ViewNotePage(exec);
	}
	
	/**
	 * Save the current note
	 * @return ViewNotePage
	 */
	public void saveEditedNote(){
		click(getLink_SaveEdit);
		sleep(10);
	}
	
	/**
	 * Edit the note subject
	 */
	public void editNoteSubject(Note note){
		type(getTextField_Subject, note.sSubject);
		triggerChange("css=input[name=\"name\"]");
	}


}
