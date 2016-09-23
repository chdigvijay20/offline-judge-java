package offlinejudge.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Judge {
	
	public void judgeExecutable(Executable executable, TestCase testCase, ArrayList<Score> scores, int timeLimit) {
		
		File outputFile = new File(executable.getExecutableFile().getParentFile() + "/" + executable.getExecutableFile().getName() + "output.txt");
		
		int numberOfFiles = testCase.getInputFiles().length;
		
		for (int i = 0; i < numberOfFiles; i++) {

			String status = runProgram(executable.getExecutableFile(), testCase.getInputFiles()[i], outputFile, timeLimit);
			
			if(status.equals(Status.TLE)) {
				scores.get(i).setRunStatus(status);
				scores.get(i).setAchievedScore(0.0);
			} else if(status.equals(Status.RE)) {
				scores.get(i).setRunStatus(status);
				scores.get(i).setAchievedScore(0.0);
			} else {
				if(areFilesIdentical(testCase.getOutputFiles()[i], outputFile)) {
					scores.get(i).setRunStatus(Status.AC);
					scores.get(i).setAchievedScore(scores.get(i).getMaximumScore());
				} else {
					scores.get(i).setRunStatus(Status.WE);
					scores.get(i).setAchievedScore(0.0);
				}
			}
		}
	}
	
	public String runProgram(File executableFile, File inputfile, File outputFile, long timeLimit) {

		ProcessBuilder processBuilder= new ProcessBuilder(executableFile.getAbsolutePath());
		processBuilder.redirectInput(inputfile);
		processBuilder.redirectOutput(outputFile);
		
		TimeLimitProcess timeLimitProcess = new TimeLimitProcess(processBuilder);

		timeLimitProcess.start();

		try {

			timeLimitProcess.join(timeLimit);
			if(timeLimitProcess.exit != null) {
				return Status.NOTLE;
			} else {
				timeLimitProcess.interrupt();
				return Status.TLE;
			}
			
		} catch (Exception e) {
			return Status.RE;
		}
	}
	
	class TimeLimitProcess extends Thread {
		ProcessBuilder processBuilder;
		Integer exit;
		
		public TimeLimitProcess(ProcessBuilder processBuilder) {
			this.processBuilder = processBuilder;
		}
		
		@Override
		public void run() {
			
			try {
				exit = processBuilder.start().waitFor();
			} catch (InterruptedException e) {
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean areFilesIdentical(File expectedOutput, File actualOutput) {
		
		BufferedReader expectedReader = null;
		BufferedReader actualReader = null;
		
		try {
			expectedReader = new BufferedReader(new FileReader(expectedOutput));
			actualReader   = new BufferedReader(new FileReader(actualOutput));
			
			String expected = expectedReader.readLine();
			String actual   = actualReader.readLine();
			
			while(expected!=null && actual!=null){
				
				expected = expected.trim();
				actual   = actual.trim();

				if (!actual.equals(expected)) {
					return false;
				}
				expected = expectedReader.readLine();
				actual   = actualReader.readLine();
			}
			
			if(actual != null || expected != null) {
				return false;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
			try {
				actualReader.close();
				expectedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return true ;
	}
}

