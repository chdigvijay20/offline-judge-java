package offlinejudge.network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;

import offlinejudge.alerts.WarningAlert;
import offlinejudge.core.Status;

public class OfflineJudgeClient {
	
	private Socket socket;
	
	private ObjectOutputStream objectOutputStream;
	private BufferedInputStream bufferedInputStream;
	
	private ObjectInputStream objectInputStream;
	private BufferedOutputStream bufferedOutputStream;
	
	private File compilationError;
	
	private byte[] buffer;
	private final int BUFFER_SIZE = 50;
	
	public OfflineJudgeClient() {
		try {
			socket = new Socket(ServerSocketAddress.getInetAddress(), ServerSocketAddress.getPort());
			objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			objectInputStream  = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			new WarningAlert("Error", "Connection refused", "The Server IP address or port may possibly be\nwrong. Enter the correct details and try again.");
		}
	}
	
	public void submitSolution(File solutionFile, String currentProblemName) {
		uploadSolution(solutionFile, currentProblemName);
		downloadResults();
	}

	public void uploadSolution(File solutionFile, String currentProblemName) {
		sendProblemName(currentProblemName);
		sendSolutionFile(solutionFile);
	}
	
	public void sendProblemName(String currentProblemName) {
		try {
			objectOutputStream.writeObject(currentProblemName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendSolutionFile(File solutionFile) {
		
		try {
			objectOutputStream.writeObject(solutionFile.getName());
			
			bufferedInputStream = new BufferedInputStream(new FileInputStream(solutionFile));
			buffer = new byte[BUFFER_SIZE];
			
			int count;
			while((count = bufferedInputStream.read(buffer)) > 0) {
				objectOutputStream.write(buffer, 0, count);
				Arrays.fill(buffer, (byte)0);
			}
			bufferedInputStream.close();
			System.out.println(socket.isClosed());
			socket.shutdownOutput();
			System.out.println(socket.isClosed());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private void downloadResults() {
		try {
			System.out.println("gonna download!!!");
			System.out.println(socket  + "\n" + objectInputStream);
			objectInputStream = new ObjectInputStream(socket.getInputStream());
			String flag = (String)objectInputStream.readObject();
//			if(flag.equals(Status.CE)) {
//				System.out.println("CE Confirm");
//				downloadCompilationErrorFile();
//			} else {
//				downloadScores();
//			}
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}

//	private void downloadScores() {
//		
//	}
//
//	private void downloadCompilationErrorFile() {
//		try {
//			compilationError = new File(System.getProperty("user.home") + "/compilationError.txt");
//			bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(compilationError));
//			System.out.println("CE file reding");
//			int count;
//			while((count = objectInputStream.read(buffer)) > 0) {
//				bufferedOutputStream.write(buffer);
//				Arrays.fill(buffer, (byte)0);
//			}
//			bufferedOutputStream.close();
//			System.out.println("CE file reding complete");
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
}
