package com.ibm.salesconnect.model.partials;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardPageFrame;


public class NotesSubpanel extends StandardPageFrame {
	Logger log = LoggerFactory.getLogger(NotesSubpanel.class);
	/**
	 * @param exec
	 */
	public NotesSubpanel(Executor exec) {
		super(exec);
		Assert.assertTrue(isPageLoaded(), "Notes Subpanel has not loaded within 60 seconds");	
	}

	@Override
	public boolean isPageLoaded() {
		scrollToBottomOfPage();
		return waitForSubpanelToLoad(pageLoaded);
	}
	
	// selectors
//	public static String pageLoaded = "//div[@id='list_subpanel_completedActivitiesandNotes']//a[contains(text(),'Status')]";
	public static String pageLoaded = "//a[@id='History_createnote_button_create_']";
	public static String createFormLoaded = "//input[@name='name']";	
	//public static String createNoteButton = "//a[@id='completedActivitiesandNotes_createnote_button']";
	public static String createNoteButton ="//a[contains(text(),'Create Note')]";
	//public static String createNoteButton = "//a[@id='History_createnote_button']";
	public static String status = "//div[@id='ajaxStatusDiv']";
	public static String notesubjectField = "//input[@name='name']";
	public static String saveButton ="//div[@id='drawers']//a[@name='save_button']";
	public void openCompletedActivitiesandNotesSubpanelForm(){
		click(createNoteButton);
		waitForSubpanelToLoad(createFormLoaded);
	}
	
	public void enterNotesInfo(String subject ){
		        switchToMainWindow();
		        type(notesubjectField, subject);
				
		
	}
	
	public void saveForm(){
		scrollElementToMiddleOfBrowser(saveButton);
		click(saveButton);
		//if(!isSaved(status)){
		//Assert.assertTrue(false, "Note has not been saved successfully");
		//}
	}
	
	public void checkNotePresent(String subject){
		if(!isTextPresent(subject)){
			Assert.assertTrue(false, "Saved note is not visible on page");
		}
	}
	
	
}
