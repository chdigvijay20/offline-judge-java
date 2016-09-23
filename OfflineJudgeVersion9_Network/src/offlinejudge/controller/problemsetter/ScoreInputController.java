package offlinejudge.controller.problemsetter;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import offlinejudge.controller.Navigator;
import offlinejudge.controller.NumberTextfield;
import offlinejudge.core.Problem;
import offlinejudge.core.Score;
import offlinejudge.database.RetrieveProblem;
import offlinejudge.database.StoreProblem;
import offlinejudge.database.UpdateDatabase;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class ScoreInputController implements Initializable {
	
	private static Problem problem = null;
	
	@FXML private GridPane scoreGridPane;
	@FXML private CheckBox scoreCheckBox;
	
	private ArrayList<NumberTextfield> scoreTextFields;
	private Button addProblemButton;
	private Button backButton;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initializeScoreInput();
		initializeListeners();
		initializeDetails();
	}
	
	public void initializeScoreInput() {
		scoreTextFields = new ArrayList<>();
		File[] inputFiles = problem.getTestCase().getInputFiles();

		int i = 2;
		for (int j = 0; j < inputFiles.length; j++) {
			
			Label label = new Label(inputFiles[j].getName());
			GridPane.setConstraints(label, 1, i);
			scoreGridPane.getChildren().add(label);
			
			scoreTextFields.add(new NumberTextfield());
			GridPane.setConstraints(scoreTextFields.get(scoreTextFields.size()-1), 2, i);
			scoreGridPane.getChildren().add(scoreTextFields.get(scoreTextFields.size()-1));
			scoreTextFields.get(scoreTextFields.size()-1).setOnKeyReleased(d -> {
				toggleAddProblemButton();
			});
			j++;
			
			if(j == inputFiles.length) {
				i++;
				break;
			}
			
			label = new Label(inputFiles[j].getName());
			GridPane.setConstraints(label, 4, i);
			scoreGridPane.getChildren().add(label);
			
			scoreTextFields.add(new NumberTextfield());
			GridPane.setConstraints(scoreTextFields.get(scoreTextFields.size()-1), 5, i);
			scoreGridPane.getChildren().add(scoreTextFields.get(scoreTextFields.size()-1));
			scoreTextFields.get(scoreTextFields.size()-1).setOnKeyReleased(d -> {
				toggleAddProblemButton();
			});
			i++;
		}
		
		Region region = new Region();
		region.setPadding(new Insets(50, 50, 50, 50));
		
		addProblemButton = new Button("Add Problem");
		addProblemButton.setDisable(true);
		
		backButton = new Button("Back");
		
		VBox vBox = new VBox();
		vBox.getChildren().addAll(region, addProblemButton);
		
		HBox hBox = new HBox();
		GridPane.setConstraints(hBox, 0, i);
		GridPane.setColumnSpan(hBox, 7);
		hBox.setAlignment(Pos.CENTER);
		
		hBox.getChildren().addAll(backButton, region, addProblemButton);
		
		scoreGridPane.getChildren().add(hBox);
	}
	
	public void toggleAddProblemButton() {
		for (NumberTextfield numberTextfield : scoreTextFields) {
			if(numberTextfield.getText().trim().equals("")) {
				addProblemButton.setDisable(true);
				return;
			}
		}
		addProblemButton.setDisable(false);
	}
	
	@FXML
	public void scoreCheckBoxClicked() {
		if(scoreCheckBox.isSelected()) {
			setValueInScoreTextField("10");
			toggleScoreTextField(true);
			addProblemButton.setDisable(false);
		} else {
			setValueInScoreTextField("");
			toggleScoreTextField(false);
			addProblemButton.setDisable(true);
		}
	}
	
	public void setValueInScoreTextField(String value) {
		for (NumberTextfield numberTextfield : scoreTextFields) {
			numberTextfield.setText(value);
		}
	}
	
	public void toggleScoreTextField(Boolean value) {
		for (NumberTextfield numberTextfield : scoreTextFields) {
			numberTextfield.setDisable(value);
		}
	}
	
	public static void setProblem(Problem problem) {
		ScoreInputController.problem = problem;
	}
	
	public void initializeListeners() {
		addProblemButton.setOnAction(d -> addProblemButtonClicked());
		addProblemButton.setOnKeyPressed(d -> {
			if(d.getCode() == KeyCode.ENTER) {
				addProblemButtonClicked();
			}
		});
		
		backButton.setOnAction(d -> backButtonClicked());
		backButton.setOnKeyPressed(d -> {
			if(d.getCode() == KeyCode.ENTER) {
				backButtonClicked();
			}
		});
	}
	
	public void backButtonClicked() {
		ProblemInputController.previousFlag = ProblemInputController.getFlag();
		ProblemInputController.setFlag(ProblemInputController.BACK);
		Navigator.loadScene(Navigator.PROBLEMINPUT);
	}
	
	public void addProblemButtonClicked() {
		ArrayList<Double> maximumScore = new ArrayList<>();
		if(scoreCheckBox.isSelected()) {
			for (int j = 0; j < scoreTextFields.size(); j++) {
				maximumScore.add(10.0);
			}
		} else {
			for (int j = 0; j < scoreTextFields.size(); j++) {
				maximumScore.add(Double.parseDouble(scoreTextFields.get(j).getText().trim()));
			}
		}
		problem.setScore(maximumScore);
		
		if(ProblemInputController.getFlag().equals(ProblemInputController.ADD)) {
			addProblemToDatabase();			
		} else if(ProblemInputController.getFlag().equals(ProblemInputController.EDIT)){
			updateProblemInDatabase();
		}
		
		Navigator.loadScene(Navigator.PROBLEMSETTERHOME);
	}
	
	public void initializeDetails() {
		
		if(ProblemInputController.getFlag().equals(ProblemInputController.EDIT)) {
			addProblemButton.setText("Update Problem");
			addProblemButton.setDisable(false);
			updateScoreValues();
		} else if(ProblemInputController.getFlag().equals(ProblemInputController.DETAILS)) {
			updateScoreValues();
			initializeControls();
		}
	}
	
	public void updateScoreValues() {
		ArrayList<Score> scores = (new RetrieveProblem()).retrieveProblem(ProblemSetterHomeController.getProblemName()).getScore();
		int i = 0;
		for (NumberTextfield numberTextfield : scoreTextFields) {
			numberTextfield.setText(scores.get(i).getMaximumScore() + "");
			i++;
		}
	}
	
	public void initializeControls() {
		scoreCheckBox.setDisable(true);
		for (NumberTextfield numberTextfield : scoreTextFields) {
			numberTextfield.setDisable(true);
		}
	}
	
	public void addProblemToDatabase() {
		(new StoreProblem()).storeProblem(problem);
	}
	
	public void updateProblemInDatabase() {
		(new UpdateDatabase()).updateProblem(ProblemSetterHomeController.getProblemName(), problem);
	}
	
}
