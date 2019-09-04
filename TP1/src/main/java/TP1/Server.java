package TP1;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Stack;

public class Server {
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		while (true) {
			ServerSocket serverSocket = null;
			Socket socket = null;
			ObjectInputStream in = null;
			ObjectOutputStream out = null;
			try {
				// Création du socket du serveur en utilisant le port 5000.
				serverSocket = new ServerSocket(5003);
				// Ici, la fonction accept est bloquante! Ainsi, l'exécution du serveur s'arrête
				// ici et attend la connection d'un client avant de poursuivre.
				socket = serverSocket.accept();
				// Création d'un input stream. Ce stream contiendra les données envoyées par le
				// client.
				in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
				// La fonction readObject est bloquante! Ainsi, le serveur arrête son exécution
				// et attend la réception de l'objet envoyé par le client!
				List<String> strings = (List<String>) in.readObject();
				Stack<String> stackOfLines = new Stack<String>();
				// Remplissage de la stack avec les lignes. La première ligne entrée sera la
				// dernière à ressortir.
				for (int i = 0; i < strings.size(); i++) {
					stackOfLines.push(strings.get(i));
				}
				// Création du output stream. Ce stream contiendra les données qui seront
				// envoyées au client.
				out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
				// Écriture des données dans la pile.
				out.writeObject(stackOfLines);
				// Envoi des données vers le client.
				out.flush();

			} finally {
				serverSocket.close();
				socket.close();
			}
		}
	}
}