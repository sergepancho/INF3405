package TP1;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.FileSystems;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Arrays;

public class Server {
	private static ServerSocket listener;

	public static void main(String[] args) throws Exception {
		int clientNumber = 0;
		String serverAddress = "132.207.29.122";
		int serverPort = 5003;

		// creation de la connexion
		listener = new ServerSocket();
		listener.setReuseAddress(true);
		InetAddress serverIP = InetAddress.getByName(serverAddress);

		// association de l'adresse et du port a la connexion
		listener.bind(new InetSocketAddress(serverIP, serverPort));

		System.out.format("The server is running on %s:%d%n", serverAddress, serverPort);

		try {
			while (true) {
				new ClientHandler(listener.accept(), clientNumber++).start();
			}
		} finally {
			listener.close();
		}
	}

	private static class ClientHandler extends Thread {
		private Socket socket;
		private int clientNumber;
		private Path currentDirectory;
		private String currentDirectoryString;
		private DataOutputStream out;

		public ClientHandler(Socket socket, int clientNumber) {
			this.socket = socket;
			this.clientNumber = clientNumber;
			System.out.println("New connection with client" + clientNumber + " at " + socket);
		}

		public void run() {
			try {
				out = new DataOutputStream(socket.getOutputStream());
				out.writeUTF("Hello from server - you are client n" + clientNumber);
				
				// ajput du repertoire courant 
				currentDirectoryString =  System.getProperty("user.dir") ;
                //
				DataInputStream in = new DataInputStream(socket.getInputStream());
				String commandName;
				do {
					String clientCommand = in.readUTF();
					// System.out.println(clientCommand);
					String command[] = clientCommand.split(" ");// le premier string est le nom de la commande
					commandName = command[0];

					// System.out.println("rien");
					// System.out.println(commandName);

					System.out.println("valeur de la commande" + Arrays.toString(command));
                   
					switch (commandName) {
					case "ls":
						File dir = new File(currentDirectoryString);

						String[] fileNames = dir.list();
						String response = "";
						for (String fileName : fileNames) {
							response += fileName + ";";
							//System.out.println(fileName + "  ");
						}
						out.writeUTF(response);
						break;

					case "cd":

					   String directoryName = command[1];
					   String slash = "..";
					  // if(directoryName == slash){//si le repertoire de deplacement est le repertoire parent
						if(directoryName.compareTo(slash) == 0){//si le repertoire de deplacement est le repertoire parent
							File dircourant = new File(currentDirectoryString);
							File  dirParent = dircourant.getParentFile();// le  dossier parent
							if(dirParent.exists()){
								currentDirectoryString = dirParent.getAbsolutePath();
							}		
					   }else{ // si le repertoire de deplacement est le repertoire enfant 
						
							File dircourant = new File(currentDirectoryString);
							//verifier si le fichier existe dans la  liste des fichiers 

								

							String[] FileNames = dircourant.list();
							boolean filefound = false;
							for (String fileName : FileNames) { 
								//System.out.println("les fichiers dans le repertoire courant sont "+fileName +" " +directoryName);
								if(fileName .compareTo(directoryName)== 0){ // si les deux strings se correspondent
									filefound = true;           
									break;
								}           
							}

								//creer un dossier a partir du fichiers choisi 
								//ouvrir le repertoire 

							if(filefound == true){ // si le fichier a ete trouve 		
								//String path =  System.getProperty("user.dir")+"/"+ directoryName ;
								//currentDirectoryString = dircourant.getAbsolutePath() + '\\' + directoryName ;
								currentDirectoryString += '\\' + directoryName ; 
								out.writeUTF("vous etes actuellement dans le chemin"+currentDirectoryString);
							//	System.out.println("les fichiers dans le repertoire courant sont "+currentDirectoryString);
							}
							else{
							//	System.out.print("le nom du fichier entre ne figure pas dans  le repertoire courant");
								out.writeUTF("le fichier de reference n'existe pas");
							}

					   }
						break;

					case "mkdir":
						String path = currentDirectoryString + "\\"+ command[1];
						File directory = new File(path);
						if (!directory.exists()) { // verifier que le dossier a ete cree
							if (directory.mkdir()) {
								out.writeUTF("Le fichier " + path + "a ete creer");
							}
						}
						break;
						

					case "upload":
						if (test(command[1])) {
							uploadFile(command[1]);
						}
						break;

					case "download":
						if (test(command[1])) {
							downloadFile(command[1]);
						}

						break;

					case "exit":
						break;

					default:
						break;
					}
				} while (commandName != "exit");

			} catch (IOException e) {
				System.out.println("Error handling client " + clientNumber + ": " + e);
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
					System.out.println("Couldn't close a socket");
				}
				System.out.println("Connection with client " + clientNumber + " closed");
			}
		}

		public boolean test(String argument) {
			if (!argument.isEmpty()) {
				return true;
			}
			// todo should return an error to the client
			try {

				out.writeUTF("no filename was provided");
				System.out.println("no filename was provided");

			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}

		public void downloadFile(String fileName) {
			try {
				File myFile = new File(System.getProperty("user.dir") + "\\" + "test.jpg");
				byte[] mybytearray = new byte[(int) myFile.length()];
				BufferedInputStream bis = new BufferedInputStream(new FileInputStream(myFile));
				bis.read(mybytearray, 0, mybytearray.length);
				OutputStream os = socket.getOutputStream();
				os.write(mybytearray, 0, mybytearray.length);
				os.flush();
				bis.close();
			} catch (IOException e) {
				System.out.println("erreur dans la creation du socket ou de l execution de la commande ");
			}
		}

		public void uploadFile(String s) {
			try {
				Path file = currentDirectory.resolve(s);
				if (file.toFile().exists() && file.toFile().isFile()) {
					FileInputStream fileStream = new FileInputStream(file.toFile());
					long size = file.toFile().length();
					out.writeLong(size);
					// copyStreamUpload(fileStream, out);
					fileStream.close();
				} else {
					out.writeLong(0);
					System.out.println("No such file was found!");
				}
			} catch (InvalidPathException e) {
				System.out.println("The file doesn't exist");

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
