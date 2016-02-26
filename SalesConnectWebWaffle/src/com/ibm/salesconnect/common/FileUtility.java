/**
 * 
 */
package com.ibm.salesconnect.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
//import java.util.ArrayList;
//import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.salesconnect.API.LoginRestAPI;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;

//import au.com.bytecode.opencsv.CSVReader;

/**
 * @author kvnlau
 * @date Mar 31, 201
 */
public class FileUtility extends ProductBaseTest {
	Logger log = LoggerFactory.getLogger(LoginRestAPI.class);
	
	public static boolean updateClientCsvForCchApi(String filePath, String separator, 
													String searchString1, String searchString2, 
													String[] GuClientList, String[] GbClientList) throws IOException {		
		try {
			File inFile = new File(filePath);
			
			if(filePath == null || separator == null ){
				throw new IllegalArgumentException("filePath or separator not specified (null). Can't load CSV.");
			}
			
			if (!inFile.isFile()) {
				System.out.print("FileUtility.loadFileToString(): No file exists at path: " + filePath);
				return false;
			}
			
            //Construct the new file that will later be renamed to the original filename.
            File tempFile = new File(inFile.getAbsolutePath() + ".tmp");

            BufferedReader br = new BufferedReader(new FileReader(filePath));
            PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

            String line = null;

            //Read from the original file and write to the new
            //unless content matches data to be removed (i.e. 'GBCLIENT' or 'GUCLIENT')
            while ((line = br.readLine()) != null) {
            	// if line contains searchstring1, skip it (i.e. don't write to new file)
            	if (searchString1!=null) {
	                if (line.trim().contains(searchString1)) {
	                	System.out.print("Omitting this line from new file: '" + line + "'\n");
	                	continue;
	                } 
            	}
            	// if line contains searchstring2, skip it (i.e. don't write to new file)
            	if (searchString1!=null) {
	                if (line.trim().contains(searchString2)) {
	                	System.out.print("Omitting this line from new file: '" + line + "'\n");
	                	continue;
	                } 
            	}
            	// write this line into file
                pw.println(line);
                pw.flush();
            }
            
            // add new GB clients into file
            for (int count =0; count<GbClientList.length; count++) {
            	if (GbClientList[count]!=null) {
            		pw.println(GbClientList[count] + ",,GBCLIENT");
            		pw.flush();
            	}
            }
            // add new GU clients into file
            for (int count =0; count<GuClientList.length; count++) {
            	if (GuClientList[count]!=null) {
            		pw.println(GuClientList[count] + ",,GUCLIENT");
            		pw.flush();
            	}
            }
            //close file handles
            pw.close();
            br.close();

            //Delete the original file
            if (!inFile.delete()) {
                System.out.println("Could not delete file");
                return false;
            }

            //Rename the new file to the filename the original file had.
            if (!tempFile.renameTo(inFile)) {
            	System.out.println("Could not rename file");
            	return false;
            }

		} catch (Exception ex) {
			System.out.print(ex);
			System.err.println("Could not load file " + filePath + " \n");
			return false;
		} 
		return true;
	}
	
//	   public static ArrayList<HashMap<String, String>> loadCSV(String filePath,String separator,Boolean headerIncluded) throws IOException{
//			
//			if(filePath == null || separator == null ){
//				throw new IllegalArgumentException("filePath or separator not specified (null). Can't load CSV.");
//			}
//			
//	    	CSVReader csvReader = new CSVReader(new FileReader(filePath), separator.toCharArray()[0]);
//	    	
//			// The map that will be returned. Will be returned empty if there is no data.
//			ArrayList<HashMap<String, String>> listOfHeaderToValueMaps = new ArrayList<HashMap<String, String>>();
//
//			// Create list of the headers, or {"0","1","2",...} as default if no headers included
//			ArrayList<String> headers = new ArrayList<String>();
//			String[] firstLine = csvReader.readNext();
//			if (headerIncluded) {
//				for (int i = 0; i < firstLine.length; i++) {
//					headers.add(firstLine[i]);
//				}
//			} else {			
//				headers = generateDefaultHeaders(firstLine.length);
//				
//				HashMap<String, String> headerToValueMap = new HashMap<String, String>();
//				for (int i = 0; i < firstLine.length; i++) {
//					headerToValueMap.put(headers.get(i), firstLine[i]);
//				}
//					// All OK, add map to list
//				listOfHeaderToValueMaps.add(headerToValueMap);
//			}
//			validateHeaders(headers);
//			
//			String[] nextLine;
//			while ((nextLine = csvReader.readNext()) != null) {
//			  if (nextLine != null) {
//				  HashMap<String, String> headerToValueMap = new HashMap<String, String>();
//					for (int i = 0; i < nextLine.length; i++) {
//						headerToValueMap.put(headers.get(i), nextLine[i]);
//					}
//					// All OK, add map to list
//					listOfHeaderToValueMaps.add(headerToValueMap);
//			  }
//			}
//	 
//			return listOfHeaderToValueMaps;
//	    }
//
//		private static int countInstances(ArrayList<String> haystack, String needle) {
//
//			int count = 0;
//			for (String straw : haystack) {
//				if (straw.equals(needle))
//					count++;
//			}
//			return count;
//		}
//		private static void validateHeaders(ArrayList<String> headers) {
//
//			// check there's at least one
//			if (!(headers.size() > 0))
//				throw new RuntimeException("Error parsing CSV, there must be at least one column.");
//
//			// check the headers are unique
//			for (String header : headers) {
//				if (countInstances(headers, header) > 1)
//					throw new RuntimeException("Error parsing CSV, the header '" + header + "' is not unique.");
//			}
//		}
//
//		private static ArrayList<String> generateDefaultHeaders(int length) {
//
//			ArrayList<String> headers = new ArrayList<String>();
//			for (int i = 0; i < length; i++) {
//				headers.add(String.valueOf(i));
//			}
//			return headers;
//		}
}
