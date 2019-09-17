package TP1;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.net.Socket;

public class Server
{
	private static ServerSocket listener;

	public static void main(String[] args) throws Exception
	{
		int clientNumber = 0;
		String serverAddress = "132.207.29.124";
		int serverPort = 5003;

		//creation de la connexion
		listener = new ServerSocket();
		listener.setReuseAddress(true);
		InetAddress serverIP = InetAddress.getByName(serverAddress);

		//association de l'adresse et du port a la connexion
		listener.bind(new InetSocketAddress(serverIP, serverPort));

		System.out.format("The server is running on %s:%d%n", serverAddress, serverPort);

		try{
			while(true)
			{
				new ClientHandler(listener.accept(), clientNumber++).start();
			}
		}
		finally
		{
			listener.close();
		}
	}

	private static class ClientHandler extends Thread{
		private Socket socket;
		private int clientNumber;
	
		public ClientHandler(Socket socket, int clientNumber)
		{
			this.socket = socket;
			this.clientNumber = clientNumber;
			System.out.println("New connection with client" + clientNumber + " at " + socket);
		}
	
		public void run()
		{
			try{
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());

				out.writeUTF("Hello from server - you are client n" + clientNumber);





				///////////si le client a envoyer un messages 
				System.out.println("Execution du message du client" );
			   DataInputStream in = new DataInputStream(socket.getInputStream());

				//Attente de la reception d'un message envoye par le serveur sur l ecanal 
				String MessageFromClient=in.readUTF();
				System.out.println(MessageFromClient);




				/////////////////////////////////
			} catch (IOException e)
			{
				System.out.println("Error handling client " + clientNumber + ": " + e);
			}
			finally
			{
				try
				{
					socket.close();
				}
				catch(IOException e)
				{
					System.out.println("Couldn't close a socket");
				}
				System.out.println("Connection with client " + clientNumber + " closed");
			}
		}
	}
}


