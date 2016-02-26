/**
 * 
 */
package com.ibm.salesconnect.model.standard.Note;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Element;
import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.objects.Note;
import com.ibm.salesconnect.model.StandardViewPage;

/**
 * @author timlehane
 * @date May 15, 2013
 */
public class ViewNotePage extends StandardViewPage
{
	Logger log = LoggerFactory.getLogger(ViewNotePage.class);

	/**
	 * @param exec
	 */
	public ViewNotePage(Executor exec) {
		super(exec);
		
		Assert.assertTrue(isPageLoaded(), "View Note page has not loaded within 60 seconds");	
	}


	/*@Override
	public boolean isPageLoaded() {
		return waitForPageToLoad(pageLoaded);
	}*/
	
	//Selectors
	/*public static String pageLoaded = "//input[@id='search_form_clear']";
	
	public static String pageLoaded = "//html/body/div[1]/div/div[3]/div/div/div[1]/div/div[2]/div[1]/div[1]/div/div/div/input";
	public static String searchField = "//html/body/div[1]/div/div[3]/div/div/div[1]/div/div[2]/div[1]/div[1]/div/div/div/input";
	public static String pageLoaded = "//*";
	public static String searchFrame = "//iframe[@id='bwc-frame']";*/
	
	public static String searchField="//*[contains(@class,'search-name')]";
	public static String pageLoaded ="//*[contains(@class,'search-name')]";
	public static String getSearchFormType = "//form[@id='search_form']//input[@name='searchFormTab']";
	public static String clearSearchForm = "//input[@id='search_form_clear']";
	
	public String getSearchField(String searchType){ return  "//input[@id='name_" + searchType + "']";}
	public String getMyNotes(String searchType){ return  "//input[@id='current_user_only_" + searchType + "']";}
	public static String searchButton = "//input[@class='search-name']";
	public static String getMyFavorites = "//input[@id='favorites_only_basic']";
	
	public static String getAllFavoriteButtons = "//div[@class='star']//div[@class='off']";
	public static String getAllUnfavoriteButtons = "//div[@class='star']//div[@class='on']";
	public static String getAllNotesNames = "//td[@scope='row']//a";
	
	
	//public String getNoteSelection(String noteSubject) {return "//form[@id='MassUpdate']//a[contains(text(),'" + noteSubject + "')]";}
	
	public String getNoteSelection(String noteSubject) {return "//tbody//a[contains(text(),'" + noteSubject + "')]";}
	
	//Methods
	/**
	 * Gets the current search form status 
	 * returns basic/advanced
	 */
	public String getSearchtype(){
		String value = getObjectAttribute(getSearchFormType, "value");
		return value.substring(0,value.length()-7);
	}
	
	/**
	 * Searches for a note based on the parameters
	 * @param note
	 */
	public void searchForNote(Note note){
		//String searchType=getSearchtype();
		
		//click(clearSearchForm);
		type(searchField, note.sSubject);
		//type(getSearchField(searchType),note.sSubject);
		
	/*	if(note.bMyNotes){
			if(!isChecked(getMyNotes(searchType))){
				click(getMyNotes(searchType));
			}
		}
		else {
			if(isChecked(getMyNotes(searchType))){
				click(getMyNotes(searchType));
			}
		}
		if(note.bMyFavorites){ //note is a favorite
			if(!isChecked(getMyFavorites)){
				click(getMyFavorites);
			}
		}
		else { //note is not a favorite
			if(isChecked(getMyFavorites)){
				click(getMyFavorites);
			}
		}
		
		click(searchButton);*/
	}

	/**
	 * Searches for a note based on the parameters
	 * @param note
	 */
	public void searchForNote(String noteSubject){
		//String searchType=getSearchtype();
		
		//click(clearSearchForm);
		type(searchField, noteSubject);
		//type(getSearchField(searchType),note.sSubject);
		
	/*	if(note.bMyNotes){
			if(!isChecked(getMyNotes(searchType))){
				click(getMyNotes(searchType));
			}
		}
		else {
			if(isChecked(getMyNotes(searchType))){
				click(getMyNotes(searchType));
			}
		}
		if(note.bMyFavorites){ //note is a favorite
			if(!isChecked(getMyFavorites)){
				click(getMyFavorites);
			}
		}
		else { //note is not a favorite
			if(isChecked(getMyFavorites)){
				click(getMyFavorites);
			}
		}
*/		
		click(searchButton);
	}

	/**
	 * Click on the correct search result
	 * @param note
	 * @return new note detail page object
	 */
	public NoteDetailPage selectResult(Note note) {
		sleep(10);
		//click(clearSearchForm);
	//	click(getNoteSelection(note.sSubject));
	//	return new NoteDetailPage(exec); 
		
		searchNoFilter(note.sSubject);
		click(getNoteSelection(note.sSubject));
		return new NoteDetailPage(exec);
	}
	
	/**
	 * Click on the correct search result
	 * @param note
	 * @return new note detail page object
	 */
	public NoteDetailPage selectResult(String noteSubject) {
		sleep(10);
	
		searchNoFilter(noteSubject);
		click(getNoteSelection(noteSubject));
		return new NoteDetailPage(exec);
	}
	
	public Boolean confirmResult(Note note) {
		if (note.sSubject == getObjectText("//td[@data-type='name']//a")) {
			return true;
		}
		return false;
	}
	
	public NoteDetailPage selectResultWithFilter(Note note){
		searchWithFilter(note.sSubject);
		click(getTableLocation(1, getColumnIndex("Subject")) + "//a");
		return new NoteDetailPage(exec);
	}

public List<String> addAllVisibleNotesToFavorites(){
		List<String> notes = new ArrayList<String>();
		
		List<Element> favoriteButtons = exec.getElements(getAllFavoriteButtons);
		List<Element> noteElements = exec.getElements(getAllNotesNames);
		exec.executeScript("window.scrollBy(0,arguments[0])", 300);
		for(int x=0; x<favoriteButtons.size(); x++){
			
			favoriteButtons.get(x).click();
			notes.add(noteElements.get(x).getText());
		}
		
		return notes;
	}
	
	public List<String> getAllVisiblenotes(){
		List<String> notes = new ArrayList<String>();
		
		exec.executeScript("window.scrollBy(0,arguments[0])", 300);
		List<Element> noteElements  = exec.getElements(getAllNotesNames);
		
		for(int x=0; x<noteElements.size(); x++){
			notes.add(noteElements.get(x).getText());
		}
		
		return notes;
	}
	
	public List<String> removeAllVisiblenotesFromFavorites(){
		List<String> notes = new ArrayList<String>();
		
		List<Element> favoriteButtons = exec.getElements(getAllUnfavoriteButtons);
		List<Element> noteElements = exec.getElements(getAllNotesNames);
		exec.executeScript("window.scrollBy(0,arguments[0])", 300);
		for(int x=0; x<noteElements.size(); x++){
			
			favoriteButtons.get(x).click();
			notes.add(noteElements.get(x).getText());
		}
		
		return notes;
	}
}

	

