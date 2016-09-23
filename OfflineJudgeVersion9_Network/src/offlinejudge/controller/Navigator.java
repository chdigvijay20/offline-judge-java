package offlinejudge.controller;

import java.io.IOException;

import javafx.fxml.FXMLLoader;

public class Navigator {
	
	public static final String MAIN		     = "/offlinejudge/fxml/Main.fxml";
	public static final String HOME 			 = "/offlinejudge/fxml/Home.fxml";
	
	public static final String PROBLEMSETTERHOME	 = "/offlinejudge/fxml/problemsetter/ProblemSetterHome.fxml";
	public static final String PROBLEMINPUT 	 = "/offlinejudge/fxml/problemsetter/ProblemInput.fxml";
	public static final String SCOREINPUT 	 = "/offlinejudge/fxml/problemsetter/ScoreInput.fxml";
	
	public static final String HACKERHOME 	 = "/offlinejudge/fxml/hacker/HackerHome.fxml";
	public static final String SUBMISSIONINFORMATION 	 = "/offlinejudge/fxml/hacker/SubmissionInformation.fxml";
	
	private static MainController mainController;
	
	public static void setMainController(MainController mainController) {
		Navigator.mainController = mainController;
	}
	
	public static void loadScene(String fxmlFile) {
        try {
			mainController.setScene(
				FXMLLoader.load(
					Navigator.class.getResource(
						fxmlFile
					)
				)
			);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}