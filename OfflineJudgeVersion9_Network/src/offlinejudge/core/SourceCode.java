package offlinejudge.core;

import java.io.File;

public class SourceCode {
	private File solutionfile;
	private String fileExtension;
	
	public SourceCode(File solutionFile) {
		this.solutionfile = solutionFile;
		
		String fileName = new String(solutionFile.getName());
		this.fileExtension = fileName.substring(fileName.lastIndexOf('.'), fileName.length());
	}
	
	public File getSolutionfile() {
		return solutionfile;
	}
	
	public String getFileExtension() {
		return fileExtension;
	}
}