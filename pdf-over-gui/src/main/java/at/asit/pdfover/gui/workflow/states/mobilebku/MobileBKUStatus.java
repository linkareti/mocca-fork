/*
 * Copyright 2012 by A-SIT, Secure Information Technology Center Austria
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * http://joinup.ec.europa.eu/software/page/eupl
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 */
package at.asit.pdfover.gui.workflow.states.mobilebku;

// Imports
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */
public class MobileBKUStatus {
	/**
	 * SLF4J Logger instance
	 **/
	private static final Logger log = LoggerFactory
			.getLogger(MobileBKUStatus.class);

	public static final int MOBILE_MAX_TAN_TRIES = 3;
	
	/**
	 * Constructor
	 */
	public MobileBKUStatus() {
		// TODO: Fill number and password with possible config values!
	}
	
	String viewstate;

	String eventvalidation;

	String sessionID;

	String phoneNumber;
	
	String mobilePassword;

	String baseURL;
	
	String vergleichswert;
	
	String errorMessage;
	
	String tan;
	
	int tanTries = MOBILE_MAX_TAN_TRIES;
	
	/**
	 * @return the tanTries
	 */
	public int getTanTries() {
		return this.tanTries;
	}

	/**
	 * Decreases the TAN Tries!
	 */
	public void decreaseTanTries() {
		this.tanTries--;
	}
	
	/**
	 * @return the tan
	 */
	public String getTan() {
		return this.tan;
	}

	/**
	 * @param tan the tan to set
	 */
	public void setTan(String tan) {
		this.tan = tan;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return this.errorMessage;
	}

	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * @return the vergleichswert
	 */
	public String getVergleichswert() {
		return this.vergleichswert;
	}

	/**
	 * @param vergleichswert the vergleichswert to set
	 */
	public void setVergleichswert(String vergleichswert) {
		this.vergleichswert = vergleichswert;
	}

	/**
	 * @return the credentialsFormAction
	 */
	public String getBaseURL() {
		return this.baseURL;
	}

	/**
	 * @param credentialsFormAction the credentialsFormAction to set
	 */
	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}

	/**
	 * @return the viewstate
	 */
	public String getViewstate() {
		return this.viewstate;
	}

	/**
	 * @param viewstate
	 *            the viewstate to set
	 */
	public void setViewstate(String viewstate) {
		this.viewstate = viewstate;
	}
	
	/**
	 * @return the eventvalidation
	 */
	public String getEventvalidation() {
		return this.eventvalidation;
	}

	/**
	 * @param eventvalidation the eventvalidation to set
	 */
	public void setEventvalidation(String eventvalidation) {
		this.eventvalidation = eventvalidation;
	}
	
	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * @return the mobilePassword
	 */
	public String getMobilePassword() {
		return this.mobilePassword;
	}

	/**
	 * @param mobilePassword the mobilePassword to set
	 */
	public void setMobilePassword(String mobilePassword) {
		this.mobilePassword = mobilePassword;
	}
	
	/**
	 * @return the identification_url
	 */
	public String getSessionID() {
		return this.sessionID;
	}

	/**
	 * @param sessionID the identification_url to set
	 */
	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}


}