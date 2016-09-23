package offlinejudge.alerts;

import java.util.Optional;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

public class ConfirmationAlert {
	
	public boolean confirm(String title, String headerText, String contentText) {
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);
		alert.getButtonTypes().clear();
		alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
		
		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getButtonTypes().stream()
		        .map(dialogPane::lookupButton)
		        .forEach(button ->
		                button.addEventHandler(
		                        KeyEvent.KEY_PRESSED,
		                        fireOnEnter
		                )
		        );
		
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == ButtonType.YES) {
			return true;
		}
		
		return false;
	}
	
	EventHandler<KeyEvent> fireOnEnter = event -> {
	    if (KeyCode.ENTER.equals(event.getCode()) && event.getTarget() instanceof Button) {
	        ((Button) event.getTarget()).fire();
	    }
	};
}
