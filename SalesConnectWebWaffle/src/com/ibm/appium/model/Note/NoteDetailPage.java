package com.ibm.appium.model.Note;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.appium.model.MobilePageFrame;

public class NoteDetailPage extends MobilePageFrame {

	Logger log = LoggerFactory.getLogger(NoteDetailPage.class);

	// XPath Selectors
	public static String pageLoaded = "//a[@class='title append-root']//span[@class='value'] ";
	public static String editButton = "//a[@class='icon icon-pencil fast-click-highlighted append-root']";
	public static String noteOwner = "//a[@class='title append-root']/following-sibling::*[1]/child::div/child::span/following-sibling::*[1]";
	public static String noteSubject = "//a[@class='title append-root']/child::span/child::div/child::span/following-sibling::*[1]";
	public static String noteDescription = "//a[@class='title append-root']/following-sibling::*[4]/child::div/child::span/following-sibling::*[1]";
	public static String favoriteIcon = "//a[@class='title append-root']/following-sibling::*[4]/child::div/child::span/following-sibling::*[1]";
	
	
	public NoteDetailPage() {
		Assert.assertTrue(isPageLoaded(), "Note Detail Page has not loaded");
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
	 * Get Note text located on note detail page for Verification.
	 */
	public String getNoteSubject() {
		return getText(noteSubject);
	}

	public String getNoteOwner() {
		return getText(noteOwner);
	}

	public String getNoteDescription() {
		return getText(noteDescription);
	}

	/**
	 * Open edit note menu from note detail page.
	 * 
	 * @return CreateNotePage
	 */
	public CreateNotePage editNote() {
		click(editButton);
		return new CreateNotePage();
	}
	
}
