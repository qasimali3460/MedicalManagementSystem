package MMC;

import java.io.PrintStream;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class StockPanel
  extends JTabbedPane
{
  public static TotalStock totalStock = new TotalStock();
  public static AddStock addStock = new AddStock();
  public static UpdateStock updateStock = new UpdateStock();
  
  StockPanel()
  {
    add(totalStock);
    add(addStock);
    add(updateStock);
    addChangeListener(new ChangeListener()
    {
      public void stateChanged(ChangeEvent arg0)
      {
        System.out.println(StockPanel.this.getSelectedIndex());
        if (StockPanel.this.getSelectedIndex() == 2) {
          StockPanel.updateStock.refreshTable();
        }
        if (StockPanel.this.getSelectedIndex() == 0) {
          StockPanel.totalStock.refreshTable();
        }
      }
    });
  }
}
