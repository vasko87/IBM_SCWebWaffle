/**
 * 
 */
package com.ibm.salesconnect.model.standard.Contact;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.StandardPageFrame;
import com.ibm.salesconnect.model.partials.ActivitiesHistorySubpanel;
import com.ibm.salesconnect.model.partials.ActivitiesSubpanel;
import com.ibm.salesconnect.model.partials.OpportunitySubpanel;
import com.ibm.salesconnect.model.partials.SetAssessmentSubpanel;
import com.ibm.salesconnect.model.standard.Call.CreateCallPage;

/**
 * @author Administrator
 *
 */
public class ContactDetailPage extends StandardPageFrame {
	
	public static String editContact = "//a[@id='edit_button']";
	public static String expandEditMenu = "//ul[@id='detail_header_action_menu']//span";
	public static String copyContact = "//a[@id='duplicate_button']";
	public static String activitiesLink = "//span[@id='show_link_activities']/a";
	public static String activitiesHistoryLink = "//span[@id='show_link_history']/a";
	public static String setAssessment = "//span[@id='show_link_ibm_assessment_contacts']";
	public static String createOpportunitesLink = "//span[@id='show_link_opportunities']";
	
	
	Logger log = LoggerFactory.getLogger(ContactDetailPage.class);

	/**
	 * @param exec
	 */
	public ContactDetailPage(Executor exec) {
		super(exec);
		Assert.assertTrue(isPageLoaded(), "Contact Detail page has not loaded within 60 seconds");	
	}

	@Override
	public boolean isPageLoaded() {
		if(checkForElement(pageLoaded)){
			return waitForPageToLoad(pageLoaded);
		}
		else {
			return waitForPageToLoad(pageLoaded);
		}
	}

	
	//Selectors
	public static String displayedContactName = "//span[@id='full_name_span']";
	public static String pageLoaded = "//span[@id='full_name_span']";
	public static String detailsFrame = "//iframe[@id='bwc-frame']";
	public static String MyFavouritesIcon = "//*[@id='content']/div[1]/h2/div/div";
	public static String editButton = "//a[@id='edit_button']";
	public static String editDropDown = "//ul[@id='detail_header_action_menu']//span";
	public static String deleteOption = "//a[@id = 'delete_button_old']";
	
	//Methods
	/**
	 * Adds Contact to My Favorites
	 */
	public void addContactToMyFavorites(){
		click (MyFavouritesIcon);// clicking on myFavourites Icon
		log.info("Contact Added to My Favorites");
	}
	
	public String getContactID(){
		String url = getCurrentURL();
		log.info("Current URL: " + url);
	      String pattern = "(.*record=)(.*)[^&](.*)";
	      String endOfString= "";
	      String contactID = "";
	      // Create a Pattern object
	      Pattern r = Pattern.compile(pattern);

	      // Now create matcher object.
	      Matcher m = r.matcher(url);
	      if (m.find( )) {
	    	  endOfString = m.group(2);
	      }
	      String pattern2 ="[^&]*";
	      
	      Pattern r1 = Pattern.compile(pattern2);
	      Matcher m1 = r1.matcher(endOfString);
	      if (m1.find( )) {
	    	  contactID = m1.group(0);
	    	  log.info("ContactID found: " + contactID);
	      }
	      return contactID;
	}
	
	
	/**
	 * Returns the displayed contact name
	 * @return displayed contact name
	 */
	public String getdisplayedContactName(){
		return getObjectText(displayedContactName);
	}
	
	public EditContactPage editContact(){
		click(editContact);
		return new EditContactPage(exec);
	}
	
	public CreateContactPage copyContact(){
		click(expandEditMenu);
		click(copyContact);
		return new CreateContactPage(exec);
	}
	
	public ActivitiesSubpanel openCreateActivitiesSubPanel(){
		click(activitiesLink);
		return new ActivitiesSubpanel(exec);
	}
	
	/**
	 * @return ActivitiesHistorySubpanelobject
	 */
	public ActivitiesHistorySubpanel openActivitiesHistorySubpanel() {
		if(getObjectAttribute(activitiesHistoryLink, "style").contains(GC.notDisplayed)){
			click(activitiesHistoryLink);
		}
		return new ActivitiesHistorySubpanel(exec);
	}
	
	/**
	 * @return SetAssessmentSubpanel
	 */
	public SetAssessmentSubpanel openSetAssessmentSubpanel() {
		if(!getObjectAttribute(setAssessment, "style").contains(GC.notDisplayed)){
			click(setAssessment);
		}
		return new SetAssessmentSubpanel(exec);
	}
	
	/**
	 * @return OpportunitySubpanel
	 */
	public OpportunitySubpanel openOpportunitySubpanel() {
		if(!getObjectAttribute(createOpportunitesLink, "style").contains(GC.notDisplayed)){
			click(createOpportunitesLink);
		}
		return new OpportunitySubpanel(exec);
	}
	
	/**
	 * Opens the edit contact page
	 */
	public CreateContactPage openEditPage(){
		click(editButton);
		return new CreateContactPage(exec);
	}
	
	public void deleteContact(){
		click(editDropDown);
		click(deleteOption);
		acceptAlert();
	}
}
