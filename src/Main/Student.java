
package Main;


import java.sql.Connection;
import db.MyConnection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
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
    //check if student gmail account already exist
    public boolean isEmailExist(String email){
        try {
            ps = con.prepareStatement("select * from students where email = ?");
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return true;
            
            }
        } catch (SQLException ex) {
            System.getLogger(Student.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return false;
        
    }
    //check if student phone number already exist
    public boolean isPhoneExist(String phone){
        try {
            ps = con.prepareStatement("select * from students where phone_number = ?");
            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return true;
            
            }
        } catch (SQLException ex) {
            System.getLogger(Student.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return false;
        
    }
    
    //get all student values from database student table
    public void getStudentValue(JTable table, String searchValue){
        String sql = "select * from students where concat(name,email,phone_number) like ? order by id desc";
       
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, "%"+searchValue+"%");
            ResultSet rs = ps.executeQuery();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            Object[] row;
            while(rs.next()){
                row = new Object[13];
                row[0] = rs.getInt(1);
                row[1] = rs.getString(2);
                row[2] = rs.getString(3);
                row[3] = rs.getString(4);
                row[4] = rs.getString(5);
                row[5] = rs.getString(6);
                row[6] = rs.getString(7);
                row[7] = rs.getString(8);
                row[8] = rs.getString(9);
                row[9] = rs.getString(10);
                row[10] = rs.getString(11);
                row[11] = rs.getString(12);
                row[12] = rs.getString(13);
                model.addRow(row);
                
            
            }
        } catch (SQLException ex) {
            System.getLogger(Student.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    
    }
}
