package com.ibm.salesconnect.test.Level1;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.ibm.appium.Objects.Task;
import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;
import com.ibm.salesconnect.model.standard.Dashboard;
import com.ibm.salesconnect.test.mobile.TaskRestAPITests;
import com.ibm.salesconnect.model.standard.Task.TaskDetailPage;
import com.ibm.salesconnect.model.standard.Task.ViewTaskPage;

/**
 * <strong>Description:</strong>
 * <br/><br/>
 * Test to validate the View, Read, Convert to Favorite and Search for Favorite Task functionality of the Task module
 * <br/><br/>
 * 
 * @author 
 * Ramesh Rangoju 
 * */

public class AT_FavoriteTask extends ProductBaseTest {
	Logger log = LoggerFactory.getLogger(AT_Sugar.class);
	Task favoriteTask;
	
	/**
	 * <br/><br/>
	 * Test steps
	 * <ol>
	 * <li>Open browser and log in</li>
	 * <li>Create Task</li>
	 * <li>Search for created Task</li>
	 * <li>Open Task detail page</li>
	 * <li>Convert as FavoriteTask</li>
	 * <li>Search for converted  Favorite Task </li>
	 * <li>Verify the FavoriteTask</li>
	 * </ol>
		 **/

@Test(groups = { "Level1","AT_Sugar","BVT","BVT1"})
public void Test_AT_FavoriteTask() throws SQLException, InterruptedException
{
	
	log.info("Start of test method Test_AT_Task");
	User user1 = commonUserAllocator.getGroupUser(GC.busAdminGroup,this);
	log.info("Logging in");
	Dashboard dashboard = launchWithLogin(user1);

	log.info("Creating task via API");
	String taskSubject = "Favorite Task Subject";
	TaskRestAPITests.createTaskHelper(user1,"Favorite Task Subject",log,baseURL);
		
	log.info("Searching for created Task");
	ViewTaskPage viewTaskPage = dashboard.openViewTask();
	viewTaskPage.searchForTask(taskSubject);

	TaskDetailPage taskDetailPage = viewTaskPage.selectResult(taskSubject);
	taskDetailPage.addToFavorite();
	
	log.info("Searching for created task");
	viewTaskPage = dashboard.openViewTask();
	viewTaskPage.searchForTask(taskSubject);
    viewTaskPage.selectFilterElement("My Favorites");
	taskDetailPage = viewTaskPage.selectResultWithFilter(taskSubject);

	Assert.assertEquals(taskDetailPage.getdisplayedTaskName().trim(),taskSubject,"Incorrect task detail page was opened");
	log.info("End of test method Test_AT_favoriteTask");
}
}

