/**
 * 
 */
package com.ibm.salesconnect.model.standard.Task;

import org.openqa.selenium.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardCreatePage;
import com.ibm.salesconnect.objects.Task;

/**
 * @author timlehane
 * @date May 14, 2013
 */
public class CreateTaskPage extends StandardCreatePage {
	Logger log = LoggerFactory.getLogger(CreateTaskPage.class);
	/**
	 * @param exec
	 */
	public CreateTaskPage(Executor exec) {
		super(exec);
		//Assert.assertTrue(isPageLoaded(), "Create Task page has not loaded within 60 seconds");	
	}

	//Selectors
	public static String pageLoaded = "//span[@data-name='name']//input[@name='name']";
	//public static String creationFrame = "//iframe[@id='bwc-frame']";
	public static String loadingAlertMessage = "//div[contains(*, 'alert alert-process']//strong";
	
	public static String getTextField_Subject = "//span[@data-name='name']//input[@name='name']";
	
	public static String getTextField_DueDate = "//div[@data-name='date_due']//input[@data-type='date']";
	public static String getListBox_DueDateTime = "//div[@data-name='date_due']//input[@data-type='time']";
	//public static String getListBox_DueDateMinutes = "//select[@id='date_due_minutes']";
	//public static String getListBox_DueDateMeridiem = "//select[@id='date_due_meridiem']";
	
	//public static String getListBox_Priority = "//html/body/div[1]/div/div[4]/div/div/div[1]/div[1]/div/div/div[2]/div[1]/div/div[1]/div[1]/span/span/div/a";
	public static String getListBox_Priority ="//div[@data-name='priority']//a";
	public static String getListBox_TaskType = "//div[@data-name='call_type']//a";
	public static String getTextField_Description = "//textarea[@name='description']";
	public static String getDropDown_AssignedTo = "//div[@data-name='assigned_user_name']//input";
	public static String getTextField_AssignedTo = "//input[@placeholder='Type to make a selection']";
	public static String getDropDown_AdditionalAssignee = "//div[@data-name='additional_assignees']//a";
	public static String getTextField_AdditionalAssignee = "//input[@placeholder='Type to make a selection']";
	public static String getListBox_Status = "//div[@data-name='status']//a";
	
	
	public static String getListBox_RelatedToType = "//span[@data-fieldname='related_to_c']//div[@class='span5']//input[@role='button']";
	public static String getDropDown_RelatedTo = "//span[@data-fieldname='related_to_c']//div[@class='span7 control-group']//input";
	public static String getTextField_RelatedTo = "//input[@id='related_to_c_1']";
	
	public static String getLink_Save = "//div[@id='Tasks']//a[@name='save_button']";
	public static String getLink_Edit = "//*[@id='edit_button']";
	
	//Methods
	/**
	 * 
	 */
	public CreateTaskPage enterTaskInfo(Task task) {
		if (task.sDueDate.length()>0){
			type(getTextField_DueDate, task.sDueDate);
		}
		
		if (task.sDueDateHours.length()>0){
			select(getListBox_DueDateTime, task.sDueDateHours + ':' + task.sDueDateMinutes + task.sDueDateMeridiem);
		}
				
		if (task.sPriority.length()>0){
			sidecarListBoxSelect(getListBox_Priority, task.sPriority);
		}
		
		if(task.sTaskType.length()>0){
			sidecarListBoxSelect(getListBox_TaskType, task.sTaskType);
		}
		
		if (task.sAssignedTo.length()>0){
			click(getDropDown_AssignedTo);
			type(getTextField_AssignedTo, task.sAssignedTo);
			sendKeys(Keys.ENTER);
		}
		
		if(task.sAdditionalAssignee.length()>0){
			click(getDropDown_AdditionalAssignee);
			type(getTextField_AdditionalAssignee, task.sAdditionalAssignee);
			sendKeys(Keys.ENTER);
		}

		if (task.sDescription.length()>0){
			type(getTextField_Description, task.sDescription);
			triggerChange("css=textarea[name=\"description\"]");
		}

		if (task.sStatus.length()>0){
			sidecarListBoxSelect(getListBox_Status, task.sStatus);
		}
		
		if (task.sRelatedToType.length()>0){
			select(getListBox_RelatedToType, task.sRelatedToType);
		}
		
		if (task.sRelatedTo.length()>0){
			type(getTextField_RelatedTo, task.sRelatedTo);
		}
		
		if (task.sName.length()>0){
			type(getTextField_Subject, task.sName);
			triggerChange("css=input[name=\"name\"]");
		}
		
		return this;
	}

	/**
	 * Save the current task
	 */
	public ViewTaskPage saveTask(){
		sleep(10);
		click(getLink_Save);
		return new ViewTaskPage(exec);
	}
	
	public void editName(String newName){
		type(getTextField_Subject, newName);
	}
	
}
