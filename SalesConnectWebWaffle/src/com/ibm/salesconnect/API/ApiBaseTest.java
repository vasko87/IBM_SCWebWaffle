/**
 * 
 */
package com.ibm.salesconnect.API;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.atmn.waffle.extensions.user.User;
import com.ibm.salesconnect.model.adminconsole.ProductBaseTest;

/**
 * Base class for rest api automation tests
 * 
 * @author brownholtz
 * @date Mar 30, 2015
 * 
 */

public abstract class ApiBaseTest extends ProductBaseTest {

	private static final Logger log = LoggerFactory
			.getLogger(ApiBaseTest.class);

	protected String applicationName;
	protected String apiExtension;
	protected String apimEnvironment;
	protected Boolean APIm;
	protected String clientIDandSecret = "";
	protected String clientID = "";
	protected String clientSecret = "";

	/**
	 * 
	 * @param apiObjectName
	 *            - e.g. tasks, leads
	 * @param applicationName
	 *            -
	 * @param APIM
	 *            - "true" use api manager, "false" direct SC
	 * @param apimEnvironment
	 *            - if api manager, environment endpoint, e.g. "test",
	 *            "development"
	 */
	public ApiBaseTest(String apiObjectName, String applicationName,
			String APIM, String apimEnvironment) {

		this.applicationName = applicationName;
		this.apimEnvironment = apimEnvironment;

		// going thru API manager
		if (APIM.equalsIgnoreCase("true")) {
			APIm = true;

			// requires a clientid and secret
			ClientIdAndSecret idsecret = new ClientIdAndSecret(apimEnvironment);

			// append together for use in query params
			this.clientIDandSecret = idsecret.getIdSecret(applicationName);

			// separate
			this.clientID = clientIDandSecret.substring(0,
					(clientIDandSecret.indexOf("&")));

			this.clientSecret = clientIDandSecret.substring((clientIDandSecret
					.indexOf("&") + 1));
			if ((apiObjectName.contains("evenue"))) {
				// api extension should preserve original case sensitivities (for CCH mapping calls)
				this.apiExtension = "rest/v10/" + apiObjectName;
			}else if (!apiObjectName.contains("ollab")) {
				// api extension must be lowercase
				this.apiExtension = apiObjectName.toLowerCase();
			} else {
				// api extension should preserve original case sensitivities (for CCH mapping calls)
				this.apiExtension = apiObjectName;
			}
		} else {

			// calls directly to SC server
			APIm = false;
			
			if ((apiObjectName.contains("evenue"))) {
				// api extension should preserve original case sensitivities (for CCH mapping calls)
				this.apiExtension = "rest/v10/" + apiObjectName;
			}
			else if (!apiObjectName.contains("ollab")) {
				// api extension for direct calls require capitalized first letter
				String correctCaseExtension = apiObjectName.substring(0, 1)
						.toUpperCase() + apiObjectName.substring(1).toLowerCase();
	
				this.apiExtension = "rest/v10/" + correctCaseExtension;
			}
			else {
				// api extension should preserve original case sensitivities (for CCH mapping calls)
				this.apiExtension = "rest/v10/" + apiObjectName;
			}
		}

	}
	
	public String getClientIdAndSecret(String applicationName){
		ClientIdAndSecret idsecret = new ClientIdAndSecret(apimEnvironment);
		return idsecret.getIdSecret(applicationName);
	}

	public String getclientIDandSecret() {

		return clientIDandSecret;
	}

	public String getclient_ID() {

		return clientID;
	}

	public String getclient_secret() {

		return clientSecret;
	}

	/**
	 * prepare full request url per environment
	 * 
	 * @param additionalUrlPath
	 * @param params
	 * @return String complete request url
	 */
	protected String getRequestUrl(String additionalUrlPath,
			ArrayList<NameValuePair> params) {
		String requestURL = null;

		// prepare addtional path
		if (additionalUrlPath == null || additionalUrlPath.equals("")) {
			additionalUrlPath = "";
		} else {
			if (!additionalUrlPath.startsWith("/"))
				additionalUrlPath = "/" + additionalUrlPath;
		}

		String combinedParams = "";

		if (APIm) {
			// add url query params clientid and secret
			combinedParams += "?" + clientIDandSecret;
			requestURL = getApiManagement() + apiExtension + additionalUrlPath;
		} else {
			requestURL = baseURL + apiExtension + additionalUrlPath;
		}

		if (null != params && !params.isEmpty()) {
			for (NameValuePair param : params) {
				if (combinedParams.length() == 0)
					combinedParams += "?";
				else

				if (combinedParams.length() > 1)
					combinedParams += "&";

				combinedParams += param.getName() + "=" + param.getValue();
			}
		}

		requestURL += combinedParams;

		log.debug("ApiBaseTest:getRequestUrl = " + requestURL);

		return requestURL;
	}
	
	/**
	 * prepare full request url per environment
	 * 
	 * @param additionalUrlPath
	 * @param params
	 * @return String complete request url
	 */
	protected String getRequestUrl(String additionalUrlPath,
			ArrayList<NameValuePair> params, String clientIDAndSecret) {
		String requestURL = null;

		// prepare addtional path
		if (additionalUrlPath == null || additionalUrlPath.equals("")) {
			additionalUrlPath = "";
		} else {
			if (!additionalUrlPath.startsWith("/"))
				additionalUrlPath = "/" + additionalUrlPath;
		}

		String combinedParams = "";

		if (APIm) {
			// add url query params clientid and secret
			combinedParams += "?" + clientIDAndSecret;
			requestURL = getApiManagement() + apiExtension + additionalUrlPath;
		} else {
			requestURL = baseURL + apiExtension + additionalUrlPath;
		}

		if (null != params && !params.isEmpty()) {
			for (NameValuePair param : params) {
				if (combinedParams.length() == 0)
					combinedParams += "?";
				else

				if (combinedParams.length() > 1)
					combinedParams += "&";

				combinedParams += param.getName() + "=" + param.getValue();
			}
		}

		requestURL += combinedParams;

		log.debug("ApiBaseTest:getRequestUrl = " + requestURL);

		return requestURL;
	}

	/**
	 * get OAuth token per environment
	 * 
	 * @param user
	 * @return String oauthtoken
	 */
	protected String getOAuthToken(User user) {

		String token = null;
		if (null != user) {
			token = getOAuthToken(user.getEmail(), user.getPassword());
		}

		return token;
	}
	
	protected String getOAuthToken(User user, String applicationName) {

		String token = null;
		if (null != user) {
			token = getOAuthToken(user.getEmail(), user.getPassword(), applicationName);
		}

		return token;
	}
	
	protected String getOAuthToken(String email, String password, String applicationName){
		String token = null;

		if (null != email && null != password) {
			LoginRestAPI loginRestAPI = new LoginRestAPI();

			if (APIm) {
				log.info("Retrieving OAuth2Token via APIm.");
				token = loginRestAPI.getOAuth2TokenViaAPIManager(
						getApiManagement() + getOAuthExtension(applicationName), email,
						password, "200");
			} else {
				log.info("Retrieving OAuth2Token.");
				token = loginRestAPI.getOAuth2Token(baseURL, email, password);
			}
		}

		return token;
	}

	/**
	 * get OAuth token via email/password per environment
	 * 
	 * @param email
	 * @param password
	 * @return
	 */
	protected String getOAuthToken(String email, String password) {

		String token = null;

		if (null != email && null != password) {
			LoginRestAPI loginRestAPI = new LoginRestAPI();

			if (APIm) {
				log.info("Retrieving OAuth2Token via APIm.");
				token = loginRestAPI.getOAuth2TokenViaAPIManager(
						getApiManagement() + getOAuthExtension(), email,
						password, "200");
			} else {
				log.info("Retrieving OAuth2Token.");
				token = loginRestAPI.getOAuth2Token(baseURL, email, password);
			}
		}

		return token;
	}

	/**
	 * get OAuth token in Header per environment
	 * 
	 * @param user
	 * @return
	 */
	protected String[] getOAuthTokenInHeader(User user) {

		String headers[] = null;

		if (null != user) {

			headers = getOAuthTokenInHeader(user.getEmail(), user.getPassword());
		}

		return headers;
	}

	/**
	 * get OAuth token in Header via email/password per environment
	 * 
	 * @param email
	 * @param password
	 * @return
	 */
	protected String[] getOAuthTokenInHeader(String email, String password) {

		String token = getOAuthToken(email, password);

		String headers[] = { "OAuth-Token", token };

		return headers;
	}

	/**
	 * get oauth portion of url
	 * 
	 * @return
	 */
	protected String getOAuthExtension() {
		if (APIm) {
			return "oauth?" + getclient_ID() + "&" + getclient_secret();
		} else
			return "";
	}
	
	protected String getOAuthExtension(String applicationName) {
		if (APIm) {
			return "oauth?" + getClientIdAndSecret(applicationName);
		} else
			return "";
	}

	/**
	 * append query params clientid and secret to url
	 * 
	 * @param url
	 * @return
	 */
	public String addclientIDAndSecret(String url) {
		return url += "?" + getclientIDandSecret();
	}

	public String getApplicationName() {
		return applicationName;
	}

	public String getmEnvironment() {
		return apimEnvironment;
	}

	public Boolean getAPIm() {
		return APIm;
	}

	public String getApiExtension() {
		return apiExtension;
	};
}