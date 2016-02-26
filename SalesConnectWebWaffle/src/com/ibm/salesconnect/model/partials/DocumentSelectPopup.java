/**
 * 
 */
package com.ibm.salesconnect.model.partials;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardPageFrame;
import com.ibm.salesconnect.objects.Document;

/**
 * @author Administrator
 *
 */
public class DocumentSelectPopup extends StandardPageFrame {
	Logger log = LoggerFactory.getLogger(DocumentSelectPopup.class);

	/**
	 * @param exec
	 */
	public DocumentSelectPopup(Executor exec) {
		super(exec);
		Assert.assertTrue(isPageLoaded(), "Client Select Popup has not loaded within 60 seconds");	
	}

	/* (non-Javadoc)
	 * @see com.ibm.salesconnect.model.StandardPageFrame#isPageLoaded()
	 */
	@Override
	public boolean isPageLoaded() {
		return waitForElement(pageLoaded);
	}
	
	//Selectors
	public static String pageLoaded = "//tr[@class='S1']";
	public static String searchInputField = "//input[@id='dcSearch']";
	public static String searchButton = "//input[@class='dcSubmit']";
	
	public String getDocumentSelection(String externalFileName){ return "//a[contains(text(),'" + externalFileName + "')]";}
	
	//public String getClientSelection(String clientID, String clientName){ return "//table[contains(@class,'list')]//span[contains(text(),'" 
		//+ clientID + "') and @class = 'LEVEL_DC_ID_PLAIN']/../..//a[contains(text(),'" + clientName + "')]";}
	
	//Tasks
	/**
	 * Searches for a client based on the parameters
	 * @param client
	 */
	public void searchForDocument(Document doc){

		type(searchInputField,doc.sExternalFileName);
		click(searchButton);
		
		waitForPageToLoad(getDocumentSelection(doc.sExternalFileName));

		
	}
	
	/**
	 * Selects the specified client from the results list
	 * @param client
	 */
	public void selectResult(Document doc) {
		if(doc.sExternalFileName.length()>0){
			click(getDocumentSelection(doc.sExternalFileName));
		}
		else{
			Assert.assertTrue(false, "Neither client Name nor ClientID present in Client");
		}
		switchToMainWindow();
	}
}
