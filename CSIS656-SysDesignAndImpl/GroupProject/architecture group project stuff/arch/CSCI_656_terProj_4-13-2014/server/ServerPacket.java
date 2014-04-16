package server;

import java.io.Serializable;
import java.net.SocketAddress;

public class ServerPacket implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 319131085644982914L;

	public enum RequestType {
		Register,
		Login,
		GetUsers,
		Disconnect,
		Acknowledgement,
		Failed
	}
	
	private RequestType request_type;
	
	private int data_length;
	
	private byte[] data;
	
	private int password_length;
	
	private byte[] password;
	
	/*
	 * These fields are used to return a user list to requestor.
	 * Since Strings are not supported for serialization, we send
	 * a 2d array of bytes for user names. The 
	 * 
	 * user_list_length is the number of user records to be sent.
	 * 
	 * user_data_length is an array of integers that reflects the length of the 
	 * 		user name at the corresponding index.
	 * 
	 * user_data contains byte arrays that are Strings converted to byte arrays
	 * 
	 * addr_length is the number of SocketAddress returned. Is redundant to 
	 * 		user_list_length
	 * 
	 * addr is a array of SocketAddress in which each address corresponds to a
	 * 		user at that index.
	 * 
	 */
	private int user_list_length;
	private int[] user_data_length;
	private byte[][] user_data;
	private SocketAddress[] addr;
	
	//user_id should be less than half max size
	public ServerPacket(RequestType r, String user_id, 
					SocketAddress[] a, byte[][] u_d) {
		
		//copy request type
		this.request_type = r;
		
		//generate data user_id data to byte array
		this.data = new byte[RegistryServer.MAX_USER_NAME];
		byte[] temp = user_id.getBytes();
		this.data_length = temp.length;
		for(int i = 0; i < data_length; i++)
			data[i] = temp[i];

		//create SOckAddr structure to specified dims
		this.addr = new SocketAddress[RegistryServer.MAX_USERS];
		if (a != null) {
			//how may users are online?
			this.user_list_length = a.length;
			for (int i = 0; i < user_list_length; i++) 
				this.addr[i] = a[i];
		}
		
		//generate user list to specified dims
		this.user_data = 
				new byte[RegistryServer.MAX_USERS][RegistryServer.MAX_USER_NAME];
		this.user_data_length = new int[RegistryServer.MAX_USERS];
		if(u_d != null) {
			for (int i = 0; i < u_d.length; i++) {
				user_data_length[i] = u_d[i].length;
				for (int j = 0; j < u_d[i].length; j++)
					user_data[i][j] = u_d[i][j];
			}
		}
	}
	
	public ServerPacket(RequestType r, String user_id) {
		this(r, user_id, null, null);
	}
	
	public RequestType getRequestType() {
		return this.request_type;
	}
	
	public byte[] getData() {
		byte[] temp = new byte[data_length];
		for (int i = 0; i < temp.length; i++)
			temp[i] = data[i];
		return temp;
	}
	
	public byte[][] getUserData() {
		byte[][] temp = new byte[user_list_length][];
		for (int i = 0; i < user_list_length; i++) 
			for (int j = 0; j < user_data_length[i]; j++) {
				temp[i][j] = user_data[i][j];
			}
		return temp;
	}
	
	public SocketAddress[] getAddr() {
		SocketAddress[] temp = new SocketAddress[user_list_length];
		for (int i = 0; i < temp.length; i++)
			temp[i] = addr[i];
		return temp;
	}

	public byte[] getPassword() {
		byte[] temp = new byte[password_length];
		for (int i = 0; i < temp.length; i++)
			temp[i] = password[i];
		return temp;
	}
}
