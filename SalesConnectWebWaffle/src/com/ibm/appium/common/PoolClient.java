/**
 * 
 */
package com.ibm.appium.common;

import java.security.InvalidParameterException;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author timlehane
 * @date May 9, 2013
 */
public class PoolClient {

	private static final Logger log = LoggerFactory.getLogger(PoolClient.class);


	/** The CCMS_ID. */
	private String CCMS_ID;

	/** The client name. */
	private String clientName;

	/** Class hashcode */
	private int HashCode = 0;

	private HashMap<String, String> attributes;

	// client constructor

	public PoolClient(String CCMS_IDIn, String clientNameIn) {

		this.CCMS_ID = CCMS_IDIn;
		this.clientName = clientNameIn;
		setAttribute("CCMS_ID", this.CCMS_ID);
		setAttribute("clientName", this.clientName);

	}

	public PoolClient(HashMap<String, String> map) {

		this.attributes = map;
		this.CCMS_ID = getAttribute("CCMS_ID");
		this.clientName = getAttribute("clientName");
	}

	public String getAttribute(String attribute) {

		String result = this.attributes.get(attribute);
		if (result == null) {
			log.error("The attribute '" + attribute + "' does not correspond to an attribute of the client. Check that the attribute corresponds to a header in your clients CSV.");
			throw new InvalidParameterException("The attribute '" + attribute
					+ "' does not correspond to an attribute of the client. Check that the attribute corresponds to a header in your client CSV.");
		} else if (result == "") {
			log.warn("Request for attribute '" + attribute + "' is returning an empty string for client " + CCMS_ID + ". Verify that your client CSV is populated correctly.");
		}
		return result;
	}

	public String setAttribute(String attribute, String value) {

		return this.attributes.put(attribute, value);
	}

	// client methods
	/**
	 * Gets the CCMS_ID.
	 * 
	 * @return the CCMS_ID
	 */
	public String getCCMS_ID() {

		return CCMS_ID;
	}

	/**
	 * Sets the CCMS_ID.
	 * 
	 * @param CCMS_ID
	 *            the CCMS_ID to set
	 */
	public void setCCMS_ID(String CCMS_IDIN) {

		CCMS_ID = CCMS_IDIN;
	}

	/**
	 * Gets the client name.
	 * 
	 * @return the clientName
	 */
	public String getClientName() {

		return clientName;
	}

	/**
	 * Sets the client name.
	 * 
	 * @param clientName
	 *            the clientName to set
	 */
	public void setClientName(String clientNameIn) {

		clientName = clientNameIn;
	}

	boolean isCheckOutToken(Object checkOutToken) {

		return this.HashCode == checkOutToken.hashCode();
	}

	void checkOut(Object checkOutToken) {

		this.HashCode = checkOutToken.hashCode();
	}

	public void checkIn() {

		this.HashCode = 0;
	}

	public boolean isCheckedOut() {

		return this.HashCode != 0;
	}
}
