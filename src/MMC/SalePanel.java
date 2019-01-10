package MMC;


import java.text.ParseException;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SalePanel
  extends JTabbedPane
{
  public static NewSale newSale = new NewSale();
  public static SaleReport saleReport = new SaleReport();
  
  SalePanel()
  {
    add(newSale);
    add(saleReport);
    addChangeListener(new ChangeListener()
    {
      public void stateChanged(ChangeEvent arg0)
      {
        if (SalePanel.this.getSelectedIndex() == 1) {
          try {
			SalePanel.saleReport.refreshTodayTable();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        }
      }
    });
  }
}
