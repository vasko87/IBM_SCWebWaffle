/**
 * 
 */
package com.ibm.salesconnect.PoolHandling.Opportunity;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.atmn.waffle.utils.Utils;

/**
 * @author timlehane
 * @date May 10, 2013
 */
public class OpportunityGroup {
	private static final Logger log = LoggerFactory.getLogger(OpportunityGroup.class);

	private String groupName;

	private boolean checkOut = true;

	private long timeout = 10000;

	private volatile ArrayList<PoolOpportunity> pool;

	OpportunityGroup(ArrayList<PoolOpportunity> pool, String groupName, boolean checkOut, long timeout) {

		this.groupName = groupName;
		this.pool = pool;
		this.checkOut = checkOut;
		this.timeout = timeout;
	}

	PoolOpportunity getAvailableOpportunityFromPool(Object checkOutToken) {

		// assert that the pool has at least one entry
		assertPoolNotEmpty();

		// Get random client in list, if not available, get first available client in list.
		// If no available clients, wait up to timeout for user to become available.

		// Return an integer in the range 0-(pool.size()-1) inclusive
		int rand = new Random().nextInt(pool.size());

		PoolOpportunity poolOpportunity = this.pool.get(rand);

		// Check that the random client does not have their hashcode set. If so, wait for available client.
		if (poolOpportunity.isCheckedOut()) {
			poolOpportunity = getFirstAvailableOpportunity(this.pool, this.groupName, this.timeout);
		}

		// set the hashcode on the user and return the user object
		if (this.checkOut) {
			poolOpportunity.checkOut(checkOutToken.hashCode());
		}

		// return the user object
		return poolOpportunity;
	}

	synchronized private PoolOpportunity getFirstAvailableOpportunity(ArrayList<PoolOpportunity> pool, String poolDescription, long timeout) {

		long deadline = System.currentTimeMillis() + timeout;
		PoolOpportunity poolOpportunity = null;

		for(int count = 0; poolOpportunity == null && (System.currentTimeMillis() < deadline || count == 0); count++) {

			for (int i = 0; i < pool.size() && poolOpportunity == null; i++) {
				if (!pool.get(i).isCheckedOut()) {
					poolOpportunity = pool.get(i);
				}
			}
			if (poolOpportunity == null)
				log.warn("Starvation warning: The opportunity pool '" + poolDescription + "' has no available opportunities. This will delay your test. " + (deadline - System.currentTimeMillis()) + " millis remaining.");
			Utils.milliSleep(new Random().nextInt(5000));
		}

		if (poolOpportunity == null) {
			log.error("Starvation: Your test starved to death waiting for available opportunity of pool '" + poolDescription + "'. You need more opportunities!");
			throw new RuntimeException("Starvation: Your test starved to death waiting for available client of pool '" + poolDescription + "'. You need more opportunites!");
		} else {
			return poolOpportunity;
		}
	}

	private void assertPoolNotEmpty() {

		if (this.pool.isEmpty()) {
			log.error("Error: You have requested an opportunity from the empty opportunity group '" + groupName + "'.");
			throw new InvalidParameterException("Error: You have requested an opportunity from the empty opportunity group '" + groupName + "'.");
		}

	}

	synchronized ArrayList<PoolOpportunity> queryOpportunities(boolean withRemoval, String attribute, ArrayList<String> indentifiers) {

		ArrayList<PoolOpportunity> newPool = new ArrayList<PoolOpportunity>();

		// loop through every opportunity in the fromPool, and if it a user matches criteria, add it to the returned list. Remove reference from fromPool if option set.
		Iterator<PoolOpportunity> iterator = this.pool.iterator();
		while (iterator.hasNext()) {
			PoolOpportunity poolOpportunity = iterator.next();
			if (indentifiers.contains(poolOpportunity.getAttribute(attribute))) {
				newPool.add(poolOpportunity);
				if (withRemoval) {
					iterator.remove();
				}
			}
		}
		return newPool;
	}

	private String generateQueryDescription(String groupName, String attribute, String... identifiers) {

		StringBuilder builder = new StringBuilder();
		builder.append(groupName);
		builder.append("[Query{attribute=");
		builder.append(attribute + ",values=");
		for (int i = 0; i < identifiers.length; i++) {
			builder.append(identifiers[i] + ",");
		}
		builder.deleteCharAt(builder.length() - 1);
		builder.append("}]");
		return builder.toString();
	}

	/**
	 * Gets the specified opportunity from map.
	 * 
	 * @param map
	 *            the opportunity map
	 * @param identifiers
	 *            the user of interest
	 * @return the specified opportunity from map
	 */
	PoolOpportunity getSpecifiedOpportunityFromPool(String attribute, String value, Object checkOutToken) {

		log.debug("Requested the opportunity from pool " + this.groupName + " with value '" + value + "' for attribute '" + attribute + "'.");

		assertPoolNotEmpty();

		ArrayList<String> indentifiers = new ArrayList<String>(Arrays.asList(value));

		ArrayList<PoolOpportunity> results = queryOpportunities(false, attribute, indentifiers);

		PoolOpportunity poolOpportunity = null;
		String poolDescription = generateQueryDescription(this.groupName, attribute, value);

		if (results.size() > 1) {
			log.error(results.size() + " opportunities returned for " + poolDescription + ", indentifier must be unique.");
			throw new InvalidParameterException(results.size() + " opportunities returned for " + poolDescription + ", indentifier must be unique.");
		} else if (results.size() < 1) {
			log.error("No opportunities returned for the query " + poolDescription + ".");
			throw new InvalidParameterException("No opportunities returned for the query " + poolDescription + ".");
		} else {// results.size() == 1
			poolOpportunity = getFirstAvailableOpportunity(results, poolDescription, timeout);
			if (this.checkOut) {
				poolOpportunity.checkOut(checkOutToken.hashCode());
			}
		}

		return poolOpportunity;
	}

	/**
	 * Clear opportunity map.
	 * 
	 * Checks the opportunity map for opportunities 'checked out' with the class instance identifier
	 * 
	 * @param map
	 *            the map of opportunities
	 * @param checkOutToken
	 *            the instance of the calling class
	 */
	void clearAllOpportunities(Object checkOutToken, boolean forceCheckIn) {

		int count = 0;

		// Iterate through the map and check for the class instance set
		// Set the hashcode id of the users to 0 if the hash matches or forceCheckIn is true
		for (PoolOpportunity poolOpportunity : this.pool) {

			// check if the hashcode has been set
			// Get the hashcode of the class instance and compare.
			// reset the hashcode to 0 if hashes match or forceCheckIn is true
			if (poolOpportunity.isCheckedOut() && (poolOpportunity.isCheckOutToken(checkOutToken) || forceCheckIn)) {
				poolOpportunity.checkIn();
				count++;
			}
		}
		log.debug("The current executing class " + checkOutToken.toString() + " checked back in " + count + " opportunities.");

	}

	/**
	 * Clear a specified list of opportunities from the map
	 */
	void clearSpecificOpportunitiesFromPool(Object checkOutToken, boolean forceCheckIn, String attribute, String... identifiers) {
		
		int count = 0;
		String poolDescription = generateQueryDescription(this.groupName, attribute, identifiers);
		//query the users
		ArrayList<String> indentifiers = new ArrayList<String>(Arrays.asList(identifiers));
		ArrayList<PoolOpportunity> results = queryOpportunities(false, attribute, indentifiers);

		// Iterate through the map and check for the class instance set
		// Set the hashcode id of the users to 0 if the hash matches or forceCheckIn is true
		for (PoolOpportunity poolOpportunity : results) {

			// check if the hashcode has been set
			// Ensure that the object trying to unlock the user is the object that requested the user initially
			// Get the hashcode of the class instance and compare.
			// reset the hashcode to 0 if hashes match or forceCheckIn is true
			if (poolOpportunity.isCheckedOut() && (poolOpportunity.isCheckOutToken(checkOutToken) || forceCheckIn)) {
				poolOpportunity.checkIn();
				count++;
			}
		}
		log.debug("The current executing class " + checkOutToken.toString() + " checked back in " + count + " opportunities that matched pool "+poolDescription+".");
	}
	
	int size(){
		
		return this.pool.size();
	}
}
