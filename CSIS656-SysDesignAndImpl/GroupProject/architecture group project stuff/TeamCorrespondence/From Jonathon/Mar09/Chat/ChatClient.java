import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.net.*;

public class ChatClient extends JFrame 
						implements ActionListener {
  private JTextArea textArea = new JTextArea();
  private JScrollPane jsp = new JScrollPane(textArea);
  private JTextField textField = new JTextField();
  private Socket connection;
  private ObjectInputStream reader;
  private ObjectOutputStream writer;

  public ChatClient() {
    super("ChatClient");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(600,400);
    add(jsp, "Center");
    add(textField, "South");
    textField.addActionListener(this);
    connect();
  }


  public void connect() {
    try {
      connection = new Socket("localhost", 8000);
      OutputStream os = connection.getOutputStream();
      writer = new ObjectOutputStream(os);
      InputStream is = connection.getInputStream();
      reader = new ObjectInputStream(is);
      ChatClientThread listener 
				= new ChatClientThread(reader, this);
      listener.start();
    }
    catch(IOException ioe){ 
      ioe.printStackTrace(); 
    }
  }
	
  public void actionPerformed(ActionEvent ae) {
	    String message = textField.getText();
	    textField.setText("");
	    ChatNetworkObject outgoing = new ChatNetworkObject(
				ChatNetworkObject.CHAT_MESSAGE, message);
	    try {
	      writer.writeObject(outgoing);
	    }
	    catch (IOException ioe) {
	      JOptionPane.showMessageDialog(this, "Server Lost");
	      System.exit(0);
	    }
  }
		
  public void addText(String text) {
		textArea.append(text + "\n");
  }
		
  public static void main(String[] args) {
		ChatClient frame = new ChatClient();
		frame.setVisible(true);
  }
}
