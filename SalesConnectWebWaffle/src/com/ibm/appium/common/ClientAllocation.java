/**
 * 
 */
package com.ibm.appium.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.atmn.waffle.utils.CSVHandler;
import com.ibm.atmn.waffle.utils.FileIOHandler;



/**
 * @author timlehane
 * @date May 10, 2013
 */
public class ClientAllocation {
	private static final Logger log = LoggerFactory.getLogger(ClientAllocation.class);

	private static final String DEFAULT_PROPERTIES_FILE_PATH = "test_config/extensions/client/default_clients.properties";
	
	/** The client map. */
	private static final String STANDARD_CLIENT_GROUP = "standard_clients";
	private static final String DC = "DC";

	private Properties clientProperties;

	private Map<String, ClientGroup> clientGroups = new HashMap<String, ClientGroup>();

	private static volatile Map<String, ClientAllocation> allocators = Collections.synchronizedMap(new HashMap<String, ClientAllocation>());

	/**
	 * Returns a ClientAllocation for a client properties file. If the path has not been used before a new ClientAllocator will be created. If their is an existing ClientAllocator for this
	 * properties file, it will be returned.
	 * 
	 * @return clientAllocation
	 */
	public static synchronized ClientAllocation getClientAllocation(String filePath) {

		ClientAllocation clientAllocator;
		if (allocators.containsKey(filePath)) {
			clientAllocator = allocators.get(filePath);
		} else {
			clientAllocator = new ClientAllocation(filePath);
			allocators.put(filePath, clientAllocator);
		}
		return clientAllocator;
	}
	
	public static ClientAllocation getClientAllocation() {
		
		return getClientAllocation(DEFAULT_PROPERTIES_FILE_PATH);
	}

	private ClientAllocation(String filePath) {

		clientProperties = FileIOHandler.loadExternalProperties(filePath);

		ArrayList<HashMap<String, String>> clientData = CSVHandler.loadCSV(clientProperties.getProperty("client_data_file_path"), clientProperties.getProperty("csv_delimiter"), true);

		if (clientData.size() == 0) {
			throw new RuntimeException("There was no data loaded from the CSV file. Verify your file and check the log for warnings.");
		} else {
			log.debug("Constructing clientAllocation from pool of " + clientData.size() + " clients.");
		}

		ArrayList<PoolClient> clientPool = createClientPoolFromData(clientData);

		this.clientGroups = splitPoolToGroups(clientPool);

		log.info(clientGroups.size() + " client groups have been loaded (including the standard/default group).");
		for(String key : this.clientGroups.keySet()){
			log.info("Group name: "+key+". Size: "+this.clientGroups.get(key).size());
		}
	}

	private synchronized Map<String, ClientGroup> splitPoolToGroups(ArrayList<PoolClient> clientPool) {

		Map<String, ClientGroup> clientGroups = new HashMap<String, ClientGroup>();
		String delimiter = clientProperties.getProperty("config_delimiter", ";");

		// Get the names of all the client groups to be formed from the client pool.
		String[] groupNames = clientProperties.getProperty("special_client_groups").split(delimiter);

		// Add all clients in the main client pool to the group called 'standard_clients'. Remove clients for all other groups if .ringfence property is true, otherwise client is shared.
		clientGroups.put(STANDARD_CLIENT_GROUP, new ClientGroup(clientPool, STANDARD_CLIENT_GROUP, Boolean.parseBoolean(clientProperties.getProperty(STANDARD_CLIENT_GROUP + ".check_out",
				"true")), Long.parseLong(clientProperties.getProperty(STANDARD_CLIENT_GROUP + ".timeout", "10000"))));
		
		// Identify the clients that belong to each group and add to map of client groups
		for (int i = 0; i < groupNames.length; i++) {

			String groupName = groupNames[i];

			// get config properties for this group
			String groupAttribute = clientProperties.getProperty(groupName + ".attribute");
			ArrayList<String> indentifiers = new ArrayList<String>(Arrays.asList(clientProperties.getProperty(groupName + ".identifiers").split(delimiter)));
			boolean checkOut = Boolean.parseBoolean(clientProperties.getProperty(groupName + ".check_out", "true"));
			long timeout = Long.parseLong(clientProperties.getProperty(groupName + ".timeout", "10000"));
			boolean ringfence = Boolean.parseBoolean(clientProperties.getProperty(groupName + ".ringfence", "true"));

			// Move clients from main pool to the group pool
			ArrayList<PoolClient> groupPool = clientGroups.get(STANDARD_CLIENT_GROUP).queryClients(ringfence, groupAttribute, indentifiers); 

			if (groupPool.isEmpty())
				log.warn("client pool '" + groupName + "' is empty.");

			// wrap group pool as ClientGroup instance with group configuration.
			ClientGroup group = new ClientGroup(groupPool, groupName, checkOut, timeout);
			clientGroups.put(groupName, group);
		}

		return clientGroups;
	}

	private ArrayList<PoolClient> createClientPoolFromData(ArrayList<HashMap<String, String>> clientData) {

		ArrayList<PoolClient> clientPool = new ArrayList<PoolClient>();

		for (HashMap<String, String> map : clientData) {
			clientPool.add(new PoolClient(map));
		}

		return clientPool;
	}

	private ClientGroup getClientGroup(String name) {

		ClientGroup group = this.clientGroups.get(name);
		if(group == null){
			log.error("The client group '" + name + "' was requested but has not been loaded.");
			throw new RuntimeException("The client group '" + name + "' was requested but has not been loaded.");
		}
		return group;
	}

	/**
	 * Gets a standard client without a token. If checkout is enabled this client would have to be checked back in manually.
	 * 
	 * @return client object representing a non-special/admin client.
	 */
	public PoolClient getClient() {

		return getClient(this);
	}

	public PoolClient getClient(Object checkOutToken) {

		return getClientGroup(DC).getAvailableClientFromPool(checkOutToken);
	}

	
	public PoolClient getGroupClient(String groupName) {

		return getGroupClient(groupName, this);
	}

	public PoolClient getGroupClient(String groupName, Object checkOutToken) {

		return getClientGroup(groupName).getAvailableClientFromPool(checkOutToken);
	}

	public void checkInAllclients() {

		getClientGroup(DC).clearAllClients(this, true);
	}

	public void checkInAllclientsWithToken(Object checkOutToken) {

		getClientGroup(DC).clearAllClients(checkOutToken, false);
	}


	public void checkInAllGroupClients(String groupName) {

		getClientGroup(groupName).clearAllClients(this, true);
	}

	public void checkInAllGroupClientWithToken(String groupName, Object checkOutToken) {

		getClientGroup(groupName).clearAllClients(checkOutToken, false);
	}
}
