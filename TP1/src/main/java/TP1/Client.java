package TP1;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/** Le client doit �tre capable de lire un fichier texte et d�envoyer son contenu au serveur qui retransmettra aussit�t son contenu au client. Ce dernier devra intercepter le contenu du fichier texte. Une fois la r�ception termin�e, le serveur devra inverser le contenu du fichier de sorte � ce que la premi�re ligne re�ue soit la derni�re ligne envoy�e vers le client. **/


public class Client {

	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {

		Socket clientSocket = null;
		try {
			// Cr�ation d'un socket client vers le serveur. Ici 127.0.0.1 est indicateur que
			// le serveur s'ex�cute sur la machine locale. Il faut changer 127.0.0.1 pour
			// l'adresse IP du serveur si celui-ci ne s'ex�cute pas sur la m�me machine. Le port est 5000.
			clientSocket = new Socket("127.0.0.1", 5000);
			ObjectOutputStream objectOutput = new ObjectOutputStream(clientSocket.getOutputStream());
			// Ici, on suppose que le fichier que vous voulez inverser se nomme text.txt
			List<String> linesToSend = readFile("text.txt");
			// �criture de l'objet � envoyer dans le output stream. Attention, la fonction
			// writeObject n'envoie pas l'objet vers le serveur! Elle ne fait qu'�crire dans
			// le output stream.
			objectOutput.writeObject(linesToSend);
			// Envoi des lignes du fichier texte vers le serveur sous forme d'une liste.
			objectOutput.flush();
			// Cr�ation du input stream, pour recevoir les donn�es trait�es du serveur.
			ObjectInputStream obj = new ObjectInputStream(clientSocket.getInputStream());
			// Not� bien que la fonction readObject est bloquante! Ainsi, l'ex�cution du
			// client s'arr�te jusqu'� la r�ception du r�sultat provenant du serveur!
			Stack<String> receivedStack = (Stack<String>) obj.readObject();
			// �criture du r�sultat dans un fichier nomm�e FichierInversee.txt
			writeToFile(receivedStack, "FichierInversee.txt");
		} finally {
			// Fermeture du socket.
			clientSocket.close();
		}
	}

	// Fonction permettant de lire un fichier et de stocker son contenu dans une liste.
	private static List<String> readFile(String nomFichier) throws IOException {
		List<String> listOfLines = new ArrayList<String>();
		String line = null;
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		try {
			fileReader = new FileReader(nomFichier);

			bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				listOfLines.add(line);
			}
		} finally {
			fileReader.close();
			bufferedReader.close();
		}
		return listOfLines;
	}

	// Fonction permettant d'�crire dans un fichier les donn�es contenues dans la
	// stack re�u du serveur.
	private static void writeToFile(Stack<String> myStack, String nomFichier) throws IOException {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new FileWriter(nomFichier));
			while (!myStack.isEmpty()) {
				out.write(myStack.pop() + "\n");
			}
		} finally {
			out.close();
		}
	}
}
