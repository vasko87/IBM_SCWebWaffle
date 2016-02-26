package com.ibm.appium.model.Task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.appium.model.MobilePageFrame;

public class TaskDetailPage extends MobilePageFrame {
	Logger log = LoggerFactory.getLogger(TaskDetailPage.class);

	public TaskDetailPage() {
		Assert.assertTrue(isPageLoaded(), "Task Detail Page has not loaded");
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
	 * Get Task text located on task detail page for Task verification.
	 */
	public String getTaskName() {
		// return
		// getText("//a[@class='title append-root']//span[@class='value']");
		return getText("//a[@class='title append-root']/child::span/child::div/child::span/following-sibling::*[1]");
	}

	public String getOwner() {
		return getText("//a[@class='title append-root']/following-sibling::*[2]/child::div/child::span/following-sibling::*[1]");
	}

	public String getTaskType() {
		return getText("//a[@class='title append-root']/following-sibling::*[3]/child::div/child::span/following-sibling::*[1]");
	}

	public String getPriority() {
		return getText("//a[@class='title append-root']/following-sibling::*[4]/child::div/child::span/following-sibling::*[1]");
	}

	public String getStatus() {
		return getText("//a[@class='title append-root']/following-sibling::*[5]/child::div/child::span/following-sibling::*[1]");
	}

	public String getDescription() {
		return getText("//a[@class='title append-root']/following-sibling::*[6]/child::div/child::span/following-sibling::*[1]");
	}

	/**
	 * Open edit task menu from task detail page.
	 * 
	 * @return CreateTaskPage
	 */
	public CreateTaskPage editTask() {
		click(editButton);
		return new CreateTaskPage();
	}
}
