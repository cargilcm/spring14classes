package P2PConnector;
import java.io.Serializable;


public class RegistrationPacket implements Serializable{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public static final transient int SIZE = 192;
		private Integer port;
		private String id;//this will be variable length when serialized?	
		
		public RegistrationPacket(String id2, Integer port2) {
			this.id = id2;
			this.port = port2;
		}
		
		public int getPort() {
			return port;
		}
		public void setPort(int port) {
			this.port = port;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
	};
