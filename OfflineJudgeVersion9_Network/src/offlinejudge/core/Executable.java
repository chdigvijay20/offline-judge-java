package offlinejudge.core;

import java.io.File;

public class Executable {
	private File executableFile;
	
	public Executable(File executableFile) {
		this.executableFile = executableFile;
	}
	
	public void setExecutableFile(File executableFile) {
		this.executableFile = executableFile;
	}
	
	public File getExecutableFile() {
		return executableFile;
	}
}
