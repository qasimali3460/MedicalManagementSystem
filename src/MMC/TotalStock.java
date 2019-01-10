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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.print.PrinterException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTable.PrintMode;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class TotalStock
  extends JPanel
{
  ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("printer.png"));
  ImageIcon icon2 = new ImageIcon(getClass().getClassLoader().getResource("update.png"));
  JPanel update = new JPanel();
  JPanel operation = new JPanel();
  JPanel tablePanel = new JPanel();
  JScrollPane scrollPane = new JScrollPane();
  JTable table = new JTable();
  TitledBorder inner = BorderFactory.createTitledBorder("Total Stock");
  Border outer = BorderFactory.createEmptyBorder(5, 5, 5, 5);
  GridBagConstraints gc = new GridBagConstraints();
//  GTextField search = new GTextField(0, 0, true);
  //GTextField search1=new GTextField(0,0,true);
//  GTextField cSearch=new GTextField(0,0,true);
  GTextField working=new GTextField(0,0,true);
  public JButton print = new JButton("Print", this.icon);
  public JButton clear = new JButton("Clear");
  public JButton searchBtn=new JButton("Search");
  int searchIndex = 3;
  Dimension d;
  ArrayList<String> added = new ArrayList();
  Thread refresh;
  
  public TotalStock()
  {
    setBackground(new Color(200, 221, 242));
    setName("Total Stock");
    setLayout(new BorderLayout());
    this.update.setPreferredSize(new Dimension(1150, 400));
    this.update.setBackground(new Color(100, 100, 100, 1));
    this.update.setBorder(BorderFactory.createCompoundBorder(this.outer, this.inner));
    this.tablePanel.setLayout(new GridBagLayout());
    this.tablePanel.add(this.update);
    this.update.setLayout(new BorderLayout());
    
    this.operation.setBorder(BorderFactory.createEtchedBorder());
    this.operation.setBackground(new Color(100, 100, 100, 1));
    this.d = this.operation.getPreferredSize();
    this.d.height = 70;
    this.operation.setPreferredSize(this.d);
    this.operation.setLayout(null);
    JLabel searchLabel = new JLabel("Search : ");
    searchLabel.setBounds(720, 10, 50, 50);
    
    working.getDataList().add("sad");
    working.setBounds(180,0,180,30);
    working.setHeightPopupPanel(5);
    working.setWidthPopupPanel(90);
    operation.add(working);
    working.addKeyListener(new KeyListener() {

		@Override
		public void keyPressed(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			if(arg0.getKeyCode()==KeyEvent.VK_ENTER)
			{
				searchTable();
			}
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}});
    
    
    
  //  cSearch.setBounds(180, 20, 180, 30);
    searchBtn.setBounds(380, 0, 90, 30);
    searchBtn.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			searchTable();
		}
    	
    });
    
   
    //operation.add(cSearch);
    operation.add(searchBtn);
    
    
    this.clear.setBounds(850, 5, 130, 50);
    operation.add(clear);
    this.print.setBounds(1000, 5, 130, 50);
    this.operation.add(this.print);
    
    add(this.operation, BorderLayout.SOUTH);

    this.tablePanel.setBackground(new Color(100, 100, 100, 1));
    add(this.tablePanel, "Center");
    
    Object[][] data = {};
    
    String[] columnN = { "sr.", "Name", "Category", "Quantity", "Price" };
    DefaultTableModel mo = new DefaultTableModel(data, columnN);
    this.table = new JTable(mo);
    this.scrollPane = new JScrollPane(this.table);
    this.scrollPane.setBackground(Color.WHITE);
    this.update.add(this.scrollPane, "Center");
    refreshTable();
    
    this.print.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent arg0)
      {
        printNow();
      }
    });
    this.clear.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent arg0)
      {
    	  table.getSelectionModel().clearSelection();
    	  working.setText("");
      }
    });
    
    
  }
  
  public void refresh()
  {
    this.operation.setVisible(false);
    this.operation.setVisible(true);
//    System.out.println("Removed");
  }
  
  public void refreshSearch(ArrayList<Medicin> list)
  {
	working.removeAll();  
	for(int i=0;i<list.size();i++)
	{
		working.getDataList().add(list.get(i).getName());
	}
  }
  public void searchTable()
  {
	  if(working.getText().equals(""))
		{
			table.getSelectionModel().clearSelection();
		}
		else
		{
			if(working.getDataList().contains(working.getText()))
			{
				int index=working.getDataList().indexOf(working.getText());
				table.setRowSelectionInterval(index, index);
				table.scrollRectToVisible(new Rectangle(table.getCellRect(index, 0, true)));
			}
		}
  }
  public void printNow()
  {

      MessageFormat header = new MessageFormat("Stock Report");
      Printing.print(table,header);
  }
  
  public void refreshTable()
  {
    this.update.remove(this.scrollPane);
    Object[][] data = new Object[0][];
    
    String[] columnN = { "sr.", "Name", "Category", "Quantity", "Price" };
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
    for (int i = 0; i < 5; i++) {
      this.table.getColumnModel().getColumn(i).setCellRenderer(center);
    }
    this.table.setDefaultRenderer(Object.class, center);
    ArrayList<Medicin> list = getArrayList();
    DefaultTableModel model = (DefaultTableModel)this.table.getModel();
    Object[] row = new Object[5];
    for (int i = 0; i < list.size(); i++)
    {
      row[0] = Integer.valueOf(1 + i);
      row[1] = ((Medicin)list.get(i)).getName();
      row[2] = ((Medicin)list.get(i)).getCategory();
      row[3] = ((Medicin)list.get(i)).getQuantity();
      row[4] = ((Medicin)list.get(i)).getPrice();
      model.addRow(row);
    }
    this.update.setVisible(false);
    this.update.setVisible(true);
    this.tablePanel.setVisible(false);
    this.tablePanel.setVisible(true);
    refreshSearch(list);
  }
  
  ArrayList<Medicin> getArrayList()
  {
    ArrayList<Medicin> list = new ArrayList();
    PreparedStatement st = null;
    Connection con = Connections.getConnection();
    try
    {
      st = con.prepareStatement("select * from md order by ne");
      ResultSet rs = null;
      rs = st.executeQuery();
      while (rs.next())
      {
        Medicin m = new Medicin(Encryption.decrypt(rs.getString("ne")), Encryption.decrypt(rs.getString("cat")), Encryption.decrypt(rs.getString("qt")), Encryption.decrypt(rs.getString("pr")));
        list.add(m);
      }
    }
    catch (Exception e)
    {
//      System.out.println(e.getMessage());
    }
    return list;
  }
}
