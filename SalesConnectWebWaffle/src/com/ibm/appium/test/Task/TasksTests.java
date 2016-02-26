package com.ibm.appium.test.Task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.appium.MobileBaseTest;
import com.ibm.appium.Objects.Task;
import com.ibm.appium.model.Dashboard;
import com.ibm.appium.model.Menus.MainMenu;
import com.ibm.appium.model.Menus.QuickCreateMenu;
import com.ibm.appium.model.Task.CreateTaskPage;
import com.ibm.appium.model.Task.TaskDetailPage;
import com.ibm.appium.model.Task.TaskListPage;
import com.ibm.atmn.waffle.extensions.user.User;

public class TasksTests extends MobileBaseTest {
	
	Logger log = LoggerFactory.getLogger(TasksTests.class);
	Task task = new Task();

	public static String showMore = "//span[@class='show-more']";
	
	/**
	 * Login to Sales Connect mobile, create new task using quick menu and
	 * verify task has been created.
	 */
	@Test
	public void s2419TasksCreate() {
		log.info("Starting method s2419TasksCreate");		
		log.info("Creating test objects");
		User user = commonUserAllocator.getGroupUser("mobile");		
		log.info("Using user: " + user.getEmail());

		try {
			task.populate();
	
			log.info("Logging in");
			Dashboard dashBoard = launchWithLogin(user);
	
			QuickCreateMenu qCM = dashBoard.openQuickCreateMenu();
	
			log.info("Creating task");
			CreateTaskPage createTaskPage = qCM.openCreateTaskPage();
			createTaskPage.enterTaskInfo(task);
			TaskDetailPage taskDetailPage = createTaskPage.saveTask();
	
			log.info("Verifying task creation");
			
			Assert.assertEquals(taskDetailPage.getTaskName(), task.getsName(),
					"Task Name does not match expected"); // name
			// Assert.assertEquals(taskDetailPage.getOwner() , expected,
			// "Task Owner does not match expected"); //owner
			Assert.assertEquals(taskDetailPage.getTaskType(), task.getsTaskType(),
					"Task Type does not match expected"); // type
			Assert.assertEquals(taskDetailPage.getPriority(), task.getsPriority(),
					"Task Priority does not match expected"); // propriety
			Assert.assertEquals(taskDetailPage.getStatus(), task.getsStatus(),
					"Task Status does not match expected"); // status
			Assert.assertEquals(taskDetailPage.getDescription(),
					task.getsDescription(),
					"Task Description does not match expected"); // description
		}
		finally{
			commonUserAllocator.checkInAllGroupUsers("mobile");
			log.info("End method s2419TasksCreate");		
		}	
	}

	@Test(dependsOnMethods = { "s2419TasksCreate" })
	public void s2421TasksDashboardSearch() {
		log.info("Starting method s2421TasksDashboardSearch");		
		log.info("Creating test objects");
		User user = commonUserAllocator.getGroupUser("mobile");		
		log.info("Using user: " + user.getEmail());

		try {
			log.info("Logging in");
			Dashboard dashBoard = launchWithLogin(user);
			dashBoard.openGlobalSearchPage();
			
			TaskDetailPage taskDetailPage = dashBoard.searchForItem(task);
			
			log.info("Verifying task creation");
			
			Assert.assertEquals(taskDetailPage.getTaskName(), task.getsName(),
					"Task Name does not match expected"); // name
		}
		finally{
			commonUserAllocator.checkInAllGroupUsers("mobile");
			log.info("End method s2421TasksDashboardSearch");		
		}		
	}

	@Test(dependsOnMethods = { "s2419TasksCreate" })
	public void s2421TasksListViewSearchMyItems() {
		log.info("Starting method s2421TasksListViewSearchMyItems");		
		log.info("Creating test objects");
		User user = commonUserAllocator.getGroupUser("mobile");		
		log.info("Using user: " + user.getEmail());

		try {
			log.info("Logging in");
			Dashboard dashBoard = launchWithLogin(user);
	
			log.info("Navigating to tasks list view");
			MainMenu mM = dashBoard.openMainMenu();
			TaskListPage taskListPage = mM.openTaskListView();
			
			TaskDetailPage taskDetailPage = taskListPage.searchForTask(task);
			
			log.info("Verifying task creation");
			
			Assert.assertEquals(taskDetailPage.getTaskName(), task.getsName(),
					"Task Name does not match expected"); // name
		}
		finally{
			commonUserAllocator.checkInAllGroupUsers("mobile");
			log.info("End method s2421TasksListViewSearchMyItems");		
		}	
	}
	
	@Test(dependsOnMethods = { "s2419TasksCreate" })
	public void s2421TasksEdit() {
		log.info("Starting method s2421TasksEdit");		
		log.info("Creating test objects");
		User user = commonUserAllocator.getGroupUser("mobile");		
		log.info("Using user: " + user.getEmail());

		try {
			log.info("Logging in");
			Dashboard dashBoard = launchWithLogin(user);
	
			log.info("Navigating to tasks list view");
			MainMenu mM = dashBoard.openMainMenu();
			TaskListPage taskListPage = mM.openTaskListView();
			
			TaskDetailPage taskDetailPage = taskListPage.searchForTask(task);
			
			log.info("Editing task");
			CreateTaskPage createTaskPage = taskDetailPage.editTask();
			createTaskPage.updateTaskInfo(task);
			createTaskPage.saveTask();
	
			log.info("Verifying task update");
			Assert.assertEquals(taskDetailPage.getTaskName(), task.getsName()
					+ task.getsNameUPD(),
					"Updated Task name does not match expected");
			task.updateDetails();
		}
		finally{
			commonUserAllocator.checkInAllGroupUsers("mobile");
			log.info("End method s2421TasksEdit");		
		}	
	}
}
