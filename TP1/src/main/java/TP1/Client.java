package TP1;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

	private static Socket socket;
	private static DataInputStream in;

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

	public static void download() {
		try {
			int fileSize;
			fileSize = in.readInt();
			System.out.print("size: " + fileSize);
			byte[] mybytearray = new byte[209982];
			InputStream is = socket.getInputStream();
			FileOutputStream fos = new FileOutputStream("test.jpg");
			BufferedOutputStream bos = new BufferedOutputStream(fos);

			int bytesRead = is.read(mybytearray, 0, mybytearray.length);
			bos.write(mybytearray, 0, bytesRead);
			System.out.print("finished downloading ");

			// bos.write(mybytearray, 0, current);
			bos.flush();
			// System.out.println("File " + "--test--" + " downloaded (" + current + " bytes
			// read)");
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
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
		// creation d un canal pour envoyer des messages au serveur ....
		try {
			while (true) {
				System.out.print("Entrer une commande:  ");
				Scanner scanner = new Scanner(System.in);
				// try{

				commandLine = scanner.nextLine();

				DataOutputStream out = new DataOutputStream(socket.getOutputStream());

				out.writeUTF(commandLine);
				String command[] = commandLine.split(" ");
				// while(!in.readUTF().isEmpty()) {
				switch (command[0]) {
				case "ls":
					readStream();
				case "cd":
					break;

				case "mkdir":
					readStream();
					break;

				case "upload":
					break;

				case "download":
					download();
					break;

				case "exit":
					readStream();
					break;

				default:
					break;
				}

			}
			// }
			/*
			 * catch(Exception e) {
			 *
			 * System.out.print("Entrer une commande parmi les suivantes");
			 *
			 * System.out.print("Entrer une commande"); }
			 */

			// }
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
