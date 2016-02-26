/**
 * 
 */
package com.ibm.salesconnect.model.standard.Task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardDetailPage;
import com.ibm.salesconnect.model.StandardPageFrame;
import com.ibm.salesconnect.model.partials.ConnectionsBusinessCard;
import com.ibm.salesconnect.model.standard.Call.CreateCallPage;

/**
 * @author timlehane
 * @date May 14, 2013
 */
public class TaskDetailPage extends StandardDetailPage {
	Logger log = LoggerFactory.getLogger(TaskDetailPage.class);

	/**
	 * @param exec
	 */
	public TaskDetailPage(Executor exec) {
		super(exec);
		Assert.assertTrue(isPageLoaded(), "Task Detail page has not loaded within 60 seconds");	
	}

	//Selectors
	public static String pageLoaded = "//span[@data-fieldname='name']";
	public static String detailsFrame = "//iframe[@id='bwc-frame']";
	
	public static String displayedTaskName = "//span[@data-fieldname='name']//div";
	public static String getAssignedUser = "//a[@class='fn url hasHover']";
	public static String businessCard = "//a[@class='lotusVCardHover']";
	public static String MyFavouritesIcon = "//*[@id='content']/div[1]/h2/div/div";
	public static String editButton = "//a[@name='edit_button']";
	public static String editDropDown = "//div[@id='Tasks']//a[@data-original-title='Actions']";
	public static String deleteOption = "//a[@name='delete_button']";
	public static String confirmDelete = "//div[@id='alerts']//a[@data-action='confirm']";
	
	//Methods
	/**
	 * Returns the displayed task name
	 * @return displayed task name
	 */
	public String getdisplayedTaskName(){
		return getObjectText(displayedTaskName);
	}
	
	public ConnectionsBusinessCard getBusinessCard(){
		mouseHoverSalesConnect(getAssignedUser);
		isPresent(businessCard);
		click(businessCard);
		return new ConnectionsBusinessCard(exec);
	}

	public void addTaskToMyFavorites() {
	
		click (MyFavouritesIcon);// clicking on myFavourites Icon
		log.info("Task Added to My Favorites");
	}
	
	/**
	 * Opens the edit call page
	 */
	public CreateTaskPage openEditPage(){
		click(editButton);
		return new CreateTaskPage(exec);
	}
	
	public void deleteTask(){
		click(editDropDown);
		click(deleteOption);
		click(confirmDelete);
	}

}
