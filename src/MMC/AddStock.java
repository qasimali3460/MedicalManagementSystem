package MMC;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class AddStock extends JPanel{
	ImageIcon icon=new ImageIcon(getClass().getClassLoader().getResource("insert.png"));
	ImageIcon icon2=new ImageIcon(getClass().getClassLoader().getResource("clear.png"));
//	ImageIcon up=new ImageIcon(getClass().getClassLoader().getResource("ups.png"));
	ImageIcon bin=new ImageIcon(getClass().getClassLoader().getResource("dust.png"));
	JLabel Name;
	 JPanel addStockPanel=new JPanel();
	 TitledBorder inner=BorderFactory.createTitledBorder("New Stock");  
	 javax.swing.border.Border outer=BorderFactory.createEmptyBorder(5,5,5,5);
	 public static GTextField name;
	 public static JComboBox category;
	  public static JTextField quantity,price; 
	  JButton add,clear;
	  JLabel error;
	  String catp[]= {"Tablet","Syrup","Drops","other"};
		
	  AddStock()
	{
		 addComponent();
		AutoCompleteDecorator.decorate(category);
	
	}
	  public void addComponent()
	  {

			setName("Add Stock");
			setLayout(new GridBagLayout());
			addStockPanel.setPreferredSize(new Dimension(600,450));
			addStockPanel.setBackground(new Color(100,100,100,1));
			addStockPanel.setBorder(BorderFactory.createCompoundBorder(outer,inner));

			addStockPanel.setLayout(null);
			
			error=new JLabel("");
			error.setBounds(250,50,150,30);
			error.setForeground(Color.RED);
			addStockPanel.add(error);
			
			JLabel nameLabel=new JLabel("Name :");
			nameLabel.setBounds(140,80,100,30);
			addStockPanel.add(nameLabel);
			
			name = new GTextField(0  , 0 , true );
			refreshSearch(getArrayList());
			name.setWidthPopupPanel(400);
			name.setHeightPopupPanel(100);
			name.setBounds(220,80,200,30);
			name.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void focusLost(FocusEvent arg0) {
					if(!name.getText().equals(""))
					{
						System.out.println("Enter is pressed");
						
						System.out.println(name.getText());
						if(name.getDataList().contains(name.getText()))
						{
							System.out.println("Contains");
							Connection con=Connections.getConnection();
							try {
								PreparedStatement st=con.prepareStatement("select * from md where ne=?");
								st.setString(1, Encryption.encrypt(name.getText()));
								ResultSet rs=st.executeQuery();
									System.out.println("found");
									while(rs.next())
									{
										category.setSelectedItem(Encryption.decrypt(rs.getString("cat")));
										price.setText(Encryption.decrypt(rs.getString("pr")));
										break;
									}
								
									category.setEnabled(false);
									price.setEnabled(false);
								
								
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
				
			});
			name.addKeyListener(new KeyListener() {

				@Override
				public void keyPressed(KeyEvent arg0) {
						
					
				}

				@Override
				public void keyReleased(KeyEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void keyTyped(KeyEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
			});
			addStockPanel.add(name);
			
			
			JLabel catLabel=new JLabel("Category :");
			catLabel.setBounds(140,130,100,30);
			addStockPanel.add(catLabel);
			
			category = new JComboBox(catp);
			category.setEditable(true);
			category.setBounds(220,130,200,30);
			category.addKeyListener(new KeyListener() {

				@Override
				public void keyPressed(KeyEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void keyReleased(KeyEvent arg0) {
					// TODO Auto-generated method stub
					if(arg0.getKeyCode()==KeyEvent.VK_ENTER)
					{
						quantity.requestFocus();
					}
				}

				@Override
				public void keyTyped(KeyEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
			});
			category.addFocusListener(new FocusListener(){

				@Override
				public void focusGained(FocusEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void focusLost(FocusEvent arg0) {
				}
				
			});
			addStockPanel.add(category);
			category.addKeyListener(new KeyListener() {

				@Override
				public void keyPressed(KeyEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void keyReleased(KeyEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void keyTyped(KeyEvent e) {
					// TODO Auto-generated method stub
					
				}
				
			});

			JLabel quLabel=new JLabel("Quantity :");
			quLabel.setBounds(140,180,100,30);
			addStockPanel.add(quLabel);
			
			quantity=new JTextField();
			quantity.setBounds(220,180,200,30);
			addStockPanel.add(quantity);
			quantity.addKeyListener(new KeyListener() {

				@Override
				public void keyPressed(KeyEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void keyReleased(KeyEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void keyTyped(KeyEvent e) {
					char c=e.getKeyChar();
					if(!(Character.isDigit(c)||c==KeyEvent.VK_BACK_SPACE||c==KeyEvent.VK_DELETE))
					{
						getToolkit().beep();
						e.consume();
					}
				}
				
			});

			JLabel prLabel=new JLabel("Unit Price :");
			prLabel.setBounds(140,230,100,30);
			addStockPanel.add(prLabel);
			
			price=new JTextField();
			price.setBounds(220,230,200,30);
			addStockPanel.add(price);
			price.addKeyListener(new KeyListener() {

				@Override
				public void keyPressed(KeyEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void keyReleased(KeyEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void keyTyped(KeyEvent e) {
					char c=e.getKeyChar();
					if(!(Character.isDigit(c)||c==KeyEvent.VK_BACK_SPACE||c==KeyEvent.VK_DELETE))
					{
						getToolkit().beep();
						e.consume();
					}
					if(e.getKeyCode()==KeyEvent.VK_ENTER)
					{
						insert();
					}
				}
				
			});
			
			add=new JButton("Insert",icon);
			//		add.setBounds(270,300,100,30);
					add.setBounds(270,300,100,30);
			addStockPanel.add(add);
			add.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					insert();
				}
				
			});

			clear=new JButton("clear",icon2);
			clear.setBounds(270,350,100,30);
			clear.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					clear();
				}});
			addStockPanel.add(clear);
			
			add(addStockPanel);
			
			setBackground(new Color(200,221,242));
	  }
	  	public void insert()
	  	{
	  		if(name.getText().equals(""))
			{
				addStockPanel.setVisible(false);
				error.setText("!! Enter Name !!");
				addStockPanel.setVisible(true);
				
			}
			else
			{
				
				if(!(category.getSelectedIndex()>=0))
				{
					addStockPanel.setVisible(false);
					error.setText("!! Select Category Correctly !!");
					addStockPanel.setVisible(true);
					}
				else
				{
					if(quantity.getText().equals(""))
					{
						addStockPanel.setVisible(false);
						error.setText("!! Enter Quantity !!");
						addStockPanel.setVisible(true);
					}
					else
					{
						if(price.getText().equals(""))
						{
							addStockPanel.setVisible(false);
							error.setText("!! Enter Price !!");
							addStockPanel.setVisible(true);
							
						}
						else
						{
							System.out.println("checking");
							if(name.getDataList().contains(name.getText()))
							{
								int previou=0;
								Connection con=Connections.getConnection();
								try {

									PreparedStatement st=con.prepareStatement("select * from md where ne=?");
									st.setString(1, Encryption.encrypt(name.getText()));
									ResultSet rs=st.executeQuery();
									System.out.println("sent");
									
									while(rs.next())
									{
										previou=Integer.parseInt(Encryption.decrypt(rs.getString("qt")));
										break;
									}
									System.out.println("previous price"+previou);
									st=con.prepareStatement("update md set qt=? where ne=?");
									
									int ne=Integer.parseInt(quantity.getText())+previou;
									st.setString(1, Encryption.encrypt(String.valueOf(ne)));
									st.setString(2, Encryption.encrypt(name.getText()));
									int number=st.executeUpdate();
									System.out.println(number);
									if(number>0)
									{
										JOptionPane.showMessageDialog(null, "New Stock Added Successfully");
										category.setEnabled(false);
										price.setEnabled(false);
										clear();
										
									}
									else
										System.out.println("failde");
									
									
									
								}catch(SQLException e) {
									System.out.println(e.getMessage());
								}
							}
							else
							{
								Connection con=Connections.getConnection();
								try {
									PreparedStatement st=con.prepareStatement("insert into md(ne,cat,qt,pr)values(?,?,?,?)");
									st.setString(1, Encryption.encrypt(name.getText()));
									st.setString(2, Encryption.encrypt(category.getSelectedItem().toString()));
									st.setString(3, Encryption.encrypt(String.valueOf(Integer.parseInt(quantity.getText()))));
									st.setString(4, Encryption.encrypt(String.valueOf(Integer.parseInt(price.getText()))));
									if(st.executeUpdate()==1)
									{
										JOptionPane.showMessageDialog(null, "New Medicin Added Successfully");
										Main.my.salePanel.newSale.refreshName();
										Thread refresh=new Thread(new Runnable() {

											@Override
											public void run() {
												
												name.getDataList().add(name.getText());
											
											}
											
										}) ;
		//								refresh.start();
										setVisible(false);
										setVisible(true);
										clear();
										refresh.start();
									}
									
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							
						}
					}
				}
					
			}
	  	}
	  	public void clear()
	  	{
	  		name.setText("");
	  		category.setSelectedIndex(0);
	  		quantity.setText("");
	  		price.setText("");
	  		category.setEnabled(true);
			price.setEnabled(true);
		
	  		name.requestFocus();
	  		
	  	}
		public void refreshSearch(ArrayList<Medicin> list)
		{
			
			for(int i=0;i<list.size();i++)
			{
				name.getDataList().add(list.get(i).getName());
			}
			
		}
		
		ArrayList<Medicin> getArrayList()
	     {
	         ArrayList<Medicin> list=new ArrayList<Medicin>();
	         PreparedStatement st=null;
	         Connection con=Connections.getConnection();
	        try {
	         	st=con.prepareStatement("select * from md");
	            ResultSet rs=null;
	            rs=st.executeQuery();
	            while(rs.next())
	            {
	                Medicin m=new Medicin(Encryption.decrypt(rs.getString("ne")),Encryption.decrypt(rs.getString("cat")),Encryption.decrypt(rs.getString("qt")),Encryption.decrypt(rs.getString("pr")));
	                list.add(m);
	            }
	            
	        } catch(Exception e)
	        {
	        	System.out.println(e.getMessage());
	        }
	         
	         return list;
	     
	     }
  
}