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

import java.util.*;
import java.io.*;

// data for each commit
class Commit {
	private String author;

	void setAuthor(String a) {
		this.author = a;
	}

	String getAuthor() {
		return this.author;
	}
}

// data for each file
class JavaGitHubData {
	private List<Commit> listOfCommits;

	JavaGitHubData() {
		listOfCommits = new ArrayList<>();
	}
	
	List<Commit> getListOfCommits() {
		return this.listOfCommits;
	}

	void getCommits(String repoFileName) {
		List<String> hashCodeList = new ArrayList<>();
		List<String> authorList = new ArrayList<>();

		try {
			// get all commits of a single file
			ProcessBuilder pb = new ProcessBuilder("bash", "resources/getCommits.sh", repoFileName);

			Process proc = pb.start();

			String s;
			try(BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream(), "UTF-8"))) {
				while((s = br.readLine()) != null) {
					if(s.startsWith("Commit")) {
						hashCodeList.add(s.split(" ")[1]);
					}
					else if(s.startsWith("Author")) {
						try {
							authorList.add(s.split(" ")[1]);
						} catch (ArrayIndexOutOfBoundsException e) {
							authorList.add("");
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			proc.destroy();
		} catch (IOException e) {
			e.printStackTrace();
		}

		int hashCodeListSize = hashCodeList.size();
		for(int i = 0; i < hashCodeListSize; i++) {
			Commit cd = new Commit();
			cd.setAuthor(authorList.get(i));
			this.listOfCommits.add(cd);
		}
	}
}