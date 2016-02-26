/**
 * 
 */
package com.ibm.salesconnect.common;

import org.apache.http.client.methods.HttpPost;

/**
 * @author timlehane
 * @date Mar 5, 2015
 */
public class MyHttpDelete extends HttpPost {

    /**
	 * @param uRL
	 */
	public MyHttpDelete(String URL) {
		super(URL);
	}

	@Override
    public String getMethod() {
        return "DELETE";
    }
}
