package offlinejudge.controller.problemsetter;

import java.net.URL;
import java.util.ResourceBundle;

import offlinejudge.alerts.ConfirmationAlert;
import offlinejudge.controller.Main;
import offlinejudge.controller.Navigator;
import offlinejudge.core.ProblemStatement;
import offlinejudge.database.DeleteProblem;
import offlinejudge.database.RetrieveProblem;
import offlinejudge.network.OfflineJudgeServer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;

public class ProblemSetterHomeController implements Initializable {
	
	@FXML private Button addProblemButton;
	@FXML private Button editProblemButton;
	@FXML private Button deleteProblemButton;
	@FXML private Button viewDetailsButton;
	
	private static String problemName;
	private static OfflineJudgeServer offlineJudgeServer;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initializeTable();
		initializeListeners();
		(new OfflineJudgeServer()).startServer();
	}
	
	public void initializeListeners() {
		initializeKeyListeners();
		initializeTableListeners();
	}
	
	public void initializeKeyListeners() {
		addProblemButton.setOnKeyPressed(d -> {
			if(d.getCode() == KeyCode.ENTER) {
				addProblemButtonClicked();
			}
		});
		
		editProblemButton.setOnKeyPressed(d -> {
			if(d.getCode() == KeyCode.ENTER) {
				editProblemButtonClicked();
			}
		});
		
		deleteProblemButton.setOnKeyPressed(d -> {
			if(d.getCode() == KeyCode.ENTER) {
				deleteProblemButtonClicked();
			}
		});
		
		viewDetailsButton.setOnKeyPressed(d -> {
			if(d.getCode() == KeyCode.ENTER) {
				viewDetailsButtonClicked();
			}
		});
	}
	
	public void initializeTableListeners() {
		problemsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			editProblemButton.setDisable(false);
			deleteProblemButton.setDisable(false);
			viewDetailsButton.setDisable(false);
			problemName = problemsTable.getSelectionModel().getSelectedItem().getProblemName();
		});
	}

	@FXML private TableView<ProblemStatement> problemsTable;
	@FXML private TableColumn<ProblemStatement, String> problemCodeColumn;
	@FXML private TableColumn<ProblemStatement, String> problemNameColumn;

	public void initializeTable() {
		problemCodeColumn.setCellValueFactory(new PropertyValueFactory<ProblemStatement, String>("problemCode"));
		problemNameColumn.setCellValueFactory(new PropertyValueFactory<ProblemStatement, String>("problemName"));

		problemsTable.getItems().setAll((new RetrieveProblem()).getProblemStatementList());
	}
	
	@FXML
	public void addProblemButtonClicked() {
		ProblemInputController.setFlag(ProblemInputController.ADD);
		Navigator.loadScene(Navigator.PROBLEMINPUT);
	}
	
	@FXML
	public void editProblemButtonClicked() {
		ProblemInputController.setFlag(ProblemInputController.EDIT);
		Navigator.loadScene(Navigator.PROBLEMINPUT);
	}
	
	@FXML
	public void deleteProblemButtonClicked() {
		boolean confirm = (new ConfirmationAlert()).confirm("Confirm Delete Problem", "The Problem will be permanently lost", 
				"Are you sure you want to permanently delete the\nproblem \"" + problemName + "\" ?");
		
		if(confirm) {
			(new DeleteProblem()).deleteProblem(problemName);
			Navigator.loadScene(Navigator.PROBLEMSETTERHOME);
		}
	}
	
	@FXML
	public void viewDetailsButtonClicked() {
		ProblemInputController.setFlag(ProblemInputController.DETAILS);
		Navigator.loadScene(Navigator.PROBLEMINPUT);
	}
	
	public static String getProblemName() {
		return problemName;
	}
}
