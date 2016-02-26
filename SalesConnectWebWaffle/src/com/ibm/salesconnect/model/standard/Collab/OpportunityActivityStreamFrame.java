/**
 * 
 */
package com.ibm.salesconnect.model.standard.Collab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardPageFrame;

/**
 * @author evafarrell
 * @date Jul 30, 2013
 */
public class OpportunityActivityStreamFrame extends StandardPageFrame {
	Logger log = LoggerFactory.getLogger(OpportunityActivityStreamFrame.class);

	/**
	 * @param exec
	 */
	public OpportunityActivityStreamFrame(Executor exec) {
		super(exec);
		
		//waitForSubpanelToLoad(iframeLoaded);
		//sleep(2);
		//enableActivityStream();
		
		//Assert.assertTrue(isPageLoaded(), "Activity Stream Frame has not loaded within 60 seconds");
	}


	@Override
	public boolean isPageLoaded() {
		return waitForSubpanelToLoad(pageLoaded);
	}
	
	String eventTitle = "";
	//selectors
	public static String activtyStreamFrame = "//iframe[@id='__gadget_gadget_OpptyActivityStreamContainer']";
	public static String activityStreamEnable = "//a[contains(text(),'Click here')]";
	public static String iframeLoaded = "//div[@id='lotusFrame']";
	public static String enableActivityStreamButton = "//input[@id='authBtn']"; 
	public static String pageLoaded = "//a[contains(text(),'Feed for these entries')]";
	public static String firstEvent = "//ul[@class='lotusStream']/li[1]/div[1]";
	public static String secondEvent = "//ul[@class='lotusStream']/li[2]/div[1]";
	public static String refresh = "//a[@id='com_ibm_social_as_gadget_refresh_RefreshButton_0']/img";
	private String getEventTitle(String eventNumber) {return getObjectText("//ul[@class='lotusStream']/li[" + eventNumber + "]/div[1]//div[@class='lotusPostContent']/div[1]");};
	//private String getEvent(String eventNumber) {return getObjectText("//ul[@class='lotusStream']/li[" + eventNumber + "]/div[1]");};
	
	public static String embeddedExperienceFrame = "//div[contains(@class,'dijitPopup') and not(contains(@style,'display: none;'))]//iframe[@title='Connections' or @title='SalesConnect EE']";
	
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
		log.info("Refreshing Activity Stream");
		click(refresh);
	}
		
	
	public boolean verifyEntry(String Entry){
		if (isTextPresent(Entry)){
			log.info("Entry is present in Activity Stream");
			return true;
		}
		//Need to wait for up to 5 mins for things to update on SalesConnect
		for (int i = 0; i < 300; i++) {
			log.info("Waiting for Entry to appear in Activity Stream");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (isTextPresent(Entry)){
				log.info("Entry is present in Activity Stream");
				return true;
			}
			refreshEventsStream();
		}
		log.info("Entry is NOT present in Activity Stream");
		return false;
	}
	
	public EmbeddedExperience openFirstEventsEmbeddedExperience(){
		log.info("Opening First Event in Activity Stream");
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
	public EmbeddedExperience openSecondEventsEmbeddedExperience(){
		log.info("Opening Second Event in Activity Stream");
		setEventTitle("2");
		click(secondEvent);
		sleep(10);
		switchToMainWindow();
		switchToFrame(embeddedExperienceFrame);
		return new EmbeddedExperience(exec);
	}
	
	public void openSecondEvent(){
		setEventTitle("2");
		click(secondEvent);
	}
	
	public void setEventTitle(String eventNumber){
		eventTitle = getEventTitle(eventNumber);
	}
	
	public String getEventTitle(){
		return eventTitle;
	}
	
}
