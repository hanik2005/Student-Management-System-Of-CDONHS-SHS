
package Main;


import java.sql.Connection;
import db.MyConnection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class Student {
    
    Connection con = MyConnection.getConnection();
    PreparedStatement ps;
    
    public int getMax(){
        int id = 0;
        Statement st;
        
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery("select max(id) from student");
            while(rs.next()){
                id = rs.getInt(1);
            
            }
        } catch (SQLException ex) {
            System.getLogger(Student.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return id + 1;
        
    
    }
}
