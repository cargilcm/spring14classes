package applet;

import java.applet.Applet;
import java.net.InetAddress;
import java.net.UnknownHostException;

import P2PConnector.P2PConnector;

public class P2PWrapper extends Applet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1099257931794371291L;

	private static final int LOCAL_PORT = 2424;//should try to get available??
	
	private P2PConnector pconn;// = new P2PConnector();
	
	public void init() {
		
		String user_id = "user1";
		
		pconn = new P2PConnector(user_id, LOCAL_PORT, this);
		
	}
	
	public Boolean addConnection(String id, String ip, int socket) {
		
		InetAddress inet_ip = null;
		try {
			inet_ip = InetAddress.getByAddress(ip.getBytes());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return this.pconn.addConnection(id, inet_ip, socket);//if not null
	}
	
	public Boolean sendMessage(String id, String msg) {
		return this.pconn.sendMessage(id, msg);
	}
	
	/*public Boolean receiveMessages(String id, MessageBoard m) {
		//should wire the connection from target output to messageboard applet
		
		return null;
		
	}*/
	
	public Boolean disconnect(String id) {
		
		
		return pconn.disconnect(id);
		
	}

}
