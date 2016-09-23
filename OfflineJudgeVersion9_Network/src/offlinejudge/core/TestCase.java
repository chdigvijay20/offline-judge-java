package offlinejudge.core;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

import offlinejudge.alerts.WarningAlert;

public class TestCase {
	private File testCaseDirectory;
	private File inputDirectory;
	private File outputDirectory;
	private File[] inputFiles ;
	private File[] outputFiles;
	
	public TestCase(File testCaseDirectory) {
		this.testCaseDirectory = testCaseDirectory;
		inputDirectory  = new File(testCaseDirectory.getAbsoluteFile()+"/input");
		outputDirectory = new File(testCaseDirectory.getAbsoluteFile()+"/output");
	}
	
	public TestCase(File testCaseDirectory, String string) {
		this.testCaseDirectory = testCaseDirectory;
		inputDirectory  = new File(testCaseDirectory.getAbsoluteFile()+"/input");
		outputDirectory = new File(testCaseDirectory.getAbsoluteFile()+"/output");
		saveInputOutputFiles(testCaseDirectory);
	}

	public boolean isTestCaseDirectoryValid() {

		if(!doesTestDirectoryContainRequiredFolders()) {
			return false;
		}
		
		saveInputOutputFiles(getTestCaseDirectory());
		if(!isNameOfInputOutputFilesSame()) {
			return false;
		}
		
		return true;
	}
	
	public void saveInputOutputFiles(File testCaseDirectory) {
		inputFiles = inputDirectory.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".txt");
			}
		});
		
		outputFiles = outputDirectory.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".txt");
			}
		});

		Arrays.sort(inputFiles);
		Arrays.sort(outputFiles);
	}
	
	public boolean doesTestDirectoryContainRequiredFolders() {
		
		if(!inputDirectory.isDirectory()) {
			new WarningAlert("Error", "Invalid Test Case directory path", "Input Test Cases directory does not exist in\nthe given Test Case directory.");
			return false;
		}
		
		if(!outputDirectory.isDirectory()) {
			new WarningAlert("Error", "Invalid Test Case directory path", "Output Test Cases directory does not exist in\nthe given Test Case directory.");
			return false;
		}
		
		return true;
	}
	
	public boolean isNameOfInputOutputFilesSame() {
		
		for (int i = 0; i < inputFiles.length; i++) {
			if(!inputFiles[i].getName().equals(outputFiles[i].getName())) {
				
				if(inputFiles[i].getName().compareTo(outputFiles[i].getName()) < 0) {
					new WarningAlert("Error", "Input-Output file names do not match", "For Input file \"" + inputFiles[i].getName() + "\" there is no file with same name\nin the Output Test Case directory.");
				} else {
					new WarningAlert("Error", "Input-Output file names do not match", "For Output file \"" + outputFiles[i].getName() + "\" there is no file with same name\nin the Input Test Case directory.");
				}
				return false;
			}
		}
		
		return true;
	}
	
	public File getInputDirectory() {
		return inputDirectory;
	}
	
	public File getOutputDirectory() {
		return outputDirectory;
	}
	
	public File getTestCaseDirectory() {
		return testCaseDirectory;
	}
	
	public File[] getInputFiles() {
		return inputFiles;
	}
	
	public File[] getOutputFiles() {
		return outputFiles;
	}
	
	public void setInputFiles(File[] inputFiles) {
		this.inputFiles = inputFiles;
	}
	
	public void setOutputFiles(File[] outputFiles) {
		this.outputFiles = outputFiles;
	}
}