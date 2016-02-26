/**
 * 
 */
package com.ibm.salesconnect.model.partials;

import java.util.ListIterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.common.GC;
import com.ibm.salesconnect.model.StandardPageFrame;
import com.ibm.salesconnect.objects.Document;

/**
 * @author timlehane
 * @date May 30, 2013
 */
public class DocumentsSubpanel extends StandardPageFrame {
	Logger log = LoggerFactory.getLogger(DocumentsSubpanel.class);
	/**
	 * @param exec
	 */
	public DocumentsSubpanel(Executor exec) {
		super(exec);
		Assert.assertTrue(isPageLoaded(), "Documents Subpanel has not loaded within 60 seconds");	
	}

	@Override
	public boolean isPageLoaded() {
		scrollToBottomOfPage();
		return waitForSubpanelToLoad(pageLoaded);
	}
	
	
	public static String pageLoaded = "//a[@id='documents_opportunities_lnk_new_documents_button_create_button' or @id='documents_accounts__button_create_button']";
	
	public static String createPageLoaded = "//input[@id='filename_file']";
	public static String attachDocumentButton = "//a[@id='documents_opportunities_lnk_new_documents_button_create_button' or @id='documents_accounts__button_create_button']";
	public static String attachDocumentButton2 = "//a[@id='documents_accounts__button_create_button']";
	public static String fullForm = "//input[@id='Documents_subpanel_full_form_button']";
	public static String selectExternalFile = "//*[@id='filename_repo_file']/..";
	public static String fileName = "//input[@id='filename_file']";
	public static String externalFileNameSearch = "//button[@id='filename_remoteSelectBtn']";
	public static String docName = "//input[@id='document_name']";
	public static String description = "//textarea[@id='description']";
	public static String shareWithEveryone = "//input[@id='share_document_type_1']";
	public static String shareWithSelectPeople = "//input[@id='share_document_type_0']";
	public static String shareWithClientTeamEdit = "//input[@id='share_document_with_4' or @id='share_document_with_2']";
	public static String shareWithIndividualsEdit = "//input[@id='share_document_with_1']";
	public static String shareWithClientTeam = "//input[@id='share_document_with_4' or @id='share_document_with_2']";
	public static String shareWithIndividuals = "//input[@id='share_document_with_1']";
	public static String addUsersIcon = "//button[@id='btn_documents_selected_users']";
	public String getDocLink(String docName){return "//a[contains(text(),'" + docName + "')]";};
	public static String status = "//div[@id='ajaxStatusDiv']";
	public String getBusinessCard(String userName) { return "//div[@id='list_subpanel_documents']//a[contains(*,'" + userName + "')]";};
	public String getEditButton(String docName) {return "//a[contains(text(),'" + docName + "')]/../../..//a[contains(text(),'edit')]";};
	public String getDeleteButton(String docName) {return "//a[contains(text(),'" + docName + "')]/../../..//span[@class='ab']";};
	public String getRemoveButton(String docName) {return "//a[contains(text(),'" + docName + "')]/../../..//span/ul/li/ul/li/a";};
	public String sharedWithUser(String userName) {return "//select[@id='documents_selected_users[]']/option[contains(text(),'" + userName + "')]";};
	public static String saveButton = "//div[@id='subpanel_documents_newDiv']//input[@name='Documents_subpanel_save_button']";
	public static String saveButtonEdit = "//input[@id='SAVE_FOOTER']";
	public static String nextDocumentPage = "//div[@id='list_subpanel_documents']//button[@title='Next']";
	public static String deleteButton = "//input[@id='delete_button']";
	public static String mainFrame = "//iframe[@id='bwc-frame']";
	
	
	public void enterDocumentInfo(Document subPanelDoc){
		try{
			scrollToBottomOfPage();
			clickJS(attachDocumentButton);
		}
		catch(AssertionError ae){
			clickAt(attachDocumentButton2);
		}
		
		scrollToBottomOfPage();
		sleep(10);
		
		if(subPanelDoc.sExternalFileName.length()>0&&subPanelDoc.sDocName.length()>0){
			click(selectExternalFile);
			click(selectExternalFile);
			click(selectExternalFile);
			DocumentSelectPopup popup = openSelectDocument();
			
			
			popup.searchForDocument(subPanelDoc);
			popup.selectResult(subPanelDoc);
			
		}
		
		if(subPanelDoc.sDocLocation.length()>0&&subPanelDoc.sDocName.length()>0){
			sendKeys(fileName,subPanelDoc.sDocLocation+subPanelDoc.sDocName);		
		}
		
		if (subPanelDoc.sDocName.length()>0){
			sleep(2);
			type(docName,subPanelDoc.sDocName);
		}
			
		
		if(subPanelDoc.sDescription.length()>0){
			type(description,subPanelDoc.sDescription);
		}
		
		if(!subPanelDoc.vShareWithOptions.isEmpty()){
			ListIterator<String> itSharingOption = subPanelDoc.vShareWithOptions.listIterator();

			while (itSharingOption.hasNext()) {
				String sharingOption =  itSharingOption.next();
				//Public
				if(sharingOption.equalsIgnoreCase(GC.gsShareWithEveryone)){
					click(shareWithEveryone);
				}
				//Oppt Team
				else if(sharingOption.equalsIgnoreCase(GC.gsShareWithClientTeam)){
					if(isPresent(shareWithClientTeam)){
						if(!isChecked(shareWithClientTeam)){
							click(shareWithClientTeam);
						}
					}
				}
				else if(sharingOption.equalsIgnoreCase(GC.gsShareWithIndividuals)){
					//Select Individuals
					if(!isChecked(shareWithIndividuals)){
						click(shareWithIndividuals);
					}
					//Deselect the other options
					if(isPresent(shareWithClientTeam)){
						if(isChecked(shareWithClientTeam)){
							click(shareWithClientTeam);
						}
					}
					int x = subPanelDoc.vUsers.size();
					for (int i = 0; i < x; i++) {
						addIndividualToShareList(subPanelDoc.vUsers.get(i));
					}
				}
				else if(sharingOption.equalsIgnoreCase(GC.gsShareWithIndividualsAndClientTeam)){
					//Select Individuals
					if(!isChecked(shareWithIndividuals)){
						clickWithScrolling(shareWithIndividuals);
					}
					//Deselect the other options
					if(isPresent(shareWithClientTeam)){
						if(!isChecked(shareWithClientTeam)){
							clickWithScrolling(shareWithClientTeam);
						}
					}
					int x = subPanelDoc.vUsers.size();
					for (int i = 0; i < x; i++) {
						addIndividualToShareList(subPanelDoc.vUsers.get(i));
					}
				}
				else if(sharingOption.equalsIgnoreCase(GC.gsShareWithIndividualUserNameSelect)){
					//Select Individuals
					if(!isChecked(shareWithIndividuals)){
						click(shareWithIndividuals);
					}
					int x = subPanelDoc.vUsers.size();
					for (int i = 0; i < x; i++) {
						addIndividualToShareListNameSelect(subPanelDoc.vUsers.get(i));
					}
				}
				else if(sharingOption.equalsIgnoreCase(GC.gsShareWithNoOne)){
					if(isChecked(shareWithEveryone)){
						click(shareWithSelectPeople);
					}
					if(isPresent(shareWithClientTeam)){
						if(isChecked(shareWithClientTeam)){
							click(shareWithClientTeam);
						}
					}
					int x = subPanelDoc.vUsers.size();
					for (int i = 0; i < x; i++) {
						addIndividualToShareList(subPanelDoc.vUsers.get(i));
					}
					
				}
			}
		}
		//Ensure you have updated <parameter name="root_folder_name" value="C:\<root_folder_name>\" /> in the .xml file you are running your script from
		if(subPanelDoc.sDocLocation.length()>0&&subPanelDoc.sDocName.length()>0){
			sendKeys(fileName,subPanelDoc.sDocLocation+subPanelDoc.sDocName);
		}
	}
	
	public DocumentSelectPopup openSelectDocument(){
		click(externalFileNameSearch);
		getPopUp();
		return  new DocumentSelectPopup(exec);
	}
	
	public void saveDocument(){
		scrollElementToMiddleOfBrowser(saveButton);
		click(saveButton);
//		if(!isSaved(status)){   // DISABLED as 'Saved' message no longer used when doc is saved.
//			Assert.assertTrue(false, "Document has not been saved successfully");
//		}
		switchToMainWindow();
		switchToFrame(mainFrame);
		
		if(!isPageLoaded()) { // waits for Doc subpanel to reload, waiting for Attach document button to appear.
			Assert.assertTrue(false, "Document has not been saved successfully");
		}

	}

	/**
	 * Adds Individual to Share List
	 * 
	 */
	public void addIndividualToShareList(String sUser){
		
		UserSelectPopup userSelectPopup = openSelectUser();
		userSelectPopup.searchForUser(sUser);
		userSelectPopup.selectResult(sUser);
	}
	
	/**
	 * Adds Individual to Share List using the name select
	 * 
	 */
	public void addIndividualToShareListNameSelect(String sUser){
		
		UserSelectPopup userSelectPopup = openSelectUser();
		userSelectPopup.searchForUser(sUser);
		userSelectPopup.selectResultUsingName(sUser);
	}
	
	/**
	 * Open the select user popup to Select Individual to Share with
	 * @return select user object
	 */
	private UserSelectPopup openSelectUser() {
		//Popup sometimes takes a while to load so needs an extra sleep
		sleep(5);
		waitForPageToLoad(addUsersIcon);
		click(addUsersIcon);
		getPopUp();
		return new UserSelectPopup(exec);
	}
	
	/**
	 * @param subPanelDoc
	 */
	public void verifyDocumentPresent(Document subPanelDoc) {
		while(true){
			
			if(!checkForElement(getDocLink(subPanelDoc.sDocName),20000)){
				if(checkForElement(nextDocumentPage)){
					click(nextDocumentPage);
					waitForPageToLoad(pageLoaded);
				}
				else{
					Assert.assertEquals(getObjectText(getDocLink(subPanelDoc.sDocName)), subPanelDoc.sDocName, "Document not found");
					break;
				}
			}
			else{
				Assert.assertTrue(getObjectText(getDocLink(subPanelDoc.sDocName)).contains(subPanelDoc.sDocName), "Document not found");
				break;
			}
		}
	}

	/**
	 * @param user1
	 */
	public void verifyBusinessCard(User user1) {
		ConnectionsBusinessCard connectionsBusinessCard = openConnectionsBusinessCard(getBusinessCard(user1.getFirstName()), exec);
		connectionsBusinessCard.verifyBusinessCardContents(user1);
		
	}
	
	public void openEditDocumentSubpanel(String docName){
		while(true){
			exec.executeScript("window.scrollBy(0,150)", new String());  
			if(!isPresent(getEditButton(docName))){
				if(isPresent(nextDocumentPage)){
					scrollElementToMiddleOfBrowser(nextDocumentPage);
					click(nextDocumentPage);
					waitForPageToLoad(pageLoaded);
				}
				else{
					Assert.assertEquals(getObjectText(getEditButton(docName)), docName, "Document not found");
					break;
				}
			}
			else{
				clickWithScrolling(getEditButton(docName));
				break;
			}
		}
		waitForSubpanelToLoad(description);
	}
	
	public boolean isSharedWith(String userName){
		String start = userName.substring(0, userName.indexOf(" "));
		String end = userName.substring(userName.indexOf(" ") + 1, userName.length());
		String sUserFull = start + " " + "(" + start + ") " + end;
		
		if(isPresent(sharedWithUser(sUserFull))){
			return true;
		}
		else if (isPresent(sharedWithUser(userName))){
			return true;
		}
		else{
			log.info("Document is not shared with user");
			return false;
		}
	}
	
	public void deleteDocument(String docName){
		click(getDeleteButton(docName));
		click(getRemoveButton(docName));
		acceptAlert();
	}
	
	public void updateSharingOptions(Document subPanelDoc){
		if(!subPanelDoc.vShareWithOptions.isEmpty()){
			ListIterator<String> itSharingOption = subPanelDoc.vShareWithOptions.listIterator();

			while (itSharingOption.hasNext()) {
				String sharingOption =  itSharingOption.next();
				//Public
				if(sharingOption.equalsIgnoreCase(GC.gsShareWithEveryone)){
						click(shareWithEveryone);
				}
				else if(sharingOption.equalsIgnoreCase(GC.gsShareWithClientTeam)){
					if(!isChecked(shareWithClientTeamEdit)){
						click(shareWithClientTeamEdit);
					}
				}
				else if(sharingOption.equalsIgnoreCase(GC.gsShareWithIndividuals)){
					//Select Individuals
					if(!isChecked(shareWithIndividualsEdit)){
						click(shareWithIndividualsEdit);
					}
					//Deselect the other options
					if(isPresent(shareWithClientTeamEdit)){
						if(isChecked(shareWithClientTeamEdit)){
							click(shareWithClientTeamEdit);
						}
					}
					int x = subPanelDoc.vUsers.size();
					for (int i = 0; i < x; i++) {
						addIndividualToShareList(subPanelDoc.vUsers.get(i));
					}
				}
				else if(sharingOption.equalsIgnoreCase(GC.gsShareWithNoOne)){
					if(isChecked(shareWithEveryone)){
						click(shareWithSelectPeople);
					}
					
					
					if(isPresent(shareWithClientTeamEdit)){
						if(isChecked(shareWithClientTeamEdit)){
							click(shareWithClientTeamEdit);
						}
					}
					
					if(isChecked(shareWithIndividualsEdit)){
						click(shareWithIndividualsEdit);
					}
					
				}
			}
		}
		click(saveButtonEdit);
	}
	
	public void deleteDocument(){
		click(deleteButton);
		acceptAlert();
	}
}
