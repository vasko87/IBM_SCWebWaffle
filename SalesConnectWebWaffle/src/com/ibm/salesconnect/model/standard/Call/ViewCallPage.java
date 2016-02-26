/**
 * 
 */
package com.ibm.salesconnect.model.standard.Call;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardPageFrame;
import com.ibm.salesconnect.objects.Call;

/**
 * @author timlehane
 * @date May 15, 2013
 */
public class ViewCallPage extends StandardPageFrame {
	Logger log = LoggerFactory.getLogger(ViewCallPage.class);
	
	/**
	 * @param exec
	 */
	public ViewCallPage(Executor exec) {
		super(exec);
		Assert.assertTrue(isPageLoaded(), "View Call page has not loaded within 60 seconds");	
	}

	/* (non-Javadoc)
	 * @see com.ibm.salesconnect.model.StandardPageFrame#isPageLoaded()
	 */
	@Override
	public boolean isPageLoaded() {
		return waitForPageToLoad(pageLoaded);
	}

	//Selectors
	public static String pageLoaded = "//form[@id='search_form']//input[@id='search_form_clear']";
	public static String searchFrame = "//iframe[@id='bwc-frame']";
	
	public static String clearSearchForm = "//form[@id='search_form']//input[@id='search_form_clear']";
	public static String searchField =  "//input[@id='name_basic' or @id='name_advanced']";
	public static String myCalls =  "//input[contains(@id,'current_user_only_')]";
	public static String searchButton = "//*[@id='search_form_submit']";
	public static String getMyFavorites = "//input[@id='favorites_only_basic' or @id='favorites_only_advanced']"; 
	
	public String getCallSelection(String callName) {return "//form[@id='MassUpdate']//a[contains(text(),'" + callName + "')]";}

	
	//Methods
	/**
	 * Searches for a contact based on the parameters
	 * @param contact
	 */
	public void searchForCall(Call call){
		
		click(clearSearchForm);
		type(searchField,call.sSubject);
		
		if(call.bMyCalls){
			if(!isChecked(myCalls)){
				click(myCalls);
			}
		}
		else {
			if(isChecked(myCalls)){
				click(myCalls);
				
				if(call.bMyFavorites){ //Call is a favorite
					if(!isChecked(getMyFavorites)){
						click(getMyFavorites);
					}
				}
				else { //Call is not a favorite
					if(isChecked(getMyFavorites)){
						click(getMyFavorites);
					}
			}
	
		click(searchButton);
	
		}
		}
	}
	
	/**
	 * Searches for a contact based on the parameters
	 * @param contact
	 */
	public void searchForCall(String callSubject, boolean MyCalls, boolean MyFavorites){
		
		click(clearSearchForm);
		type(searchField,callSubject);
		
		if(MyCalls){
			if(!isChecked(myCalls)){
				click(myCalls);
			}
		}
		else {
			if(isChecked(myCalls)){
				click(myCalls);
				
				if(MyFavorites){ //Call is a favorite
					if(!isChecked(getMyFavorites)){
						click(getMyFavorites);
					}
				}
				else { //Call is not a favorite
					if(isChecked(getMyFavorites)){
						click(getMyFavorites);
					}
			}
	
		click(searchButton);
	
		}
		}
	}
	
	/**
	 * Click on the correct search result
	 * @param contact
	 * @return new contact detail page object
	 */
	public CallDetailPage selectResult(Call call) {
		click(clearSearchForm);
		sleep(5);
		click(getCallSelection(call.sSubject));
		sleep(15);
		return new CallDetailPage(exec);
	}
	
	/**
	 * Click on the correct search result
	 * @param contact
	 * @return new contact detail page object
	 */
	public CallDetailPage selectResult(String callSubject) {
		click(clearSearchForm);
		sleep(5);
		click(getCallSelection(callSubject));
		sleep(15);
		return new CallDetailPage(exec);
	}
}


