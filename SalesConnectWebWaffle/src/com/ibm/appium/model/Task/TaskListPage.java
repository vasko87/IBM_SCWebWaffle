package com.ibm.appium.model.Task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.appium.Objects.Task;
import com.ibm.appium.model.MobilePageFrame;

public class TaskListPage extends MobilePageFrame {
	Logger log = LoggerFactory.getLogger(TaskListPage.class);

	// XPath Selectors
	public static String pageLoaded = "//div[@id='listing-Tasks']";
	public static String searchField = "//input[@class='search-query']";
	public static String searchButton = "(//a[@track='click:search'])[1]";

	// Task Sorting
	public static String sortIcon = "//i[@id='sort-btn']";
	public static String sortByTaskName = "//div[@data-index='0']";
	public static String sortByDueDate = "//div[@data-index='1']";
	public static String sortByPriority = "//div[@data-index='2']";
	public static String sortByStatus = "//div[@data-index='3']";
	public static String sortByDateModified = "//div[@data-index='4']";;
	public static String sortDecending = "//a[@id='desc']";
	public static String sortAscending = "//a[@id='asc']";
	public static String sortDone = "//a[@class='cancel fast-click-highlighted']";
	
	public TaskListPage() {
		Assert.assertTrue(isPageLoaded(), "Task List Page has not loaded");
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

	/**
	 * Get specific Task record from Task list view.
	 * 
	 * @param sName
	 *            - Task name
	 */
	public String getResult(String sName) {
		return "//div[@id='listing-Tasks']//div[contains(text(),'" + sName
				+ "')]";
	}
	
	/**
	 * Get specific Task record from Task list view.
	 * 
	 * @param sName
	 *            - Task name
	 */
	public String searchedListItem(String sName) {
		return "//div[@id='listing-Tasks']//span[contains(text(),'" + sName
				+ "')]";
	}
	
	
	/**
	 * Search for specific task using search input box located on task list
	 * view.
	 */
	public TaskDetailPage searchForTask(Task task) {
		
		String searchQuery = task.getsName();
		
		log.info("Searching for item" + searchQuery);
		click(searchButton);
		click(searchField);
		log.info("Clearing search field if needed");
		clear();
		log.info("Typing search phrase");
		type(searchField, searchQuery);
		log.debug("Typed in. Waiting for element: " + searchedListItem(searchQuery));
		waitForElementPresent(searchedListItem(searchQuery), 30);
		log.debug("Done waiting. Is it present?");
		
		if(isPresent(searchedListItem(searchQuery))){
			log.debug("Element found. Clicking.");
			click(searchedListItem(searchQuery));
		}
		else {
			log.debug("Element not found. Trying search again.");
			click(searchField);
			clear();
			log.info("Typing search phrase again");
			type(searchField, searchQuery);				
			log.debug("Typed in. Waiting for element.");
			waitForElementPresent(searchedListItem(searchQuery), 15);
			
			log.debug("Done waiting. Is it present?");
			if(isPresent(searchedListItem(searchQuery))){
				log.debug("Element found. Clicking.");
				click(searchedListItem(searchQuery));
			}
			else {
				log.debug("Element not found. Trying search last time.");
				click(searchField);
				clear();
				log.info("Typing search phrase again");
				type(searchField, searchQuery);				
				waitForElementPresent(searchedListItem(searchQuery), 15);
				click(searchedListItem(searchQuery));
			}
		}
		return new TaskDetailPage();
		
	}

	/**
	 * Select result after searching for task record and navigate to Task detail
	 * page.
	 * 
	 * @return TaskDetailPage
	 */
	public TaskDetailPage selectResult(Task task) {
		click(getResult(task.getsName()));
		return new TaskDetailPage();
	}

	/**
	 * Select result after searching for task record and navigate to Task detail
	 * page.
	 * 
	 * @return TaskDetailPage
	 */
	public TaskDetailPage selectListItem(Task task) {
		click(searchedListItem(task.getsName()));
		return new TaskDetailPage();
	}
	
	/**
	 * 
	 * @param byOption
	 * @param byOrder
	 */
	public void sortTask(String byOption, String byOrder) {
		log.info("Sorting list Decending by Task modified");
		click(sortIcon);
		click(byOption);
		click(byOrder);
		click(sortDone);
	}
}
