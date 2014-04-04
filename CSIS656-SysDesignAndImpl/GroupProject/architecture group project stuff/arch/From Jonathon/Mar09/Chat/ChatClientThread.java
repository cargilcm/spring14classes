import java.io.*;

public class ChatClientThread extends Thread {
	private ObjectInputStream reader;
    private ChatClient gui;
    private static final int REST_TIME = 33;

    public ChatClientThread(
		ObjectInputStream initReader, ChatClient initGUI) {
        reader = initReader;
        gui = initGUI;
    }

    public void run() {
      ChatNetworkObject incoming;
      while (reader != null) {
        try {
          incoming = (ChatNetworkObject)reader.readObject();
          String text = (String)incoming.getData();
          gui.addText(text);
        }
        catch (Exception e) { e.printStackTrace(); }
      }
   }
}
