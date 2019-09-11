package TP1;

import java.io.DataInputStream;
import java.net.Socket;
import java.util.Scanner; 

public class Client
{

	private static Socket socket;

	public static boolean checkAddress(String addressIp) {
        String[] addressValues = addressIp.split("\\.");

        // We need to get 4 elements exactly
        if(addressValues.length != 4)
            return false;

        // values need to be between 0 and 255    
        for (String a : addressValues){
            int valueInt = Integer.parseInt(a);
            if(valueInt > 255 || valueInt < 0){
                return false;
            }
        }
        return true;
    }

	//132.207.29.124

	public static  void main(String[] args) throws Exception
	{
		//Adresse et port du serveur 
		String serverAddress ="";
		int port =5003;

		Scanner scanner = new Scanner(System.in);
        do{
            System.out.print("Enter a valid IP address (ex: 123.123.12.1): ");
            serverAddress = scanner.next();
        }while(!checkAddress(serverAddress));

        do{
            System.out.print("Enter a valid port (entre 5000 et 5050):");
            port = scanner.nextInt();
        }while(port > 5050 || port < 5000);
	
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
