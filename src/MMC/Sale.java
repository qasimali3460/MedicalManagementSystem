package MMC;

public class Sale
{
  String name;
  String quantity;
  String price;
  String date;
  String time;
  String serial;
  
  public Sale(String n, String qt, String p, String d, String t,String se)
  {
    this.name = n;
    this.quantity = qt;
    this.price = p;
    this.date = d;
    this.time = t;
    this.serial=se;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public String getQuantity()
  {
    return this.quantity;
  }
  
  public String getPrice()
  {
    return this.price;
  }
  
  public String getDate()
  {
    return this.date;
  }

  public String getTime()
  {
    return this.time;
  }
  public String getSerial()
  {
    return this.serial;
    		}
}
