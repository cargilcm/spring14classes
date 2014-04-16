import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import com.mysql.jdbc.*;
public class connectorTest{
	
	Connection conn = null;
	
	public connectorTest(){
		try {
			System.out.println("hello");
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/test?" + "user=root&password=admin");
			System.out.println("finished");
			Statement statement = conn.createStatement();
			int outcome = statement.executeUpdate("CREATE TABLE `chatInfo` ( " 
				+ "`ipAddress` varchar(11) NOT NULL DEFAULT '0.0.0.0',"
				+ "`sessionID` varchar(33) NOT NULL,"
				+ "PRIMARY KEY (`ipAddress`)"
				+ ") ENGINE=MyISAM;"
				);
			// Do something with the Connection
			// ..ie: print the int response code
			System.out.println(outcome);
		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		catch (Exception ex2){
		System.out.println(ex2);
		}
	}

//public static void main(String[] args){
//	connectorTest connTest = new connectorTest();
//	}

}