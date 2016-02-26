/**
 * 
 */
package com.ibm.salesconnect.model.standard.LineItems;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.ibm.atmn.waffle.core.Executor;
import com.ibm.salesconnect.model.StandardCreatePage;
import com.ibm.salesconnect.model.partials.UserSelectPopup;
import com.ibm.salesconnect.model.standard.Opportunity.OpportunityDetailPage;
import com.ibm.salesconnect.objects.RevenueItem;

/**
 * @author timlehane
 * @date May 28, 2013
 */
public class EditLineItem extends StandardCreatePage {

	Logger log = LoggerFactory.getLogger(EditLineItem.class);
	
	/**
	 * @param exec
	 */
	public EditLineItem(Executor exec) {
		super(exec);
		Assert.assertTrue(isPageLoaded(), "Edit Line Item page has not loaded within 60 seconds");	
	}
	
	//Selectors
	public static String textField_Offering = "//div[@data-name='level_search']//span[@class='select2-chosen']";
	public static String textField_OfferingType = "//input[@id='level10-input']";
	public static String textField_SubBrand = "//input[@id='level15-input']";
	public String getOfferingPick(String sOffering) {return  "//ul[@role='listbox']/li[@role='option'][contains(@data-text,'"+sOffering+"')]";}
	public String getOfferingTypePick(String sOfferingType) {return  "//ul[@role='listbox']/li[@role='option'][contains(@data-text,'"+sOfferingType+"')]";}
	public String getOfferingSubBrandPick(String sOfferingSubBrand) {return  "//ul[@role='listbox']/li[@role='option'][contains(@data-text,'"+sOfferingSubBrand+"')]";}
	public static String textField_RevenueAmount = "//input[@id='revenue_amount']";
	public static String list_Probability = "//div[@data-name='probability']//span[@class='select2-chosen']";
	public static String textField_Owner = "//input[@id='assigned_user_name']";
	public static String textField_BillDate = "//input[@id='fcast_date_sign']";
	public static String getLink_Save = "//input[@id='SAVE_FOOTER' or @id='SAVE_WITH_SERVER_VALIDATION_FOOTER']";
	public static String roadMapStatus = "//select[@id='roadmap_status']";
	public static String contractType = "//select[@id='srv_work_type']";
	public static String contractChoice = "//option[@value='NEWNEW']";
	public static String competitor = "//div[@data-name='competitor']//input";
	public static String competitorSelection = "//input[@id='competitor_ac']/..//b";
	public static String getSaveHeader = "//input[@id='SAVE_HEADER' or @id='SAVE_WITH_SERVER_VALIDATION_HEADER']";
	public static String getButton_RLIOwner = "//button[@id='btn_assigned_user_name']";
	public static String alliancePartner = "//div[@id='alliance_partners_enum_1']/div/span/button";
	public String getAlliancePartnerPick(String sAlliancePartner) {return  "//ul[@role='listbox']/li[@role='option'][contains(@data-text,'"+sAlliancePartner+"')]";}
	public static String ibmFinancingLink = "//span[@id='detailpanel_2_img_show']";
	public static String getButton_IGFOwner = "//button[@id='btn_igf_owner_name']";
	public static String igfSalesStage = "//select[@id='igf_sales_stage']";
	public static String igfFinancedAmount = "//input[@id='igf_financed_amount']";
	public static String igfRoadMapStatus = "//select[@id='igf_roadmap_status']";
	public static String igfVolumesRecordingDate = "//input[@id='igf_volumes_recording_date']";
	public static String igfCloseDate = "//input[@id='igf_close_date']";
	public static String currencyType = "//div[@data-name='revenue_amount']//a//span[@class='select2-chosen']";
	public static String currencyAmount = "//div[@data-name='revenue_amount']//input[@name='revenue_amount']";
	public static String saveButton = "//a[@name='save_button' and not(@style='display: none;')]";
	public static String saveInLineButton = "//a[@name='inline-save']";
	public static String currecyField = ".//*[@id='content']//span/div[@class='currency-field ']";
	
	
	//Methods
	public void editLineItemInfo(RevenueItem rli){
		if (rli.sFindOffering.length()>0)
			select(textField_Offering, rli.sFindOffering);
		else {
			// RLI  Offering Type
			if (rli.sOfferingType.length() > 0) {
				type(textField_OfferingType,rli.sOfferingType);
				if(isPresent(getOfferingTypePick(rli.sOfferingType)))
				{
					click(getOfferingTypePick(rli.sOfferingType));
				}
			}
			
			// RLI  Sub Brand
			if (rli.sSubBrand.length() > 0) {
				click(textField_SubBrand);
				type(textField_SubBrand,rli.sSubBrand);
				click(getOfferingSubBrandPick(rli.sSubBrand));
				// If Road Map Status is empty due to the RLI Type Choosen then fill in the required extra fields
				if(roadMapStatus.isEmpty()){
					editRoadMapStatus();
				}
			}
		}
		
		
		// Competitor
		if (rli.sCompetitor.length() > 0)
			typeEnter(competitor, rli.sCompetitor);
		
		// RLI Revenue Amount
		if(rli.sRevenueAmount.length()>0){
			type(currencyAmount, rli.sRevenueAmount);
		}
		triggerChange( "css=input[name=\"revenue_amount\"]");
		
		// RLI Probability
		if(rli.sProbability.length()>0){
			select(list_Probability, rli.sProbability+"%");
		}

	}
	
	private UserSelectPopup openSelectOwner() {
		click(getButton_RLIOwner);
		getPopUp();
		return new UserSelectPopup(exec);
	}
	
	private UserSelectPopup openSelectIGFOwner() {
		click(getButton_IGFOwner);
		getPopUp();
		return new UserSelectPopup(exec);
	}
	
	public OpportunityDetailPage saveLineItem(){
		isPresent(saveButton);
		click(saveButton);

		
		return new OpportunityDetailPage(exec);
	}
	
	public void editRoadMapStatus(){
		select(roadMapStatus, "Stretch");
		select(contractType, "Expansion - GBS/GPS/SO");
		type(competitor, "Microsoft");
		sleep(1);
		click(competitorSelection);
		
	}
	
	public void editRoadMapStatus(RevenueItem rli){
		select(roadMapStatus, "Stretch");
		select(contractType, "Expansion - GBS/GPS/SO");
		typeEnter(competitor, "Microsoft");
		
		click(ibmFinancingLink);
		
		// RLI Owner
		if (rli.sOwner.length() > 0) {
			UserSelectPopup userSelectPopup = openSelectIGFOwner();
			userSelectPopup.searchForUser(rli.sOwner);
			userSelectPopup.selectResultUsingName(rli.sOwner);
		}
		
		// RLI  Volume Date
		if (rli.sIGFVolumesDate.length() > 0) {	
			type(igfVolumesRecordingDate,rli.sIGFVolumesDate);
		}
		
		// RLI Close Date
		if (rli.sIGFCloseDate.length() > 0) {	
			type(igfCloseDate,rli.sIGFCloseDate);
		}
		
		if (rli.sIGFRoadmapStatus.length() > 0 ) {
			select(igfRoadMapStatus, rli.sIGFRoadmapStatus);
		}
	}
	
	public void saveInlineLineItem(){
		click(saveInLineButton);
		waitToSave();
	}

	public void saveRoadMapStatus(){
		click(getSaveHeader);
	}
	
	public Map<String , String> editLineItemInfo1(RevenueItem rli){
		Map<String, String> sellerDeal = new HashMap<String, String>();
		
		if (rli.ssFindOffering.length()>0){
			select(textField_Offering, rli.ssFindOffering);
			sellerDeal.put("Offering/Solution", rli.ssFindOffering);
		}else{
			// RLI  Offering Type
			if (rli.sOfferingType.length() > 0){
				type(textField_OfferingType,rli.sOfferingType);
				if(isPresent(getOfferingTypePick(rli.sOfferingType))){
					click(getOfferingTypePick(rli.sOfferingType));
				}
			}			
			// RLI  Sub Brand
			if (rli.sSubBrand.length() > 0) {
				click(textField_SubBrand);
				type(textField_SubBrand,rli.sSubBrand);
				click(getOfferingSubBrandPick(rli.sSubBrand));
				// If Road Map Status is empty due to the RLI Type Choosen then fill in the required extra fields
				if(roadMapStatus.isEmpty()){
					editRoadMapStatus();
				}
			}
		}
		
		sleep(10);			
		// RLI Revenue Amount1
		if(rli.ssRevenueAmount.length()>0){
			type(currencyAmount, rli.ssRevenueAmount);
			//sellerDeal.put("Amount *", rli.ssRevenueAmount.substring(0, rli.ssRevenueAmount.length()-5));			
		}
		
		// RLI Probability
		if(rli.sProbability.length()>0){
			select(list_Probability, rli.sProbability+"%");
			sellerDeal.put("Probability *", rli.sProbability);
		}
		this.saveLineItem();
		
		//get amt from View Line Item page.
		String rliAmount = getObjectText(currecyField);
		System.out.println(rliAmount);
		sellerDeal.put("Amount *", rliAmount.substring(rliAmount.indexOf("(")+1, rliAmount.indexOf("U")-1));

		return sellerDeal;	 
	}

}
