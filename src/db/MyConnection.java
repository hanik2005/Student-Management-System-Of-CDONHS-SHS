
package db;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;
public class MyConnection {
    
   private static final String url = "jdbc:mysql://localhost:3306/cdonhs_shs_database";
   private static final String user = "root";
   private static final String pass = "";
   private static Connection conn = null;
   
   public static Connection getConnection(){
   
       try {
           // Load MySQL Driver
           Class.forName("com.mysql.cj.jdbc.Driver");
           conn = DriverManager.getConnection(url, user, pass);
       } catch (Exception ex) {
          System.out.println(ex.getMessage());
    
   }
       return conn;
   }
   
    
  
   
}
