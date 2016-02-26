/**
 * 
 */
package com.ibm.salesconnect.PoolHandling.Opportunity;

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
public class OpportunityAllocation {
	private static final Logger log = LoggerFactory.getLogger(OpportunityAllocation.class);

	private static final String DEFAULT_PROPERTIES_FILE_PATH = "test_config/extensions/opportunity/default_opportunities.properties";
	
	/** The opportunity map. */
	private static final String STANDARD_OPPORTUNITIES_GROUP = "standard_opportunities";

	private Properties opportunityProperties;

	private Map<String, OpportunityGroup> opportunityGroups = new HashMap<String, OpportunityGroup>();

	private static volatile Map<String, OpportunityAllocation> allocators = Collections.synchronizedMap(new HashMap<String, OpportunityAllocation>());

	/**
	 * Returns an OpportunityAllocation for an opportunity properties file. If the path has not been used before a new OpoortunityAllocator will be created. If their is an existing OpportunityAllocator for this
	 * properties file, it will be returned.
	 * 
	 * @return opportunityAllocation
	 */
	public static synchronized OpportunityAllocation getOpportunityAllocation(String filePath) {

		OpportunityAllocation opportunityAllocator;
		if (allocators.containsKey(filePath)) {
			opportunityAllocator = allocators.get(filePath);
		} else {
			opportunityAllocator = new OpportunityAllocation(filePath);
			allocators.put(filePath, opportunityAllocator);
		}
		return opportunityAllocator;
	}
	
	public static OpportunityAllocation getOpportunityAllocation() {
		
		return getOpportunityAllocation(DEFAULT_PROPERTIES_FILE_PATH);
	}

	private OpportunityAllocation(String filePath) {

		opportunityProperties = FileIOHandler.loadExternalProperties(filePath);

		ArrayList<HashMap<String, String>> opportunityData = CSVHandler.loadCSV(opportunityProperties.getProperty("opportunity_data_file_path"), opportunityProperties.getProperty("csv_delimiter"), true);

		if (opportunityData.size() == 0) {
			throw new RuntimeException("There was no data loaded from the CSV file. Verify your file and check the log for warnings.");
		} else {
			log.debug("Constructing opportunityAllocation from pool of " + opportunityData.size() + " opportunities.");
		}

		ArrayList<PoolOpportunity> opportunityPool = createOpportunityPoolFromData(opportunityData);

		this.opportunityGroups = splitPoolToGroups(opportunityPool);

		log.info(opportunityGroups.size() + " opportunity groups have been loaded (including the standard/default group).");
		for(String key : this.opportunityGroups.keySet()){
			log.info("Group name: "+key+". Size: "+this.opportunityGroups.get(key).size());
		}
	}

	private synchronized Map<String, OpportunityGroup> splitPoolToGroups(ArrayList<PoolOpportunity> opportunityPool) {

		Map<String, OpportunityGroup> opportunityGroups = new HashMap<String, OpportunityGroup>();
		String delimiter = opportunityProperties.getProperty("config_delimiter", ";");

		// Get the names of all the opportunity groups to be formed from the opportunity pool.
		String[] groupNames = opportunityProperties.getProperty("special_opportunity_groups").split(delimiter);

		// Add all opportunities in the main opportunity pool to the group called 'standard_opportunities'. Remove opportunites for all other groups if .ringfence property is true, otherwise opportunity is shared.
		opportunityGroups.put(STANDARD_OPPORTUNITIES_GROUP, new OpportunityGroup(opportunityPool, STANDARD_OPPORTUNITIES_GROUP, Boolean.parseBoolean(opportunityProperties.getProperty(STANDARD_OPPORTUNITIES_GROUP + ".check_out",
				"true")), Long.parseLong(opportunityProperties.getProperty(STANDARD_OPPORTUNITIES_GROUP + ".timeout", "10000"))));
		
		// Identify the opportunities that belong to each group and add to map of opportunity groups
		for (int i = 0; i < groupNames.length; i++) {

			String groupName = groupNames[i];

			// get config properties for this group
			String groupAttribute = opportunityProperties.getProperty(groupName + ".attribute");
			ArrayList<String> indentifiers = new ArrayList<String>(Arrays.asList(opportunityProperties.getProperty(groupName + ".identifiers").split(delimiter)));
			boolean checkOut = Boolean.parseBoolean(opportunityProperties.getProperty(groupName + ".check_out", "true"));
			long timeout = Long.parseLong(opportunityProperties.getProperty(groupName + ".timeout", "10000"));
			boolean ringfence = Boolean.parseBoolean(opportunityProperties.getProperty(groupName + ".ringfence", "true"));

			// Move opportunites from main pool to the group pool
			ArrayList<PoolOpportunity> groupPool = opportunityGroups.get(STANDARD_OPPORTUNITIES_GROUP).queryOpportunities(ringfence, groupAttribute, indentifiers); 

			if (groupPool.isEmpty())
				log.warn("opportunity pool '" + groupName + "' is empty.");

			// wrap group pool as OpportunityGroup instance with group configuration.
			OpportunityGroup group = new OpportunityGroup(groupPool, groupName, checkOut, timeout);
			opportunityGroups.put(groupName, group);
		}

		return opportunityGroups;
	}

	private ArrayList<PoolOpportunity> createOpportunityPoolFromData(ArrayList<HashMap<String, String>> opportunityData) {

		ArrayList<PoolOpportunity> opportunityPool = new ArrayList<PoolOpportunity>();

		for (HashMap<String, String> map : opportunityData) {
			opportunityPool.add(new PoolOpportunity(map));
		}

		return opportunityPool;
	}

	private OpportunityGroup getOpportunityGroup(String name) {

		OpportunityGroup group = this.opportunityGroups.get(name);
		if(group == null){
			log.error("The opportunity group '" + name + "' was requested but has not been loaded.");
			throw new RuntimeException("The opportunity group '" + name + "' was requested but has not been loaded.");
		}
		return group;
	}

	/**
	 * Gets a standard opportunity without a token. If checkout is enabled this opportunity would have to be checked back in manually.
	 * 
	 * @return opportunity object representing a non-special/admin opportunity.
	 */
	public PoolOpportunity getOpportunity() {

		return getOpportunity(this);
	}

	public PoolOpportunity getOpportunity(Object checkOutToken) {

		return getOpportunityGroup(STANDARD_OPPORTUNITIES_GROUP).getAvailableOpportunityFromPool(checkOutToken);
	}

	
	public PoolOpportunity getGroupOpportunity(String groupName) {

		return getGroupOpportunity(groupName, this);
	}

	public PoolOpportunity getGroupOpportunity(String groupName, Object checkOutToken) {

		return getOpportunityGroup(groupName).getAvailableOpportunityFromPool(checkOutToken);
	}

	public void checkInAllOpportunities() {

		getOpportunityGroup(STANDARD_OPPORTUNITIES_GROUP).clearAllOpportunities(this, true);
	}

	public void checkInAllopportunitiesWithToken(Object checkOutToken) {

		getOpportunityGroup(STANDARD_OPPORTUNITIES_GROUP).clearAllOpportunities(checkOutToken, false);
	}


	public void checkInAllGroupOpportunites(String groupName) {

		getOpportunityGroup(groupName).clearAllOpportunities(this, true);
	}

	public void checkInAllGroupOpportunityWithToken(String groupName, Object checkOutToken) {

		getOpportunityGroup(groupName).clearAllOpportunities(checkOutToken, false);
	}
}
