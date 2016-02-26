/**
 * 
 */
package com.ibm.salesconnect.API;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.salesconnect.common.CSVFileHandler;



/**
 * @author timlehane
 * @date May 10, 2013
 */
public class TestDataHolder {
	
    private ArrayList<HashMap<String,String>> dataRows;
    
	public TestDataHolder(){
    dataRows = new ArrayList<HashMap<String,String>>();
	}
	private static final Logger log = LoggerFactory.getLogger(TestDataHolder.class);
    
	
	public void addDataLocation(String filePath){
		addDataLocation(filePath, ",");
	}
	public void addDataLocation(String filePath, String delimiter) {
		try {
			dataRows = CSVFileHandler.loadCSV(filePath, delimiter, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (dataRows.size() == 0) {
			throw new RuntimeException("There was no data loaded from the test file. Verify your file and check the log for warnings.");
		} else {
			log.debug(dataRows+" tests loaded");
		}
	}
	
    public Object[][] getAllDataRows(){
        Object[][] returnObject = new Object[this.dataRows.size()][1];
        for(int itemIndex = 0; itemIndex < this.dataRows.size(); itemIndex++){
            returnObject[itemIndex][0] = this.dataRows.get(itemIndex);
        }
        return returnObject;
    }
    
}
