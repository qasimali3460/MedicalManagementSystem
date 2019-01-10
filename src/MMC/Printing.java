package MMC;

import java.awt.Color;
import java.awt.Font;
import java.awt.print.PrinterException;
import java.text.MessageFormat;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;

public class Printing {

	public Printing() {
		// TODO Auto-generated constructor stub
	}
	public static void print(JTable tableee,MessageFormat header)
	{
		
		
		JTable table2=tableee;
		table2.setFont(new Font("Dialog", Font.PLAIN, 22));
	    table2.setRowHeight(30);
	    JTableHeader theader = tableee.getTableHeader();
	    theader.setBackground(new Color(205, 160, 214));
	    theader.setForeground(Color.white);
	    theader.setFont(new Font("Tahoma", 1, 20));
	    
	    
	    
        MessageFormat footer = new MessageFormat("© Mushtaq Medical Complex");
        
        try {
			table2.print(JTable.PrintMode.FIT_WIDTH, header, footer);
		} catch (PrinterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        table2.setFont(new Font("Dialog",Font.PLAIN,12));
        table2.setRowHeight(16);
	}

}
