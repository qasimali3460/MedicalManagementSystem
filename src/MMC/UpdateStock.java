package MMC;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.print.PrinterException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTable.PrintMode;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

public class UpdateStock extends JPanel {
	ImageIcon bin=new ImageIcon(getClass().getClassLoader().getResource("dust.png"));
	ImageIcon icon2=new ImageIcon(getClass().getClassLoader().getResource("update.png"));
	JPanel update =new JPanel();
	JPanel operation=new JPanel();
	JPanel tablePanel=new JPanel();
	JScrollPane scrollPane=new JScrollPane();
	JTable table=new JTable();
	TitledBorder inner=BorderFactory.createTitledBorder("Total Stock");  
	javax.swing.border.Border outer=BorderFactory.createEmptyBorder(5,5,5,5);
	GridBagConstraints gc=new GridBagConstraints();
	GTextField search = new GTextField(0  , 0 , true );
	public JButton updateButton=new JButton("Update",icon2);
	public JButton dltButton=new JButton("Delete",bin);
	String on,oq,op,oc;
	int row;
	public JButton clear=new JButton("Clear");
	int searchIndex=3;		
    Dimension d;
    ArrayList<String> added= new ArrayList<String>();
    Thread refresh;
	JTextField name,price,quantity;
	JComboBox category;
	JLabel nl,catl,qul,pl;
	 String catp[]= {"Tablet","Syrup","Drops","other"};
		
	
    
	public UpdateStock() {
		setBackground(new Color(200,221,242));
		this.setName("Update Stock");
		setLayout(new BorderLayout());
		update.setPreferredSize(new Dimension(1150,400));
		update.setBackground(new Color(100,100,100,1));
		update.setBorder(BorderFactory.createCompoundBorder(outer,inner));
		tablePanel.setLayout(new GridBagLayout());
		tablePanel.add(update);
		update.setLayout(new BorderLayout());
	//	operation.setBackground(Color.RED);
		operation.setBorder(BorderFactory.createEtchedBorder());
		operation.setBackground(new Color(100,100,100,1));
		d=operation.getPreferredSize();
		d.height=70;
		operation.setPreferredSize(d);
		operation.setLayout(null);
		JLabel searchLabel=new JLabel("Search : ");
		searchLabel.setBounds(720,10,50,50);
	//	operation.add(searchLabel);
		search.setBounds(780,20,180,30);
	//	operation.add(search);
		search.getDataList().add("Tablet");
		search.getDataList().add("Syrup");
		search.getDataList().add("Drops");
		
		clear.setBounds(1000, 20, 70, 30);
	//	operation.add(clear);
		
		updateButton.setBounds(800, 5, 130, 50);
		operation.add(updateButton);
		updateButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(!(name.getText().equals(on)&&category.getSelectedItem().toString().equals(oc)&&quantity.getText().equals(oq)&&price.getText().equals(op)))
				{
					int confirm=JOptionPane.showConfirmDialog(null,"Confirm want To Update");
					if(confirm==0)
					{
						Connection con=Connections.getConnection();
						try {
							PreparedStatement st=con.prepareStatement("select * from md where ne=?");
							st.setString(1, Encryption.encrypt(on));
							ResultSet rs=st.executeQuery();
							String categoryD;
							if(rs.next())
							{
								System.out.println("name found");
								categoryD=Encryption.decrypt(rs.getString("cat"));
								System.out.println("found Category is ");
								if(category.equals(category.getSelectedItem().toString()))
								{
									JOptionPane.showMessageDialog(null, "Medicine already Exists");
								}
								else
								{
									System.out.println("updating");
									con=Connections.getConnection();
									try {
										st=con.prepareStatement("update md set ne=?,cat=?,qt=?,pr=? where ne=?");
										st.setString(1, Encryption.encrypt(name.getText()));
										st.setString(2, Encryption.encrypt(category.getSelectedItem().toString()));
										st.setString(3, Encryption.encrypt(quantity.getText()));
										st.setString(4, Encryption.encrypt(price.getText()));
										st.setString(5, Encryption.encrypt(on));
										if(st.executeUpdate()>-1)
										{
											refreshTable();
											StockPanel.addStock.name.getDataList().remove(on);
											StockPanel.addStock.name.getDataList().add(name.getText());
											Main.my.salePanel.newSale.refreshName();
											clear();
										}
										
									} catch (SQLException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
							else
							{
								
							}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
					}
				}
					
			}
			
		});
		
				
		
		add(operation,BorderLayout.SOUTH);
		search.setWidthPopupPanel(180);
		search.setHeightPopupPanel(100);
		search.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				if(arg0.getKeyCode()==KeyEvent.VK_ENTER)
				{
					if(search.getText().equals("")) {
						refreshTable();
					}
					else
					{
						System.out.println("Search is not emptyl");
						
					}
				}
			}

			@Override
			public void keyTyped(KeyEvent event) {
				
			}
			
		});
		tablePanel.setBackground(new Color(100,100,100,1));
		add(tablePanel,BorderLayout.CENTER);
		
		Object[][] data={
				{"1","paracetamol","Syrup","399","6"},
				{"2","paracetamol","Syrup","399","6"},
				{"3","Fedral","Syrup","399","6"},
				{"4","paracetamol","Syrup","399","6"},
				{"5","paracetamol","Syrup","399","6"},
				{"6","samsoul","Syrup","399","6"},
				{"7","Septran","Syrup","399","6"},
				{"8","Brufan","Syrup","399","6"},
				{"9","paracetamol","Syrup","399","6"},
				{"10","paracetamol","Syrup","399","6"}
	        };
            String[] columnN={"sr.","Name","Category","Quantity","Price"};
            DefaultTableModel mo=new DefaultTableModel(data,columnN);
            table=new JTable(mo);
            scrollPane=new JScrollPane(table);
            scrollPane.setBackground(Color.WHITE);
            update.add(scrollPane,BorderLayout.CENTER);
            refreshTable();
            
            
            
            nl=new JLabel("Name :");
        	nl.setBounds(50, 5, 50, 50);
    		operation.add(nl);
    		
            name=new JTextField();
        	name.setBounds(95, 20, 120, 25);
    		operation.add(name);
    	   
            catl=new JLabel("Category :");
            catl.setBounds(230, 5, 80, 50);
    		operation.add(catl);
    		
            category=new JComboBox(catp);
        	category.setBounds(310, 20, 120, 25);
        	operation.add(category);
    	    
            qul=new JLabel("Quantity :");
            qul.setBounds(435, 5, 80, 50);
    		operation.add(qul);
    		
            quantity=new JTextField();
            quantity.setBounds(500, 20, 120, 25);
        	operation.add(quantity);
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
    	    
            pl=new JLabel("Price :");
            pl.setBounds(640, 5, 80, 50);
    		operation.add(pl);
    		
            price=new JTextField();
            price.setBounds(695, 20, 80, 25);
    		operation.add(price);
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
				}
				
			});
    		
    		dltButton.setBounds(980,  5, 130, 50);
    		operation.add(dltButton);
    		dltButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					if(table.getSelectedRow()>=0)
					{
						System.out.println("delete is pressed");
						if(row>=0)
						{
							System.out.println(row);
							Connection con=Connections.getConnection();
							try {
								PreparedStatement st=con.prepareStatement("Delete * from md where ne=?");
								System.out.println(on);
								st.setString(1, Encryption.encrypt(on));
								getToolkit().beep();
								int confirm=JOptionPane.showConfirmDialog(null, "Confirm Delete");
								System.out.println(confirm);
								if(confirm==0) {
									int resutl=st.executeUpdate();
									System.out.println(resutl);
									if(resutl>=0)
									{
										st.close();
										con.close();
										refreshTable();
										StockPanel.addStock.name.getDataList().remove(on);
										Main.my.salePanel.newSale.refreshName();
										
										
									}
									
								}
								
								
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}
					}
					else {
						name.setText("");
						quantity.setText("");
						price.setText("");
						category.setSelectedIndex(0);
					}
				}	
    			
    		});
            
         
            clear.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					refreshTable();
					search.setText("");
					refresh();
				}
            	
            });

            
           }

  	public void clear()
  	{
  		name.setText("");
  		category.setSelectedIndex(0);
  		quantity.setText("");
  		price.setText("");
  	
  	}
		public void refresh()
		{
			operation.setVisible(false);
			operation.setVisible(true);
			System.out.println("Removed");
		}
		public void refreshSearch(ArrayList<Medicin> list)
		{
			
			if(searchIndex>3)
			{
				search.getDataList().removeAll(added);
				System.out.println(list);
//				System.out.println("hello");
//					for(int i=3;i<list.size();i++)
//				{
//					System.out.println(search.getDataList().get(i));
//					search.getDataList().remove(i);
//				}
				added=new ArrayList<String>();
				operation.setVisible(false);
				operation.setVisible(true);
				this.setVisible(false);
				this.setVisible(true);
			}
			searchIndex=3;
			for(int i=0;i<list.size();i++)
			{
				search.getDataList().add(list.get(i).getName());
				added.add(list.get(i).getName());
				searchIndex++;
			
			}
				
		
			
			
			
			
			
			
		}
		public void refreshTable()
		{

			
			
			update.remove(scrollPane);
			
		        Object[][] data={
		        };
		        String[] columnN={"sr.","Name","Category","Quantity","Price"};
	            DefaultTableModel mo=new DefaultTableModel(data,columnN);
	             table=new JTable(mo);
		         table.setPreferredScrollableViewportSize(new Dimension(500,50));
		         table.setFillsViewportHeight(true);
		         scrollPane=new JScrollPane(table);
		         update.add(scrollPane,BorderLayout.CENTER);
		         table.addMouseListener(new MouseListener() {

						@Override
						public void mouseClicked(MouseEvent arg0) {
							
							
						}

						@Override
						public void mouseEntered(MouseEvent arg0) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void mouseExited(MouseEvent arg0) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void mousePressed(MouseEvent arg0) {
							// TODO Auto-generated method stub
							row=table.getSelectedRow();
							TableModel model=table.getModel();
							on=model.getValueAt(row, 1).toString();
							oc=model.getValueAt(row, 2).toString();
							oq=model.getValueAt(row, 3).toString();
							op=model.getValueAt(row, 4).toString();
							name.setText(on);
							category.setSelectedItem(oc);
							quantity.setText(oq);
							price.setText(op);
							System.out.println(on);
						}

						@Override
						public void mouseReleased(MouseEvent arg0) {
							// TODO Auto-generated method stub
							
						}});
		         JTableHeader theader=table.getTableHeader();
		         theader.setBackground(new Color(205,160,214));
		         theader.setForeground(Color.white);
		         theader.setFont(new Font("Tahoma",Font.BOLD,15));
		      //   DefaultTableCellRenderer center=new DefaultTableCellRenderer();

		         table.getColumnModel().getColumn(0).setPreferredWidth(2);
		         table.getColumnModel().getColumn(2).setPreferredWidth(2);
		         table.getColumnModel().getColumn(3).setPreferredWidth(2);
		         table.getColumnModel().getColumn(4).setPreferredWidth(2);
		         
		         
		         DefaultTableCellRenderer center = new DefaultTableCellRenderer();
				 center.setHorizontalAlignment(JLabel.CENTER);
				  for(int i=0;i<5;i++)
		         {
		        	 table.getColumnModel().getColumn(i).setCellRenderer(center);
		         }
		         table.setDefaultRenderer(Object.class,center);
		         ArrayList<Medicin> list=getArrayList();
		         DefaultTableModel model=(DefaultTableModel) table.getModel();
		         Object row[]=new Object[5];
		         
		          for(int i=0;i<list.size();i++)
		            {
		                row[0]=1+i;
		                row[1]=list.get(i).getName();
		                row[2]=list.get(i).getCategory();
		                row[3]=list.get(i).getQuantity();
		                row[4]=list.get(i).getPrice();
		                model.addRow(row);
		                
		            }
		          
		         
		          update.setVisible(false);
		          update.setVisible(true);
		          tablePanel.setVisible(false);
		          tablePanel.setVisible(true);
		          refreshSearch(list);
		}
		ArrayList<Medicin> getArrayList()
	     {
	         ArrayList<Medicin> list=new ArrayList<Medicin>();
	         PreparedStatement st=null;
	         Connection con=Connections.getConnection();
	        try {
	         	st=con.prepareStatement("select * from md order by ne");
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
