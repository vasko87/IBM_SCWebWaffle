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
 * @date Mar 6, 2014
 */
public class ClientActivityStreamFrame extends StandardPageFrame {
	Logger log = LoggerFactory.getLogger(ClientActivityStreamFrame.class);

	/**
	 * @param exec
	 */
	public ClientActivityStreamFrame(Executor exec) {
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
	public static String enableActivityStreamButton = "//input[@id='authBtn']"; 
	public static String pageLoaded = "//a[contains(text(),'Feed for these entries')]";
	public static String firstEvent = "//ul[@class='lotusStream']/li[1]/div[1]";
	public static String secondEvent = "//ul[@class='lotusStream']/li[2]/div[1]";
	private String getEventTitle(String eventNumber) {return getObjectText("//ul[@class='lotusStream']/li[" + eventNumber + "]/div[1]//div[@class='lotusPostContent']/div[1]");};
	public static String refreshStream = "//a[@id='com_ibm_social_as_gadget_refresh_RefreshButton_0']";
	public static String eventsIFrame = "//iframe[contains(@id,'__gadget_gadget_ClientActivityStream')]";
	public static String embeddedExperienceFrame = "//div[contains(@class,'dijitPopup') and not(contains(@style,'display: none;'))]//iframe[@title='Connections' or @title='SalesConnect EE']";
	

	public void refreshEventsStream(){
		log.info("Refreshing Activity Stream");
		click(refreshStream);
	}	
	
	public boolean verifyEntry(String Entry){
		//if (isTextPresent("created the call " + Entry)){
		if (isTextPresent(Entry)){
			return true;
		}
		//Need to wait for up to 5 mins for things to update on SalesConnect
		for (int i = 0; i < 5; i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//if (isTextPresent("created the call " + Entry)){
			if (isTextPresent(Entry)){	
				return true;
			}
			refreshEventsStream();
		}
		return false;
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
}
