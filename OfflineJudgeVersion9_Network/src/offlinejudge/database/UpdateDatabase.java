package offlinejudge.database;

import offlinejudge.core.Problem;

public class UpdateDatabase {
	
	public void updateProblem(String problemName, Problem problem) {
		
		(new DeleteProblem()).deleteProblem(problemName);
		
		(new StoreProblem()).storeProblem(problem);
	}
}
