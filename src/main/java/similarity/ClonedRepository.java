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

import java.io.IOException;

class ClonedRepository {
	private String githubCloneURL;
	private String cloneDirectory;

	ClonedRepository(String g, String p) {
		this.githubCloneURL = g;
		this.cloneDirectory = p;
	}

	void cloneRepository() {
		ProcessBuilder pb = new ProcessBuilder("bash", "resources/cloneRepository.sh", this.githubCloneURL, this.cloneDirectory);

		try {
			Process proc = pb.start();
			proc.waitFor();
			proc.destroy();
		} catch (InterruptedException|IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reset the repository to a certain version
	 * @param version version hash of commit
	 */
	void resetRepositoryToVersion(String version) {
		ProcessBuilder pb = new ProcessBuilder("bash", "resources/resetCommit.sh", version, this.cloneDirectory);

		try {
			Process proc = pb.start();
			proc.waitFor();
			proc.destroy();
		} catch (IOException|InterruptedException e) {
			e.printStackTrace();
		}
	}
}
