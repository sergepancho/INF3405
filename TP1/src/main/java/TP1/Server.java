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
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;

public class Server {
	private static ServerSocket listener;

	public static void main(String[] args) throws Exception {
		int clientNumber = 0;
		String serverAddress = "127.0.0.1";
		int serverPort = 5003;

		// creation de la connexion
		listener = new ServerSocket();
		listener.setReuseAddress(true);
		InetAddress serverIP = InetAddress.getByName(serverAddress);
		Scanner scanner = new Scanner(System.in);

		do {
			System.out.print("Enter a valid port (entre 5000 et 5050):");
			serverPort = scanner.nextInt();
		} while (serverPort > 5050 || serverPort < 5000);

		// association de l'adresse et du port a la connexion
		listener.bind(new InetSocketAddress(serverIP, serverPort));

		System.out.format("The server is running on %s:%d%n", serverAddress, serverPort);

		try {
			while (true) {
				new ClientHandler(listener.accept(), clientNumber++).start();
			}
		} finally {
			listener.close();
			scanner.close();
		}
	}

	private static class ClientHandler extends Thread {
		private Socket socket;
		private int clientNumber;
		private String currentDirectoryString;
		private DataOutputStream out;
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		OutputStream os = null;
		private DataInputStream in;

		public ClientHandler(Socket socket, int clientNumber) {
			this.socket = socket;
			this.clientNumber = clientNumber;
			System.out.println("New connection with client" + clientNumber + " at " + socket);
		}

		public void run() {
			try {
				out = new DataOutputStream(socket.getOutputStream());
				out.writeUTF("Hello from server - you are client n" + clientNumber);

				// ajout du repertoire courant
				currentDirectoryString = System.getProperty("user.dir");
				//
				in = new DataInputStream(socket.getInputStream());
				String commandName;
				do {
					String clientCommand = in.readUTF();
					String command[] = clientCommand.split(" ");// le premier string est le nom de la commande
					commandName = command[0];

					switch (commandName) {
					case "ls":
						ls();
						break;

					case "cd":
						if (test(command)) {
							cd(command[1]);
						}
						break;
					case "mkdir":
						if (test(command)) {
							mkdir(command[1]);
						}
						break;

					case "upload":
						if (test(command)) {
							uploadFile(command[1]);
						}
						break;

					case "download":
						if (test(command)) {
							downloadFile(command[1]);
						}

						break;

					case "exit":
						out.writeUTF("Vous avez ete deconnecte");
						out.flush();
						break;

					default:
						out.writeUTF("La commande entree n'existe pas");
						out.flush();
						break;
					}
				} while (commandName.compareTo("exit") != 0);

			} catch (IOException e) {
				System.out.println("Error handling client " + clientNumber + ": " + e);
			} finally {
				try {
					out.close();
					in.close();
					socket.close();
				} catch (IOException e) {
					System.out.println("Couldn't close a socket");
				}
				System.out.println("Connection with client " + clientNumber + " closed");
			}
		}

		public boolean test(String[] argument) {
			if (argument.length > 1) {
				return true;
			}
			try {
				out.writeUTF("no filename was provided");
				System.out.println("no filename was provided");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}

		public void ls() {
			try {
				File dir = new File(currentDirectoryString);

				String[] fileNames = dir.list();
				String response = "";
				for (String fileName : fileNames) {
					response += fileName + ";";
					// System.out.println(fileName + " ");
				}
				out.writeUTF(response);
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// function qui permet de changer de repertoire courant
		// params: directoryName - recoit le nom du repertoire
		public void cd(String directoryName) {
			try {
				String response = "";
				if (directoryName.compareTo("..") == 0) {// si le repertoire de deplacement est le repertoire
															// parent
					File dircourant = new File(currentDirectoryString);
					File dirParent = dircourant.getParentFile();// le dossier parent
					if (dirParent.exists()) {
						currentDirectoryString = dirParent.getAbsolutePath();
						response = "vous etes actuellement dans le chemin;" + currentDirectoryString;
					}
				} else { // si le repertoire de deplacement est le repertoire enfant
					File dircourant = new File(currentDirectoryString);
					// verifier si le fichier existe dans la liste des fichiers
					String[] FileNames = dircourant.list();
					boolean filefound = false;
					for (String fileName : FileNames) {

						if (fileName.compareTo(directoryName) == 0) { // si les deux strings se correspondent
							filefound = true;
							break;
						}
					}

					if (filefound == true) { // si le fichier a ete trouve

						currentDirectoryString += '\\' + directoryName;
						response = "vous etes actuellement dans le chemin;" + currentDirectoryString;

					} else {
						response = "le nom du dossier entre ne figure pas dans  le repertoire courant";
					}

				}
				out.writeUTF(response);
				out.flush();

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		// function qui permet de creer un fichier
		// params: fileName - recoit le nom du fichier
		public void mkdir(String fileName) {
			try {
				String path = currentDirectoryString + "\\" + fileName;
				File directory = new File(path);
				if (!directory.exists()) { // verifier que le dossier a ete cree
					if (directory.mkdir()) {
						out.writeUTF("Le fichier " + path + " a ete creer;");
						out.flush();
					}
				}
				out.writeUTF("Le fichier n'a pas ete creer;");
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// function qui permet de download un fichier
		// params: fileName - recoit le nom du fichier
		public void downloadFile(String fileName) {
			System.out.println("filename: " + fileName);
			try {
				File myFile = new File(System.getProperty("user.dir") + "\\" + fileName);
				if (myFile.exists()) {
					System.out.println("file exists");
					out.writeInt(1);
					out.flush();
				} else {
					System.out.println("file doesn't exists");
					out.writeInt(0);
					out.flush();
					return;
				}

				byte[] mybytearray = new byte[(int) myFile.length()];

				// sending the file size
				out.writeLong(myFile.length());
				out.flush();

				fis = new FileInputStream(myFile);
				bis = new BufferedInputStream(fis);
				bis.read(mybytearray, 0, mybytearray.length);
				os = socket.getOutputStream();
				System.out.println("Sending " + "test" + "(" + mybytearray.length + " bytes)");
				os.write(mybytearray, 0, mybytearray.length);
				os.flush();
				System.out.println("Done.");
			} catch (IOException e) {
				System.out.println("erreur dans la creation du socket ou de l execution de la commande ");
			}
		}

		// function qui permet de download un fichier
		// params: fileName - recoit le nom du fichier
		public void uploadFile(String filename) {
			try {
				int response = in.readInt();
				if (response == 0) {
					System.out.println("the file doesn't exist ");
					return;
				}
				long fileSize;
				fileSize = in.readLong();
				System.out.println("total file size is: " + fileSize);
				byte[] buffer = new byte[8192];
				FileOutputStream fos = new FileOutputStream(filename);
				BufferedOutputStream bos = new BufferedOutputStream(fos);
				int read = 0;
				while ((read = in.read(buffer)) > 0) {
					bos.write(buffer, 0, read);
				}

				System.out.println("finished downloading");

				bos.flush();
				bos.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
