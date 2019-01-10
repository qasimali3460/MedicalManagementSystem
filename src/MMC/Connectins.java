package MMC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Connectins {

	public Connectins() {
		// TODO Auto-generated constructor stub
	}
	public static Connection getconnection()
	{
		Connection con=null;
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
        try {
            con=DriverManager.getConnection("jdbc:ucanaccess://C:Database.accdb");
        } catch (SQLException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return con;
    
	}

}
