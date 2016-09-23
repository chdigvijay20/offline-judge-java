package offlinejudge.controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import offlinejudge.alerts.SocketAddressAlert;
import offlinejudge.alerts.WarningAlert;
import offlinejudge.network.ServerSocketAddress;

public class HomeController implements Initializable {
    
	@FXML private Button problemSetterButton;
	@FXML private Button hackerButton;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initializeKeyListeners();
	}
	
	public void initializeKeyListeners() {
		problemSetterButton.setOnKeyPressed(d -> {
			if(d.getCode() == KeyCode.ENTER) {
				problemSetterButtonClicked();
			}
		});
		
		hackerButton.setOnKeyPressed(d -> {
			if(d.getCode() == KeyCode.ENTER) {
				hackerButtonClicked();
			}
		});
	}
	
    @FXML
    public void problemSetterButtonClicked() {
//		InetSocketAddress socketAddress = (new SocketAddressAlert()).getSocketAddress("Socket Address", "Enter the IP address and port number of the Server");
		try {
			InetSocketAddress socketAddress = new InetSocketAddress(InetAddress.getByName("127.0.0.1"), 8989);
			if(socketAddress != null) {
				ServerSocketAddress.setServerSocketAddress(socketAddress);
				ServerSocket dummy = new ServerSocket();
				dummy.bind(socketAddress);
				dummy.close();
				Navigator.loadScene(Navigator.PROBLEMSETTERHOME);
			}
		} catch (IOException e) {
			new WarningAlert("Error", "Bind Exception", "The address you entered is wrong or the port is not \navailable. Enter a correct address or different port number.");
//			e.printStackTrace();
		}
    }
    
    @FXML
    public void hackerButtonClicked() {
//    	InetSocketAddress socketAddress = (new SocketAddressAlert()).getSocketAddress("Socket Address", "Enter the IP address and port number of the Server");
		
    	InetSocketAddress socketAddress = null;
    	try { socketAddress = new InetSocketAddress(InetAddress.getByName("127.0.0.1"), 8989);
		} catch (UnknownHostException e) { }
    	
		if(socketAddress != null) {
    		ServerSocketAddress.setServerSocketAddress(socketAddress);
    		Navigator.loadScene(Navigator.HACKERHOME);
    	}
    }
}
