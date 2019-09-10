package TP1;

import java.io.DataInputStream;
import java.net.Socket;

public class Client
{

	private static Socket socket;

	public static  void main(String[] args) throws Exception
	{
		//Adresse et port du serveur 
		String serverAddress ="127.0.0.1";
		int port =5000;

		//Creation d'une nouvelle connexio avec le serveuer 
		socket =new Socket(serverAddress,port);
		System.out.format("The server is running on %s:%d%n",serverAddress,port);

		//Creation d'un canal entrant pour recevoir  les messages envoyes par le serveur 
		DataInputStream in = new DataInputStream(socket.getInputStream());

		//Attente de la reception d'un message envoye par le serveur sur l ecanal 
		String helloMessageFromServer=in.readUTF();
		System.out.println(helloMessageFromServer);
		//Fermeture de la connexion avec le serveur 
		socket.close();



	}





}
