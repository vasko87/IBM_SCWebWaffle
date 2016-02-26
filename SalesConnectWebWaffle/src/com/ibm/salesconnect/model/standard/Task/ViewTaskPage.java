/**
 * 
 */
package com.ibm.salesconnect.model.standard.Task;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Element;
import com.ibm.atmn.waffle.core.Executor;
import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.model.StandardViewPage;
import com.ibm.salesconnect.model.partials.ConnectionsBusinessCard;
import com.ibm.salesconnect.objects.Task;

/**
 * @author timlehane
 * @date May 14, 2013
 */
public class ViewTaskPage extends StandardViewPage {
	Logger log = LoggerFactory.getLogger(ViewTaskPage.class);
	/**
	 * @param exec
	 */
	public ViewTaskPage(Executor exec) {
		super(exec);
		sleep(1);
		Assert.assertTrue(isPageLoaded(), "View Task page has not loaded within 60 seconds");	
	}

	//Selectors
	public static String businessCardLocation = "//td[@data-type='relate']//a";
	
	public static String getSearchFormType = "//form[@id='search_form']//input[@name='searchFormTab']";
	
	public String getSearchField(String searchType){ return  "//input[@id='name_" + searchType + "']";}
	public String getMyTasks(String searchType){ return  "//input[@id='current_user_only_" + searchType + "']";}
	public String getAssignedUser(String taskName){ return "//*//a[contains(text(),'"+taskName+"')]";}
	public String businessCard = "//a[@class='lotusVCardHover']";
	
	public String getTaskSelection(String taskName) {return "//tbody//a[contains(text(),'" + taskName + "')]";}
	public static String getAllFavoriteButtons = "//div[@class='star']//div[@class='off']";
	public static String getAllUnfavoriteButtons = "//div[@class='star']//div[@class='on']";
	public static String getAllTaskNames = "//td[@scope='row']//a";
	
	//Methods
	/**
	 * Gets the current search form status 
	 * returns basic/advanced
	 * 
	 * @deprecated
	 */
	public String getSearchtype(){
		String value = getObjectAttribute(getSearchFormType, "value");
		return value.substring(0,value.length()-7);
	}
	
	/**
	 * Searches for a task based on the parameters
	 * @param task
	 */
	public void searchForTask(Task task){
		type(searchBarTextBox, task.sName);
	}
	
	/**
	 * Searches for a task based on the parameters
	 * @param task
	 */
	public void searchForTask(String taskSubject){
		type(searchBarTextBox, taskSubject);
	}
	
	/**
	 * Click on the correct search result
	 * @param task
	 * @return new task detail page object
	 */
	public TaskDetailPage selectResult(Task task) {
		searchNoFilter(task.sName);
		click(getTaskSelection(task.sName));
		return new TaskDetailPage(exec);
	}
	
	/**
	 * Click on the correct search result
	 * @param task
	 * @return new task detail page object
	 */
	public TaskDetailPage selectResult(String taskSubject) {
		searchNoFilter(taskSubject);
		click(getTaskSelection(taskSubject));
		return new TaskDetailPage(exec);
	}
	
	/**
	 * Confirm task results is displayed
	 * @param task
	 * @return true if visible false if not
	 */
	public Boolean confirmResult(Task task) {
		if (task.sName == getObjectText("//td[@data-type='name']//a")) {
			return true;
		}
		return false;
	}
	
	public TaskDetailPage selectResultWithFilter(Task task){
		searchWithFilter(task.sName);
		click(getTableLocation(1, getColumnIndex("Subject")) + "//a");
		return new TaskDetailPage(exec);
	}
	
	public TaskDetailPage selectResultWithFilter(String taskSubject){
		searchWithFilter(taskSubject);
		click(getTableLocation(1, getColumnIndex("Subject")) + "//a");
		return new TaskDetailPage(exec);
	}
	
	public ConnectionsBusinessCard getBusinessCard(Task task){
//		isPresent(getAssignedUser(task.sName));
//		mouseHoverSalesConnect(getAssignedUser(task.sName));
//		click(businessCard);
		checkForElement(getAssignedUser(task.sAssignedTo));
		mouseHoverSalesConnect(getAssignedUser(task.sAssignedTo));
		click(businessCard);
		return new ConnectionsBusinessCard(exec);
	}
	
	public void verifyBusinessCard(User user, Task task){
		ConnectionsBusinessCard connectionsBusinessCard = openConnectionsBusinessCard(businessCardLocation, exec);
		connectionsBusinessCard.verifyBusinessCardContents(user);
	}
	
	public List<String> addAllVisibleTasksToFavorites(){
		List<String> tasks = new ArrayList<String>();
		
		List<Element> favoriteButtons = exec.getElements(getAllFavoriteButtons);
		List<Element> taskElements = exec.getElements(getAllTaskNames);
		exec.executeScript("window.scrollBy(0,arguments[0])", 300);
		for(int x=0; x<favoriteButtons.size(); x++){
			
			favoriteButtons.get(x).click();
			tasks.add(taskElements.get(x).getText());
		}
		
		return tasks;
	}
	
	public List<String> getAllVisibletasks(){
		List<String> tasks = new ArrayList<String>();
		
		exec.executeScript("window.scrollBy(0,arguments[0])", 300);
		List<Element> taskElements  = exec.getElements(getAllTaskNames);
		
		for(int x=0; x<taskElements.size(); x++){
			tasks.add(taskElements.get(x).getText());
		}
		
		return tasks;
	}
	
	public List<String> removeAllVisibletasksFromFavorites(){
		List<String> tasks = new ArrayList<String>();
		
		List<Element> favoriteButtons = exec.getElements(getAllUnfavoriteButtons);
		List<Element> taskElements = exec.getElements(getAllTaskNames);
		exec.executeScript("window.scrollBy(0,arguments[0])", 300);
		for(int x=0; x<taskElements.size(); x++){
			
			favoriteButtons.get(x).click();
			tasks.add(taskElements.get(x).getText());
		}
		
		return tasks;
	}
}
