package offlinejudge.alerts;

import javafx.scene.control.ButtonBar.ButtonData;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Optional;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import offlinejudge.controller.NumberTextfield;

public class SocketAddressAlert {
		
	public InetSocketAddress getSocketAddress(String title, String headerText) {
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle(title);
		dialog.setHeaderText(headerText);

		dialog.setGraphic(new ImageView(getClass().getResource("server_icon.png").toString()));

		ButtonType saveButtonType = new ButtonType("Save", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
		
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));
		
		TextField ipAddress = new TextField();
		ipAddress.setPromptText("IP Address");
		NumberTextfield port = new NumberTextfield();
		port.setPromptText("Port");

		grid.add(new Label("IP Address"), 0, 0);
		grid.add(ipAddress, 1, 0);
		grid.add(new Label("Port"), 0, 1);
		grid.add(port, 1, 1);
		
		dialog.getDialogPane().setContent(grid);
		
		Node saveButton = dialog.getDialogPane().lookupButton(saveButtonType);
		saveButton.setDisable(true);
		
		ipAddress.textProperty().addListener((observable, oldValue, newValue) -> {
			saveButton.setDisable(newValue.trim().isEmpty() || port.getText().trim().isEmpty());
		});
		
		port.textProperty().addListener((observable, oldValue, newValue) -> {
			saveButton.setDisable(newValue.trim().isEmpty() || ipAddress.getText().trim().isEmpty());
		});
		
		ipAddress.requestFocus();
		
		dialog.setResultConverter(dialogButton -> {
			if(dialogButton == saveButtonType) {
				return new Pair<String, String>(ipAddress.getText(), port.getText());
			}
			return null;
		});
		
		Optional<Pair<String, String>> result = dialog.showAndWait();
		
		try {
			if(result.isPresent()) {
				return new InetSocketAddress(InetAddress.getByName(result.get().getKey()), Integer.parseInt(result.get().getValue().trim()));
			}
		} catch (NumberFormatException | UnknownHostException e) {
			new WarningAlert("Error", "Invalid IP address", "Enter a valid IP address of server");
//			e.printStackTrace();
		}
		return null;
	}
}
