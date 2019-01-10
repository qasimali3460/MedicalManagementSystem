package MMC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Accounts
{
  Connection con = Connections.getConnection();
  public static String adminU = "";
  public static String adminP = "";
  public static String userU = "";
  public static String userP = "";
  public static int type=0;
  
  Accounts()
  {
    try
    {
      PreparedStatement st = Connections.getConnection().prepareStatement("select * from Ac");
      ResultSet rs = st.executeQuery();
      int a = 1;
      while (rs.next()) {
        if (a == 1)
        {
          adminU = Encryption.decrypt(rs.getString("un"));
          adminP = Encryption.decrypt(rs.getString("pw"));
          a++;
        }
        else
        {
          userU = Encryption.decrypt(rs.getString("un"));
          userP = Encryption.decrypt(rs.getString("pw"));
          break;
        }
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
  }
  
  public static String getFee()
  {
    String fee = null;
    try
    {
      PreparedStatement st = Connections.getConnection().prepareStatement("select * from Ac");
      ResultSet rs = st.executeQuery();
      if (rs.next()) {
        fee = Encryption.decrypt(rs.getString("ee"));
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    return fee;
  }
  
  public static String getDiscount()
  {
    String discount = null;
    try
    {
      PreparedStatement st = Connections.getConnection().prepareStatement("select * from Ac");
      ResultSet rs = st.executeQuery();
      if (rs.next()) {
        discount = Encryption.decrypt(rs.getString("count"));
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    return discount;
  }
  
  public static String getId()
  {
    Calendar Calender = new GregorianCalendar();
    int data = Calender.get(5);
    int month = Calender.get(2);
    int year = Calender.get(1);
    int hour = Calender.get(10);
    int minute = Calender.get(12);
    int second = Calender.get(13);
    int hourOf = Calender.HOUR_OF_DAY;
    System.out.println("Hour of day is"+hourOf);
    return String.valueOf(data + month + year + hour + minute + second+hourOf);
  }
}
