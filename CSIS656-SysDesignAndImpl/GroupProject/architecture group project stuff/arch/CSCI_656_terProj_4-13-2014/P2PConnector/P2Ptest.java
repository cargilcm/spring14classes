package P2PConnector;
import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

import applet.MessageBoard;
import applet.P2PWrapper;

public class P2Ptest {
	
	public static void main(String[] args) throws IOException {
		
		P2PWrapper w = new P2PWrapper();
		P2PConnector c1 = new P2PConnector("user1", 11234, w);
		MessageBoard m1 = new MessageBoard();
		
		
		
		InetAddress addr1 = null;
		
		try {
			addr1 = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//int port_number = 
		
		c1.addConnection("user2", addr1, 11235);
		
		//c1.receiveMessages("user2", m1);
		
		//c2.receiveMessages("user1", m2);
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		while(true) {
			System.out.println("Enter a message: ");
			String msg = in.readLine();
			if(msg.equalsIgnoreCase("exit")) {break;};
			c1.sendMessage("user2", msg);
			
			
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		}
		
		
	}
	
}
