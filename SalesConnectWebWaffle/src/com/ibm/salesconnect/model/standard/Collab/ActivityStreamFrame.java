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
 * @author timlehane
 * @date Jul 4, 2013
 */
public class ActivityStreamFrame extends StandardPageFrame {
	Logger log = LoggerFactory.getLogger(ActivityStreamFrame.class);

	/**
	 * @param exec
	 */
	public ActivityStreamFrame(Executor exec) {
		super(exec);
		
		waitForSubpanelToLoad(iframeLoaded);
		sleep(2);
		enableActivityStream();
		
		Assert.assertTrue(isPageLoaded(), "Activity Stream Frame has not loaded within 60 seconds");
	}


	@Override
	public boolean isPageLoaded() {
		return waitForSubpanelToLoad(pageLoaded);
	}
	
	String eventTitle = "";
	//selectors
	public static String activtyStreamFrame = "//iframe[@title='Updates']";
	public static String activityStreamEnable = "//div[@id='oAuthQuestion']/a[contains(text(),'Click here')]";
	public static String bwcFrame = "//iframe[@id='bwc-frame']";
	
	public static String iframeLoaded = "//div[@id='lotusFrame']";
	public static String enableActivityStreamButton = "//input[@id='authBtn']"; 
	public static String pageLoaded = "//a[contains(text(),'Feed for these entries')]";
	public static String firstEvent = "//ul[@class='lotusStream']/li[1]/div[1]";
	public static String refreshStream = "//a[@id='com_ibm_social_as_gadget_refresh_RefreshButton_0']";
	//private String getEventTitle(String eventNumber) {return getObjectText("//ul[@class='lotusStream']/li[" + eventNumber + "]/div[1]//div[@class='lotusPostContent']/div[1]/a");};
	private String getEventTitle(String eventNumber) {return getObjectText("//ul[@class='lotusStream']/li[" + eventNumber + "]/div[1]//div[@class='lotusPostContent']/div[1]/span[1]/a");};
	private String getEvent(String eventNumber) {return getObjectText("//ul[@class='lotusStream']/li[" + eventNumber + "]/div[1]");};
	
	public static String embeddedExperienceFrame = "//iframe[@title='SalesConnect EE'] | //div[contains(@class,'dijitPopup') and not(contains(@style,'display: none;'))]//iframe[@title='Connections']";
	
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
	
	public EmbeddedExperience openFirstEventsEmbeddedExperience(){
		setEventTitle("1");
		click(firstEvent);
		sleep(15);
		switchToMainWindow();
		switchToFrame(bwcFrame);
		switchToFrame(embeddedExperienceFrame);
		return new EmbeddedExperience(exec);
	}
	
	public void openFirstEvent(){
		setEventTitle("1");
		click(firstEvent);
	}
	
	public void checkEventsVisible(){
		if (checkForElement("//ul[@class='lotusStream']/li[1]/div[1]//div[@class='lotusPostContent']/div[1]/span[1]/a")) {
		}
		else if (checkForElement("//ul[@aria-label='Activity Stream']//div")){
		}
		else {
			Assert.assertTrue(false,"Neither event nor no updates message displayed");
		}
	}
	
	public void setEventTitle(String eventNumber){
		eventTitle = getEventTitle(eventNumber);
	}
	
	public String getEventTitle(){
		return eventTitle;
	}
	
	public void checkEvents(String[] eventList) {
		
		for (int i = 0; i < eventList.length; i++) {
			if (!checkEventInActivityStrem(eventList[i])) {
				
				Assert.assertTrue(false, "Event "+eventList[i]+" has not been found in the activity stream");
			}
		}	
	
}

public Boolean checkEventInActivityStrem(String Event){
	for (int i = 0; i < 300; i++) {
		if (isPresent("//div[contains(@class,'lotusPostAction')]")) {
			System.out.println(getObjectText("//div[contains(@class,'lotusPostAction')]"));
		}
		System.out.println(getObjectText("//div[contains(@class,'lotusPostAction')]"));
		click(refreshStream);
		sleep(1);
	}
	return false;	
}

public boolean verifyEntry(String Entry){
	if (isTextPresent(Entry)){
		return true;
	}
	//Need to wait for up to 5 mins for things to update on SalesConnect
	for (int i = 0; i < 300; i++) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (isTextPresent(Entry)){	
			return true;
		}
	}
	return false;
}
public boolean verifyEntryNotExist(String Entry){
	if (!isTextPresent(Entry)){
		return true;
	}
	//Need to wait for up to 5 mins for things to update on SalesConnect
	for (int i = 0; i < 600; i++) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (!isTextPresent(Entry)){	
			return true;
		}
	}
	return false;
}

}
