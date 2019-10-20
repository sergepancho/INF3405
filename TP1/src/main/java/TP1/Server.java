package TP1;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
		String serverAddress = "132.207.29.123";
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
						File dir = new File(System.getProperty("user.dir"));

						String[] fileNames = dir.list();
						String response = "";
						for (String fileName : fileNames) {
							response += fileName + ";";
							System.out.println(fileName + "  ");
						}
						out.writeUTF(response);
						break;

					case "cd":
						break;

					case "mkdir":
						String name = command[1];
						File directory = new File(name);
						if (!directory.exists()) { // verifier que le dossier a ete cree
							if (directory.mkdir()) {
								out.writeUTF("Le fichier " + name + "a ;ete creer");
							}
						}
						break;

					case "upload":
						if (test(command[1])) {
							uploadFile(command[1]);
						}
						break;

					case "download":
						break;

					case "exit":
						break;

					default:
						break;
					}

					System.out.println("Command from client " + commandName);

					System.out.println("Command from client " + commandName);

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
			if (argument.isEmpty()) {
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
