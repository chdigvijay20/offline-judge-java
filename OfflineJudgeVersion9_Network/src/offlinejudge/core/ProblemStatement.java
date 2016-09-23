package offlinejudge.core;

import java.io.File;

public class ProblemStatement {
	private String problemName;
	private String problemCode;
	private File problemStatementFile;
	
	public ProblemStatement(String problemName, String problemCode, File problemStatementFile) {
		this.problemName = problemName;
		this.problemCode = problemCode;
		this.problemStatementFile = problemStatementFile;
	}
	
	public ProblemStatement(String problemName) {
		this.problemName = problemName;
	}
	
	public ProblemStatement(String problemName, File problemStatementFile) {
		this.problemName = problemName;
		this.problemStatementFile = problemStatementFile;
	}
	
	public ProblemStatement(String problemName, String problemCode) {
		this.problemName = problemName;
		this.problemCode = problemCode;
	}
	
	public File getProblemStatementFile() {
		return problemStatementFile;
	}
	
	public String getProblemCode() {
		return problemCode;
	}
	
	public String getProblemName() {
		return problemName;
	}
}