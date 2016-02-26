/**
 * 
 */
package com.ibm.salesconnect.model.standard.Collab;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardPageFrame;

/**
 * @author timlehane
 * @date Jul 4, 2013
 */
public class EmbeddedExperience extends StandardPageFrame {
	Logger log = LoggerFactory.getLogger(EmbeddedExperience.class);

	/**
	 * @param exec
	 */
	public EmbeddedExperience(Executor exec) {
		super(exec);
		waitForSubpanelToLoad(iframeLoaded);
		//Assert.assertTrue(isPageLoaded(), "Embedded Experience has not loaded within 60 seconds");	
	}


	@Override
	public boolean isPageLoaded() {
		return waitForSubpanelToLoad(pageLoaded);
	}
	
	//Selectors
	public static String activtyStreamFrame = "//iframe[@title='Updates']";
	public static String statusEEFrame = "//iframe[@id='__gadget_gadget_ClientActivityStream-eeDiv-0']";
	public static String pageLoaded = "//a[@id='historyLink'] | //span[@class='idxTooltipDialogCloseIcon']";
	public static String iframeLoaded = "//div[@id='gadgetDiv' or @id='ee_container']";
	public String getEmbeddedExperienceTitle(){return getObjectText("//h2 | //h1");};
	public String getEmbeddedExperienceSubject(){return getObjectText("//div[@id='sc_SCEmbeddedExperience_SCFieldView_0']/div[2]");};
	public String getEmbeddedExperienceStatus(){return getObjectText("//div[contains(@id,'su_ee_')]");};
	public String getEmbeddedExperienceRLIAmount(){return getObjectText("//div[@id='sc_SCEmbeddedExperience_SCFieldView_2']/div[2]");};
	public String getEmbeddedExperienceValue(String label){return getObjectText("//div[contains(text(),'"+label+"')]/../div[2]");};
	public static String underLayPresent = "//div[@id='scdijit_dijit_DialogUnderlay_0' and contains(@style,'block')]";
	public static String underLayNotPresent = "//div[@id='scdijit_dijit_DialogUnderlay_0']";
	public static String embeddedExperienceFrame = "//div[contains(@class,'dijitPopup') and not(contains(@style,'display: none;'))]//iframe[@title='SalesConnect EE'] | //div[contains(@class,'dijitPopup') and not(contains(@style,'display: none;'))]//iframe[@title='Connections']";
	public static String eeFrame = "//div[@id='ee_container']";
	public static String closeButton = "//div[contains(@class,'dijitPopup') and not(contains(@style,'display: none;'))]//span[@aria-label='Close']";
	public static String bwcFrame = "//iframe[@id='bwc-frame']";
	
	/**
	 * 
	 */
	public Boolean verifyOpened() {
		if(isPresent(eeFrame)){
			return true;
		}
		else{
			return false;
		}
	}

	public void switchToBWCFrame(){
		switchToFrame(bwcFrame);
	}

	/**
	 * Check if text is present anywhere in embedded experience
	 * @param text
	 * @return true if present, false if not
	 */
	public Boolean checkTextPresent(String text){
		return this.isTextPresent(text);
	}
	
	/**
	 * @param b
	 * @return
	 */
	public Boolean verifyUnderlay(Boolean b) {
		switchToMainWindow();
		switchToBWCFrame();
		if(b){
			if(checkForElement(underLayPresent)){
				return true;
			}
			else{
				return false;
			}
		}
		else{
			if(!checkForElement(underLayNotPresent)){
				return true;
			}
			else{
				return false;
			}
		}
	}

	public void getIframe(final WebDriver driver) {
	    final List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
	    for (WebElement iframe : iframes) {
	        System.out.println(iframe.getAttribute("id"));
	    }
	}

	/**
	 * 
	 */
	public void close() {
		click(closeButton);
		if(isPresent(closeButton)){
			click(closeButton);
		}
	}

	public String verifyStatusEE(){
		switchToFrame(statusEEFrame);
		return getEmbeddedExperienceStatus();
		
	}
}
