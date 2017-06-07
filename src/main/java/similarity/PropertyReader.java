/* *****************************************************************************
 * Copyright (c) {2017} {Software Design and Collaboration Laboratory (SDCL)
 *				, University of California, Irvine}.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    {Software Design and Collaboration Laboratory (SDCL)
 *	, University of California, Irvine}
 *			- initial API and implementation and/or initial documentation
 *******************************************************************************/

/*
 * Created by Thomas Kwak
 */

package similarity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

class PropertyReader {

	/**
	 * Reads the config file for configuring the Index Manager
	 * @param fileLocation location of file
	 * @return x
	 */
	static Map<String, String> fileToStringStringMap(String fileLocation) {
		Map<String, String> map = new HashMap<>();

		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileLocation), "UTF-8"))) {
			for(String line; (line = br.readLine()) != null; ) {
				if(!line.startsWith("#") && line.contains("=")) {
					map.put(line.split("=")[0], line.split("=")[1]);
				}
			}
		} catch (IOException e) {
			throw new IllegalArgumentException("[ERROR]: config file not found!");
		}

		return map;
	}
}
