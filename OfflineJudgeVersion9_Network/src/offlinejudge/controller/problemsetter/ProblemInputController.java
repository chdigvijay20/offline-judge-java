package offlinejudge.controller.problemsetter;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import offlinejudge.alerts.WarningAlert;
import offlinejudge.controller.Main;
import offlinejudge.controller.Navigator;
import offlinejudge.controller.NumberTextfield;
import offlinejudge.core.Problem;
import offlinejudge.core.ProblemStatement;
import offlinejudge.core.TestCase;
import offlinejudge.database.RetrieveProblem;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class ProblemInputController implements Initializable {
	
	private static String flag;
	public static String previousFlag;
	public static final String ADD 	  = "ADD";
	public static final String EDIT 	  = "EDIT";
	public static final String DETAILS = "DETAILS";
	public static final String BACK 	  = "BACK";
	
	private static Problem problem;
	
	@FXML private GridPane problemGridPane;
	
	@FXML private TextField codeTextField;
	@FXML private TextField nameTextField;
	@FXML private TextField problemStatementTextField;
	@FXML private TextField testCaseTextField;
	private NumberTextfield timeLimitTextField;
	
	@FXML private Button problemBrowseButton;
	@FXML private Button testCaseBrowseButton;
	@FXML private Button nextButton;
	@FXML private Button cancelButton;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initializeKeyListeners();
		initializeGUI();
		initializeModes();
	}
	
	public void initializeKeyListeners() {
		problemBrowseButton.setOnKeyPressed(d -> {
			if(d.getCode() == KeyCode.ENTER) {
				problemBrowseButtonClicked();
			}
		});
		
		testCaseBrowseButton.setOnKeyPressed(d -> {
			if(d.getCode() == KeyCode.ENTER) {
				testCaseBrowseButtonClicked();
			}
		});
		
		nextButton.setOnKeyPressed(d -> {
			if(d.getCode() == KeyCode.ENTER) {
				nextButtonClicked();
			}
		});
		
		cancelButton.setOnKeyPressed(d -> {
			if(d.getCode() == KeyCode.ENTER) {
				cancelButtonClicked();
			}
		});
	}
	
	public void initializeGUI() {
		timeLimitTextField = new NumberTextfield();
		GridPane.setConstraints(timeLimitTextField, 1, 4);
		problemGridPane.getChildren().add(timeLimitTextField);
	}
	
	@FXML
	public void problemBrowseButtonClicked() {
		FileChooser.ExtensionFilter textFilter = new ExtensionFilter("text", "*.txt");

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Problem Statement");
		if(problemStatementTextField.getText().trim().equals("")) {
			fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		} else {
			fileChooser.setInitialDirectory(new File(problemStatementTextField.getText().trim()).getParentFile());
		}
		fileChooser.getExtensionFilters().add(textFilter);
		
		File file = fileChooser.showOpenDialog(Main.window);
		if(file != null) {
			problemStatementTextField.setText(file.getAbsolutePath());
		}
	}
	
	@FXML
	public void testCaseBrowseButtonClicked() {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Open Test Cases");
		directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		if(testCaseTextField.getText().trim().equals("")) {
			directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		} else {
			directoryChooser.setInitialDirectory(new File(testCaseTextField.getText().trim()));
		}
		
		File file = directoryChooser.showDialog(Main.window);
		if(file != null) {
			testCaseTextField.setText(file.getAbsolutePath());
			toggleNextButton();
		}
	}
	
	@FXML
	public void toggleNextButton() {
		if((!nameTextField.getText().trim().equals("")) && (!testCaseTextField.getText().trim().equals(""))) {
			nextButton.setDisable(false);
		} else {
			nextButton.setDisable(true);
		}
	}
	
	public void cancelButtonClicked() {
		Navigator.loadScene(Navigator.PROBLEMSETTERHOME);
	}
	
	public void nextButtonClicked() {
		
		if(!new TestCase(new File(testCaseTextField.getText().trim())).isTestCaseDirectoryValid()) {
			return;
		}
		
		problem = createProblem();
		
		if(flag.equals(EDIT) && !(problem.getProblemStatement().getProblemName().equals(ProblemSetterHomeController.getProblemName()))) {
			if((new RetrieveProblem()).isEntryDuplicate(nameTextField.getText().trim())) {
				new WarningAlert("Error", "Duplicate Entry", "A problem with same name is already present\nin the database");
				return;
			}
		}
		
		if(flag.equals(ADD) && (new RetrieveProblem()).isEntryDuplicate(nameTextField.getText().trim())) {
			new WarningAlert("Error", "Duplicate Entry", "A problem with same name is already present\nin the database");
			return;
		}
		
		ScoreInputController.setProblem(problem);
		Navigator.loadScene(Navigator.SCOREINPUT);
	}
	
	public Problem createProblem() {
		ProblemStatement problemStatement = createProblemStatement();
		TestCase testCase = new TestCase(new File(testCaseTextField.getText().trim()));
		testCase.saveInputOutputFiles(testCase.getTestCaseDirectory());
		int timeLimit = getTimeLimit();
		
		return new Problem(problemStatement, testCase, timeLimit);
	}
	
	public ProblemStatement createProblemStatement() {
		if(codeTextField.getText().trim().equals("") && problemStatementTextField.getText().trim().equals("")) {
			return new ProblemStatement(nameTextField.getText().trim());
		} else if(codeTextField.getText().trim().equals("")) {
			return new ProblemStatement(nameTextField.getText().trim(), new File(problemStatementTextField.getText().trim()));
		} else if(problemStatementTextField.getText().trim().equals("")) {
			return new ProblemStatement(nameTextField.getText().trim(), codeTextField.getText().trim());
		} else {
			return new ProblemStatement(nameTextField.getText().trim(), codeTextField.getText().trim(), new File(problemStatementTextField.getText().trim()));
		}
	}
	
	public int getTimeLimit() {
		if(timeLimitTextField.getText().trim().equals("")) {
			return 10;
		} else {
			return Integer.parseInt(timeLimitTextField.getText().trim());
		}
	}
	
	public void initializeModes() {
		if(flag.equals(EDIT)) {
			initializeEditMode();
		} else if(flag.equals(DETAILS)) {
			initializeDetailsMode();
		} else if(flag.equals(BACK)) {
			initializePreviousProblem();
			flag = previousFlag;
		}
	}
	
	public void initializeEditMode() {
		Problem storedProblem = (new RetrieveProblem()).retrieveProblem(ProblemSetterHomeController.getProblemName());
		showProblemDetails(storedProblem);
		nextButton.setDisable(false);
	}
	
	public void showProblemDetails(Problem problem) {
		if(problem.getProblemStatement().getProblemCode() != null) {
			codeTextField.setText(problem.getProblemStatement().getProblemCode());
		}
		if(problem.getProblemStatement().getProblemStatementFile() != null) {
			problemStatementTextField.setText(problem.getProblemStatement().getProblemStatementFile().getAbsolutePath());
		}
		nameTextField.setText(problem.getProblemStatement().getProblemName());
		testCaseTextField.setText(problem.getTestCase().getTestCaseDirectory().getAbsolutePath());
		timeLimitTextField.setText(problem.getTimeLimit() + "");
	}
	
	private void initializeDetailsMode() {
		Problem storedProblem = (new RetrieveProblem()).retrieveProblem(ProblemSetterHomeController.getProblemName());
		showProblemDetails(storedProblem);
		initializeControlsForDetails();
	}
	
	public void initializeControlsForDetails() {
		codeTextField.setDisable(true);
		nameTextField.setDisable(true);
		timeLimitTextField.setDisable(true);
		problemBrowseButton.setDisable(true);
		testCaseBrowseButton.setDisable(true);
		nextButton.setDisable(false);
		cancelButton.setText("Back");
	}
	
	public void initializePreviousProblem() {
		showProblemDetails(problem);
		nextButton.setDisable(false);
		if(previousFlag == DETAILS) {
			initializeControlsForDetails();
		}
	}
	
	public static void setFlag(String flag) {
		ProblemInputController.flag = flag;
	}
	
	public static String getFlag() {
		return flag;
	}
}
