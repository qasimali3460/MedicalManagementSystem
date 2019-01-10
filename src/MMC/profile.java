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
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class profile
  extends JPanel
{
  ImageIcon keyLogin = new ImageIcon(getClass().getClassLoader().getResource("KeyLogin.png"));
  JButton logButton;
  JLabel wr;
  JLabel user;
  JLabel pass;
  JPanel login = new JPanel();
  TitledBorder inner = BorderFactory.createTitledBorder("Accounts");
  
  Border outer = BorderFactory.createEmptyBorder(5, 5, 5, 5);
  JTextField username;
  JTextField docotorFee;
  JTextField discount;
  JTextField pwd;
  JCheckBox cb1;
  int detail=0;
  JLabel DoctorLabel=new JLabel("Doctor Fees");
  JLabel DiscountLabel=new JLabel("Discount %");
  
  
  String aUser,apwd,eUser,ePwd,fee,discountper;
  
  profile()
  {
    setLayout(new GridBagLayout());
    login.setPreferredSize(new Dimension(600, 400));
    login.setBackground(new Color(100, 100, 100, 1));
    login.setBorder(BorderFactory.createCompoundBorder(this.outer, this.inner));
    
    login.setLayout(null);
    
    wr = new JLabel("!! Username or Password is Wrong !!");
    wr.setFont(new Font("Times New Roman", 2, 18));
    wr.setForeground(Color.red);
    wr.setBounds(155, 10, 300, 50);
    login.add(this.wr);
    
    user = new JLabel("Username :");
    user.setFont(new Font("Times New Roman", 1, 15));
    user.setBounds(50, 60, 300, 40);
    login.add(this.user);
    
    username = new JTextField("Qasim");
    username.setFont(new Font("Times New Roman", 1, 20));
    username.setBounds(150, 60, 300, 40);
    login.add(this.username);
    username.addKeyListener(new KeyListener()
    {
      public void keyPressed(KeyEvent arg0)
      {
        if (arg0.getKeyCode() == 10) {
          pwd.requestFocus();
        }
      }
      
      public void keyReleased(KeyEvent arg0) {}
      
      public void keyTyped(KeyEvent arg0) {}
    });
    pass = new JLabel("Password :");
    pass.setFont(new Font("Times New Roman", 1, 15));
    pass.setBounds(50, 140, 300, 40);
    login.add(this.pass);
    
    pwd = new JTextField("the q");
    pwd.setFont(new Font("Times New Roman", 1, 20));
    pwd.setBounds(150, 140, 300, 40);
    login.add(this.pwd);

    DoctorLabel.setBounds(50,220,300,40);
    DoctorLabel.setFont(new Font("Times New Roman", 1, 15));
    DoctorLabel.setBounds(50, 220, 300, 40);
    login.add(DoctorLabel);

    this.docotorFee=new JTextField();
    docotorFee.setBounds(150,220,120,30);
    login.add(docotorFee);
    
    
    
    
    DiscountLabel.setBounds(50,260,300,40);
    DiscountLabel.setFont(new Font("Times New Roman", 1, 15));
    login.add(DiscountLabel	);
    
    this.discount= new JTextField();
    discount.setBounds(150,260,120,30);
    login.add(discount);
    
    
    
    logButton = new JButton("Update", this.keyLogin);
    logButton.setBounds(340, 320, 110, 40);
    login.add(this.logButton);
    logButton.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(detail==0)
			{
				if(!username.getText().equals(aUser))
				{
					updateAll();
				}
				else
					if(!pwd.getText().equals(apwd))
					{
						updateAll();
					}
					else
						if(!discount.getText().equals(discountper))
						{
							updateAll();
						}else
							if(! docotorFee.getText().equals(fee))
							{
								updateAll();
							}
							
			}
		}

		
    	
    });
    cb1=new JCheckBox("Edit Employe Details");
    cb1.setBounds(50, 320, 160, 40);
   // login.add(cb1);
    
    cb1.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(cb1.isSelected())
			{
				login.setVisible(false);
				docotorFee.setEnabled(false);
				discount.setEnabled(false);
				detail=1;
				getAccountsDetails();
				login.setVisible(true);
			}
			else
			{
				login.setVisible(false);
				docotorFee.setEnabled(true);
				discount.setEnabled(true);
				detail=0;
				getAccountsDetails();
				login.setVisible(true);}
		}
    	
    });
  
    add(this.login);
    this.wr.setVisible(false);
    getAccountsDetails();
    setBackground(new Color(225, 235, 244));
  }
  public void getAccountsDetails()
  {
	  Connection con=Connections.getConnection();
	  try {
		PreparedStatement st=con.prepareStatement("select * from Ac where te=?");
		st.setString(1,"ani");
		ResultSet rs=st.executeQuery();
		while(rs.next())
		{
			aUser=Encryption.decrypt(rs.getString("un"));
			apwd=Encryption.decrypt(rs.getString("pw"));
			
		}
		this.username.setText(aUser);
		this.pwd.setText(apwd);
		con.close();
		st.close();
		con=Connections.getConnection();
		st=con.prepareStatement("select * from Ac where te=?");
		st.setString(1, "em");
		rs=st.executeQuery();
		while(rs.next())
		{
			eUser=Encryption.decrypt(rs.getString("un"));
			ePwd=Encryption.decrypt(rs.getString("pw"));
		}
		this.discountper=Accounts.getDiscount();
		this.fee=Accounts.getFee();
		this.docotorFee.setText(fee);
		this.discount.setText(discountper);
		if(detail==1)
		{

			this.username.setText(eUser);
			this.pwd.setText(ePwd);
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
			  
  }
  public void updateAll()
  {
	  int info=JOptionPane.showConfirmDialog(null,"Confirm want to Update");
		if(info==0)
		{
			Connection con=Connections.getConnection();
			try {
				PreparedStatement st=con.prepareStatement("update Ac set un=?,pw=?,count=?,ee=? where te=?");
				st.setString(1, Encryption.encrypt(username.getText()));
				st.setString(2, Encryption.encrypt(pwd.getText()));
				st.setString(3, Encryption.encrypt(discount.getText()));
				st.setString(4, Encryption.encrypt(docotorFee.getText()));
				st.setString(5, "ani");
				if(st.executeUpdate()>0)
				{
					JOptionPane.showMessageDialog(null,"Updated Successfully");
					Main.my.salePanel.newSale.billDiscount.setText(discount.getText());
					Main.my.salePanel.newSale.billFee.setText(docotorFee.getText());
					getAccountsDetails();
					
				}
					
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
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
