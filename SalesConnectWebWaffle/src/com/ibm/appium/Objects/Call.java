package com.ibm.appium.Objects;

import com.ibm.appium.common.GC;

public class Call {

	// Call form fields
	public String sSubject = "";
	public String sSubjectUPD = "";
	public String sCallDate = "";
	public String sDurationMinutes = "";
	public String sCallStatus = "";
	public String sCallType = "";
	public String sAssignedTo = "";
	public String sSummaryOfCall = "";

	public void populate() {
		sSubject = "my call " + System.currentTimeMillis();
		sSubjectUPD = "UPD";
		sCallType = GC.gsCallFaceToFace;
		// sCallStatus = GC.gsCallHeld;
		sCallStatus = GC.gsCallNotHeld;
		// sDurationMinutes = GC.gsDuration30;
		sDurationMinutes = GC.gsDuration45;
		sSummaryOfCall = "This is a call sumary section. the summary of the call goes here... "
				+ System.currentTimeMillis();
	}
	
	public void updateDetails(){
		sSubject += sSubjectUPD;
	}

	public String getsSubject() {
		return sSubject;
	}

	public void setsSubject(String sSubject) {
		this.sSubject = sSubject;
	}

	public String getsSubjectUPD() {
		return sSubjectUPD;
	}

	public void setsSubjectUPD(String sSubjectUPD) {
		this.sSubjectUPD = sSubjectUPD;
	}

	public String getsCallDate() {
		return sCallDate;
	}

	public void setsCallDate(String sCallDate) {
		this.sCallDate = sCallDate;
	}

	public String getsDurationMinutes() {
		return sDurationMinutes;
	}

	public void setsDurationMinutes(String sDurationMinutes) {
		this.sDurationMinutes = sDurationMinutes;
	}

	public String getsCallStatus() {
		return sCallStatus;
	}

	public void setsCallStatus(String sCallStatus) {
		this.sCallStatus = sCallStatus;
	}

	public String getsCallType() {
		return sCallType;
	}

	public void setsCallType(String sCallType) {
		this.sCallType = sCallType;
	}

	public String getsAssignedTo() {
		return sAssignedTo;
	}

	public void setsAssignedTo(String sAssignedTo) {
		this.sAssignedTo = sAssignedTo;
	}

	public String getsSummaryOfCall() {
		return sSummaryOfCall;
	}

	public void setsSummaryOfCall(String sSummaryOfCall) {
		this.sSummaryOfCall = sSummaryOfCall;
	}
}
