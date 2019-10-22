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
import java.net.Socket;
import java.util.Scanner;

public class Client {

	private static Socket socket;
	private static DataInputStream in;
	private static DataOutputStream out;
	static FileInputStream fis = null;
	static BufferedInputStream bis = null;
	static OutputStream os = null;

	public static String[] decode(String caractere) {

		String[] caractereValues = caractere.split(";");

		return caractereValues;
	}

	public static boolean checkAddress(String addressIp) {
		String[] addressValues = addressIp.split("\\.");

		// We need to get 4 elements exactly
		if (addressValues.length != 4)
			return false;

		// values need to be between 0 and 255
		for (String a : addressValues) {
			int valueInt = Integer.parseInt(a);
			if (valueInt > 255 || valueInt < 0) {
				return false;
			}
		}
		return true;
	}

	public static void readStream() {
		try {
			String messageFromServer = in.readUTF();
			String[] table = decode(messageFromServer);
			for (String value : table)
				System.out.println(value);
		} catch (IOException e) {
			System.out.println("erreur dans la creation du socket ou de l execution de la commande ");
		}
	}

	public static void download(String filename) {
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

	public static void upload(String fileName) {
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

	public static void main(String[] args) throws Exception {
		// Adresse et port du serveur
		String serverAddress = "127.0.0.1";// "132.207.29.122";
		int port = 5003;

		// Scanner scanner = new Scanner(System.in);
		do {
			System.out.print("Enter a valid IP address (ex: 123.123.12.1): ");
			// serverAddress = scanner.next();
		} while (!checkAddress(serverAddress));

		do {
			System.out.print("Enter a valid port (entre 5000 et 5050):");
			// port = scanner.nextInt();
		} while (port > 5050 || port < 5000);

		// Creation d'une nouvelle connexio avec le serveuer
		socket = new Socket(serverAddress, port);
		System.out.format("The server is running on %s:%d%n", serverAddress, port);

		// Creation d'un canal entrant pour recevoir les messages envoyes par le serveur
		in = new DataInputStream(socket.getInputStream());

		// Attente de la reception d'un message envoye par le serveur sur le canal
		String helloMessageFromServer = in.readUTF();
		System.out.println(helloMessageFromServer);

		// code bidon envoyer un fichier au serveur ;
		String commandLine = "";
		String command[];
		// creation d un canal pour envoyer des messages au serveur ....
		try {
			Scanner scanner = new Scanner(System.in);
			do {
				System.out.print("Entrer une commande:  ");
				// try{

				commandLine = scanner.nextLine();
				out = new DataOutputStream(socket.getOutputStream());

				out.writeUTF(commandLine);
				command = commandLine.split(" ");

				switch (command[0]) {
				case "ls":
					readStream();
					break;
				case "cd":
					readStream();
					break;

				case "mkdir":
					readStream();
					break;

				case "upload":
					upload(command[1]);
					break;

				case "download":
					download(command[1]);
					break;

				case "exit":
					readStream();
					break;

				default:
					break;
				}

			} while (command[0].compareTo("exit") != 0);
			// }
			/*
			 * catch(Exception e) {
			 *
			 * System.out.print("Entrer une commande parmi les suivantes");
			 *
			 * System.out.print("Entrer une commande"); }
			 */

			// }

			scanner.close();
		} catch (IOException e) {
			System.out.println("erreur dans la creation du socket ou de l execution de la commande ");
		} finally {
			try {

				socket.close();
			} catch (IOException e) {
				System.out.println("Couldn't close a socket");
			}
			System.out.println("fin de la communication");
		}

		//////

		/*
		 * //Fermeture de la connexion avec le serveur socket.close();
		 */
	}
}
