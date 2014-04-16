package server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class ServerTest {

	public static void main(String[] args) throws ClassNotFoundException {
		// create our server
		RegistryServer rs = new RegistryServer();
		
		
		// initialize a request and send
		DatagramSocket s = null;
		try {
			s = new DatagramSocket();
		} catch (SocketException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		ServerPacket sp = new ServerPacket(ServerPacket.RequestType.Login, "Nick");
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		byte[] temp = null;
		try {
		  out = new ObjectOutputStream(bos);   
		  out.writeObject(sp);
		  temp = bos.toByteArray();
		  System.out.println("Size of ServerPacket in bytes: " + temp.length + "\n");
		  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  finally {
		
		  try {
		    if (out != null) {
		      out.close();
		    }
		  } catch (IOException ex) {
		    // ignore close exception
		  }
		  try {
		    bos.close();
		  } catch (IOException ex) {
		    // ignore close exception
		  }
		}
		
		InetSocketAddress saddr = new InetSocketAddress("localhost", 12345);
		
		DatagramPacket dp = null;
		try {
			dp = new DatagramPacket(temp,temp.length, saddr);
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			s.send(dp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
