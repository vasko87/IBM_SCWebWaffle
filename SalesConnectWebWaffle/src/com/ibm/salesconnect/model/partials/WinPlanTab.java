/**
 * 
 */
package com.ibm.salesconnect.model.partials;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardPageFrame;
import com.ibm.salesconnect.model.standard.Opportunity.OpportunityDetailPage;

/**
 * @author timlehane
 * @date Nov 4, 2013
 */
public class WinPlanTab extends StandardPageFrame {
	Logger log = LoggerFactory.getLogger(WinPlanTab.class);
	
	/**
	 * @param exec
	 */
	public WinPlanTab(Executor exec) {
		super(exec);
		Assert.assertTrue(isPageLoaded(), "Win Plan Tab has not loaded within 60 seconds");	
	}

	@Override
	public boolean isPageLoaded() {
		switchToFrame(winplanFrame);

		return waitForSubpanelToLoad(pageLoaded);
	}
	//Navigate to other tab
	public static String updatesTab = "//em[contains(text(),'Updates')]";
	
	//Summary
	public static String pageLoaded = "//span[@class='detaillabel'][contains(text(),'Opportunity owner')]";
	public static String winplanFrame = "//iframe[@id='oppty_micro_bwc']";
	public static String IBMExecSponsor = "//button[@id='btn_executive_sponsor_name']";
	public static String IBMSDSponsor = "//button[@id='btn_snd_sponsor_name']";
	public static String IBMBrandSponsor = "//span[@class='id-ff multiple']//button[@id='btn_brand_sponsors' and @class='button firstChild']";
	public String opptyOwnerIncludeIGF(String trueFalse) { return "//input[@id='include_igf' and @value='" + trueFalse + "']";};
	public String igfOwnerIncludeIGF(String trueFalse) { return "//input[@id='igf_include_igf' and @value='" + trueFalse + "']";};
	public String internationalDeal(String trueFalse) { return "//input[@id='international_deal' and @value='" + trueFalse + "']";};
	public static String IBMExecutive = "//input[@id='igf_executive']";
	
	public String completedPercentage = "//span[@id='overall_progressporogressBarSpan']";
	public String completedIV = "//span[@id='IBM_REVENUELINEITEMS_IDEN_Span']";
	public String completedVQ = "//span[@id='IBM_REVENUELINEITEMS_VAL_Span']";
	public String completedQGCA = "//span[@id='IBM_REVENUELINEITEMS_QUA_Span']";
	public String completedCAW = "//span[@id='IBM_REVENUELINEITEMS_CON_Span']";
	
	public static String status = "//div[@id='ajaxStatusDiv']";
	public static String summarySave = "//div[@id='detailpanel_1_winplans']//input[@id='SAVE_FOOTER']";
	public static String saveIV = "//table[@id='IBM_REVENUELINEITEMS_IDEN']//input[@id='SAVE_FOOTER']";
	public static String saveVQ = "//table[@id='IBM_REVENUELINEITEMS_VAL']//input[@id='SAVE_FOOTER']";
	public static String saveQGCP = "//table[@id='IBM_REVENUELINEITEMS_QUA']//input[@id='SAVE_FOOTER']";
	public static String saveCAW = "//table[@id='IBM_REVENUELINEITEMS_CON']//input[@id='SAVE_FOOTER']";
	
	public static Pattern patternCompleted = Pattern.compile("(.*?) of (.*?) complete \\((.*?)%\\)");
	public static String questionCompleteClass = "question-status-icon-complete";
	public static String questionInCompleteClass = "question-status-icon-complete";
	public static String closeDateBox = "//a[contains(text(),'Close')]";
	
	//IV
	public static String expandIV = "//a[@id='exp_ibm_revenuelineitems_iden']";
	public static String collapseIV = "//a[@id='col_ibm_revenuelineitems_iden']";
	public static String clientNeeds = "//input[@id='s03_client_needs_id']";
	public static String sow = "//input[@id='s03_sow_drafted']"; 
	public String uniqueBusValue(String HML) { return "//input[@id='s03_unique_bus_value' and @value='" + HML + "']";};
	public static String uniqueBusValueDescId = "s03_unique_bus_descr_ac";
	public static String uniqueBusValueDesc = "//input[@id='s03_unique_bus_descr_ac']";
	public static String uniqueBusValueDescBTN = "//button[@id='s03_unique_bus_descr_dp_btn']";
	public static String IVOtherComments = "//textarea[@id='s03_comments']";
	public String uniqueBusValueDescDropDown(String description) {return "//li[@data-text='"+description+"']";};
	
	public static String clientFinancingNeeds = "//input[@id='s03_igf_client_fin_id']";
	public static String creditCheckComplete = "//input[@id='s03_igf_credit_check']";
	public static String igfBusinessValue = "//select[@id='s03_igf_bus_val_descr']";
	public static String financingProposalDrafted = "//input[@id='s03_igf_fin_prop_draft']";
	public static String financingCompetition = "//input[@id='s03_igf_fin_competition']";
	public static String igfDirectFinancing = "//select[@id='s03_igf_direct_fin_rel']";
	public static String IVIGFOtherComments = "//textarea[@id='s03_igf_comments']";
	
	public static String IVEditLineItem = "//div[@id='subpanel_ibm_revenuelineitems_iden']//a[contains(text(),'edit')]";
	public static String IVLICompetition = "//input[@id='wp_s03_competition_ac']";
	public String IVLICompetitionDropDown(String description) {return "//li[@data-text='"+description+"']";};
	public static String IVLIOtherComments = "//textarea[@id='wp_s03_comments']";
	public static String IVLISave = "//input[@name='save_button']";
	
	//VQ
	public static String expandVQ = "//a[@id='exp_ibm_revenuelineitems_val']";
	public static String collapseVQ = "//a[@id='col_ibm_revenuelineitems_val']";
	public static String roiAnalysis = "//input[@id='s04_roi_tco_analysis']";
	public static String rfp = "//input[@id='s04_rfp_released']";
	public static String clientDecision = "//input[@id='s04_perf_criteria_conf']";
	public static String initialBenefits = "//input[@id='s04_init_benefits']";
	public String reasonToAct(String HML) { return "//input[@id='s04_reason_to_act' and @value='" + HML + "']";};
	public static String reasonToAct = "//input[@id='s04_reason_to_act_descr_ac']";
	public static String reasonToActBTN = "//button[@id='s04_reason_to_act_descr_dp_btn']";
	public String reasonToActDropDown(String description) {return "//li[@data-text='"+description+"']";};
	public String accessToPower(String HML) { return "//input[@id='s04_access_to_power' and @value='" + HML + "']";};
	public static String IBMreviewers = "//button[@id='btn_s04_reviewers_approvers']";
	public static String reviewDate = "//input[@id='s04_review_appr_date']";
	public static String reviewDateTD = "//input[@id='s04_review_appr_date']/../..";
	public static String CVDMDate = "//input[@id='s04_cvdm_sess_date']";
	public static String progressionDate = "//input[@id='s04_prog_call_date']";
	public static String VQOtherComments = "//textarea[@id='s04_comments']";
	
	public static String financingDecision = "//input[@id='s04_igf_fin_decision']";
	public static String igfIBMReviewers = "//button[@id='btn_s04_igf_reviewers_approvers']";
	public static String igfReviewDate = "//input[@id='s04_igf_review_appr_date']";
	public static String VQIGFOtherComments = "//textarea[@id='s04_igf_comments']";
	
	public static String VQEditLineItem = "//div[@id='subpanel_ibm_revenuelineitems_val']//a[contains(text(),'edit')]";
	public static String VQLIClientApprover = "//input[@id='wp_s04_final_approver']";
	public static String VQLIOtherComments = "//textarea[@id='wp_s04_comments']";
	public static String VQLISave = "//input[@name='save_button']";
	
	//QGCP
	public static String expandQGCP = "//a[@id='exp_ibm_revenuelineitems_qua']";
	public static String collapseQGCP = "//a[@id='col_ibm_revenuelineitems_qua']";
	public static String propDeliveryDate = "//input[@id='s05_prop_delivery_date']";
	public static String clientBudget = "//input[@id='s05_budget_secure']";
	public static String priceAgreement = "//input[@id='s05_price_agreement']";
	public static String solutionAssurance = "//input[@id='s05_solution_assur_itar']";
	public static String tcConfirmed = "//input[@id='s05_pricing_secured']";
	public static String teamEngaged = "//input[@id='s05_team_engaged']";
	public static String QGCPOtherComments = "//textarea[@id='s05_comments']";
	
	public static String igfProposalDelivery = "//select[@id='s05_igf_grop_delivery']";
	public static String igfProposalDeliveryDate = "//input[@id='s05_igf_grop_delivery_date']";
	public static String agreementReached = "//input[@id='s05_igf_agreed_term']";
	public static String QGCPIGFOtherComments = "//textarea[@id='s05_igf_comments']";
	
	public static String QGCPEditLineItem = "//div[@id='subpanel_ibm_revenuelineitems_qua']//a[contains(text(),'edit')]";
	public static String QGCPConfTech = "//input[@id='wp_s05_conf_tech_sol' and @value='Scheduled']";
	public static String QGCPReadinessPlan = "//input[@id='wp_s05_read_plan']";
	public static String QGCPcrossDependency = "//input[@id='wp_s05_cross_deps' and @value='None']";
	public static String QGCPResources = "//input[@id='wp_s05_res_skill_conf']";
	public static String QGCPLIOtherComments = "//textarea[@id='wp_s05_comments']";
	public static String QGCPLISave = "//input[@name='save_button']";
	
	public static String QCPLIClientIssues = "//textarea[@id='wp_s05_issues_reasons_to_act']";
	public static String QCPLICompetitiveStatus = "//select[@id='wp_s05_competitive_status']";
	public static String QCPLIProposalStage = "//select[@id='wp_s05_proposal_stage']";
	public static String QCPLIScope = "//select[@id='wp_s05_scope']";
	public static String QCPLIPricing = "//select[@id='wp_s05_pricing']";
	public static String QCPLIContract = "//select[@id='wp_s05_contract_tcs']";
	public static String QCPLItargetSignDate = "//select[@id='wp_s05_target_sign_date']";
	
	
	//CAW
	public static String expandCAW = "//a[@id='exp_ibm_revenuelineitems_con']";
	public static String collapseCAW = "//a[@id='col_ibm_revenuelineitems_con']";
	public static String documentation = "//input[@id='s06_signed_contract']";
	public static String CAWOtherComments = "//textarea[@id='s06_comments']";
	
	public static String igfDocumentation = "//input[@id='s06_igf_signed_contract']";
	public static String CAWIGFOtherComments = "//textarea[@id='s06_igf_comments']";
	
	public static String CAWEditLineItem = "//div[@id='subpanel_ibm_revenuelineitems_con']//a[contains(text(),'edit')]";
	public static String liDocumentation = "//input[@id='wp_s06_signed_contract']";
	public static String CAWLIOtherComments = "//textarea[@id='wp_s06_comments']";
	public static String CAWLISave = "//input[@name='save_button']";
	
	//Progress indicator
	
	public String getCompletedPercentage(String selector){
		sleep(10);
		Matcher matcher = patternCompleted.matcher(getObjectText(selector));
		if (matcher.find())
		{
	     return matcher.group(3);
		}
		Assert.assertTrue(false, "No progress found");
		return null;
	}
	
	public String getCompletedNumber(String selector){
		Matcher matcher = patternCompleted.matcher(getObjectText(selector));
		if (matcher.find())
		{
	     return matcher.group(1);
		}
		Assert.assertTrue(false, "No progress found");
		return null;
	}
	
	public String getCompletedTotal(String selector){
		Matcher matcher = patternCompleted.matcher(getObjectText(selector));
		if (matcher.find())
		{
	     return matcher.group(2);
		}
		Assert.assertTrue(false, "No progress found");
		return null;
	}
	
	public void checkProgress(String selector, String completedNumber, String completedTotal, String completedPercentage){
		Assert.assertEquals(getCompletedNumber(selector), completedNumber, "Expected("+completedNumber+") does not equal actual("+getCompletedNumber(selector)+")");
		Assert.assertEquals(getCompletedTotal(selector), completedTotal, "Expected("+completedTotal+") does not equal actual("+getCompletedTotal(selector)+")");
		Assert.assertEquals(getCompletedPercentage(selector), completedPercentage, "Expected("+completedPercentage+") does not equal actual("+getCompletedPercentage(selector)+")");
	}
	
 
	//Summary
	public void addIBMExecSponsor(String userName){
		isPresent(IBMExecSponsor);
		click(IBMExecSponsor);
		UserSelectPopup userSelect = new UserSelectPopup(exec);
		getPopUp();
		userSelect.searchForUser(userName);
		userSelect.selectResultUsingName(userName);
	}
	
	public void addIBMBrandSponsor(String userName){
		isPresent(IBMBrandSponsor);
		click(IBMBrandSponsor);
		UserSelectPopup userSelect = new UserSelectPopup(exec);
		getPopUp();
		userSelect.searchForUser(userName);
		userSelect.selectResultUsingName(userName);
	}
	
	public void addIBMSDSponsor(String userName){
		isPresent(IBMSDSponsor);
		click(IBMSDSponsor);
		UserSelectPopup userSelect = new UserSelectPopup(exec);
		getPopUp();
		userSelect.searchForUser(userName);
		userSelect.selectResultUsingName(userName);
	}
	
	public void setOwnerIGF(Boolean ownerIGF){
		if (ownerIGF) {
			click(opptyOwnerIncludeIGF("Yes"));
		}
		else {
			click(opptyOwnerIncludeIGF("No"));
		}
	}
	
	public void setInternationalDeal(Boolean international){
		if (international) {
			click(internationalDeal("Yes"));
		}
		else {
			click(internationalDeal("No"));
		}
	}
	
	public void setIGFOwnerIGF(Boolean ownerIGF){
		if (ownerIGF) {
			click(igfOwnerIncludeIGF("Yes"));
		}
		else {
			click(igfOwnerIncludeIGF("No"));
		}
	}
	
	public void addIBMExec(String IBMExec){
		type(IBMExecutive, IBMExec);
	}
	
	//Save buttons
	public void saveSummary(){
		isPresent(summarySave);
		scrollElementToMiddleOfBrowser(summarySave);
		click(summarySave);
			if(!isSaved(status)){
				log.info("saved message not found but proceeding anyway");
			}
			else {
				log.info("saved message found");
			}

		isPageLoaded();
	}
	
	public void saveIV(){
		isPresent(saveIV);
		scrollElementToMiddleOfBrowser(saveIV);
		click(saveIV);
		if(!isSaved(status)){
			Assert.assertTrue(false, "Win Plan has not been saved successfully");
		}
		isPageLoaded();
	}
	
	public void saveVQ(){
		click(saveVQ);
		if(!isSaved(status)){
			Assert.assertTrue(false, "Win Plan has not been saved successfully");
		}
		isPageLoaded();
	}

	public void saveQGCP(){
		click(saveQGCP);
		if(!isSaved(status)){
			Assert.assertTrue(false, "Win Plan has not been saved successfully");
		}
		isPageLoaded();
	}
	
	public void saveCAW(){
		click(saveCAW);
		if(!isSaved(status)){
			Assert.assertTrue(false, "Win Plan has not been saved successfully");
		}
		isPageLoaded();
	}
	
	//Expand and collapse
	public void openIVSection(){
		click(expandIV);
	}
	
	public void closeIVSection(){
		click(collapseIV);
	}
	
	public void openVQSection(){
		click(expandVQ);
	}
	
	public void closeVQSection(){
		click(collapseVQ);
	}
	
	public void openQGCPSection(){
		click(expandQGCP);
	}
	
	public void closeQGCPSection(){
		click(collapseQGCP);
	}
	
	public void openCAWSection(){
		scrollElementToMiddleOfBrowser(expandCAW);
		click(expandCAW);
	}
	
	public void closeCAWSection(){
		click(collapseCAW);
	}
	
	//IV
	public void setClientNeeds(){
		isPresent(clientNeeds);
		scrollElementToMiddleOfBrowser(clientNeeds);
		isPresent(clientNeeds);
		click(clientNeeds);
		isPageLoaded();
		Assert.assertEquals(getObjectAttribute(clientNeeds+"/../..//div/div[1]", "class"), questionCompleteClass);
	}
	
	public void setSOW(){
		isPresent(sow);
		scrollElementToMiddleOfBrowser(sow);
		click(sow);
		Assert.assertEquals(getObjectAttribute(sow+"/../..//div/div[1]", "class"), questionCompleteClass);
	}
	
	/**
	 * Three possible parameters high, medium and low
	 * @param HML
	 */
	public void setUniqueBusValue(String HML){
		click(uniqueBusValue(HML));
		Assert.assertEquals(getObjectAttribute(uniqueBusValue(HML)+"/../..//div/div[1]", "class"), questionCompleteClass);
	}
	
	public void setUniqueBusValueDesc(String description){
		type(uniqueBusValueDesc, description);
		click(uniqueBusValueDescBTN);
		
		for (int i = 0; i < 5; i++) {
			sleep(2);
			if (isPresent(uniqueBusValueDescDropDown(description))) {
				click(uniqueBusValueDescDropDown(description));
				i+=5;
			}	
		}
		click(uniqueBusValueDescBTN);
//		for (int i = 0; i < 5; i++) {
//			sleep(2);
//			if (isPresent(uniqueBusValueDescDropDown("Business scheme - political decision relationship"))) {
//				click(uniqueBusValueDescDropDown("Business scheme - political decision relationship"));
//				i+=5;
//			}	
//		}
		sleep(3);	
		click(uniqueBusValueDesc);
		click(IVOtherComments);
		click(uniqueBusValueDesc);
	}
	
	public void populateIVOtherComments(String text){
		type(IVOtherComments, text);
	}
	
	public void setClientFinancing(){
		scrollElementToMiddleOfBrowser(clientFinancingNeeds);
		click(clientFinancingNeeds);
		Assert.assertEquals(getObjectAttribute(clientFinancingNeeds+"/../..//div/div[1]", "class"), questionCompleteClass);
	}
	
	public void setCreditCheck(){
		click(creditCheckComplete);
		Assert.assertEquals(getObjectAttribute(creditCheckComplete+"/../..//div/div[1]", "class"), questionCompleteClass);
	}
	
	public void selectIGFBusinessValue(String option){
		select(igfBusinessValue, option);
		Assert.assertEquals(getObjectAttribute(igfBusinessValue+"/../..//div/div[1]", "class"), questionCompleteClass);
	}
	
	public void setFinancingProposal(){
		click(financingProposalDrafted);
		Assert.assertEquals(getObjectAttribute(financingProposalDrafted+"/../..//div/div[1]", "class"), questionCompleteClass);
	}
	public void setFinancingCompetition(String competition){
		type(financingCompetition, competition);
		//Assert.assertEquals(getObjectAttribute(financingCompetition+"/../..//div/div[1]", "class"), questionCompleteClass);
	}
	
	public void selectIGFDirectFinancing(String option){
		select(igfDirectFinancing, option);
		Assert.assertEquals(getObjectAttribute(igfDirectFinancing+"/../..//div/div[1]", "class"), questionCompleteClass);
	}
	
	public void populateIVIGFOtherComments(String text){
		type(IVIGFOtherComments, text);
	}

	/**
	 * 
	 */
	public void openIVEditLineItem() {
		click(IVEditLineItem);
	}
	
	public void setIVLICompetition(String competition){
		type(IVLICompetition, competition);
		click(IVLICompetitionDropDown(competition));
	}

	/**
	 * @param string
	 */
	public void populateIVLineItemOtherComments(String string) {
		type(IVLIOtherComments, string);	
	}

	/**
	 * 
	 */
	public void saveIVLineItem() {
		click(IVLISave);
		if(!isSaved(status)){
			Assert.assertTrue(false, "Win Plan IV LI has not been saved successfully");
		}	
	}
	
	//VQ

	/**
	 * 
	 */
	public void populateVQOpptyOwnerSection(String userName) {
		scrollElementToMiddleOfBrowser(roiAnalysis);
		click(roiAnalysis);
		Assert.assertEquals(getObjectAttribute(roiAnalysis+"/../..//div/div[1]", "class"), questionCompleteClass);
		
		click(rfp);
		Assert.assertEquals(getObjectAttribute(rfp+"/../..//div/div[1]", "class"), questionCompleteClass);
		
		click(clientDecision);
		Assert.assertEquals(getObjectAttribute(clientDecision+"/../..//div/div[1]", "class"), questionCompleteClass);
		
		click(initialBenefits);
		Assert.assertEquals(getObjectAttribute(initialBenefits+"/../..//div/div[1]", "class"), questionCompleteClass);
		
		click(reasonToAct("Good/Strong"));
		Assert.assertEquals(getObjectAttribute(reasonToAct("Good/Strong")+"/../..//div/div[1]", "class"), questionCompleteClass);
		
		type(reasonToAct, "End of lease");
		sleep(2);
		scrollElementToMiddleOfBrowser(reasonToActBTN);
		click(reasonToActBTN);
		sleep(2);
		click(reasonToActDropDown("End of lease"));
		sleep(2);
		
		click(accessToPower("Good/Strong"));
		Assert.assertEquals(getObjectAttribute(accessToPower("Good/Strong")+"/../..//div/div[1]", "class"), questionCompleteClass);
		
		click(IBMreviewers);
		UserSelectPopup userSelect = new UserSelectPopup(exec);
		getPopUp();
		userSelect.searchForUser(userName);
		userSelect.selectResultUsingName(userName);
	
		click("//span[@id='s04_review_appr_date_trigger']");
		sleep(2);
		click("//a[@class='calnavright']");
		click("//td[@id='s04_review_appr_date_trigger_div_t_cell12']");
		Assert.assertEquals(getObjectAttribute(reviewDate+"/../..//div/div[1]", "class"), questionCompleteClass);
		
		click("//span[@id='s04_cvdm_sess_date_trigger']");
		sleep(2);
		click("//a[@class='calnavright']");
		click("//td[@id='s04_cvdm_sess_date_trigger_div_t_cell17']");
		Assert.assertEquals(getObjectAttribute(CVDMDate+"/../..//div/div[1]", "class"), questionCompleteClass);
		
		click("//span[@id='s04_prog_call_date_trigger']");
		sleep(2);
		click("//a[@class='calnavright']");
		click("//td[@id='s04_prog_call_date_trigger_div_t_cell17']");
		Assert.assertEquals(getObjectAttribute(progressionDate+"/../..//div/div[1]", "class"), questionCompleteClass);
		
		type(VQOtherComments, "VQOtherComments");
	}

	/**
	 * 
	 */
	public void populateVQIGFOwnerSection(String userName) {
		click(financingDecision);
		Assert.assertEquals(getObjectAttribute(financingDecision+"/../..//div/div[1]", "class"), questionCompleteClass);
		
		click(igfIBMReviewers);
		UserSelectPopup userSelect = new UserSelectPopup(exec);
		getPopUp();
		userSelect.searchForUser(userName);
		userSelect.selectResultUsingName(userName);
		
		click("//span[@id='s04_igf_review_appr_date_trigger']");
		sleep(2);		
		click("//div[@id='container_s04_igf_review_appr_date_trigger']//a[contains(text(),'Today')]");
		click(closeDateBox);
		sleep(2);
		Assert.assertEquals(getObjectAttribute(igfReviewDate+"/../..//div/div[1]", "class"), questionCompleteClass);
		
		type(VQIGFOtherComments, "VQIGFOtherComments");
	}

	/**
	 * 
	 */
	public void populateVQLI() {
		sleep(5);
		this.scrollElementToMiddleOfBrowser(VQEditLineItem);
		sleep(2);
		click(VQEditLineItem);
		type(VQLIClientApprover, "Client Approver");
		type(VQLIOtherComments, "VQLIOtherComments");
	}

	public void saveVQLineItem() {
		click(VQLISave);
		if(!isSaved(status)){
			Assert.assertTrue(false, "Win Plan VQ LI has not been saved successfully");
		}	
	}
	
	
	//QGCP
	public void populateQGCP(){
		click("//span[@id='s05_prop_delivery_date_trigger']");
		sleep(2);		
		click("//div[@id='container_s05_prop_delivery_date_trigger']//a[contains(text(),'Today')]");
		click(closeDateBox);
		Assert.assertEquals(getObjectAttribute(propDeliveryDate+"/../..//div/div[1]", "class"), questionCompleteClass);
		
		scrollElementToMiddleOfBrowser(clientBudget);
		click(clientBudget);
		sleep(5);
		Assert.assertEquals(getObjectAttribute(clientBudget+"/../..//div/div[1]", "class"), questionCompleteClass);
		
		click(priceAgreement);
		Assert.assertEquals(getObjectAttribute(priceAgreement+"/../..//div/div[1]", "class"), questionCompleteClass);
		
		click(solutionAssurance);
		Assert.assertEquals(getObjectAttribute(solutionAssurance+"/../..//div/div[1]", "class"), questionCompleteClass);
		
		click(tcConfirmed);
		Assert.assertEquals(getObjectAttribute(tcConfirmed+"/../..//div/div[1]", "class"), questionCompleteClass);
		
		click(teamEngaged);
		Assert.assertEquals(getObjectAttribute(teamEngaged+"/../..//div/div[1]", "class"), questionCompleteClass);
		
		type(QGCPOtherComments, "QGCPOtherComments");
	}
	
	public void populateIGFQGCP(){
		select(igfProposalDelivery, "Delivered by IGF to client");
		Assert.assertEquals(getObjectAttribute(igfProposalDelivery+"/../..//div/div[1]", "class"), questionCompleteClass);

		click("//span[@id='s05_igf_grop_delivery_date_trigger']");
		sleep(2);		
		click("//div[@id='container_s05_igf_grop_delivery_date_trigger']//a[contains(text(),'Today')]");
		click(closeDateBox);
		sleep(2);
		Assert.assertEquals(getObjectAttribute(igfProposalDeliveryDate+"/../..//div/div[1]", "class"), questionCompleteClass);

		scrollElementToMiddleOfBrowser(agreementReached);
		click(agreementReached);
		Assert.assertEquals(getObjectAttribute(agreementReached+"/../..//div/div[1]", "class"), questionCompleteClass);

		type(QGCPIGFOtherComments, "QGCPIGFOtherComments");
	}
	
	public void populateQGCPLI(){
		sleep(4);
		this.scrollElementToMiddleOfBrowser(QGCPEditLineItem);
		click(QGCPEditLineItem);
		sleep(5);
		click(QGCPConfTech);
		
		sleep(2);
		click("//span[@id='wp_s05_next_sales_date_trigger']");
		sleep(2);		
		click("//div[@id='container_wp_s05_next_sales_date_trigger']//a[contains(text(),'Today')]");
		click(closeDateBox);
		sleep(2);
		
		click(QGCPReadinessPlan);
		
		click("//span[@id='wp_s05_read_plan_date_trigger']");
		sleep(2);		
		click("//div[@id='container_wp_s05_read_plan_date_trigger']//a[contains(text(),'Today')]");
		click(closeDateBox);
		sleep(2);
		
		click(QGCPcrossDependency);
		click(QGCPResources);
		type(QGCPLIOtherComments, "QGCPLIOtherComments");
	}
	
	public void populateSOQGCPLI(){
		sleep(4);
		this.scrollElementToMiddleOfBrowser(QGCPEditLineItem);
		click(QGCPEditLineItem);
		sleep(5);
		type(QCPLIClientIssues, "QCPLIClientIssues");
		select(QCPLICompetitiveStatus, "Officially awarded");
		select(QCPLIProposalStage, "BAFO / Conditional Agreement");
		select(QCPLIScope, "Settled");
		select(QCPLIPricing, "Final");
		select(QCPLIContract, "Settled");
		select(QCPLItargetSignDate, "Agreed and scheduled");
		type(QGCPLIOtherComments, "QCPLI Other Comments");
		click(QGCPLISave);	
	}
	
	public void saveQGCPLineItem() {
		click(QGCPLISave);
		if(!isSaved(status)){
			Assert.assertTrue(false, "Win Plan QGCP LI has not been saved successfully");
		}	
	}
	
	public void populateCAW(){
		isPageLoaded();
		scrollElementToMiddleOfBrowser(documentation);
		click(documentation);
		Assert.assertEquals(getObjectAttribute(documentation+"/../..//div/div[1]", "class"), questionCompleteClass);
		
		type(CAWOtherComments, "CAWOtherComments");
	}
	
	public void populateIGFCAW(){
		click(igfDocumentation);
		Assert.assertEquals(getObjectAttribute(igfDocumentation+"/../..//div/div[1]", "class"), questionCompleteClass);

		type(CAWIGFOtherComments, "CAWIGFOtherComments");
	}
	
	public void populateLICAW(){
		click(CAWEditLineItem);
		click(liDocumentation);
		type(CAWLIOtherComments, "CAWLIOtherComments");
	}
	
	public void saveCAWLineItem() {
		click(CAWLISave);
		if(!isSaved(status)){
			Assert.assertTrue(false, "Win Plan CAW LI has not been saved successfully");
		}	
	}

	/**
	 * 
	 */
	public OpportunityDetailPage navigateToOpportunityOverview() {
		scrollToTopOfPage();
		click(updatesTab);
		return new OpportunityDetailPage(exec);
	}

}
