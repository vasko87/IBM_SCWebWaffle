/**
 * 
 */
package com.ibm.salesconnect.PoolHandling.Opportunity;

import java.security.InvalidParameterException;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author timlehane
 * @date May 9, 2013
 */
public class PoolOpportunity {

	private static final Logger log = LoggerFactory.getLogger(PoolOpportunity.class);


	/** The OpportunityNumber. */
	private String OpportunityNumber;

	/** Class hashcode */
	private int HashCode = 0;

	private HashMap<String, String> attributes;

	// client constructor

	public PoolOpportunity(String OpportunityNumberIn) {

		this.OpportunityNumber = OpportunityNumberIn;
		setAttribute("OpportunityNumber", this.OpportunityNumber);
	}

	public PoolOpportunity(HashMap<String, String> map) {

		this.attributes = map;
		this.OpportunityNumber = getAttribute("OpportunityNumber");
	}

	public String getAttribute(String attribute) {

		String result = this.attributes.get(attribute);
		if (result == null) {
			log.error("The attribute '" + attribute + "' does not correspond to an attribute of the opportunity. Check that the attribute corresponds to a header in your opportunities CSV.");
			throw new InvalidParameterException("The attribute '" + attribute
					+ "' does not correspond to an attribute of the opportunity. Check that the attribute corresponds to a header in your opportunity CSV.");
		} else if (result == "") {
			log.warn("Request for attribute '" + attribute + "' is returning an empty string for client " + OpportunityNumber + ". Verify that your opportunity CSV is populated correctly.");
		}
		return result;
	}

	public String setAttribute(String attribute, String value) {

		return this.attributes.put(attribute, value);
	}

	// client methods
	/**
	 * Gets the OpportunityNumber.
	 * 
	 * @return the OpportunityNumber
	 */
	public String getOpportunityNumber() {

		return OpportunityNumber;
	}

	/**
	 * Sets the OpportunityNumber.
	 * 
	 * @param OpportunityNumber
	 *            the OpportunityNumber to set
	 */
	public void setOpportunityNumber(String OpportunityNumberIN) {

		OpportunityNumber = OpportunityNumberIN;
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
