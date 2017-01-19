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

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

class ClonedRepository {
	private String githubCloneURL;
	private String localCloneDirectory;

	ClonedRepository(String g, String l) {
		this.githubCloneURL = g;
		this.localCloneDirectory = l;
	}

	void deleteRepository() {
		try {
			FileUtils.deleteDirectory(new File(this.localCloneDirectory));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void cloneRepository() {
		ProcessBuilder pb = new ProcessBuilder("git", "clone", this.githubCloneURL, this.localCloneDirectory);
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
	 * @param version version hash
	 */
	void resetRepositoryToVersion(String version) {
		try {
			String[] command = {"git", "reset", "--hard", version};

			ProcessBuilder pb = new ProcessBuilder(command);
			pb.directory(new File("clone"));

			Process proc = pb.start();
			proc.waitFor();
			proc.destroy();
		} catch (IOException|InterruptedException e) {
			e.printStackTrace();
		}
	}
}

// if folder does not exist
// go look for the folder (it should honeslty be the only folder in there
