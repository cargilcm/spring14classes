package P2PConnector;
import java.net.InetAddress;
import java.net.UnknownHostException;

import applet.MessageBoard;
import applet.P2PWrapper;


public class P2Ptest2 {

	public static void main(String[] args) {
		P2PWrapper wrapper = new P2PWrapper();
		P2PConnector c2 = new P2PConnector("user2", 11234,wrapper);
		MessageBoard m2 = new MessageBoard();
		
		InetAddress addr1 = null;
		
		try {
			addr1 = InetAddress.getByName("macs.citadel.edu");
			System.out.println(addr1.toString());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//int port_number = 
		
		c2.addConnection("user1", addr1, 11235);
		
		//c2.receiveMessages("user1", m2);
		
		c2.sendMessage("user1", "Word up Dogg");
		
		while(true) {

			c2.sendMessage("user1", "Word up Dogg");
			
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
