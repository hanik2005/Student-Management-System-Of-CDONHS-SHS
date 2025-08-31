/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Main;

import db.MyConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author ADMIN
 */
public class User {

    Connection con = MyConnection.getConnection();
    PreparedStatement ps;

    public void insert(int id, int user_id, String password, String type) {
        String sql = "insert into user values(?,?,?,?)";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setInt(2, user_id);
            ps.setString(3, password);
            ps.setString(4, type);

            if (ps.executeUpdate() > 0) {
                JOptionPane.showMessageDialog(null, "Your password of your user account " + password);

            }
        } catch (SQLException ex) {
            System.getLogger(User.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

    }
     public void delete(int id) {
            try {
                ps = con.prepareStatement("delete from user where Id = ?");
                ps.setInt(1, id);
                if (ps.executeUpdate() > 0) {
                    //JOptionPane.showMessageDialog(null, "Student data deleted successfully ");

                }
            } catch (SQLException ex) {
                System.getLogger(Student.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }

        }

    
}
