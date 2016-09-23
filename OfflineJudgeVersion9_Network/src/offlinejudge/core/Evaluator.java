package offlinejudge.core;

import java.io.File;

public class Evaluator {
	
	private File compilationError;
	
	public Object evaluate(SourceCode sourceCode, Problem problem) {
		
		String sourceFileName = sourceCode.getSolutionfile().getName();
		String solutionFileWithoutExtension = sourceCode.getSolutionfile().getParent() + "/" + sourceFileName.substring(0, sourceFileName.lastIndexOf('.'));
		
		String compilerErrorFileName = new String(solutionFileWithoutExtension + "compilationError.txt");
		compilationError = new File(compilerErrorFileName);
		
		Compiler compiler = new Compiler();
		Executable executableFile = compiler.compileSource(sourceCode, compilationError);
		
		if(executableFile.getExecutableFile().exists()) {
			
			System.out.println("Compilation successful");
			Judge judge = new Judge();
			judge.judgeExecutable(executableFile, problem.getTestCase(), problem.getScore(), problem.getTimeLimit()*1000);
			
			return null;
			
		} else {
			System.out.println("Compilation error" + compilationError);
			return compilationError;
		}
	}
}
