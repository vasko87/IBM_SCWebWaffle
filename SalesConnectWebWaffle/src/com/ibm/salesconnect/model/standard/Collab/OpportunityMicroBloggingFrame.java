/**
 * 
 */
package com.ibm.salesconnect.model.standard.Collab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardPageFrame;

/**
 * @author evafarrell
 * @date Aug 16, 2013
 */
public class OpportunityMicroBloggingFrame extends StandardPageFrame {
	Logger log = LoggerFactory.getLogger(OpportunityMicroBloggingFrame.class);

	/**
	 * @param exec
	 */
	public OpportunityMicroBloggingFrame(Executor exec) {
		super(exec);
		
		//waitForSubpanelToLoad(iframeLoaded);
		//sleep(2);
		//enableActivityStream();
		
		Assert.assertTrue(isPageLoaded(), "Activity Stream Frame has not loaded within 60 seconds");
	}


	@Override
	public boolean isPageLoaded() {
		return waitForSubpanelToLoad(pageLoaded);
	}
	
	String eventTitle = "";
	//selectors
	public static String activtyStreamFrame = "//iframe[@title='Updates']";
	public static String activityStreamEnable = "//a[contains(text(),'Click here')]";
	public static String iframeLoaded = "//div[@id='lotusFrame']";
	public static String enableActivityStreamButton = "//input[@id='authBtn']"; 
	public static String pageLoaded = "//a[contains(text(),'Feed for these entries')]";
	public static String firstEvent = "//ul[@class='lotusStream']/li[1]/div[1]";
	public static String refresh = "//a[@id='com_ibm_social_as_gadget_refresh_RefreshButton_0']";
	public String status(String status) { return "//div[contains(text(),'"+ status +"')]";};
	
	private String getEventTitle(String eventNumber) {return getObjectText("//ul[@class='lotusStream']/li[" + eventNumber + "]/div[1]//div[@class='lotusPostContent']/div[1]");};
	//private String getEvent(String eventNumber) {return getObjectText("//ul[@class='lotusStream']/li[" + eventNumber + "]/div[1]");};
	
	public static String embeddedExperienceFrame = "//div[contains(@class,'dijitPopup') and not(contains(@style,'display: none;'))]//iframe[@title='Connections']";
	
	public static String enableActivityStreamURLFragment = "_oauth_client_auto_authorize=true";
	
	public void enableActivityStream(){
		if(isPresent(activityStreamEnable)){
			click(activityStreamEnable);
			switchToWindowUrlContains(enableActivityStreamURLFragment);
			if(isPresent(enableActivityStreamButton)){
				click(enableActivityStreamButton);
			}
			switchToMainWindow();
			switchToFrame(activtyStreamFrame);
		}
	}
	
	public void refreshEventsStream(){
		click(refresh);
	}
	
	public boolean verifyStatus(String Status){
		sleep(5);
		return isPresent(status(Status));
	}
	
	
	public void expandEntry(String Entry){
		
	}
	
	public EmbeddedExperience openFirstEventsEmbeddedExperience(){
		setEventTitle("1");
		click(firstEvent);
		sleep(10);
		switchToMainWindow();
		switchToFrame(embeddedExperienceFrame);
		return new EmbeddedExperience(exec);
	}
	
	public void openFirstEvent(){
		setEventTitle("1");
		click(firstEvent);
	}
	
	public void setEventTitle(String eventNumber){
		eventTitle = getEventTitle(eventNumber);
	}
	
	public String getEventTitle(){
		return eventTitle;
	}
	
}
