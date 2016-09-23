package offlinejudge.core;

import java.util.List;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Compiler {
	
	private static final String[] CPPEXTENSIONS = {".cc", ".cpp", ".C", ".cp", ".cxx", ".c++"};
	
	public Executable compileSource(SourceCode sourceCode, File compilationError) {
		
		String sourceFileName = sourceCode.getSolutionfile().getName();
		String solutionFileWithoutExtension = sourceCode.getSolutionfile().getParent() + "/" + sourceFileName.substring(0, sourceFileName.lastIndexOf('.'));

		File executableFile = new File(solutionFileWithoutExtension);
		
		List<String> command = null;
		if(Arrays.asList(CPPEXTENSIONS).contains(sourceCode.getFileExtension())) {
			command = Arrays.asList("/usr/bin/g++", "-std=c++11", "-w", sourceCode.getSolutionfile().getAbsolutePath(), "-o", executableFile.getAbsolutePath());
		} else if(sourceCode.getFileExtension().equals(".c")) {
			command = Arrays.asList("/usr/bin/gcc", "-std=c11", "-w", sourceCode.getSolutionfile().getAbsolutePath(), "-o", executableFile.getAbsolutePath());
		}
		
		compile(command, compilationError);
		
		return new Executable(executableFile);
	}
	
	public void compile(List<String> command, File compilationError) {
		
		try {
			
			ProcessBuilder processBuilder = new ProcessBuilder(command);
			processBuilder.redirectError(compilationError);
			Process process = processBuilder.start();

		    process.waitFor();
			process.destroy();
	        
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
