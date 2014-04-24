import java.io.*;
import java.net.*;

public class ChatServerThread extends Thread {
  private ObjectOutputStream writer;
  private ObjectInputStream reader;
  private int num;
	    
  public ChatServerThread(Socket connection, int initNum)
						throws IOException {
    num = initNum;
    OutputStream os = connection.getOutputStream();
    writer = new ObjectOutputStream(os);
    InputStream is = connection.getInputStream();
    reader = new ObjectInputStream(is);
  }
	    
  public void send(ChatNetworkObject outgoing)
						throws IOException {
    writer.writeObject(outgoing);
  }


  public void run() {
    ChatNetworkObject incoming;
    boolean stillConnected = true;
    while(stillConnected) {
      try {
        incoming = (ChatNetworkObject)reader.readObject();
        if (incoming.getType() ==
                   ChatNetworkObject.CHAT_MESSAGE) {
          String text = (String)incoming.getData();
          ChatServer.sendToAll(text, num);
        }
      }
      catch (Exception e) {
        stillConnected = false;
      }
    }
  }
}
