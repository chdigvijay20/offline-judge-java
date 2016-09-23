package offlinejudge.controller.hacker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import offlinejudge.controller.Main;
import offlinejudge.controller.Navigator;
import offlinejudge.core.ProblemStatement;
import offlinejudge.database.RetrieveProblem;
import offlinejudge.network.OfflineJudgeClient;

public class HackerHomeController implements Initializable {
	
	private int numberOfProblems;
	private ArrayList<Button> problemButtons;
	private ArrayList<ProblemStatement> problemStatements;
	
	@FXML private Label problemStatementNameLabel;
	@FXML private TextArea problemStatementTextArea;
	@FXML private HBox problemButtonsBox;
	@FXML private TextField solutionTextField;
	@FXML private Button browseButton;
	@FXML private Button submitButton;
	
	private String currentProblemName;
	private File solutionFile;
	private File possibleFolder;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initializeProblemButtons();
		initializeListeners();
		problemButtons.get(0).fire();
	}
	
	public void initializeProblemButtons() {
		numberOfProblems = (new RetrieveProblem()).getNumberOfProblemStatements();
		problemStatements = (ArrayList<ProblemStatement>) (new RetrieveProblem()).getProblemStatementList();
		
		problemButtons = new ArrayList<>();
		for (int i = 0; i < numberOfProblems; i++) {
			problemButtons.add(new Button(new String(Character.toChars(i+'A'))));
			problemButtonsBox.getChildren().add(problemButtons.get(i));
		}
	}
	
	public void initializeListeners() {
		
		for (int i = 0; i < numberOfProblems; i++) {
			ProblemStatement problemStatement = problemStatements.get(i);
			problemButtons.get(i).setOnAction(d -> showProblemStatement(problemStatement));
			problemButtons.get(i).setOnKeyPressed(d -> {
				if(d.getCode() == KeyCode.ENTER) {
					showProblemStatement(problemStatement);
				}
			});
		}
		
		browseButton.setOnKeyPressed(d -> {
			if(d.getCode() == KeyCode.ENTER) {
				browseButtonClicked();
			}
		});
		
		submitButton.setOnKeyPressed(d -> {
			if(d.getCode() == KeyCode.ENTER) {
				submitButtonClicked();
			}
		});
	}
	
	public void showProblemStatement(ProblemStatement problemStatement) {
		problemStatementNameLabel.setText(problemStatement.getProblemName());
		File problemStatementFile = problemStatement.getProblemStatementFile();
		problemStatementTextArea.setText("");
		BufferedReader fileReader = null;
		
		try {
			if(problemStatementFile != null) {
				fileReader = new BufferedReader(new FileReader(problemStatementFile));
				
				String line;
				while((line = fileReader.readLine()) != null) {
					problemStatementTextArea.appendText(line + '\n');
				}
				problemStatementTextArea.setWrapText(true);
				fileReader.close();
			}
			currentProblemName = problemStatement.getProblemName();
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fileReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
		
	public void browseButtonClicked() {
		String[] cppExtensions = {"*.c", "*.cc", "*.cpp", "*.C", "*.cp", "*.cxx", "*.c++"};
		FileChooser.ExtensionFilter cppExtensionFilter = new ExtensionFilter("c/c++", cppExtensions);
		
		FileChooser solutionFileChooser = new FileChooser();
		solutionFileChooser.getExtensionFilters().add(cppExtensionFilter);
		
		if(possibleFolder != null) {
			solutionFileChooser.setInitialDirectory(possibleFolder);
		} else {
			solutionFileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		}
		
		solutionFile = solutionFileChooser.showOpenDialog(Main.window);
		if(solutionFile != null) {
			solutionTextField.setText(solutionFile.getName());
			possibleFolder = solutionFile.getParentFile();
			submitButton.setDisable(false);
		}
	}
	
	public void submitButtonClicked() {
		(new OfflineJudgeClient()).submitSolution(solutionFile, currentProblemName);
	}
}
