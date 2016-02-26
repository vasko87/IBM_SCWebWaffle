/**
 * 
 */
package com.ibm.salesconnect.API;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import au.com.bytecode.opencsv.CSVReader;

/**
 * @author brownholtz
 * 
 */
public class ClientIdAndSecret {

	static private Map<String, String> IdSecretMap = null;
	private String environment = "test"; // default

	public ClientIdAndSecret(String environment) {
		this.environment = environment;
	}

	String getIdSecret(String type) {

		if (IdSecretMap == null) {
			IdSecretMap = initMapFromFile(environment);
		}

		return IdSecretMap.get(type);

	}

	Map<String, String> initMapFromFile(String env) {
		CSVReader csvReader = null;

		Map<String, String> clientIDSecretData = new HashMap<String, String>();

		try {

			// columns in file: clientid,secret,type,environment
			csvReader = new CSVReader(new FileReader(
					"test_config/extensions/user/clientsecret.csv"), ',', '\'',
					1);

			String[] nextLine;

			while ((nextLine = csvReader.readNext()) != null) {

				// pull in entries from correct environment
				if (nextLine[3].equalsIgnoreCase(env)) {
					String clientSecret = "client_id=" + nextLine[0]
							+ "&client_secret=" + nextLine[1];
					clientIDSecretData.put(nextLine[2], clientSecret);

					System.out.println("** Added client_id and secret "
							+ nextLine[2] + ": " + clientSecret);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (csvReader != null) {
				try {
					csvReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return clientIDSecretData;
	}

	protected Map<String, String> getMap() {
		if (IdSecretMap == null) {
			IdSecretMap = initMapFromFile(environment);
		}
		return IdSecretMap;
	}

	public static void main(String[] args) {
		ClientIdAndSecret idsecret = new ClientIdAndSecret("development");

		Map<String, String> theMap = idsecret.getMap();

		Set<String> keys = theMap.keySet();
		for (String k : keys) {
			String clientIDandSecret = theMap.get(k);
			String clientID = clientIDandSecret.substring(0,
					(clientIDandSecret.indexOf("&")));

			String clientSecret = clientIDandSecret
					.substring((clientIDandSecret.indexOf("&") + 1));

			System.out.println(k + ":" + clientIDandSecret + "\n" + clientID
					+ ":" + clientSecret + "\n");
		}

		String apiObjectName = "tasks";

		String correctCaseExtension = apiObjectName.substring(0, 1)
				.toUpperCase() + apiObjectName.substring(1).toLowerCase();

		System.out.println("corrected case " + correctCaseExtension);

	}
}
