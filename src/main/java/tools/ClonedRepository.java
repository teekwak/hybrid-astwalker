package tools;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

class ClonedRepository {
	private String rawGithubURL;
	private String githubCloneURL;
	private String localCloneDirectory;

	ClonedRepository(String r, String g, String l) {
		this.rawGithubURL = r;
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

	String getFileName() {
		return this.rawGithubURL.split("/")[this.rawGithubURL.split("/").length - 1].split("\\?start=")[0];
	}
}
