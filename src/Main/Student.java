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

    public int getMax() {
        int id = 0;
        Statement st;

        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery("select max(student_id) from student");
            while (rs.next()) {
                id = rs.getInt(1);

            }
        } catch (SQLException ex) {
            System.getLogger(Student.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return id + 1;

    }

    //insert data into student table
    public void insert(int id, String fname, String midName, String lastName, String date, String gender, String email, String phone,
            String motherName, String fatherName, String addressLine1,
            String addressLine2, String birthCer, String form137, String imagePath, long lrn) {

        String sql = "insert into student values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setString(2, fname);
            ps.setString(3, midName);
            ps.setString(4, lastName);
            ps.setString(5, date);
            ps.setString(6, gender);
            ps.setString(7, email);
            ps.setString(8, phone);
            ps.setString(9, motherName);
            ps.setString(10, fatherName);
            ps.setString(11, addressLine1);
            ps.setString(12, addressLine2);
            ps.setString(13, birthCer);
            ps.setString(14, form137);
            ps.setString(15, imagePath);
            ps.setLong(16, lrn);

            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "New Student added successfully");

            }
        } catch (SQLException ex) {
            System.getLogger(Student.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

    }

    public boolean isEmailExist(String email, int excludeId) {
        try {
            ps = con.prepareStatement("SELECT * FROM student WHERE email = ? AND student_id <> ?");
            ps.setString(1, email);
            ps.setInt(2, excludeId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            System.getLogger(Student.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return false;
    }

    public boolean isPhoneExist(String phone, int excludeId) {
        try {
            ps = con.prepareStatement("SELECT * FROM student WHERE phone_number = ? AND student_id <> ?");
            ps.setString(1, phone);
            ps.setInt(2, excludeId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            System.getLogger(Student.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return false;
    }

    //check if student phone number already exist
    public boolean isidExist(int id) {
        try {
            ps = con.prepareStatement("select * from student where student_id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;

            }
        } catch (SQLException ex) {
            System.getLogger(Student.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return false;

    }

    //get all student values from database student table
    public void getStudentValue(JTable table, String searchValue) {
        String sql = "select * from student where concat(first_name,middle_name,last_name,email,phone_number) like ? order by student_id desc";

        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + searchValue + "%");
            ResultSet rs = ps.executeQuery();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            Object[] row;
            while (rs.next()) {
                row = new Object[15];
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
                row[13] = rs.getString(14);
                row[14] = rs.getString(15);
                model.addRow(row);

            }
        } catch (SQLException ex) {
            System.getLogger(Student.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

    }

    public void update(int id, String fname, String midName, String lname, String date, String gender, String email, String phone,
            String motherName, String fatherName, String addressLine1,
            String addressLine2, String birthCer, String form137, String imagePath, long lrn) {

        String sql = "update student set first_name=?,middle_name=?,last_name=?,date_of_birth=?,gender=?,email=?,phone_number=?,mother_name=?,"
                + "father_name=?,address1=?,address2=?,birth_certificate=?,form_137=?,image_path=?,LRN=? where student_id=?";
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, fname);
            ps.setString(2, midName);
            ps.setString(3, lname);
            ps.setString(4, date);
            ps.setString(5, gender);
            ps.setString(6, email);
            ps.setString(7, phone);
            ps.setString(8, motherName);
            ps.setString(9, fatherName);
            ps.setString(10, addressLine1);
            ps.setString(11, addressLine2);
            ps.setString(12, birthCer);
            ps.setString(13, form137);
            ps.setString(14, imagePath);
            ps.setInt(15, id);
            ps.setLong(16, lrn);

            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "Student data updated successfully ");

            }
        } catch (SQLException ex) {
            System.getLogger(Student.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

    }

    //student data delete
    public void delete(int id) {
        int yesOrNo = JOptionPane.showConfirmDialog(null, "Strand and scores record will also be deleted",
                "Student Delete", JOptionPane.OK_CANCEL_OPTION, 0);
        if (yesOrNo == JOptionPane.OK_OPTION) {
            try {
                ps = con.prepareStatement("delete from student where student_id = ?");
                ps.setInt(1, id);
                if (ps.executeUpdate() > 0) {
                    JOptionPane.showMessageDialog(null, "Student data deleted successfully ");

                }
            } catch (SQLException ex) {
                System.getLogger(Student.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }

        }

    }
}
