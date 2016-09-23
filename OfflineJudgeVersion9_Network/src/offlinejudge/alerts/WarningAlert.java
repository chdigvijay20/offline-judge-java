package offlinejudge.alerts;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class WarningAlert {

	public WarningAlert(String title, String headerText, String contentText) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);
		alert.showAndWait();
	}
	
}
