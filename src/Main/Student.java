
package Main;


import java.sql.Connection;
import db.MyConnection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
public class Student {
    
    Connection con = MyConnection.getConnection();
    PreparedStatement ps;
    
    public int getMax(){
        int id = 0;
        Statement st;
        
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery("select max(id) from students");
            while(rs.next()){
                id = rs.getInt(1);
            
            }
        } catch (SQLException ex) {
            System.getLogger(Student.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return id + 1;
        
    
    }
    //insert data into student table
    public void insert(int id, String sname, String date, String gender, String email, String phone, 
            String motherName, String fatherName, String addressLine1, 
            String addressLine2, String birthCer, String form137, String imagePath){
        
        String sql = "insert into students values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setString(2, sname);
            ps.setString(3, date);
            ps.setString(4, gender);
            ps.setString(5, email);
            ps.setString(6, phone);
            ps.setString(7, motherName);
            ps.setString(8, fatherName);
            ps.setString(9, addressLine1);
            ps.setString(10, addressLine2);
            ps.setString(11, birthCer);
            ps.setString(12, form137);
            ps.setString(13, imagePath);
            
            
            if(ps.executeUpdate() > 0){
                JOptionPane.showMessageDialog(null, "New Student added successfully");
            
            
            }
        } catch (SQLException ex) {
            System.getLogger(Student.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
        
    
    
    }
}
