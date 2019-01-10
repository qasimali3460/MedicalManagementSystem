package MMC;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

public class Connections {

	public Connections() {
		// TODO Auto-generated constructor stub
	}
	public static Connection getConnection()
	{
		Connection con=null;
        File db=new File("D://Database.accdb");
        if(!db.exists())
        {
        	db=new File("E://Database.accdb");
        	if(!db.exists())
        	{
        		db=new File("C://Database.accdb");
        		if(!db.exists())
        		{
        			JOptionPane.showMessageDialog(null,"Database not found");
        			JOptionPane.showMessageDialog(null, "Place database File in D or E or C drive");
        			
        		}
        	}
        }
       
        	
		try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
        try {
            con=DriverManager.getConnection("jdbc:ucanaccess://D://Database.accdb");
        } catch (SQLException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return con;
    
	}

}
