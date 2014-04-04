import java.util.*;
import java.io.*;
import java.net.*;
	
public class ChatServer {
  private static ArrayList<ChatServerThread> threads 
				= new ArrayList<ChatServerThread>();

  public static void sendToAll(
			String message, int clientNumber) 
				throws IOException {
      message = "Client " 
			+ clientNumber 
			+ ": " + message;
    	ChatNetworkObject outgoing 
			= new ChatNetworkObject(
				ChatNetworkObject.CHAT_MESSAGE,
				message);
	  for (int i = 0; i < threads.size(); i++)
			threads.get(i).send(outgoing);
  }
  
  public static void main(String[] args) {
	    try {
	      ServerSocket server = new ServerSocket(8000);
	      while (true) {
	        Socket socket = server.accept();
	        ChatServerThread newClient 
				= new ChatServerThread(
						socket, threads.size());
	        threads.add(newClient);
	        newClient.start();
	      }
	    } catch (IOException ioe) {ioe.printStackTrace(); }
  }
}
