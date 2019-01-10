package MMC;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.imageio.ImageIO;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
/**
 *
 * @author All Open source developers
 * @version 1.0.0.0
 * @since 2014/12/22
 */
/*This Printsupport java class was implemented to get printout.
* This class was specially designed to print a Jtable content to a paper.
* Specially this class formated to print 7cm width paper.
* Generally for pos thermel printer.
* Free to customize this source code as you want.
* Illustration of basic invoice is in this code.
* demo by gayan liyanaarachchi
 
 */

public class Printservice {
 
         static JTable itemsTable;
         public static  int total_item_count=10;
         public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss a";
         public  static String title[] = new String[] {"Item ID","Item Name","Price","Quantity"};
	
public static void setItems(Object[][] printitem){
        Object data[][]=printitem;
        DefaultTableModel model = new DefaultTableModel();
       //assume jtable has 4 columns.
        model.addColumn(title[0]);
        model.addColumn(title[1]);
        model.addColumn(title[2]);
        model.addColumn(title[3]);
        

        int rowcount=printitem.length;
        
        addtomodel(model, data, rowcount);
       

        itemsTable = new JTable(model);
}

public static void addtomodel(DefaultTableModel model,Object [][]data,int rowcount){
        int count=0;
        while(count < rowcount){
         model.addRow(data[count]);
         count++;
        }
        if(model.getRowCount()!=rowcount)
          addtomodel(model, data, rowcount);
        
        System.out.println("Check Passed.");
}
          
public Object[][] getTableData (JTable table) {
    int itemcount=table.getRowCount();
    System.out.println("Item Count:"+itemcount);
    
    DefaultTableModel dtm = (DefaultTableModel) table.getModel();
    int nRow = dtm.getRowCount(), nCol =dtm.getColumnCount();
    Object[][] tableData = new Object[nRow][nCol];
    if(itemcount==nRow)                                        //check is there any data loss.
    {
    for (int i = 0 ; i < nRow ; i++){
        for (int j = 0 ; j < nCol ; j++){
            tableData[i][j] = dtm.getValueAt(i,j);           //pass data into object array.
            }}
     if(tableData.length!=itemcount){                      //check for data losses in object array
     getTableData(table);                                  //recursively call method back to collect data
     }   
    System.out.println("Data check passed");
    }
    else{
                                                           //collecting data again because of data loss.
   getTableData(table);
   }
   return tableData;                                       //return object array with data.
    }     

public static PageFormat getPageFormat(PrinterJob pj){
        PageFormat pf = pj.defaultPage();
        Paper paper = pf.getPaper();    
             
                double middleHeight =total_item_count*1.0;  //dynamic----->change with the row count of jtable
                double headerHeight = 5.0;                  //fixed----->but can be mod
        	double footerHeight = 5.0;                  //fixed----->but can be mod
                
                double width = convert_CM_To_PPI(7);      //printer know only point per inch.default value is 72ppi
        	double height = convert_CM_To_PPI(headerHeight+middleHeight+footerHeight); 
            paper.setSize(width, height);
            paper.setImageableArea(
                            convert_CM_To_PPI(0.25), 
                            convert_CM_To_PPI(0.5), 
                            width - convert_CM_To_PPI(0.35), 
                            height - convert_CM_To_PPI(1));   //define boarder size    after that print area width is about 180 points
            
            pf.setOrientation(PageFormat.PORTRAIT);           //select orientation portrait or landscape but for this time portrait
            pf.setPaper(paper);    
            
            return pf;
}
        
        
protected static double convert_CM_To_PPI(double cm) {            
	        return toPPI(cm * 0.393600787);            
}

protected static double toPPI(double inch) {            
	        return inch * 72d;            
}

public static String now() {
//get current date and time as a String output   
Calendar cal = Calendar.getInstance();
SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
return sdf.format(cal.getTime());

}

public static class MyPrintable implements Printable {
 @Override
  public int print(Graphics graphics, PageFormat pageFormat, 
	                int pageIndex) throws PrinterException {    
	                int result = NO_SUCH_PAGE;    
	                if (pageIndex == 0) {                    
	                Graphics2D g2d = (Graphics2D) graphics;                    
	                             
	                double width = pageFormat.getImageableWidth();
	                double height = pageFormat.getImageableHeight();    
	                g2d.translate((int) pageFormat.getImageableX(),(int) pageFormat.getImageableY()*2); 
	                Font font = new Font("Monospaced",Font.PLAIN,7);       
	                g2d.setFont(font);
                       
	                
	                try {
	        	/*
                         * Draw Image*
                           assume that printing reciept has logo on top 
                         * that logo image is in .gif format .png also support
                         * image resolution is width 100px and height 50px
                         * image located in root--->image folder 
                         */	
                                    int x=100 ;                                        //print start at 100 on x axies
                                    int y=10;                                          //print start at 10 on y axies
                                    int imagewidth=100;
                                    int imageheight=50;
                          BufferedImage read = ImageIO.read(getClass().getClassLoader().getResource("KeyLogin.png"));
                          g2d.drawImage(read,x,y,imagewidth,imageheight,null);         //draw image
                          g2d.drawLine(10, y+60, 180, y+60);                          //draw line
                                 } catch (IOException e) {
	        			e.printStackTrace();
	        		}
      		try{
	        /*Draw Header*/
                    int y=80;
	              g2d.drawString(" مشتاق میڈیکل کمپلیکس   ", 40,y);  
	              g2d.drawString("مرالہ روڈ حسسن وال سیالکوٹ", 35,y+10);                 //shift a line by adding 10 to y value
	              g2d.drawString(now(), 10, y+20);                                //print date
	              g2d.drawString("Bill No: : #21342", 10, y+30);
	              g2d.drawString("Doctor Fee  : 200", 120, y+30); 
		             
	              
	        	
	              /*Draw Colums*/
                      g2d.drawLine(10, y+40, 180, y+40);
                      g2d.drawString(title[0], 10 ,y+50);
                      g2d.drawString(title[1], 50 ,y+50);
                      g2d.drawString(title[2], 100 ,y+50);
                      g2d.drawString(title[3], 150 ,y+50);
                      g2d.drawLine(10, y+60, 180, y+60);
                   
	              int cH = 0;
	              TableModel mod = itemsTable.getModel();
                        
	              for(int i = 0;i < mod.getRowCount() ; i++){
	                	/*Assume that all parameters are in string data type for this situation
                                 * All other premetive data types are accepted.
                                */
	                	String itemid = mod.getValueAt(i, 0).toString();
	                	String itemname = mod.getValueAt(i, 1).toString();
                                String price = mod.getValueAt(i, 2).toString();
                                String quantity = mod.getValueAt(i, 3).toString();
	                	
	                	cH = (y+70) + (10*i);                             //shifting drawing line
	                	
	                	g2d.drawString(itemid, 10, cH);
	                	g2d.drawString(itemname,50, cH);
	                	g2d.drawString(price , 100, cH);
                        g2d.drawString(quantity , 150, cH);
                        if(false)
                        {
                        	g2d.translate((int) pageFormat.getImageableX()+1.0,(int) pageFormat.getImageableY()+1.0); 
        	                
                        }
                              
	                }

	                /*Footer*/
	                g2d.drawLine(10, cH+10, 180, cH+10);
	                g2d.drawString("Discount : 10	%",100, cH+20);
	                g2d.drawString("Total  :  10000",100, cH+30);
	                g2d.drawString("******************************************", 10, cH+50);
	                g2d.drawString("فون   :  052-4363138, 0300-9586156  ",10, cH+60);
	                g2d.drawLine(10, cH+90, 180, cH+90);
	                
                    
                    
                    
                    
                    
                    
                                                                                 //end of the reciept
            }
            catch(Exception r){
              r.printStackTrace();
            }
  
	                result = PAGE_EXISTS;    
	            }    
	            return result;    
      }
   }
         
}