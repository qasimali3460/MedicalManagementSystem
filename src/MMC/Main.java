package MMC;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class Main
{
  public static LoginPanel loginPanel = new LoginPanel();
  ImageIcon keyLogin = new ImageIcon(getClass().getClassLoader().getResource("rlogo.png"));
  static int current = 0;
  JFrame f = new JFrame();
  JPanel Logo = new JPanel(new FlowLayout());
  static JPanel change = new JPanel();
  Dimension logoDimension;
  public static Main my;
  JLabel date = new JLabel("                                                                                                                                                                                                                             Sunday      |      March 11,2018      |      10:03:54 AM");
  StockPanel stockPanel = new StockPanel();
  SalePanel salePanel = new SalePanel();
  static profile profil=new profile();
  static Eprofile eprofil=new Eprofile();
  JPanel footer = new JPanel();
  Dimension d;
  Thread clock;
  String[] monthNames = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
  JMenuBar menuBar = new JMenuBar();
  public static JMenu Home;
  public static JMenu Sale;
  public static JMenu Stock;
  public static JMenu Report;
  JMenuItem profile;
  JMenuItem uprofile;
  JMenuItem logout;
  JMenuItem today;
  JMenuItem stockReport;
  
  Main()
  {
    this.f.setDefaultCloseOperation(3);
    this.f.setMinimumSize(new Dimension(1200, 750));
    f.setIconImage(keyLogin.getImage());
    this.f.setResizable(true);
    menu();
    
    this.logoDimension = new Dimension();
    this.logoDimension = this.Logo.getPreferredSize();
    this.logoDimension.height = 80;
    this.Logo.setPreferredSize(this.logoDimension);
    
    JLabel name = new JLabel("MUSHTAQ Medical Complex");
    name.setForeground(new Color(196, 29, 185));
    name.setFont(new Font("Times New Roman", 1, 40));
    this.Logo.add(name);
    JPanel time = new JPanel(new FlowLayout(1));
    time.add(this.date);
    
    time.setSize(100, 100);
    
    JPanel two = new JPanel(new BorderLayout());
    two.add(this.Logo, "North");
    two.add(time, "Center");
    two.setBorder(BorderFactory.createEtchedBorder());
    
    this.f.add(two, "North");
    
    change.setLayout(new BorderLayout());
    addLogin();
    
    this.f.add(change, "Center");
    
    this.d = this.footer.getPreferredSize();
    this.d.height = 40;
    this.footer.setPreferredSize(this.d);
    this.footer.setLayout(new FlowLayout(1));
    this.footer.setBorder
    (BorderFactory.createEtchedBorder());
    JLabel copyright = new JLabel(" © Mushtaq Medical Complex");
    copyright.setFont(new Font("Times New Roman", 1, 20));
    this.footer.add(copyright);
    
    this.date.setFont(new Font("Times New Roman", 1, 15));
    
   clock = new Thread(new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			for(;;)
			{
				set();
				
				try {
					clock.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
    	
    });
    
    clock.start();
    Home.setEnabled(false);
    Sale.setEnabled(false);
    Stock.setEnabled(false);
    Report.setEnabled(false);
    
    this.f.add(this.footer, "South");
    this.f.setExtendedState(6);
    this.f.setVisible(true);
  }
  
  public static void remove()
  {
    if (current == 1) {
      removeSale();
    }
    if (current == 2) {
      removeStockPanel();
    }
    if (current == 3) {
        removeLogin();
      }
    if (current == 4) {
        removeProfile();
      }
    if (current == 5) {
        removeeProfile();
      }
  }
  
  public void menu()
  {
    Home = new JMenu("Home");
    this.profile = new JMenuItem("Admin Profile");
    profile.addMouseListener(new MouseListener() {

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
			// TODO Auto-generated method stub
			Main.remove();
			Main.addProfile();
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
    	
    });
    Home.add(profile);
    
    Home.addSeparator();
    this.uprofile = new JMenuItem("User Profile");
   
    uprofile.addMouseListener(new MouseListener() {

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
			// TODO Auto-generated method stub
	
			Main.addeProfile();
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
    	
    });
    
    
    
    Home.add(this.uprofile);
    Home.addSeparator();
    this.logout = new JMenuItem("Logout");
    this.logout.addMouseListener(new MouseListener()
    {
      public void mouseClicked(MouseEvent arg0) {}
      
      public void mouseEntered(MouseEvent arg0) {}
      
      public void mouseExited(MouseEvent arg0) {}
      
      public void mousePressed(MouseEvent arg0)
      {
        Main.remove();
        Main.addLogin();
      }
      
      public void mouseReleased(MouseEvent arg0) {}
    });
    Home.add(this.logout);
    
    Sale = new JMenu("Sale");
    Sale.addMouseListener(new MouseListener()
    {
      public void mouseClicked(MouseEvent arg0) {}
      
      public void mouseEntered(MouseEvent arg0) {}
      
      public void mouseExited(MouseEvent arg0) {}
      
      public void mousePressed(MouseEvent arg0)
      {
        System.out.println("SalePressed");
        
        Main.remove();
        Main.change.setVisible(false);
        Main.addSale();
        Main.change.setVisible(true);
      }
      
      public void mouseReleased(MouseEvent arg0) {}
    });
    Stock = new JMenu("Stock");
    Stock.addMouseListener(new MouseListener()
    {
      public void mouseClicked(MouseEvent arg0) {}
      
      public void mouseEntered(MouseEvent arg0) {}
      
      public void mouseExited(MouseEvent arg0) {}
      
      public void mousePressed(MouseEvent arg0)
      {
        Main.remove();
        Main.change.setVisible(false);
        Main.addStockPanel();
        Main.change.setVisible(true);
      }
      
      public void mouseReleased(MouseEvent arg0) {}
    });
    this.menuBar.add(Home);
    this.menuBar.add(Sale);
    this.menuBar.add(Stock);
    
    Report = new JMenu("Report");

    
    this.today = new JMenuItem("Today Sale Report");
    Report.add(this.today);
    today.addMouseListener(new MouseListener() {

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
			// TODO Auto-generated method stub
			SalePanel.saleReport.show.setSelectedIndex(0);
			SalePanel.saleReport.print();
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
    	
    });
    
    
    Report.addSeparator();
    
    
    
    
    this.stockReport = new JMenuItem("Stock Report");
    Report.add(this.stockReport);
    stockReport.addMouseListener(new MouseListener() {

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
			// TODO Auto-generated method stub
			StockPanel.totalStock.printNow();
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
    	
    });
 //   this.menuBar.add(Report);
    
    this.f.setJMenuBar(this.menuBar);
  }
  
  public void set()
  {
    Calendar Calender = new GregorianCalendar();
    int data = Calender.get(5);
    int month = Calender.get(2);
    int year = Calender.get(1);
    int hour = Calender.get(10);
    int minute = Calender.get(12);
    int second = Calender.get(13);
    int am = Calender.get(11);
    String pm = "AM";
    if (am >= 12) {
      pm = "PM";
    }
    this.date.setText("                                                                                                                                                                                                                                " + this.monthNames[(month - 1)] + " " + data + "," + year + "      |      " + hour + ":" + minute + ":" + second + " " + pm);
  }
  
  public static void main(String[] args)
  {
    Main m = new Main();
    my = m;
  }

  public static void addLogin()
  {
	my.loginPanel.username.setText("");
	my.loginPanel.pwd.setText("");
    change.setVisible(false);
    remove();
    change.add(loginPanel, "Center");
    current = 3;
//    Home.setEnabled(false);
//    Sale.setEnabled(false);
//    Stock.setEnabled(false);
//    Report.setEnabled(false);
    	Home.setVisible(false);
    	Sale.setVisible(false);
    	Stock.setVisible(false);
    change.setVisible(true);
  }
  public static void addProfile()
  {
    change.setVisible(false);
    remove();
    change.add(profil, "Center");
    current = 4;
   change.setVisible(true);
  }

  public static void addeProfile()
  {
    change.setVisible(false);
    remove();
    change.add(eprofil, "Center");
    current = 5;
   change.setVisible(true);
  }
  public static void removeeProfile()
  {
	    change.remove(eprofil);
	    

		Home.setVisible(true);
		Sale.setVisible(true);
		Stock.setVisible(true);
	    Home.setEnabled(true);
	    Sale.setEnabled(true);
	    Stock.setEnabled(true);
	    Report.setEnabled(true);
	    
	    change.setVisible(false);
	    change.setVisible(true);
	  	  }
  
  public static void removeLogin()
  {
	  
    change.remove(loginPanel);
    

	Home.setVisible(true);
	Sale.setVisible(true);
	Stock.setVisible(true);
    Home.setEnabled(true);
    Sale.setEnabled(true);
    Stock.setEnabled(true);
    Report.setEnabled(true);
    
    change.setVisible(false);
    change.setVisible(true);
  }
  
  public static void removeProfile()
  {
	  
    change.remove(profil);
    

	Home.setVisible(true);
	Sale.setVisible(true);
	Stock.setVisible(true);
    Home.setEnabled(true);
    Sale.setEnabled(true);
    Stock.setEnabled(true);
    Report.setEnabled(true);
    
    change.setVisible(false);
    change.setVisible(true);
  }
  
  public static void addStockPanel()
  {
    change.add(my.stockPanel);
    current = 2;
  }
  
  public static void removeStockPanel()
  {
    change.remove(my.stockPanel);
  }
  
  public static void addSale()
  {
    change.add(my.salePanel);
    current = 1;
  }
  
  public static void removeSale()
  {
    change.remove(my.salePanel);
  }
}
