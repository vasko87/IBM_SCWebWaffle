/**
 * 
 */
package com.ibm.salesconnect.model.partials;

import java.util.ListIterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardPageFrame;
import com.ibm.salesconnect.objects.Opportunity;

/**
 * @author Administrator
 *
 */
public class EditTeamMembersSubpanel extends StandardPageFrame {
	Logger log = LoggerFactory.getLogger(EditTeamMembersSubpanel.class);
	/**
	 * @param exec
	 */
	public EditTeamMembersSubpanel(Executor exec) {
		super(exec);
		Assert.assertTrue(isPageLoaded(), "Edit team Members subpanel has not loaded within 60 seconds");
	}

	/* (non-Javadoc)
	 * @see com.ibm.salesconnect.model.StandardPageFrame#isPageLoaded()
	 */
	@Override
	public boolean isPageLoaded() {
		return waitForSubpanelToLoad(pageLoaded);
	}
	
	//Selectors
	public static String pageLoaded = "//input[@id='assigned_user_name']";
	
	public static String getButton_OpptOwner = "//button[@id='btn_assigned_user_name']";
	
	public static String getButton_AddOpptTeamMember = "css=a#addAdditionalUsersLink";
	public String getButton_AddOpportunityTeamUsers(String sIndex){
		return "//button[@id='EditView_additional_users_btn_select_" + sIndex +"']";}
	public static String getListBox_AddTeamMemberRole(String sIndex){
		return "//select[@id='EditView_additional_users_role_id_" + sIndex + "']";}
	public static String getRemoveOpportunityTeamMembers(int iRow){
		return "//img[@id='EditView_additional_users_btn_remove_"+iRow+"']";}
	
	//Methods
	public void enterTeamMembers(Opportunity oppt){
		
		if (oppt.sOpptOwner.length() > 0) {
			UserSelectPopup userSelectPopup = openSelectOwner();
			userSelectPopup.searchForUser(oppt.sOpptOwner);
			userSelectPopup.selectResult(oppt.sOpptOwner);
		}		
		int previousRows = 1;
		if(!oppt.vOpportunityTeam.isEmpty()){
			ListIterator<String> iUser = oppt.vOpportunityTeam.listIterator();	
			ListIterator<String> iRole = oppt.vOpportunityRole.listIterator();
			int sRow = 1;
			while (iUser.hasNext()) {
				addOpportunityTeamMember(iUser.next(), sRow, iRole.next());
				++sRow;
			}
			previousRows = sRow;
		}
		
		if(!oppt.vBusinessParters.isEmpty()){
			ListIterator<String> iUser = oppt.vBusinessParters.listIterator();	
			ListIterator<String> iRole = oppt.vBusinessRole.listIterator();
			int sRow = previousRows;
			if(sRow<3){
				sRow = 3;
			}
			while (iUser.hasNext()) {
				addOpportunityBusinessPartner(iUser.next(), sRow, iRole.next());
				++sRow;
			}
		}
	}
	

	/**
	 * Add one opportunity team member
	 * @param sUser - Can be User DisplayName or User Email
	 * @param sRow - Indicate which row obj. to operate
	 * @param sRole - String from gsMemberRoles for role to assign to team member
	 * @return true if field(s) were set
	 */
	public void addOpportunityTeamMember(String sUser, int iRow, String sRole){
		String sIndex = Integer.toString(iRow);
		//There are 2 team member entry exists by default, so no need to create one for first 2 rounds
		if(iRow > 2){
			click(getButton_AddOpptTeamMember);
		}
		

		UserSelectPopup userSelectPopup = openSelectUser(sIndex);
		userSelectPopup.searchForUser(sUser);
		userSelectPopup.selectResultUsingName(sUser);
		
		if(iRow >2){
		select(getListBox_AddTeamMemberRole(sIndex),sRole);
		}		
	}
	
	public void addOpportunityBusinessPartner(String sCEID, int iRow, String sRole){
		String sIndex = Integer.toString(iRow);
		//There are 2 team member entry exists by default, so no need to create one for first 2 rounds
		if(iRow > 2){
			click(getButton_AddOpptTeamMember);
		}

		UserSelectPopup userSelectPopup = openSelectUser(sIndex);
		userSelectPopup.searchForBusinessPartner(sCEID);
		userSelectPopup.selectBusinessPartnerResult();
		
		if(iRow >2){
			select(getListBox_AddTeamMemberRole(sIndex),sRole);
		}
	}
	
	public void removeOpportunityTeamMember(int iRow){
		click(getRemoveOpportunityTeamMembers(iRow));
	}
	
	/**
	 * Open the select user popup for additional team members
	 * @return select user object
	 */
	private UserSelectPopup openSelectUser(String sIndex){
		click(getButton_AddOpportunityTeamUsers(sIndex));
		getPopUp();
		return  new UserSelectPopup(exec);
	}
	
	/**
	 * Open the select user popup for the opportunity owner
	 * @return select user object
	 */
	private UserSelectPopup openSelectOwner() {
		click(getButton_OpptOwner);
		getPopUp();
		return new UserSelectPopup(exec);
	}
}	
