/**
 * 
 */
package com.ibm.salesconnect.model.standard.Connections.Community;

import java.util.List;

import org.testng.Assert;

import com.ibm.atmn.waffle.core.Element;
import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardPageFrame;

/**
 * @author timlehane
 * @date Jun 4, 2013
 */
public class MembersPage extends StandardPageFrame {

	/**
	 * @param exec
	 */
	public MembersPage(Executor exec) {
		super(exec);
		Assert.assertTrue(isPageLoaded(), "Members page has not loaded within 60 seconds");	
	}


	@Override
	public boolean isPageLoaded() {
		return waitForSubpanelToLoad(pageLoaded);
	}
	
	public List<Element> getMembersOnPage(){
		return exec.getElements(getMembers);
	}
	
	public boolean checkForMember(String member){
		boolean result = false;
		List<Element> members = getMembersOnPage();
		for(int x=0;x<members.size();x++){
			if(members.get(x).getText().contains(member)){
				result = true;
			}
		}
		
		return result;
	}
	
	public void removeMember(String member){
		click(getRemoveMember(member));
		sleep(2);
		click(getConfrimRemoveButton);
		isPageLoaded();
	}
	
	public void waitForMemberToBeAdded(String member){
		int x=0;
		while(!checkForMember(member)){
			sleep(60);
			exec.navigate().refresh();
			x++;
			if(x==11){
				break;
			}
		}
	}
	
	public void addMember(String member){
		click(getAddMember);
		type(getMemberSearchField, member);
		
		click(getDropdownSelection);
		click(getSaveButton);
		isPageLoaded();
	}

	
	//Selectors
	public static String pageLoaded = "//a[@id='findAMember']";
	public String getMember(String member){return "//div[@id='membersListId']//a[contains(text(),'" + member + "')]";};
	public String getRemoveMember(String member){return "//a[contains(text(),'" + member + "')]/../..//a[contains(text(),'Remove')]";};
	public String getAddMember = "//a[@id='memberAddButtonLink']";
	public String getMemberSearchField = "//input[@id='addMembersWidgetPeopleTypeAhead']";
	public String getDropdownSelection = "//li//b";
	public String getSaveButton = "//input[@class='lotusFormButton'][@value='Save'][@type='button']";
	public String getConfrimRemoveButton = "//input[@class='lotusFormButton submit']";
	public String getMembers = "//div[@id='membersListId']//span[@class='vcard']//a";
	
	
	
	
	
}
