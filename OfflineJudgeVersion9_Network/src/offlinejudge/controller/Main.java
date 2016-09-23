package offlinejudge.controller;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import offlinejudge.alerts.ConfirmationAlert;
import offlinejudge.controller.problemsetter.ProblemSetterHomeController;
import offlinejudge.network.OfflineJudgeServer;

public class Main extends Application {
	
	public static Stage window;
	public static OfflineJudgeServer server;
	
	@Override
	public void start(Stage primaryStage) {
		Main.window = primaryStage;
		primaryStage.setTitle("Offline Judge");
		
		primaryStage.setScene(createScene(loadMainPane()));
		
		window.setOnCloseRequest(d -> {
//			d.consume();
//			closeTheProgram();
		});
		
		primaryStage.show();
	}
	
	public void closeTheProgram() {
		Boolean result = new ConfirmationAlert().confirm("Confirm Exit", "Are you sure you want to exit the application", "Exiting the app will close the background processes.");
		if(result) {
			window.close();
		}
	}
	
	public Pane loadMainPane() {
		
		try {
			FXMLLoader loader = new FXMLLoader();

			Pane mainPane = loader.load(
				getClass().getResourceAsStream(Navigator.MAIN)
			);
			
			MainController mainController = loader.getController();
			
			Navigator.setMainController(mainController);
			Navigator.loadScene(Navigator.HOME);
			
			return mainPane;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public Scene createScene(Pane mainPane) {
		Scene scene = new Scene(mainPane);	
		return scene;
	}
	
	public static OfflineJudgeServer getServer() {
		return server;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
