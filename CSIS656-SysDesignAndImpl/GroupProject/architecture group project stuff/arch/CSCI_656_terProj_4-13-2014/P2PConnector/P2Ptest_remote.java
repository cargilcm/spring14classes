package P2PConnector;

import applet.P2PWrapper;

public class P2Ptest_remote {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		P2PWrapper w = new P2PWrapper();
		P2PConnector c2 = new P2PConnector("user2", 11235, w);
		
		while(true){}
		
	}

}
