package com.ibm.appium.model.Note;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.appium.Objects.Note;
import com.ibm.appium.model.MobilePageFrame;

public class CreateNotePage extends MobilePageFrame {
	Logger log = LoggerFactory.getLogger(CreateNotePage.class);

	// XPath Selectors
	public static String pageLoaded = "//label[contains(text(),'Subject')]/..//input";
	public static String noteSubjectField = "//label[contains(text(),'Subject')]/..//input";
	public static String saveNoteButton = "//span[contains(@class,'saveBtn']";
	public static String noteDescriptionField = "//div[@name='description']";
	public static String noteDescriptionTextArea = "//textarea[@name='somename']";
	public static String noteDescriptionSavebtn = "//span[@track='click:save']";
	
	public CreateNotePage() {
		Assert.assertTrue(isPageLoaded(), "Create Note Page has not loaded.");
	}

	/**
	 * Check if new page is loaded by finding element on that page before
	 * starting any actions.
	 */
	@Override
	public boolean isPageLoaded() {
		return waitForPageToLoad(pageLoaded);
	}

	/**
	 * Main method to populate Note form.
	 */
	public CreateNotePage enterNoteInfo(Note note) {
		if (note.getsSubject().length() > 0) {
			type(noteSubjectField, note.getsSubject());
		}
		if (note.getsDescription().length() > 0) {
			click(noteDescriptionField);
			type(noteDescriptionTextArea, note.getsDescription());
			click(noteDescriptionSavebtn);
		}
		return this;
	}

	/**
	 * Update existing Note.
	 */
	public CreateNotePage updateNoteInfo(Note note) {
		if (note.getsSubjectUPD().length() > 0) {
			type(noteSubjectField, note.getsSubjectUPD());
		}
		return this;
	}

	/**
	 * Save note form and navigate to Note detail page.
	 * 
	 * @return NoteDetailPage
	 */
	public NoteDetailPage saveNote() {
		click(noteDescriptionSavebtn);
		return new NoteDetailPage();
	}
}
