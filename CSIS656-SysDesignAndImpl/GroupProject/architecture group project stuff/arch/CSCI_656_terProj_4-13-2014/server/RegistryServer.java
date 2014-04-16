package server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class RegistryServer {
	
	
	
	private static final int SERVER_PORT = 12345;

	public static final int MAX_USER_NAME = 20;

	public static final int MAX_USERS = 20;

	/*
	 * Listening for user requests
	 */
	private DatagramSocket socket;
	
	private DatabaseConnector db;
	
	private RequestHandler req_handler;
	
	
	/*
	 * Constructor
	 */
	public RegistryServer() throws ClassNotFoundException {
		
		//initialize JDBC
		db = new DatabaseConnector();
		
		/*
		 * initialize request handler to listen for users
		 */
		try {
			
			socket = new DatagramSocket(SERVER_PORT);
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		RequestHandler req_handler = new RequestHandler(socket);
		
		Thread t = new Thread(req_handler);
		
		t.start();
		
	}
	
	/*
	 * Class handles user requests
	 */
	private class RequestHandler implements Runnable{
		
		private static final int TIMEOUT = 1000;
		private static final int BUFFER_SIZE = 5000;//Please check this!!!!!!!!!
		//send to user
		DatagramSocket socket;
		
		public RequestHandler(DatagramSocket s) {
			// TODO Auto-generated constructor stub
			socket = s;
		}

		private byte[] serialize(ServerPacket response) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutput out = null;
			byte[] temp = null;
			try {
			  out = new ObjectOutputStream(bos);   
			  out.writeObject(response);
			  temp = bos.toByteArray();
			  System.out.println("Size of ServerPacket in bytes: " + temp.length + "\n");
			  
			} catch (IOException e) {
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

		private ServerPacket deserialize(byte[] bs) {
			 ObjectInputStream ob_in;
				try {
					ob_in = new ObjectInputStream(new ByteArrayInputStream(bs));
			        ServerPacket obj = (ServerPacket) ob_in.readObject();
			        ob_in.close();
			        return obj;
				} catch (IOException | ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			return null;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			byte[] buf = new byte[BUFFER_SIZE];
			
			DatagramPacket new_packet = new DatagramPacket(buf, buf.length);
			
			while(true) {
				
				try {
					
					socket.setSoTimeout(TIMEOUT);
					
					socket.receive(new_packet);
					
					SocketAddress sender_ip = new_packet.getSocketAddress();
					
					ServerPacket new_request = this.deserialize(new_packet.getData());
					
					String user_id = new String(new_request.getData());//encoding???
					
					ServerPacket response = null;
					
					if (new_request.getRequestType() == 
								ServerPacket.RequestType.Login) {
						//do login stuff
						
						//verify that is registered user w/ correct password
						String query = ("SELECT * FROM user WHERE user_id = "
									+ user_id);
						
						ResultSet rs = db.executeQuery(query);
						if (rs == null ) {
							response = new ServerPacket(
									ServerPacket.RequestType.Failed, 
									user_id);
							
						} else {
							//update user status
							Date date = new Date();
							query = "INSERT "
									+ "INTO user_status "
									+ "VALUES "
									+ "(null, " + user_id + ", 1," 
									+ date.getTime() + ")";
							db.executeQuery(query);
							
							//send acknowledgement
							response = new ServerPacket(
										ServerPacket.RequestType.Acknowledgement, 
										user_id);
						}
						
												
					} else if (new_request.getRequestType()
							== ServerPacket.RequestType.Disconnect) {
						//do disconnect stuff
						
						//don't worry about validating user registration?
						Date date = new Date();
						String query = "INSERT INTO user_status "
								+ "VALUES (null, " + user_id + ", 0," 
								+ date.getTime();
						db.executeQuery(query);
						
						//send acknowledgment
						response = new ServerPacket(
									ServerPacket.RequestType.Acknowledgement, 
									user_id);
						
					} else if (new_request.getRequestType()
							== ServerPacket.RequestType.GetUsers) {
						//return list of users logged in
						String query = ""; //what is this going to be??lol
						
						ResultSet rs = db.executeQuery(query);
						byte[][] user_list = new byte[RegistryServer.MAX_USERS][];
						SocketAddress[] addr = new SocketAddress[RegistryServer.MAX_USERS];
						int count = 0;
						while (rs.next()) {
							//populate addr[]
							int ip = rs.getInt("ip");
							byte[] result = new byte[4];
							result[0] = (byte) (ip >> 24);
							result[1] = (byte) (ip >> 16);
							result[2] = (byte) (ip >> 8);
							result[3] = (byte) (ip /*>> 0*/);
							InetAddress temp = InetAddress.getByAddress(result);
							int port = rs.getInt("port");
							addr[count] = new InetSocketAddress(temp, port);
							
							//populate user_list
							String next_user_id = rs.getString("user_id");
							byte[] temp_byte = next_user_id.getBytes();
							for (int i = 0; i < temp_byte.length; i++)
								user_list[count][i] = temp_byte[i];
							
							//increment count
							count++;
							
						}
						
						//build response ServerPacket
						response = new ServerPacket(
								ServerPacket.RequestType.GetUsers, 
								user_id, addr, user_list); 
						
					} else if (new_request.getRequestType()
							== ServerPacket.RequestType.Register) {
						//do register stuff
						
						//verify that is not registered user w/ correct password
						String query = ("SELECT * FROM user WHERE user_id = "
									+ user_id);
						
						ResultSet rs = db.executeQuery(query);
						if (rs != null ) {// user name already taken
							response = new ServerPacket(
									ServerPacket.RequestType.Failed, 
									user_id);
							
						} else {
							//update user status
							String password = new String(new_request.getPassword());
							Date date = new Date();
							query = "INSERT "
									+ "INTO user "
									+ "VALUES (null," + user_id + "," 
									+ password + "," + date.getTime() + ")";
							db.executeQuery(query);
							
							//send acknowledgment
							response = new ServerPacket(
										ServerPacket.RequestType.Acknowledgement, 
										user_id);
						}
					} else {
						System.out.println("Unrecognized packet type!\n");
						response = new ServerPacket(
								ServerPacket.RequestType.Failed, 
								user_id);
					}
					
					//send response
					byte[] temp = this.serialize(response);
					
					DatagramPacket data_packet = 
								new DatagramPacket(temp, temp.length, 
											sender_ip);
					
					socket.send(data_packet);
					//anything else???
					
				} catch (SocketException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					// processing resultset
					e.printStackTrace();
				}
				
				//this is a good place to yield
				Thread.yield();
				
			}
		}
		
		
	}
	
	private class DatabaseConnector {
		
		/* 
		 * MySQL stuff
		 */
		Statement stm;
		private Connection con;
		static final String url = "jdbc:mysql://localhost:3306/test";
		
		public DatabaseConnector() throws ClassNotFoundException {
			
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection("jdbc:mysql://localhost/test?" + "user=root&password=admin");
				
//				con = DriverManager.getConnection(url,"root", "admin");//change to appropriate
				
				stm = this.con.createStatement();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		public Connection getConnection() {
			return this.con;
		}
		
		public ResultSet executeQuery(String query) {
			
			ResultSet rs = null;
			
			try {
				
				if(!this.con.isClosed()){}//Nothing to do
				else {
				
					rs = stm.executeQuery(query);
					
					if (!rs.isBeforeFirst() ) {
						rs = null;//return null for empty result set
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return rs;
			
		}
		
	}
	
}
