package MMC;


import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
*
* @author Qasim
*/
public class printnow {
public static boolean printcard(final String bill)
{
   final PrinterJob job=PrinterJob.getPrinterJob();
   Printable contentToPrint=new Printable(){
       @Override
       public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
           Graphics2D g2d=(Graphics2D) graphics;
           g2d.translate(pageFormat.getImageableX(),pageFormat.getImageableY());
           g2d.setFont(new Font("Times New Roman",Font.BOLD,10));
           String[] billz=bill.split(";");
           int y=15;
           for(int i=0;i<billz.length;i++)
           {
                   graphics.drawString(billz[i], 5, y);
                    y=y+15;
           }
           
           if(pageIndex>0){
               return NO_SUCH_PAGE;
           }
           return PAGE_EXISTS;
       }
   
   };
   PageFormat pageformat=new PageFormat();
   pageformat.setOrientation(PageFormat.PORTRAIT);
   Paper paperr=pageformat.getPaper();
   paperr.setImageableArea(0, 0, paperr.getWidth(), paperr.getHeight()-2);
   pageformat.setPaper(paperr);
   job.setPrintable(contentToPrint,pageformat);
   try {
       job.print();
   } catch (PrinterException ex) {
       Logger.getLogger(printnow.class.getName()).log(Level.SEVERE, null, ex);
   }
   
return true;
}
}
