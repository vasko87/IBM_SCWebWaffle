package com.ibm.appium.Objects;

public class Note {

	// Note form fields
	public String sSubject = "";
	public String sSubjectUPD = "";
	public String sAssignedTo = "";
	public String sDescription = "";

	public void populate() {
		setsSubject("my note " + System.currentTimeMillis());
		setsSubjectUPD("UPD");
		setsDescription("This is a note Description "
				+ System.currentTimeMillis());
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
