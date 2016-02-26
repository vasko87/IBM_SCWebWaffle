package com.ibm.appium.Objects;

import com.ibm.appium.common.GC;

public class Task {

	// Task form fields
	public String sName = "";
	public String sNameUPD = "";
	public String sDueDate = "";
	public String sPriority = "";
	public String sStatus = "";
	public String sTaskType = "";
	public String sAssignedTo = "";
	public String sDescription = "";

	public void populate() {
		setsName("my task " + System.currentTimeMillis());
		setsDescription("This is a description for a new Task "
				+ System.currentTimeMillis());

		// Priorities
		setsPriority(GC.gsTaskMedium);

		// Status
		setsStatus(GC.gsTaskInProgress);

		// Task Types
		// setsTaskType(GC.gsTaskTypeBlank);
		setsTaskType(GC.gsDealBoard2);

		// Updates
		setsNameUPD("UPD");
	}

	public void updateDetails(){
		sName += sNameUPD;
	}
	
	public String getsName() {
		return sName;
	}

	public void setsName(String sName) {
		this.sName = sName;
	}

	public String getsNameUPD() {
		return sNameUPD;
	}

	public void setsNameUPD(String sNameUPD) {
		this.sNameUPD = sNameUPD;
	}

	public String getsDueDate() {
		return sDueDate;
	}

	public void setsDueDate(String sDueDate) {
		this.sDueDate = sDueDate;
	}

	public String getsPriority() {
		return sPriority;
	}

	public void setsPriority(String sPriority) {
		this.sPriority = sPriority;
	}

	public String getsStatus() {
		return sStatus;
	}

	public void setsStatus(String sStatus) {
		this.sStatus = sStatus;
	}

	public String getsTaskType() {
		return sTaskType;
	}

	public void setsTaskType(String sTaskType) {
		this.sTaskType = sTaskType;
	}

	public String getsAssignedTo() {
		return sAssignedTo;
	}

	public void setsAssignedTo(String sAssignedTo) {
		this.sAssignedTo = sAssignedTo;
	}

	public String getsDescription() {
		return sDescription;
	}

	public void setsDescription(String sDescription) {
		this.sDescription = sDescription;
	}
}
