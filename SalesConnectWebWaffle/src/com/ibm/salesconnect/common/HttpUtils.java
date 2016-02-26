/**
 * 
 */
package com.ibm.salesconnect.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;


/**
 * @author timlehane
 * @date Jul 30, 2013
 */
public class HttpUtils {
	Logger log = LoggerFactory.getLogger(HttpUtils.class);

	/**
	 * Send a GET request inserting 200 as the expected response
	 * @param urlStr Base URL
	 * @param headers GET Request Headers
	 * @return Response as a string
	 */
	public String getRequest(String urlStr, String[] headers){
		return getRequest(urlStr, headers, "200");
	}
	/**
	 * Send a GET request and check that response code matches expected
	 * @param urlStr Base URL
	 * @param headers GET Request Headers
	 * @param expectedResponse Expected Response code, 999 if you don't care
	 * @return Response as a string
	 */
	public String getRequest(String urlStr, String[] headers, String expectedResponse){
		CloseableHttpClient httpclient = getCloseableHttpClient();  
		HttpGet getRequest = new HttpGet(urlStr);
		
		if (headers!=null) {
			for (int i = 0; i < headers.length; i++) {
				getRequest.addHeader(headers[i], headers[i+1]);
				i+=2;
			}
		}
		
		log.info("Request URL: " +urlStr);

		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(getRequest);
		} catch (ClientProtocolException e1) {
			log.info(e1.getMessage());
			Assert.assertTrue(false,"Error with client protocol for GET request");
		} catch (IOException e1) {
			log.info(e1.getMessage());
			Assert.assertTrue(false,"IOException with GET request: " + e1);
		}

		StringBuilder responseString = new StringBuilder();

		try {
			BufferedReader br;
			br = new BufferedReader(new InputStreamReader((response.getEntity()
					.getContent())));

			String output;

			while ((output = br.readLine()) != null) {
				responseString.append(output);
			}

		} catch (IOException e) {
			log.info(e.getMessage());
			Assert.assertTrue(false, "IOException with POST response");
		}
		
		//If expected response is not 999 then compare to expected
		if (!expectedResponse.equalsIgnoreCase("999")) 
		{
		Assert.assertEquals(Integer.toString(response.getStatusLine().getStatusCode()), expectedResponse,"Status Code from Get request is not as expected" + responseString.toString());
		}
		
		try {
			response.close();
			httpclient.close();
		} catch (IOException e) {
			e.printStackTrace();
			Assert.assertTrue(false, "IOException when closing http connection");
		}

		log.info("Response: " +responseString.toString());
		return responseString.toString();
		}
	
	/**
	 * Send a delete request adding 200 as expected response
	 * @param URL Request url
	 * @param headers Request headers
	 * @return Response as a string
	 */
	public String deleteRequest(String URL, String[] headers){
		return deleteRequest(URL, headers, "200");
	}
	
	/**
	 * Send a delete request and return the response as a string
	 * @param URL Request url
	 * @param headers Request headers
	 * @param expectedResponse Expected Response code, 999 if you don't care
	 * @return String of the response content
	 */
	public String deleteRequest(String URL, String[] headers, String expectedResponse){
		
		CloseableHttpClient httpclient = getCloseableHttpClient();
		
		HttpDelete httpDelete = new HttpDelete(URL);
		
		for (int i = 0; i < headers.length; i++) {
			httpDelete.addHeader(headers[i], headers[i+1]);
			i+=2;
		}
		
		log.info("Request URL: " +URL);
		
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httpDelete);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Assert.assertTrue(false,"ClientProtocol failure when sending delete request");
		} catch (IOException e) {
			e.printStackTrace();
			Assert.assertTrue(false,"IOException when sending post request");
		}
			
		StringBuilder responseString = new StringBuilder();
		try {
			BufferedReader br;
			br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
			String output;

			while ((output = br.readLine()) != null) {
				responseString.append(output);
				}
		}
		catch (IOException e) {
			Assert.assertTrue(false, "IOException with DELETE response");
		}
		
		//If expected response is not 999 then compare to expected
		if (!expectedResponse.equalsIgnoreCase("999")) {
		Assert.assertEquals(Integer.toString(response.getStatusLine().getStatusCode()), expectedResponse,"Status Code from Delete request is not " + 
				expectedResponse + " as expected, but was "+Integer.toString(response.getStatusLine().getStatusCode())+ " Body: "  + responseString.toString());
		}
		
		try {
			response.close();
			httpclient.close();
		} catch (IOException e) {
			e.printStackTrace();
			Assert.assertTrue(false, "IOException when closing http connection");
		}

		log.info("Response: " +responseString.toString());
		return responseString.toString(); 
	}
	
	/**
	 * Send a delete request and return the response as a string
	 * @param URL Request url
	 * @param headers Request headers
	 * @param expectedResponse Expected Response code, 999 if you don't care
	 * @return String of the response content
	 */
	public String deleteRequest(String URL, String[] headers, String body, String expectedResponse){
		
		CloseableHttpClient httpclient = getCloseableHttpClient();
		
		MyHttpDelete httpDelete = new MyHttpDelete(URL);
		
		for (int i = 0; i < headers.length; i++) {
			httpDelete.addHeader(headers[i], headers[i+1]);
			i+=2;
		}
		StringEntity requestEntity = new StringEntity(
			    body,ContentType.create("application/json"));

		httpDelete.setEntity(requestEntity);
		
		log.info("Request URL: " +URL);
		
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httpDelete);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Assert.assertTrue(false,"ClientProtocol failure when sending delete request");
		} catch (IOException e) {
			e.printStackTrace();
			Assert.assertTrue(false,"IOException when sending post request");
		}
			
		StringBuilder responseString = new StringBuilder();
		try {
			BufferedReader br;
			br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
			String output;

			while ((output = br.readLine()) != null) {
				responseString.append(output);
				}
		}
		catch (IOException e) {
			Assert.assertTrue(false, "IOException with DELETE response");
		}
		
		//If expected response is not 999 then compare to expected
		if (!expectedResponse.equalsIgnoreCase("999")) {
		Assert.assertEquals(Integer.toString(response.getStatusLine().getStatusCode()), expectedResponse,"Status Code from Delete request is not " + 
				expectedResponse + " as expected, but was "+Integer.toString(response.getStatusLine().getStatusCode())+ " Body: "  + responseString.toString());
		}
		
		try {
			response.close();
			httpclient.close();
		} catch (IOException e) {
			e.printStackTrace();
			Assert.assertTrue(false, "IOException when closing http connection");
		}

		log.info("Response: " +responseString.toString());
		return responseString.toString(); 
	}
	
	/**
	 * Sends a put request adding 200 as the expected response
	 * @param url Request URL
	 * @param headers Request headers
	 * @param body Request Body
	 * @param contentType Request content type
	 * @return Response as a string
	 */
	public String putRequest(String url, String[] headers, String body, String contentType){
		return putRequest(url, headers, body, contentType, "200");
	}
	
	/**
	 * Sends a put request and checks against the expected response
	 * @param url Request URL
	 * @param headers Request headers
	 * @param body Request Body
	 * @param contentType Request content type
	 * @param expectedResponse Expected response, 999 if you don't care
	 * @return Response as a string
	 */
	public String putRequest(String url, String[] headers, String body, String contentType, String expectedResponse) {
		   
		CloseableHttpClient httpclient = getCloseableHttpClient();
           
		HttpPut putRequest = new HttpPut(url);
		
		String headerString = "";
		for (int i = 0; i < headers.length; i++) {
			putRequest.addHeader(headers[i], headers[i+1]);
			headerString += headers[i] + " " + headers[i+1];
			i+=2;
		}
		log.info("Request URL: " +url);
		log.info("Request Body: " +body);
		StringEntity input = null;
		
		try {
			input = new StringEntity(body);
		} catch (UnsupportedEncodingException e1) {
			Assert.assertTrue(false,"Incorrect format for PUT body");
		}
		
		input.setContentType(contentType);
		
		putRequest.setEntity(input);
		
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(putRequest);
		} catch (ClientProtocolException e1) {
			log.info(e1.getMessage());
			Assert.assertTrue(false,"Error with client protocol for PUT request");
		} catch (IOException e1) {
			log.info(e1.getMessage());
			Assert.assertTrue(false,"IOException with PUT request: " + e1);
		}
		
		StringBuilder responseString = new StringBuilder();
		
		try {
			BufferedReader br;
			br = new BufferedReader( new InputStreamReader((response.getEntity().getContent())));

			String output;

			while ((output = br.readLine()) != null) {
				responseString.append(output);
			}
		}
		catch (IOException e) {
			Assert.assertTrue(false, "IOException with PUT response");
		}
		
		//If expected response is not 999 then compare to expected
		if (!expectedResponse.equalsIgnoreCase("999")) {
			log.info("Status Code: "+Integer.toString(response.getStatusLine().getStatusCode()));
		Assert.assertEquals(Integer.toString(response.getStatusLine().getStatusCode()), expectedResponse,"Status Code from Put request is not as expected" + responseString.toString());
		}
		
		try {
			response.close();
			httpclient.close();
		} catch (IOException e) {
			e.printStackTrace();
			Assert.assertTrue(false, "IOException when closing http connection");
		}
 
		log.info("Response: " +responseString.toString());
		return responseString.toString(); 
	}	
	
	/**
	 * Sends a post request adding the expected response type of 200
	 * @param URL Base url
	 * @param headers Post request headers
	 * @param body Post body as a string
	 * @param contentType Content type of the post request
	 * @return The response to the request as a string
	 */
	public String postRequest(String URL, String[] headers, String body, String contentType){
		return postRequest(URL, headers, body, contentType, "200");
	}

	/**
	 * Sends a post request changing the json body to a string
	 * @param URL Base url
	 * @param headers Post request headers
	 * @param json Body of the post request as a json object
	 * @param contentType Content type of the post request
	 * @param expectedResponse Expected response code, 999 if you don't care
	 * @return The response to the request as a string
	 */
	public String postRequest(String URL, String[] headers, JSONObject json, String contentType, String expectedResponse){
		return postRequest(URL, headers, json.toString(), contentType, expectedResponse);
	}
	

	/**
	 * Sends a post request and checks if the response code equals the expected
	 * @param URL Base url
	 * @param headers Post request headers
	 * @param body Body of the post request as a string
	 * @param contentType Content type of the post request
	 * @param expectedResponse Expected response code, 999 if you don't care
	 * @return The response to the request as a string
	 */
	public String postRequest(String URL, String[] headers, String body, String contentType, String expectedResponse){
		
		CloseableHttpClient httpclient = getCloseableHttpClient();
		
		
		
		HttpPost httpPost = new HttpPost(URL);
		
		for (int i = 0; i < headers.length; i++) {
			httpPost.addHeader(headers[i], headers[i+1]);
			i+=2;
		}
		log.info("Request URL: " +URL);
		log.info("Request Body: " +body);
		StringEntity requestEntity = new StringEntity(
			    body,ContentType.create(contentType));

		httpPost.setEntity(requestEntity);
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httpPost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Assert.assertTrue(false,"ClientProtocol failure when sending post request");
		} catch (IOException e) {
			e.printStackTrace();
			Assert.assertTrue(false,"IOException when sending post request");
		}
		
		StringBuilder responseString = new StringBuilder();
		try {
			BufferedReader br;
			br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
			String output;

			while ((output = br.readLine()) != null) 
			{
				responseString.append(output);
			}
		}
		catch (IOException e) {
			log.info(e.getMessage());
			Assert.assertTrue(false, "IOException with POST response");
		}
		
		//If expected response is not 999 then compare to expected
		if (!expectedResponse.equalsIgnoreCase("999")) {
			if(!Integer.toString(response.getStatusLine().getStatusCode()).equals(expectedResponse)){
				log.info("Response Body: " + responseString.toString());
				log.info("Expected response: " + expectedResponse +", Actual Response: " + Integer.toString(response.getStatusLine().getStatusCode()));
				Assert.assertTrue(false,"Status Code from Post request is not as expected" + responseString.toString());
			}
		}
		try {
			response.close();
			httpclient.close();
		} catch (IOException e) {
			e.printStackTrace();
			Assert.assertTrue(false, "IOException when closing http connection");
		}

		log.info("Response Body: " + responseString.toString());
		
		return responseString.toString(); 
	}
	
	
	/**
	 * Sends a post request and checks if the response code equals the expected
	 * @param URL Base url
	 * @param headers Post request headers
	 * @param body Body of the post request as a string
	 * @param contentType Content type of the post request
	 * @param expectedResponse Expected reponse code, 999 if you don't care
	 * @return The response to the request as a string
	 */
	public Header[] postRequestReturnHeaders(String URL, String[] headers, String body, String contentType, String expectedResponse){
		
		CloseableHttpClient httpclient = getCloseableHttpClient();
		
		HttpPost httpPost = new HttpPost(URL);
		
		for (int i = 0; i < headers.length; i++) {
			httpPost.addHeader(headers[i], headers[i+1]);
			i+=2;
		}
		log.info("Request URL: " +URL);
		log.info("Request Body: " +body);
		StringEntity requestEntity = new StringEntity(
			    body,ContentType.create(contentType));

		httpPost.setEntity(requestEntity);
		
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httpPost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Assert.assertTrue(false,"ClientProtocol failure when sending post request");
		} catch (IOException e) {
			e.printStackTrace();
			Assert.assertTrue(false,"IOException when sending post request");
		}
		
		StringBuilder responseString = new StringBuilder();
		try {
			BufferedReader br;
			br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
			String output;

			while ((output = br.readLine()) != null) {
				responseString.append(output);
				}
		}
		catch (IOException e) {
			log.info(e.getMessage());
			Assert.assertTrue(false, "IOException with POST response");
		}
		Header[]temp = null;
		//If expected response is not 999 then compare to expected
		if (!expectedResponse.equalsIgnoreCase("999")) {
			if(!Integer.toString(response.getStatusLine().getStatusCode()).equals(expectedResponse)){
				log.info("Response Body: " + responseString.toString());
				log.info("Expected response: " + expectedResponse +", Actual Response: " + Integer.toString(response.getStatusLine().getStatusCode()));
				Assert.assertTrue(false,"Status Code from Post request is not as expected" + responseString.toString());
			}
			temp = response.getAllHeaders();
		}
		try {
			response.close();
			httpclient.close();
		} catch (IOException e) {
			e.printStackTrace();
			Assert.assertTrue(false, "IOException when closing http connection");
		}

		log.info("Response Body: " + responseString.toString());
		
		return temp; 
	}

	/**
	 * Gets a CloseableHttpClient which accepts all clients/certs
	 * @return CloseableHttpClient
	 */
	private CloseableHttpClient getCloseableHttpClient(){
		  System.setProperty("jsse.enableSNIExtension","false");
		  SSLContext sslContext = null;
		try {
			sslContext = SSLContext.getInstance("TLSv1");
			System.setProperty("https.protocols", "TLSv1");
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
			Assert.assertTrue(false,"NoSuchAlgorithmException failure when sending post request");
		}

		
		// set up a TrustManager that trusts everything
		try {
			sslContext.init(null, new TrustManager[] { new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {
					log.info("getAcceptedIssuers =============");
				            return null;
				}

				public void checkClientTrusted(X509Certificate[] certs,String authType) {
				    log.info("checkClientTrusted =============");
				}

				public void checkServerTrusted(X509Certificate[] certs,String authType) {
				    log.info("checkServerTrusted =============");
				}
			} }, new java.security.SecureRandom());
			
		} catch (KeyManagementException e1) {
			e1.printStackTrace();
			Assert.assertTrue(false,"KeyManagementException failure when sending post request");
		}
	

		 return HttpClients.custom().setSSLSocketFactory(new SSLConnectionSocketFactory(sslContext,SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)).build();	
	}
}
