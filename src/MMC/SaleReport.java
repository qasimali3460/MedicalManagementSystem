package MMC;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.print.PrinterException;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTable.PrintMode;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.jdesktop.swingx.JXTipOfTheDay.ShowOnStartupChoice;

public class SaleReport
  extends JPanel
{
  ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("printer.png"));
  ImageIcon icon2 = new ImageIcon(getClass().getClassLoader().getResource("update.png"));
  JPanel update = new JPanel();
  JPanel operation = new JPanel();
  JPanel tablePanel = new JPanel();
  JScrollPane scrollPane = new JScrollPane();
  static JTable table = new JTable();
  TitledBorder inner = BorderFactory.createTitledBorder("Total Stock");
  Border outer = BorderFactory.createEmptyBorder(5, 5, 5, 5);
  GridBagConstraints gc = new GridBagConstraints();
  GTextField search = new GTextField(0, 0, true);
  public JButton print = new JButton("Print", this.icon);
  public JButton clear = new JButton("Clear");
  int searchIndex = 3;
  Dimension d;
  private List<String> st = new ArrayList<String>();	
  ArrayList<String> added = new ArrayList();
  static String showBy[]= {"Today","Older"};
  static JComboBox show=new JComboBox(showBy);
  JButton searchBtn=new JButton("Search");
  JButton cancelOrderBtn=new JButton("Cancel Order");
  JSpinner returnQ=new JSpinner(new SpinnerNumberModel(0,0,0,0));
  JButton returnBtn=new JButton("Return");
  JRadioButton rb1=new JRadioButton("Cancel Order"),rb2=new JRadioButton("Return");
  ButtonGroup group=new ButtonGroup();
  Thread refresh;
  int tableSize=0;
  String selectedName="";
  int selectedNumber=0;
  int selectedQuantity=0;
  
  public SaleReport()
  {
	 
    setBackground(new Color(200, 221, 242));
    setName("Sale Report");
    setLayout(new BorderLayout());
    this.update.setPreferredSize(new Dimension(1150, 400));
    this.update.setBackground(new Color(100, 100, 100, 1));
    this.update.setBorder(BorderFactory.createCompoundBorder(this.outer, this.inner));
    this.tablePanel.setLayout(new GridBagLayout());
    this.tablePanel.add(this.update);
    this.update.setLayout(new BorderLayout());
    
    this.operation.setBackground(new Color(100, 100, 100, 1));
    this.d = this.operation.getPreferredSize();
    this.d.height = 70;
    this.operation.setPreferredSize(this.d);
    this.operation.setLayout(null);
    
    
    show.setBounds(50,0,150,50);
    show.addItemListener(new ItemListener() {

		@Override
		public void itemStateChanged(ItemEvent arg0) {
			if(show.getSelectedIndex()==0)
			{
				System.out.println("Refreshing");
				
				try {
					refreshTodayTable();
					table.scrollRectToVisible(new Rectangle(table.getCellRect(tableSize, 0, true)));
					
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Refreshed");
			}
			if(show.getSelectedIndex()==1)
			{
				refreshTable();
				
				table.scrollRectToVisible(new Rectangle(table.getCellRect(tableSize, 0, true)));
			
			}
			
			
		}
    	
    });
    operation.add(show);
    
    
    
    
    JLabel searchLabel = new JLabel("Search : ");
    searchLabel.setBounds(720, 10, 50, 30);
    
    this.search.setBounds(255, 10, 180, 30);
    operation.add(search);
    
    this.search.getDataList().add("Tablet");
    this.search.getDataList().add("Syrup");
    this.search.getDataList().add("Drops");
    
    searchBtn.setBounds(440,10,100,30);
    searchBtn.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (SaleReport.this.search.getText().equals("")) {
	        	  show.setSelectedIndex(0);
	          } else {
	            searching();
	          }
		}});
    operation.add(searchBtn);

 
    
    returnQ.setBounds(530, 10, 70,30);
   // operation.add(returnQ);
    
    
    returnBtn.setBounds(610, 10, 80, 30);
    operation.add(returnBtn);
    
    returnBtn.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(table.getSelectedRow()>-1)
			{
				Connection con=Connections.getConnection();
				try {
					PreparedStatement st=con.prepareStatement("delete * from se where sn=? and ne =?");
					st.setString(1,Encryption.encrypt( String.valueOf(selectedNumber)));
					st.setString(2, Encryption.encrypt(String.valueOf(selectedName)));
					if(st.executeUpdate()>0)
					{
						System.out.println("Removed Selected row form Sale Table");
						DefaultTableModel model=(DefaultTableModel) table.getModel();
						model.removeRow(table.getSelectedRow());
						st.close();
						con.close();
						con=Connections.getConnection();
						st=con.prepareStatement("select * from md where ne =?");
						st.setString(1, Encryption.encrypt(String.valueOf(selectedName)));
						ResultSet rs=st.executeQuery();
						int old=0;
						while(rs.next())
						{
							old=Integer.parseInt(Encryption.decrypt(rs.getString("qt")));
							
						}
						System.out.println("Old Stock is" + old);
						old=old+selectedQuantity;
						st.close();
						con.createStatement();
						con=Connections.getConnection();
						st=con.prepareStatement("update md set qt =? where ne=?");
						st.setString(1,Encryption.encrypt( String.valueOf(old)));
						System.out.println(selectedName);
						st.setString(2, Encryption.encrypt(String.valueOf(selectedName)));
						if(st.executeUpdate()>0)
						{
							System.out.println("Returned stock is Updated");
							StockPanel.totalStock.refreshTable();
						}
						
					}
					
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Select a medicine to return");
			}
			
		}
    	
    });
    
    
    this.cancelOrderBtn.setBounds(700, 10, 120, 30);
    operation.add(cancelOrderBtn);
    cancelOrderBtn.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(table.getSelectedRow()>-1)
			{
				int rz=JOptionPane.showConfirmDialog(null, "Confirm Cancel Order");
				if(rz==0)
				{
		
					Connection con=Connections.getConnection();
					try {
						PreparedStatement st =con.prepareStatement("delete * from se where sn=? ");
						st.setString(1, Encryption.encrypt(String.valueOf(selectedNumber)));
						if(st.executeUpdate()>0)
						{
							System.out.println("Full Order is Cancelesd");
							show.setSelectedIndex(0);
							try {
								refreshTodayTable();
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			
		}
    	
    });
    
    
    
    
    this.clear.setBounds(930, 5, 80, 50);
    operation.add(clear);
    
    this.print.setBounds(1020, 5, 130, 50);
    this.operation.add(this.print);
    
    add(this.operation, "South");
    this.search.setWidthPopupPanel(80);
    this.search.setHeightPopupPanel(60);
    this.search.addKeyListener(new KeyListener()
    {
      public void keyPressed(KeyEvent arg0) {}
      
      public void keyReleased(KeyEvent arg0)
      {
        if (arg0.getKeyCode() == 10) {
          if (SaleReport.this.search.getText().equals("")) {
        	  show.setSelectedIndex(0);
          } else {
            searching();
          }
        }
      }
      
      public void keyTyped(KeyEvent event) {}
    });
    refreshSearch();
    this.tablePanel.setBackground(new Color(100, 100, 100, 1));
    add(this.tablePanel, "Center");
    
    Object[][] data = {
      { "1", "paracetamol", "Syrup", "399", "6" }, 
      { "2", "paracetamol", "Syrup", "399", "6" }, 
      { "3", "Fedral", "Syrup", "399", "6" }, 
      { "4", "paracetamol", "Syrup", "399", "6" }, 
      { "5", "paracetamol", "Syrup", "399", "6" }, 
      { "6", "samsoul", "Syrup", "399", "6" }, 
      { "7", "Septran", "Syrup", "399", "6" }, 
      { "8", "Brufan", "Syrup", "399", "6" }, 
      { "9", "paracetamol", "Syrup", "399", "6" }, 
      { "10", "paracetamol", "Syrup", "399", "6" } };
    
    String[] columnN = { "sr.", "Name", "Quantity", "Price", "Date", "Time" };
    DefaultTableModel mo = new DefaultTableModel(data, columnN);
    this.table = new JTable(mo);
    this.scrollPane = new JScrollPane(this.table);
    this.scrollPane.setBackground(Color.WHITE);
    this.update.add(this.scrollPane, "Center");
    show.setSelectedIndex(0);
    
    this.print.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent arg0)
      {
        print();
        }
      
    });
    this.clear.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent arg0)
      {
        show.setSelectedIndex(0);
        SaleReport.this.search.setText("");
        SaleReport.this.refresh();
        try {
			refreshTodayTable();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      }
    });
  }
  public static void print()
  {
	  if(show.getSelectedIndex()==0)
	  {
		  Printing.print(table,new MessageFormat("Today Sale Report"));
		  
	  }
	  else
		  Printing.print(table,new MessageFormat("All Sales Report"));
	  
  }
  public void refresh()
  {
    this.operation.setVisible(false);
    this.operation.setVisible(true);
    System.out.println("Removed");
  }
  
  public void refreshSearch()
  {
    ArrayList<Sale> list=getArrayList();
    search.removeAll();
    for (int i = 0; i < list.size(); i++)
    {
    	if(!(search.getDataList().contains(list.get(i).getSerial())))
    	{

   	     search.getDataList().add(list.get(i).getSerial());
    	}
    }
  }
  public void searching()
  {
	  if(search.getDataList().contains(search.getText()))
	  {
		  show.setSelectedIndex(1);
		  int index=st.indexOf(search.getText());
		  System.out.println(st.toString());
		  
		  System.out.println(index);
		  table.scrollRectToVisible(new Rectangle(table.getCellRect(index+1, 0, true)));
		  table.setRowSelectionInterval(index, index);
				
	  }
  }
  

  public void refreshTable()
  {
    this.update.remove(this.scrollPane);
    Object[][] data = new Object[0][];
    
    String[] columnN = { "sr.","Serial#", "Name", "Quantity", "Price", "Date", "Time" };
    DefaultTableModel mo = new DefaultTableModel(data, columnN);
    this.table = new JTable(mo);
    this.table.setPreferredScrollableViewportSize(new Dimension(500, 50));
    this.table.setFillsViewportHeight(true);
    this.scrollPane = new JScrollPane(this.table);
    this.update.add(this.scrollPane, "Center");
    
    JTableHeader theader = this.table.getTableHeader();
    theader.setBackground(new Color(205, 160, 214));
    theader.setForeground(Color.white);
    theader.setFont(new Font("Tahoma", 1, 15));
    System.out.println(table.getFont());
    System.out.println(table.getRowHeight());
    
    this.table.getColumnModel().getColumn(0).setPreferredWidth(2);
    this.table.getColumnModel().getColumn(1).setPreferredWidth(2);
    this.table.getColumnModel().getColumn(3).setPreferredWidth(2);
    this.table.getColumnModel().getColumn(4).setPreferredWidth(2);
    
    
    table.addMouseListener(new MouseListener() {

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
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
				DefaultTableModel model=(DefaultTableModel) table.getModel();
				selectedName=model.getValueAt(table.getSelectedRow(), 2).toString();
				System.out.println(selectedName);
				String temp=model.getValueAt(table.getSelectedRow(), 1).toString();
				StringBuilder sb = new StringBuilder(temp);
				sb.deleteCharAt(0);
				selectedNumber=Integer.parseInt(sb.toString());
				temp=model.getValueAt(table.getSelectedRow(), 3).toString();
				selectedQuantity=Integer.parseInt(temp);
				
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
    	
    });
    
    
    DefaultTableCellRenderer center = new DefaultTableCellRenderer();
    center.setHorizontalAlignment(0);
    for (int i = 0; i < 6; i++) {
      this.table.getColumnModel().getColumn(i).setCellRenderer(center);
    }
    this.table.setDefaultRenderer(Object.class, center);
    ArrayList<Sale> list = getArrayList();
    DefaultTableModel model = (DefaultTableModel)this.table.getModel();
    Object[] row = new Object[7];
    tableSize=list.size()-1;
    st=new ArrayList<String>();
    for (int i = 0; i < list.size(); i++)
    {
      row[0] = Integer.valueOf(1 + i);
      row[1] = "#"+((Sale)list.get(i)).getSerial();
      st.add(list.get(i).getSerial());
      row[2] = ((Sale)list.get(i)).getName();
      row[3] = ((Sale)list.get(i)).getQuantity();
      row[4] = ((Sale)list.get(i)).getPrice();
      row[5] = ((Sale)list.get(i)).getDate();
      row[6] = ((Sale)list.get(i)).getTime();
      
      
      model.addRow(row);
    }
    this.update.setVisible(false);
    this.update.setVisible(true);
    this.tablePanel.setVisible(false);
    this.tablePanel.setVisible(true);
  }
  public void refreshWeekTable()
  {
    this.update.remove(this.scrollPane);
    Object[][] data = new Object[0][];
    
    String[] columnN = { "sr.","Serial#", "Name", "Quantity", "Price", "Date", "Time" };
    DefaultTableModel mo = new DefaultTableModel(data, columnN);
    this.table = new JTable(mo);
    this.table.setPreferredScrollableViewportSize(new Dimension(500, 50));
    this.table.setFillsViewportHeight(true);
    this.scrollPane = new JScrollPane(this.table);
    this.update.add(this.scrollPane, "Center");
    
    JTableHeader theader = this.table.getTableHeader();
    theader.setBackground(new Color(205, 160, 214));
    theader.setForeground(Color.white);
    theader.setFont(new Font("Tahoma", 1, 15));
    
    this.table.getColumnModel().getColumn(0).setPreferredWidth(2);
    this.table.getColumnModel().getColumn(2).setPreferredWidth(2);
    this.table.getColumnModel().getColumn(3).setPreferredWidth(2);
    this.table.getColumnModel().getColumn(4).setPreferredWidth(2);
    
    DefaultTableCellRenderer center = new DefaultTableCellRenderer();
    center.setHorizontalAlignment(0);
    for (int i = 0; i < 6; i++) {
      this.table.getColumnModel().getColumn(i).setCellRenderer(center);
    }
    this.table.setDefaultRenderer(Object.class, center);
    ArrayList<Sale> list = getArrayList();
    DefaultTableModel model = (DefaultTableModel)this.table.getModel();
    Object[] row = new Object[7];
    for (int i = 0; i < list.size(); i++)
    {
      row[0] = Integer.valueOf(1 + i);
      row[1] = "#"+((Sale)list.get(i)).getSerial();
      row[2] = ((Sale)list.get(i)).getName();
      row[3] = ((Sale)list.get(i)).getQuantity();
      row[4] = ((Sale)list.get(i)).getPrice();
      row[5] = ((Sale)list.get(i)).getDate();
      row[6] = ((Sale)list.get(i)).getTime();
      
      
      model.addRow(row);
    }
    refreshSearch();
    this.update.setVisible(false);
    this.update.setVisible(true);
    this.tablePanel.setVisible(false);
    this.tablePanel.setVisible(true);
    
  }
  public void refreshTodayTable() throws ParseException
  {
    this.update.remove(this.scrollPane);
    Object[][] data = new Object[0][];
    
    String[] columnN = { "sr.","Serial#", "Name", "Quantity", "Price", "Date", "Time" };
    DefaultTableModel mo = new DefaultTableModel(data, columnN);
    this.table = new JTable(mo);
    this.table.setPreferredScrollableViewportSize(new Dimension(500, 50));
    this.table.setFillsViewportHeight(true);
    this.scrollPane = new JScrollPane(this.table);
    this.update.add(this.scrollPane, "Center");
    
    JTableHeader theader = this.table.getTableHeader();
    theader.setBackground(new Color(205, 160, 214));
    theader.setForeground(Color.white);
    theader.setFont(new Font("Tahoma", 1, 15));
    this.table.getColumnModel().getColumn(0).setPreferredWidth(2);
    this.table.getColumnModel().getColumn(1).setPreferredWidth(2);
    this.table.getColumnModel().getColumn(3).setPreferredWidth(2);
    this.table.getColumnModel().getColumn(4).setPreferredWidth(2);
    
    table.addMouseListener(new MouseListener() {

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
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
				DefaultTableModel model=(DefaultTableModel) table.getModel();
				selectedName=model.getValueAt(table.getSelectedRow(), 2).toString();
				System.out.println(selectedName);
				String temp=model.getValueAt(table.getSelectedRow(), 1).toString();
				StringBuilder sb = new StringBuilder(temp);
				sb.deleteCharAt(0);
				selectedNumber=Integer.parseInt(sb.toString());
				temp=model.getValueAt(table.getSelectedRow(), 3).toString();
				selectedQuantity=Integer.parseInt(temp);
				
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
    	
    });
    DefaultTableCellRenderer center = new DefaultTableCellRenderer();
    center.setHorizontalAlignment(0);
    for (int i = 0; i < 6; i++) {
      this.table.getColumnModel().getColumn(i).setCellRenderer(center);
    }
    this.table.setDefaultRenderer(Object.class, center);
    ArrayList<Sale> list = getArrayList();
    DefaultTableModel model = (DefaultTableModel)this.table.getModel();
    Object[] row = new Object[7];
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    Calendar Calender = new GregorianCalendar();
    int todayDate= Calender.get(5);
    int month = Calender.get(2);
    int year = Calender.get(1);
    Date date1 = null;
    String ct=sdf.format(Calender.getTime());
    date1=sdf.parse(ct);
    Date date2 ;
    int count=1;
    st=new ArrayList<String>();
    tableSize=list.size()-1;
    
    for (int i = 0; i < list.size(); i++)
    {
    	date2=(Date) sdf.parse(list.get(i).getDate());
    	if(date2.compareTo(date1)==0)
    	{
    	    row[0] = count;count++;
          row[1] = "#"+((Sale)list.get(i)).getSerial();
          row[2] = ((Sale)list.get(i)).getName();
          row[3] = ((Sale)list.get(i)).getQuantity();
          row[4] = ((Sale)list.get(i)).getPrice();
          row[5] = ((Sale)list.get(i)).getDate();
          row[6] = ((Sale)list.get(i)).getTime();
          
          
          model.addRow(row);
        
    	}
//      row[0] = Integer.valueOf(1 + i);
//      row[1] = "#"+((Sale)list.get(i)).getSerial();
//      row[2] = ((Sale)list.get(i)).getName();
//      row[3] = ((Sale)list.get(i)).getQuantity();
//      row[4] = ((Sale)list.get(i)).getPrice();
//      row[5] = ((Sale)list.get(i)).getDate();
//      row[6] = ((Sale)list.get(i)).getTime();
//      
//      
//      model.addRow(row);
    }
    this.update.setVisible(false);
    this.update.setVisible(true);
    this.tablePanel.setVisible(false);
    this.tablePanel.setVisible(true);
  }
  
  ArrayList<Sale> getArrayList()
  {
    ArrayList<Sale> list = new ArrayList();
    PreparedStatement st = null;
    Connection con = Connections.getConnection();
    try
    {
      st = con.prepareStatement("select * from se ");
      ResultSet rs = null;
      rs = st.executeQuery();
      while (rs.next())
      {
        Sale m = new Sale(Encryption.decrypt(rs.getString("ne")), Encryption.decrypt(rs.getString("qu")), Encryption.decrypt(rs.getString("pr")), Encryption.decrypt(rs.getString("date")), Encryption.decrypt(rs.getString("time")),Encryption.decrypt(rs.getString("sn")));
        list.add(m);
      }
    }
    catch (Exception e)
    {
      System.out.println(e.getMessage());
    }
    return list;
  }
}
