/**
 * 
 */
package com.ibm.salesconnect.objects;


/**
 * @author Veena H
 * @date March 9th, 2015
 */
public class Lead {

	public String sFirstName="";
	public String sLastName="";
	public String sEmailId="";
	public String sTitle="";
	public String sMobilePhone="";
	public String sOfficePhone="";
	public String sFullName="";
	public String sLeadSource="";
	public String sClientBN="";
	public String sIndustrySolutionUnit="";
	public String sIndustry="";
	public String sIndustryClass="";
	public String sISIC="";
	public String sGBQuad="";
	public String sOppDesc="";
	public String sSource;
	public String sOppSource="";
	public String sStatus="";
	public String sPrimaryAddressCountry="";
	public String sStatusDetail="";
	public String sAssignedUserID = "";
	public String sConvertedStatus="";
	public String sBillingAddress="";
	public String sBillingAddressCity="";
	public String sBillingAddressPincode="";
	public String sBillingAddressCountry="";

	public void populate() {

		sFirstName = "BVT FN" + (int) Math.round((Math.random()) * 10000);		
		sLastName = "LN";
		sEmailId = "Poughkeepsie@gmail.com";
		sTitle = "United States";
		sMobilePhone = "1234567890";
		sFullName=sFirstName+" "+sLastName;
		sLeadSource="Campaign";
		sClientBN=sFirstName+" "+sLastName;
		sIndustrySolutionUnit="Banking";
	    sIndustry="Banking";
		sIndustryClass="Banking - Other";
		sISIC="Central Banking";
		sGBQuad="Activation";
		sOppDesc="description for opportunity for the client"+" "+sClientBN;
		sOppSource="Account Plan";
		sStatus="New";
		sConvertedStatus="Converted";
		sBillingAddress="Billing adress street";
		sBillingAddressCity="Bangalore";
		sBillingAddressPincode="560026";
		sBillingAddressCountry="India";
	}

	public void setEmailID(String email){
		sEmailId=email;
	}
	
}
