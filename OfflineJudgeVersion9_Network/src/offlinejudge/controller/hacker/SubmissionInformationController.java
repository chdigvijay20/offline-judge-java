package offlinejudge.controller.hacker;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import offlinejudge.core.Score;

public class SubmissionInformationController implements Initializable {
	
	@FXML private TableView<Score> scoresTable;
	@FXML private TableColumn<Score, Double> taskColumn;
	@FXML private TableColumn<Score, Double> scoreColumn;
	@FXML private TableColumn<Score, String> resultColumn;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ArrayList<Score> scores = new ArrayList<>();
		scores.add(new Score(10.0));
		scores.add(new Score(10.0));
		scores.add(new Score(10.0));
		scores.add(new Score(10.0));
		
		for (Score score : scores) {
			score.setAchievedScore(10.0);
			score.setRunStatus("AC");
		}
		
		if("CE".equals("CE")) {
			scoresTable.setVisible(false);
		} else {
			initializeTable(scores);			
		}
	}
	
	public void initializeTable(ArrayList<Score> scores) {
		taskColumn.setCellValueFactory(new PropertyValueFactory<Score, Double>("maximumScore"));
		scoreColumn.setCellValueFactory(new PropertyValueFactory<Score, Double>("achievedScore"));
		resultColumn.setCellValueFactory(new PropertyValueFactory<Score, String>("runStatus"));
		
		scoresTable.getItems().setAll(scores);
	}
}
