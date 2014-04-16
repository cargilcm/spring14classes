package P2PConnector;
import netscape.javascript.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.ConcurrentHashMap;

import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;

import applet.P2PWrapper;

/*
 * Test class for peer-to-peer connections.
 * Uses java socket API to join to browser-based clients
 * What this should do:
 * 		Establish a connection to another user
 * 
 * 		Send data stream over that connection
 * 
 * 		receive messages from the user
 * 
 * 		disconnect from user
 * 
 */


public class P2PConnector {//?
	
	private static final int MAX_MESSAGE_SIZE = 100;
	
	private static final int MAX_CONNECTIONS = 20;

	private static final int ACCEPT_TIMEOUT = 10000;

	/* 
	 * P2PConnector.targets
	 * need a set of targets to connect to other users. when a client(s) is 
	 * selected, a socket will be established stored here. when a message is 
	 * sent, all targets will be sent
	 * hash on Target.user_id
	 */
	private ConcurrentHashMap<String, Target> targets = 
						new ConcurrentHashMap<String, Target>(MAX_CONNECTIONS);
	
	/*
	 * Socket Factory 
	 */
	
	SocketFactory socket_factory;
	
	ServerSocketFactory server_factory;
	
	/*
	 * P2PConnector.server
	 * Listens for connection requests from other users.
	 * Port number will need to be broadcast.
	 */
	private P2PServer server;

	private String id;

	private P2PWrapper wrapper;
	
	public P2PConnector(String id, int port, P2PWrapper p) {
		/*
		 * what work on create?
		 * create server socket and start listening
		 * 
		 */
		this.wrapper = p;
		
		this.id = id;
		
		socket_factory = SocketFactory.getDefault();
		
		server_factory = ServerSocketFactory.getDefault();
		
		server = new P2PServer(port);
		
		Thread t = new Thread(server);
		
		t.start();
		
	}
	
	/*
	 * Add a connection for a remote client. 
	 */
	public Boolean addConnection(String id, InetAddress ip, int socket) {
		
		ServerSocket temp_sock = null;
		Socket new_socket = null;
		try {
			temp_sock = server_factory.createServerSocket();
			
			temp_sock.bind(null);//????
			
			//form message
			byte[] reg_data = null;
			
			//could check on errors (port == -1)
			if (temp_sock.getLocalPort() < 0)
				System.out.println("accept server socket is not bound!\n");
			RegistrationPacket reg_packet = 
					new RegistrationPacket(this.id, 
							new Integer(temp_sock.getLocalPort()));
			
			//serialize this
			reg_data = this.serializePacket(reg_packet);
			//byte[] reg_data = SerializationUtils.serialize(reg_packet);
			
			//send registration message
			DatagramPacket new_msg = 
					new DatagramPacket(reg_data, RegistrationPacket.SIZE, ip, socket);
			
			DatagramSocket d_sock = new DatagramSocket();
			
			d_sock.send(new_msg);
			
			temp_sock.setSoTimeout(ACCEPT_TIMEOUT);
			
			new_socket = temp_sock.accept();
			
			Target new_targ = new Target(id, new_socket);
			
			targets.put(id, new_targ);
			
			Thread t = new Thread(new_targ);
		
			d_sock.close();
			
			t.start();
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Unable to connect to remote host on "
											+ ip.toString() + "\n");
			return false;//could not connect
		}
		
		Target targ;
		
		//need to tell the recipient who is connecting, (send id msg)
		//and wait for response
		
		if(new_socket != null && new_socket.isConnected()) {//logic?
			targ = new Target(id, new_socket);
			
			Thread t = new Thread(targ);
			
			t.start();
			
		} else {
			return false;//unable to connect
		}
		
		if(targets.containsKey(id)) 
			return true;
		else{
			targets.put(id, targ);
			return true;
		}
			
	}
	
	/*
	 * Send a message to the target 'id'
	 * interface method
	 * 
	 */
	public Boolean sendMessage(String id, String msg) {
		
		//if target doesn't exist, should mark as needing to connect?
		if (targets.containsKey(id)) {
			System.out.println("Sending \"" + msg + "\" to " + id + "\n");
			Target send_to = targets.get(id);
			if (send_to.sendMessage(msg)) {
				System.out.println("Sent!");
			}
			return true;
		} else {
			System.out.println("Didn't send\n");
			return false;
		}
	}
	
	/*
	 * Set to receive messages. When Target receives a message, it will write 
	 * to the last registered MessageBoard.
	 */
	/*public Boolean receiveMessages(String id, MessageBoard m) {//needed???
		
		if(targets.containsKey(id)) {
			Target recv_from = targets.get(id);
			recv_from.message_in = m;
			return true;
		} else {
			return false;
		}		
	}*/
	
	/*
	 * Disconnect 
	 */
	public Boolean disconnect(String id) {
		
		Target targ = null;
		
		if(targets.contains(id)) {
			targ = targets.get(id);
		}
		
		if(targ != null) {
			try {
				targ.sendMessage("Good-Bye!");//could specify disconnect message
				targ.conn.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			targets.remove(id);
			return true;
		} else {
			return false;
		}
	}
	
	/*
	 * 
	 */
	private byte[] serializePacket(RegistrationPacket rp) {
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		byte[] temp = null;
		try {
		  out = new ObjectOutputStream(bos);   
		  out.writeObject(rp);
		  temp = bos.toByteArray();
		  System.out.println("Size of RegistrationPacket in bytes: " + temp.length + "\n");
		  
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
		
		return temp;
		
	}
	
	private RegistrationPacket deserializePacket(byte[] data) {
		
	        ObjectInputStream ob_in;
			try {
				ob_in = new ObjectInputStream(new ByteArrayInputStream(data));
		        RegistrationPacket obj = (RegistrationPacket) ob_in.readObject();
		        ob_in.close();
		        return obj;
			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return null;
	}


	/*
	 * Inner Class retain information for each other user in the network.
	 * Each Target is a separate thread and will constantly check for new
	 * message to send or receive.
	 * 
	 */
	class Target implements Runnable {
		private String user_id;
		protected Socket conn;
		protected OutputStreamWriter out;
		protected InputStreamReader in;
		//private Boolean recv_message = false;
		//private MessageBoard message_in;
		
		//pointer to MessageBoard object
		
		public Target(String id, Socket sock) {
			//do stuff to init
			this.user_id = id;
			this.conn = sock;
			
			try {
				this.in = new InputStreamReader(sock.getInputStream());
				this.out = new OutputStreamWriter(sock.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		/* 
		 * P2PConnector.Target.sendMessage
		 * Send a message to user.
		 * If a message is already in the queue to send, will return false.
		 * If the message is successfully added, return true.
		 * 
		 */
		public Boolean sendMessage(String msg) {
			
			try {
				out.write(msg);
				out.flush();
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//why? need to breakdown connection??
				return false;
			}
		}
		
		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#toString()
		 * For HashMap
		 */
		public String toString() {
			return this.user_id;
		}

		@Override
		public void run() {
			
			while(true) {
				
				//System.out.println("Hello from " + id);
				
				try {
					//if(this.in.ready()) {
						//System.out.println("Hello from " + id);
						char[] cbuf = new char[MAX_MESSAGE_SIZE];
						int size = this.in.read(cbuf);
						
						if(size > 0) {
							//System.out.println("Size greater than zero!\n");
							// invoke javascript method in messageboard
							//JSObject window = JSObject.getWindow(wrapper);//make sure to add back
							
							String message = this.user_id + " >> " + new String(cbuf);
							
							
							//eval("displayMessage(\"" + message +"\")");//ADD BACK!!!!!!!!
							System.out.println(message);
							//this.message_in.put(new String(cbuf));
					//	}
					} else {System.out.println("\nNo Messages recieved!\n");}
				} catch (SocketException s) {
					s.printStackTrace();
					//destroy connection
					System.out.println("\nConnection is gone, destroy target!!\n");
					break;
				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//wait for my turn again???
				Thread.yield();
			}
		}
	};
	
	private class P2PServer implements Runnable {
				
		private DatagramSocket server;

		public P2PServer(int serverPort) {
			// TODO Auto-generated constructor stub
			
			try {
				System.out.println(InetAddress.getLocalHost());
				server = new DatagramSocket(serverPort,InetAddress.getLocalHost());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Initializing server socket on port " 
					+ server.getInetAddress() + server.getLocalPort() + "\n");
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			//receive stuff
			//types of messages:
			//    new connections should create new Target 
			//
			//    control msg from server
			//
			//    other??
			
			
			//need to exchange a identification packet
			// first message received will be id
			
			byte[] buff = new byte[RegistrationPacket.SIZE];
			DatagramPacket new_msg = new DatagramPacket(buff, RegistrationPacket.SIZE);
			while(true) {
				
				try {
					
					server.setSoTimeout(10000);
					
					server.receive(new_msg);
					System.out.println("connections received on port " + server.getLocalPort() +"\n");
					
					byte[] temp = new_msg.getData();
					
					if (temp != null) {//will this work?
						
						//need to deserialize data and reconstruct Reg Packet
						RegistrationPacket new_client = deserializePacket(temp);
						System.out.println("Recieved reg packet from " 
									+ new_client.getId() + "from: " + new_msg.getAddress() + new_client.getPort());
						
						String new_id = new_client.getId();
						
						InetAddress new_addr = new_msg.getAddress();

						int port = new_client.getPort();
						
						Socket new_socket = socket_factory.createSocket(new_addr, port);
						
						Target new_targ = new Target(new_id, new_socket);
						
						targets.put(new_id, new_targ);
						
						Thread t = new Thread(new_targ);
						
						t.start();
						
					}
					
				} catch (IOException e1) {
					//e1.printStackTrace();
					if (e1 instanceof SocketTimeoutException) {
						System.err.println("No connections received on port " + server.getLocalPort() +"\n");
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				//is this needed for non-blocking operations?
				//might use 
				Thread.yield();
			}
		}

		
	}
	
	
	
	
}
