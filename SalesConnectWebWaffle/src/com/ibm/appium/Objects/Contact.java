package com.ibm.appium.Objects;

import com.ibm.appium.common.GC;

public class Contact {

	// Contact Form Fields
	public String sSalutation = "";
	public String sFirstName = "MyFirst ";
	public String sLastName = "MyLast ";
	public String sPreferredName = "";
	public String sJobTitle = "";
	public String sClientName = "";
	public String sPrimaryAddress = "";
	public String sOfficePhone = "";
	public String sMobile = "";
	public String sEmailAddress = "";
	public String sContactStatus = "";

	// Update
	public String sFirstNameUPD = "";
	public String sLastNameUPD = "";

	// Address sub Form Fields
	public String sCountryArea = "";
	public String sStreetAddress = "";
	public String sCity = "";
	public String sStateProvince = "";
	public String sPostalCode = "";

	public void populate() {

		sSalutation = "Mr";
		sFirstName = sFirstName + System.currentTimeMillis();
		sLastName = sLastName + System.currentTimeMillis();
		sCountryArea = GC.gsCountryIE;
		sPreferredName = "Firsty";
		sJobTitle = "Team Lead";

		sStreetAddress = "123 Fake Street";
		sCity = "Fake Town";
		sPostalCode = "15";
		sOfficePhone = "01 8888888";
		sMobile = "087 8888888";
		sEmailAddress = "last.first@fake.ie";

		sFirstNameUPD = " - Update";
		sLastNameUPD = " - Update";
	}
	//If successful edit, update the details of the contact
	public void updateDetails(){
		sFirstName += sFirstNameUPD;
		sLastName += sLastNameUPD;
	}
	
	// Salutation
	public String getsSalutation() {
		return sSalutation;
	}

	public void setsSalutation(String sSalutation) {
		this.sSalutation = sSalutation;
	}

	// First Name
	public String getsFirstName() {
		return sFirstName;
	}

	public void setsFirstName(String sFirstName) {
		this.sFirstName = sFirstName;
	}

	// Last Name
	public String getsLastName() {
		return sLastName;
	}

	public void setsLastName(String sLastName) {
		this.sLastName = sLastName;
	}

	// Prefered Name
	public String getsPreferredName() {
		return sPreferredName;
	}

	public void setsPreferredName(String sPreferredName) {
		this.sPreferredName = sPreferredName;
	}

	//Full name
		public String getsFullName() {
			if(sPreferredName.length() > 0)
			  return sFirstName + " (" + sPreferredName + ") " + sLastName;
			else
			  return sFirstName + " " + sLastName;
		}
	
	// Job Title
	public String getsJobTitle() {
		return sJobTitle;
	}

	public void setsJobTitle(String sJobTitle) {
		this.sJobTitle = sJobTitle;
	}

	// Primary Address field in main contact form
	public String getsPrimaryAddress() {
		return sPrimaryAddress;
	}

	public void setsPrimaryAddress(String sPrimaryAddress) {
		this.sPrimaryAddress = sPrimaryAddress;
	}

	// Address
	public String getsCountryArea() {
		return sCountryArea;
	}

	public void setsCountryArea(String sCountryArea) {
		this.sCountryArea = sCountryArea;
	}

	public String getsStreetAddress() {
		return sStreetAddress;
	}

	public void setsStreetAddress(String sStreetAddress) {
		this.sStreetAddress = sStreetAddress;
	}

	public String getsCity() {
		return sCity;
	}

	public void setsCity(String sCity) {
		this.sCity = sCity;
	}

	public String getsStateProvince() {
		return sStateProvince;
	}

	public void setsStateProvince(String sStateProvince) {
		this.sStateProvince = sStateProvince;
	}

	public String getsPostalCode() {
		return sPostalCode;
	}

	public void setsPostalCode(String sPostalCode) {
		this.sPostalCode = sPostalCode;
	}

	public String getsOfficePhone() {
		return sOfficePhone;
	}

	public void setsOfficePhone(String sOfficePhone) {
		this.sOfficePhone = sOfficePhone;
	}

	public String getsMobile() {
		return sMobile;
	}

	public void setsMobile(String sMobile) {
		this.sMobile = sMobile;
	}

	public String getsEmailAddress() {
		return sEmailAddress;
	}

	public void setsEmailAddress(String sEmailAddress) {
		this.sEmailAddress = sEmailAddress;
	}

	// Update
	public String getsLastNameUPD() {
		return sLastNameUPD;
	}

	public void setsLastNameUPD(String sLastNameUPD) {
		this.sLastNameUPD = sLastNameUPD;
	}

	public String getsFirstNameUPD() {
		return sFirstNameUPD;
	}

	public void setsFirstNameUPD(String sFirstNameUPD) {
		this.sFirstNameUPD = sFirstNameUPD;
	}	
}
