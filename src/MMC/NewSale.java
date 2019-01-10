package MMC;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;



public class NewSale
  extends JPanel
{
	public static String rbill,rdfee,rdiscount,rtotal;
  ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("addTo.png"));
  ImageIcon icon2 = new ImageIcon(getClass().getClassLoader().getResource("cancel.png"));
  ImageIcon icon3 = new ImageIcon(getClass().getClassLoader().getResource("remove.png"));
  ImageIcon icon4 = new ImageIcon(getClass().getClassLoader().getResource("saleElse.png"));
  JPanel login = new JPanel();
  TitledBorder inner = BorderFactory.createTitledBorder("LOGIN HERE");
  Border outer = BorderFactory.createEmptyBorder(5, 5, 5, 5);
  GTextField name = new GTextField(0, 0, true);
  JPanel addMedicine = new JPanel();
  JPanel bill = new JPanel();
  JSpinner quantity = new JSpinner();
  JTable table;
  JScrollPane scrollPane;
  JPanel update = new JPanel();
  String[] catp = { "sr.", "Medicine Name", "Quantity" };
  JButton add;
  JButton cancel;
  JButton remove;
  JButton sale;
  JLabel billLabel = new JLabel("Bill");
  JLabel total = new JLabel("0");
  JLabel billItem = new JLabel("0");
  JLabel billprice = new JLabel("0");
  JLabel billFee = new JLabel("0");
  JLabel billDiscount = new JLabel("");
  JLabel pStock = new JLabel("");
  JLabel error = new JLabel("");
  int stock;
  int price;
  JCheckBox cb=new JCheckBox("Reciept");
  JTextPane textPane = new JTextPane();
  
  NewSale()
  {
    setBackground(new Color(200, 221, 242));
    setName("New Sale");
    
    setLayout(new GridBagLayout());
    this.login.setPreferredSize(new Dimension(1100, 450));
    this.login.setBorder(BorderFactory.createCompoundBorder(this.outer, this.inner));
    this.login.setLayout(null);
    
    this.addMedicine.setBounds(0, 0, 650, 450);
    this.bill.setBounds(650, 0, 450, 450);
    this.addMedicine.setBackground(new Color(200, 221, 242));
    
    this.addMedicine.setLayout(null);
    this.addMedicine.setBorder(BorderFactory.createEtchedBorder());
    
    this.error.setBounds(250, 20, 150, 30);
    this.addMedicine.add(this.error);
    this.error.setForeground(Color.red);
    
    JLabel namel = new JLabel("Medicine :");
    namel.setBounds(130, 50, 60, 30);
    this.addMedicine.add(namel);
    this.name = new GTextField(0, 0, true);
    this.addMedicine.add(this.name);
    
    this.name.addKeyListener(new KeyListener()
    {
      public void keyPressed(KeyEvent arg0) {}
      
      public void keyReleased(KeyEvent arg0)
      {
        if (arg0.getKeyCode() == 10) {
          if (NewSale.this.name.getDataList().contains(NewSale.this.name.getText()))
          {
            NewSale.this.error.setText("");
            NewSale.this.quantity.setValue(Integer.valueOf(1));
            NewSale.this.quantity.requestFocus();
            Connection con = Connections.getConnection();
            try
            {
              PreparedStatement st = con.prepareStatement("select * from md where ne=?");
              st.setString(1, Encryption.encrypt(NewSale.this.name.getText()));
              ResultSet rs = st.executeQuery();
              while (rs.next())
              {
                NewSale.this.stock = Integer.parseInt(Encryption.decrypt(rs.getString("qt")));
                NewSale.this.price = Integer.parseInt(Encryption.decrypt(rs.getString("pr")));
                NewSale.this.pStock.setText("Stock(" + NewSale.this.stock + ")");
              }
            }
            catch (SQLException e)
            {
              e.printStackTrace();
            }
          }
          else
          {
    //        NewSale.this.error.setText("!! Medicine not found !!");
          }
        }
      }
      
      public void keyTyped(KeyEvent arg0) {}
    });
    this.name.addFocusListener(new FocusListener()
    {
      public void focusGained(FocusEvent arg0)
      {
        NewSale.this.quantity.setValue(Integer.valueOf(1));
      }
      
      public void focusLost(FocusEvent arg0) {}
    });
    this.name.setWidthPopupPanel(400);
    this.name.setHeightPopupPanel(100);
    this.name.setBounds(220, 50, 200, 30);
    refreshName();
    
    JLabel ql = new JLabel("Quantity :");
    ql.setBounds(130, 90, 60, 30);
    this.addMedicine.add(ql);
    this.quantity.setBounds(220, 90, 200, 30);
    this.addMedicine.add(this.quantity);
    this.quantity.setValue(Integer.valueOf(1));
    
    this.quantity.addKeyListener(new KeyListener()
    {
      public void keyPressed(KeyEvent arg0)
      {
        if (arg0.getKeyCode() == 10) {
          System.out.println("Enter is Released");
        }
      }
      
      public void keyReleased(KeyEvent arg0)
      {
        if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
          NewSale.this.addToTable();
        }
      }
      
      public void keyTyped(KeyEvent arg0)
      {
        if (arg0.getKeyCode() == 10) {
          System.out.println("Enter is Typed");
        }
      }
    });
    this.pStock.setBounds(450, 90, 200, 30);
    this.addMedicine.add(this.pStock);
    
    this.add = new JButton("Add", this.icon);
    this.add.setBounds(260, 140, 100, 30);
    this.addMedicine.add(this.add);
    this.add.addActionListener(new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			addToTable();
			
		}
    	
    });
    
    this.cancel = new JButton("Cancel", this.icon2);
    this.cancel.setBounds(260, 180, 100, 30);
    this.addMedicine.add(this.cancel);
    this.cancel.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        NewSale.this.cancelall();
      }
    });
    this.remove = new JButton("Remove", this.icon3);
    this.remove.setBounds(7, 210, 100, 30);
    this.addMedicine.add(this.remove);
    this.remove.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent arg0)
      {
        if (NewSale.this.table.getSelectedRow() >= 0)
        {
          DefaultTableModel model = (DefaultTableModel)NewSale.this.table.getModel();
          model.removeRow(NewSale.this.table.getSelectedRow());
          NewSale.this.getPrice();
          NewSale.this.getGrandTotal();
        }
      }
    });
    this.sale = new JButton("Sale", this.icon4);
    this.sale.setBounds(500, 175, 130, 70);
    this.addMedicine.add(this.sale);
    
    this.cb.setBounds(500,110,130,40);
    cb.setBackground(new Color(200, 221, 242));
    addMedicine.add(cb);
    cb.setSelected(true);
    
    
    this.sale.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent arg0)
      {
        if (table.getRowCount() > 0)
        {
        	System.out.println(table.getRowCount()+" is now row count");
          Calendar Calender = new GregorianCalendar();
          int data = Calender.get(5);
          int month = Calender.get(2);
          int year = Calender.get(1);
          int hour = Calender.get(10);
          int minute = Calender.get(12);
          int second = Calender.get(13);
          SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
          SimpleDateFormat sdt = new SimpleDateFormat("hh:mm:ss");
          
          String instantDate = sdf.format(Calender.getTime());
          String instantTime = sdt.format(Calender.getTime());
          
          
          String id = Accounts.getId();
          rbill=id;
          DefaultTableModel model = (DefaultTableModel)NewSale.this.table.getModel();
          for (int i = 0; i < NewSale.this.table.getRowCount(); i++)
          {
            Connection con = Connections.getConnection();
            try
            {
              PreparedStatement st = con.prepareStatement("insert into se(ne,qu,pr,date,time,sn) values (?,?,?,?,?,?)");
              st.setString(1, Encryption.encrypt(model.getValueAt(i, 0).toString()));
              st.setString(2, Encryption.encrypt(model.getValueAt(i, 1).toString()));
              st.setString(3, Encryption.encrypt(model.getValueAt(i, 3).toString()));
              st.setString(4, Encryption.encrypt(instantDate));
              st.setString(5, Encryption.encrypt(instantTime));
              
              st.setString(6, Encryption.encrypt(id));
              if (st.executeUpdate() > 0)
              {
                System.out.println("Succeded");
                SalePanel.saleReport.refreshSearch();
                NewSale.this.subtract(Encryption.encrypt(model.getValueAt(i, 0).toString()), Integer.parseInt(model.getValueAt(i, 1).toString()));
              }
            }
            catch (SQLException e)
            {
              e.printStackTrace();
            }
          }
          if(cb.isSelected())
          {
              print();
          }
          else
          {
        	  JOptionPane.showMessageDialog(null,"Sold Successfully");
          }
          
          NewSale.this.cancelall();
          
        }
       
        
        System.out.println("hello");
        System.out.println(error.getText());
        error.setText("");
        
      }
    });
    this.update.setBackground(Color.GRAY);
    this.update.setBounds(5, 250, 640, 195);
    this.update.setLayout(new BorderLayout());
    this.addMedicine.add(this.update);
    
    Object[][] data = new Object[0][];
    
    String[] columnN = { "Medicine Name", "Quantity", "Unit Price", "Total Price" };
    DefaultTableModel mo = new DefaultTableModel(data, columnN);
    this.table = new JTable(mo);
    this.table.setPreferredScrollableViewportSize(new Dimension(500, 50));
    this.table.setFillsViewportHeight(true);
    this.scrollPane = new JScrollPane(this.table);
    this.update.add(this.scrollPane, "Center");
    
    this.bill.setBackground(new Color(200, 221, 242));
    this.bill.setBorder(BorderFactory.createEtchedBorder());
    this.bill.setLayout(null);
    
    this.billLabel.setFont(new Font("Calibri", 1, 50));
    this.billLabel.setBounds(180, 5, 150, 80);
    this.bill.add(this.billLabel);
    JSeparator serparator = new JSeparator();
    serparator.setBounds(0, 70, 550, 2);
    this.bill.add(serparator);
    
    JLabel totalItemLabel = new JLabel("Total Items");
    totalItemLabel.setBounds(20, 90, 200, 50);
    totalItemLabel.setFont(new Font("Calibri", 1, 20));
    this.bill.add(totalItemLabel);
    
    this.billItem.setBounds(350, 90, 200, 50);
    this.billItem.setFont(new Font("Calibri", 1, 20));
    this.bill.add(this.billItem);
    
    JLabel billPriceLabel = new JLabel("Price");
    billPriceLabel.setBounds(20, 140, 200, 50);
    billPriceLabel.setFont(new Font("Calibri", 1, 20));
    this.bill.add(billPriceLabel);
    
    this.billprice.setBounds(350, 140, 200, 50);
    this.billprice.setFont(new Font("Calibri", 1, 20));
    this.bill.add(this.billprice);
    
    JLabel billFeeLabel = new JLabel("Fee ");
    billFeeLabel.setBounds(20, 190, 200, 50);
    billFeeLabel.setFont(new Font("Calibri", 1, 20));
    this.bill.add(billFeeLabel);
    
    this.billFee.setBounds(350, 190, 200, 50);
    this.billFee.setFont(new Font("Calibri", 1, 20));
    this.bill.add(this.billFee);
    this.billFee.setText(Accounts.getFee());
    
    JLabel discountLabel = new JLabel("Discount %");
    discountLabel.setBounds(20, 240, 200, 50);
    discountLabel.setFont(new Font("Calibri", 1, 20));
    this.bill.add(discountLabel);
    
    this.billDiscount.setBounds(350, 240, 200, 50);
    this.billDiscount.setFont(new Font("Calibri", 1, 20));
    this.bill.add(this.billDiscount);
    this.billDiscount.setText(Accounts.getDiscount());
    
    JSeparator separator = new JSeparator();
    separator.setBounds(0, 380, 450, 2);
    this.bill.add(separator);
    
    JLabel totalLabel = new JLabel("Total :-");
    totalLabel.setFont(new Font("Calibri", 1, 30));
    totalLabel.setBounds(220, 380, 150, 80);
    this.bill.add(totalLabel);
    
    this.total.setFont(new Font("Calibri", 1, 25));
    this.total.setForeground(new Color(19, 142, 0));
    this.total.setBounds(350, 380, 150, 80);
    this.bill.add(this.total);
    
    this.login.add(this.addMedicine);
    this.login.add(this.bill);
    
    this.login.setBackground(new Color(200, 221, 242));
    
    add(this.login);
  }
  
  public void cancelall()
  {
    DefaultTableModel model = (DefaultTableModel)this.table.getModel();
    model.setRowCount(0);
    this.name.setText("");
    this.quantity.setValue(Integer.valueOf(1));
    this.name.requestFocus();
    
    getPrice();
    getGrandTotal();
    this.error.setText("");
    System.out.println("Error test is "+error.getText());
  }
  
  public void getGrandTotal()
  {
    if (this.table.getRowCount() > 0)
    {
      int price = Integer.parseInt(this.billprice.getText());
      
      int drfee = Integer.parseInt(Accounts.getFee());
      int discount = Integer.parseInt(Accounts.getDiscount());
      price += drfee;
      discount = price * discount / 100;
      price -= discount;
      this.total.setText(String.valueOf(price));
    }
    else
    {
      this.total.setText("0");
    }
  }
  
  public void getPrice()
  {
    this.billItem.setText(String.valueOf(this.table.getRowCount()));
    if (this.table.getRowCount() >= 0)
    {
      DefaultTableModel model = (DefaultTableModel)this.table.getModel();
      int rows = this.table.getRowCount();
      int addprice = 0;
      for (int i = 0; i < rows; i++)
      {
        System.out.println(addprice + "  " + model.getValueAt(0, 3).toString());
        addprice += Integer.parseInt(model.getValueAt(i, 3).toString());
      }
      this.billprice.setText(String.valueOf(addprice));
    }
    else
    {
      this.billprice.setText("");
    }
  }
  
  public void print()
  {
	
	  rtotal=this.total.getText();
	  rdfee=Accounts.getFee();
	  rdiscount=Accounts.getDiscount();
	  
	  
	  Printsupport ps=new Printsupport();
		 Object printitem [][]=ps.getTableData(table);
		 ps.setItems(printitem);
		       
		 PrinterJob pj = PrinterJob.getPrinterJob();
		 pj.setPrintable(new Printsupport.MyPrintable(),ps.getPageFormat(pj));
		       try {
		            pj.print();
		           
		            }
		        catch (PrinterException ex) {
		                ex.printStackTrace();
		            }
	
	  
	  
	  
	  
	  	
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
//       String Header =
//                    "******Star Mobile Center*********;\n;"
//                  + "-----------------------------------------------\n; "
//                  + "-----------------------------------------------\n; ";
////                   String a="Imei                     "+"Model         "+"Type ";
////                   String sp="\n;";
////                   String pl=(f1.getSelectedItem().toString()+"     "+f3.getText()+"     "+typ);
//                   String a="Imei                           "+"Model         "+"Price ";
//                   String sp="\n;";
//                 
//                   a=a+sp; 
////                   String real=String.format("%-15s %5s %10s %n", "Name","Quantity","Price");
////                 String divider="-------------------------------\n";
////                 	real=real+divider;
//           		String real=String.format("%-25s %-10d %10d %n", nameFormat("Paracetamol"),20,10000);
//           		real+="\n;";
//           		real=real+String.format("%-25s %-10d %10d %n", nameFormat("Paracetamol"),20,10000);
//           		real+="\n;";real=real+String.format("%-25s %-10d %10d %n", "q",20,10000);
//           		real+="\n;";real=real+String.format("%-25s %-10d %10d %n", nameFormat("Lomotil")+"     ",20,10000);
//           		real+="\n;";real=real+String.format("%-25s %-10d %10d %n", nameFormat("roshti"),20,10000);
//           		real+="\n;";real=real+String.format("%-25s %-10d %10d %n", nameFormat("alhar"),20,10000);
//           		real+="\n;";real=real+String.format("%-25s %-10d %10d %n", nameFormat("Y daren e"),20,10000);
//                   
//                   a=a+real;
//                   System.out.println(a);
//                   
////      String a="Imei     "+f1.getSelectedItem().toString()+"\n; "+"Model     "+f3.getText()+"\n; "+"Type     "+typ+"";
//           String h=Header+a;
//      String amt  =
//                  "\n;-----------------------------------------------\n; "
//                  + "Total Amount      Rs."+total.getText()+"\n; "
//                  + "-----------------------------------------------\n; "
//                  + "-----------------------------------------------\n; "
//                  + "              Contact us                \n; "
//                  + "     Call/Whatsapp 0340-8351689\n; "
//                  + "Ground floor Saithi Plaza Sialkot\n;"
//                  + "********************************\n;"
//                  + "            Thank You             \n; "
//                  + "_________________________________\n";
//      String fin=h+amt;
//      printnow p=new printnow();
//      printnow.printcard(fin);
//      System.out.print(fin);
// 
//	  
//	  
//	  
//	  
//	  
//	  
//	  
//	  
//	  
//	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
	  
//    try
//    {
//        String head = String.format("%30s\n\n", new Object[] { "   مشتاق میڈیکل سٹور  " });
//        String head1 = String.format("%30s\n\n", new Object[] { "   مرالہ روڈ حسسن وال سیالکوٹ  " });
//        String head2 = String.format("%30s\n\n", new Object[] { "-------------------"});
//      String s = String.format("%-15s %5s %10s\n", new Object[] { "Item       ", "    Qty", "Price" });
//      String s1 = String.format("%-15s %5s %10s\n", new Object[] { "---------", "    --------", " ----------" });
//      String output = head+head1+head2 + s + s1;
//      String real=String.format("%-15s %5s %10s %n", "Name","Quantity","Price");
//      String divider="-------------------------------\n";
//      	real=real+divider;
//		real=real+String.format("%-15s %5d %10d %n", "Panadol",20,10000);
//		real=real+String.format("%-15s %5d %10d %n", "Roshti",20,10000);
//		real=real+String.format("%-15s %5d %10d %n", "Hamza",20,10000);
//		real=real+String.format("%-15s %5d %10d %n", "Sandee",20,10000);
//		real=real+String.format("%-15s %5d %10d %n", "Ali",20,10000);
//		real=real+String.format("%-15s %5d %10d %n", "Sun",20,10000);
//		real=real+String.format("%-15s %5d %10d %n", "Done",20,10000);
//      
////      String Names = nameFormat("Qasim Ali");
////      String Quanti = "15";
////      float Prices = 23.3F;
////      DefaultTableModel model=(DefaultTableModel) table.getModel();
////      String linecustom ;
////      Align align = new Align();
////      // header
////      align.addLine("Name","Quanity","Price");
////     
////      for(int i=0;i<table.getRowCount();i++)
////      {
////    	  
//////    	  System.out.println(model.getValueAt(i, 3));
//////       linecustom = String.format("%.15s %-10s %s\n", new Object[] { model.getValueAt(i, 0).toString(), model.getValueAt(i, 1).toString(), model.getValueAt(i, 3).toString() });
//////       output = output + linecustom;
////    	 align.addLine(model.getValueAt(i, 0).toString(), model.getValueAt(i, 1).toString(), model.getValueAt(i, 3).toString() );
////    	 
////    	  
////      }
//      
////      String line2custom = String.format("%-15s %-5s %-10.2f\n", new Object[] { nameFormat("Septran"), Quanti, Float.valueOf(Prices) });
////      String line3custom = String.format("%-15s %-5s %-10.2f\n", new Object[] { nameFormat("Brufan"), Quanti, Float.valueOf(Prices) });
////      String line4custom = String.format("%-15s %-5s %-10.2f\n", new Object[] { nameFormat("Paracetamol"), Quanti, Float.valueOf(Prices) });
////      String line5custom = String.format("%-15s %-5s %-10.2f\n", new Object[] { nameFormat("Panadol"), Quanti, Float.valueOf(Prices) });
////       
////      
////      output = output + line2custom;
////      output = output + line3custom;
////      output = output + line4custom;
//      output = output + real;
////      
//     
//      String lin2 = String.format("%-15s\n", new Object[] { "-------------------------------------" });
//      output = output + lin2;
//      
//      String Total = String.format("%20s\n", new Object[] { "Total" });
//      String total = String.format("%20s\n\n", new Object[] { this.total.getText() });
//      String footer = String.format("%-15s\n", new Object[] { "" });
//      output = output + Total + total + footer;
//      this.textPane.setText(output);
//      
//      JOptionPane.showMessageDialog(null, output);
//      
//      PrinterJob printerJob = PrinterJob.getPrinterJob();
//      PageFormat pageFormat = printerJob.defaultPage();
//      Paper paper = new Paper();
//      paper.setSize(180.0D, paper.getHeight() + 20.0D);
//      paper.setImageableArea(0.0D, 0.0D, paper.getWidth() - 2.0D, paper.getHeight() - 2.0D);
//      pageFormat.setPaper(paper);
//      pageFormat.setOrientation(1);
//      printerJob.setPrintable(output);
//      printerJob.print();
//    }
//    catch (Exception e)
//    {
//      e.printStackTrace();
//    }
  }
  public String nameFormat(String aName)
  {
	  String fName = aName;
	  fName=fName.toLowerCase();
	  
	  System.out.println("length of "+fName+" is "+fName.length());
	  if(fName.length()>10)
	  {
		fName=fName.substring(0, 10);
	  }
	  else
	  {
			  StringBuilder str1=new StringBuilder(fName);
			  for(int i=0;i<20-fName.length();i++)
			  {
				  str1.append(".");
			  }
			  fName=str1.toString();
	
	  }
	  
	  
	  System.out.println("Return Size of Name is"+fName.length());
	 		
	return fName;
	 		
	
  }
  
  
  public void addToTable()
  {
    if (this.name.getText().equals(""))
    {
      this.error.setText("!! Medicine not Found !!");
    }
    else if (this.name.getDataList().contains(this.name.getText()))
    {
      DefaultTableModel model = (DefaultTableModel)this.table.getModel();
      int row = -1;
      for (int i = 0; i < this.table.getRowCount(); i++) {
        if (model.getValueAt(i, 0).equals(this.name.getText())) {
          row = i;
        }
      }
      if (row >= 0)
      {
        int previousPrice = Integer.parseInt(model.getValueAt(row, 2).toString());
        int previous = Integer.parseInt(model.getValueAt(row, 1).toString());
        int check = Integer.parseInt(this.quantity.getValue().toString()) + previous;
        if (check > this.stock)
        {
          this.error.setText("!! Not Enough Stock !!");
        }
        else
        {
          Object value = String.valueOf((Integer.parseInt(this.quantity.getValue().toString()) + previous) * previousPrice);
          model.setValueAt(value, row, 3);
          value = String.valueOf(Integer.parseInt(this.quantity.getValue().toString()) + previous);
          System.out.println(value);
          model.setValueAt(value, row, 1);
          this.name.setText("");
          this.error.setText("");
          this.pStock.setText("");
          this.name.requestFocus();
          getPrice();
          getGrandTotal();
        }
      }
      else
      {
        this.error.setText("");
        if ((Integer.parseInt(this.quantity.getValue().toString()) > -1) && (Integer.parseInt(this.quantity.getValue().toString()) <= this.stock))
        {
          int tPrice = Integer.parseInt(this.quantity.getValue().toString()) * this.price;
          Object[] rowe = new Object[4];
          rowe[0] = this.name.getText();
          rowe[1] = this.quantity.getValue().toString();
          rowe[2] = Integer.valueOf(this.price);
          rowe[3] = Integer.valueOf(tPrice);
          DefaultTableModel modele = (DefaultTableModel)this.table.getModel();
          modele.addRow(rowe);
          this.name.setText("");
          this.error.setText("");
          this.pStock.setText("");
          this.name.requestFocus();
          
          getPrice();
          getGrandTotal();
        }
        else
        {
          this.error.setText("!! Not Enough Stock !!");
        }
      }
    }
    else
    {
      System.out.println("!! Medicine not found !!");
    }
  }
  
  public void refreshName()
  {
    this.name.removeAll();
    Connection con = Connections.getConnection();
    try
    {
      PreparedStatement st = con.prepareStatement("select * from md");
      ResultSet rs = st.executeQuery();
      while (rs.next()) {
        this.name.getDataList().add(Encryption.decrypt(rs.getString("ne")));
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
  }
  
  public void subtract(String mn, int qut)
  {
    Connection con = Connections.getConnection();
    try
    {
      PreparedStatement st = con.prepareStatement("select * from md where ne=?");
      st.setString(1, mn);
      ResultSet rs = st.executeQuery();
      int stock = 0;
      while (rs.next())
      {
        stock = Integer.parseInt(Encryption.decrypt(rs.getString("qt")));
        System.out.println("BackStockIS" + stock);
        stock -= qut;
        
        st = con.prepareStatement("update md set qt=? where ne=?");
        st.setString(1, Encryption.encrypt(String.valueOf(stock)));
        st.setString(2, mn);
        if (st.executeUpdate() > -1) {
          System.out.println("Succeded");
        }
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
  }
}
