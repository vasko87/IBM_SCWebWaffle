package com.ibm.salesconnect.model;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;


public abstract class StandardViewPage extends StandardPageFrame{
	
	//List of Columns. This allows 
	public enum Columns{ 
		
		
		
	}

	/**Default Constructor
	 * @param exec - The current browser session
	 */
	protected StandardViewPage(Executor exec){
		super(exec);
	}
	
	/**
	 * Checks that the  page has successfully loaded without switching frames
	 */
	@Override
	public boolean isPageLoaded(){
		return checkForElement(searchBarTextBox);
	}
	
	public void select(String selector, String choice){
		sidecarListBoxSelect(selector, choice);
	}
	
	/**
	 * Checks the visibility of the loading message and waits until it disappears
	 */
	public void waitToLoad(){
		if(isPresent(loadingAlertMessage))
			sleep(10);
	}

	/**
	 * Searches for the selected string within a text and waits for loading to finish
	 * @param criteria - the text to be entered in the search box
	 */
	public void searchNoFilter(String criteria){
		clearFilters();
		type(searchBarTextBox, criteria);
		if (isPresent(searchButton)) {
			click(searchButton);
		}
		waitToLoad();
	}
	
	/**
	 * Searches for the selected string within a text and waits for loading to finish
	 * @param criteria - the text to be entered in the search box
	 */
	public void searchWithFilter(String filter, String criteria){
		selectFilterElement(filter);
		type(searchBarTextBox, criteria);
		sleep(5);
		//waitToLoad();
	}
	
	/**
	 * Selects a filter to be applied
	 * @param choice - the string of the filter to be applied
	 */
	public void selectFilterElement(String choice){
		click(filterDropdownButton);
		sleep(10);
		if(isPresent(filterDropdownSearch))
		{
			type(filterDropdownSearch, choice);
			if(choice.equals("Create"))
				click("//ul[@class='select2-results']//li[1]");
			else if(!choice.equals("Create"))
			click(getFilterOption(choice));
		}
	}
	
	/**
	 * Searches for the selected string with the current filter
	 * @param criteria - the text to be entered in the search box
	 */
	public void searchWithFilter(String criteria){
		type(searchBarTextBox, criteria);
		waitToLoad();
	}
	
	/**
	 * Creates a filter based on the given parameters
	 * @param name - The name to be applied to the filter
	 * @param field - What will be used as the search criterion in the filter
	 * @param operator - What will be listed used as the parameter in the second option
	 * @param valueOne - The first value to be searched for
	 * @param valueTwo - The Second value to be searched for
	 */
	public void createFilterTwoElements(String name, String field, String operator, String valueOne, String valueTwo)
	{
		sleep(15);
		selectFilterElement("Create");
		sleep(15);
		select(createFirstArgument, field);
		select(createSecondArgument, operator);
		if(!valueTwo.equals("")){
			type(createThirdArgument, valueOne);
			type(createFourthArgument, valueTwo);
		}
		else
			type(createThirdArgument, valueOne);
		type(createName, name);
		click(filterSaveButton);
	}
	
	public void searchWithFilterContact(String criteria){
		type(searchBarTextBoxContact, criteria);
		waitToLoad();
	}
	
	
	/**
	 * Creates a filter based on the given parameters
	 * @param - String name - The name to be applied to the filter
	 * @param - String field - What wilsl be used as the search criterion in the filter
	 * @param - String operator - What will be listed used as the parameter in the second option
	 * @param - String valueOne - The value to be searched for
	 */
	public void createFilterOneElement(String name, String filterType, String operator, String valueOne){
		createFilterTwoElements(name, filterType, operator, valueOne, "");
	}
	
	/**
	 * Enables the selected column in the search fields 
	 * @param - String field - The field to enable
	 */
	public void showFieldColumn(String Field){
		click(fieldToggleButton);
		if(!exec.getElements(getFieldToggle(Field)).get(0).getAttribute("class").equals("active"))
			click(getFieldToggle(Field));
	}
	
	/**
	 * Clicks the clear filter button until there are none
	 */
	public void clearFilters(){
		while(isVisible(clearFilterButton))
			click(clearFilterButton);
	}
	
	/**
	 * returns the text contained in an entry of the main-content table
	 * @param Row - the row to pull from
	 * @param Column - the column identifier (e.g. "email")
	 * @return A String containing the value within that entry
	 * @throws IllegalArgumentException 
	 */
	public String getEntry(int Row, int Column) throws IllegalArgumentException{
		if(getEntryType(Row, Column).equals("score"))
			throw new IllegalArgumentException("Cannot pull string from the \"Strength\" column, breaking to prevent timeout");
		if(getEntryType(Row, Column).equals("email") || getEntryType(Row, Column).equals("relate"))
			return getObjectText(getNthTableRow(Row) + "/td[" + Column + "]//a");
		else
			return getObjectText(getNthTableRow(Row) + "/td[" + Column + "]//div");
	}
	
	
	/**
	 * Checks the entries in each column to determine if they all match the entered value for the column
	 * @param Column - the field to check
	 * @param Value - the value to check against 
	 * @return whether the fields contain the string
	 */
	public boolean verifyEntries(String Column, String Value){
		boolean success = true;
		int column = getColumnIndex(Column);
		//get the number of displayed table entries
		int size = exec.getElements(tableContents + "//tr").size();
		for(int i = 1; i <= size && success; i++)
		{
			try{
				if(!getEntry(i, column).contains(Value))
					success = false;
			}
			catch(IllegalArgumentException e){
				Assert.fail("Cannot check this argument");
			}
		}
		return success;
	}
	
	
	/**
	 * 
	 * @param Column
	 * @return the index of the named column
	 */
	public int getColumnIndex(String Column)
	{
		int i = 1;
		for(i = 1; !getTopRowEntryText(i).contains(Column); i++);
		return i;
	}
	
	/**
	 * Sorts the field descending
	 * @param Column
	 */
	public void setColumnDescending(int column)
	{
		while(!exec.getElements(getTopRowEntry(column)).get(0).getAttribute("class").contains("sorting_desc"))
			click(getTopRowEntry(column));
	}
	
	/**
	 * Search by input into search bar
	 * @param criteria
	 */
	public void searchNoFilter_DL(String criteria){
		clearFilters();
		type(searchBarTextBox, criteria);
		sleep(5);
		if (isPresent(searchButton)) {
			click(searchButton);
		}
		waitToLoad();
	}

	public String searchBarTextBoxContact = "//input[@placeholder='Search by first name, last name...']";
	public String searchBarTextBox = "//input[@class='search-name']";
	public String nextPageButton = "//button[@data-action='show-next']";
	public String previousPageButton = "//button[@data-action='show-prev']";
	public String firstPageButton = "//button[@data-action='show-start']";
	public String createButton = "//a[@name='create_button']";
	public String filterDropdownButton = "//div[@class='filter-view search']//a//..";
	public String filterDropdownSearch = "//div[@id='select2-drop']//input[@type='text']";
	public String getFilterOption(String choice){return "//ul[@class='select2-results']//li[contains(*,'" + choice + "')]";}
	public String getTableHeader(String header){return "//thead//span[contains(text(), '" + header + "')]";}
	public String getTopRowEntry(int i){return "//thead//tr[1]//th[" + String.valueOf(i) + "]";}
	public String getTopRowEntryText(int i){return getObjectText( getTopRowEntry(i) + "//span");}
	public String getEntryType(int Row, int Column){return exec.getElements(getNthTableRow(Row) + "//td[" + String.valueOf(Column) + "]").get(0).getAttribute("data-type");}
	public String clearFilterButton = "//span[@class='choice-filter-close']";
	public String loadingAlertMessage = "//div[@class='alert alert-process']/strong";
	public String createFirstArgument = "//div[@data-filter='field']//a";
	public String createSecondArgument = "//div[@data-filter='operator']//a";
	public String createThirdArgument = "//div[@class='search-filter']//div[@data-filter='value']//input";
	public String createFourthArgument = "//div[@data-filter='field']//a";
	public String createName = "//div[@class='filter-header']//input";
	public String filterSaveButton = "//a[@track='click:filter-save']";
	public String getNthTableRow(int Row) {return "//tbody//tr[" + String.valueOf(Row) + "]";}
	public String getTableLocation(int Row, int Column) {return "//tbody//tr[" + String.valueOf(Row) + "]//td[" + String.valueOf(Column) + "]";}
	public String fieldToggleButton = "//th[@class='nosort morecol']//button[@data-action='fields-toggle']";
	public String getFieldToggle(String field) {return "//button//i[contains(text(), '" + field + "')]";}
	public String tableContents = "//tbody";
	public String searchButton = "//a[@name='quick_search_button']";
	//public abstract String[] NonTextColumns = {};
	
}
