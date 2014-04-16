package applet;

import java.net.DatagramSocket;
import java.net.SocketException;

public class ServerConnector {
	
	private static final int SERVER_SOCKET = 12345;//remote server
	
	private static final String SERVER = "macs.citadel.edu";//could be localhost for testing
	
	DatagramSocket dsock;
	
	public ServerConnector() {
		
		try {
			dsock = new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}
