/**
 * 
 */
package com.ibm.salesconnect.model.standard.Opportunity;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Element;
import com.ibm.atmn.waffle.core.Executor;
import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.StandardViewPage;
import com.ibm.salesconnect.model.partials.ConnectionsBusinessCard;
import com.ibm.salesconnect.objects.Opportunity;

/**
 * @author timlehane
 * @date May 13, 2013
 */
public class ViewOpportunityPage extends StandardViewPage{
	Logger log = LoggerFactory.getLogger(ViewOpportunityPage.class);

	/**
	 * @param exec
	 */
	public ViewOpportunityPage(Executor exec) {
		super(exec);
		sleep(10);
		Assert.assertTrue(waitForElement(super.searchBarTextBox), "View Opportunity page has not loaded within 60 seconds");	
	}

	//Selectors
	//public static String pageLoaded = "//input[@id='search_form_submit']";
	//public static String searchFrame = "//iframe[@id='bwc-frame']";
	
	public static String getSearchFormType = "//form[@id='search_form']//input[@name='searchFormTab']";
	public String getSearchField(String searchType){ return  "//input[@id='name_" + searchType + "']";}
	public String getMyOpportunities(String searchType){ if(searchType.equals("basic")) return  "//input[@id='current_user_only_open_" + searchType + "']";
	else return  "//input[@id='current_user_only_" + searchType + "']";}
	public String getMyFavoriteOpptys(String searchType){ return "//input[@id='favorites_only_" + searchType +"']";}
	public String getSearchDecisionDate(String searchType){ return  "//select[@id='date_closed_" + searchType + "_range_choice']";}
	
	public static String clearSearchForm = "//input[@id='search_form_clear']";
	public static String searchButton = "//*[@id='search_form_submit']";
	public static String getAllFavoriteButtons = "//div[@class='star']//div[@class='off']";
	public static String getAllUnfavoriteButtons = "//div[@class='star']//div[@class='on']";
	public static String getAllOpptyNames = "//td[@scope='row']//a";
	
	public String getOpportunitySelection(String opptyNumber) {return "//a[contains(text(),'" + opptyNumber + "')]";}
	public String getBusinessCard(String opptyNumber, String userName) {if (opptyNumber.length() > 0) return 
		"//span[@id='" + opptyNumber + "-AssignedTo']//a";
	else return "//form[@id='MassUpdate']//a[contains(text(),'" + userName + "')]";};
	
	public String getMyFavorites(String searchType) { return "//input[@id='favorites_only_" + searchType + "']";}
	public String getdateCreatedDropdown(String searchType) { return "//select[@id = 'date_entered_" + searchType + "_range_choice']";}
	public String getCloseDateDropdown(String searchType) {return "//select[@id = 'date_closed_" + searchType + "_range_choice']";}
	
	//Methods
	/**
	 * Gets the current search form status 
	 * returns basic/advanced
	 * @deprecated
	 */
	public String getSearchtype(){
		String value = getObjectAttribute(getSearchFormType, "value");
		return value.substring(0,value.length()-7); 
		
	}
	/**
	 * Searches for a opportunity based on the id
	 * @param opportunity ID
	 */
	public void searchForOpportunityID(String opptyID){
		searchWithFilter("My Open Opportunities",opptyID);
	}
	/**
	 * Searches for a opportunity based on the parameters
	 * @param opportunity
	 */
	public void searchForOpportunity(Opportunity oppt){
		/*
		String searchType=getSearchtype();
		scrollElementToMiddleOfBrowser(clearSearchForm);
		click(clearSearchForm);
		log.info(searchType);
		
		//Change decision date dropdown
		//?
		
		if(oppt.sOpptNumber.length()>0){
			type(getSearchField(searchType),oppt.sOpptNumber);
		}
		else {
			type(getSearchField(searchType),oppt.sOpptName);
		}
		if(oppt.bMyOpportunities){
			if(!isChecked(getMyOpportunities(searchType))){
				click(getMyOpportunities(searchType));
			}
		}
		else {
			if(isChecked(getMyOpportunities(searchType))){
				click(getMyOpportunities(searchType));
			}
		}
		if(oppt.bFavOpportunities){
			if(!isChecked(getMyFavoriteOpptys(searchType))){
				click(getMyFavoriteOpptys(searchType));
			}
		}
		else {
			if(isChecked(getMyFavoriteOpptys(searchType))){
				click(getMyFavoriteOpptys(searchType));
			}
		}
		if(oppt.sSearchDecisionDate.equals(null) || oppt.sSearchDecisionDate.equals("")){
			oppt.sSearchDecisionDate = GC.gsDecisionDateAll;
		}
		
		if(oppt.bMyFavorites){ 
			if(!isChecked(getMyFavorites(searchType))){
				click(getMyFavorites(searchType));//Convert Opportunity to favorite
			}
		}
		else { 
			if(isChecked(getMyFavorites(searchType))){
				click(getMyFavorites(searchType)); 
			}
		select(getSearchDecisionDate(searchType),oppt.sSearchDecisionDate);
		}
		if(oppt.sCloseDate.length() > 0){
				select(getCloseDateDropdown(searchType), oppt.sCloseDate);
		}
		click(searchButton);
		*/
		searchNoFilter(oppt.sOpptNumber);
	}
	
	/**
	 * Click on the correct search result
	 * @param oppt
	 * @return new opportunity detail page object
	 */
	public OpportunityDetailPage selectResult(Opportunity oppt) {
		//click(clearSearchForm);
		//waitForPageToLoad(pageLoaded);
		//searchNoFilter(oppt.sOpptDesc);
		click(getTableLocation(1, getColumnIndex("Opportunity Number")) + "//a");
		return new OpportunityDetailPage(exec);
	}
	
	public OpportunityDetailPage selectResultfromID(Opportunity oppt){
		searchNoFilter(oppt.sOpptNumber);
		click(getTableLocation(1, getColumnIndex("Opportunity Number")) + "//a");
		return new OpportunityDetailPage(exec);
	}
	
	public OpportunityDetailPage selectResultfromID(String opptyID){
		click(getTableLocation(1, getColumnIndex("Opportunity Number")) + "//a");
		return new OpportunityDetailPage(exec);
	}
	
	public OpportunityDetailPage selectResultWithFilter(Opportunity oppt){
		click(getTableLocation(1, getColumnIndex("Opportunity Number")) + "//a");
		return new OpportunityDetailPage(exec);
	}
	
	public boolean checkResult(Opportunity oppt) {
		click(clearSearchForm);
		return isPresent(getOpportunitySelection(oppt.sOpptNumber));
	}
	
	public void verifyBusinessCard(String opptyNumber, User user){
		ConnectionsBusinessCard connectionsBusinessCard = openConnectionsBusinessCard(getBusinessCard(opptyNumber,user.getFirstName()), exec);
		connectionsBusinessCard.verifyBusinessCardContents(user);
	}
	
	public List<String> addAllVisibleOpptysToFavorites(){
		List<String> opptys = new ArrayList<String>();
		
		List<Element> favoriteButtons = exec.getElements(getAllFavoriteButtons);
		List<Element> opptyElements = exec.getElements(getAllOpptyNames);
		exec.executeScript("window.scrollBy(0,arguments[0])", 300);
		for(int x=0; x<favoriteButtons.size(); x++){
			
			favoriteButtons.get(x).click();
			opptys.add(opptyElements.get(x).getText());
		}
		
		return opptys;
	}
	
	public List<String> getAllVisibleOpptys(){
		List<String> opptys = new ArrayList<String>();
		
		exec.executeScript("window.scrollBy(0,arguments[0])", 300);
		List<Element> opptyElements = exec.getElements(getAllOpptyNames);
		
		for(int x=0; x<opptyElements.size(); x++){
			opptys.add(opptyElements.get(x).getText());
		}
		
		return opptys;
	}
	
	public List<String> removeAllVisibleOpttysFromFavorites(){
		List<String> opptys = new ArrayList<String>();
		
		List<Element> favoriteButtons = exec.getElements(getAllUnfavoriteButtons);
		List<Element> opptyElements = exec.getElements(getAllOpptyNames);
		exec.executeScript("window.scrollBy(0,arguments[0])", 300);
		for(int x=0; x<opptyElements.size(); x++){
			
			favoriteButtons.get(x).click();
			opptys.add(opptyElements.get(x).getText());
		}
		
		return opptys;
	}
	
	public boolean communityTabExists(){
		return isTextPresent(GC.gsConnectionsCommunityTab);
		
	}
}
