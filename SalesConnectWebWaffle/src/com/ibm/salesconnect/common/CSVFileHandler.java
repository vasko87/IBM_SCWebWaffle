/**
 * 
 */
package com.ibm.salesconnect.common;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import au.com.bytecode.opencsv.CSVReader;

/**
 * @author timlehane
 * @date Mar 31, 2014
 */
public class CSVFileHandler {
	   public static ArrayList<HashMap<String, String>> loadCSV(String filePath,String separator,Boolean headerIncluded) throws IOException{
			
			if(filePath == null || separator == null ){
				throw new IllegalArgumentException("filePath or separator not specified (null). Can't load CSV.");
			}
			
	    	CSVReader csvReader = new CSVReader(new FileReader(filePath), separator.toCharArray()[0]);
	    	
			// The map that will be returned. Will be returned empty if there is no data.
			ArrayList<HashMap<String, String>> listOfHeaderToValueMaps = new ArrayList<HashMap<String, String>>();

			// Create list of the headers, or {"0","1","2",...} as default if no headers included
			ArrayList<String> headers = new ArrayList<String>();
			String[] firstLine = csvReader.readNext();
			if (headerIncluded) {
				for (int i = 0; i < firstLine.length; i++) {
					headers.add(firstLine[i]);
				}
			} else {			
				headers = generateDefaultHeaders(firstLine.length);
				
				HashMap<String, String> headerToValueMap = new HashMap<String, String>();
				for (int i = 0; i < firstLine.length; i++) {
					headerToValueMap.put(headers.get(i), firstLine[i]);
				}
					// All OK, add map to list
				listOfHeaderToValueMaps.add(headerToValueMap);
			}
			validateHeaders(headers);
			
			String[] nextLine;
			while ((nextLine = csvReader.readNext()) != null) {
			  if (nextLine != null) {
				  HashMap<String, String> headerToValueMap = new HashMap<String, String>();
					for (int i = 0; i < nextLine.length; i++) {
						headerToValueMap.put(headers.get(i), nextLine[i]);
					}
					// All OK, add map to list
					listOfHeaderToValueMaps.add(headerToValueMap);
			  }
			}
	 
			return listOfHeaderToValueMaps;
	    }

		private static int countInstances(ArrayList<String> haystack, String needle) {

			int count = 0;
			for (String straw : haystack) {
				if (straw.equals(needle))
					count++;
			}
			return count;
		}
		private static void validateHeaders(ArrayList<String> headers) {

			// check there's at least one
			if (!(headers.size() > 0))
				throw new RuntimeException("Error parsing CSV, there must be at least one column.");

			// check the headers are unique
			for (String header : headers) {
				if (countInstances(headers, header) > 1)
					throw new RuntimeException("Error parsing CSV, the header '" + header + "' is not unique.");
			}
		}

		private static ArrayList<String> generateDefaultHeaders(int length) {

			ArrayList<String> headers = new ArrayList<String>();
			for (int i = 0; i < length; i++) {
				headers.add(String.valueOf(i));
			}
			return headers;
		}
}
