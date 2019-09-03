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

/** Le client doit �tre capable de lire un fichier texte et d�envoyer son contenu au serveur qui retransmettra aussit�t son contenu au client. Ce dernier devra intercepter le contenu du fichier texte. Une fois la r�ception termin�e, le serveur devra inverser le contenu du fichier de sorte � ce que la premi�re ligne re�ue soit la derni�re ligne envoy�e vers le client. **/
//L'impl�mentation du serveur n'est pas multi-threaded. Ainsi, la connection de
//plusieurs clients en m�me temps au serveur ne fonctionnera pas! A vous de threader le serveur
//pour qu'il puisse avoir la capacit� d'accepter plusieurs clients.
public class Server {
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		while (true) {
			ServerSocket serverSocket = null;
			Socket socket = null;
			ObjectInputStream in = null;
			ObjectOutputStream out = null;
			try {
				// Cr�ation du socket du serveur en utilisant le port 5000.
				serverSocket = new ServerSocket(5000);
				// Ici, la fonction accept est bloquante! Ainsi, l'ex�cution du serveur s'arr�te
				// ici et attend la connection d'un client avant de poursuivre.
				socket = serverSocket.accept();
				// Cr�ation d'un input stream. Ce stream contiendra les donn�es envoy�es par le
				// client.
				in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
				// La fonction readObject est bloquante! Ainsi, le serveur arr�te son ex�cution
				// et attend la r�ception de l'objet envoy� par le client!
				List<String> strings = (List<String>) in.readObject();
				Stack<String> stackOfLines = new Stack<String>();
				// Remplissage de la stack avec les lignes. La premi�re ligne entr�e sera la
				// derni�re � ressortir.
				for (int i = 0; i < strings.size(); i++) {
					stackOfLines.push(strings.get(i));
				}
				// Cr�ation du output stream. Ce stream contiendra les donn�es qui seront
				// envoy�es au client.
				out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
				// �criture des donn�es dans la pile.
				out.writeObject(stackOfLines);
				// Envoi des donn�es vers le client.
				out.flush();
			} finally {
				serverSocket.close();
				socket.close();
			}
		}
	}
}
