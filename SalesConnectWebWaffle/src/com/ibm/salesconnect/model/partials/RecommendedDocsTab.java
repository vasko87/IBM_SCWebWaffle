/**
 * 
 */
package com.ibm.salesconnect.model.partials;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FilenameUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Element;
import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardPageFrame;

/**
 * @author kvnlau
 * @date Nov 29, 2013
 */
public class RecommendedDocsTab extends StandardPageFrame {
	Logger log = LoggerFactory.getLogger(WinPlanTab.class);
	
	/**
	 * @param exec
	 */
	public RecommendedDocsTab(Executor exec) {
		super(exec);
		Assert.assertTrue(isPageLoaded(), "Recommended documents Tab has not loaded within 60 seconds");	
	}


	/* (non-Javadoc)
	 * @see com.ibm.salesconnect.model.StandardPageFrame#isPageLoaded()
	 */
	@Override
	public boolean isPageLoaded() {
		
		return waitForElement(recommendedDocsLoader);
	}

	public boolean isDocResultsLoaded() {
		return waitForElement(recDocSearchResults1stColumnLabel);
	}
	
	// Strings
	public static String docTypeAll = "All document types";
	public static String docTypeSalesKitLabel = "Sales kit";
	public static String docTypeSalesPlayLabel = "Sales play";
	public static String docTypeCompSalesSupportLabel = "Competitive Sales Support";
	public static String docTypeMarketAnalysisLabel = "IBM Market Analysis";
	public static String docTypeFilesLabel = "Files";
	
	
	
	//Selectors on Recommended docs tab
	public static String updatesTab = "//em[contains(text(),'Updates')]";
	public static String recDocSearchButton = "//input[@id='df_searchButton']";
	//public static String recommendedDocsLoader = recDocSearchButton;
	public static String recommendedDocsLoader="//input[@id='df_searchButton']";
	public static String recDocsSearchFieldTitle = "//label[@class='df_searchFieldLabel']";
	public static String recDocSearchTextField = "//input[@id='df_searchTerm']";
	public static String recDocSearchResults1stColumnLabel = "//th[@id='firstColumn']";
	public static String clearAllLink = "//a[@id='df_filterClearAllLinks']";
	public static String docTypeFilter = "//select[@id='df_filter_document_typeSelection']";
	
	public static String docTypeFilterSalesKit = "//option[@value='SLK']";
	public static String docTypeFilterSalesPlay = "//option[@value='SLT']";
	public static String docTypeFilterCompSalesSupport = "TODO";
	public static String docTypeFilterIbmMarketAnalysis = "TODO";
	
	public static String recDocResultsPaginationLabel = "//div[@class='pagination']/span[@class='pageNumbers']";
	public static String docTypeColumnRow1 = "//td[@id='documentRecommender_maincontentTD']/table/tbody/tr[2]/td[2]";

	// Selectors in Doc contents page
	public static String docContentsBackToSearchResults = "//div[@id='df_backToSearchResultsArea']/a[@class='df_docOwnerLink']";
	public static String docContentsOpenInNewWindow = "//div[@id='df_backToSearchResultsArea']/a[2]";
	public static String docContentsW3PageFrame = "//div[@id='ibm_sc_documentRecommenderApp']/div[@id='df_docPreviewOutterDiv']/div[@id='df_docPreviewArea']/iframe";
	
	public String getRecDocSearchField(){
		return getObjectText(recDocSearchTextField);
	}

	/**
	 * Run search of documents related to the specified search text
	 * @param SearchText String containing search text input
	 */
	public void findDocRelatedTo(String SearchText){
		if (waitForElement(recDocSearchTextField)) {
			sendKeys(recDocSearchTextField,SearchText);
		}
		if (waitForElement(recDocSearchButton)) {
			scrollElementToMiddleOfBrowser(recDocSearchButton);
		}
		click(recDocSearchButton);
	}
	
	/**
	 * Verify the the Oppty->Recommended documents tab, loads correctly
	 * @return true/false on whether page loads successfully
	 */
	public boolean verifyRecommendedDocTab(){
		if (isPresent(recDocSearchButton) && 
				isPresent(recDocsSearchFieldTitle) && 
				isPresent(recDocSearchTextField) && 
				isPresent(clearAllLink) && isPresent(docTypeFilter) ){
			return true;
		}return false;
	}
	
	/**
	 * Verify the top rankings of related doc results are of document type Sales kit
	 * @return true/false on whether top ranking contains Sales kit document type
	 */
	public boolean verifyResultsTopRankingHasTypeSalesKit(){
		
		// Get Doc filter Sales Kit counter (using the label of the filter we want)
		int iFilterCounterSalesKit = getDocFilterCounter(docTypeSalesKitLabel);
		
		// If SalesKit filter counter indicates 1 or more documents
		if (iFilterCounterSalesKit > 0) { 
			// then check top rankings in Document Type column, have 
			// doc type of 'Sales kit', by checking top 1st row (of Doc Type column)
			if (!docTypeSalesKitLabel.equals( this.getObjectText(docTypeColumnRow1) ) ) {
				return false;
			}
			
			// NOTE: no need to check further rows as there may not be more Sales kit docs 
			// appearing in the 2nd or 3rd row which could incorrectly fail this test.
			// Luciano has confirmed that the 1st row is sufficient for verification 
			// of the top ranking applied to matching Sales Kit docs with a L20 offering.
		}
		
		// clear all docType filters (via Clear all) link
		click(clearAllLink);
		
		// Doc type Sales Kit is found at top of Recommended document results
		return true;
	}

	/**
	 * Utility to extract numerical counter embedded in document type filter option text
	 * The counter is the string contained between two substrings provided 
	 * e.g. "(1 - 15 of 62)", point start to string index 5 (iStartBracket) & end to index 7 (iEndBracket)
	 * @param sText contains the complete text from a specific specific filter option
	 * @param sStartSearchAfter contains substring where to start extraction
	 * @param sEndSearchBefore contains substring where to end extraction
	 * @return iCounter contains the numerical value of the counter extracted
	 */
	public int extractCounterFromText(String sText, String sStartSearchAfter, 
			                          String sEndSearchBefore){
		int iStartBracket = sText.indexOf(sStartSearchAfter) + (sStartSearchAfter.length());
		int iEndBracket = sText.indexOf(sEndSearchBefore);
		int iCounter = Integer.parseInt(sText.substring(iStartBracket, iEndBracket)); 
		return iCounter;
	}
	
	/**
	 * Gets text label of a specific filter option if it exists in Doc Type drop-down menu
	 * @param filterOption text of filter option
	 * @return string containing text of filter option (if found) & null (if not found)
	 */
	public String getDocTypeFilterIfExists(String filterOption){
		String sCurrentFilterText = null;
		
		List<Element> optionList = new ArrayList<Element>();
		optionList = exec.getFirstElement(docTypeFilter).useAsDropdown().getOptions();
		
		//String sTemp = optionList.get(0).getSingleElement(docTypeFilterSalesKit);
		
		// loop through all options in drop down menu & find search string
		for (int current=0; current<optionList.size(); current++) {
			String filterOptionText = optionList.get(current).getText();
			if (filterOptionText.contains(filterOption)){
				// filter exists, so get full text (includes counter in text)
				sCurrentFilterText = optionList.get(current).getText();
				
				// filter exists, select this menu option
				return sCurrentFilterText;
			}
		}
		
		return sCurrentFilterText;
	}
	
	/**
	 * Verify that a specific filter exists
	 * @param filterOptionLabel text of filter option
	 */
	public boolean verifyDocFilterExists(String filterOptionLabel) {
		if (getDocTypeFilterIfExists(filterOptionLabel)!=null) {
			return true;
		}
		return false;
	}
	
	/**
	 * Utility to extract counter from any filter option in Document Type drop-down menu 
	 * @param docTypeFilter specifies which document type filter to extract the counter from
	 * @return iCounter the counter contained in the document type filter option
	 */
	public int getDocFilterCounter(String docTypeFilter){
		String sCurrentFilterOptionLabel = null;
		String sFilterTypeLocator = null;
		int iCounter = 0;
		
		// clear all docType filters (via Clear all) link
		isPresent(clearAllLink);
		scrollElementToMiddleOfBrowser(clearAllLink);
		click(clearAllLink);
		

		// choose the desired selector of the filter we want
		if (docTypeFilter.equals(docTypeSalesKitLabel)) {
			sFilterTypeLocator = docTypeFilterSalesKit;
			// if 'Sales kit' option exists in Doc type menu, get option label text
			sCurrentFilterOptionLabel = getDocTypeFilterIfExists(docTypeSalesKitLabel);
		} else if (docTypeFilter.equals(docTypeSalesPlayLabel)) {
			sFilterTypeLocator = docTypeFilterSalesPlay;
			// if 'Sales play' option exists in doc type menu, get option label text
			sCurrentFilterOptionLabel = getDocTypeFilterIfExists(docTypeSalesPlayLabel);
		} 
		
		// If SalesKit filter option was found, get counter from option's label
		// e.g. "Sales kit(112)"
		if (sFilterTypeLocator != null && sCurrentFilterOptionLabel != null) {			
			iCounter = extractCounterFromText(sCurrentFilterOptionLabel, "(", ")");
		}
		
		// Cleanup: clear all docType filters (via Clear all) link
		click(clearAllLink);
		
		return iCounter;
	}
	
	/**
	 * Set drop-down doc type filter to specific option 
	 * @param docTypeFilterOption specifies which document type filter to select
	 */
	public void selectDocTypeFilterOption(String docTypefilterOption){
		String actualFilterLabel = null;
		
		// retrieve the actual text value inside the filter option we want
		actualFilterLabel = getDocTypeFilterIfExists(docTypefilterOption);
		
		// select this filter option (via the text value of the filter option)
		exec.getFirstElement(docTypeFilter).useAsDropdown().selectOptionByVisibleText(actualFilterLabel);
	}
	
	/**
	 * Verify all Doc Type of Recommended Docs in results list is a specific type
	 * @param expectedDocType the doc type for all docs in the results list
	 */
	public boolean verifyDocTypeOfResults(String expectedDocType){
		int initialSelectorValue = 1; // to initialize pointer to row header
		int numberOfDocsOnPage = 0;
		String tempSelector = null;
		
		// Get pagination label for docs result list
		String paginationLabel = getPaginationLabel();
		
		// extract total count of entries from pagination label
		numberOfDocsOnPage = extractCounterFromText(paginationLabel, "(1 - ", " of ");
		
		// 1st row's doc type selector
		tempSelector = "//td[@id='documentRecommender_maincontentTD']/table/tbody/tr[" + 
									Integer.toString(initialSelectorValue) + "]/td[2]";
		
		// loop to verify all entries found in doc results have same expected doc type 
		for (int counter=1; counter < numberOfDocsOnPage; counter++) {
			// form selector
			tempSelector = "//td[@id='documentRecommender_maincontentTD']/table/tbody/tr[" + 
            					Integer.toString(initialSelectorValue + counter) + "]/td[2]";
			
			// check current row's value in 'Doc Type' column
			if (!getObjectText(tempSelector).equals(expectedDocType)) {
				// doc type for current row entry is not the expected type
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Gets the 1st link(selector) of File Name in Recommended doc results that
	 * contains a document content page. These of docs that exclude Doc type="Files".
	 * @param paginationLabel string with pagination label on recommended doc results 
	 * @return XPath of 1st doc with desired condition. Null if not found.
	 */
	public String getFirstFileNameWithContentsPage(String paginationLabel){
		int initialSelectorValue = 1; // to initialize pointer to row header
		
		// extract total count of entries from pagination label
		int numberOfDocsOnPage = extractCounterFromText(paginationLabel, "(1 - ", " of ");
		log.info("Number of Docs on page: " + numberOfDocsOnPage);
//		String rowDocTypeSelector = null;
//		String rowFileNameSelector = null;
		
		// 1st row's doc type selector
//		rowDocTypeSelector = "//td[@id='documentRecommender_maincontentTD']/table/tbody/tr[" + 
//										Integer.toString(initialSelectorValue) + "]/td[2]";

		// 1st row's file name selector
//		rowFileNameSelector = "//td[@id='documentRecommender_maincontentTD']/table/tbody/tr[" + 
//										Integer.toString(initialSelectorValue) + "]/td[1]";
		
		// loop to find 1st entry in doc results that has no file extension in URL link
		// Note: counter=1 positions ptr to 2nd row, where 1st row is the header row.
		for (int counter=1; counter < numberOfDocsOnPage; counter++) {
			// form selector
//			rowDocTypeSelector = "//td[@id='documentRecommender_maincontentTD']/table/tbody/tr[" + 
//            						Integer.toString(initialSelectorValue + counter) + "]/td[2]";
			
//			rowFileNameSelector = "//td[@id='documentRecommender_maincontentTD']/table/tbody/tr[" + 
//									Integer.toString(initialSelectorValue + counter) + "]/td[1]";
			
			String rowfileNameLinkSelector = "//td[@id='documentRecommender_maincontentTD']/table/tbody/tr[" + 
												Integer.toString(initialSelectorValue + counter) + "]/td[1]" + 
												"/a[@class='df_docOwnerLink']";
			
			// Get URL of the file name link
			String rowfileNameLinkDetails = this.getObjectAttribute(rowfileNameLinkSelector, "onclick");
			
			// check doc link does not contain a file extension at the end URL
			if (!verifyLinkHasFileExtension(rowfileNameLinkDetails)){
				return rowfileNameLinkSelector;
			}
		}
		
		return null;
	}

	/**
	 * Verify document file name link contains an actual file extension in URL
	 * @param linkUrl the URL which may or may not contain a file extension
	 * @return true of false on whether link contains a file extension. 
	 * Note: no file extension means doc link can open a doc contents widget 
	 *        instead of downloading the file via the link.
	 */
	public boolean verifyLinkHasFileExtension(String linkUrl){
		String extension = FilenameUtils.getExtension(linkUrl);
		
		if (extension.isEmpty()) {
			return true; 
		}
		return false; 
	}
	/**
	 * Verify document contents can be opened and displayed in Recommended Docs tab
	 * @return true where doc contents page associated with doc link can open correctly
	 */
	public boolean verifyDocContentsInRecDocsTab(){
		String fileNameSelector = null;
		
		// Get pagination label for docs result list
		String paginationLabel = getPaginationLabel();
		
		// in doc results, get the selector of the 1st doc that has doc contents page.
		fileNameSelector =  getFirstFileNameWithContentsPage(paginationLabel);
		
		if (fileNameSelector == null) {
			log.info("verifyDocContentsInRecDocsTab(): " +
					  "results doc list does not have a file with contents page");
			return false;
		}
		
		// check that results are loaded before clicking 1st row doc link 
		if (!isDocResultsLoaded()) {
			log.info("verifyDocContentsInRecDocsTab(): " +
			  "results doc list not loaded");
			return false;
		}
			
	    //open the contents of this doc
		clickJS(fileNameSelector);
			
		// if page does not load in 60sec
		if (!this.waitForElement(docContentsBackToSearchResults) && 
			!this.waitForElement(docContentsW3PageFrame)) {
			log.info("verifyDocContentsInRecDocsTab(): " +
					  "Document contents page in Recommended documents tab " +
					  "exceeded loading time of 1min");
			return false;
		}
		
		// check loaded page contains expected format
		if (!isDocContentsPageLoaded()) {
			return false;   
		}
		
		return true;
	}
	
	/**
	 * Check if document contents page is loaded in Recommended Docs tab
	 * @return true id doc contents page is loaded
	 */
	public boolean isDocContentsPageLoaded() {
		if (!this.isVisible(docContentsBackToSearchResults)) { 
			log.info("isDocContentsPageLoaded(): " +
					 "'Back to search results' link is not loaded/visible");
			return false;
		}
		if (!this.isVisible(docContentsOpenInNewWindow)) { 
			log.info("isDocContentsPageLoaded(): " +
					 "'Open in a new window' link is not loaded/visible");
			return false;
		}
		if (!this.isPresent(docContentsW3PageFrame)) { 
			log.info("isDocContentsPageLoaded(): " +
					 "w3 mast header is not loaded/visible");
			return false;
		}
		return true;
	}
	
	/**
	 * Navigate back to search results from doc contents page
	 * @return true to can return to Recommended doc tab search results 
	 */
	public boolean verifyReturnToSearchResultsFromContentsPage(){
		clickJS(docContentsBackToSearchResults);
		
		// wait for results page to load.
		if(!isDocResultsLoaded()) {
			log.info("Doc results not loaded.");
			return false;
		}
		
		// check if search results page is loaded  correctly
		if (isVisible(recDocsSearchFieldTitle) && 
			isVisible(recDocSearchTextField)) {
			return true;
		}
		
		log.info("verifyReturnToSearchResultsFromContentsPage(): " +
				   "Missing Search field and/or search label");
		return false;
	}
	
	/**
	 * Verify specific filter type is cleared (set to default)
	 * @param filterOption selector for filter
	 * @return true if specific filter is selected & cleared
	 */
	public boolean verifyFilterIsCleared(String filterOption){
		List<Element> optionList = new ArrayList<Element>();
		optionList = exec.getFirstElement(filterOption).useAsDropdown().getOptions();
		
		//String sTemp = optionList.get(0).getSingleElement(docTypeFilterSalesKit);
		
		// loop through all options in drop down menu & to find the selected option
		for (int current=0; current<optionList.size(); current++) {
			String filterOptionText = optionList.get(current).getText();

			// if option is selected & also set to default (Cleared)
			if (optionList.get(current).isSelected() && 
					filterOptionText.startsWith("All")) {
					// filterOption is cleared 
					return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Get pagination label
	 * @param expectedDocType the doc type for all docs in the results list
	 * @return text of pagination label
	 */
	public String getPaginationLabel(){
		String paginationLabel = null;
		
		paginationLabel = getObjectText(recDocResultsPaginationLabel);
		return paginationLabel;
	}
}