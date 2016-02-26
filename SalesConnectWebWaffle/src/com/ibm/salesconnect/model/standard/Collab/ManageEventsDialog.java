/**
 * 
 */
package com.ibm.salesconnect.model.standard.Collab;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Element;
import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.StandardPageFrame;

/**
 * @author timlehane
 * @date Jul 24, 2013
 */
public class ManageEventsDialog extends StandardPageFrame {
	Logger log = LoggerFactory.getLogger(ManageEventsDialog.class);

	/**
	 * @param exec
	 */
	public ManageEventsDialog(Executor exec) {
		super(exec);
		System.out.println(pageLoaded);
		
		Assert.assertTrue(isPageLoaded(), "Manage Events Dialog has not loaded within 60 seconds");
	}


	@Override
	public boolean isPageLoaded() {
		return waitForSubpanelToLoad(pageLoaded);
	}
	
	//Selectors
	public static String pageLoaded = "//span[contains(text(),\"All clients that I am following\")]";
	public String getTab(String tabName) {return "//div[contains(@class,'dijitTabListWrapper')]//span[contains(text(),'" + tabName + "')]";}; 
	public String getSubTab(String tabName) {return "//div[contains(@class,'dijitTabPaneWrapper')]//span[contains(text(),\"" + tabName + "\")]";}; 
	public static String followAllButton = "//div[@class='dijitTabContainer dijitTabContainerTop dijitContainer dijitLayoutContainer dijitTabContainerNested dijitTabPane dijitTabContainerTop-child dijitTabContainerTop-dijitTabContainerTop dijitVisible']//span[text()='Follow All On This Page']";
	public static String stopFollowingAllButton = "//div[@class='dijitTabContainer dijitTabContainerTop dijitContainer dijitLayoutContainer dijitTabContainerNested dijitTabPane dijitTabContainerTop-child dijitTabContainerTop-dijitTabContainerTop dijitVisible']//span[text()='Stop Following All On This Page']";
	public static String opptystartFollowingButtons = "//table[@data-dojo-attach-point='myOpptysTableNode']//span[text()='Start Following']";
	public static String opptystopFollowingButtons = "//table[@data-dojo-attach-point='myOpptysTableNode']//span[text()='Stop Following']";
	public static String clientstartFollowingButtons = "//table[@data-dojo-attach-point='myClientsTableNode']//span[text()='Start Following']";
	public static String clientstopFollowingButtons = "//table[@data-dojo-attach-point='myClientsTableNode']//span[text()='Stop Following']";
	public static String itemsInTab = "//div[@class='dijitTabContainer dijitTabContainerTop dijitContainer dijitLayoutContainer dijitTabContainerNested dijitTabPane dijitTabContainerTop-child dijitTabContainerTop-dijitTabContainerTop dijitVisible']//td[@class='descriptionColumn']//span";
	public static String okButton = ".//*[@id='scdijit_dijit_form_Button_0']";
	public static String pageNumbers = "//div[contains(@class,'dijitVisible')]//span[@class='pageNumbers']";
	
	public String getStopFollowingForItem(String item) {return "//span[contains(text(),'" + item + "')]/../..//span[text()='Stop Following']";}
	public String getFollowingForItem(String item) {return "//span[contains(text(),'" + item + "')]/../..//span[text()='Start Following']";}
	public static String NextFollowingPage = "//button[@name='listViewNextButton']";
	public static String NextFollowingPageDisabled = "//button[@name='listViewNextButton'][@disabled='']";
	public String currentTab = "Clients";
	public static String opptiesNotAssigned = "//div[@id='scdijit_dijit_layout_TabContainer_2_tablist']/div[2]/span[contains(@role,'tab')]";
	public static String clientsNotAssigned = "";
	

	public static String opptyNotFollowingSearchTextBox = "//input[contains(@id,'myOpptysContentPane-contentWidget-filter')]";
	public static String opptyFollowingSearchTextBox = "//input[contains(@id,'allOpptysContentPane-contentWidget-filter')]";
	public static String clientsFollowingSearchTextBox = "//input[contains(@id,'allClientsContentPane-contentWidget-filter')]";
	public static String clientsNotFollowingSearchTextBox = "//input[contains(@id,'myClientsContentPane-contentWidget-filter')]";
	public static String opptyNotFollowingSearchButton = "//div[contains(@id,'myOpptysContentPane')]//span[contains(text(),'Search')]";
	public static String opptyFollowingSearchButton = "//div[contains(@id,'allOpptysContentPane')]//span[contains(text(),'Search')]";
	public static String clientNotFollowingSearchButton = "//div[contains(@id,'myClientsContentPane')]//span[contains(text(),'Search')]";
	public static String clientFollowingSearchButton = "//div[contains(@id,'allClientsContentPane')]//span[contains(text(),'Search')]";
	public static String searchTextBox;
	public static String searchButton;

	
	//Methods
	
	public void openSubTab(String SubTabName){
		click(getSubTab(SubTabName));
	}
	
	public boolean isTabPresent(String tabName){

		if(isVisible(getTab(tabName))){
			return true;
		}

		return false;
	}
	
	public void closeDialog(){
		click(okButton);
	}


	/**
	 * @param clientsassignedto
	 * @return
	 */
	public boolean isSubTabPresent(String tabName) {
		if(isVisible(getSubTab(tabName))){
			return true;
		}

		return false;
	}


	/**
	 * 
	 */
	public void switchToTab(String tabName) {
		try{
			click(getTab(tabName));
			currentTab = tabName;
		}
		catch (Exception e) {
			log.info(tabName + " tab already open");
		}
	}
	
	public boolean checkPagingFormat(){
		boolean format = false;
		format = Pattern.matches("\\(\\d+\\s-\\s\\d+\\sof\\s\\d+\\)", getObjectText(pageNumbers));
		return format;
	}
	
	public void followAllInTab(){
		if(isPresent(followAllButton)){
			click(followAllButton);
		}
	}
	
	public void stopFollowingAllInTab(){
		if(isPresent(stopFollowingAllButton)){
			click(stopFollowingAllButton);
		}
	}
	
	public List<String> getItemsInTab(){
		List<String> items = new ArrayList<String>();
		
		List<Element> elements = exec.getElements(itemsInTab);
		
		for(int x=0; x<elements.size(); x++){
			items.add(elements.get(x).getText());
		}
		
		return items;
	}
	
	public boolean verifyFollowingAll(String module){
		boolean result = false;
		
		if(isPresent(stopFollowingAllButton)){
			scrollElementToMiddleOfBrowser(stopFollowingAllButton);
			List<String> items = getItemsInTab();
			List<Element> elements = null;
			if(module.equalsIgnoreCase("Clients")){
				elements = exec.getElements(clientstopFollowingButtons);
			}
			else if(module.equalsIgnoreCase("Opportunities")){
				elements = exec.getElements(opptystopFollowingButtons);
			}
			else{
				log.error("module not set correctly in test");
				return false;
			}
			
			if(elements.size() == items.size()){
				result = true;
			}
		}
		
		return result;
	}
	
	public boolean verifyFollowingNone(String module){
		boolean result = false;
		
		if(isPresent(followAllButton)){
			List<String> items = getItemsInTab();
			List<Element> elements = null;
			if(module.equalsIgnoreCase("Clients")){
				elements = exec.getElements(clientstartFollowingButtons);
			}
			else if(module.equalsIgnoreCase("Opportunities")){
				elements = exec.getElements(opptystartFollowingButtons);
			}
			else{
				log.error("module not set correctly in test");
				return false;
			}
			
			if(elements.size() == items.size()){
				result = true;
			}
		}
		
		return result;
	}
	
	public boolean isItemBeingFollowed(String item){
		boolean result = false;
		
		while(true){
			if(!isPresent(getStopFollowingForItem(item))){
				sleep(5);
			}

			if(isPresent(getStopFollowingForItem(item))){
				result = true;
				break;
			}
			else if(isPresent(getFollowingForItem(item))){
				result = false;
				break;
			}
			else{
				List<Element> nextPage = exec.getElements(NextFollowingPage);
				if(isPresent(NextFollowingPageDisabled)){
					result=false;
					break;
				}
				else{
					if(currentTab.equals("Opportunities")){
						nextPage.get(1).click();
					}
					else{
						click(NextFollowingPage);
					}
					sleep(3);
				}
			}								
		}
		
		return result;
	}
	
	public boolean isItemNotFollowed(String item){
		boolean result = false;
		
		while(true){
			if(!isPresent(getStopFollowingForItem(item))){
				result = true;
				break;
			}
			if(isPresent(getFollowingForItem(item))){
				result = false;
				break;
			}
			else{
				
				List<Element> nextPage = exec.getElements(NextFollowingPage);
				if(isPresent(NextFollowingPageDisabled)){
					result=false;
					break;
				}
				else{
					if(currentTab.equals("Opportunities")){
						nextPage.get(1).click();
					}
					else{
						click(NextFollowingPage);
					}
					sleep(5);
				}
			}
		}
		
		return result;
	}
	
	public void followItem(String item){
		if(isPresent(getFollowingForItem(item))){
			clickWithScrolling(getFollowingForItem(item));
		}
	}
	
	public void unfollowItem(String item){
		if(isPresent(getStopFollowingForItem(item))){
			clickWithScrolling(getStopFollowingForItem(item));
		}
	}
	/**
	 * Click stop following button without using method-clickWithScrolling
	 * @param item
	 */
	
	public void clickStopFollowing(String item){
		if(isPresent(getStopFollowingForItem(item))){
		click(getStopFollowingForItem(item));
		}
	}	
	/**
	 * Click start following button without using method-clickWithScrolling
	 * @param item
	 */
	public void clickStartFollowing(String item){
		if(isPresent(getFollowingForItem(item))){
		click(getFollowingForItem(item));
		}
	}	
	/**
	 * Search's oppty or client in manage event dialogue box
	 * @param subTabname
	 * @param client or oppty id
	 */
	public void searchForClientOrOppty(String subTabName,String id){
		if(subTabName.contentEquals(GC.OpportunitiesImNotFollowing )){
			 searchTextBox = opptyNotFollowingSearchTextBox;
			 searchButton = opptyNotFollowingSearchButton;
			
		}
		else if(subTabName.contentEquals(GC.OpportunitiesImFollowing )){
			 searchTextBox = opptyFollowingSearchTextBox;
			 searchButton = opptyFollowingSearchButton;
			
		}
		else if(subTabName.contentEquals(GC.ClientsImFollowing  )){
			 searchTextBox = clientsFollowingSearchTextBox;
			 searchButton = clientFollowingSearchButton;
			
		}
		else if(subTabName.contentEquals(GC.ClientsImNotFollowing  )){
			 searchTextBox = clientsFollowingSearchTextBox;
			 searchButton = clientNotFollowingSearchButton;
			
		}
		type(searchTextBox,id);
		sleep(5);
		click(searchButton);
		sleep(5);
	}
	 

}
