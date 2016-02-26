/**
 * 
 */
package com.ibm.salesconnect.model.partials;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.model.StandardPageFrame;
import com.ibm.salesconnect.objects.Call;
import com.ibm.salesconnect.objects.Task;

/**
 * @author timlehane
 * @date Jun 17, 2013
 */
public class ActivitiesSubpanel extends StandardPageFrame {
	Logger log = LoggerFactory.getLogger(ActivitiesSubpanel.class);

	/**
	 * @param exec
	 */
	public ActivitiesSubpanel(Executor exec) {
		super(exec);
		Assert.assertTrue(isPageLoaded(), "ActivitiesSubpanel has not loaded within 60 seconds");	
	}


	@Override
	public boolean isPageLoaded() {
		return waitForSubpanelToLoad(pageLoaded);
	}
	
	//Selectors
	public static String pageLoaded = "//div[@id='list_subpanel_activities']//a[contains(text(),'Status')]";
	public static String createTaskButton = "//a[contains(@id, 'Activities_createtask_button')]";
	public static String taskNameField = "//input[@name='name']"; ////div[@id='drawers']/div/div/div/div/div/div/div/h1/span[2]/span/span[2]/span/input
	public static String taskPriorityDropdown = "//div[@data-name='priority']//a";
	public static String taskStatusDropdown = "//div[@data-name='status']//a[contains(@class, 'select2-choice')]";
	public static String saveTaskButton = "//a[@name='save_button']";
	public static String saveCallButton = "//form[@id='form_SubpanelQuickCreate_Calls']//div[@class='buttons']//input[@id='Calls_subpanel_save_button']";
	public String closeButton(String description){ return "//div[@id='list_subpanel_activities']//a[contains(text(),'" + description + "')]/../../..//a[contains(text(),'close')]";};
	public static String status = "//div[@id='alerts']";
	public String getBusinessCard ="//span[@sugar='slot4b']//a";
	public String closeActivityDialog = "//div[@id='closeActivityDialog']//button[contains(text(),'OK')]";
	public static String moreActions = "//div[@id='list_subpanel_activities']/table/tbody/tr/td/table/tbody/tr/td/ul/li/span";
	public static String logCall = "//a[@id='Activities_logcall_button']";
	public static String callSubjectField = "//input[@id='name']";
	public static String callStatus = "//select[@id='status']";
	
public void enterTaskInfo(Task task){
		
		if(task.sName.length() > 0){
			checkForElement(taskNameField);
			//scrollElementToMiddleOfBrowser(taskNameField);
			type(taskNameField,task.sName);
		}
		
		/*
		if(task.sPriority.length() > 0){
			select(taskPriorityDropdown, task.sPriority);
		}
		
		if(task.sStatus.length() > 0){
			select(taskStatusDropdown,task.sStatus);
		}
		*/
}
	
	public void enterCallInfo(Call call){
		if (call.sCallStatus.length()>0){
			select(callStatus, call.sCallStatus);
		}
		if (call.sSubject.length()>0){
			type(callSubjectField, call.sSubject);
		}
	}
	
	public void openCreateTaskForm(){
		click(createTaskButton);
		exec.switchToFrame().returnToTopFrame();
		waitForSubpanelToLoad(taskNameField);
		sleep(1);
}
	
	public void moreActions(){
		click(moreActions);
	}
	
	public void openCreateCallForm(){
		click(logCall);
		//click(moreActions);
		//select(moreActions,logCall);
	}
	
	public void saveTask(){
		isPresent(saveTaskButton);
		//scrollElementToMiddleOfBrowser(saveTaskButton);
		click(saveTaskButton);
		//if(!isSaved(status)){
		//	Assert.assertTrue(false, "Task has not been saved successfully");
		//}
}
	
	public void saveCall(){
		click(saveCallButton);
	}
	
	/**
	 * Complete an open task
	 * description is the name of the task
	 */
	public void completeTask(String description){
		click(closeButton(description));
		click(closeActivityDialog);
	}
	
	public void verifyBusinessCard(User user){
		ConnectionsBusinessCard connectionsBusinessCard = openConnectionsBusinessCard(getBusinessCard, exec);
		connectionsBusinessCard.verifyBusinessCardContents(user);
	}
}
