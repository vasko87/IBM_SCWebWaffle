package com.ibm.salesconnect.test.Level0;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.API.TaskRestAPI;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.model.standard.Task.CreateTaskPage;
import com.ibm.salesconnect.model.standard.Task.TaskDetailPage;
import com.ibm.salesconnect.model.standard.Task.ViewTaskPage;
import com.ibm.salesconnect.objects.Task;

/**
 * <strong>Description:</strong>
 * <br/><br/>
 * Test to validate the Create, Read, Update and Delete functionality of the Tasks module
 * <br/><br/>
 * 
 * @author 
 * Tim Lehane
 * 

 */
public class AT_Task extends ProductBaseTest {
	Logger log = LoggerFactory.getLogger(AT_Task.class);

	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Open browser and log in</li>
	 * <li>Create Task</li>
	 * <li>Search for created Task</li>
	 * <li>Open Task detail page</li>
	 * <li>Update Task description</li>
	 * <li>Delete Task</li>
	 * <li>Confirm deletion</li>
	 * </ol>
	 */
	@Test(groups = { "Level1","AT_Sugar","BVT", "BVT0"})
	public void Test_AT_Task(){
		log.info("Start of test method Test_AT_Task");

		Task task = new Task();
		task.populate();
		User user1 = commonUserAllocator.getUser(this);

		task.populate();

		Dashboard dashboard = launchWithLogin(user1);

		log.info("Create a task");
		CreateTaskPage createTaskPage = dashboard.openCreateTask();
		createTaskPage.enterTaskInfo(task);
		ViewTaskPage viewTaskPage = createTaskPage.saveTask();

		log.info("Searching for created Task");
		viewTaskPage.searchForTask(task);
		TaskDetailPage taskDetailPage = viewTaskPage.selectResult(task);

		Assert.assertEquals(taskDetailPage.getdisplayedTaskName().trim(),task.sName,"Incorrect task detail page was opened");
		
		
		log.info("Editing call");
		CreateTaskPage editTaskPage = taskDetailPage.openEditPage();
		task.setTaskName(task.sName + "Edited");
		editTaskPage.editName(task.sName);
		editTaskPage.saveTask();
		
		log.info("Searching for edited Task");
		ViewTaskPage viewEditedTaskage = dashboard.openViewTask();
		viewEditedTaskage.searchForTask(task);
		TaskDetailPage taskEditDetailPage = viewEditedTaskage.selectResult(task);
		
		Assert.assertEquals(taskEditDetailPage.getdisplayedTaskName().trim(),task.sName,"Incorrect task detail page was opened");
		
		taskEditDetailPage.deleteTask();
	
		log.info("Confirming task has been deleted via API");
		LoginRestAPI loginRestAPI = new LoginRestAPI();
		String token = loginRestAPI.getOAuth2Token(testConfig.getBrowserURL(), user1.getEmail(), user1.getPassword());
		
		TaskRestAPI taskRestAPI = new TaskRestAPI();
		taskRestAPI.getTask(testConfig.getBrowserURL(), token, task.getTaskName(), "404");
		
		log.info("End of test method Test_AT_Task");
	}
}
