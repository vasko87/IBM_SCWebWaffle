package com.ibm.appium.model.Task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.appium.Objects.Task;
import com.ibm.appium.model.MobilePageFrame;

public class CreateTaskPage extends MobilePageFrame {
	Logger log = LoggerFactory.getLogger(CreateTaskPage.class);

	public CreateTaskPage() {
		Assert.assertTrue(isPageLoaded(), "Create Task Page has not loaded");
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
	public static String taskNameField = "//label[contains(text(),'Subject')]/..//input";
	public static String priorityMenu = "//select[@name='priority']";
	public static String statusMenu = "//select[@name='status']";
	public static String saveTaskButton = "//span[contains(@class,'saveBtn')]";
	public static String taskTypeMenu = "//select[@name='call_type']";
	public static String descriptionField = "//div[@name='description']";
	public static String descriptionTextArea = "//textarea[@name='somename']";
	public static String saveButton = "//span[@track='click:save']";

	/**
	 * Main method to populate Task form.
	 */
	public CreateTaskPage enterTaskInfo(Task task) {
		if (task.getsName().length() > 0) {
			type(taskNameField, task.getsName());
		}
		if (task.getsTaskType().length() > 0) {
			select(taskTypeMenu, task.getsTaskType());
		}
		if (task.getsPriority().length() > 0) {
			select(priorityMenu, task.getsPriority());
		}
		if (task.getsStatus().length() > 0) {
			select(statusMenu, task.getsStatus());
		}
		if (task.getsDescription().length() > 0) {
			click(descriptionField);
			type(descriptionTextArea, task.getsDescription());
			click(saveButton);
		}
		return this;
	}

	/**
	 * Update existing Task.
	 */
	public void updateTaskInfo(Task task) {
		if (task.getsNameUPD().length() > 0) {
			type(taskNameField, task.getsNameUPD());
		}
		return;
	}

	/**
	 * Save task form and navigate to Task detail page.
	 * 
	 * @return TaskDetailPage
	 */
	public TaskDetailPage saveTask() {
		click(saveTaskButton);
		return new TaskDetailPage();
	}
}
