/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

import db.MyConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author ADMIN
 */
public class User {

    Connection con = MyConnection.getConnection();
    PreparedStatement ps;
    
    public int getMax() {
        int id = 0;
        Statement st;

        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery("select max(user_id) from user");
            while (rs.next()) {
                id = rs.getInt(1);

            }
        } catch (SQLException ex) {
            System.getLogger(Student.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return id + 1;

    }

    public void insert(int user_id, int username, String password, int type_id) {
        String sql = "insert into user values(?,?,?,?)";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, user_id);
            ps.setInt(2, username);
            ps.setString(3, password);
            ps.setInt(4, type_id);

            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "Your password of your user account " + password);

            }
        } catch (SQLException ex) {
            System.getLogger(User.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

    }
     public void delete(int id) {
            try {
                ps = con.prepareStatement("delete from user where user_id = ?");
                ps.setInt(1, id);
                if (ps.executeUpdate() > 0) {
                    //JOptionPane.showMessageDialog(null, "Student data deleted successfully ");

                }
            } catch (SQLException ex) {
                System.getLogger(User.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }

        }

    
}
