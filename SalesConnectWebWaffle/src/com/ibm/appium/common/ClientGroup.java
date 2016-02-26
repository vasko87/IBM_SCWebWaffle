/**
 * 
 */
package com.ibm.appium.common;

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
public class ClientGroup {
	private static final Logger log = LoggerFactory.getLogger(ClientGroup.class);

	private String groupName;

	private boolean checkOut = true;

	private long timeout = 10000;

	private volatile ArrayList<PoolClient> pool;

	ClientGroup(ArrayList<PoolClient> pool, String groupName, boolean checkOut, long timeout) {

		this.groupName = groupName;
		this.pool = pool;
		this.checkOut = checkOut;
		this.timeout = timeout;
	}

	PoolClient getAvailableClientFromPool(Object checkOutToken) {

		// assert that the pool has at least one entry
		assertPoolNotEmpty();

		// Get random client in list, if not available, get first available client in list.
		// If no available clients, wait up to timeout for user to become available.

		// Return an integer in the range 0-(pool.size()-1) inclusive
		int rand = new Random().nextInt(pool.size());

		PoolClient poolClient = this.pool.get(rand);

		// Check that the random client does not have their hashcode set. If so, wait for available client.
		if (poolClient.isCheckedOut()) {
			poolClient = getFirstAvailableClient(this.pool, this.groupName, this.timeout);
		}

		// set the hashcode on the user and return the user object
		if (this.checkOut) {
			poolClient.checkOut(checkOutToken.hashCode());
		}

		// return the user object
		return poolClient;
	}

	synchronized private PoolClient getFirstAvailableClient(ArrayList<PoolClient> pool, String poolDescription, long timeout) {

		long deadline = System.currentTimeMillis() + timeout;
		PoolClient poolClient = null;

		for(int count = 0; poolClient == null && (System.currentTimeMillis() < deadline || count == 0); count++) {

			for (int i = 0; i < pool.size() && poolClient == null; i++) {
				if (!pool.get(i).isCheckedOut()) {
					poolClient = pool.get(i);
				}
			}
			if (poolClient == null)
				log.warn("Starvation warning: The client pool '" + poolDescription + "' has no available clients. This will delay your test. " + (deadline - System.currentTimeMillis()) + " millis remaining.");
			Utils.milliSleep(new Random().nextInt(5000));
		}

		if (poolClient == null) {
			log.error("Starvation: Your test starved to death waiting for available client of pool '" + poolDescription + "'. You need more clients!");
			throw new RuntimeException("Starvation: Your test starved to death waiting for available client of pool '" + poolDescription + "'. You need more clients!");
		} else {
			return poolClient;
		}
	}

	private void assertPoolNotEmpty() {

		if (this.pool.isEmpty()) {
			log.error("Error: You have requested a client from the empty client group '" + groupName + "'.");
			throw new InvalidParameterException("Error: You have requested a client from the empty client group '" + groupName + "'.");
		}

	}

	synchronized ArrayList<PoolClient> queryClients(boolean withRemoval, String attribute, ArrayList<String> indentifiers) {

		ArrayList<PoolClient> newPool = new ArrayList<PoolClient>();

		// loop through every client in the fromPool, and if it a client matches criteria, add it to the returned list. Remove reference from fromPool if option set.
		Iterator<PoolClient> iterator = this.pool.iterator();
		while (iterator.hasNext()) {
			PoolClient poolClient = iterator.next();
			if (indentifiers.contains(poolClient.getAttribute(attribute))) {
				newPool.add(poolClient);
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
	 * Gets the specified client from map.
	 * 
	 * @param map
	 *            the client map
	 * @param identifiers
	 *            the user of interest
	 * @return the specified client from map
	 */
	PoolClient getSpecifiedClientFromPool(String attribute, String value, Object checkOutToken) {

		log.debug("Requested the client from pool " + this.groupName + " with value '" + value + "' for attribute '" + attribute + "'.");

		assertPoolNotEmpty();

		ArrayList<String> indentifiers = new ArrayList<String>(Arrays.asList(value));

		ArrayList<PoolClient> results = queryClients(false, attribute, indentifiers);

		PoolClient poolClient = null;
		String poolDescription = generateQueryDescription(this.groupName, attribute, value);

		if (results.size() > 1) {
			log.error(results.size() + " clients returned for " + poolDescription + ", indentifier must be unique.");
			throw new InvalidParameterException(results.size() + " clients returned for " + poolDescription + ", indentifier must be unique.");
		} else if (results.size() < 1) {
			log.error("No clients returned for the query " + poolDescription + ".");
			throw new InvalidParameterException("No clients returned for the query " + poolDescription + ".");
		} else {// results.size() == 1
			poolClient = getFirstAvailableClient(results, poolDescription, timeout);
			if (this.checkOut) {
				poolClient.checkOut(checkOutToken.hashCode());
			}
		}

		return poolClient;
	}

	/**
	 * Clear client map.
	 * 
	 * Checks the client map for clients 'checked out' with the class instance identifier
	 * 
	 * @param map
	 *            the map of clients
	 * @param checkOutToken
	 *            the instance of the calling class
	 */
	void clearAllClients(Object checkOutToken, boolean forceCheckIn) {

		int count = 0;

		// Iterate through the map and check for the class instance set
		// Set the hashcode id of the users to 0 if the hash matches or forceCheckIn is true
		for (PoolClient poolClient : this.pool) {

			// check if the hashcode has been set
			// Get the hashcode of the class instance and compare.
			// reset the hashcode to 0 if hashes match or forceCheckIn is true
			if (poolClient.isCheckedOut() && (poolClient.isCheckOutToken(checkOutToken) || forceCheckIn)) {
				poolClient.checkIn();
				count++;
			}
		}
		log.debug("The current executing class " + checkOutToken.toString() + " checked back in " + count + " clients.");

	}

	/**
	 * Clear a specified list of clients from the map
	 */
	void clearSpecificClientsFromPool(Object checkOutToken, boolean forceCheckIn, String attribute, String... identifiers) {
		
		int count = 0;
		String poolDescription = generateQueryDescription(this.groupName, attribute, identifiers);
		//query the users
		ArrayList<String> indentifiers = new ArrayList<String>(Arrays.asList(identifiers));
		ArrayList<PoolClient> results = queryClients(false, attribute, indentifiers);

		// Iterate through the map and check for the class instance set
		// Set the hashcode id of the users to 0 if the hash matches or forceCheckIn is true
		for (PoolClient poolClient : results) {

			// check if the hashcode has been set
			// Ensure that the object trying to unlock the user is the object that requested the user initially
			// Get the hashcode of the class instance and compare.
			// reset the hashcode to 0 if hashes match or forceCheckIn is true
			if (poolClient.isCheckedOut() && (poolClient.isCheckOutToken(checkOutToken) || forceCheckIn)) {
				poolClient.checkIn();
				count++;
			}
		}
		log.debug("The current executing class " + checkOutToken.toString() + " checked back in " + count + " clients that matched pool "+poolDescription+".");
	}
	
	int size(){
		
		return this.pool.size();
	}
}
