package offlinejudge.network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import offlinejudge.core.Evaluator;
import offlinejudge.core.Problem;
import offlinejudge.core.SourceCode;
import offlinejudge.core.Status;
import offlinejudge.database.RetrieveProblem;

public class OfflineJudgeServer {
	
	private ServerSocket serverSocket;
	
	private int problemSubmissionID = 0;
	private final String DEFAULTPATH = System.getProperty("user.home") + "/.offlinejudge";
	
	public void startServer() {
		try {
			serverSocket = new ServerSocket();
			serverSocket.bind(ServerSocketAddress.getServerSocketAddress());
			new File(DEFAULTPATH).mkdir();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ExecutorService clientProcessingPool = Executors.newFixedThreadPool(10);
		
		Runnable serverTask = new Runnable() {
			@Override
			public void run() {
				try {
					while(true) {
						Socket clientSocket = serverSocket.accept();
						clientProcessingPool.submit(new ClientTask(clientSocket));						
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		
		Thread serverThread = new Thread(serverTask);
		serverThread.start();
	}
	
	public synchronized void incrementID() {
		problemSubmissionID++;
	}
	
	private class ClientTask extends Thread {
		private Socket clientSocket;
		private ObjectInputStream objectInputStream;
		private ObjectOutputStream objectOutputStream;
		
		private BufferedOutputStream bufferedOutputStream;
		private BufferedInputStream bufferedInputStream;
		
		private Problem problem;
		private File solutionFile;
		
		private byte[] buffer;
		private final int BUFFER_SIZE = 50;
		
		public ClientTask(Socket clientSocket) {
			this.clientSocket = clientSocket;
			
			try {
				objectInputStream  = new ObjectInputStream(clientSocket.getInputStream());
				objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void run() {
			System.out.println("client running " + clientSocket.getInetAddress() + " " + clientSocket.getPort());
			receiveProblem();
			receiveSolution();
			sendResult();
		}
		
		public void receiveProblem() {
			try {
				String problemName = (String)objectInputStream.readObject();
				problem = (new RetrieveProblem()).retrieveProblem(problemName);
				System.out.println("received problemname " + problemName);
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}
		
		public void receiveSolution() {
			
			try {
				
				String fileName = (String)objectInputStream.readObject();
				String fileExtension = new SourceCode(new File(fileName)).getFileExtension();
				
				System.out.println(fileName);
				
				incrementID();
				solutionFile = new File(DEFAULTPATH + "/" + problemSubmissionID + fileExtension);
				
				bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(solutionFile));
				buffer = new byte[BUFFER_SIZE];

				int count;
				while((count = objectInputStream.read(buffer)) > 0) {
					bufferedOutputStream.write(buffer, 0, count);
					Arrays.fill(buffer, (byte)0);
				}
				
				bufferedOutputStream.close();
				System.out.println("File read complete...");
				
			} catch (ClassNotFoundException | IOException e) {
				e.printStackTrace();
			}
		}
		
		private void sendResult() {
			Object result = (new Evaluator()).evaluate(new SourceCode(solutionFile), problem);
			System.out.println(result);
			if(result instanceof File) {
				sendCompilationError((File)result);
			}// else {
//				
////				int numberOfFiles = problem.getTestCase().getInputFiles().length;
////				for (int i = 0; i < numberOfFiles; i++) {
////					System.out.println(problem.getScore().get(i).getMaximumScore() + "    " + problem.getScore().get(i).getAchievedScore() + "   " + problem.getScore().get(i).getRunStatus());
////				}
//				sendScore();
//			}			
		}
		
		public void sendCompilationError(File result) {
			try {
				objectOutputStream.writeObject(Status.CE);
				bufferedInputStream = new BufferedInputStream(new FileInputStream(result));
				
				int count;
				while ((count = bufferedInputStream.read(buffer)) > 0) {
					objectOutputStream.write(buffer, 0, count);
					Arrays.fill(buffer, (byte)0);
				}
				objectOutputStream.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		public void sendScore() {
			
		}
	}
	
	public ServerSocket getServerSocket() {
		return serverSocket;
	}
}