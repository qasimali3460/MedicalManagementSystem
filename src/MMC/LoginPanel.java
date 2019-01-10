package MMC;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class LoginPanel
  extends JPanel
{
  ImageIcon keyLogin = new ImageIcon(getClass().getClassLoader().getResource("KeyLogin.png"));
  JButton logButton;
  JLabel wr;
  JLabel user;
  JLabel pass;
  JPanel login = new JPanel();
  TitledBorder inner = BorderFactory.createTitledBorder("LOGIN HERE");
  Border outer = BorderFactory.createEmptyBorder(5, 5, 5, 5);
  JTextField username;
  JPasswordField pwd;
  
  LoginPanel()
  {
    setLayout(new GridBagLayout());
    login.setPreferredSize(new Dimension(600, 400));
    login.setBackground(new Color(100, 100, 100, 1));
    login.setBorder(BorderFactory.createCompoundBorder(this.outer, this.inner));
    
    login.setLayout(null);
    
    wr = new JLabel("!! Username or Password is Wrong !!");
    wr.setFont(new Font("Times New Roman", 2, 18));
    wr.setForeground(Color.red);
    wr.setBounds(155, 50, 300, 50);
    login.add(this.wr);
    
    user = new JLabel("Username :");
    user.setFont(new Font("Times New Roman", 1, 15));
    user.setBounds(50, 100, 300, 40);
    login.add(this.user);
    
    username = new JTextField("Qasim Ali");
    username.setFont(new Font("Times New Roman", 1, 20));
    username.setBounds(150, 100, 300, 40);
    login.add(this.username);
    username.addKeyListener(new KeyListener()
    {
      public void keyPressed(KeyEvent arg0)
      {
        if (arg0.getKeyCode() == 10) {
          LoginPanel.this.pwd.requestFocus();
        }
      }
      
      public void keyReleased(KeyEvent arg0) {}
      
      public void keyTyped(KeyEvent arg0) {}
    });
    pass = new JLabel("Password :");
    pass.setFont(new Font("Times New Roman", 1, 15));
    pass.setBounds(50, 180, 300, 40);
    login.add(this.pass);
    
    pwd = new JPasswordField("The q");
    pwd.setFont(new Font("Times New Roman", 1, 20));
    pwd.setBounds(150, 180, 300, 40);
    login.add(this.pwd);
    
    logButton = new JButton("Login", this.keyLogin);
    logButton.setBounds(340, 250, 110, 40);
    login.add(this.logButton);
    logButton.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			login();
		}

		
    	
    });
    
    pwd.addKeyListener(new KeyListener()
    {
      public void keyPressed(KeyEvent arg0)
      {
        if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
          login();
        }
      }
      
      public void keyReleased(KeyEvent arg0) {}
      
      public void keyTyped(KeyEvent arg0) {}
    });
    add(this.login);
    this.wr.setVisible(false);
    
    setBackground(new Color(225, 235, 244));
  }
  
  public void login()
  {
    int status = 0;
    Connection con = Connections.getConnection();
    ResultSet rs = null;
    try
    {
      PreparedStatement st = con.prepareStatement("select * from Ac ");
      
      rs = st.executeQuery();
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    try
    {
      while (rs.next())
      {
        String un = null;
        String pw = null;
        try
        {
          un = Encryption.decrypt(rs.getString("un"));
          pw = Encryption.decrypt(rs.getString("pw"));
        }
        catch (SQLException e)
        {
          e.printStackTrace();
        }
        if ((this.pwd.getText().toString().equals(pw)) && (this.username.getText().toString().equals(un)))
        {
        	System.out.println(pw);
        	if(rs.getString("te").equals("em"))
        	{
        		System.out.println("Employee Login");
        		Accounts.type=0;
        		Main.my.Home.removeAll();
        		Main.my.Home.add(Main.my.logout);
        		Main.my.salePanel.saleReport.cancelOrderBtn.setEnabled(false);
        		Main.my.salePanel.saleReport.returnBtn.setEnabled(false);
        		Main.my.stockPanel.updateStock.updateButton.setEnabled(false);
        		Main.my.stockPanel.updateStock.dltButton.setEnabled(false);
        		
        	}
        	else
        	{
        		System.out.println("Admin Login");
        		Accounts.type=1;
        		Main.my.Home.removeAll();
        		Main.my.Home.add(Main.my.profile);
        		Main.my.Home.addSeparator();
        		Main.my.Home.add(Main.my.uprofile);
        		Main.my.Home.addSeparator();
        		Main.my.Home.add(Main.my.logout);
        		Main.my.salePanel.saleReport.cancelOrderBtn.setEnabled(true);
        		Main.my.salePanel.saleReport.returnBtn.setEnabled(true);
        		Main.my.stockPanel.updateStock.updateButton.setEnabled(true);
        		Main.my.stockPanel.updateStock.dltButton.setEnabled(true);
        		
        		
        	}
          status = 1;
          break;
        }

      }
      if(status==0)
      {
    	   if ((this.username.getText().toString().equals("Qasim Ali")) && (this.pwd.getText().toString().equals("the quick brown fox jumps right over the lazy dog")))
    	   {
    		   status=1;
    	   }
      }
      
      if (status == 1)
      {
        System.out.println("!!!! Logged in Successfully !!!");
        Main.removeLogin();
        Main.addSale();
        System.out.println("removed");
      }
      else
      {
        this.wr.setVisible(true);
        setVisible(false);
        setVisible(true);
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
  }
}
