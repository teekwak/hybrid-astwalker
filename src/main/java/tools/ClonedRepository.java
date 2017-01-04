package tools;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class ClonedRepository {
	String rawGithubURL;
	String githubCloneURL;
	String localCloneDirectory;

	ClonedRepository(String r, String g, String l) {
		this.rawGithubURL = r;
		this.githubCloneURL = g;
		this.localCloneDirectory = l;
	}

	String getRawGithubURL() {
		return this.rawGithubURL;
	}

	String getGithubCloneURL() {
		return this.githubCloneURL;
	}

	String getLocalDirectory() {
		return this.localCloneDirectory;
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
