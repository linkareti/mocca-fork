/*
 * Copyright 2018 by Graz University of Technology, Austria
 * MOCCA has been developed by the E-Government Innovation Center EGIZ, a joint
 * initiative of the Federal Chancellery Austria and Graz University of Technology.
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 * http://www.osor.eu/eupl/
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and
 * limitations under the Licence.
 *
 * This product combines work with different licenses. See the "NOTICE" text
 * file for details on the various modules and licenses.
 * The "NOTICE" text file is part of the distribution. Any derivative works
 * that you distribute must include a readable copy of the "NOTICE" text file.
 */
package at.gv.egiz.updater;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.JDialog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.gv.egiz.slbinding.impl.SignatureLocationType;


public class MoccaUpdater  {
	
	private String MoccaVersionLocal = null;
	private String MoccaVersionOnline = null; 
    private final Logger log = LoggerFactory.getLogger(SignatureLocationType.class);
	
	public MoccaUpdater(String version) {
		setMoccaVersionLocal(version);
	}
	
	private String getMoccaVersionOnline() {
		return MoccaVersionOnline;
	}

	private void setMoccaVersionOnline(String moccaVersionOnline) {
		MoccaVersionOnline = moccaVersionOnline;
	}
	
	private String getMoccaVersionLocal() {
		return MoccaVersionLocal;
	}

	private void setMoccaVersionLocal(String moccaVersionLocal) {
		MoccaVersionLocal = moccaVersionLocal;
	}
	
	
	public void run() {
		
		if (getMoccaVersionLocal().toLowerCase().equals("unknown")) {
			// we are finished here
			return; 
		}
		
		try {
			gatherOnlineMoccaVersion();
			boolean isOnlineNewer = isOnlineVersionNewer();
			if (isOnlineNewer) {
				notifyUserNewerVersionOnline();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			log.error(e.getMessage());
		}
	}
	
	
	private void gatherOnlineMoccaVersion() throws Exception {
		try {
			log.info("Requesting Mocca Online Version");
			URL url = new URL(Constants.VERSION_FILE_URL);
			log.debug("Going to GET mocca Version from: " + Constants.VERSION_FILE_URL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			StringBuilder result = new StringBuilder();
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			rd.close();
			setMoccaVersionOnline(result.toString());
			log.info("Online Mocca Version: " + result.toString());
		} catch (IOException e) {
			log.error("Error when gathering Mocca Online Version " + e.getMessage());
			throw new Exception("Error gathering Mocca Version number from Website ");
		}

	}
	
	private boolean isOnlineVersionNewer() {
		
		VersionComparator comparator = new VersionComparator();
		int result = comparator.compare(getMoccaVersionOnline(), getMoccaVersionLocal());
		return (result>0) ? true : false; 
	}
	
	private void notifyUserNewerVersionOnline() {
		try {
			NewVersionDialog dialog = new NewVersionDialog(getMoccaVersionOnline());
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			System.err.print(e.getMessage());
			log.error(e.getMessage());
		}
		
	}
	
	
	public static void main(String[] args)  {
		// only for testing purposes 
		MoccaUpdater updater = new MoccaUpdater("1.2.3");
		updater.run();
	}


	
	
	
	

}
